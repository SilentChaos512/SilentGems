package silent.gems.block;

import net.minecraft.block.material.Material;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.GameRegistry;
import silent.gems.SilentGems;
import silent.gems.lib.Names;

public class MiscBlock extends BlockSG {
  
  public MiscBlock() {

    super(1, Material.iron);
    setHasSubtypes(true);
    setUnlocalizedName(Names.MISC_BLOCKS);
  }

  @Override
  public void addRecipes() {

    ItemStack chaosEssenceBlock = ChaosEssenceBlock.getByType(ChaosEssenceBlock.EnumType.REGULAR);
    GameRegistry.addShapelessRecipe(chaosEssenceBlock, new ItemStack(this));
  }
  
  @Override
  public String[] getVariantNames() {
    
    return new String[] { SilentGems.MOD_ID + ":" + Names.MISC_BLOCKS };
  }
}
