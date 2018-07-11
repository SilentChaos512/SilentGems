package net.silentchaos512.gems.block;

import java.util.List;
import java.util.Map;

import com.google.common.collect.Lists;

import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.common.IFuelHandler;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.silentchaos512.gems.SilentGems;
import net.silentchaos512.gems.config.GemsConfig;
import net.silentchaos512.gems.init.ModItems;
import net.silentchaos512.gems.lib.Names;
import net.silentchaos512.lib.block.BlockSL;
import net.silentchaos512.lib.registry.RecipeMaker;

public class BlockMisc extends BlockSL implements IFuelHandler {

  public static enum Type implements IStringSerializable {

    CHAOS_ESSENCE, CHAOS_ESSENCE_ENRICHED, CHAOS_ESSENCE_CRYSTALLIZED, CHAOS_COAL;

    @Override
    public String getName() {

      return name().toLowerCase();
    }
  }

  public static final PropertyEnum VARIANT = PropertyEnum.create("type", Type.class);

  public BlockMisc() {

    super(Type.values().length, SilentGems.MODID, Names.MISC_BLOCK, Material.IRON);
    setHardness(3.0f);
    setResistance(30.0f);
    setDefaultState(blockState.getBaseState().withProperty(VARIANT, Type.CHAOS_ESSENCE));
    GameRegistry.registerFuelHandler(this);
  }

  @Override
  public int getBurnTime(ItemStack fuel) {

    return fuel.getItem() == Item.getItemFromBlock(this)
        && fuel.getItemDamage() == Type.CHAOS_COAL.ordinal() ? 10 * GemsConfig.BURN_TIME_CHAOS_COAL
            : 0;
  }

  @Override
  public void addRecipes(RecipeMaker recipes) {

    ItemStack chaosCoal = getStack(Type.CHAOS_COAL, 1);
    ItemStack chaosEssence = getStack(Type.CHAOS_ESSENCE, 1);
    ItemStack chaosEssenceCrystallized = getStack(Type.CHAOS_ESSENCE_CRYSTALLIZED, 1);
    ItemStack chaosEssenceEnriched = getStack(Type.CHAOS_ESSENCE_ENRICHED, 1);

    recipes.addCompression("chaos_essence_block", ModItems.craftingMaterial.chaosEssence, chaosEssence, 9);
    recipes.addCompression("chaos_essence_enriched_block", ModItems.craftingMaterial.chaosEssenceEnriched, chaosEssenceEnriched, 9);
    recipes.addCompression("chaos_essence_crystallized_block", ModItems.craftingMaterial.chaosEssenceCrystallized, chaosEssenceCrystallized, 9);
    recipes.addCompression("chaos_coal_block", ModItems.craftingMaterial.chaosCoal, chaosCoal, 9);
  }

  public ItemStack getStack(Type type, int count) {

    return new ItemStack(this, count, type.ordinal());
  }

  @Override
  public EnumRarity getRarity(int meta) {

    switch (Type.values()[MathHelper.clamp(meta, 0, Type.values().length - 1)]) {
      case CHAOS_ESSENCE_CRYSTALLIZED:
        return EnumRarity.EPIC;
      case CHAOS_ESSENCE_ENRICHED:
        return EnumRarity.RARE;
      default:
        return EnumRarity.COMMON;
    }
  }

  @Override
  public IBlockState getStateFromMeta(int meta) {

    meta = MathHelper.clamp(meta, 0, Type.values().length - 1);
    return this.getDefaultState().withProperty(VARIANT, Type.values()[meta]);
  }

  @Override
  public int getMetaFromState(IBlockState state) {

    return ((Type) state.getValue(VARIANT)).ordinal();
  }

  @Override
  protected BlockStateContainer createBlockState() {

    return new BlockStateContainer(this, new IProperty[] { VARIANT });
  }

  @Override
  public void getModels(Map<Integer, ModelResourceLocation> models) {

    String name = getFullName().toLowerCase();
    for (Type type : Type.values())
      models.put(type.ordinal(), new ModelResourceLocation(name, "type=" + type.getName()));
  }
}
