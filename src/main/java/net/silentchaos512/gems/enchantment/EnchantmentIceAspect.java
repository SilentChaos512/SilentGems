package net.silentchaos512.gems.enchantment;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentFireAspect;
import net.minecraft.enchantment.EnumEnchantmentType;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.potion.PotionEffect;
import net.silentchaos512.gems.init.ModPotions;

public class EnchantmentIceAspect extends Enchantment {
    public static final String NAME = "IceAspect";
    public static final int EFFECT_DURATION = 80;

    public static boolean ENABLED = true;

    public EnchantmentIceAspect() {
        super(Rarity.RARE, EnumEnchantmentType.WEAPON,
                new EntityEquipmentSlot[]{EntityEquipmentSlot.MAINHAND});
        setName(NAME);
    }

    @Override
    public boolean canApplyTogether(Enchantment ench) {
        return !(ench instanceof EnchantmentFireAspect) && !(ench instanceof EnchantmentLightningAspect)
                && super.canApplyTogether(ench);
    }

    @Override
    public boolean canApplyAtEnchantingTable(ItemStack stack) {
        if (!ENABLED) return false;
        Item item = stack.getItem();
        return item instanceof ItemSword;
    }

    @Override
    public int getMinEnchantability(int level) {
        return 10 + 20 * (level - 1);
    }

    @Override
    public int getMaxEnchantability(int level) {
        return getMinEnchantability(level) + 50;
    }

    @Override
    public int getMaxLevel() {
        return 2;
    }

    @Override
    public String getName() {
        return "enchantment.silentgems:" + NAME;
    }

    /**
     * Apply effect to mob. Called in GemsCommonEvents#onLivingAttack. Also see
     * GemsCommonEvents#onLivingUpdate.
     *
     * @param entityLiving
     * @param enchLevel
     */
    public void applyTo(EntityLivingBase entityLiving, int enchLevel) {
        int duration = getEffectDuration(entityLiving, enchLevel);
        int amplifier = enchLevel - 1;
        entityLiving.addPotionEffect(new PotionEffect(ModPotions.freezing, duration, amplifier, true, false));
    }

    private int getEffectDuration(EntityLivingBase entityLiving, int enchLevel) {
        int ret = EFFECT_DURATION + (enchLevel - 1) * EFFECT_DURATION / 2;
        if (entityLiving instanceof EntityPlayer)
            ret /= 2;
        return ret;
    }
}
