package silent.gems.client.key;

import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent.KeyInputEvent;

import org.lwjgl.input.Keyboard;

import silent.gems.SilentGems;
import silent.gems.network.MessageChaosGemToggle;

public class KeyTracker {

  public static KeyTracker instance = new KeyTracker();

  public static void init() {

    FMLCommonHandler.instance().bus().register(instance);
  }

  private KeyBinding chaosGemToggleFirst;
  private KeyBinding chaosGemToggleAll;

  public KeyTracker() {

    chaosGemToggleFirst = new KeyBinding("Chaos Gem - Toggle First", Keyboard.KEY_F,
        SilentGems.MOD_NAME);
    ClientRegistry.registerKeyBinding(chaosGemToggleFirst);
    chaosGemToggleAll = new KeyBinding("Chaos Gem - Toggle All", Keyboard.KEY_H,
        SilentGems.MOD_NAME);
    ClientRegistry.registerKeyBinding(chaosGemToggleAll);
  }

  @SubscribeEvent
  public void onKeyInput(KeyInputEvent event) {

    handleChaosGemToggleFirst();
    handleChaosGemToggleAll();
  }

  private void handleChaosGemToggleFirst() {

    if (chaosGemToggleFirst.isPressed()) {
      SilentGems.network.sendToServer(new MessageChaosGemToggle(false));
    }
  }

  private void handleChaosGemToggleAll() {

    if (chaosGemToggleAll.isPressed()) {
      SilentGems.network.sendToServer(new MessageChaosGemToggle(true));
    }
  }
}
