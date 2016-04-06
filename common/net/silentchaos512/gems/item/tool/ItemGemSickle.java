package net.silentchaos512.gems.item.tool;

import java.util.List;

import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;
import com.google.common.collect.Sets;

import net.minecraft.block.Block;
import net.minecraft.block.IGrowable;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Enchantments;
import net.minecraft.init.Items;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemTool;
import net.minecraft.network.play.server.SPacketBlockChange;
import net.minecraft.server.management.PlayerInteractionManager;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldSettings;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.silentchaos512.gems.SilentGems;
import net.silentchaos512.gems.api.ITool;
import net.silentchaos512.gems.item.ModItems;
import net.silentchaos512.gems.item.ToolRenderHelper;
import net.silentchaos512.gems.lib.EnumGem;
import net.silentchaos512.gems.lib.Names;
import net.silentchaos512.gems.util.ToolHelper;
import net.silentchaos512.lib.registry.IRegistryObject;

public class ItemGemSickle extends ItemTool implements IRegistryObject, ITool {

  public static final Material[] effectiveMaterials = new Material[] { Material.cactus,
      Material.leaves, Material.plants, Material.vine, Material.web };

  private List<ItemStack> subItems = null;

  public ItemGemSickle() {

    super(ToolMaterial.DIAMOND, Sets.newHashSet(new Block[] {}));
    setCreativeTab(SilentGems.instance.creativeTab);
  }

  public ItemStack constructTool(boolean supercharged, ItemStack material) {

    return constructTool(supercharged, material, material, material);
  }

  public ItemStack constructTool(boolean supercharged, ItemStack... materials) {

    ItemStack rod = supercharged ? ModItems.craftingMaterial.getStack(Names.ORNATE_STICK_GOLD)
        : new ItemStack(Items.stick);
    return ToolHelper.constructTool(this, rod, materials);
  }

  // ===============
  // ITool overrides
  // ===============

  @Override
  public ItemStack constructTool(ItemStack rod, ItemStack... materials) {

    return ToolHelper.constructTool(this, rod, materials);
  }

  @Override
  public float getMeleeDamage(ItemStack tool) {

    return getBaseMeleeDamageModifier() + ToolHelper.getMeleeDamage(tool);
  }

  @Override
  public float getMagicDamage(ItemStack tool) {

    return 0.0f;
  }

  @Override
  public float getBaseMeleeDamageModifier() {

    return 2.0f;
  }

  @Override
  public float getBaseMeleeSpeedModifier() {

    return -1.8f;
  }

  // ==============
  // Item overrides
  // ==============

