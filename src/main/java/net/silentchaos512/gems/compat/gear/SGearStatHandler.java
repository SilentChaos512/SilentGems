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

/**
 * Handles the GetStatModifierEvent to apply stat bonuses to Silent Gear materials with the
 * Supercharged enchantment. Does not work with the old Silent's Gems tool system.
 */
public class SGearStatHandler {
//    public static final ItemStat CHARGEABILITY = new ItemStat(new ResourceLocation(SilentGems.MOD_ID, "chargeability"),
//            1f, 0f, 100f, false, TextFormatting.GOLD).setSynergyApplies(false).setAffectedByGrades(false).setHidden(true);
//
//    private static final Map<ItemStat, ToDoubleFunction<ChargedProperties>> BOOSTED_STATS =
//            ImmutableMap.<ItemStat, ToDoubleFunction<ChargedProperties>>builder()
//                    //@formatter:off
//                    .put(CommonItemStats.DURABILITY,        prop -> prop.originalStat * Math.pow(2.0, prop.chargeValue))
//                    .put(CommonItemStats.ARMOR_DURABILITY,  prop -> prop.originalStat * Math.pow(1.8, prop.chargeValue))
//                    .put(CommonItemStats.ENCHANTABILITY,    prop -> prop.originalStat * (1 + prop.superchargedLevel * (Math.sqrt(prop.chargeability) - 1)))
//                    .put(CommonItemStats.RARITY,            prop -> prop.originalStat + prop.superchargedLevel * 10)
//                    .put(CommonItemStats.HARVEST_LEVEL,     prop -> prop.originalStat + prop.superchargedLevel)
//                    .put(CommonItemStats.HARVEST_SPEED,     prop -> prop.originalStat + prop.superchargedLevel * prop.chargeValue * 3)
//                    .put(CommonItemStats.MELEE_DAMAGE,      prop -> prop.originalStat + prop.chargeValue * 2)
//                    .put(CommonItemStats.MAGIC_DAMAGE,      prop -> prop.originalStat + prop.chargeValue * 2)
//                    .put(CommonItemStats.RANGED_DAMAGE,     prop -> prop.originalStat + prop.chargeValue)
//                    .put(CommonItemStats.ARMOR,             prop -> prop.originalStat + prop.chargeValue)
//                    .put(CommonItemStats.ARMOR_TOUGHNESS,   prop -> prop.originalStat + prop.chargeValue / 2)
//                    .put(CommonItemStats.MAGIC_ARMOR,       prop -> prop.originalStat + prop.chargeValue)
//                    //@formatter:on
//                    .build();
//
//    @SubscribeEvent
//    public void onGetPartStats(GetStatModifierEvent event) {
//        ItemStack stack = event.getPart().getCraftingItem();
//        int supercharged = EnchantmentHelper.getEnchantmentLevel(ModEnchantments.supercharged, stack);
//
//        if (supercharged > 0 && BOOSTED_STATS.containsKey(event.getStat())) {
//            float chargeability = CHARGEABILITY.compute(0, event.getPart().getStatModifiers(CHARGEABILITY));
//            if (chargeability == 0) chargeability = 1;
//            float chargeLevel = chargeability * supercharged;
//
//            for (int i = 0; i < event.getModifiers().size(); ++i) {
//                StatInstance instance = event.getModifiers().get(i);
//                if (instance.getOp() == StatInstance.Operation.AVG || instance.getOp() == StatInstance.Operation.MAX) {
//                    // Replace instance with a modified one
//                    ChargedProperties chargedProperties = new ChargedProperties(supercharged, chargeLevel, instance.getValue());
//                    StatInstance replacement = new StatInstance(instance.getId() + "_supercharged_" + supercharged,
//                            (float) BOOSTED_STATS.get(event.getStat()).applyAsDouble(chargedProperties),
//                            instance.getOp());
//                    event.getModifiers().remove(instance);
//                    event.getModifiers().add(replacement);
//                }
//            }
//        }
//    }

    private static class ChargedProperties {
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
