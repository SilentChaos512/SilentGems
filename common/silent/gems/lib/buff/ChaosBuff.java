package silent.gems.lib.buff;

import java.util.ArrayList;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import silent.gems.core.registry.SRegistry;
import silent.gems.core.util.LocalizationHelper;
import silent.gems.item.CraftingMaterial;
import silent.gems.lib.Names;
import silent.gems.lib.Strings;
import cpw.mods.fml.common.registry.GameRegistry;

public class ChaosBuff {

    public final static ArrayList<ChaosBuff> all = new ArrayList<ChaosBuff>();

    public final int id;
    public final String name;
    public final int maxLevel;
    public final int potionId;

    private static int lastId = -1;

    public ChaosBuff(int id, String name, int maxLevel, int potionId) {

        this.id = id;
        this.name = name;
        this.maxLevel = maxLevel;
        this.potionId = potionId;
    }

    public static void init() {

        if (!all.isEmpty()) {
            return;
        }

        //addBuff("none", 1, -1, Item.ingotIron);
        addBuff("speed", 4, Potion.moveSpeed.id, Item.ingotGold);
        addBuff("haste", 4, Potion.digSpeed.id, Item.goldenCarrot);
        addBuff("jump", 4, Potion.jump.id, CraftingMaterial.getStack(Names.PLUME));
        addBuff("flight", 4, -1, CraftingMaterial.getStack(Names.GOLDEN_PLUME));
        addBuff("nightVision", 1, Potion.nightVision.id, Item.goldenCarrot);
        addBuff("regen", 2, Potion.regeneration.id, Item.ghastTear);
        addBuff("resist", 2, Potion.resistance.id, Item.plateLeather);
        addBuff("fireResist", 1, Potion.fireResistance.id, Item.blazeRod);
        addBuff("waterBreathing", 1, Potion.waterBreathing.id, Block.blockLapis);
        addBuff("strength", 2, Potion.damageBoost.id, Block.blockRedstone);
    }

    private static void addBuff(String name, int maxLevel, int potionId, Object material) {

        ChaosBuff buff = new ChaosBuff(++lastId, name, maxLevel, potionId);
        all.add(buff);
        GameRegistry.addShapedRecipe(new ItemStack(SRegistry.getItem(Names.CHAOS_RUNE), 1, lastId), "mcm", "cmc", "rcr", 'm', material,
                'c', CraftingMaterial.getStack(Names.CHAOS_ESSENCE_PLUS), 'r', Item.redstone);
    }
    
    public static ChaosBuff getBuffByName(String name) {
        
        for (ChaosBuff buff : all) {
            if (buff.name.equals(name)) {
                return buff;
            }
        }
        
        return null;
    }

    public void apply(EntityPlayer player, int level) {

        if (potionId > -1) {
            player.addPotionEffect(new PotionEffect(potionId, 250, level - 1, true));
        }

        // Apply other effects here.
        // TODO
    }
    
    public void remove(EntityPlayer player) {
        
        if (potionId > -1) {
            player.removePotionEffect(potionId);
        }
    }

    public String getDisplayName(int level) {

        String s = LocalizationHelper.getLocalizedString(Strings.BUFF_RESOURCE_PREFIX + this.name);
        s += " ";

        if (level == 1) {
            s += "I";
        }
        else if (level == 2) {
            s += "II";
        }
        else if (level == 3) {
            s += "III";
        }
        else if (level == 4) {
            s += "IV";
        }
        else if (level == 5) {
            s += "V";
        }
        else {
            s += level;
        }

        return s;
    }
}
