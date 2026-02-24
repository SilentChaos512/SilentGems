package net.silentchaos512.gems.data.client;

import net.minecraft.client.data.models.ItemModelGenerators;
import net.minecraft.client.data.models.ItemModelOutput;
import net.minecraft.client.data.models.model.ModelInstance;
import net.minecraft.client.data.models.model.ModelTemplates;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.registries.DeferredItem;
import net.silentchaos512.gems.setup.GemsItems;
import net.silentchaos512.gems.setup.Gems;

import java.util.function.BiConsumer;

public class GemsItemModelGenerator extends ItemModelGenerators {
    public GemsItemModelGenerator(ItemModelOutput itemModelOutput, BiConsumer<Identifier, ModelInstance> modelOutput) {
        super(itemModelOutput, modelOutput);
    }

    @Override
    public void run() {
        for (DeferredItem<? extends Item> item : GemsItems.getSimpleModelItems()) {
            generateFlatItem(item.get(), ModelTemplates.FLAT_ITEM);
        }

        for (Gems gem : Gems.values()) {
            generateFlatItem(gem.getItem(), ModelTemplates.FLAT_ITEM);
        }
    }
}
