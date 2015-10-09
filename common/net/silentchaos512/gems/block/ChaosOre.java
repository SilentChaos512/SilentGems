package net.silentchaos512.gems.block;

import cofh.api.modhelpers.ThermalExpansionHelper;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;
import net.silentchaos512.gems.configuration.Config;
import net.silentchaos512.gems.item.CraftingMaterial;
import net.silentchaos512.gems.lib.Names;
import thaumcraft.api.ThaumcraftApi;
import cpw.mods.fml.common.registry.GameRegistry;

public class ChaosOre extends BlockSG {

  public ChaosOre() {

    super(Material.rock);

    setHardness(4.0f);
    setResistance(15.0f);
    setStepSound(Block.soundTypeStone);
    setHarvestLevel("pickaxe", 3);

    setUnlocalizedName(Names.CHAOS_ORE);
  }

  @Override
  public void addOreDict() {

    OreDictionary.registerOre("oreChaos", this);
  }

  @Override
  public void addRecipes() {

    ItemStack chaosEssence = CraftingMaterial.getStack(Names.CHAOS_ESSENCE,
        Config.CHAOS_ESSENCE_PER_ORE);
    ItemStack chaosOre = new ItemStack(this);
    GameRegistry.addSmelting(this, chaosEssence, 0.5f);

    // Redstone furnace
    ThermalExpansionHelper.addFurnaceRecipe(1600, chaosOre, chaosEssence);

    // Pulverizer
    ItemStack chaosEssenceBonus = CraftingMaterial.getStack(Names.CHAOS_ESSENCE,
        2 * Config.CHAOS_ESSENCE_PER_ORE);
    ThermalExpansionHelper.addPulverizerRecipe(4000, chaosOre, chaosEssenceBonus);
    // TODO: SAG Mill?
  }

  @Override
  public void addThaumcraftStuff() {

    ThaumcraftApi.addSmeltingBonus(new ItemStack(this),
        CraftingMaterial.getStack(Names.CHAOS_ESSENCE_SHARD, 0));
  }
}
