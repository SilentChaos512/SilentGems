// This is a direct copy from Zelda Sword Skills (but with the package changed, of course).
// Big thanks to coolAlias for making the project open source!
// https://github.com/coolAlias/ZeldaSwordSkills/blob/1.8/src/main/java/zeldaswordskills/client/RenderHelperQ.java

/**
    Copyright (C) <2015> <coolAlias>
    This file is part of coolAlias' Zelda Sword Skills Minecraft Mod; as such,
    you can redistribute it and/or modify it under the terms of the GNU
    General Public License as published by the Free Software Foundation,
    either version 3 of the License, or (at your option) any later version.
    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
    GNU General Public License for more details.
    You should have received a copy of the GNU General Public License
    along with this program. If not, see <http://www.gnu.org/licenses/>.
 */

package net.silentchaos512.gems.client;

import java.awt.Color;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

import com.google.common.primitives.Ints;

/**
 *
 * Compilation of helper methods dedicated to rendering
 * @authors Hunternif, coolAlias, TheGreyGhost
 *
 */
public class RenderHelperQ {

  public static void drawTexturedRect(ResourceLocation texture, double x, double y, int u, int v, int width, int height, int imageWidth, int imageHeight, double scale) {
    Minecraft.getMinecraft().renderEngine.bindTexture(texture);
    double minU = (double)u / (double)imageWidth;
    double maxU = (double)(u + width) / (double)imageWidth;
    double minV = (double)v / (double)imageHeight;
    double maxV = (double)(v + height) / (double)imageHeight;
    Tessellator tessellator = Tessellator.getInstance();
    WorldRenderer renderer = tessellator.getWorldRenderer();
    renderer.startDrawingQuads();
    renderer.addVertexWithUV(x + scale*(double)width, y + scale*(double)height, 0, maxU, maxV);
    renderer.addVertexWithUV(x + scale*(double)width, y, 0, maxU, minV);
    renderer.addVertexWithUV(x, y, 0, minU, minV);
    renderer.addVertexWithUV(x, y + scale*(double)height, 0, minU, maxV);
    tessellator.draw();
  }

  public static void drawTexturedRect(ResourceLocation texture, double x, double y, int u, int v, int width, int height, int imageWidth, int imageHeight) {
    drawTexturedRect(texture, x, y, u, v, width, height, imageWidth, imageHeight, 1);
  }

  public static void drawFullTexture(ResourceLocation texture, double x, double y, int width, int height, double scale) {
    drawTexturedRect(texture, x, y, 0, 0, width, height, width, height, scale);
  }

  public static void drawFullTexture(ResourceLocation texture, double x, double y, int width, int height) {
    drawFullTexture(texture, x, y, width, height, 1);
  }

  /**
   * Draws textured rectangle for texture already bound to Minecraft render engine
   */
  public static void drawTexturedRect(double x, double y, int u, int v, int width, int height, int imageWidth, int imageHeight, double scale) {
    double minU = (double)u / (double)imageWidth;
    double maxU = (double)(u + width) / (double)imageWidth;
    double minV = (double)v / (double)imageHeight;
    double maxV = (double)(v + height) / (double)imageHeight;
    Tessellator tessellator = Tessellator.getInstance();
    WorldRenderer renderer = tessellator.getWorldRenderer();
    renderer.startDrawingQuads();
    renderer.addVertexWithUV(x + scale*(double)width, y + scale*(double)height, 0, maxU, maxV);
    renderer.addVertexWithUV(x + scale*(double)width, y, 0, maxU, minV);
    renderer.addVertexWithUV(x, y, 0, minU, minV);
    renderer.addVertexWithUV(x, y + scale*(double)height, 0, minU, maxV);
    tessellator.draw();
  }

  /**
   * Draws textured rectangle for texture already bound to Minecraft render engine
   * @param x       Starting x position on the screen at which to draw
   * @param u       Starting x position in the texture file from which to draw
   * @param width     Width of the section of texture to draw
   * @param imageWidth  Full width of the texture file
   */
  public static void drawTexturedRect(double x, double y, int u, int v, int width, int height, int imageWidth, int imageHeight) {
    drawTexturedRect(x, y, u, v, width, height, imageWidth, imageHeight, 1);
  }

  /** Draws textured rectangle for texture already bound to Minecraft render engine */
  public static void drawFullTexture(double x, double y, int width, int height, double scale) {
    drawTexturedRect(x, y, 0, 0, width, height, width, height, scale);
  }

  /** Draws textured rectangle for texture already bound to Minecraft render engine */
  public static void drawFullTexture(double x, double y, int width, int height) {
    drawFullTexture(x, y, width, height, 1);
  }

  public static void setGLColor(int color, float alpha) {
    float r = (float)(color >> 16 & 0xff)/256f;
    float g = (float)(color >> 8 & 0xff)/256f;
    float b = (float)(color & 0xff)/256f;
    GL11.glColor4f(r, g, b, alpha);
  }

