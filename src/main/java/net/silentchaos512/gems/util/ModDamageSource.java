package net.silentchaos512.gems.util;

import net.minecraft.entity.LivingEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.silentchaos512.gems.SilentGems;

public class ModDamageSource extends DamageSource {
    public static final DamageSource FREEZING = new ModDamageSource("freezing").setDamageBypassesArmor();
    public static final DamageSource SHOCKING = new ModDamageSource("shocking").setDamageBypassesArmor();

    public ModDamageSource(String name) {
        super(SilentGems.MOD_ID + "." + name);
    }

    @Override
    public ITextComponent getDeathMessage(LivingEntity LivingEntityIn) {
        LivingEntity entity = LivingEntityIn.getAttackingEntity();
        String s = "death.attack." + this.damageType;
        String s1 = s + ".player";
        return entity != null
                ? new TranslationTextComponent(s1, LivingEntityIn.getDisplayName(), entity.getDisplayName())
                : new TranslationTextComponent(s, LivingEntityIn.getDisplayName());
    }
}
