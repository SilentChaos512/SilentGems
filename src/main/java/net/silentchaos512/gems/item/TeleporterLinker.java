package net.silentchaos512.gems.item;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.silentchaos512.gems.config.GemsConfig;
import net.silentchaos512.gems.init.ModItemGroups;
import net.silentchaos512.lib.util.DimPos;
import net.silentchaos512.utils.Lazy;

import javax.annotation.Nullable;
import java.util.List;

public final class TeleporterLinker extends Item {
    public static final Lazy<TeleporterLinker> INSTANCE = Lazy.of(TeleporterLinker::new);

    private static final String NBT_LINKED = "Linked";

    private TeleporterLinker() {
        super(new Properties().maxStackSize(1).group(ModItemGroups.UTILITY));
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World world, List<ITextComponent> list, ITooltipFlag flag) {
        if (!isLinked(stack)) return;

        DimPos pos = getLinkedPosition(stack);
        if (!DimPos.ZERO.equals(pos)) {
            list.add(new TextComponentString(pos.toString()));
        }
    }

    public static boolean isLinked(ItemStack stack) {
        return stack.getOrCreateTag().getBoolean(NBT_LINKED);
    }

    public static void setLinked(ItemStack stack, boolean value) {
        stack.getOrCreateTag().putBoolean(NBT_LINKED, value);
    }

    public static DimPos getLinkedPosition(ItemStack stack) {
        return DimPos.read(stack.getOrCreateTag());
    }

    public static void setLinkedPosition(ItemStack stack, DimPos pos) {
        pos.write(stack.getOrCreateTag());
    }

    @Override
    public boolean hasEffect(ItemStack stack) {
        return isLinked(stack);
    }

    @OnlyIn(Dist.CLIENT)
    public static void renderGameOverlay(RenderGameOverlayEvent event) {
        if (event.getType() != RenderGameOverlayEvent.ElementType.TEXT) return;

        Minecraft mc = Minecraft.getInstance();
        EntityPlayer player = mc.player;

        ItemStack heldItem = player.getHeldItem(EnumHand.MAIN_HAND);
        if (heldItem.isEmpty()) {
            heldItem = player.getHeldItem(EnumHand.OFF_HAND);
        }

        if (heldItem.getItem() == INSTANCE.get()) {
            FontRenderer fontRender = mc.fontRenderer;
            int width = mc.mainWindow.getScaledWidth();
            int height = mc.mainWindow.getScaledHeight();

            String str;
            if (isLinked(heldItem)) {
                DimPos pos = getLinkedPosition(heldItem);
//                if (pos == null) return;
                double x = pos.getPos().getX() - player.posX;
                double z = pos.getPos().getZ() - player.posZ;
                int distance = (int) Math.sqrt(x * x + z * z);
                str = I18n.format("item.silentgems.teleporter_linker.distance", distance);

                int textX = width / 2 - fontRender.getStringWidth(str) / 2;
                int textY = height * 3 / 5;
                // Text colored differently depending on situation.
                int color = 0xffff00; // Outside free range, same dimension
                if (pos.getDimension() != player.dimension.getId()) {
                    color = 0xff6600; // Different dimension
                    str = I18n.format("item.silentgems.teleporter_linker.differentDimension");
                } else if (distance < GemsConfig.COMMON.teleporterFreeRange.get()) {
                    color = 0x00aaff; // Inside free range
                }
                fontRender.drawStringWithShadow(str, textX, textY, color);
            }
        }
    }
}
