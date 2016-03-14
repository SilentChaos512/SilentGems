package net.silentchaos512.gems.client.render.models;

import com.google.common.base.Function;
import com.google.common.collect.ImmutableMap;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.crash.CrashReport;
import net.minecraft.util.ReportedException;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.Attributes;
import net.minecraftforge.client.model.IFlexibleBakedModel;
import net.minecraftforge.client.model.IModel;
import net.minecraftforge.client.model.TRSRTransformation;
import net.minecraftforge.client.model.obj.OBJLoader;
import net.minecraftforge.client.model.obj.OBJModel;
import net.minecraftforge.client.model.pipeline.LightUtil;
import org.lwjgl.opengl.GL11;

import java.io.IOException;

public class ModelPylonPlates {

    private IFlexibleBakedModel pylonPlatesPassiveModel;
    private IFlexibleBakedModel pylonPlatesBurnerModel;

    public static final Function<ResourceLocation, TextureAtlasSprite> TEXTURE_GETTER = input -> Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite(input.toString());

    public ModelPylonPlates()
    {
        try
        {
            //load the model
            OBJModel model = ((OBJModel) OBJLoader.instance.loadModel(new ResourceLocation("silentgems:models/block/ChaosPylonPlates.obj")));

            //apply textures and orient correctly
            IModel pylonPlatesPassive = ((OBJModel) model.retexture(ImmutableMap.of("#skin.001","silentgems:blocks/ChaosPylonPassive"))).process(ImmutableMap.of("flip-v","true"));
            IModel pylonPlatesBurner = ((OBJModel) model.retexture(ImmutableMap.of("#skin.001","silentgems:blocks/ChaosPylonBurner"))).process(ImmutableMap.of("flip-v","true"));

            //"turn on" the models
            pylonPlatesPassiveModel = pylonPlatesPassive.bake(TRSRTransformation.identity(), Attributes.DEFAULT_BAKED_FORMAT, TEXTURE_GETTER);
            pylonPlatesBurnerModel = pylonPlatesBurner.bake(TRSRTransformation.identity(), Attributes.DEFAULT_BAKED_FORMAT, TEXTURE_GETTER);
        } catch (IOException e)
        {
            throw new ReportedException(new CrashReport("Error making model for the chaos pylon plates!",e));
        }
    }

    public void renderPylonPlates(int pylonType)
    {
        switch (pylonType)
        {
            case 1:
                renderModel(pylonPlatesBurnerModel);
                break;
            default:
                renderModel(pylonPlatesPassiveModel);
        }
    }

    private void renderModel(IFlexibleBakedModel model)
    {
        renderModel(model, -1);
    }

    private void renderModel(IFlexibleBakedModel model, int color)
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
