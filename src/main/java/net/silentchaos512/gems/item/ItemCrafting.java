package net.silentchaos512.gems.item;

import com.google.common.collect.Maps;
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
import net.minecraft.util.math.MathHelper;
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

public class ItemCrafting extends Item implements IAddRecipes, ICustomModel {
    public static final String[] NAMES = { //
            Names.CHAOS_ESSENCE, Names.CHAOS_ESSENCE_PLUS, Names.CHAOS_ESSENCE_PLUS_2,
            Names.CHAOS_ESSENCE_SHARD, Names.ENDER_ESSENCE, Names.ENDER_ESSENCE_SHARD, Names.CHAOS_COAL,
            Names.STICK_IRON, Names.ORNATE_STICK_GOLD, Names.ORNATE_STICK_SILVER, Names.IRON_POTATO,
            Names.FLUFFY_FABRIC, Names.UPGRADE_BASE, Names.NAME_PLATE, Names.CHAOS_CORE,
            Names.MAGNIFYING_GLASS, Names.PLUME, Names.SHINY_PLUME, Names.ENDER_FROST, Names.NETHER_SHARD,
            Names.NETHER_CLUSTER, Names.GILDED_STRING, Names.YARN_BALL, Names.RAWHIDE_BONE,
            Names.ARMOR_LATTICE_MUNDANE, Names.ARMOR_LATTICE_REGULAR, Names.ARMOR_LATTICE_SUPER,
            Names.BLAZESTONE, Names.MYSTERY_GOO, Names.CHAOS_IRON_UNFIRED, Names.CHAOS_IRON,
            Names.ENDER_SLIMEBALL, Names.SOUL_SHELL, Names.STICK_STONE};

    public static final String[] SORTED_NAMES = { //
            Names.CHAOS_ESSENCE, Names.CHAOS_ESSENCE_PLUS, Names.CHAOS_ESSENCE_PLUS_2,
            Names.CHAOS_ESSENCE_SHARD, Names.ENDER_ESSENCE, Names.ENDER_FROST, Names.ENDER_ESSENCE_SHARD,
            Names.CHAOS_IRON_UNFIRED, Names.CHAOS_IRON, Names.NETHER_SHARD, Names.NETHER_CLUSTER,
            Names.ENDER_SLIMEBALL, Names.SOUL_SHELL, Names.CHAOS_COAL, Names.STICK_STONE,
            Names.STICK_IRON, Names.ORNATE_STICK_GOLD, Names.ORNATE_STICK_SILVER,
            Names.ARMOR_LATTICE_MUNDANE, Names.ARMOR_LATTICE_REGULAR, Names.ARMOR_LATTICE_SUPER,
            Names.GILDED_STRING, Names.BLAZESTONE, Names.MYSTERY_GOO, Names.IRON_POTATO, Names.CHAOS_CORE,
            Names.FLUFFY_FABRIC, Names.PLUME, Names.SHINY_PLUME, Names.YARN_BALL, Names.RAWHIDE_BONE,
            Names.MAGNIFYING_GLASS, Names.NAME_PLATE, Names.UPGRADE_BASE};

    public final ItemStack armorLatticeMundane = getStack(Names.ARMOR_LATTICE_MUNDANE);
    public final ItemStack armorLatticeRegular = getStack(Names.ARMOR_LATTICE_REGULAR);
    public final ItemStack armorLatticeSuper = getStack(Names.ARMOR_LATTICE_SUPER);
    public final ItemStack blazestone = getStack(Names.BLAZESTONE);
    public final ItemStack chaosCoal = getStack(Names.CHAOS_COAL);
    public final ItemStack chaosCore = getStack(Names.CHAOS_CORE);
    public final ItemStack chaosEssence = getStack(Names.CHAOS_ESSENCE);
    public final ItemStack chaosEssenceEnriched = getStack(Names.CHAOS_ESSENCE_PLUS);
    public final ItemStack chaosEssenceCrystallized = getStack(Names.CHAOS_ESSENCE_PLUS_2);
    public final ItemStack chaosEssenceShard = getStack(Names.CHAOS_ESSENCE_SHARD);
    public final ItemStack chaosIron = getStack(Names.CHAOS_IRON);
    public final ItemStack chaosIronUnfired = getStack(Names.CHAOS_IRON_UNFIRED);
    public final ItemStack enderEssence = getStack(Names.ENDER_ESSENCE);
    public final ItemStack enderEssenceShard = getStack(Names.ENDER_ESSENCE_SHARD);
    public final ItemStack enderFrost = getStack(Names.ENDER_FROST);
    public final ItemStack enderSlimeBall = getStack(Names.ENDER_SLIMEBALL);
    public final ItemStack fluffyFabric = getStack(Names.FLUFFY_FABRIC);
    public final ItemStack gildedString = getStack(Names.GILDED_STRING);
    public final ItemStack ironPotato = getStack(Names.IRON_POTATO);
    public final ItemStack magnifyingGlass = getStack(Names.MAGNIFYING_GLASS);
    public final ItemStack namePlate = getStack(Names.NAME_PLATE);
    public final ItemStack netherCluster = getStack(Names.NETHER_CLUSTER);
    public final ItemStack netherShard = getStack(Names.NETHER_SHARD);
    public final ItemStack plume = getStack(Names.PLUME);
    public final ItemStack rawhideBone = getStack(Names.RAWHIDE_BONE);
    public final ItemStack shinyPlume = getStack(Names.SHINY_PLUME);
    public final ItemStack soulShell = getStack(Names.SOUL_SHELL);
    public final ItemStack toolRodGold = getStack(Names.ORNATE_STICK_GOLD);
    public final ItemStack toolRodIron = getStack(Names.STICK_IRON);
    public final ItemStack toolRodSilver = getStack(Names.ORNATE_STICK_SILVER);
    public final ItemStack toolRodStone = getStack(Names.STICK_STONE);
    public final ItemStack upgradeBase = getStack(Names.UPGRADE_BASE);
    public final ItemStack yarnBall = getStack(Names.YARN_BALL);

