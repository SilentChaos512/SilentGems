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
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;
import net.silentchaos512.gems.SilentGems;
import net.silentchaos512.gems.lib.Names;
import net.silentchaos512.lib.item.ItemNamedSubtypesSorted;
import net.silentchaos512.lib.util.RecipeHelper;

public class ItemCrafting extends ItemNamedSubtypesSorted {

  public static final String[] NAMES = new String[] { //
      Names.CHAOS_ESSENCE, Names.CHAOS_ESSENCE_PLUS, Names.CHAOS_ESSENCE_PLUS_2,
      Names.CHAOS_ESSENCE_SHARD, Names.ENDER_ESSENCE, Names.ENDER_ESSENCE_SHARD, Names.CHAOS_COAL,
      Names.STICK_IRON, Names.ORNATE_STICK_GOLD, Names.ORNATE_STICK_SILVER, Names.IRON_POTATO,
      Names.FLUFFY_FABRIC, Names.UPGRADE_BASE, Names.NAME_PLATE, Names.CHAOS_CORE,
      Names.MAGNIFYING_GLASS };

  public static final String[] SORTED_NAMES = new String[] { //
      Names.CHAOS_ESSENCE, Names.CHAOS_ESSENCE_PLUS, Names.CHAOS_ESSENCE_PLUS_2,
      Names.CHAOS_ESSENCE_SHARD, Names.ENDER_ESSENCE, Names.ENDER_ESSENCE_SHARD, Names.CHAOS_COAL,
      Names.STICK_IRON, Names.ORNATE_STICK_GOLD, Names.ORNATE_STICK_SILVER, Names.CHAOS_CORE,
      Names.IRON_POTATO, Names.FLUFFY_FABRIC, Names.MAGNIFYING_GLASS, Names.NAME_PLATE,
      Names.UPGRADE_BASE };

  public ItemCrafting() {

    super(NAMES, SORTED_NAMES, SilentGems.MOD_ID, Names.CRAFTING_MATERIAL);
  }

  @Override
  public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean advanced) {

    // TODO
    super.addInformation(stack, player, list, advanced);
  }

  @Override
  public void addRecipes() {

    // Enriched Chaos Essence
    RecipeHelper.addSurroundOre(getStack(Names.CHAOS_ESSENCE_PLUS), "dustGlowstone", "dustRedstone",
        "gemChaos");
    // Chaos Essence Shards
    RecipeHelper.addCompressionRecipe(getStack(Names.CHAOS_ESSENCE_SHARD),
        getStack(Names.CHAOS_ESSENCE), 9);
    // Ender Essence Shards
    RecipeHelper.addCompressionRecipe(getStack(Names.ENDER_ESSENCE_SHARD),
        getStack(Names.ENDER_ESSENCE), 9);
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
    GameRegistry.addRecipe(new ShapelessOreRecipe(getStack(Names.UPGRADE_BASE, 4), Items.flint,
        Items.flint, "plankWood", "stickWood"));
    // Chaos Coal
    RecipeHelper.addSurroundOre(getStack(Names.CHAOS_COAL, 8), "gemChaos", Items.coal);
    RecipeHelper.addSurroundOre(getStack(Names.CHAOS_COAL, 4), "gemChaos",
        new ItemStack(Items.coal, 1, 1));
    // Chaos Coal -> Torches
    GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(Blocks.torch, 16), "c", "s", 'c',
        getStack(Names.CHAOS_COAL), 's', "stickWood"));
    // Name Plate
    GameRegistry.addRecipe(new ShapedOreRecipe(getStack(Names.NAME_PLATE, 4), "iii", "pcp", "iii",
        'i', "ingotIron", 'p', Items.paper, 'c', "gemChaos"));
    // Chaos Core
    GameRegistry.addRecipe(new ShapedOreRecipe(getStack(Names.CHAOS_CORE), " c ", "cqc", " c ", 'c',
        getStack(Names.CHAOS_ESSENCE_PLUS), 'q', "blockQuartz"));
    // Magnifying Glass
    GameRegistry.addRecipe(new ShapedOreRecipe(getStack(Names.MAGNIFYING_GLASS), " g ", "gpg",
        "rg ", 'g', "ingotGold", 'p', "paneGlass", 'r', getStack(Names.ORNATE_STICK_GOLD)));
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

    OreDictionary.registerOre("gemChaos", getStack(Names.CHAOS_ESSENCE));
    OreDictionary.registerOre("nuggetChaos", getStack(Names.CHAOS_ESSENCE_SHARD));
  }

  @Override
  public boolean itemInteractionForEntity(ItemStack stack, EntityPlayer player,
      EntityLivingBase target, EnumHand hand) {

    // Name plates can rename mobs.
    if (stack.isItemEqual(getStack(Names.NAME_PLATE))) {
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
}
