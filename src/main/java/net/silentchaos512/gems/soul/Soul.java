package net.silentchaos512.gems.soul;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.monster.SlimeEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SpawnEggItem;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Tuple;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
import net.minecraftforge.fml.network.NetworkEvent;
import net.minecraftforge.registries.ForgeRegistries;
import net.silentchaos512.gems.GemsBase;
import net.silentchaos512.gems.item.SoulGemItem;
import net.silentchaos512.gems.network.SyncSoulsPacket;
import net.silentchaos512.lib.util.NameUtils;
import net.silentchaos512.lib.util.PlayerUtils;
import net.silentchaos512.utils.Color;

import javax.annotation.Nullable;
import java.util.*;
import java.util.function.Supplier;

public final class Soul {
    public static final int MAX_VALUE = 1000;
    public static final int STANDARD_KILL_VALUE = MAX_VALUE / 20;

    private static final Map<EntityType<?>, Soul> MAP = new HashMap<>();
    private static final Map<String, Soul> MAP_BY_ID = new HashMap<>();

    private final ResourceLocation id;
    private final Tuple<SoulElement, SoulElement> elements;
    private final Tuple<Integer, Integer> colors;
    private final EntityType<?> entityType;

    private Soul(EntityType<?> entityType) {
        this.entityType = entityType;
        this.id = NameUtils.from(entityType);
        GemsBase.LOGGER.debug("creating soul for {}", this.id);
        Random random = new Random(this.id.hashCode());

        SoulElement element1 = SoulElement.selectRandom(random);
        SoulElement element2 = SoulElement.selectRandom(random, 0.2f);
        this.elements = new Tuple<>(element1, element2 != element1 ? element2 : SoulElement.NONE);

        SpawnEggItem egg = getSpawnEggForType(entityType);
        if (egg != null) {
            this.colors = new Tuple<>(getEggPrimaryColor(egg), getEggSecondaryColor(egg));
        } else {
            this.colors = new Tuple<>(random.nextInt(0x1000000), random.nextInt(0x1000000));
            GemsBase.LOGGER.debug("No spawn egg for {}, setting colors to {} and {}",
                    this.id,
                    Color.format(this.colors.getA()),
                    Color.format(this.colors.getB()));
        }
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

    //region Getters

    public ResourceLocation getId() {
        return id;
    }

    public SoulElement getPrimaryElement() {
        return elements.getA();
    }

    public SoulElement getSecondaryElement() {
        return elements.getB();
    }

    public int getPrimaryColor() {
        return colors.getA();
    }

    public int getSecondaryColor() {
        return colors.getB();
    }

    @Nullable
    public EntityType<?> getEntityType() {
        return entityType;
    }

    public int getKillValue(LivingEntity entity) {
        if (!entity.canChangeDimensions()) {
            return MAX_VALUE;
        }
        if (entity instanceof SlimeEntity) {
            SlimeEntity slimeEntity = (SlimeEntity) entity;
            int size = MathHelper.clamp(slimeEntity.getSize(), 1, 4);
            return STANDARD_KILL_VALUE / (6 - size);
        }
        return STANDARD_KILL_VALUE;
    }

    public ITextComponent getEntityName() {
        return new TranslationTextComponent("entity." + this.id.getNamespace() + "." + this.id.getPath());
    }

    //endregion

    //region Network

    private Soul(PacketBuffer buffer) {
        this.id = buffer.readResourceLocation();
        this.elements = new Tuple<>(SoulElement.read(buffer), SoulElement.read(buffer));
        this.colors = new Tuple<>(buffer.readVarInt(), buffer.readVarInt());
        this.entityType = ForgeRegistries.ENTITIES.getValue(this.id);
    }

    public static Soul read(PacketBuffer buffer) {
        return new Soul(buffer);
    }

    public void write(PacketBuffer buffer) {
        buffer.writeResourceLocation(id);
        this.elements.getA().write(buffer);
        this.elements.getB().write(buffer);
        buffer.writeVarInt(this.colors.getA());
        buffer.writeVarInt(this.colors.getB());
    }

    public static void handleSyncPacket(SyncSoulsPacket packet, Supplier<NetworkEvent.Context> context) {
        MAP.clear();
        MAP_BY_ID.clear();

        packet.getSouls().forEach(soul -> {
            MAP.put(soul.entityType, soul);
            MAP_BY_ID.put(soul.id.toString(), soul);
        });

        GemsBase.LOGGER.info("Received {} soul info objects from server", MAP.size());
        context.get().setPacketHandled(true);
    }

    //endregion

    @Nullable
    private static SpawnEggItem getSpawnEggForType(EntityType<?> entityType) {
        for (SpawnEggItem egg : SpawnEggItem.eggs()) {
            if (egg.getType(null) == entityType) {
                return egg;
            }
        }
        return null;
    }

    private static int getEggPrimaryColor(SpawnEggItem egg) {
        Integer i = ObfuscationReflectionHelper.getPrivateValue(SpawnEggItem.class, egg, "color1");
        return i != null ? i : 0xFF00FF;
    }

    private static int getEggSecondaryColor(SpawnEggItem egg) {
        Integer i = ObfuscationReflectionHelper.getPrivateValue(SpawnEggItem.class, egg, "color2");
        return i != null ? i : 0x0;
    }

    public static boolean canHaveSoulGem(EntityType<?> type) {
        return type.getCategory() != EntityClassification.MISC;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Soul soul = (Soul) o;
        return id.equals(soul.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Mod.EventBusSubscriber(modid = GemsBase.MOD_ID)
    public static final class Events {
        private Events() {}

        @SubscribeEvent
        public static void onServerAboutToStart(FMLServerStartingEvent event) {
            MAP.clear();
            for (EntityType<?> entityType : ForgeRegistries.ENTITIES.getValues()) {
                if (canHaveSoulGem(entityType)) {
                    Soul soul = new Soul(entityType);
                    MAP.put(entityType, soul);
                    ResourceLocation id = Objects.requireNonNull(entityType.getRegistryName());
                    MAP_BY_ID.put(id.toString(), soul);
                }
            }
        }

        @SubscribeEvent
        public static void onMobKilled(LivingDeathEvent event) {
            Entity killer = event.getSource().getEntity();

            if (killer instanceof PlayerEntity) {
                PlayerEntity player = (PlayerEntity) killer;
                LivingEntity entity = event.getEntityLiving();
                Soul soul = from(entity);

                if (soul != null) {
                    int killValue = soul.getKillValue(entity);

                    // Find a partially filled soul gem
                    ItemStack partialGem = PlayerUtils.getFirstValidStack(player, true, true, false, stack -> {
                        return stack.getItem() instanceof SoulGemItem
                                && soul.equals(SoulGemItem.getSoul(stack))
                                && SoulGemItem.getSoulValue(stack) < MAX_VALUE;
                    });
                    if (!partialGem.isEmpty()) {
                        SoulGemItem.addSoulValue(partialGem, killValue);
                        GemsBase.LOGGER.debug("Fill partial soul gem with {} x{}", soul.id, killValue);
                        return;
                    }

                    // Find an empty soul gem to start filling
                    ItemStack emptyGem = PlayerUtils.getFirstValidStack(player, true, true, false, stack -> {
                        return stack.getItem() instanceof SoulGemItem
                                && SoulGemItem.getSoul(stack) == null;
                    });
                    if (!emptyGem.isEmpty()) {
                        SoulGemItem.setSoul(emptyGem, soul);
                        SoulGemItem.addSoulValue(emptyGem, killValue);
                        GemsBase.LOGGER.debug("Start filling empty soul gem with {} x{}", soul.id, killValue);
                    }
                }
            }
        }
    }
}
