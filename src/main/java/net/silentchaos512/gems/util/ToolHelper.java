package net.silentchaos512.gems.util;

import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;
import com.google.common.collect.Sets;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.Item.ToolMaterial;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemTool;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.common.util.EnumHelper;
import net.silentchaos512.gems.SilentGems;
import net.silentchaos512.gems.api.IArmor;
import net.silentchaos512.gems.api.IBlockPlacer;
import net.silentchaos512.gems.api.ITool;
import net.silentchaos512.gems.api.lib.EnumDecoPos;
import net.silentchaos512.gems.api.lib.EnumMaterialGrade;
import net.silentchaos512.gems.api.lib.EnumMaterialTier;
import net.silentchaos512.gems.api.lib.ToolPartPosition;
import net.silentchaos512.gems.api.tool.ToolStats;
import net.silentchaos512.gems.api.tool.part.*;
import net.silentchaos512.gems.config.ConfigOptionToolClass;
import net.silentchaos512.gems.config.GemsConfig;
import net.silentchaos512.gems.config.GemsConfigHC;
import net.silentchaos512.gems.guide.GuideBookGems;
import net.silentchaos512.gems.init.ModItems;
import net.silentchaos512.gems.item.ItemGemArrow;
import net.silentchaos512.gems.item.ToolRenderHelper;
import net.silentchaos512.gems.item.armor.ItemGemArmor;
import net.silentchaos512.gems.item.tool.*;
import net.silentchaos512.gems.lib.Greetings;
import net.silentchaos512.gems.lib.Names;
import net.silentchaos512.gems.lib.soul.ToolSoul;
import net.silentchaos512.gems.network.NetworkHandler;
import net.silentchaos512.gems.network.message.MessageItemRename;
import net.silentchaos512.gems.skills.SkillAreaMiner;
import net.silentchaos512.gems.skills.SkillAreaTill;
import net.silentchaos512.gems.skills.SkillLumberjack;
import net.silentchaos512.gems.skills.ToolSkill;
import net.silentchaos512.lib.util.ItemHelper;
import net.silentchaos512.lib.util.PlayerHelper;
import net.silentchaos512.lib.util.StackHelper;

import javax.annotation.Nullable;
import java.util.*;

public class ToolHelper {

  /**
   * A fake material for tools. Tools need a tool material, even if it's not used. Unfortunately, some mods still
   * reference the tool material (such as Progressive Automation, which is why I chose harvest level 1).
   */
  public static final ToolMaterial FAKE_MATERIAL = EnumHelper
      .addToolMaterial("silentgems:fake_material", 1, 512, 5.12f, 5.12f, 32);

  public static final int CHECK_NAME_FREQUENCY = 10;

  /*
   * NBT keys
   */

  // Root keys
  public static final String NBT_ROOT_CONSTRUCTION = "SGConstruction";
  public static final String NBT_ROOT_DECORATION = "SGDecoration";
  public static final String NBT_ROOT_PROPERTIES = "SGProperties";
  public static final String NBT_ROOT_TOOL_SOUL = "SGToolSoul";
  public static final String NBT_ROOT_STATISTICS = "SGStatistics";

  // UUID key
  public static final String NBT_UUID = "SG_UUID";
  public static final String NBT_SOUL_UUID = "SG_SoulUUID";

  // Settings
  public static final String NBT_SETTINGS_SPECIAL = "SpecialEnabled";

  // Special
  public static final String NBT_LOCK_STATS = "SG_LockStats";

  // Saves tool tier to save processing power.
  public static final String NBT_TOOL_TIER = "ToolTier";
  // Used for client-side name generation, stored temporarily then removed.
  public static final String NBT_TEMP_PARTLIST = "PartListForName";

  // Stats
  public static final String NBT_PROP_DURABILITY = "Durability";
  public static final String NBT_PROP_HARVEST_LEVEL = "HarvestLevel";
  public static final String NBT_PROP_HARVEST_SPEED = "HarvestSpeed";
  public static final String NBT_PROP_MELEE_DAMAGE = "MeleeDamage";
  public static final String NBT_PROP_MAGIC_DAMAGE = "MagicDamage";
  public static final String NBT_PROP_ENCHANTABILITY = "Enchantability";
  public static final String NBT_PROP_MELEE_SPEED = "MeleeSpeed";
  public static final String NBT_PROP_CHARGE_SPEED = "ChargeSpeed";
  public static final String NBT_PROP_PROTECTION = "Protection";

  // NBT for statistics
  public static final String NBT_STATS_ORIGINAL_OWNER = "OriginalOwner";
  public static final String NBT_STATS_REDECORATED = "Redecorated";
  public static final String NBT_STATS_BLOCKS_MINED = "BlocksMined";
  public static final String NBT_STATS_BLOCKS_PLACED = "BlocksPlaced";
  public static final String NBT_STATS_BLOCKS_TILLED = "BlocksTilled";
  public static final String NBT_STATS_HITS = "HitsLanded";
  public static final String NBT_STATS_KILL_COUNT = "KillCount";
  public static final String NBT_STATS_PATHS_MADE = "PathsMade";
  public static final String NBT_STATS_SHOTS_FIRED = "ShotsFired";
  public static final String NBT_STATS_SHOTS_LANDED = "ShotsLanded";
  public static final String NBT_STATS_THROWN = "ThrownCount";

  // NBT example keys
  /**
   * Indicates a tool being displayed in JEI/creative/etc. These need a UUID, but a new UUID should be generated if a
   * copy of the tool makes its way into the player's inventory.
   */
  public static final String NBT_EXAMPLE_TOOL = "ExampleToolItem";
  /**
   * Indicates a tool being displayed in an example recipe. This adds some more descriptive lines to the tooltip,
   * instead of the generic "You broke it!" line.
   */
  public static final String NBT_EXAMPLE_TOOL_TIER = "ExampleToolTier";

  public static void init() {

  }

  /**
   * Recalculate all stats and properties, including the rendering cache for the given tool. In general, this should be
   * called any time changes are made to a tool (aside from incrementing statistics, or something like that). For
   * example, this is called during construction, decoration, and for all tools in the players inventory during login.
   *
   * @param toolOrArmor
   */
  public static void recalculateStats(ItemStack toolOrArmor) {

    // Make sure the item has a UUID!
    getUUID(toolOrArmor);

    ToolPart[] parts = getConstructionParts(toolOrArmor);
    if (parts.length == 0)
      return;

    // Clear old render cache
    clearOldRenderCache(toolOrArmor);

    if (!toolOrArmor.getTagCompound().getBoolean(NBT_LOCK_STATS)) {
      ToolStats stats = getStats(toolOrArmor, true);

      String root = NBT_ROOT_PROPERTIES;
      // Tools only
      if (toolOrArmor.getItem() instanceof ITool) {
        setTagFloat(toolOrArmor, root, NBT_PROP_HARVEST_SPEED, stats.harvestSpeed);
        setTagFloat(toolOrArmor, root, NBT_PROP_MELEE_DAMAGE, stats.meleeDamage);
        setTagFloat(toolOrArmor, root, NBT_PROP_MAGIC_DAMAGE, stats.magicDamage);
        setTagFloat(toolOrArmor, root, NBT_PROP_MELEE_SPEED, stats.meleeSpeed);
        setTagFloat(toolOrArmor, root, NBT_PROP_CHARGE_SPEED, stats.chargeSpeed);
        setTagInt(toolOrArmor, root, NBT_PROP_HARVEST_LEVEL, stats.harvestLevel);
      }
      // Tools and armor
      setTagInt(toolOrArmor, root, NBT_PROP_DURABILITY, (int) stats.durability);
      setTagFloat(toolOrArmor, root, NBT_PROP_PROTECTION, stats.protection);
      setTagInt(toolOrArmor, root, NBT_PROP_ENCHANTABILITY, (int) stats.enchantability);
      setTagInt(toolOrArmor, root, NBT_TOOL_TIER, parts[0].getTier().ordinal());
    }
  }

