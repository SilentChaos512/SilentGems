package net.silentchaos512.gems.api.lib;

import javax.annotation.Nullable;

public enum ArmorPartPosition implements IPartPosition {

  WEST(0, "Part0"), NORTH(1, "Part1"), EAST(2, "Part2"), SOUTH(3, "Part3");

  final int renderPass;
  final String nbtKey;

  private ArmorPartPosition(int renderPass, String nbtKey) {

    this.renderPass = renderPass;
    this.nbtKey = nbtKey;
  }

  public static @Nullable ArmorPartPosition forRenderPass(int pass) {

    for (ArmorPartPosition pos : values()) {
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
