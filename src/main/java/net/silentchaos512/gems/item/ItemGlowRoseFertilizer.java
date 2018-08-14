package net.silentchaos512.gems.item;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.silentchaos512.gems.SilentGems;
import net.silentchaos512.gems.init.ModBlocks;
import net.silentchaos512.gems.init.ModItems;
import net.silentchaos512.gems.lib.EnumGem;
import net.silentchaos512.lib.registry.IAddRecipes;
import net.silentchaos512.lib.registry.RecipeMaker;

import java.util.Random;

public class ItemGlowRoseFertilizer extends Item implements IAddRecipes {
    @Override
    public void addRecipes(RecipeMaker recipes) {
        recipes.addSurround("glowrose_fertilizer", new ItemStack(this, 4),
                ModItems.craftingMaterial.chaosEssence,
                new ItemStack(Items.DYE, 1, EnumDyeColor.WHITE.getDyeDamage()));
    }

    @Override
    public EnumActionResult onItemUse(EntityPlayer playerIn, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        Random rand = SilentGems.random;
        BlockPos center = pos.offset(EnumFacing.UP);

        if (worldIn.isAirBlock(center)) {
            // Always one at position used.
            EnumGem gem = EnumGem.values()[rand.nextInt(16)];
            IBlockState glowRose = ModBlocks.glowRose.getDefaultState().withProperty(EnumGem.VARIANT_GEM, gem);

            // Fail to use if glow rose can't be placed at center.
            if (!ModBlocks.glowRose.canBlockStay(worldIn, center, glowRose) || !worldIn.isAirBlock(center) || !worldIn.isAirBlock(center.up()))
                return EnumActionResult.FAIL;
                // Only make changes on the server side!
            else if (worldIn.isRemote)
                return EnumActionResult.SUCCESS;

            worldIn.setBlockState(center, glowRose, 2);

            // Random extras
            int extraCount = rand.nextInt(3);
            BlockPos target;
            int x, y, z;
            for (int i = 0; i < extraCount; ++i) {
                x = center.getX() + rand.nextInt(7) - 3;
                z = center.getZ() + rand.nextInt(7) - 3;
                // Try place one block higher, move down 1-2 if needed.
                for (y = center.getY() + 1; y > center.getY() - 2; --y) {
                    target = new BlockPos(x, y, z);
                    gem = EnumGem.values()[rand.nextInt(16)];
                    glowRose = ModBlocks.glowRose.getDefaultState().withProperty(EnumGem.VARIANT_GEM, gem);
                    if (ModBlocks.glowRose.canBlockStay(worldIn, target, glowRose)
                            && worldIn.isAirBlock(target) && worldIn.isAirBlock(target.up())) {
                        worldIn.setBlockState(target, glowRose, 2);
                        break;
                    }
                }
            }

            if (!playerIn.capabilities.isCreativeMode)
                playerIn.getHeldItem(hand).shrink(1);

            return EnumActionResult.SUCCESS;
        }

        return EnumActionResult.FAIL;
    }
}