  public static ToolStats getStats(ItemStack toolOrArmor, boolean applySoulModifiers) {

    ToolPart[] parts = getConstructionParts(toolOrArmor);
    EnumMaterialGrade[] grades = getConstructionGrades(toolOrArmor);
    if (parts.length == 0)
      return new ToolStats(toolOrArmor);

    ToolStats stats = new ToolStats(toolOrArmor, parts, grades);

    if (toolOrArmor.getTagCompound().getBoolean(NBT_LOCK_STATS)) {
      // TODO: Is this right?
      stats.chargeSpeed = getChargeSpeed(toolOrArmor);
      stats.durability = getMaxDamage(toolOrArmor);
      stats.enchantability = getItemEnchantability(toolOrArmor);
      stats.harvestLevel = getHarvestLevel(toolOrArmor);
      stats.harvestSpeed = getDigSpeedOnProperMaterial(toolOrArmor);
      stats.magicDamage = getMagicDamage(toolOrArmor);
      stats.meleeDamage = getMeleeDamage(toolOrArmor);
      stats.meleeSpeed = getMeleeSpeed(toolOrArmor);
      stats.protection = getProtection(toolOrArmor);
      return stats;
    }

    stats.calculate();

    if (applySoulModifiers) {
      ToolSoul soul = SoulManager.getSoul(toolOrArmor);
      if (soul != null) {
        soul.applyToStats(stats);
      }
    }

    return stats;
  }

  @SuppressWarnings("deprecation")
  public static void clearOldRenderCache(ItemStack tool) {

    tool.getTagCompound().removeTag(ToolRenderHelper.NBT_MODEL_INDEX);
  }

  /**
   * Gets the tool's or armor's UUID. If it currently does not have a UUID, one will be created if appropriate.
   *
   * @param tool
   *          The tool or armor stack. The stack's item must implement either ITool or IArmor.
   * @return The UUID of the tool or armor, or null if the item is not allowed to have one.
   */
  public static @Nullable UUID getUUID(ItemStack tool) {

    if (!(tool.getItem() instanceof ITool || tool.getItem() instanceof IArmor)) {
      return null;
    }

    initRootTag(tool);

    if (!tool.getTagCompound().hasUniqueId(NBT_UUID)) {
      UUID uuid = UUID.randomUUID();
      tool.getTagCompound().setUniqueId(NBT_UUID, uuid);
      return uuid;
    }
    return tool.getTagCompound().getUniqueId(NBT_UUID);
  }

  /**
   * Determines if the tool or armor has a UUID, without actually creating one.
   */
  public static boolean hasUUID(ItemStack tool) {

    return !tool.isEmpty() && tool.hasTagCompound()
        && tool.getTagCompound().hasUniqueId(NBT_UUID);
  }

  /**
   * Determine if tool is an "example" (JEI, creative tabs, etc.)
   *
   * @return True if the tool has the example tool tag, false otherwise
   */
  public static boolean isExampleItem(ItemStack tool) {

    return !tool.isEmpty() && tool.hasTagCompound()
        && tool.getTagCompound().getBoolean(NBT_EXAMPLE_TOOL);
  }

  /**
   * Determine if tool is the output of an "example recipe" (the output of the recipes that show in JEI).
   *
   * @return True if the tool has the example tool tier tag, false otherwise.
   */
  public static boolean isExampleRecipeOutput(ItemStack tool) {

    return !tool.isEmpty() && tool.hasTagCompound()
        && tool.getTagCompound().getBoolean(NBT_EXAMPLE_TOOL_TIER);
  }

  // ==========================================================================
  // Mining, using, repairing, etc
  // ==========================================================================

  public static boolean getIsRepairable(ItemStack toolOrArmor, ItemStack material) {

    EnumMaterialTier tierTool = getToolTier(toolOrArmor);
    EnumMaterialTier tierMat = EnumMaterialTier.fromStack(material);

    if (tierTool == null || tierMat == null)
      return false;
    return tierTool.ordinal() <= tierMat.ordinal();
  }

  public static void attemptDamageTool(ItemStack tool, int amount, EntityLivingBase entityLiving) {

    if (entityLiving instanceof EntityPlayer
        && ((EntityPlayer) entityLiving).capabilities.isCreativeMode) {
      return;
    }

    if (!GemsConfigHC.TOOLS_BREAK) {
      amount = Math.min(tool.getMaxDamage() - tool.getItemDamage(), amount);
    }
    boolean wouldBreak = ItemHelper.attemptDamageItem(tool, amount, SilentGems.random);

    if (isBroken(tool)) {
      // Player broke the tool. Update the head model.
      recalculateStats(tool);
      ModItems.toolRenderHelper.updateModelCache(tool);
    } else if (GemsConfigHC.TOOLS_BREAK && wouldBreak) {
      // Return the tool soul, even though the tool is destroyed.
      ToolSoul soul = SoulManager.getSoul(tool);
      if (soul != null) {
        ItemStack toGive = new ItemStack(ModItems.toolSoul);
        ModItems.toolSoul.setSoul(toGive, soul);
        if (soul.hasName()) {
          // Soul already has a name.
          toGive.setStackDisplayName(soul.getName(ItemStack.EMPTY));
        }
        if (entityLiving instanceof EntityPlayer)
          PlayerHelper.giveItem((EntityPlayer) entityLiving, toGive);
      }

      entityLiving.renderBrokenItemStack(tool);
      tool.shrink(1);
    }
  }

  public static float getDigSpeed(ItemStack tool, IBlockState state, Material[] extraMaterials) {

    if (isBroken(tool)) {
      return 0.25f;
    }

    float speed = getDigSpeedOnProperMaterial(tool);

    // Tool effective on block?
    if (tool.getItem().canHarvestBlock(state, tool)) {
      return speed;
    }

    for (String type : tool.getItem().getToolClasses(tool)) {
      try {
        if (state.getBlock().isToolEffective(type, state)) {
          return speed;
        }
      } catch (IllegalArgumentException ex) {
        return 1f;
      }
    }

    if (extraMaterials != null) {
      for (Material material : extraMaterials) {
        if (state.getMaterial() == material) {
          return speed;
        }
      }
    }

    // Tool ineffective.
    return 1f;
  }

