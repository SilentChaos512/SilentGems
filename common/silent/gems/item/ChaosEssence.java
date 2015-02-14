package silent.gems.item;

import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import silent.gems.block.ChaosEssenceBlock.EnumType;
import silent.gems.core.registry.SRegistry;
import silent.gems.core.util.LocalizationHelper;
import silent.gems.core.util.RecipeHelper;
import silent.gems.lib.Names;

public class ChaosEssence extends ItemSG {

  public ChaosEssence() {

    super(EnumType.values().length);

    setMaxStackSize(64);
    setHasSubtypes(true);
    setUnlocalizedName(Names.CHAOS_ESSENCE);
  }

  public static ItemStack getByType(EnumType type) {

    return new ItemStack(SRegistry.getItem(Names.CHAOS_ESSENCE), 1, type.getMetadata());
  }

  @Override
  public void addRecipes() {

    ItemStack essenceRaw = getByType(EnumType.RAW);
    ItemStack essenceRegular = getByType(EnumType.REGULAR);
    ItemStack essenceRefined = getByType(EnumType.REFINED);
    Item itemRefiner = SRegistry.getItem(Names.CHAOS_REFINER);
    ItemStack refiner = new ItemStack(itemRefiner.setContainerItem(itemRefiner));
    ItemStack glowstone = new ItemStack(Items.glowstone_dust);

    // Raw -> Regular
    ItemStack result = new ItemStack(essenceRegular.getItem(), 8, essenceRegular.getItemDamage());
    RecipeHelper.addSurround(result, refiner, essenceRaw);
    // Regular -> Refined
    RecipeHelper.addSurround(essenceRefined, glowstone, new Object[] { Items.redstone,
        essenceRegular });
  }
  
  @Override
  public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean advanced) {
    
    int meta = stack.getItemDamage();
    String name = itemName + meta;
    int i = 1;
    String s = LocalizationHelper.getItemDescription(name, i);
    while (!s.equals(LocalizationHelper.getItemDescriptionKey(name, i)) && i < 8) {
      list.add(EnumChatFormatting.ITALIC + s);
      s = LocalizationHelper.getItemDescription(name, ++i);
    }
    
    if (i == 1) {
      s = LocalizationHelper.getItemDescription(name, 0);
      if (!s.equals(LocalizationHelper.getItemDescriptionKey(name, 0))) {
        list.add(EnumChatFormatting.ITALIC + LocalizationHelper.getItemDescription(name, 0));
      }
    }
  }
}
