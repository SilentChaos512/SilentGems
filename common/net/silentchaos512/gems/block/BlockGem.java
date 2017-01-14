package net.silentchaos512.gems.block;

import java.util.List;

import com.google.common.collect.Lists;

import net.minecraft.block.SoundType;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemModelMesher;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.silentchaos512.gems.SilentGems;
import net.silentchaos512.gems.lib.EnumGem;
import net.silentchaos512.gems.lib.Names;
import net.silentchaos512.lib.util.RecipeHelper;

public class BlockGem extends BlockGemSubtypes {

  public final boolean supercharged;

  public BlockGem(boolean dark, boolean supercharged) {

    super(dark, Names.GEM_BLOCK + (supercharged ? "Super" : "") + (dark ? "Dark" : ""));
    this.supercharged = supercharged;

    setHardness(supercharged ? 7.0f : 3.0f);
    setResistance(supercharged ? 6000000.0F : 30.0f);
    setSoundType(SoundType.METAL);
    setHarvestLevel("pickaxe", supercharged ? 3 : 1);
  }

  @Override
  public void addRecipes() {

    for (int i = 0; i < 16; ++i) {
      EnumGem gem = getGem(i);

      if (supercharged) {
        GameRegistry.addRecipe(new ShapedOreRecipe(gem.getBlockSuper(), " g ", "gog", " g ", 'g',
            gem.getItemSuperOreName(), 'o', Blocks.OBSIDIAN));
      } else {
        RecipeHelper.addCompressionRecipe(gem.getItem(), gem.getBlock(), 9);
      }
    }
  }

  @Override
  public void addOreDict() {

    for (int i = 0; i < 16; ++i) {
      EnumGem gem = getGem(i);
      if (supercharged) {
        OreDictionary.registerOre(gem.getBlockSuperOreName(), gem.getBlockSuper());
      } else {
        OreDictionary.registerOre(gem.getBlockOreName(), gem.getBlock());
      }
    }
  }

  @Override
  public void addInformation(ItemStack stack, EntityPlayer player, List<String> list,
      boolean advanced) {

    list.addAll(SilentGems.instance.localizationHelper
        .getBlockDescriptionLines(blockName.replaceFirst("Dark", "")));
  }

  @Override
  public EnumRarity getRarity(int meta) {

    return supercharged ? EnumRarity.RARE : EnumRarity.COMMON;
  }

  @Override
  public boolean canEntityDestroy(IBlockState state, IBlockAccess world, BlockPos pos,
      Entity entity) {

    // Does not prevent Withers from breaking. Only the Ender Dragon seems to call this...
    if (supercharged) {
      return entity instanceof EntityPlayer;
    }
    return super.canEntityDestroy(state, world, pos, entity);
  }
}
