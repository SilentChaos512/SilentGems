package net.silentchaos512.gems.event;

import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.BossInfo;
import net.minecraftforge.client.GuiIngameForge;
import net.minecraftforge.client.event.EntityViewRenderEvent.FOVModifier;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.silentchaos512.gems.SilentGems;
import net.silentchaos512.gems.api.IAmmoTool;
import net.silentchaos512.gems.api.ITool;
import net.silentchaos512.gems.api.lib.EnumMaterialGrade;
import net.silentchaos512.gems.api.lib.EnumMaterialTier;
import net.silentchaos512.gems.api.tool.part.ToolPart;
import net.silentchaos512.gems.api.tool.part.ToolPartMain;
import net.silentchaos512.gems.api.tool.part.ToolPartRegistry;
import net.silentchaos512.gems.api.tool.part.ToolPartRod;
import net.silentchaos512.gems.client.gui.GuiCrosshairs;
import net.silentchaos512.gems.client.handler.ClientTickHandler;
import net.silentchaos512.gems.client.key.KeyTracker;
import net.silentchaos512.gems.client.render.particle.ParticleRenderDispatcher;
import net.silentchaos512.gems.config.GemsConfig;
import net.silentchaos512.gems.init.ModItems;
import net.silentchaos512.gems.item.ItemChaosGem;
import net.silentchaos512.gems.lib.TooltipHelper;
import net.silentchaos512.gems.skills.SkillAreaMiner;
import net.silentchaos512.gems.skills.SkillLumberjack;
import net.silentchaos512.gems.skills.ToolSkill;
import net.silentchaos512.gems.util.ToolHelper;
import net.silentchaos512.lib.client.render.BufferBuilderSL;
import net.silentchaos512.lib.util.LocalizationHelper;
import net.silentchaos512.lib.util.StackHelper;

public class GemsClientEvents {

  public static String debugTextOverlay = "";

  LocalizationHelper loc = SilentGems.instance.localizationHelper;
  int fovModifier = 0;

  @SubscribeEvent
  public void onRenderGameOverlay(RenderGameOverlayEvent event) {

    if (SilentGems.instance.isDevBuild() && event.getType() == ElementType.TEXT) {
      int y = 5;
      for (String line : debugTextOverlay.split("\\n")) {
        Minecraft.getMinecraft().fontRenderer.drawStringWithShadow(line, 5, y, 0xFFFFFF);
        y += 10;
      }
    }

    ModItems.teleporterLinker.renderGameOverlay(event);
    if (event.getType() == ElementType.TEXT)
      ItemChaosGem.Gui.renderGameOverlay(Minecraft.getMinecraft());
    renderCrosshairs(event);
    renderArmorExtra(event);
    renderAmmoCount(event);
  }

  @SubscribeEvent
  public void onRenderWorldLast(RenderWorldLastEvent event) {

    ParticleRenderDispatcher.dispatch();
  }

  @SubscribeEvent
  public void onTooltip(ItemTooltipEvent event) {

    boolean ctrlDown = KeyTracker.isControlDown();
    boolean shiftDown = KeyTracker.isShiftDown();
    ItemStack stack = event.getItemStack();
    ToolPart part = StackHelper.isValid(stack) ? ToolPartRegistry.fromStack(stack) : null;

    if (part != null && !part.isBlacklisted(stack)) {
      if (part instanceof ToolPartRod) {
        onTooltipForToolRod(event, stack, part, ctrlDown, shiftDown);
      } else if (part instanceof ToolPartMain) {
        onTooltipForToolMaterial(event, stack, part, ctrlDown, shiftDown);
      }
    }
  }

  private void onTooltipForToolRod(ItemTooltipEvent event, ItemStack stack, ToolPart part, boolean ctrlDown, boolean shiftDown) {

    int index = 1;

    // Tool Rod indicator
    event.getToolTip().add(index++, loc.getMiscText("ToolPart.Rod"));

    if (ctrlDown) {
      // Compatible tiers
      String line = "";
      for (EnumMaterialTier tier : part.getCompatibleTiers()) {
        if (!line.isEmpty())
          line += ", ";
        line += tier.getLocalizedName();
      }
      event.getToolTip().add(index++, loc.getMiscText("ToolPart.ValidTiers"));
      event.getToolTip().add(index++, "  " + line);

      // Debug info
      if (shiftDown) {
        event.getToolTip().add(index++, TextFormatting.DARK_GRAY + "* Part key: " + part.getKey());
      }
    } else {
      event.getToolTip().add(index++, loc.getMiscText("PressCtrl"));
    }
  }

