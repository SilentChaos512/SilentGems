package net.silentchaos512.gems.item.tool;

import java.util.List;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.silentchaos512.gems.SilentGems;
import net.silentchaos512.gems.api.lib.EnumMaterialGrade;
import net.silentchaos512.gems.api.lib.EnumPartPosition;
import net.silentchaos512.gems.api.tool.part.ToolPartRegistry;
import net.silentchaos512.gems.item.ModItems;
import net.silentchaos512.gems.lib.EnumGem;
import net.silentchaos512.gems.lib.Names;
import net.silentchaos512.gems.util.ToolHelper;

public class ItemGemKatana extends ItemGemSword {

  public ItemGemKatana() {

    super();
    setUnlocalizedName(SilentGems.RESOURCE_PREFIX + Names.KATANA);
  }

  @Override
  public ItemStack constructTool(ItemStack rod, ItemStack... materials) {

    ItemStack result = ToolHelper.constructTool(this, rod, materials);
    return addDefaultGrip(result);
  }

  @Override
  public ItemStack constructTool(boolean supercharged, ItemStack... materials) {

    ItemStack rod = supercharged ? ModItems.craftingMaterial.toolRodGold
        : new ItemStack(Items.STICK);
    ItemStack result = ToolHelper.constructTool(this, rod, materials);
    return addDefaultGrip(result);
  }

  private ItemStack addDefaultGrip(ItemStack katana) {

    if (katana == null)
      return null;
    ItemStack blackWool = new ItemStack(Blocks.WOOL, 1, EnumDyeColor.BLACK.getMetadata());
    ToolHelper.setPart(katana, ToolPartRegistry.fromStack(blackWool), EnumMaterialGrade.NONE,
        EnumPartPosition.ROD_GRIP);
    return katana;
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

    return 2.0f;
  }

  @Override
  public float getBaseMeleeSpeedModifier() {

    return -2.2f;
  }

  @Override
  public void getSubItems(Item item, CreativeTabs tab, List list) {

    if (subItems == null) {
      subItems = ToolHelper.getSubItems(item, 3);
      for (ItemStack stack : subItems) {
        stack = addDefaultGrip(stack);
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
          line2, line3, 'g', gem.getItemSuper(), 's', ModItems.craftingMaterial.toolRodGold));
    }
  }

  @Override
  public String getName() {

    return Names.KATANA;
  }
}
