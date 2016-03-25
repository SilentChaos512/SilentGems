package net.silentchaos512.gems.item;

import java.util.List;

import org.apache.commons.lang3.NotImplementedException;
import org.lwjgl.input.Keyboard;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.silentchaos512.gems.SilentGems;
import net.silentchaos512.gems.api.energy.IChaosStorage;
import net.silentchaos512.gems.handler.PlayerDataHandler;
import net.silentchaos512.gems.handler.PlayerDataHandler.PlayerData;
import net.silentchaos512.gems.lib.Names;
import net.silentchaos512.gems.util.NBTHelper;
import net.silentchaos512.lib.item.ItemSL;
import net.silentchaos512.lib.util.PlayerHelper;

public class ItemChaosOrb extends ItemSL implements IChaosStorage {

  public static enum Type {

    //@formatter:off
    POTATO(     5000, 0.01f),
    FRAGILE(  100000, 0.20f),
    REFINED( 1000000, 0.05f),
    SUPREME(10000000, 0.00f);
    //@formatter:on

    public final int maxCharge;
    public final float breakChance;

    Type(int maxCharge, float breakChance) {

      this.maxCharge = maxCharge;
      this.breakChance = breakChance;
    }
  }

  public static final String NBT_CHARGE = "ChaosCharge";

  public ItemChaosOrb() {

    super(Type.values().length, SilentGems.MOD_ID, Names.CHAOS_ORB);
    setMaxStackSize(1);
  }

