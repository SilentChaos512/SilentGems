package net.silentchaos512.gems.util;

import net.silentchaos512.gems.SilentGems;
import net.silentchaos512.gems.block.HardenedRock;
import net.silentchaos512.gems.block.MiscBlocks;
import net.silentchaos512.gems.init.ModItems;
import net.silentchaos512.gems.item.CraftingItems;
import net.silentchaos512.gems.item.Foods;
import net.silentchaos512.gems.lib.Gems;
import net.silentchaos512.lib.util.ModelGenerator;

public final class ModelGen {
    private ModelGen() { throw new IllegalAccessError("Utility class"); }

    public static void generateModels() {
        SilentGems.LOGGER.info("DEV: Generating model files...");

        for (Gems gem : Gems.values()) {
            ModelGenerator.createFor(gem.getItem(), "gem/" + gem.getName());
            ModelGenerator.createFor(gem.getShard(), "shard/" + gem.getName());
        }

        for (Gems.Set set : Gems.Set.values()) {
            ModelGenerator.createFor(set.getMultiOre(), "ore/multi_" + set.getName());
        }

        for (HardenedRock rock : HardenedRock.values()) {
            ModelGenerator.createFor(rock.getBlock());
        }

        for (MiscBlocks misc : MiscBlocks.values()) {
            ModelGenerator.createFor(misc.getBlock(), "misc/" + misc.getName());
        }

        for (CraftingItems item : CraftingItems.values()) {
            ModelGenerator.createFor(item.asItem());
        }

        for (Foods food : Foods.values()) {
            ModelGenerator.createFor(food.asItem());
        }

        ModelGenerator.createFor(ModItems.summonKitty);
        ModelGenerator.createFor(ModItems.summonPuppy);
    }
}
