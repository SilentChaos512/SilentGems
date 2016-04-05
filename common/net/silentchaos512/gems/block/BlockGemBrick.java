package net.silentchaos512.gems.block;

import net.minecraft.block.SoundType;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;
import net.silentchaos512.gems.lib.EnumGem;
import net.silentchaos512.gems.lib.Names;
import net.silentchaos512.lib.util.RecipeHelper;

public class BlockGemBrick extends BlockGemSubtypes {

  public final boolean coated;

  public BlockGemBrick(boolean isDark, boolean coated) {

    super(isDark, Names.GEM_BRICK + (coated ? "Coated" : "Speckled") + (isDark ? "Dark" : ""));
    this.coated = coated;

    setHardness(2.0f);
    setResistance(30.0f);
    setStepSound(SoundType.STONE);
    setHarvestLevel("pickaxe", 0);
  }

  @Override
  public void addRecipes() {

    ItemStack bricks = new ItemStack(Blocks.stonebrick, 1, OreDictionary.WILDCARD_VALUE);
    for (int i = 0; i < 16; ++i) {
      EnumGem gem = getGem(i);
      RecipeHelper.addSurroundOre(new ItemStack(this, 8, i),
          coated ? gem.getItemOreName() : gem.getShardOreName(), bricks);
    }
  }
}
