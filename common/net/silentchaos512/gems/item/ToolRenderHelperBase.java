package net.silentchaos512.gems.item;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.silentchaos512.gems.SilentGems;
import net.silentchaos512.gems.core.util.LogHelper;
import net.silentchaos512.gems.core.util.ToolHelper;
import net.silentchaos512.gems.item.tool.GemAxe;
import net.silentchaos512.gems.item.tool.GemBow;
import net.silentchaos512.gems.item.tool.GemHoe;
import net.silentchaos512.gems.item.tool.GemPickaxe;
import net.silentchaos512.gems.item.tool.GemShovel;
import net.silentchaos512.gems.item.tool.GemSickle;
import net.silentchaos512.gems.item.tool.GemSword;
import net.silentchaos512.gems.lib.EnumGem;
import net.silentchaos512.gems.material.ModMaterials;

public class ToolRenderHelperBase extends Item {

  /**
   * The instanceof of the item, for easy retrieval.
   */
  public static ToolRenderHelperBase instance;

  /**
   * Sets instance.
   */
  public static void init() {

    if (instance == null) {
      instance = ModItems.toolRenderHelper;
    }
  }

  /*
   * Constants
   */

  public static final String BROKEN_SMART_MODEL_NAME = SilentGems.MOD_ID + ":SmartToolBroken";
  public static final String SMART_MODEL_NAME = SilentGems.MOD_ID + ":SmartTool";

  // The number of head types
  public static final int HEAD_TYPE_COUNT = 15;
  // The number of rod types
  public static final int ROD_TYPE_COUNT = 2;
  // The number of bow stages
  public static final int BOW_STAGE_COUNT = 4;
  // The number of rod gem decorations
  public static final int ROD_DECO_TYPE_COUNT = 15;
  // The number of wool grip types (shouldn't change)
  public static final int ROD_WOOL_TYPE_COUNT = 16;
  // The number of mining tool tips.
  public static final int TIP_TYPE_COUNT = 3;

  // Render pass IDs and count
  public static final int PASS_ROD = 0;
  public static final int PASS_HEAD_M = 1;
  public static final int PASS_HEAD_L = 2;
  public static final int PASS_HEAD_R = 3;
  public static final int PASS_ROD_DECO = 4;
  public static final int PASS_ROD_WOOL = 5;
  public static final int PASS_TIP = 6;
  public static final int RENDER_PASS_COUNT = 7;

  /**
   * Gets the base name of the tool.
   * 
   * @param tool
   * @return
   */
  public String getName(ItemStack tool) {

    Item item = tool.getItem();
    String toolClass;
    String toolType;

    // Get tool class
    if (item instanceof GemSword) {
      toolClass = "Sword";
    } else if (item instanceof GemPickaxe) {
      toolClass = "Pickaxe";
    } else if (item instanceof GemShovel) {
      toolClass = "Shovel";
    } else if (item instanceof GemAxe) {
      toolClass = "Axe";
    } else if (item instanceof GemHoe) {
      toolClass = "Hoe";
    } else if (item instanceof GemSickle) {
      toolClass = "Sickle";
    } else if (item instanceof GemBow) {
      toolClass = "Bow";
    } else {
      LogHelper.debug("ToolRenderHelper.getName: Unknown tool class! " + tool.toString());
      toolClass = "Unknown";
    }

    // Get "material"
    int gemId = ToolHelper.getToolGemId(tool);
    switch (gemId) {
      case ModMaterials.CHAOS_GEM_ID:
        toolType = "Chaos";
        break;
      case ModMaterials.FLINT_GEM_ID:
        toolType = "Flint";
        break;
      case ModMaterials.FISH_GEM_ID:
        toolType = "Fish";
        break;
      default:
        toolType = Integer.toString(gemId);
        break;
    }

    boolean supercharged = ToolHelper.getToolIsSupercharged(tool);
    return toolClass + toolType + (supercharged && gemId < EnumGem.values().length ? "Plus" : "");
  }

  /**
   * Gets the full unlocalized name of the tool.
   * 
   * @param tool
   * @return
   */
  public String getFullName(ItemStack tool) {

    return SilentGems.MOD_ID + ":" + getName(tool);
  }

  /**
   * Gets the variant names for the tool.
   * 
   * @param tool
   * @return
   */
  public String[] getVariantNames(ItemStack tool) {

    // List<String> list = Lists.newArrayList();
    // for (int i = 0; i < getAnimationFrameCount(tool); ++i) {
    // list.add(SMART_MODEL_NAME + i);
    // }
    // return list.toArray(new String[list.size()]);
    return new String[] { SMART_MODEL_NAME + "0" };
  }

  public int getAnimationFrameCount(ItemStack tool) {

    return tool.getItem() instanceof GemBow ? BOW_STAGE_COUNT : 1;
  }

  /**
   * Determines whether or not to use the enchanted glow.
   */
  public boolean hasEffect(ItemStack tool) {

    // FIXME: The effect is insanely powerful! Why?
    return tool.isItemEnchanted() && !ToolHelper.getToolNoGlint(tool);
  }

  /**
   * Prevents the bobbing caused by tools updating their damage or NBT.
   */
  public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack,
      boolean slotChanged) {

    return slotChanged;
  }

  /**
   * Attempts to get the number of possible permutations for a single tool type. I believe this is correct, but not
   * sure.
   * 
   * @return
   */
  public int getPossibleToolCombinations() {

    return HEAD_TYPE_COUNT * HEAD_TYPE_COUNT * HEAD_TYPE_COUNT * ROD_TYPE_COUNT
        * ROD_DECO_TYPE_COUNT * (ROD_WOOL_TYPE_COUNT + 1) * (TIP_TYPE_COUNT + 1);
  }
}
