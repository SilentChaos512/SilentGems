package net.silentchaos512.gems.util;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.DamageSource;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import net.silentchaos512.gems.SilentGems;
import net.silentchaos512.lib.util.LocalizationHelper;

public class ModDamageSource extends DamageSource {

  public static final DamageSource FREEZING = new ModDamageSource("freezing").setDamageBypassesArmor();
  public static final DamageSource SHOCKING = new ModDamageSource("shocking").setDamageBypassesArmor();

  public ModDamageSource(String name) {

    super(SilentGems.MODID + "." + name);
  }

  @Override
  public ITextComponent getDeathMessage(EntityLivingBase entityLivingBaseIn) {

    EntityLivingBase entitylivingbase = entityLivingBaseIn.getAttackingEntity();
    String s = "death.attack." + this.damageType;
    String s1 = s + ".player";
    LocalizationHelper loc = SilentGems.localizationHelper;
    return entitylivingbase != null
        ? new TextComponentTranslation(s1, entityLivingBaseIn.getDisplayName(), entitylivingbase.getDisplayName())
        : new TextComponentTranslation(s, entityLivingBaseIn.getDisplayName());
  }
}
