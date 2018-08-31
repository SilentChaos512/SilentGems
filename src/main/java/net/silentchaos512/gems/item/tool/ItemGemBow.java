package net.silentchaos512.gems.item.tool;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.init.Enchantments;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemArrow;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.util.*;
import net.minecraft.world.World;
import net.silentchaos512.gems.SilentGems;
import net.silentchaos512.gems.api.ITool;
import net.silentchaos512.gems.api.lib.EnumMaterialTier;
import net.silentchaos512.gems.config.ConfigOptionToolClass;
import net.silentchaos512.gems.config.GemsConfig;
import net.silentchaos512.gems.item.CraftingItems;
import net.silentchaos512.gems.item.ToolRenderHelper;
import net.silentchaos512.gems.util.ToolHelper;
import net.silentchaos512.lib.registry.IAddRecipes;
import net.silentchaos512.lib.registry.ICustomModel;
import net.silentchaos512.lib.registry.RecipeMaker;

import java.util.List;

public class ItemGemBow extends ItemBow implements ITool, IAddRecipes, ICustomModel {
    private static final int MIN_DRAW_DELAY = 10;
    public static final float ENCHANTABILITY_MULTIPLIER = 0.45f;
    public static final ResourceLocation RESOURCE_PULL = new ResourceLocation("pull");
    public static final ResourceLocation RESOURCE_PULLING = new ResourceLocation("pulling");

