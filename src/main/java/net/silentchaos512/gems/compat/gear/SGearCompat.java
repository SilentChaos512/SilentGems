/*
 * Silent's Gems -- SGearCompat
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
//import net.silentchaos512.gear.api.parts.ItemPartData;
//import net.silentchaos512.gear.api.parts.MaterialGrade;
//import net.silentchaos512.gear.api.parts.PartMain;
//import net.silentchaos512.gear.api.parts.PartRegistry;
import org.apache.commons.lang3.NotImplementedException;

public final class SGearCompat {
    private SGearCompat() {
        throw new IllegalAccessError("Utility class");
    }

    public static String getGradeString(ItemStack stack) {
//        MaterialGrade grade = MaterialGrade.fromStack(stack);
//        return grade.name();
        throw new NotImplementedException("Silent Gear compat missing!");
    }

    public static int getPartTier(ItemStack stack) {
//        ItemPartData part = ItemPartData.fromStack(stack);
//        return part != null ? part.getPart().getTier() : -1;
        throw new NotImplementedException("Silent Gear compat missing!");
    }

    public static boolean isMainPart(ItemStack stack) {
//        return PartRegistry.get(stack) instanceof PartMain;
        throw new NotImplementedException("Silent Gear compat missing!");
    }
}
