package net.silentchaos512.gems.item.container;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.network.chat.Component;

public class GemContainerScreen extends AbstractContainerScreen<GemContainer> {
    private static final ResourceLocation TEXTURE = new ResourceLocation("textures/gui/container/generic_54.png");

    private final Inventory playerInventory;
    private final int inventoryRows;

    public GemContainerScreen(GemContainer container, Inventory playerInventory, Component title) {
        super(container, playerInventory, title);
        this.playerInventory = playerInventory;
        this.inventoryRows = container.getInventoryRows();
        this.imageHeight = 114 + this.inventoryRows * 18;
    }

    @Override
    public void render(PoseStack matrix, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(matrix);
        super.render(matrix, mouseX, mouseY, partialTicks);
        this.renderTooltip(matrix, mouseX, mouseY);
    }

    @Override
    protected void renderLabels(PoseStack matrixStack, int x, int y) {
        this.font.draw(matrixStack, title.getString(), 8, 6, 4210752);
        this.font.draw(matrixStack, playerInventory.getDisplayName().getString(), 8, this.imageHeight - 96 + 2, 4210752);
    }

    @Override
    protected void renderBg(PoseStack matrixStack, float partialTicks, int x, int y) {
        if (minecraft == null) return;
        RenderSystem.clearColor(1, 1, 1, 1);
        RenderSystem.setShaderTexture(0, TEXTURE);
        int i = (this.width - this.imageWidth) / 2;
        int j = (this.height - this.imageHeight) / 2;
        blit(matrixStack, i, j, 0, 0, this.imageWidth, this.inventoryRows * 18 + 17);
        blit(matrixStack, i, j + this.inventoryRows * 18 + 17, 0, 126, this.imageWidth, 96);
    }
}
