package net.silentchaos512.gems.block;

import java.util.List;

import com.google.common.collect.Lists;

import net.minecraft.block.BlockContainer;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemModelMesher;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.silentchaos512.gems.SilentGems;
import net.silentchaos512.gems.client.gui.GuiHandlerSilentGems;
import net.silentchaos512.gems.item.ModItems;
import net.silentchaos512.gems.lib.EnumPylonType;
import net.silentchaos512.gems.lib.GemsCreativeTabs;
import net.silentchaos512.gems.lib.Names;
import net.silentchaos512.gems.tile.TileChaosPylon;
import net.silentchaos512.lib.registry.IHasSubtypes;
import net.silentchaos512.lib.registry.IRegistryObject;

public class BlockChaosPylon extends BlockContainer
    implements ITileEntityProvider, IRegistryObject, IHasSubtypes {

  public static enum VariantType implements IStringSerializable {

    PASSIVE, BURNER;

    @Override
    public String getName() {

      return name().toLowerCase();
    }
  }

  public static final PropertyEnum VARIANT = PropertyEnum.create("variant", VariantType.class);
  public static final AxisAlignedBB BOUNDING_BOX = new AxisAlignedBB(0.25f, 0.0f, 0.25f, 0.75f,
      1.0f, 0.75f);

  public BlockChaosPylon() {

    super(Material.IRON);
    setDefaultState(blockState.getBaseState().withProperty(VARIANT, VariantType.PASSIVE));

    setLightLevel(0.25f);
    setLightOpacity(0);
    setHardness(6.0f);
    setResistance(1000.0f);
    setCreativeTab(GemsCreativeTabs.blocks);
  }

  @Override
  public void addRecipes() {

    ItemStack pylonPassive = new ItemStack(this, 1, EnumPylonType.PASSIVE.getMeta());
    ItemStack pylonBurner = new ItemStack(this, 1, EnumPylonType.BURNER.getMeta());
    ItemStack chaosCore = ModItems.craftingMaterial.chaosCore;

    GameRegistry.addRecipe(new ShapedOreRecipe(pylonPassive, "lel", "lol", "ooo", 'e', chaosCore,
        'l', "gemLapis", 'o', Blocks.OBSIDIAN));
    GameRegistry.addRecipe(new ShapedOreRecipe(pylonBurner, " e ", "rpr", "ofo", 'p', pylonPassive,
        'e', chaosCore, 'f', Blocks.FURNACE, 'r', "blockRedstone", 'o', Blocks.OBSIDIAN));
  }

  @Override
  public void addOreDict() {

  }

  @Override
  public String getName() {

    return Names.CHAOS_PYLON;
  }

  @Override
  public String getFullName() {

    return getModId() + ":" + getName();
  }

  @Override
  public String getModId() {

    return SilentGems.MODID;
  }

  @Override
  public List<ModelResourceLocation> getVariants() {

    return Lists.newArrayList();
  }

  @Override
  public boolean registerModels() {

      /**
       * Dear SilentChaos512:
       *
       * This is just to let you know that this one method caused me to nearly pull all of my hair
       * out. I spent at _least_ an hour trying to figure out why the passive pylon was not rendering
       * correctly as an item. I hate this method with the fiery passion of 100 suns.
       *
       * Sincerely,
       * M4thG33k
       */

//    ItemModelMesher mesher = Minecraft.getMinecraft().getRenderItem().getItemModelMesher();
//    Item item = Item.getItemFromBlock(this);
//    ModelResourceLocation model = new ModelResourceLocation(SilentGems.MOD_ID, getName());
//    mesher.register(item, 0, model);

    return false;//true; // Cancels normal model registration.
  }

  @Override
  public TileEntity createNewTileEntity(World worldIn, int meta) {

    TileChaosPylon toReturn = new TileChaosPylon();
    toReturn.setPylonType(EnumPylonType.getByMeta(meta));
    return toReturn;
  }

  @Override
  public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {

    EnumPylonType type = EnumPylonType.getByMeta(getMetaFromState(state));

    if (world.isRemote) {
      return type == EnumPylonType.BURNER;
    }

    if (type == EnumPylonType.BURNER) {
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
  public void getSubBlocks(Item item, CreativeTabs tab, NonNullList<ItemStack> list) {

    for (EnumPylonType type : EnumPylonType.values()) {
      if (type != EnumPylonType.NONE) {
        list.add(new ItemStack(item, 1, type.getMeta()));
      }
    }
  }

  @Override
  public IBlockState getStateFromMeta(int meta) {

    return this.getDefaultState().withProperty(VARIANT,
        VariantType.values()[MathHelper.clamp(meta, 0, VariantType.values().length)]);
  }

  @Override
  public int getMetaFromState(IBlockState state) {

    return ((VariantType) state.getValue(VARIANT)).ordinal();
  }

  @Override
  protected BlockStateContainer createBlockState() {

    return new BlockStateContainer(this, new IProperty[] { VARIANT });
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
  public void breakBlock(World world, BlockPos pos, IBlockState state) {

    TileChaosPylon tilePylon = (TileChaosPylon) world.getTileEntity(pos);

    if (tilePylon != null) {
      for (int i = 0; i < tilePylon.getSizeInventory(); ++i) {
        ItemStack stack = tilePylon.getStackInSlot(i);

        if (stack != null) {
          float f = SilentGems.instance.random.nextFloat() * 0.8F + 0.1F;
          float f1 = SilentGems.instance.random.nextFloat() * 0.8F + 0.1F;
          float f2 = SilentGems.instance.random.nextFloat() * 0.8F + 0.1F;

          while (stack.getCount() > 0) {
            int j1 = SilentGems.instance.random.nextInt(21) + 10;

            if (j1 > stack.getCount()) {
              j1 = stack.getCount();
            }

            stack.shrink(j1);
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
            world.spawnEntity(entityitem);
          }
        }
      }
    }
  }

  @Override
  public boolean isOpaqueCube(IBlockState state) {

    return false;
  }

  @Override
  public boolean isBlockNormalCube(IBlockState state) {

    return false;
  }

  @Override
  public boolean isFullCube(IBlockState state) {
    return false;
  }



  @Override
  public EnumBlockRenderType getRenderType(IBlockState state) {

    return EnumBlockRenderType.ENTITYBLOCK_ANIMATED; //I got yo' back!
  }

  @Override
  public AxisAlignedBB getCollisionBoundingBox(IBlockState worldIn, IBlockAccess pos, BlockPos state) {

    // return BOUNDING_BOX;
    return super.getCollisionBoundingBox(worldIn, pos, state);
  }

  @Override
  public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {

    return BOUNDING_BOX;
    // return super.getBoundingBox(state, source, pos);
  }

  @Override
  public boolean hasSubtypes() {

    return true;
  }
}
