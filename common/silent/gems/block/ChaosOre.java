package silent.gems.block;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;
import silent.gems.configuration.Config;
import silent.gems.core.registry.SRegistry;
import silent.gems.item.CraftingMaterial;
import silent.gems.lib.Names;
import thaumcraft.api.ThaumcraftApi;
import cpw.mods.fml.common.registry.GameRegistry;

public class ChaosOre extends BlockSG {

    public ChaosOre() {

        super(Material.rock);

        setHardness(3.0f);
        setResistance(5.0f);
        setStepSound(Block.soundTypeStone);
        setHarvestLevel("pickaxe", 2);

        setUnlocalizedName(Names.CHAOS_ORE);
    }

    @Override
    public void addOreDict() {

        OreDictionary.registerOre("oreChaos", this);
    }

    @Override
    public void addRecipes() {

        GameRegistry.addSmelting(this, new ItemStack(SRegistry.getItem(Names.CRAFTING_MATERIALS), Config.CHAOS_ESSENCE_PER_ORE.value, 3),
                0.5f);
    }

    @Override
    public void addThaumcraftStuff() {

        ThaumcraftApi.addSmeltingBonus(new ItemStack(this), CraftingMaterial.getStack(Names.CHAOS_ESSENCE_SHARD, 0));
    }
}
