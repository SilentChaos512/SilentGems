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

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.registries.ForgeRegistries;
import net.silentchaos512.gems.SilentGems;
import net.silentchaos512.utils.Lazy;

import java.util.Locale;

public enum GemsSounds {
    SOUL_URN_LID(0.6f, 0, 1.1f, 0.05f),
    SOUL_URN_OPEN(0.6f, 0, 1.1f, 0.05f);

    final Lazy<SoundEvent> sound;
    final float volume;
    final float volumeVariation;
    final float pitch;
    final float pitchVariation;

    GemsSounds(float volume, float volumeVariation, float pitch, float pitchVariation) {
        this.volume = volume;
        this.volumeVariation = volumeVariation;
        this.pitch = pitch;
        this.pitchVariation = pitchVariation;

        sound = Lazy.of(() -> {
            ResourceLocation id = new ResourceLocation(SilentGems.MOD_ID, getName());
            return new SoundEvent(id);
        });
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

    public static void registerAll(RegistryEvent.Register<SoundEvent> event) {
        for (GemsSounds sound : values()) {
            register(sound.getName(), sound.get());
        }
    }

    private static void register(String name, SoundEvent sound) {
        ResourceLocation id = new ResourceLocation(SilentGems.MOD_ID, name);
        sound.setRegistryName(id);
        ForgeRegistries.SOUND_EVENTS.register(sound);
    }

    public static void playAllHotswapFix(PlayerEntity player) {
        // Hotswapping code before certain resources are used causes them to not load. In the case
        // of SoundEvents, this causes the game to freeze. Obviously not an issue outside of an
        // IDE, but playing all the sounds here should ensure I don't crash more than necessary...
        for (GemsSounds sound : values()) {
            player.world.playSound(null, player.getPosition(), sound.get(), SoundCategory.PLAYERS, 0.05f, 1f);
        }
    }
}
