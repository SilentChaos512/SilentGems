package net.silentchaos512.gems.crafting.recipe;

import net.minecraft.inventory.CraftingInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.ShapedRecipe;
import net.minecraft.world.World;
import net.silentchaos512.gems.init.GemsRecipeInit;
import net.silentchaos512.gems.item.SoulGemItem;
import net.silentchaos512.gems.lib.soul.GearSoul;
import net.silentchaos512.gems.lib.soul.Soul;
import net.silentchaos512.gems.util.SoulManager;
import net.silentchaos512.lib.collection.StackList;
import net.silentchaos512.lib.crafting.recipe.ExtendedShapedRecipe;

import java.util.Collection;
import java.util.stream.Collectors;

public class GearSoulRecipe extends ExtendedShapedRecipe {
    public GearSoulRecipe(ShapedRecipe recipe) {
        super(recipe);
    }

    @Override
    public boolean matches(CraftingInventory inv, World worldIn) {
        return getBaseRecipe().matches(inv, worldIn);
    }

    @Override
    public ItemStack getCraftingResult(CraftingInventory inv) {
        ItemStack result = getBaseRecipe().getCraftingResult(inv);

        // Grab the souls gems, collect the souls, and construct a gear soul
        StackList list = StackList.from(inv);
        Collection<Soul> souls = list.allOfType(SoulGemItem.class).stream()
                .map(SoulGemItem::getSoul)
                .collect(Collectors.toList());
        GearSoul gearSoul = GearSoul.construct(result, souls);
        SoulManager.setSoul(result, gearSoul);

        return result;
    }

    @Override
    public IRecipeSerializer<?> getSerializer() {
        return GemsRecipeInit.GEAR_SOUL_RECIPE.get();
    }
}
