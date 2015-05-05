package net.silentchaos512.gems.block;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.silentchaos512.gems.lib.EnumGem;
import net.silentchaos512.gems.lib.Names;
import cpw.mods.fml.common.registry.GameRegistry;

public class GemBrick extends BlockSG {

  public GemBrick(String name) {

    super(Material.rock);

    icons = new IIcon[EnumGem.all().length];
    setHardness(3.0f);
    setResistance(5.0f);
    setStepSound(Block.soundTypeStone);
    setHasSubtypes(true);
    setHasGemSubtypes(true);
    setUnlocalizedName(name);
  }

  @Override
  public void addRecipes() {

    String s1 = "sss";
    String s2 = "sgs";
    if (this.blockName.equals(Names.GEM_BRICK_COATED)) {
      for (int i = 0; i < EnumGem.all().length; ++i) {
        GameRegistry.addShapedRecipe(new ItemStack(this, 8, i), s1, s2, s1, 'g',
            EnumGem.all()[i].getItem(), 's', Blocks.stonebrick);
      }
    } else {
      for (int i = 0; i < EnumGem.all().length; ++i) {
        GameRegistry.addShapedRecipe(new ItemStack(this, 8, i), s1, s2, s1, 'g',
            EnumGem.all()[i].getShard(), 's', Blocks.stonebrick);
      }
    }
  }
}
