package net.silentchaos512.gems.item;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.oredict.OreDictionary;
import net.silentchaos512.gems.init.ModItems;
import net.silentchaos512.lib.registry.RecipeMaker;

import java.util.List;

public class ItemTorchBandolier extends ItemBlockPlacer {
    private static final int MAX_DAMAGE = 1024;

    public ItemTorchBandolier() {
        super(MAX_DAMAGE);
    }

    @Override
    public IBlockState getBlockPlaced(ItemStack stack) {
        return Blocks.TORCH.getDefaultState();
    }

    @Override
    public int getBlockMetaDropped(ItemStack stack) {
        return 0;
    }

    @Override
    public void addRecipes(RecipeMaker recipes) {
        String line1 = "lll";
        String line2 = "sgs";

        ItemStack bandolier = new ItemStack(this);
        setRemainingBlocks(bandolier, 0);
        ItemStack gem = new ItemStack(ModItems.gem, 1, OreDictionary.WILDCARD_VALUE);
        Object[] stacks = new Object[]{"leather", ModItems.craftingMaterial.fluffyFabric};

        int i = -1;
        for (Object stack : stacks) {
            recipes.addShapedOre("torch_bandolier_" + (++i), bandolier, line1, line2, line1, 'l', stack,
                    's', "stickWood", 'g', gem);
        }
    }

    @Override
    public void addInformation(ItemStack stack, World world, List<String> list, ITooltipFlag flag) {
        list.add(I18n.format("misc.silentgems.legacyItem"));
        super.addInformation(stack, world, list, flag);
    }
}
