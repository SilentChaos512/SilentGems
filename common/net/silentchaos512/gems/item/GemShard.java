package net.silentchaos512.gems.item;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.silentchaos512.gems.lib.EnumGem;
import net.silentchaos512.gems.lib.Names;
import net.silentchaos512.gems.lib.Strings;

public class GemShard extends ItemSG {

  public GemShard() {

    setMaxStackSize(64);
    setHasSubtypes(true);
    setHasGemSubtypes(true);
    setUnlocalizedName(Names.GEM_SHARD);
    setMaxDamage(0);
  }

  @Override
  public void addRecipes() {

    for (EnumGem gem : EnumGem.values()) {
      // Shards --> Gem
      GameRegistry.addRecipe(
          new ShapedOreRecipe(gem.getItem(), "sss", "sss", "sss", 's', gem.getShardOreName()));
      // Gems --> Shards
      ItemStack shards = gem.getShard();
      shards.stackSize = 9;
      GameRegistry.addRecipe(new ShapedOreRecipe(shards, "g", 'g', gem.getItemOreName()));
    }
  }

  @Override
  public void addOreDict() {

    ItemStack stack;
    for (EnumGem gem : EnumGem.values()) {
      stack = gem.getShard();
      OreDictionary.registerOre(gem.getShardOreName(), stack);
    }
  }
}
