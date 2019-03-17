package net.silentchaos512.gems.lib.chaosbuff;

import com.google.gson.JsonObject;
import lombok.Getter;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistries;
import net.silentchaos512.gems.SilentGems;

public class PotionChaosBuff extends SimpleChaosBuff {
    private static final ResourceLocation SERIALIZER_ID = SilentGems.getId("potion");
    static final IChaosBuffSerializer<PotionChaosBuff> SERIALIZER = new Serializer<>(
            SERIALIZER_ID,
            PotionChaosBuff::new,
            PotionChaosBuff::readJson
    );

    @Getter private Potion potion;
    private int effectDuration;

    public PotionChaosBuff(ResourceLocation id) {
        super(id);
    }

    private static void readJson(PotionChaosBuff buff, JsonObject json) {
        String str = JsonUtils.getString(json, "potion");
        buff.potion = ForgeRegistries.POTIONS.getValue(new ResourceLocation(str));
        buff.effectDuration = JsonUtils.getInt(json, "effectDuration", 110);
    }

    @Override
    public void applyTo(EntityPlayer player, int level) {
        player.addPotionEffect(new PotionEffect(this.potion, this.effectDuration, level - 1, true, false));
    }

    @Override
    public void removeFrom(EntityPlayer player) {
        player.removePotionEffect(this.potion);
    }

    @Override
    public int getRuneColor() {
        if (potion != null) return potion.getLiquidColor();
        return super.getRuneColor();
    }
}
