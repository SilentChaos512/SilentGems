package net.silentchaos512.gems.client.render.entity;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.util.ResourceLocation;
import net.silentchaos512.gems.entity.projectile.AbstractWispShotEntity;
import net.silentchaos512.gems.lib.WispTypes;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

public class WispShotRenderer extends EntityRenderer<AbstractWispShotEntity> {
    private static final Map<WispTypes, RenderType> RENDER_TYPE_MAP = Arrays.stream(WispTypes.values())
            .collect(Collectors.toMap(t -> t, t -> RenderType.getEntityCutoutNoCull(t.getShotTexture())));

    public WispShotRenderer(EntityRendererManager renderManagerIn) {
        super(renderManagerIn);
    }

    @Override
    public void render(AbstractWispShotEntity entity, float entityYaw, float partialTicks, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn) {
        matrixStackIn.push();
        float scale = 0.5f;
        matrixStackIn.scale(scale, scale, scale);
        matrixStackIn.rotate(this.renderManager.getCameraOrientation());
        matrixStackIn.rotate(Vector3f.YP.rotationDegrees(180.0F));
        MatrixStack.Entry matrixstack$entry = matrixStackIn.getLast();
        Matrix4f matrix4f = matrixstack$entry.getMatrix();
        Matrix3f matrix3f = matrixstack$entry.getNormal();
        IVertexBuilder ivertexbuilder = bufferIn.getBuffer(RENDER_TYPE_MAP.get(entity.getWispType()));
        func_229045_a_(ivertexbuilder, matrix4f, matrix3f, packedLightIn, 0.0F, 0, 0, 1);
        func_229045_a_(ivertexbuilder, matrix4f, matrix3f, packedLightIn, 1.0F, 0, 1, 1);
        func_229045_a_(ivertexbuilder, matrix4f, matrix3f, packedLightIn, 1.0F, 1, 1, 0);
        func_229045_a_(ivertexbuilder, matrix4f, matrix3f, packedLightIn, 0.0F, 1, 0, 0);
        matrixStackIn.pop();
        super.render(entity, entityYaw, partialTicks, matrixStackIn, bufferIn, packedLightIn);
    }

    private static void func_229045_a_(IVertexBuilder p_229045_0_, Matrix4f p_229045_1_, Matrix3f p_229045_2_, int p_229045_3_, float p_229045_4_, int p_229045_5_, int p_229045_6_, int p_229045_7_) {
        p_229045_0_.pos(p_229045_1_, p_229045_4_ - 0.5F, (float)p_229045_5_ - 0.25F, 0.0F).color(255, 255, 255, 255).tex((float)p_229045_6_, (float)p_229045_7_).overlay(OverlayTexture.NO_OVERLAY).lightmap(p_229045_3_).normal(p_229045_2_, 0.0F, 1.0F, 0.0F).endVertex();
    }

    @Override
    public ResourceLocation getEntityTexture(AbstractWispShotEntity entity) {
        return entity.getWispType().getShotTexture();
    }
}
