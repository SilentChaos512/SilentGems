package net.silentchaos512.gems.client.render.models;

import com.google.common.collect.ImmutableMap;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.crash.CrashReport;
import net.minecraft.util.ReportedException;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.*;
import net.minecraftforge.client.model.obj.OBJLoader;
import net.minecraftforge.client.model.obj.OBJModel;
import net.minecraftforge.client.model.pipeline.LightUtil;
import org.lwjgl.opengl.GL11;

import java.io.IOException;

public class ModelHelper {

    public static OBJModel loadModel(String suffix)
    {
        OBJModel model;
        try{
            model = ((OBJModel) OBJLoader.instance.loadModel(new ResourceLocation("silentgems:models/block/"+suffix+".obj")));
            return model;
        } catch (IOException e)
        {
            throw new ReportedException(new CrashReport("Error making the mode for " + suffix + "!",e));
        }
    }

    public static IModel retexture(OBJModel model,String toReplace,String suffix)
    {
        return (((OBJModel) model.retexture(ImmutableMap.of(toReplace,"silentgems:blocks/"+suffix))).process(ImmutableMap.of("flip-v","true")));
    }

    public static IFlexibleBakedModel bake(IModel model)
    {
        return model.bake(TRSRTransformation.identity(), Attributes.DEFAULT_BAKED_FORMAT, ModelLoader.defaultTextureGetter());
    }

    public static void renderModel(IFlexibleBakedModel model, int color)
    {
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldRenderer = tessellator.getWorldRenderer();
        worldRenderer.begin(GL11.GL_QUADS, model.getFormat());

        for (BakedQuad bakedQuad : model.getGeneralQuads())
        {
            LightUtil.renderQuadColor(worldRenderer,bakedQuad,color);
        }

        tessellator.draw();
    }
}
