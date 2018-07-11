package net.silentchaos512.gems.block;

import net.minecraft.block.SoundType;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;
import net.silentchaos512.gems.lib.EnumGem;
import net.silentchaos512.gems.lib.Names;
import net.silentchaos512.lib.registry.RecipeMaker;

public class BlockGemBrick extends BlockGemSubtypes {

  public final boolean coated;

  public BlockGemBrick(EnumGem.Set set, boolean coated) {

    super(set, nameForSet(set, Names.GEM_BRICK + (coated ? "Coated" : "Speckled")));
    this.coated = coated;

    setHardness(2.0f);
    setResistance(30.0f);
    setSoundType(SoundType.STONE);
    setHarvestLevel("pickaxe", 0);
  }

  @Override
  public void addRecipes(RecipeMaker recipes) {

    ItemStack bricks = new ItemStack(Blocks.STONEBRICK, 1, OreDictionary.WILDCARD_VALUE);
    for (int i = 0; i < 16; ++i) {
      EnumGem gem = getGem(i);
      recipes.addSurroundOre(blockName + i, new ItemStack(this, 8, i),
          coated ? gem.getItemOreName() : gem.getShardOreName(), bricks);
    }
  }
}
