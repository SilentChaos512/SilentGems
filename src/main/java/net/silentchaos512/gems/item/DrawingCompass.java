package net.silentchaos512.gems.item;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

public class DrawingCompass extends Item {
    public enum State {
        EMPTY, BLOCK1, BLOCK2
    }

    public DrawingCompass() {
        super(new Properties().maxStackSize(1));
        addPropertyOverride(new ResourceLocation("state"),
                (stack, worldIn, entityIn) -> getState(stack).ordinal());
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
//        tooltip.add(TextFormatting.ITALIC + SilentGems.i18n.subText(this, "desc"));
    }

    private State getState(ItemStack stack) {
        if (getBlock2(stack).getY() > 0) {
            return State.BLOCK2;
        } else if (getBlock1(stack).getY() > 0) {
            return State.BLOCK1;
        }
        return State.EMPTY;
    }

    @Deprecated
    public ItemStack setState(ItemStack stack, State state) {
//        stack.setItemDamage(state.ordinal());
        return stack;
    }

    private static BlockPos getBlock1(ItemStack stack) {
        return getBlock(stack, 1);
    }

    private static BlockPos getBlock2(ItemStack stack) {
        return getBlock(stack, 2);
    }

    private static BlockPos getBlock(ItemStack stack, int index) {
        CompoundNBT tags = stack.getOrCreateChildTag("Block" + index);
        return new BlockPos(tags.getInt("X"), tags.getInt("Y"), tags.getInt("Z"));
    }

    private static void setBlock1(ItemStack stack, BlockPos pos) {
        setBlock(stack, pos, 1);
    }

    private static void setBlock2(ItemStack stack, BlockPos pos) {
        setBlock(stack, pos, 2);
    }

    private static void setBlock(ItemStack stack, BlockPos pos, int index) {
        CompoundNBT tags = stack.getOrCreateChildTag("Block" + index);
        tags.putInt("X", pos.getX());
        tags.putInt("Y", pos.getY());
        tags.putInt("Z", pos.getZ());
    }

    private static void clearBlocks(ItemStack stack) {
        setBlock1(stack, BlockPos.ZERO);
        setBlock2(stack, BlockPos.ZERO);
    }

//    private Color getColor(ItemStack stack) {
//        if (!stack.hasTagCompound() || !stack.getTagCompound().hasKey("Color")) {
//            return Color.WHITE;
//        }
//        return new Color(NBTHelper.getTagInt(stack, "Color"));
//    }

    private static int getColor(ItemStack stack) {
        CompoundNBT tags = stack.getOrCreateTag();
        if (tags.contains("CompassColor"))
            return tags.getInt("CompassColor");
        return 0xFFFFFF;
    }

    @Override
    public ActionResultType onItemUse(ItemUseContext context) {
        // Only allow changes when in main hand?
//        if (hand == EnumHand.OFF_HAND) {
//            return EnumActionResult.PASS;
//        }

        if (context.getWorld().isRemote) {
            return ActionResultType.SUCCESS; // Swing on client-side.
        }

        ItemStack stack = context.getItem();

        // Get current state.
        State state = getState(stack);
        BlockPos pos = context.getPos();
        Direction facing = context.getFace();
        PlayerEntity player = context.getPlayer();

        if (state == State.BLOCK1) {
            // Set block 2
            setBlock2(stack, pos.offset(facing));
            // Display distance.
            int radius = (int) Math.sqrt(getBlock1(stack).distanceSq(getBlock2(stack)));
//            String str = SilentGems.i18n.subText(this, "radius", radius);
//            ChatHelper.sendMessage(playerIn, str);
            if (player != null) {
                player.sendMessage(new StringTextComponent("radius = " + radius)); // FIXME
            }
        } else if (state == State.EMPTY) {
            // Set block 1
            setBlock1(stack, pos.offset(facing));
        } else if (player != null) {
//            String str = SilentGems.i18n.subText(this, "howToReset");
//            ChatHelper.sendMessage(playerIn, str);
            player.sendMessage(new StringTextComponent("howToReset")); // FIXME
        }

        return ActionResultType.PASS;
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand hand) {
        ItemStack stack = playerIn.getHeldItem(hand);
        if (hand == Hand.MAIN_HAND && playerIn.isCrouching() && !worldIn.isRemote) {
            clearBlocks(stack);
            return new ActionResult<>(ActionResultType.SUCCESS, stack);
        }
        return new ActionResult<>(ActionResultType.PASS, stack);
    }

    public boolean spawnParticles(ItemStack stack, PlayerEntity player, World world) {
        BlockPos pos1 = getBlock1(stack);
        BlockPos pos2 = getBlock2(stack);

        if (pos1.getY() <= 0 || pos2.getY() <= 0 || pos1.equals(pos2)) {
            return false;
        }

        int color = getColor(stack);

        // Spawn circle.
        BlockPos center = new BlockPos(pos1.getX(), pos2.getY(), pos1.getZ());
        float radius = (float) Math.sqrt(center.distanceSq(pos2));
        int count = (int) (5 * radius);
        float increment = (float) (2 * Math.PI / count);
        float start = increment * (world.getGameTime() % 30) / 30f;

        Vec3d vec;
        for (float angle = start; angle < 2 * Math.PI + start; angle += increment) {
            vec = new Vec3d(radius, 0, 0).rotateYaw(angle);
            particle(player, world, color, center.getX() + 0.5 + vec.x, center.getY() + 0.5 + vec.y, center.getZ() + 0.5 + vec.z);
        }

        // Spawn line.
        double distance = Math.sqrt(pos1.distanceSq(pos2));
        count = (int) (2 * distance);
        double dx = (double) (pos2.getX() - pos1.getX()) / count;
        double dy = (double) (pos2.getY() - pos1.getY()) / count;
        double dz = (double) (pos2.getZ() - pos1.getZ()) / count;

        for (int i = 0; i < count; ++i) {
            vec = new Vec3d(pos1.getX() + 0.5 + i * dx, pos1.getY() + 0.5 + i * dy, pos1.getZ() + 0.5 + i * dz);
            particle(player, world, color, vec.x, vec.y, vec.z);
        }

        return true;
    }

    private void particle(PlayerEntity player, World world, int color, double x, double y, double z) {
//        if (player.getDistanceSq(x, y, z) < 2048) {
//            SilentGems.proxy.spawnParticles(EnumModParticles.DRAWING_COMPASS, color, world, x, y, z, 0, 0, 0);
//        }
    }
}
