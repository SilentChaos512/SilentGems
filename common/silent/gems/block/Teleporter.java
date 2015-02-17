package silent.gems.block;

import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.OreDictionary;
import silent.gems.core.registry.SRegistry;
import silent.gems.core.util.LocalizationHelper;
import silent.gems.core.util.LogHelper;
import silent.gems.core.util.NBTHelper;
import silent.gems.core.util.PlayerHelper;
import silent.gems.item.ChaosEssence;
import silent.gems.item.ModItems;
import silent.gems.lib.EnumGem;
import silent.gems.lib.Names;
import silent.gems.lib.Strings;
import silent.gems.tile.TileTeleporter;

public class Teleporter extends BlockSG implements ITileEntityProvider {

  public static final PropertyEnum VARIANT = PropertyEnum.create("variant", EnumGem.class);

  public static final String DESTINATION_OBSTRUCTED = "DestinationObstructed";
  public static final String LINK_END = "Link.End";
  public static final String LINK_FAIL = "Link.Fail";
  public static final String LINK_START = "Link.Start";
  public static final String NO_DESTINATION = "NoDestination";
  public static final String RETURN_HOME_BOUND = "ReturnHomeBound";

  public Teleporter() {

    super(EnumGem.count(), Material.iron);
    setDefaultState(this.blockState.getBaseState().withProperty(VARIANT, EnumGem.RUBY));

    setHardness(12.0f);
    setResistance(150.0f);
    setStepSound(Block.soundTypeGlass);

    setHasSubtypes(true);
    setHasGemSubtypes(true);
    setUnlocalizedName(Names.TELEPORTER);
  }

  @Override
  public IBlockState getStateFromMeta(int meta) {

    return this.getDefaultState().withProperty(VARIANT, EnumGem.byId(meta));
  }

  @Override
  public int getMetaFromState(IBlockState state) {

    return ((EnumGem) state.getValue(VARIANT)).getId();
  }

  @Override
  protected BlockState createBlockState() {

    return new BlockState(this, new IProperty[] { VARIANT });
  }

  @Override
  public void addRecipes() {

    ItemStack chaosEssence = ChaosEssence.getByType(ChaosEssenceBlock.EnumType.REFINED);
    ItemStack anyTeleporter = new ItemStack(this, 1, OreDictionary.WILDCARD_VALUE);
    
    for (int i = 0; i < EnumGem.count(); ++i) {
      ItemStack teleporter = new ItemStack(this, 2, i);
      ItemStack gemBlock = new ItemStack(ModBlocks.gemBlock, 1, i);
      ItemStack gem = new ItemStack(ModItems.gem, 1, i);
      
      // Normal recipe
      GameRegistry.addShapedRecipe(teleporter, "cec", " g ", "cec", 'c', chaosEssence, 'e',
          Items.ender_pearl, 'g', gemBlock);
      // Recoloring recipe
      GameRegistry.addShapelessRecipe(teleporter, anyTeleporter, gem);
    }
  }

  @Override
  public TileEntity createNewTileEntity(World var1, int var2) {

    return new TileTeleporter();
  }

  private boolean linkReturnHome(World world, BlockPos pos, EntityPlayer player) {

    if (world.isRemote) {
      return true;
    }

    ItemStack charm = player.inventory.getCurrentItem();
    NBTTagCompound tags = charm.getTagCompound();

    if (tags == null) {
      tags = new NBTTagCompound();
    }
    int x = pos.getX();
    int y = pos.getY();
    int z = pos.getZ();
    NBTHelper.setXYZD(tags, x, y, z, player.dimension);
    PlayerHelper.addChatMessage(player,
        LocalizationHelper.getOtherBlockKey(blockName, RETURN_HOME_BOUND));
    charm.setTagCompound(tags);
    return true;
  }

