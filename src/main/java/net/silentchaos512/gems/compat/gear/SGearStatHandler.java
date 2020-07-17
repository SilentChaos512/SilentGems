/*
 * Silent's Gems -- SGearStatHandler
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

package net.silentchaos512.gems.compat.gear;

import com.google.common.collect.ImmutableMap;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.silentchaos512.gear.api.event.GetMaterialStatsEvent;
import net.silentchaos512.gear.api.event.GetStatModifierEvent;
import net.silentchaos512.gear.api.stats.ItemStat;
import net.silentchaos512.gear.api.stats.ItemStats;
import net.silentchaos512.gear.api.stats.StatInstance;
import net.silentchaos512.gems.SilentGems;
import net.silentchaos512.gems.init.GemsEnchantments;

import java.util.Collections;
import java.util.EnumSet;
import java.util.Map;
import java.util.Set;
import java.util.function.ToDoubleFunction;

/**
 * Handles the GetStatModifierEvent to apply stat bonuses to Silent Gear materials with the
 * Supercharged enchantment.
 */
public class SGearStatHandler {
    public static final ItemStat CHARGEABILITY = new ItemStat(1f, 0f, 100f, TextFormatting.GOLD, new ItemStat.Properties().hidden());

    private static final Map<ItemStat, ToDoubleFunction<ChargedProperties>> BOOSTED_STATS =
            ImmutableMap.<ItemStat, ToDoubleFunction<ChargedProperties>>builder()
                    //@formatter:off
                    .put(ItemStats.DURABILITY,        prop -> prop.originalStat * Math.pow(2.0, prop.chargeValue))
                    .put(ItemStats.ARMOR_DURABILITY,  prop -> prop.originalStat * Math.pow(1.8, prop.chargeValue))
                    .put(ItemStats.ENCHANTABILITY,    prop -> prop.originalStat * (1 + prop.superchargedLevel * (Math.sqrt(prop.chargeability) - 1)))
                    .put(ItemStats.RARITY,            prop -> prop.originalStat + prop.superchargedLevel * 10)
                    .put(ItemStats.HARVEST_LEVEL,     prop -> prop.originalStat + prop.superchargedLevel)
                    .put(ItemStats.HARVEST_SPEED,     prop -> prop.originalStat + prop.superchargedLevel * prop.chargeValue * 2)
                    .put(ItemStats.MELEE_DAMAGE,      prop -> prop.originalStat + prop.chargeValue * 2)
                    .put(ItemStats.MAGIC_DAMAGE,      prop -> prop.originalStat + prop.chargeValue * 2)
                    .put(ItemStats.RANGED_DAMAGE,     prop -> prop.originalStat + prop.chargeValue)
                    .put(ItemStats.ARMOR,             prop -> prop.originalStat + prop.chargeValue)
                    .put(ItemStats.ARMOR_TOUGHNESS,   prop -> prop.originalStat + prop.chargeValue / 2)
                    .put(ItemStats.MAGIC_ARMOR,       prop -> prop.originalStat + prop.chargeValue)
                    //@formatter:on
                    .build();

    private static final Set<StatInstance.Operation> SUPPORTED_OPS = Collections.unmodifiableSet(EnumSet.of(
            StatInstance.Operation.AVG,
            StatInstance.Operation.MAX,
            StatInstance.Operation.ADD
    ));

    public static void registerStats(RegistryEvent.Register<ItemStat> event) {
        event.getRegistry().register(CHARGEABILITY.setRegistryName(SilentGems.getId("chargeability")));
    }

    @SubscribeEvent
    public void onGetMaterialStats(GetMaterialStatsEvent event) {
        ItemStack stack = event.getMaterial().getItem();
        int supercharged = EnchantmentHelper.getEnchantmentLevel(GemsEnchantments.SUPERCHARGED.get(), stack);

        if (supercharged > 0 && BOOSTED_STATS.containsKey(event.getStat())) {
            float chargeability = CHARGEABILITY.compute(0, event.getMaterial().getStatModifiers(CHARGEABILITY, event.getPartType(), ItemStack.EMPTY));
            if (chargeability == 0) chargeability = 1;
            float chargeLevel = chargeability * supercharged;

            for (int i = 0; i < event.getModifiers().size(); ++i) {
                StatInstance instance = event.getModifiers().get(i);
                if (isSupportedOp(instance)) {
                    // Replace instance with a modified one
                    ChargedProperties chargedProperties = new ChargedProperties(supercharged, chargeLevel, instance.getValue());
                    float statValue = (float) BOOSTED_STATS.get(event.getStat()).applyAsDouble(chargedProperties);
                    StatInstance replacement = new StatInstance(statValue, instance.getOp());
                    event.getModifiers().remove(instance);
                    event.getModifiers().add(i, replacement);
                }
            }
        }
    }

    private static boolean isSupportedOp(StatInstance instance) {
        return SUPPORTED_OPS.contains(instance.getOp());
    }

    @SubscribeEvent
    public void onGetPartStats(GetStatModifierEvent event) {
        // TODO: Remove me! Left in for legacy simple mains
        ItemStack stack = event.getPart().getCraftingItem();
        int supercharged = EnchantmentHelper.getEnchantmentLevel(GemsEnchantments.SUPERCHARGED.get(), stack);

        if (supercharged > 0 && BOOSTED_STATS.containsKey(event.getStat())) {
            float chargeability = CHARGEABILITY.compute(0, event.getPart().getStatModifiers(ItemStack.EMPTY, CHARGEABILITY));
            if (chargeability == 0) chargeability = 1;
            float chargeLevel = chargeability * supercharged;

            for (int i = 0; i < event.getModifiers().size(); ++i) {
                StatInstance instance = event.getModifiers().get(i);
                if (instance.getOp() == StatInstance.Operation.AVG || instance.getOp() == StatInstance.Operation.MAX) {
                    // Replace instance with a modified one
                    ChargedProperties chargedProperties = new ChargedProperties(supercharged, chargeLevel, instance.getValue());
                    float statValue = (float) BOOSTED_STATS.get(event.getStat()).applyAsDouble(chargedProperties);
                    StatInstance replacement = new StatInstance(statValue, instance.getOp());
                    event.getModifiers().remove(instance);
                    event.getModifiers().add(replacement);
                }
            }
        }
    }

    private static final class ChargedProperties {
        /**
         * The supercharged enchantment level, before chargeability is applied
         */
        private final int superchargedLevel;
        /**
         * The actual charge level after applying the chargeability stat multiplier
         */
        private final float chargeValue;
        /**
         * The chargeability stat
         */
        private final float chargeability;
        /**
         * The original value of the stat being modified
         */
        private final float originalStat;

        private ChargedProperties(int superchargedLevel, float chargeValue, float originalStat) {
            this.superchargedLevel = superchargedLevel;
            this.chargeValue = chargeValue;
            this.chargeability = this.chargeValue / this.superchargedLevel;
            this.originalStat = originalStat;
        }
    }
}
