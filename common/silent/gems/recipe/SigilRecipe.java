package silent.gems.recipe;

import silent.gems.item.SigilRune;
import silent.gems.item.tool.Sigil;
import silent.gems.lib.SigilEffect;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemDye;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class SigilRecipe implements IRecipe {

    @Override
    public boolean matches(InventoryCrafting inventorycrafting, World world) {
        
        int numSigil = 0;
        int numEffectRune = 0;
        int numAmplifyRune = 0;
        int numSpeedRune = 0;
        int numDye = 0;
        
        ItemStack stack, sigilStack = null, effectRune = null;
        int damage;
        
        // Exactly one sigil and at least one rune is required.
        // Count valid ingredients and look for invalid.
        for (int i = 0; i < inventorycrafting.getSizeInventory(); ++i) {
            stack = inventorycrafting.getStackInSlot(i);
            if (stack != null) {
                damage = stack.getItemDamage();
                if (stack.getItem() instanceof Sigil) {
                    ++numSigil;
                    sigilStack = stack;
                }
                else if (stack.getItem() instanceof SigilRune) {
                    if (damage == 0) {
                        return false;
                    }
                    else if (damage == SigilRune.AMPLIFY_META) {
                        ++numAmplifyRune;
                    }
                    else if (damage == SigilRune.SPEED_META) {
                        ++numSpeedRune;
                    }
                    else {
                        ++numEffectRune;
                        effectRune = stack;
                    }
                }
                else if (stack.getItem() instanceof ItemDye) {
                    ++numDye;
                }
                else {
                    return false;
                }
            }
        }
        
        if (numSigil != 1 || (numEffectRune + numAmplifyRune + numSpeedRune + numDye) == 0 || numEffectRune > 1 || numDye > 1) {
            return false;
        }
        
        // Can all the runes be applied to the sigil?
        // Which effect would the sigil be after crafting?
        SigilEffect sigilEffect = null;
        Sigil sigilItem = (Sigil) sigilStack.getItem();
        if (effectRune != null) {
            sigilEffect = SigilEffect.all.get(effectRune.getItemDamage());
        }
        else {
            
            sigilEffect = SigilEffect.all.get(sigilItem.getEffectID(sigilStack));
        }
        
        boolean hasEffectChanged = sigilEffect.id != sigilItem.getEffectID(sigilStack);
        
        // What would the sigils power and speed levels be after crafting?
        int powerLevel = sigilItem.getPowerLevel(sigilStack);
        int speedLevel = sigilItem.getSpeedLevel(sigilStack);
        // If new effect has lower power/speed levels, reduce current levels.
        if (hasEffectChanged) {
            powerLevel = MathHelper.clamp_int(powerLevel, 1, sigilEffect.maxPower);
            speedLevel = MathHelper.clamp_int(speedLevel, 1, sigilEffect.maxSpeed);
        }
        powerLevel += numAmplifyRune;
        speedLevel += numSpeedRune;
        
        // Are these valid?
        return powerLevel <= sigilEffect.maxPower && speedLevel <= sigilEffect.maxSpeed;
    }

    @Override
    public ItemStack getCraftingResult(InventoryCrafting inventorycrafting) {

        ItemStack sigilStack = null, stack, result;
        int i, d;
        
        // Find sigil
        for (i = 0; i < inventorycrafting.getSizeInventory(); ++i) {
            stack = inventorycrafting.getStackInSlot(i);
            if (stack != null) {
                if (stack.getItem() instanceof Sigil) {
                    sigilStack = stack;
                }
            }
        }
        
        if (sigilStack == null) {
            return null;
        }
        
        result = sigilStack.copy();
        sigilStack = null;
        Sigil sigilItem = (Sigil) result.getItem();
        SigilEffect sigilEffect = SigilEffect.all.get(sigilItem.getEffectID(result));
        
        int powerLevel = sigilItem.getPowerLevel(result);
        int speedLevel = sigilItem.getSpeedLevel(result);
        
        // Find and apply runes and dyes
        for (i = 0; i < inventorycrafting.getSizeInventory(); ++i) {
            stack = inventorycrafting.getStackInSlot(i);
            if (stack != null) {
                if (stack.getItem() instanceof SigilRune) {
                    d = stack.getItemDamage();
                    if (d == 0) {
                        return null;
                    }
                    else if (d == SigilRune.AMPLIFY_META) {
                        ++powerLevel;
                    }
                    else if (d == SigilRune.SPEED_META) {
                        ++speedLevel;
                    }
                    else {
                        sigilItem.setEffectID(result, d);
                        sigilEffect = SigilEffect.all.get(d);
                    }
                }
                else if (stack.getItem() instanceof ItemDye) {
                    sigilItem.setColor(result, stack.getItemDamage());
                }
            }
        }
        
        sigilItem.setPowerLevel(result, MathHelper.clamp_int(powerLevel, 1, sigilEffect.maxPower));
        sigilItem.setSpeedLevel(result, MathHelper.clamp_int(speedLevel, 1, sigilEffect.maxSpeed));
        
        return result;
    }

    @Override
    public int getRecipeSize() {

        // TODO What's this?
        return 0;
    }

    @Override
    public ItemStack getRecipeOutput() {

        // TODO What's this?
        return null;
    }

}
