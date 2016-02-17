package net.silentchaos512.gems.block;

import java.util.List;

import com.google.common.collect.Lists;

import net.minecraft.block.Block;
import net.minecraft.block.BlockCrops;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import net.silentchaos512.gems.SilentGems;
import net.silentchaos512.gems.core.registry.IHasVariants;
import net.silentchaos512.gems.item.ModItems;
import net.silentchaos512.gems.item.tool.GemSickle;
import net.silentchaos512.gems.lib.Names;

public class FluffyPlantBlock extends BlockCrops implements IHasVariants {

  protected String blockName; // TODO: Is this used?

  public FluffyPlantBlock() {

    setUnlocalizedName(Names.FLUFFY_PLANT);
  }

  @Override
  public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player,
      EnumFacing side, float hitX, float hitY, float hitZ) {

    if (player.getHeldItem() != null && player.getHeldItem().getItem() instanceof GemSickle) {
      return false;
    }

    // Right-click to harvest
    List<ItemStack> drops = Lists.newArrayList();
    int age = state.getValue(AGE);

    // Get drops if mature
    if (age >= 7) {
      for (int i = 0; i < 3; ++i) {
        if (i == 0 || RANDOM.nextInt(15) <= age) {
          drops.add(new ItemStack(getCrop(), 1, damageDropped(state)));
        }
      }

      // Reset to newly planted state
      world.setBlockState(pos, getDefaultState());
    }

    // Spawn items in world
    for (ItemStack stack : drops) {
      spawnAsEntity(world, pos, stack);
    }

    return !drops.isEmpty();
  }

  @Override
  protected Item getSeed() {

    return ModItems.fluffyPuff;
  }

  @Override
  protected Item getCrop() {

    return ModItems.fluffyPuff;
  }

  @Override
  public int getRenderType() {

    return 3;
  }

  @Override
  public String getUnlocalizedName() {

    return "tile." + Names.FLUFFY_PLANT;
  }

  @Override
  public Block setUnlocalizedName(String value) {

    this.blockName = value;
    super.setUnlocalizedName(value);
    return this;
  }

  @Override
  public String[] getVariantNames() {

    String[] result = new String[4];
    for (int i = 0; i < result.length; ++i) {
      result[i] = getFullName() + i;
    }
    return result;
  }

  @Override
  public String getName() {

    return Names.FLUFFY_PLANT;
  }

  @Override
  public String getFullName() {

    return SilentGems.MOD_ID + ":" + Names.FLUFFY_PLANT;
  }
}
