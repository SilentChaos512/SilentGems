package net.silentchaos512.gems.api;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.entity.monster.EntityWitherSkeleton;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.silentchaos512.gems.lib.Names;
import net.silentchaos512.lib.util.StackHelper;

public class Skulls {

  static class SkullInfo {

    float dropRate;
    ItemStack stack;
  }

  static Map<Class<? extends EntityLivingBase>, SkullInfo> map = new HashMap<>();
  static List<ItemStack> mobSkulls = new ArrayList<>();
  static List<ItemStack> playerSkulls = new ArrayList<>();

  public static ItemStack getPlayerSkull(EntityPlayer player) {

    return getPlayerSkull(player.getName());
  }

  public static ItemStack getPlayerSkull(String playerName) {

    // TODO: Need to test this somehow...
    ItemStack skull = new ItemStack(Items.SKULL, 1, 3);
    skull.setTagCompound(new NBTTagCompound());
    skull.getTagCompound().setString("SkullOwner", playerName);
    return skull;
  }

  public static ItemStack getSkull(EntityLivingBase entity) {

    return getSkull(entity.getClass());
  }

  public static ItemStack getSkull(Class<? extends EntityLivingBase> entityClass) {

    SkullInfo skullInfo = map.get(entityClass);
    if (skullInfo == null) {
      return StackHelper.empty();
    }
    ItemStack stack = skullInfo.stack;
    if (stack == null) {
      return StackHelper.empty();
    }
    return StackHelper.safeCopy(stack);
  }

  /**
   * Select a random skull. Small chance of being a registered player.
   * @param random
   * @return
   */
  public static ItemStack selectRandom(Random random) {

    if (random.nextFloat() < 0.25f)
      return playerSkulls.get(random.nextInt(playerSkulls.size()));
    else
      return mobSkulls.get(random.nextInt(mobSkulls.size())); 
  }

  public static float getDropRate(EntityLivingBase entity) {

    return getDropRate(entity.getClass());
  }

  public static float getDropRate(Class<? extends EntityLivingBase> entityClass) {

    return map.get(entityClass).dropRate;
  }

  public static void put(Class<? extends EntityLivingBase> entityClass, ItemStack skull,
      float dropRate) {

    SkullInfo info = new SkullInfo();
    info.stack = skull;
    info.dropRate = dropRate;
    map.put(entityClass, info);
    mobSkulls.add(skull);
  }

  public static void putPlayer(String playerName) {

    playerSkulls.add(getPlayerSkull(playerName));
  }

  static boolean initialized = false;

  public static void init() {

    if (initialized) {
      return;
    }
    initialized = true;

    put(EntitySkeleton.class, new ItemStack(Items.SKULL, 1, 0), 0.1f);
    put(EntityWitherSkeleton.class, new ItemStack(Items.SKULL, 1, 1), 0.1f);
    put(EntityZombie.class, new ItemStack(Items.SKULL, 1, 2), 0.1f);
    put(EntityPlayer.class, new ItemStack(Items.SKULL, 1, 3), 0.5f);
    put(EntityCreeper.class, new ItemStack(Items.SKULL, 1, 4), 0.05f);

    putPlayer(Names.SILENT_CHAOS_512);
    putPlayer(Names.CHAOTIC_PLAYZ);
    putPlayer(Names.M4THG33K);
    putPlayer("notch");
    putPlayer("MHF_Alex");
    putPlayer("MHF_Blaze");
    putPlayer("MHF_CaveSpider");
    putPlayer("MHF_Chicken");
    putPlayer("MHF_Cow");
    putPlayer("MHF_Enderman");
    putPlayer("MHF_Ghast");
    putPlayer("MHF_Golem");
    putPlayer("MHF_Herobrine");
    putPlayer("MHF_LavaSlime");
    putPlayer("MHF_MushroomCow");
    putPlayer("MHF_Ocelot");
    putPlayer("MHF_Pig");
    putPlayer("MHF_PigZombie");
    putPlayer("MHF_Sheep");
    putPlayer("MHF_Slime");
    putPlayer("MHF_Spider");
    putPlayer("MHF_Squid");
    putPlayer("MHF_Steve");
    putPlayer("MHF_Villager");
  }
}
