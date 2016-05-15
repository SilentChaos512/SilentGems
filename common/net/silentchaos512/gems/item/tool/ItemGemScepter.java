package net.silentchaos512.gems.item.tool;

import java.util.List;

import com.google.common.collect.Lists;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.silentchaos512.gems.api.lib.EnumMaterialGrade;
import net.silentchaos512.gems.api.lib.EnumPartPosition;
import net.silentchaos512.gems.api.tool.part.ToolPartRegistry;
import net.silentchaos512.gems.item.ModItems;
import net.silentchaos512.gems.lib.EnumGem;
import net.silentchaos512.gems.lib.Names;
import net.silentchaos512.gems.util.ToolHelper;

public class ItemGemScepter extends ItemGemSword {

  @Override
  public ItemStack constructTool(ItemStack rod, ItemStack... materials) {

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
  public void getSubItems(Item item, CreativeTabs tab, List list) {

    if (subItems == null) {
      subItems = ToolHelper.getSubItems(item, 5);
    }
    list.addAll(subItems);
  }

  @Override
  public void addRecipes() {

    String line1 = " g ";
    String line2 = "gsg";
    String line3 = "gsg";
    for (EnumGem gem : EnumGem.values()) {
      GameRegistry.addRecipe(new ShapedOreRecipe(constructTool(true, gem.getItemSuper()), line1,
          line2, line3, 'g', gem.getItemSuper(), 's', ModItems.craftingMaterial.toolRodGold));
    }
  }

  @Override
  public String getName() {

    return Names.SCEPTER;
  }
}
