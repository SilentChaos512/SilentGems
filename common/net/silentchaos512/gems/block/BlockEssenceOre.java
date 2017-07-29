package net.silentchaos512.gems.block;

import java.util.List;
import java.util.Map;

import com.google.common.collect.Lists;

import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.OreDictionary;
import net.silentchaos512.gems.SilentGems;
import net.silentchaos512.gems.init.ModItems;
import net.silentchaos512.gems.lib.Names;
import net.silentchaos512.gems.util.ModRecipeHelper;
import net.silentchaos512.lib.block.BlockSL;
import net.silentchaos512.lib.registry.RecipeMaker;

public class BlockEssenceOre extends BlockSL {

  public static enum Type implements IStringSerializable {

    CHAOS, ENDER;

    @Override
    public String getName() {

      return name().toLowerCase();
    }
  }

  public static final PropertyEnum VARIANT = PropertyEnum.create("gem", Type.class);

  public BlockEssenceOre() {

    super(2, SilentGems.MODID, Names.ESSENCE_ORE, Material.ROCK);
    setDefaultState(blockState.getBaseState().withProperty(VARIANT, Type.CHAOS));

    setHardness(4.0f);
    setResistance(15.0f);
    setSoundType(SoundType.STONE);
    setHarvestLevel("pickaxe", 3, getDefaultState().withProperty(VARIANT, Type.CHAOS));
    setHarvestLevel("pickaxe", 4, getDefaultState().withProperty(VARIANT, Type.ENDER));

    setUnlocalizedName(Names.ESSENCE_ORE);
  }

  @Override
  public void addRecipes(RecipeMaker recipes) {

    ItemStack chaosOre = new ItemStack(this, 1, 0);
    ItemStack chaosEssence = ModItems.craftingMaterial.chaosEssence;
    GameRegistry.addSmelting(chaosOre, chaosEssence, 0.7f);
    ModRecipeHelper.addSagMillRecipe("ChaosOre", chaosOre, chaosEssence, "cobblestone", 4000);

    ItemStack enderOre = new ItemStack(this, 1, 1);
    ItemStack enderEssence = ModItems.craftingMaterial.enderEssence;
    GameRegistry.addSmelting(enderOre, enderEssence, 1.0f);
    ModRecipeHelper.addSagMillRecipe("EnderEssenceOre", enderOre, enderEssence, "end_stone", 4000);
  }

  @Override
  public void addOreDict() {

    OreDictionary.registerOre("oreChaos", new ItemStack(this, 1, 0));
    OreDictionary.registerOre("oreEnderEssence", new ItemStack(this, 1, 1));
  }

  @Override
  public IBlockState getStateFromMeta(int meta) {

    return this.getDefaultState().withProperty(VARIANT, Type.values()[MathHelper.clamp(meta, 0, Type.values().length - 1)]);
  }

  @Override
  public int getMetaFromState(IBlockState state) {

    return ((Type) state.getValue(VARIANT)).ordinal();
  }

  @Override
  protected BlockStateContainer createBlockState() {

    return new BlockStateContainer(this, new IProperty[] { VARIANT });
  }

  @Override
  public void getModels(Map<Integer, ModelResourceLocation> models) {

    for (Type type : Type.values())
      models.put(type.ordinal(), new ModelResourceLocation(getFullName().toLowerCase(), "gem=" + type.getName()));
  }
}
