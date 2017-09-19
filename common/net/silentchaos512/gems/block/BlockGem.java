package net.silentchaos512.gems.block;

import java.util.List;

import net.minecraft.block.SoundType;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.oredict.OreDictionary;
import net.silentchaos512.gems.SilentGems;
import net.silentchaos512.gems.lib.EnumGem;
import net.silentchaos512.gems.lib.Names;
import net.silentchaos512.lib.registry.RecipeMaker;

public class BlockGem extends BlockGemSubtypes {

  public final boolean supercharged;

  public BlockGem(EnumGem.Set set, boolean supercharged) {

    super(set, nameForSet(set, Names.GEM_BLOCK + (supercharged ? "Super" : "")));
    this.supercharged = supercharged;

    setHardness(supercharged ? 7.0f : 3.0f);
    setResistance(supercharged ? 6000000.0F : 30.0f);
    setSoundType(SoundType.METAL);
    setHarvestLevel("pickaxe", supercharged ? 3 : 1);
  }

  @Override
  public void addRecipes(RecipeMaker recipes) {

    for (int i = 0; i < 16; ++i) {
      EnumGem gem = getGem(i);

      if (supercharged) {
        recipes.addShapedOre(gem.name() + "_block_super", gem.getBlockSuper(), " g ", "gog", " g ",
            'g', gem.getItemSuperOreName(), 'o', Blocks.OBSIDIAN);
      } else {
        recipes.addCompression(gem.name() + "_block", gem.getItem(), gem.getBlock(), 9);
      }
    }
  }

  @Override
  public void addOreDict() {

    for (int i = 0; i < 16; ++i) {
      EnumGem gem = getGem(i);
      if (supercharged) {
        OreDictionary.registerOre(gem.getBlockSuperOreName(), gem.getBlockSuper());
      } else {
        OreDictionary.registerOre(gem.getBlockOreName(), gem.getBlock());
      }
    }
  }

  @Override
  public void clAddInformation(ItemStack stack, World world, List<String> list, boolean advanced) {

    String str = blockName.replaceFirst("Dark", "");
    list.addAll(SilentGems.localizationHelper.getBlockDescriptionLines(str));
  }

  @Override
  public EnumRarity getRarity(int meta) {

    return supercharged ? EnumRarity.RARE : EnumRarity.COMMON;
  }

  @Override
  public boolean canEntityDestroy(IBlockState state, IBlockAccess world, BlockPos pos,
      Entity entity) {

    // Does not prevent Withers from breaking. Only the Ender Dragon seems to call this...
    if (supercharged) {
      return entity instanceof EntityPlayer;
    }
    return super.canEntityDestroy(state, world, pos, entity);
  }
}
