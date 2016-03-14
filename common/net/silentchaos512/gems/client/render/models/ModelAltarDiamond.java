package net.silentchaos512.gems.client.render.models;

import com.google.common.collect.ImmutableMap;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.crash.CrashReport;
import net.minecraft.util.ReportedException;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.*;
import net.minecraftforge.client.model.obj.OBJLoader;
import net.minecraftforge.client.model.obj.OBJModel;
import net.minecraftforge.client.model.pipeline.LightUtil;
import org.lwjgl.opengl.GL11;

import java.io.IOException;
import java.util.function.Function;

public class ModelAltarDiamond {

    private IFlexibleBakedModel diamondModel;

    public ModelAltarDiamond()
    {
        //load
        OBJModel model = ModelHelper.loadModel("ChaosAltarDiamond");
        //texture
        IModel diamond = ModelHelper.retexture(model,"#skin","ChaosAltar");
        //bake
        diamondModel = ModelHelper.bake(diamond);

    }

    public void renderDiamond()
    {
        renderModel(diamondModel);
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
