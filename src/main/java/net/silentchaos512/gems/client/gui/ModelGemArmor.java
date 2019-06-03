package net.silentchaos512.gems.client.gui;

import it.unimi.dsi.fastutil.ints.IntArrays;
import net.minecraft.client.model.ModelArmorStandArmor;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelBox;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.silentchaos512.gems.api.lib.EnumDecoPos;
import net.silentchaos512.gems.client.render.models.ArmorModelRenderer;
import net.silentchaos512.gems.util.Cache;

import javax.annotation.Nonnull;

public class ModelGemArmor extends ModelBiped {
    protected static final Cache<int[], ModelGemArmor> models;
    protected static final ModelBiped modelArmorStand = new ModelArmorStandArmor();

    protected static ModelBox helmetBase;
    protected static ModelBox helmetLayer0;
    protected static ModelBox helmetLayer1;
    protected static ModelBox helmetLayer2;
    protected static ModelBox helmetLayer3;

    protected static ModelBox chestBase;
    protected static ModelBox chestLayer0;
    protected static ModelBox chestLayer1;
    protected static ModelBox leftArmBase;
    protected static ModelBox leftArmLayer0;
    protected static ModelBox rightArmBase;
    protected static ModelBox rightArmLayer0;

    protected static ModelBox beltBase;
    protected static ModelBox beltLayer0;
    protected static ModelBox leftLegBase;
    protected static ModelBox leftLegLayer0;
    protected static ModelBox rightLegBase;
    protected static ModelBox rightLegLayer0;

    protected static ModelBox leftBootBase;
    protected static ModelBox leftBootLayer0;
    protected static ModelBox leftBootLayer1;
    protected static ModelBox rightBootBase;
    protected static ModelBox rightBootLayer0;
    protected static ModelBox rightBootLayer1;

    static {
        models = new Cache<>(IntArrays.HASH_STRATEGY, ModelGemArmor::new, new ModelGemArmor(new int[]{0, 0, 0, 0}), 256, 5000L, ModelGemArmor::dispose);
        ModelRenderer textureSize = new ModelRenderer(new ModelBase() {
            {
                textureWidth = 128;
                textureHeight = 64;
            }
        });
        float helmetScale = 0.25f;
        float scale = 0.2f;

        //modelBase, texX, texY, xOff, yOff, zOff, dx, dy, dz, scale, (mirrored)
        helmetBase = new ModelBox(textureSize, 0, 0, -4, -8, -4.5f, 8, 6, 9, helmetScale);
        helmetLayer0 = new ModelBox(textureSize, 34, 0, -4, -8, -4.5f, 8, 6, 9, helmetScale);
        helmetLayer1 = new ModelBox(textureSize, 68, 0, -4, -8, -4.5f, 8, 6, 9, helmetScale);
        helmetLayer2 = new ModelBox(textureSize, 0, 15, -4, -8, -4.5f, 8, 6, 9, helmetScale);
        helmetLayer3 = new ModelBox(textureSize, 34, 15, -4, -8, -4.5f, 8, 6, 9, helmetScale);

        chestBase = new ModelBox(textureSize, 0, 30, -4.5f, 0, -3.5f, 9, 11, 6, scale);
        chestLayer0 = new ModelBox(textureSize, 30, 30, -4.5f, 0, -3.5f, 9, 11, 6, scale);
        chestLayer1 = new ModelBox(textureSize, 60, 30, -4.5f, 0, -3.5f, 9, 11, 6, scale);
        leftArmBase = new ModelBox(textureSize, 0, 47, -2, -3.5f, -3, 6, 6, 6, scale, true);
        leftArmLayer0 = new ModelBox(textureSize, 24, 47, -2, -3.5f, -3, 6, 6, 6, scale, true);
        rightArmBase = new ModelBox(textureSize, 0, 47, -4, -3.5f, -3, 6, 6, 6, scale);
        rightArmLayer0 = new ModelBox(textureSize, 24, 47, -4, -3.5f, -3, 6, 6, 6, scale);

        beltBase = new ModelBox(textureSize, 80, 47, -4, 8.5f, -2, 8, 4, 4, scale);
        beltLayer0 = new ModelBox(textureSize, 104, 47, -4, 8.5f, -2, 8, 4, 4, scale);
        leftLegBase = new ModelBox(textureSize, 48, 47, -2, -0.25f, -2, 4, 8, 4, scale, true);
        leftLegLayer0 = new ModelBox(textureSize, 64, 47, -2, -0.25f, -2, 4, 8, 4, scale, true);
        rightLegBase = new ModelBox(textureSize, 48, 47, -2, -0.25f, -2, 4, 8, 4, scale);
        rightLegLayer0 = new ModelBox(textureSize, 64, 47, -2, -0.25f, -2, 4, 8, 4, scale);

        leftBootBase = new ModelBox(textureSize, 71, 18, -2, 8, -3, 4, 4, 5, scale, true);
        leftBootLayer0 = new ModelBox(textureSize, 89, 18, -2, 8, -3, 4, 4, 5, scale, true);
        leftBootLayer1 = new ModelBox(textureSize, 107, 18, -2, 8, -3, 4, 4, 5, scale, true);
        rightBootBase = new ModelBox(textureSize, 71, 18, -2, 8, -3, 4, 4, 5, scale);
        rightBootLayer0 = new ModelBox(textureSize, 89, 18, -2, 8, -3, 4, 4, 5, scale);
        rightBootLayer1 = new ModelBox(textureSize, 107, 18, -2, 8, -3, 4, 4, 5, scale);
    }

