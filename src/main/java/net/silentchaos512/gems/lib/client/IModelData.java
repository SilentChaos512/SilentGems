package net.silentchaos512.gems.lib.client;

import javax.annotation.Nullable;

import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.silentchaos512.gems.api.lib.IPartPosition;

public interface IModelData {

  public @Nullable ModelResourceLocation getModel(IPartPosition pos, int frame);

  public int getColor(IPartPosition pos, int frame);
}
