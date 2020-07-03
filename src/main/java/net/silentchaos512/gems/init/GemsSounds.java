/*
 * Silent's Gems -- GemsSounds
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

import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraftforge.fml.RegistryObject;
import net.silentchaos512.gems.SilentGems;

import java.util.Locale;

public enum GemsSounds {
    SOUL_URN_LID(0.6f, 0, 1.1f, 0.05f),
    SOUL_URN_OPEN(0.6f, 0, 1.1f, 0.05f);

    @SuppressWarnings("NonFinalFieldInEnum") RegistryObject<SoundEvent> sound;
    final float volume;
    final float volumeVariation;
    final float pitch;
    final float pitchVariation;

    GemsSounds(float volume, float volumeVariation, float pitch, float pitchVariation) {
        this.volume = volume;
        this.volumeVariation = volumeVariation;
        this.pitch = pitch;
        this.pitchVariation = pitchVariation;
    }

    static void register() {
        for (GemsSounds sound : GemsSounds.values()) {
            sound.sound = Registration.SOUND_EVENTS.register(sound.getName(), () ->
                    new SoundEvent(SilentGems.getId(sound.getName())));
        }
    }

    public SoundEvent get() {
        return sound.get();
    }

    public String getName() {
        return name().toLowerCase(Locale.ROOT);
    }

    /**
     * Play the sound at the given position, using the sound's default volume and pitch (plus
     * variation).
     *
     * @param world The world
     * @param pos   The position
     */
    public void play(IWorld world, BlockPos pos) {
        float volume = this.volume + volumeVariation * (float) SilentGems.random.nextGaussian();
        float pitch = this.pitch + pitchVariation * (float) SilentGems.random.nextGaussian();
        play(world, pos, volume, pitch);
    }

    /**
     * Play the sound at the given position, with the given volume and pitch (caller is responsible
     * for variation, if desired).
     *
     * @param world  The world
     * @param pos    The position
     * @param volume The exact volume level
     * @param pitch  The exact pitch level
     */
    public void play(IWorld world, BlockPos pos, float volume, float pitch) {
        world.playSound(null, pos, this.get(), SoundCategory.PLAYERS, volume, pitch);
    }
}
