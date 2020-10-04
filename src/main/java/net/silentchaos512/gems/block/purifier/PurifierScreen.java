package net.silentchaos512.gems.block.purifier;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.silentchaos512.gems.SilentGems;

public class PurifierScreen extends ContainerScreen<PurifierContainer> {
    private static final ResourceLocation TEXTURE = SilentGems.getId("textures/gui/purifier.png");

    private final PlayerInventory playerInventory;

    public PurifierScreen(PurifierContainer container, PlayerInventory playerInventory, ITextComponent title) {
        super(container, playerInventory, title);
        this.playerInventory = playerInventory;
    }

    @Override
    public void render(MatrixStack matrix, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(matrix);
        super.render(matrix, mouseX, mouseY, partialTicks);
        this.renderHoveredTooltip(matrix, mouseX, mouseY);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(MatrixStack matrixStack, float partialTicks, int x, int y) {
        if (minecraft == null) return;
        RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        minecraft.getTextureManager().bindTexture(TEXTURE);
        int xPos = (this.width - this.xSize) / 2;
        int yPos = (this.height - this.ySize) / 2;
        this.blit(matrixStack, xPos, yPos, 0, 0, this.xSize, this.ySize);

        // Progress arrow
        int time = container.getRemainingBurnTime();
        int totalTime = container.getTotalBurnTime();
        int height = totalTime != 0 && time > 0 && time < totalTime ? time * 23 / totalTime : 0;
        // 76, 31
        // 199, 23
        blit(matrixStack, xPos + 76, yPos + 31 + 23 - height, 176, 23 - height, 24, height);
    }

    @Override
    protected void drawGuiContainerForegroundLayer(MatrixStack matrixStack, int x, int y) {
        this.font.drawString(matrixStack, title.getString(), (float)(this.xSize / 2 - this.font.getStringWidth(title.getString()) / 2), 6.0F, 4210752);
        this.font.drawString(matrixStack, this.playerInventory.getDisplayName().getString(), 8.0F, (float)(this.ySize - 96 + 2), 4210752);
    }
}
