package silent.gems.item;

import java.util.List;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import silent.gems.configuration.Config;
import silent.gems.core.registry.SRegistry;
import silent.gems.core.util.LocalizationHelper;
import silent.gems.core.util.RecipeHelper;
import silent.gems.lib.Names;
import silent.gems.lib.Strings;
import silent.gems.lib.buff.ChaosBuff;

public class ChaosGem extends ItemSG {

    public final static int MAX_STACK_DAMAGE = 10;

    private final int gemId;

    public ChaosGem(int gemId) {

        this.gemId = gemId;
        setMaxStackSize(1);
        setUnlocalizedName(Names.CHAOS_GEM + gemId);
        setMaxDamage(MAX_STACK_DAMAGE);
        setCreativeTab(CreativeTabs.tabTools);
        rarity = EnumRarity.rare;
    }

    public static void addBuff(ItemStack stack, ChaosBuff buff) {

        int k = getBuffLevel(stack, buff);

        if (stack.stackTagCompound == null) {
            stack.setTagCompound(new NBTTagCompound());
        }
        if (!stack.stackTagCompound.hasKey(Strings.CHAOS_GEM_BUFF_LIST)) {
            stack.stackTagCompound.setTag(Strings.CHAOS_GEM_BUFF_LIST, new NBTTagList());
        }

        NBTTagList list = (NBTTagList) stack.stackTagCompound.getTag(Strings.CHAOS_GEM_BUFF_LIST);
        if (k == 0) {
            // Buff not already on list, add it.
            NBTTagCompound tag = new NBTTagCompound();
            tag.setShort(Strings.CHAOS_GEM_BUFF_ID, (short) buff.id);
            tag.setShort(Strings.CHAOS_GEM_BUFF_LEVEL, (short) 1);
            list.appendTag(tag);
        }
        else {
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
    }

    @Override
    public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean par4) {

        if (stack.stackTagCompound == null) {
            // Information on how to use.
            list.add(EnumChatFormatting.DARK_GRAY + LocalizationHelper.getItemDescription(Names.CHAOS_GEM, 0));
            return;
        }

        boolean enabled = stack.stackTagCompound.getBoolean(Strings.CHAOS_GEM_ENABLED);

        if (enabled) {
            list.add(EnumChatFormatting.GREEN + LocalizationHelper.getOtherItemKey(Names.CHAOS_GEM, "Enabled"));
        }
        else {
            list.add(EnumChatFormatting.RED + LocalizationHelper.getOtherItemKey(Names.CHAOS_GEM, "Disabled"));
        }

        // Charge level
        int k = stack.stackTagCompound.getInteger(Strings.CHAOS_GEM_CHARGE);
        list.add(EnumChatFormatting.YELLOW + String.format("%d / %d", k, getMaxChargeLevel(stack)));

        // Charge change rate
        k = enabled ? -getTotalChargeDrain(stack) : getRechargeAmount(stack);
        list.add(EnumChatFormatting.DARK_GRAY + (k >= 0 ? "+" : "") + k + " "
                + LocalizationHelper.getOtherItemKey(Names.CHAOS_GEM, "ChargePerSecond"));

        if (stack.stackTagCompound.hasKey(Strings.CHAOS_GEM_BUFF_LIST)) {
            // Display list of effects.
            NBTTagList tags = (NBTTagList) stack.stackTagCompound.getTag(Strings.CHAOS_GEM_BUFF_LIST);
            NBTTagCompound t;
            int id, lvl;
            for (int i = 0; i < tags.tagCount(); ++i) {
                t = (NBTTagCompound) tags.getCompoundTagAt(i);
                id = t.getShort(Strings.CHAOS_GEM_BUFF_ID);
                lvl = t.getShort(Strings.CHAOS_GEM_BUFF_LEVEL);
                list.add(ChaosBuff.all.get(id).getDisplayName(lvl));
            }
        }
        else {
            // Information on how to use.
            list.add(EnumChatFormatting.DARK_GRAY + LocalizationHelper.getItemDescription(Names.CHAOS_GEM, 0));
        }
    }

    @Override
    public void addRecipes() {

        RecipeHelper.addSurround(new ItemStack(this), new ItemStack(SRegistry.getBlock(Names.GEM_BLOCK), 1, gemId),
                CraftingMaterial.getStack(Names.CHAOS_ESSENCE_PLUS));
    }

