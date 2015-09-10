package net.silentchaos512.gems.block;

import org.apache.http.message.BasicHttpEntityEnclosingRequest;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.ShapelessOreRecipe;
import net.silentchaos512.gems.SilentGems;
import net.silentchaos512.gems.configuration.Config;
import net.silentchaos512.gems.core.util.LocalizationHelper;
import net.silentchaos512.gems.core.util.PlayerHelper;
import net.silentchaos512.gems.lib.EnumGem;
import net.silentchaos512.gems.lib.Names;
import net.silentchaos512.gems.lib.Strings;
import net.silentchaos512.gems.tile.TileTeleporter;
import cpw.mods.fml.common.registry.GameRegistry;

public class BlockRedstoneTeleporter extends BlockTeleporter {

  public BlockRedstoneTeleporter() {

    this.setUnlocalizedName(Names.TELEPORTER_REDSTONE);
  }

  @Override
  public void addRecipes() {

    if (!Config.recipeRedstoneTeleporterDisabled) {
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
      // Same icons as base teleporters.
      icons[i] = reg.registerIcon(Strings.RESOURCE_PREFIX + Names.TELEPORTER + i);
    }
  }

  @Override
  public void onNeighborBlockChange(World world, int x, int y, int z, Block neighborBlock) {

    final double searchRange = Config.redstoneTeleporterSearchRange;

    TileTeleporter tile = (TileTeleporter) world.getTileEntity(x, y, z);
    if (!world.isRemote && world.isBlockIndirectlyGettingPowered(x, y, z)) {
      if (!this.isDestinationSafe(tile.destX, tile.destY, tile.destZ, tile.destD)) {
        return;
      }
      double dx = x + 0.5;
      double dy = y + 0.5;
      double dz = z + 0.5;
      boolean playSound = false;
      for (int i = 0; i < world.loadedEntityList.size(); ++i) {
        Entity entity = (Entity) world.loadedEntityList.get(i);
        if (entity != null && entity.getDistanceSq(dx, dy, dz) < searchRange) {
          this.teleporterEntityTo(entity, tile.destX, tile.destY, tile.destZ, tile.destD);
          playSound = true;
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
