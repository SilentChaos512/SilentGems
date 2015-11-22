package net.silentchaos512.gems.item.block;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.BlockGlowstone;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.silentchaos512.gems.block.BlockSG;
import net.silentchaos512.gems.core.registry.IHasSubtypes;
import net.silentchaos512.gems.core.util.LocalizationHelper;
import net.silentchaos512.gems.lib.Strings;

public class ItemBlockSG extends ItemBlock {

  protected boolean gemSubtypes = false;
  protected Block block;
  protected String itemName = "null";

  public ItemBlockSG(Block block) {

    super(block);
    this.setMaxDamage(0);

    // Block and block name
    this.block = block;
    this.itemName = block.getUnlocalizedName().substring(5);

    // Subtypes?
    if (block instanceof BlockSG) {
      BlockSG blockSG = (BlockSG) block;
      gemSubtypes = blockSG.getHasGemSubtypes();
      setHasSubtypes(blockSG.getHasSubtypes());
    } else if (block instanceof IHasSubtypes) {
      IHasSubtypes b = (IHasSubtypes) block;
      hasSubtypes = b.hasSubtypes();
      gemSubtypes = b.hasGemSubtypes();
    }
  }

  @Override
  public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean par4) {

    int i = 1;
    String s = LocalizationHelper.getBlockDescription(itemName, i);
    while (!s.equals(LocalizationHelper.getBlockDescriptionKey(itemName, i)) && i < 8) {
      list.add(EnumChatFormatting.ITALIC + s);
      s = LocalizationHelper.getBlockDescription(itemName, ++i);
    }

    if (i == 1) {
      s = LocalizationHelper.getBlockDescription(itemName, 0);
      if (!s.equals(LocalizationHelper.getBlockDescriptionKey(itemName, 0))) {
        list.add(EnumChatFormatting.ITALIC + LocalizationHelper.getBlockDescription(itemName, 0));
      }
    }
  }

  @Override
  public EnumRarity getRarity(ItemStack stack) {

    if (this.block instanceof BlockSG) {
      return ((BlockSG) block).getRarity(stack);
    } else {
      return super.getRarity(stack);
    }
  }

  @Override
  public String getUnlocalizedName(ItemStack stack) {

    String result = "tile." + Strings.RESOURCE_PREFIX + itemName;
    if (hasSubtypes) {
      result += stack.getItemDamage();
    }
    return result;
  }
  
  @Override
  public int getMetadata(int meta) {
    
    return meta;
  }
}
