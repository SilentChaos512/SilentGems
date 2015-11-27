package net.silentchaos512.gems.block;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemDye;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.oredict.OreDictionary;
import net.silentchaos512.gems.core.util.LogHelper;
import net.silentchaos512.gems.core.util.RecipeHelper;
import net.silentchaos512.gems.lib.EnumGem;
import net.silentchaos512.gems.lib.Names;

public class BlockFluffyBlock extends BlockSG {
  
  public static final PropertyEnum COLOR = PropertyEnum.create("color", EnumDyeColor.class);

  public BlockFluffyBlock() {

    super(EnumDyeColor.values().length, Material.cloth);
    setHasSubtypes(true);
    setDefaultState(blockState.getBaseState().withProperty(COLOR, EnumDyeColor.WHITE));

    setHardness(0.8f);
    setResistance(3.0f);
    setStepSound(Block.soundTypeCloth);
    setHarvestLevel("", 0);

    setUnlocalizedName(Names.FLUFFY_BLOCK);
  }
  
  @Override
  public void addRecipes() {

    ItemStack any = new ItemStack(this, 1, OreDictionary.WILDCARD_VALUE);
    for (int i = 0; i < 16; ++i) {
      String dye = EnumDyeColor.byMetadata(i).getUnlocalizedName();
      if (dye.equals("silver")) {
        dye = "lightGray";
      }
      dye = "dye" + Character.toUpperCase(dye.charAt(0)) + dye.substring(1);
      RecipeHelper.addSurroundOre(new ItemStack(this, 8, i), dye, any);
    }
  }

  @Override
  public void onFallenUpon(World world, BlockPos pos, Entity entity, float distance) {

    if (distance < 2 || world.isRemote) {
      return;
    }

    // Count the number of fluffy blocks that are stacked up.
    int stackedBlocks = 0;
    for (; world.getBlockState(pos).getBlock() == this; pos = pos.down()) {
      ++stackedBlocks;
    }
    LogHelper.debug(distance + ", " + stackedBlocks);

    // Reduce fall distance by 10 blocks per stacked block
    distance -= Math.min(10 * stackedBlocks, distance);
    // Just changing entity fall distance seems weird in 1.8, so I'll apply the damage here.
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
  protected BlockState createBlockState() {

    return new BlockState(this, new IProperty[] { COLOR });
  }
}
