package net.silentchaos512.gems.block;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IStringSerializable;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.silentchaos512.gems.item.CraftingMaterial;
import net.silentchaos512.gems.lib.Names;

public class ChaosEssenceBlock extends BlockSG {

  public static enum EnumType implements IStringSerializable {

    RAW, REGULAR, REFINED;

    @Override
    public String getName() {

      return name().toLowerCase();
    }
  }

  public static final PropertyEnum VARIANT = PropertyEnum.create("variant", EnumType.class);

  public ChaosEssenceBlock() {

    super(EnumType.values().length, Material.iron);
    setDefaultState(blockState.getBaseState().withProperty(VARIANT, EnumType.RAW));

    setHardness(4.0f);
    setResistance(6.0f);
    setStepSound(Block.soundTypeMetal);

    setHasSubtypes(true);
    setUnlocalizedName(Names.CHAOS_ESSENCE_BLOCK_OLD);
  }

  @Override
  public IBlockState getStateFromMeta(int meta) {

    if (meta >= 0 && meta < EnumType.values().length) {
      return this.getDefaultState().withProperty(VARIANT, EnumType.values()[meta]);
    }
    return getDefaultState();
  }

  @Override
  public int getMetaFromState(IBlockState state) {

    return ((EnumType) state.getValue(VARIANT)).ordinal();
  }

  @Override
  protected BlockState createBlockState() {

    return new BlockState(this, new IProperty[] { VARIANT });
  }
  
  @Override
  public String getFullName() {
    
    return super.getFullName() + "Old";
  }

  @Override
  public void addRecipes() {

    // Craft back into the old (new?) versions.
    ItemStack result = MiscBlock.getStack(Names.CHAOS_ESSENCE_BLOCK);
    GameRegistry.addShapelessRecipe(result, new ItemStack(this, 1, 0));
    GameRegistry.addShapelessRecipe(result, new ItemStack(this, 1, 1));
    result = MiscBlock.getStack(Names.CHAOS_ESSENCE_BLOCK_REFINED);
    GameRegistry.addShapelessRecipe(result, new ItemStack(this, 1, 2));
  }
}
