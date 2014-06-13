package silent.gems.item;

import java.util.List;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IIcon;
import net.minecraftforge.oredict.ShapedOreRecipe;
import silent.gems.core.registry.SRegistry;
import silent.gems.core.util.LocalizationHelper;
import silent.gems.core.util.RecipeHelper;
import silent.gems.lib.Names;
import silent.gems.lib.Strings;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class CraftingMaterial extends ItemSG {

    public static final String[] names = { Names.ORNATE_STICK, Names.MYSTERY_GOO, Names.YARN_BALL, Names.CHAOS_ESSENCE,
            Names.CHAOS_ESSENCE_PLUS, Names.PLUME, Names.GOLDEN_PLUME, Names.CHAOS_SHARD, Names.CHAOS_CAPACITOR, Names.CHAOS_BOOSTER,
            Names.RAWHIDE_BONE };

    public CraftingMaterial() {

        super();

        icons = new IIcon[names.length];
        setMaxStackSize(64);
        setHasSubtypes(true);
        setMaxDamage(0);
        setCreativeTab(CreativeTabs.tabMaterials);
        setUnlocalizedName(Names.CRAFTING_MATERIALS);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean par4) {

        list.add(EnumChatFormatting.DARK_GRAY + LocalizationHelper.getItemDescription(Names.CRAFTING_MATERIALS, 0));
        list.add(EnumChatFormatting.ITALIC + LocalizationHelper.getItemDescription(names[stack.getItemDamage()], 0));
    }

    @Override
    public void addRecipes() {

        // Ornate stick
        GameRegistry.addRecipe(getStack(Names.ORNATE_STICK, 4), "gig", "geg", "gig", 'g', Items.gold_ingot, 'i', Items.iron_ingot, 'e',
                CraftingMaterial.getStack(Names.CHAOS_ESSENCE));
        // Mystery goo
        GameRegistry.addRecipe(getStack(Names.MYSTERY_GOO, 1), "mmm", "mam", "mmm", 'm', Blocks.mossy_cobblestone, 'a', Items.apple);
        // Yarn ball
        GameRegistry.addRecipe(new ShapedOreRecipe(getStack(Names.YARN_BALL, 1), "sss", "sgs", "sss", 's', Items.string, 'g',
                Strings.ORE_DICT_GEM_SHARD));
        // Refined chaos essence
        RecipeHelper.addSurround(getStack(Names.CHAOS_ESSENCE_PLUS, 1), new ItemStack(Items.glowstone_dust), new Object[] { Items.redstone,
                getStack(Names.CHAOS_ESSENCE) });
        // Plume
        GameRegistry.addRecipe(new ShapedOreRecipe(getStack(Names.PLUME, 1), "fff", "fsf", "fff", 'f', Items.feather, 's',
                Strings.ORE_DICT_GEM_BASIC));
        // Golden plume
        RecipeHelper.addSurround(getStack(Names.GOLDEN_PLUME, 1), getStack(Names.PLUME), getStack(Names.CHAOS_ESSENCE), Items.gold_ingot);
        // Chaos Shard
        GameRegistry.addShapedRecipe(getStack(Names.CHAOS_SHARD, 24), "ccc", "cnc", "ccc", 'c', getStack(Names.CHAOS_ESSENCE_PLUS), 'n',
                Items.nether_star);
        // Chaos Capacitor
        GameRegistry.addShapedRecipe(getStack(Names.CHAOS_CAPACITOR, 3), "srs", "ses", "srs", 's', getStack(Names.CHAOS_SHARD), 'r',
                Items.redstone, 'e', Items.emerald);
        // Chaos Booster
        GameRegistry.addShapedRecipe(getStack(Names.CHAOS_BOOSTER, 3), "sgs", "ses", "sgs", 's', getStack(Names.CHAOS_SHARD), 'g',
                Items.glowstone_dust, 'e', Items.emerald);
        // Rawhide bone
        GameRegistry.addShapedRecipe(getStack(Names.RAWHIDE_BONE, 1), " l ", "lbl", " l ", 'l', Items.leather, 'b', Items.bone);
    }

    public static ItemStack getStack(String name) {

        for (int i = 0; i < names.length; ++i) {
            if (names[i].equals(name)) {
                return new ItemStack(SRegistry.getItem(Names.CRAFTING_MATERIALS), 1, i);
            }
        }

        return null;
    }

    public static ItemStack getStack(String name, int count) {

        for (int i = 0; i < names.length; ++i) {
            if (names[i].equals(name)) {
                return new ItemStack(SRegistry.getItem(Names.CRAFTING_MATERIALS), count, i);
            }
        }

        return null;
    }

    @Override
    public String getUnlocalizedName(ItemStack stack) {

        return getUnlocalizedName(names[stack.getItemDamage()]);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public boolean hasEffect(ItemStack stack, int pass) {

        return stack.getItemDamage() == getStack(Names.CHAOS_ESSENCE_PLUS).getItemDamage();
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void registerIcons(IIconRegister iconRegister) {

        for (int i = 0; i < names.length; ++i) {
            icons[i] = iconRegister.registerIcon(Strings.RESOURCE_PREFIX + names[i]);
        }
    }
}
