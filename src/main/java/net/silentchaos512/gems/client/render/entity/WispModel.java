package net.silentchaos512.gems.client.render.entity;

import com.google.common.collect.ImmutableList;
import net.minecraft.client.renderer.entity.model.SegmentedModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.util.math.MathHelper;
import net.silentchaos512.gems.entity.AbstractWispEntity;

import java.util.Arrays;

public class WispModel<T extends AbstractWispEntity> extends SegmentedModel<T> {
    private final ModelRenderer[] satellites = new ModelRenderer[12];
    private final ModelRenderer mainBody;
    private final ImmutableList<ModelRenderer> modelParts;

    public WispModel() {
        for (int i = 0; i < this.satellites.length; ++i) {
            this.satellites[i] = new ModelRenderer(this, 0, 16);
            this.satellites[i].addBox(0f, 0f, 0f, 2, 2, 2);
        }

        this.mainBody = new ModelRenderer(this, 0, 0);
        this.mainBody.addBox(-4f, -1f, -4f, 8, 8, 8);

        ImmutableList.Builder<ModelRenderer> builder = ImmutableList.builder();
        builder.add(this.mainBody);
        builder.addAll(Arrays.asList(this.satellites));
        this.modelParts = builder.build();
    }

    @Override
    public Iterable<ModelRenderer> getParts() {
        return this.modelParts;
    }

    @Override
    public void render(T entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        float f = ((float)Math.PI / 4F) + ageInTicks * (float)Math.PI * 0.03F;

        for (int i = 0; i < this.satellites.length; ++i) {
            this.satellites[i].rotationPointY = 2.0F + MathHelper.cos(((float)(i * 2) + ageInTicks) * 0.25F);
            this.satellites[i].rotationPointX = MathHelper.cos(f) * 7.0F;
            this.satellites[i].rotationPointZ = MathHelper.sin(f) * 7.0F;
            f += 0.525f;
        }

//        this.mainBody.rotateAngleY = netHeadYaw * ((float)Math.PI / 180F);
//        this.mainBody.rotateAngleX = headPitch * ((float)Math.PI / 180F);
        this.mainBody.rotateAngleY = -ageInTicks * (float)Math.PI * -0.05F;
    }
}
