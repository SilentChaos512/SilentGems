package net.silentchaos512.gems.item.tool;

import java.util.List;

import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemSpade;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.silentchaos512.gems.SilentGems;
import net.silentchaos512.gems.api.IPlaceable;
import net.silentchaos512.gems.client.renderers.tool.ToolRenderHelper;
import net.silentchaos512.gems.configuration.Config;
import net.silentchaos512.gems.core.registry.SRegistry;
import net.silentchaos512.gems.core.util.LocalizationHelper;
import net.silentchaos512.gems.core.util.ToolHelper;
import net.silentchaos512.gems.enchantment.EnchantmentAOE;
import net.silentchaos512.gems.enchantment.ModEnchantments;
import net.silentchaos512.gems.item.CraftingMaterial;
import net.silentchaos512.gems.item.ModItems;
import net.silentchaos512.gems.lib.Names;
import net.silentchaos512.gems.lib.Strings;
import net.silentchaos512.gems.material.ModMaterials;

public class GemShovel extends ItemSpade {

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
  public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean advanced) {

    ToolHelper.addInformation(stack, player, list, advanced);
  }

  public static void addRecipe(ItemStack tool, int gemId, boolean supercharged) {

    ItemStack material = new ItemStack(ModItems.gem, 1, gemId + (supercharged ? 16 : 0));

    // Fish/flint tools
    if (gemId == ModMaterials.FISH_GEM_ID) {
      material = new ItemStack(Items.fish);
    } else if (gemId == ModMaterials.FLINT_GEM_ID) {
      material = new ItemStack(Items.flint);
    }

    if (supercharged) {
      GameRegistry.addRecipe(new ShapedOreRecipe(tool, true, "g", "s", "s", 'g', material, 's',
          CraftingMaterial.getStack(Names.ORNATE_STICK)));
    } else {
      GameRegistry.addRecipe(
          new ShapedOreRecipe(tool, true, "g", "s", "s", 'g', material, 's', "stickWood"));
    }
  }

  @Override
  public float getDigSpeed(ItemStack stack, Block block, int meta) {

    float speed = efficiencyOnProperMaterial;

    if (ForgeHooks.isToolEffective(stack, block, meta)) {
      return speed;
    }

    return super.getDigSpeed(stack, block, meta);
  }
  
  @Override
  public int getHarvestLevel(ItemStack stack, String toolClass) {
    
    int level = super.getHarvestLevel(stack, toolClass);
    if (level < 0) {
      return level;
    }

    if (stack.stackTagCompound != null) {
      int tip = ToolHelper.getToolHeadTip(stack);
      if (tip == 1 && level < Config.MINING_LEVEL_IRON_TIP) {
        // Iron tip
        level = Config.MINING_LEVEL_IRON_TIP;
      } else if (tip == 2 && level < Config.MINING_LEVEL_DIAMOND_TIP) {
        // Diamond tip
        level = Config.MINING_LEVEL_DIAMOND_TIP;
      }
    }
    
    return level;
  }
  
  @Override
  public int getMaxDamage(ItemStack stack) {

    return super.getMaxDamage(stack) + ToolHelper.getDurabilityBoost(stack);
  }

  public int getGemId() {

    return gemId;
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

    return LocalizationHelper.TOOL_PREFIX + "Shovel" + gemId + (supercharged ? "Plus" : "");
  }

  @Override
  public boolean hasEffect(ItemStack stack, int pass) {

    return ToolHelper.hasEffect(stack, pass);
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
      itemIcon = ToolRenderHelper.instance.shovelIcons.headM[gemId];
    }
  }

  @Override
  public boolean onBlockStartBreak(ItemStack stack, int x, int y, int z, EntityPlayer player) {

    if (EnchantmentHelper.getEnchantmentLevel(ModEnchantments.AOE_ID, stack) > 0) {
      EnchantmentAOE.tryActivate(stack, x, y, z, player);
    }

    return super.onBlockStartBreak(stack, x, y, z, player);
  }
}
