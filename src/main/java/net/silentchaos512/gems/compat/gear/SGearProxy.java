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

import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.ModList;
import net.silentchaos512.gems.SilentGems;

public final class SGearProxy {
    private static boolean modLoaded = false;

    private SGearProxy() {
        throw new IllegalAccessError("Utility class");
    }

    public static void detectSilentGear() {
        modLoaded = ModList.get().isLoaded("silentgear");
        if (modLoaded) {
            SilentGems.LOGGER.info("Detected Silent Gear!");
        }
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

    public static boolean isMainPart(ItemStack stack) {
        if (modLoaded) return SGearCompat.isMainPart(stack);
        return false;
    }
}