    private void applyEffects(ItemStack stack, EntityPlayer player) {

        if (stack.stackTagCompound == null) {
            stack.stackTagCompound = new NBTTagCompound();
        }
        if (!stack.stackTagCompound.hasKey(Strings.CHAOS_GEM_BUFF_LIST)) {
            stack.stackTagCompound.setTag(Strings.CHAOS_GEM_BUFF_LIST, new NBTTagList());
        }

        NBTTagList list = (NBTTagList) stack.stackTagCompound.getTag(Strings.CHAOS_GEM_BUFF_LIST);
        NBTTagCompound tag;
        short id, lvl;
        for (int i = 0; i < list.tagCount(); ++i) {
            tag = (NBTTagCompound) list.getCompoundTagAt(i);
            id = tag.getShort(Strings.CHAOS_GEM_BUFF_ID);
            lvl = tag.getShort(Strings.CHAOS_GEM_BUFF_LEVEL);
            ChaosBuff.all.get(id).apply(player, lvl);
        }
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

        if (stack.stackTagCompound == null) {
            stack.setTagCompound(new NBTTagCompound());
        }
        if (!stack.stackTagCompound.hasKey(Strings.CHAOS_GEM_BUFF_LIST)) {
            stack.stackTagCompound.setTag(Strings.CHAOS_GEM_BUFF_LIST, new NBTTagList());
        }

        boolean enabled = stack.stackTagCompound.getBoolean(Strings.CHAOS_GEM_ENABLED);

        // Apply effects?
        if (stack.stackTagCompound.hasKey(Strings.CHAOS_GEM_BUFF_LIST)) {
            if (enabled) {
                applyEffects(stack, player);
            }
            else {
                removeEffects(stack, player);
            }
        }

        // Update charge level
        final int maxCharge = getMaxChargeLevel(stack);
        if (!stack.stackTagCompound.hasKey(Strings.CHAOS_GEM_CHARGE)) {
            stack.stackTagCompound.setInteger(Strings.CHAOS_GEM_CHARGE, Config.CHAOS_GEM_MAX_CHARGE.value);
        }
        int charge = stack.stackTagCompound.getInteger(Strings.CHAOS_GEM_CHARGE);
        if (enabled) {
            charge -= getTotalChargeDrain(stack);
            // Disable if out of charge.
            if (charge <= 0) {
                charge = 0;
                stack.stackTagCompound.setBoolean(Strings.CHAOS_GEM_ENABLED, false);
            }
        }
        else if (charge < maxCharge) {
            charge += getRechargeAmount(stack);
            if (charge > maxCharge) {
                charge = maxCharge;
            }
        }
        else if (charge > maxCharge) {
            charge = maxCharge;
        }
        stack = setChargeLevel(stack, charge);
    }

    public static int getBuffCount(ItemStack stack) {

        // Does buff tag list exist?
        if (stack == null || stack.stackTagCompound == null || !stack.stackTagCompound.hasKey(Strings.CHAOS_GEM_BUFF_LIST)) {
            return 0;
        }
        else {
            NBTTagList list = (NBTTagList) stack.stackTagCompound.getTag(Strings.CHAOS_GEM_BUFF_LIST);
            return list.tagCount();
        }
    }

