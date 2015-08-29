package net.silentchaos512.gems.enchantment;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnumEnchantmentType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemBook;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemTool;
import net.minecraft.network.play.server.S23PacketBlockChange;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.StatCollector;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.event.world.BlockEvent;
import net.silentchaos512.gems.core.util.InventoryHelper;
import net.silentchaos512.gems.core.util.LogHelper;
import net.silentchaos512.gems.item.tool.GemAxe;
import net.silentchaos512.gems.item.tool.GemPickaxe;
import net.silentchaos512.gems.lib.Names;

public class EnchantmentAOE extends Enchantment {
  
  public static final float DIG_SPEED_MULTIPLIER = 0.2f;
//  public static final float DIG_SPEED_REDUCTION = 8.0f;

  protected EnchantmentAOE(int par1, int par2, EnumEnchantmentType par3EnumEnchantmentType) {

    super(par1, par2, par3EnumEnchantmentType);
    setName(Names.AOE);
  }

  @Override
  public boolean canApply(ItemStack stack) {

    // This enchantment is for gem tools and books.
    if (InventoryHelper.isGemTool(stack) || stack.getItem() instanceof ItemBook) {
      return stack.isItemStackDamageable() ? true : super.canApply(stack);
    }

    return false;
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
  
  public static boolean isToolEffective(ItemStack tool, Block block, int meta) {
    
    boolean toolEffective = ForgeHooks.isToolEffective(tool, block, meta);
    
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

  public static void tryActivate(ItemStack tool, int x, int y, int z, EntityPlayer player) {

    Block block = player.worldObj.getBlock(x, y, z);
    int meta = player.worldObj.getBlockMetadata(x, y, z);
    
    if (!(tool.getItem() instanceof ItemTool) || block == null) {
      return;
    }
    
    boolean toolEffective = isToolEffective(tool, block, meta);
    if (!toolEffective) {
      return;
    }

    MovingObjectPosition mop = raytraceFromEntity(player.worldObj, player, false, 4.5);
    if (mop == null) {
      return;
    }
    int sideHit = mop.sideHit;

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

    for (int xPos = x - xRange; xPos <= x + xRange; xPos++) {
      for (int yPos = y - yRange; yPos <= y + yRange; yPos++) {
        for (int zPos = z - zRange; zPos <= z + zRange; zPos++) {
          if (xPos == x && yPos == y && zPos == z) {
            continue;
          }

          breakExtraBlock(tool, player.worldObj, xPos, yPos, zPos, sideHit, player, x, y, z);
        }
      }
    }
  }

  public static void breakExtraBlock(ItemStack tool, World world, int x, int y, int z,
      int sidehit, EntityPlayer playerEntity, int refX, int refY, int refZ) {

    if (world.isAirBlock(x, y, z))
      return;

    if (!(playerEntity instanceof EntityPlayerMP))
      return;
    EntityPlayerMP player = (EntityPlayerMP) playerEntity;

    Block block = world.getBlock(x, y, z);
    int meta = world.getBlockMetadata(x, y, z);

    if (!isToolEffective(tool, block, world.getBlockMetadata(x, y, z))) {
      return;
    }

    Block refBlock = world.getBlock(refX, refY, refZ);
    float refStrength = ForgeHooks.blockStrength(refBlock, player, world, refX, refY, refZ);
    float strength = ForgeHooks.blockStrength(block, player, world, x, y, z);

//    LogHelper.list(Block.getIdFromBlock(refBlock), refStrength, strength, refStrength / strength);
    if (!ForgeHooks.canHarvestBlock(block, player, meta) || refStrength / strength > 10f) {
      return;
    }

    BlockEvent.BreakEvent event = ForgeHooks.onBlockBreakEvent(world,
        player.theItemInWorldManager.getGameType(), player, x, y, z);
    if (event.isCanceled())
      return;

    if (player.capabilities.isCreativeMode) {
      block.onBlockHarvested(world, x, y, z, meta, player);
      if (block.removedByPlayer(world, player, x, y, z, false))
        block.onBlockDestroyedByPlayer(world, x, y, z, meta);

      if (!world.isRemote) {
        player.playerNetServerHandler.sendPacket(new S23PacketBlockChange(x, y, z, world));
      }
      return;
    }

    player.getCurrentEquippedItem().func_150999_a(world, block, x, y, z, player);

    if (!world.isRemote) {

      block.onBlockHarvested(world, x, y, z, meta, player);

      if (block.removedByPlayer(world, player, x, y, z, true)) {
        block.onBlockDestroyedByPlayer(world, x, y, z, meta);
        block.harvestBlock(world, player, x, y, z, meta);
        block.dropXpOnBlockBreak(world, x, y, z, event.getExpToDrop());
      }

      player.playerNetServerHandler.sendPacket(new S23PacketBlockChange(x, y, z, world));
    } else {
      world.playAuxSFX(2001, x, y, z, Block.getIdFromBlock(block) + (meta << 12));
      if (block.removedByPlayer(world, player, x, y, z, true)) {
        block.onBlockDestroyedByPlayer(world, x, y, z, meta);
      }

      ItemStack itemstack = player.getCurrentEquippedItem();
      if (itemstack != null) {
        itemstack.func_150999_a(world, block, x, y, z, player);

        if (itemstack.stackSize == 0) {
          player.destroyCurrentEquippedItem();
        }
      }
    }
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
    Vec3 vec3 = Vec3.createVectorHelper(d0, d1, d2);
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
    return world.func_147447_a(vec3, vec31, par3, !par3, par3);
  }
}
