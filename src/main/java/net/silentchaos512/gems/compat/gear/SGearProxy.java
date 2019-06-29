/*
 * Silent's Gems -- SGearProxy
 * Copyright (C) 2018 SilentChaos512
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation version 3
 * of the License.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package net.silentchaos512.gems.compat.gear;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.Loader;
import net.silentchaos512.gems.SilentGems;
import net.silentchaos512.gems.api.IGearItem;
import net.silentchaos512.gems.lib.EnumToolType;

import javax.annotation.Nullable;

public final class SGearProxy {
    private static boolean modLoaded = false;

    private SGearProxy() {
        throw new IllegalAccessError("Utility class");
    }

    public static void detectSilentGear() {
        modLoaded = Loader.isModLoaded("silentgear");
        if (modLoaded)
            SilentGems.logHelper.info("Detected Silent Gear!");
    }

    public static boolean isLoaded() {
        return modLoaded;
    }

    public static String getGradeString(ItemStack stack) {
        if (modLoaded) return SGearCompat.getGradeString(stack);
        return "N/A";
    }

    public static int getPartTier(ItemStack stack) {
        if (modLoaded) return SGearCompat.getPartTier(stack);
        return -1;
    }

    public static boolean isGearItem(ItemStack stack) {
        if (modLoaded) return SGearCompat.isGearItem(stack);
        return false;
    }

    public static boolean isMainPart(ItemStack stack) {
        if (modLoaded) return SGearCompat.isMainPart(stack);
        return false;
    }

    public static EnumToolType getSGemsTypeFromGearItem(ItemStack stack) {
        if (stack.getItem() instanceof IGearItem)
            return ((IGearItem) stack.getItem()).getToolType();

        if (modLoaded) return SGearCompat.getSGemsTypeFromGearItem(stack);
        return EnumToolType.NONE;
    }

    public static void recalculateStats(ItemStack stack, @Nullable EntityPlayer player) {
        if (modLoaded) SGearCompat.recalculateStats(stack, player);
    }

    public static void addSoulPart(ItemStack stack) {
        if (modLoaded) SGearCompat.addSoulPart(stack);
    }
}
