package net.silentchaos512.gems.client.render.particle;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import org.lwjgl.opengl.GL11;

public final class ParticleRenderDispatcher {
    public static int countChaos = 0;

    public static void dispatch() {
        Tessellator tess = Tessellator.getInstance();

        GL11.glPushAttrib(GL11.GL_LIGHTING_BIT);
        GlStateManager.depthMask(false);
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE);
        GlStateManager.alphaFunc(GL11.GL_GREATER, 0.003921569f);
        GlStateManager.disableLighting();

        ParticleChaos.dispatchQueuedRenders(tess);

        GlStateManager.alphaFunc(GL11.GL_GREATER, 0.1f);
        GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GlStateManager.disableBlend();
        GlStateManager.depthMask(true);
        GL11.glPopAttrib();
    }
}
