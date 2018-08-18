package net.silentchaos512.gems.item.quiver;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.entity.projectile.EntityTippedArrow;
import net.minecraft.init.Enchantments;
import net.minecraft.item.ItemArrow;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;
import net.minecraftforge.items.IItemHandler;
import net.silentchaos512.gems.SilentGems;

import java.util.List;

public class ItemQuiver extends ItemArrow implements IQuiver {
    public static final int MAX_STACKS = 4;

    public ItemQuiver() {
        setMaxStackSize(1);
    }

    @Override
    public EntityArrow createArrow(World worldIn, ItemStack quiverStack, EntityLivingBase shooter) {
        IItemHandler itemHandler = getInventory(quiverStack);
        for (int i = 0; i < itemHandler.getSlots(); ++i) {
            ItemStack arrowStack = itemHandler.getStackInSlot(i);
            if (!arrowStack.isEmpty() && arrowStack.getItem() instanceof ItemArrow) {
                // Found arrow stack in quiver
                boolean playerIsCreativeMode = shooter instanceof EntityPlayer
                        && ((EntityPlayer) shooter).capabilities.isCreativeMode;
                if (!playerIsCreativeMode) {
                    // Remove arrow from quiver
                    itemHandler.extractItem(i, 1, false);
                    updateQuiver(quiverStack, itemHandler, (EntityPlayer) shooter);
                }
                // Create arrow entity
                ItemArrow itemArrow = (ItemArrow) arrowStack.getItem();
                EntityArrow entity = itemArrow.createArrow(worldIn, arrowStack, shooter);
                if (!playerIsCreativeMode) {
                    QuiverHelper.instance.addFiredArrow(entity);
                }
                return entity;
            }
        }

        // Something went wrong?
        SilentGems.logHelper.warn("Quiver could not find arrow! Player: " + shooter.getName());
        return new EntityTippedArrow(worldIn, shooter);
    }

    @Override
    public boolean isInfinite(ItemStack stack, ItemStack bow, net.minecraft.entity.player.EntityPlayer player) {

        int infinity = EnchantmentHelper.getEnchantmentLevel(Enchantments.INFINITY, bow);
        if (infinity <= 0) {
            // TODO
            // NBTTagCompound nbt;
            // nbt = stack.getTagCompound();
            // nbt.setInteger("Arrows", nbt.getInteger("Arrows") - 1);
            // stack.setTagCompound(nbt);
            return true;
        } else {
            return true;
        }
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand) {
        return QuiverHelper.onItemRightClick(world, player, hand);
    }

    @Override
    public void addInformation(ItemStack stack, World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        QuiverHelper.addInformation(stack, worldIn, tooltip, flagIn);
    }
}
