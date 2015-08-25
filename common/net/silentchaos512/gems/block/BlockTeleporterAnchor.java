package net.silentchaos512.gems.block;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.silentchaos512.gems.lib.Names;
import cpw.mods.fml.common.registry.GameRegistry;

public class BlockTeleporterAnchor extends BlockTeleporter {

  public BlockTeleporterAnchor() {

    super();
    this.isAnchor = true;
    this.setHasSubtypes(false);
    this.setHasGemSubtypes(false);
    this.setUnlocalizedName(Names.TELEPORTER_ANCHOR);
  }

  @Override
  public void addRecipes() {

    GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(this, 4), "cec", " i ", "cec", 'c',
        "gemChaos", 'e', Items.ender_pearl, 'i', Blocks.iron_block));
  }
}
