package net.silentchaos512.gems.client.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHandSide;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import net.silentchaos512.gems.lib.soul.ToolSoul;
import net.silentchaos512.gems.util.SoulManager;
import net.silentchaos512.lib.util.Color;

public class GuiToolSouls {

    private static final ResourceLocation WIDGETS_TEX_PATH = new ResourceLocation("textures/gui/widgets.png");

    public static void renderXPBar(RenderGameOverlayEvent event) {
        // TODO

//    if (event.getType() != ElementType.EXPERIENCE || !(event instanceof RenderGameOverlayEvent.Post))
//      return;
//
//    Minecraft mc = Minecraft.getMinecraft();
//    if (!mc.playerController.gameIsSurvivalOrAdventure())
//      return;
//
//    ItemStack mainHand = mc.player.getHeldItemMainhand();
//    ToolSoul soul = SoulManager.getSoul(mainHand);
//    if (soul == null)
//      return;
//
//    ScaledResolution sr = event.getResolution();
//    int cap =
//    int width = sr.getScaledWidth();
//    int left = width / 2 - 91;
    }

    public static void renderAPBars(RenderGameOverlayEvent event) {
        if (event.getType() != ElementType.HOTBAR || !(event instanceof RenderGameOverlayEvent.Post))
            return;

        Minecraft mc = Minecraft.getMinecraft();
        ScaledResolution sr = event.getResolution();

        GlStateManager.pushMatrix();

        mc.getTextureManager().bindTexture(WIDGETS_TEX_PATH);
        EntityPlayer player = mc.player;
        ItemStack itemstack = player.getHeldItemOffhand();
        EnumHandSide enumhandside = player.getPrimaryHand().opposite();
        int i = sr.getScaledWidth() / 2;
        int j = 182;
        int k = 91;

        GlStateManager.enableRescaleNormal();
        GlStateManager.enableBlend();
        GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA,
                GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE,
                GlStateManager.DestFactor.ZERO);
        RenderHelper.enableGUIStandardItemLighting();

        if (!itemstack.isEmpty()) {
            int l1 = sr.getScaledHeight() - 16 - 2;

            if (enumhandside == EnumHandSide.LEFT) {
                renderAPBarForItem(itemstack, i - 91 - 26, l1);
            } else {
                renderAPBarForItem(itemstack, i + 91 + 10, l1);
            }
        }

        for (int l = 0; l < 9; ++l) {
            int i1 = i - 90 + l * 20 + 2;
            int j1 = sr.getScaledHeight() - 16 - 2;

            renderAPBarForItem(player.inventory.mainInventory.get(l), i1, j1);
        }

        RenderHelper.disableStandardItemLighting();
        GlStateManager.disableRescaleNormal();
        GlStateManager.disableBlend();

        GlStateManager.popMatrix();
    }

    private static void renderAPBarForItem(ItemStack stack, int xPosition, int yPosition) {
        if (!stack.isEmpty()) {
            ToolSoul soul = SoulManager.getSoul(stack);
            if (soul != null) {
                GlStateManager.disableLighting();
                GlStateManager.disableDepth();
                GlStateManager.disableTexture2D();
                GlStateManager.disableAlpha();
                GlStateManager.disableBlend();
                Tessellator tessellator = Tessellator.getInstance();
                BufferBuilder bufferbuilder = tessellator.getBuffer();
                double ap = 1.0 - ((double) soul.getActionPoints() / soul.getMaxActionPoints());
                int rgbfordisplay = new Color(0f, 0.75f * (1f - (float) ap), 1f).getColor();
                int i = Math.round(13.0F - (float) ap * 13.0F);
                int j = rgbfordisplay;
                draw(bufferbuilder, xPosition + 2, yPosition + 13, 13, 2, 0, 0, 0, 255);
                draw(bufferbuilder, xPosition + 2, yPosition + 13, i, 1, j >> 16 & 255, j >> 8 & 255,
                        j & 255, 255);
                GlStateManager.enableBlend();
                GlStateManager.enableAlpha();
                GlStateManager.enableTexture2D();
                GlStateManager.enableLighting();
                GlStateManager.enableDepth();
            }
        }
    }

    private static void draw(BufferBuilder renderer, int x, int y, int width, int height, int red, int green, int blue, int alpha) {
        renderer.begin(7, DefaultVertexFormats.POSITION_COLOR);
        renderer.pos((double) (x + 0), (double) (y + 0), 0.0D).color(red, green, blue, alpha).endVertex();
        renderer.pos((double) (x + 0), (double) (y + height), 0.0D).color(red, green, blue, alpha).endVertex();
        renderer.pos((double) (x + width), (double) (y + height), 0.0D).color(red, green, blue, alpha).endVertex();
        renderer.pos((double) (x + width), (double) (y + 0), 0.0D).color(red, green, blue, alpha).endVertex();
        Tessellator.getInstance().draw();
    }
}
