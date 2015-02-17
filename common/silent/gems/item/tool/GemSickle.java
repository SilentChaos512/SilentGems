package silent.gems.item.tool;

import java.util.List;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemTool;
import net.minecraft.stats.StatList;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.IShearable;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.ShapedOreRecipe;
import silent.gems.SilentGems;
import silent.gems.core.registry.IAddRecipe;
import silent.gems.core.registry.IHasVariants;
import silent.gems.core.registry.SRegistry;
import silent.gems.core.util.LocalizationHelper;
import silent.gems.item.CraftingMaterial;
import silent.gems.item.ModItems;
import silent.gems.lib.Names;
import silent.gems.material.ModMaterials;

import com.google.common.collect.Sets;

public class GemSickle extends ItemTool implements IAddRecipe, IHasVariants {

  private int gemId;
  private boolean supercharged;

  public static final Material[] effectiveMaterials = new Material[] { Material.cactus,
      Material.leaves, Material.plants, Material.vine, Material.web };

  public GemSickle(ToolMaterial toolMaterial, int gemId, boolean supercharged) {

    super(2.0f, toolMaterial, Sets.newHashSet(new Block[] {}));

    this.gemId = gemId;
    this.supercharged = supercharged;
    this.setMaxDamage(toolMaterial.getMaxUses());
    this.setCreativeTab(SilentGems.tabSilentGems);
  }

  @Override
  public String[] getVariantNames() {

    return new String[] { getFullName() };
  }

  @Override
  public String getName() {

    return "Sickle" + gemId + (supercharged ? "Plus" : "");
  }

  @Override
  public String getFullName() {

    return SilentGems.MOD_ID + ":" + getName();
  }

  @Override
  public void addRecipes() {

    ItemStack tool = new ItemStack(this);
    ItemStack material = new ItemStack(ModItems.gem, 1, gemId + (supercharged ? 16 : 0));

    // Fish tools
    if (gemId == ModMaterials.FISH_GEM_ID) {
      material = new ItemStack(Items.fish);
    }

    if (supercharged) {
      GameRegistry.addRecipe(new ShapedOreRecipe(tool, true, new Object[] { " g", "gg", "s ", 'g',
          material, 's', CraftingMaterial.getStack(Names.ORNATE_STICK) }));
    } else {
      GameRegistry.addRecipe(new ShapedOreRecipe(tool, true, new Object[] { " g", "gg", "s ", 'g',
          material, 's', "stickWood" }));
    }
  }

  @Override
  public void addOreDict() {

  }

  @Override
  public float getStrVsBlock(ItemStack stack, Block block) {

    for (Material material : effectiveMaterials) {
      if (block.getMaterial() == material) {
        return this.efficiencyOnProperMaterial;
      }
    }
    return super.getStrVsBlock(stack, block);
  }

  // TODO: What's this function?
  // @Override
  // public boolean func_150897_b(Block block) {
  //
  // return block.getMaterial() == Material.web;
  // }

  public int getGemId() {

    return gemId;
  }

  @Override
  public boolean getIsRepairable(ItemStack stack1, ItemStack stack2) {

    ItemStack material = new ItemStack(SRegistry.getItem(Names.GEM_ITEM), 1, gemId
        + (supercharged ? 16 : 0));
    if (material.getItem() == stack2.getItem()
        && material.getItemDamage() == stack2.getItemDamage()) {
      return true;
    } else {
      return super.getIsRepairable(stack1, stack2);
    }
  }

  @Override
  public String getUnlocalizedName(ItemStack stack) {

    return LocalizationHelper.TOOL_PREFIX + "Sickle" + gemId + (supercharged ? "Plus" : "");
  }

  @Override
  public boolean onBlockDestroyed(ItemStack stack, World world, Block block, BlockPos pos,
      EntityLivingBase player) {

    if (block.getMaterial() != Material.leaves && block != Blocks.web && block != Blocks.tallgrass
        && block != Blocks.vine && block != Blocks.tripwire && !(block instanceof IShearable)) {
      return super.onBlockDestroyed(stack, world, block, pos, player);
    } else {
      return true;
    }
  }

  @Override
  public boolean onBlockStartBreak(ItemStack sickle, BlockPos pos, EntityPlayer player) {

    if (player.worldObj.isRemote) {
      return false;
    }

    IBlockState state = player.worldObj.getBlockState(pos);
    Block block = state.getBlock();
    if (block instanceof IShearable) {
      IShearable target = (IShearable) block;
      if (target.isShearable(sickle, player.worldObj, pos)
          && EnchantmentHelper.getEnchantmentLevel(Enchantment.silkTouch.effectId, sickle) > 0) {
        List<ItemStack> drops = target.onSheared(sickle, player.worldObj, pos,
            EnchantmentHelper.getEnchantmentLevel(Enchantment.fortune.effectId, sickle));
        Random rand = new Random();

        for (ItemStack stack : drops) {
          float f = 0.7F;
          double d = (double) (rand.nextFloat() * f) + (double) (1.0F - f) * 0.5D;
          double d1 = (double) (rand.nextFloat() * f) + (double) (1.0F - f) * 0.5D;
          double d2 = (double) (rand.nextFloat() * f) + (double) (1.0F - f) * 0.5D;
          EntityItem entityitem = new EntityItem(player.worldObj, (double) pos.getX() + d,
              (double) pos.getY() + d1, (double) pos.getZ() + d2, stack);
          entityitem.setPickupDelay(10);
          player.worldObj.spawnEntityInWorld(entityitem);
        }

        sickle.damageItem(1, player);
        player.addStat(StatList.mineBlockStatArray[Block.getIdFromBlock(block)], 1);
      }
    }
    return false;
  }
}
