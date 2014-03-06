package silent.gems.enchantment;

import silent.gems.core.util.InventoryHelper;
import silent.gems.lib.Names;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.EnumEnchantmentType;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;

public class EnchantmentMending extends Enchantment {

    // The chance of the nth enchantment level repairing the tool each tick.
    private final static int[] rates = { 12, 10, 8, 6, 4, 2, 1, 1, 1, 1 };

    public EnchantmentMending(int par1, int par2, EnumEnchantmentType par3EnumEnchantmentType) {

        super(par1, par2, par3EnumEnchantmentType);
        setName(Names.MENDING);
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
    public boolean canApply(ItemStack stack) {

        // This enchantment is for abyss tools and books.
        if (InventoryHelper.isGemTool(stack) || stack.itemID == Item.book.itemID) {
            return stack.isItemStackDamageable() ? true : super.canApply(stack);
        }

        return false;
    }

    @Override
    public boolean canApplyTogether(Enchantment enchant) {

        return super.canApplyTogether(enchant) && enchant.effectId != Enchantment.unbreaking.effectId;
    }

    @Override
    public String getTranslatedName(int par1) {

        return Names.MENDING + " " + StatCollector.translateToLocal("enchantment.level." + par1);
    }
}