    public final Map<Integer, IRecipe> guideRecipeMap = Maps.newHashMap();

    public ItemStack getStack(String name) {
        return getStack(name, 1);
    }

    public ItemStack getStack(String name, int amount) {
        for (int i = 0; i < NAMES.length; ++i) {
            if (NAMES[i].equalsIgnoreCase(name)) {
                return new ItemStack(this, amount, i);
            }
        }
        return ItemStack.EMPTY;
    }

    @Override
    public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> items) {
        if (!isInCreativeTab(tab)) return;
        for (String name : SORTED_NAMES) {
            items.add(getStack(name));
        }
    }

    @Override
    public void registerModels() {
        for (int i = 0; i < NAMES.length; ++i) {
            SilentGems.registry.setModel(this, i, NAMES[i]);
        }
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World world, List<String> list, ITooltipFlag flag) {
        list.add(TextFormatting.GOLD + SilentGems.i18n.itemSubText(Names.CRAFTING_MATERIAL, "desc"));

        if (KeyTracker.isShiftDown()) {
            String key = this.getTranslationKey(stack) + ".desc";
            if (SilentGems.i18n.hasKey(key))
                list.add(TextFormatting.ITALIC + SilentGems.i18n.translate(key));
        }
    }

    @Override
    public void addRecipes(RecipeMaker recipes) {
        IRecipe recipe;

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
        recipe = recipes.addShapedOre("iron_rod", getStack(Names.STICK_IRON, 8), "igi", "igi", "igi", 'i', "ingotIron", 'g', new ItemStack(ModItems.gemShard, 1, OreDictionary.WILDCARD_VALUE));
        guideRecipeMap.put(toolRodIron.getItemDamage(), recipe);

        // Ornate Rod Gold
        recipe = recipes.addShapedOre("ornate_rod_gold", getStack(Names.ORNATE_STICK_GOLD, 8), "ifi", "ici", "ifi", 'i', "ingotGold", 'f', "ingotIron", 'c', "gemChaos");
        guideRecipeMap.put(toolRodGold.getItemDamage(), recipe);

        // Ornate Rod Silver
        if (OreDictionary.doesOreNameExist("ingotSilver")) {
            recipe = recipes.addShapedOre("ornate_rod_silver", getStack(Names.ORNATE_STICK_SILVER, 8), "ifi", "ici", "ifi", 'i', "ingotSilver", 'f', "ingotIron", 'c', "gemChaos");
            guideRecipeMap.put(toolRodSilver.getItemDamage(), recipe);
        }

        // Upgrade Base
        recipe = recipes.addShapelessOre("upgrade_base", getStack(Names.UPGRADE_BASE, 4), Items.FLINT, Items.FLINT, "plankWood", "stickWood");
        guideRecipeMap.put(upgradeBase.getItemDamage(), recipe);

        // Chaos Coal
        recipe = recipes.addSurroundOre("chaos_coal_0", getStack(Names.CHAOS_COAL, 8), "gemChaos", new ItemStack(Items.COAL));
        recipes.addSurroundOre("chaos_coal_1", getStack(Names.CHAOS_COAL, 4), "gemChaos", new ItemStack(Items.COAL, 1, 1));
        guideRecipeMap.put(chaosCoal.getItemDamage(), recipe);
        // Chaos Coal -> Torches
        recipes.addShapedOre("torches_chaos_coal", new ItemStack(Blocks.TORCH, 16), "c", "s", 'c', chaosCoal, 's', "stickWood");

        // Name Plate
        recipe = recipes.addShapedOre("name_plate", getStack(Names.NAME_PLATE, 4), "iii", "pcp", "iii", 'i', "ingotIron", 'p', "paper", 'c', "gemChaos");
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
        recipe = recipes.addSurroundOre("nether_shard", getStack(Names.NETHER_SHARD, 24), netherStar, chaosEssenceEnriched, enderEssence);
        guideRecipeMap.put(netherShard.getItemDamage(), recipe);
        recipe = recipes.addShaped("nether_cluster", netherCluster, "sss", "s s", "sss", 's', netherShard);
        guideRecipeMap.put(netherCluster.getItemDamage(), recipe);
        recipes.addShapeless("nether_star", netherStar, netherCluster, netherCluster, netherCluster);

        // Armor Lattice
        recipeLatticeMundane = recipes.addSurroundOre("armor_lattice_mundane", getStack(Names.ARMOR_LATTICE_MUNDANE, 24), "stickWood", "string", Items.FLINT);
        guideRecipeMap.put(armorLatticeMundane.getItemDamage(), recipeLatticeMundane);
        recipeLatticeRegular = recipes.addSurroundOre("armor_lattice_regular", getStack(Names.ARMOR_LATTICE_REGULAR, 24), fluffyFabric, "ingotIron", new ItemStack(ModItems.gemShard, 1, OreDictionary.WILDCARD_VALUE));
        guideRecipeMap.put(armorLatticeRegular.getItemDamage(), recipeLatticeRegular);
        recipeLatticeSuper = recipes.addSurroundOre("armor_lattice_super", getStack(Names.ARMOR_LATTICE_SUPER, 24), "gemLapis", "gemDiamond", chaosEssenceEnriched);
        guideRecipeMap.put(armorLatticeSuper.getItemDamage(), recipeLatticeSuper);

        // Chaos Iron
        recipe = recipes.addShapedOre("chaos_iron_unfired", chaosIronUnfired, "c", "i", "c", 'c', "gemChaos", 'i', "ingotIron");
        guideRecipeMap.put(chaosIronUnfired.getItemDamage(), recipe);
        recipes.addSmelting(chaosIronUnfired, chaosIron, 0.4f);

        // Soul Shell
        recipe = recipes.addShapedOre("soul_shell", getStack(Names.SOUL_SHELL, 2), " g ", "gdg", " g ", 'g', "blockGlass", 'd', "gemDiamond");
        guideRecipeMap.put(soulShell.getItemDamage(), recipe);

        // Stone Rods
        recipe = recipes.addShapedOre("stone_rod", getStack(Names.STICK_STONE, 4), "s", "s", 's', "cobblestone");
        guideRecipeMap.put(toolRodStone.getItemDamage(), recipe);
    }

    public IRecipe recipeLatticeMundane, recipeLatticeRegular, recipeLatticeSuper;

    @Override
    public void addOreDict() {
        if (!OreDictionary.doesOreNameExist("paper"))
            OreDictionary.registerOre("paper", Items.PAPER);
        if (!OreDictionary.doesOreNameExist("feather"))
            OreDictionary.registerOre("feather", Items.FEATHER);

        OreDictionary.registerOre("gemChaos", chaosEssence);
        OreDictionary.registerOre("nuggetChaos", chaosEssenceShard);
        OreDictionary.registerOre("gemEnderEssence", enderEssence);
        OreDictionary.registerOre("nuggetEnderEssence", enderEssenceShard);
        OreDictionary.registerOre("paper", fluffyFabric);
        OreDictionary.registerOre("slimeball", enderSlimeBall);
        OreDictionary.registerOre("rodStone", toolRodStone);
    }

    @Override
    public boolean itemInteractionForEntity(ItemStack stack, EntityPlayer player, EntityLivingBase target, EnumHand hand) {
        // Name plates can rename mobs.
        if (stack.isItemEqual(namePlate)) {
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
        return fuel.getItem() == this && fuel.getItemDamage() == chaosCoal.getItemDamage()
                ? GemsConfig.BURN_TIME_CHAOS_COAL
                : 0;
    }

    @Override
    public String getTranslationKey(ItemStack stack) {
        return "item." + SilentGems.MODID + "." + NAMES[MathHelper.clamp(stack.getItemDamage(), 0, NAMES.length - 1)];
    }
}
