package net.silentchaos512.gems.client.key;

import org.lwjgl.input.Keyboard;

import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraftforge.client.settings.IKeyConflictContext;
import net.minecraftforge.client.settings.KeyConflictContext;
import net.minecraftforge.client.settings.KeyModifier;
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
  private KeyBinding toggleSpecial = createBinding("Toggle Special", KeyConflictContext.IN_GAME,
      KeyModifier.SHIFT, Keyboard.KEY_C);

  /**
   * Creates and registers a KeyBinding.
   * 
   * @return The created KeyBinding.
   */
  private KeyBinding createBinding(String name, IKeyConflictContext keyConflictContext,
      KeyModifier keyModifier, int keyCode) {

    KeyBinding binding = new KeyBinding(name, keyConflictContext, keyModifier, keyCode,
        SilentGems.MOD_ID);
    ClientRegistry.registerKeyBinding(binding);
    return binding;
  }

  public static boolean isShiftDown() {

    return Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) || Keyboard.isKeyDown(Keyboard.KEY_RSHIFT);
  }

  public static boolean isControlDown() {

    return Keyboard.isKeyDown(Keyboard.KEY_LCONTROL) || Keyboard.isKeyDown(Keyboard.KEY_RCONTROL);
  }

  public static boolean isAltDown() {

    return Keyboard.isKeyDown(Keyboard.KEY_LMENU) || Keyboard.isKeyDown(Keyboard.KEY_RMENU);
  }

  @SubscribeEvent
  public void onKeyInput(KeyInputEvent event) {

    this.player = Minecraft.getMinecraft().thePlayer;
    handleToggleSpecial();
  }

  private void handleToggleSpecial() {

    if (!toggleSpecial.isKeyDown()) {
      return;
    }

    ItemStack mainHand = player.getHeldItem(EnumHand.MAIN_HAND);

    if (mainHand != null && mainHand.getItem() instanceof ITool) {
      if (ToolHelper.getToolTier(mainHand).ordinal() >= EnumMaterialTier.SUPER.ordinal()) {
        // ToolHelper.toggleSpecialAbility(mainHand);
        NetworkHandler.INSTANCE.sendToServer(new MessageToggleSpecial());
      }
    }
  }
}
