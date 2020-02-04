package net.silentchaos512.gems.init;

import net.minecraft.block.*;
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
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.registries.ForgeRegistries;
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
import net.silentchaos512.gems.item.GemBlockItem;
import net.silentchaos512.gems.lib.Gems;

import javax.annotation.Nullable;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;

public final class GemsBlocks {
    public static final List<PedestalBlock> pedestals = new ArrayList<>();
    public static final List<TeleporterBaseBlock> teleporters = new ArrayList<>();
    public static SoulUrnBlock soulUrn;

    private GemsBlocks() {}

    public static void registerAll(RegistryEvent.Register<Block> event) {
        registerGemBlocks(Gems::getOre, gem -> gem.getName() + "_ore");
        registerGemBlocks(Gems::getBlock, gem -> gem.getName() + "_block");
        registerGemBlocks(Gems::getBricks, gem -> gem.getName() + "_bricks");
        registerGemBlocks(Gems::getGlass, gem -> gem.getName() + "_glass");
        for (GemLampBlock.State state : GemLampBlock.State.values()) {
            Function<Gems, Block> blockFactory = gem -> gem.getLamp(state);
            Function<Gems, String> nameFactory = gem -> GemLampBlock.nameFor(gem, state);
            if (state.hasItem()) {
                registerGemBlocks(blockFactory, nameFactory);
            } else {
                registerGemBlocksNoItem(blockFactory, nameFactory);
            }
        }
        registerGemBlocks(Gems::getGlowrose, gem -> gem.getName() + "_glowrose");
        registerGemBlocksNoItem(Gems::getPottedGlowrose, gem -> "potted_" + gem.getName() + "_glowrose");
        Arrays.stream(Gems.values()).forEach(gem -> {
            ResourceLocation flowerId = Objects.requireNonNull(gem.getGlowrose().getRegistryName());
            ((FlowerPotBlock) Blocks.FLOWER_POT).addPlant(flowerId, gem::getPottedGlowrose);
        });

        registerGemBlocks(Gems::getTeleporter, gem -> gem.getName() + "_teleporter", teleporters::add);
        registerGemBlocks(Gems::getRedstoneTeleporter, gem -> gem.getName() + "_redstone_teleporter", teleporters::add);
        teleporters.add(register("teleporter_anchor", new TeleporterBaseBlock(true)));

        register("multi_ore_classic", Gems.Set.CLASSIC.getMultiOre());
        register("multi_ore_dark", Gems.Set.DARK.getMultiOre());
        register("multi_ore_light", Gems.Set.LIGHT.getMultiOre());

        for (MiscBlocks misc : MiscBlocks.values()) {
            register(misc.getName(), misc.getBlock(), misc.getBlockItem());
        }

        for (MiscOres ore : MiscOres.values()) {
            register(ore.getName(), ore.getBlock());
        }

        for (HardenedRock type : HardenedRock.values()) {
            register(type.getName(), type.asBlock());
        }

        for (CorruptedBlocks block : CorruptedBlocks.values()) {
            register(block.getName(), block.asBlock());
        }

        for (DyeColor color : DyeColor.values()) {
            register(color.getName() + "_fluffy_block", FluffyBlock.get(color));
        }

        soulUrn = register("soul_urn", SoulUrnBlock.INSTANCE.get(), new SoulUrnBlock.SoulUrnBlockItem(SoulUrnBlock.INSTANCE.get()));

        register("supercharger", SuperchargerBlock.INSTANCE.get());
        register("token_enchanter", TokenEnchanterBlock.INSTANCE.get());
        register("transmutation_altar", AltarBlock.INSTANCE.get());
        register("purifier", PurifierBlock.INSTANCE.get());

        registerPedestal("stone_pedestal", new PedestalBlock(Block.Properties.create(Material.ROCK).hardnessAndResistance(4, 5).sound(SoundType.STONE)));
        registerPedestal("granite_pedestal", new PedestalBlock(Block.Properties.create(Material.ROCK).hardnessAndResistance(4, 5).sound(SoundType.STONE)));
        registerPedestal("diorite_pedestal", new PedestalBlock(Block.Properties.create(Material.ROCK).hardnessAndResistance(4, 5).sound(SoundType.STONE)));
        registerPedestal("andesite_pedestal", new PedestalBlock(Block.Properties.create(Material.ROCK).hardnessAndResistance(4, 5).sound(SoundType.STONE)));
        registerPedestal("obsidian_pedestal", new PedestalBlock(Block.Properties.create(Material.ROCK).hardnessAndResistance(30, 1200).sound(SoundType.STONE)));

        register("luminous_flower_pot", LuminousFlowerPotBlock.INSTANCE.get());
        register("phantom_light", PhantomLightBlock.INSTANCE.get(), null);

        register("fluffy_puff_plant", FluffyPuffPlant.INSTANCE.get(), null);
        register("wild_fluffy_puff_plant", WildFluffyPuffPlant.INSTANCE.get(), null);
    }

    @OnlyIn(Dist.CLIENT)
    public static void registerRenderTypes(FMLClientSetupEvent event) {
        for (Gems gem : Gems.values()) {
            RenderTypeLookup.setRenderLayer(gem.getGlass(), RenderType.translucent());
            RenderTypeLookup.setRenderLayer(gem.getGlowrose(), RenderType.cutout());
        }
        RenderTypeLookup.setRenderLayer(FluffyPuffPlant.INSTANCE.get(), RenderType.cutout());
        RenderTypeLookup.setRenderLayer(WildFluffyPuffPlant.INSTANCE.get(), RenderType.cutout());
    }

    private static void registerPedestal(String name, PedestalBlock block) {
        register(name, block);
        pedestals.add(block);
    }

    private static <T extends Block> T register(String name, T block) {
        BlockItem item = new GemBlockItem(block, new Item.Properties().group(GemsItemGroups.BLOCKS));
        return register(name, block, item);
    }

    private static <T extends Block> T register(String name, T block, @Nullable BlockItem item) {
        ResourceLocation id = SilentGems.getId(name);
        block.setRegistryName(id);
        ForgeRegistries.BLOCKS.register(block);

        if (item != null) {
            item.setRegistryName(id);
            GemsItems.BLOCKS_TO_REGISTER.add(item);
        }

        return block;
    }

    private static <T extends Block> void registerGemBlocks(Function<Gems, T> factory, Function<Gems, String> name) {
        registerGemBlocks(factory, name, t -> {});
    }

    private static <T extends Block> void registerGemBlocks(Function<Gems, T> factory, Function<Gems, String> name, Consumer<T> extraAction) {
        for (Gems gem : Gems.values()) {
            T block = factory.apply(gem);
            register(name.apply(gem), block);
            extraAction.accept(block);
        }
    }

    private static <T extends Block> void registerGemBlocksNoItem(Function<Gems, T> factory, Function<Gems, String> name) {
        for (Gems gem : Gems.values()) {
            register(name.apply(gem), factory.apply(gem), null);
        }
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
