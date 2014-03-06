package silent.gems.enchantment;

import net.minecraft.enchantment.EnumEnchantmentType;
import silent.gems.item.EnchantToken;


public class ModEnchantments {

    public final static int MENDING_ID_DEFAULT = 128;
    
    public static int MENDING_ID;
    
    public static EnchantmentMending mending;
    
    public static void init() {
        
        mending = new EnchantmentMending(MENDING_ID, 1, EnumEnchantmentType.all);
        
        EnchantToken.init();
    }
}
