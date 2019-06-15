package net.silentchaos512.gems.item;

import net.minecraft.client.Minecraft;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Rarity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.silentchaos512.gems.api.IPedestalItem;
import net.silentchaos512.gems.api.chaos.ChaosEmissionRate;
import net.silentchaos512.gems.chaos.Chaos;
import net.silentchaos512.gems.init.ModItemGroups;
import net.silentchaos512.gems.lib.Gems;
import net.silentchaos512.gems.lib.IGem;
import net.silentchaos512.gems.lib.chaosbuff.ChaosBuffManager;
import net.silentchaos512.gems.lib.chaosbuff.IChaosBuff;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class ChaosGemItem extends Item implements IGem, IPedestalItem {
    private static final String NBT_ENABLED = "Enabled";
    private static final String NBT_BUFF_LIST = "Buffs";
    private static final String NBT_BUFF_KEY = "ID";
    private static final String NBT_BUFF_LEVEL = "Level";

    private static final int MAX_SLOTS = 20;
    private static final int PEDESTAL_RANGE = 8;

    private final Gems gem;

    public ChaosGemItem(Gems gem) {
        super(new Properties()
                .group(ModItemGroups.UTILITY)
                .maxStackSize(1)
                .rarity(Rarity.RARE)
        );
        this.gem = gem;
    }

    @Override
    public Gems getGem() {
        return this.gem;
    }

    //region Buffs and chaos

    public static Map<IChaosBuff, Integer> getBuffs(ItemStack stack) {
        if (stack.isEmpty() || !stack.hasTag() || !stack.getOrCreateTag().contains(NBT_BUFF_LIST)) {
            return new HashMap<>();
        }

        ListNBT tagList = stack.getOrCreateTag().getList(NBT_BUFF_LIST, 10);
        Map<IChaosBuff, Integer> map = new LinkedHashMap<>();

        for (INBT nbt : tagList) {
            if (nbt instanceof CompoundNBT) {
                CompoundNBT tag = (CompoundNBT) nbt;
                String key = tag.getString(NBT_BUFF_KEY);
                int level = tag.getInt(NBT_BUFF_LEVEL);
                IChaosBuff buff = ChaosBuffManager.get(key);
                if (buff != null) {
                    map.put(buff, level);
                }
            }
        }

        return map;
    }

    public static boolean addBuff(ItemStack stack, IChaosBuff buff) {
        if (canAddBuff(stack, buff)) {
            Map<IChaosBuff, Integer> buffMap = getBuffs(stack);
            int currentLevel = 0;
            if (buffMap.containsKey(buff)) {
                currentLevel = buffMap.get(buff);
            }
            buffMap.put(buff, currentLevel + 1);
            setBuffs(stack, buffMap);

            return true;
        }

        return false;
    }

    public static void setBuffs(ItemStack stack, Map<IChaosBuff, Integer> buffs) {
        if (stack.isEmpty()) return;

        CompoundNBT tag = stack.getOrCreateTag();
        if (tag.contains(NBT_BUFF_LIST)) {
            tag.remove(NBT_BUFF_LIST);
        }

        ListNBT tagList = new ListNBT();

        for (Map.Entry<IChaosBuff, Integer> entry : buffs.entrySet()) {
            IChaosBuff buff = entry.getKey();
            int level = entry.getValue();
            CompoundNBT compound = new CompoundNBT();
            compound.putString(NBT_BUFF_KEY, buff.getId().toString());
            compound.putShort(NBT_BUFF_LEVEL, (short) level);
            tagList.add(compound);
        }

        tag.put(NBT_BUFF_LIST, tagList);
    }

    public static boolean canAddBuff(ItemStack stack, IChaosBuff buff) {
        if (stack.isEmpty()) return false;
        if (!stack.hasTag()) return true;

        Map<IChaosBuff, Integer> buffMap = getBuffs(stack);

        if (buffMap.containsKey(buff)) {
            // We already have this buff, but might be able to increment
            int currentLevel = buffMap.get(buff);
            if (currentLevel >= buff.getMaxLevel()) {
                return false;
            }
            buffMap.put(buff, currentLevel + 1);
        } else {
            // We don't have the buff
            buffMap.put(buff, 1);
        }

        // Not exceeding max slots?
        return getSlotsUsed(buffMap) <= MAX_SLOTS;
    }

    public static int getSlotsUsed(ItemStack stack) {
        return getSlotsUsed(getBuffs(stack));
    }

    public static int getSlotsUsed(Map<IChaosBuff, Integer> buffMap) {
        return buffMap.entrySet().stream()
                .mapToInt(entry -> entry.getKey().getSlotsForLevel(entry.getValue()))
                .sum();
    }

    public static int getChaosGenerated(ItemStack stack, PlayerEntity player) {
        return getBuffs(stack).entrySet().stream()
                .mapToInt(entry -> entry.getKey().getChaosGenerated(player, entry.getValue()))
                .sum();
    }

    public static int getMaxChaosGenerated(ItemStack stack) {
        return getBuffs(stack).entrySet().stream()
                .mapToInt(entry -> entry.getKey().getActiveChaosGenerated(entry.getValue()))
                .sum();
    }

    public static boolean isEnabled(ItemStack stack) {
        return !stack.isEmpty() && stack.getOrCreateTag().getBoolean(NBT_ENABLED);
    }

    public static void setEnabled(ItemStack stack, boolean value) {
        if (stack.isEmpty()) return;
        stack.getOrCreateTag().putBoolean(NBT_ENABLED, value);
    }

    public static int getBuffLevel(ItemStack stack, IChaosBuff buff) {
        Map<IChaosBuff, Integer> buffMap = getBuffs(stack);
        return buffMap.getOrDefault(buff, 0);
    }

    public static void applyEffects(ItemStack stack, PlayerEntity player) {
        getBuffs(stack).forEach((buff, level) -> buff.applyTo(player, level));
    }

    public static void removeEffects(ItemStack stack, PlayerEntity player) {
        getBuffs(stack).keySet().forEach(buff -> buff.removeFrom(player));
    }

    //endregion

    //region IPedestalItem

    @Override
    public boolean pedestalPowerChange(ItemStack stack, World world, BlockPos pos, boolean powered) {
        setEnabled(stack, powered);
        return true;
    }

    @Override
    public void pedestalTick(ItemStack stack, World world, BlockPos pos) {
        if (world.isRemote || !isEnabled(stack)) return;

        int totalChaos = 0;
        AxisAlignedBB boundingBox = new AxisAlignedBB(pos).grow(PEDESTAL_RANGE);
        List<PlayerEntity> players = world.getEntitiesWithinAABB(PlayerEntity.class, boundingBox);
        for (PlayerEntity player : players) {
            applyEffects(stack, player);
            totalChaos += getChaosGenerated(stack, player);
        }

        // Generate chaos, but scale back with more players
        float divisor = 1 + 0.25f * (players.size() - 1);
        int discountedChaos = (int) (totalChaos / divisor);
        Chaos.generate(world, discountedChaos, pos);
    }

    //endregion

    //region Item overrides

    @Override
    public void inventoryTick(ItemStack stack, World world, Entity entity, int itemSlot, boolean isSelected) {
        if (world.isRemote || !(entity instanceof PlayerEntity)) return;

        // Apply effects?
        if (isEnabled(stack)) {
            PlayerEntity player = (PlayerEntity) entity;
            applyEffects(stack, player);
            Chaos.generate(player, getChaosGenerated(stack, player), true);
        }
    }

    @Override
    public boolean onDroppedByPlayer(ItemStack stack, PlayerEntity player) {
        setEnabled(stack, false);
        removeEffects(stack, player);
        return true;
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World world, PlayerEntity player, Hand hand) {
        ItemStack stack = player.getHeldItem(hand);

        if (!stack.isEmpty()) {
            setEnabled(stack, !isEnabled(stack));

            if (isEnabled(stack)) {
                applyEffects(stack, player);
            } else {
                removeEffects(stack, player);
            }
        }

        return new ActionResult<>(ActionResultType.SUCCESS, stack);
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        getBuffs(stack).forEach((buff, level) -> tooltip.add(buff.getDisplayName(level)));
        tooltip.add(new TranslationTextComponent("item.silentgems.chaos_gem.slots", getSlotsUsed(stack), MAX_SLOTS));
        tooltip.add(chaosGenTooltip("item.silentgems.chaos_gem.chaos", getChaosGenerated(stack, Minecraft.getInstance().player)));
        tooltip.add(chaosGenTooltip("item.silentgems.chaos_gem.chaosMax", getMaxChaosGenerated(stack)));
    }

    private static ITextComponent chaosGenTooltip(String key, int chaos) {
        ChaosEmissionRate emissionRate = ChaosEmissionRate.fromAmount(chaos);
        return new TranslationTextComponent(key, emissionRate.getDisplayName(chaos));
    }

    @Override
    public ITextComponent getDisplayName(ItemStack stack) {
        return getName();
    }

    @Override
    public ITextComponent getName() {
        return new TranslationTextComponent("item.silentgems.chaos_gem", this.gem.getDisplayName());
    }

    @Override
    public boolean hasEffect(ItemStack stack) {
        return isEnabled(stack);
    }

    @Override
    public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged) {
        boolean oldEnabled = isEnabled(oldStack);
        boolean newEnabled = isEnabled(newStack);
        return slotChanged || oldEnabled != newEnabled || !oldStack.isItemEqual(newStack);
    }

    //endregion
}
