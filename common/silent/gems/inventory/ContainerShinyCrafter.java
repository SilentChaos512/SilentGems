package silent.gems.inventory;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import silent.gems.core.util.LogHelper;
import silent.gems.tileentity.TileShinyCrafter;

public class ContainerShinyCrafter extends Container {

    private TileShinyCrafter tile;

    public ContainerShinyCrafter(InventoryPlayer inventoryPlayer, TileShinyCrafter tile) {

        this.tile = tile;
        
        this.addSlotToContainer(new Slot(tile, TileShinyCrafter.SLOT_INDEX_TOOL, 36, 53));
        this.addSlotToContainer(new Slot(tile, TileShinyCrafter.SLOT_INDEX_UPGRADE, 62, 53));
        this.addSlotToContainer(new Slot(tile, TileShinyCrafter.SLOT_INDEX_OUTPUT, 120, 53));
        
        // Add the player's inventory slots to the container
        for (int inventoryRowIndex = 0; inventoryRowIndex < 3; ++inventoryRowIndex)
        {
            for (int inventoryColumnIndex = 0; inventoryColumnIndex < 9; ++inventoryColumnIndex)
            {
                this.addSlotToContainer(new Slot(inventoryPlayer, inventoryColumnIndex + inventoryRowIndex * 9 + 9, 8 + inventoryColumnIndex * 18, 84 + inventoryRowIndex * 18));
            }
        }
        
        // Add the player's action bar slots to the container
        for (int actionBarSlotIndex = 0; actionBarSlotIndex < 9; ++actionBarSlotIndex)
        {
            this.addSlotToContainer(new Slot(inventoryPlayer, actionBarSlotIndex, 8 + actionBarSlotIndex * 18, 142));
        }
    }

    @Override
    public boolean canInteractWith(EntityPlayer player)
    {
        return true;
    }

    @Override
    public ItemStack transferStackInSlot(EntityPlayer entityPlayer, int slotIndex) {
        
        ItemStack itemStack = null;
        Slot slot = (Slot) inventorySlots.get(slotIndex);
        
        if (slot != null && slot.getHasStack()) {
            ItemStack slotItemStack = slot.getStack();
            if (!TileShinyCrafter.isItemTool(slotItemStack) && !TileShinyCrafter.isItemUpgrade(slotItemStack)) {
                return null;
            }
            itemStack = slotItemStack.copy();
            
            if (slotIndex < TileShinyCrafter.INVENTORY_SIZE) {
                if (!this.mergeItemStack(slotItemStack, TileShinyCrafter.INVENTORY_SIZE, inventorySlots.size(), false)) {
                    return null;
                }
            }
            else {
                // Attack to put item into tool or upgrade slot, as appropriate.
                if (TileShinyCrafter.isItemTool(slotItemStack)) {
                    if (!this.mergeItemStack(slotItemStack, TileShinyCrafter.SLOT_INDEX_TOOL, TileShinyCrafter.SLOT_INDEX_OUTPUT, false)) {
                        return null;
                    }
                }
                else if (TileShinyCrafter.isItemUpgrade(slotItemStack)) {
                    if (!this.mergeItemStack(slotItemStack, TileShinyCrafter.SLOT_INDEX_UPGRADE, TileShinyCrafter.SLOT_INDEX_OUTPUT, false)) {
                        return null;
                    }
                }
            }
            
            if (slotItemStack.stackSize == 0) {
                slot.putStack(null);
            }
            else {
                slot.onSlotChanged();
            }
        }

        return itemStack;
    }
}
