package net.silentchaos512.gems.block;

import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.ShapelessOreRecipe;
import net.silentchaos512.gems.SilentGems;
import net.silentchaos512.gems.configuration.Config;
import net.silentchaos512.gems.core.util.DimensionalPosition;
import net.silentchaos512.gems.core.util.LocalizationHelper;
import net.silentchaos512.gems.core.util.PlayerHelper;
import net.silentchaos512.gems.lib.EnumGem;
import net.silentchaos512.gems.lib.Names;
import net.silentchaos512.gems.lib.Strings;
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
  public void registerBlockIcons(IIconRegister reg) {

    icons = new IIcon[EnumGem.values().length];
    for (int i = 0; i < icons.length; ++i) {
      icons[i] = reg.registerIcon(Strings.RESOURCE_PREFIX + Names.TELEPORTER + "Redstone" + i);
    }
  }

  @Override
  public void onNeighborBlockChange(World world, int x, int y, int z, Block neighborBlock) {

    final double searchRange = Config.REDSTONE_TELEPORTER_SEARCH_RANGE
        * Config.REDSTONE_TELEPORTER_SEARCH_RANGE;

    TileTeleporter tile = (TileTeleporter) world.getTileEntity(x, y, z);
    if (!world.isRemote && world.isBlockIndirectlyGettingPowered(x, y, z)) {
      DimensionalPosition destination = new DimensionalPosition(tile.destX, tile.destY, tile.destZ,
          tile.destD);

      // Is this a "dumb" teleport and are they allowed if so?
      if (!isDestinationAllowedIfDumb(destination)) {
        String str = LocalizationHelper.getOtherBlockKey(Names.TELEPORTER,
            DESTINATION_NO_TELEPORTER);
        return;
      }

      // Destination safe?
      if (!this.isDestinationSafe(destination)) {
        return;
      }
      double dx = x + 0.5;
      double dy = y + 0.5;
      double dz = z + 0.5;
      boolean playSound = false;

      // Check all entities, teleport those close to the teleporter.
      DimensionalPosition source = null;
      for (int i = 0; i < world.loadedEntityList.size(); ++i) {
        Entity entity = (Entity) world.loadedEntityList.get(i);
        if (entity != null && entity.getDistanceSq(dx, dy, dz) < searchRange) {
          // Get source position (have to have the entity for this because dim?)
          if (source == null) {
            source = new DimensionalPosition(x, y, z, entity.dimension);
          }
          if (entity instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer) entity;
            // XP Drain.
            if (!checkAndDrainXP(player, source, destination)) {
              continue;
            }
          }
          if (teleporterEntityTo(entity, destination)) {
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
