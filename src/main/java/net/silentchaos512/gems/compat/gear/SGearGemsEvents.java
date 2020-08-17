package net.silentchaos512.gems.compat.gear;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.silentchaos512.gear.api.event.GearNamePrefixesEvent;
import net.silentchaos512.gear.gear.part.PartData;
import net.silentchaos512.gems.init.GemsEnchantments;

public class SGearGemsEvents {
    @SubscribeEvent
    public void onGetNamePrefixes(GearNamePrefixesEvent event) {
        for (PartData part : event.getParts()) {
            ItemStack craftingItem = part.getCraftingItem();
            int supercharged = EnchantmentHelper.getEnchantmentLevel(GemsEnchantments.SUPERCHARGED.get(), craftingItem);
            if (supercharged > 0) {
                event.getPrefixes().add(new TranslationTextComponent("enchantment.silentgems.supercharged"));
                return;
            }
        }
    }
}
