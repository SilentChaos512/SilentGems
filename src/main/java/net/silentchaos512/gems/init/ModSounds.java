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

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.silentchaos512.gems.SilentGems;
import net.silentchaos512.lib.registry.SRegistry;

import java.util.ArrayList;
import java.util.List;

public class ModSounds {
    private static final List<SoundEvent> ALL = new ArrayList<>();

    public static final SoundEvent SOUL_URN_LID = create("soul_urn_lid");
    public static final SoundEvent SOUL_URN_OPEN = create("soul_urn_open");

    public static void registerAll(SRegistry reg) {
        reg.registerSoundEvent(SOUL_URN_LID, "soul_urn_lid");
        reg.registerSoundEvent(SOUL_URN_OPEN, "soul_urn_open");
    }

    private static SoundEvent create(String soundId) {
        SoundEvent soundEvent = new SoundEvent(new ResourceLocation(SilentGems.MOD_ID, soundId));
        ALL.add(soundEvent);
        return soundEvent;
    }

    public static void playAllHotswapFix(EntityPlayer player) {
        // Hotswapping code before certain resources are used causes them to not load. In the case
        // of SoundEvents, this causes the game to freeze. Obviously not an issue outside of an
        // IDE, but playing all the sounds here should ensure I don't crash more than necessary...
        for (SoundEvent sound : ALL) {
            player.world.playSound(null, player.getPosition(), sound, SoundCategory.PLAYERS, 0.05f, 1f);
        }
    }
}
