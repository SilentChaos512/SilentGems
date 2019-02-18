package net.silentchaos512.gems.item;

import net.minecraft.item.Item;
import net.silentchaos512.gems.init.ModItemGroups;

public class TeleporterLinker extends Item {
    public TeleporterLinker() {
        super(new Properties().maxStackSize(1).group(ModItemGroups.UTILITY));
    }

//    @Override
//    public void addInformation(ItemStack stack, World world, List<String> list, ITooltipFlag flag) {
//        DimensionalPosition pos = getLinkedPosition(stack);
//        if (pos != null) {
//            list.add(new TextComponentString(pos.toString()));
//        }
//    }
//
//    public boolean isLinked(ItemStack stack) {
//        return stack.getItemDamage() != 0;
//    }
//
//    public void setLinked(ItemStack stack, boolean value) {
//        stack.setItemDamage(value ? 1 : 0);
//    }

//    @Nullable
//    public DimensionalPosition getLinkedPosition(ItemStack stack) {
//        if (!stack.hasTagCompound()) {
//            return null;
//        }
//        return DimensionalPosition.readFromNBT(stack.getTagCompound());
//    }
//
//    public void setLinkedPosition(ItemStack stack, DimensionalPosition pos) {
//        if (!stack.hasTagCompound()) {
//            stack.setTagCompound(new NBTTagCompound());
//        }
//        pos.writeToNBT(stack.getTagCompound());
//    }
//
//    @Override
//    public boolean hasEffect(ItemStack stack) {
//        return isLinked(stack);
//    }
//
//    @SideOnly(Side.CLIENT)
//    public void renderGameOverlay(RenderGameOverlayEvent event) {
//        if (event.getType() != ElementType.TEXT) return;
//
//        Minecraft mc = Minecraft.getMinecraft();
//        EntityPlayer player = mc.player;
//
//        ItemStack heldItem = mc.player.getHeldItem(EnumHand.MAIN_HAND);
//        if (heldItem.isEmpty()) {
//            heldItem = mc.player.getHeldItem(EnumHand.OFF_HAND);
//        }
//
//        if (heldItem.getItem() == this) {
//
//            ScaledResolution res = new ScaledResolution(mc);
//            FontRenderer fontRender = mc.fontRenderer;
//            int width = res.getScaledWidth();
//            int height = res.getScaledHeight();
//
//            String str;
//            if (isLinked(heldItem)) {
//                DimensionalPosition pos = getLinkedPosition(heldItem);
//                if (pos == null) return;
//                double x = pos.x - player.posX;
//                double z = pos.z - player.posZ;
//                int distance = (int) Math.sqrt(x * x + z * z);
//                str = SilentGems.i18n.subText(this, "distance", distance);
//
//                int textX = width / 2 - fontRender.getStringWidth(str) / 2;
//                int textY = height * 3 / 5;
//                // Text colored differently depending on situation.
//                int color = 0xffff00; // Outside free range, same dimension
//                if (pos.dim != player.dimension) {
//                    color = 0xff6600; // Different dimension
//                    str = SilentGems.i18n.subText(this, "differentDimension");
//                } else if (distance < GemsConfig.TELEPORTER_FREE_RANGE) {
//                    color = 0x00aaff; // Inside free range
//                }
//                fontRender.drawStringWithShadow(str, textX, textY, color);
//            }
//        }
//    }
}
