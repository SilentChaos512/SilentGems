package net.silentchaos512.gems.item;

import java.util.List;
import java.util.Random;

import com.google.common.collect.Lists;

import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.MobEffects;
import net.minecraft.item.EnumAction;
import net.minecraft.item.Item;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.silentchaos512.gems.SilentGems;
import net.silentchaos512.gems.config.Config;
import net.silentchaos512.gems.lib.Names;
import net.silentchaos512.lib.registry.IRegistryObject;
import net.silentchaos512.lib.util.RecipeHelper;

public class ItemFoodSG extends ItemFood implements IRegistryObject {

  public static final String[] NAMES = { Names.POTATO_STICK, Names.SUGAR_COOKIE, Names.SECRET_DONUT,
      Names.MEATY_STEW_UNCOOKED, Names.MEATY_STEW, Names.CANDY_CANE, Names.COFFEE_CUP };

  public static final int[] foodLevel = { 8, 2, 6, 4, 12, 2, 1 };
  public static final float[] saturationLevel = { 0.8f, 0.4f, 0.8f, 0.6f, 1.6f, 0.2f, 0.2f };
  public static final boolean[] alwaysEdible = { false, true, false, false, false, true, true };

  public final ItemStack potatoStick = getStack(Names.POTATO_STICK);
  public final ItemStack sugarCookie = getStack(Names.SUGAR_COOKIE);
  public final ItemStack secretDonut = getStack(Names.SECRET_DONUT);
  public final ItemStack meatyStewUncooked = getStack(Names.MEATY_STEW_UNCOOKED);
  public final ItemStack meatyStew = getStack(Names.MEATY_STEW);
  public final ItemStack candyCane = getStack(Names.CANDY_CANE);
  public final ItemStack coffeeCup = getStack(Names.COFFEE_CUP);

  public static final List<SecretDonutEffect> secretDonutEffects = Lists.newArrayList();

  public ItemFoodSG() {

    super(1, 1.0f, false);

    setHasSubtypes(true);
    setMaxDamage(0);
    setUnlocalizedName(Names.FOOD);
    setCreativeTab(SilentGems.instance.creativeTab);

    // Add secret donut effects.
    secretDonutEffects.clear();
    secretDonutEffects.add(new SecretDonutEffect(MobEffects.BLINDNESS, 0.5f));
    secretDonutEffects.add(new SecretDonutEffect(MobEffects.NAUSEA, 0.5f));
    secretDonutEffects.add(new SecretDonutEffect(MobEffects.STRENGTH, 1.5f));
    secretDonutEffects.add(new SecretDonutEffect(MobEffects.MINING_FATIGUE, 2.0f));
    secretDonutEffects.add(new SecretDonutEffect(MobEffects.HASTE, 2.0f));
    secretDonutEffects.add(new SecretDonutEffect(MobEffects.FIRE_RESISTANCE, 4.0f));
    secretDonutEffects.add(new SecretDonutEffect(MobEffects.HUNGER, 0.5f));
    secretDonutEffects.add(new SecretDonutEffect(MobEffects.INVISIBILITY, 0.5f));
    secretDonutEffects.add(new SecretDonutEffect(MobEffects.JUMP_BOOST, 1.0f));
    secretDonutEffects.add(new SecretDonutEffect(MobEffects.SLOWNESS, 2.0f));
    secretDonutEffects.add(new SecretDonutEffect(MobEffects.SPEED, 2.0f));
    secretDonutEffects.add(new SecretDonutEffect(MobEffects.NIGHT_VISION, 1.0f));
    secretDonutEffects.add(new SecretDonutEffect(MobEffects.POISON, 0.5f));
    secretDonutEffects.add(new SecretDonutEffect(MobEffects.REGENERATION, 0.5f));
    secretDonutEffects.add(new SecretDonutEffect(MobEffects.RESISTANCE, 0.5f));
    secretDonutEffects.add(new SecretDonutEffect(MobEffects.WATER_BREATHING, 2.0f));
    secretDonutEffects.add(new SecretDonutEffect(MobEffects.WEAKNESS, 1.5f));
    secretDonutEffects.add(new SecretDonutEffect(MobEffects.WITHER, 0.5f));
  }

