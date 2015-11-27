package net.silentchaos512.gems.block;

import net.minecraft.block.Block;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemDye;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import net.minecraftforge.oredict.OreDictionary;
import net.silentchaos512.gems.core.util.RecipeHelper;
import net.silentchaos512.gems.lib.Names;
import net.silentchaos512.gems.lib.Strings;

public class BlockFluffyBlock extends BlockSG {

  public BlockFluffyBlock() {

    super(Material.cloth);
    icons = new IIcon[16];
    setHasSubtypes(true);

    setHardness(0.125f);
    setResistance(3.0f);
    setStepSound(Block.soundTypeCloth);

    setUnlocalizedName(Names.FLUFFY_BLOCK);
  }

  @Override
  public void addRecipes() {

    ItemStack any = new ItemStack(this, 1, OreDictionary.WILDCARD_VALUE);
    for (int i = 0; i < 16; ++i) {
      String dye = ItemDye.field_150923_a[~i & 15];
      if (dye.equals("silver")) {
        dye = "lightGray";
      }
      dye = "dye" + Character.toUpperCase(dye.charAt(0)) + dye.substring(1);
      RecipeHelper.addSurroundOre(new ItemStack(this, 8, i), dye, any);
    }
  }

  @Override
  public void onFallenUpon(World world, int x, int y, int z, Entity entity, float distance) {

    // Count the number of fluffy blocks that are stacked up.
    int stackedBlocks = 0;
    for (int depth = 0; y - depth > 0 && world.getBlock(x, y - depth, z) == this; ++depth) {
      ++stackedBlocks;
    }

    // Reduce fall distance by 10 blocks per stacked block
    entity.fallDistance -= Math.min(10 * stackedBlocks, distance);
  }

  @Override
  public void registerBlockIcons(IIconRegister reg) {

    for (int i = 0; i < icons.length; ++i) {
      icons[i] = reg.registerIcon(Strings.RESOURCE_PREFIX + blockName + i);
    }
  }

  @Override
  public MapColor getMapColor(int meta) {

    return MapColor.getMapColorForBlockColored(meta);
  }
}
