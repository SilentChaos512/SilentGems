package net.silentchaos512.gems.enchantment;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnumEnchantmentType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemBook;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemTool;
import net.minecraft.network.play.server.S23PacketBlockChange;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import net.minecraft.world.WorldSettings.GameType;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.event.world.BlockEvent;
import net.silentchaos512.gems.core.util.InventoryHelper;
import net.silentchaos512.gems.item.tool.GemAxe;
import net.silentchaos512.gems.item.tool.GemPickaxe;
import net.silentchaos512.gems.lib.Names;

public class EnchantmentAOE extends Enchantment {

  public static final float DIG_SPEED_MULTIPLIER = 0.2f;
  // public static final float DIG_SPEED_REDUCTION = 8.0f;

  protected EnchantmentAOE(int par1, int par2, EnumEnchantmentType par3EnumEnchantmentType) {

    super(par1, new ResourceLocation("aoe"), par2, par3EnumEnchantmentType);
    setName(Names.AOE);
  }

  @Override
  public boolean canApply(ItemStack stack) {

    // This enchantment is for gem tools and books.
    if (InventoryHelper.isGemMiningTool(stack) || stack.getItem() instanceof ItemBook) {
      return stack.isItemStackDamageable() ? true : super.canApply(stack);
    }

    return false;
  }

  @Override
  public boolean canApplyTogether(Enchantment e) {

    return e != ModEnchantments.lumberjack && super.canApplyTogether(e);
  }

  @Override
  public int getMinEnchantability(int par1) {

    return 15;
  }

  @Override
  public int getMaxEnchantability(int par1) {

    return super.getMinEnchantability(par1) + 50;
  }

  @Override
  public int getMaxLevel() {

    return 1;
  }

  @Override
  public String getTranslatedName(int par1) {

    return StatCollector.translateToLocal("enchantment." + Names.AOE) + " "
        + StatCollector.translateToLocal("enchantment.level." + par1);
  }

  public static boolean isToolEffective(ItemStack tool, World world, BlockPos pos,
      IBlockState state) {

    boolean toolEffective = ForgeHooks.isToolEffective(world, pos, tool);
    Block block = state.getBlock();

    if (tool.getItem().canHarvestBlock(block, tool)) {
      return true;
    }

    if (tool.getItem() instanceof GemPickaxe) {
      for (Material m : GemPickaxe.extraEffectiveMaterials) {
        if (block.getMaterial() == m) {
          toolEffective = true;
        }
      }
    } else if (tool.getItem() instanceof GemAxe) {
      for (Material m : GemAxe.extraEffectiveMaterials) {
        if (block.getMaterial() == m) {
          toolEffective = true;
        }
      }
    }
    return toolEffective;
  }

