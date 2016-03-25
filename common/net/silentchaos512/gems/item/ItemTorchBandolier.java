package net.silentchaos512.gems.item;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.silentchaos512.gems.SilentGems;
import net.silentchaos512.gems.api.IBlockPlacer;
import net.silentchaos512.gems.lib.Names;
import net.silentchaos512.lib.item.ItemSL;


public class ItemTorchBandolier extends ItemBlockPlacer {

  public static final int MAX_DAMAGE = 1024;

  public ItemTorchBandolier() {

    super(Names.TORCH_BANDOLIER, MAX_DAMAGE);
  }

  @Override
  public IBlockState getBlockPlaced(ItemStack stack) {

    return Blocks.torch.getDefaultState();
  }
}
