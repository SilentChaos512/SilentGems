package net.silentchaos512.gems.client.key;

import org.lwjgl.input.Keyboard;

import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent.KeyInputEvent;
import net.silentchaos512.gems.SilentGems;
import net.silentchaos512.gems.core.util.LogHelper;
import net.silentchaos512.gems.item.ChaosGem;
import net.silentchaos512.gems.network.MessageChaosGemToggle;

public class KeyTracker {

  public static KeyTracker instance = new KeyTracker();

  public static void init() {

    FMLCommonHandler.instance().bus().register(instance);
  }

  private EntityPlayer player;
  private KeyBinding chaosGemToggleFirst;
  //private KeyBinding chaosGemToggleAll;

  public KeyTracker() {

    // TODO: Combine into one, using shift to specify all?
    chaosGemToggleFirst = new KeyBinding("Chaos Gem - Toggle First", Keyboard.KEY_G,
        SilentGems.MOD_NAME);
    ClientRegistry.registerKeyBinding(chaosGemToggleFirst);
    // chaosGemToggleAll = new KeyBinding("Chaos Gem - Toggle All", Keyboard.KEY_H,
    // SilentGems.MOD_NAME);
    // ClientRegistry.registerKeyBinding(chaosGemToggleAll);

    player = Minecraft.getMinecraft().thePlayer;
  }

  private boolean isShiftPressed() {

    return Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) || Keyboard.isKeyDown(Keyboard.KEY_RSHIFT);
  }

  @SubscribeEvent
  public void onKeyInput(KeyInputEvent event) {

    player = Minecraft.getMinecraft().thePlayer;
    handleChaosGemToggleFirst();
    handleChaosGemToggleAll();
  }

  private void handleChaosGemToggleFirst() {

    boolean shifted = isShiftPressed();
    if (chaosGemToggleFirst.isPressed() && !shifted) {
      // Client
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

    boolean shiftToggle = chaosGemToggleFirst.isKeyDown() && isShiftPressed();
    if (shiftToggle) {
      // Client
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