  public static int tryActivate(ItemStack tool, int x, int y, int z, EntityPlayer player) {

    World world = player.worldObj;
    BlockPos pos = new BlockPos(x, y, z);
    IBlockState state = world.getBlockState(pos);
    Block block = state.getBlock();

    if (!(tool.getItem() instanceof ItemTool) || block == null || player.isSneaking()) {
      return 0;
    }

    boolean toolEffective = isToolEffective(tool, world, pos, state);
    if (!toolEffective) {
      return 0;
    }

    MovingObjectPosition mop = raytraceFromEntity(player.worldObj, player, false, 4.5);
    if (mop == null) {
      return 0;
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

    for (int xPos = x - xRange; xPos <= x + xRange; xPos++) {
      for (int yPos = y - yRange; yPos <= y + yRange; yPos++) {
        for (int zPos = z - zRange; zPos <= z + zRange; zPos++) {
          if (xPos == x && yPos == y && zPos == z) {
            continue;
          }

          if (breakExtraBlock(tool, player.worldObj, xPos, yPos, zPos, sideHit, player, x, y, z)) {
            ++blocksBroken;
          }
        }
      }
    }

    return blocksBroken;
  }

  public static boolean breakExtraBlock(ItemStack tool, World world, int x, int y, int z,
      int sidehit, EntityPlayer playerEntity, int refX, int refY, int refZ) {

    BlockPos pos = new BlockPos(x, y, z);

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

    BlockPos refPos = new BlockPos(refX, refY, refZ);
    IBlockState refState = world.getBlockState(refPos);
    float refStrength = ForgeHooks.blockStrength(state, player, world, refPos);
    float strength = ForgeHooks.blockStrength(state, player, world, pos);

    // LogHelper.list(Block.getIdFromBlock(refBlock), refStrength, strength, refStrength / strength);
    if (!ForgeHooks.canHarvestBlock(block, player, world, pos) || refStrength / strength > 10f) {
      return false;
    }

    GameType gameType = player.theItemInWorldManager.getGameType();
    int xpDropped = ForgeHooks.onBlockBreakEvent(world, gameType, player, pos);
    boolean canceled = xpDropped == -1;
    if (canceled) {
      return false;
    }

    if (player.capabilities.isCreativeMode) {
      block.onBlockHarvested(world, pos, state, player);
      if (block.removedByPlayer(world, pos, player, false)) {
        block.onBlockDestroyedByPlayer(world, pos, state);
      }

      if (!world.isRemote) {
        player.playerNetServerHandler.sendPacket(new S23PacketBlockChange(world, pos));
      }
      return true;
    }

    player.getCurrentEquippedItem().onBlockDestroyed(world, block, pos, player);

    if (!world.isRemote) {

      block.onBlockHarvested(world, pos, state, player);

      if (block.removedByPlayer(world, pos, player, true)) {
        block.onBlockDestroyedByPlayer(world, pos, state);
        block.harvestBlock(world, player, pos, state, null);
        block.dropXpOnBlockBreak(world, pos, xpDropped);
      }

      player.playerNetServerHandler.sendPacket(new S23PacketBlockChange(world, pos));
    } else {
      world.playAuxSFX(2001, pos, Block.getIdFromBlock(block) /* + (meta << 12) */); // TODO: What's the meta for?
      if (block.removedByPlayer(world, pos, player, true)) {
        block.onBlockDestroyedByPlayer(world, pos, state);
      }

      ItemStack itemstack = player.getCurrentEquippedItem();
      if (itemstack != null) {
        itemstack.onBlockDestroyed(world, block, pos, player);

        if (itemstack.stackSize == 0) {
          player.destroyCurrentEquippedItem();
        }
      }
    }

    return true;
  }

  private static MovingObjectPosition raytraceFromEntity(World world, Entity player, boolean par3,
      double range) {

    float f = 1.0F;
    float f1 = player.prevRotationPitch + (player.rotationPitch - player.prevRotationPitch) * f;
    float f2 = player.prevRotationYaw + (player.rotationYaw - player.prevRotationYaw) * f;
    double d0 = player.prevPosX + (player.posX - player.prevPosX) * (double) f;
    double d1 = player.prevPosY + (player.posY - player.prevPosY) * (double) f;
    if (!world.isRemote && player instanceof EntityPlayer)
      d1 += 1.62D;
    double d2 = player.prevPosZ + (player.posZ - player.prevPosZ) * (double) f;
    Vec3 vec3 = new Vec3(d0, d1, d2);
    float f3 = MathHelper.cos(-f2 * 0.017453292F - (float) Math.PI);
    float f4 = MathHelper.sin(-f2 * 0.017453292F - (float) Math.PI);
    float f5 = -MathHelper.cos(-f1 * 0.017453292F);
    float f6 = MathHelper.sin(-f1 * 0.017453292F);
    float f7 = f4 * f5;
    float f8 = f3 * f5;
    double d3 = range;
    if (player instanceof EntityPlayerMP) {
      d3 = ((EntityPlayerMP) player).theItemInWorldManager.getBlockReachDistance();
    }
    Vec3 vec31 = vec3.addVector((double) f7 * d3, (double) f6 * d3, (double) f8 * d3);
    return world.rayTraceBlocks(vec3, vec31, par3, !par3, par3);
  }
}
