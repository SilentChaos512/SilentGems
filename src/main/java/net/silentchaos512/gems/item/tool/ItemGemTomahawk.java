package net.silentchaos512.gems.item.tool;

import java.util.List;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Enchantments;
import net.minecraft.item.EnumAction;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.silentchaos512.gems.SilentGems;
import net.silentchaos512.gems.api.IAmmoTool;
import net.silentchaos512.gems.config.ConfigOptionToolClass;
import net.silentchaos512.gems.config.GemsConfig;
import net.silentchaos512.gems.entity.EntityThrownTomahawk;
import net.silentchaos512.gems.lib.Names;
import net.silentchaos512.gems.util.ToolHelper;
import net.silentchaos512.lib.registry.RecipeMaker;
import net.silentchaos512.lib.util.ItemHelper;
import net.silentchaos512.lib.util.StackHelper;

public class ItemGemTomahawk extends ItemGemAxe implements IAmmoTool {

  public static final String NBT_AMMO = "SG.Ammo";

  public ItemGemTomahawk() {

    super();
    setUnlocalizedName(SilentGems.RESOURCE_PREFIX + Names.TOMAHAWK);
  }

  public ConfigOptionToolClass getConfig() {

    return GemsConfig.tomahawk;
  }

  @Override
  public ItemStack constructTool(ItemStack rod, ItemStack... materials) {

    if (getConfig().isDisabled)
      return StackHelper.empty();
    return ToolHelper.constructTool(this, rod, materials);
  }

  public float getThrownDamage(ItemStack tomahawk) {

    int sharpness = EnchantmentHelper.getEnchantmentLevel(Enchantments.SHARPNESS, tomahawk);
    float modifier = sharpness > 0 ? 1f + Math.max(0, sharpness - 1) / 2f : 0f;
    return 6.0f + modifier + ToolHelper.getMeleeDamage(tomahawk);
  }

  @Override
  public float getMeleeDamage(ItemStack tool) {

    return (getMeleeDamageModifier() + ToolHelper.getMeleeDamage(tool)) / 2;
  }

  @Override
  public float getMeleeDamageModifier() {

    return 1.0f;
  }

  @Override
  public float getMagicDamageModifier() {

    return 1.0f;
  }

  @Override
  public float getMeleeSpeedModifier() {

    return -1.8f;
  }

  @Override
  public int getAmmo(ItemStack tool) {

    if (StackHelper.isValid(tool) && tool.hasTagCompound()) {
      if (!tool.getTagCompound().hasKey(NBT_AMMO))
        tool.getTagCompound().setByte(NBT_AMMO, (byte) getMaxAmmo(tool));
      return tool.getTagCompound().getByte(NBT_AMMO);
    }
    return 0;
  }

  @Override
  public int getMaxAmmo(ItemStack tool) {

    return GemsConfig.TOMAHAWK_MAX_AMMO;
  }

  @Override
  public void addAmmo(ItemStack tool, int amount) {

    if (StackHelper.isValid(tool) && tool.hasTagCompound()) {
      int current = getAmmo(tool);
      int newAmount = Math.min(current + amount, getMaxAmmo(tool));
      tool.getTagCompound().setByte(NBT_AMMO, (byte) newAmount);
    }
  }

  @Override
  public boolean isDiggingTool() {

    return false; // Don't need custom crosshairs.
  }

  @Override
  public float getDestroySpeed(ItemStack stack, IBlockState state) {

    float digSpeed = ToolHelper.getDigSpeed(stack, state, EXTRA_EFFECTIVE_MATERIALS);
    // On blocks typically harvested with axes, reduce the harvest speed.
    // Note: Ladders use the "circuits" material. Weird, but true!
    if (state.getMaterial() == Material.WOOD || state.getMaterial() == Material.GOURD
        || state.getMaterial() == Material.CIRCUITS) {
      return digSpeed / 2.5f;
    }
    // On other blocks, full speed.
    return digSpeed;
  }

  @Override
  protected void clGetSubItems(Item item, CreativeTabs tab, List<ItemStack> list) {

    if (!ItemHelper.isInCreativeTab(item, tab))
      return;

    list.addAll(ToolHelper.getSubItems(item, 4));
  }

  @Override
  public void addRecipes(RecipeMaker recipes) {

    if (!getConfig().isDisabled)
      ToolHelper.addExampleRecipe(this, "hhh", "hr ", " r ");
  }

  @Override
  public String getName() {

    return Names.TOMAHAWK;
  }

  @Override
  public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand) {

    ItemStack stack = player.getHeldItem(hand);

    // Prepare to throw
    if (!ToolHelper.isBroken(stack) && (getAmmo(stack) > 0 || player.capabilities.isCreativeMode)) {
      player.setActiveHand(hand);
      return new ActionResult(EnumActionResult.SUCCESS, stack);
    }
    return new ActionResult(EnumActionResult.FAIL, stack);
  }

  @Override
  public void onPlayerStoppedUsing(ItemStack stack, World worldIn, EntityLivingBase entityLiving,
      int timeLeft) {

    // Throw it!
    if (entityLiving instanceof EntityPlayer) {
      EntityPlayer player = (EntityPlayer) entityLiving;

      boolean hasAmmo = player.capabilities.isCreativeMode || getAmmo(stack) > 0;
      int useDuration = getMaxItemUseDuration(stack) - timeLeft;
      if (useDuration < 4 || !hasAmmo)
        return;
      float speed = MathHelper.clamp(1.5f * useDuration / 12, 0.1f, EntityThrownTomahawk.MAX_SPEED);

      EntityThrownTomahawk projectile = new EntityThrownTomahawk(player, stack, speed);
      projectile.setPosition(player.posX, player.posY + 1.6, player.posZ);
      worldIn.spawnEntity(projectile);

      // Damage, reduce "ammo" count, increment statistics
      if (!player.capabilities.isCreativeMode) {
        ToolHelper.attemptDamageTool(stack, 3, player);
        addAmmo(stack, -1);
      }

      // Statistics
      ToolHelper.incrementStatThrownCount(stack, 1);
    }
  }

  @Override
  public EnumActionResult onItemUse(EntityPlayer player, World world, BlockPos pos, EnumHand hand,
      EnumFacing side, float hitX, float hitY, float hitZ) {

    // Cancel right-click-to-place.
    return EnumActionResult.PASS;
  }

  @Override
  public int getMaxItemUseDuration(ItemStack stack) {

    return 72000;
  }

  @Override
  public EnumAction getItemUseAction(ItemStack stack) {

    return EnumAction.BOW;
  }

  // =================================
  // Cross Compatibility (MC 10/11/12)
  // =================================

  // onItemUse
  public EnumActionResult func_180614_a(ItemStack stack, EntityPlayer player, World world,
      BlockPos pos, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {

    return onItemUse(player, world, pos, hand, side, hitX, hitY, hitZ);
  }

  // onItemRightClick
  public ActionResult<ItemStack> func_77659_a(ItemStack stack, World world, EntityPlayer player,
      EnumHand hand) {

    return onItemRightClick(world, player, hand);
  }
}
