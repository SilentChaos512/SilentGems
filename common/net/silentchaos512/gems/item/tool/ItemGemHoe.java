package net.silentchaos512.gems.item.tool;

import java.util.List;
import java.util.Map;

import com.google.common.collect.Multimap;

import net.minecraft.block.Block;
import net.minecraft.block.BlockDirt;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemHoe;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.silentchaos512.gems.SilentGems;
import net.silentchaos512.gems.api.ITool;
import net.silentchaos512.gems.api.lib.EnumMaterialTier;
import net.silentchaos512.gems.config.ConfigOptionToolClass;
import net.silentchaos512.gems.config.GemsConfig;
import net.silentchaos512.gems.init.ModItems;
import net.silentchaos512.gems.item.ToolRenderHelper;
import net.silentchaos512.gems.lib.Names;
import net.silentchaos512.gems.util.ToolHelper;
import net.silentchaos512.lib.registry.IRegistryObject;
import net.silentchaos512.lib.registry.RecipeMaker;
import net.silentchaos512.lib.util.ItemHelper;
import net.silentchaos512.lib.util.StackHelper;

public class ItemGemHoe extends ItemHoe implements IRegistryObject, ITool {

  public ItemGemHoe() {

    super(ToolHelper.FAKE_MATERIAL);
    setUnlocalizedName(SilentGems.RESOURCE_PREFIX + Names.HOE);
    setNoRepair();
  }

  public ItemStack constructTool(boolean supercharged, ItemStack material) {

    return constructTool(supercharged, material, material, material);
  }

  public ItemStack constructTool(boolean supercharged, ItemStack... materials) {

    ItemStack rod = supercharged ? ModItems.craftingMaterial.toolRodGold
        : new ItemStack(Items.STICK);
    return ToolHelper.constructTool(this, rod, materials);
  }

  // 1.11.2
  @Override
  public EnumActionResult onItemUse(EntityPlayer player, World world, BlockPos pos, EnumHand hand,
      EnumFacing side, float hitX, float hitY, float hitZ) {

    return compatOnItemUse(player.getHeldItem(hand), player, world, pos, hand, side, hitX, hitY,
        hitZ);
  }

