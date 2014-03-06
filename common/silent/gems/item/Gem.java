package silent.gems.item;

import java.util.List;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Icon;
import net.minecraftforge.oredict.OreDictionary;
import silent.gems.core.registry.SRegistry;
import silent.gems.core.util.LocalizationHelper;
import silent.gems.lib.EnumGem;
import silent.gems.lib.Names;
import silent.gems.lib.Strings;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class Gem extends ItemSG {

    public Gem(int id) {

        super(id);
        icons = new Icon[EnumGem.all().length];
        setMaxStackSize(64);
        setHasSubtypes(true);
        setHasGemSubtypes(true);
        setMaxDamage(0);
        setCreativeTab(CreativeTabs.tabMaterials);
        setUnlocalizedName(Names.GEM_ITEM);
    }

    @Override
    public boolean hasEffect(ItemStack stack, int pass) {

        return (stack.getItemDamage() & 16) == 16;
    }

    @Override
    public EnumRarity getRarity(ItemStack stack) {

        return (stack.getItemDamage() & 16) == 16 ? EnumRarity.rare : EnumRarity.common;
    }

    @Override
    public String getUnlocalizedName(ItemStack stack) {

        return LocalizationHelper.GEMS_PREFIX + stack.getItemDamage();
    }

    @Override
    @SideOnly(Side.CLIENT)
    public Icon getIconFromDamage(int meta) {

        return icons[meta & 15];
    }

    @Override
    @SuppressWarnings({ "rawtypes", "unchecked" })
    @SideOnly(Side.CLIENT)
    public void getSubItems(int itemID, CreativeTabs tabs, List list) {

        for (int i = 0; i < icons.length; ++i) {
            list.add(new ItemStack(this, 1, i));
            list.add(new ItemStack(this, 1, i | 16));
        }
    }

    @Override
    public void addRecipes() {

        /*
         * Supercharged gems
         */
        ItemStack chaosEssence = CraftingMaterial.getStack(Names.CHAOS_ESSENCE);
        for (int i = 0; i < icons.length; ++i) {
            GameRegistry.addShapedRecipe(new ItemStack(this, 1, i | 16), "ere", "ege", "ere", 'e', chaosEssence, 'r', Item.redstone, 'g',
                    new ItemStack(SRegistry.getItem(Names.GEM_ITEM), 1, i));
        }
    }

    @Override
    public void addOreDict() {

        OreDictionary.registerOre("gemRuby", new ItemStack(this, 1, EnumGem.RUBY.id));
        OreDictionary.registerOre("gemGarnet", new ItemStack(this, 1, EnumGem.GARNET.id));
        OreDictionary.registerOre("gemTopaz", new ItemStack(this, 1, EnumGem.TOPAZ.id));
        OreDictionary.registerOre("gemHeliodor", new ItemStack(this, 1, EnumGem.HELIODOR.id));
        OreDictionary.registerOre("gemPeridot", new ItemStack(this, 1, EnumGem.PERIDOT.id));
        OreDictionary.registerOre("gemEmerald", new ItemStack(this, 1, EnumGem.EMERALD.id));
        OreDictionary.registerOre("gemAquamarine", new ItemStack(this, 1, EnumGem.AQUAMARINE.id));
        OreDictionary.registerOre("gemSapphire", new ItemStack(this, 1, EnumGem.SAPPHIRE.id));
        OreDictionary.registerOre("gemIolite", new ItemStack(this, 1, EnumGem.IOLITE.id));
        OreDictionary.registerOre("gemAmethyst", new ItemStack(this, 1, EnumGem.AMETHYST.id));
        OreDictionary.registerOre("gemMorganite", new ItemStack(this, 1, EnumGem.MORGANITE.id));
        OreDictionary.registerOre("gemOnyx", new ItemStack(this, 1, EnumGem.ONYX.id));

        for (int i = 0; i < EnumGem.all().length; ++i) {
            OreDictionary.registerOre(Strings.ORE_DICT_GEM_BASIC, new ItemStack(this, 1, i));
        }
    }
}
