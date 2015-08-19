package net.silentchaos512.gems.block;

import java.util.List;

import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.silentchaos512.gems.SilentGems;
import net.silentchaos512.gems.core.registry.IAddRecipe;
import net.silentchaos512.gems.core.registry.IHasSubtypes;
import net.silentchaos512.gems.core.util.LogHelper;
import net.silentchaos512.gems.gui.GuiHandlerSilentGems;
import net.silentchaos512.gems.item.CraftingMaterial;
import net.silentchaos512.gems.lib.Names;
import net.silentchaos512.gems.lib.Strings;
import net.silentchaos512.gems.tile.TileChaosAltar;
import net.silentchaos512.gems.tile.TileChaosPylon;

public class BlockChaosPylon extends BlockContainer
    implements ITileEntityProvider, IAddRecipe, IHasSubtypes {

  public static enum Type {
    PASSIVE, BURNER;
  }

  private IIcon[] iconTop = new IIcon[Type.values().length];
  private IIcon[] iconBottom = new IIcon[Type.values().length];
  private IIcon[] iconSide = new IIcon[Type.values().length];
  private IIcon[] iconSideA = new IIcon[Type.values().length];
  private IIcon[] iconSideB = new IIcon[Type.values().length];
  private IIcon[] iconSideAB = new IIcon[Type.values().length];

  public BlockChaosPylon() {

    super(Material.iron);
    this.setHardness(7.0f);
    this.setResistance(1000.0f);
    this.setCreativeTab(SilentGems.tabSilentGems);
  }

  public Type getType(int meta) {

    if (meta >= 0 && meta < Type.values().length) {
      return Type.values()[meta];
    }
    return null;
  }

  @Override
  public void addRecipes() {

    ItemStack passivePylon = new ItemStack(this, 1, 0);
    ItemStack burnerPylon = new ItemStack(this, 1, 1);
    ItemStack refinedEssenceBlock = MiscBlock.getStack(Names.CHAOS_ESSENCE_BLOCK_REFINED);
    ItemStack refinedEssence = CraftingMaterial.getStack(Names.CHAOS_ESSENCE_PLUS);

    GameRegistry.addRecipe(new ShapedOreRecipe(passivePylon, "lel", "lol", " o ", 'l', "gemLapis",
        'e', refinedEssenceBlock, 'o', Blocks.obsidian));
    GameRegistry.addRecipe(new ShapedOreRecipe(burnerPylon, " p ", "eee", " f ", 'p', passivePylon, 'e',
        refinedEssence, 'f', Blocks.furnace));
  }

  @Override
  public void addOreDict() {

  }

  @Override
  public TileEntity createNewTileEntity(World p_149915_1_, int p_149915_2_) {

    return new TileChaosPylon();
  }

  @Override
  public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side,
      float hitX, float hitY, float hitZ) {

    Type type = this.getType(world.getBlockMetadata(x, y, z));

    if (world.isRemote) {
      return type == Type.BURNER;
    }

    if (type == Type.BURNER) {
      TileEntity tile = world.getTileEntity(x, y, z);
      if (tile instanceof TileChaosPylon) {
        player.openGui(SilentGems.instance, GuiHandlerSilentGems.ID_BURNER_PYLON, world, x, y, z);
      }
      return true;
    }

    return false;
  }

  @Override
  public void getSubBlocks(Item item, CreativeTabs tab, List list) {

    for (int i = 0; i < Type.values().length; ++i) {
      list.add(new ItemStack(item, 1, i));
    }
  }

  @Override
  public void registerBlockIcons(IIconRegister reg) {

    for (int i = 0; i < Type.values().length; ++i) {
      String prefix = Strings.RESOURCE_PREFIX + Names.CHAOS_PYLON + i + "_";
      iconTop[i] = reg.registerIcon(prefix + "Top");
      iconBottom[i] = reg.registerIcon(prefix + "Bottom");
      iconSide[i] = reg.registerIcon(prefix + "Side");
      iconSideA[i] = reg.registerIcon(prefix + "SideA");
      iconSideB[i] = reg.registerIcon(prefix + "SideB");
      iconSideAB[i] = reg.registerIcon(prefix + "SideAB");
    }
  }

  @Override
  public IIcon getIcon(int side, int meta) {

    switch (side) {
      case 0:
        return iconBottom[meta];
      case 1:
        return iconTop[meta];
      default:
        return iconSide[meta];
    }
  }

  @Override
  public int damageDropped(int meta) {

    return meta & 3;
  }

  @Override
  public String getUnlocalizedName() {

    return "tile." + Names.CHAOS_PYLON;
  }

  @Override
  public boolean getHasSubtypes() {

    return true;
  }

  @Override
  public void breakBlock(World world, int x, int y, int z, Block block, int par6) {

    TileChaosPylon tilePylon = (TileChaosPylon) world.getTileEntity(x, y, z);

    if (tilePylon != null) {
      for (int i = 0; i < tilePylon.getSizeInventory(); ++i) {
        ItemStack stack = tilePylon.getStackInSlot(i);

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
