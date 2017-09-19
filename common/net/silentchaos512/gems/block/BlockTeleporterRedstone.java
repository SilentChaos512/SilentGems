package net.silentchaos512.gems.block;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.ShapelessOreRecipe;
import net.silentchaos512.gems.SilentGems;
import net.silentchaos512.gems.config.GemsConfig;
import net.silentchaos512.gems.init.ModBlocks;
import net.silentchaos512.gems.lib.EnumGem;
import net.silentchaos512.gems.lib.Names;
import net.silentchaos512.gems.tile.TileTeleporter;
import net.silentchaos512.lib.registry.RecipeMaker;
import net.silentchaos512.lib.util.DimensionalPosition;

public class BlockTeleporterRedstone extends BlockTeleporter {

  public BlockTeleporterRedstone(EnumGem.Set set) {

    super(set, false, Names.TELEPORTER_REDSTONE);
  }

  @Override
  public void addRecipes(RecipeMaker recipes) {

    if (GemsConfig.RECIPE_TELEPORTER_REDSTONE_DISABLE) {
      return;
    }

    ItemStack[] anyTeleporter = new ItemStack[] {
        new ItemStack(ModBlocks.teleporterRedstone, 1, OreDictionary.WILDCARD_VALUE),
        new ItemStack(ModBlocks.teleporterRedstoneDark, 1, OreDictionary.WILDCARD_VALUE) };

    int lastIndex = -1;

    for (int i = 0; i < subBlockCount; ++i) {
      EnumGem gem = getGem(i);
      ItemStack teleporterRedstone = new ItemStack(this, 1, i);
      ItemStack teleporterBasic = new ItemStack(ModBlocks.teleporter, 1, i);
      recipes.addShapelessOre(blockName + i, teleporterRedstone, teleporterBasic, "dustRedstone");
      for (ItemStack stack : anyTeleporter) {
        recipes.addShapelessOre(blockName + "_" + (++lastIndex) + "_recolor", new ItemStack(this, 1, i), stack,
            gem.getItemOreName());
      }
    }
  }

  @Override
  protected void clOnNeighborChanged(IBlockState state, World world, BlockPos pos, Block block) {

    final double searchRange = GemsConfig.TELEPORTER_REDSTONE_SEARCH_RADIUS
        * GemsConfig.TELEPORTER_REDSTONE_SEARCH_RADIUS;

    TileTeleporter tile = (TileTeleporter) world.getTileEntity(pos);

    if (!world.isRemote && world.isBlockIndirectlyGettingPowered(pos) != 0) {
      // Is this a "dumb" teleport and are they allowed if so?
      if (!tile.isDestinationAllowedIfDumb(null)) {
        String str = SilentGems.instance.localizationHelper.getBlockSubText(Names.TELEPORTER,
            "NoReceiver");
        return;
      }

      // Destination safe?
      if (!tile.isDestinationSafe(null)) {
        return;
      }

      boolean playSound = false;
      // Check all entities, teleport those close to the teleporter.
      DimensionalPosition source = null;
      for (Entity entity : world.getEntities(Entity.class,
          e -> e.getDistanceSqToCenter(pos) < searchRange)) {
        // Get source position (have to have the entity for this because dim?)
        if (source == null) {
          source = new DimensionalPosition(pos, entity.dimension);
        }
        if (entity instanceof EntityPlayer) {
          EntityPlayer player = (EntityPlayer) entity;
          // Chaos drain.
          if (!tile.checkAndDrainChaos(player)) {
            continue;
          }
        }
        if (tile.teleportEntityToDestination(entity)) {
          playSound = true;
        }
      }

      if (playSound) {
        float pitch = 0.7f + 0.3f * SilentGems.instance.random.nextFloat();
        for (BlockPos p : new BlockPos[] { pos, tile.getDestination().toBlockPos() }) {
          world.playSound(null, p, SoundEvents.ENTITY_ENDERMEN_TELEPORT, SoundCategory.BLOCKS, 1.0f,
              pitch);
        }
      }
    }
  }
}
