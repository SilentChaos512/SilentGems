package net.silentchaos512.gemschaos.data;

import net.minecraft.data.BlockTagsProvider;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.silentchaos512.gemschaos.ChaosMod;

import javax.annotation.Nullable;

public class ChaosBlockTagsProvider extends BlockTagsProvider {
    public ChaosBlockTagsProvider(DataGenerator generatorIn, @Nullable ExistingFileHelper existingFileHelper) {
        super(generatorIn, ChaosMod.MOD_ID, existingFileHelper);
    }

    @Override
    protected void registerTags() {
    }
}
