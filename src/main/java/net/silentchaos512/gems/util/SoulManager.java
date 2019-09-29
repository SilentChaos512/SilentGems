package net.silentchaos512.gems.util;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.silentchaos512.gear.api.item.ICoreItem;
import net.silentchaos512.gear.parts.PartData;
import net.silentchaos512.gear.util.GearData;
import net.silentchaos512.gear.util.GearHelper;
import net.silentchaos512.gems.SilentGems;
import net.silentchaos512.gems.item.GearSoulItem;
import net.silentchaos512.gems.lib.soul.GearSoul;
import net.silentchaos512.gems.lib.soul.GearSoulPart;
import net.silentchaos512.lib.util.PlayerUtils;

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
        PartData part = GearData.getPartOfType(gear, GearSoulPart.TYPE);
        if (part == null || !gear.hasTag() || !gear.getOrCreateTag().contains(NBT_SOUL)) {
            return null;
        }

        // Soul already in map?
        UUID uuid = GearData.getUUID(gear);
        GearSoul soul = SOULS.get(uuid);
        if (soul != null) {
            return soul;
        }

        // Not in map; read from NBT and save it in map for quick access
        GearSoul readSoul = getSoulFromItem(gear);
        SOULS.put(uuid, readSoul);
        return readSoul;
    }

    private static GearSoul getSoulFromItem(ItemStack soulItem) {
        CompoundNBT tags = soulItem.getOrCreateChildTag(NBT_SOUL);
        return GearSoul.read(tags);
    }

    @Nullable
    public static GearSoul getSoulByUuid(UUID uuid) {
        return SOULS.get(uuid);
    }

    public static void setSoul(ItemStack gear, GearSoul soul) {
        CompoundNBT tags = new CompoundNBT();
        soul.write(tags);
        gear.getOrCreateTag().put(NBT_SOUL, tags);
    }

    public static void addSoulXp(int amount, ItemStack tool, @Nullable PlayerEntity player) {
        GearSoul soul = getSoul(tool);
        if (soul != null && amount > 0) {
            soul.addXp(amount, tool, player);
        }
    }

    static void queueSoulsForWrite(PlayerEntity player) {
        for (ItemStack tool : PlayerUtils.getNonEmptyStacks(player, true, true, true, s -> s.getItem() instanceof ICoreItem)) {
            GearSoul soul = getSoul(tool);
            if (soul != null) {
                soul.setReadyToSave(true);
            }
        }
    }

    static void writeSoulsToNBT(PlayerEntity player, boolean forceAll) {
        // Find all the players tools. Find the matching souls in the map.
        int count = 0;
        for (ItemStack tool : PlayerUtils.getNonEmptyStacks(player, true, true, true, s -> s.getItem() instanceof ICoreItem)) {
            GearSoul soul = getSoul(tool);
            if (soul != null && (forceAll || soul.isReadyToSave())) {
                setSoul(tool, soul);
                ++count;
            }
        }
        SilentGems.LOGGER.debug("Saved {} gear souls for {}", count, player.getScoreboardName());
    }
}
