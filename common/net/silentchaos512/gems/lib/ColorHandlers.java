package net.silentchaos512.gems.lib;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.client.renderer.color.ItemColors;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.silentchaos512.gems.api.lib.EnumDecoPos;
import net.silentchaos512.gems.api.lib.EnumPartPosition;
import net.silentchaos512.gems.api.tool.part.ToolPart;
import net.silentchaos512.gems.client.handler.ClientTickHandler;
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

      @Override
      public int getColorFromItemstack(ItemStack stack, int tintIndex) {

        // return ToolHelper.getColorForPass(stack, tintIndex);
        // FIXME
        EnumPartPosition pos = tintIndex == 0 ? EnumPartPosition.HEAD_LEFT
            : tintIndex == 1 ? EnumPartPosition.HEAD_MIDDLE
                : tintIndex == 2 ? EnumPartPosition.HEAD_RIGHT
                    : tintIndex == 3 ? EnumPartPosition.ROD
                        : tintIndex == 4 ? EnumPartPosition.ROD_DECO : null;
        if (pos == null)
          return 0xFFFFFF;
        ToolPart part = ToolHelper.getRenderPart(stack, pos);
        if (part == null)
          return 0xFFFFFF;
        return part.getColor(stack);
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
  }
}