    public static ModelGemArmor getModel(int[] colors) {
        return models.get(colors);
    }

    private final ArmorModelRenderer helmet;
    private final ArmorModelRenderer chestplate;
    private final ArmorModelRenderer leftArm;
    private final ArmorModelRenderer rightArm;
    private final ArmorModelRenderer belt;
    private final ArmorModelRenderer leftLeg;
    private final ArmorModelRenderer rightLeg;
    private final ArmorModelRenderer leftBoot;
    private final ArmorModelRenderer rightBoot;

    //TODO chestplate sticks out at the front
    public ModelGemArmor(int[] colors) {
        textureWidth = 128;
        textureHeight = 64;

        helmet = new ArmorModelRenderer(this);
        helmet.addChild(helmetBase);
        helmet.addChild(helmetLayer0, colors[EnumDecoPos.SOUTH.ordinal()]);
        helmet.addChild(helmetLayer1, colors[EnumDecoPos.WEST.ordinal()]);
        helmet.addChild(helmetLayer2, colors[EnumDecoPos.NORTH.ordinal()]);
        helmet.addChild(helmetLayer3, colors[EnumDecoPos.EAST.ordinal()]);
        bipedHead = helmet;
        bipedHeadwear.isHidden = true;

        chestplate = new ArmorModelRenderer(this);
        chestplate.addChild(chestBase);
        chestplate.addChild(chestLayer0, colors[EnumDecoPos.NORTH.ordinal()]);
        chestplate.addChild(chestLayer1, colors[EnumDecoPos.SOUTH.ordinal()]);

        leftArm = new ArmorModelRenderer(this);
        leftArm.setRotationPoint(0, 3, 0);
        leftArm.addChild(leftArmBase, colors[EnumDecoPos.NORTH.ordinal()]);
        leftArm.addChild(leftArmLayer0, colors[EnumDecoPos.EAST.ordinal()]);
        bipedLeftArm = leftArm;

        rightArm = new ArmorModelRenderer(this);
        rightArm.setRotationPoint(0, 3, 0);
        rightArm.addChild(rightArmBase, colors[EnumDecoPos.NORTH.ordinal()]);
        rightArm.addChild(rightArmLayer0, colors[EnumDecoPos.WEST.ordinal()]);
        bipedRightArm = rightArm;

        belt = new ArmorModelRenderer(this);
        belt.addChild(beltBase, colors[EnumDecoPos.SOUTH.ordinal()]);
        belt.addChild(beltLayer0, colors[EnumDecoPos.NORTH.ordinal()]);

        leftLeg = new ArmorModelRenderer(this);
        leftLeg.setRotationPoint(1.9f, 12, 0);
        leftLeg.addChild(leftLegBase, colors[EnumDecoPos.SOUTH.ordinal()]);
        leftLeg.addChild(leftLegLayer0, colors[EnumDecoPos.EAST.ordinal()]);

        rightLeg = new ArmorModelRenderer(this);
        rightLeg.setRotationPoint(-1.9f, 12, 0);
        rightLeg.addChild(rightLegBase, colors[EnumDecoPos.SOUTH.ordinal()]);
        rightLeg.addChild(rightLegLayer0, colors[EnumDecoPos.WEST.ordinal()]);

        leftBoot = new ArmorModelRenderer(this);
        leftBoot.setRotationPoint(2, 12, 0);
        leftBoot.addChild(leftBootBase, colors[EnumDecoPos.SOUTH.ordinal()]);
        leftBoot.addChild(leftBootLayer0, colors[EnumDecoPos.EAST.ordinal()]);
        leftBoot.addChild(leftBootLayer1, colors[EnumDecoPos.NORTH.ordinal()]);

        rightBoot = new ArmorModelRenderer(this);
        rightBoot.setRotationPoint(-2, 12, 0);
        rightBoot.addChild(rightBootBase, colors[EnumDecoPos.SOUTH.ordinal()]);
        rightBoot.addChild(rightBootLayer0, colors[EnumDecoPos.WEST.ordinal()]);
        rightBoot.addChild(rightBootLayer1, colors[EnumDecoPos.NORTH.ordinal()]);
    }

