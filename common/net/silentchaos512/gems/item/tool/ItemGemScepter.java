package net.silentchaos512.gems.item.tool;

import java.util.List;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.silentchaos512.gems.SilentGems;
import net.silentchaos512.gems.config.ConfigOptionToolClass;
import net.silentchaos512.gems.config.GemsConfig;
import net.silentchaos512.gems.init.ModItems;
import net.silentchaos512.gems.lib.EnumGem;
import net.silentchaos512.gems.lib.Names;
import net.silentchaos512.gems.util.ToolHelper;
import net.silentchaos512.lib.registry.RecipeMaker;
import net.silentchaos512.lib.util.ItemHelper;
import net.silentchaos512.lib.util.StackHelper;

public class ItemGemScepter extends ItemGemSword {

  public ItemGemScepter() {

    super();
    setUnlocalizedName(SilentGems.RESOURCE_PREFIX + Names.SCEPTER);
  }
  
  public ConfigOptionToolClass getConfig() {

    return GemsConfig.scepter;
  }

  @Override
  public ItemStack constructTool(ItemStack rod, ItemStack... materials) {

    if (getConfig().isDisabled) return StackHelper.empty();

    if (materials.length >= 2) {
      ItemStack temp = materials[0];
      materials[0] = materials[1];
      materials[1] = temp;
    }
    return ToolHelper.constructTool(this, rod, materials);
  }

  @Override
  public float getMagicDamage(ItemStack tool) {

    return 5.0f + ToolHelper.getMagicDamage(tool);
  }

  @Override
  public float getBaseMeleeDamageModifier() {

    return 1.0f;
  }

  @Override
  public float getBaseMeleeSpeedModifier() {

    return -3.2f;
  }

  @Override
  public float getDurabilityMultiplier() {

    return 0.75f;
  }

  @SuppressWarnings("deprecation")
  @Override
  public boolean isSuperTool() {

    return true;
  }

  @Override
  protected void clGetSubItems(Item item, CreativeTabs tab, List<ItemStack> list) {

    if (!ItemHelper.isInCreativeTab(item, tab))
      return;

    list.addAll(ToolHelper.getSubItems(item, 5));
  }

  @Override
  public void addRecipes(RecipeMaker recipes) {

    if (!getConfig().isDisabled)
      ToolHelper.addExampleRecipe(this, " g ", "gsg", "gsg");
  }

  @Override
  public String getName() {

    return Names.SCEPTER;
  }
}
