package net.silentchaos512.gems.core.util;

import java.util.List;

import org.lwjgl.input.Keyboard;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeHooks;
import net.silentchaos512.gems.SilentGems;
import net.silentchaos512.gems.achievement.GemsAchievement;
import net.silentchaos512.gems.api.IPlaceable;
import net.silentchaos512.gems.configuration.Config;
import net.silentchaos512.gems.enchantment.EnchantmentAOE;
import net.silentchaos512.gems.enchantment.EnchantmentLumberjack;
import net.silentchaos512.gems.enchantment.ModEnchantments;
import net.silentchaos512.gems.item.CraftingMaterial;
import net.silentchaos512.gems.item.Gem;
import net.silentchaos512.gems.item.ModItems;
import net.silentchaos512.gems.item.tool.GemAxe;
import net.silentchaos512.gems.item.tool.GemBow;
import net.silentchaos512.gems.item.tool.GemHoe;
import net.silentchaos512.gems.item.tool.GemPickaxe;
import net.silentchaos512.gems.item.tool.GemShovel;
import net.silentchaos512.gems.item.tool.GemSickle;
import net.silentchaos512.gems.item.tool.GemSword;
import net.silentchaos512.gems.lib.EnumGem;
import net.silentchaos512.gems.lib.EnumTipUpgrade;
import net.silentchaos512.gems.lib.Names;
import net.silentchaos512.gems.material.ModMaterials;

/**
 * The purpose of this class is to have shared code for tools in one place, to make updating/expanding the mod easier.
 */
public class ToolHelper {

  public static final String[] TOOL_CLASSES = { "Sword", "Pickaxe", "Shovel", "Axe", "Hoe",
      "Sickle", "Bow" };

  /*
   * NBT constants
   */

  public static final String NBT_ROOT = SilentGems.MOD_ID + "Tool";
  public static final String NBT_HEAD_L = "HeadL";
  public static final String NBT_HEAD_M = "HeadM";
  public static final String NBT_HEAD_R = "HeadR";
  public static final String NBT_ROD = "Rod";
  public static final String NBT_ROD_DECO = "RodDeco";
  public static final String NBT_ROD_WOOL = "RodWool";
  public static final String NBT_TIP = "Tip";
  public static final String NBT_NO_GLINT = "NoGlint";
  public static final String NBT_ANIMATION_FRAME = "AnimationFrame";

  public static final String NBT_STATS_BLOCKS_MINED = "BlocksMined";
  public static final String NBT_STATS_BLOCKS_PLACED = "BlocksPlaced";
  public static final String NBT_STATS_BLOCKS_TILLED = "BlocksTilled";
  public static final String NBT_STATS_HITS = "HitsLanded";
  public static final String NBT_STATS_SHOTS_FIRED = "ShotsFired";
  public static final String NBT_STATS_REDECORATED = "Redecorated";

  /**
   * Gets the "gem ID", or base material ID for a tool. Note that the regular and supercharged gems have the same ID
   * (ie, regular ruby == supercharged ruby == 0). For gems, this number is in [0, 11]. Other IDs can be found in
   * ModMaterials.
   * 
   * @param tool
   * @return The material ID, or -1 if it can't be determined.
   */
  public static int getToolGemId(ItemStack tool) {

    if (tool == null) {
      return -1;
    }

    Item item = tool.getItem();
    if (item instanceof GemSword) {
      return ((GemSword) item).gemId;
    } else if (item instanceof GemPickaxe) {
      return ((GemPickaxe) item).gemId;
    } else if (item instanceof GemShovel) {
      return ((GemShovel) item).gemId;
    } else if (item instanceof GemAxe) {
      return ((GemAxe) item).gemId;
    } else if (item instanceof GemHoe) {
      return ((GemHoe) item).gemId;
    } else if (item instanceof GemSickle) {
      return ((GemSickle) item).gemId;
    } else if (item instanceof GemBow) {
      return ((GemBow) item).gemId;
    } else {
      LogHelper.debug("Called ToolHelper.getToolGemId on a non-Gems tool!");
      return -1;
    }
  }

