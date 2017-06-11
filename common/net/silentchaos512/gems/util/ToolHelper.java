package net.silentchaos512.gems.util;

import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.annotation.Nullable;

import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;
import com.google.common.collect.Sets;

import jline.internal.Log;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.Item.ToolMaterial;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemTool;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.common.util.EnumHelper;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.silentchaos512.gems.SilentGems;
import net.silentchaos512.gems.api.IBlockPlacer;
import net.silentchaos512.gems.api.ITool;
import net.silentchaos512.gems.api.lib.EnumDecoPos;
import net.silentchaos512.gems.api.lib.EnumMaterialGrade;
import net.silentchaos512.gems.api.lib.EnumMaterialTier;
import net.silentchaos512.gems.api.lib.EnumPartPosition;
import net.silentchaos512.gems.api.tool.ToolStats;
import net.silentchaos512.gems.api.tool.part.ToolPart;
import net.silentchaos512.gems.api.tool.part.ToolPartMain;
import net.silentchaos512.gems.api.tool.part.ToolPartRegistry;
import net.silentchaos512.gems.api.tool.part.ToolPartRod;
import net.silentchaos512.gems.config.GemsConfig;
import net.silentchaos512.gems.guide.GuideBookGems;
import net.silentchaos512.gems.item.ModItems;
import net.silentchaos512.gems.item.ToolRenderHelper;
import net.silentchaos512.gems.item.tool.ItemGemAxe;
import net.silentchaos512.gems.item.tool.ItemGemBow;
import net.silentchaos512.gems.item.tool.ItemGemHoe;
import net.silentchaos512.gems.item.tool.ItemGemPickaxe;
import net.silentchaos512.gems.item.tool.ItemGemShield;
import net.silentchaos512.gems.item.tool.ItemGemShovel;
import net.silentchaos512.gems.item.tool.ItemGemSickle;
import net.silentchaos512.gems.item.tool.ItemGemSword;
import net.silentchaos512.gems.lib.Names;
import net.silentchaos512.gems.network.NetworkHandler;
import net.silentchaos512.gems.network.message.MessageItemRename;
import net.silentchaos512.gems.skills.SkillAreaMiner;
import net.silentchaos512.gems.skills.SkillLumberjack;
import net.silentchaos512.gems.skills.ToolSkill;
import net.silentchaos512.lib.registry.IRegistryObject;
import net.silentchaos512.lib.util.ItemHelper;
import net.silentchaos512.lib.util.LocalizationHelper;
import net.silentchaos512.lib.util.PlayerHelper;
import net.silentchaos512.lib.util.StackHelper;
import net.silentchaos512.lib.util.WorldHelper;

public class ToolHelper {

  public static final String[] TOOL_CLASSES = { "Sword", "Pickaxe", "Shovel", "Axe", "Hoe",
      "Sickle", "Bow" };

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
  public static final String NBT_ROOT_STATISTICS = "SGStatistics";

  // Settings
  public static final String NBT_SETTINGS_SPECIAL = "SpecialEnabled";

  // Construction
  public static final String NBT_PART_ROOT = "Part";

  public static final String NBT_PART_HEAD_L = "Part0";
  public static final String NBT_PART_HEAD_M = "Part1";
  public static final String NBT_PART_HEAD_R = "Part2";
  public static final String NBT_PART_ROD = "PartRod";
  public static final String NBT_PART_ROD_DECO = "PartRodDeco";
  public static final String NBT_PART_ROD_WOOL = "PartRodWool";
  public static final String NBT_PART_HEAD_TIP = "PartHeadTip";

  // Saves tool tier to save processing power.
  public static final String NBT_TOOL_TIER = "ToolTier";
  // Used for client-side name generation, stored temporarily then removed.
  public static final String NBT_TEMP_PARTLIST = "PartListForName";

  // Decoration
  public static final String NBT_DECO_HEAD_L = "DecoHeadL";
  public static final String NBT_DECO_HEAD_M = "DecoHeadM";
  public static final String NBT_DECO_HEAD_R = "DecoHeadR";
  public static final String NBT_DECO_ROD = "DecoRod";

