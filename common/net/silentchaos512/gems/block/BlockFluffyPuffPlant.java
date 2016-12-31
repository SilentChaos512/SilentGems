package net.silentchaos512.gems.block;

import java.util.List;
import java.util.Random;

import com.google.common.collect.Lists;

import net.minecraft.block.BlockCrops;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.EnumPlantType;
import net.silentchaos512.gems.SilentGems;
import net.silentchaos512.gems.item.ModItems;
import net.silentchaos512.gems.lib.Names;
import net.silentchaos512.lib.registry.IRegistryObject;

public class BlockFluffyPuffPlant extends BlockCrops implements IRegistryObject {

  public BlockFluffyPuffPlant() {

    setHardness(0.1f);
    setUnlocalizedName(Names.FLUFFY_PUFF_PLANT);
  }

  @Override
  public List<ItemStack> getDrops(IBlockAccess world, BlockPos pos, IBlockState state, int fortune) {

    List<ItemStack> list = Lists.newArrayList(new ItemStack(getSeed()));

    int age = getAge(state);
    Random rand = SilentGems.instance.random;

    if (age >= 7) {
      // Seeds
      for (int i = 0; i < 1 + fortune; ++i) {
        if (rand.nextInt(15) <= age) {
          list.add(new ItemStack(getSeed()));
        }
      }
      // Puffs
      for (int i = 0; i < 2 + fortune + rand.nextInt(3); ++i) {
        list.add(new ItemStack(getCrop()));
      }
    }

    return list;
  }

  @Override
  public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {

    ItemStack heldItem = player.getHeldItem(hand);

    if (heldItem.getItem() == ModItems.sickle) {
      return false;
    }

    // Right-click to harvest
    List<ItemStack> drops = Lists.newArrayList();
    int age = state.getValue(AGE);

    // Get drops if mature
    if (age >= 7) {
      for (int i = 0; i < 3; ++i) {
        if (i == 0 || RANDOM.nextInt(15) <= age) {
          drops.add(new ItemStack(getCrop(), 1, damageDropped(state)));
        }
      }

      // Reset to newly planted state
      world.setBlockState(pos, getDefaultState());
    }

    // Spawn items in world
    for (ItemStack stack : drops) {
      spawnAsEntity(world, pos, stack);
    }

    return !drops.isEmpty();
  }

  @Override
  protected Item getSeed() {

    return ModItems.fluffyPuffSeeds;
  }

  @Override
  protected Item getCrop() {

    return ModItems.fluffyPuff;
  }

  @Override
  public EnumPlantType getPlantType(IBlockAccess world, BlockPos pos) {

    return EnumPlantType.Crop;
  }

  @Override
  public String getUnlocalizedName() {

    return "tile." + Names.FLUFFY_PUFF_PLANT;
  }

  @Override
  public void addRecipes() {

  }

  @Override
  public void addOreDict() {

  }

  @Override
  public String getName() {

    return Names.FLUFFY_PUFF_PLANT;
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

    List<ModelResourceLocation> list = Lists.newArrayList();
    for (int i = 0; i < 4; ++i) {
      list.add(new ModelResourceLocation(getFullName() + i, "inventory"));
    }
    return list;
  }

  @Override
  public boolean registerModels() {

    return false;
  }
}
