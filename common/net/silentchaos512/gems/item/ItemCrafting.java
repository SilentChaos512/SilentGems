package net.silentchaos512.gems.item;

import java.util.List;

import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
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

public class ItemCrafting extends ItemNamedSubtypesSorted implements IFuelHandler {

  public static final String[] NAMES = { //
      Names.CHAOS_ESSENCE, Names.CHAOS_ESSENCE_PLUS, Names.CHAOS_ESSENCE_PLUS_2,
      Names.CHAOS_ESSENCE_SHARD, Names.ENDER_ESSENCE, Names.ENDER_ESSENCE_SHARD, Names.CHAOS_COAL,
      Names.STICK_IRON, Names.ORNATE_STICK_GOLD, Names.ORNATE_STICK_SILVER, Names.IRON_POTATO,
      Names.FLUFFY_FABRIC, Names.UPGRADE_BASE, Names.NAME_PLATE, Names.CHAOS_CORE,
      Names.MAGNIFYING_GLASS, Names.PLUME, Names.SHINY_PLUME, Names.ENDER_FROST, Names.NETHER_SHARD,
      Names.NETHER_CLUSTER, Names.GILDED_STRING, Names.YARN_BALL, Names.RAWHIDE_BONE,
      Names.ARMOR_LATTICE_MUNDANE, Names.ARMOR_LATTICE_REGULAR, Names.ARMOR_LATTICE_SUPER,
      Names.BLAZESTONE };

  public static final String[] SORTED_NAMES = { //
      Names.CHAOS_ESSENCE, Names.CHAOS_ESSENCE_PLUS, Names.CHAOS_ESSENCE_PLUS_2,
      Names.CHAOS_ESSENCE_SHARD, Names.ENDER_ESSENCE, Names.ENDER_FROST, Names.ENDER_ESSENCE_SHARD,
      Names.NETHER_SHARD, Names.NETHER_CLUSTER, Names.CHAOS_COAL, Names.STICK_IRON,
      Names.ORNATE_STICK_GOLD, Names.ORNATE_STICK_SILVER, Names.ARMOR_LATTICE_MUNDANE,
      Names.ARMOR_LATTICE_REGULAR, Names.ARMOR_LATTICE_SUPER, Names.GILDED_STRING, Names.BLAZESTONE,
      Names.CHAOS_CORE, Names.IRON_POTATO, Names.FLUFFY_FABRIC, Names.PLUME, Names.SHINY_PLUME,
      Names.YARN_BALL, Names.RAWHIDE_BONE, Names.MAGNIFYING_GLASS, Names.NAME_PLATE,
      Names.UPGRADE_BASE };

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

  public ItemCrafting() {

    super(NAMES, SORTED_NAMES, SilentGems.MOD_ID, Names.CRAFTING_MATERIAL);
    GameRegistry.registerFuelHandler(this);
  }

