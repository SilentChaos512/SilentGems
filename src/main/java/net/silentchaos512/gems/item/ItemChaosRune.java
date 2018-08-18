package net.silentchaos512.gems.item;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.Loader;
import net.silentchaos512.gems.SilentGems;
import net.silentchaos512.gems.client.key.KeyTracker;
import net.silentchaos512.gems.init.ModItems;
import net.silentchaos512.gems.lib.ChaosBuff;
import net.silentchaos512.gems.lib.Names;
import net.silentchaos512.lib.registry.IAddRecipes;
import net.silentchaos512.lib.registry.RecipeMaker;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

public class ItemChaosRune extends Item implements IAddRecipes {
    private static final String NBT_BUFF = "chaos_buff";

    public ItemChaosRune() {
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World world, List<String> list, ITooltipFlag flag) {
        ChaosBuff buff = getBuff(stack);
        if (buff != null) {
            // Name
            TextFormatting nameColor = buff.getPotion() != null && buff.getPotion().isBadEffect()
                    ? TextFormatting.RED
                    : TextFormatting.GOLD;
            list.add(nameColor + buff.getLocalizedName(1));
            // Description (may not have one)
            String desc = "  " + buff.getDescription();
            if (!desc.isEmpty())
                list.add(TextFormatting.DARK_GRAY + desc);

            list.add("  " + SilentGems.i18n.itemSubText(Names.CHAOS_RUNE, "maxLevel", buff.getMaxLevel()));
            list.add("  " + SilentGems.i18n.itemSubText(Names.CHAOS_RUNE, "slotsUsed", buff.getSlotsUsed(1)));
            String varCost = buff.hasVariableCost() ? SilentGems.i18n.itemSubText(Names.CHAOS_RUNE, "variableCost") : "";
            list.add("  " + SilentGems.i18n.itemSubText(Names.CHAOS_RUNE, "chaosCost", buff.getChaosCost(1, null), varCost));

            // Debug
            if (KeyTracker.isAltDown()) {
                list.add(TextFormatting.DARK_GRAY + String.format("Key: %s", buff.getKey()));
                list.add(TextFormatting.DARK_GRAY + String.format("Potion: %s", buff.getPotion()));
                list.add(TextFormatting.DARK_GRAY + String.format("Color: %X", buff.getColor()));
            }
        }
    }

    @Override
    public void addRecipes(RecipeMaker recipes) {
        addRecipe(ChaosBuff.CAPACITY, new ItemStack(ModItems.chaosOrb, 1, ItemChaosOrb.Type.FRAGILE.ordinal()), 1);
        addRecipe(ChaosBuff.RECHARGE, ModItems.craftingMaterial.chaosCore, 1);
        addRecipe(ChaosBuff.FLIGHT, ModItems.craftingMaterial.shinyPlume, 3);
        addRecipe(ChaosBuff.SPEED, Items.SUGAR, 3);
        addRecipe(ChaosBuff.HASTE, "dustGlowstone", 3);
        addRecipe(ChaosBuff.JUMP_BOOST, ModItems.craftingMaterial.plume, 2);
        addRecipe(ChaosBuff.STRENGTH, ModItems.craftingMaterial.blazestone, 3);
        addRecipe(ChaosBuff.REGENERATION, Items.GHAST_TEAR, 3);
        addRecipe(ChaosBuff.RESISTANCE, "blockIron", 1);
        addRecipe(ChaosBuff.FIRE_RESISTANCE, Items.MAGMA_CREAM, 3);
        addRecipe(ChaosBuff.WATER_BREATHING, "blockLapis", 2);
        addRecipe(ChaosBuff.NIGHT_VISION, Items.GOLDEN_CARROT, 2);
        addRecipe(ChaosBuff.INVISIBILITY, Items.FERMENTED_SPIDER_EYE, 3);
        addRecipe(ChaosBuff.LEVITATION, Items.ENDER_EYE, 2);
        addRecipe(ChaosBuff.GLOWING, "torch", 3);
        addRecipe(ChaosBuff.SLOWNESS, "dirt", 3);
        addRecipe(ChaosBuff.MINING_FATIGUE, Blocks.STONE, 3);
        addRecipe(ChaosBuff.NAUSEA, "bone", 3);
        addRecipe(ChaosBuff.BLINDNESS, "string", 3);
        addRecipe(ChaosBuff.HUNGER, Items.ROTTEN_FLESH, 3);
        addRecipe(ChaosBuff.WEAKNESS, "feather", 3);
        addRecipe(ChaosBuff.POISON, Items.SPIDER_EYE, 2);
        addRecipe(ChaosBuff.WITHER, new ItemStack(Items.SKULL, 1, 1), 1);
        if (Loader.isModLoaded("toughasnails")) {
            addRecipe(ChaosBuff.COLD_RESISTANCE, new ItemStack(Item.getByNameOrId("toughasnails:ice_charge")), 3);
            addRecipe(ChaosBuff.HEAT_RESISTANCE, new ItemStack(Items.FIRE_CHARGE), 3);
            addRecipe(ChaosBuff.THIRST, new ItemStack(Item.getByNameOrId("toughasnails:water_bottle")), 3);
        }
    }

    private void addRecipe(ChaosBuff buff, Object obj, int count) {
        ItemStack result = new ItemStack(this);
        String line1 = count > 1 ? "coc" : "c c";
        String line2 = count != 2 ? "ror" : "r r";
        String line3 = line1;
        setBuff(result, buff);
        String name = "chaos_rune_" + buff.getKey().replaceFirst(SilentGems.RESOURCE_PREFIX, "").replaceAll(":", "_");
        SilentGems.registry.getRecipeMaker().addShapedOre(name, result, line1, line2, line3, 'r', "dustRedstone",
                'c', ModItems.craftingMaterial.chaosEssenceEnriched, 'o', obj);
    }

    @Override
    public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> list) {
        if (!isInCreativeTab(tab)) return;

        for (ChaosBuff buff : ChaosBuff.getAllBuffs()) {
            list.add(setBuff(new ItemStack(this), buff));
        }
    }

    @Nullable
    public ChaosBuff getBuff(@Nonnull ItemStack stack) {
        if (!stack.hasTagCompound()) return null;
        return ChaosBuff.byKey(stack.getTagCompound().getString(NBT_BUFF));
    }

    public ItemStack setBuff(@Nonnull ItemStack stack, ChaosBuff buff) {
        if (!stack.hasTagCompound())
            stack.setTagCompound(new NBTTagCompound());
        stack.getTagCompound().setString(NBT_BUFF, buff.getKey());
        return stack;
    }

    @Override
    public EnumRarity getRarity(ItemStack stack) {
        return EnumRarity.RARE;
    }
}