  public static float getDigSpeedOnProperMaterial(ItemStack tool) {

    return getTagFloat(tool, NBT_ROOT_PROPERTIES, NBT_PROP_HARVEST_SPEED);
  }

  public static float getMeleeSpeed(ItemStack tool) {

    return getTagFloat(tool, NBT_ROOT_PROPERTIES, NBT_PROP_MELEE_SPEED);
  }

  public static float getMeleeDamage(ItemStack tool) {

    return getTagFloat(tool, NBT_ROOT_PROPERTIES, NBT_PROP_MELEE_DAMAGE);
  }

  public static float getMagicDamage(ItemStack tool) {

    return getTagFloat(tool, NBT_ROOT_PROPERTIES, NBT_PROP_MAGIC_DAMAGE);
  }

  public static float getProtection(ItemStack shieldOrArmor) {

    if (isBroken(shieldOrArmor))
      return 0;

    return getTagFloat(shieldOrArmor, NBT_ROOT_PROPERTIES, NBT_PROP_PROTECTION);
  }

  public static float getMagicProtection(ItemStack shieldOrArmor) {

    float magic = getMagicDamage(shieldOrArmor);
    float protection = getProtection(shieldOrArmor);
    // Mostly impacted by magic, a little bit by protection.
    return magic / 16f + protection / 300f;
  }

  public static int getHarvestLevel(ItemStack tool) {

    return getTagInt(tool, NBT_ROOT_PROPERTIES, NBT_PROP_HARVEST_LEVEL);
  }

  public static int getMaxDamage(ItemStack tool) {

    return getTagInt(tool, NBT_ROOT_PROPERTIES, NBT_PROP_DURABILITY);
  }

  public static float getChargeSpeed(ItemStack tool) {

    return getTagFloat(tool, NBT_ROOT_PROPERTIES, NBT_PROP_CHARGE_SPEED);
  }

  public static boolean isBroken(ItemStack tool) {

    if (tool.getItem() instanceof ItemGemArrow) {
      // Quick hack for arrow coloring.
      return true;
    }

    if (GemsConfigHC.TOOLS_BREAK) {
      return false;
    }

    int maxDamage = tool.getMaxDamage();
    if (tool.isEmpty() || maxDamage <= 0) {
      return false;
    }
    return tool.getItemDamage() >= maxDamage;
  }

  /**
   * Check if the tool has no construction parts. Only checks the head, but no valid tools will have no head parts.
   *
   * @return true if the has no construction parts.
   */
  public static boolean hasNoConstruction(ItemStack tool) {

    String key = getPartId(tool, ToolPartPosition.HEAD.getKey(0));
    return key == null || key.isEmpty();
  }

  public static int getItemEnchantability(ItemStack tool) {

    return getTagInt(tool, NBT_ROOT_PROPERTIES, NBT_PROP_ENCHANTABILITY);
  }

  public static Multimap<String, AttributeModifier> getAttributeModifiers(EntityEquipmentSlot slot,
      ItemStack tool) {

    String name;
    if (tool.getItem() instanceof ItemTool)
      name = "Tool modifier";
    else
      name = "Weapon modifier";

    @SuppressWarnings("deprecation")
    Multimap<String, AttributeModifier> map = tool.getItem().getItemAttributeModifiers(slot);

    if (slot == EntityEquipmentSlot.MAINHAND) {

      // Melee Damage
      String key = SharedMonsterAttributes.ATTACK_DAMAGE.getName();
      float value = getMeleeDamageModifier(tool);
      replaceAttributeModifierInMap(map, key, value);

      // Melee Speed
      key = SharedMonsterAttributes.ATTACK_SPEED.getName();
      value = getMeleeSpeedModifier(tool);
      replaceAttributeModifierInMap(map, key, value);
    }

    return map;
  }

  private static void replaceAttributeModifierInMap(Multimap<String, AttributeModifier> map,
      String key, float value) {

    if (map.containsKey(key)) {
      Iterator<AttributeModifier> iter = map.get(key).iterator();
      if (iter.hasNext()) {
        AttributeModifier mod = iter.next();
        map.removeAll(key);
        map.put(key, new AttributeModifier(mod.getID(), mod.getName(), value, mod.getOperation()));
      }
    }
  }

  public static float getMeleeDamageModifier(ItemStack tool) {

    if (isBroken(tool))
      return 1f;

    float val;
    if (tool.getItem() instanceof ITool)
      val = ((ITool) tool.getItem()).getMeleeDamage(tool);
    else
      val = getMeleeDamage(tool);

    return val < 0 ? 0 : val;
  }

  public static float getMagicDamageModifier(ItemStack tool) {

    if (isBroken(tool))
      return 0f;

    float val;
    if (tool.getItem() instanceof ITool)
      val = ((ITool) tool.getItem()).getMagicDamage(tool);
    else
      val = getMagicDamage(tool);

    return val < 0 ? 0 : val;
  }

  public static float getMeleeSpeedModifier(ItemStack tool) {

    if (!(tool.getItem() instanceof ITool))
      return 0.0f;

    float base = ((ITool) tool.getItem()).getMeleeSpeedModifier();
    float speed = getMeleeSpeed(tool) + (isBroken(tool) ? 0.7f : 0f);
    return ((base + 4) * speed - 4);
  }

