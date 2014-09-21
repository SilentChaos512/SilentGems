package silent.gems.item;

import silent.gems.core.registry.SRegistry;
import silent.gems.item.armor.ArmorSG;
import silent.gems.item.tool.GemAxe;
import silent.gems.item.tool.GemBow;
import silent.gems.item.tool.GemHoe;
import silent.gems.item.tool.GemPickaxe;
import silent.gems.item.tool.GemShovel;
import silent.gems.item.tool.GemSickle;
import silent.gems.item.tool.GemSword;
import silent.gems.lib.EnumGem;
import silent.gems.lib.Names;
import silent.gems.material.ModMaterials;
import silent.gems.recipe.ChaosRuneRecipe;
import silent.gems.recipe.DecorateToolRecipe;
import silent.gems.recipe.EnchantToolRecipe;
import silent.gems.recipe.TorchBandolierRecipe;
import cpw.mods.fml.common.registry.GameRegistry;

public class ModItems {

	public static void init() {
		
		SRegistry.registerItem(Gem.class, Names.GEM_ITEM);
		SRegistry.registerItem(GemShard.class, Names.GEM_SHARD);
		SRegistry.registerItem(CraftingMaterial.class, Names.CRAFTING_MATERIALS);
//		SRegistry.registerItem(GemRod.class, Names.GEM_ROD);
		SRegistry.registerItem(FoodSG.class, Names.FOOD);
		SRegistry.registerItem(TorchBandolier.class, Names.TORCH_BANDOLIER);
		SRegistry.registerItem(TeleporterLinker.class, Names.TELEPORTER_LINKER);
		SRegistry.registerItem(FluffyPlantSeeds.class, Names.FLUFFY_SEED);
		SRegistry.registerItem(ReturnHome.class, Names.RETURN_HOME);
		SRegistry.registerItem(PetSummon.class, Names.SUMMON_PET);
		SRegistry.registerItem(EnchantToken.class, Names.ENCHANT_TOKEN);
		SRegistry.registerItem(ChaosRune.class, Names.CHAOS_RUNE);
		
		// Register chaos gems.
        for (int i = 0; i < EnumGem.all().length; ++i) {
            SRegistry.registerItem(ChaosGem.class, Names.CHAOS_GEM + i, new Object[] { i });
        }
        SRegistry.registerItem(ChaosGem.class, Names.CHAOS_GEM + ChaosGem.CHEATY_GEM_ID, new Object[] { ChaosGem.CHEATY_GEM_ID });
        
        // Register tools.
        int gem;
        Object[] params = new Object[] { null, 0, false }; // Constructor parameters
        for (int i = 0; i < 24; ++i) {
            boolean supercharged = i >= 12;
            gem = supercharged ? i - 12 : i;
            params[0] = EnumGem.values()[gem].getToolMaterial(supercharged);
            params[1] = gem;
            params[2] = supercharged;
            String s = gem + (supercharged ? "Plus" : "");
            SRegistry.registerItem(GemSword.class, "Sword" + s, params);
            SRegistry.registerItem(GemPickaxe.class, "Pickaxe" + s, params);
            SRegistry.registerItem(GemShovel.class, "Shovel" + s, params);
            SRegistry.registerItem(GemAxe.class, "Axe" + s, params);
            SRegistry.registerItem(GemHoe.class, "Hoe" + s, params);
            SRegistry.registerItem(GemSickle.class, "Sickle" + s, params);
//            if (i < 12) {
//                SRegistry.registerItem(GemBow.class, "Bow" + s, new Object[] { params[0], params[1] });
//            }
        }
        
        params[0] = ModMaterials.toolFish;
        params[1] = ModMaterials.FISH_GEM_ID;
        params[2] = false;
        SRegistry.registerItem(GemSword.class, "SwordFish", params);
        SRegistry.registerItem(GemPickaxe.class, "PickaxeFish", params);
        SRegistry.registerItem(GemShovel.class, "ShovelFish", params);
        SRegistry.registerItem(GemAxe.class, "AxeFish", params);
        SRegistry.registerItem(GemHoe.class, "HoeFish", params);
        SRegistry.registerItem(GemSickle.class, "SickleFish", params);
        
        // Register armor
        SRegistry.registerItem(ArmorSG.class, "CottonHelmet", new Object[] { ArmorSG.materialCotton, 0, 0, "CottonHelmet" });
        SRegistry.registerItem(ArmorSG.class, "CottonChestplate", new Object[] { ArmorSG.materialCotton, 0, 1, "CottonChestplate" });
        SRegistry.registerItem(ArmorSG.class, "CottonLeggings", new Object[] { ArmorSG.materialCotton, 0, 2, "CottonLeggings" });
        SRegistry.registerItem(ArmorSG.class, "CottonBoots", new Object[] { ArmorSG.materialCotton, 0, 3, "CottonBoots" });
        
        // Debug Item
        SRegistry.registerItem(DebugItem.class, Names.DEBUG_ITEM);
	}
	
    public static void initItemRecipes() {
        
        GameRegistry.addRecipe(new ChaosRuneRecipe());
        GameRegistry.addRecipe(new DecorateToolRecipe());
        GameRegistry.addRecipe(new EnchantToolRecipe());
        GameRegistry.addRecipe(new TorchBandolierRecipe());
    }

    public static void addRandomChestGenLoot() {

        // TODO Auto-generated method stub
        
    }

}
