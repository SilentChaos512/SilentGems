package net.silentchaos512.gems.api.lib;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.MathHelper;
import net.silentchaos512.gems.SilentGems;
import net.silentchaos512.lib.util.StackHelper;

import java.util.Random;

public enum EnumMaterialGrade {
    NONE(0), E(2), D(4), C(6), B(8), A(12), S(16), SS(24), SSS(32);

    public static final String NBT_KEY = "SG_Grade";

    public final int bonusPercent;

    EnumMaterialGrade(int bonusPercent) {

        this.bonusPercent = bonusPercent;
    }

    public static EnumMaterialGrade fromStack(ItemStack stack) {
        if (StackHelper.isValid(stack) && stack.hasTagCompound() && stack.getTagCompound().hasKey(NBT_KEY)) {
            String str = stack.getTagCompound().getString(NBT_KEY);
            return fromString(str);
        }
        return EnumMaterialGrade.NONE;
    }

    public static EnumMaterialGrade fromString(String str) {
        if (str != null) {
            for (EnumMaterialGrade grade : values()) {
                if (grade.name().equals(str)) {
                    return grade;
                }
            }
        }
        return EnumMaterialGrade.NONE;
    }

    public static EnumMaterialGrade selectRandom(Random random) {
        // If I understand the math here, 95% of the time, we should get grades between E and SS,
        // inclusive. SSS should be about 2.5% chance, I think. E picks up the 2.5% on the low end.
        int val = (int) (1.5 * random.nextGaussian() + 4);
        val = MathHelper.clamp(val, 1, 8);
        return values()[val];
    }

    public void setGradeOnStack(ItemStack stack) {
        if (!stack.isEmpty()) {
            if (!stack.hasTagCompound()) {
                stack.setTagCompound(new NBTTagCompound());
            }
            stack.getTagCompound().setString(NBT_KEY, name());
        }
    }

    public String getLocalizedName() {
        return this == NONE ? SilentGems.i18n.miscText("grade.none") : name();
    }
}
