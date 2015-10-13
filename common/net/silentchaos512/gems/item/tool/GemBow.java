package net.silentchaos512.gems.item.tool;

import java.util.List;

import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.silentchaos512.gems.SilentGems;
import net.silentchaos512.gems.client.renderers.tool.ToolRenderHelper;
import net.silentchaos512.gems.core.registry.IAddRecipe;
import net.silentchaos512.gems.core.registry.SRegistry;
import net.silentchaos512.gems.core.util.LocalizationHelper;
import net.silentchaos512.gems.core.util.ToolHelper;
import net.silentchaos512.gems.item.CraftingMaterial;
import net.silentchaos512.gems.lib.Names;
import net.silentchaos512.gems.lib.Strings;

public class GemBow extends ItemBow implements IAddRecipe {

  public final int gemId;
  public final boolean supercharged;
  public final double arrowDamage;

  public GemBow(ToolMaterial toolMaterial, int gemId, boolean supercharged) {

    this.gemId = gemId;
    this.supercharged = supercharged;
    this.arrowDamage = (double) (toolMaterial.getDamageVsEntity()) / 2.0 + 0.5;
    this.setMaxDamage(toolMaterial.getMaxUses());
    this.setCreativeTab(SilentGems.tabSilentGems);
  }

  @Override
  public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean advanced) {

    ToolHelper.addInformation(stack, player, list, advanced);
  }

  @Override
  public void addRecipes() {

    ItemStack material = ToolHelper.getCraftingMaterial(gemId, supercharged);
    ItemStack result = new ItemStack(this);
    if (supercharged) {
      ItemStack rod = CraftingMaterial.getStack(Names.ORNATE_STICK);
      ItemStack string = CraftingMaterial.getStack(Names.GILDED_STRING);
      GameRegistry.addShapedRecipe(result, "rgs", "g s", "rgs", 'r', rod, 'g', material, 's',
          string);
    } else {
      GameRegistry.addRecipe(new ShapedOreRecipe(result, "rgs", "g s", "rgs", 'r', "stickWood", 'g',
          material, 's', Items.string));
    }
  }

  @Override
  public void addOreDict() {

  }

  @Override
  public int getMaxDamage(ItemStack stack) {

    return super.getMaxDamage(stack) + ToolHelper.getDurabilityBoost(stack);
  }

  @Override
  public IIcon getIcon(ItemStack stack, int pass) {

    return ToolRenderHelper.instance.getIcon(stack, pass, gemId, supercharged);
  }

  @Override
  public void registerIcons(IIconRegister reg) {

    // TODO
  }

  @Override
  public int getRenderPasses(int meta) {

    return ToolRenderHelper.RENDER_PASS_COUNT;
  }

  @Override
  public boolean requiresMultipleRenderPasses() {

    return true;
  }

  @Override
  public boolean getIsRepairable(ItemStack stack1, ItemStack stack2) {

    return ToolHelper.getIsRepairable(stack1, stack2);
  }

  @Override
  public String getUnlocalizedName(ItemStack stack) {

    return LocalizationHelper.TOOL_PREFIX + "Bow" + gemId + (supercharged ? "Plus" : "");
  }

  @Override
  public boolean hasEffect(ItemStack stack, int pass) {

    return ToolRenderHelper.instance.hasEffect(stack, pass);
  }

  @Override
  public boolean onBlockStartBreak(ItemStack stack, int x, int y, int z, EntityPlayer player) {

    boolean canceled = super.onBlockStartBreak(stack, x, y, z, player);
    if (!canceled) {
      ToolHelper.onBlockStartBreak(stack, x, y, z, player);
    }
    return canceled;
  }

  @Override
  public boolean hitEntity(ItemStack stack, EntityLivingBase entity1, EntityLivingBase entity2) {

    ToolHelper.hitEntity(stack);
    return super.hitEntity(stack, entity1, entity2);
  }
}
