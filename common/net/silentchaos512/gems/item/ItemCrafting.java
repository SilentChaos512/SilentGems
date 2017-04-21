package net.silentchaos512.gems.item;

import java.util.List;
import java.util.Map;

import com.google.common.collect.Maps;

import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.EnumHand;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.common.IFuelHandler;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;
import net.silentchaos512.gems.SilentGems;
import net.silentchaos512.gems.config.GemsConfig;
import net.silentchaos512.gems.lib.Names;
import net.silentchaos512.lib.item.ItemNamedSubtypesSorted;
import net.silentchaos512.lib.util.RecipeHelper;
import net.silentchaos512.lib.util.StackHelper;

public class ItemCrafting extends ItemNamedSubtypesSorted implements IFuelHandler {

  public static final String[] NAMES = { //
      Names.CHAOS_ESSENCE, Names.CHAOS_ESSENCE_PLUS, Names.CHAOS_ESSENCE_PLUS_2,
      Names.CHAOS_ESSENCE_SHARD, Names.ENDER_ESSENCE, Names.ENDER_ESSENCE_SHARD, Names.CHAOS_COAL,
      Names.STICK_IRON, Names.ORNATE_STICK_GOLD, Names.ORNATE_STICK_SILVER, Names.IRON_POTATO,
      Names.FLUFFY_FABRIC, Names.UPGRADE_BASE, Names.NAME_PLATE, Names.CHAOS_CORE,
      Names.MAGNIFYING_GLASS, Names.PLUME, Names.SHINY_PLUME, Names.ENDER_FROST, Names.NETHER_SHARD,
      Names.NETHER_CLUSTER, Names.GILDED_STRING, Names.YARN_BALL, Names.RAWHIDE_BONE,
      Names.ARMOR_LATTICE_MUNDANE, Names.ARMOR_LATTICE_REGULAR, Names.ARMOR_LATTICE_SUPER,
      Names.BLAZESTONE, Names.MYSTERY_GOO };

  public static final String[] SORTED_NAMES = { //
      Names.CHAOS_ESSENCE, Names.CHAOS_ESSENCE_PLUS, Names.CHAOS_ESSENCE_PLUS_2,
      Names.CHAOS_ESSENCE_SHARD, Names.ENDER_ESSENCE, Names.ENDER_FROST, Names.ENDER_ESSENCE_SHARD,
      Names.NETHER_SHARD, Names.NETHER_CLUSTER, Names.CHAOS_COAL, Names.STICK_IRON,
      Names.ORNATE_STICK_GOLD, Names.ORNATE_STICK_SILVER, Names.ARMOR_LATTICE_MUNDANE,
      Names.ARMOR_LATTICE_REGULAR, Names.ARMOR_LATTICE_SUPER, Names.GILDED_STRING, Names.BLAZESTONE,
      Names.MYSTERY_GOO, Names.CHAOS_CORE, Names.IRON_POTATO, Names.FLUFFY_FABRIC, Names.PLUME,
      Names.SHINY_PLUME, Names.YARN_BALL, Names.RAWHIDE_BONE, Names.MAGNIFYING_GLASS,
      Names.NAME_PLATE, Names.UPGRADE_BASE };

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
  public final ItemStack enderEssence = getStack(Names.ENDER_ESSENCE);
  public final ItemStack enderEssenceShard = getStack(Names.ENDER_ESSENCE_SHARD);
  public final ItemStack enderFrost = getStack(Names.ENDER_FROST);
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
  public final ItemStack toolRodGold = getStack(Names.ORNATE_STICK_GOLD);
  public final ItemStack toolRodIron = getStack(Names.STICK_IRON);
  public final ItemStack toolRodSilver = getStack(Names.ORNATE_STICK_SILVER);
  public final ItemStack upgradeBase = getStack(Names.UPGRADE_BASE);
  public final ItemStack yarnBall = getStack(Names.YARN_BALL);

  public final Map<Integer, IRecipe> guideRecipeMap = Maps.newHashMap();

  public ItemCrafting() {

    super(NAMES, SORTED_NAMES, SilentGems.MODID, Names.CRAFTING_MATERIAL);
    GameRegistry.registerFuelHandler(this);
  }