  @Override
  public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean advanced) {

    final boolean shifted = Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)
        || Keyboard.isKeyDown(Keyboard.KEY_LSHIFT);

    String line = SilentGems.instance.localizationHelper.getMiscText("ChaosCharge");
    line = String.format(line, getCharge(stack), getMaxCharge(stack));
    list.add(line);

    line = SilentGems.instance.localizationHelper.getItemSubText(itemName, "breakChance");
    line = String.format(line, (int) (getType(stack).breakChance * 100));
    list.add(line);

    if (shifted) {
      for (String str : SilentGems.instance.localizationHelper.getItemDescriptionLines(itemName)) {
        list.add(TextFormatting.ITALIC + str);
      }
    }
  }

  @Override
  public void addRecipes() {

    String chaosEssence = "gemChaos";
    ItemStack chaosEssenceEnriched = ModItems.craftingMaterial.getStack(Names.CHAOS_ESSENCE_PLUS);
    ItemStack chaosEssenceCrystallized = ModItems.craftingMaterial
        .getStack(Names.CHAOS_ESSENCE_PLUS_2);
    String chaosEssenceShard = "nuggetChaos";

    for (Type type : Type.values()) {
      ItemStack result = new ItemStack(this, 1, type.ordinal());

      switch (type) {
        case FRAGILE:
          GameRegistry.addRecipe(new ShapedOreRecipe(result, "ccc", "cdc", "ccc", 'c', chaosEssence,
              'd', "gemDiamond"));
          break;
        case POTATO:
          GameRegistry.addRecipe(new ShapedOreRecipe(result, "ccc", "cpc", "ccc", 'c',
              chaosEssenceShard, 'p', Items.potato));
          break;
        case REFINED:
          GameRegistry.addShapedRecipe(result, " c ", "coc", " c ", 'c', chaosEssenceEnriched, 'o',
              new ItemStack(this, 1, Type.FRAGILE.ordinal()));
          break;
        case SUPREME:
          GameRegistry.addShapedRecipe(result, " c ", "coc", " c ", 'c', chaosEssenceCrystallized,
              'o', new ItemStack(this, 1, Type.REFINED.ordinal()));
          break;
        default:
          throw new NotImplementedException("No recipe for chaos orb of type " + type);
      }
    }
  }

  @Override
  public void getSubItems(Item item, CreativeTabs tab, List list) {

    for (Type type : Type.values()) {
      ItemStack empty = new ItemStack(item, 1, type.ordinal());
      ItemStack half = empty.copy();
      ItemStack full = empty.copy();

      NBTHelper.setTagInt(half, NBT_CHARGE, getMaxCharge(half) / 2);
      NBTHelper.setTagInt(full, NBT_CHARGE, this.getMaxCharge(full));

      list.add(empty);
      list.add(half);
      list.add(full);
    }
  }

  @Override
  public double getDurabilityForDisplay(ItemStack stack) {

    int energy = getCharge(stack);
    int capacity = getMaxCharge(stack);
    return (double) (capacity - energy) / (double) capacity;
  }

  @Override
  public boolean showDurabilityBar(ItemStack stack) {

    return getCharge(stack) < getMaxCharge(stack);
  }

  @Override
  public void onUpdate(ItemStack stack, World worldIn, Entity entityIn, int itemSlot,
      boolean isSelected) {

    if (worldIn.isRemote || worldIn.getTotalWorldTime() % 20 != 0) {
      return;
    }

    if (entityIn instanceof EntityPlayer) {
      EntityPlayer player = (EntityPlayer) entityIn;
      PlayerData data = PlayerDataHandler.get(player);

      int amount = extractCharge(stack, data.getChaosChargeSpeed(), true);
      amount = data.sendChaos(amount);
      extractCharge(stack, amount, false);

      // if (amount > 0) {
      // SilentGems.instance.logHelper.debug(amount, getCharge(stack), getMaxCharge(stack),
      // getBreakChance(stack));
      // }

      float breakTries = (int) (amount / PlayerData.CHAOS_MAX_TRANSFER);
      for (int i = 0; i < breakTries; ++i) {
        if (amount > 0 && SilentGems.instance.random.nextFloat() < getBreakChance(stack)) {
          breakOrb(stack, player);
        }
      }
    }
  }

  public Type getType(ItemStack stack) {

    int meta = stack.getItemDamage();
    if (meta >= 0 && meta < Type.values().length) {
      return Type.values()[meta];
    }
    return Type.POTATO;
  }

  public float getBreakChance(ItemStack stack) {

    float baseChance = getType(stack).breakChance;
    int charge = getCharge(stack);
    int maxCharge = getMaxCharge(stack);
    float percent = (float) charge / maxCharge;

    if (percent > 0.5f) {
      return 0f;
    }

    return baseChance * 2 * (0.5f - percent);
  }

  public void breakOrb(ItemStack stack, EntityPlayer player) {

    String line = SilentGems.instance.localizationHelper.getItemSubText(itemName, "break");
    int pieceCount = SilentGems.instance.random.nextInt(99000) + 1000;
    line = String.format(line, stack.getDisplayName(), pieceCount);
    player.addChatMessage(new TextComponentString(line));
    player.worldObj.playSound(null, player.posX, player.posY, player.posZ,
        SoundEvents.block_glass_break, SoundCategory.AMBIENT, 0.7f, -2.5f);
    PlayerHelper.removeItem(player, stack);
  }

  @Override
  public int receiveCharge(ItemStack stack, int maxReceive, boolean simulate) {

    int charge = getCharge(stack);
    int capacity = getMaxCharge(stack);
    int received = Math.min(capacity - charge, maxReceive);

    if (!simulate) {
      NBTHelper.setTagInt(stack, NBT_CHARGE, charge + received);
    }

    return received;
  }

  @Override
  public int extractCharge(ItemStack stack, int maxExtract, boolean simulate) {

    int charge = getCharge(stack);
    int capacity = getMaxCharge(stack);
    int extracted = Math.min(charge, maxExtract);

    if (!simulate) {
      NBTHelper.setTagInt(stack, NBT_CHARGE, charge - extracted);
    }

    return extracted;
  }

  @Override
  public int getCharge(ItemStack stack) {

    return NBTHelper.getTagInt(stack, NBT_CHARGE);
  }

  @Override
  public int getMaxCharge(ItemStack stack) {

    return getType(stack).maxCharge;
  }
}
