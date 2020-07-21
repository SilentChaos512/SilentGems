package net.silentchaos512.gems.api;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.monster.CreeperEntity;
import net.minecraft.entity.monster.SkeletonEntity;
import net.minecraft.entity.monster.WitherSkeletonEntity;
import net.minecraft.entity.monster.ZombieEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.silentchaos512.gems.lib.Names;

import java.util.*;
import java.util.function.Supplier;

public final class Skulls {
    static class SkullInfo {
        Supplier<ItemStack> stack;
        float dropRate;
    }

    private Skulls() {}

    private static final Map<Class<? extends LivingEntity>, SkullInfo> map = new HashMap<>();
    private static final List<SkullInfo> mobSkulls = new ArrayList<>();
    private static final List<SkullInfo> playerSkulls = new ArrayList<>();

    public static ItemStack getPlayerSkull(PlayerEntity player) {
        return getPlayerSkull(player.getName().getString());
    }

    public static ItemStack getPlayerSkull(String playerName) {
        ItemStack skull = new ItemStack(Items.PLAYER_HEAD);
        CompoundNBT tags = skull.getOrCreateTag();
        tags.putString("SkullOwner", playerName);
        return skull;
    }

    public static ItemStack getSkull(LivingEntity entity) {
        return getSkull(entity.getClass());
    }

    public static ItemStack getSkull(Class<? extends LivingEntity> entityClass) {
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

    public static float getDropRate(LivingEntity entity) {
        return getDropRate(entity.getClass());
    }

    public static float getDropRate(Class<? extends LivingEntity> entityClass) {
        SkullInfo skullInfo = map.get(entityClass);
        return skullInfo != null ? skullInfo.dropRate : 0;
    }

    public static void put(Class<? extends LivingEntity> entityClass, Supplier<ItemStack> skull, float dropRate) {
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
        put(SkeletonEntity.class, () -> new ItemStack(Items.SKELETON_SKULL), 0.1f);
        put(WitherSkeletonEntity.class, () -> new ItemStack(Items.WITHER_SKELETON_SKULL), 0.1f);
        put(ZombieEntity.class, () -> new ItemStack(Items.ZOMBIE_HEAD), 0.1f);
        put(PlayerEntity.class, () -> new ItemStack(Items.PLAYER_HEAD), 0.5f);
        put(CreeperEntity.class, () -> new ItemStack(Items.CREEPER_HEAD), 0.05f);

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
