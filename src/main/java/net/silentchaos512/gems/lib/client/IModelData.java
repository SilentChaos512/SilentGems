package net.silentchaos512.gems.lib.client;

import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.silentchaos512.gems.api.lib.IPartPosition;

import javax.annotation.Nullable;

public interface IModelData {
    @Nullable
    ModelResourceLocation getModel(IPartPosition pos, int frame);

    int getColor(IPartPosition pos, int frame);
}
