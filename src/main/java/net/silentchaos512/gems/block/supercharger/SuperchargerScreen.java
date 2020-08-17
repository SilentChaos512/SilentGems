/*
 * Silent's Gems -- SuperchargerScreen
 * Copyright (C) 2018 SilentChaos512
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation version 3
 * of the License.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package net.silentchaos512.gems.block.supercharger;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.silentchaos512.gems.SilentGems;
import net.silentchaos512.gems.api.chaos.ChaosEmissionRate;

public class SuperchargerScreen extends ContainerScreen<SuperchargerContainer> {
    private static final ResourceLocation TEXTURE = new ResourceLocation(SilentGems.MOD_ID, "textures/gui/altar.png");

    public SuperchargerScreen(SuperchargerContainer container, PlayerInventory playerInventory, ITextComponent title) {
        super(container, playerInventory, title);
    }

    @Override
    public void render(MatrixStack matrix, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(matrix);
        super.render(matrix, mouseX, mouseY, partialTicks);
        this.func_230459_a_(matrix, mouseX, mouseY);
    }

    @Override
    protected void drawGuiContainerForegroundLayer(MatrixStack matrixStack, int x, int y) {
        int color = -1;

        // Structure tier
        int tier = container.tileEntity.getStructureLevel();
        String textTier = I18n.format("block.silentgems.supercharger.tier", String.valueOf(tier));
        font.drawStringWithShadow(matrixStack, textTier, 5, 5, color);

        // Chaos generated
        int chaosGenerated = container.tileEntity.getChaosGenerated();
        ChaosEmissionRate emissionRate = ChaosEmissionRate.fromAmount(chaosGenerated);
        String text = emissionRate.getEmissionText(chaosGenerated).getString();
        font.drawStringWithShadow(matrixStack, text, 5, 15, color);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(MatrixStack matrixStack, float partialTicks, int x, int y) {
        if (minecraft == null) return;
        RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        minecraft.getTextureManager().bindTexture(TEXTURE);
        int xPos = (this.width - this.xSize) / 2;
        int yPos = (this.height - this.ySize) / 2;
        blit(matrixStack, xPos, yPos, 0, 0, this.xSize, this.ySize);

        // Progress arrow
        int progress = container.tileEntity.getProgress();
        int cost = container.tileEntity.getProcessTime();
        int length = cost > 0 && progress > 0 && progress < cost ? progress * 24 / cost : 0;
        blit(matrixStack, xPos + 79, yPos + 34, 176, 14, length + 1, 16);
    }
}
