package net.silentchaos512.gems.block.purifier;

import com.mojang.blaze3d.platform.GlStateManager;
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
    public void render(int mouseX, int mouseY, float partialTicks) {
        this.renderBackground();
        super.render(mouseX, mouseY, partialTicks);
        this.renderHoveredToolTip(mouseX, mouseY);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        if (minecraft == null) return;
        GlStateManager.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        minecraft.getTextureManager().bindTexture(TEXTURE);
        int xPos = (this.width - this.xSize) / 2;
        int yPos = (this.height - this.ySize) / 2;
        this.blit(xPos, yPos, 0, 0, this.xSize, this.ySize);

        // Progress arrow
        int time = container.getRemainingBurnTime();
        int totalTime = container.getTotalBurnTime();
        int height = totalTime != 0 && time > 0 && time < totalTime ? time * 23 / totalTime : 0;
        // 76, 31
        // 199, 23
        blit(xPos + 76, yPos + 31 + 23 - height, 176, 23 - height, 24, height);
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        this.font.drawString(title.getFormattedText(), (float)(this.xSize / 2 - this.font.getStringWidth(title.getFormattedText()) / 2), 6.0F, 4210752);
        this.font.drawString(this.playerInventory.getDisplayName().getFormattedText(), 8.0F, (float)(this.ySize - 96 + 2), 4210752);
    }
}
