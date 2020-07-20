package net.silentchaos512.gems.data.client;

import net.minecraft.block.Block;
import net.minecraft.data.DataGenerator;
import net.minecraft.item.SpawnEggItem;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.generators.ExistingFileHelper;
import net.minecraftforge.client.model.generators.ItemModelBuilder;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.client.model.generators.ModelFile;
import net.silentchaos512.gems.SilentGems;
import net.silentchaos512.gems.block.*;
import net.silentchaos512.gems.block.pedestal.PedestalBlock;
import net.silentchaos512.gems.init.GemsBlocks;
import net.silentchaos512.gems.init.GemsItems;
import net.silentchaos512.gems.init.Registration;
import net.silentchaos512.gems.item.*;
import net.silentchaos512.gems.lib.Gems;
import net.silentchaos512.lib.block.IBlockProvider;
import net.silentchaos512.lib.util.NameUtils;

import javax.annotation.Nonnull;
import java.util.Arrays;

public class GemsItemModelProvider extends ItemModelProvider {
    public GemsItemModelProvider(DataGenerator generator, ExistingFileHelper existingFileHelper) {
        super(generator, SilentGems.MOD_ID, existingFileHelper);
    }

    @Nonnull
    @Override
    public String getName() {
        return "Silent's Gems - Item Models";
    }

