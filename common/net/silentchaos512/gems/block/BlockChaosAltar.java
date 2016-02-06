package net.silentchaos512.gems.block;

import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.silentchaos512.gems.SilentGems;
import net.silentchaos512.gems.client.gui.GuiHandlerSilentGems;
import net.silentchaos512.gems.core.registry.IAddRecipe;
import net.silentchaos512.gems.core.util.LogHelper;
import net.silentchaos512.gems.item.CraftingMaterial;
import net.silentchaos512.gems.lib.Names;
import net.silentchaos512.gems.lib.Strings;
import net.silentchaos512.gems.tile.TileChaosAltar;

public class BlockChaosAltar extends BlockContainer implements IAddRecipe {

  private IIcon iconTop;
  private IIcon iconSide;
  private IIcon iconBottom;

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
  public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side,
      float hitX, float hitY, float hitZ) {

    if (world.isRemote) {
      return true;
    }

    TileEntity tile = world.getTileEntity(x, y, z);

    if (tile instanceof TileChaosAltar) {
      player.openGui(SilentGems.instance, GuiHandlerSilentGems.ID_ALTAR, world, x, y, z);
    }

    return true;
  }

  @Override
  public void breakBlock(World world, int x, int y, int z, Block block, int par6) {

    TileChaosAltar tileAltar = (TileChaosAltar) world.getTileEntity(x, y, z);

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
            EntityItem entityitem = new EntityItem(world, (double) ((float) x + f),
                (double) ((float) y + f1), (double) ((float) z + f2),
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
  public int getComparatorInputOverride(World world, int x, int y, int z, int par5) {

    TileEntity tile = world.getTileEntity(x, y, z);
    if (tile != null && tile instanceof TileChaosAltar) {
      TileChaosAltar altar = (TileChaosAltar) tile;
      float storedRatio = (float) altar.getEnergyStored() / altar.getMaxEnergyStored();
      return (int) (15 * storedRatio);
    }
    return 0;
  }

  /* ADDED BY MATHG33K */
  @Override
  public boolean shouldSideBeRendered(IBlockAccess world, int x, int y, int z, int side) {

    return false;
  }

  @Override
  public boolean isOpaqueCube() {

    return false;
  }

  @Override
  public boolean renderAsNormalBlock() {

    return false;
  }

  // the following changes the block bounds (so it's not considered a full block...you can remove this if you really
  // want)

  @Override
  public void setBlockBoundsBasedOnState(IBlockAccess world, int x, int y, int z) {

    this.setBlockBounds(0.0f, 0.0f, 0.0f, 1.0f, 0.75f, 1.0f);
  }

  // re-added these two methods in order to have the "block breaking" particles look similar to the actual block...
  @Override
  public void registerBlockIcons(IIconRegister reg) {

    iconTop = reg.registerIcon(Blocks.obsidian.getIcon(0, 0).getIconName());
  }

  @Override
  public IIcon getIcon(int side, int meta) {

    return iconTop;
  }

  /* END ADDED BY MATHG33K */
}
