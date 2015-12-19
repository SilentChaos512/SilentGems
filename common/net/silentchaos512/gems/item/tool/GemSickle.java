package net.silentchaos512.gems.item.tool;

import java.util.List;

import com.google.common.collect.Sets;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemTool;
import net.minecraft.network.play.server.S23PacketBlockChange;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.common.IShearable;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.silentchaos512.gems.SilentGems;
import net.silentchaos512.gems.client.renderers.tool.ToolRenderHelper;
import net.silentchaos512.gems.core.registry.IHasVariants;
import net.silentchaos512.gems.core.util.LocalizationHelper;
import net.silentchaos512.gems.core.util.ToolHelper;
import net.silentchaos512.gems.item.CraftingMaterial;
import net.silentchaos512.gems.lib.Names;

public class GemSickle extends ItemTool implements IHasVariants {

  public final int gemId;
  public final boolean supercharged;

  public static final Material[] effectiveMaterials = new Material[] { Material.cactus,
      Material.leaves, Material.plants, Material.vine, Material.web };

  public GemSickle(ToolMaterial toolMaterial, int gemId, boolean supercharged) {

    super(2.0f, toolMaterial, Sets.newHashSet(new Block[] {}));

    this.gemId = gemId;
    this.supercharged = supercharged;
    this.setMaxDamage(toolMaterial.getMaxUses());
    addRecipe(new ItemStack(this), gemId, supercharged);
    this.setCreativeTab(SilentGems.tabSilentGems);
  }
  
  @Override
  public String[] getVariantNames() {

    return ToolRenderHelper.instance.getVariantNames(new ItemStack(this));
  }

  @Override
  public String getName() {

    return ToolRenderHelper.instance.getName(new ItemStack(this));
  }

  @Override
  public String getFullName() {

    return ToolRenderHelper.instance.getFullName(new ItemStack(this));
  }

