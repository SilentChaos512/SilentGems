package silent.gems.block;

import net.minecraft.block.material.Material;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.GameRegistry;
import silent.gems.core.registry.SRegistry;
import silent.gems.item.ChaosEssence;
import silent.gems.item.CraftingMaterial;
import silent.gems.lib.Names;
import silent.gems.lib.Reference;
import silent.gems.lib.Strings;

public class MiscBlock extends BlockSG {

//  public final static String[] names = { Names.CHAOS_ESSENCE_BLOCK_OLD };

  public MiscBlock() {

    super(1, Material.iron);
    setHasSubtypes(true);
    setUnlocalizedName(Names.MISC_BLOCKS);
  }

  @Override
  public void addRecipes() {

    ItemStack chaosEssenceBlock = ChaosEssenceBlock.getByType(ChaosEssenceBlock.EnumType.REGULAR);
    GameRegistry.addShapelessRecipe(chaosEssenceBlock, new ItemStack(this));
//    GameRegistry.addShapedRecipe(getStack(Names.CHAOS_ESSENCE_BLOCK), "ccc", "ccc", "ccc", 'c',
//        CraftingMaterial.getStack(Names.CHAOS_ESSENCE));
//    GameRegistry.addShapelessRecipe(CraftingMaterial.getStack(Names.CHAOS_ESSENCE, 9),
//        getStack(Names.CHAOS_ESSENCE_BLOCK));
  }
  
  @Override
  public String[] getVariantNames() {
    
    return new String[] { Reference.MOD_ID + ":" + Names.MISC_BLOCKS };
  }

//  public static ItemStack getStack(String name) {
//
//    for (int i = 0; i < names.length; ++i) {
//      if (names[i].equals(name)) {
//        return new ItemStack(SRegistry.getBlock(Names.MISC_BLOCKS), 1, i);
//      }
//    }
//
//    return null;
//  }
//
//  public static ItemStack getStack(String name, int count) {
//
//    for (int i = 0; i < names.length; ++i) {
//      if (names[i].equals(name)) {
//        return new ItemStack(SRegistry.getBlock(Names.MISC_BLOCKS), count, i);
//      }
//    }
//
//    return null;
//  }

//  @Override
//  public void registerBlockIcons(IIconRegister iconRegister) {
//
//    for (int i = 0; i < names.length; ++i) {
//      icons[i] = iconRegister.registerIcon(Strings.RESOURCE_PREFIX + names[i]);
//    }
//  }
}
