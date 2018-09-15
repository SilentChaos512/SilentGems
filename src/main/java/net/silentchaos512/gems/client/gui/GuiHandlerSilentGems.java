package net.silentchaos512.gems.client.gui;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;
import net.silentchaos512.gems.SilentGems;

import javax.annotation.Nullable;

public class GuiHandlerSilentGems implements IGuiHandler {
    @Nullable
    @Override
    public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        BlockPos pos = new BlockPos(x, y, z);
        TileEntity tile = world.getTileEntity(pos);

        GuiTypes type = GuiTypes.byId(ID);
        if (type != null) {
            if (type.tileEntityMatches(tile)) {
                return type.getContainer(tile, player, x);
            } else {
                SilentGems.logHelper.warn(
                        "Tried to open GUI type {} (ID {}), but TileEntity {} at position {} does not match!",
                        type, ID, tile, pos);
            }
        } else {
            SilentGems.logHelper.warn("Tried to open GUI with unknown ID {} (position {})", ID, pos);
        }

        return null;
    }

    @Nullable
    @Override
    public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        BlockPos pos = new BlockPos(x, y, z);
        TileEntity tile = world.getTileEntity(pos);

        GuiTypes type = GuiTypes.byId(ID);
        if (type != null) {
            if (type.tileEntityMatches(tile)) {
                return type.getGuiScreen(tile, player, x);
            } else {
                SilentGems.logHelper.warn(
                        "Tried to open GUI type {} (ID {}), but TileEntity {} at position {} does not match!",
                        type, ID, tile, pos);
            }
        } else {
            SilentGems.logHelper.warn("Tried to open GUI with unknown ID {} (position {})", ID, pos);
        }

        return null;
    }
}
