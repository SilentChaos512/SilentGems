package net.silentchaos512.gems.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.silentchaos512.gems.SilentGems;
import net.silentchaos512.gems.core.registry.IAddRecipe;
import net.silentchaos512.gems.lib.Names;
import net.silentchaos512.gems.lib.Strings;
import net.silentchaos512.gems.tile.TileChaosAltar;
import cpw.mods.fml.common.registry.GameRegistry;

public class BlockChaosAltar extends BlockContainer implements IAddRecipe {

  public BlockChaosAltar() {

    super(Material.iron);
    this.setHardness(15.0f);
    this.setResistance(6000.0f);
    this.setStepSound(Block.soundTypeMetal);
    this.setCreativeTab(SilentGems.tabSilentGems);
    this.setBlockTextureName(Strings.RESOURCE_PREFIX + Names.CHAOS_ALTAR);
  }

  @Override
  public TileEntity createNewTileEntity(World world, int p_149915_2_) {

    return new TileChaosAltar();
  }

  @Override
  public void addOreDict() {

  }

  @Override
  public void addRecipes() {

    ItemStack result = new ItemStack(this);
    ItemStack refinedEssenceBlock = MiscBlock.getStack(Names.CHAOS_ESSENCE_BLOCK_REFINED);
    GameRegistry.addRecipe(new ShapedOreRecipe(result, " d ", "eoe", "ooo", 'e', refinedEssenceBlock, 'o',
        Blocks.obsidian, 'd', "gemDiamond"));
  }

  @Override
  public String getUnlocalizedName() {

    return "tile." + Names.CHAOS_ALTAR;
  }

  @Override
  public void registerBlockIcons(IIconRegister reg) {

    // TODO
  }

  @Override
  public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side,
      float hitX, float hitY, float hitZ) {

    if (world.isRemote) {
      return true;
    }

    TileEntity tile = world.getTileEntity(x, y, z);

    if (tile instanceof TileChaosAltar) {
      player.openGui(SilentGems.instance, 0, world, x, y, z);
    }

    return true;
  }
}
