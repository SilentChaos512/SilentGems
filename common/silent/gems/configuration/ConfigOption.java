package silent.gems.configuration;

import net.minecraftforge.common.config.Configuration;

public abstract class ConfigOption {

    public String name;

    /**
     * Loads the setting from the config file.
     * 
     * @param c
     * @param category
     * @return
     */
    public abstract ConfigOption loadValue(Configuration c, String category);

    /**
     * Loads the setting from the config file and adds a comment.
     * 
     * @param c
     * @param category
     * @param comment
     * @return
     */
    public abstract ConfigOption loadValue(Configuration c, String category, String comment);

    /**
     * Checks certain config values that might cause errors. If the deriving class's validate override does not have
     * code to check this ConfigOption's value, a warning message should be output to the log file.
     * 
     * @return
     */
    public abstract ConfigOption validate();
}
