package net.silentchaos512.gems.init;

import net.minecraft.block.AirBlock;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.DyeColor;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.server.ServerWorld;
import net.minecraft.world.storage.loot.LootTableManager;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.registries.ForgeRegistries;
import net.silentchaos512.gear.SilentGear;
import net.silentchaos512.gems.SilentGems;
import net.silentchaos512.gems.block.*;
import net.silentchaos512.gems.block.altar.AltarBlock;
import net.silentchaos512.gems.block.flowerpot.LuminousFlowerPotBlock;
import net.silentchaos512.gems.block.flowerpot.PhantomLightBlock;
import net.silentchaos512.gems.block.pedestal.PedestalBlock;
import net.silentchaos512.gems.block.purifier.PurifierBlock;
import net.silentchaos512.gems.block.supercharger.SuperchargerBlock;
import net.silentchaos512.gems.block.teleporter.TeleporterBaseBlock;
import net.silentchaos512.gems.block.tokenenchanter.TokenEnchanterBlock;
import net.silentchaos512.gems.block.urn.SoulUrnBlock;
import net.silentchaos512.gems.lib.Gems;
import net.silentchaos512.lib.registry.BlockRegistryObject;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.function.Function;
import java.util.function.Supplier;

@SuppressWarnings("unused")
public final class GemsBlocks {
    static {
        Gems.registerBlocks();
    }

    public static final BlockRegistryObject<TeleporterBaseBlock> TELEPORTER_ANCHOR = register("teleporter_anchor", () -> new TeleporterBaseBlock(true));
    public static final BlockRegistryObject<MultiGemOreBlock> MULTI_ORE_CLASSIC = register("multi_ore_classic", () -> new MultiGemOreBlock(Gems.Set.CLASSIC));
    public static final BlockRegistryObject<MultiGemOreBlock> MULTI_ORE_DARK = register("multi_ore_dark", () -> new MultiGemOreBlock(Gems.Set.DARK));
    public static final BlockRegistryObject<MultiGemOreBlock> MULTI_ORE_LIGHT = register("multi_ore_light", () -> new MultiGemOreBlock(Gems.Set.LIGHT));

    static {
        MiscBlocks.registerBlocks();
        MiscOres.registerBlocks();
        CorruptedBlocks.registerBlocks();
        HardenedRock.registerBlocks();
    }

    public static final BlockRegistryObject<FluffyBlock> WHITE_FLUFFY_BLOCK = registerFluffyBlock(DyeColor.WHITE);
    public static final BlockRegistryObject<FluffyBlock> ORANGE_FLUFFY_BLOCK = registerFluffyBlock(DyeColor.ORANGE);
    public static final BlockRegistryObject<FluffyBlock> MAGENTA_FLUFFY_BLOCK = registerFluffyBlock(DyeColor.MAGENTA);
    public static final BlockRegistryObject<FluffyBlock> LIGHT_BLUE_FLUFFY_BLOCK = registerFluffyBlock(DyeColor.LIGHT_BLUE);
    public static final BlockRegistryObject<FluffyBlock> YELLOW_FLUFFY_BLOCK = registerFluffyBlock(DyeColor.YELLOW);
    public static final BlockRegistryObject<FluffyBlock> LIME_FLUFFY_BLOCK = registerFluffyBlock(DyeColor.LIME);
    public static final BlockRegistryObject<FluffyBlock> PINK_FLUFFY_BLOCK = registerFluffyBlock(DyeColor.PINK);
    public static final BlockRegistryObject<FluffyBlock> GRAY_FLUFFY_BLOCK = registerFluffyBlock(DyeColor.GRAY);
    public static final BlockRegistryObject<FluffyBlock> LIGHT_GRAY_FLUFFY_BLOCK = registerFluffyBlock(DyeColor.LIGHT_GRAY);
    public static final BlockRegistryObject<FluffyBlock> CYAN_FLUFFY_BLOCK = registerFluffyBlock(DyeColor.CYAN);
    public static final BlockRegistryObject<FluffyBlock> PURPLE_FLUFFY_BLOCK = registerFluffyBlock(DyeColor.PURPLE);
    public static final BlockRegistryObject<FluffyBlock> BLUE_FLUFFY_BLOCK = registerFluffyBlock(DyeColor.BLUE);
    public static final BlockRegistryObject<FluffyBlock> BROWN_FLUFFY_BLOCK = registerFluffyBlock(DyeColor.BROWN);
    public static final BlockRegistryObject<FluffyBlock> GREEN_FLUFFY_BLOCK = registerFluffyBlock(DyeColor.GREEN);
    public static final BlockRegistryObject<FluffyBlock> RED_FLUFFY_BLOCK = registerFluffyBlock(DyeColor.RED);
    public static final BlockRegistryObject<FluffyBlock> BLACK_FLUFFY_BLOCK = registerFluffyBlock(DyeColor.BLACK);

    public static final BlockRegistryObject<SoulUrnBlock> SOUL_URN = register("soul_urn",
            SoulUrnBlock::new,
            bro -> () -> new SoulUrnBlock.SoulUrnBlockItem(bro.get()));
    public static final BlockRegistryObject<SuperchargerBlock> SUPERCHARGER = register("supercharger", SuperchargerBlock::new);
    public static final BlockRegistryObject<TokenEnchanterBlock> TOKEN_ENCHANTER = register("token_enchanter", TokenEnchanterBlock::new);
    public static final BlockRegistryObject<AltarBlock> TRANSMUTATION_ALTAR = register("transmutation_altar", AltarBlock::new);
    public static final BlockRegistryObject<PurifierBlock> PURIFIER = register("purifier", PurifierBlock::new);

