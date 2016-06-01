package net.silentchaos512.gems.item.armor;

import java.util.List;

import com.google.common.collect.Lists;

import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraftforge.common.ISpecialArmor;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.silentchaos512.gems.SilentGems;
import net.silentchaos512.gems.client.gui.ModelGemArmor;
import net.silentchaos512.lib.registry.IRegistryObject;

public class ItemGemArmor extends ItemArmor implements ISpecialArmor, IRegistryObject {

  public static final double[] ABSORPTION_RATIO_BY_SLOT = { 0.175, 0.3, 0.4, 0.125 }; // sum = 1, starts with boots

  ModelBiped model;

  protected String itemName;

  public ItemGemArmor(int renderIndexIn, EntityEquipmentSlot equipmentSlotIn, String name) {

    super(ArmorMaterial.DIAMOND, renderIndexIn, equipmentSlotIn);
    this.itemName = name;
  }

  @Override
  public ArmorProperties getProperties(EntityLivingBase player, ItemStack armor,
      DamageSource source, double damage, int slot) {

    // TODO Auto-generated method stub
    // Iron: max 7.5?
    // Diamond: max 10
    double ratio = ABSORPTION_RATIO_BY_SLOT[slot];
    // SilentGems.instance.logHelper.debug(slot);
    return new ArmorProperties(0, ratio, (int) (4 * ratio * 8));
  }

  @Override
  public int getArmorDisplay(EntityPlayer player, ItemStack armor, int slot) {

    // TODO Auto-generated method stub
    return 5;
  }

  @Override
  public void damageArmor(EntityLivingBase entity, ItemStack stack, DamageSource source, int damage,
      int slot) {

    // TODO Auto-generated method stub
  }

  @Override
  @SideOnly(Side.CLIENT)
  public ModelBiped getArmorModel(EntityLivingBase entityLiving, ItemStack itemStack,
      EntityEquipmentSlot armorSlot, ModelBiped _default) {

    // SilentGems.instance.logHelper.debug(_default);
    if (model == null) {
      model = new ModelGemArmor();
    }
    return model;
  }

  @Override
  public String getArmorTexture(ItemStack stack, Entity entity, EntityEquipmentSlot slot,
      String type) {

    return SilentGems.RESOURCE_PREFIX + "textures/armor/" + "FluffyArmor" + "_"
        + (slot == EntityEquipmentSlot.LEGS ? "2" : "1") + ".png";
  }

  @Override
  public boolean getIsRepairable(ItemStack toRepair, ItemStack repair) {

    // TODO Tier detection
    return false;
  }

  @Override
  public void addRecipes() {

    // TODO Auto-generated method stub

  }

  @Override
  public void addOreDict() {

  }

  @Override
  public String getName() {

    return itemName;
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

    return Lists.newArrayList(new ModelResourceLocation(getModId() + ":Armor", "inventory"));
  }

  @Override
  public boolean registerModels() {

    // TODO Auto-generated method stub
    return false;
  }

  @Override
  public String getUnlocalizedName(ItemStack stack) {

    return "item.silentgems:" + itemName;
  }

  @Override
  public Item setUnlocalizedName(String name) {

    this.itemName = name;
    return super.setUnlocalizedName(name);
  }
}
