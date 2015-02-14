package silent.gems.item;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.ShapedOreRecipe;
import silent.gems.SilentGems;
import silent.gems.configuration.Config;
import silent.gems.core.registry.IAddRecipe;
import silent.gems.core.registry.IHasVariants;
import silent.gems.core.util.LocalizationHelper;
import silent.gems.core.util.PlayerHelper;
import silent.gems.core.util.RecipeHelper;
import silent.gems.lib.Names;
import silent.gems.lib.Reference;

public class FoodSG extends ItemFood implements IAddRecipe, IHasVariants {

  public final static String[] NAMES = { Names.POTATO_STICK, Names.SUGAR_COOKIE,
      Names.SECRET_DONUT, Names.MEATY_STEW_UNCOOKED, Names.MEATY_STEW };
  
  public final static int[] foodLevel = { 8, 2, 6, 4, 12 };
  public final static float[] saturationLevel = { 0.8f, 0.4f, 0.8f, 0.6f, 1.6f };
  public final static boolean[] alwaysEdible = { false, true, false, false, false };

  public final static ArrayList<SecretDonutEffect> secretDonutEffects = new ArrayList<SecretDonutEffect>();
  public final static int SECRET_DONUT_CHANCE = 20;

  public FoodSG() {

    super(1, 1.0f, false);

    setMaxStackSize(64);
    setHasSubtypes(true);
    setMaxDamage(0);
    setUnlocalizedName(Names.FOOD);
    setCreativeTab(SilentGems.tabSilentGems);

    // Add secret donut effects.
    secretDonutEffects.clear();
    secretDonutEffects.add(new SecretDonutEffect(Potion.blindness.id, 0.5f));
    secretDonutEffects.add(new SecretDonutEffect(Potion.confusion.id, 0.5f));
    secretDonutEffects.add(new SecretDonutEffect(Potion.damageBoost.id, 1.5f));
    secretDonutEffects.add(new SecretDonutEffect(Potion.digSlowdown.id, 2.0f));
    secretDonutEffects.add(new SecretDonutEffect(Potion.digSpeed.id, 2.0f));
    secretDonutEffects.add(new SecretDonutEffect(Potion.fireResistance.id, 4.0f));
    secretDonutEffects.add(new SecretDonutEffect(Potion.hunger.id, 0.5f));
    secretDonutEffects.add(new SecretDonutEffect(Potion.invisibility.id, 0.5f));
    secretDonutEffects.add(new SecretDonutEffect(Potion.jump.id, 1.0f));
    secretDonutEffects.add(new SecretDonutEffect(Potion.moveSlowdown.id, 2.0f));
    secretDonutEffects.add(new SecretDonutEffect(Potion.moveSpeed.id, 2.0f));
    secretDonutEffects.add(new SecretDonutEffect(Potion.nightVision.id, 1.0f));
    secretDonutEffects.add(new SecretDonutEffect(Potion.poison.id, 0.5f));
    secretDonutEffects.add(new SecretDonutEffect(Potion.regeneration.id, 0.5f));
    secretDonutEffects.add(new SecretDonutEffect(Potion.resistance.id, 0.5f));
    secretDonutEffects.add(new SecretDonutEffect(Potion.waterBreathing.id, 2.0f));
    secretDonutEffects.add(new SecretDonutEffect(Potion.weakness.id, 1.5f));
    secretDonutEffects.add(new SecretDonutEffect(Potion.wither.id, 0.5f));
  }

