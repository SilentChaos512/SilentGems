package net.silentchaos512.gems.block.tokenenchanter;

import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Direction;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.silentchaos512.gems.block.AbstractChaosMachineTileEntity;
import net.silentchaos512.gems.crafting.recipe.TokenEnchanterRecipe;
import net.silentchaos512.gems.init.GemsTileEntities;

import javax.annotation.Nullable;
import java.util.stream.IntStream;

public class TokenEnchanterTileEntity extends AbstractChaosMachineTileEntity<TokenEnchanterRecipe> {
    private static final int INVENTORY_SIZE = 1 + 6 + 1;
    private static final int[] SLOTS_INPUT = IntStream.range(0, 7).toArray();
    private static final int[] SLOTS_OUTPUT = {7};
    private static final int[] SLOTS_ALL = IntStream.range(0, 8).toArray();

    public TokenEnchanterTileEntity() {
        super(GemsTileEntities.TOKEN_ENCHANTER, INVENTORY_SIZE);
    }

    @Override
    protected int[] getOutputSlots() {
        //noinspection AssignmentOrReturnOfFieldWithMutableType
        return SLOTS_OUTPUT;
    }

    @Nullable
    @Override
    protected TokenEnchanterRecipe getRecipe() {
        if (world == null) return null;
        return world.getRecipeManager().getRecipe(TokenEnchanterRecipe.RECIPE_TYPE, this, world).orElse(null);
    }

    @Override
    protected int getChaosGenerated(TokenEnchanterRecipe recipe) {
        return recipe.getChaosGenerated();
    }

    @Override
    protected int getProcessTime(TokenEnchanterRecipe recipe) {
        return recipe.getProcessTime();
    }

    @Override
    protected ItemStack getProcessResult(TokenEnchanterRecipe recipe) {
        return recipe.getCraftingResult(this);
    }

    @Override
    protected void consumeIngredients(TokenEnchanterRecipe recipe) {
        recipe.consumeIngredients(this);
    }

    @Override
    protected ITextComponent getDefaultName() {
        return new TranslationTextComponent("container.silentgems.token_enchanter");
    }

    @Override
    protected Container createMenu(int p_213906_1_, PlayerInventory p_213906_2_) {
        return new TokenEnchanterContainer(p_213906_1_, p_213906_2_, this, this.fields);
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
}
