package net.silentchaos512.gems.client.key;

import org.lwjgl.input.Keyboard;

import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent.KeyInputEvent;
import net.silentchaos512.gems.SilentGems;
import net.silentchaos512.gems.api.ITool;
import net.silentchaos512.gems.api.lib.EnumMaterialTier;
import net.silentchaos512.gems.network.NetworkHandler;
import net.silentchaos512.gems.network.message.MessageToggleSpecial;
import net.silentchaos512.gems.util.ToolHelper;

public class KeyTracker {

  public static KeyTracker INSTANCE = new KeyTracker();

  private EntityPlayer player;
  private KeyBinding toggleSpecial = createBinding("Toggle Special", Keyboard.KEY_C);

  private KeyBinding createBinding(String name, int key) {

    KeyBinding binding = new KeyBinding(name, key, SilentGems.MOD_ID);
    ClientRegistry.registerKeyBinding(binding);
    return binding;
  }

  @SubscribeEvent
  public void onKeyInput(KeyInputEvent event) {

    this.player = Minecraft.getMinecraft().thePlayer;
    handleToggleSpecial();
  }

  private boolean isShiftPressed() {

    return Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) || Keyboard.isKeyDown(Keyboard.KEY_RSHIFT);
  }

  private void handleToggleSpecial() {

    if (!toggleSpecial.isKeyDown()) {
      return;
    }

    ItemStack mainHand = player.getHeldItem(EnumHand.MAIN_HAND);

    if (mainHand != null && mainHand.getItem() instanceof ITool) {
      if (ToolHelper.getToolTier(mainHand) == EnumMaterialTier.SUPER) {
//        ToolHelper.toggleSpecialAbility(mainHand);
        NetworkHandler.INSTANCE.sendToServer(new MessageToggleSpecial());
      }
    }
  }
}
