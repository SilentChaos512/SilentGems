package net.silentchaos512.gems.lib;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.client.renderer.color.ItemColors;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.MathHelper;
import net.silentchaos512.gems.api.lib.ArmorPartPosition;
import net.silentchaos512.gems.api.lib.ToolPartPosition;
import net.silentchaos512.gems.client.handler.ClientTickHandler;
import net.silentchaos512.gems.init.ModItems;
import net.silentchaos512.gems.item.ItemGemArrow;
import net.silentchaos512.gems.item.ItemHoldingGem;
import net.silentchaos512.gems.item.ItemSoulGem.Soul;
import net.silentchaos512.gems.item.ToolRenderHelper;
import net.silentchaos512.gems.lib.soul.ToolSoul;
import net.silentchaos512.gems.util.ArmorHelper;
import net.silentchaos512.lib.util.Color;
import net.silentchaos512.lib.util.StackHelper;

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

    // Soul Gems
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

    // Tool Soul
    itemColors.registerItemColorHandler(new IItemColor() {

      @Override
      public int getColorFromItemstack(ItemStack stack, int tintIndex) {

        // TODO Auto-generated method stub
        ToolSoul soul = ModItems.toolSoul.getSoul(stack);
        if (soul == null)
          return tintIndex == 1 ? 0xFF00FF : tintIndex == 2 ? 0x0 : 0xFFFFFF;

        switch (tintIndex) {
          case 0:
            float ratio = 0.5f + MathHelper.sin((float) ClientTickHandler.ticksInGame / 15) / 6;
            return Color.blend(soul.getPrimaryElement().color, soul.getSecondaryElement().color,
                ratio);
          case 1:
            return soul.getPrimaryElement().color;
          case 2:
            return soul.getSecondaryElement().color;
          default:
            return 0xFFFFFF;
        }
      }
    }, ModItems.toolSoul);

    // Arrows
    itemColors.registerItemColorHandler(new IItemColor() {

      @Override
      public int getColorFromItemstack(ItemStack stack, int tintIndex) {

        if (StackHelper.isEmpty(stack) || !stack.hasTagCompound()
            || !stack.getTagCompound().hasKey(ItemGemArrow.NBT_STATS)) {
          return 0xFFFFFF;
        }

        NBTTagCompound tags = stack.getTagCompound().getCompoundTag(ItemGemArrow.NBT_STATS);
        switch (tintIndex) {
          case 0:
            return tags.getInteger(ItemGemArrow.NBT_COLOR_SHAFT);
          case 1:
            return tags.getInteger(ItemGemArrow.NBT_COLOR_FLETCHING);
          case 2:
            return tags.getInteger(ItemGemArrow.NBT_COLOR_HEAD);
          default:
            return 0xFFFFFF;
        }
      }
    }, ModItems.arrow);
  }
}
