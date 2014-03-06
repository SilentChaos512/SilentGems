package silent.gems.item;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Icon;
import net.minecraftforge.oredict.OreDictionary;
import silent.gems.configuration.Config;
import silent.gems.core.util.LocalizationHelper;
import silent.gems.lib.EnumGem;
import silent.gems.lib.Names;
import silent.gems.lib.Strings;
import cpw.mods.fml.common.registry.GameRegistry;


public class GemShard extends ItemSG {

    public GemShard(int id) {
        super(id);
        icons = new Icon[EnumGem.all().length];
        setMaxStackSize(64);
        setHasSubtypes(true);
        setHasGemSubtypes(true);
        setUnlocalizedName(Names.GEM_SHARD);
        setMaxDamage(0);
        setCreativeTab(CreativeTabs.tabMaterials);
    }

    @Override
    public void addRecipes() {
        
        ItemStack s;
        for (int i = 0; i < EnumGem.all().length; ++i) {
            s = new ItemStack(this, 1, i);
            // 9 shards --> 1 gem
            GameRegistry.addShapelessRecipe(EnumGem.all()[i].getItem(), s, s, s, s, s, s, s, s, s);
            // 1 gem --> 9 shards
            GameRegistry.addShapelessRecipe(new ItemStack(this, 9, i), EnumGem.all()[i].getItem());
        }
    }
    
    @Override
    public void addOreDict() {
        
        for (int i = 0; i < EnumGem.all().length; ++i) {
            OreDictionary.registerOre(Strings.ORE_DICT_GEM_SHARD, new ItemStack(this, 1, i));
        }
    }
}
