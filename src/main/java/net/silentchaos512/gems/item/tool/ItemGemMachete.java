package net.silentchaos512.gems.item.tool;

import com.google.common.collect.ImmutableSet;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.silentchaos512.gems.SilentGems;
import net.silentchaos512.gems.config.ConfigOptionToolClass;
import net.silentchaos512.gems.config.GemsConfig;
import net.silentchaos512.gems.init.ModItems;
import net.silentchaos512.gems.lib.Names;
import net.silentchaos512.gems.util.ToolHelper;
import net.silentchaos512.lib.registry.RecipeMaker;

import java.util.Set;

public class ItemGemMachete extends ItemGemSword {

  public ItemGemMachete() {

    super();
    setTranslationKey(SilentGems.RESOURCE_PREFIX + Names.MACHETE);
  }

  @Override
  public ConfigOptionToolClass getConfig() {

    return GemsConfig.machete;
  }

  @Override
  public float getMeleeDamageModifier() {

    return 4.0f;
  }

  @Override
  public float getMagicDamageModifier() {

    return 1.0f;
  }

  @Override
  public float getMeleeSpeedModifier() {

    return -2.7f;
  }

  @Override
  public float getDurabilityMultiplier() {

    return 1.25f;
  }

  @Override
  public boolean onBlockStartBreak(ItemStack stack, BlockPos pos, EntityPlayer player) {

    if (!player.isSneaking()) {
      // Allow clearing vegetation, just like sickles but with a smaller range.
      return ModItems.sickle.onSickleStartBreak(stack, pos, player, 2);
    }
    // Standard behavior if player is sneaking.
    return super.onBlockStartBreak(stack, pos, player);
  }

  @Override
  public Set<String> getToolClasses(ItemStack stack) {

    // Machete can be used as an axe.
    return ToolHelper.isBroken(stack) ? ImmutableSet.of() : ImmutableSet.of("axe");
  }

  @Override
  public float getDestroySpeed(ItemStack stack, IBlockState state) {

    float digSpeed = ToolHelper.getDigSpeed(stack, state, ItemGemAxe.EXTRA_EFFECTIVE_MATERIALS);
    // On blocks typically harvested with axes, reduce the harvest speed.
    // Note: Ladders use the "circuits" material. Weird, but true!
    if (state.getMaterial() == Material.WOOD || state.getMaterial() == Material.GOURD
        || state.getMaterial() == Material.CIRCUITS) {
      return digSpeed / 2.5f;
    }
    // On other blocks, full speed.
    return digSpeed;
  }

  @Override
  public void addRecipes(RecipeMaker recipes) {

    if (!getConfig().isDisabled)
      ToolHelper.addExampleRecipe(this, " hh", " h ", "r  ");
  }

  @Override
  public String getName() {

    return Names.MACHETE;
  }
}
