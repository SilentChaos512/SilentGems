package net.silentchaos512.gems.lib;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.client.renderer.color.ItemColors;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.silentchaos512.gems.api.lib.EnumDecoPos;
import net.silentchaos512.gems.client.handler.ClientTickHandler;
import net.silentchaos512.gems.item.ItemHoldingGem;
import net.silentchaos512.gems.item.ModItems;
import net.silentchaos512.gems.util.ArmorHelper;
import net.silentchaos512.gems.util.ToolHelper;

public class ColorHandlers {

  public static void init() {

    ItemColors itemColors = Minecraft.getMinecraft().getItemColors();

    // Tools
    itemColors.registerItemColorHandler(new IItemColor() {

      @Override
      public int getColorFromItemstack(ItemStack stack, int tintIndex) {

        return ToolHelper.getColorForPass(stack, tintIndex);
      };
    }, ModItems.tools.toArray(new Item[ModItems.tools.size()]));

    // Shields
    itemColors.registerItemColorHandler(new IItemColor() {

      int[] passes = { 2, 1, 3, 0, 4, 5, 6 }; // 5 & 6 unused

      @Override
      public int getColorFromItemstack(ItemStack stack, int tintIndex) {

        if (tintIndex < 0 || tintIndex >= passes.length)
          return 0xFFFFFF;
        return ToolHelper.getColorForPass(stack, passes[tintIndex]);
      }
    }, ModItems.shield);

    // Armor (temp)
    itemColors.registerItemColorHandler(new IItemColor() {

      @Override
      public int getColorFromItemstack(ItemStack stack, int tintIndex) {

        // return ArmorHelper.getRenderPart(stack, EnumDecoPos.NORTH).getColor(stack);
        return ArmorHelper.getRenderColor(stack, EnumDecoPos.NORTH); // FIXME: Multiple passes needed?
      }
    }, ModItems.gemHelmet, ModItems.gemChestplate, ModItems.gemLeggings, ModItems.gemBoots);

    // Gem Shards
    itemColors.registerItemColorHandler(new IItemColor() {

      @Override
      public int getColorFromItemstack(ItemStack stack, int tintIndex) {

        return stack.getItemDamage() > 15 ? 0x999999 : 0xFFFFFF;
      }
    }, ModItems.gemShard);

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

        return stack.getItemDamage() > 15 && tintIndex == 1 ? 0x999999 : 0xFFFFFF;
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

        return tintIndex == 1 ? ModItems.chaosRune.getBuff(stack).getColor() : 0xFFFFFF;
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
          if (state != null)
            return state.getMapColor().colorValue;
        }
        return 0xFFFFFF;
      }
    }, ModItems.holdingGem);
  }
}
