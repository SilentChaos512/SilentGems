package net.silentchaos512.gems.item.tool;

import java.util.List;

import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemAxe;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.silentchaos512.gems.SilentGems;
import net.silentchaos512.gems.client.renderers.tool.ToolRenderHelper;
import net.silentchaos512.gems.core.util.LocalizationHelper;
import net.silentchaos512.gems.core.util.ToolHelper;
import net.silentchaos512.gems.item.CraftingMaterial;
import net.silentchaos512.gems.lib.EnumMaterialClass;
import net.silentchaos512.gems.lib.IGemItem;
import net.silentchaos512.gems.lib.Names;

public class GemAxe extends ItemAxe implements IGemItem {

  public final int gemId;
  public final boolean supercharged;

  public static final Material[] extraEffectiveMaterials = { Material.wood, Material.leaves,
      Material.plants, Material.vine };

  public GemAxe(ToolMaterial toolMaterial, int gemId, boolean supercharged) {

    super(toolMaterial);
    this.gemId = gemId;
    this.supercharged = supercharged;
    this.setMaxDamage(toolMaterial.getMaxUses());
    addRecipe(new ItemStack(this), gemId, supercharged);
    this.setCreativeTab(SilentGems.tabSilentGems);
  }

  @Override
  public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean advanced) {

    ToolHelper.addInformation(stack, player, list, advanced);
  }

  public static void addRecipe(ItemStack tool, int gemId, boolean supercharged) {

    ItemStack material = ToolHelper.getCraftingMaterial(gemId, supercharged);

    if (supercharged) {
      GameRegistry.addRecipe(new ShapedOreRecipe(tool, true, "gg", "gs", " s", 'g', material, 's',
          CraftingMaterial.getStack(Names.ORNATE_STICK)));
    } else {
      GameRegistry.addRecipe(
          new ShapedOreRecipe(tool, true, "gg", "gs", " s", 'g', material, 's', "stickWood"));
    }
  }

  @Override
  public float getDigSpeed(ItemStack stack, Block block, int meta) {

    return ToolHelper.getDigSpeed(stack, efficiencyOnProperMaterial, block, meta,
        extraEffectiveMaterials);
  }

  public float getEfficiencyOnProperMaterial() {

    return efficiencyOnProperMaterial;
  }

  @Override
  public int getHarvestLevel(ItemStack stack, String toolClass) {

    int level = super.getHarvestLevel(stack, toolClass);
    return ToolHelper.getAdjustedMiningLevel(stack, level);
  }

  @Override
  public int getMaxDamage(ItemStack stack) {

    return super.getMaxDamage(stack) + ToolHelper.getDurabilityBoost(stack);
  }

  public int getGemId() {

    return gemId;
  }

  @Override
  public int getColorFromItemStack(ItemStack stack, int pass) {

    return ToolRenderHelper.instance.getColorFromItemStack(stack, pass);
  }

  @Override
  public IIcon getIcon(ItemStack stack, int pass) {

    return ToolRenderHelper.instance.getIcon(stack, pass, gemId, supercharged);
  }

  @Override
  public boolean getIsRepairable(ItemStack stack1, ItemStack stack2) {

    return ToolHelper.getIsRepairable(stack1, stack2);
  }

  @Override
  public int getRenderPasses(int meta) {

    return ToolRenderHelper.RENDER_PASS_COUNT;
  }

  @Override
  public String getUnlocalizedName(ItemStack stack) {

    return LocalizationHelper.TOOL_PREFIX + "Axe" + gemId + (supercharged ? "Plus" : "");
  }

  @Override
  public boolean hasEffect(ItemStack stack, int pass) {

    return ToolRenderHelper.instance.hasEffect(stack, pass);
  }

  @Override
  public boolean onBlockDestroyed(ItemStack stack, World world, Block block, int x, int y, int z,
      EntityLivingBase entity) {

    if (block.getMaterial() != Material.leaves) {
      return super.onBlockDestroyed(stack, world, block, x, y, z, entity);
    } else {
      return true;
    }
  }

  @Override
  public boolean onItemUse(ItemStack stack, EntityPlayer player, World world, int x, int y, int z,
      int side, float hitX, float hitY, float hitZ) {

    return ToolHelper.onItemUse(stack, player, world, x, y, z, side, hitX, hitY, hitZ);
  }

  @Override
  public boolean requiresMultipleRenderPasses() {

    return true;
  }

  @Override
  public void registerIcons(IIconRegister reg) {

    if (gemId >= 0 && gemId < ToolRenderHelper.HEAD_TYPE_COUNT) {
      itemIcon = ToolRenderHelper.instance.axeIcons.headM[gemId];
    }
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

  @Override
  public int getGemId(ItemStack stack) {

    return gemId;
  }

  @Override
  public boolean isSupercharged(ItemStack stack) {

    return supercharged;
  }

  @Override
  public EnumMaterialClass getGemMaterialClass(ItemStack stack) {

    return ToolHelper.getToolMaterialClass(stack);
  }
}
