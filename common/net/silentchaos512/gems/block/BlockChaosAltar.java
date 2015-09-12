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
import net.minecraft.world.World;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.silentchaos512.gems.SilentGems;
import net.silentchaos512.gems.core.registry.IAddRecipe;
import net.silentchaos512.gems.gui.GuiHandlerSilentGems;
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
    this.setHardness(15.0f);
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
    GameRegistry.addRecipe(new ShapedOreRecipe(result, " d ", "eoe", "ooo", 'e',
        chaosCore, 'o', Blocks.obsidian, 'd', "gemDiamond"));
  }

  @Override
  public String getUnlocalizedName() {

    return "tile." + Names.CHAOS_ALTAR;
  }

  @Override
  public void registerBlockIcons(IIconRegister reg) {

    String prefix = Strings.RESOURCE_PREFIX + Names.CHAOS_ALTAR + "_";
    iconTop = reg.registerIcon(prefix + "Top");
    iconSide = reg.registerIcon(prefix + "Side");
    iconBottom = reg.registerIcon(prefix + "Bottom");
  }

  @Override
  public IIcon getIcon(int side, int meta) {

    switch (side) {
      case 0:
        return iconBottom;
      case 1:
        return iconTop;
      default:
        return iconSide;
    }
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
            entityitem.motionX = (double) ((float) SilentGems.instance.random.nextGaussian() * f3);
            entityitem.motionY = (double) ((float) SilentGems.instance.random.nextGaussian() * f3
                + 0.2F);
            entityitem.motionZ = (double) ((float) SilentGems.instance.random.nextGaussian() * f3);
            world.spawnEntityInWorld(entityitem);
          }
        }
      }
    }
  }
}
