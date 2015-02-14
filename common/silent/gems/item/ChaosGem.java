package silent.gems.item;

import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import silent.gems.block.ChaosEssenceBlock;
import silent.gems.configuration.Config;
import silent.gems.core.registry.SRegistry;
import silent.gems.core.util.LocalizationHelper;
import silent.gems.core.util.RecipeHelper;
import silent.gems.lib.EnumGem;
import silent.gems.lib.Names;
import silent.gems.lib.Strings;
import silent.gems.lib.buff.ChaosBuff;

public class ChaosGem extends ItemSG {

  public final static int MAX_STACK_DAMAGE = 10;
  public final static int CHEATY_GEM_ID = 42;

  private final int gemId;
  public final boolean isCheaty;

  public ChaosGem(int gemId) {

    super(EnumGem.all().length + 1);

    this.gemId = gemId;
    this.isCheaty = gemId == CHEATY_GEM_ID;
    setMaxStackSize(1);
    setUnlocalizedName(Names.CHAOS_GEM + gemId);
    setMaxDamage(MAX_STACK_DAMAGE);
    rarity = EnumRarity.RARE;
  }

  public static void addBuff(ItemStack stack, ChaosBuff buff) {

    int k = getBuffLevel(stack, buff);

    NBTTagCompound tags = stack.getTagCompound();

    if (tags == null) {
      stack.setTagCompound(new NBTTagCompound());
    }
    if (!tags.hasKey(Strings.CHAOS_GEM_BUFF_LIST)) {
      tags.setTag(Strings.CHAOS_GEM_BUFF_LIST, new NBTTagList());
    }

    NBTTagList list = (NBTTagList) tags.getTag(Strings.CHAOS_GEM_BUFF_LIST);
    if (k == 0) {
      // Buff not already on list, add it.
      NBTTagCompound tag = new NBTTagCompound();
      tag.setShort(Strings.CHAOS_GEM_BUFF_ID, (short) buff.id);
      tag.setShort(Strings.CHAOS_GEM_BUFF_LEVEL, (short) 1);
      list.appendTag(tag);
    } else {
      // Increase buff level.
      NBTTagCompound tag;
      for (int i = 0; i < list.tagCount(); ++i) {
        tag = (NBTTagCompound) list.getCompoundTagAt(i);
        k = tag.getShort(Strings.CHAOS_GEM_BUFF_ID);
        if (k == buff.id) {
          k = tag.getShort(Strings.CHAOS_GEM_BUFF_LEVEL);
          tag.setShort(Strings.CHAOS_GEM_BUFF_LEVEL, (short) (k + 1));
        }
      }
    }

    stack.setTagCompound(tags);
  }