  private boolean linkTeleporters(World world, BlockPos pos, EntityPlayer player) {

    if (world.isRemote) {
      return true;
    }

    ItemStack linker = player.inventory.getCurrentItem();
    NBTTagCompound tags = linker.getTagCompound();

    if (tags == null) {
      tags = new NBTTagCompound();
      tags.setBoolean(Strings.TELEPORTER_LINKER_STATE, false);
      NBTHelper.setXYZD(tags, 0, 0, 0, 0);
    }

    // Safety check
    if (!NBTHelper.hasValidXYZD(tags)) {
      tags.setBoolean(Strings.TELEPORTER_LINKER_STATE, false);
    }

    boolean state = tags.getBoolean(Strings.TELEPORTER_LINKER_STATE);
    int sx = tags.getInteger("X");
    int sy = tags.getInteger("Y");
    int sz = tags.getInteger("Z");
    int sd = tags.getInteger("D");
    int x = pos.getX();
    int y = pos.getY();
    int z = pos.getZ();

    if (tags.getBoolean(Strings.TELEPORTER_LINKER_STATE)) {
      // Active state, link teleporters and set inactive.
      TileTeleporter t1, t2;
      t1 = (TileTeleporter) MinecraftServer.getServer().worldServerForDimension(sd)
          .getTileEntity(new BlockPos(sx, sy, sz));
      t2 = (TileTeleporter) MinecraftServer.getServer().worldServerForDimension(player.dimension)
          .getTileEntity(new BlockPos(x, y, z));

      // Safety check
      if (t1 == null) {
        PlayerHelper
            .addChatMessage(
                player,
                EnumChatFormatting.DARK_RED
                    + LocalizationHelper.getOtherBlockKey(blockName, LINK_FAIL));
        LogHelper.warning("A teleporter link failed because teleporter 1 could not be found.");
        tags.setBoolean(Strings.TELEPORTER_LINKER_STATE, false);
        return false;
      } else if (t2 == null) {
        PlayerHelper
            .addChatMessage(
                player,
                EnumChatFormatting.DARK_RED
                    + LocalizationHelper.getOtherBlockKey(blockName, LINK_FAIL));
        LogHelper.warning("A teleporter link failed because teleporter 2 could not be found.");
        tags.setBoolean(Strings.TELEPORTER_LINKER_STATE, false);
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

      tags.setBoolean(Strings.TELEPORTER_LINKER_STATE, false);
    } else {
      // Inactive state, set active state and location data.
      tags.setBoolean(Strings.TELEPORTER_LINKER_STATE, true);
      NBTHelper.setXYZD(tags, x, y, z, player.dimension);
      PlayerHelper.addChatMessage(player,
          LocalizationHelper.getOtherBlockKey(blockName, LINK_START));
    }

    linker.setTagCompound(tags);
    return true;
  }

  @Override
  public boolean onBlockActivated(World world, BlockPos pos, IBlockState state,
      EntityPlayer player, EnumFacing side, float hitX, float hitY, float hitZ) {

    if (world.isRemote) {
      return true;
    }

    // Link teleporters
    if (PlayerHelper.isPlayerHoldingItem(player, SRegistry.getItem(Names.TELEPORTER_LINKER))) {
      return linkTeleporters(world, pos, player);
    }

    // Link return home charm
    if (PlayerHelper.isPlayerHoldingItem(player, SRegistry.getItem(Names.RETURN_HOME))) {
      return linkReturnHome(world, pos, player);
    }

    TileTeleporter tile = (TileTeleporter) world.getTileEntity(pos);

    if (tile != null) {
      // Safety checks:
      if (tile.destY <= 0) {
        PlayerHelper.addChatMessage(player,
            LocalizationHelper.getOtherBlockKey(blockName, NO_DESTINATION));
        return true;
      }

      // Destination not obstructed
      BlockPos destPos = new BlockPos(tile.destX, tile.destY + 2, tile.destZ);
      WorldServer server = MinecraftServer.getServer().worldServerForDimension(tile.destD);
      if (server.getBlockState(destPos).getBlock().isSolidFullCube()) {
        PlayerHelper.addChatMessage(player,
            LocalizationHelper.getOtherBlockKey(blockName, DESTINATION_OBSTRUCTED));
        return true;
      }

      // Teleport player if everything is OK.
      if (tile.destD != player.dimension) {
        player.travelToDimension(tile.destD);
      }
      player.setPositionAndUpdate(tile.destX + 0.5, tile.destY + 1, tile.destZ + 0.5);
      world.playSoundAtEntity(player, "mob.endermen.portal", 1.0f, 1.0f);
    } else {
      LogHelper.warning("Teleporter at " + LogHelper.coord(pos) + " not found!");
    }

    return true;
  }
}
