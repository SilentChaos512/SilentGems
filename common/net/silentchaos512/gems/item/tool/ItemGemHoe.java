package net.silentchaos512.gems.item.tool;

import java.util.List;

import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;

import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.EntityEquipmentSlot;
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
import net.silentchaos512.gems.config.GemsConfig;
import net.silentchaos512.gems.item.ModItems;
import net.silentchaos512.gems.item.ToolRenderHelper;
import net.silentchaos512.gems.lib.EnumGem;
import net.silentchaos512.gems.lib.Names;
import net.silentchaos512.gems.util.ToolHelper;
import net.silentchaos512.lib.registry.IRegistryObject;
import net.silentchaos512.lib.util.StackHelper;

public class ItemGemHoe extends ItemHoe implements IRegistryObject, ITool {

  private List<ItemStack> subItems = null;

  public ItemGemHoe() {

    super(ToolHelper.FAKE_MATERIAL);
    setUnlocalizedName(SilentGems.RESOURCE_PREFIX + Names.HOE);
  }

  public ItemStack constructTool(boolean supercharged, ItemStack material) {

    return constructTool(supercharged, material, material, material);
  }

  public ItemStack constructTool(boolean supercharged, ItemStack... materials) {

    ItemStack rod = supercharged ? ModItems.craftingMaterial.toolRodGold
        : new ItemStack(Items.STICK);
    return ToolHelper.constructTool(this, rod, materials);
  }

  @Override
  public EnumActionResult onItemUse(EntityPlayer player, World world, BlockPos pos, EnumHand hand,
      EnumFacing side, float hitX, float hitY, float hitZ) {

    ItemStack stack = player.getHeldItem(hand);
    if (ToolHelper.isBroken(stack)) {
      return EnumActionResult.PASS;
    }

    EnumActionResult result = super.onItemUse(player, world, pos, hand, side, hitX, hitY, hitZ);

    if (result == EnumActionResult.SUCCESS) {
      ToolHelper.incrementStatBlocksTilled(stack, 1);
    }

    return result;
  }

  // ===============
  // ITool overrides
  // ===============

  @Override
  public ItemStack constructTool(ItemStack rod, ItemStack... materials) {

    if (GemsConfig.TOOL_DISABLE_HOE)
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
  public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean advanced) {

    ToolRenderHelper.getInstance().addInformation(stack, player, list, advanced);
  }

  @Override
  public void getSubItems(Item item, CreativeTabs tab, NonNullList list) {

    if (subItems == null) {
      subItems = ToolHelper.getSubItems(item, 2);
    }
    list.addAll(subItems);
  }

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
  public void addRecipes() {

    if (GemsConfig.TOOL_DISABLE_HOE)
      return;

    String l1 = "gg";
    String l2 = " s";
    String l3 = " s";
    ItemStack flint = new ItemStack(Items.FLINT);
    ItemStack rodGold = ModItems.craftingMaterial.toolRodGold;

    // Flint
    ToolHelper.addRecipe(constructTool(false, flint), l1, l2, l3, flint, "stickWood");
    for (EnumGem gem : EnumGem.values()) {
      // Regular
      ToolHelper.addRecipe(constructTool(false, gem.getItem()), l1, l2, l3, gem.getItem(),
          "stickWood");
      // Super
      ToolHelper.addRecipe(constructTool(true, gem.getItemSuper()), l1, l2, l3, gem.getItemSuper(),
          rodGold);
    }
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
  public List<ModelResourceLocation> getVariants() {

    return Lists.newArrayList(ToolRenderHelper.SMART_MODEL);
  }

  @Override
  public boolean registerModels() {

    return false;
  }

  // ==============================
  // Cross Compatibility (MC 10/11)
  // ==============================

  // getSubItems
  public void func_150895_a(Item item, CreativeTabs tab, List<ItemStack> list) {

    if (subItems == null) {
      subItems = ToolHelper.getSubItems(item, 2);
    }
    list.addAll(subItems);
  }

  // onItemUse
  public EnumActionResult func_180614_a(ItemStack stack, EntityPlayer player, World world, BlockPos pos,
      EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {

    return onItemUse(player, world, pos, hand, side, hitX, hitY, hitZ);
  }
}
