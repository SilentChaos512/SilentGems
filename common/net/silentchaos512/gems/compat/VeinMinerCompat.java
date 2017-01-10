package net.silentchaos512.gems.compat;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.event.FMLInterModComms;
import net.silentchaos512.gems.SilentGems;
import net.silentchaos512.gems.lib.Names;

public class VeinMinerCompat {

  public static void init() {

    sendWhitelistMessage("item", "pickaxe", SilentGems.RESOURCE_PREFIX + Names.PICKAXE.toLowerCase());
    sendWhitelistMessage("item", "shovel", SilentGems.RESOURCE_PREFIX + Names.SHOVEL.toLowerCase());
    sendWhitelistMessage("item", "axe", SilentGems.RESOURCE_PREFIX + Names.AXE.toLowerCase());
  }

  private static void sendWhitelistMessage(String itemType, String toolType, String blockName) {

    NBTTagCompound message = new NBTTagCompound();
    message.setString("whitelistType", itemType);
    message.setString("toolType", toolType);
    message.setString("blockName", blockName);
    FMLInterModComms.sendMessage("VeinMiner", "whitelist", message);
  }
}