    public static final BlockRegistryObject<PedestalBlock> STONE_PEDESTAL = register("stone_pedestal", () ->
            new PedestalBlock(Block.Properties.create(Material.ROCK).hardnessAndResistance(4, 5).sound(SoundType.STONE)));
    public static final BlockRegistryObject<PedestalBlock> GRANITE_PEDESTAL = register("granite_pedestal", () ->
            new PedestalBlock(Block.Properties.create(Material.ROCK).hardnessAndResistance(4, 5).sound(SoundType.STONE)));
    public static final BlockRegistryObject<PedestalBlock> DIORITE_PEDESTAL = register("diorite_pedestal", () ->
            new PedestalBlock(Block.Properties.create(Material.ROCK).hardnessAndResistance(4, 5).sound(SoundType.STONE)));
    public static final BlockRegistryObject<PedestalBlock> ANDESITE_PEDESTAL = register("andesite_pedestal", () ->
            new PedestalBlock(Block.Properties.create(Material.ROCK).hardnessAndResistance(4, 5).sound(SoundType.STONE)));
    public static final BlockRegistryObject<PedestalBlock> OBSIDIAN_PEDESTAL = register("obsidian_pedestal", () ->
            new PedestalBlock(Block.Properties.create(Material.ROCK).hardnessAndResistance(30, 1200).sound(SoundType.STONE)));

    public static final BlockRegistryObject<LuminousFlowerPotBlock> LUMINOUS_FLOWER_POT = register("luminous_flower_pot", LuminousFlowerPotBlock::new);
    public static final BlockRegistryObject<PhantomLightBlock> PHANTOM_LIGHT = registerNoItem("phantom_light", PhantomLightBlock::new);
    public static final BlockRegistryObject<FluffyPuffPlant> FLUFFY_PUFF_PLANT = registerNoItem("fluffy_puff_plant", FluffyPuffPlant::new);
    public static final BlockRegistryObject<WildFluffyPuffPlant> WILD_FLUFFY_PUFF_PLANT = registerNoItem("wild_fluffy_puff_plant", WildFluffyPuffPlant::new);

    private GemsBlocks() {}

    static void register() {}

    @OnlyIn(Dist.CLIENT)
    public static void registerRenderTypes(FMLClientSetupEvent event) {
        for (Gems gem : Gems.values()) {
            RenderTypeLookup.setRenderLayer(gem.getGlass(), RenderType.getTranslucent());
            RenderTypeLookup.setRenderLayer(gem.getGlowrose(), RenderType.getCutout());
            RenderTypeLookup.setRenderLayer(gem.getPottedGlowrose(), RenderType.getCutout());
        }
        RenderTypeLookup.setRenderLayer(FLUFFY_PUFF_PLANT.get(), RenderType.getCutout());
        RenderTypeLookup.setRenderLayer(WILD_FLUFFY_PUFF_PLANT.get(), RenderType.getCutout());
    }

    private static <T extends Block> BlockRegistryObject<T> registerNoItem(String name, Supplier<T> block) {
        return new BlockRegistryObject<>(Registration.BLOCKS.register(name, block));
    }

    private static <T extends Block> BlockRegistryObject<T> register(String name, Supplier<T> block) {
        return register(name, block, GemsBlocks::defaultItem);
    }

    private static <T extends Block> BlockRegistryObject<T> register(String name, Supplier<T> block, Function<BlockRegistryObject<T>, Supplier<? extends BlockItem>> item) {
        BlockRegistryObject<T> ret = registerNoItem(name, block);
        Registration.ITEMS.register(name, item.apply(ret));
        return ret;
    }

    private static BlockRegistryObject<FluffyBlock> registerFluffyBlock(DyeColor color) {
        return register(color.getName() + "_fluffy_block", () -> new FluffyBlock(color));
    }

    public static <T extends Block> Supplier<BlockItem> defaultItem(BlockRegistryObject<T> block) {
        return () -> new BlockItem(block.get(), new Item.Properties().group(SilentGear.ITEM_GROUP));
    }

    @Nullable
    public static ITextComponent checkForMissingLootTables(PlayerEntity player) {
        // Checks for missing block loot tables, but only in dev
        if (!(player.world instanceof ServerWorld) || !SilentGems.isDevBuild()) return null;

        LootTableManager lootTableManager = ((ServerWorld) player.world).getServer().getLootTableManager();
        Collection<String> missing = new ArrayList<>();

        for (Block block : ForgeRegistries.BLOCKS.getValues()) {
            ResourceLocation lootTable = block.getLootTable();
            // The AirBlock check filters out removed blocks
            if (lootTable.getNamespace().equals(SilentGems.MOD_ID) && !(block instanceof AirBlock) && !lootTableManager.getLootTableKeys().contains(lootTable)) {
                SilentGems.LOGGER.error("Missing block loot table '{}' for {}", lootTable, block.getRegistryName());
                missing.add(lootTable.toString());
            }
        }

        if (!missing.isEmpty()) {
            String list = String.join(", ", missing);
            return new StringTextComponent("The following block loot tables are missing: " + list).applyTextStyle(TextFormatting.RED);
        }

        return null;
    }
}
