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
import net.silentchaos512.gems.core.registry.SRegistry;
import net.silentchaos512.gems.core.util.LocalizationHelper;
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
  // public static final String NBT_CHEATY = "cheaty";
  
  // This is a tag for players, but I put it here anyway.
  public static final String NBT_FLIGHT_TIME = "ChaosGem.FlightTime";

  private final int gemId;
  public final boolean isCheaty;

  public ChaosGem(int gemId) {

    this.gemId = gemId;
    this.isCheaty = gemId == CHEATY_GEM_ID;
    this.setMaxStackSize(1);
    this.setNoRepair();
    this.setMaxDamage(MAX_STACK_DAMAGE);
    this.setUnlocalizedName(Names.CHAOS_GEM + gemId);
    this.rarity = EnumRarity.rare;
  }

  @Override
  public void getSubItems(Item item, CreativeTabs tab, List list) {

    ItemStack stack = new ItemStack(this);
    stack.setTagCompound(new NBTTagCompound());
    stack.stackTagCompound.setInteger(NBT_CHARGE, this.getMaxEnergyStored(stack));
    list.add(stack);
  }

  @Override
  public void addRecipes() {

    if (this.gemId != CHEATY_GEM_ID) {
      RecipeHelper.addSurroundOre(new ItemStack(this), EnumGem.all()[gemId].getBlockOreName(),
          CraftingMaterial.getStack(Names.CHAOS_ESSENCE_PLUS));
    }
  }

  @Override
  public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean advanced) {

    if (this.getBuffList(stack).tagCount() == 0) {
      // Information on how to use.
      list.add(EnumChatFormatting.DARK_GRAY
          + LocalizationHelper.getItemDescription(Names.CHAOS_GEM, 0));
      return;
    }

    boolean shifted = Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)
        || Keyboard.isKeyDown(Keyboard.KEY_RSHIFT);
    String str;

    boolean enabled = stack.stackTagCompound.getBoolean(NBT_ENABLED);
    int energy = this.getEnergyStored(stack);
    int capacity = this.getMaxEnergyStored(stack);

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
      // TODO: Formatting?
      list.add(EnumChatFormatting.YELLOW + String.format("%d / %d", energy, capacity));

      // Charge change rate
      str = LocalizationHelper.getOtherItemKey(Names.CHAOS_GEM, "CostPerTick");
      str = String.format(str, this.getTotalChargeDrain(stack, player));
      list.add(EnumChatFormatting.DARK_GRAY + str);
    }

    if (stack.stackTagCompound.hasKey(NBT_BUFF_LIST)) {
      // Display list of effects.
      NBTTagList tags = (NBTTagList) stack.stackTagCompound.getTag(NBT_BUFF_LIST);
      NBTTagCompound t;
      int id, lvl;
      for (int i = 0; i < tags.tagCount(); ++i) {
        t = (NBTTagCompound) tags.getCompoundTagAt(i);
        id = t.getShort(NBT_BUFF_ID);
        lvl = t.getShort(NBT_BUFF_LEVEL);
        if (id >= 0 && id < ChaosBuff.values().length) {
          list.add(ChaosBuff.values()[id].getDisplayName(lvl));
        } else {
          str = LocalizationHelper.getOtherItemKey(Names.CHAOS_RUNE, "BadRune");
          list.add(str);
        }
      }
    } else {
      // Information on how to use.
      list.add(EnumChatFormatting.DARK_GRAY
          + LocalizationHelper.getItemDescription(Names.CHAOS_GEM, 0));
    }
  }

  @Override
  public int getDamage(ItemStack stack) {

    int value = (int) (MAX_STACK_DAMAGE * this.getDurabilityForDisplay(stack));
    return MathHelper.clamp_int(value, 0, MAX_STACK_DAMAGE - 1);
  }

  @Override
  public void setDamage(ItemStack stack, int damage) {

  }

  @Override
  public double getDurabilityForDisplay(ItemStack stack) {

    int energy = this.getEnergyStored(stack);
    int capacity = this.getMaxEnergyStored(stack);
    return (double) (capacity - energy) / (double) capacity;
  }

  @Override
  public boolean showDurabilityBar(ItemStack stack) {

    return this.getEnergyStored(stack) < this.getMaxEnergyStored(stack);
  }

  @Override
  public boolean hasEffect(ItemStack stack, int pass) {

    return this.isEnabled(stack);
  }

  private void applyEffects(ItemStack stack, EntityPlayer player) {

    NBTTagList list = this.getBuffList(stack);
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

    NBTTagList list = this.getBuffList(stack);
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
      if (stack.stackTagCompound == null) {
        stack.setTagCompound(new NBTTagCompound());
      }

      if (stack.stackTagCompound.hasKey(NBT_BUFF_LIST)) {
        return (NBTTagList) stack.stackTagCompound.getTag(NBT_BUFF_LIST);
      } else {
        stack.stackTagCompound.setTag(NBT_BUFF_LIST, new NBTTagList());
        return (NBTTagList) stack.stackTagCompound.getTag(NBT_BUFF_LIST);
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
    if (level == 0 && getBuffCount(stack) >= Config.CHAOS_GEM_MAX_BUFFS.value) {
      return false;
    }
    // Limit level to max.
    return level < buff.maxLevel;
  }

  public void addBuff(ItemStack stack, ChaosBuff buff) {

    int level = getBuffLevel(stack, buff);

    NBTTagList list = this.getBuffList(stack);
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

    // Does buff tag list exist?
    if (stack == null || stack.stackTagCompound == null
        || !stack.stackTagCompound.hasKey(NBT_BUFF_LIST)) {
      return 0;
    } else {
      NBTTagList list = (NBTTagList) stack.stackTagCompound.getTag(NBT_BUFF_LIST);
      return list.tagCount();
    }
  }

  public int getBuffLevel(ItemStack stack, ChaosBuff buff) {

    // Does buff tag list exist?
    if (stack == null || buff == null || stack.stackTagCompound == null
        || !stack.stackTagCompound.hasKey(NBT_BUFF_LIST)) {
      return 0;
    } else {
      NBTTagList list = (NBTTagList) stack.stackTagCompound.getTag(NBT_BUFF_LIST);

      // Does the specified buff exist on list?
      for (int i = 0; i < list.tagCount(); ++i) {
        NBTTagCompound tag = list.getCompoundTagAt(i);
        if (tag.getShort(NBT_BUFF_ID) == buff.id) {
          return tag.getShort(NBT_BUFF_LEVEL);
        }
      }

      // Not there
      return 0;
    }
  }

  public int getTotalChargeDrain(ItemStack stack, EntityPlayer player) {

    int drain = 0;

    if (stack != null) {
      NBTTagList list = this.getBuffList(stack);
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
          drain += ChaosBuff.values()[id].getCostPerTick(level) / 10;
        }
      }
    }

    return drain;
  }

  public boolean isEnabled(ItemStack stack) {

    return stack != null && stack.stackTagCompound != null
        && stack.stackTagCompound.hasKey(NBT_ENABLED)
        && stack.stackTagCompound.getBoolean(NBT_ENABLED);
  }

  public void setEnabled(ItemStack stack, boolean value) {

    if (stack != null) {
      if (stack.stackTagCompound == null) {
        stack.setTagCompound(new NBTTagCompound());
      }

      stack.stackTagCompound.setBoolean(NBT_ENABLED, value);
    }
  }

  @Override
  public void onUpdate(ItemStack stack, World world, Entity entity, int par4, boolean par5) {

    if (world.isRemote || !(entity instanceof EntityPlayer)) {
      return;
    }

    EntityPlayer player = (EntityPlayer) entity;
    boolean enabled = this.isEnabled(stack);

    // Apply effects?
    if (enabled) {
      applyEffects(stack, player);
    } else {
//      removeEffects(stack, player);
      return;
    }

    // Cheaty gem or creative? Don't do charge
    if (this.isCheaty) {
      return;
    }

    // Update charge level
    this.extractEnergy(stack, this.getTotalChargeDrain(stack, player), false);
    // Disable if out of charge.
    if (this.getEnergyStored(stack) <= 0) {
      this.setEnabled(stack, false);
      this.removeEffects(stack, player);
    }
    // TODO: Self-recharging gem upgrades?
  }

  @Override
  public boolean onDroppedByPlayer(ItemStack stack, EntityPlayer player) {

    // Set disabled
    this.setEnabled(stack, false);
    this.removeEffects(stack, player);
    return true;
  }

  @Override
  public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player) {

    // Enable/disable
    if (this.getEnergyStored(stack) > 0) {
      this.setEnabled(stack, !this.isEnabled(stack));
      if (this.isEnabled(stack)) {
        this.applyEffects(stack, player);
      } else {
        this.removeEffects(stack, player);
      }
    }

    return stack;
  }

  @Override
  public int receiveEnergy(ItemStack stack, int maxReceive, boolean simulate) {

    int energy = this.getEnergyStored(stack);
    int capacity = this.getMaxEnergyStored(stack);
    int received = Math.min(capacity - energy, maxReceive);

    if (!simulate) {
      stack.stackTagCompound.setInteger(NBT_CHARGE, energy + received);
    }

    return received;
  }

  @Override
  public int extractEnergy(ItemStack stack, int maxExtract, boolean simulate) {

    int energy = this.getEnergyStored(stack);
    int capacity = this.getMaxEnergyStored(stack);
    int extracted = Math.min(energy, maxExtract);

    if (!simulate) {
      stack.stackTagCompound.setInteger(NBT_CHARGE, energy - extracted);
    }

    return extracted;
  }

  @Override
  public int getEnergyStored(ItemStack stack) {

    if (stack.stackTagCompound == null) {
      stack.setTagCompound(new NBTTagCompound());
    }

    if (stack.stackTagCompound.hasKey(NBT_CHARGE)) {
      return stack.stackTagCompound.getInteger(NBT_CHARGE);
    } else {
      stack.stackTagCompound.setInteger(NBT_CHARGE, 0);
      return 0;
    }
  }

  @Override
  public int getMaxEnergyStored(ItemStack stack) {

    return 1000000; // FIXME: Chaos gem max charge?
  }
}
