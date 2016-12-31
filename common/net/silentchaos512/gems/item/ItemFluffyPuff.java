package net.silentchaos512.gems.item;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.silentchaos512.gems.SilentGems;
import net.silentchaos512.gems.block.ModBlocks;
import net.silentchaos512.gems.lib.Names;
import net.silentchaos512.lib.item.ItemSL;
import net.silentchaos512.lib.util.RecipeHelper;

public class ItemFluffyPuff extends ItemSL {

  public ItemFluffyPuff() {

    super(1, SilentGems.MODID, Names.FLUFFY_PUFF);
  }

  @Override
  public void addRecipes() {

    ItemStack puff = new ItemStack(this);
    ItemStack fabric = ModItems.craftingMaterial.fluffyFabric;
    ItemStack block = new ItemStack(ModBlocks.fluffyBlock);

    RecipeHelper.addCompressionRecipe(puff, fabric, 4);
    RecipeHelper.addCompressionRecipe(fabric, block, 4);

    ItemStack string = new ItemStack(Items.STRING);
    ItemStack feather = new ItemStack(Items.FEATHER);
    ItemStack wool = new ItemStack(Blocks.WOOL);
    ItemStack seed = new ItemStack(ModItems.fluffyPuffSeeds);

    GameRegistry.addShapedRecipe(string, "pp", 'p', puff);
    GameRegistry.addShapedRecipe(feather, " pp", "pp ", "p  ", 'p', puff);
    GameRegistry.addShapedRecipe(wool, "ppp", "p p", "ppp", 'p', puff);
    GameRegistry.addShapelessRecipe(seed, puff);
  }
}
