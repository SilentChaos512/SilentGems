package net.silentchaos512.gems.compat.rei;

import me.shedaniel.rei.api.RecipeDisplay;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.tags.Tag;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.ResourceLocation;
import net.silentchaos512.gear.api.parts.IGearPart;
import net.silentchaos512.gear.parts.PartManager;
import net.silentchaos512.gems.init.ModEnchantments;
import net.silentchaos512.gems.init.ModTags;

import javax.annotation.Nullable;
import java.util.*;

public class SuperchargerRecipeDisplay implements RecipeDisplay {
    private final int tier;

    public SuperchargerRecipeDisplay(int tier) {
        this.tier = tier;
    }

    public int getTier() {
        return tier;
    }

    @Override
    public Optional getRecipe() {
        return Optional.empty();
    }

    @Override
    public List<List<ItemStack>> getInput() {
        List<List<ItemStack>> lists = new ArrayList<>();
        // Mains
        List<ItemStack> mains = new ArrayList<>();
        for (IGearPart part : PartManager.getMains()) {
            Tag<Item> tag = part.getMaterials().getTag();
            IItemProvider item = part.getMaterials().getItem();
            if (tag != null) {
                ItemStack[] matchingStacks = Ingredient.fromTag(tag).getMatchingStacks();
                if (matchingStacks.length > 0) {
                    mains.add(matchingStacks[0]);
                }
            } else if (item != null) {
                mains.add(new ItemStack(item));
            }
        }
        lists.add(mains);
        // Catalyst
        Tag<Item> catalystTag = getCatalystTag();
        if (catalystTag != null) {
            Ingredient ingredient = Ingredient.fromTag(catalystTag);
            lists.add(Arrays.asList(ingredient.getMatchingStacks()));
        } else {
            lists.add(Collections.emptyList());
        }
        return lists;
    }

    @Nullable
    private Tag<Item> getCatalystTag() {
        if (tier == 1) return ModTags.Items.CHARGING_AGENT_TIER1;
        if (tier == 2) return ModTags.Items.CHARGING_AGENT_TIER2;
        if (tier == 3) return ModTags.Items.CHARGING_AGENT_TIER3;
        return null;
    }

    @Override
    public List<ItemStack> getOutput() {
        List<ItemStack> list = new ArrayList<>();
        for (IGearPart part : PartManager.getMains()) {
            Tag<Item> tag = part.getMaterials().getTag();
            IItemProvider item = part.getMaterials().getItem();
            if (item != null) {
                ItemStack stack = new ItemStack(item);
                stack.addEnchantment(ModEnchantments.supercharged, this.tier);
                list.add(stack);
            } else if (tag != null) {
                ItemStack[] matchingStacks = Ingredient.fromTag(tag).getMatchingStacks();
                if (matchingStacks.length > 0) {
                    ItemStack stack = matchingStacks[0].copy();
                    stack.addEnchantment(ModEnchantments.supercharged, this.tier);
                    list.add(stack);
                }
            }
        }
        return list;
    }

    @Override
    public ResourceLocation getRecipeCategory() {
        return ReiPluginGems.SUPERCHARGING;
    }
}
