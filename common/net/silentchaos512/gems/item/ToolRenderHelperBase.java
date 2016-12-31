package net.silentchaos512.gems.item;

import net.minecraft.item.ItemStack;
import net.silentchaos512.gems.SilentGems;
import net.silentchaos512.gems.lib.Names;
import net.silentchaos512.lib.item.ItemSL;
import net.silentchaos512.lib.util.LogHelper;

public class ToolRenderHelperBase extends ItemSL {

  protected static LogHelper log;

  public static ToolRenderHelperBase instance;

  public static void init() {

    if (instance == null) {
      instance = ModItems.toolRenderHelper;
      log = SilentGems.instance.logHelper;
    }
  }

  public ToolRenderHelperBase() {

    super(1, SilentGems.MODID, Names.TOOL_RENDER_HELPER);
  }

  /*
   * Constants
   */

  public static final int BOW_STAGE_COUNT = 4;

  // Render pass IDs and count
  public static final int PASS_ROD = 0;
  public static final int PASS_HEAD_M = 1;
  public static final int PASS_HEAD_L = 2;
  public static final int PASS_HEAD_R = 3;
  public static final int PASS_ROD_DECO = 4;
  public static final int PASS_ROD_WOOL = 5;
  public static final int PASS_TIP = 6;
  public static final int RENDER_PASS_COUNT = 7;

  public boolean hasEffect(ItemStack tool) {

    // return tool.isItemEnchanted();
    return false; // TODO: If anyone can fix this, please do so!
  }

  public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack,
      boolean slotChanged) {

    return slotChanged || !oldStack.isItemEqual(newStack)
        || (oldStack.hasTagCompound() && newStack.hasTagCompound()
            && !oldStack.getTagCompound().equals(newStack.getTagCompound()));
  }
}
