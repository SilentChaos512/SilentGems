package silent.gems.block;

import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.world.World;
import net.minecraftforge.oredict.OreDictionary;
import silent.gems.core.registry.SRegistry;
import silent.gems.core.util.LocalizationHelper;
import silent.gems.core.util.LogHelper;
import silent.gems.core.util.NBTHelper;
import silent.gems.core.util.PlayerHelper;
import silent.gems.item.CraftingMaterial;
import silent.gems.lib.EnumGem;
import silent.gems.lib.Names;
import silent.gems.lib.Strings;
import silent.gems.tile.TileTeleporter;
import cpw.mods.fml.common.registry.GameRegistry;


public class Teleporter extends BlockSG implements ITileEntityProvider {

    public final static String DESTINATION_OBSTRUCTED = "DestinationObstructed";
    public final static String LINK_END = "Link.End";
    public final static String LINK_FAIL = "Link.Fail";
    public final static String LINK_START = "Link.Start";
    public final static String NO_DESTINATION = "NoDestination";
    public final static String RETURN_HOME_BOUND = "ReturnHomeBound";

    public Teleporter() {
        
        super(Material.iron);
        setHardness(12.0f);
        setResistance(12.0f);
        setStepSound(Block.soundTypeGlass);
        setCreativeTab(CreativeTabs.tabBlock);
        setHasSubtypes(true);
        setHasGemSubtypes(true);
        setUnlocalizedName(Names.TELEPORTER);
    }
    
    @Override
    public void addRecipes() {

        for (int i = 0; i < EnumGem.all().length; ++i) {
            GameRegistry.addShapedRecipe(new ItemStack(this, 2, i), "cec", " g ", "cec", 'c',
                    CraftingMaterial.getStack(Names.CHAOS_ESSENCE_PLUS), 'e', Items.ender_pearl, 'g', EnumGem.all()[i].getBlock());
            GameRegistry.addShapelessRecipe(new ItemStack(this, 1, i), new ItemStack(this, 1, OreDictionary.WILDCARD_VALUE),
                    EnumGem.all()[i].getItem());
        }
    }
    
    @Override
    public TileEntity createNewTileEntity(World var1, int var2) {

        return new TileTeleporter();
    }
    
    private boolean linkReturnHome(World world, int x, int y, int z, EntityPlayer player) {
        
        if (world.isRemote) {
            return true;
        }
        
        ItemStack charm = player.inventory.getCurrentItem();
        if (charm.stackTagCompound == null) {
            charm.stackTagCompound = new NBTTagCompound();
        }
        NBTHelper.setXYZD(charm.stackTagCompound, x, y, z, player.dimension);
        PlayerHelper.addChatMessage(player, LocalizationHelper.getOtherBlockKey(blockName, RETURN_HOME_BOUND));
        return true;
    }
    
    private boolean linkTeleporters(World world, int x, int y, int z, EntityPlayer player) {

        if (world.isRemote) {
            return true;
        }
        
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
            TileTeleporter t1, t2;
            t1 = (TileTeleporter) MinecraftServer.getServer().worldServerForDimension(sd).getTileEntity(sx, sy, sz);
            t2 = (TileTeleporter) MinecraftServer.getServer().worldServerForDimension(player.dimension).getTileEntity(x, y, z);

            // Safety check
            if (t1 == null) {
                PlayerHelper.addChatMessage(player, EnumChatFormatting.DARK_RED + LocalizationHelper.getOtherBlockKey(blockName, LINK_FAIL));
                LogHelper.warning("A teleporter link failed because teleporter 1 could not be found.");
                linker.stackTagCompound.setBoolean(Strings.TELEPORTER_LINKER_STATE, false);
                return false;
            }
            else if (t2 == null) {
                PlayerHelper.addChatMessage(player, EnumChatFormatting.DARK_RED + LocalizationHelper.getOtherBlockKey(blockName, LINK_FAIL));
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

            PlayerHelper.addChatMessage(player, LocalizationHelper.getOtherBlockKey(blockName, LINK_END));

            linker.stackTagCompound.setBoolean(Strings.TELEPORTER_LINKER_STATE, false);
        }
        else {
            // Inactive state, set active state and location data.
            linker.stackTagCompound.setBoolean(Strings.TELEPORTER_LINKER_STATE, true);
            NBTHelper.setXYZD(linker.stackTagCompound, x, y, z, player.dimension);
            PlayerHelper.addChatMessage(player, LocalizationHelper.getOtherBlockKey(blockName, LINK_START));

            // TODO Force chunk load?
            // I'm not doing this now because I'm afraid I don't fully understand
            // the implications of forcing chunks to stay loaded, although it never
            // caused any problems before.
        }

        return true;
    }
    
    @Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float hitX, float hitY, float hitZ) {
        
        if (world.isRemote) {
            return true;
        }
        
        // Link teleporters
        if (PlayerHelper.isPlayerHoldingItem(player, SRegistry.getItem(Names.TELEPORTER_LINKER))) {
            return linkTeleporters(world, x, y, z, player);
        }
        
        // Link return home charm
        if (PlayerHelper.isPlayerHoldingItem(player, SRegistry.getItem(Names.RETURN_HOME))) {
            return linkReturnHome(world, x, y, z, player);
        }
        
        TileTeleporter tile = (TileTeleporter) world.getTileEntity(x, y, z);
        
        if (tile != null) {
            // Safety checks:
            if (tile.destY <= 0) {
                PlayerHelper.addChatMessage(player, LocalizationHelper.getOtherBlockKey(blockName, NO_DESTINATION));
                return true;
            }
            
            // Destination not obstructed
            if (MinecraftServer.getServer().worldServerForDimension(tile.destD).isBlockNormalCubeDefault(tile.destX, tile.destY + 2, tile.destZ, true)) {
                PlayerHelper.addChatMessage(player, LocalizationHelper.getOtherBlockKey(blockName, DESTINATION_OBSTRUCTED));
                return true;
            }
            
            // Teleport player if everything is OK.
            if (tile.destD != player.dimension) {
                player.travelToDimension(tile.destD);
            }
            player.setPositionAndUpdate(tile.destX + 0.5, tile.destY + 1, tile.destZ + 0.5);
            player.worldObj.playSoundAtEntity(player, "mob.enderman.portal", 1.0f, 1.0f); // TODO: Doesn't seem to work anymore...
        }
        else {
            LogHelper.warning("Teleporter at " + LogHelper.coord(x, y, z) + " not found!");
        }
        
        return true;
    }
}
