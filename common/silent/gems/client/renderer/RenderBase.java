package silent.gems.client.renderer;

import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

import silent.gems.core.util.Color;
import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;


@SideOnly(Side.CLIENT)
public class RenderBase {

    public void push() {
        
        GL11.glPushMatrix();
    }
    
    public void pop() {
        
        GL11.glPopMatrix();
    }
    
    public void bindTexture(ResourceLocation texture) {
        
        FMLClientHandler.instance().getClient().renderEngine.bindTexture(texture);
    }
    
    public void rotateRoll(float angle) {
        
        GL11.glRotatef(angle, 0.0f, 0.0f, 1.0f);
    }
    
    public void rotatePitch(float angle) {
        
        GL11.glRotatef(angle, 1.0f, 0.0f, 0.0f);
    }
    
    public void rotateYaw(float angle) {
        
        GL11.glRotatef(angle, 0.0f, 1.0f, 0.0f);
    }
    
    public void scaleAll(float scale) {
        
        GL11.glScalef(scale, scale, scale);
    }
    
    public void translate(float x, float y, float z) {
        
        GL11.glTranslatef(x, y, z);
    }
    
    public void color(Color c) {
        
        GL11.glColor3ub((byte) c.r, (byte) c.g, (byte) c.b);
    }
}
