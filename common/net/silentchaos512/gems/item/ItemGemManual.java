package net.silentchaos512.gems.item;

import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.silentchaos512.gems.SilentGems;
import net.silentchaos512.gems.client.gui.GuiHandlerSilentGems;
import net.silentchaos512.gems.lib.Names;

public class ItemGemManual extends ItemSG {

  public ItemGemManual() {

    setMaxStackSize(1);
    setMaxDamage(0);
    setUnlocalizedName(Names.GEM_MANUAL);
  }

  @Override
  public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean advanced) {

    // TODO
  }

  @Override
  public boolean onItemUse(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ) {

    // TODO
    return false;
  }

  @Override
  public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player) {

    // TODO
    openBook(player, stack, world, false);
    return stack;
  }

  public static void openBook(EntityPlayer player, ItemStack stack, World world, boolean skipSound) {

    // TODO
    player.openGui(SilentGems.instance, GuiHandlerSilentGems.ID_MANUAL, world, 0, 0, 0);
  }

  // onUpdate?
}
