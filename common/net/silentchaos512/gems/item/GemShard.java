package net.silentchaos512.gems.item;

import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraftforge.oredict.OreDictionary;
import net.silentchaos512.gems.core.util.RecipeHelper;
import net.silentchaos512.gems.lib.EnumGem;
import net.silentchaos512.gems.lib.Names;
import net.silentchaos512.gems.lib.Strings;
import cpw.mods.fml.common.registry.GameRegistry;

public class GemShard extends ItemSG {

  public GemShard() {

    icons = new IIcon[EnumGem.all().length];
    setMaxStackSize(64);
    setHasSubtypes(true);
    setHasGemSubtypes(true);
    setUnlocalizedName(Names.GEM_SHARD);
    setMaxDamage(0);
  }

  @Override
  public void addRecipes() {

    for (int i = 0; i < EnumGem.all().length; ++i) {
      ItemStack shard = new ItemStack(this, 1, i);
      ItemStack gem = EnumGem.all()[i].getItem();
      RecipeHelper.addCompressionRecipe(shard, gem, 9);
    }
  }

  @Override
  public void addOreDict() {

    OreDictionary.registerOre("nuggetRuby", new ItemStack(this, 1, EnumGem.RUBY.id));
    OreDictionary.registerOre("nuggetGarnet", new ItemStack(this, 1, EnumGem.GARNET.id));
    OreDictionary.registerOre("nuggetTopaz", new ItemStack(this, 1, EnumGem.TOPAZ.id));
    OreDictionary.registerOre("nuggetHeliodor", new ItemStack(this, 1, EnumGem.HELIODOR.id));
    OreDictionary.registerOre("nuggetPeridot", new ItemStack(this, 1, EnumGem.PERIDOT.id));
    OreDictionary.registerOre("nuggetBeryl", new ItemStack(this, 1, EnumGem.EMERALD.id));
    OreDictionary.registerOre("nuggetAquamarine", new ItemStack(this, 1, EnumGem.AQUAMARINE.id));
    OreDictionary.registerOre("nuggetSapphire", new ItemStack(this, 1, EnumGem.SAPPHIRE.id));
    OreDictionary.registerOre("nuggetIolite", new ItemStack(this, 1, EnumGem.IOLITE.id));
    OreDictionary.registerOre("nuggetAmethyst", new ItemStack(this, 1, EnumGem.AMETHYST.id));
    OreDictionary.registerOre("nuggetMorganite", new ItemStack(this, 1, EnumGem.MORGANITE.id));
    OreDictionary.registerOre("nuggetOnyx", new ItemStack(this, 1, EnumGem.ONYX.id));

    for (int i = 0; i < EnumGem.all().length; ++i) {
      OreDictionary.registerOre(Strings.ORE_DICT_GEM_SHARD, new ItemStack(this, 1, i));
    }
  }
}
