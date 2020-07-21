package net.silentchaos512.gems.client;

import net.minecraft.item.IItemPropertyGetter;
import net.minecraft.item.ItemModelsProperties;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.silentchaos512.gems.block.urn.SoulUrnBlock;
import net.silentchaos512.gems.init.GemsBlocks;
import net.silentchaos512.gems.init.GemsItems;
import net.silentchaos512.gems.init.Registration;
import net.silentchaos512.gems.item.ChaosMeterItem;
import net.silentchaos512.gems.item.ChaosOrbItem;
import net.silentchaos512.gems.item.EnchantmentTokenItem;
import net.silentchaos512.gems.lib.urn.UrnHelper;

public final class GemsModelProperties {
    private GemsModelProperties() {}

    public static void register(FMLClientSetupEvent event) {
        register(GemsBlocks.SOUL_URN, SoulUrnBlock.LIDLESS, (stack, world, entity) ->
                UrnHelper.hasLid(stack) ? 0 : 1);

        Registration.getItems(ChaosOrbItem.class).forEach(item ->
                register(item, ChaosOrbItem.CRACK_STAGE, (stack, world, entity) ->
                        ChaosOrbItem.getCrackStage(stack)));

        register(GemsItems.ENCHANTMENT_TOKEN, EnchantmentTokenItem.MODEL_INDEX, EnchantmentTokenItem::getModel);

        register(GemsItems.CHAOS_METER, ChaosMeterItem.CHAOS_LEVEL, (stack, world, entity) ->
                ChaosMeterItem.getChaosLevel(stack));
    }

    private static void register(IItemProvider item, ResourceLocation property, IItemPropertyGetter getter) {
        ItemModelsProperties.func_239418_a_(item.asItem(), property, getter);
    }
}
