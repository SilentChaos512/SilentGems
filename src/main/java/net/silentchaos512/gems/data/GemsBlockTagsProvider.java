package net.silentchaos512.gems.data;

import net.minecraft.data.tags.BlockTagsProvider;
import net.minecraft.data.DataGenerator;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.silentchaos512.gems.GemsBase;
import net.silentchaos512.gems.setup.GemsBlocks;
import net.silentchaos512.gems.setup.GemsTags;
import net.silentchaos512.gems.util.Gems;

import javax.annotation.Nullable;

public class GemsBlockTagsProvider extends BlockTagsProvider {
    public GemsBlockTagsProvider(DataGenerator generatorIn, @Nullable ExistingFileHelper existingFileHelper) {
        super(generatorIn, GemsBase.MOD_ID, existingFileHelper);
    }

    @Override
    protected void addTags() {
        for (Gems gem : Gems.values()) {
            tag(gem.getModOresTag()).add(gem.getOre(Level.OVERWORLD), gem.getOre(Level.NETHER), gem.getOre(Level.END));
            tag(gem.getOreTag()).addTag(gem.getModOresTag());
            tag(gem.getBlockTag()).add(gem.getBlock());
            tag(gem.getGlowroseTag()).add(gem.getGlowrose());

            // Group tags
            tag(GemsTags.Blocks.GEM_ORES).addTag(gem.getModOresTag());
            tag(GemsTags.Blocks.GLOWROSES).addTag(gem.getGlowroseTag());
            tag(Tags.Blocks.ORES).addTag(gem.getOreTag());
            tag(Tags.Blocks.STORAGE_BLOCKS).add(gem.getBlock());
        }

        tag(GemsTags.Blocks.ORES_SILVER).add(GemsBlocks.SILVER_ORE.get());
    }
}
