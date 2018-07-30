package net.silentchaos512.gems.client.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.client.GuiIngameForge;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.silentchaos512.gems.SilentGems;
import net.silentchaos512.gems.client.handler.ClientTickHandler;
import net.silentchaos512.gems.config.GemsConfig;
import net.silentchaos512.gems.handler.PlayerDataHandler;
import net.silentchaos512.gems.handler.PlayerDataHandler.PlayerData;
import org.lwjgl.util.Color;

@SideOnly(Side.CLIENT)
public class GuiChaosBar extends Gui {
    public static final GuiChaosBar INSTANCE = new GuiChaosBar(Minecraft.getMinecraft());

    private static final ResourceLocation TEXTURE = new ResourceLocation(SilentGems.MODID, "textures/gui/hud.png");
    private static final int POPUP_TIME = 300;
    private static final float COLOR_CHANGE_DELAY = 200f;
    private static final float COLOR_CHANGE_STEP = 8f;

    private int currentChaos = 0;
    private int maxChaos = 10000;
    private int lastUpdateTime = 0;
    private int currentTime;

    private Minecraft mc;

    public GuiChaosBar(Minecraft mc) {
        super();
        this.mc = mc;
    }

    public void update(int currentChaos, int maxChaos) {
        this.currentChaos = currentChaos;
        this.maxChaos = maxChaos < currentChaos ? currentChaos : maxChaos;
        show();
    }

    public void show() {
        lastUpdateTime = ClientTickHandler.ticksInGame;
    }

    @SubscribeEvent
    public void onRenderGameOverlay(RenderGameOverlayEvent.Pre event) {
        if (event.getType() != ElementType.AIR) return;

        Minecraft mc = Minecraft.getMinecraft();
        EntityPlayer player = mc.player;

        currentTime = ClientTickHandler.ticksInGame;
        if (player.capabilities.isCreativeMode || (!GemsConfig.CHAOS_BAR_SHOW_ALWAYS && currentTime > lastUpdateTime + POPUP_TIME))
            return;

        int width = event.getResolution().getScaledWidth();
        int height = event.getResolution().getScaledHeight();

        PlayerData data = PlayerDataHandler.get(player);
        int chaos = data.getCurrentChaos();
        int maxChaos = data.getMaxChaos();
        int chaosHalves = (int) (20f * chaos / maxChaos);

        int rowHeight = 10;

        int left = width / 2 + 10 + GemsConfig.CHAOS_BAR_OFFSET_X;
        int top = height - GuiIngameForge.right_height + GemsConfig.CHAOS_BAR_OFFSET_Y;
        if (GemsConfig.CHAOS_BAR_BUMP_HEIGHT)
            GuiIngameForge.right_height += rowHeight;

        final int textureX = 0;
        final int textureY = 23;
        final int textureWidth = 9;
        final int textureHeight = 9;

        GlStateManager.pushMatrix();
        GlStateManager.enableBlend();
        mc.renderEngine.bindTexture(TEXTURE);

        for (int i = 9; i >= 0; --i) {
            int row = MathHelper.ceil((i + 1) / 10f) - 1;
            int x = left + i % 10 * 8;
            int y = top - row * rowHeight;

            GlStateManager.color(1f, 1f, 1f, 1f);

            drawTexturedModalRect(x, y, textureX + 2 * textureWidth, textureY, textureWidth, textureHeight);

            Color color = new Color();
            float hue = ((currentTime + COLOR_CHANGE_STEP * i) % COLOR_CHANGE_DELAY) / COLOR_CHANGE_DELAY;
            color.fromHSB(hue, 0.6f, 1f);

            GlStateManager.color(color.getRed() / 255f, color.getGreen() / 255f, color.getBlue() / 255f, 1f);

            if (i * 2 + 1 < chaosHalves) {
                drawTexturedModalRect(x, y, textureX, textureY, textureWidth, textureHeight);
            } else if (i * 2 + 1 == chaosHalves) {
                drawTexturedModalRect(x, y, textureX + textureWidth, textureY, textureWidth, textureHeight);
            }
        }

        GlStateManager.color(1f, 1f, 1f, 1f);

        mc.renderEngine.bindTexture(Gui.ICONS);
        GlStateManager.disableBlend();
        GlStateManager.popMatrix();
    }
}
