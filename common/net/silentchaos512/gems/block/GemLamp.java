package net.silentchaos512.gems.block;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.silentchaos512.gems.SilentGems;
import net.silentchaos512.gems.core.registry.SRegistry;
import net.silentchaos512.gems.core.util.RecipeHelper;
import net.silentchaos512.gems.lib.EnumGem;
import net.silentchaos512.gems.lib.Names;

public class GemLamp extends BlockSG {

  public static final PropertyEnum VARIANT = PropertyEnum.create("variant", EnumGem.class);

  private final boolean lit;
  private final boolean inverted;

  public GemLamp(boolean lit, boolean inverted) {

    super(EnumGem.values().length, Material.redstoneLight);
    setDefaultState(this.blockState.getBaseState().withProperty(VARIANT, EnumGem.RUBY));

    this.lit = lit;
    this.inverted = inverted;
    if (lit == inverted) {
      this.setCreativeTab(SilentGems.tabSilentGems);
    } else {
      this.setCreativeTab(null);
    }

    setHardness(0.3f);
    setResistance(3.0f);
    this.setHasGemSubtypes(true);
    this.setHasSubtypes(true);

    if (lit && inverted) {
      setLightLevel(1.0f);
      setUnlocalizedName(Names.GEM_LAMP_INV_LIT);
    } else if (lit) {
      setLightLevel(1.0f);
      setUnlocalizedName(Names.GEM_LAMP_LIT);
    } else if (inverted) {
      setUnlocalizedName(Names.GEM_LAMP_INV);
    } else {
      setUnlocalizedName(Names.GEM_LAMP);
    }
  }

  @Override
  public IBlockState getStateFromMeta(int meta) {

    return this.getDefaultState().withProperty(VARIANT, EnumGem.get(meta));
  }

  @Override
  public int getMetaFromState(IBlockState state) {

    return ((EnumGem) state.getValue(VARIANT)).id;
  }

  @Override
  protected BlockState createBlockState() {

    return new BlockState(this, new IProperty[] { VARIANT });
  }

  private void setState(World world, BlockPos pos, String name, EnumGem gem) {

    world.setBlockState(pos, SRegistry.getBlock(name).getDefaultState().withProperty(VARIANT, gem),
        2);
  }

  @Override
  public void onBlockAdded(World world, BlockPos pos, IBlockState state) {

    if (!world.isRemote) {
      boolean isPowered = world.isBlockPowered(pos);
      EnumGem gem = EnumGem.get(this.getMetaFromState(state));
      if (inverted) {
        if (!lit && !isPowered) {
          setState(world, pos, Names.GEM_LAMP_INV_LIT, gem);
        } else if (lit && isPowered) {
          setState(world, pos, Names.GEM_LAMP_INV, gem);
        }
      } else {
        if (lit && !isPowered) {
          setState(world, pos, Names.GEM_LAMP, gem);
        } else if (!lit && isPowered) {
          setState(world, pos, Names.GEM_LAMP_LIT, gem);
        }
      }
    }
  }

  @Override
  public void onNeighborBlockChange(World world, BlockPos pos, IBlockState state,
      Block neighborBlock) {

    if (!world.isRemote) {
      boolean isPowered = world.isBlockPowered(pos);
      EnumGem gem = EnumGem.get(this.getMetaFromState(state));
      if (inverted) {
        if (!lit && !isPowered) {
          world.scheduleUpdate(pos, this, 4);
        } else if (lit && isPowered) {
          setState(world, pos, Names.GEM_LAMP_INV, gem);
        }
      } else {
        if (lit && !isPowered) {
          world.scheduleUpdate(pos, this, 4);
        } else if (!lit && isPowered) {
          setState(world, pos, Names.GEM_LAMP_LIT, gem);
        }
      }
    }
  }

  @Override
  public void updateTick(World world, BlockPos pos, IBlockState state, Random random) {

    if (!world.isRemote) {
      boolean isPowered = world.isBlockPowered(pos);
      EnumGem gem = EnumGem.get(this.getMetaFromState(state));
      if (inverted) {
        if (!lit && !isPowered) {
          setState(world, pos, Names.GEM_LAMP_INV_LIT, gem);
        }
      } else {
        if (lit && !isPowered) {
          setState(world, pos, Names.GEM_LAMP, gem);
        }
      }
    }
  }

  @Override
  protected boolean canSilkHarvest() {

    return false;
  }

  @Override
  public Item getItemDropped(IBlockState state, Random random, int fortune) {

    if (inverted) {
      return Item.getItemFromBlock(ModBlocks.gemLampInvertedLit);
    } else {
      return Item.getItemFromBlock(ModBlocks.gemLamp);
    }
  }

  @Override
  public Item getItem(World world, BlockPos pos) {

    if (inverted) {
      return Item.getItemFromBlock(ModBlocks.gemLampInvertedLit);
    } else {
      return Item.getItemFromBlock(ModBlocks.gemLamp);
    }
  }

  @Override
  protected ItemStack createStackedBlock(IBlockState state) {

    if (inverted) {
      return new ItemStack(ModBlocks.gemLampInvertedLit);
    } else {
      return new ItemStack(ModBlocks.gemLamp);
    }
  }

  @Override
  public void addRecipes() {

    if (!lit && !inverted) {
      for (EnumGem gem : EnumGem.values()) {
        RecipeHelper.addSurroundOre(new ItemStack(this, 1, gem.id), gem.getItemOreName(),
            "dustRedstone", "dustGlowstone");
      }
    } else if (lit && inverted) {
      for (EnumGem gem : EnumGem.values()) {
        GameRegistry.addShapelessRecipe(new ItemStack(this, 1, gem.id),
            new ItemStack(ModBlocks.gemLamp, 1, gem.id), Blocks.redstone_torch);
      }
    }
  }
}
