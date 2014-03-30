package silent.gems.client.gui.inventory;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.StatCollector;
import silent.gems.inventory.ContainerShinyCrafter;
import silent.gems.lib.Strings;
import silent.gems.lib.Textures;
import silent.gems.tileentity.TileShinyCrafter;


public class GuiShinyCrafter extends GuiContainer {

    private TileShinyCrafter tile;
    
    public GuiShinyCrafter(InventoryPlayer player, TileShinyCrafter tile) {

        super(new ContainerShinyCrafter(player, tile));
        ySize = 176;
        this.tile = tile;
    }
    
    @Override
    protected void drawGuiContainerForegroundLayer(int x, int y) {
        
        String containerName = tile.isInvNameLocalized() ? tile.getInvName() : StatCollector.translateToLocal(tile.getInvName());
        fontRenderer.drawString(containerName, xSize / 2 - fontRenderer.getStringWidth(containerName) / 2, 6, 4210752);
        fontRenderer.drawString(StatCollector.translateToLocal(Strings.CONTAINER_INVENTORY), 8, ySize - 103, 4210752);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float opacity, int x, int y) {

        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
        
        this.mc.getTextureManager().bindTexture(Textures.GUI_SHINY_CRAFTER);
        
        int xStart = (width - xSize) / 2;
        int yStart = (height - ySize) / 2;
        this.drawTexturedModalRect(xStart, yStart, 0, 0, xSize, ySize);
        int scaleAdjustment = 1;

        //this.drawTexturedModalRect(xStart + 83, yStart + 34, 176, 14, scaleAdjustment + 1, 16);
    }
}