  private void onTooltipForToolMaterial(ItemTooltipEvent event, ItemStack stack, ToolPart part, boolean ctrlDown, boolean shiftDown) {

    int index = 1;
    final String sep = loc.getMiscText("Tooltip.Separator");
    List<String> list = event.getToolTip();

    // Material grade
    EnumMaterialGrade grade = EnumMaterialGrade.fromStack(stack);
    if (grade != EnumMaterialGrade.NONE) {
      list.add(index++, loc.getMiscText("ToolPart.Grade", grade.getLocalizedName()));
    }

    // Material tier
    EnumMaterialTier tier = part.getTier();
    list.add(index++, loc.getMiscText("ToolPart.Tier", tier.getLocalizedName()));

    // Show stats?
    if (ctrlDown) {
      int multi = 100 + EnumMaterialGrade.fromStack(stack).bonusPercent;

      //@formatter:off

      list.add(index++, sep);
      TextFormatting color = TextFormatting.GOLD;
      list.add(index++, color + TooltipHelper.get("HarvestSpeed", part.getHarvestSpeed() * multi / 100));
      list.add(index++, color + TooltipHelper.get("HarvestLevel", part.getHarvestLevel() * multi / 100));
      list.add(index++, sep);
      
      color = TextFormatting.DARK_GREEN;
      list.add(index++, color + TooltipHelper.get("MeleeSpeed", (int) (part.getMeleeSpeed() * multi)));
      list.add(index++, color + TooltipHelper.get("MeleeDamage", part.getMeleeDamage() * multi / 100));
      list.add(index++, color + TooltipHelper.get("MagicDamage", part.getMagicDamage() * multi / 100));
      
      list.add(index++, color + TooltipHelper.get("Protection", part.getProtection() * multi / 100));
      list.add(index++, sep);
      
      color = TextFormatting.BLUE;
      list.add(index++, color + TooltipHelper.get("Durability", part.getDurability() * multi / 100));
      list.add(index++, color + TooltipHelper.get("ChargeSpeed", part.getChargeSpeed() * multi / 100));
      list.add(index++, color + TooltipHelper.get("Enchantability", part.getEnchantability() * multi / 100));
      list.add(index++, sep);

   // Debug info
      if (shiftDown) {
        event.getToolTip().add(index++, TextFormatting.DARK_GRAY + "* Part key: " + part.getKey());
      }
      //@formatter:on
    } else {
      list.add(index++, TextFormatting.GOLD + loc.getMiscText("PressCtrl"));
    }
  }

  private void renderCrosshairs(RenderGameOverlayEvent event) {

    EntityPlayer player = Minecraft.getMinecraft().player;
    ItemStack mainHand = player.getHeldItem(EnumHand.MAIN_HAND);

    if (mainHand == null) {
      return;
    }

    Item item = mainHand.getItem();
    boolean isDigger = item instanceof ITool && ((ITool) item).isDiggingTool();

    if (isDigger && event.isCancelable() && event.getType() == ElementType.CROSSHAIRS) {
      if (ToolHelper.isSpecialAbilityEnabled(mainHand)) {
        event.setCanceled(true);
        ToolSkill skill = ToolHelper.getSuperSkill(mainHand);
        int type = skill == SkillAreaMiner.INSTANCE ? 0 : skill == SkillLumberjack.INSTANCE ? 1 : 2;
        GuiCrosshairs.INSTANCE.renderOverlay(event, type, skill);
      }
    }

    // if (event.isCancelable() || event.getType() != ElementType.CROSSHAIRS) {
    // return;
    // }
  }

