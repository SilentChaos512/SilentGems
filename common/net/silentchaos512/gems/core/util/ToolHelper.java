package net.silentchaos512.gems.core.util;

import java.util.List;

import org.lwjgl.input.Keyboard;

import net.minecraft.block.Block;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import net.silentchaos512.gems.SilentGems;
import net.silentchaos512.gems.api.IPlaceable;
import net.silentchaos512.gems.client.renderers.tool.ToolRenderHelper;
import net.silentchaos512.gems.configuration.Config;
import net.silentchaos512.gems.core.registry.SRegistry;
import net.silentchaos512.gems.enchantment.EnchantmentAOE;
import net.silentchaos512.gems.enchantment.ModEnchantments;
import net.silentchaos512.gems.item.Gem;
import net.silentchaos512.gems.item.ModItems;
import net.silentchaos512.gems.item.tool.GemAxe;
import net.silentchaos512.gems.item.tool.GemHoe;
import net.silentchaos512.gems.item.tool.GemPickaxe;
import net.silentchaos512.gems.item.tool.GemShovel;
import net.silentchaos512.gems.item.tool.GemSickle;
import net.silentchaos512.gems.item.tool.GemSword;
import net.silentchaos512.gems.lib.EnumGem;
import net.silentchaos512.gems.lib.Names;
import net.silentchaos512.gems.material.ModMaterials;

/**
 * The purpose of this class is to have shared code for tools in one place, to make updating/expanding the mod easier.
 */
public class ToolHelper {

  /*
   * NBT constants
   */

  public static final String NBT_ROOT_TOOL = SilentGems.MOD_ID + "Tool";
  public static final String NBT_HEAD_L = "HeadL";
  public static final String NBT_HEAD_M = "HeadM";
  public static final String NBT_HEAD_R = "HeadR";
  public static final String NBT_ROD = "Rod";
  public static final String NBT_ROD_DECO = "RodDeco";
  public static final String NBT_ROD_WOOL = "RodWool";
  public static final String NBT_TIP = "Tip";

