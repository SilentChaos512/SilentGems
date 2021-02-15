package net.silentchaos512.gemschaos.item;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.IEntityReader;
import net.minecraft.world.World;
import net.silentchaos512.gemschaos.ChaosMod;

import javax.annotation.Nullable;
import java.util.List;
import java.util.UUID;

public abstract class PlayerLinkedItem extends Item {
    private static final String NBT_PLAYER = "Player";

    public PlayerLinkedItem(Properties properties) {
        super(properties);
    }

    @Nullable
    public static PlayerEntity getOwner(ItemStack stack, IEntityReader world) {
        UUID uuid = getOwnerUuid(stack);
        if (uuid != null) {
            return world.getPlayerByUuid(uuid);
        }
        return null;
    }

    @Nullable
    public static UUID getOwnerUuid(ItemStack stack) {
        if (stack.getOrCreateTag().contains(NBT_PLAYER)) {
            return stack.getOrCreateTag().getUniqueId(NBT_PLAYER);
        }
        return null;
    }

    public static void setOwner(ItemStack stack, PlayerEntity player) {
        stack.getOrCreateTag().putUniqueId(NBT_PLAYER, player.getUniqueID());
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        UUID ownerUuid = getOwnerUuid(stack);

        if (worldIn != null) {
            PlayerEntity owner = getOwner(stack, worldIn);

            if (ownerUuid != null) {
                tooltip.add(getOwnerText(ownerUuid, owner));
            }
        }
    }

    private static ITextComponent getOwnerText(UUID ownerUuid, @Nullable PlayerEntity owner) {
        ITextComponent text = owner != null
                ? new StringTextComponent(owner.getScoreboardName()).mergeStyle(TextFormatting.GREEN)
                : new StringTextComponent(ownerUuid.toString()).mergeStyle(TextFormatting.RED);
        return ChaosMod.TEXT.translate("item", "chaos_linker.owner", text);
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand handIn) {
        ItemStack stack = playerIn.getHeldItem(handIn);
        UUID ownerUuid = getOwnerUuid(stack);

        if (ownerUuid == null) {
            if (playerIn.isSneaking()) {
                setOwner(stack, playerIn);
                return ActionResult.resultSuccess(stack);
            } else {
                playerIn.sendStatusMessage(ChaosMod.TEXT.translate("item", "chaos_linker.notSneaking"), true);
                return ActionResult.resultFail(stack);
            }
        }

        return ActionResult.resultPass(stack);
    }
}
