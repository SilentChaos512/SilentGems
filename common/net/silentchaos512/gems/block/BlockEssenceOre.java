package net.silentchaos512.gems.block;

import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.OreDictionary;
import net.silentchaos512.gems.SilentGems;
import net.silentchaos512.gems.item.ModItems;
import net.silentchaos512.gems.lib.Names;
import net.silentchaos512.lib.block.BlockSL;

public class BlockEssenceOre extends BlockSL {

  public static enum Type implements IStringSerializable {

    CHAOS, ENDER;

    @Override
    public String getName() {

      return name().toLowerCase();
    }
  }

  public static final PropertyEnum VARIANT = PropertyEnum.create("variant", Type.class);

  public BlockEssenceOre() {

    super(2, SilentGems.MOD_ID, Names.ESSENCE_ORE, Material.rock);
    setDefaultState(blockState.getBaseState().withProperty(VARIANT, Type.CHAOS));

    setHardness(4.0f);
    setResistance(15.0f);
    setStepSound(SoundType.STONE);
    setHarvestLevel("pickaxe", 3, getDefaultState().withProperty(VARIANT, Type.CHAOS));
    setHarvestLevel("pickaxe", 4, getDefaultState().withProperty(VARIANT, Type.ENDER));

    setUnlocalizedName(Names.ESSENCE_ORE);
  }

  public void addRecipes() {

    ItemStack chaosOre = new ItemStack(this, 1, 0);
    ItemStack chaosEssence = ModItems.craftingMaterial.getStack(Names.CHAOS_ESSENCE);
    GameRegistry.addSmelting(chaosOre, chaosEssence, 0.7f);

    ItemStack enderOre = new ItemStack(this, 1, 1);
    ItemStack enderEssence = ModItems.craftingMaterial.getStack(Names.ENDER_ESSENCE);
    GameRegistry.addSmelting(enderOre, enderEssence, 1.0f);
  }

  @Override
  public void addOreDict() {

    OreDictionary.registerOre("oreChaos", new ItemStack(this, 1, 0));
    // TODO
  }

  @Override
  public IBlockState getStateFromMeta(int meta) {

    return this.getDefaultState().withProperty(VARIANT,
        Type.values()[MathHelper.clamp_int(meta, 0, Type.values().length - 1)]);
  }

  @Override
  public int getMetaFromState(IBlockState state) {

    return ((Type) state.getValue(VARIANT)).ordinal();
  }

  @Override
  protected BlockStateContainer createBlockState() {

    return new BlockStateContainer(this, new IProperty[] { VARIANT });
  }
}
