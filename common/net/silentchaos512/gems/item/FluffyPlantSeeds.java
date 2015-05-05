package net.silentchaos512.gems.item;

import java.util.List;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemSeeds;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.oredict.OreDictionary;
import net.silentchaos512.gems.core.registry.IAddRecipe;
import net.silentchaos512.gems.core.registry.SRegistry;
import net.silentchaos512.gems.core.util.LocalizationHelper;
import net.silentchaos512.gems.lib.Names;
import net.silentchaos512.gems.lib.Strings;
import cpw.mods.fml.common.registry.GameRegistry;

public class FluffyPlantSeeds extends ItemSeeds implements IAddRecipe {

  public FluffyPlantSeeds() {

    super(SRegistry.getBlock(Names.FLUFFY_PLANT), Blocks.farmland);
    this.setUnlocalizedName(Names.FLUFFY_SEED);
    MinecraftForge.addGrassSeed(new ItemStack(this), 2);
  }

  @Override
  public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean par4) {

    list.add(EnumChatFormatting.ITALIC
        + LocalizationHelper.getItemDescription(Names.FLUFFY_SEED, 0));
  }

  @Override
  public void addOreDict() {

    OreDictionary.registerOre("materialCotton", this);
  }

  @Override
  public void addRecipes() {

    GameRegistry.addShapedRecipe(new ItemStack(Items.string), "ff", 'f', this);
    GameRegistry.addShapedRecipe(new ItemStack(Blocks.wool), "fff", "f f", "fff", 'f', this);
    GameRegistry.addShapedRecipe(new ItemStack(Items.feather), " ff", "ff ", "f  ", 'f', this);
  }

  @Override
  public String getUnlocalizedName(ItemStack stack) {

    return LocalizationHelper.ITEM_PREFIX + Names.FLUFFY_SEED;
  }

  @Override
  public void registerIcons(IIconRegister reg) {

    itemIcon = reg.registerIcon(Strings.RESOURCE_PREFIX + Names.FLUFFY_SEED);
  }
}
