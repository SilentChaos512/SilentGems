package net.silentchaos512.gems.client.render;

import com.google.common.collect.Lists;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.*;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms.TransformType;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.model.TRSRTransformation;
import net.silentchaos512.gems.SilentGems;
import net.silentchaos512.gems.api.lib.ToolPartPosition;
import net.silentchaos512.gems.init.ModItems;
import net.silentchaos512.gems.item.ToolRenderHelper;
import net.silentchaos512.gems.item.tool.ItemGemBow;
import net.silentchaos512.gems.item.tool.ItemGemScepter;
import net.silentchaos512.gems.lib.module.ModuleAprilTricks;
import net.silentchaos512.gems.util.ToolHelper;
import net.silentchaos512.lib.client.model.MultiLayerModelSL;
import org.apache.commons.lang3.tuple.Pair;
import org.lwjgl.util.vector.Vector3f;

import javax.vecmath.Matrix4f;
import java.util.ArrayList;
import java.util.List;

public class ToolModel extends MultiLayerModelSL {
    private static ModelManager modelManager = null;
    private final IBakedModel baseModel;

    private ItemStack tool;
    private boolean isGui = false;

    public ToolModel(IBakedModel baseModel) {
        super(baseModel);
        this.baseModel = baseModel;
    }

    public IBakedModel handleItemState(ItemStack stack) {
        tool = stack;
        return this;
    }

    @Override
    public List<BakedQuad> getQuads(IBlockState state, EnumFacing side, long rand) {
        if (tool == null) {
            return new ArrayList<>();
        }

        if (modelManager == null) {
            modelManager = Minecraft.getMinecraft().getRenderItem().getItemModelMesher().getModelManager();
        }

        List<BakedQuad> quads = Lists.newArrayList();
        ModelResourceLocation location;
        IBakedModel model;
        IBakedModel rodModel = null;

        // Invalid tools models.
        if (ToolHelper.getMaxDamage(tool) <= 0) {
            String name = tool.getItem().getRegistryName().getPath();
            location = new ModelResourceLocation(SilentGems.MODID + ":" + name.toLowerCase() + "/_error", "inventory");
            model = modelManager.getModel(location);
            if (model != null) {
                quads.addAll(model.getQuads(state, side, rand));
            }

            return quads;
        }

        for (ToolPartPosition partPos : ToolPartPosition.values()) {
            // Scepter rods on top of head.
            if (tool.getItem() instanceof ItemGemScepter) {
                if (partPos == ToolPartPosition.ROD) {
                    location = ToolRenderHelper.getInstance().getModel(tool, partPos);
                    rodModel = modelManager.getModel(location);
                    continue;
                } else if (partPos == ToolPartPosition.ROD_DECO) {
                    quads.addAll(rodModel.getQuads(state, side, rand));
                }
            }

            // Normal logic.
            location = ToolRenderHelper.getInstance().getModel(tool, partPos);
            if (location != null) {
                model = modelManager.getModel(location);
                if (model != null) {
                    quads.addAll(model.getQuads(state, side, rand));
                }
            }
        }

        if (ModuleAprilTricks.instance.isEnabled() && ModuleAprilTricks.instance.isRightDay()) {
            model = modelManager.getModel(ToolRenderHelper.getInstance().modelGooglyEyes);
            quads.addAll(model.getQuads(state, side, rand));
        }

        return quads;
    }

    @Override
    public boolean isAmbientOcclusion() {
        return baseModel.isAmbientOcclusion();
    }

    @Override
    public boolean isGui3d() {
        return baseModel.isGui3d();
    }

    @Override
    public boolean isBuiltInRenderer() {
        return baseModel.isBuiltInRenderer();
    }

    @Override
    public TextureAtlasSprite getParticleTexture() {
        return baseModel.getParticleTexture();
    }

    private static ItemTransformVec3f thirdPersonLeft = null;
    private static ItemTransformVec3f thirdPersonRight = null;
    private static ItemTransformVec3f firstPersonLeft = null;
    private static ItemTransformVec3f firstPersonRight = null;