  /**
   * Draws extra (yellow) armor pieces over the normal bar, if player armor is above 20.
   * 
   * @param event
   */
  private void renderArmorExtra(RenderGameOverlayEvent event) {

    if (!GemsConfig.SHOW_BONUS_ARMOR_BAR || !event.isCancelable() || event.getType() != ElementType.ARMOR)
      return;

    int width = event.getResolution().getScaledWidth();
    int height = event.getResolution().getScaledHeight();

    GlStateManager.enableBlend();
    GlStateManager.color(1.0f, 1.0f, 0.3f);
    int left = width / 2 - 91;
    int top = height - GuiIngameForge.left_height;

    int level = ForgeHooks.getTotalArmorValue(Minecraft.getMinecraft().player) - 20;
    for (int i = 1; level > 0 && i < 20; i += 2) {
      if (i < level) {
        drawTexturedModalRect(left, top, 34, 9, 9, 9);
      } else if (i == level) {
        drawTexturedModalRect(left, top, 25, 9, 5, 9);
      }
      left += 8;
    }

    GlStateManager.disableBlend();
    GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
  }

  private void renderAmmoCount(RenderGameOverlayEvent event) {

    if (event.getType() != ElementType.TEXT)
      return;

    int width = event.getResolution().getScaledWidth();
    int height = event.getResolution().getScaledHeight();
    FontRenderer fontRender = Minecraft.getMinecraft().fontRenderer;

    EntityPlayer player = Minecraft.getMinecraft().player;
    ItemStack right = player.getHeldItemMainhand();
    ItemStack left = player.getHeldItemOffhand();

    doAmmoCountWithOffset(left, width, height, -7, 3);
    doAmmoCountWithOffset(right, width, height, 5, 3);
  }

  private void doAmmoCountWithOffset(ItemStack tool, int width, int height, int xOffset, int yOffset) {

    if (tool != null && tool.getItem() instanceof IAmmoTool) {
      FontRenderer fontRender = Minecraft.getMinecraft().fontRenderer;

      IAmmoTool ammo = (IAmmoTool) tool.getItem();
      int amount = ammo.getAmmo(tool);
      String str = "" + amount;

      int stringWidth = fontRender.getStringWidth(str);
      float scale = 0.7f;
      int posX = (int) ((width / 2 + xOffset) / scale);
      int posY = (int) ((height / 2 + yOffset) / scale);

      GlStateManager.pushMatrix();
      GlStateManager.scale(scale, scale, 1f);
      fontRender.drawString(str, posX, posY, amount > 0 ? 0xFFFFFF : 0xFF0000);
      GlStateManager.popMatrix();
    }
  }

  public static void drawTexturedModalRect(int x, int y, int textureX, int textureY, int width, int height) {

    float f = 0.00390625F;
    float f1 = 0.00390625F;
    Tessellator tessellator = Tessellator.getInstance();
    BufferBuilderSL vertexbuffer = BufferBuilderSL.INSTANCE.acquireBuffer(tessellator);
    vertexbuffer.begin(7, DefaultVertexFormats.POSITION_TEX);
    vertexbuffer.pos(x + 0, y + height, 0).tex((textureX + 0) * f, (textureY + height) * f1).endVertex();
    vertexbuffer.pos(x + width, y + height, 0).tex((textureX + width) * f, (textureY + height) * f1).endVertex();
    vertexbuffer.pos(x + width, y + 0, 0).tex((textureX + width) * f, (textureY + 0) * f1).endVertex();
    vertexbuffer.pos(x + 0, y + 0, 0).tex((textureX + 0) * f, (textureY + 0) * f1).endVertex();
    tessellator.draw();
  }

  @SubscribeEvent
  public void onFOVModifier(FOVModifier event) {

    event.setFOV(event.getFOV() - ClientTickHandler.fovModifier);
  }

  @SubscribeEvent
  public void stitchTexture(TextureStitchEvent.Pre pre) {

    SilentGems.instance.logHelper.info("Stitching misc textures into the map - M4thG33k");
    pre.getMap().registerSprite(new ResourceLocation("silentgems", "blocks/ChaosPylonPassive"));
    pre.getMap().registerSprite(new ResourceLocation("silentgems", "blocks/ChaosPylonBurner"));
    pre.getMap().registerSprite(new ResourceLocation("silentgems", "blocks/ChaosAltar"));
  }

  // @SubscribeEvent
  // public void onWitBlockInfo(WitBlockInfoEvent event) {
  //
  // event.lines.add("Testing from Gems");
  // event.lines.add(event.tileEntity == null ? "null" : event.tileEntity.toString());
  // }
}
