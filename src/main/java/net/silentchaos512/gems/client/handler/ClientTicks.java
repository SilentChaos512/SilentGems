package net.silentchaos512.gems.client.handler;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.silentchaos512.gems.SilentGems;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedDeque;

public final class ClientTicks {
    private static final ClientTicks INSTANCE = new ClientTicks();
    private static final int QUEUE_OVERFLOW_LIMIT = 200;

    @SuppressWarnings("FieldMayBeFinal")
    private volatile Queue<Runnable> scheduledActions = new ConcurrentLinkedDeque<>();

    public int ticksInGame = 0;
    public float partialTicks = 0f;
    public float deltaTicks = 0f;
    public float totalTicks = 0f;

    private ClientTicks() {
        MinecraftForge.EVENT_BUS.addListener(this::clientTickEnd);
        MinecraftForge.EVENT_BUS.addListener(this::renderTick);
    }

    public static void scheduleAction(Runnable action) {
        INSTANCE.scheduledActions.add(action);

        if (INSTANCE.scheduledActions.size() >= QUEUE_OVERFLOW_LIMIT) {
            // Queue overflow?
            SilentGems.LOGGER.warn("Too many client tick actions queued! Currently at {} items. Would have added '{}'.",
                    INSTANCE.scheduledActions.size(), action);
            SilentGems.LOGGER.catching(new IllegalStateException("ClientTicks queue overflow"));
            INSTANCE.scheduledActions.clear();
        }
    }

    private void clientTickEnd(TickEvent.ClientTickEvent event) {
        if (event.phase != TickEvent.Phase.END) return;
        runScheduledActions();
        updateTickCounters();
    }

    private void renderTick(TickEvent.RenderTickEvent event) {
        if (event.phase == TickEvent.Phase.START)
            partialTicks = event.renderTickTime;
    }

    private void runScheduledActions() {
        Runnable action = scheduledActions.poll();
        while (action != null) {
            action.run();
            action = scheduledActions.poll();
        }
    }

    private void updateTickCounters() {
        GuiScreen gui = Minecraft.getInstance().currentScreen;
        if (gui == null || !gui.doesGuiPauseGame()) {
            ++ticksInGame;
            partialTicks = 0;
        }

        float oldTotal = totalTicks;
        totalTicks = ticksInGame + partialTicks;
        deltaTicks = totalTicks - oldTotal;
    }

    public static int ticksInGame() {
        return INSTANCE.ticksInGame;
    }

    public static float partialTicks() {
        return INSTANCE.partialTicks;
    }

    public static float deltaTicks() {
        return INSTANCE.deltaTicks;
    }

    public static float totalTicks() {
        return INSTANCE.totalTicks;
    }
}
