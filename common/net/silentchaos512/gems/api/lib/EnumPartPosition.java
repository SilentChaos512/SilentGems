package net.silentchaos512.gems.api.lib;

public enum EnumPartPosition {

  ROD(0),
  HEAD_MIDDLE(1),
  HEAD_LEFT(2),
  HEAD_RIGHT(3),
  ROD_GRIP(4),
  ROD_DECO(5),
  TIP(6);

  public final int renderPass;

  private EnumPartPosition(int renderPass) {

    this.renderPass = renderPass;
  }
}
