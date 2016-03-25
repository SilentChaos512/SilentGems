package net.silentchaos512.gems.block;

import net.minecraft.block.SoundType;
import net.minecraftforge.oredict.OreDictionary;
import net.silentchaos512.gems.lib.EnumGem;
import net.silentchaos512.gems.lib.Names;
import net.silentchaos512.lib.util.RecipeHelper;

public class BlockGem extends BlockGemSubtypes {

  public BlockGem(boolean dark) {

    super(dark, dark ? Names.GEM_BLOCK_DARK : Names.GEM_BLOCK);

    setHardness(3.0f);
    setResistance(30.0f);
    setStepSound(SoundType.METAL);
    setHarvestLevel("pickaxe", 1);
  }

  @Override
  public void addRecipes() {

    for (int i = 0; i < 16; ++i) {
      EnumGem gem = getGem(i);
      RecipeHelper.addCompressionRecipe(gem.getItem(), gem.getBlock(), 9);
    }
  }

  @Override
  public void addOreDict() {

    for (int i = 0; i < 16; ++i) {
      EnumGem gem = getGem(i);
      OreDictionary.registerOre(gem.getBlockOreName(), gem.getBlock());
    }
  }
}
