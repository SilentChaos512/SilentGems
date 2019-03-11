package net.silentchaos512.gems.util.gen;

import net.minecraft.item.EnumDyeColor;
import net.minecraft.util.ResourceLocation;
import net.silentchaos512.gems.SilentGems;
import net.silentchaos512.gems.block.*;
import net.silentchaos512.gems.block.teleporter.TeleporterAnchor;
import net.silentchaos512.gems.init.ModItems;
import net.silentchaos512.gems.item.CraftingItems;
import net.silentchaos512.gems.item.FluffyPuffSeeds;
import net.silentchaos512.gems.item.Foods;
import net.silentchaos512.gems.item.GlowroseFertilizer;
import net.silentchaos512.gems.lib.Gems;
import net.silentchaos512.lib.util.generator.ModelGenerator;
import net.silentchaos512.lib.util.generator.ModelGenerator.BlockBuilder;
import net.silentchaos512.lib.util.generator.ModelGenerator.ItemBuilder;

/**
 * Generates model JSON files. This doesn't necessarily generate ALL models. Models with custom
 * shapes are omitted. This does not add anything in-game. The files need to be copied to the
 * correct directory for them to load. They end up in 'run/output'.
 */
public final class GenModels {
    private GenModels() { throw new IllegalAccessError("Utility class"); }

    public static void generateModels() {
        SilentGems.LOGGER.info("DEV: Generating model files...");

        for (Gems gem : Gems.values()) {
            // Gem Blocks
            ModelGenerator.create(BlockBuilder.create(gem.getBlock())
                    .texture("gem/" + gem.getName() + "_block"));
            // Bricks
            ModelGenerator.create(BlockBuilder.create(gem.getBricks())
                    .texture("bricks/" + gem.getName()));
            // Glass
            ModelGenerator.create(BlockBuilder.create(gem.getGlass())
                    .texture("glass/" + gem.getName()));
            // Teleporters
            ModelGenerator.create(BlockBuilder.create(gem.getTeleporter())
                    .texture("teleporter/standard/" + gem.getName()));
            ModelGenerator.create(BlockBuilder.create(gem.getRedstoneTeleporter())
                    .texture("teleporter/redstone/" + gem.getName()));
            // Glowroses
            ModelGenerator.create(BlockBuilder.create(gem.getGlowrose())
                    .parent("block/cross")
                    .texture("cross", "glowrose/" + gem.getName())
                    .item(ItemBuilder.create(gem.getGlowrose().asItem())
                            .texture(new ResourceLocation(SilentGems.MOD_ID, "block/glowrose/" + gem.getName()))
                    )
            );
            // Potted Glowroses
            ModelGenerator.create(BlockBuilder.create(gem.getPottedGlowrose())
                    .parent("block/flower_pot_cross")
                    .texture("plant", "glowrose/" + gem.getName()));
            // Ores
            ModelGenerator.create(BlockBuilder.create(gem.getOre())
                    .texture("ore/gem/" + gem.getName()));

            // Lamps
            for (GemLamp.State state : GemLamp.State.values()) {
                String texture = "lamp/" + gem.getName() + (state.lit() ? "_lit" : "");
                ModelGenerator.create(BlockBuilder.create(gem.getLamp(state)).texture(texture));
            }

            // Gem Items
            ModelGenerator.create(ItemBuilder.create(gem.getItem())
                    .texture("gem/" + gem.getName()));
            // Shards
            ModelGenerator.create(ItemBuilder.create(gem.getShard())
                    .texture("shard/" + gem.getName()));
        }

        ModelGenerator.create(BlockBuilder.create(TeleporterAnchor.INSTANCE.get())
                .texture("teleporter/anchor"));

        for (Gems.Set set : Gems.Set.values()) {
            ModelGenerator.create(BlockBuilder.create(set.getMultiOre())
                    .texture("ore/multi_" + set.getName()));
        }

        for (HardenedRock rock : HardenedRock.values()) {
            ModelGenerator.create(rock.getBlock());
        }

        for (MiscBlocks misc : MiscBlocks.values()) {
            ModelGenerator.create(BlockBuilder.create(misc.getBlock())
                    .texture("misc/" + misc.getName()));
        }

        for (MiscOres ore : MiscOres.values()) {
            ModelGenerator.create(BlockBuilder.create(ore.getBlock())
                    .texture("ore/" + ore.getName()));
        }

        for (EnumDyeColor color : EnumDyeColor.values()) {
            ModelGenerator.create(BlockBuilder.create(FluffyBlock.get(color))
                    .texture("fluffy/" + color.getName()));
        }

        // Won't work, does not generate all block models
//        ModelGenerator.create(BlockBuilder.create(ModBlocks.fluffyPuffPlant)
//                .variant("age=0", "fluffy_plant0")
//                .variant("age=1", "fluffy_plant0")
//                .variant("age=2", "fluffy_plant1")
//                .variant("age=3", "fluffy_plant1")
//                .variant("age=4", "fluffy_plant2")
//                .variant("age=5", "fluffy_plant2")
//                .variant("age=6", "fluffy_plant2")
//                .variant("age=7", "fluffy_plant3"));

        for (CraftingItems item : CraftingItems.values()) {
            ModelGenerator.create(item.asItem());
        }

        for (Foods food : Foods.values()) {
            ModelGenerator.create(food.asItem());
        }

        ModelGenerator.create(FluffyPuffSeeds.INSTANCE.get());
        ModelGenerator.create(GlowroseFertilizer.INSTANCE.get());
        ModelGenerator.create(ModItems.summonKitty);
        ModelGenerator.create(ModItems.summonPuppy);
    }
}