  public EnumActionResult compatOnItemUse(ItemStack stack, EntityPlayer player, World world,
      BlockPos pos, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {

    if (ToolHelper.isBroken(stack)) {
      return EnumActionResult.PASS;
    }

    EnumActionResult result = compatTillGround(stack, player, world, pos, hand, side, hitX, hitY,
        hitZ);
    int tilledCount = result == EnumActionResult.SUCCESS ? 1 : 0;

    // Super hoe area till?
    boolean isSuper = ToolHelper.getToolTier(stack).ordinal() >= EnumMaterialTier.SUPER.ordinal();
    if (result == EnumActionResult.SUCCESS && player.isSneaking() && isSuper) {
      BlockPos[] array = new BlockPos[] { new BlockPos(pos.getX() - 1, pos.getY(), pos.getZ() - 1),
          new BlockPos(pos.getX() - 1, pos.getY(), pos.getZ() + 0),
          new BlockPos(pos.getX() - 1, pos.getY(), pos.getZ() + 1),
          new BlockPos(pos.getX() + 0, pos.getY(), pos.getZ() - 1),
          new BlockPos(pos.getX() + 0, pos.getY(), pos.getZ() + 1),
          new BlockPos(pos.getX() + 1, pos.getY(), pos.getZ() - 1),
          new BlockPos(pos.getX() + 1, pos.getY(), pos.getZ() + 0),
          new BlockPos(pos.getX() + 1, pos.getY(), pos.getZ() + 1) };
      for (BlockPos blockpos : array) {
        if (compatTillGround(stack, player, world, blockpos, hand, side, hitX, hitY,
            hitZ) == EnumActionResult.SUCCESS) {
          ++tilledCount;
        }
      }
    }

    if (tilledCount > 0) {
      ToolHelper.incrementStatBlocksTilled(stack, tilledCount);
    }

    return result;
  }

  // Copied from ItemHoe#onItemUse
  public EnumActionResult compatTillGround(ItemStack itemstack, EntityPlayer player, World worldIn,
      BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {

    if (!player.canPlayerEdit(pos.offset(facing), facing, itemstack)) {
      return EnumActionResult.FAIL;
    } else {
      int hook = net.minecraftforge.event.ForgeEventFactory.onHoeUse(itemstack, player, worldIn,
          pos);
      if (hook != 0)
        return hook > 0 ? EnumActionResult.SUCCESS : EnumActionResult.FAIL;

      IBlockState iblockstate = worldIn.getBlockState(pos);
      Block block = iblockstate.getBlock();

      if (facing != EnumFacing.DOWN && worldIn.isAirBlock(pos.up())) {
        if (block == Blocks.GRASS || block == Blocks.GRASS_PATH) {
          this.setBlock(itemstack, player, worldIn, pos, Blocks.FARMLAND.getDefaultState());
          return EnumActionResult.SUCCESS;
        }

        if (block == Blocks.DIRT) {
          switch ((BlockDirt.DirtType) iblockstate.getValue(BlockDirt.VARIANT)) {
            case DIRT:
              this.setBlock(itemstack, player, worldIn, pos, Blocks.FARMLAND.getDefaultState());
              return EnumActionResult.SUCCESS;
            case COARSE_DIRT:
              this.setBlock(itemstack, player, worldIn, pos, Blocks.DIRT.getDefaultState()
                  .withProperty(BlockDirt.VARIANT, BlockDirt.DirtType.DIRT));
              return EnumActionResult.SUCCESS;
          }
        }
      }

      return EnumActionResult.PASS;
    }
  }

  // ===============
  // ITool overrides
  // ===============

  public ConfigOptionToolClass getConfig() {

    return GemsConfig.hoe;
  }

  @Override
  public ItemStack constructTool(ItemStack rod, ItemStack... materials) {

    if (getConfig().isDisabled)
      return StackHelper.empty();
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

    return -4.0f;
  }

  @Override
  public float getBaseMeleeSpeedModifier() {

    return 1.0f;
  }

  // ==============
  // Item overrides
  // ==============

  @Override
  public boolean onBlockStartBreak(ItemStack stack, BlockPos pos, EntityPlayer player) {

    boolean canceled = super.onBlockStartBreak(stack, pos, player);
    if (!canceled) {
      ToolHelper.onBlockStartBreak(stack, pos, player);
    }
    return canceled;
  }

  @Override
  public Multimap<String, AttributeModifier> getAttributeModifiers(EntityEquipmentSlot slot,
      ItemStack stack) {

    return ToolHelper.getAttributeModifiers(slot, stack);
  }

  @Override
  public int getMaxDamage(ItemStack stack) {

    return ToolHelper.getMaxDamage(stack);
  }

  // @Override
  // public int getColorFromItemStack(ItemStack stack, int pass) {
  //
  // return ToolRenderHelper.getInstance().getColorFromItemStack(stack, pass);
  // }

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
  public EnumRarity getRarity(ItemStack stack) {

    return ToolRenderHelper.instance.getRarity(stack);
  }

  @Override
  public int getItemEnchantability(ItemStack stack) {

    return ToolHelper.getItemEnchantability(stack);
  }

  @Override
  public boolean hitEntity(ItemStack stack, EntityLivingBase entity1, EntityLivingBase entity2) {

    return ToolHelper.hitEntity(stack, entity1, entity2);
  }

  @Override
  public boolean getIsRepairable(ItemStack stack1, ItemStack stack2) {

    return ToolHelper.getIsRepairable(stack1, stack2);
  }

  @Override
  public void onUpdate(ItemStack tool, World world, Entity entity, int itemSlot,
      boolean isSelected) {

    ToolHelper.onUpdate(tool, world, entity, itemSlot, isSelected);
  }

  // ===============
  // IRegistryObject
  // ===============

  @Override
  public void addRecipes(RecipeMaker recipes) {

    if (!getConfig().isDisabled)
      ToolHelper.addExampleRecipe(this, "gg", " s", " s");
  }

  @Override
  public void addOreDict() {

  }

  @Override
  public String getName() {

    return Names.HOE;
  }

  @Override
  public String getFullName() {

    return getModId() + ":" + getName();
  }

  @Override
  public String getModId() {

    return SilentGems.MODID;
  }

  @Override
  public void getModels(Map<Integer, ModelResourceLocation> models) {

    models.put(0, ToolRenderHelper.SMART_MODEL);
  }

  @Override
  public boolean registerModels() {

    return false;
  }

  // =================================
  // Cross Compatibility (MC 10/11/12)
  // =================================

  @Override
  public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean advanced) {

    ToolRenderHelper.getInstance().clAddInformation(stack, player.world, list, advanced);
  }

  // getSubItems 1.10.2
  public void func_150895_a(Item item, CreativeTabs tab, List<ItemStack> list) {

    clGetSubItems(item, tab, list);
  }

  @Override
  public void getSubItems(Item item, CreativeTabs tab, NonNullList<ItemStack> list) {

    clGetSubItems(this, tab, list);
  }

  protected void clGetSubItems(Item item, CreativeTabs tab, List<ItemStack> list) {

    if (!ItemHelper.isInCreativeTab(item, tab))
      return;

    list.addAll(ToolHelper.getSubItems(item, 2));
  }

  // onItemUse 1.10.2
  public EnumActionResult func_180614_a(ItemStack stack, EntityPlayer player, World world,
      BlockPos pos, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {

    return compatOnItemUse(stack, player, world, pos, hand, side, hitX, hitY, hitZ);
  }
}
