package net.silentchaos512.gems.item.tool;

import java.util.Set;

import com.google.common.collect.ImmutableSet;

import net.minecraft.block.Block;
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

public class ItemGemMachete extends ItemGemSword {

  public ItemGemMachete() {

    super();
    setUnlocalizedName(SilentGems.RESOURCE_PREFIX + Names.MACHETE);
  }

  @Override
  public ConfigOptionToolClass getConfig() {

    return GemsConfig.machete;
  }

  @Override
  public float getMagicDamage(ItemStack tool) {

    return 1.0f + ToolHelper.getMagicDamage(tool);
  }

  @Override
  public float getBaseMeleeDamageModifier() {

    return 4.0f;
  }

  @Override
  public float getBaseMeleeSpeedModifier() {

    return -2.7f;
  }

  @Override
  public float getDurabilityMultiplier() {

    return 1.25f;
  }

  @Override
  public boolean onBlockStartBreak(ItemStack sickle, BlockPos pos, EntityPlayer player) {

    // Allow clearing vegetation, just like sickles but with a smaller range.
    return ModItems.sickle.onSickleStartBreak(sickle, pos, player, 2);
  }

  @Override
  public Set<String> getToolClasses(ItemStack stack) {

    return ToolHelper.isBroken(stack) ? ImmutableSet.of()
        : ImmutableSet.of("axe");
  }

  @Override
  public float getStrVsBlock(ItemStack stack, IBlockState state) {

    return ToolHelper.getDigSpeed(stack, state, ItemGemAxe.EXTRA_EFFECTIVE_MATERIALS) / 3;
  }

  @Override
  public void addRecipes(RecipeMaker recipes) {

    if (!getConfig().isDisabled)
      ToolHelper.addExampleRecipe(this, " gg", " g ", "s  ");
  }

  @Override
  public String getName() {

    return Names.MACHETE;
  }
}
