package net.silentchaos512.gems.item;

import java.util.List;

import com.google.common.collect.Lists;

import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.ShapelessOreRecipe;
import net.silentchaos512.gems.SilentGems;
import net.silentchaos512.gems.api.tool.part.ToolPart;
import net.silentchaos512.gems.api.tool.part.ToolPartRegistry;
import net.silentchaos512.gems.lib.EnumTipUpgrade;
import net.silentchaos512.gems.lib.Names;
import net.silentchaos512.gems.util.ToolHelper;
import net.silentchaos512.lib.item.ItemSL;

public class ItemTipUpgrade extends ItemSL {

  public static final String[] NAMES = new String[] { "UpgradeIronTip", "UpgradeGoldTip",
      "UpgradeDiamondTip", "UpgradeEmeraldTip" };

  public ItemTipUpgrade() {

    super(EnumTipUpgrade.values().length - 1, SilentGems.MOD_ID, Names.UPGRADE_TIP);
  }

  @Override
  public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean advanced) {

    // TODO
  }

  // public EnumTipUpgrade getTipForUpgrade(int meta) {
  //
  // if (meta >= 0 && meta < EnumTipUpgrade.values().length - 1) {
  // return EnumTipUpgrade.values()[meta - 1];
  // }
  // return EnumTipUpgrade.NONE;
  // }

  public ItemStack applyToTool(ItemStack tool, ItemStack upgrade) {

    if (tool == null || upgrade == null) {
      return null;
    }

    ToolPart part = ToolPartRegistry.fromStack(upgrade);
    if (part == null) {
      SilentGems.instance.logHelper.derp();
      return null;
    }

    ItemStack result = tool.copy();
    ToolHelper.setConstructionTip(result, part);
    return result;
  }

  @Override
  public void addRecipes() {

    ItemStack base = ModItems.craftingMaterial.getStack(Names.UPGRADE_BASE);
    GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(this, 1, 0), "ingotIron", base));
    GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(this, 1, 1), "ingotGold", base));
    GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(this, 1, 2), "gemDiamond", base));
    GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(this, 1, 3), "gemEmerald", base));
  }

  @Override
  public List<ModelResourceLocation> getVariants() {

    List<ModelResourceLocation> models = Lists.newArrayList();
    for (String str : NAMES) {
      models.add(new ModelResourceLocation(SilentGems.MOD_ID + ":" + str, "inventory"));
    }
    return models;
  }

  @Override
  public String getNameForStack(ItemStack stack) {

    return NAMES[MathHelper.clamp_int(stack.getItemDamage(), 0, NAMES.length - 1)];
  }
}
