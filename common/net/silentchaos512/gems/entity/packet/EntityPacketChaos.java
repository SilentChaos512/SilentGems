package net.silentchaos512.gems.entity.packet;

import java.util.List;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.silentchaos512.gems.SilentGems;
import net.silentchaos512.gems.api.energy.IChaosAccepter;
import net.silentchaos512.gems.api.energy.IChaosStorage;
import net.silentchaos512.gems.entity.packet.EntityChaosNodePacket.ColorPair;
import net.silentchaos512.gems.handler.PlayerDataHandler;
import net.silentchaos512.gems.handler.PlayerDataHandler.PlayerData;
import net.silentchaos512.lib.util.Color;
import net.silentchaos512.lib.util.PlayerHelper;

public class EntityPacketChaos extends EntityChaosNodePacket {

  public static final Color COLOR_HEAD = new Color(0xFFFF66);
  public static final Color COLOR_TAIL = new Color(0xFFFFCC);
  public static final ColorPair COLOR_PAIR = new ColorPair(COLOR_HEAD, COLOR_TAIL);
  public static final int COLOR_INDEX = 1;

  public EntityPacketChaos(World worldIn, EntityLivingBase target, int amount) {

    super(worldIn, target);
    this.amount = amount;
  }

  public EntityPacketChaos(World worldIn, BlockPos target, int amount) {

    super(worldIn, target);
    this.amount = amount;
  }

  public EntityPacketChaos(World worldIn) {

    super(worldIn);
  }

  @Override
  public void onImpactWithEntity(EntityLivingBase entity) {

    if (!(entity instanceof EntityPlayer)) {
      super.onImpactWithEntity(entity);
      return;
    }

    EntityPlayer player = (EntityPlayer) entity;
    int amountLeft = (int) amount;

    // Try to give directly to player first.
    PlayerData data = PlayerDataHandler.get(player);
    amountLeft -= data.sendChaos(amountLeft, true);

    if (amountLeft > 0) {
      List<ItemStack> list = PlayerHelper.getNonNullStacks(player);

      for (ItemStack stack : list) {
        if (stack.getItem() instanceof IChaosStorage) {
          amountLeft -= ((IChaosStorage) stack.getItem()).receiveCharge(stack, amountLeft, false);
          if (amountLeft <= 0) {
            break;
          }
        }
      }
    }

    super.onImpactWithEntity(entity);
  }

  @Override
  public void onImpactWithBlock(BlockPos pos, IBlockState state) {

    TileEntity tile = worldObj.getTileEntity(pos);
    if (tile != null && tile instanceof IChaosAccepter) {
      IChaosAccepter accepter = (IChaosAccepter) tile;
      accepter.receiveCharge((int) amount, false);
//      SilentGems.instance.logHelper.debug(amount, accepter.getCharge());
    }

    super.onImpactWithBlock(pos, state);
  }

  @Override
  public Color getColorHead() {

    return COLOR_HEAD;
  }

  @Override
  public Color getColorTail() {

    return COLOR_TAIL;
  }

  @Override
  public ColorPair getColorPair() {

    return COLOR_PAIR;
  }

  @Override
  public int getColorIndex() {

    return COLOR_INDEX;
  }
}
