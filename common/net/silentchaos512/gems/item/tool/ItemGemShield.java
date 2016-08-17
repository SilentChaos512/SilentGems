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
import net.minecraft.item.EnumAction;
import net.minecraft.item.Item;
import net.minecraft.item.ItemShield;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.silentchaos512.gems.SilentGems;
import net.silentchaos512.gems.api.ITool;
import net.silentchaos512.gems.item.ToolRenderHelper;
import net.silentchaos512.gems.lib.EnumGem;
import net.silentchaos512.gems.lib.Names;
import net.silentchaos512.gems.util.ToolHelper;
import net.silentchaos512.lib.registry.IRegistryObject;

public class ItemGemShield extends ItemShield implements IRegistryObject, ITool {

  private List<ItemStack> subItems = null;

  // ===========================================
  // = Modified damage blocking and attributes =
  // ===========================================

  @SubscribeEvent(priority = EventPriority.LOW)
  public void blockExtraDamage(LivingHurtEvent event) {

    DamageSource source = event.getSource();
    if (source.isUnblockable() || source.isMagicDamage() || source.isExplosion()
        || source.isProjectile() || event.isCanceled())
      return;

    if (!shouldBlockDamage(event.getEntityLiving()))
      return;

    EntityPlayer player = (EntityPlayer) event.getEntityLiving();
    ItemStack shield = player.getActiveItemStack();

    float damage = event.getAmount();
    float blockPower = ToolHelper.getBlockingPower(shield);
    damage = damage < 2f ? 1f : damage / 2f;
    event.setAmount(event.getAmount() / Math.max(blockPower, 0.4f));

    if (source.getEntity() != null) {
      source.getEntity().attackEntityFrom(DamageSource.causeThornsDamage(player),
          getMeleeDamage(shield));
      damage = damage * 1.5f;
    }

    SilentGems.logHelper.debug(damage, Math.round(damage));
    ToolHelper.attemptDamageTool(shield, Math.round(damage), player);
  }

  protected boolean shouldBlockDamage(EntityLivingBase entityLiving) {

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

  public ItemStack constructShield(ItemStack... materials) {

    return constructTool(null, materials);
  }

  @Override
  public ItemStack constructTool(ItemStack rod, ItemStack... materials) {

    if (materials.length == 1)
      return constructTool(rod, materials[0], materials[0], materials[0]);
    return ToolHelper.constructTool(this, rod, materials);
  }

  @Override
  public float getMeleeDamage(ItemStack tool) {

    return Math.max(0, (getBaseMeleeDamageModifier() + ToolHelper.getMeleeDamage(tool)) / 2);
  }

  @Override
  public float getMagicDamage(ItemStack tool) {

    return 0.0f;
  }

  @Override
  public float getBaseMeleeDamageModifier() {

    return -2.0f;
  }

  @Override
  public float getBaseMeleeSpeedModifier() {

    return -3.2f;
  }

  @Override
  public void addRecipes() {

    String line1 = "gwg";
    String line2 = "www";
    String line3 = " g ";
    ItemStack flint = new ItemStack(Items.FLINT);

    GameRegistry.addRecipe(new ShapedOreRecipe(constructShield(flint), line1, line2, line3, 'g',
        flint, 'w', "plankWood"));

    for (EnumGem gem : EnumGem.values()) {
      GameRegistry.addRecipe(new ShapedOreRecipe(constructShield(gem.getItem()), line1, line2,
          line3, 'g', gem.getItem(), 'w', "plankWood"));
      GameRegistry.addRecipe(new ShapedOreRecipe(constructShield(gem.getItemSuper()), line1, line2,
          line3, 'g', gem.getItemSuper(), 'w', "plankWood"));
    }
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
  public int getItemEnchantability(ItemStack stack) {

    return ToolHelper.getItemEnchantability(stack);
  }

  @Override
  public void onUpdate(ItemStack tool, World world, Entity entity, int itemSlot,
      boolean isSelected) {

    ToolHelper.onUpdate(tool, world, entity, itemSlot, isSelected);
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

    return SilentGems.MOD_ID;
  }

  @Override
  public List<ModelResourceLocation> getVariants() {

    // TODO Auto-generated method stub
    return Lists.newArrayList(new ModelResourceLocation(getFullName(), "inventory"));
  }

  @Override
  public boolean registerModels() {

    // TODO Auto-generated method stub
    return false;
  }
}
