package net.silentchaos512.gems.lib.soul;

import lombok.Getter;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.monster.EntitySlime;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemSpawnEgg;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.dedicated.DedicatedServer;
import net.minecraft.server.dedicated.PropertyManager;
import net.minecraft.server.integrated.IntegratedServer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.WorldSettings;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import net.minecraftforge.fml.event.server.FMLServerAboutToStartEvent;
import net.minecraftforge.registries.ForgeRegistries;
import net.silentchaos512.gems.SilentGems;
import net.silentchaos512.gems.item.SoulGem;
import net.silentchaos512.utils.Color;
import net.silentchaos512.utils.MathUtils;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.*;

public class Soul {
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

        Random random = new Random(seed + entityType.getEntityClass().getCanonicalName().hashCode());
        this.primaryElement = SoulElement.selectRandom(random);
        SoulElement element2 = SoulElement.selectRandom(random, 0.2f);
        this.secondaryElement = element2 != this.primaryElement ? element2 : SoulElement.NONE;
        this.dropRate = 0.035f + 0.002f * (float) random.nextGaussian();

        ItemSpawnEgg egg = getSpawnEggForType(entityType);
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

    @Nullable
    private static ItemSpawnEgg getSpawnEggForType(EntityType<?> entityType) {
        for (ItemSpawnEgg egg : ItemSpawnEgg.getEggs()) {
            if (egg.getType(null) == entityType) {
                return egg;
            }
        }
        return null;
    }

    private static int getEggPrimaryColor(ItemSpawnEgg egg) {
        return ObfuscationReflectionHelper.getPrivateValue(ItemSpawnEgg.class, egg, "field_195988_c");
    }

    private static int getEggSecondaryColor(ItemSpawnEgg egg) {
        return ObfuscationReflectionHelper.getPrivateValue(ItemSpawnEgg.class, egg, "field_195989_d");
    }

    public float getDropRate(EntityLivingBase entity) {
        // 100% for bosses
        if (!entity.isNonBoss())
            return 1;
        // Half rate for slimes
        if (entity instanceof EntitySlime)
            return this.dropRate / 2;
        return this.dropRate;
    }

    public ItemStack getSoulGem() {
        return SoulGem.getStack(this);
    }

    public ITextComponent getEntityName() {
        return new TextComponentTranslation("entity." + this.id.getNamespace() + "." + this.id.getPath());
    }

    @Nullable
    public static Soul from(EntityLivingBase entity) {
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
        public void onServerAboutToStart(FMLServerAboutToStartEvent event) {
            MAP.clear();
            long seed = calculateSeed(event);
            for (EntityType<?> entityType : ForgeRegistries.ENTITIES.getValues()) {
                if (canHaveSoulGem(entityType)) {
                    Soul soul = new Soul(seed, entityType);
                    MAP.put(entityType, soul);
                    ResourceLocation id = Objects.requireNonNull(entityType.getRegistryName());
                    MAP_BY_ID.put(id.toString(), soul);
                }
            }
        }

        private static boolean canHaveSoulGem(EntityType<?> type) {
            return EntityLivingBase.class.isAssignableFrom(type.getEntityClass())
                    && !EntityArmorStand.class.isAssignableFrom(type.getEntityClass());
        }

        @SuppressWarnings("MethodMayBeStatic")
        @SubscribeEvent
        public void onLivingDrops(LivingDropsEvent event) {
            EntityLivingBase entity = event.getEntityLiving();
            Soul soul = Soul.from(entity);

            if (soul != null && shouldDropSoulGem(event, entity, soul)) {
                ItemStack soulGem = soul.getSoulGem();
                EntityItem entityItem = entity.entityDropItem(soulGem);
                event.getDrops().add(entityItem);
            }
        }

        private static boolean shouldDropSoulGem(LivingDropsEvent event, EntityLivingBase entity, @Nonnull Soul soul) {
            return event.getSource().getTrueSource() instanceof EntityPlayer
                    && MathUtils.tryPercentage(soul.getDropRate(entity));
        }

        private static long calculateSeed(FMLServerAboutToStartEvent event) {
            // Have to get the seed before the world actually exists...
            MinecraftServer server = event.getServer();

            //noinspection ChainOfInstanceofChecks
            if (server instanceof DedicatedServer) {
                DedicatedServer dedicatedServer = (DedicatedServer) server;
                PropertyManager settings = ObfuscationReflectionHelper.getPrivateValue(
                        DedicatedServer.class, dedicatedServer, "field_71340_o"); // settings
                // Not sure how dedicated server actually computes the seed
                // All that matters is we get a consistent result
                return settings.getStringProperty("level-seed", "").hashCode();
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
