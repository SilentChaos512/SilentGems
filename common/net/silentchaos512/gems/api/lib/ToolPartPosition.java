package net.silentchaos512.gems.api.lib;

import javax.annotation.Nullable;

public enum ToolPartPosition implements IPartPosition {

  ROD(0, "PartRod"),
  HEAD(1, "Part%d"),
  TIP(2, "PartHeadTip"),
  ROD_DECO(3, "PartRodDeco"),
  ROD_GRIP(4, "PartRodWool");

  final int renderPass;
  final String nbtKey;

  private ToolPartPosition(int renderPass, String nbtKey) {

    this.renderPass = renderPass;
    this.nbtKey = nbtKey;
  }

  public static @Nullable ToolPartPosition forRenderPass(int pass) {

    for (ToolPartPosition pos : values()) {
      if (pos.renderPass == pass) {
        return pos;
      }
    }
    return null;
  }

  @Override
  public int getRenderPass() {

    return renderPass;
  }

  @Override
  public String getKey(int subPosition) {

    if (nbtKey.contains("%d"))
      return String.format(nbtKey, subPosition);
    return nbtKey;
  }
}
