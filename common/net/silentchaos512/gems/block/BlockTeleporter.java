package net.silentchaos512.gems.block;

import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;
import net.silentchaos512.gems.SilentGems;
import net.silentchaos512.gems.configuration.Config;
import net.silentchaos512.gems.core.util.DimensionalPosition;
import net.silentchaos512.gems.core.util.LocalizationHelper;
import net.silentchaos512.gems.core.util.LogHelper;
import net.silentchaos512.gems.core.util.NBTHelper;
import net.silentchaos512.gems.core.util.PlayerHelper;
import net.silentchaos512.gems.core.util.TeleportUtil;
import net.silentchaos512.gems.item.CraftingMaterial;
import net.silentchaos512.gems.item.ModItems;
import net.silentchaos512.gems.lib.EnumGem;
import net.silentchaos512.gems.lib.Names;
import net.silentchaos512.gems.lib.Strings;
import net.silentchaos512.gems.tile.TileTeleporter;

public class BlockTeleporter extends BlockSG implements ITileEntityProvider {

  public static final String DESTINATION_OBSTRUCTED = "DestinationObstructed";
  public static final String DESTINATION_NO_TELEPORTER = "DestinationNoTeleporter";
  public static final String LINK_END = "Link.End";
  public static final String LINK_FAIL = "Link.Fail";
  public static final String LINK_START = "Link.Start";
  public static final String NO_DESTINATION = "NoDestination";
  public static final String NOT_ENOUGH_XP = "NotEnoughXP";
  public static final String RETURN_HOME_BOUND = "ReturnHomeBound";

  protected boolean isAnchor = false;

  public BlockTeleporter() {

    super(Material.iron);
    setHardness(15.0f);
    setResistance(2000.0f);
    setStepSound(Block.soundTypeGlass);
    setHasSubtypes(true);
    setHasGemSubtypes(true);
    setUnlocalizedName(Names.TELEPORTER);
  }

