package net.silentchaos512.gems.block.tokenenchanter;

import lombok.Getter;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.ItemHandlerHelper;
import net.silentchaos512.gems.chaos.Chaos;
import net.silentchaos512.gems.crafting.tokenenchanter.TokenEnchanterRecipe;
import net.silentchaos512.gems.crafting.tokenenchanter.TokenEnchanterRecipeManager;
import net.silentchaos512.gems.init.GemsTileEntities;
import net.silentchaos512.lib.tile.SyncVariable;
import net.silentchaos512.lib.tile.TileSidedInventorySL;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Map;

public class TokenEnchanterTileEntity extends TileSidedInventorySL implements ITickable {
    private static final int INVENTORY_SIZE = 1 + 6 + 1;

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
        super(GemsTileEntities.TOKEN_ENCHANTER.type());
    }

    @Override
    public void tick() {
        if (this.world.isRemote) return;

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
    public int getSizeInventory() {
        return INVENTORY_SIZE;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public ITextComponent getName() {
        return new TextComponentTranslation("container.silentgems.token_enchanter");
    }

    @Nullable
    @Override
    public ITextComponent getCustomName() {
        return null;
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable EnumFacing side) {
        return super.getCapability(cap, side);
    }
}
