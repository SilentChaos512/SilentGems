package net.silentchaos512.gems.api.lib;

public enum EnumPartPosition {

  ROD(0),
  HEAD(1),
  TIP(2),
  ROD_DECO(3),
  ROD_GRIP(4);

  public final int renderPass;

  private EnumPartPosition(int renderPass) {

    this.renderPass = renderPass;
  }
}
