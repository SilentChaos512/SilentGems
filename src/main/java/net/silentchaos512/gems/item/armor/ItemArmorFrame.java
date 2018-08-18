package net.silentchaos512.gems.item.armor;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.silentchaos512.gems.SilentGems;
import net.silentchaos512.gems.api.lib.EnumMaterialTier;
import net.silentchaos512.gems.init.ModItems;
import net.silentchaos512.lib.registry.IAddRecipes;
import net.silentchaos512.lib.registry.ICustomModel;
import net.silentchaos512.lib.registry.RecipeMaker;

import javax.annotation.Nullable;
import java.util.List;

public class ItemArmorFrame extends Item implements IAddRecipes, ICustomModel {
    public ItemArmorFrame() {
        // Meta bits: TTAA
        // Where T = tier, A = armor type
        // For armor type, 0 = Helmet, 1 = Chestplate, etc. (opposite of slot index)

        setHasSubtypes(true);
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        tooltip.add(SilentGems.i18n.miscText("legacyItem"));
    }

    public ItemStack getFrameForArmorPiece(ItemArmor itemArmor, EnumMaterialTier tier) {
        return getFrameForArmorPiece(itemArmor.armorType, tier);
    }

    public ItemStack getFrameForArmorPiece(EntityEquipmentSlot slot, EnumMaterialTier tier) {
        int type = 3 - slot.getIndex();
        return new ItemStack(this, 1, type | (tier.ordinal() << 2));
    }

    public EnumMaterialTier getTier(ItemStack stack) {
        int index = stack.getItemDamage() >> 2;
        return EnumMaterialTier.values()[MathHelper.clamp(index, 0, EnumMaterialTier.values().length - 1)];
    }

    public Item getOutputItem(ItemStack stack) {
        return getOutputItem(stack.getItemDamage() & 3);
    }

    private Item getOutputItem(int armorType) {
        switch (armorType) {
            case 0:
                return ModItems.gemHelmet;
            case 1:
                return ModItems.gemChestplate;
            case 2:
                return ModItems.gemLeggings;
            case 3:
                return ModItems.gemBoots;
            default:
                return null;
        }
    }

    @Override
    public void addRecipes(RecipeMaker recipes) {
        ItemStack lattice;

        for (int tier = 0; tier < EnumMaterialTier.values().length; ++tier) {
            lattice = tier == 0 ? ModItems.craftingMaterial.armorLatticeMundane
                    : tier == 1 ? ModItems.craftingMaterial.armorLatticeRegular
                    : tier == 2 ? ModItems.craftingMaterial.armorLatticeSuper : ItemStack.EMPTY;

            if (!lattice.isEmpty())
                for (int type = 0; type < 4; ++type)
                    addRecipe(recipes, "armor_frame_" + tier, new ItemStack(this, 1, type + (tier << 2)), lattice, type);
        }
    }

    private void addRecipe(RecipeMaker recipes, String name, ItemStack output, ItemStack lattice, int armorType) {
        switch (armorType) {
            case 0:
                recipes.addShaped(name + "_helmet", output, "lll", "l l", 'l', lattice);
                break;
            case 1:
                recipes.addShaped(name + "_chestplate", output, "l l", "lll", "lll", 'l', lattice);
                break;
            case 2:
                recipes.addShaped(name + "_leggings", output, "lll", "l l", "l l", 'l', lattice);
                break;
            case 3:
                recipes.addShaped(name + "_boots", output, "l l", "l l", 'l', lattice);
                break;
        }
    }

    @Override
    public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> items) {
        if (!isInCreativeTab(tab)) return;
        for (int i = 0; i < 12; ++i) {
            items.add(new ItemStack(this, 1, i));
        }
    }

    @Override
    public void registerModels() {
        for (int i = 0; i < 12; ++i) {
            SilentGems.registry.setModel(this, i, "armorframe" + i);
        }
    }

    @Override
    public String getTranslationKey(ItemStack stack) {
        return super.getTranslationKey(stack) + stack.getItemDamage();
    }
}
