package net.silentchaos512.gems.block.altar;

import lombok.Getter;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.items.ItemHandlerHelper;
import net.silentchaos512.gems.chaos.Chaos;
import net.silentchaos512.gems.crafting.altar.AltarRecipe;
import net.silentchaos512.gems.crafting.altar.AltarRecipeManager;
import net.silentchaos512.gems.init.GemsTileEntities;
import net.silentchaos512.lib.tile.LockableSidedInventoryTileEntity;
import net.silentchaos512.lib.tile.SyncVariable;

import javax.annotation.Nullable;

public class AltarTileEntity extends LockableSidedInventoryTileEntity implements ITickableTileEntity {
    private static final int INVENTORY_SIZE = 3;

    @Getter
    @SyncVariable(name = "Progress")
    private int progress;
    @Getter
    @SyncVariable(name = "ProcessTime")
    private int processTime;
    @Getter
    @SyncVariable(name = "ChaosGenerated")
    private int chaosGenerated;
    private int chaosBuffer;

    public AltarTileEntity() {
        super(GemsTileEntities.TRANSMUTATION_ALTAR.type(), INVENTORY_SIZE);
    }

    @Override
    public void tick() {
        if (world == null || world.isRemote) return;

        AltarRecipe recipe = getMatchingRecipe();
        if (recipe != null && hasRoomInOutput(recipe)) {
            // Process
            ++progress;
            chaosGenerated = recipe.getChaosGenerated();
            chaosBuffer += chaosGenerated;
            processTime = recipe.getProcessTime();

            if (progress >= processTime) {
                // Create result
                placeResultInOutput(recipe);
                consumeIngredients(recipe);
                progress = 0;
            }
            sendUpdate();
        } else {
            // Not processing anything
            setNeutralState();
        }

        if (this.chaosBuffer > 0 && this.world.getGameTime() % 20 == 0) {
            Chaos.generate(this.world, this.chaosBuffer, this.pos);
            this.chaosBuffer = 0;
        }
    }

    private void sendUpdate() {
        if (world != null) {
            BlockState state = world.getBlockState(pos);
            world.notifyBlockUpdate(pos, state, state, 3);
        }
    }

    private void setNeutralState() {
        boolean update = false;
        if (progress > 0) {
            progress = 0;
            update = true;
        }
        if (chaosGenerated > 0) {
            chaosGenerated = 0;
            update = true;
        }
        if (update) {
            sendUpdate();
        }
    }

    @Nullable
    private AltarRecipe getMatchingRecipe() {
        if (getStackInSlot(0).isEmpty()) return null;
        return AltarRecipeManager.getMatch(this);
    }

    private boolean hasRoomInOutput(AltarRecipe recipe) {
        ItemStack output = getStackInSlot(INVENTORY_SIZE - 1);
        return output.isEmpty() || ItemHandlerHelper.canItemStacksStack(output, recipe.getResult());
    }

    private void consumeIngredients(AltarRecipe recipe) {
        decrStackSize(0, 1);
        ItemStack catalyst = recipe.consumeCatalyst(getStackInSlot(1));
        setInventorySlotContents(1, catalyst);
    }

    private void placeResultInOutput(AltarRecipe recipe) {
        ItemStack output = getStackInSlot(INVENTORY_SIZE - 1);
        ItemStack result = recipe.getResult();
        if (output.isEmpty()) {
            setInventorySlotContents(INVENTORY_SIZE - 1, result.copy());
        } else {
            output.setCount(output.getCount() + result.getCount());
        }
    }

    @Override
    public int[] getSlotsForFace(Direction side) {
        switch (side) {
            case UP:
                return new int[]{0, 1};
            case DOWN:
                return new int[]{0, 1, 2};
            default:
                return new int[]{0, 1, 2};
        }
    }

    @Override
    public boolean canInsertItem(int index, ItemStack itemStackIn, @Nullable Direction direction) {
        return index != 2;
    }

    @Override
    public boolean canExtractItem(int index, ItemStack stack, Direction direction) {
        return index == 2;
    }

    @Override
    protected ITextComponent getDefaultName() {
        return new TranslationTextComponent("container.silentgems.transmutation_altar");
    }

    @Override
    protected Container createMenu(int p_213906_1_, PlayerInventory p_213906_2_) {
        return new AltarContainer(p_213906_1_, p_213906_2_, this);
    }

    @Override
    public void read(CompoundNBT tags) {
        super.read(tags);
        SyncVariable.Helper.readSyncVars(this, tags);
    }

    @Override
    public CompoundNBT write(CompoundNBT tags) {
        super.write(tags);
        SyncVariable.Helper.writeSyncVars(this, tags, SyncVariable.Type.WRITE);
        return tags;
    }

    @Override
    public CompoundNBT getUpdateTag() {
        CompoundNBT tags = super.getUpdateTag();
        SyncVariable.Helper.writeSyncVars(this, tags, SyncVariable.Type.PACKET);
        return tags;
    }

    @Override
    public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket packet) {
        super.onDataPacket(net, packet);
        SyncVariable.Helper.readSyncVars(this, packet.getNbtCompound());
    }
}
