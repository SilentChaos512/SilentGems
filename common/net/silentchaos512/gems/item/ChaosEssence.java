package net.silentchaos512.gems.item;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.silentchaos512.gems.block.ChaosEssenceBlock;
import net.silentchaos512.gems.lib.Names;

public class ChaosEssence extends ItemSG {

  public ChaosEssence() {

    super(ChaosEssenceBlock.EnumType.values().length);

    setHasSubtypes(true);
    setUnlocalizedName(Names.CHAOS_ESSENCE_OLD);
  }
  
  @Override
  public String getFullName() {
    
    return super.getFullName() + "Old";
  }

  @Override
  public void addRecipes() {

    // Craft back into the old (new?) versions.
    ItemStack result = CraftingMaterial.getStack(Names.CHAOS_ESSENCE);
    GameRegistry.addShapelessRecipe(result, new ItemStack(this, 1, 0));
    GameRegistry.addShapelessRecipe(result, new ItemStack(this, 1, 1));
    result = CraftingMaterial.getStack(Names.CHAOS_ESSENCE_PLUS);
    GameRegistry.addShapelessRecipe(result, new ItemStack(this, 1, 2));
  }
}
