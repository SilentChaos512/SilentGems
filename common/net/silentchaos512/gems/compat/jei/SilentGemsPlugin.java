package net.silentchaos512.gems.compat.jei;

import mezz.jei.api.IGuiHelper;
import mezz.jei.api.IJeiHelpers;
import mezz.jei.api.IJeiRuntime;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.IModRegistry;
import mezz.jei.api.JEIPlugin;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;
import net.silentchaos512.gems.api.lib.EnumMaterialGrade;
import net.silentchaos512.gems.block.ModBlocks;
import net.silentchaos512.gems.item.ItemChaosStorage;
import net.silentchaos512.gems.item.ItemReturnHome;
import net.silentchaos512.gems.item.ModItems;
import net.silentchaos512.gems.lib.Names;

@JEIPlugin
public class SilentGemsPlugin implements IModPlugin {

  public static IJeiHelpers jeiHelper;

  @Override
  public void onRuntimeAvailable(IJeiRuntime runtime) {

  }

  @Override
  public void register(IModRegistry reg) {

    jeiHelper = reg.getJeiHelpers();
    IGuiHelper guiHelper = jeiHelper.getGuiHelper();

    // Hide blocks/items
    jeiHelper.getItemBlacklist().addItemToBlacklist(new ItemStack(ModBlocks.chaosNode));
    jeiHelper.getItemBlacklist().addItemToBlacklist(new ItemStack(ModBlocks.fluffyPuffPlant));
    jeiHelper.getItemBlacklist().addItemToBlacklist(new ItemStack(ModItems.toolRenderHelper));

    // NBT ignores
    jeiHelper.getNbtIgnoreList().ignoreNbtTagNames(EnumMaterialGrade.NBT_KEY);
    jeiHelper.getNbtIgnoreList().ignoreNbtTagNames(ItemChaosStorage.NBT_CHARGE);
    jeiHelper.getNbtIgnoreList().ignoreNbtTagNames(ModItems.returnHomeCharm, ItemReturnHome.NBT_READY);

    // Descriptions
    String descPrefix = "jei.silentgems.desc.";
    reg.addDescription(new ItemStack(ModItems.gem, 1, OreDictionary.WILDCARD_VALUE),
        descPrefix + Names.GEM);
  }
}
