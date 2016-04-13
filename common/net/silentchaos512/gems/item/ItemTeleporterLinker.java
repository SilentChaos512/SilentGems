package net.silentchaos512.gems.item;

import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumHand;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.silentchaos512.gems.SilentGems;
import net.silentchaos512.gems.config.Config;
import net.silentchaos512.gems.lib.Names;
import net.silentchaos512.lib.item.ItemSL;
import net.silentchaos512.lib.util.DimensionalPosition;
import net.silentchaos512.lib.util.LocalizationHelper;

public class ItemTeleporterLinker extends ItemSL {

  public ItemTeleporterLinker() {

    super(1, SilentGems.MOD_ID, Names.TELEPORTER_LINKER);
    setMaxStackSize(1);
  }

  @Override
  public void addRecipes() {

    GameRegistry.addShapedRecipe(new ItemStack(this), "c", "r", 'c',
        ModItems.craftingMaterial.chaosEssenceEnriched, 'r', ModItems.craftingMaterial.toolRodGold);
  }

  @Override
  public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean advanced) {

    DimensionalPosition pos = getLinkedPosition(stack);
    if (pos != null) {
      list.add(pos.toString());
    }
  }

  public boolean isLinked(ItemStack stack) {

    return stack.getItemDamage() != 0;
  }

  public void setLinked(ItemStack stack, boolean value) {

    stack.setItemDamage(value ? 1 : 0);
  }

  public DimensionalPosition getLinkedPosition(ItemStack stack) {

    if (!stack.hasTagCompound()) {
      return null;
    }
    return DimensionalPosition.readFromNBT(stack.getTagCompound());
  }

  public void setLinkedPosition(ItemStack stack, DimensionalPosition pos) {

    if (!stack.hasTagCompound()) {
      stack.setTagCompound(new NBTTagCompound());
    }
    pos.writeToNBT(stack.getTagCompound());
  }

  @Override
  public boolean hasEffect(ItemStack stack) {

    return isLinked(stack);
  }

  @SideOnly(Side.CLIENT)
  public void renderGameOverlay(Minecraft mc) {

    EntityPlayer player = mc.thePlayer;

    ItemStack heldItem = mc.thePlayer.getHeldItem(EnumHand.MAIN_HAND);
    if (heldItem == null) {
      heldItem = mc.thePlayer.getHeldItem(EnumHand.OFF_HAND);
    }

    if (heldItem != null && heldItem.getItem() == this) {

      ScaledResolution res = new ScaledResolution(mc);
      FontRenderer fontRender = mc.fontRendererObj;
      int width = res.getScaledWidth();
      int height = res.getScaledHeight();

      String str;
      if (isLinked(heldItem)) {
        DimensionalPosition pos = getLinkedPosition(heldItem);
        double x = pos.x - player.posX;
        double z = pos.z - player.posZ;
        int distance = (int) Math.sqrt(x * x + z * z);
        LocalizationHelper loc = SilentGems.instance.localizationHelper;
        str = loc.getItemSubText(itemName, "Distance");
        str = String.format(str, distance);

        int textX = width / 2 - fontRender.getStringWidth(str) / 2;
        int textY = height * 3 / 5;
        // Text colored differently depending on situation.
        int color = 0xffff00; // Outside free range, same dimension
        if (pos.dim != player.dimension) {
          color = 0xff6600; // Different dimension
          str = loc.getItemSubText(itemName, "DifferentDimension");
        } else if (distance < Config.TELEPORTER_FREE_RANGE) {
          color = 0x00aaff; // Inside free range
        }
        fontRender.drawStringWithShadow(str, textX, textY, color);
      }
    }
  }
}
