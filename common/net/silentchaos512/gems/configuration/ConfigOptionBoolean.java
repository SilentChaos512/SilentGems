package net.silentchaos512.gems.configuration;

import net.minecraftforge.common.config.Configuration;


public class ConfigOptionBoolean extends ConfigOption {

    public boolean value;
    public final boolean defaultValue;

    public ConfigOptionBoolean(String name, boolean defaultValue) {

        this.name = name;
        value = false;
        this.defaultValue = defaultValue;
    }

    @Override
    public ConfigOption loadValue(Configuration c, String category) {

        value = c.get(category, name, defaultValue).getBoolean(defaultValue);
        return this;
    }

    @Override
    public ConfigOption loadValue(Configuration c, String category, String comment) {

        value = c.get(category, name, defaultValue, comment).getBoolean(defaultValue);
        return this;
    }

    @Override
    public ConfigOption validate() {

        // I can't imagine any situation this would be needed for booleans.
        return this;
    }

}
