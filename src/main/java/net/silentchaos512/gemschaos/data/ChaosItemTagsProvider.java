package net.silentchaos512.gemschaos.data;

import net.minecraft.data.BlockTagsProvider;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.ItemTagsProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.silentchaos512.gemschaos.ChaosMod;

import javax.annotation.Nullable;

public class ChaosItemTagsProvider extends ItemTagsProvider {
    public ChaosItemTagsProvider(DataGenerator dataGenerator, BlockTagsProvider blockTagProvider, @Nullable ExistingFileHelper existingFileHelper) {
        super(dataGenerator, blockTagProvider, ChaosMod.MOD_ID, existingFileHelper);
    }

    @Override
    protected void registerTags() {
    }
}