  // Stats
  public static final String NBT_PROP_DURABILITY = "Durability";
  public static final String NBT_PROP_HARVEST_LEVEL = "HarvestLevel";
  public static final String NBT_PROP_HARVEST_SPEED = "HarvestSpeed";
  public static final String NBT_PROP_MELEE_DAMAGE = "MeleeDamage";
  public static final String NBT_PROP_MAGIC_DAMAGE = "MagicDamage";
  public static final String NBT_PROP_ENCHANTABILITY = "Enchantability";
  public static final String NBT_PROP_MELEE_SPEED = "MeleeSpeed";
  public static final String NBT_PROP_CHARGE_SPEED = "ChargeSpeed";
  public static final String NBT_PROP_BLOCKING_POWER = "BlockingPower";

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

  public static void init() {

  }

  /**
   * Recalculate all stats and properties, including the rendering cache for the given tool. In general, this should be
   * called any time changes are made to a tool (aside from incrementing statistics, or something like that). For
   * example, this is called during construction, decoration, and for all tools in the players inventory during login.
   * 
   * @param tool
   */
  public static void recalculateStats(ItemStack tool) {

    ToolPart[] parts = getConstructionParts(tool);
    EnumMaterialGrade[] grades = getConstructionGrades(tool);
    if (parts.length == 0)
      return;

    ToolStats stats = new ToolStats(tool, parts, grades).calculate();

    // Reset render cache
    for (EnumPartPosition pos : EnumPartPosition.values()) {
      NBTTagCompound compound = tool.getTagCompound()
          .getCompoundTag(ToolRenderHelper.NBT_MODEL_INDEX);
      String str = "Layer" + pos.ordinal();
      for (int frame = 0; frame < (tool.getItem() instanceof ItemGemBow ? 4 : 1); ++frame)
        compound.removeTag(str + (frame > 0 ? "_" + frame : ""));
      compound.removeTag(str + "Color");
    }

    // Set color for parts
    ToolPart renderHeadM = getRenderPart(tool, EnumPartPosition.HEAD_MIDDLE);
    ToolPart renderHeadL = getRenderPart(tool, EnumPartPosition.HEAD_LEFT);
    ToolPart renderHeadR = getRenderPart(tool, EnumPartPosition.HEAD_RIGHT);
    ToolPart renderRodDeco = getRenderPart(tool, EnumPartPosition.ROD_DECO);
    ToolPart renderRod = getRenderPart(tool, EnumPartPosition.ROD);

    if (renderHeadM != null)
      setTagInt(tool, ToolRenderHelper.NBT_MODEL_INDEX,
          "Layer" + ToolRenderHelper.PASS_HEAD_M + "Color", renderHeadM.getColor(tool));
    if (renderHeadL != null)
      setTagInt(tool, ToolRenderHelper.NBT_MODEL_INDEX,
          "Layer" + ToolRenderHelper.PASS_HEAD_L + "Color", renderHeadL.getColor(tool));
    if (renderHeadR != null)
      setTagInt(tool, ToolRenderHelper.NBT_MODEL_INDEX,
          "Layer" + ToolRenderHelper.PASS_HEAD_R + "Color", renderHeadR.getColor(tool));
    if (renderRodDeco != null)
      setTagInt(tool, ToolRenderHelper.NBT_MODEL_INDEX,
          "Layer" + ToolRenderHelper.PASS_ROD_DECO + "Color", renderRodDeco.getColor(tool));
    if (renderRod != null)
      setTagInt(tool, ToolRenderHelper.NBT_MODEL_INDEX,
          "Layer" + ToolRenderHelper.PASS_ROD + "Color", renderRod.getColor(tool));

    setTagInt(tool, NBT_ROOT_PROPERTIES, NBT_PROP_DURABILITY, (int) stats.durability);
    setTagFloat(tool, NBT_ROOT_PROPERTIES, NBT_PROP_HARVEST_SPEED, stats.harvestSpeed);
    setTagFloat(tool, NBT_ROOT_PROPERTIES, NBT_PROP_MELEE_DAMAGE, stats.meleeDamage);
    setTagFloat(tool, NBT_ROOT_PROPERTIES, NBT_PROP_MAGIC_DAMAGE, stats.magicDamage);
    setTagFloat(tool, NBT_ROOT_PROPERTIES, NBT_PROP_MELEE_SPEED, stats.meleeSpeed);
    setTagFloat(tool, NBT_ROOT_PROPERTIES, NBT_PROP_CHARGE_SPEED, stats.chargeSpeed);
    setTagFloat(tool, NBT_ROOT_PROPERTIES, NBT_PROP_BLOCKING_POWER, stats.blockingPower);
    setTagInt(tool, NBT_ROOT_PROPERTIES, NBT_PROP_ENCHANTABILITY, (int) stats.enchantability);
    setTagInt(tool, NBT_ROOT_PROPERTIES, NBT_PROP_HARVEST_LEVEL, stats.harvestLevel);
    setTagInt(tool, NBT_ROOT_PROPERTIES, NBT_TOOL_TIER, parts[0].getTier().ordinal());
  }

