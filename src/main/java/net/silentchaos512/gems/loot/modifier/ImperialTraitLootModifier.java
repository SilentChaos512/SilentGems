package net.silentchaos512.gems.loot.modifier;

import com.google.gson.JsonObject;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.LootContext;
import net.minecraft.world.storage.loot.LootParameter;
import net.minecraft.world.storage.loot.LootParameters;
import net.minecraft.world.storage.loot.conditions.ILootCondition;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.loot.GlobalLootModifierSerializer;
import net.minecraftforge.common.loot.LootModifier;
import net.silentchaos512.gear.util.TraitHelper;
import net.silentchaos512.gems.SilentGems;
import net.silentchaos512.gems.init.GemsTraits;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

public class ImperialTraitLootModifier extends LootModifier {
    private static final float BASE_CHANCE = 0.2f;

    public ImperialTraitLootModifier(ILootCondition[] conditionsIn) {
        super(conditionsIn);
    }

    @Nonnull
    @Override
    protected List<ItemStack> doApply(List<ItemStack> generatedLoot, LootContext context) {
        List<ItemStack> ret = new ArrayList<>(generatedLoot);
        ItemStack tool = context.get(LootParameters.TOOL);

        if (tool != null) {
            int traitLevel = TraitHelper.getTraitLevel(tool, GemsTraits.IMPERIAL);
            generatedLoot.forEach(s -> {
                ItemStack stack = tryApply(s, traitLevel);
                if (!stack.isEmpty()) {
                    ret.add(stack);
                }
            });
        }

        return ret;
    }

    private static ItemStack tryApply(ItemStack stack, int traitLevel) {
        if (SilentGems.random.nextFloat() < BASE_CHANCE * traitLevel && stack.getItem().isIn(Tags.Items.GEMS)) {
            return stack.copy();
        }
        return ItemStack.EMPTY;
    }

    public static class Serializer extends GlobalLootModifierSerializer<ImperialTraitLootModifier> {
        @Override
        public ImperialTraitLootModifier read(ResourceLocation name, JsonObject json, ILootCondition[] conditionsIn) {
            return new ImperialTraitLootModifier(conditionsIn);
        }
    }
}
