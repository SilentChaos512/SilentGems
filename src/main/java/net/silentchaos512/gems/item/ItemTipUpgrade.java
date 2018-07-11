package net.silentchaos512.gems.item;

import java.util.List;
import java.util.Map;

import com.google.common.collect.Lists;

import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.silentchaos512.gems.SilentGems;
import net.silentchaos512.gems.api.tool.part.ToolPart;
import net.silentchaos512.gems.api.tool.part.ToolPartRegistry;
import net.silentchaos512.gems.api.tool.part.ToolPartTip;
import net.silentchaos512.gems.init.ModItems;
import net.silentchaos512.gems.lib.EnumTipUpgrade;
import net.silentchaos512.gems.lib.Names;
import net.silentchaos512.gems.util.ToolHelper;
import net.silentchaos512.lib.item.ItemSL;
import net.silentchaos512.lib.registry.RecipeMaker;
import net.silentchaos512.lib.util.LocalizationHelper;
import net.silentchaos512.lib.util.StackHelper;

public class ItemTipUpgrade extends ItemSL {

  public static final String[] NAMES = new String[] { "UpgradeIronTip", "UpgradeGoldTip",
      "UpgradeDiamondTip", "UpgradeEmeraldTip" };

  // For guide book...
  public static List<IRecipe> RECIPES = Lists.newArrayList();

  public ItemTipUpgrade() {

    super(EnumTipUpgrade.values().length - 1, SilentGems.MODID, Names.UPGRADE_TIP);
  }

  @Override
  public void clAddInformation(ItemStack stack, World world, List list, boolean advanced) {

    ToolPartTip part = (ToolPartTip) ToolPartRegistry.fromStack(stack);
    if (part != null) {
      LocalizationHelper loc = SilentGems.instance.localizationHelper;
      String line;

      list.add(loc.getItemSubText(itemName, "willReplace"));

      list.add(loc.getItemSubText(itemName, "harvestLevel", part.getHarvestLevel()));

      tooltipLine(loc, list, "durability", part.getDurability());
      tooltipLine(loc, list, "harvestSpeed", part.getHarvestSpeed());
      // tooltipLine(loc, list, "attackSpeed", part.getMeleeSpeed());
      tooltipLine(loc, list, "meleeDamage", part.getMeleeDamage());
      tooltipLine(loc, list, "magicDamage", part.getMagicDamage());
    }
  }

  protected void tooltipLine(LocalizationHelper loc, List list, String key, Number value) {

    if (value.floatValue() == 0) {
      return;
    }

    String numberString = (value.floatValue() < 0 ? "-" : "+")
        + String.format(value instanceof Integer ? "%d" : "%.1f", value);
    String line = loc.getItemSubText(itemName, key, numberString);
    list.add(String.format(line, numberString));
  }

  public ItemStack applyToTool(ItemStack tool, ItemStack upgrade) {

    ToolPart part = ToolPartRegistry.fromStack(upgrade);
    if (part == null) {
      return null;
    }

    ItemStack result = StackHelper.safeCopy(tool);
    ToolHelper.setConstructionTip(result, part);
    ToolHelper.recalculateStats(result);
    return result;
  }

  @Override
  public void addRecipes(RecipeMaker recipes) {

    ItemStack base = ModItems.craftingMaterial.upgradeBase;
    RECIPES.add(recipes.addShapelessOre("tip_upgrade_iron", new ItemStack(this, 1, 0), "ingotIron", base));
    RECIPES.add(recipes.addShapelessOre("tip_upgrade_gold", new ItemStack(this, 1, 1), "ingotGold", base));
    RECIPES.add(recipes.addShapelessOre("tip_upgrade_diamond", new ItemStack(this, 1, 2), "gemDiamond", base));
    RECIPES.add(recipes.addShapelessOre("tip_upgrade_emerald", new ItemStack(this, 1, 3), "gemEmerald", base));
  }

  @Override
  public void getModels(Map<Integer, ModelResourceLocation> models) {

    int i = 0;
    for (String str : NAMES) {
      String name = (SilentGems.RESOURCE_PREFIX + str).toLowerCase();
      models.put(i++, new ModelResourceLocation(name, "inventory"));
    }
  }

  @Override
  public String getNameForStack(ItemStack stack) {

    return NAMES[MathHelper.clamp(stack.getItemDamage(), 0, NAMES.length - 1)];
  }
}
