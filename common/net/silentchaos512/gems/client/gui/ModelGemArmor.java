package net.silentchaos512.gems.client.gui;

import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.silentchaos512.gems.api.lib.EnumDecoPos;
import net.silentchaos512.gems.client.render.models.ArmorModelRenderer;
import net.silentchaos512.lib.util.Color;

import javax.annotation.Nonnull;

public class ModelGemArmor extends ModelBiped {

  private final ArmorModelRenderer helmetBase;
  private final ModelRenderer helmetLayer0;
  private final ModelRenderer helmetLayer1;
  private final ModelRenderer helmetLayer2;
  private final ModelRenderer helmetLayer3;
//  private final ModelRenderer helmetLayer4;
//  private final ModelRenderer helmetBack;

  private final ArmorModelRenderer chestplateBase;
  private final ArmorModelRenderer chestplateLayer0;
  private final ModelRenderer chestplateLayer1;
//  private final ModelRenderer chestplate2;

  private final ArmorModelRenderer rightArmBase;
  private final ModelRenderer rightArmLayer0;

  private final ArmorModelRenderer leftArmBase;
  private final ModelRenderer leftArmLayer0;

  private final ArmorModelRenderer belt;
  private final ModelRenderer beltLayer0;

  private final ArmorModelRenderer rightLegBase;
  private final ModelRenderer rightLegLayer0;

  private final ArmorModelRenderer leftLegBase;
  private final ModelRenderer leftLegLayer0;

  private final ArmorModelRenderer rightBootBase;
  private final ModelRenderer rightBootLayer0;
  private final ModelRenderer rightBootLayer1;

  private final ArmorModelRenderer leftBootBase;
  private final ModelRenderer leftBootLayer0;
  private final ModelRenderer leftBootLayer1;

  private final EntityEquipmentSlot slot;
  private final int[] colors;

