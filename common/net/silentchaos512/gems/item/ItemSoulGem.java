package net.silentchaos512.gems.item;

import static net.silentchaos512.gems.lib.soul.EnumSoulElement.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Nonnull;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityList.EntityEggInfo;
import net.minecraft.entity.boss.EntityDragon;
import net.minecraft.entity.boss.EntityWither;
import net.minecraft.entity.monster.EntityBlaze;
import net.minecraft.entity.monster.EntityCaveSpider;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.monster.EntityElderGuardian;
import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.entity.monster.EntityEndermite;
import net.minecraft.entity.monster.EntityEvoker;
import net.minecraft.entity.monster.EntityGhast;
import net.minecraft.entity.monster.EntityGuardian;
import net.minecraft.entity.monster.EntityHusk;
import net.minecraft.entity.monster.EntityIllusionIllager;
import net.minecraft.entity.monster.EntityIronGolem;
import net.minecraft.entity.monster.EntityMagmaCube;
import net.minecraft.entity.monster.EntityPigZombie;
import net.minecraft.entity.monster.EntityPolarBear;
import net.minecraft.entity.monster.EntityShulker;
import net.minecraft.entity.monster.EntitySilverfish;
import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.entity.monster.EntitySlime;
import net.minecraft.entity.monster.EntitySnowman;
import net.minecraft.entity.monster.EntitySpider;
import net.minecraft.entity.monster.EntityStray;
import net.minecraft.entity.monster.EntityVex;
import net.minecraft.entity.monster.EntityWitch;
import net.minecraft.entity.monster.EntityWitherSkeleton;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.passive.EntityBat;
import net.minecraft.entity.passive.EntityChicken;
import net.minecraft.entity.passive.EntityCow;
import net.minecraft.entity.passive.EntityDonkey;
import net.minecraft.entity.passive.EntityHorse;
import net.minecraft.entity.passive.EntityLlama;
import net.minecraft.entity.passive.EntityMooshroom;
import net.minecraft.entity.passive.EntityMule;
import net.minecraft.entity.passive.EntityOcelot;
import net.minecraft.entity.passive.EntityParrot;
import net.minecraft.entity.passive.EntityPig;
import net.minecraft.entity.passive.EntityRabbit;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.entity.passive.EntitySquid;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.passive.EntityWolf;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.event.world.BlockEvent;
import net.silentchaos512.gems.SilentGems;
import net.silentchaos512.gems.entity.EntityEnderSlime;
import net.silentchaos512.gems.init.ModItems;
import net.silentchaos512.gems.lib.Names;
import net.silentchaos512.gems.lib.soul.EnumSoulElement;
import net.silentchaos512.lib.item.ItemSL;
import net.silentchaos512.lib.util.ItemHelper;
import net.silentchaos512.lib.util.LocalizationHelper;
import net.silentchaos512.lib.util.StackHelper;

public class ItemSoulGem extends ItemSL {

  public static final String NBT_SOUL = "sg_soul_gem";

  List<Soul> listSorted = new ArrayList<>();
  Map<Class, Soul> classToSoul = new HashMap<>();
  Map<String, Soul> idToSoul = new HashMap<>();

