package net.silentchaos512.gems.item.tool;

import java.util.List;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemSpade;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.silentchaos512.gems.SilentGems;
import net.silentchaos512.gems.client.render.tool.ToolRenderHelper;
import net.silentchaos512.gems.core.registry.IHasVariants;
import net.silentchaos512.gems.core.util.LocalizationHelper;
import net.silentchaos512.gems.core.util.LogHelper;
import net.silentchaos512.gems.core.util.ToolHelper;
import net.silentchaos512.gems.item.CraftingMaterial;
import net.silentchaos512.gems.lib.Names;

public class GemShovel extends ItemSpade implements IHasVariants {

  public final int gemId;
  public final boolean supercharged;

  public GemShovel(ToolMaterial toolMaterial, int gemId, boolean supercharged) {

    super(toolMaterial);
    this.gemId = gemId;
    this.supercharged = supercharged;
    this.setMaxDamage(toolMaterial.getMaxUses());
    addRecipe(new ItemStack(this), gemId, supercharged);
    this.setCreativeTab(SilentGems.tabSilentGems);
  }

  @Override
  public String[] getVariantNames() {

    return ToolRenderHelper.instance.getVariantNames(new ItemStack(this));
  }

  @Override
  public String getName() {

    return ToolRenderHelper.instance.getName(new ItemStack(this));
  }

  @Override
  public String getFullName() {

    return ToolRenderHelper.instance.getFullName(new ItemStack(this));
  }

  @Override
  public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean advanced) {

    ToolHelper.addInformation(stack, player, list, advanced);
  }

  @Override
  public int getColorFromItemStack(ItemStack stack, int pass) {

    return ToolRenderHelper.instance.getColorFromItemStack(stack, pass);
  }

  public static void addRecipe(ItemStack tool, int gemId, boolean supercharged) {

    ItemStack material = ToolHelper.getCraftingMaterial(gemId, supercharged);

    if (supercharged) {
      GameRegistry.addRecipe(new ShapedOreRecipe(tool, true, "g", "s", "s", 'g', material, 's',
          CraftingMaterial.getStack(Names.ORNATE_STICK)));
    } else {
      GameRegistry.addRecipe(
          new ShapedOreRecipe(tool, true, "g", "s", "s", 'g', material, 's', "stickWood"));
    }
  }

  @Override
  public float getDigSpeed(ItemStack stack, IBlockState state) {

    return ToolHelper.getDigSpeed(stack, efficiencyOnProperMaterial, state, null);
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
  public boolean getIsRepairable(ItemStack stack1, ItemStack stack2) {

    return ToolHelper.getIsRepairable(stack1, stack2);
  }

  @Override
  public String getUnlocalizedName(ItemStack stack) {

    return LocalizationHelper.TOOL_PREFIX + "Shovel" + gemId + (supercharged ? "Plus" : "");
  }

  @Override
  public boolean hasEffect(ItemStack stack) {

    return ToolRenderHelper.instance.hasEffect(stack);
  }

  @Override
  public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack,
      boolean slotChanged) {

    return ToolRenderHelper.instance.shouldCauseReequipAnimation(oldStack, newStack, slotChanged);
  }

  @Override
  public boolean onItemUse(ItemStack stack, EntityPlayer player, World world, BlockPos pos,
      EnumFacing side, float hitX, float hitY, float hitZ) {

    return ToolHelper.onItemUse(stack, player, world, pos, side, hitX, hitY, hitZ);
  }

  @Override
  public boolean onBlockStartBreak(ItemStack stack, BlockPos pos, EntityPlayer player) {

    boolean canceled = super.onBlockStartBreak(stack, pos, player);
    if (!canceled) {
      ToolHelper.onBlockStartBreak(stack, pos, player);
    }
    return canceled;
  }

  @Override
  public boolean hitEntity(ItemStack stack, EntityLivingBase entity1, EntityLivingBase entity2) {

    ToolHelper.hitEntity(stack);
    return super.hitEntity(stack, entity1, entity2);
  }
}
