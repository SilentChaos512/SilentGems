package silent.gems.item.tool;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemPickaxe;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.ShapedOreRecipe;
import silent.gems.SilentGems;
import silent.gems.core.registry.IAddRecipe;
import silent.gems.core.registry.IHasVariants;
import silent.gems.core.registry.SRegistry;
import silent.gems.core.util.LocalizationHelper;
import silent.gems.item.CraftingMaterial;
import silent.gems.item.ModItems;
import silent.gems.item.TorchBandolier;
import silent.gems.lib.Names;
import silent.gems.material.ModMaterials;

public class GemPickaxe extends ItemPickaxe implements IAddRecipe, IHasVariants {

  private final int gemId;
  private final boolean supercharged;

  public static final Material[] extraEffectiveMaterials = { Material.circuits, Material.glass,
      Material.piston };

  public GemPickaxe(ToolMaterial toolMaterial, int gemId, boolean supercharged) {

    super(toolMaterial);
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

    return "Pickaxe" + gemId + (supercharged ? "Plus" : "");
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
      GameRegistry.addRecipe(new ShapedOreRecipe(tool, true, new Object[] { "ggg", " s ", " s ",
          'g', material, 's', CraftingMaterial.getStack(Names.ORNATE_STICK) }));
    } else {
      GameRegistry.addRecipe(new ShapedOreRecipe(tool, true, new Object[] { "ggg", " s ", " s ",
          'g', material, 's', "stickWood" }));
    }
  }

  @Override
  public void addOreDict() {

  }

  @Override
  public float getDigSpeed(ItemStack stack, IBlockState block) {

    for (Material m : extraEffectiveMaterials) {
      if (block.getBlock().getMaterial() == m) {
        return efficiencyOnProperMaterial;
      }
    }

    return super.getDigSpeed(stack, block);
  }

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

    return LocalizationHelper.TOOL_PREFIX + "Pickaxe" + gemId + (supercharged ? "Plus" : "");
  }

  @Override
  public boolean onItemUse(ItemStack stack, EntityPlayer player, World world, BlockPos pos,
      EnumFacing side, float hitX, float hitY, float hitZ) {

    boolean used = false;
    int toolSlot = player.inventory.currentItem;
    int itemSlot = toolSlot + 1;
    ItemStack nextStack = null;

    if (toolSlot < 8) {
      nextStack = player.inventory.getStackInSlot(itemSlot);
      if (nextStack != null) {
        Item item = nextStack.getItem();
        if (item instanceof ItemBlock || item instanceof TorchBandolier) {
          BlockPos targetPos = pos.offset(side);
          int playerX = (int) Math.floor(player.posX);
          int playerY = (int) Math.floor(player.posY);
          int playerZ = (int) Math.floor(player.posZ);

          // Check for overlap with player, except for torches and torch bandolier
          int ty = targetPos.getY();
          if (Item.getIdFromItem(item) != Block.getIdFromBlock(Blocks.torch)
              && item != SRegistry.getItem(Names.TORCH_BANDOLIER) && targetPos.getX() == playerX
              && (ty == playerY || ty == playerY + 1 || ty == playerY - 1)
              && targetPos.getZ() == playerZ) {
            return false;
          }

          used = item.onItemUse(nextStack, player, world, pos, side, hitX, hitY, hitZ);
          if (nextStack.stackSize < 1) {
            nextStack = null;
            player.inventory.setInventorySlotContents(itemSlot, null);
          }
        }
      }
    }

    return used;
  }
}
