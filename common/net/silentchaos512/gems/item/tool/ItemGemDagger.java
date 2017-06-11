package net.silentchaos512.gems.item.tool;

import java.util.List;

import com.google.common.collect.Lists;

import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;
import net.silentchaos512.gems.SilentGems;
import net.silentchaos512.gems.config.ConfigOptionToolClass;
import net.silentchaos512.gems.config.GemsConfig;
import net.silentchaos512.gems.item.ModItems;
import net.silentchaos512.gems.lib.EnumGem;
import net.silentchaos512.gems.lib.Names;
import net.silentchaos512.gems.util.ToolHelper;
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
  public void addRecipes() {

    if (getConfig().isDisabled)
      return;

    String l1 = "g";
    String l2 = "s";
    String l3 = "f";
    ItemStack flint = new ItemStack(Items.FLINT);
    ItemStack rodGold = ModItems.craftingMaterial.toolRodGold;

    // Flint
    ToolHelper.addRecipe(constructTool(false, flint), l1, l2, l3, flint, "stickWood");
    for (EnumGem gem : EnumGem.values()) {
      // Regular
      ToolHelper.addRecipe(constructTool(false, gem.getItem()), l1, l2, l3, gem.getItem(), "stickWood");
      // Super
      ToolHelper.addRecipe(constructTool(true, gem.getItemSuper()), l1, l2, l3, gem.getItemSuper(), rodGold);
    }
  }

  @Override
  public String getName() {

    return Names.DAGGER;
  }
}
