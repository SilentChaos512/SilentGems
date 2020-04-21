package net.silentchaos512.gems.item;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.network.play.server.SCooldownPacket;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import net.silentchaos512.gems.SilentGems;
import net.silentchaos512.gems.api.chaos.IChaosSource;
import net.silentchaos512.gems.chaos.ChaosSourceCapability;
import net.silentchaos512.gems.client.ClientPlayerInfo;
import net.silentchaos512.gems.init.GemsItemGroups;
import net.silentchaos512.utils.Lazy;

import javax.annotation.Nullable;
import java.util.List;

public class ChaosMeterItem extends Item {
    public static final Lazy<ChaosMeterItem> INSTANCE = Lazy.of(ChaosMeterItem::new);

    private static final String NBT_PLAYER_MODE = "ChaosMeter.PlayerMode";

    public ChaosMeterItem() {
        super(new Properties().group(GemsItemGroups.UTILITY).maxStackSize(1));
        addPropertyOverride(SilentGems.getId("chaos_level"), (stack, world, entity) -> getChaosLevel(stack));
    }

    private static int getChaosLevel(ItemStack stack) {
        // Pulls values from ClientPlayerInfo, since caps don't exist on the client
        return isPlayerMode(stack) ? ClientPlayerInfo.playerChaos : ClientPlayerInfo.worldChaos;
    }

    private static int getChaosLevelServer(ItemStack stack, ICapabilityProvider chaosSource) {
        LazyOptional<IChaosSource> optional = chaosSource.getCapability(ChaosSourceCapability.INSTANCE);
        if (optional.isPresent()) {
            return optional.orElseGet(ChaosSourceCapability::new).getChaos();
        }
        return -1;
    }

    private static boolean isPlayerMode(ItemStack stack) {
        return stack.getOrCreateTag().getBoolean(NBT_PLAYER_MODE);
    }

    private static void setPlayerMode(ItemStack stack, boolean playerMode) {
        stack.getOrCreateTag().putBoolean(NBT_PLAYER_MODE, playerMode);
    }

    private static ITextComponent formatValue(int value, boolean playerMode) {
        ITextComponent typeText = new TranslationTextComponent("misc.silentgems." + (playerMode ? "player" : "world"));
        String valueStr = String.format("%,d", value);
        return new TranslationTextComponent("item.silentgems.chaos_meter.valueFormat", typeText, valueStr);
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand handIn) {
        ItemStack heldItem = playerIn.getHeldItem(handIn);

        if (!worldIn.isRemote) {
            if (playerIn.isCrouching()) {
                boolean playerMode = isPlayerMode(heldItem);
                setPlayerMode(heldItem, !playerMode);
            }

            boolean playerMode = isPlayerMode(heldItem);
            int chaos = getChaosLevelServer(heldItem, playerMode ? playerIn : worldIn);
            if (chaos > -1) {
                playerIn.sendStatusMessage(formatValue(chaos, playerMode), true);
                playerIn.getCooldownTracker().setCooldown(this, 20);
            }
        }

        return new ActionResult<>(ActionResultType.PASS, heldItem);
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        boolean playerMode = isPlayerMode(stack);
        ITextComponent mode = new TranslationTextComponent("misc.silentgems." + (playerMode ? "player" : "world"));
        tooltip.add(new TranslationTextComponent("item.silentgems.chaos_meter.mode", mode));
    }
}
