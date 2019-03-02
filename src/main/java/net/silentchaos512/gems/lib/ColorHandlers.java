package net.silentchaos512.gems.lib;

import net.minecraft.client.renderer.color.BlockColors;
import net.minecraft.client.renderer.color.ItemColors;
import net.minecraftforge.client.event.ColorHandlerEvent;
import net.silentchaos512.gems.SilentGems;

public final class ColorHandlers {
    private ColorHandlers() {}

    public static void onBlockColors(ColorHandlerEvent.Block event) {
        BlockColors colors = event.getBlockColors();
        SilentGems.LOGGER.info("ColorHandlers#onBlockColors: {}", colors);
        if (colors == null) {
            SilentGems.LOGGER.error("BlockColors is null?", new IllegalStateException("wat?"));
            return;
        }

        // Soul Urn
//        colors.register(BlockSoulUrn::getBlockColor, ModBlocks.soulUrn);
    }

    public static void onItemColors(ColorHandlerEvent.Item event) {
        ItemColors colors = event.getItemColors();
        SilentGems.LOGGER.info("ColorHandlers#onItemColors: {}", colors);
        if (colors == null) {
            SilentGems.LOGGER.error("ItemColors is null?", new IllegalStateException("wat?"));
            return;
        }

        // Soul Urn
//        colors.register(BlockSoulUrn::getItemColor, ModBlocks.soulUrn);

        // Enchantment Tokens
//        colors.register(EnchantmentToken::getItemColor, EnchantmentToken.INSTANCE);

        // Return Home Charm
//        itemColors.register((stack, tintIndex) -> {
//            if (tintIndex == 0) {
//                int meta = stack.getItemDamage();
//                if (meta >= 0 && meta < Gems.values().length) {
//                    Gems gem = Gems.values()[meta];
//                    return gem.getColor();
//                }
//            }
//            return 0xFFFFFF;
//        }, ModItems.returnHomeCharm);

        // Drawing Compass
//        itemColors.register((stack, tintIndex) -> tintIndex == 0 ? ModItems.drawingCompass.getColor(stack).getColor() : 0xFFFFFF, ModItems.drawingCompass);

        // Chaos Runes
//        itemColors.registerItemColorHandler((stack, tintIndex) -> {
//            if (tintIndex == 1) {
//                ChaosBuff buff = ModItems.chaosRune.getBuff(stack);
//                if (buff != null) {
//                    return buff.getColor();
//                }
//            }
//            return 0xFFFFFF;
//        }, ModItems.chaosRune);

        // Holding Gem
//        itemColors.registerItemColorHandler((stack, tintIndex) -> {
//            if (tintIndex == 1) {
//                if (stack.hasTagCompound()) {
//                    int id = stack.getTagCompound().getShort(ItemHoldingGem.NBT_GEM_ID);
//                    if (id >= 0 && id < Gems.values().length)
//                        return Gems.values()[id].getColor();
//                }
//            } else if (tintIndex == 2) {
//                IBlockState state = ModItems.holdingGem.getBlockPlaced(stack);
//                if (state != null) {
//                    return 0xFFFFFF;
//                    // return state.getMapColor().colorValue;
//                }
//            }
//            return 0xFFFFFF;
//        }, ModItems.holdingGem);

        // Soul Gems
//        itemColors.registerItemColorHandler((stack, tintIndex) -> {
//            Soul soul = ModItems.soulGem.getSoul(stack);
//            if (soul == null) {
//                return 0xFFFFFF;
//            } else if (tintIndex == 1) {
//                return soul.colorSecondary;
//            } else {
//                return soul.colorPrimary;
//            }
//        }, ModItems.soulGem);

        // Tool Soul
//        itemColors.registerItemColorHandler((stack, tintIndex) -> {
//            ToolSoul soul = ModItems.toolSoul.getSoul(stack);
//            if (soul == null)
//                return tintIndex == 1 ? 0xFF00FF : tintIndex == 2 ? 0x0 : 0xFFFFFF;
//
//            switch (tintIndex) {
//                case 0:
//                    float ratio = 0.5f + MathHelper.sin((float) ClientTickHandler.ticksInGame / 15) / 6;
//                    return Color.blend(soul.getPrimaryElement().color, soul.getSecondaryElement().color,
//                            ratio);
//                case 1:
//                    return soul.getPrimaryElement().color;
//                case 2:
//                    return soul.getSecondaryElement().color;
//                default:
//                    return 0xFFFFFF;
//            }
//        }, ModItems.toolSoul);
    }
}