  /**
   * Determines whether the tool is "supercharged" or not.
   * 
   * @param tool
   * @return True for supercharged tools (ornate rod) or false for regular tools (wooden rod).
   */
  public static boolean getToolIsSupercharged(ItemStack tool) {

    if (tool == null) {
      return false;
    }

    Item item = tool.getItem();
    if (item instanceof GemSword) {
      return ((GemSword) item).supercharged;
    } else if (item instanceof GemPickaxe) {
      return ((GemPickaxe) item).supercharged;
    } else if (item instanceof GemShovel) {
      return ((GemShovel) item).supercharged;
    } else if (item instanceof GemAxe) {
      return ((GemAxe) item).supercharged;
    } else if (item instanceof GemHoe) {
      return ((GemHoe) item).supercharged;
    } else if (item instanceof GemSickle) {
      return ((GemSickle) item).supercharged;
    } else if (item instanceof GemBow) {
      return ((GemBow) item).supercharged;
    } else {
      LogHelper.debug("Called ToolHelper.getToolIsSupercharged on a non-Gems tool!");
      return false;
    }
  }

  /**
   * Gets the material ID for the given material, or -1 if it's not a recognized tool material.
   * 
   * @param material
   * @return
   */
  public static int getIdFromMaterial(ItemStack material) {

    Item item = material.getItem();
    if (item instanceof Gem) {
      return material.getItemDamage() & 0xF;
    } else if (CraftingMaterial.doesStackMatch(material, Names.CHAOS_ESSENCE_PLUS_2)) {
      return ModMaterials.CHAOS_GEM_ID;
    } else if (item == Items.flint) {
      return ModMaterials.FLINT_GEM_ID;
    } else if (item == Items.fish) {
      return ModMaterials.FISH_GEM_ID;
    } else {
      return -1;
    }
  }

  public static ItemStack getCraftingMaterial(int gemId, boolean supercharged) {

    if (gemId < EnumGem.values().length) {
      return new ItemStack(ModItems.gem, 1, gemId | (supercharged ? 0x10 : 0));
    }

    switch (gemId) {
      case ModMaterials.CHAOS_GEM_ID:
        return CraftingMaterial.getStack(Names.CHAOS_ESSENCE_PLUS_2);
      case ModMaterials.FLINT_GEM_ID:
        return new ItemStack(Items.flint);
      case ModMaterials.FISH_GEM_ID:
        return new ItemStack(Items.fish);
      default:
        LogHelper.warning("ToolHelper.getCraftingMaterial: Unknown gem ID: " + gemId);
        return null;
    }
  }

  // ==========================================================================
  // Tooltip helpers
  // ==========================================================================

  public static void addInformation(ItemStack tool, EntityPlayer player, List list,
      boolean advanced) {

    boolean keyControl = Keyboard.isKeyDown(Keyboard.KEY_LCONTROL)
        || Keyboard.isKeyDown(Keyboard.KEY_RCONTROL);
    boolean keyShift = Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)
        || Keyboard.isKeyDown(Keyboard.KEY_RSHIFT);

    String line;

    // Old NBT warning.
    if (hasOldNBT(tool)) {
      addInformationOldNBT(tool, player, list, advanced);
      return;
    }

    // Tool Upgrades
    addInformationUpgrades(tool, player, list, advanced);

    // Statistics
    if (keyControl) {
      addInformationStatistics(tool, player, list, advanced);
    } else {
      line = LocalizationHelper.getMiscText("PressCtrl");
      list.add(EnumChatFormatting.YELLOW + line);
    }

    // Decoration debug
    if (keyControl && keyShift) {
      addInformationDecoDebug(tool, player, list, advanced);
    }

