package net.silentchaos512.gems.lib.module;

import java.util.List;
import java.util.Random;
import java.util.Set;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.EnumDifficulty;
import net.silentchaos512.gems.SilentGems;
import net.silentchaos512.gems.item.ModItems;
import net.silentchaos512.gems.item.tool.ItemGemSword;
import net.silentchaos512.gems.lib.EnumGem;
import net.silentchaos512.gems.lib.Names;
import net.silentchaos512.gems.util.ToolHelper;

public class ModuleEntityRandomEquipment {

  public static final String MODULE_NAME = "mob_equipment";

  public static final ItemStack SILENT_KATANA = ModItems.katana.constructTool(
      ModItems.craftingMaterial.toolRodSilver, EnumGem.RUBY.getItemSuper(),
      EnumGem.ONYX.getItemSuper()); // Deliberately using 2 parts because I can :)

  public static boolean enabled = true;
  public static float swordChance = 0.075f;
  public static float katanaChance = 0.5f;
  public static float swordExtraGemChance = 0.33f;
  public static float superChance = 0.25f;
  public static float selectExtraGemChance = 0.6f;

  public static void tryGiveMobEquipment(EntityLivingBase entity) {

    if (entity.worldObj.isRemote || !(entity instanceof EntityMob))
      return;

    EnumDifficulty worldDiff = entity.worldObj.getDifficulty();
    DifficultyInstance localDiff = entity.worldObj.getDifficultyForLocation(entity.getPosition());
    Random rand = SilentGems.random;

    ItemStack sword = null;

    // Allowed mobs: zombies, skeletons, and Headcrumbs humans. Different mobs have different
    // chances of spawning with equipment.
    if (entity instanceof EntityZombie) {
      if (selectBasedOnDifficulty(swordChance, worldDiff, localDiff, rand)) {
        sword = generateRandomMeleeWeapon(entity, rand);
      }
    } else if (entity instanceof EntitySkeleton) {
      if (selectBasedOnDifficulty(0.5f * swordChance, worldDiff, localDiff, rand)) {
        sword = generateRandomMeleeWeapon(entity, rand);
      }
    } else if (EntityList.NAME_TO_CLASS.get("headcrumbs.Human") == entity.getClass()) {
      if (selectBasedOnDifficulty(2.0f * swordChance, worldDiff, localDiff, rand)) {
        // A little easter egg...
        if (entity.getName().equals(Names.SILENT_CHAOS_512)) {
          sword = SILENT_KATANA.copy();
          sword.setStackDisplayName("Silent's Creatively Named Katana");
        } else {
          sword = generateRandomMeleeWeapon(entity, rand);
        }
      }
    }

    ItemStack currentMain = entity.getItemStackFromSlot(EntityEquipmentSlot.MAINHAND);
    if (sword != null && (currentMain == null || !currentMain.hasTagCompound())) {
      String makerName = SilentGems.localizationHelper.getMiscText("Tooltip.OriginalOwner.Mob",
          entity.getName());
      ToolHelper.setOriginalOwner(sword, makerName);
      entity.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, sword);
    }
  }

  public static ItemStack generateRandomMeleeWeapon(EntityLivingBase entity, Random rand) {

    EnumDifficulty worldDiff = entity.worldObj.getDifficulty();
    DifficultyInstance localDiff = entity.worldObj.getDifficultyForLocation(entity.getPosition());

    boolean superTier = selectBasedOnDifficulty(superChance, worldDiff, localDiff, rand);
    boolean genKatana = superTier
        && selectBasedOnDifficulty(katanaChance, worldDiff, localDiff, rand);

    ItemGemSword item;
    int maxGemCount;

    if (genKatana) {
      item = ModItems.katana;
      maxGemCount = 3;
    } else {
      item = ModItems.sword;
      maxGemCount = rand.nextFloat() < swordExtraGemChance ? 3 : 2;
    }

    Set<EnumGem> gemSet = selectRandomGems(maxGemCount, rand);
    List<EnumGem> gemList = expandGemsSet(gemSet, maxGemCount, rand);

    List<ItemStack> mats = Lists.newArrayList();
    for (EnumGem gem : gemList)
      mats.add(superTier ? gem.getItemSuper() : gem.getItem());

    // SilentGems.logHelper.debug(superTier, gemList);
    return item.constructTool(superTier, mats.toArray(new ItemStack[mats.size()]));
  }

  public static void equipEntityWithItem(EntityLivingBase entity, ItemStack stack,
      EntityEquipmentSlot slot) {

    // Null checks.
    if (entity == null || stack == null)
      return;

    // Don't replace items with NBT.
    ItemStack current = entity.getItemStackFromSlot(slot);
    if (current != null && current.hasTagCompound())
      return;

    // Set tool owner name.
    String makerName = SilentGems.localizationHelper.getMiscText("Tooltip.OriginalOwner.Mob",
        entity.getName());
    ItemStack copy = stack.copy();
    ToolHelper.setOriginalOwner(copy, makerName);

    // Set it.
    entity.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, copy);
  }

  public static Set<EnumGem> selectRandomGems(int maxCount, Random rand) {

    Set<EnumGem> gems = Sets.newHashSet();
    for (int i = 0; i < maxCount; ++i) {
      int index = rand.nextInt(EnumGem.values().length);
      gems.add(EnumGem.values()[index]);
      if (rand.nextFloat() > selectExtraGemChance)
        return gems;
    }
    return gems;
  }

  public static List<EnumGem> expandGemsSet(Set<EnumGem> gems, int targetCount, Random rand) {

    List<EnumGem> list = Lists.newArrayList(gems.iterator());
    for (int i = list.size(); i < targetCount; ++i)
      list.add(list.get(rand.nextInt(gems.size())));
    return list;
  }

  public static boolean selectBasedOnDifficulty(float baseChance, EnumDifficulty worldDifficulty,
      DifficultyInstance localDifficulty, Random rand) {

    float f = 1.0f;
    switch (worldDifficulty) {
      //@formatter:off
      case PEACEFUL: return false;
      case EASY: f = 0.5f; break;
      case NORMAL: f = 1.0f; break;
      case HARD: f = 2.0f; break;
      // @formatter:on
    }
    f *= 1.0f + localDifficulty.getClampedAdditionalDifficulty();
    // SilentGems.logHelper.debug("f = " + f, "c = " + baseChance, "fc = " + f * baseChance);
    return rand.nextFloat() < f * baseChance;
  }
}
