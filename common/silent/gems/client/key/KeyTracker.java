package silent.gems.client.key;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityClientPlayerMP;
import net.minecraft.client.settings.KeyBinding;

import org.lwjgl.input.Keyboard;

import silent.gems.SilentGems;
import silent.gems.network.MessageChaosGemToggle;
import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.InputEvent.KeyInputEvent;


public class KeyTracker {

    public static KeyTracker instance = new KeyTracker();
    
    public static void init() {
        
        FMLCommonHandler.instance().bus().register(instance);
    }
    
    private KeyBinding chaosGemToggleFirst;
    
    public KeyTracker() {
        
        chaosGemToggleFirst = new KeyBinding("Chaos Gem - Toggle First", Keyboard.KEY_F, "Silent's Gems");
        ClientRegistry.registerKeyBinding(chaosGemToggleFirst);
    }
    
    @SubscribeEvent
    public void onKeyInput(KeyInputEvent event) {
        
        handleChaosGemToggleFirst();
    }
    
    private void handleChaosGemToggleFirst() {
        
        if (chaosGemToggleFirst.getIsKeyPressed()) {
            SilentGems.network.sendToServer(new MessageChaosGemToggle(Minecraft.getMinecraft().thePlayer));
        }
    }
}
