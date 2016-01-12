package net.silentchaos512.gems.item.tool;

import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.MathHelper;
import net.silentchaos512.gems.client.render.tool.ToolRenderHelper;
import net.silentchaos512.gems.core.util.LocalizationHelper;
import net.silentchaos512.gems.core.util.ToolHelper;
import net.silentchaos512.gems.item.CraftingMaterial;
import net.silentchaos512.gems.item.Gem;
import net.silentchaos512.gems.item.ItemSG;
import net.silentchaos512.gems.item.ModItems;
import net.silentchaos512.gems.lib.Names;
import net.silentchaos512.gems.material.ModMaterials;

public class ItemBrokenTool extends ItemSG {

  public static final String NBT_TOOL = "BrokenTool";
  public static final String NBT_ITEM_ID = "OriginalID";

  public ItemBrokenTool() {

    setMaxStackSize(1);
    setUnlocalizedName(Names.BROKEN_TOOL);
  }
  
  @Override
  public String[] getVariantNames() {

    return new String[] { ToolRenderHelper.BROKEN_SMART_MODEL_NAME };
  }

  @Override
  public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean advanced) {

    ItemStack tool = getTool(stack);
    if (tool != null) {
      list.add(EnumChatFormatting.RED + tool.getDisplayName());
    } else {
      list.add(EnumChatFormatting.RED
          + LocalizationHelper.getOtherItemKey(Names.BROKEN_TOOL, "Invalid"));
    }

    super.addInformation(stack, player, list, advanced);
  }

  public ItemStack getFromTool(ItemStack tool) {

    ItemStack result = new ItemStack(this);

    result.setTagCompound(new NBTTagCompound());
    NBTTagCompound tags = result.getTagCompound();
    tags.setTag(NBT_TOOL, tool.getTagCompound());
    tags.setInteger(NBT_ITEM_ID, Item.getIdFromItem(tool.getItem()));

    return result;
  }

  public ItemStack getTool(ItemStack broken) {

    if (broken.hasTagCompound() && broken.getTagCompound().hasKey(NBT_ITEM_ID)) {
      Item itemTool = Item.getItemById(broken.getTagCompound().getInteger(NBT_ITEM_ID));
      if (itemTool != null) {
        ItemStack tool = new ItemStack(itemTool);
        tool.setTagCompound(broken.getTagCompound().getCompoundTag(NBT_TOOL));
        return tool;
      }
    }

    return null;
  }

  public int getTotalRepairValue(ItemStack tool, List<ItemStack> materials) {

    int result = 0;

    for (ItemStack material : materials) {
      if (!isValidRepairMaterial(tool, material)) {
        return 0;
      } else if (material != null) {
        result += ModItems.recipeDecorateTool.getRepairAmount(tool, material);
      }
    }

    return MathHelper.clamp_int(result, 0, tool.getItem().getMaxDamage(tool));
  }

  public boolean isValidRepairMaterial(ItemStack tool, ItemStack material) {

    Item item = material.getItem();
    if (item instanceof Gem
        || CraftingMaterial.doesStackMatch(material, Names.CHAOS_ESSENCE_PLUS_2)) {
      return true;
    }

    int gemId = ToolHelper.getToolGemId(tool);
    if (gemId == ModMaterials.FLINT_GEM_ID && item == Items.flint) {
      return true;
    }

    return false;
  }

  // @Override
  // public IIcon getIcon(ItemStack stack, int pass) {
  //
  // ItemStack tool = getTool(stack);
  // if (tool == null) {
  // return ToolRenderHelper.instance.iconError;
  // }
  //
  // if (tool.getItem() instanceof GemBow) {
  // switch (pass) {
  // case ToolRenderHelper.PASS_HEAD_L:
  // case ToolRenderHelper.PASS_HEAD_M:
  // case ToolRenderHelper.PASS_HEAD_R:
  // return ToolRenderHelper.instance.getIcon(tool, pass);
  // default:
  // return ToolRenderHelper.instance.iconBlank;
  // }
  // }
  //
  // switch (pass) {
  // case ToolRenderHelper.PASS_ROD:
  // case ToolRenderHelper.PASS_ROD_DECO:
  // case ToolRenderHelper.PASS_ROD_WOOL:
  // return ToolRenderHelper.instance.getIcon(tool, pass);
  // case ToolRenderHelper.PASS_HEAD_L:
  // if (tool.getItem() instanceof GemSword) {
  // return ToolRenderHelper.instance.getIcon(tool, pass);
  // }
  // break;
  // }
  //
  // return ToolRenderHelper.instance.iconBlank;
  // }

  @Override
  public boolean isFull3D() {

    return true;
  }

  // @Override
  // public boolean requiresMultipleRenderPasses() {
  //
  // return true;
  // }
  //
  // @Override
  // public int getRenderPasses(int meta) {
  //
  // return ToolRenderHelper.RENDER_PASS_COUNT;
  // }
}
