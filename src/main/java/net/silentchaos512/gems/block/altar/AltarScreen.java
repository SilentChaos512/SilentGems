package net.silentchaos512.gems.block.altar;

import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.silentchaos512.gems.SilentGems;
import net.silentchaos512.gems.api.chaos.ChaosEmissionRate;

public class AltarScreen extends ContainerScreen<AltarContainer> {
    private static final ResourceLocation TEXTURE = SilentGems.getId("textures/gui/altar.png");

    private final PlayerInventory playerInventory;

    public AltarScreen(AltarContainer container, PlayerInventory playerInventory, ITextComponent title) {
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
        int progress = container.tileEntity.getProgress();
        int cost = container.tileEntity.getProcessTime();
        int length = cost != 0 && progress > 0 && progress < cost ? progress * 24 / cost : 0;
        blit(xPos + 79, yPos + 34, 176, 14, length + 1, 16);
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        this.font.drawString(title.getFormattedText(), (float)(this.xSize / 2 - this.font.getStringWidth(title.getFormattedText()) / 2), 6.0F, 4210752);
        this.font.drawString(this.playerInventory.getDisplayName().getFormattedText(), 8.0F, (float)(this.ySize - 96 + 2), 4210752);

        // Chaos generated
        int chaosGenerated = container.tileEntity.getChaosGenerated();
        ChaosEmissionRate emissionRate = ChaosEmissionRate.fromAmount(chaosGenerated);
        String text = emissionRate.getEmissionText(chaosGenerated).getFormattedText();
        font.drawString(text, 5, 7 + font.FONT_HEIGHT, 4210752);
    }
}
