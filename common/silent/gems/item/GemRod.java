package silent.gems.item;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.OreDictionary;
import silent.gems.lib.EnumGem;
import silent.gems.lib.Names;
import silent.gems.lib.Strings;

public class GemRod extends ItemSG {

  public GemRod() {

    super(EnumGem.count());
    setMaxStackSize(64);
    setHasSubtypes(true);
    setHasGemSubtypes(true);
    setUnlocalizedName(Names.GEM_ROD);
    setMaxDamage(0);
  }

  @Override
  public void addRecipes() {

    for (int i = 0; i < EnumGem.count(); ++i) {
      GameRegistry.addShapelessRecipe(new ItemStack(this, 1, i),
          CraftingMaterial.getStack(Names.ORNATE_STICK), EnumGem.byId(i).getItem());
    }
  }

  @Override
  public void addOreDict() {

    for (int i = 0; i < EnumGem.count(); ++i) {
      OreDictionary.registerOre(Strings.ORE_DICT_STICK_FANCY, new ItemStack(this, 1, i));
    }
  }
}
