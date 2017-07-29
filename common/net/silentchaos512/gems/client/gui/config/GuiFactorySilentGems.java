package net.silentchaos512.gems.client.gui.config;

import java.util.Set;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.client.IModGuiFactory;
import net.silentchaos512.gems.SilentGems;
import net.silentchaos512.lib.gui.config.GuiFactorySL;

public class GuiFactorySilentGems extends GuiFactorySL {

  @Override
  public Class<? extends GuiScreen> mainConfigGuiClass() {

    return GuiConfigSilentGems.class;
  }

  @Override
  public RuntimeOptionGuiHandler getHandlerFor(RuntimeOptionCategoryElement element) {

    // TODO Auto-generated method stub
    return null;
  }
}
