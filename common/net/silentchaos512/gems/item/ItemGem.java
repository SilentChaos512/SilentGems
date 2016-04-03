package net.silentchaos512.gems.item;

import java.util.List;

import org.lwjgl.input.Keyboard;

import com.google.common.collect.Lists;

import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.silentchaos512.gems.SilentGems;
import net.silentchaos512.gems.api.lib.EnumMaterialGrade;
import net.silentchaos512.gems.api.lib.EnumMaterialTier;
import net.silentchaos512.gems.lib.EnumGem;
import net.silentchaos512.gems.lib.Names;
import net.silentchaos512.lib.item.ItemSL;
import net.silentchaos512.lib.util.LocalizationHelper;
import net.silentchaos512.lib.util.RecipeHelper;

public class ItemGem extends ItemSL {

  public ItemGem() {

    super(64, SilentGems.MOD_ID, Names.GEM);
  }

  @Override
  public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean advanced) {

    EnumGem gem = EnumGem.getFromStack(stack);
    EnumMaterialTier tier = stack.getItemDamage() > 31 ? EnumMaterialTier.SUPER
        : EnumMaterialTier.REGULAR;
    LocalizationHelper loc = SilentGems.instance.localizationHelper;
    String line;

    boolean controlDown = Keyboard.isKeyDown(Keyboard.KEY_LCONTROL)
        || Keyboard.isKeyDown(Keyboard.KEY_RCONTROL);

    if (gem != null && controlDown) {
      int multi = 100 + EnumMaterialGrade.fromStack(stack).bonusPercent;
      line = loc.getMiscText("Tooltip.Durability");
      list.add(String.format(line, gem.getDurability(tier) * multi / 100));
      line = loc.getMiscText("Tooltip.HarvestSpeed");
      list.add(String.format(line, gem.getMiningSpeed(tier) * multi / 100));
      line = loc.getMiscText("Tooltip.HarvestLevel");
      list.add(String.format(line, gem.getHarvestLevel(tier) * multi / 100));
      line = loc.getMiscText("Tooltip.MeleeDamage");
      list.add(String.format(line, gem.getMeleeDamage(tier) * multi / 100));
      line = loc.getMiscText("Tooltip.MagicDamage");
      list.add(String.format(line, gem.getMagicDamage(tier) * multi / 100));
      line = loc.getMiscText("Tooltip.ChargeSpeed");
      list.add(String.format(line, gem.getChargeSpeed(tier) * multi / 100));
      line = loc.getMiscText("Tooltip.Enchantability");
      list.add(String.format(line, gem.getEnchantability(tier) * multi / 100));
    } else {
      list.add(TextFormatting.GOLD + loc.getMiscText("PressCtrl"));
    }
  }

  @Override
  public void addRecipes() {

    for (EnumGem gem : EnumGem.values()) {
      // Supercharged gems
      GameRegistry.addRecipe(new ShapedOreRecipe(gem.getItemSuper(), "cgc", "cdc", "cgc", 'g',
          gem.getItem(), 'd', "dustGlowstone", 'c', "gemChaos"));
      // Gems <--> shards
      RecipeHelper.addCompressionRecipe(gem.getShard(), gem.getItem(), 9);
    }
  }

  @Override
  public boolean hasEffect(ItemStack stack) {

    return stack.getItemDamage() > 31;
  }

  @Override
  public List<ModelResourceLocation> getVariants() {

    List<ModelResourceLocation> models = Lists.newArrayList();
    int i;
    for (i = 0; i < 16; ++i) {
      models.add(new ModelResourceLocation(getFullName() + i, "inventory"));
    }
    for (i = 0; i < 16; ++i) {
      models.add(new ModelResourceLocation(getFullName() + "Dark" + i, "inventory"));
    }
    for (i = 0; i < 16; ++i) {
      models.add(new ModelResourceLocation(getFullName() + "Super" + i, "inventory"));
    }
    for (i = 0; i < 16; ++i) {
      models.add(new ModelResourceLocation(getFullName() + "SuperDark" + i, "inventory"));
    }
    return models;
  }
}
