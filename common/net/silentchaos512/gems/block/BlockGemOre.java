package net.silentchaos512.gems.block;

import java.util.Random;

import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.Item;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.OreDictionary;
import net.silentchaos512.gems.SilentGems;
import net.silentchaos512.gems.item.ModItems;
import net.silentchaos512.gems.lib.EnumGem;
import net.silentchaos512.gems.lib.Names;
import net.silentchaos512.lib.block.BlockSL;

public class BlockGemOre extends BlockGemSubtypes {

  public BlockGemOre(boolean dark) {

    super(dark, dark ? Names.GEM_ORE_DARK : Names.GEM_ORE);

    setHardness(3.0f);
    setResistance(15.0f);
    setStepSound(SoundType.STONE);
    setHarvestLevel("pickaxe", 2);
  }

  @Override
  public void addRecipes() {

    for (int i = 0; i < 16; ++i) {
      EnumGem gem = getGem(i);
      GameRegistry.addSmelting(gem.getOre(), gem.getItem(), 0.5f);
    }
  }

  @Override
  public void addOreDict() {

    for (int i = 0; i < 16; ++i) {
      EnumGem gem = getGem(i);
      OreDictionary.registerOre(gem.getOreOreName(), gem.getOre());
    }
  }

  @Override
  public Item getItemDropped(IBlockState state, Random rand, int fortune) {

    return ModItems.gem;
  }

  @Override
  public int quantityDroppedWithBonus(int fortune, Random random) {

    if (fortune > 0) {
      int j = random.nextInt(fortune + 2) - 1;

      if (j < 0) {
        j = 0;
      }

      return quantityDropped(random) * (j + 1);
    } else {
      return quantityDropped(random);
    }
  }

  @Override
  public int getExpDrop(IBlockState state, IBlockAccess world, BlockPos pos, int fortune) {

    Item item = getItemDropped(world.getBlockState(pos), RANDOM, fortune);
    if (item != Item.getItemFromBlock(this)) {
      return 1 + RANDOM.nextInt(5);
    }
    return 0;
  }
}
