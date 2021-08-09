package net.silentchaos512.gems.item;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.silentchaos512.lib.util.PlayerUtils;

import javax.annotation.Nullable;

public class GemsFoodItem extends Item {
    private final ItemLike returnItem;

    public GemsFoodItem(FoodProperties.Builder foodBuilder, @Nullable ItemLike returnItem, Properties properties) {
        super(properties.food(foodBuilder.build()));
        this.returnItem = returnItem;
    }

    @Override
    public ItemStack finishUsingItem(ItemStack stack, Level worldIn, LivingEntity entityLiving) {
        if (returnItem != null && !worldIn.isClientSide && entityLiving instanceof Player) {
            PlayerUtils.giveItem((Player) entityLiving, new ItemStack(returnItem));
        }
        return super.finishUsingItem(stack, worldIn, entityLiving);
    }
}
