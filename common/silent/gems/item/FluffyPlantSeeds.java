package silent.gems.item;

import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemSeeds;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.OreDictionary;
import silent.gems.core.registry.IAddRecipe;
import silent.gems.core.registry.SRegistry;
import silent.gems.core.util.LocalizationHelper;
import silent.gems.lib.Names;
import silent.gems.lib.Strings;

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

//  @Override
//  public void registerIcons(IIconRegister reg) {
//
//    itemIcon = reg.registerIcon(Strings.RESOURCE_PREFIX + Names.FLUFFY_SEED);
//  }
}
