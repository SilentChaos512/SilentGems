package net.silentchaos512.gems.item.quiver;

import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;
import net.silentchaos512.gems.SilentGems;
import net.silentchaos512.gems.lib.Names;
import net.silentchaos512.lib.registry.IRegistryObject;
import net.silentchaos512.lib.registry.RecipeMaker;

import java.util.List;
import java.util.Map;

public class ItemQuiverEmpty extends Item implements IRegistryObject, IQuiver {

  public ItemQuiverEmpty() {

    setMaxStackSize(1);
    setTranslationKey(getFullName());
    setRegistryName(getName());
  }

  @Override
  public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand) {

    return QuiverHelper.onItemRightClick(world, player, hand);
  }

  @Override
  public void addInformation(ItemStack stack, World worldIn, List<String> tooltip,
      ITooltipFlag flagIn) {

    QuiverHelper.addInformation(stack, worldIn, tooltip, flagIn);
  }

  @Override
  public void addRecipes(RecipeMaker recipes) {

    recipes.addShaped(Names.QUIVER, new ItemStack(this), "lsl", "lal", " l ", 'l', "leather", 'a',
        Items.ARROW, 's', "string");
  }

  @Override
  public void addOreDict() {

  }

  @Override
  public String getModId() {

    return SilentGems.MODID;
  }

  @Override
  public String getName() {

    return Names.QUIVER;
  }

  @Override
  public void getModels(Map<Integer, ModelResourceLocation> models) {

    models.put(0, new ModelResourceLocation(getFullName(), "inventory"));
  }
}
