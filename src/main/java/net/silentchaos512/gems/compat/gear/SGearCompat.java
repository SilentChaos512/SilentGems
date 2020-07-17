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
import net.minecraft.util.ResourceLocation;
import net.silentchaos512.gear.api.material.IMaterial;
import net.silentchaos512.gear.api.parts.MaterialGrade;
import net.silentchaos512.gear.api.parts.PartType;
import net.silentchaos512.gear.gear.material.MaterialManager;
import net.silentchaos512.gear.parts.PartData;
import net.silentchaos512.gear.util.TraitHelper;

public final class SGearCompat {
    private SGearCompat() {
        throw new IllegalAccessError("Utility class");
    }

    public static String getGradeString(ItemStack stack) {
        MaterialGrade grade = MaterialGrade.fromStack(stack);
        return grade.name();
    }

    public static int getPartTier(ItemStack stack) {
        PartData part = PartData.fromStackFast(stack);
        return part != null ? part.getPart().getTier() : -1;
    }

    public static boolean isMainPart(ItemStack stack) {
        PartData part = PartData.fromStackFast(stack);
        return part != null && part.getType() == PartType.MAIN;
    }

    public static int getTraitLevel(ItemStack stack, ResourceLocation traitId) {
        return TraitHelper.getTraitLevel(stack, traitId);
    }

    public static boolean isMaterial(ItemStack stack) {
        return MaterialManager.from(stack) != null;
    }

    public static int getMaterialTier(ItemStack stack) {
        IMaterial material = MaterialManager.from(stack);
        return material != null ? material.getTier(PartType.MAIN) : -1;
    }
}
