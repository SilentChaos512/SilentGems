package net.silentchaos512.gems.util;

import java.util.List;

import com.google.common.collect.Lists;

import net.minecraft.entity.EntityList;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;
import net.silentchaos512.gems.SilentGems;
import net.silentchaos512.gems.api.ITool;
import net.silentchaos512.gems.item.ModItems;
import net.silentchaos512.gems.item.tool.ItemGemKatana;
import net.silentchaos512.gems.lib.EnumGem;
import net.silentchaos512.lib.util.EntityHelper;

public class ToolRandomizer {

  public static final float SECOND_GEM_CHANCE = 0.7f;
  public static final float THIRD_GEM_CHANCE = 0.5f;
  public static final float SUPER_TIER_CHANCE = 0.33f;

  private static final String[] NAME_ADJECTIVES_BASE = { "an abstract", "an ancient",
      "a bite-sized", "a brave", "a convenient", "a creepy", "a dashing", "a defenestrated",
      "a disturbed", "a dizzy", "a fanatical", "a grieving", "a languid", "a mysterious",
      "a psychotic", "a quaint", "a questionable", "a quizzical", "a redundant", "a sentient",
      "a sturdy", "a tasteful", "a thoughtful", "an unbiased", "an unloved", };
  private static final String[] NAME_NOUNS_BASE = { "alien", "alpaca", "bench", "buffalo", "cake",
      "cat", "crate", "dime", "egg", "fish", "fork", "guitar", "hammer", "hatred", "key", "lamp",
      "mitten", "pair of dice", "pendulum", "pizza", "potato", "rice cooker", "rock", "shelf",
      "sock puppet", "spoon", "square", "storm", "surprise", "toaster", "toothbrush", "toy", "tree",
      "twig", "wheel" };

  public List<String> nameAdjectives = Lists.newArrayList(NAME_ADJECTIVES_BASE);
  public List<String> nameNouns = Lists.newArrayList(NAME_NOUNS_BASE);

  public static ToolRandomizer INSTANCE = new ToolRandomizer();

  private ToolRandomizer() {

    for (String name : EntityHelper.getEntityNameList()) {
      String entityName = "entity." + name + ".name";
      String localizedName = SilentGems.localizationHelper.getLocalizedString(entityName);
      if (!localizedName.endsWith(".name")) {
        nameNouns.add(localizedName.toLowerCase());
      }
    }
  }

  public ItemStack randomize(ItemStack tool) {

    return randomize(tool, SUPER_TIER_CHANCE);
  }

  public ItemStack randomize(ItemStack tool, float superChance) {

    if (!(tool.getItem() instanceof ITool) || !ToolHelper.hasNoConstruction(tool))
      return tool;

    ITool itool = (ITool) tool.getItem();

    // Regular or super?
    boolean superTier = itool.isSuperTool() || SilentGems.random.nextFloat() < superChance;

    // How many gems?
    boolean gem2 = SilentGems.random.nextFloat() < SECOND_GEM_CHANCE;
    boolean gem3 = gem2 && SilentGems.random.nextFloat() < THIRD_GEM_CHANCE;

    // Gem array
    EnumGem[] gems;
    if (gem3) {
      // 3 gems
      gems = new EnumGem[] { EnumGem.getRandom(), EnumGem.getRandom(), EnumGem.getRandom() };
    } else if (gem2) {
      // 2 gems (3rd = 1st)
      EnumGem g1 = EnumGem.getRandom();
      EnumGem g2 = EnumGem.getRandom();
      if (tool.getItem() == ModItems.sword)
        gems = new EnumGem[] { g1, g2 };
      else
        gems = new EnumGem[] { g1, g2, g1 };
    } else {
      // 1 gem
      EnumGem g1 = EnumGem.getRandom();
      gems = new EnumGem[] { g1, g1, g1 };
    }

    // Crafting stack array
    ItemStack[] craftingStacks = new ItemStack[gems.length];
    for (int i = 0; i < craftingStacks.length; ++i) {
      craftingStacks[i] = superTier ? gems[i].getItemSuper() : gems[i].getItem();
    }

    // Select rod
    ItemStack rod;
    if (superTier) {
      ItemStack[] choices = new ItemStack[] { ModItems.craftingMaterial.toolRodGold,
          ModItems.craftingMaterial.toolRodSilver, ModItems.craftingMaterial.toolRodIron };
      rod = choices[SilentGems.random.nextInt(choices.length)];
    } else {
      ItemStack[] choices = new ItemStack[] { new ItemStack(Items.STICK), new ItemStack(Items.BONE),
          ModItems.craftingMaterial.toolRodIron };
      rod = choices[SilentGems.random.nextInt(choices.length)];
    }

    ItemStack temp = ToolHelper.constructTool(tool.getItem(), rod, craftingStacks);
    if (temp.getItem() instanceof ItemGemKatana)
      temp = ((ItemGemKatana) temp.getItem()).addDefaultGrip(temp);

    // Set name
    String ownerName = nameAdjectives.get(SilentGems.random.nextInt(nameAdjectives.size())) + " "
        + nameNouns.get(SilentGems.random.nextInt(nameNouns.size()));
    ToolHelper.setOriginalOwner(temp, TextFormatting.AQUA + ownerName);

    tool.setTagCompound(temp.getTagCompound());

    return temp;
  }
}
