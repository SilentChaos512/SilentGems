package silent.gems.configuration;

import net.minecraft.util.MathHelper;
import net.minecraftforge.common.Configuration;
import silent.gems.core.util.LogHelper;

public class ConfigOptionInt extends ConfigOption {

    public int value;
    public final int defaultValue;

    public ConfigOptionInt(String name, int defaultValue) {

        this.name = name;
        value = 0;
        this.defaultValue = defaultValue;
    }

    @Override
    public ConfigOption loadValue(Configuration c, String category) {

        value = c.get(category, name, defaultValue).getInt(defaultValue);
        return this;
    }

    @Override
    public ConfigOption loadValue(Configuration c, String category, String comment) {

        value = c.get(category, name, defaultValue, comment).getInt(defaultValue);
        return this;
    }

    @Override
    public ConfigOption validate() {

        if (name.equals(Config.CHAOS_ESSENCE_PER_ORE.name)) {
            value = MathHelper.clamp_int(value, 1, 8);
        }
        else if (name.equals(Config.CHAOS_GEM_MAX_BUFFS.name)) {
            value = MathHelper.clamp_int(value, 1, 8);
        }
        else if (name.equals(Config.CHAOS_GEM_MAX_CHARGE.name)) {
            value = value < 0 ? 0 : value;
        }
        else if (name.equals(Config.WORLD_CHAOS_ORE_RARITY.name)) {
            value = value < 1 ? 1 : value;
        }
        // Mistake catcher
        else {
            LogHelper.warning("No validation code for config setting " + name + " found!");
        }

        return this;
    }

}
