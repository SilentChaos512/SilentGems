package net.silentchaos512.gems.util;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.silentchaos512.gear.util.GearData;
import net.silentchaos512.gear.util.GearHelper;
import net.silentchaos512.gems.item.GearSoulItem;
import net.silentchaos512.gems.lib.soul.GearSoul;
import net.silentchaos512.gems.lib.soul.GearSoulPart;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public final class SoulManager {
    private static final String NBT_SOUL = "SG_GearSoul";

    static final Map<UUID, GearSoul> SOULS = new HashMap<>();

    private SoulManager() {}

    @Nullable
    public static GearSoul getSoul(ItemStack gearOrSoul) {
        if (!gearOrSoul.isEmpty()) {
            if (GearHelper.isGear(gearOrSoul)) {
                return getSoulFromGear(gearOrSoul);
            }
            if (gearOrSoul.getItem() instanceof GearSoulItem) {
                return getSoulFromItem(gearOrSoul);
            }
        }

        return null;
    }

    @Nullable
    private static GearSoul getSoulFromGear(ItemStack gear) {
        // Find soul part, if it exists
        // We can't actually read/write anything to part data, just make sure the part is there
        if (!GearData.hasPartOfType(gear, GearSoulPart.TYPE) || !gear.hasTag() || !gear.getOrCreateTag().contains(NBT_SOUL)) {
            return null;
        }

        return getSoulFromItem(gear);
    }

    private static GearSoul getSoulFromItem(ItemStack soulItem) {
        return new GearSoul(soulItem);
    }

    public static void setSoul(ItemStack gear, GearSoul soul) {
        CompoundNBT tags = new CompoundNBT();
        soul.write(tags);
        gear.getOrCreateTag().put(NBT_SOUL, tags);
    }

    public static void addSoulXp(int amount, ItemStack gear, @Nullable PlayerEntity player) {
        GearSoul soul = getSoul(gear);
        if (soul != null && amount > 0) {
            soul.addXp(amount, player);
        }
    }
}
