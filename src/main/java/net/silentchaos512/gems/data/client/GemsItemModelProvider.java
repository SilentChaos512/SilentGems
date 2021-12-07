package net.silentchaos512.gems.data.client;

import net.minecraft.data.DataGenerator;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.RegistryObject;
import net.silentchaos512.gems.GemsBase;
import net.silentchaos512.gems.block.GlowroseBlock;
import net.silentchaos512.gems.setup.GemsItems;
import net.silentchaos512.gems.setup.Registration;
import net.silentchaos512.gems.util.Gems;
import net.silentchaos512.lib.block.IBlockProvider;
import net.silentchaos512.lib.util.NameUtils;

public class GemsItemModelProvider extends ItemModelProvider {
    public GemsItemModelProvider(DataGenerator generator, ExistingFileHelper existingFileHelper) {
        super(generator, GemsBase.MOD_ID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        ModelFile itemGenerated = getExistingFile(new ResourceLocation("item/generated"));

        Registration.BLOCKS.getEntries().stream()
                .map(RegistryObject::get)
                .filter(block -> block.asItem() != Items.AIR)
                .forEach(this::blockBuilder);

        GemsItems.getSimpleModelItems().forEach(iro -> builder(iro.get(), itemGenerated));

        for (Gems gem : Gems.values()) {
            builder(gem.getItem(), itemGenerated);
        }

        builder(GemsItems.GEM_BAG, itemGenerated);
        builder(GemsItems.FLOWER_BASKET, itemGenerated);

        getBuilder("soul_gem").parent(itemGenerated)
                .texture("layer0", modLoc("item/soul_gem_back"))
                .texture("layer1", modLoc("item/soul_gem_front"));
    }

    private void blockBuilder(IBlockProvider block) {
        blockBuilder(block.asBlock());
    }

    private void blockBuilder(Block block) {
        String name = NameUtils.from(block).getPath();
        if (!blockBuilderExceptions(block, name)) {
            withExistingParent(name, modLoc("block/" + name));
        }
    }

    private boolean blockBuilderExceptions(Block block, String name) {
        // Overrides the default block item models for specific blocks
        if (block instanceof GlowroseBlock) {
            getBuilder(name).parent(getExistingFile(new ResourceLocation("item/generated")))
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
