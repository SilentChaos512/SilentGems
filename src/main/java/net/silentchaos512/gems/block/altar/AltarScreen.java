package net.silentchaos512.gems.block.altar;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
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
    public void render(MatrixStack matrix, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(matrix);
        super.render(matrix, mouseX, mouseY, partialTicks);
        this.func_230459_a_(matrix, mouseX, mouseY);
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
        int progress = container.getProgress();
        int cost = container.getProcessTime();
        int length = cost != 0 && progress > 0 && progress < cost ? progress * 24 / cost : 0;
        blit(matrixStack, xPos + 79, yPos + 34, 176, 14, length + 1, 16);
    }

    @Override
    protected void drawGuiContainerForegroundLayer(MatrixStack matrixStack, int x, int y) {
        this.font.drawString(matrixStack, title.getString(), (float)(this.xSize / 2 - this.font.getStringWidth(title.getString()) / 2), 6.0F, 4210752);
        this.font.drawString(matrixStack, this.playerInventory.getDisplayName().getString(), 8.0F, (float)(this.ySize - 96 + 2), 4210752);

        // Chaos generated
        int chaosGenerated = container.getChaosGenerated();
        ChaosEmissionRate emissionRate = ChaosEmissionRate.fromAmount(chaosGenerated);
        String text = emissionRate.getEmissionText(chaosGenerated).getString();
        font.drawString(matrixStack, text, 5, 7 + font.FONT_HEIGHT, 4210752);
    }
}
