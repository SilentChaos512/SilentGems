package net.silentchaos512.gems.item;

import java.util.List;
import java.util.Map;

import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.oredict.OreDictionary;
import net.silentchaos512.gems.SilentGems;
import net.silentchaos512.gems.client.key.KeyTracker;
import net.silentchaos512.gems.config.GemsConfigHC;
import net.silentchaos512.gems.lib.EnumGem;
import net.silentchaos512.gems.lib.Names;
import net.silentchaos512.lib.item.ItemSL;
import net.silentchaos512.lib.registry.RecipeMaker;
import net.silentchaos512.lib.util.StackHelper;

public class ItemGem extends ItemSL {

  public ItemGem() {

    super(EnumGem.values().length, SilentGems.MODID, Names.GEM);
  }

  @Override
  public void clAddInformation(ItemStack stack, World world, List list, boolean advanced) {

    EnumGem gem = EnumGem.getFromStack(stack);
    boolean controlDown = KeyTracker.isControlDown();

    if (controlDown && (gem == EnumGem.RUBY || gem == EnumGem.BERYL || gem == EnumGem.SAPPHIRE
        || gem == EnumGem.TOPAZ)) {
      list.add(SilentGems.localizationHelper.getItemSubText(itemName, "original4"));
    }
  }

  @Override
  public void addRecipes(RecipeMaker recipes) {

    String centerItem = GemsConfigHC.HARD_SUPER_GEMS ? "gemEnderEssence" : "dustGlowstone";
    for (EnumGem gem : EnumGem.values()) {
      // Supercharged gems
      recipes.addShapedOre("gem_super_" + gem.name(), gem.getItemSuper(), "cgc", "cdc", "cgc", 'g',
          gem.getItem(), 'd', centerItem, 'c', "gemChaos");
      // Gems <--> shards
      recipes.addCompression("gem_" + gem.name(), gem.getShard(), gem.getItem(), 9);
      ItemStack shards = gem.getShard();
      StackHelper.setCount(shards, 9);
      recipes.addShapelessOre("gem_shard_" + gem.name() + "_oredict", shards, gem.getItemOreName());
    }
  }

  @Override
  public void addOreDict() {

    for (EnumGem gem : EnumGem.values()) {
      OreDictionary.registerOre(gem.getItemOreName(), gem.getItem());
      OreDictionary.registerOre(gem.getItemSuperOreName(), gem.getItemSuper());
    }
  }
}
