package net.silentchaos512.gems.item.tool;

import java.util.List;
import java.util.Map;

import javax.annotation.Nullable;

import com.google.common.collect.Multimap;

import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.client.util.ITooltipFlag.TooltipFlags;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.EnumAction;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemShield;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.silentchaos512.gems.SilentGems;
import net.silentchaos512.gems.api.ITool;
import net.silentchaos512.gems.api.tool.part.ToolPart;
import net.silentchaos512.gems.api.tool.part.ToolPartRegistry;
import net.silentchaos512.gems.config.ConfigOptionToolClass;
import net.silentchaos512.gems.config.GemsConfig;
import net.silentchaos512.gems.init.ModItems;
import net.silentchaos512.gems.item.ToolRenderHelper;
import net.silentchaos512.gems.lib.EnumGem;
import net.silentchaos512.gems.lib.Names;
import net.silentchaos512.gems.util.ToolHelper;
import net.silentchaos512.lib.registry.IRegistryObject;
import net.silentchaos512.lib.registry.RecipeMaker;
import net.silentchaos512.lib.util.ItemHelper;
import net.silentchaos512.lib.util.StackHelper;

public class ItemGemShield extends ItemShield implements IRegistryObject, ITool {

  public ItemGemShield() {

    setNoRepair();
  }

  public boolean shouldBlockDamage(EntityLivingBase entityLiving) {

    if (!(entityLiving instanceof EntityPlayer))
      return false;

    EntityPlayer player = (EntityPlayer) entityLiving;
    if (!player.isActiveItemStackBlocking() || player.getActiveItemStack().getItem() != this)
      return false;

    return !ToolHelper.isBroken(player.getActiveItemStack());
  }

  @Override
  public Multimap<String, AttributeModifier> getAttributeModifiers(EntityEquipmentSlot slot,
      ItemStack stack) {

    return ToolHelper.getAttributeModifiers(slot, stack);
  }

  // ================
  // = Construction =
  // ================

  public ConfigOptionToolClass getConfig() {

    return GemsConfig.shield;
  }

  @Override
  public ItemStack constructTool(ItemStack rod, ItemStack... materials) {

    if (getConfig().isDisabled)
      return StackHelper.empty();

    if (materials.length == 1)
      return constructTool(rod, materials[0], materials[0], materials[0]);
    return ToolHelper.constructTool(this, rod, materials);
  }

  @Override
  public float getMeleeDamage(ItemStack tool) {

    return Math.max(0, (getMeleeDamageModifier() + ToolHelper.getMeleeDamage(tool)) / 2);
  }

  @Override
  public float getMagicDamage(ItemStack tool) {

    return 0.0f;
  }

  @Override
  public float getMeleeDamageModifier() {

    return -4.0f;
  }

  @Override
  public float getMagicDamageModifier() {

    return 0.0f;
  }

  @Override
  public float getMeleeSpeedModifier() {

    return -3.2f;
  }

  @Override
  public void addRecipes(RecipeMaker recipes) {

    if (getConfig().isDisabled)
      return;

    ItemStack flint = new ItemStack(Items.FLINT);

    ItemStack rodWood = new ItemStack(Items.STICK);
    ItemStack rodIron = ModItems.craftingMaterial.toolRodIron;
    ItemStack rodGold = ModItems.craftingMaterial.toolRodGold;

    addRecipe(constructTool(rodWood, flint), flint, "stickWood");

    for (EnumGem gem : EnumGem.values()) {
      addRecipe(constructTool(rodIron, gem.getItem()), gem.getItem(), rodIron);
      addRecipe(constructTool(rodGold, gem.getItemSuper()), gem.getItemSuper(), rodGold);
    }
  }

  int lastIndex = -1;

  private void addRecipe(ItemStack result, ItemStack head, Object rod) {

    ToolPart part = ToolPartRegistry.fromStack(head);
    if (part != null && !part.isBlacklisted(head))
      SilentGems.registry.recipes.addShapedOre("shield_example" + (++lastIndex), result, "gwg",
          "wrw", " g ", 'g', head, 'w', "plankWood", 'r', rod);
  }

  @Override
  public void addOreDict() {

  }

  // ========================
  // = ItemShield overrides =
  // ========================

  public EnumAction getItemUseAction(ItemStack stack) {

    return ToolHelper.isBroken(stack) ? EnumAction.NONE : EnumAction.BLOCK;
  }

  // ==================
  // = Item overrides =
  // ==================

  @Override
  public int getMaxDamage(ItemStack stack) {

    return ToolHelper.getMaxDamage(stack);
  }

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
  public void onUpdate(ItemStack tool, World world, Entity entity, int itemSlot,
      boolean isSelected) {

    ToolHelper.onUpdate(tool, world, entity, itemSlot, isSelected);
  }

  @Override
  public boolean onEntityItemUpdate(EntityItem entityItem) {

    return ToolHelper.onEntityItemUpdate(entityItem);
  }

  @Override
  public boolean isShield(ItemStack stack, @Nullable EntityLivingBase entity) {

    return true;
  }

  // =============================
  // = IRegistryObject overrides =
  // =============================

  @Override
  public String getName() {

    return Names.SHIELD;
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

    models.put(0, new ModelResourceLocation(getFullName().toLowerCase(), "inventory"));
  }

  @Override
  public boolean registerModels() {

    // TODO Auto-generated method stub
    return false;
  }

  // =================================
  // Cross Compatibility (MC 10/11/12)
  // =================================

  // addInformation 1.10.2/1.11.2
  @SideOnly(Side.CLIENT)
  public void func_77624_a(ItemStack stack, EntityPlayer player, List list, boolean advanced) {

    ToolRenderHelper.getInstance().clAddInformation(stack, player.world, list, advanced);
  }

  @SideOnly(Side.CLIENT)
  @Override
  public void addInformation(ItemStack stack, World world, List list, ITooltipFlag flag) {

    ToolRenderHelper.getInstance().clAddInformation(stack, world, list, flag == TooltipFlags.ADVANCED);
  }

  // getSubItems 1.10.2
  public void func_150895_a(Item item, CreativeTabs tab, List<ItemStack> list) {

    clGetSubItems(item, tab, list);
  }

  // getSubItems 1.11.2
  public void func_150895_a(Item item, CreativeTabs tab, NonNullList<ItemStack> list) {

    clGetSubItems(item, tab, list);
  }

  @Override
  public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> list) {

    clGetSubItems(this, tab, list);
  }

  protected void clGetSubItems(Item item, CreativeTabs tab, List<ItemStack> list) {

    if (!ItemHelper.isInCreativeTab(item, tab))
      return;

    list.addAll(ToolHelper.getSubItems(item, 3));
  }
}
