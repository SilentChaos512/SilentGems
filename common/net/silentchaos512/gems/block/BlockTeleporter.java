package net.silentchaos512.gems.block;

import java.util.List;

import com.google.common.collect.Lists;

import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;
import net.silentchaos512.gems.SilentGems;
import net.silentchaos512.gems.config.Config;
import net.silentchaos512.gems.item.ModItems;
import net.silentchaos512.gems.lib.EnumGem;
import net.silentchaos512.gems.lib.Names;
import net.silentchaos512.gems.tile.TileTeleporter;
import net.silentchaos512.lib.util.DimensionalPosition;
import net.silentchaos512.lib.util.LocalizationHelper;
import net.silentchaos512.lib.util.PlayerHelper;
import net.silentchaos512.wit.api.IWitHudInfo;

public class BlockTeleporter extends BlockGemSubtypes implements ITileEntityProvider, IWitHudInfo {

  public final boolean isAnchor;

  public BlockTeleporter(boolean isDark, boolean isAnchor) {

    this(isDark, isAnchor, Names.TELEPORTER);
  }

  public BlockTeleporter(boolean isDark, boolean isAnchor, String name) {

    super(isAnchor ? 1 : 16, isDark, name + (isDark ? "Dark" : ""), Material.iron);
    this.isAnchor = isAnchor;

    setHardness(15.0f);
    setResistance(2000.0f);
    setStepSound(SoundType.METAL);
    setHarvestLevel("pickaxe", 1);
  }

  @Override
  public void addRecipes() {

    if (Config.RECIPE_TELEPORTER_DISABLE) {
      return;
    }

    ItemStack[] anyTeleporter = new ItemStack[] {
        new ItemStack(ModBlocks.gemTeleporter, 1, OreDictionary.WILDCARD_VALUE),
        new ItemStack(ModBlocks.gemTeleporterDark, 1, OreDictionary.WILDCARD_VALUE) };

    for (int i = 0; i < subBlockCount; ++i) {
      EnumGem gem = getGem(i);
      GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(this, 2, i), "cec", " g ", "cec",
          'c', ModItems.craftingMaterial.chaosEssenceEnriched, 'e',
          ModItems.craftingMaterial.enderEssence, 'g', gem.getBlockOreName()));
      for (ItemStack stack : anyTeleporter) {
        GameRegistry.addRecipe(
            new ShapelessOreRecipe(new ItemStack(this, 1, i), stack, gem.getItemOreName()));
      }
    }
  }

  @Override
  public List<String> getWitLines(IBlockState state, BlockPos pos, EntityPlayer player,
      boolean advanced) {

    TileEntity tile = player.worldObj.getTileEntity(pos);
    if (tile == null || !(tile instanceof TileTeleporter)) {
      return null;
    }

    TileTeleporter teleporter = (TileTeleporter) tile;
    DimensionalPosition destination = teleporter.getDestination();
    return Lists.newArrayList(destination != null ? destination.toString() : "null");
  }

  @Override
  public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player,
      EnumHand hand, ItemStack heldItem, EnumFacing side, float hitX, float hitY, float hitZ) {

    boolean holdingLinker = heldItem != null && heldItem.getItem() == ModItems.teleporterLinker;
    boolean holdingReturnHome = false; // TODO

    if (world.isRemote) {
      return holdingLinker || holdingReturnHome ? true : !isAnchor;
    }

    TileTeleporter tile = (TileTeleporter) world.getTileEntity(pos);
    if (tile == null) {
      SilentGems.instance.logHelper.warning("Teleporter tile at " + pos + " not found!");
      return false;
    }

    // Link teleporters with linker.
    if (holdingLinker) {
      return tile.linkTeleporters(player, world, pos, heldItem, hand);
    }

    // Link return home charm.
    if (holdingReturnHome) {
      return tile.linkReturnHomeCharm(player, world, pos, heldItem, hand);
    }

    // If this is an anchor, we're done.
    if (isAnchor) {
      return false;
    }

    LocalizationHelper loc = SilentGems.instance.localizationHelper;

    // Destination set?
    if (!tile.isDestinationSet()) {
      PlayerHelper.addChatMessage(player, loc.getBlockSubText(Names.TELEPORTER, "NoDestination"));
      return true;
    }

    // Safety checks before teleporting:
    if (!tile.isDestinationSane(player)) {
      PlayerHelper.addChatMessage(player, loc.getBlockSubText(Names.TELEPORTER, "NotSane"));
      return true;
    }
    if (!tile.isDestinationSafe(player)) {
      PlayerHelper.addChatMessage(player, loc.getBlockSubText(Names.TELEPORTER, "NotSafe"));
      return true;
    }
    if (!tile.isDestinationAllowedIfDumb(player)) {
      PlayerHelper.addChatMessage(player, loc.getBlockSubText(Names.TELEPORTER, "NoReceiver"));
      return true;
    }

    // Check available charge, drain if there is enough.
    if (!tile.checkAndDrainChaos(player)) {
      return true;
    }

    // Teleport player
    tile.teleportEntityToDestination(player);

    // Play sounds
    float pitch = 0.7f + 0.3f * SilentGems.instance.random.nextFloat();
    world.playSound(null, pos, SoundEvents.entity_endermen_teleport, SoundCategory.BLOCKS, 1.0f,
        pitch);
    world.playSound(null, tile.getDestination().toBlockPos(), SoundEvents.entity_endermen_teleport,
        SoundCategory.BLOCKS, 1.0f, pitch);

    return true;
  }

  @Override
  public TileEntity createNewTileEntity(World worldIn, int meta) {

    return new TileTeleporter(isAnchor);
  }
}
