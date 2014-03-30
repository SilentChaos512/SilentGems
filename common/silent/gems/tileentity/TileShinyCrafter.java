package silent.gems.tileentity;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import silent.gems.core.registry.SRegistry;
import silent.gems.core.util.InventoryHelper;
import silent.gems.core.util.LogHelper;
import silent.gems.lib.Names;
import silent.gems.lib.Strings;

public class TileShinyCrafter extends TileSG implements IInventory {

    private ItemStack[] inventory;

    public final static int INVENTORY_SIZE = 3;

    public final static int SLOT_INDEX_TOOL = 0;
    public final static int SLOT_INDEX_UPGRADE = 1;
    public final static int SLOT_INDEX_OUTPUT = 2;

    public TileShinyCrafter() {

        inventory = new ItemStack[INVENTORY_SIZE];
    }
    
    @Override
    public void onInventoryChanged() {

        ItemStack tool = inventory[SLOT_INDEX_TOOL];
        ItemStack upgrade = inventory[SLOT_INDEX_UPGRADE];
        
        if (isItemTool(tool) && isItemUpgrade(upgrade)) {
            // TODO
        }
    }

    @Override
    public int getSizeInventory() {

        return inventory.length;
    }

    @Override
    public ItemStack getStackInSlot(int i) {

        return inventory[i];
    }

    @Override
    public ItemStack decrStackSize(int slot, int amount) {

        if (inventory[slot] != null) {
            ItemStack stack;

            if (inventory[slot].stackSize <= amount) {
                stack = inventory[slot];
                inventory[slot] = null;
                return stack;
            }
            else {
                stack = inventory[slot].splitStack(amount);

                if (inventory[slot].stackSize == 0) {
                    inventory[slot] = null;
                }

                return stack;
            }
        }
        else {
            return null;
        }
    }

    @Override
    public ItemStack getStackInSlotOnClosing(int slot) {

        if (inventory[slot] != null) {
            ItemStack stack = inventory[slot];
            inventory[slot] = null;
            return stack;
        }
        else {
            return null;
        }
    }

    @Override
    public void setInventorySlotContents(int slot, ItemStack stack) {

        inventory[slot] = stack;

        if (stack != null && stack.stackSize > this.getInventoryStackLimit()) {
            stack.stackSize = this.getInventoryStackLimit();
        }
    }

    @Override
    public String getInvName() {

        return this.hasCustomName() ? this.getCustomName() : Strings.CONTAINER_SHINY_CRAFTER_NAME;
    }

    @Override
    public boolean isInvNameLocalized() {

        return this.hasCustomName();
    }

    @Override
    public int getInventoryStackLimit() {

        return 64;
    }

    @Override
    public boolean isUseableByPlayer(EntityPlayer player) {

        return true;
    }

    @Override
    public void openChest() {

    }

    @Override
    public void closeChest() {

    }

    @Override
    public boolean isItemValidForSlot(int slot, ItemStack stack) {

        if (slot == SLOT_INDEX_TOOL) {
            return isItemTool(stack);
        }
        else if (slot == SLOT_INDEX_UPGRADE) {
            return isItemUpgrade(stack);
        }
        else {
            return false;
        }
    }

    @Override
    public void readFromNBT(NBTTagCompound tags) {

        super.readFromNBT(tags);

        NBTTagList list = tags.getTagList("Items");
        inventory = new ItemStack[this.getSizeInventory()];

        for (int i = 0; i < list.tagCount(); ++i) {
            NBTTagCompound t = (NBTTagCompound) list.tagAt(i);
            byte b0 = t.getByte("Slot");

            if (b0 >= 0 && b0 < inventory.length) {
                inventory[b0] = ItemStack.loadItemStackFromNBT(t);
            }
        }

        // TODO?
    }

    @Override
    public void writeToNBT(NBTTagCompound tags) {

        super.writeToNBT(tags);
        NBTTagList list = new NBTTagList();

        for (int i = 0; i < inventory.length; ++i) {
            if (inventory[i] != null) {
                NBTTagCompound t = new NBTTagCompound();
                t.setByte("Slot", (byte) i);
                inventory[i].writeToNBT(t);
                list.appendTag(t);
            }
        }

        tags.setTag("Items", list);

        // TODO?
    }

    public static boolean isItemTool(ItemStack stack) {

        if (stack == null) {
            return false;
        }
        return stack.getItem().itemID == SRegistry.getItem(Names.CHAOS_GEM).itemID || InventoryHelper.isGemTool(stack);
    }
    
    public static boolean isItemUpgrade(ItemStack stack) {
        
        if (stack == null) {
            return false;
        }
        int k = stack.getItem().itemID;
        return k == SRegistry.getItem(Names.CHAOS_RUNE).itemID || k == SRegistry.getItem(Names.ENCHANT_TOKEN).itemID;
    }
}
