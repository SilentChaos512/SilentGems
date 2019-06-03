package net.silentchaos512.gems.api.tool.part;

import net.minecraft.item.ItemStack;
import net.silentchaos512.gems.api.lib.EnumMaterialGrade;
import net.silentchaos512.gems.api.lib.EnumMaterialTier;
import net.silentchaos512.gems.api.lib.IPartPosition;
import net.silentchaos512.gems.api.lib.ToolPartPosition;
import net.silentchaos512.gems.api.stats.CommonItemStats;
import net.silentchaos512.gems.api.stats.ItemStat;
import net.silentchaos512.gems.api.stats.ItemStatModifier;
import net.silentchaos512.gems.api.stats.ItemStatModifier.Operation;
import net.silentchaos512.gems.api.tool.ToolStats;

public abstract class ToolPartTip extends ToolPart {
    private int miningLevel;
    private int durabilityBoost;
    private float speedBoost;
    private float meleeBoost;
    private float magicBoost;

    protected ToolPartTip(String key, ItemStack craftingStack, int harvestLevel, int durabilityBoost, float speedBoost, float meleeDamage, float magicDamage) {
        super(key, craftingStack);
        this.miningLevel = harvestLevel;
        this.durabilityBoost = durabilityBoost;
        this.speedBoost = speedBoost;
        this.meleeBoost = meleeDamage;
        this.magicBoost = magicDamage;
    }

    @Override
    public ItemStatModifier getStatModifier(ItemStat stat, EnumMaterialGrade grade) {
        float val = stats.getStat(stat);
        Operation op = ItemStatModifier.Operation.ADD;
        if (stat == CommonItemStats.ATTACK_SPEED)
            val -= 1f;
        else if (stat == CommonItemStats.HARVEST_LEVEL)
            op = ItemStatModifier.Operation.MAX;
        return new ItemStatModifier(getUnlocalizedName(), val, op);
    }

    @Override
    public void applyStats(ToolStats stats) {
        stats.durability += getDurability();
        stats.harvestSpeed += getHarvestSpeed();
        stats.meleeDamage += getMeleeDamage();
        stats.magicDamage += getMagicDamage();
        stats.meleeSpeed += getMeleeSpeed() - 1.0f;
        stats.chargeSpeed += getChargeSpeed();
        stats.enchantability += getEnchantability();
        stats.harvestLevel = Math.max(stats.harvestLevel, getHarvestLevel());
    }

    @Override
    public int getDurability() {
        return durabilityBoost;
    }

    @Override
    public float getHarvestSpeed() {
        return speedBoost;
    }

    @Override
    public int getHarvestLevel() {
        return miningLevel;
    }

    @Override
    public float getMeleeDamage() {
        return meleeBoost;
    }

    @Override
    public float getMagicDamage() {
        return magicBoost;
    }

    @Override
    public int getEnchantability() {
        return 0;
    }

    @Override
    public float getMeleeSpeed() {
        return 1.0f;
    }

    @Override
    public float getChargeSpeed() {
        return 0;
    }

    @Override
    public final float getProtection() {
        return 0;
    }

    @Override
    public boolean validForPosition(IPartPosition pos) {
        return pos == ToolPartPosition.TIP;
    }

    @Override
    public boolean validForToolOfTier(EnumMaterialTier toolTier) {
        return true;
    }
}