  // ==========================================================================
  // Mining, using, repairing, etc
  // ==========================================================================

  public static boolean getIsRepairable(ItemStack tool, ItemStack material) {

    EnumMaterialTier tierTool = getToolTier(tool);
    EnumMaterialTier tierMat = EnumMaterialTier.fromStack(material);

    if (tierTool == null || tierMat == null)
      return false;
    return tierTool.ordinal() <= tierMat.ordinal();
  }

  public static void attemptDamageTool(ItemStack tool, int amount, EntityLivingBase entityLiving) {

    amount = Math.min(getMaxDamage(tool) - tool.getItemDamage(), amount);
    tool.attemptDamageItem(amount, SilentGems.random);
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

  public static int getHarvestLevel(ItemStack tool) {

    return getTagInt(tool, NBT_ROOT_PROPERTIES, NBT_PROP_HARVEST_LEVEL);
  }

  public static int getMaxDamage(ItemStack tool) {

    return getTagInt(tool, NBT_ROOT_PROPERTIES, NBT_PROP_DURABILITY);
  }

  public static float getChargeSpeed(ItemStack tool) {

    return getTagFloat(tool, NBT_ROOT_PROPERTIES, NBT_PROP_CHARGE_SPEED);
  }

  public static float getBlockingPower(ItemStack tool) {

    return getTagFloat(tool, NBT_ROOT_PROPERTIES, NBT_PROP_BLOCKING_POWER);
  }

  public static boolean isBroken(ItemStack tool) {

    return tool.getItemDamage() >= getMaxDamage(tool);
  }

  /**
   * Check if the tool has no construction parts. Only checks the head, but no valid tools will have no head parts.
   * @return true if the has no construction parts.
   */
  public static boolean hasNoConstruction(ItemStack tool) {

    String key = getPartId(tool, "Part0");
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

    float base = ((ITool) tool.getItem()).getBaseMeleeSpeedModifier();
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
    if (StackHelper.isValid(stackOffHand) && stackOffHand.getItem() instanceof ItemBlock) {
      ItemBlock itemBlock = (ItemBlock) stackOffHand.getItem();
      BlockPos target = pos;

      if (!itemBlock.getBlock().isReplaceable(world, pos))
        target = pos.offset(side);

      if (player.canPlayerEdit(target, side, stackOffHand) && WorldHelper.mayPlace(world,
          itemBlock.block, target, false, side, player, stackOffHand))
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
    ItemStack nextStack = StackHelper.empty();
    ItemStack lastStack = player.inventory.getStackInSlot(8); // Slot 9 in hotbar

    if (toolSlot < 8) {
      // Get stack in slot after tool.
      nextStack = player.inventory.getStackInSlot(itemSlot);

      // If there's nothing there we can use, try slot 9 instead.
      if (StackHelper.isEmpty(nextStack) || (!(nextStack.getItem() instanceof ItemBlock)
          && !(nextStack.getItem() instanceof IBlockPlacer))) {
        nextStack = lastStack;
        itemSlot = 8;
      }

      if (StackHelper.isValid(nextStack)) {
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
            IBlockState state = block.getStateFromMeta(itemBlock.getMetadata(nextStack));
            if (state.getMaterial().blocksMovement() && playerBounds.intersectsWith(blockBounds)) {
              return EnumActionResult.FAIL;
            }
          }

          int prevSize = StackHelper.getCount(nextStack);
          result = ItemHelper.useItemAsPlayer(nextStack, player, world, pos, side, hitX, hitY,
              hitZ);

          // Don't consume in creative mode?
          if (player.capabilities.isCreativeMode) {
            StackHelper.setCount(nextStack, prevSize);
          }

          // Remove empty stacks.
          if (StackHelper.isEmpty(nextStack)) {
            nextStack = StackHelper.empty();
            player.inventory.setInventorySlotContents(itemSlot, StackHelper.empty());
          }
        }
      }
    }

    if (result == EnumActionResult.SUCCESS)
      incrementStatBlocksPlaced(stack, 1);

    return result;
  }

