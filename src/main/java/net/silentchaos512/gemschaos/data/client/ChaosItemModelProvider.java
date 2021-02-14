package net.silentchaos512.gemschaos.data.client;

import net.minecraft.block.Block;
import net.minecraft.data.DataGenerator;
import net.minecraft.util.IItemProvider;
import net.minecraftforge.client.model.generators.ItemModelBuilder;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.fml.RegistryObject;
import net.silentchaos512.gemschaos.ChaosMod;
import net.silentchaos512.gemschaos.item.ChaosOrbItem;
import net.silentchaos512.gemschaos.setup.ChaosItems;
import net.silentchaos512.gemschaos.setup.ChaosRegistration;
import net.silentchaos512.lib.block.IBlockProvider;
import net.silentchaos512.lib.util.NameUtils;

public class ChaosItemModelProvider extends ItemModelProvider {
    public ChaosItemModelProvider(DataGenerator generator, ExistingFileHelper existingFileHelper) {
        super(generator, ChaosMod.MOD_ID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        ModelFile itemGenerated = getExistingFile(mcLoc("item/generated"));

        ChaosRegistration.BLOCKS.getEntries().stream()
                .map(RegistryObject::get)
                .forEach(this::blockBuilder);

        ChaosItems.getSimpleModelItems().forEach(iro -> builder(iro.get(), itemGenerated));

        builder(ChaosItems.CHAOS_POTATO, itemGenerated);
        chaosOrb(ChaosItems.FRAGILE_CHAOS_ORB.get(), "crack2", "crack4");
        chaosOrb(ChaosItems.REFINED_CHAOS_ORB.get(), "crack1", "crack2", "crack3", "crack4");
        chaosOrb(ChaosItems.PERFECT_CHAOS_ORB.get(), "crack1", "crack2", "crack3", "crack4");
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

    private void chaosOrb(@SuppressWarnings("TypeMayBeWeakened") ChaosOrbItem item, String... crackTextures) {
        String name = NameUtils.from(item).getPath();
        ModelFile.ExistingModelFile itemGenerated = getExistingFile(mcLoc("item/generated"));

        for (int i = 0; i < crackTextures.length; ++i) {
            getBuilder(name + "_crack" + (i + 1))
                    .parent(itemGenerated)
                    .texture("layer0", modLoc("item/" + name))
                    .texture("layer1", modLoc("item/chaos_orb_" + crackTextures[i]));
        }

        ItemModelBuilder builder = getBuilder(name)
                .parent(itemGenerated)
                .texture("layer0", modLoc("item/" + name));
        for (int i = 0; i < crackTextures.length; ++i) {
            int stage = i + 1;
            builder.override()
                    .model(getExistingFile(modLoc("item/" + name + "_crack" + stage)))
                    .predicate(ChaosOrbItem.CRACK_STAGE, stage)
                    .end();
        }
    }
}
