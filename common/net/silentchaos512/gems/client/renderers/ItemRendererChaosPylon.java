package net.silentchaos512.gems.client.renderers;

import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.client.IItemRenderer;
import net.silentchaos512.gems.tile.TileChaosPylon;

import org.lwjgl.opengl.GL11;

/**
 * Created by M4thG33k on 9/13/2015.
 */
public class ItemRendererChaosPylon implements IItemRenderer{
    private TileChaosPylon tileEntity;

    public ItemRendererChaosPylon(TileEntitySpecialRenderer tesr, TileEntity ent)
    {
        this.tileEntity = ((TileChaosPylon)ent);
    }

    @Override
    public boolean handleRenderType(ItemStack item, ItemRenderType type)
    {
        return (type==ItemRenderType.ENTITY || type==ItemRenderType.INVENTORY || type==ItemRenderType.EQUIPPED || type==ItemRenderType.EQUIPPED_FIRST_PERSON);
    }

    @Override
    public boolean shouldUseRenderHelper(ItemRenderType type, ItemStack item, ItemRendererHelper helper)
    {
        return true;
    }

    @Override
    public void renderItem(ItemRenderType type, ItemStack item, Object... data)
    {
    	tileEntity.setPylonTypeInteger(item.getItemDamage());

        if (type==ItemRenderType.ENTITY)
        {
            GL11.glTranslated(-0.5, -0.5, -0.5);
            GL11.glPushMatrix();

            TileEntityRendererDispatcher.instance.renderTileEntityAt(tileEntity,0.0,0.0,0.0,0.0f);
            GL11.glPopMatrix();
        }

        if (type==ItemRenderType.INVENTORY)
        {
            GL11.glTranslated(-0.5,-0.5,-0.5);
            GL11.glPushMatrix();
            TileEntityRendererDispatcher.instance.renderTileEntityAt(tileEntity,0.0,0.0,0.0,0.0f);
            GL11.glPopMatrix();
        }

        if (type==ItemRenderType.EQUIPPED)
        {
            GL11.glTranslated(0.0,0.0,0.0);
            GL11.glPushMatrix();
            TileEntityRendererDispatcher.instance.renderTileEntityAt(tileEntity,0.0,0.0,0.0,0.0f);
            GL11.glPopMatrix();
        }

        if (type==ItemRenderType.EQUIPPED_FIRST_PERSON)
        {
            GL11.glTranslated(0.0,0.0,0.0);
            GL11.glPushMatrix();
            TileEntityRendererDispatcher.instance.renderTileEntityAt(tileEntity,0.0,0.0,0.0,0.0f);
            GL11.glPopMatrix();
        }
    }
}
