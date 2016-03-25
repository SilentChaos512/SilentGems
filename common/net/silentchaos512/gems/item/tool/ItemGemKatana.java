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

public class ItemGemKatana extends ItemGemSword {

  @Override
  public ItemStack constructTool(ItemStack rod, ItemStack... materials) {

    ItemStack result = ToolHelper.constructTool(this, rod, materials);
    if (result != null) {
      ToolHelper.setPart(result,
          ToolPartRegistry
              .fromStack(new ItemStack(Blocks.wool, 1, EnumDyeColor.BLACK.getMetadata())),
          EnumMaterialGrade.NONE, EnumPartPosition.ROD_WOOL);
    }
    return result;
  }

  @Override
  public float getMeleeDamage(ItemStack tool) {

    return getBaseMeleeDamageModifier() + ToolHelper.getMeleeDamage(tool);
  }

  @Override
  public float getMagicDamage(ItemStack tool) {

    return 3.0f + ToolHelper.getMagicDamage(tool);
  }

  @Override
  public float getBaseMeleeDamageModifier() {

    return 3.0f;
  }

  @Override
  public float getBaseMeleeSpeedModifier() {

    return -2.0f;
  }

  @Override
  public void getSubItems(Item item, CreativeTabs tab, List list) {

    if (subItems == null) {
      ItemStack rod = ModItems.craftingMaterial.getStack(Names.ORNATE_STICK_GOLD);
      ItemStack gemStack;
      subItems = Lists.newArrayList();
      for (EnumGem gem : EnumGem.values()) {
        gemStack = gem.getItemSuper();
        subItems.add(constructTool(rod, gemStack, gemStack, gemStack));
      }
    }

    list.addAll(subItems);
  }

  @Override
  public void addRecipes() {

    String line1 = "gg";
    String line2 = "g ";
    String line3 = "s ";
    for (EnumGem gem : EnumGem.values()) {
      GameRegistry.addRecipe(new ShapedOreRecipe(constructTool(true, gem.getItemSuper()), line1,
          line2, line3, 'g', gem.getItemSuper(), 's',
          ModItems.craftingMaterial.getStack(Names.ORNATE_STICK_GOLD)));
    }
  }

  @Override
  public String getName() {

    return Names.KATANA;
  }
}
