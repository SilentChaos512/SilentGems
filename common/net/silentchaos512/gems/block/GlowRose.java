package net.silentchaos512.gems.block;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.EnumPlantType;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.common.util.ForgeDirection;
import net.silentchaos512.gems.configuration.Config;
import net.silentchaos512.gems.core.registry.SRegistry;
import net.silentchaos512.gems.core.util.RecipeHelper;
import net.silentchaos512.gems.lib.EnumGem;
import net.silentchaos512.gems.lib.Names;
import cofh.api.modhelpers.ThermalExpansionHelper;
import cpw.mods.fml.common.registry.GameRegistry;

public class GlowRose extends BlockSG implements IPlantable {

  public GlowRose() {

    super(Material.plants);
    this.setHardness(0.0f);
    this.setStepSound(Block.soundTypeGrass);
    this.setTickRandomly(true);
    float f = 0.2F;
    this.setBlockBounds(0.5F - f, 0.0F, 0.5F - f, 0.5F + f, f * 3.0F, 0.5F + f);
    this.lightValue = Config.GLOW_ROSE_LIGHT_LEVEL;
    setHasGemSubtypes(true);
    setHasSubtypes(true);
    setUnlocalizedName(Names.GLOW_ROSE);
  }

  @Override
  public void addRecipes() {

    Item dye = Items.dye;
    Item dyeSG = SRegistry.getItem(Names.DYE);

    // Flowers to dye.
    // 0=black
    addFlowerToDyeRecipe(new ItemStack(dyeSG, 1, 0), new ItemStack(this, 1, EnumGem.ONYX.id));
    // 1=red
    addFlowerToDyeRecipe(new ItemStack(dye, 1, 1), new ItemStack(this, 1, EnumGem.RUBY.id));
    addFlowerToDyeRecipe(new ItemStack(dye, 1, 1), new ItemStack(this, 1, EnumGem.GARNET.id));
    // 2=green
    addFlowerToDyeRecipe(new ItemStack(dye, 1, 2), new ItemStack(this, 1, EnumGem.EMERALD.id));
    // 3=brown
    // 4=blue
    addFlowerToDyeRecipe(new ItemStack(dyeSG, 1, 4), new ItemStack(this, 1, EnumGem.SAPPHIRE.id));
    addFlowerToDyeRecipe(new ItemStack(dyeSG, 1, 4), new ItemStack(this, 1, EnumGem.IOLITE.id));
    // 5=purple
    addFlowerToDyeRecipe(new ItemStack(dye, 1, 5), new ItemStack(this, 1, EnumGem.AMETHYST.id));
    // 6=cyan
    // 7=light gray
    // 8=gray
    // 9=pink
    addFlowerToDyeRecipe(new ItemStack(dye, 1, 9), new ItemStack(this, 1, EnumGem.MORGANITE.id));
    // 10=lime
    addFlowerToDyeRecipe(new ItemStack(dye, 1, 10), new ItemStack(this, 1, EnumGem.PERIDOT.id));
    // 11=yellow
    addFlowerToDyeRecipe(new ItemStack(dye, 1, 11), new ItemStack(this, 1, EnumGem.HELIODOR.id));
    // 12=light blue
    addFlowerToDyeRecipe(new ItemStack(dye, 1, 12), new ItemStack(this, 1, EnumGem.AQUAMARINE.id));
    // 13-magenta
    // 14-orange
    addFlowerToDyeRecipe(new ItemStack(dye, 1, 14), new ItemStack(this, 1, EnumGem.TOPAZ.id));
  }

  private void addFlowerToDyeRecipe(ItemStack dye, ItemStack flower) {

    GameRegistry.addShapelessRecipe(new ItemStack(dye.getItem(), 2, dye.getItemDamage()), flower);
    // Pulverizer
    ThermalExpansionHelper.addPulverizerRecipe(1600, flower,
        new ItemStack(dye.getItem(), 4, dye.getItemDamage()));
  }

  @Override
  public boolean canBlockStay(World world, int x, int y, int z) {

    return world.getBlock(x, y - 1, z).canSustainPlant(world, x, y - 1, z, ForgeDirection.UP, this);
  }

  @Override
  public boolean canPlaceBlockAt(World world, int x, int y, int z) {

    return super.canPlaceBlockAt(world, x, y, z) && this.canBlockStay(world, x, y, z);
  }

  protected boolean canPlaceBlockOn(Block block) {

    return block == Blocks.grass || block == Blocks.dirt || block == Blocks.farmland;
  }

  protected void checkAndDropBlock(World world, int x, int y, int z) {

    if (!this.canBlockStay(world, x, y, z)) {
      this.dropBlockAsItem(world, x, y, z, world.getBlockMetadata(x, y, z), 0);
      world.setBlock(x, y, z, getBlockById(0), 0, 2);
    }
  }

  @Override
  public AxisAlignedBB getCollisionBoundingBoxFromPool(World world, int x, int y, int z) {

    return null;
  }

  @Override
  public Block getPlant(IBlockAccess world, int x, int y, int z) {

    return this;
  }

  @Override
  public int getPlantMetadata(IBlockAccess world, int x, int y, int z) {

    return world.getBlockMetadata(x, y, z);
  }

  @Override
  public EnumPlantType getPlantType(IBlockAccess world, int x, int y, int z) {

    return EnumPlantType.Plains;
  }

  @Override
  public int getRenderType() {

    return 1;
  }

  @Override
  public boolean isOpaqueCube() {

    return false;
  }

  @Override
  public void onNeighborBlockChange(World world, int x, int y, int z, Block block) {

    super.onNeighborBlockChange(world, x, y, z, block);
    this.checkAndDropBlock(world, x, y, z);
  }

  @Override
  public boolean renderAsNormalBlock() {

    return false;
  }

  @Override
  public void updateTick(World world, int x, int y, int z, Random random) {

    this.checkAndDropBlock(world, x, y, z);
  }
}
