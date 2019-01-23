package net.silentchaos512.gems.client.key;

import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.client.settings.IKeyConflictContext;
import net.minecraftforge.client.settings.KeyModifier;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent.KeyInputEvent;
import org.apache.commons.lang3.NotImplementedException;

public class KeyTracker {
    public static KeyTracker INSTANCE = new KeyTracker();

//    private EntityPlayer player;
//    private KeyBinding toggleSpecial = createBinding("Toggle Special",
//            KeyConflictContext.IN_GAME, KeyModifier.SHIFT, Keyboard.KEY_C);
//    private KeyBinding toggleChaosGemFirst = createBinding("Toggle Chaos Gem (First)",
//            KeyConflictContext.IN_GAME, KeyModifier.NONE, Keyboard.KEY_G);
//    private KeyBinding toggleChaosGemAll = createBinding("Toggle Chaos Gem (All)",
//            KeyConflictContext.IN_GAME, KeyModifier.SHIFT, Keyboard.KEY_G);
//    private KeyBinding triggerReturnHome = createBinding("Use Return Home Charm",
//            KeyConflictContext.IN_GAME, KeyModifier.SHIFT, Keyboard.KEY_P);

    /**
     * Creates and registers a KeyBinding.
     *
     * @return The created KeyBinding.
     */
    private KeyBinding createBinding(String name, IKeyConflictContext keyConflictContext, KeyModifier keyModifier, int keyCode) {
//        KeyBinding binding = new KeyBinding(name, keyConflictContext, keyModifier, keyCode, SilentGems.MOD_ID);
//        ClientRegistry.registerKeyBinding(binding);
//        return binding;
        throw new NotImplementedException("Not implemented");
    }

    public static boolean isShiftDown() {
//        return InputMappings.isKeyDown(GLFW.GLFW_MOD_SHIFT);
        return false;
    }

    public static boolean isControlDown() {
//        return InputMappings.isKeyDown(GLFW.GLFW_MOD_CONTROL);
        return false;
    }

    public static boolean isAltDown() {
//        return InputMappings.isKeyDown(GLFW.GLFW_MOD_ALT);
        return false;
    }

    @SubscribeEvent
    public void onKeyInput(KeyInputEvent event) {
//        this.player = Minecraft.getMinecraft().player;
//
//        if (toggleSpecial.isKeyDown())
//            handleToggleSpecial();
//
//        if (toggleChaosGemAll.isKeyDown())
//            handleToggleChaosGem(true);
//        else if (toggleChaosGemFirst.isKeyDown())
//            handleToggleChaosGem(false);
//
//        if (triggerReturnHome.isPressed())
//            handleTriggerReturnHome();
    }

    private void handleToggleChaosGem(boolean all) {
//        NetworkHandler.INSTANCE.sendToServer(new MessageToggleChaosGem(all));
    }

    private void handleTriggerReturnHome() {
//        NetworkHandler.INSTANCE.sendToServer(new MessageKeyReturnHome());
    }
}
