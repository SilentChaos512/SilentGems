package silent.gems.nei;

import net.minecraft.item.ItemStack;
import silent.gems.SilentGems;
import silent.gems.block.ModBlocks;
import silent.gems.core.registry.SRegistry;
import silent.gems.item.CraftingMaterial;
import silent.gems.lib.EnumGem;
import silent.gems.lib.Names;
import codechicken.nei.api.API;
import codechicken.nei.api.IConfigureNEI;

public class NEISilentGemsConfig implements IConfigureNEI {

  @Override
  public void loadConfig() {

    API.hideItem(new ItemStack(SRegistry.getBlock(Names.FLUFFY_PLANT)));
    API.hideItem(new ItemStack(SRegistry.getItem(Names.DEBUG_ITEM))); // still in creative menu
    for (int i = 0; i < EnumGem.count(); ++i) {
      API.hideItem(new ItemStack(ModBlocks.gemLampLit, 1, i));
      API.hideItem(new ItemStack(ModBlocks.gemLampInv, 1, i));
    }
    for (int i = CraftingMaterial.HIDE_AFTER_META; i < CraftingMaterial.NAMES.length; ++i) {
      API.hideItem(new ItemStack(SRegistry.getItem(Names.CRAFTING_MATERIALS), 1, i));
    }
  }

  @Override
  public String getName() {

    return "Silent's Gems NEI Plugin";
  }

  @Override
  public String getVersion() {

    return SilentGems.VERSION_NUMBER;
  }

}
