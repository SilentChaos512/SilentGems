package net.silentchaos512.gems.item;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.silentchaos512.gems.SilentGems;
import net.silentchaos512.gems.lib.Names;
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

    registerSoul(EntityZombie.class, new Soul("Zombie"));
    registerSoul(EntitySkeleton.class, new Soul("Skeleton"));
    registerSoul(EntityCreeper.class, new Soul("Creeper"));
    registerSoul(EntitySpider.class, new Soul("Spider"));
    registerSoul(EntityCaveSpider.class, new Soul("CaveSpider", "cave_spider"));
    registerSoul(EntitySilverfish.class, new Soul("Silverfish"));
    registerSoul(EntitySlime.class, new Soul("Slime"));
    registerSoul(EntityWitch.class, new Soul("Witch"));
    registerSoul(EntityGuardian.class, new Soul("Guardian"));
    registerSoul(EntityHusk.class, new Soul("Husk"));
    registerSoul(EntityStray.class, new Soul("Stray"));

    registerSoul(EntityEvoker.class, new Soul("EvocationIllager", "evocation_illager"));
    registerSoul(EntityVex.class, new Soul("Vex"));
    registerSoul(EntityIllusionIllager.class, new Soul("IllusionIllager", "illusion_illager"));

    registerSoul(EntityPigZombie.class, new Soul("PigZombie", "zombie_pigman"));
    registerSoul(EntityGhast.class, new Soul("Ghast"));
    registerSoul(EntityBlaze.class, new Soul("Blaze"));
    registerSoul(EntityMagmaCube.class, new Soul("LavaSlime", "magma_cube"));
    registerSoul(EntityWitherSkeleton.class, new Soul("WitherSkeleton", "wither_skeleton"));

    registerSoul(EntityEnderman.class, new Soul("Enderman"));
    registerSoul(EntityEndermite.class, new Soul("Endermite"));
    registerSoul(EntityShulker.class, new Soul("Shulker"));

    registerSoul(EntityPig.class, new Soul("Pig"));
    registerSoul(EntitySheep.class, new Soul("Sheep"));
    registerSoul(EntityCow.class, new Soul("Cow"));
    registerSoul(EntityChicken.class, new Soul("Chicken"));
    registerSoul(EntityRabbit.class, new Soul("Rabbit"));
    registerSoul(EntitySquid.class, new Soul("Squid"));
    registerSoul(EntityBat.class, new Soul("Bat"));
    registerSoul(EntityWolf.class, new Soul("Wolf"));
    registerSoul(EntityOcelot.class, new Soul("Ozelot", "ocelot"));
    registerSoul(EntityParrot.class, new Soul("Parrot"));
    registerSoul(EntityMooshroom.class, new Soul("MushroomCow", "mooshroom"));
    registerSoul(EntityPolarBear.class, new Soul("PolarBear", "polar_bear"));
    registerSoul(EntitySnowman.class, new Soul("SnowMan", 0xFFFFFF, 0xFF8800));
    registerSoul(EntityHorse.class, new Soul("Horse"));
    registerSoul(EntityDonkey.class, new Soul("Donkey"));
    registerSoul(EntityMule.class, new Soul("Mule"));
    registerSoul(EntityLlama.class, new Soul("Llama"));

    registerSoul(EntityIronGolem.class, new Soul("VillagerGolem", 0xEEEEEE, 0xFFAAAA));
    registerSoul(EntityVillager.class, new Soul("Villager"));

    registerSoul(EntityDragon.class, new Soul("EnderDragon", 0x222222, 0xEE88EE).setDropRate(1.0f));
    registerSoul(EntityWither.class, new Soul("WitherBoss", 0x333333, 0xEECCEE).setDropRate(1.0f));
    registerSoul(EntityElderGuardian.class, new Soul("ElderGuardian", "elder_guardian").setDropRate(1.0f));
  }

  public void registerSoul(Class clazz, Soul soul) {

    listSorted.add(soul);
    classToSoul.put(clazz, soul);
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

  public Soul getSoul(ItemStack stack) {

    if (StackHelper.isEmpty(stack) || !stack.hasTagCompound()) {
      return null;
    }
    return idToSoul.get(stack.getTagCompound().getString(NBT_SOUL));
  }

  public Soul getSoul(Class<? extends Entity> clazz) {

    return classToSoul.get(clazz);
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

    if (StackHelper.isValid(stack) && stack.hasTagCompound()) {
      // TODO
    }
  }

  @Override
  public String getItemStackDisplayName(ItemStack stack) {

    LocalizationHelper loc = SilentGems.localizationHelper;
    Soul soul = getSoul(stack);
    if (soul == null) {
      return getUnlocalizedName(stack);
    }
    String name = "entity." + soul.id + ".name";
    name = loc.getLocalizedString(name);
    return loc.getItemSubText(Names.SOUL_GEM, "name_proper", name);
  }

  public static class Soul {

    public final String id;
    public final int colorPrimary, colorSecondary;
    float dropRate = 0.05f;

    public Soul(String id, int colorPrimary, int colorSecondary) {

      this.id = id;
      this.colorPrimary = colorPrimary;
      this.colorSecondary = colorSecondary;
    }

    public Soul(String entityId) {

      this(entityId, entityId);
    }

    public Soul(String entityId, String eggName) {

      this.id = entityId;
      EntityEggInfo info = EntityList.ENTITY_EGGS.get(new ResourceLocation(eggName));
      if (info != null) {
        this.colorPrimary = info.primaryColor;
        this.colorSecondary = info.secondaryColor;
      } else {
        this.colorPrimary = this.colorSecondary = 0xFF00FF;
      }
    }

    public float getDropRate() {

      return dropRate;
    }

    public Soul setDropRate(float value) {

      dropRate = value;
      return this;
    }
  }
}
