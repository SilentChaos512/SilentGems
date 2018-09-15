/*
 * Silent's Gems -- ItemChargingAgent
 * Copyright (C) 2018 SilentChaos512
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation version 3
 * of the License.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package net.silentchaos512.gems.item;

import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Items;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;
import net.silentchaos512.gems.SilentGems;
import net.silentchaos512.lib.item.IColoredItem;
import net.silentchaos512.lib.registry.IAddRecipes;
import net.silentchaos512.lib.registry.ICustomModel;
import net.silentchaos512.lib.registry.RecipeMaker;

import javax.annotation.Nullable;
import java.util.List;

public class ItemChargingAgent extends Item implements IAddRecipes, ICustomModel, IColoredItem {
    private static final int[] COLORS = {0xEEDBD4, 0xF5E2AE, 0xF3E6FF};
    private static final int SUBTYPES = 3;

    public ItemChargingAgent() {
        this.setHasSubtypes(true);
    }

    public int getTier(ItemStack stack) {
        return stack.getItem() == this ? stack.getItemDamage() + 1 : 0;
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        String tier = stack.getItemDamage() < SUBTYPES ? "" + (1 + stack.getItemDamage())
                : SilentGems.i18n.subText(this, "tier.invalid");
        tooltip.add(SilentGems.i18n.subText(this, "tier", tier));
    }

    @Override
    public EnumRarity getRarity(ItemStack stack) {
        return EnumRarity.RARE;
    }

    @Override
    public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> items) {
        if (!this.isInCreativeTab(tab)) return;
        for (int i = 0; i < SUBTYPES; ++i)
            items.add(new ItemStack(this, 1, i));
    }

    @Override
    public void addRecipes(RecipeMaker recipes) {
        recipes.addShapedOre("charging_agent_tier_1", new ItemStack(this, 2, 0),
                "cgc", "cbc", "cgc",
                'c', "gemChaos", 'g', "dustGlowstone", 'b', CraftingItems.BLAZESTONE.getStack());
        recipes.addShapedOre("charging_agent_tier_2", new ItemStack(this, 2, 1),
                "cfc", "aea", "cfc",
                'a', new ItemStack(this, 1, 0), 'c', "gemChaos", 'f', Items.CHORUS_FRUIT, 'e', Items.ENDER_EYE);
        recipes.addShapedOre("charging_agent_tier_3", new ItemStack(this, 2, 2),
                "cec", "ana", "cec",
                'a', new ItemStack(this, 1, 1), 'c', "gemChaos", 'e', "gemEnderEssence", 'n', CraftingItems.NETHER_SHARD.getStack());
    }

    @Override
    public void registerModels() {
        for (int i = 0; i < SUBTYPES; ++i)
            SilentGems.registry.setModel(this, i, "charging_agent");
    }

    @Override
    public IItemColor getColorHandler() {
        return (stack, tintIndex) -> stack.getItemDamage() < SUBTYPES ? COLORS[stack.getItemDamage()] : 0xFFFFFF;
    }
}
