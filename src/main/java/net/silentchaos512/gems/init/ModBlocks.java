package net.silentchaos512.gems.init;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.registries.ForgeRegistries;
import net.silentchaos512.gems.SilentGems;
import net.silentchaos512.gems.block.*;
import net.silentchaos512.gems.block.flowerpot.LuminousFlowerPotBlock;
import net.silentchaos512.gems.block.flowerpot.PhantomLightBlock;
import net.silentchaos512.gems.block.pedestal.PedestalBlock;
import net.silentchaos512.gems.block.supercharger.BlockSupercharger;
import net.silentchaos512.gems.block.teleporter.TeleporterAnchor;
import net.silentchaos512.gems.block.tokenenchanter.TokenEnchanterBlock;
import net.silentchaos512.gems.block.urn.BlockSoulUrn;
import net.silentchaos512.gems.item.GemBlockItem;
import net.silentchaos512.gems.lib.Gems;

import javax.annotation.Nullable;
import java.util.function.Function;

public final class ModBlocks {
    public static BlockSoulUrn soulUrn;

    private ModBlocks() {}

    public static void registerAll(RegistryEvent.Register<Block> event) {
        if (!event.getRegistry().getRegistryName().equals(ForgeRegistries.BLOCKS.getRegistryName())) return;

        registerGemBlocks(Gems::getOre, gem -> gem.getName() + "_ore");
        registerGemBlocks(Gems::getBlock, gem -> gem.getName() + "_block");
        registerGemBlocks(Gems::getBricks, gem -> gem.getName() + "_bricks");
        registerGemBlocks(Gems::getGlass, gem -> gem.getName() + "_glass");
        for (GemLamp.State state : GemLamp.State.values()) {
            Function<Gems, Block> blockFactory = gem -> gem.getLamp(state);
            Function<Gems, String> nameFactory = gem -> GemLamp.nameFor(gem, state);
            if (state.hasItem()) {
                registerGemBlocks(blockFactory, nameFactory);
            } else {
                registerGemBlocksNoItem(blockFactory, nameFactory);
            }
        }
        registerGemBlocks(Gems::getGlowrose, gem -> gem.getName() + "_glowrose");
        registerGemBlocksNoItem(Gems::getPottedGlowrose, gem -> "potted_" + gem.getName() + "_glowrose");
        registerGemBlocks(Gems::getTeleporter, gem -> gem.getName() + "_teleporter");
        registerGemBlocks(Gems::getRedstoneTeleporter, gem -> gem.getName() + "_redstone_teleporter");
        register("teleporter_anchor", TeleporterAnchor.INSTANCE.get());

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
            register(type.getName(), type.getBlock());
        }

        for (CorruptedBlocks block : CorruptedBlocks.values()) {
            register(block.getName(), block.asBlock());
        }

        for (EnumDyeColor color : EnumDyeColor.values()) {
            register(color.getName() + "_fluffy_block", FluffyBlock.get(color));
        }

        soulUrn = new BlockSoulUrn();
        register("soul_urn", soulUrn, new BlockSoulUrn.ItemBlockSoulUrn(soulUrn));

        register("supercharger", BlockSupercharger.INSTANCE.get());
        register("token_enchanter", TokenEnchanterBlock.INSTANCE.get());
        // TODO: uncomment
//        register("transmutation_altar", AltarBlock.INSTANCE.get());
        register("stone_pedestal", new PedestalBlock(Block.Properties.create(Material.ROCK).hardnessAndResistance(4, 5).sound(SoundType.STONE)));
        register("granite_pedestal", new PedestalBlock(Block.Properties.create(Material.ROCK).hardnessAndResistance(4, 5).sound(SoundType.STONE)));
        register("diorite_pedestal", new PedestalBlock(Block.Properties.create(Material.ROCK).hardnessAndResistance(4, 5).sound(SoundType.STONE)));
        register("andesite_pedestal", new PedestalBlock(Block.Properties.create(Material.ROCK).hardnessAndResistance(4, 5).sound(SoundType.STONE)));
        register("obsidian_pedestal", new PedestalBlock(Block.Properties.create(Material.ROCK).hardnessAndResistance(30, 1200).sound(SoundType.STONE)));
        register("luminous_flower_pot", LuminousFlowerPotBlock.INSTANCE.get());
        register("phantom_light", PhantomLightBlock.INSTANCE.get(), null);

        register("fluffy_puff_plant", FluffyPuffPlant.NORMAL.get(), null);
        register("wild_fluffy_puff_plant", FluffyPuffPlant.WILD.get(), null);
    }

    private static <T extends Block> T register(String name, T block) {
        ItemBlock item = new GemBlockItem(block, new Item.Properties().group(ModItemGroups.BLOCKS));
        return register(name, block, item);
    }

    private static <T extends Block> T register(String name, T block, @Nullable ItemBlock item) {
        ResourceLocation id = new ResourceLocation(SilentGems.MOD_ID, name);
        block.setRegistryName(id);
        ForgeRegistries.BLOCKS.register(block);

        if (item != null) {
            item.setRegistryName(id);
            ModItems.blocksToRegister.add(item);
        }

        return block;
    }

    private static void registerGemBlocks(Function<Gems, ? extends Block> factory, Function<Gems, String> name) {
        for (Gems gem : Gems.values()) {
            register(name.apply(gem), factory.apply(gem));
        }
    }

    private static void registerGemBlocksNoItem(Function<Gems, ? extends Block> factory, Function<Gems, String> name) {
        for (Gems gem : Gems.values()) {
            register(name.apply(gem), factory.apply(gem), null);
        }
    }
}
