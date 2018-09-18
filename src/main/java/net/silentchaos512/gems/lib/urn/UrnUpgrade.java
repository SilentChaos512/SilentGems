/*
 * Silent's Gems -- SoulUrnUpgradeBase
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

package net.silentchaos512.gems.lib.urn;

import lombok.Getter;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.silentchaos512.gems.SilentGems;
import net.silentchaos512.gems.block.urn.TileSoulUrn;
import net.silentchaos512.gems.item.SoulUrnUpgrades;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

/**
 * A soul urn upgrade. Instances of this are stored on the tile entity. To add a new upgrade, just
 * construct a new {@link Serializer}, extending the class if necessary.
 */
public class UrnUpgrade {
    private static final Map<ResourceLocation, Serializer<? extends UrnUpgrade>> SERIALIZERS = new HashMap<>();

    ResourceLocation id;

    public void tickTile(TileSoulUrn.SoulUrnState state, World world, BlockPos pos) {
    }

    public void tickItem(ItemStack urn, World world, EntityPlayer player, int itemSlot, boolean isSelected) {
    }

    public String getTranslationKey() {
        return "urn_upgrade." + this.id;
    }

    public static class Serializer<T extends UrnUpgrade> {
        @Getter
        private final ResourceLocation id;
        private final Supplier<T> constructor;

        public Serializer(ResourceLocation id, Supplier<T> constructor) {
            this.id = id;
            this.constructor = constructor;

            SERIALIZERS.put(id, this);
        }

        public NBTTagCompound serialize() {
            return new NBTTagCompound();
        }

        public T deserialize(NBTTagCompound nbt) {
            T result = constructor.get();
            result.id = this.id;
            return result;
        }
    }

    public static final class ListHelper {
        private ListHelper() {
        }

        public static NonNullList<UrnUpgrade> load(ItemStack urn) {
            return load(urn.getOrCreateSubCompound(UrnConst.NBT_ROOT));
        }

        public static NonNullList<UrnUpgrade> load(NBTTagCompound tagCompound) {
            NonNullList<UrnUpgrade> result = NonNullList.create();

            NBTTagList tagList = tagCompound.getTagList(UrnConst.NBT_UPGRADES, 10);
            for (NBTBase nbt : tagList) {
                if (nbt instanceof NBTTagCompound) {
                    NBTTagCompound tags = (NBTTagCompound) nbt;
                    ResourceLocation id = new ResourceLocation(tags.getString(UrnConst.NBT_UPGRADE_ID));
                    Serializer<? extends UrnUpgrade> serializer = SERIALIZERS.get(id);

                    if (serializer != null) {
                        result.add(serializer.deserialize(tags));
                    } else {
                        SilentGems.logHelper.error("Serializer for urn upgrade {} not found! Data will be lost.", id);
                    }
                }
            }

            return result;
        }

        public static void save(List<UrnUpgrade> upgrades, NBTTagCompound tagCompound) {
            NBTTagList tagList = new NBTTagList();

            for (UrnUpgrade upgrade : upgrades) {
                Serializer<? extends UrnUpgrade> serializer = SERIALIZERS.get(upgrade.id);
                if (serializer != null) {
                    NBTTagCompound tags = serializer.serialize();
                    tags.setString(UrnConst.NBT_UPGRADE_ID, upgrade.id.toString());
                    tagList.appendTag(tags);
                } else {
                    SilentGems.logHelper.error("Serializer for urn upgrade {} not found! Data will be lost.", upgrade.id);
                }
            }

            tagCompound.setTag(UrnConst.NBT_UPGRADES, tagList);
        }

        public static boolean contains(List<UrnUpgrade> upgrades, Serializer<? extends UrnUpgrade> serializer) {
            for (UrnUpgrade upgrade : upgrades)
                if (upgrade.id.equals(serializer.id))
                    return true;
            return false;
        }

        public static boolean contains(List<UrnUpgrade> upgrades, SoulUrnUpgrades upgrade) {
            return contains(upgrades, upgrade.getSerializer());
        }
    }
}