    public static int getBuffLevel(ItemStack stack, ChaosBuff buff) {

        // Does buff tag list exist?
        if (stack == null || buff == null || stack.stackTagCompound == null || !stack.stackTagCompound.hasKey(Strings.CHAOS_GEM_BUFF_LIST)) {
            return 0;
        }
        else {
            NBTTagList list = (NBTTagList) stack.stackTagCompound.getTag(Strings.CHAOS_GEM_BUFF_LIST);

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

    public static int getChargeLevel(ItemStack gem) {

        if (gem != null && gem.stackTagCompound != null && gem.stackTagCompound.hasKey(Strings.CHAOS_GEM_CHARGE)) {
            return gem.stackTagCompound.getInteger(Strings.CHAOS_GEM_CHARGE);
        }
        else {
            return -1;
        }
    }

    public static int getMaxChargeLevel(ItemStack gem) {

        int maxCharge = Config.CHAOS_GEM_MAX_CHARGE.value;

        if (gem != null && gem.stackTagCompound != null) {
            int capacityLevel = getBuffLevel(gem, ChaosBuff.getBuffByName(ChaosBuff.CAPACITY));
            return maxCharge + capacityLevel * maxCharge / 4;
        }
        else {
            return maxCharge;
        }
    }

    public static int getRechargeAmount(ItemStack gem) {

        int amount = Config.CHAOS_GEM_RECHARGE_RATE.value;

        if (gem != null && gem.stackTagCompound != null) {
            int boosterLevel = getBuffLevel(gem, ChaosBuff.getBuffByName(ChaosBuff.BOOSTER));
            return amount + boosterLevel * amount / 4;
        }
        else {
            return amount;
        }
    }

    public static int getTotalChargeDrain(ItemStack gem) {

        int drain = 0;

        if (gem != null && gem.stackTagCompound != null) {
            NBTTagList list = (NBTTagList) gem.stackTagCompound.getTag(Strings.CHAOS_GEM_BUFF_LIST);
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
    public boolean hasEffect(ItemStack stack, int pass) {

        return stack.stackTagCompound != null && stack.stackTagCompound.getBoolean(Strings.CHAOS_GEM_ENABLED);
    }

    public static boolean isEnabled(ItemStack stack) {

        return stack != null && stack.stackTagCompound != null & stack.stackTagCompound.hasKey(Strings.CHAOS_GEM_ENABLED)
                && stack.stackTagCompound.getBoolean(Strings.CHAOS_GEM_ENABLED);
    }

    @Override
    public boolean onDroppedByPlayer(ItemStack stack, EntityPlayer player) {

        // Set disabled
        if (stack.stackTagCompound != null) {
            stack.stackTagCompound.setBoolean(Strings.CHAOS_GEM_ENABLED, false);
        }

        return true;
    }

    @Override
    public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player) {

        if (stack.stackTagCompound == null) {
            stack.setTagCompound(new NBTTagCompound());
        }
        if (!stack.stackTagCompound.hasKey(Strings.CHAOS_GEM_ENABLED)) {
            stack.stackTagCompound.setBoolean(Strings.CHAOS_GEM_ENABLED, false);
        }

        // Enable/disable
        boolean b = stack.stackTagCompound.getBoolean(Strings.CHAOS_GEM_ENABLED);
        if (stack.stackTagCompound.getInteger(Strings.CHAOS_GEM_CHARGE) > 0) {
            stack.stackTagCompound.setBoolean(Strings.CHAOS_GEM_ENABLED, !b);
            if (b) {
                removeEffects(stack, player);
            }
            else {
                applyEffects(stack, player);
            }
        }

        return stack;
    }

    private void removeEffects(ItemStack stack, EntityPlayer player) {

        if (stack.stackTagCompound == null) {
            stack.stackTagCompound = new NBTTagCompound();
        }
        if (!stack.stackTagCompound.hasKey(Strings.CHAOS_GEM_BUFF_LIST)) {
            stack.stackTagCompound.setTag(Strings.CHAOS_GEM_BUFF_LIST, new NBTTagList());
        }

        NBTTagList list = (NBTTagList) stack.stackTagCompound.getTag(Strings.CHAOS_GEM_BUFF_LIST);
        NBTTagCompound tag;
        short id, lvl;
        for (int i = 0; i < list.tagCount(); ++i) {
            tag = (NBTTagCompound) list.getCompoundTagAt(i);
            id = tag.getShort(Strings.CHAOS_GEM_BUFF_ID);
            ChaosBuff.all.get(id).remove(player);
        }
    }

    public static ItemStack setChargeLevel(ItemStack gem, int charge) {

        if (gem != null && gem.stackTagCompound != null && gem.stackTagCompound.hasKey(Strings.CHAOS_GEM_CHARGE)) {
            int maxCharge = getMaxChargeLevel(gem);
            charge = MathHelper.clamp_int(charge, 0, maxCharge);
            int damage = MAX_STACK_DAMAGE - (MAX_STACK_DAMAGE * charge / maxCharge);

            // Save actual charge level in NBT. Item damage is only for display purposes.
            gem.stackTagCompound.setInteger(Strings.CHAOS_GEM_CHARGE, charge);
            gem.setItemDamage(damage);
        }

        return gem;
    }
}
