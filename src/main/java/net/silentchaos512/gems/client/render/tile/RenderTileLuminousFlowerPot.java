package net.silentchaos512.gems.client.render.tile;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import net.silentchaos512.gems.block.flowerpot.LuminousFlowerPotTileEntity;
import net.silentchaos512.gems.lib.Gems;

public class RenderTileLuminousFlowerPot extends TileEntityRenderer<LuminousFlowerPotTileEntity> {
    // Was "F", has something to do with vertex positions, I think?
    private static final double POORLY_NAMED_CONSTANT = 0.4 * (1 - Math.sqrt(2) / 2);

    private static final ResourceLocation[] TEXTURES = new ResourceLocation[Gems.values().length];

    public RenderTileLuminousFlowerPot() {
        for (int i = 0; i < TEXTURES.length; ++i) {
            Gems gem = Gems.values()[i];
            TEXTURES[i] = new ResourceLocation("silentgems:textures/blocks/glowrose/" + gem.getName() + ".png");
        }
    }

    @Override
    public void render(LuminousFlowerPotTileEntity te, double x, double y, double z, float partialTicks, int destroyStage) {
        Tessellator tess = Tessellator.getInstance();
        BufferBuilder buff = tess.getBuffer();

        int flowerId = te.getFlowerId();
        if (flowerId < 0 || flowerId >= TEXTURES.length) {
            return;
        }

        Minecraft.getInstance().textureManager.bindTexture(TEXTURES[flowerId]);

        double x1 = x + POORLY_NAMED_CONSTANT;
        double x2 = x + 1 - POORLY_NAMED_CONSTANT;
        double y1 = y + POORLY_NAMED_CONSTANT;
        double y2 = y + 1.15 - POORLY_NAMED_CONSTANT;
        double z1 = z + POORLY_NAMED_CONSTANT;
        double z2 = z + 1 - POORLY_NAMED_CONSTANT;

        GlStateManager.pushMatrix();
        GlStateManager.disableLighting();

        buff.begin(7, DefaultVertexFormats.POSITION_TEX);
        buff.pos(x1, y2, z1).tex(0, 0).endVertex();
        buff.pos(x2, y2, z2).tex(1, 0).endVertex();
        buff.pos(x2, y1, z2).tex(1, 1).endVertex();
        buff.pos(x1, y1, z1).tex(0, 1).endVertex();
        tess.draw();

        buff.begin(7, DefaultVertexFormats.POSITION_TEX);
        buff.pos(x1, y1, z1).tex(0, 1).endVertex();
        buff.pos(x2, y1, z2).tex(1, 1).endVertex();
        buff.pos(x2, y2, z2).tex(1, 0).endVertex();
        buff.pos(x1, y2, z1).tex(0, 0).endVertex();
        tess.draw();

        buff.begin(7, DefaultVertexFormats.POSITION_TEX);
        buff.pos(x2, y2, z1).tex(0, 0).endVertex();
        buff.pos(x1, y2, z2).tex(1, 0).endVertex();
        buff.pos(x1, y1, z2).tex(1, 1).endVertex();
        buff.pos(x2, y1, z1).tex(0, 1).endVertex();
        tess.draw();

        buff.begin(7, DefaultVertexFormats.POSITION_TEX);
        buff.pos(x2, y1, z1).tex(0, 1).endVertex();
        buff.pos(x1, y1, z2).tex(1, 1).endVertex();
        buff.pos(x1, y2, z2).tex(1, 0).endVertex();
        buff.pos(x2, y2, z1).tex(0, 0).endVertex();
        tess.draw();

        GlStateManager.popMatrix();
    }
}
