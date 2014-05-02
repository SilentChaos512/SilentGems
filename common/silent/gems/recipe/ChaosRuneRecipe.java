package silent.gems.recipe;

import silent.gems.core.registry.SRegistry;
import silent.gems.core.util.LogHelper;
import silent.gems.item.ChaosGem;
import silent.gems.lib.Names;
import silent.gems.lib.buff.ChaosBuff;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.world.World;


public class ChaosRuneRecipe implements IRecipe {

    @Override
    public boolean matches(InventoryCrafting inventorycrafting, World world) {

        int numGems = 0;
        int numRunes = 0;

        final int chaosRuneId = SRegistry.getItem(Names.CHAOS_RUNE).itemID;
        
        ItemStack stack, gem = null, rune = null;
        
        // Count valid ingredients and look for invalid
        for (int i = 0; i < inventorycrafting.getSizeInventory(); ++i) {
            stack = inventorycrafting.getStackInSlot(i);
            if (stack != null) {
                if (stack.getItem() instanceof ChaosGem) {
                    ++numGems;
                    gem = stack;
                }
                else if (stack.itemID == chaosRuneId) {
                    ++numRunes;
                    rune = stack;
                }
                else {
                    // Invalid item
                    return false;
                }
            }
        }
        
        return numGems == 1 && numRunes == 1 && ChaosGem.canAddBuff(gem, ChaosBuff.all.get(rune.getItemDamage()));
    }

    @Override
    public ItemStack getCraftingResult(InventoryCrafting inventorycrafting) {

        ItemStack gem = null, rune = null, stack = null;

        final int chaosRuneId = SRegistry.getItem(Names.CHAOS_RUNE).itemID;
        
        // Find ingredients
        for (int i = 0; i < inventorycrafting.getSizeInventory(); ++i) {
            stack = inventorycrafting.getStackInSlot(i);
            if (stack != null) {
                if (stack.getItem() instanceof ChaosGem) {
                    gem = stack;
                }
                else if (stack.itemID == chaosRuneId) {
                    rune = stack;
                }
            }
        }
        
        if (gem == null || rune == null) {
            return null;
        }
        
        ItemStack result = gem.copy();
        
        ChaosBuff buff = ChaosBuff.all.get(rune.getItemDamage());
        if (ChaosGem.canAddBuff(result, buff)) {
            ChaosGem.addBuff(result, buff);
        }
        
        return result;
    }

    @Override
    public int getRecipeSize() {

        // TODO What's this?
        return 2;
    }

    @Override
    public ItemStack getRecipeOutput() {

        // TODO What's this?
        return null;
    }

}
