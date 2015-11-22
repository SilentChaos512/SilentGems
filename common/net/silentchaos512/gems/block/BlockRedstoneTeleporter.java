package net.silentchaos512.gems.block;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.ShapelessOreRecipe;
import net.silentchaos512.gems.SilentGems;
import net.silentchaos512.gems.configuration.Config;
import net.silentchaos512.gems.lib.EnumGem;
import net.silentchaos512.gems.lib.Names;
import net.silentchaos512.gems.tile.TileTeleporter;

public class BlockRedstoneTeleporter extends BlockTeleporter {

  public BlockRedstoneTeleporter() {

    this.setUnlocalizedName(Names.TELEPORTER_REDSTONE);
  }

  @Override
  public void addRecipes() {

    if (!Config.RECIPE_REDSTONE_TELEPORTER_DISABLED) {
      for (EnumGem gem : EnumGem.values()) {
        ItemStack redstoneTeleporter = new ItemStack(this, 1, gem.id);
        ItemStack basicTeleporter = new ItemStack(ModBlocks.teleporter, 1, gem.id);
        // Base recipe
        GameRegistry
            .addRecipe(new ShapelessOreRecipe(redstoneTeleporter, basicTeleporter, "dustRedstone"));
        // Recolor recipe
        ItemStack anyRedstoneTeleporter = new ItemStack(this, 1, OreDictionary.WILDCARD_VALUE);
        GameRegistry.addRecipe(new ShapelessOreRecipe(redstoneTeleporter, anyRedstoneTeleporter,
            gem.getItemOreName()));
      }
    }
  }

  @Override
  public void onNeighborBlockChange(World world, BlockPos pos, IBlockState state,
      Block neighborBlock) {

    final double searchRange = Config.REDSTONE_TELEPORTER_SEARCH_RANGE
        * Config.REDSTONE_TELEPORTER_SEARCH_RANGE;

    TileTeleporter tile = (TileTeleporter) world.getTileEntity(pos);
    if (!world.isRemote && world.isBlockIndirectlyGettingPowered(pos) != 0) { // TODO: Is != 0 correct here?
      if (!this.isDestinationSafe(tile.destX, tile.destY, tile.destZ, tile.destD)) {
        return;
      }
      final int x = pos.getX();
      final int y = pos.getY();
      final int z = pos.getZ();

      double dx = x + 0.5;
      double dy = y + 0.5;
      double dz = z + 0.5;
      boolean playSound = false;
      // Check all entities, teleport those close to the teleporter.
      for (int i = 0; i < world.loadedEntityList.size(); ++i) {
        Entity entity = (Entity) world.loadedEntityList.get(i);
        if (entity != null && entity.getDistanceSq(dx, dy, dz) < searchRange) {
          if (teleporterEntityTo(entity, tile.destX, tile.destY, tile.destZ, tile.destD)) {
            playSound = true;
          }
        }
      }

      if (playSound) {
        float soundPitch = SilentGems.instance.random.nextFloat();
        soundPitch = soundPitch * 0.3f + 0.7f;
        world.playSoundEffect(x + 0.5, y + 0.5, z + 0.5, "mob.endermen.portal", 1.0f, soundPitch);
        world.playSoundEffect(tile.destX + 0.5, tile.destY + 0.5, tile.destZ + 0.5,
            "mob.endermen.portal", 1.0f, soundPitch);
      }
    }
  }
}
