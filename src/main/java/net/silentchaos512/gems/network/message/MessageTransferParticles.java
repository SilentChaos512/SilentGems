package net.silentchaos512.gems.network.message;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.silentchaos512.gems.SilentGems;
import net.silentchaos512.gems.client.handler.ClientTickHandler;
import net.silentchaos512.gems.network.Message;
import net.silentchaos512.gems.util.NodePacketHelper;

public class MessageTransferParticles extends Message {

  public int color;
  public float startX;
  public float startY;
  public float startZ;
  public float endX;
  public float endY;
  public float endZ;

  public MessageTransferParticles() {

    startX = 0;
    startY = 0;
    startZ = 0;
    endX = 0;
    endY = 0;
    endZ = 0;
  }

  public MessageTransferParticles(BlockPos from, BlockPos to, int color) {

    this.color = color;
    this.startX = from.getX();
    this.startY = from.getY();
    this.startZ = from.getZ();
    this.endX = to.getX();
    this.endY = to.getY();
    this.endZ = to.getZ();
  }

  public MessageTransferParticles(Vec3d from, Vec3d to, int color) {

    this.color = color;
    this.startX = (float) from.x;
    this.startY = (float) from.y;
    this.startZ = (float) from.z;
    this.endX = (float) to.x;
    this.endY = (float) to.y;
    this.endZ = (float) to.z;
  }

  @Override
  @SideOnly(Side.CLIENT)
  public IMessage handleMessage(MessageContext context) {
    ClientTickHandler.scheduledActions.add(() -> {
      Vec3d from = new Vec3d(startX, startY, startZ);
      Vec3d to = new Vec3d(endX, endY, endZ);
      NodePacketHelper.spawnParticles(Minecraft.getMinecraft().world, from, to, color);
    });
    return null;
  }
}
