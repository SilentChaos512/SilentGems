package silent.gems.item;

import java.util.List;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.oredict.OreDictionary;

import org.lwjgl.input.Keyboard;

import silent.gems.block.ChaosEssenceBlock;
import silent.gems.core.util.LocalizationHelper;
import silent.gems.lib.EnumGem;
import silent.gems.lib.Names;
import silent.gems.lib.Strings;

public class Gem extends ItemSG {

  public Gem() {

    super(EnumGem.count());

    setMaxStackSize(64);
    setHasSubtypes(true);
    setHasGemSubtypes(true);
    setMaxDamage(0);
    setUnlocalizedName(Names.GEM_ITEM);
  }

  @SideOnly(Side.CLIENT)
  @Override
  public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean par4) {

    boolean shifted = Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)
        || Keyboard.isKeyDown(Keyboard.KEY_RSHIFT);

    if (shifted) {
      int id = stack.getItemDamage() & 0xF;
      boolean supercharged = stack.getItemDamage() > 15;
      ToolMaterial material = EnumGem.byId(id).getToolMaterial(supercharged);

      list.add(EnumChatFormatting.ITALIC
          + LocalizationHelper.getOtherItemKey(itemName, "ToolProperties"));

      // Durability
      String format = "%s: %d";
      String s = LocalizationHelper.getOtherItemKey(itemName, "MaxUses");
      s = String.format(format, s, material.getMaxUses());
      list.add(s);

      // Efficiency
      format = "%s: %.1f";
      s = LocalizationHelper.getOtherItemKey(itemName, "Efficiency");
      s = String.format(format, s, material.getEfficiencyOnProperMaterial());
      list.add(s);

      // Damage
      format = "%s: %d";
      s = LocalizationHelper.getOtherItemKey(itemName, "Damage");
      s = String.format(format, s, (int) material.getDamageVsEntity());
      list.add(s);

      // Decorate tool hint.
      if (stack.getItemDamage() < EnumGem.count()) {
        list.add(EnumChatFormatting.DARK_AQUA
            + LocalizationHelper.getOtherItemKey(itemName, "Decorate1"));
        list.add(EnumChatFormatting.DARK_AQUA
            + LocalizationHelper.getOtherItemKey(itemName, "Decorate2"));
        list.add(EnumChatFormatting.DARK_AQUA
            + LocalizationHelper.getOtherItemKey(itemName, "Decorate3"));
        list.add(EnumChatFormatting.DARK_AQUA
            + LocalizationHelper.getOtherItemKey(itemName, "Decorate4"));
      }
    } else {
      list.add(EnumChatFormatting.ITALIC + LocalizationHelper.getMiscText(Strings.PRESS_SHIFT));
    }
  }

  @Override
  public void addOreDict() {

    OreDictionary.registerOre("gemRuby", new ItemStack(this, 1, EnumGem.RUBY.getId()));
    OreDictionary.registerOre("gemGarnet", new ItemStack(this, 1, EnumGem.GARNET.getId()));
    OreDictionary.registerOre("gemTopaz", new ItemStack(this, 1, EnumGem.TOPAZ.getId()));
    OreDictionary.registerOre("gemHeliodor", new ItemStack(this, 1, EnumGem.HELIODOR.getId()));
    OreDictionary.registerOre("gemPeridot", new ItemStack(this, 1, EnumGem.PERIDOT.getId()));
    OreDictionary.registerOre("gemEmerald", new ItemStack(this, 1, EnumGem.EMERALD.getId()));
    OreDictionary.registerOre("gemAquamarine", new ItemStack(this, 1, EnumGem.AQUAMARINE.getId()));
    OreDictionary.registerOre("gemSapphire", new ItemStack(this, 1, EnumGem.SAPPHIRE.getId()));
    OreDictionary.registerOre("gemIolite", new ItemStack(this, 1, EnumGem.IOLITE.getId()));
    OreDictionary.registerOre("gemAmethyst", new ItemStack(this, 1, EnumGem.AMETHYST.getId()));
    OreDictionary.registerOre("gemMorganite", new ItemStack(this, 1, EnumGem.MORGANITE.getId()));
    OreDictionary.registerOre("gemOnyx", new ItemStack(this, 1, EnumGem.ONYX.getId()));

    for (int i = 0; i < EnumGem.count(); ++i) {
      OreDictionary.registerOre(Strings.ORE_DICT_GEM_BASIC, new ItemStack(this, 1, i));
    }
  }

  @Override
  public void addRecipes() {

    ItemStack chaosEssence = ChaosEssence.getByType(ChaosEssenceBlock.EnumType.REGULAR);
    for (int i = 0; i < EnumGem.count(); ++i) {
      GameRegistry.addShapedRecipe(new ItemStack(this, 1, i | 16), "ere", "ege", "ere", 'e',
          chaosEssence, 'r', Items.redstone, 'g', new ItemStack(this, 1, i));
    }
  }

  @Override
  public void addThaumcraftStuff() {

    // ThaumcraftApi.registerObjectTag(EnumGem.RUBY.getItem(),
    // (new AspectList()).add(Aspect.CRYSTAL, 1).add(Aspect.WEAPON, 2));
    // ThaumcraftApi.registerObjectTag(EnumGem.GARNET.getItem(),
    // (new AspectList()).add(Aspect.CRYSTAL, 1).add(Aspect.FIRE, 2));
    // ThaumcraftApi.registerObjectTag(EnumGem.TOPAZ.getItem(),
    // (new AspectList()).add(Aspect.CRYSTAL, 1).add(Aspect.EARTH, 2));
    // ThaumcraftApi.registerObjectTag(EnumGem.HELIODOR.getItem(),
    // (new AspectList()).add(Aspect.CRYSTAL, 1).add(Aspect.GREED, 2));
    // ThaumcraftApi.registerObjectTag(EnumGem.PERIDOT.getItem(),
    // (new AspectList()).add(Aspect.CRYSTAL, 1).add(Aspect.DEATH, 2));
    // ThaumcraftApi.registerObjectTag(EnumGem.EMERALD.getItem(),
    // (new AspectList()).add(Aspect.CRYSTAL, 1).add(Aspect.MOTION, 2));
    // ThaumcraftApi.registerObjectTag(EnumGem.AQUAMARINE.getItem(),
    // (new AspectList()).add(Aspect.CRYSTAL, 1).add(Aspect.AURA, 2));
    // ThaumcraftApi.registerObjectTag(EnumGem.SAPPHIRE.getItem(),
    // (new AspectList()).add(Aspect.CRYSTAL, 1).add(Aspect.ARMOR, 2));
    // ThaumcraftApi.registerObjectTag(EnumGem.IOLITE.getItem(),
    // (new AspectList()).add(Aspect.CRYSTAL, 1).add(Aspect.CLOTH, 2));
    // ThaumcraftApi.registerObjectTag(EnumGem.AMETHYST.getItem(),
    // (new AspectList()).add(Aspect.CRYSTAL, 1).add(Aspect.POISON, 2));
    // ThaumcraftApi.registerObjectTag(EnumGem.MORGANITE.getItem(),
    // (new AspectList()).add(Aspect.CRYSTAL, 1).add(Aspect.MAN, 2));
    // ThaumcraftApi.registerObjectTag(EnumGem.ONYX.getItem(),
    // (new AspectList()).add(Aspect.CRYSTAL, 1).add(Aspect.DARKNESS, 2));
  }

  @Override
  public EnumRarity getRarity(ItemStack stack) {

    return (stack.getItemDamage() & 16) == 16 ? EnumRarity.RARE : EnumRarity.COMMON;
  }

  @SideOnly(Side.CLIENT)
  @Override
  public void getSubItems(Item item, CreativeTabs tabs, List list) {

    int i;
    for (i = 0; i < EnumGem.count(); ++i) {
      list.add(new ItemStack(this, 1, i));
    }
    for (i = 16; i < 16 + EnumGem.count(); ++i) {
      list.add(new ItemStack(this, 1, i));
    }
  }

  @Override
  public String[] getVariantNames() {

    int gemCount = EnumGem.count();
    String[] result = new String[28];

    int i = 0;
    // Regular gems
    for (; i < gemCount; ++i) {
      result[i] = getFullName() + i;
    }
    // Blanks
    for (; i < 16; ++i) {
      result[i] = null;
    }
    // Supercharged gems
    for (; i < 28; ++i) {
      result[i] = getFullName() + i;
    }

    return result;
  }

  @Override
  public String getUnlocalizedName(ItemStack stack) {

    return LocalizationHelper.GEMS_PREFIX + stack.getItemDamage();
  }

  @Override
  public boolean hasEffect(ItemStack stack) {

    return (stack.getItemDamage() & 16) == 16;
  }
}
