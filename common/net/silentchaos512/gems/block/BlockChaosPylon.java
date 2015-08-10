package net.silentchaos512.gems.block;

import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.silentchaos512.gems.item.CraftingMaterial;
import net.silentchaos512.gems.lib.Names;
import net.silentchaos512.gems.tile.TileChaosPylon;
import cpw.mods.fml.common.registry.GameRegistry;

public class BlockChaosPylon extends BlockSG implements ITileEntityProvider {

  public BlockChaosPylon() {

    super(Material.iron);
    this.setHardness(7.0f);
    this.setResistance(1000.0f);
    this.setUnlocalizedName(Names.CHAOS_PYLON);
  }

  @Override
  public void addRecipes() {

    ItemStack result = new ItemStack(this);
    ItemStack refinedEssenceBlock = MiscBlock.getStack(Names.CHAOS_ESSENCE_BLOCK_REFINED);
    GameRegistry.addRecipe(new ShapedOreRecipe(result, "lel", "lol", "ooo", 'l', "gemLapis", 'e',
        refinedEssenceBlock, 'o', Blocks.obsidian));
  }

  @Override
  public TileEntity createNewTileEntity(World p_149915_1_, int p_149915_2_) {

    return new TileChaosPylon();
  }
}
