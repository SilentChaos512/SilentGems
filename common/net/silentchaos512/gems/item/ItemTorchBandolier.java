package net.silentchaos512.gems.item;

import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.silentchaos512.gems.lib.GemsCreativeTabs;
import net.silentchaos512.gems.lib.Names;

public class ItemTorchBandolier extends ItemBlockPlacer {

  public static final int MAX_DAMAGE = 1024;

  public ItemTorchBandolier() {

    super(Names.TORCH_BANDOLIER, MAX_DAMAGE);
    setNoRepair();
  }

  @Override
  public IBlockState getBlockPlaced(ItemStack stack) {

    return Blocks.TORCH.getDefaultState();
  }

  @Override
  public int getBlockMetaDropped(ItemStack stack) {

    return 0;
  }

  @Override
  public void addRecipes() {

    String line1 = "lll";
    String line2 = "sgs";

    ItemStack bandolier = new ItemStack(this, 1, MAX_DAMAGE);
    ItemStack gem = new ItemStack(ModItems.gem, 1, OreDictionary.WILDCARD_VALUE);
    Object[] stacks = new Object[] { "leather", ModItems.craftingMaterial.fluffyFabric };

    for (Object stack : stacks) {
      GameRegistry.addRecipe(new ShapedOreRecipe(bandolier, line1, line2, line1, 'l', stack, 's',
          "stickWood", 'g', gem));
    }
  }
}
