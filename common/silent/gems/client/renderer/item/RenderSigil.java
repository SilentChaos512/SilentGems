package silent.gems.client.renderer.item;

import silent.gems.client.model.ModelSG;
import silent.gems.client.renderer.RenderBase;
import silent.gems.core.util.Color;
import silent.gems.item.tool.Sigil;
import silent.gems.lib.Strings;
import silent.gems.lib.Textures;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemDye;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MathHelper;
import net.minecraftforge.client.IItemRenderer;
import net.minecraftforge.client.IItemRenderer.ItemRenderType;
import net.minecraftforge.client.IItemRenderer.ItemRendererHelper;

public class RenderSigil extends RenderBase implements IItemRenderer {

    private ModelSG model;
    private Color outerRingColor = new Color(0xF6CD00);
    
    public RenderSigil() {
        
        model = new ModelSG("Sigil.obj", true);
    }
    @Override
    public boolean handleRenderType(ItemStack item, ItemRenderType type) {

        return true;
    }

    @Override
    public boolean shouldUseRenderHelper(ItemRenderType type, ItemStack item, ItemRendererHelper helper) {

        return true;
    }

    @Override
    public void renderItem(ItemRenderType type, ItemStack stack, Object... data) {

        float x = 0.5f, y = 0.5f, z = -0.5f;
        int colorIndex = 15;
        if (stack.stackTagCompound != null && stack.stackTagCompound.hasKey(Strings.SIGIL_COLOR)) {
            colorIndex = stack.stackTagCompound.getInteger(Strings.SIGIL_COLOR);
        }
        
        switch (type) {
            case ENTITY: {
                renderEntity(stack);
                return;
            }
            case EQUIPPED: {
                renderEquipped(stack, (EntityLivingBase) data[1]);
                return;
            }
            case EQUIPPED_FIRST_PERSON: {
                renderEquippedFirstPerson(stack, (EntityLivingBase) data[1]);
                return;
            }
            case INVENTORY: {
                renderInventory(stack);
                return;
            }
            default:
                return;
        }
    }
    
    private void renderEntity(ItemStack stack) {
        
        float x = 0.0f, y = 0.125f, z = 0.0f, scale = 0.5f;
        
        push();
        color(outerRingColor);
        translate(x, y, z);
        scaleAll(scale);
        bindTexture(Textures.ITEM_SIGIL);
        model.render();
        pop();
        
        scale = 0.35f;
        push();
        color(getSigilColor(stack));
        translate(x, y, z);
        scaleAll(scale);
        model.render();
        pop();
    }
    
    private void renderEquipped(ItemStack stack, EntityLivingBase entity) {
        
        float x = 0.25f, y = 0.1f, z = 0.25f, scale = 0.75f;
        float innerRingAngle = getInnerRingRotation(stack, entity);
        float roll = -20.0f;
        float yaw = -225.0f;
        
        push();
        color(outerRingColor);
        translate(x, y, z);
        rotateYaw(yaw);
        rotateRoll(roll);
        scaleAll(scale);
        bindTexture(Textures.ITEM_SIGIL);
        model.render();
        pop();
        
        scale = 0.525f;
        push();
        color(getSigilColor(stack));
        translate(x, y, z);
        rotateYaw(yaw);
        rotateRoll(roll);
        rotatePitch(innerRingAngle);
        
        scaleAll(scale);
        model.render();
        pop();
    }
    
    private void renderEquippedFirstPerson(ItemStack stack, EntityLivingBase entity) {
        
        float x = 0.75f, y = 0.7f, z = 0.0f, scale = 0.75f;
        float innerRingAngle = getInnerRingRotation(stack, entity);
        float roll = 15.0f;
        float yaw = 45.0f;
        
        push();
        color(outerRingColor);
        translate(x, y, z);
        rotateYaw(yaw);
        rotateRoll(roll);
        scaleAll(scale);
        bindTexture(Textures.ITEM_SIGIL);
        model.render();
        pop();
        
        scale = 0.525f;
        push();
        color(getSigilColor(stack));
        translate(x, y, z);
        rotateYaw(yaw);
        rotateRoll(roll);
        rotatePitch(innerRingAngle);
        scaleAll(scale);
        model.render();
        pop();
    }
    
    private void renderInventory(ItemStack stack) {
        
        float x = -0.5f, y = -0.4f, z = -0.5f, scale = 0.7f;
        
        push();
        color(outerRingColor);
        translate(x, y, z);
        // rotate here
        rotatePitch(45.0f);
        //rotateYaw(45.0f);
        scaleAll(scale);
        bindTexture(Textures.ITEM_SIGIL);
        model.render();
        pop();
        
        scale = 0.49f;
        push();
        color(getSigilColor(stack));
        translate(x, y, z);
        // rotate here
        rotatePitch(45.0f);
        scaleAll(scale);
        model.render();
        pop();
    }
    
    private Color getSigilColor(ItemStack stack) {
        
        int k = 15;
        
        if (stack != null && stack.stackTagCompound != null && stack.stackTagCompound.hasKey(Strings.SIGIL_COLOR)) {
            k = stack.stackTagCompound.getInteger(Strings.SIGIL_COLOR);
        }
        
        k = ItemDye.dyeColors[k];
        return new Color(k);
    }
    
    private float getInnerRingRotation(ItemStack stack, EntityLivingBase entity) {
        
        EntityPlayer player = entity instanceof EntityPlayer ? (EntityPlayer) entity : null;
        Sigil sigil = stack.getItem() instanceof Sigil ? (Sigil) stack.getItem() : null;
        
        if (player == null || sigil == null) {
            return 0.0f;
        }

        return 180.0f * MathHelper.clamp_float((float) player.getItemInUseDuration() / sigil.getChargeTime(stack), 0.0f, 1.0f);
    }
}
