package net.silentchaos512.gems.item.quiver;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;
import net.silentchaos512.gems.lib.Names;
import net.silentchaos512.lib.registry.IAddRecipes;
import net.silentchaos512.lib.registry.RecipeMaker;

import java.util.List;

public class ItemQuiverEmpty extends Item implements IQuiver, IAddRecipes {
    public ItemQuiverEmpty() {
        setMaxStackSize(1);
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand) {
        return QuiverHelper.onItemRightClick(world, player, hand);
    }

    @Override
    public void addInformation(ItemStack stack, World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        QuiverHelper.addInformation(stack, worldIn, tooltip, flagIn);
    }

    @Override
    public void addRecipes(RecipeMaker recipes) {
        recipes.addShaped(Names.QUIVER, new ItemStack(this), "lsl", "lal", " l ", 'l', "leather", 'a',
                Items.ARROW, 's', "string");
    }
}
