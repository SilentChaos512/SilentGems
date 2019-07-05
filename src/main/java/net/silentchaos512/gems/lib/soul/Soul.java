package net.silentchaos512.gems.lib.soul;

import lombok.Getter;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.monster.SlimeEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SpawnEggItem;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.rcon.IServer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.dedicated.DedicatedServer;
import net.minecraft.server.integrated.IntegratedServer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraft.world.WorldSettings;
import net.minecraft.world.dimension.DimensionType;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
import net.minecraftforge.fml.network.NetworkDirection;
import net.minecraftforge.fml.network.NetworkEvent;
import net.minecraftforge.registries.ForgeRegistries;
import net.silentchaos512.gems.SilentGems;
import net.silentchaos512.gems.config.GemsConfig;
import net.silentchaos512.gems.item.SoulGemItem;
import net.silentchaos512.gems.network.Network;
import net.silentchaos512.gems.network.SyncSoulsPacket;
import net.silentchaos512.utils.Color;
import net.silentchaos512.utils.MathUtils;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.*;
import java.util.function.Supplier;

public final class Soul {
    @Getter private final ResourceLocation id;
    @Getter private final SoulElement primaryElement;
    @Getter private final SoulElement secondaryElement;
    @Getter private final int primaryColor;
    @Getter private final int secondaryColor;
    private final float dropRate;
    @Nullable
    @Getter
    private final EntityType<?> entityType;

    private static final Marker MARKER = MarkerManager.getMarker("Souls");
    private static final Map<EntityType<?>, Soul> MAP = new HashMap<>();
    private static final Map<String, Soul> MAP_BY_ID = new HashMap<>();

    private Soul(long seed, EntityType<?> entityType) {
        this.entityType = entityType;
        this.id = Objects.requireNonNull(entityType.getRegistryName());
        SilentGems.LOGGER.debug(MARKER, "Creating soul for {}", this.id);

        Random random = new Random(seed + entityType.getRegistryName().hashCode());
        this.primaryElement = SoulElement.selectRandom(random);
        SoulElement element2 = SoulElement.selectRandom(random, 0.2f);
        this.secondaryElement = element2 != this.primaryElement ? element2 : SoulElement.NONE;
        // Drop rate = average + deviation * randomGaussian
        this.dropRate = (float) (GemsConfig.COMMON.soulGemDropRateAverage.get()
                + GemsConfig.COMMON.soulGemDropRateDeviation.get() * random.nextGaussian());

        SpawnEggItem egg = getSpawnEggForType(entityType);
        if (egg != null) {
            this.primaryColor = getEggPrimaryColor(egg);
            this.secondaryColor = getEggSecondaryColor(egg);
        } else {
            this.primaryColor = random.nextInt(0x1000000);
            this.secondaryColor = random.nextInt(0x1000000);
            SilentGems.LOGGER.debug(MARKER, "No spawn egg for {}, setting colors to {} and {}",
                    this.id, Color.format(this.primaryColor), Color.format(this.secondaryColor));
        }
    }

    //region Network

    private Soul(PacketBuffer buffer) {
        this.id = buffer.readResourceLocation();
        this.primaryElement = SoulElement.read(buffer);
        this.secondaryElement = SoulElement.read(buffer);
        this.primaryColor = buffer.readVarInt();
        this.secondaryColor = buffer.readVarInt();
        this.dropRate = buffer.readFloat();
        this.entityType = ForgeRegistries.ENTITIES.getValue(this.id);
    }

    public static Soul read(PacketBuffer buffer) {
        return new Soul(buffer);
    }

    public void write(PacketBuffer buffer) {
        buffer.writeResourceLocation(id);
        primaryElement.write(buffer);
        secondaryElement.write(buffer);
        buffer.writeVarInt(primaryColor);
        buffer.writeVarInt(secondaryColor);
        buffer.writeFloat(dropRate);
    }

    public static void handleSyncPacket(SyncSoulsPacket packet, Supplier<NetworkEvent.Context> context) {
        MAP.clear();
        MAP_BY_ID.clear();

        packet.getSouls().forEach(soul -> {
            MAP.put(soul.entityType, soul);
            MAP_BY_ID.put(soul.id.toString(), soul);
        });
    }

    //endregion

    @Nullable
    private static SpawnEggItem getSpawnEggForType(EntityType<?> entityType) {
        for (SpawnEggItem egg : SpawnEggItem.getEggs()) {
            if (egg.getType(null) == entityType) {
                return egg;
            }
        }
        return null;
    }

    private static int getEggPrimaryColor(SpawnEggItem egg) {
        return ObfuscationReflectionHelper.getPrivateValue(SpawnEggItem.class, egg, "field_195988_c");
    }

