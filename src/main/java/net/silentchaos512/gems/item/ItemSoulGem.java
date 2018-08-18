package net.silentchaos512.gems.item;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityList.EntityEggInfo;
import net.minecraft.entity.boss.EntityDragon;
import net.minecraft.entity.boss.EntityWither;
import net.minecraft.entity.monster.*;
import net.minecraft.entity.passive.*;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.event.world.BlockEvent;
import net.silentchaos512.gems.SilentGems;
import net.silentchaos512.gems.entity.EntityEnderSlime;
import net.silentchaos512.gems.init.ModItems;
import net.silentchaos512.gems.lib.soul.EnumSoulElement;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static net.silentchaos512.gems.lib.soul.EnumSoulElement.*;

public class ItemSoulGem extends Item {
    private static final String NBT_SOUL = "sg_soul_gem";

    private List<Soul> listSorted = new ArrayList<>();
    private Map<Class, Soul> classToSoul = new HashMap<>();
    private Map<String, Soul> idToSoul = new HashMap<>();

    public ItemSoulGem() {
        setHasSubtypes(true);

        float blockDropRate = 0.025f;
        registerStackSoul(new Soul("Wheat", new ItemStack(Items.WHEAT),
                0xD5DA45, 0x404A10, FLORA, LIGHTNING).setDropRate(blockDropRate));
        registerStackSoul(new Soul("Potato", new ItemStack(Items.POTATO),
                0xE9BA62, 0x8F6830, FLORA, EARTH).setDropRate(blockDropRate));
        registerStackSoul(new Soul("Carrot", new ItemStack(Items.CARROT),
                0xFF911B, 0x2C8A2A, FLORA, FIRE).setDropRate(blockDropRate));
        registerStackSoul(new Soul("Beetroot", new ItemStack(Items.BEETROOT),
                0x812921, 0xA74D54, FLORA, WATER).setDropRate(blockDropRate));
        registerStackSoul(new Soul("FluffyPuff", new ItemStack(ModItems.fluffyPuff),
                0xFFFFFF, 0x999999, FLORA, WIND).setDropRate(blockDropRate));

        float rateHigh = 0.075f;

        registerEntitySoul(EntityZombie.class, new Soul("Zombie", MONSTER, VENOM));
        registerEntitySoul(EntitySkeleton.class, new Soul("Skeleton", MONSTER, LIGHTNING));
        registerEntitySoul(EntityCreeper.class, new Soul("Creeper", FIRE, FLORA));
        registerEntitySoul(EntitySpider.class, new Soul("Spider", WIND, FAUNA));
        registerEntitySoul(EntityCaveSpider.class, new Soul("CaveSpider", "cave_spider", VENOM, FAUNA));
        registerEntitySoul(EntitySilverfish.class, new Soul("Silverfish", EARTH, FAUNA));
        registerEntitySoul(EntitySlime.class, new Soul("Slime", WATER, EARTH));
        registerEntitySoul(EntityWitch.class, new Soul("Witch", VENOM, ICE));
        registerEntitySoul(EntityGuardian.class, new Soul("Guardian", WATER, LIGHTNING));
        registerEntitySoul(EntityHusk.class, new Soul("Husk", MONSTER, EARTH));
        registerEntitySoul(EntityStray.class, new Soul("Stray", MONSTER, ICE));

        registerEntitySoul(EntityEvoker.class, new Soul("EvocationIllager", "evocation_illager", ICE));
        registerEntitySoul(EntityVex.class, new Soul("Vex", LIGHTNING));
        registerEntitySoul(EntityIllusionIllager.class,
                new Soul("IllusionIllager", "illusion_illager", WATER));

        registerEntitySoul(EntityPigZombie.class, new Soul("PigZombie", "zombie_pigman", EARTH, FIRE));
        registerEntitySoul(EntityGhast.class, new Soul("Ghast", LIGHTNING, FIRE));
        registerEntitySoul(EntityBlaze.class, new Soul("Blaze", FIRE, WIND));
        registerEntitySoul(EntityMagmaCube.class, new Soul("LavaSlime", "magma_cube", FIRE, EARTH));
        registerEntitySoul(EntityWitherSkeleton.class,
                new Soul("WitherSkeleton", "wither_skeleton", EARTH, MONSTER));

        registerEntitySoul(EntityEnderman.class, new Soul("Enderman", LIGHTNING, ALIEN).setDropRate(rateHigh));
        registerEntitySoul(EntityEndermite.class, new Soul("Endermite", EARTH, ALIEN).setDropRate(rateHigh));
        registerEntitySoul(EntityShulker.class, new Soul("Shulker", WATER, ALIEN).setDropRate(2 * rateHigh));
        registerEntitySoul(EntityEnderSlime.class, new Soul("EnderSlime", 0x003333, 0xAA00AA, FIRE, ALIEN)
                .setDropRate(rateHigh));

        registerEntitySoul(EntityPig.class, new Soul("Pig", FAUNA, EARTH));
        registerEntitySoul(EntitySheep.class, new Soul("Sheep", FAUNA, WIND));
        registerEntitySoul(EntityCow.class, new Soul("Cow", FAUNA, WATER));
        registerEntitySoul(EntityChicken.class, new Soul("Chicken", FAUNA, FIRE));
        registerEntitySoul(EntityRabbit.class, new Soul("Rabbit", FAUNA, LIGHTNING));
        registerEntitySoul(EntitySquid.class, new Soul("Squid", FAUNA, WATER));
        registerEntitySoul(EntityBat.class, new Soul("Bat", FAUNA, ICE));
        registerEntitySoul(EntityWolf.class, new Soul("Wolf", FAUNA, WIND));
        registerEntitySoul(EntityOcelot.class, new Soul("Ozelot", "ocelot", FAUNA, FIRE)
                .setDropRate(rateHigh));
        registerEntitySoul(EntityParrot.class, new Soul("Parrot", FAUNA, WIND)
                .setDropRate(2 * rateHigh));
        registerEntitySoul(EntityMooshroom.class, new Soul("MushroomCow", "mooshroom", FAUNA, FLORA));
        registerEntitySoul(EntityPolarBear.class, new Soul("PolarBear", "polar_bear", FAUNA, ICE)
                .setDropRate(rateHigh));
        registerEntitySoul(EntitySnowman.class, new Soul("SnowMan", 0xFFFFFF, 0xFF8800, MONSTER, ICE));
        registerEntitySoul(EntityHorse.class, new Soul("Horse", FAUNA, FIRE).setDropRate(rateHigh));
        registerEntitySoul(EntityDonkey.class, new Soul("Donkey", FAUNA, EARTH).setDropRate(rateHigh));
        registerEntitySoul(EntityMule.class, new Soul("Mule", FAUNA, METAL).setDropRate(rateHigh));
        registerEntitySoul(EntityLlama.class, new Soul("Llama", FAUNA, WATER).setDropRate(rateHigh));

        registerEntitySoul(EntityIronGolem.class,
                new Soul("VillagerGolem", 0xEEEEEE, 0xFFAAAA, METAL, MONSTER).setDropRate(0.6f));
        registerEntitySoul(EntityVillager.class, new Soul("Villager", EARTH).setDropRate(0.1f));

        registerEntitySoul(EntityDragon.class, new Soul("EnderDragon", 0x222222, 0xEE88EE, FIRE, ALIEN)
                .setDropRate(1.0f).setDropCount(2));
        registerEntitySoul(EntityWither.class, new Soul("WitherBoss", 0x333333, 0xEECCEE, LIGHTNING, ALIEN)
                .setDropRate(1.0f).setDropCount(2));
        registerEntitySoul(EntityElderGuardian.class, new Soul("ElderGuardian", "elder_guardian", WATER, ALIEN)
                .setDropRate(1.0f).setDropCount(2));
    }

