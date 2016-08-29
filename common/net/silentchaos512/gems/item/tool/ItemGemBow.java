package net.silentchaos512.gems.item.tool;

import java.lang.reflect.Method;
import java.util.List;

import com.google.common.collect.Lists;

import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.init.Enchantments;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.IItemPropertyGetter;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArrow;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.world.World;
import net.silentchaos512.gems.SilentGems;
import net.silentchaos512.gems.api.ITool;
import net.silentchaos512.gems.item.ModItems;
import net.silentchaos512.gems.item.ToolRenderHelper;
import net.silentchaos512.gems.lib.Names;
import net.silentchaos512.gems.util.ToolHelper;
import net.silentchaos512.lib.registry.IRegistryObject;

public class ItemGemBow extends ItemBow implements IRegistryObject, ITool {

  public static final float ENCHANTABILITY_MULTIPLIER = 0.45f;
  public static final ResourceLocation RESOURCE_PULLING = new ResourceLocation("pulling");

  private List<ItemStack> subItems = null;

  public ItemGemBow() {

    setUnlocalizedName(SilentGems.RESOURCE_PREFIX + Names.BOW);

    addPropertyOverride(RESOURCE_PULLING, new IItemPropertyGetter() {

      @Override
      public float apply(ItemStack stack, World worldIn, EntityLivingBase entityIn) {

        if (entityIn == null)
          return 0f;
        ItemStack itemstack = entityIn.getActiveItemStack();
        return itemstack != null && itemstack.getItem() == ModItems.bow
            ? (stack.getMaxItemUseDuration() - entityIn.getItemInUseCount()) / getDrawDelay(stack)
            : 0f;
      }
    });
  }

  // ===================
  // = ITool overrides =
  // ===================

  @Override
  public ItemStack constructTool(ItemStack rod, ItemStack... materials) {

    if (materials.length == 1)
      return constructTool(rod, materials[0], materials[0], materials[0]);
    return ToolHelper.constructTool(this, rod, materials);
  }

  @Override
  public float getMeleeDamage(ItemStack tool) {

    return 0;
  }

  @Override
  public float getMagicDamage(ItemStack tool) {

    return 0;
  }

  @Override
  public float getBaseMeleeDamageModifier() {

    return 0;
  }

  @Override
  public float getBaseMeleeSpeedModifier() {

    return 0;
  }

  // =============
  // = Bow stuff =
  // =============

  public float getDrawDelay(ItemStack stack) {

    return 38.4f - 1.4f * ToolHelper.getMeleeSpeed(stack) * ToolHelper.getDigSpeedOnProperMaterial(stack);
  }

  public float getArrowVelocity(ItemStack stack, int charge) {

    float f = charge / getDrawDelay(stack);
    f = (f * f + f * 2f) / 3f;
    return f > 1f ? 1f : f;
  }

  protected ItemStack findAmmo(EntityPlayer player) {

    if (this.isArrow(player.getHeldItem(EnumHand.OFF_HAND))) {
      return player.getHeldItem(EnumHand.OFF_HAND);
    } else if (this.isArrow(player.getHeldItem(EnumHand.MAIN_HAND))) {
      return player.getHeldItem(EnumHand.MAIN_HAND);
    } else {
      for (int i = 0; i < player.inventory.getSizeInventory(); ++i) {
        ItemStack itemstack = player.inventory.getStackInSlot(i);

        if (this.isArrow(itemstack)) {
          return itemstack;
        }
      }

      return null;
    }
  }

  // Same as vanilla bow, except it can be fired without arrows with infinity.
  @Override
  public ActionResult<ItemStack> onItemRightClick(ItemStack stack, World world, EntityPlayer player,
      EnumHand hand) {

    boolean hasAmmo = findAmmo(player) != null
        || EnchantmentHelper.getEnchantmentLevel(Enchantments.INFINITY, stack) > 0;

    ActionResult<ItemStack> ret = net.minecraftforge.event.ForgeEventFactory.onArrowNock(stack,
        world, player, hand, hasAmmo);
    if (ret != null)
      return ret;

    if (!player.capabilities.isCreativeMode && !hasAmmo) {
      return !hasAmmo ? new ActionResult(EnumActionResult.FAIL, stack)
          : new ActionResult(EnumActionResult.PASS, stack);
    } else {
      player.setActiveHand(hand);
      return new ActionResult(EnumActionResult.SUCCESS, stack);
    }
  }

