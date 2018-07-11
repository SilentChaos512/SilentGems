package net.silentchaos512.gems.block;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.silentchaos512.gems.SilentGems;
import net.silentchaos512.gems.item.ItemChaosGem;
import net.silentchaos512.gems.lib.Names;
import net.silentchaos512.gems.tile.TileChaosPlinth;
import net.silentchaos512.lib.block.BlockContainerSL;
import net.silentchaos512.lib.registry.RecipeMaker;
import net.silentchaos512.lib.util.StackHelper;

public class BlockChaosPlinth extends BlockContainerSL {

  protected BlockChaosPlinth() {

    super(1, SilentGems.MODID, Names.CHAOS_PLINTH, Material.ROCK);
  }

  @Override
  public TileEntity createNewTileEntity(World world, int meta) {

    return new TileChaosPlinth();
  }

  @Override
  public void addRecipes(RecipeMaker recipes) {

    // TODO
  }

  @Override
  protected boolean clOnBlockActivated(World world, BlockPos pos, IBlockState state,
      EntityPlayer player, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {

    ItemStack heldItem = player.getHeldItem(hand);
    TileEntity t = world.getTileEntity(pos);

    if (t != null && t instanceof TileChaosPlinth) {
      TileChaosPlinth tile = (TileChaosPlinth) t;
      ItemStack currentGem = tile.getStackInSlot(0);

      if (StackHelper.isValid(heldItem) && heldItem.getItem() instanceof ItemChaosGem) {
        player.setHeldItem(hand, currentGem);
        tile.setInventorySlotContents(0, heldItem);
        return true;
      } else if (StackHelper.isValid(currentGem) && StackHelper.isEmpty(heldItem)) {
        // TODO
      }
    }

    return false;
  }
}
