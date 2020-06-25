package net.silentchaos512.gems.item;

import net.minecraft.entity.Entity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Rarity;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.silentchaos512.gems.compat.gear.SGearProxy;
import net.silentchaos512.gems.init.GemsItemGroups;
import net.silentchaos512.gems.lib.soul.GearSoul;
import net.silentchaos512.gems.util.SoulManager;
import net.silentchaos512.lib.event.ClientTicks;
import net.silentchaos512.utils.Color;

public final class GearSoulItem extends Item {
    public GearSoulItem() {
        super(new Properties()
                .group(GemsItemGroups.UTILITY)
                .rarity(Rarity.RARE)
                .maxStackSize(1)
        );
    }

    @Override
    public void inventoryTick(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
        if (SGearProxy.isLoaded() && !stack.isEmpty() && !stack.hasTag()) {
            GearSoul soul = GearSoul.randomSoul();
            SoulManager.setSoul(stack, soul);
        }
    }

    public static int getColor(ItemStack stack, int tintIndex) {
        GearSoul soul = SoulManager.getSoul(stack);
        if (soul == null) return Color.VALUE_WHITE;

        switch (tintIndex) {
            case 0:
                float ratio = 0.5f + MathHelper.sin((float) ClientTicks.ticksInGame() / 15) / 6;
                return Color.blend(soul.getPrimaryElement().color, soul.getSecondaryElement().color, ratio);
            case 1:
                return soul.getPrimaryElement().color;
            case 2:
                return soul.getSecondaryElement().color;
            default:
                return Color.VALUE_WHITE;
        }
    }
}
