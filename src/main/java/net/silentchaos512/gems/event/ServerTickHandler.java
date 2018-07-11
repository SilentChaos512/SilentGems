package net.silentchaos512.gems.event;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.ArrayDeque;
import java.util.Queue;

/**
 * Similar to {@link net.silentchaos512.gems.client.handler.ClientTickHandler}, allows actions that
 * don't belong on the network thread to be queued up to run.
 */
public class ServerTickHandler {

    public static final ServerTickHandler INSTANCE = new ServerTickHandler();

    private static final Queue<Runnable> scheduledActions = new ArrayDeque<>();

    private ServerTickHandler() {
        MinecraftForge.EVENT_BUS.register(this);
    }

    public static void schedule(Runnable runnable) {
        scheduledActions.add(runnable);
    }

    @SubscribeEvent
    public void serverTickEnd(TickEvent.ServerTickEvent event) {
        if (event.phase == TickEvent.Phase.START) return;
        while (!scheduledActions.isEmpty()) {
            scheduledActions.poll().run();
        }
    }
}
