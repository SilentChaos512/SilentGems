/*
 * Silent's Gems -- SGearMaterials
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

package net.silentchaos512.gems.init;

import net.minecraft.util.ResourceLocation;
import net.silentchaos512.gear.api.parts.PartMain;
import net.silentchaos512.gear.api.parts.PartOrigins;
import net.silentchaos512.gear.api.parts.PartRegistry;
import net.silentchaos512.gems.SilentGems;
import net.silentchaos512.gems.lib.EnumGem;
import net.silentchaos512.gems.lib.soul.ToolSoulPart;

public final class SGearMaterials {
    public static ToolSoulPart soulPart;

    private SGearMaterials() {}

    public static void init() {
        for (EnumGem gem : EnumGem.values()) {
            String gemName = gem == EnumGem.BERYL ? "green_sapphire" : gem.getName();
            ResourceLocation name = new ResourceLocation(SilentGems.MODID, "main_" + gemName);
            PartRegistry.putPart(new PartMain(name, PartOrigins.BUILTIN_ADDON));
        }

        PartRegistry.putPart(new PartMain(new ResourceLocation(SilentGems.MODID, "main_chaos_iron"), PartOrigins.BUILTIN_ADDON));

        soulPart = PartRegistry.putPart(new ToolSoulPart(new ResourceLocation(SilentGems.MODID, "gear_soul"), PartOrigins.BUILTIN_ADDON));
    }
}
