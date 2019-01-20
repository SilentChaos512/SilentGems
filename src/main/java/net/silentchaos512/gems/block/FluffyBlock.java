/*
 * Silent's Gems -- FluffyBlock
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

package net.silentchaos512.gems.block;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemShears;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.PlayerEvent;

import java.util.EnumMap;
import java.util.Map;

public class FluffyBlock extends Block {
    private static final Map<EnumDyeColor, FluffyBlock> MAP = new EnumMap<>(EnumDyeColor.class);

    private final EnumDyeColor color;

    static {
        MinecraftForge.EVENT_BUS.addListener(FluffyBlock::onGetBreakSpeed);
    }

    private FluffyBlock(EnumDyeColor color) {
        super(Builder.create(Material.CLOTH)
                .hardnessAndResistance(0.8f, 3)
                .sound(SoundType.CLOTH));
        this.color = color;
    }

    public static FluffyBlock get(EnumDyeColor color) {
        if (!MAP.containsKey(color)) {
            FluffyBlock block = new FluffyBlock(color);
            MAP.put(color, block);
        }
        return MAP.get(color);
    }

    @Override
    public void onFallenUpon(World world, BlockPos pos, Entity entity, float distance) {
        if (distance < 2 || world.isRemote) return;

        // Count the number of fluffy blocks that are stacked up.
        int stackedBlocks = 0;
        while (world.getBlockState(pos).getBlock() == this) {
            pos = pos.down();
            ++stackedBlocks;
        }

        // Reduce fall distance by 10 blocks per stacked block
        distance -= Math.min(10 * stackedBlocks, distance);
        entity.fallDistance = 0f;
        entity.fall(distance, 1f);
    }

    private static void onGetBreakSpeed(PlayerEvent.BreakSpeed event) {
        ItemStack mainHand = event.getEntityPlayer().getHeldItem(EnumHand.MAIN_HAND);
        if (!mainHand.isEmpty() && mainHand.getItem() instanceof ItemShears) {
            int efficiency = EnchantmentHelper.getEfficiencyModifier(event.getEntityPlayer());

            float speed = event.getNewSpeed() * 4;
            if (efficiency > 0) {
                speed += (efficiency * efficiency + 1);
            }

            event.setNewSpeed(speed);
        }
    }
}