  @Override
  public void addRecipes() {

    if (!Config.RECIPE_TELEPORTER_DISABLED) {
      ItemStack essencePlus = CraftingMaterial.getStack(Names.CHAOS_ESSENCE_PLUS);
      ItemStack anyTeleporter = new ItemStack(this, 1, OreDictionary.WILDCARD_VALUE);

      for (EnumGem gem : EnumGem.values()) {
        // Main recipe
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(this, 2, gem.id), "cec", " g ",
            "cec", 'c', essencePlus, 'e', Items.ender_pearl, 'g', gem.getBlockOreName()));
        // Recolor recipe
        GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(this, 1, gem.id), anyTeleporter,
            gem.getItemOreName()));
      }
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
    String str = LocalizationHelper.getOtherBlockKey(Names.TELEPORTER, RETURN_HOME_BOUND);
    PlayerHelper.addChatMessage(player, str);
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

    int sx = linker.stackTagCompound.getInteger("X");
    int sy = linker.stackTagCompound.getInteger("Y");
    int sz = linker.stackTagCompound.getInteger("Z");
    int sd = linker.stackTagCompound.getInteger("D");

    if (linker.stackTagCompound.getBoolean(Strings.TELEPORTER_LINKER_STATE)) {
      // Active state, link teleporters and set inactive.
      TileTeleporter t1, t2;
      t1 = (TileTeleporter) MinecraftServer.getServer().worldServerForDimension(sd)
          .getTileEntity(sx, sy, sz);
      t2 = (TileTeleporter) MinecraftServer.getServer().worldServerForDimension(player.dimension)
          .getTileEntity(x, y, z);

      // Safety check
      if (t1 == null) {
        // Teleporter 1 not found.
        String str = LocalizationHelper.getOtherBlockKey(Names.TELEPORTER, LINK_FAIL);
        PlayerHelper.addChatMessage(player, EnumChatFormatting.DARK_RED + str);
        LogHelper.warning("A teleporter link failed because teleporter 1 could not be found.");
        linker.stackTagCompound.setBoolean(Strings.TELEPORTER_LINKER_STATE, false);
        return false;
      } else if (t2 == null) {
        // Teleporter 2 not found.
        String str = LocalizationHelper.getOtherBlockKey(Names.TELEPORTER, LINK_FAIL);
        PlayerHelper.addChatMessage(player, EnumChatFormatting.DARK_RED + str);
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

      String str = LocalizationHelper.getOtherBlockKey(Names.TELEPORTER, LINK_END);
      PlayerHelper.addChatMessage(player, str);

      linker.stackTagCompound.setBoolean(Strings.TELEPORTER_LINKER_STATE, false);
    } else {
      // Inactive state, set active state and location data.
      linker.stackTagCompound.setBoolean(Strings.TELEPORTER_LINKER_STATE, true);
      NBTHelper.setXYZD(linker.stackTagCompound, x, y, z, player.dimension);
      String str = LocalizationHelper.getOtherBlockKey(Names.TELEPORTER, LINK_START);
      PlayerHelper.addChatMessage(player, str);
    }

    return true;
  }

  @Override
  public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side,
      float hitX, float hitY, float hitZ) {

    boolean playerHoldingLinker = PlayerHelper.isPlayerHoldingItem(player,
        ModItems.teleporterLinker);
    boolean playerHoldingReturnHome = PlayerHelper.isPlayerHoldingItem(player, ModItems.returnHome);

    if (world.isRemote) {
      if (playerHoldingLinker || playerHoldingReturnHome) {
        return true;
      }
      return !this.isAnchor;
    }

    // Link teleporters
    if (playerHoldingLinker) {
      return linkTeleporters(world, x, y, z, player);
    }

    // Link return home charm
    if (playerHoldingReturnHome) {
      return linkReturnHome(world, x, y, z, player);
    }

    // Is this an anchor? If so, don't continue.
    if (this.isAnchor) {
      return false;
    }

    TileTeleporter tile = (TileTeleporter) world.getTileEntity(x, y, z);
    if (tile != null) {
      DimensionalPosition destination = new DimensionalPosition(tile.destX, tile.destY, tile.destZ,
          tile.destD);
      DimensionalPosition source = new DimensionalPosition(x, y, z, player.dimension);

      // Safety checks:
      if (destination.y <= 0) {
        // Destination not sane.
        String str = LocalizationHelper.getOtherBlockKey(Names.TELEPORTER, NO_DESTINATION);
        PlayerHelper.addChatMessage(player, str);
        return true;
      }

      // Is this a "dumb" teleport and are they allowed if so?
      if (!isDestinationAllowedIfDumb(destination)) {
        String str = LocalizationHelper.getOtherBlockKey(Names.TELEPORTER,
            DESTINATION_NO_TELEPORTER);
        PlayerHelper.addChatMessage(player, str);
        return true;
      }

      // Destination not obstructed
      if (!isDestinationSafe(destination)) {
        // Destination not safe.
        String str = LocalizationHelper.getOtherBlockKey(Names.TELEPORTER, DESTINATION_OBSTRUCTED);
        PlayerHelper.addChatMessage(player, str);
        return true;
      }

      // XP Drain.
      if (!checkAndDrainXP(player, source, destination)) {
        return true;
      }

      // Teleport player
      teleporterEntityTo(player, destination);
      // Play sounds
      float soundPitch = SilentGems.instance.random.nextFloat();
      soundPitch = soundPitch * 0.3f + 0.7f;
      world.playSoundEffect(x + 0.5, y + 0.5, z + 0.5, "mob.endermen.portal", 1.0f, soundPitch);
      world.playSoundEffect(tile.destX + 0.5, tile.destY + 0.5, tile.destZ + 0.5,
          "mob.endermen.portal", 1.0f, soundPitch);
    } else {
      LogHelper.warning("Teleporter at " + LogHelper.coord(x, y, z) + " not found!");
    }

    return true;
  }

  protected boolean checkAndDrainXP(EntityPlayer player, DimensionalPosition source,
      DimensionalPosition destination) {

    // Not enough XP?
    int xpCost = getRequiredXP(player, source, destination);
    if (xpCost > player.experienceTotal) {
      String str = LocalizationHelper.getOtherBlockKey(Names.TELEPORTER, NOT_ENOUGH_XP);
      str = String.format(str, player.experienceTotal, xpCost);
      PlayerHelper.addChatMessage(player, str);
      return false;
    }

    // Drain XP
    PlayerHelper.removeExperience(player, xpCost);
    return true;
  }

  protected int getRequiredXP(EntityPlayer player, DimensionalPosition source,
      DimensionalPosition destination) {

    if (source.d != destination.d) {
      return Config.TELEPORTER_XP_CROSS_DIMENSION;
    }

    double distance = getDistanceBetweenTeleporters(source, destination);

    if (distance < Config.TELEPORTER_XP_FREE_RANGE) {
      return 0;
    } else {
      return (int) (Config.TELEPORTER_XP_PER_1K_BLOCKS * distance / 1000);
    }
  }

  /**
   * Calculates the distance between two points. Used to calculate XP cost of teleports.
   * 
   * @param source
   * @param destination
   * @return
   */
  public double getDistanceBetweenTeleporters(DimensionalPosition source,
      DimensionalPosition destination) {

    double x = source.x - destination.x;
    double z = source.z - destination.z;
    return Math.sqrt(x * x + z * z);
  }

  protected boolean isDestinationSafe(DimensionalPosition pos) {

    // Check for obstructions at player head level. This may need additional tuning...
    WorldServer server = MinecraftServer.getServer().worldServerForDimension(pos.d);
    return !server.isBlockNormalCubeDefault(pos.x, pos.y + 2, pos.z, true);
  }

  protected boolean isDestinationAllowedIfDumb(DimensionalPosition pos) {

    // Dumb teleports are when a teleport is performed without a teleporter at the destination.
    // By default this is allowed.
    if (Config.TELEPORTER_ALLOW_DUMB) {
      return true;
    }
    WorldServer server = MinecraftServer.getServer().worldServerForDimension(pos.d);
    return server.getBlock(pos.x, pos.y, pos.z) instanceof BlockTeleporter;
  }

  protected boolean teleporterEntityTo(Entity entity, DimensionalPosition pos) {

    // Teleport
    if (entity instanceof EntityPlayerMP) {
      return TeleportUtil.teleportPlayerTo((EntityPlayerMP) entity, pos.x, pos.y, pos.z, pos.d);
    } else {
      return TeleportUtil.teleportEntityTo(entity, pos.x, pos.y, pos.z, pos.d);
    }
  }
}
