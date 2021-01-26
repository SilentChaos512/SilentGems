package net.silentchaos512.gems.data.client;

import net.minecraft.block.Block;
import net.minecraft.data.DataGenerator;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.fml.RegistryObject;
import net.silentchaos512.gems.GemsBase;
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
                .forEach(this::blockBuilder);

        GemsItems.getSimpleModelItems().forEach(iro -> builder(iro.get(), itemGenerated));

        for (Gems gem : Gems.values()) {
            builder(gem.getItem(), itemGenerated);
        }

        builder(GemsItems.GEM_BAG, itemGenerated);
        builder(GemsItems.FLOWER_BASKET, itemGenerated);
    }

    private void blockBuilder(IBlockProvider block) {
        blockBuilder(block.asBlock());
    }

    private void blockBuilder(Block block) {
        String name = NameUtils.from(block).getPath();
        withExistingParent(name, modLoc("block/" + name));
    }

    private void builder(IItemProvider item, ModelFile parent) {
        String name = NameUtils.fromItem(item).getPath();
        builder(item, parent, "item/" + name);
    }

    private void builder(IItemProvider item, ModelFile parent, String texture) {
        getBuilder(NameUtils.fromItem(item).getPath())
                .parent(parent)
                .texture("layer0", modLoc(texture));
    }
}
