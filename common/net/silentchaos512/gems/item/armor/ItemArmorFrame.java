package net.silentchaos512.gems.item.armor;

import java.util.List;

import com.google.common.collect.Lists;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemModelMesher;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.silentchaos512.gems.SilentGems;
import net.silentchaos512.gems.api.lib.EnumMaterialTier;
import net.silentchaos512.gems.item.ModItems;
import net.silentchaos512.gems.lib.Names;
import net.silentchaos512.lib.item.ItemSL;

public class ItemArmorFrame extends ItemSL {

  public ItemArmorFrame() {

    // Meta bits: TTAA
    // Where T = tier, A = armor type
    // For armor type, 0 = Helmet, 1 = Chestplate, etc. (opposite of slot index)

    super(4 * EnumMaterialTier.values().length, SilentGems.MODID, Names.ARMOR_FRAME);
  }

  public ItemStack getFrameForArmorPiece(ItemArmor itemArmor, EnumMaterialTier tier) {

    int type = 3 - itemArmor.armorType.getIndex();
    return new ItemStack(this, 1, type | (tier.ordinal() << 2));
  }

  public EnumMaterialTier getTier(ItemStack stack) {

    int index = stack.getItemDamage() >> 2;
    return EnumMaterialTier.values()[MathHelper.clamp(index, 0,
        EnumMaterialTier.values().length - 1)];
  }

  public Item getOutputItem(ItemStack stack) {

    return getOutputItem(stack.getItemDamage() & 3);
  }

  private Item getOutputItem(int armorType) {

    switch (armorType) {
      //@formatter:off
      case 0: return ModItems.gemHelmet;
      case 1: return ModItems.gemChestplate;
      case 2: return ModItems.gemLeggings;
      case 3: return ModItems.gemBoots;
      default: return null;
      //@formatter:on
    }
  }

  @Override
  public void addRecipes() {

    ItemStack lattice;

    for (int tier = 0; tier < EnumMaterialTier.values().length; ++tier) {
      lattice = tier == 0 ? ModItems.craftingMaterial.armorLatticeMundane
          : tier == 1 ? ModItems.craftingMaterial.armorLatticeRegular
              : tier == 2 ? ModItems.craftingMaterial.armorLatticeSuper : ItemStack.EMPTY;

      if (!lattice.isEmpty())
        for (int type = 0; type < 4; ++type)
          addRecipe(new ItemStack(this, 1, type + (tier << 2)), lattice, type);
    }
  }

  private void addRecipe(ItemStack output, ItemStack lattice, int armorType) {

    switch (armorType) {
      case 0:
        GameRegistry.addShapedRecipe(output, "lll", "l l", 'l', lattice);
        break;
      case 1:
        GameRegistry.addShapedRecipe(output, "l l", "lll", "lll", 'l', lattice);
        break;
      case 2:
        GameRegistry.addShapedRecipe(output, "lll", "l l", "l l", 'l', lattice);
        break;
      case 3:
        GameRegistry.addShapedRecipe(output, "l l", "l l", 'l', lattice);
        break;
    }
  }

  // @Override
  // public List<ModelResourceLocation> getVariants() {
  //
  // List<ModelResourceLocation> models = Lists.newArrayList();
  // for (int i = 0; i < 4; ++i)
  // models.add(new ModelResourceLocation(getFullName() + i, "inventory"));
  // return models;
  // }
  //
  // @Override
  // public boolean registerModels() {
  //
  // List<ModelResourceLocation> models = getVariants();
  // ItemModelMesher mesher = Minecraft.getMinecraft().getRenderItem().getItemModelMesher();
  //
  // for (int i = 0; i < subItemCount; ++i)
  // mesher.register(this, i, models.get(i & 3));
  //
  // return true;
  // }
}
