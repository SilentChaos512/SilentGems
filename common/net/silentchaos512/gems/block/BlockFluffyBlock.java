package net.silentchaos512.gems.block;

import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.oredict.OreDictionary;
import net.silentchaos512.gems.SilentGems;
import net.silentchaos512.gems.lib.Names;
import net.silentchaos512.lib.block.BlockSL;
import net.silentchaos512.lib.util.RecipeHelper;

public class BlockFluffyBlock extends BlockSL {

  public static final PropertyEnum COLOR = PropertyEnum.create("color", EnumDyeColor.class);

  public BlockFluffyBlock() {

    super(EnumDyeColor.values().length, SilentGems.MOD_ID, Names.FLUFFY_BLOCK, Material.cloth);
    setHasSubtypes(true);
    setDefaultState(blockState.getBaseState().withProperty(COLOR, EnumDyeColor.WHITE));

    setHardness(0.8f);
    setResistance(3.0f);
    setStepSound(SoundType.CLOTH);
    setHarvestLevel("", 0);

    setUnlocalizedName(Names.FLUFFY_BLOCK);
  }

  @Override
  public void addRecipes() {

    ItemStack any = new ItemStack(this, 1, OreDictionary.WILDCARD_VALUE);
    for (EnumDyeColor color : EnumDyeColor.values()) {
      String dyeName = color.getUnlocalizedName();
      if (dyeName.equals("silver")) {
        dyeName = "lightGray";
      }
      dyeName = "dye" + Character.toUpperCase(dyeName.charAt(0)) + dyeName.substring(1);
      RecipeHelper.addSurroundOre(new ItemStack(this, 8, color.getMetadata()), dyeName, any);
    }
  }

  @Override
  public void onFallenUpon(World world, BlockPos pos, Entity entity, float distance) {

    SilentGems.instance.logHelper.debug(distance);
    if (distance < 2 || world.isRemote) {
      return;
    }

    // Count the number of fluffy blocks that are stacked up.
    int stackedBlocks = 0;
    for (; world.getBlockState(pos).getBlock() == this; pos = pos.down()) {
      ++stackedBlocks;
    }

    // Reduce fall distance by 10 blocks per stacked block
    distance -= Math.min(10 * stackedBlocks, distance);
    entity.fallDistance = 0f;
    entity.fall(distance, 1f);
  }

  @Override
  public IBlockState getStateFromMeta(int meta) {

    return this.getDefaultState().withProperty(COLOR, EnumDyeColor.byMetadata(meta));
  }

  @Override
  public int getMetaFromState(IBlockState state) {

    return ((EnumDyeColor) state.getValue(COLOR)).getMetadata();
  }

  @Override
  protected BlockStateContainer createBlockState() {

    return new BlockStateContainer(this, new IProperty[] { COLOR });
  }
}
