package net.silentchaos512.gems.item.tool;

import java.util.List;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.util.BlockPos;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.silentchaos512.gems.SilentGems;
import net.silentchaos512.gems.client.renderers.tool.ToolRenderHelper;
import net.silentchaos512.gems.core.registry.IHasVariants;
import net.silentchaos512.gems.core.util.LocalizationHelper;
import net.silentchaos512.gems.core.util.ToolHelper;
import net.silentchaos512.gems.item.CraftingMaterial;
import net.silentchaos512.gems.lib.Names;

public class GemSword extends ItemSword implements IHasVariants {

  public final int gemId;
  public final boolean supercharged;
  private final ToolMaterial toolMaterial;

  public GemSword(ToolMaterial toolMaterial, int gemId, boolean supercharged) {

    super(toolMaterial);
    this.gemId = gemId;
    this.supercharged = supercharged;
    this.toolMaterial = toolMaterial;
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

  public static void addRecipe(ItemStack tool, int gemId, boolean supercharged) {

    ItemStack material = ToolHelper.getCraftingMaterial(gemId, supercharged);

    if (supercharged) {
      GameRegistry.addRecipe(new ShapedOreRecipe(tool, true, "g", "g", "s", 'g', material, 's',
          CraftingMaterial.getStack(Names.ORNATE_STICK)));
    } else {
      GameRegistry.addRecipe(
          new ShapedOreRecipe(tool, true, "g", "g", "s", 'g', material, 's', "stickWood"));
    }
  }

  public int getGemId() {

    return gemId;
  }

  @Override
  public int getMaxDamage(ItemStack stack) {

    return super.getMaxDamage(stack) + ToolHelper.getDurabilityBoost(stack);
  }

  @Override
  public boolean getIsRepairable(ItemStack stack1, ItemStack stack2) {

    return ToolHelper.getIsRepairable(stack1, stack2);
  }

  @Override
  public String getUnlocalizedName(ItemStack stack) {

    return LocalizationHelper.TOOL_PREFIX + "Sword" + gemId + (supercharged ? "Plus" : "");
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
