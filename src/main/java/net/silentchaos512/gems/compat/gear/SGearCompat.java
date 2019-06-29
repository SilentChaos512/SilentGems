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

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.silentchaos512.gear.api.item.ICoreArmor;
import net.silentchaos512.gear.api.item.ICoreItem;
import net.silentchaos512.gear.api.parts.*;
import net.silentchaos512.gear.util.GearData;
import net.silentchaos512.gems.init.SGearMaterials;
import net.silentchaos512.gems.lib.EnumToolType;
import net.silentchaos512.gems.lib.soul.ToolSoulPart;

import javax.annotation.Nullable;

public final class SGearCompat {
    private SGearCompat() {
        throw new IllegalAccessError("Utility class");
    }

    public static String getGradeString(ItemStack stack) {
        MaterialGrade grade = MaterialGrade.fromStack(stack);
        return grade.name();
    }

    public static int getPartTier(ItemStack stack) {
        ItemPartData part = ItemPartData.fromStack(stack);
        return part != null ? part.getPart().getTier() : -1;
    }

    public static boolean isGearItem(ItemStack stack) {
        return stack.getItem() instanceof ICoreItem;
    }

    public static boolean isMainPart(ItemStack stack) {
        return PartRegistry.get(stack) instanceof PartMain;
    }

    @SuppressWarnings("ChainOfInstanceofChecks")
    public static EnumToolType getSGemsTypeFromGearItem(ItemStack stack) {
        if (stack.getItem() instanceof ICoreArmor) {
            return EnumToolType.ARMOR;
        }
        if (stack.getItem() instanceof ICoreItem) {
            ICoreItem item = (ICoreItem) stack.getItem();
            if (item.getGearType().matches("melee_weapon"))
                return EnumToolType.SWORD;
            if (item.getGearType().matches("ranged_weapon"))
                return EnumToolType.BOW;
            if (item.getGearType().matches("harvest_tool"))
                return EnumToolType.HARVEST;
        }

        return EnumToolType.NONE;
    }

    public static void recalculateStats(ItemStack stack, @Nullable EntityPlayer player) {
        GearData.recalculateStats(player, stack);
    }

    public static void addSoulPart(ItemStack stack) {
        PartDataList parts = GearData.getConstructionParts(stack);
        parts.removeIf(p -> p.getPart().getType() == ToolSoulPart.PART_TYPE);
        parts.add(ItemPartData.instance(SGearMaterials.soulPart));
        GearData.writeConstructionParts(stack, parts);
    }
}