  public static @Nullable ToolSkill getSuperSkill(ItemStack tool) {

    if (getToolTier(tool).ordinal() < EnumMaterialTier.SUPER.ordinal())
      return null;

    Item item = tool.getItem();
    if (item instanceof ItemGemPickaxe || item instanceof ItemGemShovel) {
      return SkillAreaMiner.INSTANCE;
    } else if (item instanceof ItemGemAxe) {
      return GemsConfig.SWITCH_AXE_SUPER ? SkillAreaMiner.INSTANCE : SkillLumberjack.INSTANCE;
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

  public static void onUpdate(ItemStack tool, World world, Entity entity, int itemSlot,
      boolean isSelected) {

    if (!world.isRemote) {
      if (hasNoConstruction(tool)) {
        ItemStack newTool = ToolRandomizer.INSTANCE.randomize(tool);
      }
      return;
    }

    if (world.getTotalWorldTime() % CHECK_NAME_FREQUENCY == 0 && entity instanceof EntityPlayer) {
      EntityPlayer player = (EntityPlayer) entity;
      if (tool.hasTagCompound() && tool.getTagCompound().hasKey(NBT_TEMP_PARTLIST)) {
        NBTTagCompound compound = tool.getTagCompound().getCompoundTag(NBT_TEMP_PARTLIST);

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
        String displayName = createToolName(tool.getItem(), parts);
        // tool.setStackDisplayName(displayName);

        // Send to the server.
        MessageItemRename message = new MessageItemRename(player.getName(), itemSlot, displayName,
            tool);
        SilentGems.logHelper.info("Sending tool name \"" + displayName + "\" to server.");
        NetworkHandler.INSTANCE.sendToServer(message);
      }
    }
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
      if (StackHelper.isEmpty(materials[i])) {
        String str = "ToolHelper.constructTool: empty part! ";
        for (ItemStack stack : materials)
          str += stack + ", ";
        throw new IllegalArgumentException(str);
      }
      part = ToolPartRegistry.fromStack(materials[i]);
      EnumMaterialGrade grade = EnumMaterialGrade.fromStack(materials[i]);
      setTagPart(result, "Part" + i, part, grade);

      // Write part list for client-side name generation.
      result.getTagCompound().getCompoundTag(NBT_TEMP_PARTLIST).setTag("part" + i,
          materials[i].writeToNBT(new NBTTagCompound()));
    }
    // Rod
    part = ToolPartRegistry.fromStack(rod);
    setTagPart(result, "PartRod", part, EnumMaterialGrade.NONE);

    // Create name
    String displayName = createToolName(item, materials);
    result.setStackDisplayName(displayName);

    recalculateStats(result);

    // Is this tier valid for this tool class?
    EnumMaterialTier toolTier = getToolTier(result);
    if (item instanceof ITool && !((ITool) item).getValidTiers().contains(toolTier)) {
      return StackHelper.empty();
    }

    return result;
  }

  public static String createToolName(Item item, ItemStack[] materials) {

    return createToolName(item, Lists.newArrayList(materials));
  }

  public static String createToolName(Item item, List<ItemStack> materials) {

    ToolPart part;
    LocalizationHelper loc = SilentGems.localizationHelper;
    Set<String> prefixSet = Sets.newLinkedHashSet();
    Set<String> materialSet = Sets.newLinkedHashSet();
    for (ItemStack stack : materials) {
      part = ToolPartRegistry.fromStack(stack);
      if (part != null) {
        String prefix = part.getDisplayNamePrefix(stack);
        if (prefix != null && !prefix.isEmpty())
          prefixSet.add(prefix);
        materialSet.add(part.getDisplayName(stack));
      }
    }

    String prefix = String.join(" ", prefixSet);
    if (!prefix.isEmpty())
      prefix += " ";
    String delimiter = loc.getLocalizedString("tool.silentgems:delimiter");
    String materialName = String.join(delimiter, materialSet);
    String toolName = ((IRegistryObject) item).getName();
    String name = loc.getLocalizedString("tool", toolName, materialName);

    return prefix + name;
  }

  public static ToolPart[] getConstructionParts(ItemStack tool) {

    if (StackHelper.isEmpty(tool))
      return new ToolPart[] {};

    List<ToolPart> parts = Lists.newArrayList();
    int index = -1;
    String key;
    ToolPart part;
    do {
      key = getPartId(tool, "Part" + ++index);
      if (key != null) {
        part = ToolPartRegistry.getPart(key);
        if (part != null)
          parts.add(part);
      }
    } while (key != null && !key.isEmpty());

    return parts.toArray(new ToolPart[parts.size()]);
  }

  public static EnumMaterialGrade[] getConstructionGrades(ItemStack tool) {

    if (StackHelper.isEmpty(tool))
      return new EnumMaterialGrade[] {};

    List<EnumMaterialGrade> grades = Lists.newArrayList();
    int index = -1;
    String key;
    do {
      key = getPartId(tool, "Part" + ++index);
      if (key != null)
        grades.add(getPartGrade(tool, "Part" + index));
    } while (key != null && !key.isEmpty());

    return grades.toArray(new EnumMaterialGrade[grades.size()]);
  }

  public static ToolPart getConstructionRod(ItemStack tool) {

    String key = getPartId(tool, "PartRod");
    return ToolPartRegistry.getPart(key);
  }

  public static ToolPart getConstructionTip(ItemStack tool) {

    return getPart(tool, EnumPartPosition.TIP);
  }

  public static void setConstructionTip(ItemStack tool, ToolPart part) {

    setTagPart(tool, NBT_PART_HEAD_TIP, part, EnumMaterialGrade.NONE);
  }

  public static ItemStack decorateTool(ItemStack tool, ItemStack west, ItemStack north,
      ItemStack east, ItemStack south) {

    if (StackHelper.isEmpty(tool))
      return StackHelper.empty();

    ItemStack result = tool.copy();
    result = decorate(result, west, EnumDecoPos.WEST);
    result = decorate(result, north, EnumDecoPos.NORTH);
    result = decorate(result, east, EnumDecoPos.EAST);
    result = decorate(result, south, EnumDecoPos.SOUTH);
    return result;
  }

  private static ItemStack decorate(ItemStack tool, ItemStack material, EnumDecoPos pos) {

    if (StackHelper.isEmpty(tool)) // Something went wrong
      return StackHelper.empty();

    if (StackHelper.isEmpty(material)) // No material in the slot is OK.
      return tool;

    // Shields have different deco positions.
    // West - 'Top-left' (head left or 0)
    // North - 'Plate' (deco)
    // East - 'Top-Right' (head middle or 1)
    // South - 'Bottom' (head right or 2)
    if (tool.getItem() instanceof ItemGemShield) { //@formatter:off
      if (pos == EnumDecoPos.NORTH) pos = EnumDecoPos.SOUTH;
      else if (pos == EnumDecoPos.EAST)  pos = EnumDecoPos.NORTH;
      else if (pos == EnumDecoPos.SOUTH) pos = EnumDecoPos.EAST;
    } //@formatter:on

    // No deco bit on certain (mostly non-super) rods.
    if (pos == EnumDecoPos.SOUTH && !(tool.getItem() instanceof ItemGemShield)) {
      ToolPartRod partRod = (ToolPartRod) getConstructionRod(tool);
      if (partRod == null || !partRod.supportsDecoration())
        return tool;
    }

    // Get the tool part, making sure it exists.
    ToolPart part = ToolPartRegistry.fromStack(material);
    if (part == null)
      return null;

    // Only main parts (like gems) work
    if (!(part instanceof ToolPartMain))
      return tool;

    ItemStack result = StackHelper.safeCopy(tool);
    switch (pos) {
      case WEST:
        setTagPart(result, NBT_DECO_HEAD_L, part, EnumMaterialGrade.fromStack(material));
        break;
      case NORTH:
        setTagPart(result, NBT_DECO_HEAD_M, part, EnumMaterialGrade.fromStack(material));
        break;
      case EAST:
        setTagPart(result, NBT_DECO_HEAD_R, part, EnumMaterialGrade.fromStack(material));
        break;
      case SOUTH:
        setTagPart(result, NBT_PART_ROD_DECO, part, EnumMaterialGrade.fromStack(material));
        break;
      default:
        SilentGems.instance.logHelper.warning("ToolHelper.decorate: invalid deco pos " + pos);
        break;
    }
    return result;
  }

  public static List<ItemStack> getSubItems(Item item, int materialLength) {

    List<ItemStack> list = Lists.newArrayList();
    //final boolean isSuperTool = item instanceof ITool && ((ITool) item).isSuperTool();
    final ItemStack rodWood = new ItemStack(Items.STICK);
    final ItemStack rodIron = ModItems.craftingMaterial.toolRodIron;
    final ItemStack rodGold = ModItems.craftingMaterial.toolRodGold;

    for (ToolPartMain part : ToolPartRegistry.getMains()) {
      if (StackHelper.isEmpty(part.getCraftingStack())) {
        throw new IllegalArgumentException("Part with empty crafting stack: " + part);
      }
      if (!part.isBlacklisted(part.getCraftingStack())) {
        if (item instanceof ITool && !((ITool) item).getValidTiers().contains(part.getTier())) {
          continue;
        }
        list.add(constructTool(item,
            part.getTier() == EnumMaterialTier.SUPER ? rodGold
                : item instanceof ItemGemShield && part.getTier() == EnumMaterialTier.REGULAR
                    ? rodIron : rodWood,
            part.getCraftingStack()));
      }
    }

    // Set maker name.
    String makerName = SilentGems.localizationHelper.getMiscText("Tooltip.OriginalOwner.Creative");
    for (ItemStack stack : list)
      ToolHelper.setOriginalOwner(stack, makerName);

    return list;
  }

  public static boolean areToolsEqual(ItemStack a, ItemStack b) {

    return a.getItem() == b.getItem()
        && getRootTag(a, NBT_ROOT_CONSTRUCTION).equals(getRootTag(b, NBT_ROOT_CONSTRUCTION))
        && getRootTag(a, NBT_ROOT_DECORATION).equals(getRootTag(b, NBT_ROOT_DECORATION));
  }

  /**
   * Adds a "display" or "example" recipes for players to view in JEI.
   */
  public static void addRecipe(ItemStack result, String line1, String line2, String line3,
      ItemStack head, Object rod) {

    ToolPart part = ToolPartRegistry.fromStack(head);
    EnumMaterialTier tier = getToolTier(result);
    if (part != null && !part.isBlacklisted(head))
      GameRegistry.addRecipe(new ShapedOreRecipe(result, line1, line2, line3, 'g', head, 's', rod,
          'f', tier.getFiller()));
  }

  // ==========================================================================
  // NBT helper methods
  // ==========================================================================

  private static NBTTagCompound getRootTag(ItemStack tool, String key) {

    if (key != null && !key.isEmpty()) {
      if (!tool.getTagCompound().hasKey(key)) {
        tool.getTagCompound().setTag(key, new NBTTagCompound());
      }
      return tool.getTagCompound().getCompoundTag(key);
    }
    return tool.getTagCompound();
  }

  private static void initRootTag(ItemStack tool) {

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

  public static ToolPart getPart(ItemStack tool, EnumPartPosition pos) {

    // NBTTagCompound tag;
    String key;
    switch (pos) {
      case HEAD_LEFT:
        key = getPartId(tool, NBT_PART_HEAD_L);
        break;
      case HEAD_MIDDLE:
        key = getPartId(tool, NBT_PART_HEAD_M);
        break;
      case HEAD_RIGHT:
        key = getPartId(tool, NBT_PART_HEAD_R);
        break;
      case ROD:
        key = getPartId(tool, NBT_PART_ROD);
        break;
      case ROD_DECO:
        key = getPartId(tool, NBT_PART_ROD_DECO);
        break;
      case ROD_GRIP:
        key = getPartId(tool, NBT_PART_ROD_WOOL);
        break;
      case TIP:
        key = getPartId(tool, NBT_PART_HEAD_TIP);
        break;
      default:
        Log.warn("ToolHelper.getPart: Unknown EnumPartPosition " + pos);
        key = null;
    }

    if (key == null || key.isEmpty()) {
      return null;
    }
    return ToolPartRegistry.getPart(key);
  }

  public static void setPart(ItemStack tool, ToolPart part, EnumMaterialGrade grade,
      EnumPartPosition pos) {

    if (!part.validForToolOfTier(getToolTier(tool))) {
      return;
    }
    switch (pos) {
      case HEAD_LEFT:
        setTagPart(tool, NBT_PART_HEAD_L, part, grade);
        break;
      case HEAD_MIDDLE:
        setTagPart(tool, NBT_PART_HEAD_M, part, grade);
        break;
      case HEAD_RIGHT:
        setTagPart(tool, NBT_PART_HEAD_R, part, grade);
        break;
      case ROD:
        setTagPart(tool, NBT_PART_ROD, part, grade);
        break;
      case ROD_DECO:
        setTagPart(tool, NBT_PART_ROD_DECO, part, grade);
        break;
      case ROD_GRIP:
        setTagPart(tool, NBT_PART_ROD_WOOL, part, grade);
        break;
      case TIP:
        setTagPart(tool, NBT_PART_HEAD_TIP, part, grade);
        break;
      default:
        Log.warn("ToolHelper.getPart: Unknown EnumPartPosition " + pos);
    }
  }

  public static ToolPart getRenderPart(ItemStack tool, EnumPartPosition pos) {

    // NBTTagCompound tag;
    String key;
    switch (pos) {
      case HEAD_LEFT:
        key = getPartId(tool, NBT_DECO_HEAD_L);
        break;
      case HEAD_MIDDLE:
        key = getPartId(tool, NBT_DECO_HEAD_M);
        break;
      case HEAD_RIGHT:
        key = getPartId(tool, NBT_DECO_HEAD_R);
        break;
      case ROD:
        key = getPartId(tool, NBT_DECO_ROD);
        break;
      case ROD_DECO:
        key = getPartId(tool, NBT_PART_ROD_DECO);
        break;
      case ROD_GRIP:
        key = getPartId(tool, NBT_PART_ROD_WOOL);
        break;
      case TIP:
        key = getPartId(tool, NBT_PART_HEAD_TIP);
        break;
      default:
        Log.warn("ToolHelper.getPart: Unknown EnumPartPosition " + pos);
        // tag = null;
        key = null;
    }

    if (key == null || key.isEmpty()) {
      // return null;
      return getPart(tool, pos);
    }
    return ToolPartRegistry.getPart(key);
  }

  public static void setRenderPart(ItemStack tool, ToolPart part, EnumMaterialGrade grade,
      EnumPartPosition pos) {

    if (!part.validForToolOfTier(getToolTier(tool))) {
      return;
    }
    switch (pos) {
      case HEAD_LEFT:
        setTagPart(tool, NBT_DECO_HEAD_L, part, grade);
        break;
      case HEAD_MIDDLE:
        setTagPart(tool, NBT_DECO_HEAD_M, part, grade);
        break;
      case HEAD_RIGHT:
        setTagPart(tool, NBT_DECO_HEAD_R, part, grade);
        break;
      case ROD:
        setTagPart(tool, NBT_DECO_ROD, part, grade);
        break;
      case ROD_DECO:
        setTagPart(tool, NBT_PART_ROD_DECO, part, grade);
        break;
      case ROD_GRIP:
        setTagPart(tool, NBT_PART_ROD_WOOL, part, grade);
        break;
      case TIP:
        setTagPart(tool, NBT_PART_HEAD_TIP, part, grade);
        break;
      default:
        Log.warn("ToolHelper.getPart: Unknown EnumPartPosition " + pos);
    }
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

  // ---------------------
  // Rendering NBT methods
  // ---------------------

  public static int getColorForPass(ItemStack tool, int pass) {

    return getTagInt(tool, ToolRenderHelper.NBT_MODEL_INDEX, "Layer" + pass + "Color", 0xFFFFFF);
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
