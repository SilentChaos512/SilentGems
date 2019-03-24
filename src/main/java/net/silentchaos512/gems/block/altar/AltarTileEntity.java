package net.silentchaos512.gems.block.altar;

import lombok.Getter;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ITickable;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.items.ItemHandlerHelper;
import net.silentchaos512.gems.chaos.Chaos;
import net.silentchaos512.gems.crafting.altar.AltarRecipe;
import net.silentchaos512.gems.crafting.altar.AltarRecipeManager;
import net.silentchaos512.gems.init.ModTileEntities;
import net.silentchaos512.lib.tile.SyncVariable;
import net.silentchaos512.lib.tile.TileSidedInventorySL;

import javax.annotation.Nullable;

public class AltarTileEntity extends TileSidedInventorySL implements ITickable {
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

    public AltarTileEntity() {
        super(ModTileEntities.TRANSMUTATION_ALTAR.type());
    }

    @Override
    public void tick() {
        if (this.world.isRemote) return;

        AltarRecipe recipe = getMatchingRecipe();
        if (recipe != null && hasRoomInOutput(recipe)) {
            // Process
            ++progress;
            chaosGenerated = recipe.getChaosGenerated();
            processTime = recipe.getProcessTime();
            Chaos.generate(this.world, chaosGenerated);

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
    public int getSizeInventory() {
        return INVENTORY_SIZE;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public ITextComponent getName() {
        return new TextComponentTranslation("container.silentgems.transmutation_altar");
    }

    @Nullable
    @Override
    public ITextComponent getCustomName() {
        return null;
    }
}
