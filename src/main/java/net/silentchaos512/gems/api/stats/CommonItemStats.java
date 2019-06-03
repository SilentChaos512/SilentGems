package net.silentchaos512.gems.api.stats;

public class CommonItemStats {
    // Generic
    public static final ItemStat DURABILITY = new ItemStat("durability", 1f, 1f, 32766f); // short max is 32767
    public static final ItemStat ENCHANTABILITY = new ItemStat("enchantability", 0f, 0f, 10000f);
    public static final ItemStat CHARGE_SPEED = new ItemStat("charge_speed", 1f, 1f, 10f);
    // Harvesting Tools
    public static final ItemStat HARVEST_LEVEL = new ItemStat("harvest_level", 0f, 0f, 10000f);
    public static final ItemStat HARVEST_SPEED = new ItemStat("harvest_speed", 1f, 0f, 10000f);
    // Melee Weapons
    public static final ItemStat MELEE_DAMAGE = new ItemStat("melee_damage", 0f, 0f, 10000f);
    public static final ItemStat MAGIC_DAMAGE = new ItemStat("magic_damage", 0f, 0f, 10000f);
    public static final ItemStat ATTACK_SPEED = new ItemStat("attack_speed", 1f, 0f, 100f);
    // Ranged Weapons
    public static final ItemStat RANGED_DAMAGE = new ItemStat("ranged_damage", 0f, 0f, 10000f);
    public static final ItemStat RANGED_SPEED = new ItemStat("ranged_speed", 1f, 0f, 100f);
    // Armor
    public static final ItemStat ARMOR = new ItemStat("armor", 0f, 0f, 40f);
    public static final ItemStat MAGIC_ARMOR = new ItemStat("magic_armor", 0f, 0f, 40f);

    public static void init() {
        // Just need to call something to initialize everything above.
    }
}
