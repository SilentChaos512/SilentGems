package net.silentchaos512.gems.client.gui;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;
import net.silentchaos512.gems.SilentGems;
import net.silentchaos512.gems.block.urn.ContainerSoulUrn;
import net.silentchaos512.gems.block.urn.GuiSoulUrn;
import net.silentchaos512.gems.block.urn.TileSoulUrn;
import net.silentchaos512.gems.inventory.ContainerBurnerPylon;
import net.silentchaos512.gems.inventory.ContainerChaosAltar;
import net.silentchaos512.gems.inventory.ContainerMaterialGrader;
import net.silentchaos512.gems.inventory.ContainerQuiver;
import net.silentchaos512.gems.tile.TileChaosAltar;
import net.silentchaos512.gems.tile.TileChaosPylon;
import net.silentchaos512.gems.tile.TileMaterialGrader;

public class GuiHandlerSilentGems implements IGuiHandler {
    public enum GuiType {
        INVALID,
        ALTAR,
        BURNER_PYLON,
        MATERIAL_GRADER,
        QUIVER,
        SOUL_URN;

        public final int id = ordinal() - 1;

        static GuiType byId(int id) {
            for (GuiType type : values())
                if (type.id == id)
                    return type;
            return INVALID;
        }
    }

    @Override
    public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        BlockPos pos = new BlockPos(x, y, z);
        TileEntity tile = world.getTileEntity(pos);
        if (ID != GuiType.QUIVER.id && tile == null) {
            SilentGems.logHelper.warn("Missing TileEntity at {}!", pos);
            return null;
        }

        switch (GuiType.byId(ID)) {
            case ALTAR:
                if (tile instanceof TileChaosAltar) {
                    TileChaosAltar tileAltar = (TileChaosAltar) tile;
                    return new ContainerChaosAltar(player.inventory, tileAltar);
                }
                return null;
            case BURNER_PYLON:
                if (tile instanceof TileChaosPylon) {
                    return new ContainerBurnerPylon(player.inventory, (TileChaosPylon) tile);
                }
                return null;
            case MATERIAL_GRADER:
                if (tile instanceof TileMaterialGrader) {
                    return new ContainerMaterialGrader(player.inventory, (TileMaterialGrader) tile);
                }
                return null;
            case QUIVER:
                EnumHand hand = x == 1 ? EnumHand.OFF_HAND : EnumHand.MAIN_HAND;
                ItemStack stack = player.getHeldItem(hand);
                return new ContainerQuiver(stack, player.inventory, hand);
            case SOUL_URN:
                if (tile instanceof TileSoulUrn) {
                    return new ContainerSoulUrn(player.inventory, (TileSoulUrn) tile);
                }
            default:
                SilentGems.logHelper.error("No GUI with ID {}!", ID);
                return null;
        }
    }

    @Override
    public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        BlockPos pos = new BlockPos(x, y, z);
        TileEntity tile = world.getTileEntity(pos);
        if (ID != GuiType.QUIVER.id && tile == null) {
            SilentGems.logHelper.warn("Missing TileEntity at {}!", pos);
            return null;
        }

        switch (GuiType.byId(ID)) {
            case ALTAR:
                if (tile instanceof TileChaosAltar) {
                    TileChaosAltar tileAltar = (TileChaosAltar) tile;
                    return new GuiChaosAltar(player.inventory, tileAltar);
                }
                return null;
            case BURNER_PYLON:
                if (tile instanceof TileChaosPylon) {
                    return new GuiBurnerPylon(player.inventory, (TileChaosPylon) tile);
                }
                return null;
            case MATERIAL_GRADER:
                if (tile instanceof TileMaterialGrader) {
                    return new GuiMaterialGrader(player.inventory, (TileMaterialGrader) tile);
                }
                return null;
            case QUIVER:
                EnumHand hand = x == 1 ? EnumHand.OFF_HAND : EnumHand.MAIN_HAND;
                ItemStack stack = player.getHeldItem(hand);
                return new GuiQuiver(new ContainerQuiver(stack, player.inventory, hand));
            case SOUL_URN:
                if (tile instanceof TileSoulUrn) {
                    return new GuiSoulUrn(player.inventory, (TileSoulUrn) tile);
                }
                return null;
            default:
                SilentGems.logHelper.error("No GUI with ID {}!", ID);
                return null;
        }
    }
}