  //====================== Thanks to The Grey Ghost for the following code ===================//
  /**
       // Creates a baked quad for the given face.
       // When you are directly looking at the face, the quad is centred at [centreLR, centreUD]
       // The left<->right "width" of the face is width, the bottom<-->top "height" is height.
       // The amount that the quad is displaced towards the viewer i.e. (perpendicular to the flat face you can see) is forwardDisplacement
       //   - for example, for an EAST face, a value of 0.00 lies directly on the EAST face of the cube.  a value of 0.01 lies
       //     slightly to the east of the EAST face (at x=1.01).  a value of -0.01 lies slightly to the west of the EAST face (at x=0.99).
       // The orientation of the faces is as per the diagram on this page
       //   http://greyminecraftcoder.blogspot.com.au/2014/12/block-models-texturing-quads-faces.html
       // Read this page to learn more about how to draw a textured quad
       //   http://greyminecraftcoder.blogspot.co.at/2014/12/the-tessellator-and-worldrenderer-18.html
   * @param centreLR the centre point of the face left-right
   * @param width    width of the face
   * @param centreUD centre point of the face top-bottom
   * @param height height of the face from top to bottom
   * @param forwardDisplacement the displacement of the face (towards the front)
   * @param itemRenderLayer which item layer the quad is on
   * @param texture the texture to use for the quad
   * @param face the face to draw this quad on
   * @return
   */
  public static BakedQuad createBakedQuadForFace(float centreLR, float width, float centreUD, float height,
      float forwardDisplacement, int itemRenderLayer, TextureAtlasSprite texture, EnumFacing face)
  {
    float x1, x2, x3, x4;
    float y1, y2, y3, y4;
    float z1, z2, z3, z4;
    final float CUBE_MIN = 0.0F;
    final float CUBE_MAX = 1.0F;

    switch (face) {
    case UP: {
      x1 = x2 = centreLR + width/2.0F;
      x3 = x4 = centreLR - width/2.0F;
      z1 = z4 = centreUD + height/2.0F;
      z2 = z3 = centreUD - height/2.0F;
      y1 = y2 = y3 = y4 = CUBE_MAX + forwardDisplacement;
      break;
    }
    case DOWN: {
      x1 = x2 = centreLR + width/2.0F;
      x3 = x4 = centreLR - width/2.0F;
      z1 = z4 = centreUD - height/2.0F;
      z2 = z3 = centreUD + height/2.0F;
      y1 = y2 = y3 = y4 = CUBE_MIN - forwardDisplacement;
      break;
    }
    case WEST: {
      z1 = z2 = centreLR + width/2.0F;
      z3 = z4 = centreLR - width/2.0F;
      y1 = y4 = centreUD - height/2.0F;
      y2 = y3 = centreUD + height/2.0F;
      x1 = x2 = x3 = x4 = CUBE_MIN - forwardDisplacement;
      break;
    }
    case EAST: {
      z1 = z2 = centreLR - width/2.0F;
      z3 = z4 = centreLR + width/2.0F;
      y1 = y4 = centreUD - height/2.0F;
      y2 = y3 = centreUD + height/2.0F;
      x1 = x2 = x3 = x4 = CUBE_MAX + forwardDisplacement;
      break;
    }
    case NORTH: {
      x1 = x2 = centreLR - width/2.0F;
      x3 = x4 = centreLR + width/2.0F;
      y1 = y4 = centreUD - height/2.0F;
      y2 = y3 = centreUD + height/2.0F;
      z1 = z2 = z3 = z4 = CUBE_MIN - forwardDisplacement;
      break;
    }
    case SOUTH: {
      x1 = x2 = centreLR + width/2.0F;
      x3 = x4 = centreLR - width/2.0F;
      y1 = y4 = centreUD - height/2.0F;
      y2 = y3 = centreUD + height/2.0F;
      z1 = z2 = z3 = z4 = CUBE_MAX + forwardDisplacement;
      break;
    }
    default: {
      assert false : "Unexpected facing in createBakedQuadForFace:" + face;
    return null;
    }
    }

    return new BakedQuad(Ints.concat(vertexToInts(x1, y1, z1, Color.WHITE.getRGB(), texture, 16, 16),
        vertexToInts(x2, y2, z2, Color.WHITE.getRGB(), texture, 16, 0),
        vertexToInts(x3, y3, z3, Color.WHITE.getRGB(), texture, 0, 0),
        vertexToInts(x4, y4, z4, Color.WHITE.getRGB(), texture, 0, 16)),
        itemRenderLayer, face);
  }

  /**
   * Converts the vertex information to the int array format expected by BakedQuads.
   * @param x x coordinate
   * @param y y coordinate
   * @param z z coordinate
   * @param color RGBA colour format - white for no effect, non-white to tint the face with the specified colour
   * @param texture the texture to use for the face
   * @param u u-coordinate of the texture (0 - 16) corresponding to [x,y,z]
   * @param v v-coordinate of the texture (0 - 16) corresponding to [x,y,z]
   * @return
   */
  public static int[] vertexToInts(float x, float y, float z, int color, TextureAtlasSprite texture, float u, float v)
  {
    return new int[] {
        Float.floatToRawIntBits(x),
        Float.floatToRawIntBits(y),
        Float.floatToRawIntBits(z),
        color,
        Float.floatToRawIntBits(texture.getInterpolatedU(u)),
        Float.floatToRawIntBits(texture.getInterpolatedV(v)),
        0
    };
  }
}