package net.silentchaos512.gems.client.gui;

import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.inventory.EntityEquipmentSlot;

import javax.annotation.Nonnull;

public class ModelGemArmor extends ModelBiped {

  private final ModelRenderer helmetBase;
//  private final ModelRenderer helmetBack;

  private final ModelRenderer chestplate;
  private final ModelRenderer chestplate2;

  private final ModelRenderer rightArm;
  private final ModelRenderer leftArm;
  private final ModelRenderer belt;
  private final ModelRenderer rightBoot;
  private final ModelRenderer leftBoot;
  private final ModelRenderer rightLeg;
  private final ModelRenderer leftLeg;

  private final EntityEquipmentSlot slot;

  public ModelGemArmor(EntityEquipmentSlot slot)
  {
    this.slot = slot;

    textureWidth = 64;
    textureHeight = 128;
    float scale = 0.2f;
    float helmetScale = 0.25f;

    helmetBase = new ModelRenderer(this, 0, 0);
    helmetBase.setRotationPoint(0, 0, 0);
    helmetBase.addBox(-4, -8, -4.5f, 8, 6, 9, helmetScale);
    setRotationAngle(helmetBase, 0.08726646259971647f, 0, 0);

//    helmetBack = new ModelRenderer(this, 0, 0);
//    helmetBack.setRotationPoint(0, 0, 0);
//    helmetBack.addBox(-4, -8, 4.5f, 8, 3, 4, scale);
//    setRotationAngle(helmetBack, 0.08726646259971647f, 0, 0);

    chestplate = new ModelRenderer(this, 0, 15);
    chestplate.setRotationPoint(0, 0, 0);
    chestplate.addBox(-4.5f, 0, -3.5f, 9, 11, 6, scale);
    setRotationAngle(chestplate, 0.08726646259971647f, 0, 0);

    chestplate2 = new ModelRenderer(this, 0, 59);
    chestplate2.setRotationPoint(0, 0, 0);
    chestplate2.addBox(-4, 6, -2.5f, 8, 4, 5, scale);
    setRotationAngle(chestplate2, -0.08726646259971647f, 0, 0);

    rightArm = new ModelRenderer(this, 0, 68);
    rightArm.setRotationPoint(-5.0f, 2.0f, 0f);
    rightArm.addBox(-3.0f, 3.0f, -2.0f, 2, 6, 4, scale);
    setRotationAngle(rightArm, 0f, 0f, 0.17453292519943295f);

    leftArm = new ModelRenderer(this, 0, 68);
    leftArm.mirror = true;
    leftArm.setRotationPoint(5f, 2.0f, 0f);
    leftArm.addBox(1.0f, 3.0f, -2.0f, 2, 6, 4, scale);
    setRotationAngle(leftArm, 0f, 0f, -0.17453292519943295f);

    belt = new ModelRenderer(this, 26, 59);
    belt.setRotationPoint(0, 0, 0);
    belt.addBox(-4.5f, 9.5f, -3.0f, 9, 3, 6, scale);

    rightBoot = new ModelRenderer(this, 28, 68);
    rightBoot.setRotationPoint(-2, 12, 0);
    rightBoot.addBox(-2, 8, -3, 4, 4, 5, scale);
    setRotationAngle(rightBoot, 0, 0, 0);

    leftBoot = new ModelRenderer(this, 28, 68);
    leftBoot.mirror = true;
    leftBoot.setRotationPoint(2, 12, 0);
    leftBoot.addBox(-2, 8, -3, 4, 4, 5, scale);
    setRotationAngle(leftBoot, 0, 0, 0);

    rightLeg = new ModelRenderer(this, 12, 68);
    rightLeg.setRotationPoint(-1.9f, 12, 0);
    rightLeg.addBox(-2, 0, -2, 4, 8, 4, scale);
    setRotationAngle(rightLeg, 0, 0, 0);

    leftLeg = new ModelRenderer(this, 12, 68);
    leftLeg.mirror = true;
    leftLeg.setRotationPoint(1.9f, 12, 0);
    leftLeg.addBox(-2, 0, -2, 4, 8, 4, scale);
    setRotationAngle(leftLeg, 0, 0, 0);


//    helmetBase.addChild(helmetBack);


//    chestplate.addChild(chestplate2);

    belt.addChild(rightLeg);
    belt.addChild(leftLeg);
  }

  private void setRotationAngle(ModelRenderer modelRenderer, float x, float y, float z)
  {
    modelRenderer.rotateAngleX = x;
    modelRenderer.rotateAngleY = y;
    modelRenderer.rotateAngleZ = z;
  }

