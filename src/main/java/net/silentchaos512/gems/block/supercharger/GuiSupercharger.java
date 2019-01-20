/*
 * Silent's Gems -- GuiSupercharger
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

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import net.silentchaos512.gems.SilentGems;

public class GuiSupercharger extends GuiContainer {
    private static final ResourceLocation TEXTURE = new ResourceLocation(SilentGems.MOD_ID, "textures/gui/chaosaltar.png");
    private final TileSupercharger tileEntity;

    public GuiSupercharger(InventoryPlayer inventoryPlayer, TileSupercharger tileEntity) {
        super(new ContainerSupercharger(inventoryPlayer, tileEntity));
        this.tileEntity = tileEntity;
    }

    @Override
    public void render(int mouseX, int mouseY, float partialTicks) {
        this.drawDefaultBackground();
        super.render(mouseX, mouseY, partialTicks);
        this.renderHoveredToolTip(mouseX, mouseY);
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
//        if (GemsConfig.DEBUG_MODE) {
//            int y = 5;
//            for (String line : getDebugText().split("\n")) {
//                this.fontRenderer.drawStringWithShadow(line, 5, y, 0xFFFFFF);
//                y += 10;
//            }
//        }
    }

    private String getDebugText() {
        final int chaos = this.tileEntity.getField(0);
        final int maxChaos = TileSupercharger.MAX_CHAOS_STORED;
        final int progress = this.tileEntity.getField(1);
        final int cost = this.tileEntity.getChaosCostForCharging();
        final int rate = this.tileEntity.getChaosDrainPerTick();
        return String.format("Chaos: %,d / %,d\nProgress: %,d / %,d\nRate: %d", chaos, maxChaos, progress, cost, rate);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        GlStateManager.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.getTextureManager().bindTexture(TEXTURE);
        int xPos = (this.width - this.xSize) / 2;
        int yPos = (this.height - this.ySize) / 2;
        this.drawTexturedModalRect(xPos, yPos, 0, 0, this.xSize, this.ySize);

        // Progress arrow
        int progress = this.tileEntity.getField(1);
        int cost = this.tileEntity.getChaosCostForCharging();
        int length = cost > 0 && progress > 0 && progress < cost ? progress * 24 / cost : 0;
        drawTexturedModalRect(xPos + 79, yPos + 34, 176, 14, length + 1, 16);

        // Chaos stored
        int chaos = this.tileEntity.getField(0);
        drawTexturedModalRect(xPos + 79, yPos + 34, 176, 31, 24 * chaos / TileSupercharger.MAX_CHAOS_STORED, 17);
    }
}
