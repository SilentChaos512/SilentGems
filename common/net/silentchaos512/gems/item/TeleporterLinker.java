package net.silentchaos512.gems.item;

import java.util.List;

import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumChatFormatting;
import net.silentchaos512.gems.configuration.Config;
import net.silentchaos512.gems.core.util.DimensionalPosition;
import net.silentchaos512.gems.core.util.LocalizationHelper;
import net.silentchaos512.gems.lib.Names;
import net.silentchaos512.gems.lib.Strings;

public class TeleporterLinker extends ItemSG {

  public TeleporterLinker() {

    setMaxStackSize(1);
    setUnlocalizedName(Names.TELEPORTER_LINKER);
  }

  @Override
  public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean par4) {

    // Verify NBT
    if (stack.stackTagCompound == null) {
      stack.stackTagCompound = new NBTTagCompound();
    }
    if (!stack.stackTagCompound.hasKey(Strings.TELEPORTER_LINKER_STATE)) {
      stack.stackTagCompound.setBoolean(Strings.TELEPORTER_LINKER_STATE, false);
    }

    // Display state and coords (if active)
    if (isLinked(stack)) {
      list.add(EnumChatFormatting.GREEN + LocalizationHelper.getOtherItemKey(itemName, "Active"));
      DimensionalPosition pos = getLinkedPosition(stack);
      String posStr = LocalizationHelper.getMiscText("DimensionalPosition");
      posStr = String.format(posStr, pos.x, pos.y, pos.z, pos.d);
      list.add(posStr);
    } else {
      list.add(EnumChatFormatting.RED + LocalizationHelper.getOtherItemKey(itemName, "Inactive"));
    }

    list.add(EnumChatFormatting.DARK_GRAY + LocalizationHelper.getItemDescription(itemName, 0));
  }

  @Override
  public void addRecipes() {

    GameRegistry.addShapedRecipe(new ItemStack(this), "c", "s", 'c',
        CraftingMaterial.getStack(Names.CHAOS_ESSENCE_PLUS), 's',
        CraftingMaterial.getStack(Names.ORNATE_STICK));
  }

  @Override
  public boolean hasEffect(ItemStack stack, int pass) {

    return isLinked(stack);
  }
  
  public boolean isLinked(ItemStack stack) {
    
    NBTTagCompound tags = stack.getTagCompound();
    if (tags != null) {
      return tags.getBoolean(Strings.TELEPORTER_LINKER_STATE);
    }
    return false;
  }
  
  public DimensionalPosition getLinkedPosition(ItemStack stack) {
    
    if (!stack.hasTagCompound()) {
      return new DimensionalPosition(0, 0, 0, 0);
    }
    return DimensionalPosition.fromNBT(stack.getTagCompound());
  }

  @SideOnly(Side.CLIENT)
  public static void renderGameOverlay(Minecraft mc) {

    EntityPlayer player = mc.thePlayer;

    ItemStack heldItem = mc.thePlayer.getHeldItem();
    if (heldItem != null && heldItem.getItem() == ModItems.teleporterLinker) {
      TeleporterLinker linker = (TeleporterLinker) heldItem.getItem();

      ScaledResolution res = new ScaledResolution(mc, mc.displayWidth, mc.displayHeight);
      FontRenderer fontRender = mc.fontRenderer;
      int width = res.getScaledWidth();
      int height = res.getScaledHeight();

      String str;
      if (linker.isLinked(heldItem)) {
        DimensionalPosition pos = linker.getLinkedPosition(heldItem);
        double x = pos.x - player.posX;
        double z = pos.z - player.posZ;
        int distance = (int) Math.sqrt(x * x + z * z);
        str = LocalizationHelper.getOtherItemKey(Names.TELEPORTER_LINKER, "Distance");
        str = String.format(str, distance);

        int textX = width / 2 - fontRender.getStringWidth(str) / 2;
        int textY = height * 3 / 5;
        // Text colored differently depending on situation.
        int color = 0xffff00; // Outside free range, same dimension
        if (pos.d != player.dimension) {
          color = 0xff6600; // Different dimension
          str = LocalizationHelper.getOtherItemKey(Names.TELEPORTER_LINKER, "DifferentDimension");
        } else if (distance < Config.TELEPORTER_XP_FREE_RANGE) {
          color = 0x00aaff; // Inside free range
        }
        fontRender.drawStringWithShadow(str, textX, textY, color);
      }
    }
  }
}
