package net.silentchaos512.gems.loot.modifier;

import com.google.gson.JsonObject;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.LootContext;
import net.minecraft.loot.LootParameters;
import net.minecraft.loot.conditions.ILootCondition;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.loot.GlobalLootModifierSerializer;
import net.minecraftforge.common.loot.LootModifier;
import net.silentchaos512.gear.util.TraitHelper;
import net.silentchaos512.gems.SilentGems;
import net.silentchaos512.gems.api.Skulls;
import net.silentchaos512.gems.lib.soul.SoulTraits;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

public class SkullCollectorTraitLootModifier extends LootModifier {
    public SkullCollectorTraitLootModifier(ILootCondition[] conditionsIn) {
        super(conditionsIn);
    }

    @Nonnull
    @Override
    protected List<ItemStack> doApply(List<ItemStack> generatedLoot, LootContext context) {
        Entity entity = context.get(LootParameters.THIS_ENTITY);
        int level = TraitHelper.getTraitLevel(getItemUsed(context), SoulTraits.SKULL_COLLECTOR);
        List<ItemStack> ret = new ArrayList<>(generatedLoot);
        if (entity instanceof LivingEntity) {
            float dropRate = level * Skulls.getDropRate((LivingEntity) entity);
            if (SilentGems.random.nextFloat() < dropRate) {
                ItemStack skull = Skulls.getSkull((LivingEntity) entity);
                if (!skull.isEmpty()) {
                    ret.add(skull);
                }
            }
        }
        return ret;
    }

    private static ItemStack getItemUsed(LootContext context) {
        ItemStack tool = context.get(LootParameters.TOOL);
        if (tool != null && !tool.isEmpty())
            return tool;
        Entity entity = context.get(LootParameters.KILLER_ENTITY);
        if (entity instanceof PlayerEntity)
            return ((PlayerEntity) entity).getHeldItemMainhand();
        return ItemStack.EMPTY;
    }

    public static class Serializer extends GlobalLootModifierSerializer<SkullCollectorTraitLootModifier> {
        @Override
        public SkullCollectorTraitLootModifier read(ResourceLocation name, JsonObject json, ILootCondition[] conditionsIn) {
            return new SkullCollectorTraitLootModifier(conditionsIn);
        }
    }
}