  public static final String NBT_ROOT_STATS = SilentGems.MOD_ID + "Stats";
  public static final String NBT_STATS_MINED = "BlocksMined";
  public static final String NBT_STATS_HITS = "HitsLanded";
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
    } else if (item == Items.flint) {
      return ModMaterials.FLINT_GEM_ID;
    } else if (item == Items.fish) {
      return ModMaterials.FISH_GEM_ID;
    } else {
      return -1;
    }
  }

  // ==========================================================================
  // Tooltip helpers
  // ==========================================================================

  public static void addInformation(ItemStack tool, EntityPlayer player, List list,
      boolean advanced) {

    String line;

    // Old NBT warning.
    if (hasOldNBT(tool)) {
      line = LocalizationHelper.getMiscText("Tool.OldNBT1");
      list.add(EnumChatFormatting.DARK_BLUE + line);
      line = LocalizationHelper.getMiscText("Tool.OldNBT2");
      list.add(EnumChatFormatting.DARK_BLUE + line);
      return;
    }

    // Tipped upgrade
    int tip = getToolHeadTip(tool);
    if (tip == 1) {
      line = LocalizationHelper.getMiscText("Tool.IronTipped");
      list.add(EnumChatFormatting.WHITE + line);
    } else if (tip == 2) {
      line = LocalizationHelper.getMiscText("Tool.DiamondTipped");
      list.add(EnumChatFormatting.AQUA + line);
    }

    // Statistics test
    boolean keyControl = Keyboard.isKeyDown(Keyboard.KEY_LCONTROL)
        || Keyboard.isKeyDown(Keyboard.KEY_RCONTROL);
    int amount;

    if (keyControl) {
      String separator = EnumChatFormatting.DARK_GRAY
          + LocalizationHelper.getMiscText("Tool.Stats.Separator");
      // Header
      line = LocalizationHelper.getMiscText("Tool.Stats.Header");
      list.add(EnumChatFormatting.YELLOW + line);
      list.add(separator);

      // Blocks mined
      amount = getStatBlocksMined(tool);
      line = LocalizationHelper.getMiscText("Tool.Stats.Mined");
      line = String.format(line, amount);
      list.add(line);
      
      // Hits landed
      amount = getStatHitsLanded(tool);
      line = LocalizationHelper.getMiscText("Tool.Stats.Hits");
      line = String.format(line, amount);
      list.add(line);

      // Redecorated count
      amount = getStatRedecorated(tool);
      line = LocalizationHelper.getMiscText("Tool.Stats.Redecorated");
      line = String.format(line, amount);
      list.add(line);

      list.add(separator);
    } else {
      line = LocalizationHelper.getMiscText("PressCtrl");
      list.add(EnumChatFormatting.YELLOW + line);
    }
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

  /**
   * Gets the additional amount to add to the tool's max damage in getMaxDamage(ItemStack).
   * 
   * @return The amount to add to max damage.
   */
  public static int getDurabilityBoost(ItemStack tool) {

    int tip = getToolHeadTip(tool);
    switch (tip) {
      case 2:
        return Config.DURABILITY_BOOST_DIAMOND_TIP;
      case 1:
        return Config.DURABILITY_BOOST_IRON_TIP;
      default:
        return 0;
    }
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

    int tip = getToolHeadTip(tool);
    switch (tip) {
      case 2:
        return Math.max(baseLevel, Config.MINING_LEVEL_DIAMOND_TIP);
      case 1:
        return Math.max(baseLevel, Config.MINING_LEVEL_IRON_TIP);
      default:
        return baseLevel;
    }
  }

  /**
   * This controls the block placing ability of mining tools.
   */
  public static boolean onItemUse(ItemStack stack, EntityPlayer player, World world, int x, int y,
      int z, int side, float hitX, float hitY, float hitZ) {

    boolean used = false;
    int toolSlot = player.inventory.currentItem;
    int itemSlot = toolSlot + 1;
    ItemStack nextStack = null;

    if (toolSlot < 8) {
      nextStack = player.inventory.getStackInSlot(itemSlot);
      if (nextStack != null) {
        Item item = nextStack.getItem();
        if (item instanceof ItemBlock || item instanceof IPlaceable) {
          ForgeDirection d = ForgeDirection.VALID_DIRECTIONS[side];

          int px = x + d.offsetX;
          int py = y + d.offsetY;
          int pz = z + d.offsetZ;
          int playerX = (int) Math.floor(player.posX);
          int playerY = (int) Math.floor(player.posY);
          int playerZ = (int) Math.floor(player.posZ);

          // Check for overlap with player, except for torches and torch bandolier
          if (Item.getIdFromItem(item) != Block.getIdFromBlock(Blocks.torch)
              && item != SRegistry.getItem(Names.TORCH_BANDOLIER) && px == playerX
              && (py == playerY || py == playerY + 1 || py == playerY - 1) && pz == playerZ) {
            return false;
          }

          used = item.onItemUse(nextStack, player, world, x, y, z, side, hitX, hitY, hitZ);
          if (nextStack.stackSize < 1) {
            nextStack = null;
            player.inventory.setInventorySlotContents(itemSlot, null);
          }
        }
      }
    }

    return used;
  }

  /**
   * Called by mining tools if block breaking isn't canceled.
   * 
   * @return False in all cases, because this method is only called when Item.onBlockStartBreak returns false.
   */
  public static boolean onBlockStartBreak(ItemStack stack, int x, int y, int z,
      EntityPlayer player) {

    // Number of blocks broken.
    int amount = 1;
    // Try to activate Area Miner enchantment.
    if (EnchantmentHelper.getEnchantmentLevel(ModEnchantments.AOE_ID, stack) > 0) {
      amount += EnchantmentAOE.tryActivate(stack, x, y, z, player);
    }
    // Increase number of blocks mined statistic.
    ToolHelper.incrementStatBlocksMined(stack, amount);

    return false;
  }

  public static void hitEntity(ItemStack tool) {
    
    ToolHelper.incrementStatHitsLanded(tool, 1);
  }

  // ==========================================================================
  // Rendering
  // ==========================================================================

  public static boolean hasEffect(ItemStack tool, int pass) {

    return tool.isItemEnchanted() && pass == ToolRenderHelper.RENDER_PASS_COUNT - 1;
  }

  // ==========================================================================
  // NBT helper methods
  // ==========================================================================

  private static int getTagByte(String name, ItemStack tool) {

    // Create tag compound, if needed.
    if (tool.stackTagCompound == null) {
      tool.setTagCompound(new NBTTagCompound());
    }

    // Create root tag, if needed.
    if (!tool.stackTagCompound.hasKey(NBT_ROOT_TOOL)) {
      tool.stackTagCompound.setTag(NBT_ROOT_TOOL, new NBTTagCompound());
    }

    // Get the requested value.
    NBTTagCompound tags = (NBTTagCompound) tool.stackTagCompound.getTag(NBT_ROOT_TOOL);
    if (!tags.hasKey(name)) {
      return -1;
    }
    return tags.getByte(name);
  }

  private static void setTagByte(String name, int value, ItemStack tool) {

    // Create tag compound, if needed.
    if (tool.stackTagCompound == null) {
      tool.setTagCompound(new NBTTagCompound());
    }

    // Create root tag, if needed.
    if (!tool.stackTagCompound.hasKey(NBT_ROOT_TOOL)) {
      tool.stackTagCompound.setTag(NBT_ROOT_TOOL, new NBTTagCompound());
    }

    // Set the tag.
    NBTTagCompound tags = (NBTTagCompound) tool.stackTagCompound.getTag(NBT_ROOT_TOOL);
    tags.setByte(name, (byte) value);
  }

  private static int getTagInt(String name, ItemStack tool) {

    // Create tag compound, if needed.
    if (tool.stackTagCompound == null) {
      tool.setTagCompound(new NBTTagCompound());
    }

    // Create root tag, if needed.
    if (!tool.stackTagCompound.hasKey(NBT_ROOT_TOOL)) {
      tool.stackTagCompound.setTag(NBT_ROOT_TOOL, new NBTTagCompound());
    }

    // Get the requested value.
    NBTTagCompound tags = (NBTTagCompound) tool.stackTagCompound.getTag(NBT_ROOT_TOOL);
    if (!tags.hasKey(name)) {
      return 0; // NOTE: This is 0, where the byte version is -1!
    }
    return tags.getInteger(name);
  }

  private static void setTagInt(String name, int value, ItemStack tool) {

    // Create tag compound, if needed.
    if (tool.stackTagCompound == null) {
      tool.setTagCompound(new NBTTagCompound());
    }

    // Create root tag, if needed.
    if (!tool.stackTagCompound.hasKey(NBT_ROOT_TOOL)) {
      tool.stackTagCompound.setTag(NBT_ROOT_TOOL, new NBTTagCompound());
    }

    // Set the tag.
    NBTTagCompound tags = (NBTTagCompound) tool.stackTagCompound.getTag(NBT_ROOT_TOOL);
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

  public static int getToolRod(ItemStack tool, int id) {

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

  // --------------
  // Statistics NBT
  // --------------

  public static int getStatBlocksMined(ItemStack tool) {

    return getTagInt(NBT_STATS_MINED, tool);
  }

  public static void incrementStatBlocksMined(ItemStack tool, int amount) {

    setTagInt(NBT_STATS_MINED, getStatBlocksMined(tool) + amount, tool);
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

  // ---------------------
  // NBT converter methods
  // ---------------------

  private static int getOldTag(ItemStack tool, String name) {

    if (!tool.stackTagCompound.hasKey(name)) {
      return -1;
    }
    return tool.stackTagCompound.getByte(name);
  }

  private static void removeOldTag(ItemStack tool, String name) {

    tool.stackTagCompound.removeTag(name);
  }

  public static boolean hasOldNBT(ItemStack tool) {

    return convertToNewNBT(tool.copy());
  }

  public static boolean convertToNewNBT(ItemStack tool) {

    if (tool == null || tool.stackTagCompound == null || !InventoryHelper.isGemTool(tool)) {
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
