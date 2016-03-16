package net.silentchaos512.gems.block;

import com.google.common.collect.Lists;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.silentchaos512.gems.SilentGems;
import net.silentchaos512.gems.core.registry.IAddRecipe;
import net.silentchaos512.gems.core.registry.IHasSubtypes;
import net.silentchaos512.gems.core.registry.IHasVariants;
import net.silentchaos512.gems.gui.GuiHandlerSilentGems;
import net.silentchaos512.gems.item.CraftingMaterial;
import net.silentchaos512.gems.lib.Names;
import net.silentchaos512.gems.tile.TileChaosPylon;

import java.util.List;

public class BlockChaosPylon extends BlockContainer
    implements ITileEntityProvider, IAddRecipe, IHasSubtypes, IHasVariants {

  public static enum Type implements IStringSerializable {

    PASSIVE("Passive"), BURNER("Burner");

    private final String name;

    private Type(String name) {

      this.name = name;
    }

    @Override
    public String getName() {

      return name;
    }

    public static Type get(int meta) {

      return values()[MathHelper.clamp_int(meta, 0, values().length - 1)];
    }
  }

  public static final PropertyEnum VARIANT = PropertyEnum.create("variant", Type.class);

  public BlockChaosPylon() {

    super(Material.iron);
    setDefaultState(blockState.getBaseState().withProperty(VARIANT, Type.PASSIVE));

    setHardness(6.0f);
    setResistance(1000.0f);
    setCreativeTab(SilentGems.tabSilentGems);
  }

  public Type getType(IBlockState state) {

    int meta = getMetaFromState(state);
    if (meta >= 0 && meta < Type.values().length) {
      return Type.values()[meta];
    }
    return null;
  }

  @Override
  public void addRecipes() {

    ItemStack passivePylon = new ItemStack(this, 1, 0);
    ItemStack burnerPylon = new ItemStack(this, 1, 1);
    ItemStack chaosCore = CraftingMaterial.getStack(Names.CHAOS_CORE);

    GameRegistry.addRecipe(new ShapedOreRecipe(passivePylon, "lel", "lol", "ooo", 'l', "gemLapis",
        'e', chaosCore, 'o', Blocks.obsidian));
    GameRegistry.addRecipe(new ShapedOreRecipe(burnerPylon, " p ", "rer", "ofo", 'p', passivePylon,
        'e', chaosCore, 'f', Blocks.furnace, 'r', "dustRedstone", 'o', Blocks.obsidian));
  }

  @Override
  public void addOreDict() {

  }

  @Override
  public TileEntity createNewTileEntity(World world, int meta) {

    TileChaosPylon toReturn = new TileChaosPylon();
    toReturn.setPylonTypeInteger(meta); // use the metadata of the block to set the pylon type
    return toReturn;
  }

  @Override
  public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player,
      EnumFacing side, float hitX, float hitY, float hitZ) {

    Type type = getType(state);

    if (world.isRemote) {
      return type == Type.BURNER;
    }

    if (type == Type.BURNER) {
      TileEntity tile = world.getTileEntity(pos);
      if (tile instanceof TileChaosPylon) {
        player.openGui(SilentGems.instance, GuiHandlerSilentGems.ID_BURNER_PYLON, world, pos.getX(),
            pos.getY(), pos.getZ());
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
  public IBlockState getStateFromMeta(int meta) {

    return this.getDefaultState().withProperty(VARIANT, Type.get(meta));
  }

  @Override
  public int getMetaFromState(IBlockState state) {

    return ((Type) state.getValue(VARIANT)).ordinal();
  }

  @Override
  protected BlockState createBlockState() {

    return new BlockState(this, new IProperty[] { VARIANT });
  }

  @Override
  public int damageDropped(IBlockState state) {

    return getMetaFromState(state);
  }

  @Override
  public String getUnlocalizedName() {

    return "tile." + Names.CHAOS_PYLON;
  }

  @Override
  public boolean hasSubtypes() {

    return true;
  }

  @Override
  public boolean hasGemSubtypes() {

    return false;
  }

  @Override
  public void breakBlock(World world, BlockPos pos, IBlockState state) {

    TileChaosPylon tilePylon = (TileChaosPylon) world.getTileEntity(pos);

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
            EntityItem entityitem = new EntityItem(world, (double) ((float) pos.getX() + f),
                (double) ((float) pos.getY() + f1), (double) ((float) pos.getZ() + f2),
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

  @Override
  public boolean shouldSideBeRendered(IBlockAccess world, BlockPos pos, EnumFacing side) {

    return false;
  }

  @Override
  public boolean isOpaqueCube() {

    return false;
  }

  @Override
  public boolean isFullBlock() {
    return false;
  }

  @Override
  public boolean isVisuallyOpaque() {
    return false;
  }

  @Override
  public boolean isNormalCube() {
    return false;
  }

  @Override
  public boolean isBlockSolid(IBlockAccess worldIn, BlockPos pos, EnumFacing side) {
    return false;
  }

  @Override
  public boolean isBlockNormalCube() {
    return false;
  }

  @Override
  public int getRenderType() {

    return 2;//3;
  }



  @Override
  public void setBlockBoundsBasedOnState(IBlockAccess world, BlockPos pos) {

    this.setBlockBounds(0.25f, 0.0f, 0.25f, 0.75f, 1.0f, 0.75f); //tightened the bounds a bit to make it look less "empty"
  }

  @Override
  public String[] getVariantNames() {

    List<String> list = Lists.newArrayList();
    for (int i = 0; i < Type.values().length; ++i) {
      list.add(getFullName() + i);
    }
    return list.toArray(new String[list.size()]);
  }

  @Override
  public String getName() {

    return Names.CHAOS_PYLON;
  }

  @Override
  public String getFullName() {

    return Names.convert(SilentGems.MOD_ID + ":" + getName());
  }
}
