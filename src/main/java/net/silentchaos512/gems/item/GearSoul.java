package net.silentchaos512.gems.item;

import net.minecraft.item.Item;
import net.silentchaos512.gems.init.ModItemGroups;

public class GearSoul extends Item {
    private static final String NBT_KEY = "SGGearSoul";

    public GearSoul() {
        super(new Item.Builder()
                .maxStackSize(1)
                .group(ModItemGroups.UTILITY));
    }

//    @Nullable
//    public ToolSoul getSoul(ItemStack stack) {
//        if (!stack.hasTagCompound() || !stack.getTagCompound().hasKey(NBT_KEY)) {
//            return null;
//        }
//        return ToolSoul.readFromNBT(stack.getTagCompound().getCompoundTag(NBT_KEY));
//    }
//
//    public void setSoul(ItemStack stack, ToolSoul soul) {
//        if (!stack.hasTagCompound()) {
//            stack.setTagCompound(new NBTTagCompound());
//        }
//        if (!stack.getTagCompound().hasKey(NBT_KEY)) {
//            stack.getTagCompound().setTag(NBT_KEY, new NBTTagCompound());
//        }
//        soul.writeToNBT(stack.getTagCompound().getCompoundTag(NBT_KEY));
//    }
//
//    @Override
//    public void addInformation(ItemStack stack, World world, List<String> list, ITooltipFlag flag) {
//        ToolSoul soul = getSoul(stack);
//        if (soul != null) {
//            soul.addInformation(stack, world, list, flag.isAdvanced());
//        }
//    }
//
//    @Override
//    public void onUpdate(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
//        if (!stack.isEmpty() && !stack.hasTagCompound()) {
//            // Randomize souls with no data!
//            ToolSoul soul = ToolSoul.randomSoul();
//            setSoul(stack, soul);
//        }
//    }
}
