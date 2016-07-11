package net.silentchaos512.gems.compat.jei;

import java.util.Map;
import java.util.Map.Entry;

import mezz.jei.api.IGuiHelper;
import mezz.jei.api.IJeiHelpers;
import mezz.jei.api.IJeiRuntime;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.IModRegistry;
import mezz.jei.api.ISubtypeRegistry.ISubtypeInterpreter;
import mezz.jei.api.JEIPlugin;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
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
    int any = OreDictionary.WILDCARD_VALUE;
    jeiHelper.getItemBlacklist()
        .addItemToBlacklist(new ItemStack(ModBlocks.gemLampInverted, 1, any));
    jeiHelper.getItemBlacklist()
        .addItemToBlacklist(new ItemStack(ModBlocks.gemLampInvertedDark, 1, any));
    jeiHelper.getItemBlacklist().addItemToBlacklist(new ItemStack(ModBlocks.gemLampLit, 1, any));
    jeiHelper.getItemBlacklist()
        .addItemToBlacklist(new ItemStack(ModBlocks.gemLampLitDark, 1, any));
    // jeiHelper.getItemBlacklist().addItemToBlacklist(new ItemStack(ModBlocks.chaosNode));
    jeiHelper.getItemBlacklist().addItemToBlacklist(new ItemStack(ModBlocks.fluffyPuffPlant));
    jeiHelper.getItemBlacklist().addItemToBlacklist(new ItemStack(ModItems.toolRenderHelper));

    // NBT subtypes (doesn't work?)
    jeiHelper.getSubtypeRegistry().useNbtForSubtypes(ModItems.enchantmentToken);

    // NBT ignores (deprecated)
    jeiHelper.getNbtIgnoreList().ignoreNbtTagNames(EnumMaterialGrade.NBT_KEY);
    jeiHelper.getNbtIgnoreList().ignoreNbtTagNames(ItemChaosStorage.NBT_CHARGE);
    jeiHelper.getNbtIgnoreList().ignoreNbtTagNames(ModItems.returnHomeCharm,
        ItemReturnHome.NBT_READY);

    // Descriptions
    String descPrefix = "jei.silentgems.desc.";
    reg.addDescription(new ItemStack(ModItems.gem, 1, OreDictionary.WILDCARD_VALUE),
        descPrefix + Names.GEM);
  }
}
