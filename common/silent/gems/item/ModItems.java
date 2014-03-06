package silent.gems.item;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.WeightedRandomChestContent;
import net.minecraftforge.common.ChestGenHooks;
import silent.gems.core.registry.SRegistry;
import silent.gems.core.util.LogHelper;
import silent.gems.item.tool.GemAxe;
import silent.gems.item.tool.GemHoe;
import silent.gems.item.tool.GemPickaxe;
import silent.gems.item.tool.GemShovel;
import silent.gems.item.tool.GemSword;
import silent.gems.lib.EnumGem;
import silent.gems.lib.Names;
import silent.gems.recipe.EnchantToolRecipe;
import cpw.mods.fml.common.registry.GameRegistry;


public class ModItems {

    private final static int CRAFTING_MATERIAL_ID = 6005;
    private final static int DEBUG_ITEM_ID = 6020;
    private final static int ENCHANT_TOKEN_ID = 6004;
    private final static int FOOD_ID = 6006;
    private final static int GEM_ITEM_ID = 6000;
    private final static int GEM_SHARD_ID = 6001;
    private final static int KITTY_SUMMON_ID = 6013;
    private final static int SIGIL_ID = 6011;
    private final static int SIGIL_RUNE_ID = 6012;
    private final static int TOOL_START_ID = 6060;
    private final static int TORCH_BANDOLIER_ID = 6010;
    
    public static void init() {
        
        SRegistry.registerItem(CraftingMaterial.class, Names.CRAFTING_MATERIALS, CRAFTING_MATERIAL_ID);
        SRegistry.registerItem(DebugItem.class, Names.DEBUG_ITEM, DEBUG_ITEM_ID);
        SRegistry.registerItem(EnchantToken.class, Names.ENCHANT_TOKEN, ENCHANT_TOKEN_ID);
        SRegistry.registerItem(FoodSG.class, Names.FOOD, FOOD_ID);
        SRegistry.registerItem(Gem.class, Names.GEM_ITEM, GEM_ITEM_ID);
        SRegistry.registerItem(GemShard.class, Names.GEM_SHARD, GEM_SHARD_ID);
        SRegistry.registerItem(KittySummon.class, Names.KITTY_SUMMON, KITTY_SUMMON_ID);
//        SRegistry.registerItem(Sigil.class, Names.SIGIL, SIGIL_ID);
//        SRegistry.registerItem(SigilRune.class, Names.SIGIL_RUNE, SIGIL_RUNE_ID);
        SRegistry.registerItem(TorchBandolier.class, Names.TORCH_BANDOLIER, TORCH_BANDOLIER_ID);
        
        // Register tools.
        int id = TOOL_START_ID - 1;
        int gem;
        Object[] params = new Object[] { null, 0, false }; // Constructor parameters
        for (int i = 0; i < 24; ++i) {
            boolean supercharged = i >= 12;
            gem = supercharged ? i - 12 : i;
            params[0] = EnumGem.values()[gem].getToolMaterial(supercharged);
            params[1] = gem;
            params[2] = supercharged;
            String s = gem + (supercharged ? "Plus" : "");
            SRegistry.registerItem(GemSword.class, "Sword" + s, ++id, "", params);
            SRegistry.registerItem(GemPickaxe.class, "Pickaxe" + s, ++id, "", params);
            SRegistry.registerItem(GemShovel.class, "Shovel" + s, ++id, "", params);
            SRegistry.registerItem(GemAxe.class, "Axe" + s, ++id, "", params);
            SRegistry.registerItem(GemHoe.class, "Hoe" + s, ++id, "", params);
        }
    }
    
    public static void initItemRecipes() {
        
        GameRegistry.addRecipe(new EnchantToolRecipe());
    }
    
    public static void addRandomChestGenLoot() {
        
        // TODO
    }
    
    private static void addLoot(String chest, Object loot, int min, int max, int weight) {

        ItemStack stack;

        if (loot instanceof Item) {
            stack = new ItemStack((Item) loot);
        }
        else if (loot instanceof Block) {
            stack = new ItemStack((Block) loot);
        }
        else if (loot instanceof ItemStack) {
            stack = (ItemStack) loot;
        }
        else {
            LogHelper.warning("Can't add loot that is not a Block, Item, or ItemStack. Ignoring");
            return;
        }

        ChestGenHooks.getInfo(chest).addItem(new WeightedRandomChestContent(stack, min, max, weight));
    }
}
