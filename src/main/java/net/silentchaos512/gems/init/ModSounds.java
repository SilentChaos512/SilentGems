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
import net.minecraft.util.LazyLoadBase;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.IForgeRegistry;
import net.silentchaos512.gems.SilentGems;

import java.util.Locale;

public enum ModSounds {
    SOUL_URN_LID,
    SOUL_URN_OPEN;

    final LazyLoadBase<SoundEvent> sound;

    ModSounds() {
        sound = new LazyLoadBase<>(() -> create(name().toLowerCase(Locale.ROOT)));
    }

    public SoundEvent get() {
        return sound.getValue();
    }

    public static void registerAll(RegistryEvent.Register<SoundEvent> event) {
        if (event.getRegistry().getRegistrySuperType() != SoundEvent.class) return;

        IForgeRegistry<SoundEvent> reg = ForgeRegistries.SOUND_EVENTS;

        for (ModSounds sound : values()) {
            reg.register(sound.get());
        }
    }

    private static SoundEvent create(String soundId) {
        ResourceLocation name = new ResourceLocation(SilentGems.MOD_ID, soundId);
        SoundEvent soundEvent = new SoundEvent(name);
        soundEvent.setRegistryName(name);
        return soundEvent;
    }

    public static void playAllHotswapFix(EntityPlayer player) {
        // Hotswapping code before certain resources are used causes them to not load. In the case
        // of SoundEvents, this causes the game to freeze. Obviously not an issue outside of an
        // IDE, but playing all the sounds here should ensure I don't crash more than necessary...
        for (ModSounds sound : values()) {
            player.world.playSound(null, player.getPosition(), sound.get(), SoundCategory.PLAYERS, 0.05f, 1f);
        }
    }
}
