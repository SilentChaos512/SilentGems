package net.silentchaos512.gems.api.lib;

import javax.annotation.Nullable;

// TODO: Rename to ToolPartPosition
public enum EnumPartPosition implements IPartPosition {

  ROD(0, "PartRod"),
  HEAD(1, "Part%d"),
  TIP(2, "PartHeadTip"),
  ROD_DECO(3, "PartRodDeco"),
  ROD_GRIP(4, "PartRodWool");

  final int renderPass;
  final String nbtKey; // TODO

  private EnumPartPosition(int renderPass, String nbtKey) {

    this.renderPass = renderPass;
    this.nbtKey = nbtKey;
  }

  public static @Nullable EnumPartPosition forRenderPass(int pass) {

    for (EnumPartPosition pos : values()) {
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
}
