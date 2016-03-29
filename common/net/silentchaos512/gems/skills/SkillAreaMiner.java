package net.silentchaos512.gems.skills;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.server.SPacketBlockChange;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.WorldSettings.GameType;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.silentchaos512.gems.SilentGems;
import net.silentchaos512.gems.api.ITool;
import net.silentchaos512.gems.api.lib.EnumMaterialTier;
import net.silentchaos512.gems.handler.PlayerDataHandler;
import net.silentchaos512.gems.handler.PlayerDataHandler.PlayerData;
import net.silentchaos512.gems.util.ToolHelper;

public class SkillAreaMiner extends ToolSkill {

  public static final float DIG_SPEED_MULTIPLIER = 0.33f;
  public static final int CHAOS_COST = 100;

  public static final SkillAreaMiner INSTANCE = new SkillAreaMiner();

  public void onGetBreakSpeed(PlayerEvent.BreakSpeed event) {

    PlayerData data = PlayerDataHandler.get(event.getEntityPlayer());
    int cost = event.getEntityPlayer().capabilities.isCreativeMode ? 0 : CHAOS_COST;
    if (data.chaos >= cost) {
      event.setNewSpeed(event.getNewSpeed() * DIG_SPEED_MULTIPLIER);
    }
  }

  @Override
  public boolean activate(ItemStack tool, EntityPlayer player, BlockPos pos) {

    World world = player.worldObj;
    IBlockState state = world.getBlockState(pos);

    // Must be a tool, no nulls.
    if (!(tool.getItem() instanceof ITool) || state == null || state.getBlock() == null) {
      return false;
    }

    // Must be super tool with special enabled.
    if (ToolHelper.getToolTier(tool) != EnumMaterialTier.SUPER
        || !ToolHelper.isSpecialAbilityEnabled(tool)) {
      return false;
    }

    // Tool must be effective on block!
    if (!isToolEffective(tool, world, pos, state)) {
      return false;
    }

    // Does player have enough chaos?
    PlayerData data = PlayerDataHandler.get(player);
    int cost = player.capabilities.isCreativeMode ? 0 : CHAOS_COST;
    if (data.chaos >= cost) {
      data.drainChaos(cost);
    } else {
      return false;
    }

    RayTraceResult mop = raytraceFromEntity(world, player, false, 4.5);
    if (mop == null) {
      return false;
    }
    int sideHit = mop.sideHit.getIndex();

    int xRange = 1;
    int yRange = 1;
    int zRange = 0;
    switch (sideHit) {
      case 0:
      case 1:
        yRange = 0;
        zRange = 1;
        break;
      case 2:
      case 3:
        xRange = 1;
        zRange = 0;
        break;
      case 4:
      case 5:
        xRange = 0;
        zRange = 1;
        break;
    }

    int blocksBroken = 0;

    final int x = pos.getX();
    final int y = pos.getY();
    final int z = pos.getZ();
    for (int xPos = x - xRange; xPos <= x + xRange; xPos++) {
      for (int yPos = y - yRange; yPos <= y + yRange; yPos++) {
        for (int zPos = z - zRange; zPos <= z + zRange; zPos++) {
          if (xPos == x && yPos == y && zPos == z) {
            continue;
          }

          if (breakExtraBlock(tool, player.worldObj, new BlockPos(xPos, yPos, zPos), player, pos)) {
            ++blocksBroken;
          }
        }
      }
    }

    ToolHelper.incrementStatBlocksMined(tool, blocksBroken);
    return true;
  }

  protected boolean isToolEffective(ItemStack tool, World world, BlockPos pos, IBlockState state) {

    Block block = state.getBlock();

    if (tool.canHarvestBlock(state)) {
      return true;
    }

    if (tool.getItem() instanceof ITool) {
      for (Material material : ((ITool) tool.getItem()).getExtraEffectiveMaterials()) {
        if (state.getMaterial() == material) {
          return true;
        }
      }
    }

    return ForgeHooks.isToolEffective(world, pos, tool);
  }