    @Override
    public void render(@Nonnull Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
        EntityEquipmentSlot slot = bipedHead.showModel ? EntityEquipmentSlot.HEAD : !bipedBody.showModel ? EntityEquipmentSlot.FEET : bipedLeftLeg.showModel && bipedRightLeg.showModel ? EntityEquipmentSlot.LEGS : EntityEquipmentSlot.CHEST;
        switch (slot) {
            case HEAD:
                break;
            case CHEST:
                bipedBody = chestplate;
                chestplate.showModel = true;
                break;
            case LEGS:
                bipedBody = belt;
                belt.showModel = true;
                bipedLeftLeg = leftLeg;
                leftLeg.showModel = true;
                bipedRightLeg = rightLeg;
                rightLeg.showModel = true;
                break;
            case FEET:
                bipedLeftLeg = leftBoot;
                leftBoot.showModel = true;
                bipedRightLeg = rightBoot;
                leftBoot.showModel = true;
                break;
        }

        GlStateManager.enableBlend();
        super.render(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
        GlStateManager.disableBlend();
    }

    @Override
    public void setRotationAngles(float swing, float amount, float age, float yaw, float pitch, float scale, Entity entity) {
        if (entity instanceof EntityArmorStand) {
            fixArmorStand(entity, swing, amount, age, yaw, pitch, scale);
        } else {
            belt.rotationPointY = 0;
            super.setRotationAngles(swing, amount, age, yaw, pitch, scale, entity);
        }
    }

    protected void fixArmorStand(Entity stand, float swing, float swingAmount, float age, float yaw, float pitch, float scale) {
        modelArmorStand.setRotationAngles(swing, swingAmount, age, yaw, pitch, scale, stand);
        copyModelAngles(modelArmorStand.bipedHead, bipedHead);
        copyModelAngles(modelArmorStand.bipedBody, bipedBody);
        copyModelAngles(modelArmorStand.bipedLeftArm, bipedLeftArm);
        copyModelAngles(modelArmorStand.bipedRightArm, bipedRightArm);
        copyModelAngles(modelArmorStand.bipedLeftLeg, bipedLeftLeg);
        copyModelAngles(modelArmorStand.bipedRightLeg, bipedRightLeg);
        belt.rotationPointY = -1f;
    }

    protected void dispose() {
        helmet.dispose();
        chestplate.dispose();
        leftArm.dispose();
        rightArm.dispose();
        belt.dispose();
        leftLeg.dispose();
        rightLeg.dispose();
        leftBoot.dispose();
        rightBoot.dispose();
    }
}