    @Override
    public ItemCameraTransforms getItemCameraTransforms() {
        Vector3f rotation;
        Vector3f translation;
        Vector3f scale = new Vector3f(1f, 1f, 1f);

        // Third Person
        rotation = new Vector3f(0f, (float) -Math.PI / 2f, (float) Math.PI * 7f / 36f); // (0, 90, -35)
        translation = new Vector3f(0f, 5.5f, 2.5f);
        if (tool != null && tool.getItem() == ModItems.katana) {
            translation.y += 2.0f;
        }
        translation.scale(0.0625f);
        thirdPersonRight = new ItemTransformVec3f(rotation, translation, scale);

        rotation = new Vector3f(0f, (float) Math.PI / 2f, (float) -Math.PI * 7f / 36f); // (0, 90, -35)
        thirdPersonLeft = new ItemTransformVec3f(rotation, translation, scale);

        // First Person
        rotation = new Vector3f(0f, (float) -Math.PI * 1f / 2f, (float) Math.PI * 5f / 36f);
        translation = new Vector3f(1.13f, 3.2f, 1.13f);
        if (tool != null && tool.getItem() == ModItems.katana) {
            translation.y += 1.5f;
        }
        translation.scale(0.0625f);
        scale = new Vector3f(0.68f, 0.68f, 0.68f);
        firstPersonRight = new ItemTransformVec3f(rotation, translation, scale);

        rotation = new Vector3f(0f, (float) Math.PI * 1f / 2f, (float) -Math.PI * 5f / 36f);
        firstPersonLeft = new ItemTransformVec3f(rotation, translation, scale);

        // Head and GUI are default.
        return new ItemCameraTransforms(thirdPersonLeft, thirdPersonRight, firstPersonLeft,
                firstPersonRight, ItemTransformVec3f.DEFAULT, ItemTransformVec3f.DEFAULT,
                ItemTransformVec3f.DEFAULT, ItemTransformVec3f.DEFAULT);
    }

    @Override
    public Pair<? extends IBakedModel, Matrix4f> handlePerspective(TransformType cameraTransformType) {
        boolean isBow = tool != null && tool.getItem() instanceof ItemGemBow;
        Matrix4f matrix = new Matrix4f();
        switch (cameraTransformType) {
            case FIRST_PERSON_RIGHT_HAND:
                matrix = getMatrix(getItemCameraTransforms().firstperson_right);
                break;
            case FIRST_PERSON_LEFT_HAND:
                matrix = getMatrix(getItemCameraTransforms().firstperson_left);
                break;
            case GUI:
                matrix = getMatrix(getItemCameraTransforms().gui);
                isGui = true;
                break;
            case HEAD:
                matrix = getMatrix(getItemCameraTransforms().head);
                break;
            case THIRD_PERSON_RIGHT_HAND:
                matrix = getMatrix(getItemCameraTransforms().thirdperson_right);
                if (isBow)
                    matrix.setTranslation(new javax.vecmath.Vector3f(0f, 0f, 0.2f));
                break;
            case THIRD_PERSON_LEFT_HAND:
                matrix = getMatrix(getItemCameraTransforms().thirdperson_left);
                if (isBow)
                    matrix.setTranslation(new javax.vecmath.Vector3f(0f, 0f, 0.2f));
                break;
            case GROUND:
                matrix = getMatrix(getItemCameraTransforms().ground);
                matrix.setScale(matrix.getScale() * 0.5f);
                break;
            case FIXED:
                matrix = getMatrix(getItemCameraTransforms().fixed);
                // Fix item frame rotation
                matrix.rotY((float) Math.PI);
                break;
            default:
                break;
        }
        // TODO: Getter function for scale?
        if (tool != null && tool.getItem() == ModItems.katana) {
            if (cameraTransformType != TransformType.GUI) {
                matrix.setScale(matrix.getScale() * 1.3f);
            }
        } else if (tool != null && tool.getItem() == ModItems.scepter) {
            if (cameraTransformType != TransformType.GUI) {
                matrix.setScale(matrix.getScale() * 1.2f);
            }
        } else if (tool != null && tool.getItem() == ModItems.paxel) {
            if (cameraTransformType != TransformType.GUI) {
                if (cameraTransformType == TransformType.FIRST_PERSON_LEFT_HAND
                        || cameraTransformType == TransformType.FIRST_PERSON_RIGHT_HAND)
                    matrix.setScale(matrix.getScale() * 1.1f);
                else
                    matrix.setScale(matrix.getScale() * 1.2f);
            }
        } else if (tool != null && tool.getItem() == ModItems.dagger) {
            if (cameraTransformType != TransformType.GUI) {
                matrix.setScale(matrix.getScale() * 0.85f);
            }
        }
        return Pair.of((IBakedModel) this, matrix);
    }

    @Override
    public ItemOverrideList getOverrides() {
        return ToolItemOverrideHandler.INSTANCE;
    }

    // Was in ForgeHooksClient, removed in 2703.
    public static Matrix4f getMatrix(ItemTransformVec3f transform) {
        javax.vecmath.Matrix4f m = new javax.vecmath.Matrix4f(), t = new javax.vecmath.Matrix4f();
        m.setIdentity();
        m.setTranslation(TRSRTransformation.toVecmath(transform.translation));
        t.setIdentity();
        t.rotY(transform.rotation.y);
        m.mul(t);
        t.setIdentity();
        t.rotX(transform.rotation.x);
        m.mul(t);
        t.setIdentity();
        t.rotZ(transform.rotation.z);
        m.mul(t);
        t.setIdentity();
        t.m00 = transform.scale.x;
        t.m11 = transform.scale.y;
        t.m22 = transform.scale.z;
        m.mul(t);
        return m;
    }
}
