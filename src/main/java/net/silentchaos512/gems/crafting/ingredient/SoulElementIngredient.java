package net.silentchaos512.gems.crafting.ingredient;

import com.google.gson.JsonObject;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.common.crafting.IIngredientSerializer;
import net.silentchaos512.gems.SilentGems;
import net.silentchaos512.gems.item.SoulGemItem;
import net.silentchaos512.gems.lib.soul.Soul;
import net.silentchaos512.gems.lib.soul.SoulElement;
import net.silentchaos512.utils.EnumUtils;

import javax.annotation.Nullable;
import java.util.stream.Stream;

public class SoulElementIngredient extends Ingredient {
    private final SoulElement primary;
    private final SoulElement secondary;

    public SoulElementIngredient(SoulElement primary, SoulElement secondary) {
        super(Stream.of());
        this.primary = primary;
        this.secondary = secondary;
    }

    @Override
    public boolean test(@Nullable ItemStack stack) {
        if (stack == null || stack.isEmpty() || !(stack.getItem() instanceof SoulGemItem))
            return false;
        return testSoul(SoulGemItem.getSoul(stack));
    }

    private boolean testSoul(@Nullable Soul soul) {
        return soul != null
                && (this.primary == SoulElement.NONE || this.primary == soul.getPrimaryElement())
                && (this.secondary == SoulElement.NONE || this.secondary == soul.getSecondaryElement());
    }

    @Override
    public IIngredientSerializer<? extends Ingredient> getSerializer() {
        return Serializer.INSTANCE;
    }

    private ItemStack[] matchingStacks;

    @Override
    public ItemStack[] getMatchingStacks() {
        determineMatchingStacks();
        //noinspection AssignmentOrReturnOfFieldWithMutableType
        return matchingStacks;
    }

    private void determineMatchingStacks() {
        if (matchingStacks == null || matchingStacks.length == 0) {
            matchingStacks = Soul.getValues().stream()
                    .filter(this::testSoul)
                    .map(SoulGemItem::getStack)
                    .toArray(ItemStack[]::new);
        }
    }

    @Override
    public boolean hasNoMatchingItems() {
        return false;
    }

    @Override
    public boolean isSimple() {
        return false;
    }

    public static final class Serializer implements IIngredientSerializer<SoulElementIngredient> {
        public static final Serializer INSTANCE = new Serializer();
        public static final ResourceLocation NAME = SilentGems.getId("soul_elements");

        @Override
        public SoulElementIngredient parse(PacketBuffer buffer) {
            SoulElement primary = EnumUtils.byOrdinal(buffer.readByte(), SoulElement.NONE);
            SoulElement secondary = EnumUtils.byOrdinal(buffer.readByte(), SoulElement.NONE);
            return new SoulElementIngredient(primary, secondary);
        }

        @Override
        public SoulElementIngredient parse(JsonObject json) {
            SoulElement primary = EnumUtils.byName(JSONUtils.getString(json, "primary", "none"), SoulElement.NONE);
            SoulElement secondary = EnumUtils.byName(JSONUtils.getString(json, "secondary", "none"), SoulElement.NONE);
            return new SoulElementIngredient(primary, secondary);
        }

        @Override
        public void write(PacketBuffer buffer, SoulElementIngredient ingredient) {
            buffer.writeByte(ingredient.primary.ordinal());
            buffer.writeByte(ingredient.secondary.ordinal());
        }

        public static void register() {
            CraftingHelper.register(NAME, INSTANCE);
        }
    }
}
