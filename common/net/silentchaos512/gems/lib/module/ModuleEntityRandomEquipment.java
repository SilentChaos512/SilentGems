package net.silentchaos512.gems.lib.module;

import java.util.List;
import java.util.Random;
import java.util.Set;

import org.apache.commons.lang3.mutable.MutableDouble;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.EnumDifficulty;
import net.minecraftforge.common.config.Configuration;
import net.silentchaos512.gems.SilentGems;
import net.silentchaos512.gems.config.GemsConfig;
import net.silentchaos512.gems.init.ModItems;
import net.silentchaos512.gems.item.tool.ItemGemSword;
import net.silentchaos512.gems.lib.EnumGem;
import net.silentchaos512.gems.lib.Names;
import net.silentchaos512.gems.util.ToolHelper;
import net.silentchaos512.lib.util.StackHelper;

public class ModuleEntityRandomEquipment {

  public static final String MODULE_NAME = "mob_equipment";

  public static final ItemStack SILENT_KATANA = ModItems.katana.constructTool(
      ModItems.craftingMaterial.toolRodSilver, EnumGem.RUBY.getItemSuper(),
      EnumGem.ONYX.getItemSuper()); // Deliberately using 2 parts because I can :)

  public static boolean MODULE_ENABLED = true;
  public static float SWORD_CHANCE = 0.075f;
  public static float KATANA_CHANCE = 0.5f;
  public static float SUPER_CHANCE = 0.25f;
  public static float SWORD_EXTRA_GEM_CHANCE = 0.33f;
  public static float SELECT_EXTRA_GEM_CHANCE = 0.6f;
  public static float EQUIPMENT_DROP_CHANCE = 0.11f; // Vanilla 8.5%
  public static float SWORD_MULTI_HUMAN = 2.0f;
  public static float SWORD_MULTI_SKELETON = 0.5f;
  public static float SWORD_MULTI_ZOMBIE = 1.0f;

  public static void loadConfig(Configuration c) {

    String cat = GemsConfig.CAT_MAIN + c.CATEGORY_SPLITTER + MODULE_NAME;
    c.setCategoryComment(cat, "Configs for mobs spawning with gem equipment.");

    MODULE_ENABLED = c.getBoolean("Enabled", cat, MODULE_ENABLED,
        "Enables/disables mob gem equipment spawns.");

    SWORD_CHANCE = c.getFloat("SwordChance", cat, SWORD_CHANCE, 0, 1,
        "Base chance of a mob getting a gem sword.");
    KATANA_CHANCE = c.getFloat("KatanaChance", cat, KATANA_CHANCE, 0, 1,
        "Chance that a super-tier sword will be a katana.");
    SUPER_CHANCE = c.getFloat("SuperChance", cat, SUPER_CHANCE, 0, 1,
        "Chance that equipment will be super-tier if given.");
    SWORD_EXTRA_GEM_CHANCE = c.getFloat("SwordExtraGemChance", cat, SWORD_EXTRA_GEM_CHANCE, 0, 1,
        "Chance that a sword (not katanas) will get a third gem. The cheaters!");
    SELECT_EXTRA_GEM_CHANCE = c.getFloat("SelectExtraGemChance", cat, SELECT_EXTRA_GEM_CHANCE, 0, 1,
        "Chance that another gem will be selected after the previous one (for example, after one is\n"
            + "selected this is the chance of getting a second.)");
    EQUIPMENT_DROP_CHANCE = c.getFloat("EquipmentDropChance", cat, EQUIPMENT_DROP_CHANCE, 0, 1,
        "Chance the item will be dropped on death (vanilla is 0.085)");

    SWORD_MULTI_HUMAN = c.getFloat("SwordMulti.Human", cat, SWORD_MULTI_HUMAN, 0, 100,
        "Multiplier for the chance that a Headcrumbs mob will spawn with gem equipment.");
    SWORD_MULTI_SKELETON = c.getFloat("SwordMulti.Skeleton", cat, SWORD_MULTI_SKELETON, 0, 100,
        "Multiplier for the chance that a Skelton will spawn with gem equipment.");
    SWORD_MULTI_ZOMBIE = c.getFloat("SwordMulti.Zombie", cat, SWORD_MULTI_ZOMBIE, 0, 100,
        "Multiplier for the chance that a Zombie will spawn with gem equipment.");
  }