  @Override
  public void render(@Nonnull  Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
    if (entity instanceof EntityArmorStand)
    {
      netHeadYaw = 0;
    }

    helmetBase.showModel = slot == EntityEquipmentSlot.HEAD;
    chestplate.showModel = slot == EntityEquipmentSlot.CHEST;
    rightArm.showModel = slot == EntityEquipmentSlot.CHEST;
    leftArm.showModel = slot == EntityEquipmentSlot.CHEST;
    rightLeg.showModel = slot == EntityEquipmentSlot.LEGS;
    leftLeg.showModel = slot == EntityEquipmentSlot.LEGS;
    rightBoot.showModel = slot == EntityEquipmentSlot.FEET;
    leftBoot.showModel = slot == EntityEquipmentSlot.FEET;
    bipedHeadwear.showModel = false;

    bipedHead = helmetBase;
    bipedBody = chestplate;
    bipedRightArm = rightArm;
    bipedLeftArm = leftArm;
    if (slot == EntityEquipmentSlot.LEGS)
    {
      bipedRightLeg = rightLeg;
      bipedLeftLeg = leftLeg;
    }
    else
    {
      bipedRightLeg = rightBoot;
      bipedLeftLeg = leftBoot;
    }

    super.render(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
  }

  //  @Override
//  public void render(Entity entityIn, float p_78088_2_, float limbSwing, float ageInTicks,
//      float netHeadYaw, float headPitch, float scale) {
//
//    this.setRotationAngles(p_78088_2_, limbSwing, ageInTicks, netHeadYaw, headPitch, scale,
//        entityIn);
//    GlStateManager.pushMatrix();
//
//    // FIXME?
//    if (entityIn instanceof EntityLivingBase) {
//      EntityLivingBase entityLiving = ((EntityLivingBase) entityIn);
//      this.isChild = entityLiving.isChild();
//      this.isSneak = entityLiving.isSneaking();
//      setArmPoses(entityLiving);
//    }
//
//    if (this.isChild) {
//      float f = 2.0F;
//      GlStateManager.scale(1.5F / f, 1.5F / f, 1.5F / f);
//      GlStateManager.translate(0.0F, 16.0F * scale, 0.0F);
//      this.bipedHead.render(scale);
//      GlStateManager.popMatrix();
//      GlStateManager.pushMatrix();
//      GlStateManager.scale(1.0F / f, 1.0F / f, 1.0F / f);
//      GlStateManager.translate(0.0F, 24.0F * scale, 0.0F);
//      this.bipedBody.render(scale);
//      this.bipedRightArm.render(scale);
//      this.bipedLeftArm.render(scale);
//      this.bipedRightLeg.render(scale);
//      this.bipedLeftLeg.render(scale);
//      this.bipedHeadwear.render(scale);
//    } else {
//      if (entityIn.isSneaking()) {
//        GlStateManager.translate(0.0F, 0.2F, 0.0F);
//      }
//
//      GlStateManager.color(1f, 0f, 0f);
//      GlStateManager.pushMatrix();
//      GlStateManager.translate(0.0f, 0.08f, 0.0f);
//      this.bipedHead.render(1.2f * scale);
//      GlStateManager.popMatrix();
//
//      GlStateManager.color(1f, 1f, 1f);
//      GlStateManager.pushMatrix();
////      GlStateManager.scale(1.2f, 1.2f, 1.2f);
//      this.bipedBody.render(1.2f * scale);
//      GlStateManager.popMatrix();
//
//      GlStateManager.color(0f, 1f, 0f);
//      this.bipedRightArm.render(scale);
//
//      GlStateManager.color(0f, 0f, 1f);
//      this.bipedLeftArm.render(scale);
//
//      GlStateManager.color(1f, 1f, 1f);
//      this.bipedRightLeg.render(scale);
//
//      this.bipedLeftLeg.render(scale);
//
//      this.bipedHeadwear.render(scale);
//    }
//
//    GlStateManager.popMatrix();
//  }
//
//  // Adapted from RenderPlayer.setModelVisibilities.
//  protected void setArmPoses(EntityLivingBase entity) {
//
//    ItemStack itemstack = entity.getHeldItemMainhand();
//    ItemStack itemstack1 = entity.getHeldItemOffhand();
//    ModelBiped.ArmPose modelbiped$armpose = ModelBiped.ArmPose.EMPTY;
//    ModelBiped.ArmPose modelbiped$armpose1 = ModelBiped.ArmPose.EMPTY;
//
//    if (itemstack != null) {
//      modelbiped$armpose = ModelBiped.ArmPose.ITEM;
//
//      if (entity.getItemInUseCount() > 0) {
//        EnumAction enumaction = itemstack.getItemUseAction();
//
//        if (enumaction == EnumAction.BLOCK) {
//          modelbiped$armpose = ModelBiped.ArmPose.BLOCK;
//        } else if (enumaction == EnumAction.BOW) {
//          modelbiped$armpose = ModelBiped.ArmPose.BOW_AND_ARROW;
//        }
//      }
//    }
//
//    if (itemstack1 != null) {
//      modelbiped$armpose1 = ModelBiped.ArmPose.ITEM;
//
//      if (entity.getItemInUseCount() > 0) {
//        EnumAction enumaction1 = itemstack1.getItemUseAction();
//
//        if (enumaction1 == EnumAction.BLOCK) {
//          modelbiped$armpose1 = ModelBiped.ArmPose.BLOCK;
//        }
//      }
//    }
//
//    if (entity.getPrimaryHand() == EnumHandSide.RIGHT) {
//      this.rightArmPose = modelbiped$armpose;
//      this.leftArmPose = modelbiped$armpose1;
//    } else {
//      this.rightArmPose = modelbiped$armpose1;
//      this.leftArmPose = modelbiped$armpose;
//    }
//  }
}
