package net.silentchaos512.gems.item;

import java.util.List;

import org.lwjgl.input.Keyboard;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IIcon;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.silentchaos512.gems.configuration.Config;
import net.silentchaos512.gems.core.util.LocalizationHelper;
import net.silentchaos512.gems.core.util.RecipeHelper;
import net.silentchaos512.gems.lib.Names;
import net.silentchaos512.gems.lib.Strings;
import thaumcraft.api.ThaumcraftApi;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import cpw.mods.fml.common.registry.GameRegistry;

public class CraftingMaterial extends ItemSG {

  public final static int HIDE_AFTER_META = 12;
  public final static String[] names = { Names.ORNATE_STICK, Names.MYSTERY_GOO, Names.YARN_BALL,
      Names.CHAOS_ESSENCE, Names.CHAOS_ESSENCE_PLUS, Names.PLUME, Names.GOLDEN_PLUME,
      Names.CHAOS_SHARD, Names.CHAOS_CAPACITOR, Names.CHAOS_BOOSTER, Names.RAWHIDE_BONE,
      Names.CHAOS_ESSENCE_SHARD };

  public CraftingMaterial() {

    super();

    icons = new IIcon[names.length];
    setMaxStackSize(64);
    setHasSubtypes(true);
    setMaxDamage(0);
    setUnlocalizedName(Names.CRAFTING_MATERIALS);
  }

  @Override
  public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean par4) {

    list.add(EnumChatFormatting.DARK_GRAY
        + LocalizationHelper.getItemDescription(Names.CRAFTING_MATERIALS, 0));

    if (this.showFlavorText()) {
      list.add(EnumChatFormatting.ITALIC
          + LocalizationHelper.getItemDescription(names[stack.getItemDamage()], 0));
    }
  }

  @Override
  public void addOreDict() {

    OreDictionary.registerOre("gemChaos", getStack(Names.CHAOS_ESSENCE));
    OreDictionary.registerOre("nuggetChaos", getStack(Names.CHAOS_ESSENCE_SHARD));
  }

  @Override
  public void addRecipes() {

    ItemStack chaosEssence = getStack(Names.CHAOS_ESSENCE);

    // Ornate stick
    GameRegistry.addRecipe(getStack(Names.ORNATE_STICK, 4), "gig", "geg", "gig", 'g',
        Items.gold_ingot, 'i', Items.iron_ingot, 'e', chaosEssence);
    // Mystery goo
    GameRegistry.addRecipe(getStack(Names.MYSTERY_GOO, 1), "mmm", "mam", "mmm", 'm',
        Blocks.mossy_cobblestone, 'a', Items.apple);
    // Yarn ball
    GameRegistry.addRecipe(new ShapedOreRecipe(getStack(Names.YARN_BALL, 1), "sss", "sgs", "sss",
        's', Items.string, 'g', Strings.ORE_DICT_GEM_SHARD));
    // Refined chaos essence
    RecipeHelper.addSurround(getStack(Names.CHAOS_ESSENCE_PLUS, 1), new ItemStack(
        Items.glowstone_dust), new Object[] { Items.redstone, chaosEssence });
    // Plume
    GameRegistry.addRecipe(new ShapedOreRecipe(getStack(Names.PLUME, 1), "fff", "fsf", "fff", 'f',
        Items.feather, 's', Strings.ORE_DICT_GEM_BASIC));
    // Golden plume
    RecipeHelper.addSurround(getStack(Names.GOLDEN_PLUME, 1), getStack(Names.PLUME), chaosEssence,
        Items.gold_ingot);
    // Chaos Shard
    GameRegistry.addShapedRecipe(getStack(Names.CHAOS_SHARD, 24), "ccc", "cnc", "ccc", 'c',
        getStack(Names.CHAOS_ESSENCE_PLUS), 'n', Items.nether_star);
    // Chaos Capacitor
    GameRegistry.addShapedRecipe(getStack(Names.CHAOS_CAPACITOR, 3), "srs", "ses", "srs", 's',
        getStack(Names.CHAOS_SHARD), 'r', Items.redstone, 'e', Items.emerald);
    // Chaos Booster
    GameRegistry.addShapedRecipe(getStack(Names.CHAOS_BOOSTER, 3), "sgs", "ses", "sgs", 's',
        getStack(Names.CHAOS_SHARD), 'g', Items.glowstone_dust, 'e', Items.emerald);
    // Rawhide bone
    GameRegistry.addShapedRecipe(getStack(Names.RAWHIDE_BONE, 1), " l ", "lbl", " l ", 'l',
        Items.leather, 'b', Items.bone);
    // Chaos Essence Shard
    RecipeHelper.addCompressionRecipe(getStack(Names.CHAOS_ESSENCE_SHARD), chaosEssence, 9);
  }

  @Override
  public void addThaumcraftStuff() {

    ThaumcraftApi.registerObjectTag(getStack(Names.CHAOS_ESSENCE),
        (new AspectList()).add(Aspect.GREED, 4).add(Aspect.ENTROPY, 2));
  }

  @Override
  public EnumRarity getRarity(ItemStack stack) {

    if (stack.getItemDamage() == getMetaFor(Names.CHAOS_ESSENCE_PLUS)) {
      return EnumRarity.rare;
    } else {
      return super.getRarity(stack);
    }
  }

  public static ItemStack getStack(String name) {

    for (int i = 0; i < names.length; ++i) {
      if (names[i].equals(name)) {
        return new ItemStack(ModItems.craftingMaterial, 1, i);
      }
    }

    return null;
  }

  public static ItemStack getStack(String name, int count) {

    for (int i = 0; i < names.length; ++i) {
      if (names[i].equals(name)) {
        return new ItemStack(ModItems.craftingMaterial, count, i);
      }
    }

    return null;
  }

  public static int getMetaFor(String name) {

    for (int i = 0; i < names.length; ++i) {
      if (names[i].equals(name)) {
        return i;
      }
    }

    return -1;
  }

  @Override
  public String getUnlocalizedName(ItemStack stack) {

    return getUnlocalizedName(names[stack.getItemDamage()]);
  }

  @Override
  public boolean hasEffect(ItemStack stack, int pass) {

    return stack.getItemDamage() == getStack(Names.CHAOS_ESSENCE_PLUS).getItemDamage();
  }

  @Override
  public void registerIcons(IIconRegister iconRegister) {

    for (int i = 0; i < names.length; ++i) {
      icons[i] = iconRegister.registerIcon(Strings.RESOURCE_PREFIX + names[i]);
    }
  }
}
