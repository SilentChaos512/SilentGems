package net.silentchaos512.gems.client.renderers;

import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.AdvancedModelLoader;
import net.minecraftforge.client.model.IModelCustom;
import net.silentchaos512.gems.SilentGems;
import net.silentchaos512.gems.tile.TileChaosAltar;
import org.lwjgl.opengl.GL11;

/**
 * Created by M4thG33k on 9/13/2015.
 */
public class RendererChaosAltar extends TileEntitySpecialRenderer {

    private IModelCustom baseModel;
    private IModelCustom diamondModel;
    private ResourceLocation altarUVmap;

    public RendererChaosAltar()
    {
        //loads the OBJ model for the altar into the game
        baseModel = AdvancedModelLoader.loadModel(new ResourceLocation("silentgems","textures/renders/SilentChaosAltar.obj"));

        //loads the OBJ model for the floating diamond
        diamondModel = AdvancedModelLoader.loadModel(new ResourceLocation("silentgems","textures/renders/SilentChaosAltarDiamond.obj"));

        //loads the texture to be mapped to the model
        altarUVmap = new ResourceLocation("silentgems","textures/renders/SilentChaosAltar.png");
    }

    @Override
    public void renderTileEntityAt(TileEntity tileEntity, double x, double y, double z, float f)
    {
        TileChaosAltar altar = (TileChaosAltar)tileEntity;
        float timer = altar.getTimer();
        timer += f;

        //render the altar model
        GL11.glPushMatrix();
        GL11.glTranslated(x+0.5,y+0.5,z+0.5);
        bindTexture(altarUVmap);
        baseModel.renderAll();
        GL11.glPopMatrix();

        if (timer!=-1) { //the only time timer==-1 is when we're rendering it as an item
            //render the diamond model
            GL11.glPushMatrix();
            GL11.glTranslated(x + 0.5, y + 0.55 + 0.02 * Math.sin(timer * 3.14159 / 90.0), z + 0.5);
            GL11.glRotated(timer, 0.0, 1.0, 0.0);
            GL11.glScaled(0.75, 0.75, 0.75);
            bindTexture(altarUVmap);
            diamondModel.renderAll();
            GL11.glPopMatrix();
        }
    }


}
