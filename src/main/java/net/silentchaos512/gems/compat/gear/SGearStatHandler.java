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
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.silentchaos512.gear.api.event.GetStatModifierEvent;
import net.silentchaos512.gear.api.stats.CommonItemStats;
import net.silentchaos512.gear.api.stats.ItemStat;
import net.silentchaos512.gear.api.stats.StatInstance;
import net.silentchaos512.gems.SilentGems;
import net.silentchaos512.gems.init.ModEnchantments;

import java.util.Map;
import java.util.function.ToDoubleBiFunction;

@Mod.EventBusSubscriber
public class SGearStatHandler {
    public static final ItemStat CHARGEABILITY = new ItemStat(new ResourceLocation(SilentGems.MODID, "chargeability"),
            1f, 0f, 100f, false, TextFormatting.GOLD).setSynergyApplies(false).setAffectedByGrades(false).setHidden(true);

    private static final Map<ItemStat, ToDoubleBiFunction<Float, Float>> BOOSTED_STATS =
            ImmutableMap.<ItemStat, ToDoubleBiFunction<Float, Float>>builder()
                    .put(CommonItemStats.DURABILITY, (level, value) -> value * Math.pow(2.0, level))
                    .put(CommonItemStats.ARMOR_DURABILITY, (level, value) -> value * Math.pow(1.8, level))
                    .put(CommonItemStats.ENCHANTABILITY, (level, value) -> value + level * 4)
                    .put(CommonItemStats.RARITY, (level, value) -> value + level * 15)
                    .put(CommonItemStats.HARVEST_LEVEL, (level, value) -> value + level)
                    .put(CommonItemStats.HARVEST_SPEED, (level, value) -> value + level * level * 4)
                    .put(CommonItemStats.MELEE_DAMAGE, (level, value) -> value + level * 2)
                    .put(CommonItemStats.MAGIC_DAMAGE, (level, value) -> value + level * 2)
                    .put(CommonItemStats.RANGED_DAMAGE, (level, value) -> value + level)
                    .put(CommonItemStats.ARMOR, (level, value) -> value + level * 2)
                    .put(CommonItemStats.ARMOR_TOUGHNESS, (level, value) -> value + level)
                    .put(CommonItemStats.MAGIC_ARMOR, (level, value) -> value + level)
                    .build();

    @SubscribeEvent
    public static void onGetPartStats(GetStatModifierEvent event) {
        ItemStack stack = event.getPart().getCraftingItem();
        int supercharged = EnchantmentHelper.getEnchantmentLevel(ModEnchantments.supercharged, stack);

        if (supercharged > 0 && BOOSTED_STATS.containsKey(event.getStat())) {
            float chargeability = CHARGEABILITY.compute(0, event.getPart().getStatModifiers(CHARGEABILITY));
            if (chargeability == 0) chargeability = 1;
            float chargeLevel = chargeability * supercharged;
//            SilentGems.logHelper.debug(chargeability, chargeLevel);

            for (int i = 0; i < event.getModifiers().size(); ++i) {
                StatInstance instance = event.getModifiers().get(i);
                if (instance.getOp() == StatInstance.Operation.AVG || instance.getOp() == StatInstance.Operation.MAX) {
                    // Replace instance with a modified one
                    StatInstance replacement = new StatInstance(instance.getId() + "_supercharged_" + supercharged,
                            (float) BOOSTED_STATS.get(event.getStat()).applyAsDouble(chargeLevel, instance.getValue()),
                            instance.getOp());
                    event.getModifiers().remove(instance);
                    event.getModifiers().add(replacement);
                }
            }
        }
    }

    public static void init() {
        // NO-OP
    }
}
