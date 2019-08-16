package net.silentchaos512.gems.trait;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.silentchaos512.gear.api.traits.TraitActionContext;
import net.silentchaos512.gear.traits.SimpleTrait;
import net.silentchaos512.gems.SilentGems;
import net.silentchaos512.utils.MathUtils;

public class PersistenceTrait extends SimpleTrait {
    public static final Serializer<PersistenceTrait> SERIALIZER = new Serializer<>(SilentGems.getId("persistence"), PersistenceTrait::new);

    public PersistenceTrait(ResourceLocation id) {
        super(id, SERIALIZER);
    }

    @Override
    public void onUpdate(TraitActionContext context, boolean isEquipped) {
        PlayerEntity player = context.getPlayer();
        if (player != null && player.world.getGameTime() % 20 == 0) {
            ItemStack gear = context.getGear();
            if (gear.isDamaged() && MathUtils.tryPercentage(0.05)) {
                gear.setDamage(gear.getDamage() - 1);
            }
        }
    }
}
