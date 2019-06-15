package net.silentchaos512.gems.block.altar;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import net.silentchaos512.gems.SilentGems;
import net.silentchaos512.gems.api.chaos.ChaosEmissionRate;

public class AltarGui extends GuiContainer {
    private static final ResourceLocation TEXTURE = SilentGems.getId("textures/gui/altar.png");

    private final InventoryPlayer playerInventory;
    private final AltarTileEntity altar;

    public AltarGui(InventoryPlayer inventoryPlayer, AltarTileEntity altar) {
        super(new AltarContainer(inventoryPlayer, altar));
        this.playerInventory = inventoryPlayer;
        this.altar = altar;
    }

    @Override
    public void render(int mouseX, int mouseY, float partialTicks) {
        this.drawDefaultBackground();
        super.render(mouseX, mouseY, partialTicks);
        this.renderHoveredToolTip(mouseX, mouseY);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        GlStateManager.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.getTextureManager().bindTexture(TEXTURE);
        int xPos = (this.width - this.xSize) / 2;
        int yPos = (this.height - this.ySize) / 2;
        this.drawTexturedModalRect(xPos, yPos, 0, 0, this.xSize, this.ySize);

        // Progress arrow
        int progress = this.altar.getProgress();
        int cost = this.altar.getProcessTime();
        int length = cost != 0 && progress > 0 && progress < cost ? progress * 24 / cost : 0;
        drawTexturedModalRect(xPos + 79, yPos + 34, 176, 14, length + 1, 16);
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        String s = this.altar.getDisplayName().getFormattedText();
        this.fontRenderer.drawString(s, (float)(this.xSize / 2 - this.fontRenderer.getStringWidth(s) / 2), 6.0F, 4210752);
        this.fontRenderer.drawString(this.playerInventory.getDisplayName().getFormattedText(), 8.0F, (float)(this.ySize - 96 + 2), 4210752);

        // Chaos generated
        int chaosGenerated = this.altar.getChaosGenerated();
        ChaosEmissionRate emissionRate = ChaosEmissionRate.fromAmount(chaosGenerated);
        String text = emissionRate.getEmissionText(chaosGenerated).getFormattedText();
        fontRenderer.drawString(text, 5, 7 + fontRenderer.FONT_HEIGHT, 4210752);
    }
}
