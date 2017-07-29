package net.silentchaos512.gems.client.render.models;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelBox;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.GLAllocation;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.VertexBuffer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.silentchaos512.lib.SilentLib;
import net.silentchaos512.lib.util.Color;

public class ArmorModelRenderer extends ModelRenderer{

    protected Map<ModelRenderer, Integer> childColors = new HashMap<>();

    protected boolean compiled;
    protected int displayList;

    public ArmorModelRenderer(ModelBase model, int texOffX, int texOffY)
    {
        super(model, texOffX, texOffY);
    }

    public void addChild(ModelRenderer renderer, int color)
    {
        super.addChild(renderer);
        childColors.put(renderer, color);
    }



    @SideOnly(Side.CLIENT)
    @Override
    public void render(float scale)
    {
        if (!this.isHidden)
        {
            if (this.showModel)
            {
                if (!compiled)
                {
                    this.compileDisplayList(scale);
                }

                GlStateManager.translate(this.offsetX, this.offsetY, this.offsetZ);

                if (this.rotateAngleX == 0.0F && this.rotateAngleY == 0.0F && this.rotateAngleZ == 0.0F)
                {
                    if (this.rotationPointX == 0.0F && this.rotationPointY == 0.0F && this.rotationPointZ == 0.0F)
                    {
                        GlStateManager.callList(this.displayList);

                        if (this.childModels != null)
                        {
                            for (ModelRenderer child : this.childModels)
                            {
                                setRenderColor(childColors.get(child));
                                child.render(scale);
                            }
//                            for (int k = 0; k < this.childModels.size(); ++k)
//                            {
//
//                                ((ModelRenderer)this.childModels.get(k)).render(scale);
//                            }
                        }
                    }
                    else
                    {
                        GlStateManager.translate(this.rotationPointX * scale, this.rotationPointY * scale, this.rotationPointZ * scale);
                        GlStateManager.callList(this.displayList);

                        if (this.childModels != null)
                        {
                            for (ModelRenderer child : this.childModels)
                            {
                                setRenderColor(childColors.get(child));
                                child.render(scale);
                            }
//                            for (int j = 0; j < this.childModels.size(); ++j)
//                            {
//                                ((ModelRenderer)this.childModels.get(j)).render(scale);
//                            }
                        }

                        GlStateManager.translate(-this.rotationPointX * scale, -this.rotationPointY * scale, -this.rotationPointZ * scale);
                    }
                }
                else
                {
                    GlStateManager.pushMatrix();
                    GlStateManager.translate(this.rotationPointX * scale, this.rotationPointY * scale, this.rotationPointZ * scale);

                    if (this.rotateAngleZ != 0.0F)
                    {
                        GlStateManager.rotate(this.rotateAngleZ * (180F / (float)Math.PI), 0.0F, 0.0F, 1.0F);
                    }

                    if (this.rotateAngleY != 0.0F)
                    {
                        GlStateManager.rotate(this.rotateAngleY * (180F / (float)Math.PI), 0.0F, 1.0F, 0.0F);
                    }

                    if (this.rotateAngleX != 0.0F)
                    {
                        GlStateManager.rotate(this.rotateAngleX * (180F / (float)Math.PI), 1.0F, 0.0F, 0.0F);
                    }

                    GlStateManager.callList(this.displayList);

                    if (this.childModels != null)
                    {
                        for (ModelRenderer child : this.childModels)
                        {
                            setRenderColor(childColors.get(child));
                            child.render(scale);
                        }
//                        for (int i = 0; i < this.childModels.size(); ++i)
//                        {
//                            ((ModelRenderer)this.childModels.get(i)).render(scale);
//                        }
                    }

                    GlStateManager.popMatrix();
                }

                GlStateManager.translate(-this.offsetX, -this.offsetY, -this.offsetZ);
            }
        }
    }

    @SideOnly(Side.CLIENT)
    protected void compileDisplayList(float scale)
    {
        this.displayList = GLAllocation.generateDisplayLists(1);
        GlStateManager.glNewList(this.displayList, 4864);
        VertexBuffer vertexBuffer = Tessellator.getInstance().getBuffer();

        for (int i=0; i < this.cubeList.size(); i++)
        {
            ((ModelBox)this.cubeList.get(i)).render(vertexBuffer, scale);
        }

        GlStateManager.glEndList();
        this.compiled = true;
    }

    protected void setRenderColor(int color)
    {
        Color c = new Color(color);
        GlStateManager.color(c.getRed(), c.getGreen(), c.getBlue(), c.getAlpha());
    }

}
