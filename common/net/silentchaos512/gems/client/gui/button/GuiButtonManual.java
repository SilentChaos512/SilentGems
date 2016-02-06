package net.silentchaos512.gems.client.gui.button;

import net.minecraft.client.audio.SoundHandler;
import net.minecraft.client.gui.GuiButton;

public class GuiButtonManual extends GuiButton {

  public GuiButtonManual(int id, int posX, int posY, int width, int height, String str) {

    super(id, posX, posY, width, height, str);
  }

  @Override
  public void func_146113_a(SoundHandler soundHandler) {

    // soundHandler.playSound(PositionedSoundRecord.func_147674_a(new ResourceLocation("botania:lexiconPage"), 1.0F));
    super.func_146113_a(soundHandler);
  }
}
