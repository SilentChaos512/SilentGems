package silent.gems.item;

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import silent.gems.block.ChaosEssenceBlock;
import silent.gems.core.registry.SRegistry;
import silent.gems.core.util.RecipeHelper;
import silent.gems.lib.Names;

public class ChaosRefiner extends ItemSG {

  public ChaosRefiner() {

    super(1);

    setMaxStackSize(1);
    setUnlocalizedName(Names.CHAOS_REFINER);
  }
  
  @Override
  public void addRecipes() {
    
    int meta = ChaosEssenceBlock.EnumType.RAW.getMetadata();
    ItemStack chaosEssence = new ItemStack(SRegistry.getItem(Names.CHAOS_ESSENCE), 1, meta);
    RecipeHelper.addSurround(new ItemStack(this), new ItemStack(Items.diamond), chaosEssence);
  }
}