    private static int getEggSecondaryColor(SpawnEggItem egg) {
        return ObfuscationReflectionHelper.getPrivateValue(SpawnEggItem.class, egg, "field_195989_d");
    }

    public float getDropRate(LivingEntity entity) {
        // Separate rate for bosses (default is 100%)
        if (!entity.isNonBoss())
            return GemsConfig.COMMON.soulGemDropRateBoss.get().floatValue();
        // Half rate for slimes
        if (entity instanceof SlimeEntity)
            return this.dropRate / 2;
        return this.dropRate;
    }

    public float getBaseDropRate() {
        return this.dropRate;
    }

    public ItemStack getSoulGem() {
        return SoulGemItem.getStack(this);
    }

    public ITextComponent getEntityName() {
        return new TranslationTextComponent("entity." + this.id.getNamespace() + "." + this.id.getPath());
    }

    @Nullable
    public static Soul from(LivingEntity entity) {
        return MAP.get(entity.getType());
    }

    @Nullable
    public static Soul from(ResourceLocation id) {
        return from(id.toString());
    }

    @Nullable
    public static Soul from(String id) {
        return MAP_BY_ID.get(id);
    }

    public static Collection<Soul> getValues() {
        return MAP.values();
    }

    public static final class Events {
        public static final Events INSTANCE = new Events();

        private Events() {}

        @SuppressWarnings("MethodMayBeStatic")
        @SubscribeEvent
        public void onServerAboutToStart(FMLServerStartingEvent event) {
            MAP.clear();
            long seed = calculateSeed(event);
            World world = event.getServer().getWorld(DimensionType.OVERWORLD);
            for (EntityType<?> entityType : ForgeRegistries.ENTITIES.getValues()) {
                if (canHaveSoulGem(entityType, world)) {
                    Soul soul = new Soul(seed, entityType);
                    MAP.put(entityType, soul);
                    ResourceLocation id = Objects.requireNonNull(entityType.getRegistryName());
                    MAP_BY_ID.put(id.toString(), soul);
                }
            }
        }

        private static boolean canHaveSoulGem(EntityType<?> type, World world) {
            try {
                Entity entity = type.create(world);
                boolean result = entity instanceof MobEntity || entity instanceof PlayerEntity;
                if (entity != null) entity.remove();
                return result;
            } catch (Exception ex) {
                SilentGems.LOGGER.debug(MARKER, "Could not verify type of {}", type.getRegistryName());
                SilentGems.LOGGER.catching(ex);
                return false;
            }
        }

        @SubscribeEvent
        public void onPlayerJoinServer(PlayerEvent.PlayerLoggedInEvent event) {
            // Send soul information to player
            PlayerEntity player = event.getPlayer();
            if (player instanceof ServerPlayerEntity) {
                NetworkManager netManager = ((ServerPlayerEntity) player).connection.netManager;
                Network.channel.sendTo(new SyncSoulsPacket(MAP.values()), netManager, NetworkDirection.PLAY_TO_CLIENT);
            }
        }

        @SuppressWarnings("MethodMayBeStatic")
        @SubscribeEvent
        public void onLivingDrops(LivingDropsEvent event) {
            LivingEntity entity = event.getEntityLiving();
            Soul soul = Soul.from(entity);

            if (soul != null && shouldDropSoulGem(event, entity, soul)) {
                ItemStack soulGem = soul.getSoulGem();
                ItemEntity entityItem = entity.entityDropItem(soulGem);
                event.getDrops().add(entityItem);
            }
        }

        private static boolean shouldDropSoulGem(LivingDropsEvent event, LivingEntity entity, @Nonnull Soul soul) {
            boolean killedByPlayer = event.getSource().getTrueSource() instanceof PlayerEntity;
            float dropRate = soul.getDropRate(entity);
            return killedByPlayer && MathUtils.tryPercentage(dropRate);
        }

        private static long calculateSeed(FMLServerStartingEvent event) {
            // Have to get the seed before the world actually exists...
            MinecraftServer server = event.getServer();

            //noinspection ChainOfInstanceofChecks
            if (server instanceof DedicatedServer) {
                IServer dedicatedServer = (DedicatedServer) server;
                // Not sure how dedicated server actually computes the seed
                // All that matters is we get a consistent result
                return dedicatedServer.getServerProperties().worldSeed.hashCode();
            } else if (server instanceof IntegratedServer) {
                IntegratedServer integratedServer = (IntegratedServer) server;
                WorldSettings settings = ObfuscationReflectionHelper.getPrivateValue(
                        IntegratedServer.class, integratedServer, "field_71350_m"); // worldSettings
                return settings.getSeed();
            }

            return 0;
        }
    }
}