  @Override
  public void onPlayerStoppedUsing(ItemStack stack, World worldIn, EntityLivingBase entityLiving,
      int timeLeft) {

    if (entityLiving instanceof EntityPlayer) {
      EntityPlayer player = (EntityPlayer) entityLiving;
      boolean flag = player.capabilities.isCreativeMode
          || EnchantmentHelper.getEnchantmentLevel(Enchantments.INFINITY, stack) > 0;
      ItemStack ammo = this.findAmmo(player);

      int i = this.getMaxItemUseDuration(stack) - timeLeft;
      i = net.minecraftforge.event.ForgeEventFactory.onArrowLoose(stack, worldIn,
          (EntityPlayer) entityLiving, i, ammo != null || flag);
      if (i < 0)
        return;

      if (ammo != null || flag) {
        if (ammo == null) {
          ammo = new ItemStack(Items.ARROW);
        }

        float velocity = getArrowVelocity(stack, i);
        SilentGems.logHelper.debug("Bow use: ", i, getDrawDelay(stack), velocity);

        if ((double) velocity >= 0.1D) {
          boolean flag1 = player.capabilities.isCreativeMode
              || (ammo.getItem() instanceof ItemArrow
                  ? ((ItemArrow) ammo.getItem()).isInfinite(ammo, stack, player)
                  : false);

          if (!worldIn.isRemote) {
            ItemArrow itemarrow = (ItemArrow) ((ItemArrow) (ammo.getItem() instanceof ItemArrow
                ? ammo.getItem() : Items.ARROW));
            EntityArrow entityarrow = itemarrow.createArrow(worldIn, ammo, player);
            entityarrow.setAim(player, player.rotationPitch, player.rotationYaw,
                0.0F, velocity * 3.0F, 1.0F);

            if (velocity == 1.0F) {
              entityarrow.setIsCritical(true);
            }

            int power = EnchantmentHelper.getEnchantmentLevel(Enchantments.POWER, stack);

            if (power > 0) {
              // TODO: Damage modifier?
              entityarrow.setDamage(entityarrow.getDamage() + (double) power * 0.5D + 0.5D);
            }

            int k = EnchantmentHelper.getEnchantmentLevel(Enchantments.PUNCH, stack);

            if (k > 0) {
              entityarrow.setKnockbackStrength(k);
            }

            if (EnchantmentHelper.getEnchantmentLevel(Enchantments.FLAME, stack) > 0) {
              entityarrow.setFire(100);
            }

            stack.damageItem(1, player);

            if (flag1) {
              entityarrow.pickupStatus = EntityArrow.PickupStatus.CREATIVE_ONLY;
            }

            worldIn.spawnEntityInWorld(entityarrow);
          }

          worldIn.playSound((EntityPlayer) null, player.posX, player.posY,
              player.posZ, SoundEvents.ENTITY_ARROW_SHOOT, SoundCategory.NEUTRAL, 1.0F,
              1.0F / (itemRand.nextFloat() * 0.4F + 1.2F) + velocity * 0.5F);

          if (!flag1) {
            --ammo.stackSize;

            if (ammo.stackSize == 0) {
              player.inventory.deleteStack(ammo);
            }
          }

          player.addStat(StatList.getObjectUseStats(this));
          // Shots fired statistic
          ToolHelper.incrementStatShotsFired(stack, 1);
        }
      }
    }
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

    if (subItems == null)
      subItems = ToolHelper.getSubItems(item, 3);
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

    return Math.round(ToolHelper.getItemEnchantability(stack) * ENCHANTABILITY_MULTIPLIER);
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
  public void addOreDict() {

  }

  @Override
  public void addRecipes() {

    // TODO Auto-generated method stub
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
  public String getName() {

    return Names.BOW;
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
