package net.silentchaos512.gems.client.render;

import com.google.common.collect.ImmutableList;

import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemOverride;
import net.minecraft.client.renderer.block.model.ItemOverrideList;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ToolItemOverrideHandler extends ItemOverrideList {

  public static final ToolItemOverrideHandler INSTANCE = new ToolItemOverrideHandler();
  public static IBakedModel baseModel;

  public ToolItemOverrideHandler() {

    super(ImmutableList.<ItemOverride> of());
  }

  @Override
  public IBakedModel handleItemState(IBakedModel originalModel, ItemStack stack, World world,
      EntityLivingBase entity) {

    if (stack == null) {
      return baseModel;
    }
    return new ToolModel(baseModel).handleItemState(stack);
  }
}
