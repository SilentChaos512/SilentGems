package net.silentchaos512.gems.client.key;

import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.silentchaos512.gems.SilentGems;
import net.silentchaos512.gems.item.ChaosGem;
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

    // TODO: Combine into one, using shift to specify all?
    chaosGemToggleFirst = new KeyBinding("Chaos Gem - Toggle First", Keyboard.KEY_G,
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

    boolean shifted = Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)
        || Keyboard.isKeyDown(Keyboard.KEY_RSHIFT);
    if (chaosGemToggleFirst.getIsKeyPressed() && !shifted) {
      // Client
      EntityPlayer player = Minecraft.getMinecraft().thePlayer;
      for (ItemStack stack : player.inventory.mainInventory) {
        if (stack != null && stack.getItem() instanceof ChaosGem) {
          ((ChaosGem) stack.getItem()).onItemRightClick(stack, player.worldObj, player);
          break;
        }
      }
      // Server
      SilentGems.network.sendToServer(new MessageChaosGemToggle(false));
    }
  }

  private void handleChaosGemToggleAll() {

    boolean shiftToggle = chaosGemToggleFirst.getIsKeyPressed()
        && (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) || Keyboard.isKeyDown(Keyboard.KEY_RSHIFT));
    if (chaosGemToggleAll.getIsKeyPressed() || shiftToggle) {
      // Client
      EntityPlayer player = Minecraft.getMinecraft().thePlayer;
      for (ItemStack stack : player.inventory.mainInventory) {
        if (stack != null && stack.getItem() instanceof ChaosGem) {
          ((ChaosGem) stack.getItem()).onItemRightClick(stack, player.worldObj, player);
        }
      }
      // Server
      SilentGems.network.sendToServer(new MessageChaosGemToggle(true));
    }
  }
}
