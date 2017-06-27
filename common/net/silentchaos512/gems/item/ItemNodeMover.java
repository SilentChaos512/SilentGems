package net.silentchaos512.gems.item;

import java.util.List;
import java.util.Map;

import com.google.common.collect.Lists;

import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.silentchaos512.gems.SilentGems;
import net.silentchaos512.gems.init.ModBlocks;
import net.silentchaos512.gems.init.ModItems;
import net.silentchaos512.gems.lib.Names;
import net.silentchaos512.gems.tile.TileChaosNode;
import net.silentchaos512.lib.item.ItemSL;
import net.silentchaos512.lib.registry.RecipeMaker;

public class ItemNodeMover extends ItemSL {

  public static final int META_EMPTY = 0;
  public static final int META_FILLED = 1;
  public static final int META_USED = 2;

  public ItemNodeMover() {

    super(1, SilentGems.MODID, Names.NODE_MOVER);
    setMaxStackSize(1);
  }

  @Override
  public void addRecipes(RecipeMaker recipes) {

    ItemStack empty = new ItemStack(this, 1, META_EMPTY);
    ItemStack spent = new ItemStack(this, 1, META_USED);
    ItemStack chaosCore = ModItems.craftingMaterial.chaosCore;
    ItemStack netherShard = ModItems.craftingMaterial.netherShard;
    ItemStack enderFrost = ModItems.craftingMaterial.enderFrost;

    recipes.addSurroundOre("node_mover", empty, chaosCore, netherShard, enderFrost);
    recipes.addShaped("node_mover_recharge", empty, "sms", 's', netherShard, 'm', spent);
  }

  @Override
  protected EnumActionResult clOnItemUse(EntityPlayer playerIn, World worldIn,
      BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {

    ItemStack stack = playerIn.getHeldItem(hand);

    if (stack.getItemDamage() >= META_USED) {
      return EnumActionResult.PASS;
    }

    if (stack.hasTagCompound()) {
      return releaseNode(stack, worldIn, pos, facing);
    }

    TileEntity tile = worldIn.getTileEntity(pos);
    if (tile != null && tile instanceof TileChaosNode) {
      return captureNode(stack, worldIn, pos, (TileChaosNode) tile);
    }

    return EnumActionResult.PASS;
  }

  protected EnumActionResult releaseNode(ItemStack stack, World worldIn, BlockPos pos,
      EnumFacing facing) {

    pos = pos.offset(facing);
    if (!worldIn.isAirBlock(pos)) {
      return EnumActionResult.PASS;
    }

    worldIn.setBlockState(pos, ModBlocks.chaosNode.getDefaultState(), 2);
    TileChaosNode tileNode = (TileChaosNode) worldIn.getTileEntity(pos);
    NBTTagCompound compound = stack.getTagCompound();
    compound.setInteger("x", pos.getX());
    compound.setInteger("y", pos.getY());
    compound.setInteger("z", pos.getZ());
    tileNode.readFromNBT(compound);

    stack.setTagCompound(null);
    stack.setItemDamage(META_USED);

    return EnumActionResult.SUCCESS;
  }

  protected EnumActionResult captureNode(ItemStack stack, World worldIn, BlockPos pos,
      TileChaosNode tileNode) {

    NBTTagCompound compound = new NBTTagCompound();
    tileNode.writeToNBT(compound);
    stack.setTagCompound(compound);
    stack.setItemDamage(META_FILLED);

    worldIn.setBlockToAir(pos);

    return EnumActionResult.SUCCESS;
  }

  @Override
  public String getNameForStack(ItemStack stack) {

    return itemName + stack.getItemDamage();
  }

  @Override
  public void getModels(Map<Integer, ModelResourceLocation> models) {

    String name = (SilentGems.RESOURCE_PREFIX + "NodeMover").toLowerCase();
    for (int i = 0; i < 3; ++i) {
      models.put(i, new ModelResourceLocation(name + i, "inventory"));
    }
  }
}
