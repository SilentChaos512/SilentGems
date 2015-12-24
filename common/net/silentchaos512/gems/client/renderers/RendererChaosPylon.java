package net.silentchaos512.gems.client.renderers;

import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.AdvancedModelLoader;
import net.minecraftforge.client.model.IModelCustom;
import net.silentchaos512.gems.block.BlockChaosPylon;
import net.silentchaos512.gems.tile.TileChaosPylon;
import org.lwjgl.opengl.GL11;

/**
 * Created by M4thG33k on 9/13/2015.
 */
public class RendererChaosPylon extends TileEntitySpecialRenderer {

    private IModelCustom pylonModel;
    private IModelCustom plateModel;
    private ResourceLocation pylonUVmap;

    public RendererChaosPylon()
    {
        pylonModel = AdvancedModelLoader.loadModel(new ResourceLocation("silentgems","textures/renders/SilentChaosPylon.obj"));
        plateModel = AdvancedModelLoader.loadModel(new ResourceLocation("silentgems","textures/renders/SilentChaosPylonPlates.obj"));
    }

    @Override
    public void renderTileEntityAt(TileEntity tileEntity, double x, double y, double z, float f) {
        int pylonType = ((TileChaosPylon)tileEntity).getPylonTypeInteger();
//    	BlockChaosPylon.Type pylonType = ((TileChaosPylon)tileEntity).getPylonType();

        //makes sure we get the right UV map for the pylon
        if (pylonType == 1)
        {
            pylonUVmap = new ResourceLocation("silentgems","textures/renders/SilentChaosPylonBurner.png");
        }
        else if (pylonType == 0)
        {
            pylonUVmap = new ResourceLocation("silentgems","textures/renders/SilentChaosPylonPassive.png");
        }
        else
        {
        	pylonUVmap = new ResourceLocation("silentgems","textures/renders/SilentChaosPylonError.png");
        }

        float timer = ((TileChaosPylon)tileEntity).getTimer();
        timer += f;

        //render the pylon itself
        GL11.glPushMatrix();
        GL11.glTranslated(x+0.5,y+0.5+0.1*Math.sin(timer*3.14159/180.0),z+0.5);
        GL11.glRotated(timer,0.0,1.0,0.0);
        bindTexture(pylonUVmap);
        pylonModel.renderAll();
        GL11.glPopMatrix();

        //render the plates
        GL11.glPushMatrix();
        GL11.glTranslated(x+0.5,y+0.5+0.1*Math.sin(timer*3.14159/180.0),z+0.5);
        GL11.glRotated(-timer,0.0,1.0,0.0);
        bindTexture(pylonUVmap);
        plateModel.renderAll();
        GL11.glPopMatrix();
    }
}
