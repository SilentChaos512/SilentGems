package net.silentchaos512.gems.client.render.models;

import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelBox;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GLAllocation;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.silentchaos512.lib.SilentLib;
import org.lwjgl.opengl.GL11;

public class ArmorModelRenderer extends ModelRenderer {

  protected Object2IntMap<ModelBox> childColors = new Object2IntOpenHashMap<>();

  public ArmorModelRenderer(ModelBase model) {
    super(model);
  }

  public void addChild(ModelBox box, int color) {
    cubeList.add(box);
    childColors.put(box, color);
  }

  public void addChild(ModelBox box) {
    cubeList.add(box);
  }

  @Override
  @SideOnly(Side.CLIENT)
  public void compileDisplayList(float scale) {
    if (SilentLib.getMCVersion() < 12) {
      return; // FIXME
    }
    this.displayList = GLAllocation.generateDisplayLists(1);
    GlStateManager.glNewList(this.displayList, GL11.GL_COMPILE);
    BufferBuilder vertexBuffer = Tessellator.getInstance().getBuffer();

    for (ModelBox box : this.cubeList) {
      int color = 0xFFFFFF;
      if (childColors.containsKey(box)) {
        color = childColors.getInt(box);
      }
      GlStateManager
          .color((color >> 16 & 0xFF) / 255f, (color >> 8 & 0xFF) / 255f, (color & 0xFF) / 255f);
      box.render(vertexBuffer, scale);
    }
    GlStateManager.color(1, 1, 1, 1);
    GlStateManager.glEndList();
    this.compiled = true;
  }

  public void dispose() {
    compiled = false;
    GLAllocation.deleteDisplayLists(displayList);
    displayList = 0;
  }
}
