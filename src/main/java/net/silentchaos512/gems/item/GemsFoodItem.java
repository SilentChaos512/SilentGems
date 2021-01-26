package net.silentchaos512.gems.item;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Food;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IItemProvider;
import net.minecraft.world.World;
import net.silentchaos512.lib.util.PlayerUtils;

import javax.annotation.Nullable;

public class GemsFoodItem extends Item {
    private final IItemProvider returnItem;

    public GemsFoodItem(Food.Builder foodBuilder, @Nullable IItemProvider returnItem, Properties properties) {
        super(properties.food(foodBuilder.build()));
        this.returnItem = returnItem;
    }

    @Override
    public ItemStack onItemUseFinish(ItemStack stack, World worldIn, LivingEntity entityLiving) {
        if (returnItem != null && !worldIn.isRemote && entityLiving instanceof PlayerEntity) {
            PlayerUtils.giveItem((PlayerEntity) entityLiving, new ItemStack(returnItem));
        }
        return super.onItemUseFinish(stack, worldIn, entityLiving);
    }
}
