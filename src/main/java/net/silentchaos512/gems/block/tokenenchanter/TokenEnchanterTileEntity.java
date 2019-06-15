package net.silentchaos512.gems.block.tokenenchanter;

import lombok.Getter;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.items.ItemHandlerHelper;
import net.silentchaos512.gems.chaos.Chaos;
import net.silentchaos512.gems.crafting.tokenenchanter.TokenEnchanterRecipe;
import net.silentchaos512.gems.crafting.tokenenchanter.TokenEnchanterRecipeManager;
import net.silentchaos512.gems.init.GemsTileEntities;
import net.silentchaos512.lib.tile.LockableSidedInventoryTileEntity;
import net.silentchaos512.lib.tile.SyncVariable;

import javax.annotation.Nullable;
import java.util.Map;
import java.util.stream.IntStream;

// TODO: Can we implement IRecipeHolder, IRecipeHelperPopulator?
public class TokenEnchanterTileEntity extends LockableSidedInventoryTileEntity implements ITickableTileEntity {
    private static final int INVENTORY_SIZE = 1 + 6 + 1;
    private static final int[] SLOTS_INPUT = IntStream.range(0, 7).toArray();
    private static final int[] SLOTS_OUTPUT = {7};
    private static final int[] SLOTS_ALL = IntStream.range(0, 8).toArray();

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

    public TokenEnchanterTileEntity() {
        super(GemsTileEntities.TOKEN_ENCHANTER.type(), INVENTORY_SIZE);
    }

    @Override
    public void tick() {
        if (world == null || world.isRemote) return;

        TokenEnchanterRecipe recipe = getMatchingRecipe();
        if (recipe != null && hasRoomInOutput(recipe)) {
            // Process
            ++this.progress;
            this.chaosGenerated = recipe.getChaosGenerated();
            this.processTime = recipe.getProcessTime();
            this.chaosBuffer += this.chaosGenerated;

            if (this.progress >= this.processTime) {
                // Create result
                placeResultInOutput(recipe);
                consumeIngredients(recipe);
                this.progress = 0;
            }
            sendUpdate();
        } else {
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
    private TokenEnchanterRecipe getMatchingRecipe() {
        // Check that we at least have some items in the input slots before
        // searching for a recipe match.
        if (getStackInSlot(0).isEmpty()) return null;
        for (int i = 1; i < INVENTORY_SIZE - 1; ++i) {
            if (!getStackInSlot(i).isEmpty()) {
                return TokenEnchanterRecipeManager.getMatch(this);
            }
        }
        return null;
    }

    private boolean hasRoomInOutput(TokenEnchanterRecipe recipe) {
        ItemStack output = getStackInSlot(INVENTORY_SIZE - 1);
        return output.isEmpty() || ItemHandlerHelper.canItemStacksStack(output, recipe.getResult());
    }

    private void consumeIngredients(TokenEnchanterRecipe recipe) {
        decrStackSize(0, 1);
        for (Map.Entry<Ingredient, Integer> entry : recipe.getIngredients().entrySet()) {
            Ingredient ingredient = entry.getKey();
            int countRemaining = entry.getValue();

            for (int i = 1; i < INVENTORY_SIZE - 1; ++i) {
                ItemStack stackInSlot = getStackInSlot(i);
                if (ingredient.test(stackInSlot)) {
                    int toRemove = Math.min(countRemaining, stackInSlot.getCount());
                    decrStackSize(i, toRemove);
                    countRemaining -= toRemove;

                    if (countRemaining <= 0) {
                        break;
                    }
                }
            }
        }
    }

    private void placeResultInOutput(TokenEnchanterRecipe recipe) {
        ItemStack output = getStackInSlot(INVENTORY_SIZE - 1);
        if (output.isEmpty()) {
            setInventorySlotContents(INVENTORY_SIZE - 1, recipe.getResult().copy());
        } else {
            output.setCount(output.getCount() + recipe.getResult().getCount());
        }
    }

    @Override
    protected ITextComponent getDefaultName() {
        return new TranslationTextComponent("container.silentgems.token_enchanter");
    }

    @Override
    protected Container createMenu(int p_213906_1_, PlayerInventory p_213906_2_) {
        return new TokenEnchanterContainer(p_213906_1_, p_213906_2_, this);
    }

    @SuppressWarnings("AssignmentOrReturnOfFieldWithMutableType")
    @Override
    public int[] getSlotsForFace(Direction side) {
        switch (side) {
            case UP:
                return SLOTS_INPUT;
            case DOWN:
                return SLOTS_ALL;
            default:
                return SLOTS_ALL;
        }
    }

    @Override
    public boolean canInsertItem(int index, ItemStack itemStackIn, @Nullable Direction direction) {
        return index < INVENTORY_SIZE - 1;
    }

    @Override
    public boolean canExtractItem(int index, ItemStack stack, Direction direction) {
        return index == INVENTORY_SIZE - 1;
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
    public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket packet) {
        super.onDataPacket(net, packet);
        SyncVariable.Helper.readSyncVars(this, packet.getNbtCompound());
    }

    @Override
    public CompoundNBT getUpdateTag() {
        CompoundNBT tags = super.getUpdateTag();
        SyncVariable.Helper.writeSyncVars(this, tags, SyncVariable.Type.PACKET);
        return tags;
    }
}
