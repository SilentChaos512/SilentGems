package net.silentchaos512.gems.item;

import com.google.common.collect.Lists;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.silentchaos512.gems.SilentGems;
import net.silentchaos512.gems.api.tool.part.ToolPart;
import net.silentchaos512.gems.api.tool.part.ToolPartRegistry;
import net.silentchaos512.gems.api.tool.part.ToolPartTip;
import net.silentchaos512.lib.registry.IAddRecipes;
import net.silentchaos512.lib.registry.ICustomModel;
import net.silentchaos512.lib.registry.RecipeMaker;

import javax.annotation.Nullable;
import java.util.List;

public class ItemTipUpgrade extends Item implements IAddRecipes, ICustomModel {
    private static final String[] NAMES = new String[]{"upgradeirontip", "upgradegoldtip", "upgradediamondtip", "upgradeemeraldtip"};

    // For guide book...
    public static List<IRecipe> RECIPES = Lists.newArrayList();

    public ItemTipUpgrade() {
        setHasSubtypes(true);
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World world, List<String> list, ITooltipFlag flag) {
        ToolPartTip part = (ToolPartTip) ToolPartRegistry.fromStack(stack);
        if (part != null) {
            list.add(SilentGems.i18n.subText(this, "willReplace"));

            list.add(SilentGems.i18n.subText(this, "harvestLevel", part.getHarvestLevel()));

            tooltipLine(list, "durability", part.getDurability());
            tooltipLine(list, "harvestSpeed", part.getHarvestSpeed());
            tooltipLine(list, "meleeDamage", part.getMeleeDamage());
            tooltipLine(list, "magicDamage", part.getMagicDamage());
        }
    }

    private void tooltipLine(List<String> list, String key, Number value) {
        if (value.floatValue() == 0) return;
        String numberString = (value.floatValue() < 0 ? "-" : "+") + String.format(value instanceof Integer ? "%d" : "%.1f", value);
        String line = SilentGems.i18n.subText(this, key, numberString);
        list.add(String.format(line, numberString));
    }

    public ItemStack applyToTool(ItemStack tool, ItemStack upgrade) {
        ToolPart part = ToolPartRegistry.fromStack(upgrade);
        if (part == null) {
            return ItemStack.EMPTY;
        }

        ItemStack result = tool.copy();
        ToolHelper.setConstructionTip(result, part);
        ToolHelper.recalculateStats(result);
        return result;
    }

    @Override
    public void addRecipes(RecipeMaker recipes) {
        ItemStack base = CraftingItems.UPGRADE_BASE.getStack();
        RECIPES.add(recipes.makeShapelessOre("tip_upgrade_iron", new ItemStack(this, 1, 0), "ingotIron", base));
        RECIPES.add(recipes.makeShapelessOre("tip_upgrade_gold", new ItemStack(this, 1, 1), "ingotGold", base));
        RECIPES.add(recipes.makeShapelessOre("tip_upgrade_diamond", new ItemStack(this, 1, 2), "gemDiamond", base));
        RECIPES.add(recipes.makeShapelessOre("tip_upgrade_emerald", new ItemStack(this, 1, 3), "gemEmerald", base));
    }

    @Override
    public String getTranslationKey(ItemStack stack) {
        return "item." + SilentGems.MOD_ID + "." + NAMES[MathHelper.clamp(stack.getItemDamage(), 0, NAMES.length - 1)];
    }

    @Override
    public void registerModels() {
        for (int i = 0; i < NAMES.length; ++i) {
            SilentGems.registry.setModel(this, i, NAMES[i]);
        }
    }

    @Override
    public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> items) {
        if (!isInCreativeTab(tab)) return;
        for (int i = 0; i < NAMES.length; ++i) {
            items.add(new ItemStack(this, 1, i));
        }
    }
}
