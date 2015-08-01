package net.silentchaos512.gems.block;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.silentchaos512.gems.lib.EnumGem;
import net.silentchaos512.gems.lib.Names;
import cpw.mods.fml.common.registry.GameRegistry;

public class GemBrick extends BlockSG {

  public GemBrick(String name) {

    super(Material.rock);

    icons = new IIcon[EnumGem.all().length];
    setHardness(2.0f);
    setResistance(30.0f);
    setStepSound(Block.soundTypeStone);
    setHasSubtypes(true);
    setHasGemSubtypes(true);
    setUnlocalizedName(name);
  }

  @Override
  public void addRecipes() {

    if (this.blockName.equals(Names.GEM_BRICK_COATED)) {
      for (EnumGem gem : EnumGem.values()) {
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(this, 8, gem.id), "sss", "sgs",
            "sss", 's', Blocks.stonebrick, 'g', gem.getItemOreName()));
      }
    } else {
      for (EnumGem gem : EnumGem.values()) {
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(this, 8, gem.id), "sss", "sgs",
            "sss", 's', Blocks.stonebrick, 'g', gem.getShardOreName()));
      }
    }
  }
}
