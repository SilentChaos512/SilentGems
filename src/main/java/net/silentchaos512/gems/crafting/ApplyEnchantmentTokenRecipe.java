package net.silentchaos512.gems.crafting;

import com.google.gson.JsonObject;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.silentchaos512.gems.SilentGems;
import net.silentchaos512.gems.item.EnchantmentToken;

import java.util.List;

public class ApplyEnchantmentTokenRecipe implements IRecipe {
    private static final ResourceLocation NAME = new ResourceLocation(SilentGems.MOD_ID, "apply_enchantment_token");

    @Override
    public boolean matches(IInventory inv, World worldIn) {
        return !getCraftingResult(inv).isEmpty();
    }

    @Override
    public ItemStack getCraftingResult(IInventory inv) {
        ItemStack tool = ItemStack.EMPTY;
        List<ItemStack> tokens = NonNullList.create();

        for (int i = 0; i < inv.getSizeInventory(); ++i) {
            ItemStack stack = inv.getStackInSlot(i);
            if (!stack.isEmpty()) {
                if (stack.getItem() instanceof EnchantmentToken) {
                    // Any number of tokens
                    tokens.add(stack);
                } else {
                    // Only one tool
                    if (!tool.isEmpty()) {
                        return ItemStack.EMPTY;
                    }
                    tool = stack;
                }
            }
        }

        if (tool.isEmpty() || tokens.isEmpty()) {
            return ItemStack.EMPTY;
        }

        ItemStack result = tool.copy();
        for (ItemStack token : tokens) {
            if (!EnchantmentToken.applyTokenToTool(token, result)) {
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
    public ItemStack getRecipeOutput() {
        // Could be anything
        return ItemStack.EMPTY;
    }

    @Override
    public ResourceLocation getId() {
        return NAME;
    }

    @Override
    public IRecipeSerializer<?> getSerializer() {
        return Serializer.INSTANCE;
    }

    @Override
    public String getGroup() {
        return SilentGems.MOD_ID;
    }

    @Override
    public boolean isDynamic() {
        // Don't show in recipe book
        return true;
    }

    public static final class Serializer implements IRecipeSerializer<ApplyEnchantmentTokenRecipe> {
        public static final Serializer INSTANCE = new Serializer();

        private Serializer() { }

        @Override
        public ApplyEnchantmentTokenRecipe read(ResourceLocation recipeId, JsonObject json) {
            return new ApplyEnchantmentTokenRecipe();
        }

        @Override
        public ApplyEnchantmentTokenRecipe read(ResourceLocation recipeId, PacketBuffer buffer) {
            return new ApplyEnchantmentTokenRecipe();
        }

        @Override
        public void write(PacketBuffer buffer, ApplyEnchantmentTokenRecipe recipe) { }

        @Override
        public ResourceLocation getName() {
            return NAME;
        }
    }
}
