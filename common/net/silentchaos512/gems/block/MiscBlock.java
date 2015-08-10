package net.silentchaos512.gems.block;

import java.util.List;

import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.silentchaos512.gems.configuration.Config;
import net.silentchaos512.gems.core.util.RecipeHelper;
import net.silentchaos512.gems.item.CraftingMaterial;
import net.silentchaos512.gems.lib.Names;
import net.silentchaos512.gems.lib.Strings;
import cpw.mods.fml.common.IFuelHandler;
import cpw.mods.fml.common.registry.GameRegistry;

public class MiscBlock extends BlockSG implements IFuelHandler {

  public final static String[] NAMES = { Names.CHAOS_ESSENCE_BLOCK,
      Names.CHAOS_ESSENCE_BLOCK_REFINED, "reserved", Names.CHAOS_COAL_BLOCK };

  public MiscBlock() {

    super(Material.iron);
    this.icons = new IIcon[NAMES.length];
    this.setResistance(30.0f);
    this.setHasSubtypes(true);
    this.setUnlocalizedName(Names.MISC_BLOCKS);
  }

  @Override
  public void addRecipes() {

    GameRegistry.registerFuelHandler(this);

    // Chaos essence blocks
    ItemStack chaosEssence = CraftingMaterial.getStack(Names.CHAOS_ESSENCE);
    ItemStack chaosEssenceRefined = CraftingMaterial.getStack(Names.CHAOS_ESSENCE_PLUS);
    ItemStack essenceBlock = this.getStack(Names.CHAOS_ESSENCE_BLOCK);
    ItemStack refinedEssenceBlock = this.getStack(Names.CHAOS_ESSENCE_BLOCK_REFINED);
    RecipeHelper.addCompressionRecipe(chaosEssence, essenceBlock, 9);
    RecipeHelper.addCompressionRecipe(chaosEssenceRefined, refinedEssenceBlock, 9);

    // Chaos coal block
    ItemStack chaosCoal = CraftingMaterial.getStack(Names.CHAOS_COAL);
    ItemStack chaosCoalBlock = this.getStack(Names.CHAOS_COAL_BLOCK);
    RecipeHelper.addCompressionRecipe(chaosCoal, chaosCoalBlock, 9);
    RecipeHelper.addSurround(this.getStack(Names.CHAOS_COAL_BLOCK, 8), essenceBlock,
        Blocks.coal_block);
  }

  @Override
  public int getBurnTime(ItemStack stack) {

    if (stack != null && stack.getItem() == Item.getItemFromBlock(this)
        && stack.getItemDamage() == getStack(Names.CHAOS_COAL_BLOCK).getItemDamage()) {
      return Config.chaosCoalBurnTime * 10;
    }
    return 0;
  }

  @Override
  public EnumRarity getRarity(ItemStack stack) {

    if (stack.getItemDamage() == 1) {
      return EnumRarity.rare;
    } else {
      return super.getRarity(stack);
    }
  }

  public static ItemStack getStack(String name) {

    for (int i = 0; i < NAMES.length; ++i) {
      if (NAMES[i].equals(name)) {
        return new ItemStack(ModBlocks.miscBlock, 1, i);
      }
    }

    return null;
  }

  public static ItemStack getStack(String name, int count) {

    for (int i = 0; i < NAMES.length; ++i) {
      if (NAMES[i].equals(name)) {
        return new ItemStack(ModBlocks.miscBlock, count, i);
      }
    }

    return null;
  }

  @Override
  public void getSubBlocks(Item item, CreativeTabs tab, List list) {

    for (int i = 0; i < 4; ++i) {
      list.add(new ItemStack(this, 1, i));
    }
  }

  @Override
  public void registerBlockIcons(IIconRegister iconRegister) {

    for (int i = 0; i < NAMES.length; ++i) {
      icons[i] = iconRegister.registerIcon(Strings.RESOURCE_PREFIX + NAMES[i]);
    }
  }
}
