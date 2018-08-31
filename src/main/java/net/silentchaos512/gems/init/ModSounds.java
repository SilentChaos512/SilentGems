/*
 * Silent's Gems -- ModSounds
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
import net.minecraft.util.SoundEvent;
import net.silentchaos512.gems.SilentGems;
import net.silentchaos512.lib.registry.SRegistry;

public class ModSounds {
    public static final SoundEvent SOUL_URN_LID = create("soul_urn_lid");
    public static final SoundEvent SOUL_URN_OPEN = create("soul_urn_open");

    public static void registerAll(SRegistry reg) {
        reg.registerSoundEvent(SOUL_URN_LID, "soul_urn_lid");
        reg.registerSoundEvent(SOUL_URN_OPEN, "soul_urn_open");
    }

    private static SoundEvent create(String soundId) {
        return new SoundEvent(new ResourceLocation(SilentGems.MODID, soundId));
    }
}
