package net.silentchaos512.gems.client.gui.config;

import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.fml.client.config.GuiConfig;
import net.silentchaos512.gems.SilentGems;
import net.silentchaos512.gems.config.GemsConfig;

public class GuiConfigSilentGems extends GuiConfig {

  public GuiConfigSilentGems(GuiScreen parent) {
    super(parent, GemsConfig.INSTANCE.getConfigElements(), SilentGems.MOD_ID, false, false,
        "Silent's Gems Config");
  }
}
