package net.silentchaos512.gems.block;

import java.util.List;

import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.MathHelper;
import net.minecraftforge.fml.common.IFuelHandler;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.silentchaos512.gems.SilentGems;
import net.silentchaos512.gems.configuration.Config;
import net.silentchaos512.gems.core.util.RecipeHelper;
import net.silentchaos512.gems.item.CraftingMaterial;
import net.silentchaos512.gems.lib.EnumGem;
import net.silentchaos512.gems.lib.Names;
import net.silentchaos512.gems.lib.Strings;

public class MiscBlock extends BlockSG implements IFuelHandler {

  public static enum EnumVariant implements IStringSerializable {
    
    // This is hideous. Why, Mojang, why?
    CHAOS_ESSENCE(Names.CHAOS_ESSENCE_BLOCK),
    CHAOS_ESSENCE_REFINED(Names.CHAOS_ESSENCE_BLOCK_REFINED),
    CHAOS_ESSENCE_CRYSTALLIZED(Names.CHAOS_ESSENCE_BLOCK_CRYSTALLIZED),
    CHAOS_COAL(Names.CHAOS_COAL_BLOCK);
    
    public final String name;
    
    private EnumVariant(String name) {
      
      this.name = name;
    }
    
    @Override
    public String getName() {
      
      return name;
    }
    
    public static EnumVariant get(int meta) {

      return values()[MathHelper.clamp_int(meta, 0, values().length - 1)];
    }
  }
  
  public static final PropertyEnum VARIANT = PropertyEnum.create("variant", EnumVariant.class);

  public MiscBlock() {

    super(EnumVariant.values().length, Material.iron);

    this.setResistance(30.0f);
    this.setHasSubtypes(true);
    this.setUnlocalizedName(Names.MISC_BLOCKS);
  }

  @Override
  public void addRecipes() {

    GameRegistry.registerFuelHandler(this);

    // Chaos essence blocks
    ItemStack essence = CraftingMaterial.getStack(Names.CHAOS_ESSENCE);
    ItemStack essenceRefined = CraftingMaterial.getStack(Names.CHAOS_ESSENCE_PLUS);
    ItemStack essenceCrystallized = CraftingMaterial.getStack(Names.CHAOS_ESSENCE_PLUS_2);
    ItemStack blockEssence = this.getStack(Names.CHAOS_ESSENCE_BLOCK);
    ItemStack blockRefinedEssence = this.getStack(Names.CHAOS_ESSENCE_BLOCK_REFINED);
    ItemStack blockCrystallizedEssence = this.getStack(Names.CHAOS_ESSENCE_BLOCK_CRYSTALLIZED);
    RecipeHelper.addCompressionRecipe(essence, blockEssence, 9);
    RecipeHelper.addCompressionRecipe(essenceRefined, blockRefinedEssence, 9);
    RecipeHelper.addCompressionRecipe(essenceCrystallized, blockCrystallizedEssence, 9);

    // Chaos coal block
    ItemStack chaosCoal = CraftingMaterial.getStack(Names.CHAOS_COAL);
    ItemStack chaosCoalBlock = this.getStack(Names.CHAOS_COAL_BLOCK);
    RecipeHelper.addCompressionRecipe(chaosCoal, chaosCoalBlock, 9);
    RecipeHelper.addSurround(this.getStack(Names.CHAOS_COAL_BLOCK, 8), blockEssence,
        Blocks.coal_block);
  }
  
  @Override
  public IBlockState getStateFromMeta(int meta) {

    return this.getDefaultState().withProperty(VARIANT, EnumVariant.get(meta));
  }

  @Override
  public int getMetaFromState(IBlockState state) {

    return ((EnumVariant) state.getValue(VARIANT)).ordinal();
  }

  @Override
  protected BlockState createBlockState() {

    return new BlockState(this, new IProperty[] { VARIANT });
  }

  @Override
  public int getBurnTime(ItemStack stack) {

    if (stack != null && stack.getItem() == Item.getItemFromBlock(this)
        && stack.getItemDamage() == getStack(Names.CHAOS_COAL_BLOCK).getItemDamage()) {
      return Config.CHAOS_COAL_BURN_TIME * 10;
    }
    return 0;
  }

  @Override
  public EnumRarity getRarity(ItemStack stack) {

    int meta = stack.getItemDamage();
    if (meta == 1) {
      return EnumRarity.RARE;
    } else if (meta == 2) {
      return EnumRarity.EPIC;
    } else {
      return super.getRarity(stack);
    }
  }

  public static ItemStack getStack(String name) {

    for (int i = 0; i < EnumVariant.values().length; ++i) {
      if (EnumVariant.values()[i].name.equals(name)) {
        return new ItemStack(ModBlocks.miscBlock, 1, i);
      }
    }

    return null;
  }

  public static ItemStack getStack(String name, int count) {

    for (int i = 0; i < EnumVariant.values().length; ++i) {
      if (EnumVariant.values()[i].name.equals(name)) {
        return new ItemStack(ModBlocks.miscBlock, count, i);
      }
    }

    return null;
  }

  @Override
  public void getSubBlocks(Item item, CreativeTabs tab, List list) {

    for (int i = 0; i < EnumVariant.values().length; ++i) {
      list.add(new ItemStack(item, 1, i));
    }
  }
}
