package silent.gems.item;

import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.ShapedOreRecipe;
import silent.gems.SilentGems;
import silent.gems.block.ChaosEssenceBlock;
import silent.gems.core.registry.SRegistry;
import silent.gems.core.util.LocalizationHelper;
import silent.gems.core.util.RecipeHelper;
import silent.gems.lib.Names;
import silent.gems.lib.Strings;

public class CraftingMaterial extends ItemSG {

  public final static int HIDE_AFTER_META = 12;
  public final static String[] NAMES = { Names.ORNATE_STICK, Names.MYSTERY_GOO, Names.YARN_BALL,
      Names.CHAOS_ESSENCE_OLD, Names.CHAOS_ESSENCE_PLUS_OLD, Names.PLUME, Names.GOLDEN_PLUME,
      Names.CHAOS_SHARD, Names.CHAOS_CAPACITOR, Names.CHAOS_BOOSTER, Names.RAWHIDE_BONE,
      Names.CHAOS_ESSENCE_SHARD, Names.GILDED_STRING };

  public CraftingMaterial() {

    super(NAMES.length);

    setMaxStackSize(64);
    setHasSubtypes(true);
    setMaxDamage(0);
    setUnlocalizedName(Names.CRAFTING_MATERIALS);
  }

  @Override
  public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean par4) {

    list.add(EnumChatFormatting.DARK_GRAY
        + LocalizationHelper.getItemDescription(Names.CRAFTING_MATERIALS, 0));
    list.add(EnumChatFormatting.ITALIC
        + LocalizationHelper.getItemDescription(NAMES[stack.getItemDamage()], 0));
  }

  @Override
  public void addRecipes() {

    int meta = ChaosEssenceBlock.EnumType.REGULAR.getMetadata();
    ItemStack chaosEssence = new ItemStack(ModItems.chaosEssence, 1, meta);
    meta = ChaosEssenceBlock.EnumType.REFINED.getMetadata();
    ItemStack chaosEssencePlus = new ItemStack(ModItems.chaosEssence, 1, meta);

    // Ornate stick
    GameRegistry.addRecipe(getStack(Names.ORNATE_STICK, 8), "gig", "geg", "gig", 'g',
        Items.gold_ingot, 'i', Items.iron_ingot, 'e', chaosEssence);
    // Mystery goo
    GameRegistry.addRecipe(getStack(Names.MYSTERY_GOO, 1), "mmm", "mam", "mmm", 'm',
        Blocks.mossy_cobblestone, 'a', Items.apple);
    // Yarn ball
    GameRegistry.addRecipe(new ShapedOreRecipe(getStack(Names.YARN_BALL, 1), "sss", "sgs", "sss",
        's', Items.string, 'g', Strings.ORE_DICT_GEM_SHARD));
    // Chaos essence
    GameRegistry.addShapelessRecipe(chaosEssence, getStack(Names.CHAOS_ESSENCE_OLD));
    // Refined chaos essence
    GameRegistry.addShapelessRecipe(chaosEssencePlus, getStack(Names.CHAOS_ESSENCE_PLUS_OLD));
    // Plume
    GameRegistry.addRecipe(new ShapedOreRecipe(getStack(Names.PLUME, 1), "fff", "fsf", "fff", 'f',
        Items.feather, 's', Strings.ORE_DICT_GEM_BASIC));
    // Golden plume
    RecipeHelper.addSurround(getStack(Names.GOLDEN_PLUME, 1), getStack(Names.PLUME), chaosEssence,
        Items.gold_ingot);
    // Chaos Shard
    GameRegistry.addShapedRecipe(getStack(Names.CHAOS_SHARD, 24), "ccc", "cnc", "ccc", 'c',
        chaosEssencePlus, 'n', Items.nether_star);
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
    // Gilded String
    GameRegistry.addShapedRecipe(getStack(Names.GILDED_STRING, 3), "gsg", "gsg", "gsg", 's',
        Items.string, 'g', Items.gold_nugget);
  }

  @Override
  public void addThaumcraftStuff() {

    // ThaumcraftApi.registerObjectTag(getStack(Names.CHAOS_ESSENCE),
    // (new AspectList()).add(Aspect.GREED, 4).add(Aspect.ENTROPY, 2));
  }
  
  @Override
  public String[] getVariantNames() {
    
    String[] result = new String[NAMES.length];
    for (int i = 0; i < result.length; ++i) {
      result[i] = SilentGems.MOD_ID + ":" + NAMES[i];
    }
    return result;
  }

  public static ItemStack getStack(String name) {

    for (int i = 0; i < NAMES.length; ++i) {
      if (NAMES[i].equals(name)) {
        return new ItemStack(ModItems.craftingMaterial, 1, i);
      }
    }

    return null;
  }

  public static ItemStack getStack(String name, int count) {

    for (int i = 0; i < NAMES.length; ++i) {
      if (NAMES[i].equals(name)) {
        return new ItemStack(ModItems.craftingMaterial, count, i);
      }
    }

    return null;
  }

  public static int getMetaFor(String name) {

    for (int i = 0; i < NAMES.length; ++i) {
      if (NAMES[i].equals(name)) {
        return i;
      }
    }

    return -1;
  }

  @Override
  public String getUnlocalizedName(ItemStack stack) {

    return getUnlocalizedName(NAMES[stack.getItemDamage()]);
  }

  @Override
  public boolean hasEffect(ItemStack stack) {

    return stack.getItemDamage() == getStack(Names.CHAOS_ESSENCE_PLUS_OLD).getItemDamage();
  }
}
