package net.silentchaos512.gems.api.lib;

import javax.annotation.Nullable;

public enum ArmorPartPosition implements IPartPosition {

  WEST(0), NORTH(1), EAST(2), SOUTH(3);

  final int renderPass;

  private ArmorPartPosition(int renderPass) {

    this.renderPass = renderPass;
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
}