  @Override
  public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean advanced) {

    int meta = stack.getItemDamage();
    if (meta < NAMES.length) {
      list.addAll(SilentGems.instance.localizationHelper.getItemDescriptionLines(NAMES[meta]));
    }
  }

  private void addSecretDonutEffect(World world, EntityPlayer player) {

    SecretDonutEffect effect = secretDonutEffects
        .get(world.rand.nextInt(secretDonutEffects.size()));
    player.addPotionEffect(new PotionEffect(effect.potion,
        (int) (Config.FOOD_SUPPORT_DURATION * effect.durationMulti), 0, true, false));
  }

  @Override
  public int getMaxItemUseDuration(ItemStack stack) {

    if (stack.getItemDamage() == 1) {
      return 16;
    } else {
      return 32;
    }
  }

  @Override
  public EnumAction getItemUseAction(ItemStack stack) {

    if (stack.getItemDamage() == coffeeCup.getItemDamage()) {
      return EnumAction.DRINK;
    }
    return super.getItemUseAction(stack);
  }

  public ItemStack getStack(String name) {

    return getStack(name, 1);
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
      return "item.silentgems:" + NAMES[stack.getItemDamage()];
    } else {
      return super.getUnlocalizedName(stack);
    }
  }

  @Override
  public ActionResult<ItemStack> onItemRightClick(ItemStack stack, World world, EntityPlayer player,
      EnumHand hand) {

    if (player.canEat(alwaysEdible[stack.getItemDamage()])) {
      player.setActiveHand(hand);
      return new ActionResult(EnumActionResult.SUCCESS, stack);
    }

    return new ActionResult<ItemStack>(EnumActionResult.FAIL, stack);
  }

  @Override
  public void onFoodEaten(ItemStack stack, World world, EntityPlayer player) {

    if (!world.isRemote) {
      int d = stack.getItemDamage();
      if (d == 0) {
        // Potato on a stick
        player.addPotionEffect(
            new PotionEffect(MobEffects.STRENGTH, Config.FOOD_SUPPORT_DURATION, 0, true, false));
        givePlayerItem(player, new ItemStack(Items.STICK));
      } else if (d == 1) {
        // Sugar cookie
        player.addPotionEffect(
            new PotionEffect(MobEffects.HASTE, Config.FOOD_SUPPORT_DURATION, 0, true, false));
        player.addPotionEffect(
            new PotionEffect(MobEffects.SPEED, Config.FOOD_SUPPORT_DURATION, 0, true, false));
      } else if (d == 2) {
        // Secret donut
        onDonutEaten(world, player);
      } else if (d == 3 || d == 4) {
        // Meaty Stew
        givePlayerItem(player, new ItemStack(Items.BOWL));
      } else if (d == 5) {
        // Candy Cane
        player.addPotionEffect(new PotionEffect(MobEffects.REGENERATION,
            Config.FOOD_SUPPORT_DURATION / 6, 0, true, false));
      } else if (d == 6) {
        // Coffee Cup
        int duration = (int) (2.0f * Config.FOOD_SUPPORT_DURATION);
        player.addPotionEffect(new PotionEffect(MobEffects.SPEED, duration * 2, 1, true, false));
        player.addPotionEffect(new PotionEffect(MobEffects.HASTE, duration, 1, true, false));
      }
    }

    super.onFoodEaten(stack, world, player);
  }

  private void onDonutEaten(World world, EntityPlayer player) {

    // Give potion effect?
    Random rand = SilentGems.instance.random;
    if (rand.nextFloat() < Config.FOOD_SECRET_DONUT_CHANCE) {
      addSecretDonutEffect(world, player);
      // Smaller chance of a second effect.
      if (rand.nextFloat() < Config.FOOD_SECRET_DONUT_CHANCE) {
        addSecretDonutEffect(world, player);
        // Even smaller chance of third effect.
        if (rand.nextFloat() < Config.FOOD_SECRET_DONUT_CHANCE) {
          addSecretDonutEffect(world, player);
        }
      }
    }

    // Add chat message
    if (rand.nextFloat() < Config.FOOD_SECRET_DONUT_TEXT_CHANCE) {
      List<String> list = SilentGems.instance.localizationHelper
          .getDescriptionLines("donut.silentgems:");
      String line = list.get(rand.nextInt(list.size()));
      player.addChatMessage(new TextComponentString(line));
    }
  }

  private void givePlayerItem(EntityPlayer player, ItemStack stack) {

    EntityItem item = new EntityItem(player.worldObj, player.posX, player.posY + 1.0, player.posZ,
        stack);
    player.worldObj.spawnEntityInWorld(item);
  }

  @Override
  public int getHealAmount(ItemStack stack) {

    int meta = stack.getItemDamage();
    if (meta < 0 || meta >= NAMES.length) {
      return 0;
    }
    return foodLevel[meta];
  }

  @Override
  public float getSaturationModifier(ItemStack stack) {

    int meta = stack.getItemDamage();
    if (meta < 0 || meta >= NAMES.length) {
      return 0f;
    }
    return saturationLevel[meta];
  }

  @Override
  public boolean isWolfsFavoriteMeat() {

    return false;
  }

  @Override
  public void getSubItems(Item item, CreativeTabs tab, List list) {

    for (int i = 0; i < NAMES.length; ++i) {
      list.add(new ItemStack(item, 1, i));
    }
  }

  @Override
  public void addRecipes() {

    // Potato on a Stick
    GameRegistry.addRecipe(new ShapedOreRecipe(getStack(Names.POTATO_STICK, 1), " p", "s ", 'p',
        Items.BAKED_POTATO, 's', "stickWood"));
    // Sugar Cookie
    GameRegistry.addShapedRecipe(getStack(Names.SUGAR_COOKIE, 8), " s ", "www", " s ", 's',
        Items.SUGAR, 'w', Items.WHEAT);
    // Secret Donut
    RecipeHelper.addSurround(getStack(Names.SECRET_DONUT, 8), new ItemStack(Blocks.RED_MUSHROOM),
        Items.WHEAT);
    // Meaty Stew
    Item[] meats = { Items.BEEF, Items.PORKCHOP, Items.CHICKEN };
    for (Item meat : meats) {
      GameRegistry.addShapelessRecipe(getStack(Names.MEATY_STEW_UNCOOKED, 1), Items.BOWL, meat,
          Items.POTATO, Items.CARROT);
    }
    GameRegistry.addSmelting(getStack(Names.MEATY_STEW_UNCOOKED, 1), getStack(Names.MEATY_STEW, 1),
        0.5f);
    // Candy Cane
    GameRegistry.addRecipe(new ShapedOreRecipe(getStack(Names.CANDY_CANE, 6), "ss", "rs", " s", 's',
        Items.SUGAR, 'r', "dyeRed"));
  }

  @Override
  public void addOreDict() {

  }

  @Override
  public String getName() {

    return Names.FOOD;
  }

  @Override
  public String getFullName() {

    return getModId() + ":" + getName();
  }

  @Override
  public String getModId() {

    return SilentGems.MOD_ID.toLowerCase();
  }

  @Override
  public List<ModelResourceLocation> getVariants() {

    List<ModelResourceLocation> models = Lists.newArrayList();
    for (int i = 0; i < NAMES.length; ++i) {
      models.add(new ModelResourceLocation(SilentGems.MOD_ID + ":" + NAMES[i], "inventory"));
    }
    return models;
  }

  @Override
  public boolean registerModels() {

    // TODO Auto-generated method stub
    return false;
  }

  public static class SecretDonutEffect {

    public Potion potion;
    public float durationMulti;

    public SecretDonutEffect(Potion potion, float durationMulti) {

      this.potion = potion;
      this.durationMulti = durationMulti;
    }
  }
}