    public void registerEntitySoul(Class clazz, Soul soul) {
        listSorted.add(soul);
        classToSoul.put(clazz, soul);
        idToSoul.put(soul.id, soul);
    }

    public void registerStackSoul(Soul soul) {
        listSorted.add(soul);
        idToSoul.put(soul.id, soul);
    }

    public ItemStack getStack(Class clazz) {
        Soul soul = classToSoul.get(clazz);
        if (soul == null) {
            return ItemStack.EMPTY;
        }
        return getStack(soul);
    }

    public ItemStack getStack(Soul soul) {
        ItemStack stack = new ItemStack(this);
        NBTTagCompound tags = new NBTTagCompound();
        tags.setString(NBT_SOUL, soul.id);
        stack.setTagCompound(tags);
        return stack;
    }

    public ItemStack getStack(String soulKey) {
        for (Soul soul : listSorted) {
            if (soul.id.equals(soulKey)) {
                return getStack(soul);
            }
        }
        return ItemStack.EMPTY;
    }

    @Nullable
    public Soul getSoul(ItemStack soulGem) {
        if (soulGem.isEmpty() || !soulGem.hasTagCompound()) {
            return null;
        }
        return idToSoul.get(soulGem.getTagCompound().getString(NBT_SOUL));
    }

    public Soul getSoul(Class<? extends Entity> clazz) {
        return classToSoul.get(clazz);
    }

