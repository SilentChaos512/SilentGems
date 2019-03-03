package net.silentchaos512.gems.init;

import net.minecraft.block.Block;
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
import net.silentchaos512.gems.block.supercharger.BlockSupercharger;
import net.silentchaos512.gems.block.urn.BlockSoulUrn;
import net.silentchaos512.gems.item.GemBlockItem;
import net.silentchaos512.gems.lib.Gems;

import java.util.function.Function;

public final class ModBlocks {
    public static FluffyPuffPlant fluffyPuffPlant;
    public static BlockSoulUrn soulUrn;

    private ModBlocks() {}

    public static void registerAll(RegistryEvent.Register<Block> event) {
        if (event.getRegistry().getRegistrySuperType() != Block.class) return;

        registerGemBlocks(Gems::getOre, gem -> gem.getName() + "_ore");
        registerGemBlocks(Gems::getBlock, gem -> gem.getName() + "_block");
        registerGemBlocks(Gems::getBricks, gem -> gem.getName() + "_bricks");
        registerGemBlocks(Gems::getGlass, gem -> gem.getName() + "_glass");
        for (GemLamp.State state : GemLamp.State.values()) {
            registerGemBlocks(gem -> gem.getLamp(state), gem -> GemLamp.nameFor(gem, state));
        }
        registerGemBlocks(Gems::getGlowrose, gem -> gem.getName() + "_glowrose");
        registerGemBlocks(Gems::getPottedGlowrose, gem -> "potted_" + gem.getName() + "_glowrose");

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

        for (EnumDyeColor color : EnumDyeColor.values()) {
            register(color.getName() + "_fluffy_block", FluffyBlock.get(color));
        }

        soulUrn = new BlockSoulUrn();
        register("soul_urn", soulUrn, new BlockSoulUrn.ItemBlockSoulUrn(soulUrn));

        register("supercharger", BlockSupercharger.INSTANCE.get());
        register("luminous_flower_pot", LuminousFlowerPotBlock.INSTANCE.get());
        register("phantom_light", PhantomLightBlock.INSTANCE.get());

        fluffyPuffPlant = register("fluffy_puff_plant", new FluffyPuffPlant());
    }

    private static <T extends Block> T register(String name, T block) {
        ItemBlock item = new GemBlockItem(block, new Item.Properties().group(ModItemGroups.BLOCKS));
        return register(name, block, item);
    }

    private static <T extends Block> T register(String name, T block, ItemBlock item) {
        ResourceLocation id = new ResourceLocation(SilentGems.MOD_ID, name);
        block.setRegistryName(id);
        ForgeRegistries.BLOCKS.register(block);

        item.setRegistryName(id);
        ModItems.blocksToRegister.add(item);

        return block;
    }

    private static void registerGemBlocks(Function<Gems, ? extends Block> factory, Function<Gems, String> name) {
        for (Gems gem : Gems.values()) {
            register(name.apply(gem), factory.apply(gem));
        }
    }
}
