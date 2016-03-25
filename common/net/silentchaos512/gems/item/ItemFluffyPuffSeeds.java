package net.silentchaos512.gems.item;

import java.util.List;

import com.google.common.collect.Lists;

import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemSeeds;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.silentchaos512.gems.SilentGems;
import net.silentchaos512.gems.block.ModBlocks;
import net.silentchaos512.gems.lib.Names;
import net.silentchaos512.lib.registry.IRegistryObject;

public class ItemFluffyPuffSeeds extends ItemSeeds implements IRegistryObject {

  public ItemFluffyPuffSeeds() {

    super(ModBlocks.fluffyPuffPlant, Blocks.farmland);
    setUnlocalizedName(Names.FLUFFY_PUFF_SEEDS);
    MinecraftForge.addGrassSeed(new ItemStack(this), 2);
  }

  @Override
  public String getUnlocalizedName(ItemStack stack) {

    return "item.silentgems:" + Names.FLUFFY_PUFF_SEEDS;
  }

  @Override
  public void addRecipes() {

  }

  @Override
  public void addOreDict() {

  }

  @Override
  public String getName() {

    return Names.FLUFFY_PUFF_SEEDS;
  }

  @Override
  public String getFullName() {

    return getModId() + ":" + getName();
  }

  @Override
  public String getModId() {

    return SilentGems.MOD_ID.toLowerCase();
  }

  @Override
  public List<ModelResourceLocation> getVariants() {

    return Lists.newArrayList(new ModelResourceLocation(getFullName(), "inventory"));
  }

  @Override
  public boolean registerModels() {

    return false;
  }
}