    @Nullable
    public Soul getSoul(BlockEvent.HarvestDropsEvent event) {
        for (Soul soul : listSorted) {
            if (!soul.matchStack.isEmpty()) {
                for (ItemStack stack : event.getDrops()) {
                    if (stack.isItemEqual(soul.matchStack)) {
                        return soul;
                    }
                }
            }
        }

        return null;
    }

    @Override
    public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> list) {
        if (!isInCreativeTab(tab)) return;
        listSorted.forEach(soul -> list.add(getStack(soul)));
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World world, List<String> list, ITooltipFlag flag) {
        Soul soul = getSoul(stack);
        if (soul != null) {
            list.add("Soul ID: " + soul.id);
            list.add("Elements: " + soul.getElementsForDisplay());
        }
    }

    @Override
    public String getItemStackDisplayName(ItemStack stack) {
        Soul soul = getSoul(stack);
        if (soul == null) {
            return SilentGems.i18n.translatedName(this);
        }

        if (!soul.matchStack.isEmpty()) {
            return SilentGems.i18n.subText(this, "name_proper", soul.matchStack.getDisplayName());
        } else {
            String name = "entity." + soul.id + ".name";
            name = SilentGems.i18n.translate(name);
            return SilentGems.i18n.subText(this, "name_proper", name);
        }
    }

    public static class Soul {
        public final SoulType soulType;
        public final String id;
        public final int colorPrimary, colorSecondary;
        public final @Nonnull
        EnumSoulElement element1;
        public final @Nonnull
        EnumSoulElement element2;
        public final ItemStack matchStack;
        float dropRate = 0.05f;
        int dropCount = 1;

        public Soul(String id, int colorPrimary, int colorSecondary, EnumSoulElement... elements) {
            this.id = id;
            this.colorPrimary = colorPrimary;
            this.colorSecondary = colorSecondary;
            this.element1 = elements[0];
            this.element2 = elements.length > 1 ? elements[1] : EnumSoulElement.NONE;
            this.matchStack = ItemStack.EMPTY;
            this.soulType = SoulType.MOB;
        }

        public Soul(String entityId, EnumSoulElement... elements) {
            this(entityId, entityId, elements);
        }

        public Soul(String entityId, String eggName, EnumSoulElement... elements) {
            this.id = entityId;
            EntityEggInfo info = EntityList.ENTITY_EGGS.get(new ResourceLocation(eggName));
            if (info != null) {
                this.colorPrimary = info.primaryColor;
                this.colorSecondary = info.secondaryColor;
            } else {
                this.colorPrimary = this.colorSecondary = 0xFF00FF;
            }
            this.element1 = elements[0];
            this.element2 = elements.length > 1 ? elements[1] : EnumSoulElement.NONE;
            this.matchStack = ItemStack.EMPTY;
            this.soulType = SoulType.MOB;
        }

        /**
         * Soul for block drops.
         */
        public Soul(String id, ItemStack match, int colorPrimary, int colorSecondary, EnumSoulElement... elements) {
            this.id = id;
            this.colorPrimary = colorPrimary;
            this.colorSecondary = colorSecondary;
            this.element1 = elements[0];
            this.element2 = elements.length > 1 ? elements[1] : EnumSoulElement.NONE;
            this.matchStack = match;
            this.soulType = SoulType.CROP;
        }

        public float getDropRate() {
            return dropRate;
        }

        public Soul setDropRate(float value) {
            dropRate = value;
            return this;
        }

        public int getDropCount() {
            return dropCount;
        }

        public Soul setDropCount(int value) {
            dropCount = value;
            return this;
        }

        public String getElementsForDisplay() {
            if (element2 == NONE) {
                return element1.getDisplayName();
            } else {
                return element1.getDisplayName() + ", " + element2.getDisplayName();
            }
        }

        public ItemStack getStack() {
            return ModItems.soulGem.getStack(this);
        }
    }

    public enum SoulType {
        MOB, CROP, BLOCK
    }
}
