package net.silentchaos512.gems.block;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraftforge.oredict.OreDictionary;
import net.silentchaos512.gems.core.util.RecipeHelper;
import net.silentchaos512.gems.lib.EnumGem;
import net.silentchaos512.gems.lib.Names;

public class GemBlock extends BlockSG {

  public GemBlock() {

    super(Material.iron);
    icons = new IIcon[EnumGem.all().length];
    setHardness(3.0f);
    setResistance(30.0f);
    setStepSound(Block.soundTypeStone);
    setHasSubtypes(true);
    setHasGemSubtypes(true);
    setUnlocalizedName(Names.GEM_BLOCK);
  }

  @Override
  public void addRecipes() {
    
    for (int i = 0; i < EnumGem.all().length; ++i) {
      ItemStack gem = EnumGem.all()[i].getItem();
      ItemStack block = new ItemStack(this, 1, i);
      RecipeHelper.addCompressionRecipe(gem, block, 9);
    }
  }
  
  @Override
  public void addOreDict() {
    
    for (EnumGem gem : EnumGem.values()) {
      OreDictionary.registerOre(gem.getBlockOreName(), new ItemStack(this, 1, gem.id));
    }
  }
}
