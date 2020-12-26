package net.silentchaos512.gems.data;

import net.minecraft.data.BlockTagsProvider;
import net.minecraft.data.DataGenerator;
import net.minecraft.world.World;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.silentchaos512.gems.GemsBase;
import net.silentchaos512.gems.util.Gems;

import javax.annotation.Nullable;

public class GemsBlockTagsProvider extends BlockTagsProvider {
    public GemsBlockTagsProvider(DataGenerator generatorIn, @Nullable ExistingFileHelper existingFileHelper) {
        super(generatorIn, GemsBase.MOD_ID, existingFileHelper);
    }

    @Override
    protected void registerTags() {
        for (Gems gem : Gems.values()) {
            getOrCreateBuilder(gem.getOreTag())
                    .add(gem.getOre(World.OVERWORLD), gem.getOre(World.THE_NETHER), gem.getOre(World.THE_END));

            // Group tags
            getOrCreateBuilder(Tags.Blocks.ORES)
                    .add(gem.getOre(World.OVERWORLD), gem.getOre(World.THE_NETHER), gem.getOre(World.THE_END));
        }
    }
}