  @Override
  public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean advanced) {

    ToolHelper.addInformation(stack, player, list, advanced);
  }

  public static void addRecipe(ItemStack tool, int gemId, boolean supercharged) {

    ItemStack material = ToolHelper.getCraftingMaterial(gemId, supercharged);

    if (supercharged) {
      GameRegistry.addRecipe(new ShapedOreRecipe(tool, true, " g", "gg", "s ", 'g', material, 's',
          CraftingMaterial.getStack(Names.ORNATE_STICK)));
    } else {
      GameRegistry.addRecipe(
          new ShapedOreRecipe(tool, true, " g", "gg", "s ", 'g', material, 's', "stickWood"));
    }
  }

  @Override
  public float getStrVsBlock(ItemStack stack, Block block) {

    for (Material material : effectiveMaterials) {
      if (block.getMaterial() == material) {
        return this.efficiencyOnProperMaterial;
      }
    }
    return super.getStrVsBlock(stack, block);
  }

  @Override
  public boolean canHarvestBlock(Block block) {

    return block.getMaterial() == Material.web;
  }

  public int getGemId() {

    return gemId;
  }

  @Override
  public int getMaxDamage(ItemStack stack) {

    return super.getMaxDamage(stack) + ToolHelper.getDurabilityBoost(stack);
  }

  @Override
  public boolean getIsRepairable(ItemStack stack1, ItemStack stack2) {

    return ToolHelper.getIsRepairable(stack1, stack2);
  }

  @Override
  public String getUnlocalizedName(ItemStack stack) {

    return LocalizationHelper.TOOL_PREFIX + "Sickle" + gemId + (supercharged ? "Plus" : "");
  }

  @Override
  public boolean hasEffect(ItemStack stack) {

    return ToolRenderHelper.instance.hasEffect(stack);
  }
  
  @Override
  public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack,
      boolean slotChanged) {

    return ToolRenderHelper.instance.shouldCauseReequipAnimation(oldStack, newStack, slotChanged);
  }


  private boolean isEffectiveOnMaterial(Material material) {

    for (Material m : effectiveMaterials) {
      if (material == m) {
        return true;
      }
    }
    return false;
  }

  @Override
  public boolean onBlockDestroyed(ItemStack stack, World world, Block block, BlockPos pos,
      EntityLivingBase entity) {

    if (block.getMaterial() != Material.leaves && block != Blocks.web && block != Blocks.tallgrass
        && block != Blocks.vine && block != Blocks.tripwire && !(block instanceof IShearable)) {
      return super.onBlockDestroyed(stack, world, block, pos, entity);
    } else {
      return true;
    }
  }

  @Override
  public boolean onBlockStartBreak(ItemStack sickle, BlockPos pos, EntityPlayer player) {

    IBlockState state = player.worldObj.getBlockState(pos);
    Block block = state.getBlock();

    if (!isEffectiveOnMaterial(block.getMaterial())) {
      ToolHelper.incrementStatBlocksMined(sickle, 1);
      return false;
    }

    int xRange = 4;
    int zRange = 4;
    int blocksBroken = 1;

    final int x = pos.getX();
    final int y = pos.getY();
    final int z = pos.getZ();

    for (int xPos = x - xRange; xPos <= x + xRange; ++xPos) {
      for (int zPos = z - zRange; zPos <= z + zRange; ++zPos) {
        if (xPos == x && zPos == z) {
          continue;
        }

        if (breakExtraBlock(sickle, player.worldObj, xPos, y, zPos, 0, player, x, y, z)) {
          ++blocksBroken;
        }
      }
    }

    sickle.damageItem(1, player);
    ToolHelper.incrementStatBlocksMined(sickle, blocksBroken);
    return super.onBlockStartBreak(sickle, pos, player);
  }

  private boolean breakExtraBlock(ItemStack sickle, World world, int x, int y, int z, int sideHit,
      EntityPlayer player, int refX, int refY, int refZ) {

    BlockPos pos = new BlockPos(x, y, z);

    if (world.isAirBlock(pos) || !(player instanceof EntityPlayerMP)) {
      return false;
    }

    EntityPlayerMP playerMP = (EntityPlayerMP) player;
    IBlockState state = player.worldObj.getBlockState(pos);
    Block block = state.getBlock();

    boolean effectiveOnBlock = false;
    for (Material mat : effectiveMaterials) {
      if (mat == block.getMaterial()) {
        effectiveOnBlock = true;
        break;
      }
    }
    if (!effectiveOnBlock) {
      return false;
    }

    int xpDropped = ForgeHooks.onBlockBreakEvent(world,
        playerMP.theItemInWorldManager.getGameType(), playerMP, pos);
    boolean canceled = xpDropped == -1;
    if (canceled) {
      return false;
    }

    if (playerMP.capabilities.isCreativeMode) {
      block.onBlockHarvested(world, pos, state, player);
      if (block.removedByPlayer(world, pos, playerMP, false)) {
        block.onBlockDestroyedByPlayer(world, pos, state);
      }
      if (!world.isRemote) {
        playerMP.playerNetServerHandler.sendPacket(new S23PacketBlockChange(world, pos));
      }
      return true;
    }

    sickle.onBlockDestroyed(world, block, pos, player); // TODO: Was func_150999_a. Is this right?

    if (!world.isRemote) {
      block.onBlockHarvested(world, pos, state, playerMP);

      if (block.removedByPlayer(world, pos, playerMP, true)) {
        block.onBlockDestroyedByPlayer(world, pos, state);
        block.harvestBlock(world, player, pos, state, null);
        block.dropXpOnBlockBreak(world, pos, xpDropped);
      }

      playerMP.playerNetServerHandler.sendPacket(new S23PacketBlockChange(world, pos));
    } else {
      world.playAuxSFX(2001, pos, Block.getIdFromBlock(block) /* + (meta << 12) */); // TODO: Why the meta thing?
      if (block.removedByPlayer(world, pos, playerMP, true)) {
        block.onBlockDestroyedByPlayer(world, pos, state);
      }

      sickle.onBlockDestroyed(world, block, pos, playerMP);
      if (sickle.stackSize == 0) {
        player.destroyCurrentEquippedItem();
      }
    }

    return true;
  }

  @Override
  public boolean hitEntity(ItemStack stack, EntityLivingBase entity1, EntityLivingBase entity2) {

    ToolHelper.hitEntity(stack);
    return super.hitEntity(stack, entity1, entity2);
  }
}
