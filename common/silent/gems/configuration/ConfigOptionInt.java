package silent.gems.configuration;

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

        if (false) {
            // derp
        }
        // Mistake catcher
        else {
            LogHelper.warning("No validation code for config setting " + name + " found!");
        }

        return this;
    }

}
