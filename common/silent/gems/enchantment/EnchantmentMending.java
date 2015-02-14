package silent.gems.enchantment;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.EnumEnchantmentType;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBook;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;
import silent.gems.core.util.InventoryHelper;
import silent.gems.lib.Names;

public class EnchantmentMending extends Enchantment {

  // The chance of the nth enchantment level repairing the tool each tick.
  private final static int[] rates = { 16, 13, 10, 7, 4, 2, 1, 1, 1, 1 };

  protected EnchantmentMending(int enchID, int enchWeight, EnumEnchantmentType enchType) {

    super(enchID, new ResourceLocation("mending"), enchWeight, enchType);
    setName(Names.MENDING);
  }

  @Override
  public boolean canApply(ItemStack stack) {

    // This enchantment is for gem tools and books.
    if (InventoryHelper.isGemTool(stack) || stack.getItem() instanceof ItemBook) {
      return stack.isItemStackDamageable() ? true : super.canApply(stack);
    }

    return false;
  }

  @Override
  public boolean canApplyTogether(Enchantment enchant) {

    return super.canApplyTogether(enchant) && enchant.effectId != Enchantment.unbreaking.effectId;
  }

  @Override
  public int getMinEnchantability(int par1) {

    return 5 + (par1 - 1) * 8;
  }

  @Override
  public int getMaxEnchantability(int par1) {

    return super.getMinEnchantability(par1) + 50;
  }

  @Override
  public int getMaxLevel() {

    return 5;
  }

  @Override
  public String getTranslatedName(int par1) {

    return StatCollector.translateToLocal("enchantment." + Names.MENDING) + " "
        + StatCollector.translateToLocal("enchantment.level." + par1);
  }

  public void tryActivate(EntityPlayer player, ItemStack itemStack) {

    if (itemStack.getItemDamage() == 0 || !itemStack.isItemStackDamageable()) {
      return;
    }

    // Get enchantment level. Only evaluate if level is between 1 and 10, inclusive.
    int lvl = EnchantmentHelper.getEnchantmentLevel(this.effectId, itemStack);
    if (lvl < 1) {
      return;
    }
    if (lvl > 10) {
      lvl = 10;
    }

    if (player.worldObj.rand.nextInt(rates[lvl - 1]) == 0) {
      itemStack.setItemDamage(itemStack.getItemDamage() - 1);
    }
  }
}