  /**
   * This controls the block placing ability of mining tools.
   */
  public static EnumActionResult onItemUse(EntityPlayer player, World world, BlockPos pos,
      EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {

    ItemStack stack = player.getHeldItem(hand);

    // If block is in offhand, allow that to place instead.
    ItemStack stackOffHand = player.getHeldItemOffhand();
    if (!stackOffHand.isEmpty() && stackOffHand.getItem() instanceof ItemBlock) {
      ItemBlock itemBlock = (ItemBlock) stackOffHand.getItem();
      BlockPos target = pos;

      if (!itemBlock.getBlock().isReplaceable(world, pos))
        target = pos.offset(side);

      if (player.canPlayerEdit(target, side, stackOffHand) && world.mayPlace(itemBlock.getBlock(), target, false, side, player))
        return EnumActionResult.PASS;
    }

    // Behavior configs.
    if (!GemsConfig.RIGHT_CLICK_TO_PLACE_ENABLED)
      return EnumActionResult.PASS;
    if (GemsConfig.RIGHT_CLICK_TO_PLACE_ON_SNEAK_ONLY && !player.isSneaking())
      return EnumActionResult.PASS;

    // Disallow for broken tools
    if (isBroken(stack))
      return EnumActionResult.PASS;

    EnumActionResult result = EnumActionResult.PASS;
    int toolSlot = player.inventory.currentItem;
    int itemSlot = toolSlot + 1;
    ItemStack nextStack = ItemStack.EMPTY;
    ItemStack lastStack = player.inventory.getStackInSlot(8); // Slot 9 in hotbar

    if (toolSlot < 8) {
      // Get stack in slot after tool.
      nextStack = player.inventory.getStackInSlot(itemSlot);
      boolean emptyOrNoPlacingTag = nextStack.isEmpty()
          || (nextStack.hasTagCompound() && nextStack.getTagCompound().hasKey("NoPlacing"));

      // If there's nothing there we can use, try slot 9 instead.
      if (emptyOrNoPlacingTag || (!(nextStack.getItem() instanceof ItemBlock)
          && !(nextStack.getItem() instanceof IBlockPlacer))) {
        nextStack = lastStack;
        itemSlot = 8;
      }

      emptyOrNoPlacingTag = nextStack.isEmpty()
          || (nextStack.hasTagCompound() && nextStack.getTagCompound().hasKey("NoPlacing"));

      if (!emptyOrNoPlacingTag) {
        Item item = nextStack.getItem();
        if (item instanceof ItemBlock || item instanceof IBlockPlacer) {
          BlockPos targetPos = pos.offset(side);
          int playerX = (int) Math.floor(player.posX);
          int playerY = (int) Math.floor(player.posY);
          int playerZ = (int) Math.floor(player.posZ);

          // Check for block overlap with player, if necessary.
          if (item instanceof ItemBlock) {
            int px = targetPos.getX();
            int py = targetPos.getY();
            int pz = targetPos.getZ();
            AxisAlignedBB blockBounds = new AxisAlignedBB(px, py, pz, px + 1, py + 1, pz + 1);
            AxisAlignedBB playerBounds = player.getEntityBoundingBox();
            ItemBlock itemBlock = (ItemBlock) item;
            Block block = itemBlock.getBlock();
            @SuppressWarnings("deprecation") IBlockState state = block.getStateFromMeta(itemBlock.getMetadata(nextStack));
            if (state.getMaterial().blocksMovement() && playerBounds.intersects(blockBounds)) {
              return EnumActionResult.FAIL;
            }
          }

          int prevSize = nextStack.getCount();
          result = ItemHelper.useItemAsPlayer(nextStack, player, world, pos, side, hitX, hitY,
              hitZ);

          // Don't consume in creative mode?
          if (player.capabilities.isCreativeMode) {
            nextStack.setCount(prevSize);
          }

          // Remove empty stacks.
          if (nextStack.isEmpty()) {
            nextStack = ItemStack.EMPTY;
            player.inventory.setInventorySlotContents(itemSlot, ItemStack.EMPTY);
          }
        }
      }
    }

    if (result == EnumActionResult.SUCCESS)
      incrementStatBlocksPlaced(stack, 1);

    return result;
  }

  @Nullable
  public static ToolSkill getSuperSkill(ItemStack tool) {

    if (getToolTier(tool).ordinal() < EnumMaterialTier.SUPER.ordinal())
      return null;

    Item item = tool.getItem();
    if (item instanceof ItemGemPickaxe || item instanceof ItemGemShovel) {
      return SkillAreaMiner.INSTANCE;
    } else if (item instanceof ItemGemAxe) {
      return GemsConfig.SWITCH_AXE_SUPER ? SkillAreaMiner.INSTANCE : SkillLumberjack.INSTANCE;
    } else if (item instanceof ItemGemHoe) {
      return SkillAreaTill.INSTANCE;
    }

    return null;
  }

  /**
   * Called by mining tools if block breaking isn't canceled.
   *
   * @return False in all cases, because this method is only called when Item.onBlockStartBreak returns false.
   */
  public static boolean onBlockStartBreak(ItemStack stack, BlockPos pos, EntityPlayer player) {

    boolean abilityActivated = false;

    ToolSkill skill = getSuperSkill(stack);
    if (skill != null)
      skill.activate(stack, player, pos);
    incrementStatBlocksMined(stack, 1);

    // XP for tool soul
    if (player != null && !player.world.isRemote) {
      IBlockState stateMined = player.world.getBlockState(pos);
      ToolSoul soul = SoulManager.getSoul(stack);
      if (soul != null) {
        int xpForBlockHarvest = soul.getXpForBlockHarvest(player.world, pos, stateMined);
        SoulManager.addSoulXp(xpForBlockHarvest, stack, player);
      }
    }

    // Mining achievements TODO: Uncomment
    // amount = getStatBlocksMined(stack);
    // if (amount >= 1000) {
    // player.addStat(GemsAchievement.mined1K, 1);
    // }
    // if (amount >= 10000) {
    // player.addStat(GemsAchievement.mined10K, 1);
    // }
    // if (amount >= 100000) {
    // player.addStat(GemsAchievement.mined100K, 1);
    // }

    return false;
  }

  public static boolean onBlockDestroyed(ItemStack tool, World world, IBlockState state,
      BlockPos pos, EntityLivingBase entityLiving) {

    boolean isSickle = tool.getItem() == ModItems.sickle;
    if ((isSickle || state.getBlockHardness(world, pos) != 0) && !isBroken(tool)) {
      if (state.getMaterial() != Material.LEAVES) {
        attemptDamageTool(tool, isSickle ? ItemGemSickle.DURABILITY_USAGE : 1, entityLiving);

        if (isBroken(tool))
          entityLiving.renderBrokenItemStack(tool);
      }
    }
    return true;
  }

  public static boolean hitEntity(ItemStack tool, EntityLivingBase target,
      EntityLivingBase attacker) {

    ToolHelper.incrementStatHitsLanded(tool, 1);

    boolean isSword = tool.getItem() instanceof ItemGemSword;
    boolean isShield = tool.getItem() instanceof ItemGemShield;
    boolean isTool = tool.getItem() instanceof ItemTool || tool.getItem() instanceof ItemGemHoe;
    boolean isBroken = isBroken(tool);

    if (!isBroken) {
      int currentDmg = tool.getItemDamage();
      int maxDmg = tool.getMaxDamage();
      attemptDamageTool(tool, isTool ? 2 : (isSword || isShield ? 1 : 0), attacker);

      if (isBroken(tool)) {
        attacker.renderBrokenItemStack(tool);
      }
    }

    return !isBroken && isTool;
  }

  public static void onUpdate(ItemStack toolOrArmor, World world, Entity entity, int itemSlot,
      boolean isSelected) {

    if (!world.isRemote) {
      // Randomize tools with no data.
      if (!toolOrArmor.hasTagCompound()) {
        ToolRandomizer.INSTANCE.randomize(toolOrArmor);
      }

      // If the player gave him/herself a tool via JEI or creative, remove the example tag.
      if (isExampleItem(toolOrArmor)) {
        // tool.getTagCompound().setBoolean(NBT_EXAMPLE_TOOL, false);
        toolOrArmor.getTagCompound().removeTag(NBT_EXAMPLE_TOOL);
      }

      initRootTag(toolOrArmor);

      // Generate UUID if tool does not have one.
      if (!hasUUID(toolOrArmor)) {
        toolOrArmor.getTagCompound().setUniqueId(NBT_UUID, UUID.randomUUID());
      }
    }

    // Tick tool souls
    if (entity instanceof EntityPlayer) {
      ToolSoul soul = SoulManager.getSoul(toolOrArmor);
      if (soul != null) {
        soul.updateTick(toolOrArmor, (EntityPlayer) entity);
      }
    }

    if (!world.isRemote) return;

    // Client-side name generation
    if (world.getTotalWorldTime() % CHECK_NAME_FREQUENCY == 0 && entity instanceof EntityPlayer) {
      EntityPlayer player = (EntityPlayer) entity;
      if (toolOrArmor.hasTagCompound() && toolOrArmor.getTagCompound().hasKey(NBT_TEMP_PARTLIST)) {
        NBTTagCompound compound = toolOrArmor.getTagCompound().getCompoundTag(NBT_TEMP_PARTLIST);

        int i = 0;
        String key = "part" + i;
        List<ItemStack> parts = Lists.newArrayList();

        // Load part stacks.
        do {
          NBTTagCompound tag = compound.getCompoundTag(key);
          parts.add(StackHelper.loadFromNBT(tag));
          key = "part" + ++i;
        } while (compound.hasKey(key));

        // Create name on the client.
        String displayName = createToolName(toolOrArmor.getItem(), parts);
        // tool.setStackDisplayName(displayName);

        // Send to the server.
        MessageItemRename message = new MessageItemRename(player.getName(), itemSlot, displayName,
            toolOrArmor);
        String line = String.format("%s crafted \"%s\"", player.getName(), displayName);
        SilentGems.logHelper.info(line);
        NetworkHandler.INSTANCE.sendToServer(message);
      }
    }
  }

  public static boolean onEntityItemUpdate(EntityItem entityItem) {

    // Ideal: return tool to player when it hits lava. We'll need to store the tools current owner.
    // TODO: ToolHelper#onEntityItemUpdate
    if (entityItem.world.getBlockState(new BlockPos(entityItem)).getMaterial() == Material.LAVA) {
      // Quick and dirty "kinda works" solution.
      entityItem.motionX = 0.0;
      entityItem.motionY = 0.2;
      entityItem.motionZ = 0.0;
      entityItem.setNoPickupDelay();
      return true;
    }

    return false;
  }

  public static boolean isSpecialAbilityEnabled(ItemStack tool) {

    return getTagBoolean(tool, NBT_ROOT_PROPERTIES, NBT_SETTINGS_SPECIAL);
  }

  public static void toggleSpecialAbility(ItemStack tool) {

    setTagBoolean(tool, NBT_ROOT_PROPERTIES, NBT_SETTINGS_SPECIAL,
        !getTagBoolean(tool, NBT_ROOT_PROPERTIES, NBT_SETTINGS_SPECIAL));
  }

  // ==========================================================================
  // Tool construction and decoration
  // ==========================================================================

  public static EnumMaterialTier getToolTier(ItemStack tool) {

    int id = getTagInt(tool, NBT_ROOT_PROPERTIES, NBT_TOOL_TIER);
    return EnumMaterialTier.values()[MathHelper.clamp(id, 0, EnumMaterialTier.values().length - 1)];
  }

  public static ItemStack constructTool(Item item, ItemStack rod, ItemStack head, int headCount) {

    // Expands material list to the desired length.
    ItemStack[] mats = new ItemStack[headCount];
    for (int i = 0; i < headCount; ++i)
      mats[i] = head;
    return constructTool(item, rod, mats);
  }

  public static ItemStack constructTool(Item item, ItemStack rod, ItemStack... materials) {

    if (materials.length == 1) {
      ItemStack[] newMats = new ItemStack[3];
      for (int i = 0; i < newMats.length; ++i)
        newMats[i] = materials[0];
      materials = newMats;
    }

    ItemStack result = new ItemStack(item);
    result.setTagCompound(new NBTTagCompound());
    result.getTagCompound().setTag(NBT_TEMP_PARTLIST, new NBTTagCompound());

    // Set construction materials
    ToolPart part;
    // Head
    for (int i = 0; i < materials.length; ++i) {
      if (materials[i].isEmpty()) {
        String str = "ToolHelper.constructTool: empty part! ";
        for (ItemStack stack : materials)
          str += stack + ", ";
        throw new IllegalArgumentException(str);
      }
      part = ToolPartRegistry.fromStack(materials[i]);
      EnumMaterialGrade grade = EnumMaterialGrade.fromStack(materials[i]);
      setTagPart(result, ToolPartPosition.HEAD.getKey(i), part, grade);

      // Write part list for client-side name generation.
      result.getTagCompound().getCompoundTag(NBT_TEMP_PARTLIST).setTag("part" + i,
          materials[i].writeToNBT(new NBTTagCompound()));
    }
    // Rod
    part = ToolPartRegistry.fromStack(rod);
    if (part == null) {
      return ItemStack.EMPTY;
    }
    setTagPart(result, ToolPartPosition.ROD.getKey(0), part, EnumMaterialGrade.NONE);

    // Create name
    String displayName = createToolName(item, materials);
    result.setStackDisplayName(displayName);

    recalculateStats(result);

    // Is this tier valid for this tool class?
    EnumMaterialTier toolTier = getToolTier(result);
    if (item instanceof ITool && !((ITool) item).getValidTiers().contains(toolTier)) {
      return ItemStack.EMPTY;
    }

    return result;
  }

  public static String createToolName(Item item, ItemStack[] materials) {

    return createToolName(item, Lists.newArrayList(materials));
  }

  public static String createToolName(Item item, List<ItemStack> materials) {

    ToolPart part;
    Set<String> prefixSet = Sets.newLinkedHashSet();
    Set<String> materialSet = Sets.newLinkedHashSet();
    for (ItemStack stack : materials) {
      part = ToolPartRegistry.fromStack(stack);
      if (part instanceof ToolPartMain) {
        String prefix = part.getDisplayNamePrefix(stack);
        if (prefix != null && !prefix.isEmpty())
          prefixSet.add(prefix);
        materialSet.add(part.getDisplayName(stack));
      }
    }

    String prefix = String.join(" ", prefixSet);
    if (!prefix.isEmpty())
      prefix += " ";
    String delimiter = SilentGems.i18n.translate("tool.silentgems.delimiter");
    String materialName = String.join(delimiter, materialSet);
    String toolName = Objects.requireNonNull(item.getRegistryName()).getPath();
    String name = SilentGems.i18n.translate("tool", toolName, materialName);

    return prefix + name;
  }

  public static ToolPart[] getConstructionParts(ItemStack tool) {

    if (tool.isEmpty())
      return new ToolPart[] {};

    List<ToolPart> parts = Lists.newArrayList();
    int index = -1;
    String key;
    ToolPart part;
    do {
      key = getPartId(tool, ToolPartPosition.HEAD.getKey(++index));
      if (key != null) {
        part = ToolPartRegistry.getPart(key);
        if (part != null)
          parts.add(part);
      }
    } while (key != null && !key.isEmpty());

    return parts.toArray(new ToolPart[0]);
  }

  public static EnumMaterialGrade[] getConstructionGrades(ItemStack tool) {

    if (tool.isEmpty())
      return new EnumMaterialGrade[] {};

    List<EnumMaterialGrade> grades = new ArrayList<>();
    int index = -1;
    String key;
    do {
      key = getPartId(tool, ToolPartPosition.HEAD.getKey(++index));
      if (key != null)
        grades.add(getPartGrade(tool, ToolPartPosition.HEAD.getKey(index)));
    } while (key != null && !key.isEmpty());

    return grades.toArray(new EnumMaterialGrade[0]);
  }

  public static ToolPart getConstructionRod(ItemStack tool) {

    String key = getPartId(tool, ToolPartPosition.ROD.getKey(0));
    return ToolPartRegistry.getPart(key);
  }

  public static ToolPart getConstructionTip(ItemStack tool) {

    return getPart(tool, ToolPartPosition.TIP);
  }

  public static void setConstructionTip(ItemStack tool, ToolPart part) {

    setTagPart(tool, ToolPartPosition.TIP.getKey(0), part, EnumMaterialGrade.NONE);
  }

  public static ItemStack decorateTool(ItemStack tool, ItemStack west, ItemStack north,
      ItemStack east, ItemStack south) {

    if (tool.isEmpty())
      return ItemStack.EMPTY;

    ItemStack result = tool.copy();
    result = decorate(result, west, EnumDecoPos.WEST);
    result = decorate(result, north, EnumDecoPos.NORTH);
    result = decorate(result, east, EnumDecoPos.EAST);
    result = decorate(result, south, EnumDecoPos.SOUTH);
    return result;
  }

  private static ItemStack decorate(ItemStack tool, ItemStack material, EnumDecoPos pos) {

    if (tool.isEmpty()) // Something went wrong
      return ItemStack.EMPTY;

    if (material.isEmpty()) // No material in the slot is OK.
      return tool;

    // Shields have different deco positions.
    // West - 'Top-left' (head left or 0)
    // North - 'Plate' (deco)
    // East - 'Top-Right' (head middle or 1)
    // South - 'Bottom' (head right or 2)
    if (tool.getItem() instanceof ItemGemShield) {
      if (pos == EnumDecoPos.NORTH) pos = EnumDecoPos.SOUTH;
      else if (pos == EnumDecoPos.EAST)  pos = EnumDecoPos.NORTH;
      else if (pos == EnumDecoPos.SOUTH) pos = EnumDecoPos.EAST;
    }

    // No deco bit on certain (mostly non-super) rods.
    if (pos == EnumDecoPos.SOUTH && !(tool.getItem() instanceof ItemGemShield)) {
      ToolPartRod partRod = (ToolPartRod) getConstructionRod(tool);
      if (partRod == null || !partRod.supportsDecoration())
        return tool;
    }

    // Get the tool part, making sure it exists.
    ToolPart part = ToolPartRegistry.fromDecoStack(material);
    if (part == null)
      return null;

    // Only main parts (like gems) work
    if (!(part instanceof ToolPartMain))
      return tool;

    ItemStack result = tool.copy();
    setTagPart(result, pos.nbtKey, part, EnumMaterialGrade.fromStack(material));
    return result;
  }

  private static boolean foundEmptyPart = false;
  private static Set<ToolPartMain> emptyPartSet = Sets.newHashSet();
  private static Map<Item, List<ItemStack>> toolSubItems = new HashMap<>();

  public static List<ItemStack> getSubItems(Item item, int materialLength) {

    if (toolSubItems.containsKey(item)) {
      // Return cached list.
      return toolSubItems.get(item);
    }

    List<ItemStack> list = Lists.newArrayList();
    // final boolean isSuperTool = item instanceof ITool && ((ITool) item).isSuperTool();
    final ItemStack rodWood = new ItemStack(Items.STICK);
    final ItemStack rodIron = ModItems.craftingMaterial.toolRodIron;
    final ItemStack rodGold = ModItems.craftingMaterial.toolRodGold;

    for (ToolPartMain part : ToolPartRegistry.getMains()) {
      // Check for parts with empty crafting stacks and scream at the user if any are found.
      if (part.getCraftingStack().isEmpty()) {
        if (!emptyPartSet.contains(part)) {
          emptyPartSet.add(part);
          SilentGems.logHelper.error("Part with empty crafting stack: " + part);
          if (!foundEmptyPart) {
            Greetings.addExtraMessage(TextFormatting.RED
                + "Errored tool part found! Please report this issue on the GitHub issue tracker.");
            foundEmptyPart = true;
          }
          Greetings.addExtraMessage(TextFormatting.ITALIC + part.toString());
        }
      } else {
        if (!part.isBlacklisted(part.getCraftingStack())) {
          if (item instanceof ITool && !((ITool) item).getValidTiers().contains(part.getTier())) {
            continue;
          }
          ItemStack rod = part.getTier() == EnumMaterialTier.SUPER ? rodGold
              : item instanceof ItemGemShield && part.getTier() == EnumMaterialTier.REGULAR
                  ? rodIron
                  : rodWood;
          ItemStack tool = constructTool(item, rod, part.getCraftingStack());
          tool.getTagCompound().setBoolean(NBT_EXAMPLE_TOOL, true);
          list.add(tool);
        }
      }
    }

    // Set maker name.
    String makerName = SilentGems.i18n.miscText("tooltip.OriginalOwner.Creative");
    for (ItemStack stack : list)
      ToolHelper.setOriginalOwner(stack, makerName);

    // Save for later to prevent duplicates with different UUIDs.
    toolSubItems.put(item, list);

    return list;
  }

  public static boolean areToolsEqual(ItemStack a, ItemStack b) {

    return a.isItemEqual(b) && getUUID(a).equals(getUUID(b));
  }

  static int lastRecipeIndex = -1;
  public static List<IRecipe> EXAMPLE_RECIPES = new ArrayList<>();

  public static void addExampleRecipe(Item item, String... lines) {

    addExampleRecipe(item, EnumMaterialTier.values(), lines);
  }

  public static void addExampleRecipe(Item item, EnumMaterialTier tier, String[] lines,
      Object... extraParams) {

    addExampleRecipe(item, new EnumMaterialTier[] { tier }, lines, extraParams);
  }

  public static void addExampleRecipe(Item item, EnumMaterialTier[] tiers, String[] lines,
      Object... extraParams) {

    // New ingredient-based recipes

    ConfigOptionToolClass config = item instanceof ITool ? ((ITool) item).getConfig() : null;

    for (EnumMaterialTier tier : tiers) {
      // Only add recipes for valid tiers
      if (config != null && !config.validTiers.contains(tier))
        continue;

      // Head parts for tier
      List<ItemStack> heads = new ArrayList<>();
      for (ToolPart part : ToolPartRegistry.getMains())
        if (!part.isBlacklisted(part.getCraftingStack()) && part.getTier() == tier
            && !part.getCraftingStack().isEmpty())
          heads.add(part.getCraftingStack());
      Ingredient headIngredient = Ingredient.fromStacks(heads.toArray(new ItemStack[0]));
      // Rods for tier
      List<ItemStack> rods = new ArrayList<>();
      for (ToolPart part : ToolPartRegistry.getRods())
        if (!part.isBlacklisted(part.getCraftingStack())
            && part.getCompatibleTiers().contains(tier))
          rods.add(part.getCraftingStack());
      Ingredient rodIngredient = Ingredient.fromStacks(rods.toArray(new ItemStack[0]));
      // Armor frames
      List<ItemStack> frames = new ArrayList<>();
      if (item instanceof ItemGemArmor) {
        EntityEquipmentSlot slot = ((ItemGemArmor) item).armorType;
        for (ToolPart part : ToolPartRegistry.getValues())
          if (part instanceof ArmorPartFrame && ((ArmorPartFrame) part).getSlot() == slot
              && part.getTier() == tier && !part.isBlacklisted(part.getCraftingStack()))
            frames.add(part.getCraftingStack());
      }
      Ingredient frameIngredient = Ingredient.fromStacks(frames.toArray(new ItemStack[0]));

      ResourceLocation recipeName = new ResourceLocation(
          item.getRegistryName().toString() + "_" + tier.name().toLowerCase() + "_example");
      ItemStack result = new ItemStack(item);
      NBTTagCompound tags = new NBTTagCompound();
      tags.setInteger(NBT_EXAMPLE_TOOL_TIER, tier.ordinal());
      result.setTagCompound(tags);

      // Super ugly, but I've got never better at the moment.
      List<Object> params = new ArrayList<>();
      for (String line : lines)
        params.add(line);
      if (recipeContainsKey(lines, "h")) {
        params.add('h');
        params.add(headIngredient);
      }
      if (recipeContainsKey(lines, "r")) {
        params.add('r');
        params.add(rodIngredient);
      }
      if (recipeContainsKey(lines, "a")) {
        params.add('a');
        params.add(frameIngredient);
      }
      for (Object obj : extraParams)
        params.add(obj);

      EXAMPLE_RECIPES.add(SilentGems.registry.getRecipeMaker().makeShaped(recipeName.getPath(),
          result, params.toArray()));
    }
  }

  private static boolean recipeContainsKey(String[] lines, String c) {

    for (String line : lines)
      if (line.contains(c))
        return true;
    return false;
  }

  // Tool Souls

  /**
   * UUID for tool souls. Must be kept separate from the normal UUID to prevent soul duping.
   *
   * @param toolOrArmor
   * @return
   */
  public static @Nullable UUID getSoulUUID(ItemStack toolOrArmor) {

    if (!(toolOrArmor.getItem() instanceof ITool || toolOrArmor.getItem() instanceof IArmor)) {
      return null;
    }

    initRootTag(toolOrArmor);

    if (!toolOrArmor.getTagCompound().hasUniqueId(NBT_SOUL_UUID)) {
      return null;
    }
    return toolOrArmor.getTagCompound().getUniqueId(NBT_SOUL_UUID);
  }

  public static void setRandomSoulUUID(ItemStack toolOrArmor) {

    initRootTag(toolOrArmor);
    toolOrArmor.getTagCompound().setUniqueId(NBT_SOUL_UUID, UUID.randomUUID());
  }

  // ==========================================================================
  // NBT helper methods
  // ==========================================================================

  static NBTTagCompound getRootTag(ItemStack tool, String key) {

    if (!(tool.getItem() instanceof ITool || tool.getItem() instanceof IArmor)) {
      return new NBTTagCompound();
    }

    if (key != null && !key.isEmpty()) {
      if (!tool.getTagCompound().hasKey(key)) {
        tool.getTagCompound().setTag(key, new NBTTagCompound());
      }
      return tool.getTagCompound().getCompoundTag(key);
    }
    return tool.getTagCompound();
  }

  static void initRootTag(ItemStack tool) {

    if (!tool.hasTagCompound()) {
      tool.setTagCompound(new NBTTagCompound());
    }
  }

  private static int getTagInt(ItemStack tool, String root, String name) {

    if (!tool.hasTagCompound()) {
      return 0;
    }
    return getRootTag(tool, root).getInteger(name);
  }

  private static int getTagInt(ItemStack tool, String root, String name, int defaultValue) {

    if (!tool.hasTagCompound() || !getRootTag(tool, root).hasKey(name)) {
      return defaultValue;
    }
    return getRootTag(tool, root).getInteger(name);
  }

  private static void setTagInt(ItemStack tool, String root, String name, int value) {

    initRootTag(tool);
    getRootTag(tool, root).setInteger(name, value);
  }

  private static float getTagFloat(ItemStack tool, String root, String name) {

    if (!tool.hasTagCompound()) {
      return 0.0f;
    }
    return getRootTag(tool, root).getFloat(name);
  }

  private static void setTagFloat(ItemStack tool, String root, String name, float value) {

    initRootTag(tool);
    getRootTag(tool, root).setFloat(name, value);
  }

  private static boolean getTagBoolean(ItemStack tool, String root, String name) {

    if (!tool.hasTagCompound()) {
      return false;
    }
    return getRootTag(tool, root).getBoolean(name);
  }

  private static void setTagBoolean(ItemStack tool, String root, String name, boolean value) {

    initRootTag(tool);
    getRootTag(tool, root).setBoolean(name, value);
  }

  private static String getTagString(ItemStack tool, String root, String name) {

    if (!tool.hasTagCompound()) {
      return "";
    }
    return getRootTag(tool, root).getString(name);
  }

  private static void setTagString(ItemStack tool, String root, String name, String value) {

    initRootTag(tool);
    getRootTag(tool, root).setString(name, value);
  }

  // private static NBTTagCompound getTagPart(ItemStack tool, String name) {
  //
  // if (!tool.hasTagCompound() /*|| !tool.getTagCompound().hasKey(NBT_ROOT)*/) {
  // return null;
  // }
  //
  // return getRootTag(tool).getCompoundTag(name);
  // }

  private static void setTagPart(ItemStack tool, String name, ToolPart part,
      EnumMaterialGrade grade) {

    initRootTag(tool);
    getRootTag(tool, NBT_ROOT_CONSTRUCTION).setString(name, part.getKey() + "#" + grade.name());
  }

  // ---------------------
  // Tool construction NBT
  // ---------------------

  public static String getPartId(ItemStack tool, String key) {

    if (!tool.hasTagCompound()) {
      return null;
    }

    return getRootTag(tool, NBT_ROOT_CONSTRUCTION).getString(key).split("#")[0];
  }

  public static EnumMaterialGrade getPartGrade(ItemStack tool, String key) {

    if (!tool.hasTagCompound()) {
      return null;
    }

    String[] array = getRootTag(tool, NBT_ROOT_CONSTRUCTION).getString(key).split("#");
    if (array.length < 2) {
      return EnumMaterialGrade.NONE;
    }
    return EnumMaterialGrade.fromString(array[1]);
  }

  public static ToolPart getPart(ItemStack tool, ToolPartPosition pos) {

    String key = getPartId(tool, pos.getKey(0));
    if (key == null || key.isEmpty()) {
      return null;
    }
    return ToolPartRegistry.getPart(key);
  }

  public static void setPart(ItemStack tool, ToolPart part, EnumMaterialGrade grade,
      ToolPartPosition pos) {

//    if (!part.validForToolOfTier(getToolTier(tool))) {
//      return;
//    }
    setTagPart(tool, pos.getKey(0), part, grade);
  }

  public static ToolPart getRenderPart(ItemStack tool, ToolPartPosition pos) {

    String key = getPartId(tool, pos.getDecoKey());
    if (key == null || key.isEmpty()) {
      return getPart(tool, pos);
    }
    return ToolPartRegistry.getPart(key);
  }

  public static void setRenderPart(ItemStack tool, ToolPart part, EnumMaterialGrade grade,
      ToolPartPosition pos) {

    if (!part.validForToolOfTier(getToolTier(tool))) {
      return;
    }
    setTagPart(tool, pos.getDecoKey(), part, grade);
  }

  // --------------
  // Statistics NBT
  // --------------

  public static String getOriginalOwner(ItemStack tool) {

    return getTagString(tool, NBT_ROOT_STATISTICS, NBT_STATS_ORIGINAL_OWNER);
  }

  public static void setOriginalOwner(ItemStack tool, EntityPlayer player) {

    setOriginalOwner(tool, player.getName());
  }

  public static void setOriginalOwner(ItemStack tool, String name) {

    if (getOriginalOwner(tool).isEmpty()) {
      if (name.equals(Names.SILENT_CHAOS_512))
        name = TextFormatting.RED + name;
      else if (name.equals(Names.M4THG33K))
        name = TextFormatting.GREEN + name;
      else if (name.equals(Names.CHAOTIC_PLAYZ))
        name = TextFormatting.BLUE + name;
      else if (name.equals(GuideBookGems.TOOL_OWNER_NAME))
        name = TextFormatting.AQUA + name;
      else
        name = PatronColors.instance.getColor(name) + name;

      setTagString(tool, NBT_ROOT_STATISTICS, NBT_STATS_ORIGINAL_OWNER, name);
    }
  }

  public static int getStatBlocksMined(ItemStack tool) {

    return getTagInt(tool, NBT_ROOT_STATISTICS, NBT_STATS_BLOCKS_MINED);
  }

  public static void incrementStatBlocksMined(ItemStack tool, int amount) {

    setTagInt(tool, NBT_ROOT_STATISTICS, NBT_STATS_BLOCKS_MINED, getStatBlocksMined(tool) + amount);
  }

  public static int getStatBlocksPlaced(ItemStack tool) {

    return getTagInt(tool, NBT_ROOT_STATISTICS, NBT_STATS_BLOCKS_PLACED);
  }

  public static void incrementStatBlocksPlaced(ItemStack tool, int amount) {

    setTagInt(tool, NBT_ROOT_STATISTICS, NBT_STATS_BLOCKS_PLACED,
        getStatBlocksPlaced(tool) + amount);
  }

  public static int getStatBlocksTilled(ItemStack tool) {

    return getTagInt(tool, NBT_ROOT_STATISTICS, NBT_STATS_BLOCKS_TILLED);
  }

  public static void incrementStatBlocksTilled(ItemStack tool, int amount) {

    setTagInt(tool, NBT_ROOT_STATISTICS, NBT_STATS_BLOCKS_TILLED,
        getStatBlocksTilled(tool) + amount);
  }

  public static int getStatPathsMade(ItemStack tool) {

    return getTagInt(tool, NBT_ROOT_STATISTICS, NBT_STATS_PATHS_MADE);
  }

  public static void incrementStatPathsMade(ItemStack tool, int amount) {

    setTagInt(tool, NBT_ROOT_STATISTICS, NBT_STATS_PATHS_MADE, getStatPathsMade(tool) + amount);
  }

  public static int getStatHitsLanded(ItemStack tool) {

    return getTagInt(tool, NBT_ROOT_STATISTICS, NBT_STATS_HITS);
  }

  public static void incrementStatHitsLanded(ItemStack tool, int amount) {

    setTagInt(tool, NBT_ROOT_STATISTICS, NBT_STATS_HITS, getStatHitsLanded(tool) + amount);
  }

  public static int getStatKillCount(ItemStack tool) {

    return getTagInt(tool, NBT_ROOT_STATISTICS, NBT_STATS_KILL_COUNT);
  }

  public static void incrementStatKillCount(ItemStack tool, int amount) {

    setTagInt(tool, NBT_ROOT_STATISTICS, NBT_STATS_KILL_COUNT, getStatKillCount(tool) + amount);
  }

  public static int getStatRedecorated(ItemStack tool) {

    return getTagInt(tool, NBT_ROOT_STATISTICS, NBT_STATS_REDECORATED);
  }

  public static void incrementStatRedecorated(ItemStack tool, int amount) {

    setTagInt(tool, NBT_ROOT_STATISTICS, NBT_STATS_REDECORATED, getStatRedecorated(tool) + amount);
  }

  public static int getStatShotsFired(ItemStack tool) {

    return getTagInt(tool, NBT_ROOT_STATISTICS, NBT_STATS_SHOTS_FIRED);
  }

  public static void incrementStatShotsFired(ItemStack tool, int amount) {

    setTagInt(tool, NBT_ROOT_STATISTICS, NBT_STATS_SHOTS_FIRED, getStatShotsFired(tool) + amount);
  }

  public static int getStatShotsLanded(ItemStack tool) {

    return getTagInt(tool, NBT_ROOT_STATISTICS, NBT_STATS_SHOTS_LANDED);
  }

  public static void incrementStatShotsLanded(ItemStack tool, int amount) {

    setTagInt(tool, NBT_ROOT_STATISTICS, NBT_STATS_SHOTS_FIRED, getStatShotsLanded(tool) + amount);
  }

  public static int getStatThrownCount(ItemStack tool) {

    return getTagInt(tool, NBT_ROOT_STATISTICS, NBT_STATS_THROWN);
  }

  public static void incrementStatThrownCount(ItemStack tool, int amount) {

    setTagInt(tool, NBT_ROOT_STATISTICS, NBT_STATS_THROWN, getStatThrownCount(tool) + amount);
  }

  // public static int getAnimationFrame(ItemStack tool) {
  //
  // return getTagInt(tool, NBT_ANIMATION_FRAME);
  // }
  //
  // public static void setAnimationFrame(ItemStack tool, int value) {
  //
  // setTagInt(tool, NBT_ANIMATION_FRAME, value);
  // }
}
