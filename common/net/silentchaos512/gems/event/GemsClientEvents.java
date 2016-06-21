package net.silentchaos512.gems.event;

import java.util.List;

import org.lwjgl.input.Keyboard;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.profiler.Profiler;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;
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
import net.silentchaos512.gems.api.ITool;
import net.silentchaos512.gems.api.lib.EnumMaterialGrade;
import net.silentchaos512.gems.api.lib.EnumMaterialTier;
import net.silentchaos512.gems.api.tool.part.ToolPart;
import net.silentchaos512.gems.api.tool.part.ToolPartMain;
import net.silentchaos512.gems.api.tool.part.ToolPartRegistry;
import net.silentchaos512.gems.api.tool.part.ToolPartRod;
import net.silentchaos512.gems.client.fx.ParticleRenderDispatcher;
import net.silentchaos512.gems.client.gui.GuiCrosshairs;
import net.silentchaos512.gems.client.handler.ClientTickHandler;
import net.silentchaos512.gems.config.Config;
import net.silentchaos512.gems.item.ModItems;
import net.silentchaos512.gems.lib.TooltipHelper;
import net.silentchaos512.gems.util.ToolHelper;
import net.silentchaos512.lib.util.LocalizationHelper;

public class GemsClientEvents {

  LocalizationHelper loc = SilentGems.instance.localizationHelper;
  int fovModifier = 0;

  @SubscribeEvent
  public void onRenderGameOverlay(RenderGameOverlayEvent event) {

    ModItems.teleporterLinker.renderGameOverlay(event);
    renderCrosshairs(event);
    renderArmorExtra(event);
  }

  @SubscribeEvent
  public void onRenderWorldLast(RenderWorldLastEvent event) {

    Profiler profiler = Minecraft.getMinecraft().mcProfiler;

    profiler.startSection("silentgems-particles");
    ParticleRenderDispatcher.dispatch();
    profiler.endStartSection("sg-renderworldlast");
  }

  @SubscribeEvent
  public void onTooltip(ItemTooltipEvent event) {

    boolean modifierKey = Keyboard.isKeyDown(Keyboard.KEY_LCONTROL)
        || Keyboard.isKeyDown(Keyboard.KEY_RCONTROL);
    ItemStack stack = event.getItemStack();
    ToolPart part = stack != null ? ToolPartRegistry.fromStack(stack) : null;

    if (part != null) {
      if (part instanceof ToolPartRod) {
        onTooltipForToolRod(event, stack, part, modifierKey);
      } else if (part instanceof ToolPartMain) {
        onTooltipForToolMaterial(event, stack, part, modifierKey);
      }
    }
  }

  private void onTooltipForToolRod(ItemTooltipEvent event, ItemStack stack, ToolPart part,
      boolean modifierKey) {

    int index = 1;

    // Tool Rod indicator
    event.getToolTip().add(index++, loc.getMiscText("ToolPart.Rod"));

    if (modifierKey) {
      // Compatible tiers
      String line = "";
      for (EnumMaterialTier tier : part.getCompatibleTiers()) {
        if (!line.isEmpty())
          line += ", ";
        line += tier.getLocalizedName();
      }
      event.getToolTip().add(index++, loc.getMiscText("ToolPart.ValidTiers"));
      event.getToolTip().add(index++, "  " + line);
    } else {
      event.getToolTip().add(index++, loc.getMiscText("PressCtrl"));
    }
  }

  private void onTooltipForToolMaterial(ItemTooltipEvent event, ItemStack stack, ToolPart part,
      boolean modifierKey) {

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
    if (modifierKey) {
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

      //@formatter:on
    } else {
      list.add(index++, TextFormatting.GOLD + loc.getMiscText("PressCtrl"));
    }
  }

  private void renderCrosshairs(RenderGameOverlayEvent event) {

    EntityPlayer player = Minecraft.getMinecraft().thePlayer;
    ItemStack mainHand = player.getHeldItem(EnumHand.MAIN_HAND);

    if (mainHand == null) {
      return;
    }

    Item item = mainHand.getItem();
    boolean isDigger = item instanceof ITool && ((ITool) item).isDiggingTool();

    if (isDigger && event.isCancelable() && event.getType() == ElementType.CROSSHAIRS) {
      if (ToolHelper.isSpecialAbilityEnabled(mainHand)) {
        event.setCanceled(true);
        GuiCrosshairs.INSTANCE.renderOverlay(event, item == ModItems.axe ? 1 : 0);
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

    if (!Config.SHOW_BONUS_ARMOR_BAR || !event.isCancelable()
        || event.getType() != ElementType.ARMOR)
      return;

    int width = event.getResolution().getScaledWidth();
    int height = event.getResolution().getScaledHeight();

    GlStateManager.enableBlend();
    GlStateManager.color(1.0f, 1.0f, 0.3f);
    int left = width / 2 - 91;
    int top = height - GuiIngameForge.left_height;

    int level = ForgeHooks.getTotalArmorValue(Minecraft.getMinecraft().thePlayer) - 20;
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

  public void drawTexturedModalRect(int x, int y, int textureX, int textureY, int width,
      int height) {

    float f = 0.00390625F;
    float f1 = 0.00390625F;
    Tessellator tessellator = Tessellator.getInstance();
    VertexBuffer vertexbuffer = tessellator.getBuffer();
    vertexbuffer.begin(7, DefaultVertexFormats.POSITION_TEX);
    vertexbuffer.pos(x + 0, y + height, 0).tex((textureX + 0) * f, (textureY + height) * f1)
        .endVertex();
    vertexbuffer.pos(x + width, y + height, 0).tex((textureX + width) * f, (textureY + height) * f1)
        .endVertex();
    vertexbuffer.pos(x + width, y + 0, 0).tex((textureX + width) * f, (textureY + 0) * f1)
        .endVertex();
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
