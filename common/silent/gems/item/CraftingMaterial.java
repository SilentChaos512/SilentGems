package silent.gems.item;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.Icon;
import net.minecraftforge.oredict.ShapedOreRecipe;
import silent.gems.block.MushroomBlock;
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
            Names.CHAOS_ESSENCE_PLUS, Names.PLUME, Names.GOLDEN_PLUME, Names.AMANITA_DUST };

    public CraftingMaterial(int id) {

        super(id);
        icons = new Icon[names.length];
        setMaxStackSize(64);
        setHasSubtypes(true);
        setMaxDamage(0);
        setCreativeTab(CreativeTabs.tabMaterials);
        setUnlocalizedName(Names.CRAFTING_MATERIALS);
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
    @SuppressWarnings({ "rawtypes", "unchecked" })
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean par4) {

        list.add(LocalizationHelper.getMessageText(Names.CRAFTING_MATERIALS, EnumChatFormatting.DARK_GRAY));
        list.add(LocalizationHelper.getMessageText(names[stack.getItemDamage()]));
    }

    @Override
    @SideOnly(Side.CLIENT)
    public boolean hasEffect(ItemStack stack, int pass) {

        return stack.getItemDamage() == getStack(Names.CHAOS_ESSENCE_PLUS).getItemDamage();
    }

    @Override
    public String getUnlocalizedName(ItemStack stack) {

        return getUnlocalizedName(names[stack.getItemDamage()]);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void registerIcons(IconRegister iconRegister) {

        for (int i = 0; i < names.length; ++i) {
            icons[i] = iconRegister.registerIcon(Strings.RESOURCE_PREFIX + names[i]);
        }
    }

    @Override
    public void addRecipes() {

        GameRegistry.addRecipe(getStack(Names.ORNATE_STICK, 4), "gig", "geg", "gig", 'g', Item.ingotGold, 'i', Item.ingotIron, 'e',
                CraftingMaterial.getStack(Names.CHAOS_ESSENCE));
        GameRegistry.addRecipe(getStack(Names.MYSTERY_GOO, 1), "mmm", "mam", "mmm", 'm', Block.cobblestoneMossy, 'a', Item.appleRed);
        // RecipeHelper.addSurround(getStack(Names.YARN_BALL), new ItemStack(Item.goldNugget), Item.silk);
        GameRegistry.addRecipe(new ShapedOreRecipe(getStack(Names.YARN_BALL, 1), "sss", "sgs", "sss", 's', Item.silk, 'g',
                Strings.ORE_DICT_GEM_SHARD));
        RecipeHelper.addSurround(getStack(Names.CHAOS_ESSENCE_PLUS, 1), new ItemStack(Item.ingotGold), new Object[] { Item.redstone,
                getStack(Names.CHAOS_ESSENCE) });
        GameRegistry.addRecipe(new ShapedOreRecipe(getStack(Names.PLUME, 1), " f ", "fsf", " f ", 'f', Item.feather, 's',
                Strings.ORE_DICT_GEM_BASIC));
        RecipeHelper.addSurround(getStack(Names.GOLDEN_PLUME, 1), getStack(Names.PLUME), getStack(Names.CHAOS_ESSENCE), Item.ingotGold);
        GameRegistry.addShapelessRecipe(getStack(Names.AMANITA_DUST, 4), MushroomBlock.getStack(Names.FLY_AMANITA));
    }
}
