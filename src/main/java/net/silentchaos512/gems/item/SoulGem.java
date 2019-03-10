package net.silentchaos512.gems.item;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.EntityType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;
import net.silentchaos512.gems.init.ModItemGroups;
import net.silentchaos512.gems.lib.soul.Soul;
import net.silentchaos512.gems.lib.soul.SoulElement;
import net.silentchaos512.utils.Color;
import net.silentchaos512.utils.Lazy;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Objects;

public class SoulGem extends Item {
    public static final Lazy<SoulGem> INSTANCE = Lazy.of(SoulGem::new);

    private static final String NBT_KEY = "SGems_SoulGem";

    public SoulGem() {
        super(new Properties().group(ModItemGroups.MATERIALS));
    }

    public static ItemStack getStack(Soul soul) {
        ItemStack result = new ItemStack(INSTANCE.get());
        EntityType<?> entityType = soul.getEntityType();
        if (entityType != null) {
            ResourceLocation name = Objects.requireNonNull(entityType.getRegistryName());
            result.getOrCreateTag().setString(NBT_KEY, name.toString());
        }
        return result;
    }

    @Nullable
    public static Soul getSoul(ItemStack stack) {
        return Soul.from(getSoulId(stack));
    }

    private static String getSoulId(ItemStack stack) {
        return stack.getOrCreateTag().getString(NBT_KEY);
    }

    public static int getColor(ItemStack stack, int tintIndex) {
        Soul soul = getSoul(stack);
        if (soul == null) return Color.VALUE_WHITE;
        if (tintIndex == 0) return soul.getPrimaryColor();
        if (tintIndex == 1) return soul.getSecondaryColor();
        return Color.VALUE_WHITE;
    }

    @Override
    public ITextComponent getDisplayName(ItemStack stack) {
        Soul soul = getSoul(stack);
        if (soul != null) {
            ITextComponent entityName = soul.getEntityName();
            return new TextComponentTranslation(this.getTranslationKey() + ".nameProper", entityName);
        }
        return super.getDisplayName(stack);
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        Soul soul = getSoul(stack);
        if (soul != null) {
            tooltip.add(soul.getPrimaryElement().getDisplayName());
            SoulElement element2 = soul.getSecondaryElement();
            if (element2 != SoulElement.NONE) {
                tooltip.add(element2.getDisplayName());
            }
        }
    }

    @Override
    public void fillItemGroup(ItemGroup group, NonNullList<ItemStack> items) {
        if (!isInGroup(group)) return;
        for (Soul soul : Soul.getValues()) {
            items.add(getStack(soul));
        }
    }
}