  @Override
  public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean advanced) {

    // TODO
    super.addInformation(stack, player, list, advanced);
  }

  @Override
  public void addRecipes() {

    // Enriched Chaos Essence
    RecipeHelper.addSurroundOre(chaosEssenceEnriched, "dustGlowstone", "dustRedstone", "gemChaos");
    // Crystallized Chaos Essence
    RecipeHelper.addSurroundOre(chaosEssenceCrystallized, enderEssence, netherShard,
        chaosEssenceEnriched);
    // Chaos Essence Shards
    RecipeHelper.addCompressionRecipe(chaosEssenceShard, chaosEssence, 9);
    // Ender Essence Shards
    RecipeHelper.addCompressionRecipe(enderEssenceShard, enderEssence, 9);
    // Iron Rod
    GameRegistry.addRecipe(new ShapedOreRecipe(getStack(Names.STICK_IRON, 8), "igi", "igi", "igi",
        'i', "ingotIron", 'g', new ItemStack(ModItems.gemShard, 1, OreDictionary.WILDCARD_VALUE)));
    // Ornate Rod Gold
    GameRegistry.addRecipe(new ShapedOreRecipe(getStack(Names.ORNATE_STICK_GOLD, 8), "ifi", "ici",
        "ifi", 'i', "ingotGold", 'f', "ingotIron", 'c', "gemChaos"));
    // Ornate Rod Silver
    GameRegistry.addRecipe(new ShapedOreRecipe(getStack(Names.ORNATE_STICK_SILVER, 8), "ifi", "ici",
        "ifi", 'i', "ingotSilver", 'f', "ingotIron", 'c', "gemChaos"));
    // Upgrade Base
    GameRegistry.addRecipe(new ShapelessOreRecipe(getStack(Names.UPGRADE_BASE, 4), Items.FLINT,
        Items.FLINT, "plankWood", "stickWood"));
    // Chaos Coal
    RecipeHelper.addSurroundOre(getStack(Names.CHAOS_COAL, 8), "gemChaos", Items.COAL);
    RecipeHelper.addSurroundOre(getStack(Names.CHAOS_COAL, 4), "gemChaos",
        new ItemStack(Items.COAL, 1, 1));
    // Chaos Coal -> Torches
    GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(Blocks.TORCH, 16), "c", "s", 'c',
        chaosCoal, 's', "stickWood"));
    // Name Plate
    for (Object paper : new Object[] { "paper", Items.PAPER }) {
      GameRegistry.addRecipe(new ShapedOreRecipe(getStack(Names.NAME_PLATE, 4), "iii", "pcp", "iii",
          'i', "ingotIron", 'p', paper, 'c', "gemChaos"));
    }
    // Chaos Core
    GameRegistry.addRecipe(new ShapedOreRecipe(chaosCore, " c ", "cqc", " c ", 'c',
        chaosEssenceEnriched, 'q', "blockQuartz"));
    // Magnifying Glass
    GameRegistry.addRecipe(new ShapedOreRecipe(magnifyingGlass, " g ", "gpg", "rg ", 'g',
        "ingotGold", 'p', "paneGlass", 'r', toolRodGold));
    // Plume
    for (Object feather : new Object[] { "feather", Items.FEATHER }) {
      RecipeHelper.addSurroundOre(plume,
          new ItemStack(ModItems.gemShard, 1, OreDictionary.WILDCARD_VALUE), feather);
    }
    // Shiny Plume
    RecipeHelper.addSurroundOre(shinyPlume, plume, "gemChaos", "ingotGold");
    // Ender Frost
    RecipeHelper.addSurround(enderFrost, enderEssence, Blocks.ICE);
    // Gilded String
    GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(this, 3, gildedString.getItemDamage()),
        "gsg", "gsg", "gsg", 's', Items.STRING, 'g', "nuggetGold"));
    // Yarn Ball
    RecipeHelper.addSurround(yarnBall,
        new ItemStack(ModItems.gemShard, 1, OreDictionary.WILDCARD_VALUE), Items.STRING);
    // Rawhide Bone
    GameRegistry.addShapedRecipe(rawhideBone, " l ", "lbl", " l ", 'l', Items.LEATHER, 'b',
        Items.BONE);
    // Blazestone
    RecipeHelper.addSurroundOre(blazestone, "dustRedstone", Items.BLAZE_POWDER);

    // Nether shards and clusters
    ItemStack netherStar = new ItemStack(Items.NETHER_STAR);
    RecipeHelper.addSurroundOre(getStack(Names.NETHER_SHARD, 24), netherStar, chaosEssenceEnriched,
        enderEssence);
    GameRegistry.addShapedRecipe(netherCluster, "sss", "s s", "sss", 's', netherShard);
    GameRegistry.addShapelessRecipe(netherStar, netherCluster, netherCluster, netherCluster);

    // Armor Lattice
    RecipeHelper.addSurroundOre(getStack(Names.ARMOR_LATTICE_MUNDANE, 24), "stickWood", "leather",
        Items.FLINT);
    RecipeHelper.addSurroundOre(getStack(Names.ARMOR_LATTICE_REGULAR, 24), fluffyFabric,
        "ingotIron", new ItemStack(ModItems.gemShard, 1, OreDictionary.WILDCARD_VALUE));
    RecipeHelper.addSurroundOre(getStack(Names.ARMOR_LATTICE_SUPER, 24), "gemLapis", "gemDiamond",
        chaosEssenceEnriched);
  }

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
        --stack.stackSize;
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
