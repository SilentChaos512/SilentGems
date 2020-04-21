package net.silentchaos512.gems.crafting.recipe;

import com.google.gson.JsonObject;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.ICraftingRecipe;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.registries.ForgeRegistryEntry;
import net.silentchaos512.gems.SilentGems;
import net.silentchaos512.gems.item.EnchantmentTokenItem;

import java.util.List;

public class ApplyEnchantmentTokenRecipe implements ICraftingRecipe {
    public static final ResourceLocation NAME = SilentGems.getId("apply_enchantment_token");
    public static final Serializer SERIALIZER = new Serializer();

    @Override
    public boolean matches(CraftingInventory inv, World worldIn) {
        return !getCraftingResult(inv).isEmpty();
    }

    @Override
    public ItemStack getCraftingResult(CraftingInventory inv) {
        ItemStack tool = ItemStack.EMPTY;
        List<ItemStack> tokens = NonNullList.create();

        for (int i = 0; i < inv.getSizeInventory(); ++i) {
            ItemStack stack = inv.getStackInSlot(i);
            if (!stack.isEmpty()) {
                if (stack.getItem() instanceof EnchantmentTokenItem) {
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
        result.setCount(1);
        for (ItemStack token : tokens) {
            if (!EnchantmentTokenItem.applyTokenToTool(token, result)) {
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
        return SERIALIZER;
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

    public static final class Serializer extends ForgeRegistryEntry<IRecipeSerializer<?>> implements IRecipeSerializer<ApplyEnchantmentTokenRecipe> {
        private Serializer() {}

        @Override
        public ApplyEnchantmentTokenRecipe read(ResourceLocation recipeId, JsonObject json) {
            return new ApplyEnchantmentTokenRecipe();
        }

        @Override
        public ApplyEnchantmentTokenRecipe read(ResourceLocation recipeId, PacketBuffer buffer) {
            return new ApplyEnchantmentTokenRecipe();
        }

        @Override
        public void write(PacketBuffer buffer, ApplyEnchantmentTokenRecipe recipe) {}
    }
}
