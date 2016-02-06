package net.silentchaos512.gems.client.gui;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

// https://github.com/Vazkii/Botania/blob/master/src/main/java/vazkii/botania/client/gui/lexicon/GuiLexicon.java

public class GuiGemsManual extends GuiScreen {

  public static GuiGemsManual currentOpenManual = new GuiGemsManual();
  public static ItemStack stackUsed;

  public static final ResourceLocation texture = new ResourceLocation("silentgems:textures/gui/manual.png");

  //List<LexiconCategory> allCategories;

  String title;
  int guiWidth = 146;
  int guiHeight = 180;
  int left, top;

  @Override
  public void initGui() {

    super.initGui();

    // TODO

    onInitGui();
  }

  public void onInitGui() {

    // TODO

    title = stackUsed.getDisplayName();
    currentOpenManual = this;

    left = width / 2 - guiWidth / 2;
    top = height / 2 - guiHeight / 2;

    buttonList.clear();

    // TODO
  }

  @Override
  public void drawScreen(int par1, int par2, float partialTicks) {

    // TODO

    GL11.glColor4f(1f, 1f, 1f, 1f);
    mc.renderEngine.bindTexture(texture);
    drawTexturedModalRect(left, top, 0, 0, guiWidth, guiHeight);
  }
}
