package net.silentchaos512.gems.client.key;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityClientPlayerMP;
import net.minecraft.client.settings.KeyBinding;
import net.silentchaos512.gems.SilentGems;
import net.silentchaos512.gems.lib.Reference;
import net.silentchaos512.gems.network.MessageChaosGemToggle;

import org.lwjgl.input.Keyboard;

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
    private KeyBinding chaosGemToggleAll;
    
    public KeyTracker() {
        
        chaosGemToggleFirst = new KeyBinding("Chaos Gem - Toggle First", Keyboard.KEY_F, Reference.MOD_NAME);
        ClientRegistry.registerKeyBinding(chaosGemToggleFirst);
        chaosGemToggleAll = new KeyBinding("Chaos Gem - Toggle All", Keyboard.KEY_H, Reference.MOD_NAME);
        ClientRegistry.registerKeyBinding(chaosGemToggleAll);
    }
    
    @SubscribeEvent
    public void onKeyInput(KeyInputEvent event) {
        
        handleChaosGemToggleFirst();
        handleChaosGemToggleAll();
    }
    
    private void handleChaosGemToggleFirst() {
        
        if (chaosGemToggleFirst.getIsKeyPressed()) {
            SilentGems.network.sendToServer(new MessageChaosGemToggle(false));
        }
    }
    
    private void handleChaosGemToggleAll() {
        
        if (chaosGemToggleAll.getIsKeyPressed()) {
            SilentGems.network.sendToServer(new MessageChaosGemToggle(true));
        }
    }
}
