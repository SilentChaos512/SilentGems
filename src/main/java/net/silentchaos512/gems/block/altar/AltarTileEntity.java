package net.silentchaos512.gems.block.altar;

import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Direction;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.silentchaos512.gems.block.AbstractChaosMachineTileEntity;
import net.silentchaos512.gems.crafting.recipe.AltarTransmutationRecipe;
import net.silentchaos512.gems.init.GemsTileEntities;

import javax.annotation.Nullable;

public class AltarTileEntity extends AbstractChaosMachineTileEntity<AltarTransmutationRecipe> {
    private static final int INVENTORY_SIZE = 3;

    public AltarTileEntity() {
        super(GemsTileEntities.TRANSMUTATION_ALTAR, INVENTORY_SIZE);
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
        return new AltarContainer(p_213906_1_, p_213906_2_, this, this.fields);
    }

    @Override
    protected int[] getOutputSlots() {
        return new int[]{2};
    }

    @Nullable
    @Override
    protected AltarTransmutationRecipe getRecipe() {
        if (world == null) return null;
        return world.getRecipeManager().getRecipe(AltarTransmutationRecipe.RECIPE_TYPE, this, world).orElse(null);
    }

    @Override
    protected int getChaosGenerated(AltarTransmutationRecipe recipe) {
        return recipe.getChaosGenerated();
    }

    @Override
    protected int getProcessTime(AltarTransmutationRecipe recipe) {
        return recipe.getProcessTime();
    }

    @Override
    protected ItemStack getProcessResult(AltarTransmutationRecipe recipe) {
        return recipe.getCraftingResult(this);
    }

    @Override
    protected void consumeIngredients(AltarTransmutationRecipe recipe) {
        decrStackSize(0, 1);
    }
}
