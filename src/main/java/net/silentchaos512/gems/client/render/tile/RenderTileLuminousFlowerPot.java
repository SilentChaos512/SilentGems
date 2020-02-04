package net.silentchaos512.gems.client.render.tile;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.util.ResourceLocation;
import net.silentchaos512.gems.SilentGems;
import net.silentchaos512.gems.block.flowerpot.LuminousFlowerPotTileEntity;
import net.silentchaos512.gems.lib.Gems;

import java.util.Arrays;

public class RenderTileLuminousFlowerPot extends TileEntityRenderer<LuminousFlowerPotTileEntity> {
    // Was "F", has something to do with vertex positions, I think?
    private static final double POORLY_NAMED_CONSTANT = 0.4 * (1 - Math.sqrt(2) / 2);

    private static final ResourceLocation[] TEXTURES = Arrays.stream(Gems.values()).map(g -> SilentGems.getId("textures/blocks/glowrose/" + g.getName())).toArray(ResourceLocation[]::new);

    public RenderTileLuminousFlowerPot(TileEntityRendererDispatcher rendererDispatcherIn) {
        super(rendererDispatcherIn);
    }

    @Override
    public void render(LuminousFlowerPotTileEntity te, float partialTicks, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int combinedLightIn, int combinedOverlayIn) {
        /*Tessellator tess = Tessellator.getInstance();
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

        GlStateManager.popMatrix();*/
    }
}
