package net.silentchaos512.gems.client.render.models;

import com.google.common.base.Function;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
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
import java.util.Set;

public class ModelPylon {

    private IFlexibleBakedModel pylonPassiveModel;
    private IFlexibleBakedModel pylonBurnerModel;

    //private static final Set<String> GROUP_NAMES = ImmutableSet.of("Pylon");

    public static final Function<ResourceLocation, TextureAtlasSprite> TEXTURE_GETTER = input -> Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite(input.toString());

    public ModelPylon()
    {
        try
        {
            //loading the model
            ResourceLocation resourceLocation = new ResourceLocation("silentgems:models/block/ChaosPylon.obj");
            OBJModel model = ((OBJModel) OBJLoader.instance.loadModel(resourceLocation));

            //apply textures and orient correctly
            IModel pylonPassive = ((OBJModel) model.retexture(ImmutableMap.of("#None", "silentgems:models/block/ChaosPylonPassive"))).process(ImmutableMap.of("flip-v","true"));
            IModel pylonBurner = ((OBJModel) model.retexture(ImmutableMap.of("#None", "silentgems:models/block/ChaosPylonBurner"))).process(ImmutableMap.of("flip-v","true"));

            //"turn on" the models
            pylonPassiveModel = pylonPassive.bake(TRSRTransformation.identity(), Attributes.DEFAULT_BAKED_FORMAT, TEXTURE_GETTER);
            pylonBurnerModel = pylonBurner.bake(TRSRTransformation.identity(), Attributes.DEFAULT_BAKED_FORMAT, TEXTURE_GETTER);
        } catch (IOException e)
        {
            throw new ReportedException(new CrashReport("Error making model for chaos pylons!",e));
        }
    }

    public void renderPylon(int pylonType)
    {
        switch (pylonType)
        {
            case 1:
                renderModel(pylonBurnerModel);
                break;
            default:
                renderModel(pylonPassiveModel);
        }
    }

    private void renderModel(IFlexibleBakedModel model)
    {
        renderModel(model,-1);
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
