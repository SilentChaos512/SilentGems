package net.silentchaos512.gems.block;

import net.minecraft.block.BlockTorch;
import net.minecraft.block.SoundType;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;
import net.silentchaos512.gems.item.CraftingItems;
import net.silentchaos512.lib.registry.IAddRecipes;
import net.silentchaos512.lib.registry.RecipeMaker;

public class BlockStoneTorch extends BlockTorch implements IAddRecipes {
    public BlockStoneTorch() {
        this.setHardness(0.0f);
        this.setLightLevel(0.9375f);
        this.setSoundType(SoundType.STONE);
    }

    @Override
    public void addRecipes(RecipeMaker recipes) {
        recipes.addShapedOre("stone_torch", new ItemStack(this, 4),
                "c", "s",
                'c', new ItemStack(Items.COAL, 1, OreDictionary.WILDCARD_VALUE), 's', "rodStone");
        recipes.addShapedOre("stone_torch_chaos", new ItemStack(this, 16),
                "c", "s",
                'c', CraftingItems.CHAOS_COAL.getStack(), 's', "rodStone");
    }
}
