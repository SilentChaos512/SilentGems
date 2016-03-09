/*
    * This class is basically a rewrite of <Vaskii>'s class with the same name adapted to our environment.
    * All credit for the creation of this class goes to <Vaskii>.
    *
    * This class is used in order allow block animations rely on the client ticks rather than the tile
    * entity's data - this allows for smoother animations and less chance of visual lag.
 */

package net.silentchaos512.gems.client.render.handlers;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class ClientTickHandler {

    public static int ticksInGame = 0;
    public static float partialTicks = 0;
    public static float delta = 0;
    public static float total = 0;

    private void calcDelta()
    {
        float oldTotal = total;
        total = ticksInGame + partialTicks;
        delta = total - oldTotal;
    }

    @SubscribeEvent
    public void renderTick(TickEvent.RenderTickEvent event)
    {
        if (event.phase == TickEvent.Phase.START)
        {
            partialTicks = event.renderTickTime;
        }
        else
        {
            calcDelta();
        }
    }

    @SubscribeEvent
    public void clientTickEnd(TickEvent.ClientTickEvent event)
    {
        if (event.phase == TickEvent.Phase.END)
        {
            GuiScreen gui = Minecraft.getMinecraft().currentScreen;
            if (gui == null || !gui.doesGuiPauseGame())
            {
                ticksInGame++;
                partialTicks = 0;
            }
            calcDelta();
        }
    }
}
