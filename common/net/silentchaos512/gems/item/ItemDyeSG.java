package net.silentchaos512.gems.item;

import java.util.List;
import java.util.Map;

import com.google.common.collect.Lists;

import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.oredict.OreDictionary;
import net.silentchaos512.gems.SilentGems;
import net.silentchaos512.gems.lib.Names;
import net.silentchaos512.lib.item.ItemSL;

public class ItemDyeSG extends ItemSL {

  public ItemDyeSG() {

    super(2, SilentGems.MODID, Names.DYE);
  }

  @Override
  public void addOreDict() {

    OreDictionary.registerOre("dyeBlack", new ItemStack(this, 1, 0));
    OreDictionary.registerOre("dyeBlue", new ItemStack(this, 1, 4));
  }

  @Override
  public void getModels(Map<Integer, ModelResourceLocation> models) {

    String prefix = SilentGems.RESOURCE_PREFIX + "dye";
    models.put(0, new ModelResourceLocation(prefix + "black", "inventory"));
    models.put(4, new ModelResourceLocation(prefix + "blue", "inventory"));
  }

  @Override
  protected void clGetSubItems(Item item, CreativeTabs tab, List<ItemStack> list) {

    if (!isInCreativeTab(tab))
      return;

    list.add(new ItemStack(this, 1, 0));
    list.add(new ItemStack(this, 1, 4));
  }

  @Override
  public String getNameForStack(ItemStack stack) {

    if (stack.getItemDamage() == 0) {
      return "DyeBlack";
    } else if (stack.getItemDamage() == 4) {
      return "DyeBlue";
    } else {
      return "DyeUnknown";
    }
  }
}
