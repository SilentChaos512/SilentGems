package net.silentchaos512.gems.item.tool;

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
import net.minecraft.item.EnumRarity;
import net.minecraft.item.IItemPropertyGetter;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArrow;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
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

public class ItemGemBow extends ItemBow implements IRegistryObject, ITool {

  public static final float ENCHANTABILITY_MULTIPLIER = 0.45f;
  public static final ResourceLocation RESOURCE_PULL = new ResourceLocation("pull");
  public static final ResourceLocation RESOURCE_PULLING = new ResourceLocation("pulling");

  private List<ItemStack> subItems = null;

  public ItemGemBow() {

    setUnlocalizedName(SilentGems.RESOURCE_PREFIX + Names.BOW);

    addPropertyOverride(RESOURCE_PULL, new IItemPropertyGetter() {

      @Override
      public float apply(ItemStack stack, World worldIn, EntityLivingBase entityIn) {

        if (entityIn == null)
          return 0f;
        ItemStack itemstack = entityIn.getActiveItemStack();
        return StackHelper.isValid(itemstack) && ToolHelper.areToolsEqual(stack, itemstack)
            ? (stack.getMaxItemUseDuration() - entityIn.getItemInUseCount()) / getDrawDelay(stack)
            : 0f;
      }
    });
  }

  // ===================
  // = ITool overrides =
  // ===================
  
  public ConfigOptionToolClass getConfig() {

    return GemsConfig.bow;
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

    float mspeed = ToolHelper.getMeleeSpeed(stack);
    float dspeed = ToolHelper.getDigSpeedOnProperMaterial(stack);
    return Math.max(38.4f - 1.4f * mspeed * dspeed, 10);
  }

  public float getArrowVelocity(ItemStack stack, int charge) {

    float f = charge / getDrawDelay(stack);
    f = (f * f + f * 2f) / 3f;
    return f > 1f ? 1f : f;
  }

  public float getArrowDamage(ItemStack stack) {

    return 0.4f * ToolHelper.getMeleeDamage(stack) - 1.0f;
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

      return StackHelper.empty();
    }
  }

  // Same as vanilla bow, except it can be fired without arrows with infinity.
  @Override
  public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand) {

    ItemStack stack = player.getHeldItem(hand);

    boolean hasAmmo = StackHelper.isValid(findAmmo(player))
        || EnchantmentHelper.getEnchantmentLevel(Enchantments.INFINITY, stack) > 0;
    boolean isBroken = ToolHelper.isBroken(stack);

    if (isBroken)
      return new ActionResult(EnumActionResult.PASS, stack);

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
          (EntityPlayer) entityLiving, i, StackHelper.isValid(ammo) || flag);
      if (i < 0)
        return;

      if (StackHelper.isValid(ammo) || flag) {
        if (StackHelper.isEmpty(ammo)) {
          ammo = new ItemStack(Items.ARROW);
        }

        float velocity = getArrowVelocity(stack, i);

        if ((double) velocity >= 0.1D) {
          boolean flag1 = player.capabilities.isCreativeMode || (ammo.getItem() instanceof ItemArrow
              ? ((ItemArrow) ammo.getItem()).isInfinite(ammo, stack, player) : false);

          if (!worldIn.isRemote) {
            ItemArrow itemarrow = (ItemArrow) ((ItemArrow) (ammo.getItem() instanceof ItemArrow
                ? ammo.getItem() : Items.ARROW));
            EntityArrow entityarrow = itemarrow.createArrow(worldIn, ammo, player);
            entityarrow.setAim(player, player.rotationPitch, player.rotationYaw, 0.0F,
                velocity * 3.0F, 1.0F);

            if (velocity == 1.0F) {
              entityarrow.setIsCritical(true);
            }

            int power = EnchantmentHelper.getEnchantmentLevel(Enchantments.POWER, stack);
            float powerBoost = power > 0 ? power * 0.5f + 0.5f : 0.0f;
            float damageBoost = getArrowDamage(stack);
            entityarrow.setDamage(entityarrow.getDamage() + damageBoost + powerBoost);

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

            worldIn.spawnEntity(entityarrow);
          }

          worldIn.playSound((EntityPlayer) null, player.posX, player.posY, player.posZ,
              SoundEvents.ENTITY_ARROW_SHOOT, SoundCategory.NEUTRAL, 1.0F,
              1.0F / (itemRand.nextFloat() * 0.4F + 1.2F) + velocity * 0.5F);

          if (!flag1) {
            StackHelper.shrink(ammo, 1);

            if (StackHelper.getCount(ammo) == 0) {
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
  public void getSubItems(Item item, CreativeTabs tab, NonNullList list) {

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
  public EnumRarity getRarity(ItemStack stack) {

    return ToolRenderHelper.instance.getRarity(stack);
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

    if (getConfig().isDisabled)
      return;

    String line1 = "sgw";
    String line2 = "g w";
    String line3 = "sgw";

    ItemStack flint = new ItemStack(Items.FLINT);
    ItemStack rodWood = new ItemStack(Items.STICK);
    ItemStack rodIron = ModItems.craftingMaterial.toolRodIron;
    ItemStack rodGold = ModItems.craftingMaterial.toolRodGold;
    ItemStack gildedString = ModItems.craftingMaterial.gildedString;

    // Flint
    addRecipe(constructTool(rodWood, flint), flint, "stickWood", Items.STRING);
    for (EnumGem gem : EnumGem.values()) {
      // Regular
      addRecipe(constructTool(rodIron, gem.getItem()), gem.getItem(), rodIron, Items.STRING);
      // Super
      addRecipe(constructTool(rodGold, gem.getItemSuper()), gem.getItemSuper(), rodGold,
          gildedString);
    }
  }

  private void addRecipe(ItemStack result, ItemStack head, Object rod, Object string) {

    ToolPart part = ToolPartRegistry.fromStack(head);
    if (part != null && !part.isBlacklisted(head))
      GameRegistry.addRecipe(
          new ShapedOreRecipe(result, "sgw", "g w", "sgw", 'g', head, 's', rod, 'w', string));
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
