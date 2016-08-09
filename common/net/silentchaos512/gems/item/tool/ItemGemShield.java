package net.silentchaos512.gems.item.tool;

import java.util.List;

import com.google.common.collect.Lists;

import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.ItemShield;
import net.minecraft.item.ItemStack;
import net.silentchaos512.gems.SilentGems;
import net.silentchaos512.gems.api.ITool;
import net.silentchaos512.gems.lib.Names;
import net.silentchaos512.lib.registry.IRegistryObject;

public class ItemGemShield extends ItemShield implements IRegistryObject, ITool {

  @Override
  public ItemStack constructTool(ItemStack rod, ItemStack... materials) {

    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public float getMeleeDamage(ItemStack tool) {

    // TODO Auto-generated method stub
    return 0;
  }

  @Override
  public float getMagicDamage(ItemStack tool) {

    // TODO Auto-generated method stub
    return 0;
  }

  @Override
  public float getBaseMeleeDamageModifier() {

    // TODO Auto-generated method stub
    return 0;
  }

  @Override
  public float getBaseMeleeSpeedModifier() {

    // TODO Auto-generated method stub
    return 0;
  }

  @Override
  public void addRecipes() {

    // TODO Auto-generated method stub
    
  }

  @Override
  public void addOreDict() {

    // TODO Auto-generated method stub
    
  }

  @Override
  public String getName() {

    return Names.SHIELD;
  }

  @Override
  public String getFullName() {

    return getModId() + ":" + getName();
  }

  @Override
  public String getModId() {

    return SilentGems.MOD_ID;
  }

  @Override
  public List<ModelResourceLocation> getVariants() {

    // TODO Auto-generated method stub
    return Lists.newArrayList(new ModelResourceLocation(getFullName(), "inventory"));
  }

  @Override
  public boolean registerModels() {

    // TODO Auto-generated method stub
    return false;
  }
}
