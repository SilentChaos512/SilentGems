package net.silentchaos512.gems.crafting.recipe;

import com.google.gson.JsonObject;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.ICraftingRecipe;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.SpecialRecipe;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.registries.ForgeRegistryEntry;
import net.silentchaos512.gems.SilentGems;
import net.silentchaos512.gems.init.GemsRecipeInit;
import net.silentchaos512.gems.item.ChaosGemItem;
import net.silentchaos512.gems.item.ChaosRuneItem;
import net.silentchaos512.gems.lib.chaosbuff.IChaosBuff;
import net.silentchaos512.lib.collection.StackList;

import java.util.List;

public class ApplyChaosRuneRecipe extends SpecialRecipe {
    public ApplyChaosRuneRecipe(ResourceLocation idIn) {
        super(idIn);
    }

    @Override
    public boolean matches(CraftingInventory inv, World worldIn) {
        StackList list = StackList.from(inv);
        // One and only one chaos gem
        ItemStack chaosGem = list.uniqueOfType(ChaosGemItem.class);
        // At least one rune
        int runes = list.countOfType(ChaosRuneItem.class);
        return !chaosGem.isEmpty() && runes > 0;
    }

    @Override
    public ItemStack getCraftingResult(CraftingInventory inv) {
        ItemStack chaosGem = ItemStack.EMPTY;
        List<ItemStack> runes = NonNullList.create();

        for (int i = 0; i < inv.getSizeInventory(); ++i) {
            ItemStack stack = inv.getStackInSlot(i);
            if (!stack.isEmpty()) {
                if (stack.getItem() instanceof ChaosRuneItem) {
                    // Any number of runes
                    runes.add(stack);
                } else {
                    // Only one chaos gem
                    if (!chaosGem.isEmpty()) {
                        return ItemStack.EMPTY;
                    }
                    chaosGem = stack;
                }
            }
        }

        if (chaosGem.isEmpty() || runes.isEmpty()) {
            return ItemStack.EMPTY;
        }

        ItemStack result = chaosGem.copy();
        for (ItemStack rune : runes) {
            IChaosBuff buff = ChaosRuneItem.getBuff(rune);
            if (buff == null || !ChaosGemItem.addBuff(result, buff)) {
                return ItemStack.EMPTY;
            }
        }
        return result;
    }

    @Override
    public boolean canFit(int width, int height) {
        return true;
    }

    @Override
    public IRecipeSerializer<?> getSerializer() {
        return GemsRecipeInit.APPLY_CHAOS_RUNE.get();
    }
}