  @Override
  public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean par4) {

    NBTTagCompound tags = stack.getTagCompound();

    if (tags == null) {
      // Information on how to use.
      list.add(EnumChatFormatting.DARK_GRAY
          + LocalizationHelper.getItemDescription(Names.CHAOS_GEM, 0));
      return;
    }

    boolean enabled = tags.getBoolean(Strings.CHAOS_GEM_ENABLED);

    if (enabled) {
      list.add(EnumChatFormatting.GREEN
          + LocalizationHelper.getOtherItemKey(Names.CHAOS_GEM, "Enabled"));
    } else {
      list.add(EnumChatFormatting.RED
          + LocalizationHelper.getOtherItemKey(Names.CHAOS_GEM, "Disabled"));
    }

    if (this.isCheaty) {
      list.add(EnumChatFormatting.DARK_GRAY
          + LocalizationHelper.getOtherItemKey(Names.CHAOS_GEM, "Cheaty"));
    } else {
      // Charge level
      int k = tags.getInteger(Strings.CHAOS_GEM_CHARGE);
      list.add(EnumChatFormatting.YELLOW + String.format("%d / %d", k, getMaxChargeLevel(stack)));

      // Charge change rate
      k = enabled ? -getTotalChargeDrain(stack) : getRechargeAmount(stack);
      list.add(EnumChatFormatting.DARK_GRAY + (k >= 0 ? "+" : "") + k + " "
          + LocalizationHelper.getOtherItemKey(Names.CHAOS_GEM, "ChargePerSecond"));
    }
    if (tags.hasKey(Strings.CHAOS_GEM_BUFF_LIST)) {
      // Display list of effects.
      NBTTagList tagList = (NBTTagList) tags.getTag(Strings.CHAOS_GEM_BUFF_LIST);
      NBTTagCompound t;
      int id, lvl;
      for (int i = 0; i < tagList.tagCount(); ++i) {
        t = (NBTTagCompound) tagList.getCompoundTagAt(i);
        id = t.getShort(Strings.CHAOS_GEM_BUFF_ID);
        lvl = t.getShort(Strings.CHAOS_GEM_BUFF_LEVEL);
        list.add(ChaosBuff.all.get(id).getDisplayName(lvl));
      }
    } else {
      // Information on how to use.
      list.add(EnumChatFormatting.DARK_GRAY
          + LocalizationHelper.getItemDescription(Names.CHAOS_GEM, 0));
    }
  }

  @Override
  public void addRecipes() {

    if (this.gemId != CHEATY_GEM_ID) {
      ItemStack chaosEssence = ChaosEssence.getByType(ChaosEssenceBlock.EnumType.REFINED);
      RecipeHelper.addSurround(new ItemStack(this),
          new ItemStack(SRegistry.getBlock(Names.GEM_BLOCK), 1, gemId), chaosEssence);
    }
  }

  private void applyEffects(ItemStack stack, EntityPlayer player) {

    NBTTagCompound tags = stack.getTagCompound();

    if (tags == null) {
      tags = new NBTTagCompound();
    }
    if (!tags.hasKey(Strings.CHAOS_GEM_BUFF_LIST)) {
      tags.setTag(Strings.CHAOS_GEM_BUFF_LIST, new NBTTagList());
    }
    if (this.gemId == CHEATY_GEM_ID && !tags.hasKey(Strings.CHAOS_GEM_CHEATY)) {
      tags.setBoolean(Strings.CHAOS_GEM_CHEATY, true);
    }

    NBTTagList list = (NBTTagList) tags.getTag(Strings.CHAOS_GEM_BUFF_LIST);
    NBTTagCompound tag;
    short id, lvl;
    for (int i = 0; i < list.tagCount(); ++i) {
      tag = (NBTTagCompound) list.getCompoundTagAt(i);
      id = tag.getShort(Strings.CHAOS_GEM_BUFF_ID);
      lvl = tag.getShort(Strings.CHAOS_GEM_BUFF_LEVEL);
      ChaosBuff.all.get(id).apply(player, lvl);
    }

    stack.setTagCompound(tags);
  }

  public static boolean canAddBuff(ItemStack stack, ChaosBuff buff) {

    if (buff == null || stack == null) {
      return false;
    }
    // Get the level of this buff currently on the gem (0 if none).
    int k = getBuffLevel(stack, buff);
    // Don't allow more than a certain number of buffs per gem.
    if (k == 0 && getBuffCount(stack) >= Config.CHAOS_GEM_MAX_BUFFS.value) {
      return false;
    }
    // Limit level to max.
    return k < buff.maxLevel;
  }

  public void doTick(ItemStack stack, EntityPlayer player) {

    if (player.worldObj.isRemote) {
      return;
    }

    NBTTagCompound tags = stack.getTagCompound();

    if (tags == null) {
      tags = new NBTTagCompound();
    }
    if (!tags.hasKey(Strings.CHAOS_GEM_BUFF_LIST)) {
      tags.setTag(Strings.CHAOS_GEM_BUFF_LIST, new NBTTagList());
    }

    boolean enabled = tags.getBoolean(Strings.CHAOS_GEM_ENABLED);

    // Apply effects?
    if (tags.hasKey(Strings.CHAOS_GEM_BUFF_LIST)) {
      if (enabled) {
        applyEffects(stack, player);
      } else {
        removeEffects(stack, player);
      }
    }

    // Cheaty gem? Don't do charge
    if (this.isCheaty) {
      return;
    }

    // Update charge level
    final int maxCharge = getMaxChargeLevel(stack);
    if (!tags.hasKey(Strings.CHAOS_GEM_CHARGE)) {
      tags.setInteger(Strings.CHAOS_GEM_CHARGE, Config.CHAOS_GEM_MAX_CHARGE.value);
    }
    int charge = tags.getInteger(Strings.CHAOS_GEM_CHARGE);
    if (enabled) {
      charge -= getTotalChargeDrain(stack);
      // Disable if out of charge.
      if (charge <= 0) {
        charge = 0;
        tags.setBoolean(Strings.CHAOS_GEM_ENABLED, false);
      }
    } else if (charge < maxCharge) {
      charge += getRechargeAmount(stack);
      if (charge > maxCharge) {
        charge = maxCharge;
      }
    } else if (charge > maxCharge) {
      charge = maxCharge;
    }

    stack.setTagCompound(tags);
    stack = setChargeLevel(stack, charge);
  }

  public static int getBuffCount(ItemStack stack) {

    // Does buff tag list exist?
    NBTTagCompound tags = stack.getTagCompound();
    if (stack == null || tags == null || !tags.hasKey(Strings.CHAOS_GEM_BUFF_LIST)) {
      return 0;
    } else {
      NBTTagList list = (NBTTagList) tags.getTag(Strings.CHAOS_GEM_BUFF_LIST);
      return list.tagCount();
    }
  }

  public static int getBuffLevel(ItemStack stack, ChaosBuff buff) {

    // Does buff tag list exist?
    NBTTagCompound tags = stack.getTagCompound();
    if (stack == null || buff == null || tags == null || !tags.hasKey(Strings.CHAOS_GEM_BUFF_LIST)) {
      return 0;
    } else {
      NBTTagList list = (NBTTagList) tags.getTag(Strings.CHAOS_GEM_BUFF_LIST);

      // Does the specified buff exist on list?
      for (int i = 0; i < list.tagCount(); ++i) {
        if (((NBTTagCompound) list.getCompoundTagAt(i)).getShort(Strings.CHAOS_GEM_BUFF_ID) == buff.id) {
          return ((NBTTagCompound) list.getCompoundTagAt(i)).getShort(Strings.CHAOS_GEM_BUFF_LEVEL);
        }
      }

      // Not there
      return 0;
    }
  }

  public static int getChargeLevel(ItemStack stack) {

    NBTTagCompound tags = stack.getTagCompound();
    if (stack != null && tags != null && tags.hasKey(Strings.CHAOS_GEM_CHARGE)) {
      return tags.getInteger(Strings.CHAOS_GEM_CHARGE);
    } else {
      return -1;
    }
  }

  public static int getMaxChargeLevel(ItemStack stack) {

    final int maxCharge = Config.CHAOS_GEM_MAX_CHARGE.value;
    NBTTagCompound tags = stack.getTagCompound();

    if (stack != null && tags != null) {
      int capacityLevel = getBuffLevel(stack, ChaosBuff.getBuffByName(ChaosBuff.CAPACITY));
      return maxCharge + capacityLevel * maxCharge / 4;
    } else {
      return maxCharge;
    }
  }

  public static int getRechargeAmount(ItemStack stack) {

    final int amount = Config.CHAOS_GEM_RECHARGE_RATE.value;
    NBTTagCompound tags = stack.getTagCompound();

    if (stack != null && tags != null) {
      int boosterLevel = getBuffLevel(stack, ChaosBuff.getBuffByName(ChaosBuff.BOOSTER));
      return amount + boosterLevel * amount / 4;
    } else {
      return amount;
    }
  }

  public static int getTotalChargeDrain(ItemStack stack) {

    int drain = 0;
    NBTTagCompound tags = stack.getTagCompound();

    if (stack != null & tags != null) {
      NBTTagList list = (NBTTagList) tags.getTag(Strings.CHAOS_GEM_BUFF_LIST);
      NBTTagCompound tag;
      short id;
      for (int i = 0; i < list.tagCount(); ++i) {
        tag = (NBTTagCompound) list.getCompoundTagAt(i);
        id = tag.getShort(Strings.CHAOS_GEM_BUFF_ID);
        drain += ChaosBuff.all.get(id).cost;
      }
    }

    return drain;
  }

  @Override
  public boolean hasEffect(ItemStack stack) {

    NBTTagCompound tags = stack.getTagCompound();
    return tags != null && tags.getBoolean(Strings.CHAOS_GEM_ENABLED);
  }

  public static boolean isEnabled(ItemStack stack) {

    NBTTagCompound tags = stack.getTagCompound();
    return stack != null && tags != null && tags.hasKey(Strings.CHAOS_GEM_ENABLED)
        && tags.getBoolean(Strings.CHAOS_GEM_ENABLED);
  }

  @Override
  public boolean onDroppedByPlayer(ItemStack stack, EntityPlayer player) {

    // Set disabled
    NBTTagCompound tags = stack.getTagCompound();
    if (tags != null) {
      tags.setBoolean(Strings.CHAOS_GEM_ENABLED, false);
      removeEffects(stack, player);
    }
    stack.setTagCompound(tags);

    return true;
  }

  @Override
  public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player) {

    NBTTagCompound tags = stack.getTagCompound();

    if (tags == null) {
      stack.setTagCompound(new NBTTagCompound());
    }
    if (!tags.hasKey(Strings.CHAOS_GEM_ENABLED)) {
      tags.setBoolean(Strings.CHAOS_GEM_ENABLED, false);
    }
    if (!tags.hasKey(Strings.CHAOS_GEM_CHARGE)) {
      tags.setInteger(Strings.CHAOS_GEM_CHARGE, Config.CHAOS_GEM_MAX_CHARGE.value);
    }

    // Enable/disable
    boolean b = tags.getBoolean(Strings.CHAOS_GEM_ENABLED);
    if (tags.getInteger(Strings.CHAOS_GEM_CHARGE) > 0) {
      tags.setBoolean(Strings.CHAOS_GEM_ENABLED, !b);
      if (b) {
        removeEffects(stack, player);
      } else {
        applyEffects(stack, player);
      }
    }

    stack.setTagCompound(tags);
    return stack;
  }

  private void removeEffects(ItemStack stack, EntityPlayer player) {

    NBTTagCompound tags = stack.getTagCompound();

    if (tags == null) {
      tags = new NBTTagCompound();
    }
    if (!tags.hasKey(Strings.CHAOS_GEM_BUFF_LIST)) {
      tags.setTag(Strings.CHAOS_GEM_BUFF_LIST, new NBTTagList());
    }

    NBTTagList list = (NBTTagList) tags.getTag(Strings.CHAOS_GEM_BUFF_LIST);
    NBTTagCompound tag;
    short id, lvl;
    for (int i = 0; i < list.tagCount(); ++i) {
      tag = (NBTTagCompound) list.getCompoundTagAt(i);
      id = tag.getShort(Strings.CHAOS_GEM_BUFF_ID);
      ChaosBuff.all.get(id).remove(player);
    }

    stack.setTagCompound(tags);
  }

  public static ItemStack setChargeLevel(ItemStack stack, int charge) {

    NBTTagCompound tags = stack.getTagCompound();

    if (stack != null && tags != null && tags.hasKey(Strings.CHAOS_GEM_CHARGE)) {
      int maxCharge = getMaxChargeLevel(stack);
      charge = MathHelper.clamp_int(charge, 0, maxCharge);
      int damage = MAX_STACK_DAMAGE - (MAX_STACK_DAMAGE * charge / maxCharge);

      // Save actual charge level in NBT. Item damage is only for display purposes.
      tags.setInteger(Strings.CHAOS_GEM_CHARGE, charge);
      stack.setItemDamage(damage);

      stack.setTagCompound(tags);
    }

    return stack;
  }
}