  @Override
  public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean advanced) {

    ToolRenderHelper.getInstance().addInformation(stack, player, list, advanced);
  }

  @Override
  public void getSubItems(Item item, CreativeTabs tab, List list) {

    if (subItems == null) {
      subItems = ToolHelper.getSubItems(item, 3);
    }
    list.addAll(subItems);
  }

  @Override
  public EnumActionResult onItemUse(ItemStack stack, EntityPlayer player, World world, BlockPos pos,
      EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {

    if (ToolHelper.isBroken(stack)) {
      return EnumActionResult.PASS;
    }

    boolean flag = false;

    IBlockState state = world.getBlockState(pos);
    if (state.getBlock() instanceof IGrowable) {
      // Harvest a group of fully grown crops!
      final int radius = 2;
      BlockPos targetPos;
      Block block;

      for (int z = pos.getZ() - radius; z <= pos.getZ() + radius; ++z) {
        for (int x = pos.getX() - radius; x <= pos.getX() + radius; ++x) {
          targetPos = new BlockPos(x, pos.getY(), z);
          state = world.getBlockState(targetPos);
          block = state.getBlock();

          if (block instanceof IGrowable) {
            IGrowable crop = (IGrowable) block;
            if (!crop.canGrow(world, targetPos, state, world.isRemote)) {
              // Fully grown crop, get the drops
              List<ItemStack> drops = block.getDrops(world, targetPos, state,
                  EnchantmentHelper.getEnchantmentLevel(Enchantments.fortune, stack));

              // Spawn drops in world, remove first seed.
              boolean foundSeed = false;
              for (ItemStack dropStack : drops) {
                if (!foundSeed && dropStack.getItem() instanceof IPlantable) {
                  IPlantable seed = (IPlantable) dropStack.getItem();
                  if (seed.getPlant(world, targetPos) == block.getDefaultState()) {
                    foundSeed = true;
                  }
                } else {
                  Block.spawnAsEntity(world, targetPos, dropStack);
                }
              }
              // Reset to default state.
              world.setBlockState(targetPos, block.getDefaultState(), 2);
              flag = true;
            }
          }
        }
      }
    }

    if (flag) {
      stack.damageItem(1, player);
    }
    return flag ? EnumActionResult.SUCCESS : EnumActionResult.PASS;
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
  public boolean onBlockStartBreak(ItemStack sickle, BlockPos pos, EntityPlayer player) {

    if (ToolHelper.isBroken(sickle)) {
      return false;
    }

    IBlockState state = player.worldObj.getBlockState(pos);
    Block block = state.getBlock();

    if (!isEffectiveOnMaterial(block.getMaterial(state))) {
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

    ToolHelper.incrementStatBlocksMined(sickle, blocksBroken);
    return super.onBlockStartBreak(sickle, pos, player);
  }

  @Override
  public boolean onBlockDestroyed(ItemStack stack, World world, IBlockState state, BlockPos pos,
      EntityLivingBase entityLiving) {

    return ToolHelper.onBlockDestroyed(stack, world, state, pos, entityLiving);
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
      if (mat == state.getMaterial()) {
        effectiveOnBlock = true;
        break;
      }
    }
    if (!effectiveOnBlock) {
      return false;
    }

    int xpDropped = ForgeHooks.onBlockBreakEvent(world,
        playerMP.interactionManager.getGameType(), playerMP, pos);
    boolean canceled = xpDropped == -1;
    if (canceled) {
      return false;
    }

    if (playerMP.capabilities.isCreativeMode) {
      block.onBlockHarvested(world, pos, state, player);
      if (block.removedByPlayer(state, world, pos, playerMP, false)) {
        block.onBlockDestroyedByPlayer(world, pos, state);
      }
      if (!world.isRemote) {
        playerMP.playerNetServerHandler.sendPacket(new SPacketBlockChange(world, pos));
      }
      return true;
    }

    if (!world.isRemote) {
      block.onBlockHarvested(world, pos, state, playerMP);

      if (block.removedByPlayer(state, world, pos, playerMP, true)) {
        block.onBlockDestroyedByPlayer(world, pos, state);
        block.harvestBlock(world, player, pos, state, null, sickle);
        block.dropXpOnBlockBreak(world, pos, xpDropped);
      }

      playerMP.playerNetServerHandler.sendPacket(new SPacketBlockChange(world, pos));
    } else {
      world.playAuxSFX(2001, pos, Block.getIdFromBlock(block) /* + (meta << 12) */); // TODO: Why the meta thing?
      if (block.removedByPlayer(state, world, pos, playerMP, true)) {
        block.onBlockDestroyedByPlayer(world, pos, state);
      }

      sickle.onBlockDestroyed(world, state, pos, playerMP);
      if (sickle.stackSize == 0) {
        // FIXME ?
//        player.destroyCurrentEquippedItem();
      }
    }

    return true;
  }

  @Override
  public int getMaxDamage(ItemStack stack) {

    return ToolHelper.getMaxDamage(stack);
  }

//  @Override
//  public int getColorFromItemStack(ItemStack stack, int pass) {
//
//    return ToolRenderHelper.getInstance().getColorFromItemStack(stack, pass);
//  }

  @Override
  public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack,
      boolean slotChanged) {

    return ToolRenderHelper.instance.shouldCauseReequipAnimation(oldStack, newStack, slotChanged);
  }

  @Override
  public boolean hasEffect(ItemStack stack) {

    return ToolRenderHelper.instance.hasEffect(stack);
  }

  @Override
  public int getItemEnchantability(ItemStack stack) {

    return ToolHelper.getItemEnchantability(stack);
  }

  // ==================
  // ItemTool overrides
  // ==================

  @Override
  public float getStrVsBlock(ItemStack stack, IBlockState state) {

    for (Material material : effectiveMaterials) {
      if (state.getMaterial() == material) {
        return this.efficiencyOnProperMaterial;
      }
    }
    return super.getStrVsBlock(stack, state);
  }

  @Override
  public boolean canHarvestBlock(IBlockState state) {

    return state.getMaterial() == Material.web;
  }

  @Override
  public int getHarvestLevel(ItemStack stack, String toolClass) {

    if (super.getHarvestLevel(stack, toolClass) < 0) {
      SilentGems.instance.logHelper.derp();
      return 0;
    }
    return ToolHelper.getHarvestLevel(stack);
  }

  @Override
  public Multimap<String, AttributeModifier> getAttributeModifiers(EntityEquipmentSlot slot, ItemStack stack) {

    return ToolHelper.getAttributeModifiers(slot, stack);
  }

  @Override
  public boolean hitEntity(ItemStack stack, EntityLivingBase entity1, EntityLivingBase entity2) {

    return ToolHelper.hitEntity(stack, entity1, entity2);
  }

  @Override
  public boolean getIsRepairable(ItemStack stack1, ItemStack stack2) {

    return ToolHelper.getIsRepairable(stack1, stack2);
  }

  // ===============
  // IRegistryObject
  // ===============

  @Override
  public void addRecipes() {

    String line1 = " g";
    String line2 = "gg";
    String line3 = "s ";
    ItemStack flint = new ItemStack(Items.flint);
    GameRegistry.addRecipe(new ShapedOreRecipe(constructTool(false, flint), line1, line2, line3,
        'g', flint, 's', "stickWood"));
    for (EnumGem gem : EnumGem.values()) {
      GameRegistry.addRecipe(new ShapedOreRecipe(constructTool(false, gem.getItem()), line1, line2,
          line3, 'g', gem.getItem(), 's', "stickWood"));
      GameRegistry.addRecipe(new ShapedOreRecipe(constructTool(true, gem.getItemSuper()), line1,
          line2, line3, 'g', gem.getItemSuper(), 's',
          ModItems.craftingMaterial.getStack(Names.ORNATE_STICK_GOLD)));
    }
  }

  @Override
  public void addOreDict() {

  }

  @Override
  public String getName() {

    return Names.SICKLE;
  }

  @Override
  public String getFullName() {

    return getModId() + ":" + getName();
  }

  @Override
  public String getModId() {

    return SilentGems.MOD_ID;
  }

  @Override
  public List<ModelResourceLocation> getVariants() {

    return Lists.newArrayList(ToolRenderHelper.SMART_MODEL);
  }

  @Override
  public boolean registerModels() {

    return false;
  }
}
