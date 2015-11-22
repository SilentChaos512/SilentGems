package net.silentchaos512.gems.item;

import java.util.List;

import org.lwjgl.input.Keyboard;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.silentchaos512.gems.configuration.Config;
import net.silentchaos512.gems.core.util.LocalizationHelper;
import net.silentchaos512.gems.core.util.LogHelper;
import net.silentchaos512.gems.core.util.RecipeHelper;
import net.silentchaos512.gems.energy.IChaosStorage;
import net.silentchaos512.gems.lib.EnumGem;
import net.silentchaos512.gems.lib.Names;
import net.silentchaos512.gems.lib.buff.ChaosBuff;

public class ChaosGem extends ItemSG implements IChaosStorage {

  public static final int MAX_STACK_DAMAGE = 100;
  public static final int CHEATY_GEM_ID = 42;

  public static final String NBT_CHARGE = "charge";
  public static final String NBT_ENABLED = "enabled";
  public static final String NBT_BUFF_LIST = "buff";
  public static final String NBT_BUFF_ID = "id";
  public static final String NBT_BUFF_LEVEL = "lvl";
  public static final String NBT_MINI_PYLON = "pylons";

  // This is a tag for players, but I put it here anyway.
  public static final String NBT_FLIGHT_TIME = "ChaosGem.FlightTime";

  private final int gemId;
  public final boolean isCheaty;

  public ChaosGem(int gemId) {

    this.gemId = gemId;
    this.isCheaty = gemId == CHEATY_GEM_ID;
    setMaxStackSize(1);
    setNoRepair();
    setMaxDamage(MAX_STACK_DAMAGE);
    setUnlocalizedName(Names.CHAOS_GEM + gemId);
    rarity = EnumRarity.RARE;
  }

  private int getTagInteger(ItemStack gem, String key) {

    if (gem != null) {
      if (!gem.hasTagCompound()) {
        return 0;
      } else {
        return gem.getTagCompound().getInteger(key);
      }
    }
    return 0;
  }

  private void setTagInteger(ItemStack gem, String key, int value) {

    if (gem != null) {
      if (!gem.hasTagCompound()) {
        gem.setTagCompound(new NBTTagCompound());
      }
      gem.getTagCompound().setInteger(key, value);
    }
  }

  private boolean getTagBoolean(ItemStack gem, String key) {

    if (gem != null) {
      if (!gem.hasTagCompound()) {
        return false;
      } else {
        return gem.getTagCompound().getBoolean(key);
      }
    }
    return false;
  }

  private void setTagBoolean(ItemStack gem, String key, boolean value) {

    if (gem != null) {
      if (!gem.hasTagCompound()) {
        gem.setTagCompound(new NBTTagCompound());
      }
      gem.getTagCompound().setBoolean(key, value);
    }
  }

  @Override
  public void getSubItems(Item item, CreativeTabs tab, List list) {

    ItemStack stack = new ItemStack(this);
    setTagInteger(stack, NBT_CHARGE, getMaxEnergyStored(stack));
    list.add(stack);
  }

  @Override
  public void addRecipes() {

    if (gemId != CHEATY_GEM_ID) {
      RecipeHelper.addSurroundOre(new ItemStack(this), EnumGem.get(gemId).getBlockOreName(),
          CraftingMaterial.getStack(Names.CHAOS_ESSENCE_PLUS));
    }
  }

