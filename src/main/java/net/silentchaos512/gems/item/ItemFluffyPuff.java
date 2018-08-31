package net.silentchaos512.gems.item;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.silentchaos512.gems.init.ModBlocks;
import net.silentchaos512.gems.init.ModItems;
import net.silentchaos512.lib.registry.IAddRecipes;
import net.silentchaos512.lib.registry.RecipeMaker;

public class ItemFluffyPuff extends Item implements IAddRecipes {
  @Override
  public void addRecipes(RecipeMaker recipes) {

    ItemStack puff = new ItemStack(this);
    ItemStack fabric = CraftingItems.FLUFFY_FABRIC.getStack();
    ItemStack block = new ItemStack(ModBlocks.fluffyBlock);

    IRecipe rec = recipes.addCompression("fluffy_fabric", puff, fabric, 4)[0];
    CraftingItems.ItemCrafting.guideRecipeMap.put(fabric.getItemDamage(), rec);
    recipes.addCompression("fluffy_block", fabric, block, 4);

    ItemStack string = new ItemStack(Items.STRING);
    ItemStack feather = new ItemStack(Items.FEATHER);
    ItemStack wool = new ItemStack(Blocks.WOOL);
    ItemStack seed = new ItemStack(ModItems.fluffyPuffSeeds);

    recipes.addShaped("string_fluffypuff", string, "pp", 'p', puff);
    recipes.addShaped("feather_fluffypuff", feather, " pp", "pp ", "p  ", 'p', puff);
    recipes.addShaped("wool_fluffypuff", wool, "ppp", "p p", "ppp", 'p', puff);
    recipes.addShapeless("fluff_puff_seed", seed, puff);
  }
}
