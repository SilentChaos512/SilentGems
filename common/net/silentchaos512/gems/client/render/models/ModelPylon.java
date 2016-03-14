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
import net.minecraft.util.MouseHelper;
import net.minecraft.util.ReportedException;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.*;
import net.minecraftforge.client.model.obj.OBJLoader;
import net.minecraftforge.client.model.obj.OBJModel;
import net.minecraftforge.client.model.pipeline.LightUtil;
import org.lwjgl.opengl.GL11;

import java.io.IOException;
import java.util.Set;

public class ModelPylon {

    private IFlexibleBakedModel pylonPassiveModel;
    private IFlexibleBakedModel pylonBurnerModel;


    public ModelPylon()
    {
        //loading the model
        OBJModel model = ModelHelper.loadModel("ChaosPylon");

        //apply textures and orient correctly
        IModel pylonPassive = ModelHelper.retexture(model,"#ChaosPylon","ChaosPylonPassive");
        IModel pylonBurner = ModelHelper.retexture(model,"#ChaosPylon","ChaosPylonBurner");

        //"turn on" the models
        pylonPassiveModel = ModelHelper.bake(pylonPassive);
        pylonBurnerModel = ModelHelper.bake(pylonBurner);
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
        ModelHelper.renderModel(model,color);
    }
}
