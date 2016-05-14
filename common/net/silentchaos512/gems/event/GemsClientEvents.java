package net.silentchaos512.gems.event;

import org.lwjgl.input.Keyboard;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.client.event.EntityViewRenderEvent.FOVModifier;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import net.minecraftforge.client.event.TextureStitchEvent;
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
import net.silentchaos512.gems.client.gui.GuiCrosshairs;
import net.silentchaos512.gems.client.handler.ClientTickHandler;
import net.silentchaos512.gems.item.ModItems;
import net.silentchaos512.gems.util.ToolHelper;
import net.silentchaos512.lib.util.LocalizationHelper;

public class GemsClientEvents {

  LocalizationHelper loc = SilentGems.instance.localizationHelper;
  int fovModifier = 0;

  @SubscribeEvent
  public void onRenderGameOverlay(RenderGameOverlayEvent event) {

    ModItems.teleporterLinker.renderGameOverlay(event);
    renderCrosshairs(event);
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

    // Material grade
    EnumMaterialGrade grade = EnumMaterialGrade.fromStack(stack);
    String line = loc.getMiscText("ToolPart.Grade");
    event.getToolTip().add(index++, String.format(line, grade.getLocalizedName()));

    // Material tier
    EnumMaterialTier tier = part.getTier();
    line = loc.getMiscText("ToolPart.Tier");
    event.getToolTip().add(index++, String.format(line, tier.getLocalizedName()));

    // Show stats?
    if (modifierKey) {
      int multi = 100 + EnumMaterialGrade.fromStack(stack).bonusPercent;
      line = loc.getMiscText("Tooltip.Durability");
      event.getToolTip().add(index++, String.format(line, part.getDurability() * multi / 100));
      line = loc.getMiscText("Tooltip.HarvestSpeed");
      event.getToolTip().add(index++, String.format(line, part.getHarvestSpeed() * multi / 100));
      line = loc.getMiscText("Tooltip.HarvestLevel");
      event.getToolTip().add(index++, String.format(line, part.getHarvestLevel() * multi / 100));
      line = loc.getMiscText("Tooltip.MeleeDamage");
      event.getToolTip().add(index++, String.format(line, part.getMeleeDamage() * multi / 100));
      line = loc.getMiscText("Tooltip.MagicDamage");
      event.getToolTip().add(index++, String.format(line, part.getMagicDamage() * multi / 100));
      line = loc.getMiscText("Tooltip.ChargeSpeed");
      event.getToolTip().add(index++, String.format(line, part.getChargeSpeed() * multi / 100));
      line = loc.getMiscText("Tooltip.Enchantability");
      event.getToolTip().add(index++, String.format(line, part.getEnchantability() * multi / 100));
    } else {
      event.getToolTip().add(index++, TextFormatting.GOLD + loc.getMiscText("PressCtrl"));
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
}
