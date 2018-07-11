package net.silentchaos512.gems.item.tool;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.silentchaos512.gems.SilentGems;
import net.silentchaos512.gems.config.ConfigOptionToolClass;
import net.silentchaos512.gems.config.GemsConfig;
import net.silentchaos512.gems.init.ModItems;
import net.silentchaos512.gems.lib.Names;
import net.silentchaos512.gems.util.ToolHelper;
import net.silentchaos512.lib.registry.RecipeMaker;
import net.silentchaos512.lib.util.StackHelper;

public class ItemGemDagger extends ItemGemSword {

  public ItemGemDagger() {

    super();
    setUnlocalizedName(SilentGems.RESOURCE_PREFIX + Names.DAGGER);
  }
  
  public ConfigOptionToolClass getConfig() {

    return GemsConfig.dagger;
  }

  @Override
  public ItemStack constructTool(ItemStack rod, ItemStack... materials) {

    if (getConfig().isDisabled)
      return StackHelper.empty();
    return ToolHelper.constructTool(this, rod, materials);
  }

  @Override
  public ItemStack constructTool(boolean supercharged, ItemStack... materials) {

    if (getConfig().isDisabled)
      return StackHelper.empty();
    ItemStack rod = supercharged ? ModItems.craftingMaterial.toolRodGold
        : new ItemStack(Items.STICK);
    return ToolHelper.constructTool(this, rod, materials);
  }

  @Override
  public float getMeleeDamage(ItemStack tool) {

    return (getMeleeDamageModifier() + ToolHelper.getMeleeDamage(tool)) / 2;
  }

  @Override
  public float getMagicDamage(ItemStack tool) {

    return 1.0f + ToolHelper.getMagicDamage(tool);
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

    return -1.4f;
  }

  @Override
  public float getDurabilityMultiplier() {

    return 1.0f;
  }

  @Override
  public float getRepairMultiplier() {

    return 2.0f;
  }

  @Override
  public boolean hitEntity(ItemStack stack, EntityLivingBase target, EntityLivingBase attacker) {

    target.hurtResistantTime *= 0.67f; // Make target vulnerable sooner.
    return super.hitEntity(stack, target, attacker);
  }

//  // onItemRightClick 1.10.2
//  public ActionResult<ItemStack> func_77659_a(ItemStack stack, World world, EntityPlayer player,
//      EnumHand hand) {
//
//    return compatOnItemRightClick(world, player, hand);
//  }
//
//  @Override
//  public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand) {
//
//    return compatOnItemRightClick(world, player, hand);
//  }
//
//  protected ActionResult<ItemStack> compatOnItemRightClick(World world, EntityPlayer player,
//      EnumHand hand) {
//
//    EnumHand handOther = hand == EnumHand.MAIN_HAND ? EnumHand.OFF_HAND : EnumHand.MAIN_HAND;
//    ItemStack stackOther = player.getHeldItem(handOther);
//    if (StackHelper.isValid(stackOther)) {
//      player.swingArm(hand);
//    }
//    return new ActionResult<ItemStack>(EnumActionResult.PASS, player.getHeldItem(hand));
//  }

  @Override
  public void addRecipes(RecipeMaker recipes) {

    if (!getConfig().isDisabled)
      ToolHelper.addExampleRecipe(this, "h", "r");
  }

  @Override
  public String getName() {

    return Names.DAGGER;
  }
}
