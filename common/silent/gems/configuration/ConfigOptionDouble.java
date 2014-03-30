package silent.gems.configuration;

import net.minecraftforge.common.Configuration;
import silent.gems.core.util.LogHelper;

public class ConfigOptionDouble extends ConfigOption {

    public double value;
    public final double defaultValue;

    public ConfigOptionDouble(String name, double defaultValue) {

        this.name = name;
        value = 0.0;
        this.defaultValue = defaultValue;
    }

    @Override
    public ConfigOption loadValue(Configuration c, String category) {

        value = c.get(category, name, defaultValue).getDouble(defaultValue);
        return this;
    }

    @Override
    public ConfigOption loadValue(Configuration c, String category, String comment) {

        value = c.get(category, name, defaultValue, comment).getDouble(defaultValue);
        return this;
    }

    @Override
    public ConfigOption validate() {

        if (name.equals(Config.CHAOS_GEM_FLIGHT_MAX_SPEED.name)) {
            if (value < 0.1) {
                value = 0.1;
            }
        }
        else {
            LogHelper.warning("No validation code for config settings " + name + " found!");
        }

        return this;
    }

}