  public ItemSoulGem() {

    super(1, SilentGems.MODID, Names.SOUL_GEM);

    //@formatter:off
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

    float sentientBlockRate = 0.1f;
//    registerStackSoul(new Soul("SentientStone", ModItems.craftingMaterial.sentientStoneShard,
//        0x747474, 0x8F8F8F, FIRE).setDropRate(sentientBlockRate));
//    registerStackSoul(new Soul("SentientDirt", ModItems.craftingMaterial.sentientDirtShard,
//        0x593D29, 0x966C4A, EARTH).setDropRate(sentientBlockRate));
//    registerStackSoul(new Soul("SentientGravel", ModItems.craftingMaterial.sentientGravelShard,
//        0xAA9E98, 0xABAAA9, WIND).setDropRate(sentientBlockRate));
//    registerStackSoul(new Soul("SentientSand", ModItems.craftingMaterial.sentientSandShard,
//        0xB0AA72, 0xF1E6BA, WATER).setDropRate(sentientBlockRate));

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

    registerEntitySoul(EntityDragon.class,new Soul("EnderDragon", 0x222222, 0xEE88EE, FIRE, ALIEN)
        .setDropRate(1.0f).setDropCount(2));
    registerEntitySoul(EntityWither.class, new Soul("WitherBoss", 0x333333, 0xEECCEE, LIGHTNING, ALIEN)
        .setDropRate(1.0f).setDropCount(2));
    registerEntitySoul(EntityElderGuardian.class, new Soul("ElderGuardian", "elder_guardian", WATER, ALIEN)
        .setDropRate(1.0f).setDropCount(2));
    //@formatter:on
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
      return StackHelper.empty();
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
    return StackHelper.empty();
  }

  public Soul getSoul(ItemStack soulGem) {

    if (StackHelper.isEmpty(soulGem) || !soulGem.hasTagCompound()) {
      return null;
    }
    return idToSoul.get(soulGem.getTagCompound().getString(NBT_SOUL));
  }

  public Soul getSoul(Class<? extends Entity> clazz) {

    return classToSoul.get(clazz);
  }

  public Soul getSoul(BlockEvent.HarvestDropsEvent event) {

    for (Soul soul : listSorted) {
      if (StackHelper.isValid(soul.matchStack)) {
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
  protected void clGetSubItems(Item item, CreativeTabs tab, List<ItemStack> list) {

    if (!ItemHelper.isInCreativeTab(item, tab))
      return;

    for (Soul soul : listSorted) {
      list.add(getStack(soul));
    }
  }

  @Override
  public void clAddInformation(ItemStack stack, World world, List<String> list, boolean advanced) {

    Soul soul = getSoul(stack);
    if (soul != null) {
      list.add("Soul ID: " + soul.id);
      list.add("Elements: " + soul.getElementsForDisplay());
    }
  }

  @Override
  public String getItemStackDisplayName(ItemStack stack) {

    LocalizationHelper loc = SilentGems.localizationHelper;
    Soul soul = getSoul(stack);
    if (soul == null) {
      return getUnlocalizedName(stack);
    }

    if (StackHelper.isValid(soul.matchStack)) {
      return loc.getItemSubText(Names.SOUL_GEM, "name_proper", soul.matchStack.getDisplayName());
    } else {
      String name = "entity." + soul.id + ".name";
      name = loc.getLocalizedString(name);
      return loc.getItemSubText(Names.SOUL_GEM, "name_proper", name);
    }
  }

  public static class Soul {

    public final String id;
    public final int colorPrimary, colorSecondary;
    public final @Nonnull EnumSoulElement element1;
    public final @Nonnull EnumSoulElement element2;
    public final ItemStack matchStack;
    float dropRate = 0.05f;
    int dropCount = 1;

    public Soul(String id, int colorPrimary, int colorSecondary, EnumSoulElement... elements) {

      this.id = id;
      this.colorPrimary = colorPrimary;
      this.colorSecondary = colorSecondary;
      this.element1 = elements[0];
      this.element2 = elements.length > 1 ? elements[1] : EnumSoulElement.NONE;
      this.matchStack = StackHelper.empty();
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
      this.matchStack = StackHelper.empty();
    }

    /**
     * Soul for block drops.
     */
    public Soul(String id, ItemStack match, int colorPrimary, int colorSecondary,
        EnumSoulElement... elements) {

      this.id = id;
      this.colorPrimary = colorPrimary;
      this.colorSecondary = colorSecondary;
      this.element1 = elements[0];
      this.element2 = elements.length > 1 ? elements[1] : EnumSoulElement.NONE;
      this.matchStack = match;
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
}