    @Override
    protected void registerModels() {
        ModelFile itemGenerated = getExistingFile(new ResourceLocation("item/generated"));

        Registration.getBlocks(FluffyBlock.class).forEach(this::blockBuilder);
        Registration.getBlocks(PedestalBlock.class).forEach(this::blockBuilder);

        Arrays.stream(HardenedRock.values()).forEach(this::blockBuilder);
        Arrays.stream(MiscBlocks.values()).forEach(this::blockBuilder);
        Arrays.stream(MiscOres.values()).forEach(this::blockBuilder);
        Arrays.stream(CorruptedBlocks.values()).forEach(block -> {
            blockBuilder(block);
            builder(block.getPile(), itemGenerated);
        });

        blockBuilder(GemsBlocks.MULTI_ORE_CLASSIC);
        blockBuilder(GemsBlocks.MULTI_ORE_DARK);
        blockBuilder(GemsBlocks.MULTI_ORE_LIGHT);

        blockBuilder(GemsBlocks.LUMINOUS_FLOWER_POT);
        blockBuilder(GemsBlocks.PURIFIER);
        blockBuilder(GemsBlocks.SUPERCHARGER);
        blockBuilder(GemsBlocks.TOKEN_ENCHANTER);
        blockBuilder(GemsBlocks.TRANSMUTATION_ALTAR);
        blockBuilder(GemsBlocks.TELEPORTER_ANCHOR);

        for (Gems gem : Gems.values()) {
            String name = gem.getName();

            blockBuilder(gem.getOre());
            blockBuilder(gem.getBlock());
            blockBuilder(gem.getBricks());
            blockBuilder(gem.getGlass());
            blockBuilder(gem.getLamp(GemLampBlock.State.UNLIT));
            blockBuilder(gem.getLamp(GemLampBlock.State.INVERTED_LIT));
            blockBuilder(gem.getTeleporter());
            blockBuilder(gem.getRedstoneTeleporter());
            builder(gem.getGlowrose(), itemGenerated, "block/glowrose/" + name);

            builder(gem.getItem(), itemGenerated, "item/gem/" + name);
            builder(gem.getShard(), itemGenerated, "item/shard/" + name);
            builder(gem.getChaosGem(), itemGenerated, "item/chaos_gem/" + name);
            getBuilder(NameUtils.from(gem.getReturnHomeCharm()).getPath())
                    .parent(itemGenerated)
                    .texture("layer0", modLoc("item/return_home_gem"))
                    .texture("layer1", modLoc("item/return_home_base"));
        }

        Arrays.stream(CraftingItems.values()).forEach(item -> builder(item, itemGenerated));
        Arrays.stream(ModFoods.values()).forEach(item -> builder(item, itemGenerated));
        Arrays.stream(SoulUrnUpgrades.values()).forEach(item -> builder(item, itemGenerated));

        Registration.getItems(SpawnEggItem.class).forEach(item ->
                getBuilder(NameUtils.from(item).getPath()).parent(getExistingFile(mcLoc("item/template_spawn_egg"))));

        for (int i = 1; i < 8; ++i) {
            getBuilder("chaos_meter_" + i)
                    .parent(itemGenerated)
                    .texture("layer0", modLoc("item/chaos_meter"))
                    .texture("layer1", modLoc("item/chaos_meter_" + i));
        }
        getBuilder("chaos_meter")
                .parent(itemGenerated)
                .texture("layer0", modLoc("item/chaos_meter"))
                .texture("layer1", modLoc("item/chaos_meter_1"))
                .override().model(getExistingFile(modLoc("chaos_meter_1"))).predicate(ChaosMeterItem.CHAOS_LEVEL, 0).end()
                .override().model(getExistingFile(modLoc("chaos_meter_2"))).predicate(ChaosMeterItem.CHAOS_LEVEL, 100).end()
                .override().model(getExistingFile(modLoc("chaos_meter_3"))).predicate(ChaosMeterItem.CHAOS_LEVEL, 1_000).end()
                .override().model(getExistingFile(modLoc("chaos_meter_4"))).predicate(ChaosMeterItem.CHAOS_LEVEL, 10_000).end()
                .override().model(getExistingFile(modLoc("chaos_meter_5"))).predicate(ChaosMeterItem.CHAOS_LEVEL, 100_000).end()
                .override().model(getExistingFile(modLoc("chaos_meter_6"))).predicate(ChaosMeterItem.CHAOS_LEVEL, 1_000_000).end()
                .override().model(getExistingFile(modLoc("chaos_meter_7"))).predicate(ChaosMeterItem.CHAOS_LEVEL, 10_000_000).end();

        for (EnchantmentTokenItem.Icon icon : EnchantmentTokenItem.Icon.values()) {
            getBuilder("enchantment_token_" + icon.getName())
                    .parent(itemGenerated)
                    .texture("layer0", modLoc("item/enchantment_token/base"))
                    .texture("layer1", modLoc("item/enchantment_token/outline"))
                    .texture("layer2", modLoc("item/enchantment_token/" + icon.getName()));
        }
        ItemModelBuilder enchantmentTokenBuilder = getBuilder("enchantment_token")
                .parent(itemGenerated)
                .texture("layer0", modLoc("item/enchantment_token/any"));
        for (EnchantmentTokenItem.Icon icon : EnchantmentTokenItem.Icon.values()) {
            enchantmentTokenBuilder.override()
                    .model(getExistingFile(modLoc("item/enchantment_token_" + icon.getName())))
                    .predicate(EnchantmentTokenItem.MODEL_INDEX, icon.ordinal())
                    .end();
        }

        builder(GemsItems.CHAOS_POTATO, itemGenerated);
        chaosOrb(GemsItems.FRAGILE_CHAOS_ORB.get(), "crack2", "crack4");
        chaosOrb(GemsItems.REFINED_CHAOS_ORB.get(), "crack1", "crack2", "crack3", "crack4");
        chaosOrb(GemsItems.PERFECT_CHAOS_ORB.get(), "crack1", "crack2", "crack3", "crack4");

        builder(GemsItems.CORRUPTING_POWDER, itemGenerated);
        builder(GemsItems.PURIFYING_POWDER, itemGenerated);

        builder(GemsItems.FLUFFY_PUFF_SEEDS, itemGenerated);
        builder(GemsItems.GEM_BAG, itemGenerated);
        builder(GemsItems.GLOWROSE_BASKET, itemGenerated);
        builder(GemsItems.GLOWROSE_FERTILIZER, itemGenerated);
        builder(GemsItems.SUMMON_KITTY, itemGenerated);
        builder(GemsItems.SUMMON_PUPPY, itemGenerated);
        builder(GemsItems.TELEPORTER_LINKER, itemGenerated);

        getBuilder("soul_gem")
                .parent(itemGenerated)
                .texture("layer0", modLoc("item/soul_gem_back"))
                .texture("layer1", modLoc("item/soul_gem_front"));

        getBuilder("gear_soul")
                .parent(itemGenerated)
                .texture("layer0", modLoc("item/gear_soul_middle"))
                .texture("layer1", modLoc("item/gear_soul_left"))
                .texture("layer2", modLoc("item/gear_soul_right"))
                .texture("layer3", modLoc("item/gear_soul_highlight"));

        getBuilder("chaos_rune")
                .parent(itemGenerated)
                .texture("layer0", modLoc("item/chaos_rune_base"))
                .texture("layer1", modLoc("item/chaos_rune_overlay"));
    }

    private void chaosOrb(ChaosOrbItem item, String... crackTextures) {
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
