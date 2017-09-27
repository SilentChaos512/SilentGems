package net.silentchaos512.gems.event;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.silentchaos512.gems.init.ModItems;
import net.silentchaos512.gems.item.tool.ItemGemShield;
import net.silentchaos512.gems.util.ToolHelper;

public class ShieldEventHandler {

  @SubscribeEvent(priority = EventPriority.LOW)
  public void onBlockDamage(LivingHurtEvent event) {

    if (!(event.getEntityLiving() instanceof EntityPlayer)) {
      return;
    }

    ItemGemShield item = ModItems.shield;
    DamageSource source = event.getSource();
    EntityPlayer player = (EntityPlayer) event.getEntityLiving();
    ItemStack shield = player.getActiveItemStack();
    float damage = event.getAmount();

    // Block magic damage?
    if (item.shouldBlockDamage(event.getEntityLiving()) && source.isMagicDamage()) {
      float protection = ToolHelper.getMagicProtection(shield);

      damage = damage < 2f ? 1f : damage / 2f;
      event.setAmount(event.getAmount() * MathHelper.clamp(1f - protection, 0f, 1f));
    } else {
      damage = 0f;
    }

    // Rebound damage?
    if (source.getImmediateSource() != null) {
      float melee = item.getMeleeDamage(shield);
      if (melee > 0f) {
        source.getImmediateSource().attackEntityFrom(DamageSource.causeThornsDamage(player),
            melee);
        damage += 1f;
      }
    }

    // Damage the shield.
    ToolHelper.attemptDamageTool(shield, Math.round(damage), player);
  }
}
