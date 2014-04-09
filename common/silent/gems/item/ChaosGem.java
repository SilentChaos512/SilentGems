package silent.gems.item;

import java.util.List;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.Icon;
import net.minecraft.world.World;
import silent.gems.configuration.Config;
import silent.gems.core.registry.SRegistry;
import silent.gems.core.util.LocalizationHelper;
import silent.gems.core.util.LogHelper;
import silent.gems.core.util.PlayerHelper;
import silent.gems.core.util.RecipeHelper;
import silent.gems.lib.EnumGem;
import silent.gems.lib.Names;
import silent.gems.lib.Strings;
import silent.gems.lib.buff.ChaosBuff;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ChaosGem extends ItemSG {

    public ChaosGem(int id) {

        super(id);
        icons = new Icon[EnumGem.all().length];
        setMaxStackSize(1);
        setHasSubtypes(true);
        setHasGemSubtypes(true);
        setUnlocalizedName(Names.CHAOS_GEM);
        setMaxDamage(0);
        setCreativeTab(CreativeTabs.tabTools);
        rarity = EnumRarity.rare;
    }

    @Override
    @SuppressWarnings({ "rawtypes", "unchecked" })
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean par4) {

        if (stack.stackTagCompound == null) {
            // Information on how to use.
            list.add(LocalizationHelper.getMessageText(Strings.CHAOS_GEM_1, EnumChatFormatting.DARK_GRAY));
            return;
        }

        if (stack.stackTagCompound.getBoolean(Strings.CHAOS_GEM_ENABLED)) {
            // list.add(EnumChatFormatting.GREEN + "Enabled");
            list.add(LocalizationHelper.getMessageText(Strings.STATE_ENABLED, EnumChatFormatting.GREEN));
        }
        else {
            // list.add(EnumChatFormatting.RED + "Disabled");
            list.add(LocalizationHelper.getMessageText(Strings.STATE_DISABLED, EnumChatFormatting.RED));
        }

        int k = stack.stackTagCompound.getInteger(Strings.CHAOS_GEM_CHARGE);
        list.add(EnumChatFormatting.YELLOW + String.format("%d / %d", k, Config.CHAOS_GEM_MAX_CHARGE.value));

        if (stack.stackTagCompound.hasKey(Strings.CHAOS_GEM_BUFF_LIST)) {
            // Display list of effects.
            NBTTagList tags = (NBTTagList) stack.stackTagCompound.getTag(Strings.CHAOS_GEM_BUFF_LIST);
            NBTTagCompound t;
            int id, lvl;
            for (int i = 0; i < tags.tagCount(); ++i) {
                t = (NBTTagCompound) tags.tagAt(i);
                id = t.getShort(Strings.CHAOS_GEM_BUFF_ID);
                lvl = t.getShort(Strings.CHAOS_GEM_BUFF_LEVEL);
                list.add(ChaosBuff.all.get(id).getDisplayName(lvl));
            }
        }
        else {
            // Information on how to use.
            list.add(LocalizationHelper.getMessageText(Strings.CHAOS_GEM_1, EnumChatFormatting.DARK_GRAY));
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
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

    public static void addBuff(ItemStack stack, ChaosBuff buff) {

        int k = getBuffLevel(stack, buff);

        if (stack.stackTagCompound == null) {
            stack.setTagCompound(new NBTTagCompound());
        }
        if (!stack.stackTagCompound.hasKey(Strings.CHAOS_GEM_BUFF_LIST)) {
            stack.stackTagCompound.setTag(Strings.CHAOS_GEM_BUFF_LIST, new NBTTagList(Strings.CHAOS_GEM_BUFF_LIST));
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
                tag = (NBTTagCompound) list.tagAt(i);
                k = tag.getShort(Strings.CHAOS_GEM_BUFF_ID);
                if (k == buff.id) {
                    k = tag.getShort(Strings.CHAOS_GEM_BUFF_LEVEL);
                    tag.setShort(Strings.CHAOS_GEM_BUFF_LEVEL, (short) (k + 1));
                }
            }
        }

        // return stack;
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
                if (((NBTTagCompound) list.tagAt(i)).getShort(Strings.CHAOS_GEM_BUFF_ID) == buff.id) {
                    return ((NBTTagCompound) list.tagAt(i)).getShort(Strings.CHAOS_GEM_BUFF_LEVEL);
                }
            }

            // Not there
            return 0;
        }
    }

    private void applyEffects(ItemStack stack, EntityPlayer player) {

        if (stack.stackTagCompound == null) {
            stack.stackTagCompound = new NBTTagCompound();
        }
        if (!stack.stackTagCompound.hasKey(Strings.CHAOS_GEM_BUFF_LIST)) {
            stack.stackTagCompound.setTag(Strings.CHAOS_GEM_BUFF_LIST, new NBTTagList(Strings.CHAOS_GEM_BUFF_LIST));
        }

        NBTTagList list = (NBTTagList) stack.stackTagCompound.getTag(Strings.CHAOS_GEM_BUFF_LIST);
        NBTTagCompound tag;
        short id, lvl;
        for (int i = 0; i < list.tagCount(); ++i) {
            tag = (NBTTagCompound) list.tagAt(i);
            id = tag.getShort(Strings.CHAOS_GEM_BUFF_ID);
            lvl = tag.getShort(Strings.CHAOS_GEM_BUFF_LEVEL);
            // LogHelper.list(id, lvl, ChaosBuff.all.get(id));
            ChaosBuff.all.get(id).apply(player, lvl);
        }
    }

    private void removeEffects(ItemStack stack, EntityPlayer player) {

        if (stack.stackTagCompound == null) {
            stack.stackTagCompound = new NBTTagCompound();
        }
        if (!stack.stackTagCompound.hasKey(Strings.CHAOS_GEM_BUFF_LIST)) {
            stack.stackTagCompound.setTag(Strings.CHAOS_GEM_BUFF_LIST, new NBTTagList(Strings.CHAOS_GEM_BUFF_LIST));
        }

        NBTTagList list = (NBTTagList) stack.stackTagCompound.getTag(Strings.CHAOS_GEM_BUFF_LIST);
        NBTTagCompound tag;
        short id, lvl;
        for (int i = 0; i < list.tagCount(); ++i) {
            tag = (NBTTagCompound) list.tagAt(i);
            id = tag.getShort(Strings.CHAOS_GEM_BUFF_ID);
            ChaosBuff.all.get(id).remove(player);
        }
    }

    public void doTick(ItemStack stack, EntityPlayer player) {

        if (stack.stackTagCompound == null) {
            stack.setTagCompound(new NBTTagCompound());
        }
        if (!stack.stackTagCompound.hasKey(Strings.CHAOS_GEM_BUFF_LIST)) {
            stack.stackTagCompound.setTag(Strings.CHAOS_GEM_BUFF_LIST, new NBTTagList(Strings.CHAOS_GEM_BUFF_LIST));
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
        if (!stack.stackTagCompound.hasKey(Strings.CHAOS_GEM_CHARGE)) {
            stack.stackTagCompound.setInteger(Strings.CHAOS_GEM_CHARGE, Config.CHAOS_GEM_MAX_CHARGE.value);
        }
        int charge = stack.stackTagCompound.getInteger(Strings.CHAOS_GEM_CHARGE);
        if (enabled) {
            --charge;
            // Disable if out of charge.
            if (charge <= 0) {
                stack.stackTagCompound.setBoolean(Strings.CHAOS_GEM_ENABLED, false);
            }
            // Warn player if low on charge.
            if (Config.CHAOS_GEM_MAX_CHARGE.value > 0 && charge == Config.CHAOS_GEM_MAX_CHARGE.value / 10) {
                String s = LocalizationHelper.getMessageText(Strings.CHAOS_GEM_LOW_POWER);
                s = String.format(s, ((ItemSG) stack.getItem()).getLocalizedName(stack));
                PlayerHelper.addChatMessage(player, s, EnumChatFormatting.YELLOW, false);
            }
        }
        else if (charge < Config.CHAOS_GEM_MAX_CHARGE.value) {
            ++charge;
        }
        else if (charge > Config.CHAOS_GEM_MAX_CHARGE.value) {
            charge = Config.CHAOS_GEM_MAX_CHARGE.value;
        }
        stack.stackTagCompound.setInteger(Strings.CHAOS_GEM_CHARGE, charge);
    }

    @Override
    public void addRecipes() {

        for (int i = 0; i < EnumGem.all().length; ++i) {
            RecipeHelper.addSurround(new ItemStack(this, 1, i), new ItemStack(SRegistry.getBlock(Names.GEM_BLOCK), 1, i),
                    CraftingMaterial.getStack(Names.CHAOS_ESSENCE_PLUS));
        }
    }
}