  public ModelGemArmor(EntityEquipmentSlot slot, int[] colors)
  {
    this.slot = slot;
    this.colors = colors.clone();

    textureWidth = 256;
    textureHeight = 128;
    float scale = 0.2f;
    float helmetScale = 0.25f;

    helmetBase = new ArmorModelRenderer(this, 64, 15);
    helmetBase.setRotationPoint(0, 0, 0);
    helmetBase.addBox(-4, -8, -4.5f, 8, 6, 9, helmetScale);
    setRotationAngle(helmetBase, 0.08726646259971647f, 0, 0);

    helmetLayer0 = new ModelRenderer(this, 0, 0);
    helmetLayer0.setRotationPoint(0, 0, 0);
    helmetLayer0.addBox(-4, -8, -4.5f, 8, 6, 9, helmetScale);
    setRotationAngle(helmetLayer0, 0, 0, 0);

    helmetLayer1 = new ModelRenderer(this, 34, 0);
    helmetLayer1.setRotationPoint(0, 0, 0);
    helmetLayer1.addBox(-4, -8, -4.5f, 8, 6, 9, helmetScale);
    setRotationAngle(helmetLayer1, 0, 0, 0);

    helmetLayer2 = new ModelRenderer(this, 68, 0);
    helmetLayer2.setRotationPoint(0, 0, 0);
    helmetLayer2.addBox(-4, -8, -4.5f, 8, 6, 9, helmetScale);
    setRotationAngle(helmetLayer2, 0, 0, 0);

    helmetLayer3 = new ModelRenderer(this, 102, 0);
    helmetLayer3.setRotationPoint(0, 0, 0);
    helmetLayer3.addBox(-4, -8, -4.5f, 8, 6, 9, helmetScale);
    setRotationAngle(helmetLayer3, 0, 0, 0);

//    helmetLayer4 = new ModelRenderer(this, 64, 15);
//    helmetLayer4.setRotationPoint(0, 0, 0);
//    helmetLayer4.addBox(-4, -8, -4.5f, 8, 6, 9, helmetScale);
//    setRotationAngle(helmetLayer4, 0, 0, 0);

//    helmetBack = new ModelRenderer(this, 0, 0);
//    helmetBack.setRotationPoint(0, 0, 0);
//    helmetBack.addBox(-4, -8, 4.5f, 8, 3, 4, scale);
//    setRotationAngle(helmetBack, 0.08726646259971647f, 0, 0);

    chestplateBase = new ArmorModelRenderer(this, 0, 15);
    chestplateBase.setRotationPoint(0, 0, 0);
    chestplateBase.addBox(-4.5f, 0, -3.5f, 9, 11, 6, scale);
    setRotationAngle(chestplateBase, 0.08726646259971647f, 0, 0);

    chestplateLayer0 = new ArmorModelRenderer(this, 0, 32);
    chestplateLayer0.setRotationPoint(0, 0, 0);
    chestplateLayer0.addBox(-4.5f, 0, -3.5f, 9, 11, 6, scale);
    setRotationAngle(chestplateLayer0, 0, 0, 0);

    chestplateLayer1 = new ArmorModelRenderer(this, 30, 15);
    chestplateLayer1.setRotationPoint(0, 0, 0);
    chestplateLayer1.addBox(-4.5f, 0, -3.5f, 9, 11, 6, scale);
    setRotationAngle(chestplateLayer1, 0, 0, 0);

    rightArmBase = new ArmorModelRenderer(this, 30, 32);
    rightArmBase.setRotationPoint(0f, 3.0f, 0f);
    rightArmBase.addBox(-4.0f, -3.5f, -3.0f, 6, 6, 6, scale);
//    rightArmBase.addBox(-4.0f, -0.5f, -3.0f, 6, 6, 6, scale);
//    setRotationAngle(rightArmBase, 0f, 0f, 0.5f);
    setRotationAngle(rightArmBase, 0f, 0f, 0.17453292519943295f);

    rightArmLayer0 = new ModelRenderer(this, 54, 32);
    rightArmLayer0.setRotationPoint(0f, 3.0f, 0f);
    rightArmLayer0.addBox(-4.0f, -6.5f, -3.0f, 6, 6, 6, scale*1.05f);
    setRotationAngle(rightArmLayer0, 0f, 0f, 0f);

    leftArmBase = new ArmorModelRenderer(this, 30, 32);
    leftArmBase.mirror = true;
    leftArmBase.setRotationPoint(0f, 3.0f, 0f);
    leftArmBase.addBox(-2.0f, -3.5f, -3.0f, 6, 6, 6, scale);

    leftArmLayer0 = new ModelRenderer(this, 54, 32);
    leftArmLayer0.mirror = true;
    leftArmLayer0.setRotationPoint(0, 3.0f, 0);
    leftArmLayer0.addBox(-2.0f, -6.5f, -3.0f, 6, 6, 6, scale*1.05f);
    setRotationAngle(leftArmLayer0, 0f, 0f, 0f);

//    leftArm = new ModelRenderer(this, 0, 68);
//    leftArm.mirror = true;
//    leftArm.setRotationPoint(5f, 2.0f, 0f);
//    leftArm.addBox(1.0f, 3.0f, -2.0f, 2, 6, 4, scale);
//    setRotationAngle(leftArm, 0f, 0f, -0.17453292519943295f);

//    belt = new ArmorModelRenderer(this, 0, 49);
//    belt.setRotationPoint(0, 0, 0);
//    belt.addBox(-4.0f, 9.5f, -2f, 8, 4, 4, scale);

    belt = new ArmorModelRenderer(this, 0, 49);
    belt.setRotationPoint(0, 0, 0);
    belt.addBox(-4.0f, 8.5f, -2f, 8, 4, 4, scale);

    beltLayer0 = new ModelRenderer(this, 24, 49);
    beltLayer0.setRotationPoint(0, 0, 0);
    beltLayer0.addBox(-4.0f, 8.5f, -2f, 8, 4, 4, scale * 1.05f);

    rightLegBase = new ArmorModelRenderer(this, 0, 57);
    rightLegBase.setRotationPoint(-1.9f, 12, 0);
    rightLegBase.addBox(-2, 0, -2, 4, 8, 4, scale);
    setRotationAngle(rightLegBase, 0, 0, 0);

    rightLegLayer0 = new ModelRenderer(this, 16, 57);
    rightLegLayer0.setRotationPoint(-1.9f, 12, 0);
    rightLegLayer0.addBox(-0.1f, -12, -2, 4, 8, 4, scale * 1.05f);
    setRotationAngle(rightLegLayer0, 0, 0, 0);

    leftLegBase = new ArmorModelRenderer(this, 0, 57);
    leftLegBase.mirror = true;
    leftLegBase.setRotationPoint(1.9f, 12, 0);
    leftLegBase.addBox(-2, 0, -2, 4, 8, 4, scale);
    setRotationAngle(leftLegBase, 0, 0, 0);

    leftLegLayer0 = new ModelRenderer(this, 16, 57);
    leftLegLayer0.mirror = true;
    leftLegLayer0.setRotationPoint(1.9f, 12f, 0);
    leftLegLayer0.addBox(-3.9f, -12, -2, 4, 8, 4, scale * 1.05f);
    setRotationAngle(leftLegLayer0, 0, 0, 0);

    rightBootBase = new ArmorModelRenderer(this, 0, 69);
    rightBootBase.setRotationPoint(-2, 12, 0);
    rightBootBase.addBox(-2, 8, -3, 4, 4, 5, scale);
    setRotationAngle(rightBootBase, 0, 0, 0);

    rightBootLayer0 = new ModelRenderer(this, 18, 69);
    rightBootLayer0.setRotationPoint(-2,12,0);
    rightBootLayer0.addBox(0, -4, -3, 4, 4, 5, scale * 1.05f);
    setRotationAngle(rightBootLayer0, 0, 0, 0);

    rightBootLayer1 = new ModelRenderer(this, 36, 69);
    rightBootLayer1.setRotationPoint(-2, 12, 0);
    rightBootLayer1.addBox(0, -4, -3, 4, 4, 5, scale * 1.05f);
    setRotationAngle(rightBootLayer1, 0, 0, 0);

    leftBootBase = new ArmorModelRenderer(this, 0, 69);
    leftBootBase.mirror = true;
    leftBootBase.setRotationPoint(2, 12, 0);
    leftBootBase.addBox(-2, 8, -3, 4, 4, 5, scale);
    setRotationAngle(leftBootBase, 0, 0, 0);

    leftBootLayer0 = new ModelRenderer(this, 18, 69);
    leftBootLayer0.mirror = true;
    leftBootLayer0.setRotationPoint(2, 12, 0);
    leftBootLayer0.addBox(-4, -4, -3, 4, 4, 5, scale * 1.05f);
    setRotationAngle(leftBootLayer0, 0, 0, 0);

    leftBootLayer1 = new ModelRenderer(this, 36, 69);
    leftBootLayer1.mirror = true;
    leftBootLayer1.setRotationPoint(2, 12, 0);
    leftBootLayer1.addBox(-4, -4, -3, 4, 4, 5, scale * 1.05f);
    setRotationAngle(leftBootLayer1, 0, 0, 0);


    helmetBase.addChild(helmetLayer0, colors[EnumDecoPos.SOUTH.ordinal()]);
    helmetBase.addChild(helmetLayer1,colors[EnumDecoPos.WEST.ordinal()]);
    helmetBase.addChild(helmetLayer2, colors[EnumDecoPos.NORTH.ordinal()]);
    helmetBase.addChild(helmetLayer3, colors[EnumDecoPos.EAST.ordinal()]);

    chestplateLayer0.addChild(chestplateLayer1, colors[EnumDecoPos.SOUTH.ordinal()]);
    chestplateBase.addChild(chestplateLayer0, colors[EnumDecoPos.NORTH.ordinal()]);

    rightArmBase.addChild(rightArmLayer0, colors[EnumDecoPos.WEST.ordinal()]);

    leftArmBase.addChild(leftArmLayer0, colors[EnumDecoPos.EAST.ordinal()]);

//    chestplate.addChild(chestplate2);

    rightLegBase.addChild(rightLegLayer0, colors[EnumDecoPos.WEST.ordinal()]);

    leftLegBase.addChild(leftLegLayer0, colors[EnumDecoPos.EAST.ordinal()]);

    rightBootBase.addChild(rightBootLayer0, colors[EnumDecoPos.WEST.ordinal()]);
    rightBootBase.addChild(rightBootLayer1, colors[EnumDecoPos.NORTH.ordinal()]);

    leftBootBase.addChild(leftBootLayer0, colors[EnumDecoPos.EAST.ordinal()]);
    leftBootBase.addChild(leftBootLayer1, colors[EnumDecoPos.NORTH.ordinal()]);

    belt.addChild(beltLayer0,colors[EnumDecoPos.NORTH.ordinal()]);
    belt.addChild(rightLegBase,colors[EnumDecoPos.SOUTH.ordinal()]);
    belt.addChild(leftLegBase,colors[EnumDecoPos.SOUTH.ordinal()]);
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
//    helmetLayer1.showModel = slot == EntityEquipmentSlot.HEAD;
//    helmetLayer2.showModel = slot == EntityEquipmentSlot.HEAD;
//    helmetLayer3.showModel = slot == EntityEquipmentSlot.HEAD;
//    helmetLayer4.showModel = slot == EntityEquipmentSlot.HEAD;

    chestplateBase.showModel = slot == EntityEquipmentSlot.CHEST;
    rightArmBase.showModel = slot == EntityEquipmentSlot.CHEST;
    leftArmBase.showModel = slot == EntityEquipmentSlot.CHEST;
    rightLegBase.showModel = slot == EntityEquipmentSlot.LEGS;
    leftLegBase.showModel = slot == EntityEquipmentSlot.LEGS;
    rightBootBase.showModel = slot == EntityEquipmentSlot.FEET;
    leftBootBase.showModel = slot == EntityEquipmentSlot.FEET;
    bipedHeadwear.showModel = false;

    bipedHead = helmetBase;
    bipedBody = chestplateBase;
    bipedRightArm = rightArmBase;
    bipedLeftArm = leftArmBase;
    if (slot == EntityEquipmentSlot.LEGS)
    {
      bipedRightLeg = rightLegBase;
      bipedLeftLeg = leftLegBase;
    }
    else
    {
      bipedRightLeg = rightBootBase;
      bipedLeftLeg = leftBootBase;
    }

    this.specialRender(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
  }

  private void specialRender(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale)
  {
    this.setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale, entity);

    GlStateManager.pushMatrix();

    GlStateManager.enableBlend();

    if (this.isChild)
    {
      float f = 2.0f;
      GlStateManager.scale(1.5f / f, 1.5f / f, 1.5f / f);
      GlStateManager.translate(0.0f, 16.0f * scale, 0.0f);
      renderHelmet(scale);
      //this.bipedHead.render(scale);
      GlStateManager.popMatrix();

      GlStateManager.pushMatrix();
      GlStateManager.scale(1.0f / f, 1.0f / f, 1.0f / f );
      GlStateManager.translate(0.0f, 24.0f * scale, 0.0f);
      this.renderChest(scale);
      this.renderArms(scale);
      this.renderLegs(scale);
//      this.bipedBody.render(scale);
//      this.bipedRightArm.render(scale);
//      this.bipedLeftArm.render(scale);
//      this.bipedRightLeg.render(scale);
//      this.bipedLeftLeg.render(scale);
      this.bipedHeadwear.render(scale);
    }
    else
    {
      if (entity.isSneaking())
      {
        GlStateManager.translate(0.0f, 0.2f, 0.0f);
      }

      renderHelmet(scale);
      renderChest(scale);
      renderArms(scale);
      renderLegs(scale);
      this.bipedHeadwear.render(scale);
    }

    GlStateManager.disableBlend();
    GlStateManager.popMatrix();
  }

