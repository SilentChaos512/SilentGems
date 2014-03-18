package silent.gems.block;

import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.world.World;
import net.minecraftforge.oredict.OreDictionary;
import silent.gems.core.registry.SRegistry;
import silent.gems.core.util.LogHelper;
import silent.gems.core.util.NBTHelper;
import silent.gems.core.util.PlayerHelper;
import silent.gems.item.CraftingMaterial;
import silent.gems.lib.EnumGem;
import silent.gems.lib.Names;
import silent.gems.lib.Strings;
import silent.gems.tileentity.TileEntityTeleporter;

public class Teleporter extends BlockSG implements ITileEntityProvider {

    public Teleporter(int id) {

        super(id, Material.iron);

        setHardness(12.0f);
        setResistance(12.0f);
        setStepSound(Block.soundGlassFootstep);
        setCreativeTab(CreativeTabs.tabBlock);
        setHasSubtypes(true);
        setHasGemSubtypes(true);
        setUnlocalizedName(Names.TELEPORTER);
    }

    @Override
    public TileEntity createNewTileEntity(World world) {

        return new TileEntityTeleporter();
    }

    @Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int par6, float par7, float par8, float par9) {

        // Link teleporters
        if (PlayerHelper.isPlayerHoldingItem(player, SRegistry.getItem(Names.TELEPORTER_LINKER))) {
            return linkTeleporters(world, x, y, z, player);
        }
        // Link teleport sigils
        // TODO

        TileEntityTeleporter tile = (TileEntityTeleporter) world.getBlockTileEntity(x, y, z);

        if (tile != null) {
            // Safety checks:
            // Destination above y=0?
            if (tile.destY <= 0) {
                PlayerHelper.addChatMessage(player, Strings.TELEPORTER_NO_DESTINATION, true);
                return true;
            }
            // Destination not obstructed?
            LogHelper.list(tile.destX, tile.destY, tile.destZ, tile.destD);
            if (MinecraftServer.getServer().worldServerForDimension(tile.destD).isBlockOpaqueCube(tile.destX, tile.destY + 2, tile.destZ)) {
                PlayerHelper.addChatMessage(player, Strings.TELEPORTER_DESTINATION_OBSTRUCTED, true);
                return true;
            }

            // Spawn some particles. Apparently this has to be on client-side.
            // I broke this again :(
            if (world.isRemote) {
                for (int l = 0; l < 128; ++l) {
                    double d0 = x - 0.5 + world.rand.nextDouble() + 0.5;
                    double d1 = y + 0.5 + world.rand.nextDouble() * 2;
                    double d2 = z - 0.5 + world.rand.nextDouble() + 0.5;
                    double d3 = 0;
                    double d4 = 0;
                    double d5 = 0;
                    player.worldObj.spawnParticle("portal", d0, d1, d2, d3, d4, d5);
                }
            }

            // Teleport player if everything is OK.
            if (tile.destD != player.dimension) {
                player.travelToDimension(tile.destD);
            }
            player.setPositionAndUpdate(tile.destX + 0.5D, tile.destY + 1, tile.destZ + 0.5D);
            player.worldObj.playSoundAtEntity(player, "mob.endermen.portal", 1.0f, 1.0f);
        }
        else {
            LogHelper.warning("Teleporter at " + LogHelper.coord(x, y, z) + " not found!");
        }

        return true;
    }

    private boolean linkTeleporters(World world, int x, int y, int z, EntityPlayer player) {

        ItemStack linker = player.inventory.getCurrentItem();

        if (linker.stackTagCompound == null) {
            linker.stackTagCompound = new NBTTagCompound();
            linker.stackTagCompound.setBoolean(Strings.TELEPORTER_LINKER_STATE, false);
            NBTHelper.setXYZD(linker.stackTagCompound, 0, 0, 0, 0);
        }

        // Safety check
        if (!NBTHelper.hasValidXYZD(linker.stackTagCompound)) {
            linker.stackTagCompound.setBoolean(Strings.TELEPORTER_LINKER_STATE, false);
        }

        boolean state = linker.stackTagCompound.getBoolean(Strings.TELEPORTER_LINKER_STATE);
        int sx = linker.stackTagCompound.getInteger("X");
        int sy = linker.stackTagCompound.getInteger("Y");
        int sz = linker.stackTagCompound.getInteger("Z");
        int sd = linker.stackTagCompound.getInteger("D");

        if (linker.stackTagCompound.getBoolean(Strings.TELEPORTER_LINKER_STATE)) {
            // Active state, link teleporters and set inactive.
            TileEntityTeleporter t1, t2;
            t1 = (TileEntityTeleporter) world.getBlockTileEntity(sx, sy, sz);
            t2 = (TileEntityTeleporter) world.getBlockTileEntity(x, y, z);

            // Safety check
            if (t1 == null) {
                PlayerHelper.addChatMessage(player, Strings.TELEPORTER_LINK_FAIL, EnumChatFormatting.DARK_RED, true);
                LogHelper.warning("A teleporter link failed because teleporter 1 could not be found.");
                linker.stackTagCompound.setBoolean(Strings.TELEPORTER_LINKER_STATE, false);
                return false;
            }
            else if (t2 == null) {
                PlayerHelper.addChatMessage(player, Strings.TELEPORTER_LINK_FAIL, EnumChatFormatting.DARK_RED, true);
                LogHelper.warning("A teleporter link failed because teleporter 2 could not be found.");
                linker.stackTagCompound.setBoolean(Strings.TELEPORTER_LINKER_STATE, false);
                return false;
            }

            // Set destinations for both teleporters.
            t1.destX = x;
            t1.destY = y;
            t1.destZ = z;
            t1.destD = player.dimension;
            t2.destX = sx;
            t2.destY = sy;
            t2.destZ = sz;
            t2.destD = sd;

            PlayerHelper.addChatMessage(player, Strings.TELEPORTER_LINK_END, true);

            linker.stackTagCompound.setBoolean(Strings.TELEPORTER_LINKER_STATE, false);
        }
        else {
            // Inactive state, set active state and location data.
            linker.stackTagCompound.setBoolean(Strings.TELEPORTER_LINKER_STATE, true);
            NBTHelper.setXYZD(linker.stackTagCompound, x, y, z, player.dimension);
            PlayerHelper.addChatMessage(player, Strings.TELEPORTER_LINK_START, true);

            // TODO Force chunk load?
            // I'm not doing this now because I'm afraid I don't fully understand
            // the implications of forcing chunks to stay loaded, although it never
            // caused any problems before.
        }

        return true;
    }

    @Override
    public void addRecipes() {

        for (int i = 0; i < EnumGem.all().length; ++i) {
            GameRegistry.addShapedRecipe(new ItemStack(this, 2, i), "cec", " g ", "cec", 'c',
                    CraftingMaterial.getStack(Names.CHAOS_ESSENCE_PLUS), 'e', Item.enderPearl, 'g', EnumGem.all()[i].getBlock());
            GameRegistry.addShapelessRecipe(new ItemStack(this, 1, i), new ItemStack(this, 1, OreDictionary.WILDCARD_VALUE),
                    EnumGem.all()[i].getItem());
        }
    }
}