    public ItemGemBow() {
        setNoRepair();

        addPropertyOverride(RESOURCE_PULL, (stack, worldIn, entityIn) -> {
            if (entityIn == null) return 0f;
            ItemStack itemstack = entityIn.getActiveItemStack();
            return !itemstack.isEmpty() && ToolHelper.areToolsEqual(stack, itemstack)
                    ? (stack.getMaxItemUseDuration() - entityIn.getItemInUseCount()) / getDrawDelay(stack)
                    : 0f;
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
            return ItemStack.EMPTY;

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
    public float getMeleeDamageModifier() {
        return 0;
    }

    @Override
    public float getMagicDamageModifier() {
        return 0;
    }

    @Override
    public float getMeleeSpeedModifier() {
        return 0;
    }

    // =============
    // = Bow stuff =
    // =============

    public float getDrawDelay(ItemStack stack) {
        // Vanilla bows would be 20.
        float mspeed = ToolHelper.getMeleeSpeed(stack);
        float dspeed = ToolHelper.getDigSpeedOnProperMaterial(stack);
        return getDrawDelay(mspeed, dspeed);
    }

    public static float getDrawDelay(float meleeSpeed, float digSpeed) {
        // Vanilla bows would be 20.
        return Math.max(32.7f - 1.1f * meleeSpeed * digSpeed, MIN_DRAW_DELAY);
    }

    /**
     * Display speed as a factor of vanilla bow speed, because users sometimes misunderstand draw
     * delay (admittedly, it is counterintuitive for lower numbers to mean better).
     *
     * @return Draw speed for tooltip display.
     */
    public float getDrawSpeedForDisplay(ItemStack stack) {
        return 20f / getDrawDelay(stack);
    }

    public static float getDrawSpeedForDisplay(float meleeSpeed, float digSpeed) {
        return 20f / getDrawDelay(meleeSpeed, digSpeed);
    }

    public float getArrowVelocity(ItemStack stack, int charge) {
        float f = charge / getDrawDelay(stack);
        f = (f * f + f * 2f) / 3f;
        return f > 1f ? 1f : f;
    }

    public float getArrowDamage(ItemStack stack) {
        return getArrowDamage(ToolHelper.getMeleeDamage(stack));
    }

    public static float getArrowDamage(float meleeDamage) {
        return 0.4f * meleeDamage - 1.0f;
    }

    /**
     * Display arrow damage as a factor of vanilla arrow damage. Makes comparing bows easier.
     *
     * @return Arrow damage for tooltip display.
     */
    public float getArrowDamageForDisplay(ItemStack stack) {
        return (2f + getArrowDamage(stack)) / 2f;
    }

    public static float getArrowDamageForDisplay(float meleeDamage) {
        return (2f + getArrowDamage(meleeDamage)) / 2f;
    }

    private ItemStack findAmmo(EntityPlayer player) {
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

            return ItemStack.EMPTY;
        }
    }

    // Same as vanilla bow, except it can be fired without arrows with infinity.
    @Override
    public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand) {
        ItemStack stack = player.getHeldItem(hand);

        boolean hasAmmo = !findAmmo(player).isEmpty() || EnchantmentHelper.getEnchantmentLevel(Enchantments.INFINITY, stack) > 0;
        boolean isBroken = ToolHelper.isBroken(stack);

        if (isBroken)
            return new ActionResult<>(EnumActionResult.PASS, stack);

        ActionResult<ItemStack> ret = net.minecraftforge.event.ForgeEventFactory.onArrowNock(stack,
                world, player, hand, hasAmmo);
        if (ret != null && ret.getType() == EnumActionResult.FAIL)
            return ret;

        if (!player.capabilities.isCreativeMode && !hasAmmo) {
            return new ActionResult<>(EnumActionResult.FAIL, stack);
        } else {
            player.setActiveHand(hand);
            return new ActionResult<>(EnumActionResult.SUCCESS, stack);
        }
    }

    @Override
    public void onPlayerStoppedUsing(ItemStack stack, World worldIn, EntityLivingBase entityLiving, int timeLeft) {
        if (entityLiving instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer) entityLiving;
            boolean infiniteAmmo = player.capabilities.isCreativeMode || EnchantmentHelper.getEnchantmentLevel(Enchantments.INFINITY, stack) > 0;
            ItemStack ammo = this.findAmmo(player);

            int i = this.getMaxItemUseDuration(stack) - timeLeft;
            // i = net.minecraftforge.event.ForgeEventFactory.onArrowLoose(stack, worldIn,
            // (EntityPlayer) entityLiving, i, StackHelper.isValid(ammo) || infiniteAmmo);
            if (i < 0)
                return;

            if (!ammo.isEmpty() || infiniteAmmo) {
                if (ammo.isEmpty()) {
                    ammo = new ItemStack(Items.ARROW);
                }

                float velocity = getArrowVelocity(stack, i);

                if ((double) velocity >= 0.1D) {
                    boolean flag1 = player.capabilities.isCreativeMode || (ammo.getItem() instanceof ItemArrow && ((ItemArrow) ammo.getItem()).isInfinite(ammo, stack, player));

                    if (!worldIn.isRemote) {
                        ItemArrow itemarrow = (ItemArrow) (ammo.getItem() instanceof ItemArrow ? ammo.getItem() : Items.ARROW);
                        EntityArrow entityarrow = itemarrow.createArrow(worldIn, ammo, player);
                        entityarrow.shoot(player, player.rotationPitch, player.rotationYaw, 0.0F, velocity * 3.0F, 1.0F);

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

                    worldIn.playSound(null, player.posX, player.posY, player.posZ,
                            SoundEvents.ENTITY_ARROW_SHOOT, SoundCategory.NEUTRAL, 1.0F,
                            1.0F / (itemRand.nextFloat() * 0.4F + 1.2F) + velocity * 0.5F);

                    if (!flag1) {
                        ammo.shrink(1);

                        if (ammo.getCount() == 0) {
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
    public int getMaxDamage(ItemStack stack) {
        return ToolHelper.getMaxDamage(stack);
    }

    @Override
    public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged) {
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
    public void onUpdate(ItemStack tool, World world, Entity entity, int itemSlot, boolean isSelected) {
        ToolHelper.onUpdate(tool, world, entity, itemSlot, isSelected);
    }

    @Override
    public boolean onEntityItemUpdate(EntityItem entityItem) {
        return ToolHelper.onEntityItemUpdate(entityItem);
    }

    @Override
    public void addRecipes(RecipeMaker recipes) {

        if (getConfig().isDisabled)
            return;

        String[] lines = new String[]{"rhw", "h w", "rhw"};
        ToolHelper.addExampleRecipe(this,
                new EnumMaterialTier[]{EnumMaterialTier.MUNDANE, EnumMaterialTier.REGULAR}, lines, 'w',
                new ItemStack(Items.STRING));
        ToolHelper.addExampleRecipe(this, new EnumMaterialTier[]{EnumMaterialTier.SUPER}, lines, 'w',
                CraftingItems.GILDED_STRING.getStack());
    }

    @Override
    public void addInformation(ItemStack stack, World world, List list, ITooltipFlag flag) {
        ToolRenderHelper.getInstance().addInformation(stack, world, list, flag);
    }

    @Override
    public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> list) {
        if (!isInCreativeTab(tab)) return;
        list.addAll(ToolHelper.getSubItems(this, 3));
    }

    @Override
    public void registerModels() {
        SilentGems.registry.setModel(this, 0, "tool");
    }
}