  private void setRenderColor(int color)
  {
    Color c = new Color(color);
    GlStateManager.color(c.getRed(), c.getGreen(), c.getBlue(), c.getAlpha());
//    GlStateManager.color(((color >> 0)& 0xFF)/ (0xFF), ((color >> 8) & 0xFF) / 0xFF, ((color >> 16) & 0xFF) / 0xFF, 1.0f);
  }

  private void renderHelmet(float scale)
  {
    GlStateManager.pushMatrix();
    GlStateManager.enableBlend();
//    setRenderColor(colors[EnumDecoPos.SOUTH.ordinal()]);
    setRenderColor(Color.WHITE.getColor());
    this.bipedHead.render(scale);
    GlStateManager.disableBlend();
    GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
    GlStateManager.popMatrix();
  }

  private void renderChest(float scale)
  {
    GlStateManager.pushMatrix();
    GlStateManager.enableBlend();
    setRenderColor(Color.WHITE.getColor());
    this.bipedBody.render(scale);
    GlStateManager.disableBlend();
    GlStateManager.popMatrix();
  }

  private void renderArms(float scale)
  {
    GlStateManager.pushMatrix();
    GlStateManager.enableBlend();
    setRenderColor(colors[EnumDecoPos.NORTH.ordinal()]);
    this.bipedRightArm.render(scale);

    setRenderColor(colors[EnumDecoPos.NORTH.ordinal()]);
    this.bipedLeftArm.render(scale);
    GlStateManager.disableBlend();
    GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
    GlStateManager.popMatrix();
  }

  private void renderLegs(float scale)
  {
    GlStateManager.pushMatrix();
    GlStateManager.enableBlend();
    setRenderColor(Color.WHITE.getColor());
    if (this.leftLegBase.showModel && this.rightLegBase.showModel)
    {
      setRenderColor(colors[EnumDecoPos.SOUTH.ordinal()]);
      this.belt.render(scale);
//      this.bipedRightLeg.render(scale);
//      this.bipedLeftLeg.render(scale);
    }
    else
    {
      if (this.leftLegBase.showModel || this.rightLegBase.showModel)
      {
        this.bipedRightLeg.render(scale);
        this.bipedLeftLeg.render(scale);
      }
      else
      {
        setRenderColor(colors[EnumDecoPos.SOUTH.ordinal()]);
        this.bipedRightLeg.render(scale);
        setRenderColor(colors[EnumDecoPos.SOUTH.ordinal()]);
        this.bipedLeftLeg.render(scale);
      }
    }
    GlStateManager.disableBlend();
    setRenderColor(Color.WHITE.getColor());
    GlStateManager.popMatrix();
  }
}
