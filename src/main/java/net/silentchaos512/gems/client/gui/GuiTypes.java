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

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.IInteractionObject;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.network.FMLPlayMessages;
import net.minecraftforge.fml.network.NetworkHooks;
import net.silentchaos512.gems.SilentGems;
import net.silentchaos512.gems.block.altar.AltarContainer;
import net.silentchaos512.gems.block.altar.AltarGui;
import net.silentchaos512.gems.block.altar.AltarTileEntity;
import net.silentchaos512.gems.block.supercharger.ContainerSupercharger;
import net.silentchaos512.gems.block.supercharger.GuiSupercharger;
import net.silentchaos512.gems.block.supercharger.TileSupercharger;
import net.silentchaos512.gems.block.tokenenchanter.TokenEnchanterContainer;
import net.silentchaos512.gems.block.tokenenchanter.TokenEnchanterGui;
import net.silentchaos512.gems.block.tokenenchanter.TokenEnchanterTileEntity;
import net.silentchaos512.gems.block.urn.ContainerSoulUrn;
import net.silentchaos512.gems.block.urn.GuiSoulUrn;
import net.silentchaos512.gems.block.urn.TileSoulUrn;
import net.silentchaos512.lib.inventory.TileEntityContainerType;

import javax.annotation.Nullable;
import java.util.Locale;
import java.util.Objects;
import java.util.Optional;

public enum GuiTypes {
    SOUL_URN() {
        @Override
        public Container getContainer(TileEntityContainerType<?> tileType, EntityPlayer player) {
            TileSoulUrn tileEntity = (TileSoulUrn) tileType.getTileEntity(player);
            return new ContainerSoulUrn(player.inventory, Objects.requireNonNull(tileEntity));
        }

        @OnlyIn(Dist.CLIENT)
        @Override
        public GuiContainer getGui(TileEntityContainerType<?> tileType, EntityPlayer player) {
            TileSoulUrn tileEntity = (TileSoulUrn) tileType.getTileEntity(player);
            return new GuiSoulUrn(player.inventory, Objects.requireNonNull(tileEntity));
        }
    },
    SUPERCHARGER() {
        @Override
        public Container getContainer(TileEntityContainerType<?> tileType, EntityPlayer player) {
            TileSupercharger tileEntity = (TileSupercharger) tileType.getTileEntity(player);
            return new ContainerSupercharger(player.inventory, Objects.requireNonNull(tileEntity));
        }

        @OnlyIn(Dist.CLIENT)
        @Override
        public GuiContainer getGui(TileEntityContainerType<?> tileType, EntityPlayer player) {
            TileSupercharger tileEntity = (TileSupercharger) tileType.getTileEntity(player);
            return new GuiSupercharger(player.inventory, Objects.requireNonNull(tileEntity));
        }
    },
    TOKEN_ENCHANTER {
        @Override
        public Container getContainer(TileEntityContainerType<?> tileType, EntityPlayer player) {
            TokenEnchanterTileEntity tileEntity = (TokenEnchanterTileEntity) tileType.getTileEntity(player);
            return new TokenEnchanterContainer(player.inventory, Objects.requireNonNull(tileEntity));
        }

        @OnlyIn(Dist.CLIENT)
        @Override
        public GuiContainer getGui(TileEntityContainerType<?> tileType, EntityPlayer player) {
            TokenEnchanterTileEntity tileEntity = (TokenEnchanterTileEntity) tileType.getTileEntity(player);
            return new TokenEnchanterGui(player.inventory, Objects.requireNonNull(tileEntity));
        }
    },
    TRANSMUTATION_ALTAR() {
        @Override
        public Container getContainer(TileEntityContainerType<?> tileType, EntityPlayer player) {
            AltarTileEntity tileEntity = (AltarTileEntity) tileType.getTileEntity(player);
            return new AltarContainer(player.inventory, Objects.requireNonNull(tileEntity));
        }

        @OnlyIn(Dist.CLIENT)
        @Override
        public GuiContainer getGui(TileEntityContainerType<?> tileType, EntityPlayer player) {
            AltarTileEntity tileEntity = (AltarTileEntity) tileType.getTileEntity(player);
            return new AltarGui(player.inventory, Objects.requireNonNull(tileEntity));
        }
    };

    private final ResourceLocation guiId;

    GuiTypes() {
        this.guiId = new ResourceLocation(SilentGems.MOD_ID, name().toLowerCase(Locale.ROOT));
    }

    public ResourceLocation getId() {
        return guiId;
    }

    public <C extends Container> TileEntityContainerType<C> getContainerType() {
        return new TileEntityContainerType<>(guiId);
    }

    public TileEntityContainerType<?> getContainerType(BlockPos pos) {
        return new TileEntityContainerType<>(guiId, pos);
    }

    /**
     * Try to open a block's GUI for the player.
     * @param player The player
     * @param world The world
     * @param pos The tile entity position
     */
    public void display(EntityPlayer player, World world, BlockPos pos) {
        if (!(player instanceof EntityPlayerMP)) {
            SilentGems.LOGGER.error("Tried to send GUI packet from client?");
            return;
        }
        TileEntityContainerType<?> tileType = getContainerType(pos);
        IInteractionObject containerSupplier = new Interactable(this, pos);
        NetworkHooks.openGui((EntityPlayerMP) player, containerSupplier, tileType::toBytes);
    }

    public abstract Container getContainer(TileEntityContainerType<?> tileType, EntityPlayer player);

    @OnlyIn(Dist.CLIENT)
    public abstract GuiContainer getGui(TileEntityContainerType<?> tileType, EntityPlayer player);

    @OnlyIn(Dist.CLIENT)
    public static Optional<GuiTypes> from(FMLPlayMessages.OpenContainer msg) {
        for (GuiTypes type : values()) {
            if (type.getId().equals(msg.getId())) {
                return Optional.of(type);
            }
        }
        return Optional.empty();
    }

    static class Interactable implements IInteractionObject {
        private final GuiTypes type;
        private final BlockPos pos;

        Interactable(GuiTypes type, BlockPos pos) {
            this.type = type;
            this.pos = pos;
        }

        @Override
        public Container createContainer(InventoryPlayer playerInventory, EntityPlayer playerIn) {
            return type.getContainer(type.getContainerType(pos), playerIn);
        }

        @Override
        public String getGuiID() {
            return type.getId().toString();
        }

        @Override
        public ITextComponent getName() {
            ResourceLocation id = type.getId();
            return new TextComponentTranslation("container." + id.getNamespace() + "." + id.getPath());
        }

        @Override
        public boolean hasCustomName() {
            return false;
        }

        @Nullable
        @Override
        public ITextComponent getCustomName() {
            return null;
        }
    }
}