    // Chaos tools
    int gemId = getToolGemId(tool);
    if (gemId == ModMaterials.CHAOS_GEM_ID) {
      Item item = tool.getItem();
      // Work in progress warning
      list.add(EnumChatFormatting.RED + "Work in progress, suggestions welcome.");
      // Chaos sword
      if (item instanceof GemSword) {
        for (int i = 1; i < 4; ++i) {
          line = LocalizationHelper.getMiscText("Tool.FireChaosOrbs" + i);
          list.add(EnumChatFormatting.AQUA + line);
        }
      }
      // No flying penalty
      if (item instanceof GemPickaxe || item instanceof GemShovel || item instanceof GemAxe) {
        line = LocalizationHelper.getMiscText("Tool.NoSpeedPenalty");
        list.add(EnumChatFormatting.AQUA + line);
      }
    }
  }

  private static void addInformationUpgrades(ItemStack tool, EntityPlayer player, List list,
      boolean advanced) {

    String line;

    // Tipped upgrades
    int tip = getToolHeadTip(tool);
    if (tip == 1) {
      line = LocalizationHelper.getMiscText("Tool.IronTipped");
      list.add(EnumChatFormatting.WHITE + line);
    } else if (tip == 2) {
      line = LocalizationHelper.getMiscText("Tool.DiamondTipped");
      list.add(EnumChatFormatting.AQUA + line);
    } else if (tip == 3) {
      line = LocalizationHelper.getMiscText("Tool.EmeraldTipped");
      list.add(EnumChatFormatting.GREEN + line);
    }
  }

  private static void addInformationStatistics(ItemStack tool, EntityPlayer player, List list,
      boolean advanced) {

    String line;
    int amount;
    String separator = EnumChatFormatting.DARK_GRAY
        + LocalizationHelper.getMiscText("Tool.Stats.Separator");
    boolean isBow = tool.getItem() instanceof GemBow;

    // Header
    line = LocalizationHelper.getMiscText("Tool.Stats.Header");
    list.add(EnumChatFormatting.YELLOW + line);
    list.add(separator);

    // Blocks mined
    amount = getStatBlocksMined(tool);
    line = LocalizationHelper.getMiscText("Tool.Stats.Mined");
    line = String.format(line, amount);
    list.add(line);

    // Blocks placed (mining tools only)
    if (InventoryHelper.isGemMiningTool(tool)) {
      amount = getStatBlocksPlaced(tool);
      line = LocalizationHelper.getMiscText("Tool.Stats.Placed");
      line = String.format(line, amount);
      list.add(line);
    }

    // Blocks tilled (hoes only)
    if (tool.getItem() instanceof GemHoe) {
      amount = getStatBlocksTilled(tool);
      line = LocalizationHelper.getMiscText("Tool.Stats.Tilled");
      line = String.format(line, amount);
      list.add(line);
    }

    // Hits landed (I would like this to register arrow hits, WIP)
    amount = getStatHitsLanded(tool);
    line = LocalizationHelper.getMiscText("Tool.Stats.Hits");
    line = String.format(line, amount);
    list.add(line);

    // Shots fired (bows only)
    if (isBow) {
      amount = getStatShotsFired(tool);
      line = LocalizationHelper.getMiscText("Tool.Stats.ShotsFired");
      line = String.format(line, amount);
      list.add(line);
    }

    // Redecorated count
    amount = getStatRedecorated(tool);
    line = LocalizationHelper.getMiscText("Tool.Stats.Redecorated");
    line = String.format(line, amount);
    list.add(line);

    list.add(separator);
  }

  private static void addInformationDecoDebug(ItemStack tool, EntityPlayer player, List list,
      boolean advanced) {

    String line = "Deco:";
    line += " HL:" + getToolHeadLeft(tool);
    line += " HM:" + getToolHeadMiddle(tool);
    line += " HR:" + getToolHeadRight(tool);
    line += " RD:" + getToolRodDeco(tool);
    line += " RW:" + getToolRodWool(tool);
    line += " R:" + getToolRod(tool);
    line += " T:" + getToolHeadTip(tool);
    list.add(EnumChatFormatting.DARK_GRAY + line);
  }

  private static void addInformationOldNBT(ItemStack tool, EntityPlayer player, List list,
      boolean advanced) {

    String line;
    line = LocalizationHelper.getMiscText("Tool.OldNBT1");
    list.add(EnumChatFormatting.DARK_BLUE + line);
    line = LocalizationHelper.getMiscText("Tool.OldNBT2");
    list.add(EnumChatFormatting.DARK_BLUE + line);
  }

  // ==========================================================================
  // Mining, using, repairing, etc
  // ==========================================================================

  /**
   * Determines if a tool can be repaired with the given material in an anvil. It's not the desired why to repair tools,
   * but it should be an option.
   * 
   * @return True if the material can repair the tool, false otherwise.
   */
  public static boolean getIsRepairable(ItemStack tool, ItemStack material) {

    int baseMaterial = getToolGemId(tool);
    boolean supercharged = getToolIsSupercharged(tool);
    ItemStack correctMaterial = null;

    if (baseMaterial < EnumGem.values().length) {
      // Gem tools.
      correctMaterial = new ItemStack(ModItems.gem, 1, baseMaterial | (supercharged ? 0x10 : 0));
    } else if (baseMaterial == ModMaterials.FLINT_GEM_ID) {
      // Flint tools.
      correctMaterial = new ItemStack(Items.flint);
    } else if (baseMaterial == ModMaterials.FISH_GEM_ID) {
      // Fish tools.
      correctMaterial = new ItemStack(Items.fish);
    }

    return correctMaterial != null && correctMaterial.getItem() == material.getItem()
        && correctMaterial.getItemDamage() == material.getItemDamage();
  }

  public static float getDigSpeed(ItemStack tool, float baseSpeed, IBlockState state,
      Material[] extraMaterials) {

    float speed = getAdjustedDigSpeed(tool, baseSpeed);

    if (tool.getItem().canHarvestBlock(state.getBlock())) {
      return speed;
    }

    for (String type : tool.getItem().getToolClasses(tool)) {
      if (state.getBlock().isToolEffective(type, state)) {
        return speed;
      }
    }

    if (extraMaterials != null) {
      for (Material material : extraMaterials) {
        if (state.getBlock().getMaterial() == material) {
          return speed;
        }
      }
    }

    return 1f;
  }

  /**
   * Gets the additional amount to add to the tool's max damage in getMaxDamage(ItemStack).
   * 
   * @return The amount to add to max damage.
   */
  public static int getDurabilityBoost(ItemStack tool) {

    int id = getToolHeadTip(tool);
    EnumTipUpgrade tip = EnumTipUpgrade.getById(id);
    return tip.getDurabilityBoost();
  }

  /**
   * Adjusts the mining level of the tool if it has a tip upgrade.
   * 
   * @param tool
   *          The tool.
   * @param baseLevel
   *          The value returned by ItemTool.getHarvestLevel. May be -1 if the toolclasses is incorrect for the block
   *          being mined.
   * @return The highest possible mining level, given the upgrades. Returns baseLevel if baseLevel is less than zero.
   */
  public static int getAdjustedMiningLevel(ItemStack tool, int baseLevel) {

    if (baseLevel < 0) {
      return baseLevel;
    }

    int id = getToolHeadTip(tool);
    EnumTipUpgrade tip = EnumTipUpgrade.getById(id);
    return Math.max(baseLevel, tip.getMiningLevel());
  }

  public static float getAdjustedDigSpeed(ItemStack tool, float baseSpeed) {

    float speed = baseSpeed;

    // Tip speed bonus.
    int tip = getToolHeadTip(tool);
    speed += EnumTipUpgrade.getById(tip).getSpeedBoost();

    // Wool grip bonus.
    if (getToolRodWool(tool) > -1) {
      speed += 0.5f;
    }

    return speed;
  }

  /**
   * This controls the block placing ability of mining tools.
   */
  public static boolean onItemUse(ItemStack stack, EntityPlayer player, World world, BlockPos pos,
      EnumFacing side, float hitX, float hitY, float hitZ) {

    if (!Config.RIGHT_CLICK_TO_PLACE_ENABLED) {
      return false;
    }
    if (Config.RIGHT_CLICK_TO_PLACE_ON_SNEAK_ONLY && !player.isSneaking()) {
      return false;
    }

    boolean used = false;
    int toolSlot = player.inventory.currentItem;
    int itemSlot = toolSlot + 1;
    ItemStack nextStack = null;
    ItemStack lastStack = player.inventory.getStackInSlot(8); // Slot 9 in hotbar

    if (toolSlot < 8) {
      // Get stack in slot after tool.
      nextStack = player.inventory.getStackInSlot(itemSlot);

      // If there's nothing there we can use, try slot 9 instead.
      if (nextStack == null || (!(nextStack.getItem() instanceof ItemBlock)
          && !(nextStack.getItem() instanceof IPlaceable))) {
        nextStack = lastStack;
        itemSlot = 8;
      }

      if (nextStack != null) {
        Item item = nextStack.getItem();
        if (item instanceof ItemBlock || item instanceof IPlaceable) {
          BlockPos targetPos = pos.offset(side);
          int playerX = (int) Math.floor(player.posX);
          int playerY = (int) Math.floor(player.posY);
          int playerZ = (int) Math.floor(player.posZ);

          // Check for block overlap with player, if necessary.
          if (item instanceof ItemBlock) {
            int px = targetPos.getX();
            int py = targetPos.getY();
            int pz = targetPos.getZ();
            AxisAlignedBB blockBounds = AxisAlignedBB.fromBounds(px, py, pz, px + 1, py + 1,
                pz + 1);
            AxisAlignedBB playerBounds = player.getEntityBoundingBox();
            Block block = ((ItemBlock) item).getBlock();
            if (block.getMaterial().blocksMovement() && playerBounds.intersectsWith(blockBounds)) {
              return false;
            }
          }

          used = item.onItemUse(nextStack, player, world, pos, side, hitX, hitY, hitZ);
          if (nextStack.stackSize < 1) {
            nextStack = null;
            player.inventory.setInventorySlotContents(itemSlot, null);
          }
        }
      }
    }

    if (used) {
      incrementStatBlocksPlaced(stack, 1);
    }

    return used;
  }

  /**
   * Called by mining tools if block breaking isn't canceled.
   * 
   * @return False in all cases, because this method is only called when Item.onBlockStartBreak returns false.
   */
  public static boolean onBlockStartBreak(ItemStack stack, BlockPos pos, EntityPlayer player) {

    final int x = pos.getX();
    final int y = pos.getY();
    final int z = pos.getZ();
    // Number of blocks broken.
    int amount = 1;
    // Try to activate Lumberjack or Area Miner enchantments.
    if (EnchantmentHelper.getEnchantmentLevel(ModEnchantments.LUMBERJACK_ID, stack) > 0) {
      amount += EnchantmentLumberjack.tryActivate(stack, x, y, z, player);
    } else if (EnchantmentHelper.getEnchantmentLevel(ModEnchantments.AOE_ID, stack) > 0) {
      amount += EnchantmentAOE.tryActivate(stack, x, y, z, player);
    }
    // Increase number of blocks mined statistic.
    incrementStatBlocksMined(stack, amount);

    // Mining achievements
    amount = getStatBlocksMined(stack);
    if (amount >= 1000) {
      player.addStat(GemsAchievement.mined1K, 1);
    }
    if (amount >= 10000) {
      player.addStat(GemsAchievement.mined10K, 1);
    }
    if (amount >= 100000) {
      player.addStat(GemsAchievement.mined100K, 1);
    }

    return false;
  }

  public static void hitEntity(ItemStack tool) {

    ToolHelper.incrementStatHitsLanded(tool, 1);
  }

  // ==========================================================================
  // NBT helper methods
  // ==========================================================================

  private static int getTagByte(String name, ItemStack tool) {

    // Create tag compound, if needed.
    if (!tool.hasTagCompound()) {
      tool.setTagCompound(new NBTTagCompound());
    }

    // Create root tag, if needed.
    if (!tool.getTagCompound().hasKey(NBT_ROOT)) {
      tool.getTagCompound().setTag(NBT_ROOT, new NBTTagCompound());
    }

    // Get the requested value.
    NBTTagCompound tags = (NBTTagCompound) tool.getTagCompound().getTag(NBT_ROOT);
    if (!tags.hasKey(name)) {
      return -1;
    }
    return tags.getByte(name);
  }

  private static void setTagByte(String name, int value, ItemStack tool) {

    // Create tag compound, if needed.
    if (!tool.hasTagCompound()) {
      tool.setTagCompound(new NBTTagCompound());
    }

    // Create root tag, if needed.
    if (!tool.getTagCompound().hasKey(NBT_ROOT)) {
      tool.getTagCompound().setTag(NBT_ROOT, new NBTTagCompound());
    }

    // Set the tag.
    NBTTagCompound tags = (NBTTagCompound) tool.getTagCompound().getTag(NBT_ROOT);
    tags.setByte(name, (byte) value);
  }

  private static int getTagInt(String name, ItemStack tool) {

    // Create tag compound, if needed.
    if (!tool.hasTagCompound()) {
      tool.setTagCompound(new NBTTagCompound());
    }

    // Create root tag, if needed.
    if (!tool.getTagCompound().hasKey(NBT_ROOT)) {
      tool.getTagCompound().setTag(NBT_ROOT, new NBTTagCompound());
    }

    // Get the requested value.
    NBTTagCompound tags = (NBTTagCompound) tool.getTagCompound().getTag(NBT_ROOT);
    if (!tags.hasKey(name)) {
      return 0; // NOTE: This is 0, where the byte version is -1!
    }
    return tags.getInteger(name);
  }

  private static void setTagInt(String name, int value, ItemStack tool) {

    // Create tag compound, if needed.
    if (!tool.hasTagCompound()) {
      tool.setTagCompound(new NBTTagCompound());
    }

    // Create root tag, if needed.
    if (!tool.getTagCompound().hasKey(NBT_ROOT)) {
      tool.getTagCompound().setTag(NBT_ROOT, new NBTTagCompound());
    }

    // Set the tag.
    NBTTagCompound tags = (NBTTagCompound) tool.getTagCompound().getTag(NBT_ROOT);
    tags.setInteger(name, value);
  }

  // ---------------
  // Tool design NBT
  // ---------------

  public static int getToolHeadLeft(ItemStack tool) {

    return getTagByte(NBT_HEAD_L, tool);
  }

  public static void setToolHeadLeft(ItemStack tool, int id) {

    setTagByte(NBT_HEAD_L, id, tool);
  }

  public static int getToolHeadMiddle(ItemStack tool) {

    return getTagByte(NBT_HEAD_M, tool);
  }

  public static void setToolHeadMiddle(ItemStack tool, int id) {

    setTagByte(NBT_HEAD_M, id, tool);
  }

  public static int getToolHeadRight(ItemStack tool) {

    return getTagByte(NBT_HEAD_R, tool);
  }

  public static void setToolHeadRight(ItemStack tool, int id) {

    setTagByte(NBT_HEAD_R, id, tool);
  }

  public static int getToolRodDeco(ItemStack tool) {

    return getTagByte(NBT_ROD_DECO, tool);
  }

  public static void setToolRodDeco(ItemStack tool, int id) {

    setTagByte(NBT_ROD_DECO, id, tool);
  }

  public static int getToolRodWool(ItemStack tool) {

    return getTagByte(NBT_ROD_WOOL, tool);
  }

  public static void setToolRodWool(ItemStack tool, int id) {

    setTagByte(NBT_ROD_WOOL, id, tool);
  }

  public static int getToolRod(ItemStack tool) {

    return getTagByte(NBT_ROD, tool);
  }

  public static void setToolRod(ItemStack tool, int id) {

    setTagByte(NBT_ROD, id, tool);
  }

  public static int getToolHeadTip(ItemStack tool) {

    return getTagByte(NBT_TIP, tool);
  }

  public static void setToolHeadTip(ItemStack tool, int id) {

    setTagByte(NBT_TIP, id, tool);
  }

  public static boolean getToolNoGlint(ItemStack tool) {

    return getTagByte(NBT_NO_GLINT, tool) > 0;
  }

  public static void setToolNoGlint(ItemStack tool, boolean value) {

    setTagByte(NBT_NO_GLINT, value ? 1 : 0, tool);
  }

  // --------------
  // Statistics NBT
  // --------------

  public static int getStatBlocksMined(ItemStack tool) {

    return getTagInt(NBT_STATS_BLOCKS_MINED, tool);
  }

  public static void incrementStatBlocksMined(ItemStack tool, int amount) {

    setTagInt(NBT_STATS_BLOCKS_MINED, getStatBlocksMined(tool) + amount, tool);
  }

  public static int getStatBlocksPlaced(ItemStack tool) {

    return getTagInt(NBT_STATS_BLOCKS_PLACED, tool);
  }

  public static void incrementStatBlocksPlaced(ItemStack tool, int amount) {

    setTagInt(NBT_STATS_BLOCKS_PLACED, getStatBlocksPlaced(tool) + amount, tool);
  }

  public static int getStatBlocksTilled(ItemStack tool) {

    return getTagInt(NBT_STATS_BLOCKS_TILLED, tool);
  }

  public static void incrementStatBlocksTilled(ItemStack tool, int amount) {

    setTagInt(NBT_STATS_BLOCKS_TILLED, getStatBlocksTilled(tool) + amount, tool);
  }

  public static int getStatHitsLanded(ItemStack tool) {

    return getTagInt(NBT_STATS_HITS, tool);
  }

  public static void incrementStatHitsLanded(ItemStack tool, int amount) {

    setTagInt(NBT_STATS_HITS, getStatHitsLanded(tool) + amount, tool);
  }

  public static int getStatRedecorated(ItemStack tool) {

    return getTagInt(NBT_STATS_REDECORATED, tool);
  }

  public static void incrementStatRedecorated(ItemStack tool, int amount) {

    setTagInt(NBT_STATS_REDECORATED, getStatRedecorated(tool) + amount, tool);
  }

  public static int getStatShotsFired(ItemStack tool) {

    return getTagInt(NBT_STATS_SHOTS_FIRED, tool);
  }

  public static void incrementStatShotsFired(ItemStack tool, int amount) {

    setTagInt(NBT_STATS_SHOTS_FIRED, getStatShotsFired(tool) + amount, tool);
  }

  // ---------------------
  // Rendering NBT methods
  // ---------------------

  public static int getAnimationFrame(ItemStack tool) {

    return getTagInt(NBT_ANIMATION_FRAME, tool);
  }

  public static void setAnimationFrame(ItemStack tool, int value) {

    setTagInt(NBT_ANIMATION_FRAME, value, tool);
  }

  // ---------------------
  // NBT converter methods
  // ---------------------

  private static int getOldTag(ItemStack tool, String name) {

    if (!tool.getTagCompound().hasKey(name)) {
      return -1;
    }
    return tool.getTagCompound().getByte(name);
  }

  private static void removeOldTag(ItemStack tool, String name) {

    tool.getTagCompound().removeTag(name);
  }

  public static boolean hasOldNBT(ItemStack tool) {

    return convertToNewNBT(tool.copy());
  }

  public static boolean convertToNewNBT(ItemStack tool) {

    if (tool == null || !tool.hasTagCompound() || !InventoryHelper.isGemTool(tool)) {
      return false;
    }

    boolean updated = false;

    int headL = getOldTag(tool, "HeadL");
    int headM = getOldTag(tool, "HeadM");
    int headR = getOldTag(tool, "HeadR");
    int rodDeco = getOldTag(tool, "Deco");
    int rodWool = getOldTag(tool, "Rod");
    int rod = getOldTag(tool, "Handle");
    int tip = getOldTag(tool, "Tip");

    if (headL > -1) {
      setToolHeadLeft(tool, headL);
      removeOldTag(tool, "HeadL");
      updated = true;
    }
    if (headM > -1) {
      setToolHeadMiddle(tool, headM);
      removeOldTag(tool, "HeadM");
      updated = true;
    }
    if (headR > -1) {
      setToolHeadRight(tool, headR);
      removeOldTag(tool, "HeadR");
      updated = true;
    }
    if (rodDeco > -1) {
      setToolRodDeco(tool, rodDeco);
      removeOldTag(tool, "Deco");
      updated = true;
    }
    if (rodWool > -1) {
      setToolRodWool(tool, rodWool);
      removeOldTag(tool, "Rod");
      updated = true;
    }
    if (rod > -1) {
      setToolRod(tool, rod);
      removeOldTag(tool, "Handle");
      updated = true;
    }
    if (tip > -1) {
      setToolHeadTip(tool, tip);
      removeOldTag(tool, "Tip");
      updated = true;
    }

    return updated;
  }
}
