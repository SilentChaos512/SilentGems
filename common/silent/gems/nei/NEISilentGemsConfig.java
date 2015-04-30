package silent.gems.nei;

import net.minecraft.item.ItemStack;
import silent.gems.core.registry.SRegistry;
import silent.gems.item.CraftingMaterial;
import silent.gems.lib.EnumGem;
import silent.gems.lib.Names;
import silent.gems.lib.Reference;
import codechicken.nei.api.API;
import codechicken.nei.api.IConfigureNEI;


public class NEISilentGemsConfig implements IConfigureNEI {

    @Override
    public void loadConfig() {

        API.hideItem(new ItemStack(SRegistry.getBlock(Names.FLUFFY_PLANT)));
        for (int i = 0; i < EnumGem.all().length; ++i) {
            API.hideItem(new ItemStack(SRegistry.getBlock(Names.GEM_LAMP_LIT), 1, i));
            API.hideItem(new ItemStack(SRegistry.getBlock(Names.GEM_LAMP_INV), 1, i));
        }
        for (int i = CraftingMaterial.HIDE_AFTER_META; i < CraftingMaterial.names.length; ++i) {
            API.hideItem(new ItemStack(SRegistry.getItem(Names.CRAFTING_MATERIALS), 1, i));
        }
    }

    @Override
    public String getName() {

        return "Silent's Gems NEI Plugin";
    }

    @Override
    public String getVersion() {

        return Reference.VERSION_NUMBER;
    }

}
