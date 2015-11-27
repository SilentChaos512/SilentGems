package net.silentchaos512.gems.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.silentchaos512.gems.SilentGems;
import net.silentchaos512.gems.core.registry.IAddRecipe;
import net.silentchaos512.gems.core.registry.IHasVariants;
import net.silentchaos512.gems.gui.GuiHandlerSilentGems;
import net.silentchaos512.gems.item.CraftingMaterial;
import net.silentchaos512.gems.lib.Names;
import net.silentchaos512.gems.tile.TileChaosAltar;

public class BlockChaosAltar extends BlockContainer implements IAddRecipe, IHasVariants {

  public BlockChaosAltar() {

    super(Material.iron);
    this.setHardness(12.0f);
    this.setResistance(6000.0f);
    this.setStepSound(Block.soundTypeMetal);
    this.setCreativeTab(SilentGems.tabSilentGems);
  }

  @Override
  public TileEntity createNewTileEntity(World world, int p_149915_2_) {

    return new TileChaosAltar();
  }

  @Override
  public void addOreDict() {

  }

  @Override
  public void addRecipes() {

    ItemStack result = new ItemStack(this);
    ItemStack chaosCore = CraftingMaterial.getStack(Names.CHAOS_CORE);
    GameRegistry.addRecipe(new ShapedOreRecipe(result, " d ", "eoe", "ooo", 'e', chaosCore, 'o',
        Blocks.obsidian, 'd', "gemDiamond"));
  }

  @Override
  public String getUnlocalizedName() {

    return "tile." + Names.CHAOS_ALTAR;
  }

  @Override
  public String getName() {

    return Names.CHAOS_ALTAR;
  }

  @Override
  public String getFullName() {

    return Names.convert(SilentGems.MOD_ID + ":" + getName());
  }

  @Override
  public String[] getVariantNames() {

    return new String[] { getFullName() };
  }

  @Override
  public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player,
      EnumFacing side, float hitX, float hitY, float hitZ) {

    if (world.isRemote) {
      return true;
    }

    TileEntity tile = world.getTileEntity(pos);

    if (tile instanceof TileChaosAltar) {
      player.openGui(SilentGems.instance, GuiHandlerSilentGems.ID_ALTAR, world, pos.getX(),
          pos.getY(), pos.getZ());
    }

    return true;
  }

  @Override
  public void breakBlock(World world, BlockPos pos, IBlockState state) {

    TileChaosAltar tileAltar = (TileChaosAltar) world.getTileEntity(pos);

    if (tileAltar != null) {
      for (int i = 0; i < tileAltar.getSizeInventory(); ++i) {
        ItemStack stack = tileAltar.getStackInSlot(i);

        if (stack != null) {
          float f = SilentGems.instance.random.nextFloat() * 0.8F + 0.1F;
          float f1 = SilentGems.instance.random.nextFloat() * 0.8F + 0.1F;
          float f2 = SilentGems.instance.random.nextFloat() * 0.8F + 0.1F;

          while (stack.stackSize > 0) {
            int j1 = SilentGems.instance.random.nextInt(21) + 10;

            if (j1 > stack.stackSize) {
              j1 = stack.stackSize;
            }

            stack.stackSize -= j1;
            EntityItem entityitem = new EntityItem(world, (double) ((float) pos.getX() + f),
                (double) ((float) pos.getY() + f1), (double) ((float) pos.getZ() + f2),
                new ItemStack(stack.getItem(), j1, stack.getItemDamage()));

            if (stack.hasTagCompound()) {
              entityitem.getEntityItem()
                  .setTagCompound((NBTTagCompound) stack.getTagCompound().copy());
            }

            float f3 = 0.05F;
            entityitem.motionX = (double) (SilentGems.instance.random.nextGaussian() * f3);
            entityitem.motionY = (double) (SilentGems.instance.random.nextGaussian() * f3 + 0.2F);
            entityitem.motionZ = (double) (SilentGems.instance.random.nextGaussian() * f3);
            world.spawnEntityInWorld(entityitem);
          }
        }
      }
    }
  }

  @Override
  public boolean hasComparatorInputOverride() {

    return true;
  }

  @Override
  public int getComparatorInputOverride(World world, BlockPos pos) {

    TileEntity tile = world.getTileEntity(pos);
    if (tile != null && tile instanceof TileChaosAltar) {
      TileChaosAltar altar = (TileChaosAltar) tile;
      float storedRatio = (float) altar.getEnergyStored() / altar.getMaxEnergyStored();
      return (int) (15 * storedRatio);
    }
    return 0;
  }

  @Override
  public int getRenderType() {

    return 3;
  }

  /* ADDED BY MATHG33K */
  // @Override
  // public boolean shouldSideBeRendered(IBlockAccess world, BlockPos pos, EnumFacing side) {
  //
  // return true;
  // }

  @Override
  public boolean isOpaqueCube() {

    return false;
  }

  // the following changes the block bounds (so it's not considered a full block...you can remove this if you really
  // want)

  @Override
  public void setBlockBoundsBasedOnState(IBlockAccess world, BlockPos pos) {

    this.setBlockBounds(0.0f, 0.0f, 0.0f, 1.0f, 0.75f, 1.0f);
  }

  /* END ADDED BY MATHG33K */
}
