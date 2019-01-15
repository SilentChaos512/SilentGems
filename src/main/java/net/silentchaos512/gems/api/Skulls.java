package net.silentchaos512.gems.api;

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

import java.util.*;
import java.util.function.Supplier;

public final class Skulls {
    static class SkullInfo {
        Supplier<ItemStack> stack;
        float dropRate;
    }

    private Skulls() {}

    private static final Map<Class<? extends EntityLivingBase>, SkullInfo> map = new HashMap<>();
    private static final List<SkullInfo> mobSkulls = new ArrayList<>();
    private static final List<SkullInfo> playerSkulls = new ArrayList<>();

    public static ItemStack getPlayerSkull(EntityPlayer player) {
        return getPlayerSkull(player.getName().getFormattedText());
    }

    public static ItemStack getPlayerSkull(String playerName) {
        ItemStack skull = new ItemStack(Items.PLAYER_HEAD);
        NBTTagCompound tags = skull.getOrCreateTag();
        tags.setString("SkullOwner", playerName);
        return skull;
    }

    public static ItemStack getSkull(EntityLivingBase entity) {
        return getSkull(entity.getClass());
    }

    public static ItemStack getSkull(Class<? extends EntityLivingBase> entityClass) {
        SkullInfo skullInfo = map.get(entityClass);
        if (skullInfo == null) return ItemStack.EMPTY;
        return skullInfo.stack.get();
    }

    /**
     * Select a random skull. Small chance of being a registered player.
     */
    public static ItemStack selectRandom(Random random) {
        return selectRandom(random, 0.25f);
    }

    public static ItemStack selectRandom(Random random, float playerChance) {
        if (random.nextFloat() < playerChance) {
            int index = random.nextInt(playerSkulls.size());
            return playerSkulls.get(index).stack.get();
        } else {
            int index = random.nextInt(mobSkulls.size());
            return mobSkulls.get(index).stack.get();
        }
    }

    public static float getDropRate(EntityLivingBase entity) {
        return getDropRate(entity.getClass());
    }

    public static float getDropRate(Class<? extends EntityLivingBase> entityClass) {
        SkullInfo skullInfo = map.get(entityClass);
        return skullInfo != null ? skullInfo.dropRate : 0;
    }

    public static void put(Class<? extends EntityLivingBase> entityClass, Supplier<ItemStack> skull, float dropRate) {
        SkullInfo info = new SkullInfo();
        info.stack = skull;
        info.dropRate = dropRate;
        map.put(entityClass, info);
        mobSkulls.add(info);
    }

    public static void putPlayer(String playerName) {
        SkullInfo info = new SkullInfo();
        info.stack = () -> getPlayerSkull(playerName);
        playerSkulls.add(info);
    }

    static {
        put(EntitySkeleton.class, () -> new ItemStack(Items.SKELETON_SKULL), 0.1f);
        put(EntityWitherSkeleton.class, () -> new ItemStack(Items.WITHER_SKELETON_SKULL), 0.1f);
        put(EntityZombie.class, () -> new ItemStack(Items.ZOMBIE_HEAD), 0.1f);
        put(EntityPlayer.class, () -> new ItemStack(Items.PLAYER_HEAD), 0.5f);
        put(EntityCreeper.class, () -> new ItemStack(Items.CREEPER_HEAD), 0.05f);

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
