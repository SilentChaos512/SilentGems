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
import net.minecraftforge.oredict.OreDictionary;
import net.silentchaos512.gems.SilentGems;
import net.silentchaos512.gems.config.GemsConfig;
import net.silentchaos512.gems.init.ModBlocks;
import net.silentchaos512.gems.init.ModItems;
import net.silentchaos512.gems.lib.EnumGem;
import net.silentchaos512.gems.lib.Names;
import net.silentchaos512.gems.tile.TileTeleporter;
import net.silentchaos512.lib.registry.RecipeMaker;
import net.silentchaos512.lib.util.ChatHelper;
import net.silentchaos512.lib.util.DimensionalPosition;
import net.silentchaos512.lib.util.LocalizationHelper;
import net.silentchaos512.lib.util.StackHelper;
import net.silentchaos512.wit.api.IWitHudInfo;

public class BlockTeleporter extends BlockGemSubtypes implements ITileEntityProvider, IWitHudInfo {

  public final boolean isAnchor;

  // For anchors only.
  public BlockTeleporter(String name) {

    super(name);
    isAnchor = true;
  }

  public BlockTeleporter(EnumGem.Set set, boolean isAnchor) {

    this(set, isAnchor, Names.TELEPORTER);
  }

  public BlockTeleporter(EnumGem.Set set, boolean isAnchor, String name) {

    super(isAnchor ? 1 : 16, set, nameForSet(set, name), Material.IRON);
    this.isAnchor = isAnchor;

    setHardness(15.0f);
    setResistance(2000.0f);
    setSoundType(SoundType.METAL);
    setHarvestLevel("pickaxe", 1);
  }

  @Override
  public void addRecipes(RecipeMaker recipes) {

    if (GemsConfig.RECIPE_TELEPORTER_DISABLE) {
      return;
    }

    ItemStack[] anyTeleporter = new ItemStack[] {
        new ItemStack(ModBlocks.teleporter, 1, OreDictionary.WILDCARD_VALUE),
        new ItemStack(ModBlocks.teleporterDark, 1, OreDictionary.WILDCARD_VALUE) };

    int lastIndex = -1;

    for (int i = 0; i < subBlockCount; ++i) {
      EnumGem gem = getGem(i);
      recipes.addShapedOre(blockName + i, new ItemStack(this, 2, i), "cec", " g ", "cec", 'c',
          ModItems.craftingMaterial.chaosEssenceEnriched, 'e',
          ModItems.craftingMaterial.enderEssence, 'g', gem.getBlockOreName());
      for (ItemStack stack : anyTeleporter) {
        recipes.addShapelessOre(blockName + "_" + (++lastIndex) + "_recolor", new ItemStack(this, 1, i), stack,
            gem.getItemOreName());
      }
    }
  }

  @Override
  public List<String> getWitLines(IBlockState state, BlockPos pos, EntityPlayer player,
      boolean advanced) {

    TileEntity tile = player.world.getTileEntity(pos);
    if (tile == null || !(tile instanceof TileTeleporter)) {
      return null;
    }

    TileTeleporter teleporter = (TileTeleporter) tile;
    DimensionalPosition destination = teleporter.getDestination();
    return Lists.newArrayList(destination != null ? destination.toString() : "null");
  }

  @Override
  protected boolean clOnBlockActivated(World world, BlockPos pos, IBlockState state,
      EntityPlayer player, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {

    ItemStack heldItem = player.getHeldItem(hand);
    boolean holdingLinker = StackHelper.isValid(heldItem)
        && heldItem.getItem() == ModItems.teleporterLinker;
    boolean holdingReturnHome = StackHelper.isValid(heldItem)
        && heldItem.getItem() == ModItems.returnHomeCharm;

    if (world.isRemote) {
      return holdingLinker || holdingReturnHome ? true : !isAnchor;
    }

    TileTeleporter tile = (TileTeleporter) world.getTileEntity(pos);
    if (tile == null) {
      SilentGems.logHelper.warning("Teleporter tile at " + pos + " not found!");
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
      ChatHelper.sendMessage(player, loc.getBlockSubText(Names.TELEPORTER, "NoDestination"));
      return true;
    }

    // Safety checks before teleporting:
    if (!tile.isDestinationSane(player)) {
      ChatHelper.sendMessage(player, loc.getBlockSubText(Names.TELEPORTER, "NotSane"));
      return true;
    }
    if (!tile.isDestinationSafe(player)) {
      ChatHelper.sendMessage(player, loc.getBlockSubText(Names.TELEPORTER, "NotSafe"));
      return true;
    }
    if (!tile.isDestinationAllowedIfDumb(player)) {
      ChatHelper.sendMessage(player, loc.getBlockSubText(Names.TELEPORTER, "NoReceiver"));
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
    for (BlockPos p : new BlockPos[] { pos, tile.getDestination().toBlockPos() }) {
      world.playSound(null, p, SoundEvents.ENTITY_ENDERMEN_TELEPORT, SoundCategory.BLOCKS, 1.0f,
          pitch);
    }

    return true;
  }

  @Override
  public TileEntity createNewTileEntity(World worldIn, int meta) {

    return new TileTeleporter(isAnchor);
  }
}
