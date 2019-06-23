package net.silentchaos512.gems.block.tokenenchanter;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.RecipeBookContainer;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.RecipeItemHelper;
import net.minecraft.world.World;
import net.silentchaos512.gems.init.GemsContainers;
import net.silentchaos512.lib.inventory.SlotOutputOnly;
import net.silentchaos512.lib.util.InventoryUtils;

import javax.annotation.Nonnull;

public class TokenEnchanterContainer extends RecipeBookContainer<IInventory> {
    final TokenEnchanterTileEntity tileEntity;
    private final World world;

    public TokenEnchanterContainer(int id, PlayerInventory playerInventory) {
        this(id, playerInventory, new TokenEnchanterTileEntity());
    }

    @SuppressWarnings("OverridableMethodCallDuringObjectConstruction")
    public TokenEnchanterContainer(int id, PlayerInventory playerInventory, TokenEnchanterTileEntity tileEntity) {
        super(GemsContainers.TOKEN_ENCHANTER.type(), id);
        this.tileEntity = tileEntity;
        this.world = playerInventory.player.world;

        // Token slot
        this.addSlot(new Slot(this.tileEntity, 0, 22, 35));
        // Other ingredients slot
        this.addSlot(new Slot(this.tileEntity, 1, 48, 25));
        this.addSlot(new Slot(this.tileEntity, 2, 66, 25));
        this.addSlot(new Slot(this.tileEntity, 3, 84, 25));
        this.addSlot(new Slot(this.tileEntity, 4, 48, 43));
        this.addSlot(new Slot(this.tileEntity, 5, 66, 43));
        this.addSlot(new Slot(this.tileEntity, 6, 84, 43));
        // Output
        this.addSlot(new SlotOutputOnly(this.tileEntity, 7, 132, 35));

        InventoryUtils.createPlayerSlots(playerInventory, 8, 84).forEach(this::addSlot);

        func_216961_a(this.tileEntity.fields);
    }

    @Override
    public boolean canInteractWith(PlayerEntity playerIn) {
        return this.tileEntity.isUsableByPlayer(playerIn);
    }

    @Nonnull
    @Override
    public ItemStack transferStackInSlot(PlayerEntity player, int slotIndex) {
        ItemStack stack = ItemStack.EMPTY;
        Slot slot = this.inventorySlots.get(slotIndex);

        if (slot != null && slot.getHasStack()) {
            ItemStack stack1 = slot.getStack();
            stack = stack1.copy();

            final int playerStart = 5;
            final int playerEnd = playerStart + 36;

            if (slotIndex == 4) {
                // Transfer from output slot
                if (!this.mergeItemStack(stack1, playerStart, playerEnd, true)) {
                    return ItemStack.EMPTY;
                }
                slot.onSlotChange(stack1, stack);
            } else if (slotIndex > 4) {
                // Transfer from player
                if (this.tileEntity.isItemValidForSlot(0, stack1)) {
                    // Blank tokens slot
                    if (!this.mergeItemStack(stack1, 0, 1, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (this.tileEntity.isItemValidForSlot(1, stack1)) {
                    // Ingredients start
                    if (!this.mergeItemStack(stack1, 1, 4, false)) {
                        return ItemStack.EMPTY;
                    }
                } else {
                    // Move from hotbar to backpack or vice versa
                    final int hotbarStart = playerStart + 27;
                    if (slotIndex < hotbarStart) {
                        if (!this.mergeItemStack(stack1, hotbarStart, playerEnd, false)) {
                            return ItemStack.EMPTY;
                        }
                    } else if (slotIndex < playerEnd && !this.mergeItemStack(stack1, playerStart, hotbarStart, false)) {
                        return ItemStack.EMPTY;
                    }
                }
            } else if (!this.mergeItemStack(stack1, playerStart, playerEnd, false)) {
                return ItemStack.EMPTY;
            }

            if (stack1.isEmpty()) {
                slot.putStack(ItemStack.EMPTY);
            } else {
                slot.onSlotChanged();
            }

            if (stack1.getCount() == stack.getCount()) {
                return ItemStack.EMPTY;
            }

            slot.onTake(player, stack1);
        }

        return stack;
    }

    @Override
    public void func_201771_a(RecipeItemHelper helper) {
        tileEntity.fillStackedContents(helper);
    }

    @Override
    public void clear() {
        tileEntity.clear();
    }

    @Override
    public boolean matches(IRecipe<? super IInventory> recipeIn) {
        return recipeIn.matches(tileEntity, world);
    }

    @Override
    public int getOutputSlot() {
        return TokenEnchanterTileEntity.INVENTORY_SIZE - 1;
    }

    @Override
    public int getWidth() {
        return 3;
    }

    @Override
    public int getHeight() {
        return 3;
    }

    @Override
    public int getSize() {
        return TokenEnchanterTileEntity.INVENTORY_SIZE;
    }
}
