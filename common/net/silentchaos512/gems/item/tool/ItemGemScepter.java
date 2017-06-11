package net.silentchaos512.gems.item.tool;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.silentchaos512.gems.SilentGems;
import net.silentchaos512.gems.config.ConfigOptionToolClass;
import net.silentchaos512.gems.config.GemsConfig;
import net.silentchaos512.gems.item.ModItems;
import net.silentchaos512.gems.lib.EnumGem;
import net.silentchaos512.gems.lib.Names;
import net.silentchaos512.gems.util.ToolHelper;
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
  public void getSubItems(Item item, CreativeTabs tab, NonNullList list) {

    if (subItems == null) {
      subItems = ToolHelper.getSubItems(item, 5);
    }
    list.addAll(subItems);
  }

  @Override
  public void addRecipes() {

    if (getConfig().isDisabled) return;

    String line1 = " g ";
    String line2 = "gsg";
    String line3 = "gsg";
    for (EnumGem gem : EnumGem.values()) {
      ToolHelper.addRecipe(constructTool(true, gem.getItemSuper()), line1, line2, line3,
          gem.getItemSuper(), ModItems.craftingMaterial.toolRodGold);
    }
  }

  @Override
  public String getName() {

    return Names.SCEPTER;
  }
}
