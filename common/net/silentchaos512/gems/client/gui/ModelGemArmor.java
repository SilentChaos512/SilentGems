package net.silentchaos512.gems.client.gui;

import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHandSide;

public class ModelGemArmor extends ModelBiped {

  @Override
  public void render(Entity entityIn, float p_78088_2_, float limbSwing, float ageInTicks,
      float netHeadYaw, float headPitch, float scale) {

    this.setRotationAngles(p_78088_2_, limbSwing, ageInTicks, netHeadYaw, headPitch, scale,
        entityIn);
    GlStateManager.pushMatrix();

    // FIXME?
    if (entityIn instanceof EntityLivingBase) {
      EntityLivingBase entityLiving = ((EntityLivingBase) entityIn);
      this.isChild = entityLiving.isChild();
      this.isSneak = entityLiving.isSneaking();
      setArmPoses(entityLiving);
    }

    if (this.isChild) {
      float f = 2.0F;
      GlStateManager.scale(1.5F / f, 1.5F / f, 1.5F / f);
      GlStateManager.translate(0.0F, 16.0F * scale, 0.0F);
      this.bipedHead.render(scale);
      GlStateManager.popMatrix();
      GlStateManager.pushMatrix();
      GlStateManager.scale(1.0F / f, 1.0F / f, 1.0F / f);
      GlStateManager.translate(0.0F, 24.0F * scale, 0.0F);
      this.bipedBody.render(scale);
      this.bipedRightArm.render(scale);
      this.bipedLeftArm.render(scale);
      this.bipedRightLeg.render(scale);
      this.bipedLeftLeg.render(scale);
      this.bipedHeadwear.render(scale);
    } else {
      if (entityIn.isSneaking()) {
        GlStateManager.translate(0.0F, 0.2F, 0.0F);
      }

      GlStateManager.color(1f, 0f, 0f);
      GlStateManager.pushMatrix();
      GlStateManager.translate(0.0f, 0.08f, 0.0f);
      this.bipedHead.render(1.2f * scale);
      GlStateManager.popMatrix();

      GlStateManager.color(1f, 1f, 1f);
      GlStateManager.pushMatrix();
//      GlStateManager.scale(1.2f, 1.2f, 1.2f);
      this.bipedBody.render(1.2f * scale);
      GlStateManager.popMatrix();

      GlStateManager.color(0f, 1f, 0f);
      this.bipedRightArm.render(scale);

      GlStateManager.color(0f, 0f, 1f);
      this.bipedLeftArm.render(scale);

      GlStateManager.color(1f, 1f, 1f);
      this.bipedRightLeg.render(scale);

      this.bipedLeftLeg.render(scale);

      this.bipedHeadwear.render(scale);
    }

    GlStateManager.popMatrix();
  }

  // Adapted from RenderPlayer.setModelVisibilities.
  protected void setArmPoses(EntityLivingBase entity) {

    ItemStack itemstack = entity.getHeldItemMainhand();
    ItemStack itemstack1 = entity.getHeldItemOffhand();
    ModelBiped.ArmPose modelbiped$armpose = ModelBiped.ArmPose.EMPTY;
    ModelBiped.ArmPose modelbiped$armpose1 = ModelBiped.ArmPose.EMPTY;

    if (itemstack != null) {
      modelbiped$armpose = ModelBiped.ArmPose.ITEM;

      if (entity.getItemInUseCount() > 0) {
        EnumAction enumaction = itemstack.getItemUseAction();

        if (enumaction == EnumAction.BLOCK) {
          modelbiped$armpose = ModelBiped.ArmPose.BLOCK;
        } else if (enumaction == EnumAction.BOW) {
          modelbiped$armpose = ModelBiped.ArmPose.BOW_AND_ARROW;
        }
      }
    }

    if (itemstack1 != null) {
      modelbiped$armpose1 = ModelBiped.ArmPose.ITEM;

      if (entity.getItemInUseCount() > 0) {
        EnumAction enumaction1 = itemstack1.getItemUseAction();

        if (enumaction1 == EnumAction.BLOCK) {
          modelbiped$armpose1 = ModelBiped.ArmPose.BLOCK;
        }
      }
    }

    if (entity.getPrimaryHand() == EnumHandSide.RIGHT) {
      this.rightArmPose = modelbiped$armpose;
      this.leftArmPose = modelbiped$armpose1;
    } else {
      this.rightArmPose = modelbiped$armpose1;
      this.leftArmPose = modelbiped$armpose;
    }
  }
}
