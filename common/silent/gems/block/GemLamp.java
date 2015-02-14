package silent.gems.block;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;
import silent.gems.SilentGems;
import silent.gems.core.registry.SRegistry;
import silent.gems.core.util.RecipeHelper;
import silent.gems.lib.EnumGem;
import silent.gems.lib.Names;
import silent.gems.lib.Strings;

public class GemLamp extends BlockSG {
  
  public static final PropertyEnum VARIANT = PropertyEnum.create("variant", EnumGem.class);

  private final boolean lit;
  private final boolean inverted;

  public GemLamp(boolean lit, boolean inverted) {

    super(EnumGem.all().length, Material.redstoneLight);
    setDefaultState(this.blockState.getBaseState().withProperty(VARIANT, EnumGem.RUBY));
    
    // Lit/inverted
    this.lit = lit;
    this.inverted = inverted;
    if (this.lit == this.inverted) {
      this.setCreativeTab(SilentGems.tabSilentGems);
    } else {
      this.setCreativeTab(null);
    }
    
    this.setHardness(0.3f);
    
    this.setHasGemSubtypes(true);
    this.setHasSubtypes(true);

    // Unlocalized name
    if (lit && inverted) {
      this.setLightLevel(1.0f);
      this.setUnlocalizedName(Names.GEM_LAMP_INV_LIT);
    } else if (lit) {
      this.setLightLevel(1.0f);
      this.setUnlocalizedName(Names.GEM_LAMP_LIT);
    } else if (inverted) {
      this.setUnlocalizedName(Names.GEM_LAMP_INV);
    } else {
      this.setUnlocalizedName(Names.GEM_LAMP);
    }
  }
  
  @Override
  public IBlockState getStateFromMeta(int meta) {
    
    return this.getDefaultState().withProperty(VARIANT, EnumGem.byId(meta));
  }
  
  @Override
  public int getMetaFromState(IBlockState state) {
    
    return ((EnumGem) state.getValue(VARIANT)).getId();
  }
  
  @Override
  protected BlockState createBlockState() {
    
    return new BlockState(this, new IProperty[] { VARIANT });
  }
  
  private void setState(World world, BlockPos pos, String name, EnumGem gem) {
    
    world.setBlockState(pos, SRegistry.getBlock(name).getDefaultState().withProperty(VARIANT, gem), 2);
  }

  @Override
  public void onBlockAdded(World world, BlockPos pos, IBlockState state) {

    if (!world.isRemote) {
      boolean isPowered = world.isBlockPowered(pos);
      EnumGem gem = EnumGem.byId(this.getMetaFromState(state));
      if (this.inverted) {
        if (!this.lit && !isPowered) {
          setState(world, pos, Names.GEM_LAMP_INV_LIT, gem);
        } else if (this.lit && isPowered) {
          setState(world, pos, Names.GEM_LAMP_INV, gem);
        }
      } else {
        if (this.lit && !isPowered) {
          setState(world, pos, Names.GEM_LAMP, gem);
        } else if (!this.lit && isPowered) {
          setState(world, pos, Names.GEM_LAMP_LIT, gem);
        }
      }
    }
  }

  @Override
  public void onNeighborBlockChange(World world, BlockPos pos, IBlockState state, Block neighborBlock) {

    if (!world.isRemote) {
      boolean isPowered = world.isBlockPowered(pos);
      EnumGem gem = EnumGem.byId(this.getMetaFromState(state));
      if (this.inverted) {
        if (!this.lit && !isPowered) {
          world.scheduleUpdate(pos, this, 4);
        } else if (this.lit && isPowered) {
          setState(world, pos, Names.GEM_LAMP_INV, gem);
        }
      } else {
        if (this.lit && !isPowered) {
          world.scheduleUpdate(pos, this, 4);
        } else if (!this.lit && isPowered) {
          setState(world, pos, Names.GEM_LAMP_LIT, gem);
        }
      }
    }
  }

  @Override
  public void updateTick(World world, BlockPos pos, IBlockState state, Random random) {

    if (!world.isRemote) {
      boolean isPowered = world.isBlockPowered(pos);
      EnumGem gem = EnumGem.byId(this.getMetaFromState(state));
      if (inverted) {
        if (!this.lit && !isPowered) {
          setState(world, pos, Names.GEM_LAMP_INV_LIT, gem);
        }
      } else {
        if (this.lit && !isPowered) {
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
  public Item getItemDropped(IBlockState state, Random rand, int fortune) {

    if (this.inverted) {
      return Item.getItemFromBlock(SRegistry.getBlock(Names.GEM_LAMP_INV_LIT));
    } else {
      return Item.getItemFromBlock(SRegistry.getBlock(Names.GEM_LAMP));
    }
  }

  @Override
  public Item getItem(World world, BlockPos pos) {

    if (this.inverted) {
      return Item.getItemFromBlock(SRegistry.getBlock(Names.GEM_LAMP_INV_LIT));
    } else {
      return Item.getItemFromBlock(SRegistry.getBlock(Names.GEM_LAMP));
    }
  }

  @Override
  protected ItemStack createStackedBlock(IBlockState state) {

    if (this.inverted) {
      return new ItemStack(SRegistry.getBlock(Names.GEM_LAMP_INV_LIT));
    } else {
      return new ItemStack(SRegistry.getBlock(Names.GEM_LAMP));
    }
  }

  @Override
  public void addRecipes() {

    if (!lit && !inverted) {
      for (int i = 0; i < EnumGem.all().length; ++i) {
        RecipeHelper.addSurround(new ItemStack(this, 1, i), EnumGem.all()[i].getItem(),
            new Object[] { Items.redstone, Items.glowstone_dust });
      }
    } else if (lit && inverted) {
      for (int i = 0; i < EnumGem.all().length; ++i) {
        GameRegistry.addShapelessRecipe(new ItemStack(this, 1, i),
            new ItemStack(SRegistry.getBlock(Names.GEM_LAMP), 1, i), Blocks.redstone_torch);
      }
    }
  }

//  @Override
//  public void registerBlockIcons(IIconRegister reg) {
//
//    if (icons == null || icons.length != EnumGem.all().length) {
//      icons = new IIcon[EnumGem.all().length];
//    }
//
//    for (int i = 0; i < EnumGem.all().length; ++i) {
//      icons[i] = reg.registerIcon(Strings.RESOURCE_PREFIX
//          + (this.lit ? Names.GEM_LAMP_LIT : Names.GEM_LAMP) + i);
//    }
//  }
}