  public boolean breakExtraBlock(ItemStack tool, World world, BlockPos pos,
      EntityPlayer playerEntity, BlockPos refPos) {

    if (world.isAirBlock(pos))
      return false;

    if (!(playerEntity instanceof EntityPlayerMP)) {
      return false;
    }

    IBlockState state = world.getBlockState(pos);
    Block block = state.getBlock();
    EntityPlayerMP player = (EntityPlayerMP) playerEntity;

    if (!isToolEffective(tool, world, pos, state)) {
      return false;
    }

    IBlockState refState = world.getBlockState(refPos);
    // float refStrength = ForgeHooks.blockStrength(state, player, world, refPos); // Throws an exception in some cases.
    float refStrength = player.getBreakSpeed(refState, refPos)
        / refState.getBlockHardness(world, refPos) / 30f;
    float strength = ForgeHooks.blockStrength(state, player, world, pos); // But this one doesn't?

    // LogHelper.list(Block.getIdFromBlock(refBlock), refStrength, strength, refStrength / strength);
    if (!ForgeHooks.canHarvestBlock(block, player, world, pos) || refStrength / strength > 10f) {
      return false;
    }

    GameType gameType = player.interactionManager.getGameType();
    int xpDropped = ForgeHooks.onBlockBreakEvent(world, gameType, player, pos);
    boolean canceled = xpDropped == -1;
    if (canceled) {
      return false;
    }

    if (player.capabilities.isCreativeMode) {
      block.onBlockHarvested(world, pos, state, player);
      if (block.removedByPlayer(state, world, pos, player, false)) {
        block.onBlockDestroyedByPlayer(world, pos, state);
      }

      if (!world.isRemote) {
        player.playerNetServerHandler.sendPacket(new SPacketBlockChange(world, pos));
      }
      return true;
    }

    tool.onBlockDestroyed(world, state, pos, player);

    if (!world.isRemote) {

      block.onBlockHarvested(world, pos, state, player);

      if (block.removedByPlayer(state, world, pos, player, true)) {
        block.onBlockDestroyedByPlayer(world, pos, state);
        block.harvestBlock(world, player, pos, state, world.getTileEntity(pos), tool);
        block.dropXpOnBlockBreak(world, pos, xpDropped);
      }

      player.playerNetServerHandler.sendPacket(new SPacketBlockChange(world, pos));
    } else {
      world.playAuxSFX(2001, pos, Block.getIdFromBlock(block));
      if (block.removedByPlayer(state, world, pos, player, true)) {
        block.onBlockDestroyedByPlayer(world, pos, state);
      }

      tool.onBlockDestroyed(world, state, pos, player);

      // if (tool.stackSize == 0) {
      // PlayerHelper.removeItem(player, tool);
      // }
    }

    return true;
  }

  private RayTraceResult raytraceFromEntity(World world, Entity player, boolean par3,
      double range) {

    float f = 1.0F;
    float f1 = player.prevRotationPitch + (player.rotationPitch - player.prevRotationPitch) * f;
    float f2 = player.prevRotationYaw + (player.rotationYaw - player.prevRotationYaw) * f;
    double d0 = player.prevPosX + (player.posX - player.prevPosX) * (double) f;
    double d1 = player.prevPosY + (player.posY - player.prevPosY) * (double) f;
    if (!world.isRemote && player instanceof EntityPlayer)
      d1 += 1.62D;
    double d2 = player.prevPosZ + (player.posZ - player.prevPosZ) * (double) f;
    Vec3d vec3 = new Vec3d(d0, d1, d2);
    float f3 = MathHelper.cos(-f2 * 0.017453292F - (float) Math.PI);
    float f4 = MathHelper.sin(-f2 * 0.017453292F - (float) Math.PI);
    float f5 = -MathHelper.cos(-f1 * 0.017453292F);
    float f6 = MathHelper.sin(-f1 * 0.017453292F);
    float f7 = f4 * f5;
    float f8 = f3 * f5;
    double d3 = range;
    if (player instanceof EntityPlayerMP) {
      d3 = ((EntityPlayerMP) player).interactionManager.getBlockReachDistance();
    }
    Vec3d vec31 = vec3.addVector((double) f7 * d3, (double) f6 * d3, (double) f8 * d3);
    return world.rayTraceBlocks(vec3, vec31, par3, !par3, par3);
  }

  @Override
  public String getTranslatedName() {

    return SilentGems.instance.localizationHelper.getLocalizedString("skill", "AreaMiner");
  }
}
