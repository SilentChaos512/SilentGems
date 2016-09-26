package net.silentchaos512.gems.item.tool;

import java.util.List;

import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Enchantments;
import net.minecraft.init.Items;
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
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.silentchaos512.gems.SilentGems;
import net.silentchaos512.gems.api.IAmmoTool;
import net.silentchaos512.gems.config.GemsConfig;
import net.silentchaos512.gems.entity.EntityThrownTomahawk;
import net.silentchaos512.gems.item.ModItems;
import net.silentchaos512.gems.lib.EnumGem;
import net.silentchaos512.gems.lib.Names;
import net.silentchaos512.gems.util.ToolHelper;

public class ItemGemTomahawk extends ItemGemAxe implements IAmmoTool {

  public static final String NBT_AMMO = "SG.Ammo";

  public ItemGemTomahawk() {

    super();
    setUnlocalizedName(SilentGems.RESOURCE_PREFIX + Names.TOMAHAWK);
  }

  @Override
  public ItemStack constructTool(ItemStack rod, ItemStack... materials) {

    return ToolHelper.constructTool(this, rod, materials);
  }

  public float getThrownDamage(ItemStack tomahawk) {

    int sharpness = EnchantmentHelper.getEnchantmentLevel(Enchantments.SHARPNESS, tomahawk);
    float modifier = sharpness > 0 ? 1f + Math.max(0, sharpness - 1) / 2f : 0f;
    return 6.0f + modifier + ToolHelper.getMeleeDamage(tomahawk);
  }

  @Override
  public float getMeleeDamage(ItemStack tool) {

    return (getBaseMeleeDamageModifier() + ToolHelper.getMeleeDamage(tool)) / 2;
  }

  @Override
  public float getMagicDamage(ItemStack tool) {

    return 1.0f + ToolHelper.getMagicDamage(tool);
  }

  @Override
  public float getBaseMeleeDamageModifier() {

    return 1.0f;
  }

  @Override
  public float getBaseMeleeSpeedModifier() {

    return -1.8f;
  }

  @Override
  public int getAmmo(ItemStack tool) {

    if (tool != null && tool.hasTagCompound()) {
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

    if (tool != null && tool.hasTagCompound()) {
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
  public float getStrVsBlock(ItemStack stack, IBlockState state) {

    return ToolHelper.getDigSpeed(stack, state, extraEffectiveMaterials) / 3;
  }

  @Override
  public void getSubItems(Item item, CreativeTabs tab, List list) {

    if (subItems == null)
      subItems = ToolHelper.getSubItems(item, 4);
    list.addAll(subItems);
  }

  @Override
  public void addRecipes() {

    String line1 = "ggg";
    String line2 = "gs ";
    String line3 = " s ";
    ItemStack flint = new ItemStack(Items.FLINT);
    ItemStack rodWood = new ItemStack(Items.STICK);
    ItemStack rodGold = ModItems.craftingMaterial.toolRodGold;

    // Flint
    GameRegistry.addRecipe(new ShapedOreRecipe(constructTool(rodWood, flint), line1, line2, line3,
        'g', flint, 's', "stickWood"));
    for (EnumGem gem : EnumGem.values()) {
      // Regular
      GameRegistry.addRecipe(new ShapedOreRecipe(constructTool(rodWood, gem.getItem()), line1,
          line2, line3, 'g', gem.getItem(), 's', "stickWood"));
      // Super
      GameRegistry.addRecipe(new ShapedOreRecipe(constructTool(rodGold, gem.getItemSuper()), line1,
          line2, line3, 'g', gem.getItemSuper(), 's', rodGold));
    }
  }

  @Override
  public String getName() {

    return Names.TOMAHAWK;
  }

  @Override
  public ActionResult<ItemStack> onItemRightClick(ItemStack stack, World world, EntityPlayer player,
      EnumHand hand) {

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
      float speed = MathHelper.clamp_float(1.5f * useDuration / 12, 0.1f,
          EntityThrownTomahawk.MAX_SPEED);

      EntityThrownTomahawk projectile = new EntityThrownTomahawk(player, stack, speed);
      projectile.setPosition(player.posX, player.posY + 1.6, player.posZ);
      worldIn.spawnEntityInWorld(projectile);

      // Damage, reduce "ammo" count, increment statistics
      if (!player.capabilities.isCreativeMode) {
        ToolHelper.attemptDamageTool(stack, 3, player);
        addAmmo(stack, -1);
      }
      // TODO: Statistics?
    }
  }

  @Override
  public EnumActionResult onItemUse(ItemStack stack, EntityPlayer player, World world, BlockPos pos,
      EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {

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
}
