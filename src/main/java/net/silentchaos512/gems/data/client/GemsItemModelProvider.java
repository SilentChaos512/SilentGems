package net.silentchaos512.gems.data.client;

import net.minecraft.data.DataGenerator;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.client.model.generators.ItemModelProvider;
import net.neoforged.neoforge.client.model.generators.ModelFile;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.silentchaos512.gems.SilentGems;
import net.silentchaos512.gems.block.GlowroseBlock;
import net.silentchaos512.gems.setup.GemsBlocks;
import net.silentchaos512.gems.setup.GemsItems;
import net.silentchaos512.gems.util.Gems;
import net.silentchaos512.lib.block.IBlockProvider;
import net.silentchaos512.lib.util.NameUtils;

public class GemsItemModelProvider extends ItemModelProvider {
    public GemsItemModelProvider(DataGenerator generator, ExistingFileHelper existingFileHelper) {
        super(generator.getPackOutput(), SilentGems.MOD_ID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        ModelFile itemGenerated = getExistingFile(ResourceLocation.withDefaultNamespace("item/generated"));

        GemsBlocks.BLOCKS.getEntries().stream()
                .map(DeferredHolder::get)
                .filter(block -> block.asItem() != Items.AIR)
                .forEach(this::blockBuilder);

        GemsItems.getSimpleModelItems().forEach(iro -> builder(iro.get(), itemGenerated));

        for (Gems gem : Gems.values()) {
            builder(gem.getItem(), itemGenerated);
        }
    }

    private void blockBuilder(IBlockProvider block) {
        blockBuilder(block.asBlock());
    }

    private void blockBuilder(Block block) {
        String name = NameUtils.fromBlock(block).getPath();
        if (!blockBuilderExceptions(block, name)) {
            withExistingParent(name, modLoc("block/" + name));
        }
    }

    private boolean blockBuilderExceptions(Block block, String name) {
        // Overrides the default block item models for specific blocks
        if (block instanceof GlowroseBlock) {
            getBuilder(name).parent(getExistingFile(ResourceLocation.withDefaultNamespace("item/generated")))
                    .texture("layer0", modLoc("block/" + name));
            return true;
        }
        return false;
    }

    private void builder(ItemLike item, ModelFile parent) {
        String name = NameUtils.fromItem(item).getPath();
        builder(item, parent, "item/" + name);
    }

    private void builder(ItemLike item, ModelFile parent, String texture) {
        getBuilder(NameUtils.fromItem(item).getPath())
                .parent(parent)
                .texture("layer0", modLoc(texture));
    }
}