  @Override
  public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean advanced) {

    list.add(TextFormatting.GOLD
        + SilentGems.localizationHelper.getItemSubText(Names.CRAFTING_MATERIAL, "desc"));
    super.addInformation(stack, player, list, advanced);
  }

  @Override
  public void addRecipes() {

    IRecipe recipe;

    // Enriched Chaos Essence
    recipe = RecipeHelper.addSurroundOre(chaosEssenceEnriched, "dustGlowstone", "dustRedstone",
        "gemChaos");
    guideRecipeMap.put(chaosEssenceEnriched.getItemDamage(), recipe);

    // Crystallized Chaos Essence
    recipe = RecipeHelper.addSurroundOre(chaosEssenceCrystallized, enderEssence, netherShard,
        chaosEssenceEnriched);
    guideRecipeMap.put(chaosEssenceCrystallized.getItemDamage(), recipe);

    // Chaos Essence Shards
    RecipeHelper.addCompressionRecipe(chaosEssenceShard, chaosEssence, 9);
    guideRecipeMap.put(chaosEssenceShard.getItemDamage(), new ShapelessOreRecipe(
        new ItemStack(this, 9, chaosEssenceShard.getItemDamage()), chaosEssence));

    // Ender Essence Shards
    RecipeHelper.addCompressionRecipe(enderEssenceShard, enderEssence, 9);
    guideRecipeMap.put(enderEssenceShard.getItemDamage(), new ShapelessOreRecipe(
        new ItemStack(this, 9, enderEssenceShard.getItemDamage()), enderEssence));

    // Iron Rod
    recipe = new ShapedOreRecipe(getStack(Names.STICK_IRON, 8), "igi", "igi", "igi", 'i',
        "ingotIron", 'g', new ItemStack(ModItems.gemShard, 1, OreDictionary.WILDCARD_VALUE));
    GameRegistry.addRecipe(recipe);
    guideRecipeMap.put(toolRodIron.getItemDamage(), recipe);

    // Ornate Rod Gold
    recipe = new ShapedOreRecipe(getStack(Names.ORNATE_STICK_GOLD, 8), "ifi", "ici", "ifi", 'i',
        "ingotGold", 'f', "ingotIron", 'c', "gemChaos");
    GameRegistry.addRecipe(recipe);
    guideRecipeMap.put(toolRodGold.getItemDamage(), recipe);

    // Ornate Rod Silver
    recipe = new ShapedOreRecipe(getStack(Names.ORNATE_STICK_SILVER, 8), "ifi", "ici", "ifi", 'i',
        "ingotSilver", 'f', "ingotIron", 'c', "gemChaos");
    GameRegistry.addRecipe(recipe);
    guideRecipeMap.put(toolRodSilver.getItemDamage(), recipe);

    // Upgrade Base
    recipe = new ShapelessOreRecipe(getStack(Names.UPGRADE_BASE, 4), Items.FLINT, Items.FLINT,
        "plankWood", "stickWood");
    GameRegistry.addRecipe(recipe);
    guideRecipeMap.put(upgradeBase.getItemDamage(), recipe);

    // Chaos Coal
    recipe = RecipeHelper.addSurroundOre(getStack(Names.CHAOS_COAL, 8), "gemChaos", Items.COAL);
    RecipeHelper.addSurroundOre(getStack(Names.CHAOS_COAL, 4), "gemChaos",
        new ItemStack(Items.COAL, 1, 1));
    guideRecipeMap.put(chaosCoal.getItemDamage(), recipe);
    // Chaos Coal -> Torches
    GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(Blocks.TORCH, 16), "c", "s", 'c',
        chaosCoal, 's', "stickWood"));

    // Name Plate
    recipe = new ShapedOreRecipe(getStack(Names.NAME_PLATE, 4), "iii", "pcp", "iii", 'i',
        "ingotIron", 'p', "paper", 'c', "gemChaos");
    GameRegistry.addRecipe(recipe);
    guideRecipeMap.put(namePlate.getItemDamage(), recipe);

    // Chaos Core
    recipe = new ShapedOreRecipe(chaosCore, " c ", "cqc", " c ", 'c', chaosEssenceEnriched, 'q',
        "blockQuartz");
    GameRegistry.addRecipe(recipe);
    guideRecipeMap.put(chaosCore.getItemDamage(), recipe);

    // Magnifying Glass
    recipe = new ShapedOreRecipe(magnifyingGlass, " g ", "gpg", "rg ", 'g', "ingotGold", 'p',
        "paneGlass", 'r', toolRodGold);
    GameRegistry.addRecipe(recipe);
    guideRecipeMap.put(magnifyingGlass.getItemDamage(), recipe);

    // Plume
    recipe = RecipeHelper.addSurroundOre(plume,
        new ItemStack(ModItems.gemShard, 1, OreDictionary.WILDCARD_VALUE), "feather");
    guideRecipeMap.put(plume.getItemDamage(), recipe);

    // Shiny Plume
    recipe = RecipeHelper.addSurroundOre(shinyPlume, plume, "gemChaos", "ingotGold");
    guideRecipeMap.put(shinyPlume.getItemDamage(), recipe);

    // Ender Frost
    recipe = RecipeHelper.addSurround(enderFrost, enderEssence, Blocks.ICE);
    guideRecipeMap.put(enderFrost.getItemDamage(), recipe);

    // Gilded String
    recipe = new ShapedOreRecipe(new ItemStack(this, 3, gildedString.getItemDamage()), "gsg", "gsg",
        "gsg", 's', Items.STRING, 'g', "nuggetGold");
    GameRegistry.addRecipe(recipe);
    guideRecipeMap.put(gildedString.getItemDamage(), recipe);

    // Yarn Ball
    recipe = RecipeHelper.addSurround(yarnBall,
        new ItemStack(ModItems.gemShard, 1, OreDictionary.WILDCARD_VALUE), Items.STRING);
    guideRecipeMap.put(yarnBall.getItemDamage(), recipe);

    // Rawhide Bone
    recipe = new ShapedOreRecipe(rawhideBone, " l ", "lbl", " l ", 'l', Items.LEATHER, 'b',
        Items.BONE);
    GameRegistry.addRecipe(recipe);
    guideRecipeMap.put(rawhideBone.getItemDamage(), recipe);

    // Blazestone
    recipe = RecipeHelper.addSurroundOre(blazestone, "dustRedstone", Items.BLAZE_POWDER);
    guideRecipeMap.put(blazestone.getItemDamage(), recipe);

    // Nether shards and clusters
    ItemStack netherStar = new ItemStack(Items.NETHER_STAR);
    recipe = RecipeHelper.addSurroundOre(getStack(Names.NETHER_SHARD, 24), netherStar, chaosEssenceEnriched,
        enderEssence);
    GameRegistry.addShapedRecipe(netherCluster, "sss", "s s", "sss", 's', netherShard);
    GameRegistry.addShapelessRecipe(netherStar, netherCluster, netherCluster, netherCluster);
    guideRecipeMap.put(netherShard.getItemDamage(), recipe);

    // Armor Lattice
    recipeLatticeMundane = RecipeHelper.addSurroundOre(getStack(Names.ARMOR_LATTICE_MUNDANE, 24),
        "stickWood", "string", Items.FLINT);
    guideRecipeMap.put(armorLatticeMundane.getItemDamage(), recipeLatticeMundane);
    recipeLatticeRegular = RecipeHelper.addSurroundOre(getStack(Names.ARMOR_LATTICE_REGULAR, 24),
        fluffyFabric, "ingotIron",
        new ItemStack(ModItems.gemShard, 1, OreDictionary.WILDCARD_VALUE));
    guideRecipeMap.put(armorLatticeRegular.getItemDamage(), recipeLatticeRegular);
    recipeLatticeSuper = RecipeHelper.addSurroundOre(getStack(Names.ARMOR_LATTICE_SUPER, 24),
        "gemLapis", "gemDiamond", chaosEssenceEnriched);
    guideRecipeMap.put(armorLatticeSuper.getItemDamage(), recipeLatticeSuper);
  }

  public IRecipe recipeLatticeMundane, recipeLatticeRegular, recipeLatticeSuper;

  @Override
  public EnumRarity getRarity(ItemStack stack) {

    int meta = stack.getItemDamage();
    if (meta == getMetaFor(Names.CHAOS_ESSENCE_PLUS)) {
      return EnumRarity.RARE;
    }
    return super.getRarity(stack);
  }

  @Override
  public void addOreDict() {

    // Confirm vanilla oredict keys are working (can't remember what MC version they were added).
    if (!OreDictionary.doesOreNameExist("paper"))
      OreDictionary.registerOre("paper", Items.PAPER);
    if (!OreDictionary.doesOreNameExist("feather"))
      OreDictionary.registerOre("feather", Items.FEATHER);

    OreDictionary.registerOre("gemChaos", chaosEssence);
    OreDictionary.registerOre("nuggetChaos", chaosEssenceShard);
    OreDictionary.registerOre("gemEnderEssence", enderEssence);
    OreDictionary.registerOre("nuggetEnderEssence", enderEssenceShard);
    OreDictionary.registerOre("paper", fluffyFabric);
  }

  @Override
  public boolean itemInteractionForEntity(ItemStack stack, EntityPlayer player,
      EntityLivingBase target, EnumHand hand) {

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
        StackHelper.shrink(stack, 1);
        return true;
      }
    }

    // Other items have default behavior.
    return super.itemInteractionForEntity(stack, player, target, hand);
  }

  @Override
  public int getBurnTime(ItemStack fuel) {

    return fuel.getItem() == this && fuel.getItemDamage() == chaosCoal.getItemDamage()
        ? GemsConfig.BURN_TIME_CHAOS_COAL : 0;
  }
}
