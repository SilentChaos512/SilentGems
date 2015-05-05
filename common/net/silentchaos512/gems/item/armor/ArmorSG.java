package net.silentchaos512.gems.item.armor;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.EnumHelper;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.silentchaos512.gems.SilentGems;
import net.silentchaos512.gems.core.registry.IAddRecipe;
import net.silentchaos512.gems.core.util.LocalizationHelper;
import net.silentchaos512.gems.lib.Strings;
import cpw.mods.fml.common.registry.GameRegistry;

public class ArmorSG extends ItemArmor implements IAddRecipe {

    public final static ArmorMaterial materialCotton = EnumHelper.addArmorMaterial("gemsCotton", 4, new int[] { 1, 2, 2, 1 }, 17);
//    public final static ArmorMaterial materialHeadphones = EnumHelper.addArmorMaterial("headphones", 12, new int[] { 5, 1, 2, 0 }, 20);

    private final String itemName;

    public ArmorSG(ArmorMaterial material, int renderIndex, int armorType, String name) {

        super(material, renderIndex, armorType);

        itemName = name;
        setCreativeTab(SilentGems.tabSilentGems);
        setUnlocalizedName(name);
    }

    @Override
    public void addRecipes() {

        if (this.getArmorMaterial() == materialCotton) {
            addArmorRecipe("materialCotton", this.armorType);
        }
    }

    private void addArmorRecipe(Object material, int armorType) {

        if (armorType == 0) {
            GameRegistry.addRecipe(new ShapedOreRecipe(this, true, new Object[] { "mmm", "m m", 'm', material }));
        }
        else if (armorType == 1) {
            GameRegistry.addRecipe(new ShapedOreRecipe(this, true, new Object[] { "m m", "mmm", "mmm", 'm', material }));
        }
        else if (armorType == 2) {
            GameRegistry.addRecipe(new ShapedOreRecipe(this, true, new Object[] { "mmm", "m m", "m m", 'm', material }));
        }
        else if (armorType == 3) {
            GameRegistry.addRecipe(new ShapedOreRecipe(this, true, new Object[] { "m m", "m m", 'm', material }));
        }
    }

    @Override
    public void addOreDict() {

        // TODO Auto-generated method stub

    }

    @Override
    public String getUnlocalizedName(ItemStack stack) {

        return LocalizationHelper.ITEM_PREFIX + itemName;
    }

    @Override
    public void registerIcons(IIconRegister reg) {

        itemIcon = reg.registerIcon(Strings.RESOURCE_PREFIX + itemName);
    }
}