  @Override
  public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean par4) {

    if (stack.getItemDamage() < NAMES.length) {
      list.add(EnumChatFormatting.DARK_GRAY + LocalizationHelper.getItemDescription(Names.FOOD, 0));
      list.add(LocalizationHelper.getItemDescription(NAMES[stack.getItemDamage()], 0));
    }
  }

  @Override
  public void addOreDict() {

    // This fixes a crash caused by cooking Meaty Stew in a TC4 Infernal Furnace (this probably doesn't matter
    // anymore, Forge issue)
    OreDictionary.registerOre("foodMeatyStewUncooked", getStack(Names.MEATY_STEW_UNCOOKED, 1));
  }

  @Override
  public void addRecipes() {

    // Potato on a Stick
    GameRegistry.addRecipe(new ShapedOreRecipe(getStack(Names.POTATO_STICK, 1), " p", "s ", 'p',
        Items.baked_potato, 's', "stickWood"));
    // Sugar Cookie
    GameRegistry.addShapedRecipe(getStack(Names.SUGAR_COOKIE, 8), " s ", "www", " s ", 's',
        Items.sugar, 'w', Items.wheat);
    // Secret Donut
    RecipeHelper.addSurround(getStack(Names.SECRET_DONUT, 8), new ItemStack(Blocks.red_mushroom),
        Items.wheat);
    // Meaty Stew
    Item[] meats = { Items.beef, Items.porkchop, Items.chicken };
    for (Item meat : meats) {
      GameRegistry.addShapelessRecipe(getStack(Names.MEATY_STEW_UNCOOKED, 1), Items.bowl, meat,
          Items.potato, Items.carrot);
    }
    GameRegistry.addSmelting(getStack(Names.MEATY_STEW_UNCOOKED, 1), getStack(Names.MEATY_STEW, 1),
        0.5f);
  }
  
  @Override
  public String[] getVariantNames() {
    
    String[] result = new String[NAMES.length];
    for (int i = 0; i < result.length; ++i) {
      result[i] = Reference.MOD_ID + ":" + NAMES[i];
    }
    return result;
  }
  
  @Override
  public String getName() {
    
    return Names.FOOD;
  }
  
  @Override
  public String getFullName() {
    
    return Reference.MOD_ID + ":" + Names.FOOD;
  }

  private void addSecretDonutEffect(World world, EntityPlayer player) {

    SecretDonutEffect effect = secretDonutEffects
        .get(world.rand.nextInt(secretDonutEffects.size()));
    player.addPotionEffect(new PotionEffect(effect.potionId,
        (int) (Config.FOOD_SUPPORT_DURATION.value * effect.durationMulti), 0, false, true));
  }

  @Override
  public int getMaxItemUseDuration(ItemStack stack) {

    if (stack.getItemDamage() == 1) {
      return 16;
    } else {
      return 32;
    }
  }

  public ItemStack getStack(String name, int count) {

    for (int i = 0; i < NAMES.length; ++i) {
      if (NAMES[i].equals(name)) {
        return new ItemStack(this, count, i);
      }
    }
    return null;
  }

  @Override
  public String getUnlocalizedName(ItemStack stack) {

    if (stack.getItemDamage() < NAMES.length) {
      return LocalizationHelper.ITEM_PREFIX + NAMES[stack.getItemDamage()];
    } else {
      return super.getUnlocalizedName(stack);
    }
  }

  @Override
  public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player) {

    if (player.canEat(alwaysEdible[stack.getItemDamage()])) {
      player.setItemInUse(stack, this.getMaxItemUseDuration(stack));
    }

    return stack;
  }

  @Override
  public void onFoodEaten(ItemStack stack, World world, EntityPlayer player) {

    if (!world.isRemote) {
      int d = stack.getItemDamage();
      if (d == 0) {
        // Potato on a stick
        player.addPotionEffect(new PotionEffect(Potion.damageBoost.id,
            Config.FOOD_SUPPORT_DURATION.value, 0, false, true));
        PlayerHelper.addItemToInventoryOrDrop(player, new ItemStack(Items.stick));
      } else if (d == 1) {
        // Sugar cookie
        player.addPotionEffect(new PotionEffect(Potion.digSpeed.id,
            Config.FOOD_SUPPORT_DURATION.value, 0, false, true));
        player.addPotionEffect(new PotionEffect(Potion.moveSpeed.id,
            Config.FOOD_SUPPORT_DURATION.value, 0, false, true));
      } else if (d == 2) {
        // Secret donut
        // 100% chance of first effect.
        addSecretDonutEffect(world, player);
        // Smaller chance of a second effect.
        if (world.rand.nextInt(100) < SECRET_DONUT_CHANCE) {
          addSecretDonutEffect(world, player);
          // Even smaller chance of third effect.
          if (world.rand.nextInt(100) < SECRET_DONUT_CHANCE) {
            addSecretDonutEffect(world, player);
          }
        }
      } else if (d == 3 || d == 4) {
        // Meaty Stew
        PlayerHelper.addItemToInventoryOrDrop(player, new ItemStack(Items.bowl));
      }
    }

    super.onFoodEaten(stack, world, player);
  }

//  @Override
//  public void registerIcons(IIconRegister iconRegister) {
//
//    for (int i = 0; i < names.length; ++i) {
//      icons[i] = iconRegister.registerIcon(Strings.RESOURCE_PREFIX + names[i]);
//    }
//  }

  @Override
  public int getHealAmount(ItemStack stack) {

    return foodLevel[stack.getItemDamage()];
  }

  @Override
  public float getSaturationModifier(ItemStack stack) {

    return saturationLevel[stack.getItemDamage()];
  }

  @Override
  public boolean isWolfsFavoriteMeat() {

    return false;
  }

//  @Override
//  public IIcon getIconFromDamage(int meta) {
//
//    if (hasSubtypes && icons != null && meta < icons.length) {
//      return icons[meta];
//    } else {
//      return super.getIconFromDamage(meta);
//    }
//  }

  @Override
  public void getSubItems(Item item, CreativeTabs tab, List list) {

    for (int i = 0; i < NAMES.length; ++i) {
      list.add(new ItemStack(this, 1, i));
    }
  }

  public static class SecretDonutEffect {

    public int potionId;
    public float durationMulti;

    public SecretDonutEffect(int potionId, float durationMulti) {

      this.potionId = potionId;
      this.durationMulti = durationMulti;
    }
  }
}
