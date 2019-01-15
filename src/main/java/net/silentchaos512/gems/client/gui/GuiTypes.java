/*
 * Silent's Gems -- GuiTypes
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

package net.silentchaos512.gems.client.gui;

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.silentchaos512.gems.SilentGems;
import net.silentchaos512.gems.block.supercharger.ContainerSupercharger;
import net.silentchaos512.gems.block.supercharger.GuiSupercharger;
import net.silentchaos512.gems.block.supercharger.TileSupercharger;
import net.silentchaos512.gems.block.urn.ContainerSoulUrn;
import net.silentchaos512.gems.block.urn.GuiSoulUrn;
import net.silentchaos512.gems.block.urn.TileSoulUrn;
import net.silentchaos512.gems.inventory.ContainerBurnerPylon;
import net.silentchaos512.gems.inventory.ContainerChaosAltar;
import net.silentchaos512.gems.inventory.ContainerQuiver;
import net.silentchaos512.gems.tile.TileChaosAltar;
import net.silentchaos512.gems.tile.TileChaosPylon;

import javax.annotation.Nullable;

public enum GuiTypes {
    ALTAR(0, TileChaosAltar.class) {
        @Override
        Container getContainer(TileEntity tile, EntityPlayer player, int subtype) {
            return new ContainerChaosAltar(player.inventory, (TileChaosAltar) tile);
        }

        @SideOnly(Side.CLIENT)
        @Override
        GuiScreen getGuiScreen(TileEntity tile, EntityPlayer player, int subtype) {
            return new GuiChaosAltar(player.inventory, (TileChaosAltar) tile);
        }
    },
    BURNER_PYLON(1, TileChaosPylon.class) {
        @Override
        Container getContainer(TileEntity tile, EntityPlayer player, int subtype) {
            return new ContainerBurnerPylon(player.inventory, (TileChaosPylon) tile);
        }

        @SideOnly(Side.CLIENT)
        @Override
        GuiScreen getGuiScreen(TileEntity tile, EntityPlayer player, int subtype) {
            return new GuiBurnerPylon(player.inventory, (TileChaosPylon) tile);
        }
    },
    MATERIAL_GRADER(2, TileMaterialGrader.class) {
        @Override
        Container getContainer(TileEntity tile, EntityPlayer player, int subtype) {
            return new ContainerMaterialGrader(player.inventory, (TileMaterialGrader) tile);
        }

        @SideOnly(Side.CLIENT)
        @Override
        GuiScreen getGuiScreen(TileEntity tile, EntityPlayer player, int subtype) {
            return new GuiMaterialGrader(player.inventory, (TileMaterialGrader) tile);
        }
    },
    QUIVER(3, null) {
        @Override
        Container getContainer(TileEntity tile, EntityPlayer player, int subtype) {
            EnumHand hand = subtype == 1 ? EnumHand.OFF_HAND : EnumHand.MAIN_HAND;
            ItemStack stack = player.getHeldItem(hand);
            return new ContainerQuiver(stack, player.inventory, hand);
        }

        @SideOnly(Side.CLIENT)
        @Override
        GuiScreen getGuiScreen(TileEntity tile, EntityPlayer player, int subtype) {
            return new GuiQuiver((ContainerQuiver) getContainer(tile, player, subtype));
        }
    },
    SOUL_URN(4, TileSoulUrn.class) {
        @Override
        Container getContainer(TileEntity tile, EntityPlayer player, int subtype) {
            return new ContainerSoulUrn(player.inventory, (TileSoulUrn) tile);
        }

        @SideOnly(Side.CLIENT)
        @Override
        GuiScreen getGuiScreen(TileEntity tile, EntityPlayer player, int subtype) {
            return new GuiSoulUrn(player.inventory, (TileSoulUrn) tile);
        }
    },
    SUPERCHARGER(5, TileSupercharger.class) {
        @Override
        Container getContainer(TileEntity tile, EntityPlayer player, int subtype) {
            return new ContainerSupercharger(player.inventory, (TileSupercharger) tile);
        }

        @SideOnly(Side.CLIENT)
        @Override
        GuiScreen getGuiScreen(TileEntity tile, EntityPlayer player, int subtype) {
            return new GuiSupercharger(player.inventory, (TileSupercharger) tile);
        }
    };

    private final int id;
    @Nullable
    private final Class<? extends TileEntity> tileClass;

    GuiTypes(int id, @Nullable Class<? extends TileEntity> tileClass) {
        this.id = id;
        this.tileClass = tileClass;
    }

    /**
     * Try to open a block's GUI for the player.
     * @param player The player
     * @param world The world
     * @param pos The tile entity position
     */
    public void open(EntityPlayer player, World world, BlockPos pos) {
        player.openGui(SilentGems.instance, this.id, world, pos.getX(), pos.getY(), pos.getZ());
    }

    /**
     * Try to open a non-block GUI for the player.
     * @param player The player
     * @param world The world
     * @param subtype Subtype parameter, interpreted by the GuiTypes
     */
    public void open(EntityPlayer player, World world, int subtype) {
        player.openGui(SilentGems.instance, this.id, world, subtype, 0, 0);
    }

    abstract Container getContainer(TileEntity tile, EntityPlayer player, int subtype);

    @SideOnly(Side.CLIENT)
    abstract GuiScreen getGuiScreen(TileEntity tile, EntityPlayer player, int subtype);

    boolean tileEntityMatches(@Nullable TileEntity tile) {
        return this.tileClass == null || this.tileClass.isInstance(tile);
    }

    @Nullable
    static GuiTypes byId(int id) {
        for (GuiTypes type : values())
            if (type.id == id)
                return type;
        return null;
    }
}
