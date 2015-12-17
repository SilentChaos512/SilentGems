package net.silentchaos512.gems.item.tool;

import java.util.List;

import com.google.common.collect.Sets;

import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemTool;
import net.minecraft.network.play.server.S23PacketBlockChange;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.common.IShearable;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.silentchaos512.gems.SilentGems;
import net.silentchaos512.gems.client.renderers.tool.ToolRenderHelper;
import net.silentchaos512.gems.core.registry.SRegistry;
import net.silentchaos512.gems.core.util.LocalizationHelper;
import net.silentchaos512.gems.core.util.ToolHelper;
import net.silentchaos512.gems.item.CraftingMaterial;
import net.silentchaos512.gems.item.ModItems;
import net.silentchaos512.gems.lib.Names;
import net.silentchaos512.gems.material.ModMaterials;

public class GemSickle extends ItemTool {

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
  public float func_150893_a(ItemStack stack, Block block) {

    for (Material material : effectiveMaterials) {
      if (block.getMaterial() == material) {
        return this.efficiencyOnProperMaterial;
      }
    }
    return super.func_150893_a(stack, block);
  }

  @Override
  public boolean func_150897_b(Block block) {

    return block.getMaterial() == Material.web;
  }

  public int getGemId() {

    return gemId;
  }

  @Override
  public IIcon getIcon(ItemStack stack, int pass) {

    return ToolRenderHelper.instance.getIcon(stack, pass, gemId, supercharged);
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
  public int getRenderPasses(int meta) {

    return ToolRenderHelper.RENDER_PASS_COUNT;
  }

  @Override
  public String getUnlocalizedName(ItemStack stack) {

    return LocalizationHelper.TOOL_PREFIX + "Sickle" + gemId + (supercharged ? "Plus" : "");
  }

  @Override
  public boolean hasEffect(ItemStack stack, int pass) {

    return ToolRenderHelper.instance.hasEffect(stack, pass);
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
  public boolean onBlockDestroyed(ItemStack stack, World world, Block block, int x, int y, int z,
      EntityLivingBase entity) {

    if (block.getMaterial() != Material.leaves && block != Blocks.web && block != Blocks.tallgrass
        && block != Blocks.vine && block != Blocks.tripwire && !(block instanceof IShearable)) {
      return super.onBlockDestroyed(stack, world, block, x, y, z, entity);
    } else {
      return true;
    }
  }

  @Override
  public boolean onBlockStartBreak(ItemStack sickle, int x, int y, int z, EntityPlayer player) {

    Block block = player.worldObj.getBlock(x, y, z);
    int meta = player.worldObj.getBlockMetadata(x, y, z);

    if (!isEffectiveOnMaterial(block.getMaterial())) {
      ToolHelper.incrementStatBlocksMined(sickle, 1);
      return false;
    }

    int xRange = 4;
    int zRange = 4;
    int blocksBroken = 1;

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
    return super.onBlockStartBreak(sickle, x, y, z, player);
  }

  private boolean breakExtraBlock(ItemStack sickle, World world, int x, int y, int z, int sideHit,
      EntityPlayer player, int refX, int refY, int refZ) {

    if (world.isAirBlock(x, y, z) || !(player instanceof EntityPlayerMP)) {
      return false;
    }

    EntityPlayerMP playerMP = (EntityPlayerMP) player;
    Block block = player.worldObj.getBlock(x, y, z);

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

    BlockEvent.BreakEvent event = ForgeHooks.onBlockBreakEvent(world,
        playerMP.theItemInWorldManager.getGameType(), playerMP, x, y, z);
    if (event.isCanceled()) {
      return false;
    }

    int meta = world.getBlockMetadata(x, y, z);
    if (playerMP.capabilities.isCreativeMode) {
      block.onBlockHarvested(world, x, y, z, meta, player);
      if (block.removedByPlayer(world, playerMP, x, y, z, false)) {
        block.onBlockDestroyedByPlayer(world, x, y, z, meta);
      }
      if (!world.isRemote) {
        playerMP.playerNetServerHandler.sendPacket(new S23PacketBlockChange(x, y, z, world));
      }
      return true;
    }

    player.getCurrentEquippedItem().func_150999_a(world, block, x, y, z, player);

    if (!world.isRemote) {
      block.onBlockHarvested(world, x, y, z, meta, playerMP);

      if (block.removedByPlayer(world, playerMP, x, y, z, true)) {
        block.onBlockDestroyedByPlayer(world, x, y, z, meta);
        block.harvestBlock(world, player, x, y, z, meta);
        block.dropXpOnBlockBreak(world, x, y, z, event.getExpToDrop());
      }

      playerMP.playerNetServerHandler.sendPacket(new S23PacketBlockChange(x, y, z, world));
    } else {
      world.playAuxSFX(2001, x, y, z, Block.getIdFromBlock(block) + (meta << 12));
      if (block.removedByPlayer(world, playerMP, x, y, z, true)) {
        block.onBlockDestroyedByPlayer(world, x, y, z, meta);
      }

      sickle.func_150999_a(world, block, x, y, z, playerMP);
      if (sickle.stackSize == 0) {
        player.destroyCurrentEquippedItem();
      }
    }
    
    return true;
  }

  @Override
  public boolean requiresMultipleRenderPasses() {

    return true;
  }

  @Override
  public void registerIcons(IIconRegister reg) {

    if (gemId >= 0 && gemId < ToolRenderHelper.HEAD_TYPE_COUNT) {
      itemIcon = ToolRenderHelper.instance.sickleIcons.headM[gemId];
    }
  }
  
  @Override
  public boolean hitEntity(ItemStack stack, EntityLivingBase entity1, EntityLivingBase entity2) {

    ToolHelper.hitEntity(stack);
    return super.hitEntity(stack, entity1, entity2);
  }
}
