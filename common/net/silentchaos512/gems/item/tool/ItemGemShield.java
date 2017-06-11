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
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.silentchaos512.gems.SilentGems;
import net.silentchaos512.gems.api.ITool;
import net.silentchaos512.gems.api.tool.part.ToolPart;
import net.silentchaos512.gems.api.tool.part.ToolPartRegistry;
import net.silentchaos512.gems.config.ConfigOptionToolClass;
import net.silentchaos512.gems.config.GemsConfig;
import net.silentchaos512.gems.item.ModItems;
import net.silentchaos512.gems.item.ToolRenderHelper;
import net.silentchaos512.gems.lib.EnumGem;
import net.silentchaos512.gems.lib.Names;
import net.silentchaos512.gems.util.ToolHelper;
import net.silentchaos512.lib.registry.IRegistryObject;
import net.silentchaos512.lib.util.StackHelper;

public class ItemGemShield extends ItemShield implements IRegistryObject, ITool {

  public static final float MIN_BLOCKING_POWER = 0.4f;

  private List<ItemStack> subItems = null;

  // ===========================================
  // = Modified damage blocking and attributes =
  // ===========================================

  // Class to wrap the event handler method. This prevents a crash when running on 1.10.2.
  public static class EventHandler {

    @SubscribeEvent(priority = EventPriority.LOW)
    public void blockExtraDamage(LivingHurtEvent event) {

      ItemGemShield item = ModItems.shield;

      DamageSource source = event.getSource();
      if (source.isUnblockable() || source.isMagicDamage() || source.isExplosion()
          || source.isProjectile() || event.isCanceled())
        return;

      if (!item.shouldBlockDamage(event.getEntityLiving()))
        return;

      EntityPlayer player = (EntityPlayer) event.getEntityLiving();
      ItemStack shield = player.getActiveItemStack();

      float damage = event.getAmount();
      float blockPower = ToolHelper.getBlockingPower(shield);
      damage = damage < 2f ? 1f : damage / 2f;
      event.setAmount(event.getAmount() / Math.max(blockPower, MIN_BLOCKING_POWER));

      if (source.getEntity() != null) {
        source.getEntity().attackEntityFrom(DamageSource.causeThornsDamage(player),
            event.getAmount() + item.getMeleeDamage(shield));
        damage = damage * 1.5f;
      }

      // SilentGems.logHelper.debug(damage, Math.round(damage));
      ToolHelper.attemptDamageTool(shield, Math.round(damage), player);
    }
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

    return Math.max(0, (getBaseMeleeDamageModifier() + ToolHelper.getMeleeDamage(tool)) / 2);
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

    return -3.2f;
  }

  @Override
  public void addRecipes() {

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

  private void addRecipe(ItemStack result, ItemStack head, Object rod) {

    ToolPart part = ToolPartRegistry.fromStack(head);
    if (part != null && !part.isBlacklisted(head))
      GameRegistry.addRecipe(
          new ShapedOreRecipe(result, "gwg", "wrw", " g ", 'g', head, 'w', "plankWood", 'r', rod));
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
  public void getSubItems(Item item, CreativeTabs tab, NonNullList<ItemStack> list) {

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

    return SilentGems.MODID;
  }

  @Override
  public List<ModelResourceLocation> getVariants() {

    // TODO Auto-generated method stub
    return Lists.newArrayList(new ModelResourceLocation(getFullName().toLowerCase(), "inventory"));
  }

  @Override
  public boolean registerModels() {

    // TODO Auto-generated method stub
    return false;
  }

  // ==============================
  // Cross Compatibility (MC 10/11)
  // ==============================

  // getSubItems
  public void func_150895_a(Item item, CreativeTabs tab, List<ItemStack> list) {

    if (subItems == null) {
      subItems = ToolHelper.getSubItems(item, 3);
    }
    list.addAll(subItems);
  }
}
