package silent.gems.item;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.OreDictionary;
import silent.gems.core.util.RecipeHelper;
import silent.gems.lib.EnumGem;
import silent.gems.lib.Names;
import silent.gems.lib.Strings;

public class GemShard extends ItemSG {

  public GemShard() {

    super(EnumGem.count());

    setMaxStackSize(64);
    setHasSubtypes(true);
    setHasGemSubtypes(true);
    setUnlocalizedName(Names.GEM_SHARD);
    setMaxDamage(0);
  }

  @Override
  public void addRecipes() {

    ItemStack s;
    for (int i = 0; i < EnumGem.count(); ++i) {
      ItemStack gem = new ItemStack(ModItems.gem, 1, i);
      ItemStack gemShard = new ItemStack(this, 1, i);
      RecipeHelper.addCompressionRecipe(gemShard, gem, 9);
    }
  }

  @Override
  public void addOreDict() {

    OreDictionary.registerOre("nuggetRuby", new ItemStack(this, 1, EnumGem.RUBY.getId()));
    OreDictionary.registerOre("nuggetGarnet", new ItemStack(this, 1, EnumGem.GARNET.getId()));
    OreDictionary.registerOre("nuggetTopaz", new ItemStack(this, 1, EnumGem.TOPAZ.getId()));
    OreDictionary.registerOre("nuggetHeliodor", new ItemStack(this, 1, EnumGem.HELIODOR.getId()));
    OreDictionary.registerOre("nuggetPeridot", new ItemStack(this, 1, EnumGem.PERIDOT.getId()));
    OreDictionary.registerOre("nuggetEmerald", new ItemStack(this, 1, EnumGem.EMERALD.getId()));
    OreDictionary.registerOre("nuggetAquamarine", new ItemStack(this, 1, EnumGem.AQUAMARINE.getId()));
    OreDictionary.registerOre("nuggetSapphire", new ItemStack(this, 1, EnumGem.SAPPHIRE.getId()));
    OreDictionary.registerOre("nuggetIolite", new ItemStack(this, 1, EnumGem.IOLITE.getId()));
    OreDictionary.registerOre("nuggetAmethyst", new ItemStack(this, 1, EnumGem.AMETHYST.getId()));
    OreDictionary.registerOre("nuggetMorganite", new ItemStack(this, 1, EnumGem.MORGANITE.getId()));
    OreDictionary.registerOre("nuggetOnyx", new ItemStack(this, 1, EnumGem.ONYX.getId()));

    for (int i = 0; i < EnumGem.count(); ++i) {
      OreDictionary.registerOre(Strings.ORE_DICT_GEM_SHARD, new ItemStack(this, 1, i));
    }
  }
}
