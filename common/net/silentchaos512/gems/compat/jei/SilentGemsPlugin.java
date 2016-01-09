package net.silentchaos512.gems.compat.jei;

import mezz.jei.api.IItemRegistry;
import mezz.jei.api.IJeiHelpers;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.IModRegistry;
import mezz.jei.api.IRecipeRegistry;
import mezz.jei.api.JEIPlugin;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.silentchaos512.gems.block.ModBlocks;
import net.silentchaos512.gems.core.util.LogHelper;
import net.silentchaos512.gems.core.util.ToolHelper;
import net.silentchaos512.gems.item.CraftingMaterial;
import net.silentchaos512.gems.item.ModItems;
import net.silentchaos512.gems.lib.EnumGem;
import net.silentchaos512.gems.lib.Names;

@JEIPlugin
public class SilentGemsPlugin implements IModPlugin {

  public static IJeiHelpers jeiHelper;

  @Override
  public void register(IModRegistry arg0) {

    // TODO Auto-generated method stub

    int i;
    jeiHelper.getItemBlacklist().addItemToBlacklist(new ItemStack(ModBlocks.fluffyPlant));
    for (i = 0; i < EnumGem.values().length; ++i) {
      jeiHelper.getItemBlacklist().addItemToBlacklist(new ItemStack(ModBlocks.gemLampLit, 1, i));
      jeiHelper.getItemBlacklist().addItemToBlacklist(new ItemStack(ModBlocks.gemLampInverted, 1, i));
    }
    jeiHelper.getItemBlacklist().addItemToBlacklist(CraftingMaterial.getStack(Names.CHAOS_BOOSTER));
    jeiHelper.getItemBlacklist().addItemToBlacklist(CraftingMaterial.getStack(Names.CHAOS_CAPACITOR));
    jeiHelper.getItemBlacklist().addItemToBlacklist(new ItemStack(ModItems.chaosRune, 1, 10));
    jeiHelper.getItemBlacklist().addItemToBlacklist(new ItemStack(ModItems.chaosRune, 1, 11));
    jeiHelper.getItemBlacklist().addItemToBlacklist(new ItemStack(ModItems.chaosRune, 1, 12));
    jeiHelper.getItemBlacklist().addItemToBlacklist(new ItemStack(ModItems.debugItem));
    jeiHelper.getItemBlacklist().addItemToBlacklist(new ItemStack(ModItems.brokenTool));
    jeiHelper.getItemBlacklist().addItemToBlacklist(new ItemStack(ModItems.toolRenderHelper));

    // TODO: What's this do exactly?
    jeiHelper.getNbtIgnoreList().ignoreNbtTagNames(ToolHelper.NBT_ROOT);
  }

  @Override
  public void onItemRegistryAvailable(IItemRegistry arg0) {

    // TODO Auto-generated method stub
  }

  @Override
  public void onJeiHelpersAvailable(IJeiHelpers arg0) {

    jeiHelper = arg0;
  }

  @Override
  public void onRecipeRegistryAvailable(IRecipeRegistry arg0) {

    // TODO Auto-generated method stub
  }
}
