package net.silentchaos512.gems.client.gui.config;

import net.minecraft.client.gui.GuiScreen;
import net.silentchaos512.lib.gui.config.GuiFactorySL;

public class GuiFactorySilentGems extends GuiFactorySL {

  @Override
  public Class<? extends GuiScreen> mainConfigGuiClass() {
    return GuiConfigSilentGems.class;
  }
}