  public static void tryGiveMobEquipment(EntityLivingBase entity) {

    if (!MODULE_ENABLED || entity.world.isRemote || !(entity instanceof EntityMob))
      return;

    EnumDifficulty worldDiff = entity.world.getDifficulty();
    DifficultyInstance localDiff = entity.world.getDifficultyForLocation(entity.getPosition());
    Random rand = SilentGems.random;

    ItemStack sword = null;

    // Allowed mobs: zombies, skeletons, and Headcrumbs humans. Different mobs have different
    // chances of spawning with equipment.
    if (entity instanceof EntityZombie) {
      if (selectBasedOnDifficulty(SWORD_MULTI_ZOMBIE * SWORD_CHANCE, worldDiff, localDiff, rand))
        sword = generateRandomMeleeWeapon(entity, rand);
    } else if (entity instanceof EntitySkeleton) {
      if (selectBasedOnDifficulty(SWORD_MULTI_SKELETON * SWORD_CHANCE, worldDiff, localDiff, rand))
        sword = generateRandomMeleeWeapon(entity, rand);
    } /*else if (EntityList.NAME_TO_CLASS.get("headcrumbs.Human") == entity.getClass()) {
      if (selectBasedOnDifficulty(SWORD_MULTI_HUMAN * SWORD_CHANCE, worldDiff, localDiff, rand)) {
        // A little easter egg...
        if (entity.getName().equals(Names.SILENT_CHAOS_512)) {
          sword = SILENT_KATANA.copy();
          sword.setStackDisplayName("Silent's Creatively Named Katana");
        } else {
          sword = generateRandomMeleeWeapon(entity, rand);
        }
      }
    }*/ // FIXME

    ItemStack currentMain = entity.getItemStackFromSlot(EntityEquipmentSlot.MAINHAND);
    if (StackHelper.isValid(sword) && (StackHelper.isEmpty(currentMain) || !currentMain.hasTagCompound())) {
      String makerName = SilentGems.localizationHelper.getMiscText("Tooltip.OriginalOwner.Mob",
          entity.getName());
      ToolHelper.setOriginalOwner(sword, makerName);
      entity.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, sword);
      if (entity instanceof EntityLiving)
        ((EntityLiving) entity).setDropChance(EntityEquipmentSlot.MAINHAND, EQUIPMENT_DROP_CHANCE);
    }
  }

  public static ItemStack generateRandomMeleeWeapon(EntityLivingBase entity, Random rand) {

    EnumDifficulty worldDiff = entity.world.getDifficulty();
    DifficultyInstance localDiff = entity.world.getDifficultyForLocation(entity.getPosition());

    boolean superTier = selectBasedOnDifficulty(SUPER_CHANCE, worldDiff, localDiff, rand);
    boolean genKatana = superTier && rand.nextFloat() < KATANA_CHANCE;

    ItemGemSword item;
    int maxGemCount;

    if (genKatana) {
      item = ModItems.katana;
      maxGemCount = 3;
    } else {
      item = ModItems.sword;
      maxGemCount = rand.nextFloat() < SWORD_EXTRA_GEM_CHANCE ? 3 : 2;
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
    if (entity == null || StackHelper.isEmpty(stack))
      return;

    // Don't replace items with NBT.
    ItemStack current = entity.getItemStackFromSlot(slot);
    if (StackHelper.isValid(current) && current.hasTagCompound())
      return;

    // Set tool owner name.
    String makerName = SilentGems.localizationHelper.getMiscText("Tooltip.OriginalOwner.Mob",
        entity.getName());
    ItemStack copy = StackHelper.safeCopy(stack);
    ToolHelper.setOriginalOwner(copy, makerName);

    // Set it.
    entity.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, copy);
  }

  public static Set<EnumGem> selectRandomGems(int maxCount, Random rand) {

    Set<EnumGem> gems = Sets.newHashSet();
    for (int i = 0; i < maxCount; ++i) {
      int index = rand.nextInt(EnumGem.values().length);
      gems.add(EnumGem.values()[index]);
      if (rand.nextFloat() > SELECT_EXTRA_GEM_CHANCE)
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
      case HARD: f = 1.75f; break;
      // @formatter:on
    }
    f *= 1.0f + localDifficulty.getClampedAdditionalDifficulty();
    // SilentGems.logHelper.debug("f = " + f, "c = " + baseChance, "fc = " + f * baseChance);
    return rand.nextFloat() < f * baseChance;
  }
}
