package net.silentchaos512.gems.client.key;

import org.lwjgl.input.Keyboard;

import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.InputEvent.KeyInputEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
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

  public KeyTracker() {

    // TODO: Combine into one, using shift to specify all?
    chaosGemToggleFirst = new KeyBinding("Chaos Gem - Toggle First", Keyboard.KEY_F,
        SilentGems.MOD_NAME);
    ClientRegistry.registerKeyBinding(chaosGemToggleFirst);

    player = Minecraft.getMinecraft().thePlayer;
  }

  @SubscribeEvent
  public void onKeyInput(KeyInputEvent event) {

    player = Minecraft.getMinecraft().thePlayer;
    handleChaosGemToggleFirst();
    handleChaosGemToggleAll();
  }

  private boolean isShiftPressed() {

    return Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) || Keyboard.isKeyDown(Keyboard.KEY_RSHIFT);
  }

  private void handleChaosGemToggleFirst() {

    boolean shifted = isShiftPressed();
    if (chaosGemToggleFirst.getIsKeyPressed() && !shifted) {
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

    boolean shiftToggle = chaosGemToggleFirst.getIsKeyPressed() && isShiftPressed();
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
