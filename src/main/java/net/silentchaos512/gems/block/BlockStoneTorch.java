package net.silentchaos512.gems.block;

import java.util.Map;

import net.minecraft.block.BlockTorch;
import net.minecraft.block.SoundType;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;
import net.silentchaos512.gems.SilentGems;
import net.silentchaos512.gems.init.ModItems;
import net.silentchaos512.gems.lib.Names;
import net.silentchaos512.lib.registry.IRegistryObject;
import net.silentchaos512.lib.registry.RecipeMaker;

public class BlockStoneTorch extends BlockTorch implements IRegistryObject {

  public BlockStoneTorch() {

    this.setHardness(0.0f);
    this.setLightLevel(0.9375f);
    this.setSoundType(SoundType.STONE);
  }

  @Override
  public void addRecipes(RecipeMaker recipes) {

    // TODO Auto-generated method stub
    recipes.addShapedOre("stone_torch", new ItemStack(this, 4), "c", "s", 'c',
        new ItemStack(Items.COAL, 1, OreDictionary.WILDCARD_VALUE), 's', "rodStone");
    recipes.addShapedOre("stone_torch_chaos", new ItemStack(this, 16), "c", "s", 'c',
        ModItems.craftingMaterial.chaosCoal, 's', "rodStone");
  }

  @Override
  public void addOreDict() {

  }

  @Override
  public String getModId() {

    return SilentGems.MODID;
  }

  @Override
  public String getName() {

    return Names.STONE_TORCH;
  }

  @Override
  public void getModels(Map<Integer, ModelResourceLocation> models) {

    models.put(0, new ModelResourceLocation(getFullName().toLowerCase(), "inventory"));
  }
}
