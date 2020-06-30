package net.silentchaos512.gems.crafting.recipe;

import net.minecraft.inventory.CraftingInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.ShapedRecipe;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.silentchaos512.gems.SilentGems;
import net.silentchaos512.gems.init.GemsBlocks;
import net.silentchaos512.gems.init.GemsTags;
import net.silentchaos512.gems.lib.Gems;
import net.silentchaos512.gems.lib.urn.UrnConst;
import net.silentchaos512.lib.collection.StackList;
import net.silentchaos512.lib.crafting.recipe.ExtendedShapedRecipe;
import net.silentchaos512.utils.Color;

public final class SoulUrnRecipe extends ExtendedShapedRecipe {
    public static final ResourceLocation NAME = new ResourceLocation(SilentGems.MOD_ID, "soul_urn");
    public static final ExtendedShapedRecipe.Serializer<SoulUrnRecipe> SERIALIZER = new ExtendedShapedRecipe.Serializer<>(
            SoulUrnRecipe::new,
            (json, recipe) -> recipe.color = Color.from(json, "urn_clay_color", UrnConst.UNDYED_COLOR).getColor() & 0xFFFFFF,
            (buffer, recipe) -> recipe.color = buffer.readVarInt(),
            (buffer, recipe) -> buffer.writeVarInt(recipe.color)
    );

    // The clay color of the urn being crafted. Typically matches the terracotta color, could be anything.
    private int color = UrnConst.UNDYED_COLOR;

    private SoulUrnRecipe(ShapedRecipe recipe) {
        super(recipe);
    }

    @Override
    public boolean matches(CraftingInventory inv, World worldIn) {
        return getBaseRecipe().matches(inv, worldIn);
    }

    @Override
    public ItemStack getCraftingResult(CraftingInventory inv) {
        StackList list = StackList.from(inv);
        ItemStack gemStack = list.firstMatch(s -> s.getItem().isIn(GemsTags.Items.GEMS));
        Gems gem = Gems.from(gemStack);
        return GemsBlocks.SOUL_URN.get().getStack(this.color, gem);
    }

    @Override
    public ItemStack getRecipeOutput() {
        return GemsBlocks.SOUL_URN.get().getStack(this.color, null);
    }

    @Override
    public IRecipeSerializer<?> getSerializer() {
        return SERIALIZER;
    }
}
