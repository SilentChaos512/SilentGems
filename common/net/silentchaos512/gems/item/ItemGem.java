package net.silentchaos512.gems.item;

import java.util.List;

import org.lwjgl.input.Keyboard;

import com.google.common.collect.Lists;

import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;
import net.silentchaos512.gems.SilentGems;
import net.silentchaos512.gems.api.lib.EnumMaterialGrade;
import net.silentchaos512.gems.api.lib.EnumMaterialTier;
import net.silentchaos512.gems.client.key.KeyTracker;
import net.silentchaos512.gems.lib.EnumGem;
import net.silentchaos512.gems.lib.Names;
import net.silentchaos512.lib.item.ItemSL;
import net.silentchaos512.lib.util.LocalizationHelper;
import net.silentchaos512.lib.util.RecipeHelper;

public class ItemGem extends ItemSL {

  public ItemGem() {

    super(64, SilentGems.MODID, Names.GEM);
  }

  @Override
  public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean advanced) {

    EnumGem gem = EnumGem.getFromStack(stack);
    boolean controlDown = KeyTracker.isControlDown();

    if (controlDown && (gem == EnumGem.RUBY || gem == EnumGem.BERYL || gem == EnumGem.SAPPHIRE
        || gem == EnumGem.TOPAZ)) {
      list.add(SilentGems.localizationHelper.getItemSubText(itemName, "original4"));
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
      ItemStack shards = gem.getShard();
      shards.setCount(9);
      GameRegistry.addRecipe(new ShapelessOreRecipe(shards, gem.getItemOreName()));
    }
  }

  @Override
  public void addOreDict() {

    for (EnumGem gem : EnumGem.values()) {
      OreDictionary.registerOre(gem.getItemOreName(), gem.getItem());
      OreDictionary.registerOre(gem.getItemSuperOreName(), gem.getItemSuper());
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
