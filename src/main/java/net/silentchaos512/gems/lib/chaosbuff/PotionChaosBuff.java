package net.silentchaos512.gems.lib.chaosbuff;

import lombok.Getter;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectInstance;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistries;
import net.silentchaos512.gems.SilentGems;

import java.util.Objects;

public class PotionChaosBuff extends SimpleChaosBuff {
    private static final ResourceLocation SERIALIZER_ID = SilentGems.getId("potion");
    static final IChaosBuffSerializer<PotionChaosBuff> SERIALIZER = new Serializer<>(
            SERIALIZER_ID,
            PotionChaosBuff::new,
            (buff, json) -> {
                String str = JSONUtils.getString(json, "effect");
                buff.effect = ForgeRegistries.POTIONS.getValue(new ResourceLocation(str));
                buff.effectDuration = JSONUtils.getInt(json, "effectDuration", 50);
            },
            (buff, buffer) -> {
                buff.effect = ForgeRegistries.POTIONS.getValue(buffer.readResourceLocation());
                buff.effectDuration = buffer.readVarInt();
            },
            (buff, buffer) -> {
                buffer.writeResourceLocation(Objects.requireNonNull(buff.effect.getRegistryName()));
                buffer.writeVarInt(buff.effectDuration);
            }
    );

    @Getter private Effect effect;
    private int effectDuration;

    public PotionChaosBuff(ResourceLocation id) {
        super(id);
    }

    @Override
    public void applyTo(PlayerEntity player, int level) {
        player.addPotionEffect(new EffectInstance(this.effect, this.effectDuration, level - 1, true, false));
    }

    @Override
    public void removeFrom(PlayerEntity player) {
        player.removePotionEffect(this.effect);
    }

    @Override
    public int getRuneColor() {
        if (effect != null) return effect.getLiquidColor();
        return super.getRuneColor();
    }

    @Override
    public IChaosBuffSerializer<?> getSerializer() {
        return SERIALIZER;
    }
}
