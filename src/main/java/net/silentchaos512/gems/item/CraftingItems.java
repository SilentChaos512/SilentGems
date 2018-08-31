/*
 * Silent's Gems -- CraftingItems
 * Copyright (C) 2018 SilentChaos512
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation version 3
 * of the License.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package net.silentchaos512.gems.item;

import com.google.common.collect.Maps;
import lombok.Getter;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.oredict.OreDictionary;
import net.silentchaos512.gems.SilentGems;
import net.silentchaos512.gems.client.key.KeyTracker;
import net.silentchaos512.gems.config.GemsConfig;
import net.silentchaos512.gems.init.ModItems;
import net.silentchaos512.gems.lib.Names;
import net.silentchaos512.lib.registry.IAddRecipes;
import net.silentchaos512.lib.registry.ICustomModel;
import net.silentchaos512.lib.registry.RecipeMaker;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Map;

// Sort of pseudo-IEnumItems until 1.13...
public enum CraftingItems {
    CHAOS_ESSENCE(0, "chaosessence"),
    ENRICHED_CHAOS_ESSENCE(1, "chaosessenceplus"),
    CRYSTALLIZED_CHAOS_ESSENCE(2, "chaosessenceplus2"),
    CHAOS_ESSENCE_SHARD(3, "chaosessenceshard"),
    ENDER_ESSENCE(4, "enderessence"),
    ENDER_FROST(18, "enderfrost"),
    ENDER_ESSENCE_SHARD(5, "enderessenceshard"),
    CHAOS_IRON_UNFIRED(29, "chaosironunfired"),
    CHAOS_IRON(30, "chaosiron"),
    NETHER_SHARD(19, "nethershard"),
    NETHER_CLUSTER(20, "nethercluster"),
    ENDER_SLIMEBALL(31, "enderslimeball"),
    SOUL_SHELL(32, "soulshell"),
    CHAOS_COAL(6, "chaoscoal"),
    STONE_ROD(33, "stickstone"),
    IRON_ROD(7, "stickiron"),
    ORNATE_GOLD_ROD(8, "ornatestickgold"),
    ORNATE_SILVER_ROD(9, "ornatesticksilver"),
    ARMOR_LATTICE_MUNDANE(24, "armorlatticemundane"),
    ARMOR_LATTICE_REGULAR(25, "armorlatticeregular"),
    ARMOR_LATTICE_SUPER(26, "armorlatticesuper"),
    GILDED_STRING(21, "gildedstring"),
    BLAZESTONE(27, "blazestone"),
    MYSTERY_GOO(28, "mysterygoo"),
    IRON_POTATO(10, "ironpotato"),
    CHAOS_CORE(14, "chaoscore"),
    FLUFFY_FABRIC(11, "fluffyfabric"),
    PLUME(16, "plume"),
    SHINY_PLUME(17, "shinyplume"),
    YARN_BALL(22, "yarnball"),
    RAWHIDE_BONE(23, "rawhidebone"),
    MAGNIFYING_GLASS(15, "magnifyingglass"),
    NAME_PLATE(13, "nameplate"),
    UPGRADE_BASE(12, "upgradebase");

    @Getter
    private final int metadata;
    @Getter
    private final String name;

    CraftingItems(int metadata, String name) {
        this.metadata = metadata;
        this.name = name;
    }

    public Item getItem() {
        return ItemCrafting.INSTANCE;
    }

    public ItemStack getStack() {
        return this.getStack(1);
    }

    public ItemStack getStack(int count) {
        return new ItemStack(this.getItem(), count, this.getMetadata());
    }

    @Nullable
    public static CraftingItems byMetadata(int meta) {
        for (CraftingItems item : values())
            if (item.getMetadata() == meta)
                return item;
        return null;
    }

    public static class ItemCrafting extends Item implements IAddRecipes, ICustomModel {
        public static final ItemCrafting INSTANCE = new ItemCrafting();
        public static final Map<Integer, IRecipe> guideRecipeMap = Maps.newHashMap();
        public static IRecipe recipeLatticeMundane, recipeLatticeRegular, recipeLatticeSuper;

        private ItemCrafting() {
            this.setHasSubtypes(true);
        }

        @Override
        public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> items) {
            if (!this.isInCreativeTab(tab)) return;
            for (CraftingItems item : values())
                items.add(item.getStack());
        }

        @Override
        public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
            tooltip.add(TextFormatting.GOLD + SilentGems.i18n.itemSubText(Names.CRAFTING_MATERIAL, "desc"));

            if (KeyTracker.isShiftDown()) {
                String key = this.getTranslationKey(stack) + ".desc";
                if (SilentGems.i18n.hasKey(key))
                    tooltip.add(TextFormatting.ITALIC + SilentGems.i18n.translate(key));
            }
        }

        @Override
        public void registerModels() {
            for (CraftingItems item : values())
                SilentGems.registry.setModel(this, item.getMetadata(), item.getName());
        }

        @Override
        public boolean itemInteractionForEntity(ItemStack stack, EntityPlayer player, EntityLivingBase target, EnumHand hand) {
            // Name plates can rename mobs.
            if (stack.isItemEqual(NAME_PLATE.getStack())) {
                if (!stack.hasDisplayName()) {
                    return false;
                }
                if (target instanceof EntityLiving) {
                    EntityLiving entityLiving = (EntityLiving) target;
                    if (entityLiving.hasCustomName()
                            && entityLiving.getCustomNameTag().equals(stack.getDisplayName())) {
                        return false;
                    }
                    entityLiving.setCustomNameTag(stack.getDisplayName());
                    entityLiving.enablePersistence();
                    stack.shrink(1);
                    return true;
                }
            }

            // Other items have default behavior.
            return super.itemInteractionForEntity(stack, player, target, hand);
        }

        @Override
        public int getItemBurnTime(ItemStack fuel) {
            return fuel.getItem() == this && fuel.getItemDamage() == CHAOS_COAL.getMetadata()
                    ? GemsConfig.BURN_TIME_CHAOS_COAL
                    : 0;
        }

        @Override
        public String getTranslationKey(ItemStack stack) {
            CraftingItems item = byMetadata(stack.getItemDamage());
            return SilentGems.i18n.getKey("item", item != null ? item.getName() : "null");
        }

        @Override
        public void addOreDict() {
            if (!OreDictionary.doesOreNameExist("paper"))
                OreDictionary.registerOre("paper", Items.PAPER);
            if (!OreDictionary.doesOreNameExist("feather"))
                OreDictionary.registerOre("feather", Items.FEATHER);

            OreDictionary.registerOre("gemChaos", CHAOS_ESSENCE.getStack());
            OreDictionary.registerOre("nuggetChaos", CHAOS_ESSENCE_SHARD.getStack());
            OreDictionary.registerOre("gemEnderEssence", ENDER_ESSENCE.getStack());
            OreDictionary.registerOre("nuggetEnderEssence", ENDER_ESSENCE_SHARD.getStack());
            OreDictionary.registerOre("paper", FLUFFY_FABRIC.getStack());
            OreDictionary.registerOre("slimeball", ENDER_SLIMEBALL.getStack());
            OreDictionary.registerOre("rodStone", STONE_ROD.getStack());
        }

        @Override
        public void addRecipes(RecipeMaker recipes) {
            IRecipe recipe;

            ItemStack chaosEssenceEnriched = ENRICHED_CHAOS_ESSENCE.getStack();
            ItemStack chaosEssenceCrystallized = CRYSTALLIZED_CHAOS_ESSENCE.getStack();
            ItemStack enderEssence = ENDER_ESSENCE.getStack();
            ItemStack netherShard = NETHER_SHARD.getStack();
            ItemStack chaosEssenceShard = CHAOS_ESSENCE_SHARD.getStack();
            ItemStack chaosEssence = CHAOS_ESSENCE.getStack();
            ItemStack enderEssenceShard = ENDER_ESSENCE_SHARD.getStack();
            ItemStack toolRodIron = IRON_ROD.getStack();
            ItemStack toolRodGold = ORNATE_GOLD_ROD.getStack();
            ItemStack toolRodSilver = ORNATE_SILVER_ROD.getStack();
            ItemStack upgradeBase = UPGRADE_BASE.getStack();
            ItemStack chaosCoal = CHAOS_COAL.getStack();
            ItemStack namePlate = NAME_PLATE.getStack();
            ItemStack chaosCore = CHAOS_CORE.getStack();
            ItemStack magnifyingGlass = MAGNIFYING_GLASS.getStack();
            ItemStack plume = PLUME.getStack();
            ItemStack shinyPlume = SHINY_PLUME.getStack();
            ItemStack enderFrost = ENDER_FROST.getStack();
            ItemStack yarnBall = YARN_BALL.getStack();
            ItemStack rawhideBone = RAWHIDE_BONE.getStack();
            ItemStack blazestone = BLAZESTONE.getStack();
            ItemStack netherCluster = NETHER_CLUSTER.getStack();
            ItemStack fluffyFabric = FLUFFY_FABRIC.getStack();
            ItemStack chaosIronUnfired = CHAOS_IRON_UNFIRED.getStack();
            ItemStack chaosIron = CHAOS_IRON.getStack();
            ItemStack gildedString = GILDED_STRING.getStack();

            // Enriched Chaos Essence
            recipe = recipes.addSurroundOre("chaos_essence_enriched", chaosEssenceEnriched, "dustGlowstone", "dustRedstone", "gemChaos");
            guideRecipeMap.put(chaosEssenceEnriched.getItemDamage(), recipe);

            // Crystallized Chaos Essence
            recipe = recipes.addSurroundOre("chaos_essence_crystallized", chaosEssenceCrystallized, enderEssence, netherShard, chaosEssenceEnriched);
            guideRecipeMap.put(chaosEssenceCrystallized.getItemDamage(), recipe);

            // Chaos Essence Shards
            recipes.addCompression("chaos_essence", chaosEssenceShard, chaosEssence, 9);
            guideRecipeMap.put(chaosEssenceShard.getItemDamage(), recipes.makeShapelessOre(new ItemStack(this, 9, chaosEssenceShard.getItemDamage()), chaosEssence));

            // Ender Essence Shards
            recipes.addCompression("ender_essence", enderEssenceShard, enderEssence, 9);
            guideRecipeMap.put(enderEssenceShard.getItemDamage(), recipes.makeShapeless(new ItemStack(this, 9, enderEssenceShard.getItemDamage()), enderEssence));

            // Iron Rod
            recipe = recipes.addShapedOre("iron_rod", IRON_ROD.getStack(8), "igi", "igi", "igi", 'i', "ingotIron", 'g', new ItemStack(ModItems.gemShard, 1, OreDictionary.WILDCARD_VALUE));
            guideRecipeMap.put(toolRodIron.getItemDamage(), recipe);

            // Ornate Rod Gold
            recipe = recipes.addShapedOre("ornate_rod_gold", ORNATE_GOLD_ROD.getStack(8), "ifi", "ici", "ifi", 'i', "ingotGold", 'f', "ingotIron", 'c', "gemChaos");
            guideRecipeMap.put(toolRodGold.getItemDamage(), recipe);

            // Ornate Rod Silver
            if (OreDictionary.doesOreNameExist("ingotSilver")) {
                recipe = recipes.addShapedOre("ornate_rod_silver", ORNATE_SILVER_ROD.getStack(8), "ifi", "ici", "ifi", 'i', "ingotSilver", 'f', "ingotIron", 'c', "gemChaos");
                guideRecipeMap.put(toolRodSilver.getItemDamage(), recipe);
            }

            // Upgrade Base
            recipe = recipes.addShapelessOre("upgrade_base", UPGRADE_BASE.getStack(4), Items.FLINT, Items.FLINT, "plankWood", "stickWood");
            guideRecipeMap.put(upgradeBase.getItemDamage(), recipe);

            // Chaos Coal
            recipe = recipes.addSurroundOre("chaos_coal_0", CHAOS_COAL.getStack(8), "gemChaos", new ItemStack(Items.COAL));
            recipes.addSurroundOre("chaos_coal_1", CHAOS_COAL.getStack(4), "gemChaos", new ItemStack(Items.COAL, 1, 1));
            guideRecipeMap.put(chaosCoal.getItemDamage(), recipe);
            // Chaos Coal -> Torches
            recipes.addShapedOre("torches_chaos_coal", new ItemStack(Blocks.TORCH, 16), "c", "s", 'c', chaosCoal, 's', "stickWood");

            // Name Plate
            recipe = recipes.addShapedOre("name_plate", NAME_PLATE.getStack(4), "iii", "pcp", "iii", 'i', "ingotIron", 'p', "paper", 'c', "gemChaos");
            guideRecipeMap.put(namePlate.getItemDamage(), recipe);

            // Chaos Core
            recipe = recipes.addShapedOre("chaos_core", chaosCore, " c ", "cqc", " c ", 'c', chaosEssenceEnriched, 'q', "blockQuartz");
            guideRecipeMap.put(chaosCore.getItemDamage(), recipe);

            // Magnifying Glass
            recipe = recipes.addShapedOre("magnifying_glass", magnifyingGlass, " g ", "gpg", "rg ", 'g', "ingotGold", 'p', "paneGlass", 'r', toolRodGold);
            guideRecipeMap.put(magnifyingGlass.getItemDamage(), recipe);

            // Plume
            recipe = recipes.addSurroundOre("plume", plume, new ItemStack(ModItems.gemShard, 1, OreDictionary.WILDCARD_VALUE), "feather");
            guideRecipeMap.put(plume.getItemDamage(), recipe);

            // Shiny Plume
            recipe = recipes.addSurroundOre("shiny_plume", shinyPlume, plume, "gemChaos", "ingotGold");
            guideRecipeMap.put(shinyPlume.getItemDamage(), recipe);

            // Ender Frost
            recipe = recipes.addSurround("ender_frost", enderFrost, enderEssence, Blocks.ICE);
            guideRecipeMap.put(enderFrost.getItemDamage(), recipe);

            // Gilded String
            recipe = recipes.addShapedOre("gilded_string", new ItemStack(this, 3, gildedString.getItemDamage()), "gsg", "gsg", "gsg", 's', Items.STRING, 'g', "nuggetGold");
            guideRecipeMap.put(gildedString.getItemDamage(), recipe);

            // Yarn Ball
            recipe = recipes.addSurround("yarn_ball", yarnBall, new ItemStack(ModItems.gemShard, 1, OreDictionary.WILDCARD_VALUE), Items.STRING);
            guideRecipeMap.put(yarnBall.getItemDamage(), recipe);

            // Rawhide Bone
            recipe = recipes.addShapedOre("rawhide_bone", rawhideBone, " l ", "lbl", " l ", 'l', Items.LEATHER, 'b', Items.BONE);
            guideRecipeMap.put(rawhideBone.getItemDamage(), recipe);

            // Blazestone
            recipe = recipes.addSurroundOre("blazestone", blazestone, "dustRedstone", Items.BLAZE_POWDER);
            guideRecipeMap.put(blazestone.getItemDamage(), recipe);

            // Nether shards and clusters
            ItemStack netherStar = new ItemStack(Items.NETHER_STAR);
            recipe = recipes.addSurroundOre("nether_shard", NETHER_SHARD.getStack(24), netherStar, chaosEssenceEnriched, enderEssence);
            guideRecipeMap.put(netherShard.getItemDamage(), recipe);
            recipe = recipes.addShaped("nether_cluster", netherCluster, "sss", "s s", "sss", 's', netherShard);
            guideRecipeMap.put(netherCluster.getItemDamage(), recipe);
            recipes.addShapeless("nether_star", netherStar, netherCluster, netherCluster, netherCluster);

            // Armor Lattice
            recipeLatticeMundane = recipes.addSurroundOre("armor_lattice_mundane", ARMOR_LATTICE_MUNDANE.getStack(24), "stickWood", "string", Items.FLINT);
            guideRecipeMap.put(ARMOR_LATTICE_MUNDANE.getMetadata(), recipeLatticeMundane);
            recipeLatticeRegular = recipes.addSurroundOre("armor_lattice_regular", ARMOR_LATTICE_REGULAR.getStack(24), fluffyFabric, "ingotIron", new ItemStack(ModItems.gemShard, 1, OreDictionary.WILDCARD_VALUE));
            guideRecipeMap.put(ARMOR_LATTICE_REGULAR.getMetadata(), recipeLatticeRegular);
            recipeLatticeSuper = recipes.addSurroundOre("armor_lattice_super", ARMOR_LATTICE_SUPER.getStack(24), "gemLapis", "gemDiamond", chaosEssenceEnriched);
            guideRecipeMap.put(ARMOR_LATTICE_SUPER.getMetadata(), recipeLatticeSuper);

            // Chaos Iron
            recipe = recipes.addShapedOre("chaos_iron_unfired", chaosIronUnfired, "c", "i", "c", 'c', "gemChaos", 'i', "ingotIron");
            guideRecipeMap.put(chaosIronUnfired.getItemDamage(), recipe);
            recipes.addSmelting(chaosIronUnfired, chaosIron, 0.4f);

            // Soul Shell
            recipe = recipes.addShapedOre("soul_shell", SOUL_SHELL.getStack(2), " g ", "gdg", " g ", 'g', "blockGlass", 'd', "gemDiamond");
            guideRecipeMap.put(SOUL_SHELL.getMetadata(), recipe);

            // Stone Rods
            recipe = recipes.addShapedOre("stone_rod", STONE_ROD.getStack(4), "s", "s", 's', "cobblestone");
            guideRecipeMap.put(STONE_ROD.getMetadata(), recipe);
        }
    }
}
