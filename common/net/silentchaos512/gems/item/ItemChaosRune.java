package net.silentchaos512.gems.item;

import java.util.List;

import javax.annotation.Nonnull;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.silentchaos512.gems.SilentGems;
import net.silentchaos512.gems.lib.ChaosBuff;
import net.silentchaos512.gems.lib.Names;
import net.silentchaos512.lib.item.ItemSL;

public class ItemChaosRune extends ItemSL {

  public static final String NBT_BUFF = "chaos_buff";

  public ItemChaosRune() {

    super(1, SilentGems.MOD_ID, Names.CHAOS_RUNE);
  }

  @Override
  public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean advanced) {

    ChaosBuff buff = getBuff(stack);
    if (buff != null) {
      list.add(buff.getLocalizedName(1));

      // Debug
      list.add("  " + buff.getKey());
      list.add(String.format("  Max Level: %d", buff.getMaxLevel()));
      list.add(String.format("  Slots Used: %d", buff.getSlotsUsed(1)));
      list.add(String.format("  Chaos Cost: %d", buff.getChaosCost(1, player)));
      list.add(String.format("  Potion: %s", buff.getPotion()));
      list.add(String.format("  Color: %X", buff.getColor()));
    }
  }

  @Override
  public void getSubItems(Item item, CreativeTabs tab, List list) {

    for (ChaosBuff buff : ChaosBuff.getAllBuffs()) {
      list.add(setBuff(new ItemStack(this), buff));
    }
  }

  public ChaosBuff getBuff(@Nonnull ItemStack stack) {

    if (!stack.hasTagCompound())
      return null;
    return ChaosBuff.byKey(stack.getTagCompound().getString(NBT_BUFF));
  }

  public ItemStack setBuff(@Nonnull ItemStack stack, ChaosBuff buff) {

    if (!stack.hasTagCompound())
      stack.setTagCompound(new NBTTagCompound());
    stack.getTagCompound().setString(NBT_BUFF, buff.getKey());
    return stack;
  }

  @Override
  public EnumRarity getRarity(ItemStack stack) {

    return EnumRarity.RARE;
  }
}
