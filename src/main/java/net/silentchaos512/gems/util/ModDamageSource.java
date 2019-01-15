package net.silentchaos512.gems.util;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.DamageSource;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;
import net.silentchaos512.gems.SilentGems;

public class ModDamageSource extends DamageSource {

  public static final DamageSource FREEZING = new ModDamageSource("freezing").setDamageBypassesArmor();
  public static final DamageSource SHOCKING = new ModDamageSource("shocking").setDamageBypassesArmor();
  public static final DamageSource NODE_ATTACK = new ModDamageSource("node_attack").setDamageBypassesArmor().setMagicDamage();

  public ModDamageSource(String name) {

    super(SilentGems.MOD_ID + "." + name);
  }

  @Override
  public ITextComponent getDeathMessage(EntityLivingBase entityLivingBaseIn) {

    EntityLivingBase entitylivingbase = entityLivingBaseIn.getAttackingEntity();
    String s = "death.attack." + this.damageType;
    String s1 = s + ".player";
    return entitylivingbase != null
        ? new TextComponentTranslation(s1, entityLivingBaseIn.getDisplayName(), entitylivingbase.getDisplayName())
        : new TextComponentTranslation(s, entityLivingBaseIn.getDisplayName());
  }
}