  @Override
  public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean advanced) {

    String s;

    if (this.getBuffList(stack).tagCount() == 0) {
      // Information on how to use.
      for (int i = 1; i < 3; ++i) {
        s = LocalizationHelper.getItemDescription(Names.CHAOS_GEM, i);
        list.add(EnumChatFormatting.DARK_GRAY + s);
      }
      return;
    }

    boolean shifted = Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)
        || Keyboard.isKeyDown(Keyboard.KEY_RSHIFT);

    boolean enabled = getTagBoolean(stack, NBT_ENABLED);
    int energy = getEnergyStored(stack);
    int capacity = getMaxEnergyStored(stack);

    // Enabled/disabled
    if (enabled) {
      s = LocalizationHelper.getOtherItemKey(Names.CHAOS_GEM, "Enabled");
      list.add(EnumChatFormatting.GREEN + s);
    } else {
      s = LocalizationHelper.getOtherItemKey(Names.CHAOS_GEM, "Disabled");
      list.add(EnumChatFormatting.RED + s);
    }

    if (this.isCheaty) {
      // Cheaty gems don't display charge.
      s = LocalizationHelper.getOtherItemKey(Names.CHAOS_GEM, "Cheaty");
      list.add(EnumChatFormatting.DARK_GRAY + s);
    } else {
      // Charge level
      s = String.format("%d / %d", energy, capacity);
      list.add(EnumChatFormatting.YELLOW + s);

      if (shifted) {
        // Charge change rate
        s = LocalizationHelper.getOtherItemKey(Names.CHAOS_GEM, "CostPerTick");
        s = String.format(s, this.getTotalChargeDrain(stack, player));
        list.add(EnumChatFormatting.DARK_GRAY + s);

        // Mini pylon count
        int pylonCount = getTagInteger(stack, NBT_MINI_PYLON);
        s = LocalizationHelper.getOtherItemKey(Names.CHAOS_GEM, "MiniPylon");
        s = String.format("%s: %d", s, pylonCount);
        list.add(EnumChatFormatting.DARK_GRAY + s);
      }
    }

    NBTTagList buffList = getBuffList(stack);
    if (buffList != null && buffList.tagCount() != 0) {
      // Display list of effects.
      NBTTagCompound t;
      int id, lvl;
      for (int i = 0; i < buffList.tagCount(); ++i) {
        t = (NBTTagCompound) buffList.getCompoundTagAt(i);
        id = t.getShort(NBT_BUFF_ID);
        lvl = t.getShort(NBT_BUFF_LEVEL);
        if (id >= 0 && id < ChaosBuff.values().length) {
          list.add(ChaosBuff.values()[id].getDisplayName(lvl));
        } else {
          s = LocalizationHelper.getOtherItemKey(Names.CHAOS_RUNE, "BadRune");
          list.add(s);
        }
      }
    } else {
      // Information on how to use.
      for (int i = 1; i < 3; ++i) {
        s = LocalizationHelper.getItemDescription(Names.CHAOS_GEM, i);
        list.add(EnumChatFormatting.DARK_GRAY + s);
      }
    }
  }

  @Override
  public int getDamage(ItemStack stack) {

    int value = (int) (MAX_STACK_DAMAGE * getDurabilityForDisplay(stack));
    return MathHelper.clamp_int(value, 0, MAX_STACK_DAMAGE - 1);
  }

  @Override
  public void setDamage(ItemStack stack, int damage) {

  }

  /**
   * Fixes bobbing effect when charge is draining.
   */
  @Override
  public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack,
      boolean slotChanged) {

    return slotChanged;
  }

  @Override
  public double getDurabilityForDisplay(ItemStack stack) {

    int energy = getEnergyStored(stack);
    int capacity = getMaxEnergyStored(stack);
    return (double) (capacity - energy) / (double) capacity;
  }

  @Override
  public boolean showDurabilityBar(ItemStack stack) {

    return getEnergyStored(stack) < getMaxEnergyStored(stack);
  }

  @Override
  public boolean hasEffect(ItemStack stack) {

    return isEnabled(stack);
  }

  private void applyEffects(ItemStack stack, EntityPlayer player) {

    NBTTagList list = getBuffList(stack);
    NBTTagCompound tag;
    short id, lvl;
    for (int i = 0; i < list.tagCount(); ++i) {
      tag = (NBTTagCompound) list.getCompoundTagAt(i);
      id = tag.getShort(NBT_BUFF_ID);
      lvl = tag.getShort(NBT_BUFF_LEVEL);
      if (id >= 0 && id < ChaosBuff.values().length) {
        ChaosBuff.values()[id].apply(player, lvl);
      }
    }
  }

  private void removeEffects(ItemStack stack, EntityPlayer player) {

    NBTTagList list = getBuffList(stack);
    NBTTagCompound tag;
    short id, lvl;
    for (int i = 0; i < list.tagCount(); ++i) {
      tag = (NBTTagCompound) list.getCompoundTagAt(i);
      id = tag.getShort(NBT_BUFF_ID);
      if (id >= 0 && id < ChaosBuff.values().length) {
        ChaosBuff.values()[id].remove(player);
      }
    }
  }

  public static void removeFlight(EntityPlayer player) {

    if (!player.capabilities.isCreativeMode) {
      player.capabilities.allowFlying = false;
      player.capabilities.isFlying = false;
      player.fallDistance = 0;
    }
  }

  public NBTTagList getBuffList(ItemStack stack) {

    if (stack != null) {
      NBTTagCompound root = stack.getTagCompound();

      if (root == null) {
        root = new NBTTagCompound();
      }

      if (root.hasKey(NBT_BUFF_LIST)) {
        return (NBTTagList) root.getTag(NBT_BUFF_LIST);
      } else {
        root.setTag(NBT_BUFF_LIST, new NBTTagList());
        return (NBTTagList) root.getTag(NBT_BUFF_LIST);
      }
    }

    return null;
  }

  public boolean canAddBuff(ItemStack stack, ChaosBuff buff) {

    if (buff == null || stack == null) {
      return false;
    }
    // Get the level of this buff currently on the gem (0 if none).
    int level = getBuffLevel(stack, buff);
    // Don't allow more than a certain number of buffs per gem.
    if (level == 0 && getBuffCount(stack) >= Config.CHAOS_GEM_MAX_BUFFS) {
      return false;
    }
    // Limit level to max.
    return level < buff.maxLevel;
  }

  public void addBuff(ItemStack stack, ChaosBuff buff) {

    int level = getBuffLevel(stack, buff);

    NBTTagList list = getBuffList(stack);
    if (level == 0) {
      // Buff not already on list, add it.
      NBTTagCompound tag = new NBTTagCompound();
      tag.setShort(NBT_BUFF_ID, (short) buff.id);
      tag.setShort(NBT_BUFF_LEVEL, (short) 1);
      list.appendTag(tag);
    } else {
      // Increase buff level.
      NBTTagCompound tag;
      for (int i = 0; i < list.tagCount(); ++i) {
        tag = (NBTTagCompound) list.getCompoundTagAt(i);
        int id = tag.getShort(NBT_BUFF_ID);
        if (id == buff.id) {
          level = tag.getShort(NBT_BUFF_LEVEL);
          tag.setShort(NBT_BUFF_LEVEL, (short) (level + 1));
        }
      }
    }
  }

  public int getBuffCount(ItemStack stack) {

    NBTTagList buffList = getBuffList(stack);
    return buffList == null ? 0 : buffList.tagCount();
  }

  public int getBuffLevel(ItemStack stack, ChaosBuff buff) {

    NBTTagList buffList = getBuffList(stack);
    if (buffList != null) {
      for (int i = 0; i < buffList.tagCount(); ++i) {
        NBTTagCompound tag = buffList.getCompoundTagAt(i);
        if (tag.getShort(NBT_BUFF_ID) == buff.id) {
          return tag.getShort(NBT_BUFF_LEVEL);
        }
      }
    }

    return 0;
  }

  public boolean canAddMiniPylon(ItemStack stack) {

    int pylonCount = getTagInteger(stack, NBT_MINI_PYLON);
    return pylonCount < Config.CHAOS_GEM_MAX_MINI_PYLON;
  }

  public void addMiniPylon(ItemStack stack) {

    int pylonCount = getTagInteger(stack, NBT_MINI_PYLON);
    setTagInteger(stack, NBT_MINI_PYLON, pylonCount + 1);
  }

  public int getTotalChargeDrain(ItemStack stack, EntityPlayer player) {

    int drain = 0;

    if (stack != null) {
      NBTTagList list = getBuffList(stack);
      NBTTagCompound tag;
      int id;
      int level;
      boolean isEffectFlight;
      for (int i = 0; i < list.tagCount(); ++i) {
        tag = (NBTTagCompound) list.getCompoundTagAt(i);
        id = tag.getShort(NBT_BUFF_ID);
        level = tag.getShort(NBT_BUFF_LEVEL);
        isEffectFlight = id == ChaosBuff.FLIGHT.id;
        if ((isEffectFlight && player.capabilities.isFlying) || !isEffectFlight) {
          drain += ChaosBuff.values()[id].getCostPerTick(level);
        } else if (isEffectFlight && player.motionY < -0.88) {
          // Flight fall damage protection
          drain += ChaosBuff.values()[id].getCostPerTick(level) / 10;
        }
      }
    }

    return drain;
  }

  public int getSelfRechargeAmount(ItemStack stack) {

    return getTagInteger(stack, NBT_MINI_PYLON);
  }

  public boolean isEnabled(ItemStack stack) {

    return getTagBoolean(stack, NBT_ENABLED);
  }

  public void setEnabled(ItemStack stack, boolean value) {

    setTagBoolean(stack, NBT_ENABLED, value);
  }

  @Override
  public void onUpdate(ItemStack stack, World world, Entity entity, int par4, boolean par5) {

    if (world.isRemote || !(entity instanceof EntityPlayer)) {
      return;
    }

    EntityPlayer player = (EntityPlayer) entity;
    boolean enabled = this.isEnabled(stack);

    // Mini pylons (self recharge)
    receiveEnergy(stack, getSelfRechargeAmount(stack), false);

    // Apply effects?
    if (enabled) {
      applyEffects(stack, player);
    } else {
      // removeEffects(stack, player);
      return;
    }

    // Cheaty gem or creative? Don't do charge
    if (this.isCheaty) {
      return;
    }

    // Update charge level
    extractEnergy(stack, this.getTotalChargeDrain(stack, player), false);
    // Disable if out of charge.
    if (getEnergyStored(stack) <= 0) {
      setEnabled(stack, false);
      removeEffects(stack, player);
    }
  }

  @Override
  public boolean onDroppedByPlayer(ItemStack stack, EntityPlayer player) {

    // Set disabled
    setEnabled(stack, false);
    removeEffects(stack, player);
    return true;
  }

  @Override
  public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player) {

    // Enable/disable
    if (getEnergyStored(stack) > 0) {
      setEnabled(stack, !isEnabled(stack));
      if (isEnabled(stack)) {
        applyEffects(stack, player);
      } else {
        removeEffects(stack, player);
      }
    }

    return stack;
  }

  @Override
  public int receiveEnergy(ItemStack stack, int maxReceive, boolean simulate) {

    int energy = getEnergyStored(stack);
    int capacity = getMaxEnergyStored(stack);
    int received = Math.min(capacity - energy, maxReceive);

    if (!simulate) {
      setTagInteger(stack, NBT_CHARGE, energy + received);
    }

    return received;
  }

  @Override
  public int extractEnergy(ItemStack stack, int maxExtract, boolean simulate) {

    int energy = this.getEnergyStored(stack);
    int capacity = this.getMaxEnergyStored(stack);
    int extracted = Math.min(energy, maxExtract);

    if (!simulate) {
      setTagInteger(stack, NBT_CHARGE, energy - extracted);
    }

    return extracted;
  }

  @Override
  public int getEnergyStored(ItemStack stack) {

    return getTagInteger(stack, NBT_CHARGE);
  }

  @Override
  public int getMaxEnergyStored(ItemStack stack) {

    return 1000000;
  }
}
