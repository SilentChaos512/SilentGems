package net.silentchaos512.gems.item;

import java.util.List;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.EnumAction;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.world.World;
import net.silentchaos512.gems.core.util.LocalizationHelper;
import net.silentchaos512.gems.core.util.LogHelper;
import net.silentchaos512.gems.core.util.NBTHelper;
import net.silentchaos512.gems.core.util.PlayerHelper;
import net.silentchaos512.gems.lib.Names;
import net.silentchaos512.gems.lib.Strings;

import org.lwjgl.input.Keyboard;

import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ReturnHome extends ItemSG {
    
    public final static String BOUND_TO = "BoundTo";
    public final static String NOT_BOUND = "NotBound";

    public ReturnHome() {

        super();

        setMaxStackSize(1);
        setUnlocalizedName(Names.RETURN_HOME);

        rarity = EnumRarity.uncommon;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean par4) {

        // Is ctrl key down?
        boolean modifier = Keyboard.isKeyDown(Keyboard.KEY_LCONTROL) || Keyboard.isKeyDown(Keyboard.KEY_RCONTROL);
        
        // How to use
        list.add(EnumChatFormatting.AQUA + LocalizationHelper.getItemDescription(itemName, 1));
        // Item description
        list.add(EnumChatFormatting.ITALIC + LocalizationHelper.getItemDescription(itemName, 2));
        
        // Display coordinates if modifier key is held.
        if (stack.stackTagCompound != null && NBTHelper.hasValidXYZD(stack.stackTagCompound)) {
            if (modifier) {
                String s = LocalizationHelper.getOtherItemKey(itemName, BOUND_TO);
                s = String.format(s, LogHelper.coordFromNBT(stack.stackTagCompound));
                list.add(EnumChatFormatting.GREEN + s);
            }
            else {
                list.add(LocalizationHelper.getMiscText(Strings.PRESS_CTRL));
            }
        }
        else {
            list.add(EnumChatFormatting.RED + LocalizationHelper.getOtherItemKey(itemName, NOT_BOUND));
        }
    }

    @Override
    public void addRecipes() {

        GameRegistry.addShapedRecipe(new ItemStack(this), " s ", "s s", "gcg", 's', Items.string, 'g', Items.gold_ingot, 'c',
                CraftingMaterial.getStack(Names.CHAOS_ESSENCE_PLUS));
    }

    @Override
    public EnumAction getItemUseAction(ItemStack stack) {

        return EnumAction.bow;
    }

    @Override
    public int getMaxItemUseDuration(ItemStack stack) {

        return 133700;
    }

    @Override
    public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player) {

        if (stack != null && stack.stackTagCompound != null && NBTHelper.hasValidXYZD(stack.stackTagCompound)) {
            player.setItemInUse(stack, this.getMaxItemUseDuration(stack));
        }
        else if (!world.isRemote) {
            PlayerHelper.addChatMessage(player, LocalizationHelper.getOtherItemKey(itemName, NOT_BOUND));
        }

        return stack;
    }

    @Override
    public void onPlayerStoppedUsing(ItemStack stack, World world, EntityPlayer player, int itemInUseCount) {

        if (!world.isRemote) {
            int timeUsed = this.getMaxItemUseDuration(stack) - itemInUseCount;

            // Did player use item long enough?
            if (timeUsed < 24) {
                return;
            }

            // TODO teleport player
            teleportPlayer(stack, player);
            world.playSoundAtEntity(player, "mob.endermen.portal", 1.0f, 1.0f);
        }
    }

    private void teleportPlayer(ItemStack stack, EntityPlayer player) {

        if (stack.stackTagCompound == null) {
            return;
        }

        NBTTagCompound tags = stack.stackTagCompound;
        if (!NBTHelper.hasValidXYZD(tags) || tags.getInteger("Y") <= 0) {
            LogHelper.warning("Invalid location for teleport effect");
            return;
        }

        int dx = tags.getInteger("X");
        int dy = tags.getInteger("Y");
        int dz = tags.getInteger("Z");
        int dd = tags.getInteger("D");

        // Dismount and teleport mount
        // This doesn't work very well.
        if (player.ridingEntity != null) {
            Entity mount = player.ridingEntity;
            player.mountEntity((Entity) null);
            if (dd != mount.dimension) {
                mount.travelToDimension(dd);
            }
            mount.setLocationAndAngles(dx + 0.5, dy + 1.0, dz + 0.5, mount.rotationYaw, mount.rotationPitch);
        }

        // Teleport player
        if (dd != player.dimension) {
            player.travelToDimension(dd);
        }
        player.setPositionAndUpdate(dx + 0.5, dy + 1.0, dz + 0.5);
    }
}
