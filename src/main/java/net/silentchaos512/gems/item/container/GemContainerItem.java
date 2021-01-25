package net.silentchaos512.gems.item.container;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.inventory.container.SimpleNamedContainerProvider;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.Util;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

public abstract class GemContainerItem extends Item implements IContainerItem {
    private final ITextComponent containerName;

    public GemContainerItem(String containerName, Properties properties) {
        super(properties);
        this.containerName = new TranslationTextComponent("container.silentgems." + containerName);
    }

    protected abstract ContainerType<? extends GemContainer> getContainerType();

    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        tooltip.add(new TranslationTextComponent(Util.makeTranslationKey("item", this.getRegistryName()) + ".desc"));
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand handIn) {
        if (!worldIn.isRemote) {
            playerIn.openContainer(new SimpleNamedContainerProvider((id, playerInventory, player) ->
                    this.getContainerType().create(id, playerInventory),
                    this.containerName));
        }

        return new ActionResult<>(ActionResultType.SUCCESS, playerIn.getHeldItem(handIn));
    }
}
