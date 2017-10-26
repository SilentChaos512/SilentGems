package net.silentchaos512.gems.lib;

import java.awt.Color;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.client.renderer.color.ItemColors;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.silentchaos512.gems.api.lib.ArmorPartPosition;
import net.silentchaos512.gems.api.lib.EnumDecoPos;
import net.silentchaos512.gems.api.lib.ToolPartPosition;
import net.silentchaos512.gems.client.handler.ClientTickHandler;
import net.silentchaos512.gems.init.ModItems;
import net.silentchaos512.gems.item.ItemHoldingGem;
import net.silentchaos512.gems.item.ItemSoulGem.Soul;
import net.silentchaos512.gems.item.ToolRenderHelper;
import net.silentchaos512.gems.util.ArmorHelper;
import net.silentchaos512.gems.util.ToolHelper;

public class ColorHandlers {

  public static void init() {

    ItemColors itemColors = Minecraft.getMinecraft().getItemColors();

    // Tools
    itemColors.registerItemColorHandler(new IItemColor() {

      @Override
      public int getColorFromItemstack(ItemStack stack, int tintIndex) {

        return ToolRenderHelper.getInstance().getColor(stack,
            ToolPartPosition.forRenderPass(tintIndex));
      };
    }, ModItems.tools.toArray(new Item[ModItems.tools.size()]));

    // Shields
    itemColors.registerItemColorHandler(new IItemColor() {

      int[] passes = { ToolRenderHelper.PASS_HEAD, ToolRenderHelper.PASS_ROD };

      @Override
      public int getColorFromItemstack(ItemStack stack, int tintIndex) {

        if (tintIndex < 0 || tintIndex >= passes.length)
          return 0xFFFFFF;
        return ToolRenderHelper.getInstance().getColor(stack,
            ToolPartPosition.forRenderPass(tintIndex));
      }
    }, ModItems.shield);

    // Armor (temp)
    itemColors.registerItemColorHandler(new IItemColor() {

      @Override
      public int getColorFromItemstack(ItemStack stack, int tintIndex) {

        // return ArmorHelper.getRenderPart(stack, EnumDecoPos.NORTH).getColor(stack);
        return ArmorHelper.getRenderColor(stack, ArmorPartPosition.NORTH); // FIXME: Multiple passes needed?
      }
    }, ModItems.gemHelmet, ModItems.gemChestplate, ModItems.gemLeggings, ModItems.gemBoots);

    // Enchantment Tokens
    itemColors.registerItemColorHandler(new IItemColor() {

      @Override
      public int getColorFromItemstack(ItemStack stack, int tintIndex) {

        if (tintIndex == 1)
          return ModItems.enchantmentToken.getOutlineColor(stack);
        return 0xFFFFFF;
      }
    }, ModItems.enchantmentToken);

    // Return Home Charm
    itemColors.registerItemColorHandler(new IItemColor() {

      @Override
      public int getColorFromItemstack(ItemStack stack, int tintIndex) {

        if (tintIndex == 0) {
          int meta = stack.getItemDamage();
          if (meta >= 0 && meta < EnumGem.values().length) {
            EnumGem gem = EnumGem.values()[meta];
            return gem.color;
          }
        }
        return 0xFFFFFF;
      }
    }, ModItems.returnHomeCharm);

    // Node Mover
    itemColors.registerItemColorHandler(new IItemColor() {

      @Override
      public int getColorFromItemstack(ItemStack stack, int tintIndex) {

        return tintIndex != 1 ? 0xFFFFFF : ClientTickHandler.nodeMoverColor.getColor();
      }
    }, ModItems.nodeMover);

    // Drawing Compass
    itemColors.registerItemColorHandler(new IItemColor() {

      @Override
      public int getColorFromItemstack(ItemStack stack, int tintIndex) {

        return tintIndex == 0 ? ModItems.drawingCompass.getColor(stack).getColor() : 0xFFFFFF;
      }
    }, ModItems.drawingCompass);

    // Chaos Runes
    itemColors.registerItemColorHandler(new IItemColor() {

      @Override
      public int getColorFromItemstack(ItemStack stack, int tintIndex) {

        if (tintIndex == 1) {
          ChaosBuff buff = ModItems.chaosRune.getBuff(stack);
          if (buff != null) {
            return buff.getColor();
          }
        }
        return 0xFFFFFF;
      }
    }, ModItems.chaosRune);

    // Holding Gem
    itemColors.registerItemColorHandler(new IItemColor() {

      @Override
      public int getColorFromItemstack(ItemStack stack, int tintIndex) {

        if (tintIndex == 1) {
          if (stack.hasTagCompound()) {
            int id = stack.getTagCompound().getShort(ItemHoldingGem.NBT_GEM_ID);
            if (id >= 0 && id < EnumGem.values().length)
              return EnumGem.values()[id].color;
          }
        } else if (tintIndex == 2) {
          IBlockState state = ModItems.holdingGem.getBlockPlaced(stack);
          if (state != null) {
            return 0xFFFFFF;
            // return state.getMapColor().colorValue;
          }
        }
        return 0xFFFFFF;
      }
    }, ModItems.holdingGem);

    // Sould Gems
    itemColors.registerItemColorHandler(new IItemColor() {

      @Override
      public int getColorFromItemstack(ItemStack stack, int tintIndex) {

        Soul soul = ModItems.soulGem.getSoul(stack);
        if (soul == null) {
          return 0xFFFFFF;
        } else if (tintIndex == 1) {
          return soul.colorSecondary;
        } else {
          return soul.colorPrimary;
        }
      }
    }, ModItems.soulGem);
  }
}
