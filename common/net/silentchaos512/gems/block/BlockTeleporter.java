package net.silentchaos512.gems.block;

import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
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
  public static final String LINK_END = "Link.End";
  public static final String LINK_FAIL = "Link.Fail";
  public static final String LINK_START = "Link.Start";
  public static final String NO_DESTINATION = "NoDestination";
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

    ItemStack essencePlus = CraftingMaterial.getStack(Names.CHAOS_ESSENCE_PLUS);
    ItemStack anyTeleporter = new ItemStack(this, 1, OreDictionary.WILDCARD_VALUE);
    for (EnumGem gem : EnumGem.values()) {
      GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(this, 2, gem.id), "cec", " g ",
          "cec", 'c', essencePlus, 'e', Items.ender_pearl, 'g', gem.getBlockOreName()));
      GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(this, 1, gem.id), anyTeleporter,
          gem.getItemOreName()));
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
      // Safety checks:
      if (tile.destY <= 0) {
        // Destination not sane.
        String str = LocalizationHelper.getOtherBlockKey(Names.TELEPORTER, NO_DESTINATION);
        PlayerHelper.addChatMessage(player, str);
        return true;
      }

      // Destination not obstructed
      if (!this.isDestinationSafe(tile.destX, tile.destY, tile.destZ, tile.destD)) {
        // Destination not safe.
        String str = LocalizationHelper.getOtherBlockKey(Names.TELEPORTER, DESTINATION_OBSTRUCTED);
        PlayerHelper.addChatMessage(player, str);
        return true;
      }

      this.teleporterEntityTo(player, tile.destX, tile.destY, tile.destZ, tile.destD);
    } else {
      LogHelper.warning("Teleporter at " + LogHelper.coord(x, y, z) + " not found!");
    }

    return true;
  }

  protected boolean isDestinationSafe(int x, int y, int z, int dimension) {

    // Check for obstructions at player head level. This may need additional tuning...
    WorldServer server = MinecraftServer.getServer().worldServerForDimension(dimension);
    return !server.isBlockNormalCubeDefault(x, y + 2, z, true);
  }

  protected void teleporterEntityTo(Entity entity, int x, int y, int z, int dimension) {

    // Change dimensions?
//    if (dimension != entity.dimension) {
//      if (entity instanceof EntityPlayerMP) {
//        EntityPlayerMP playerMP = (EntityPlayerMP) entity;
//        playerMP.mcServer.getConfigurationManager().transferPlayerToDimension(playerMP, dimension);
//      } else {
//        entity.travelToDimension(dimension); // FIXME: Does this also spawn Nether portals?
//      }
//    }

    // Calculate sound pitch.
    float soundPitch = SilentGems.instance.random.nextFloat();
    soundPitch = soundPitch * 0.3f + 0.7f;

    // Sound at source
    entity.worldObj.playSoundAtEntity(entity, "mob.endermen.portal", 1.0f, soundPitch);

    // Teleport
    if (entity instanceof EntityPlayerMP) {
      TeleportUtil.teleportPlayerTo((EntityPlayerMP) entity, x, y, z, dimension);
    } else {
      TeleportUtil.teleportEntityTo(entity, x, y, z, dimension);
    }
//    if (entity instanceof EntityPlayerMP) {
//      TeleportUtil.teleportPlayerTo((EntityPlayerMP) entity, x, y, z, dimension);
//    } else if (entity instanceof EntityLivingBase) {
//      // TODO: Is this necessary?
//      EntityLivingBase entityLivingBase = (EntityLivingBase) entity;
//      entityLivingBase.setPositionAndUpdate(x + 0.5, y + 1.0, z + 0.5);
//    } else {
//      entity.setPosition(x + 0.5, y + 1.0, z + 0.5);
//    }

    // Sound at destination
    entity.worldObj.playSoundAtEntity(entity, "mob.endermen.portal", 1.0f, soundPitch);
  }
}
