package net.silentchaos512.gems.item;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.EnumAction;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.*;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionType;
import net.silentchaos512.gems.SilentGems;
import net.silentchaos512.gems.chaos.Chaos;
import net.silentchaos512.gems.client.key.KeyTracker;
import net.silentchaos512.gems.config.GemsConfig;
import net.silentchaos512.gems.init.ModItemGroups;
import net.silentchaos512.gems.lib.Gems;
import net.silentchaos512.gems.util.TeleportUtil;
import net.silentchaos512.lib.util.DimPos;
import net.silentchaos512.utils.Lazy;

import javax.annotation.Nullable;
import java.util.List;

//@Optional.InterfaceList({
//        @Optional.Interface(iface = "baubles.api.IBauble", modid = BaublesCompat.MOD_ID),
//        @Optional.Interface(iface = "baubles.api.render.IRenderBauble", modid = BaublesCompat.MOD_ID)})
public class ReturnHomeCharm extends Item /*implements IBauble, IRenderBauble*/ {
    public static final Lazy<ReturnHomeCharm> INSTANCE = Lazy.of(ReturnHomeCharm::new);

    private static final String NBT_GEM = "Gem";
    private static final String NBT_READY = "IsReady";

    public ReturnHomeCharm() {
        super(new Properties()
                .group(ModItemGroups.UTILITY)
                .maxStackSize(1)
        );
    }

    @Nullable
    public static Gems getGem(ItemStack stack) {
        return Gems.fromName(stack.getOrCreateTag().getString(NBT_GEM));
    }

    public static void setGem(ItemStack stack, Gems gem) {
        stack.getOrCreateTag().setString(NBT_GEM, gem.getName());
    }

    @Nullable
    public static DimPos getBoundPosition(ItemStack stack) {
        if (!stack.hasTag()) return null;
        DimPos pos = DimPos.read(stack.getOrCreateTag());
        if (pos.equals(DimPos.ZERO)) return null;
        return pos;
    }

    public int getTeleportCost(ItemStack stack, EntityPlayer player) {
        return Chaos.getChaosGeneratedByTeleport(player, getBoundPosition(stack));
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World world, List<ITextComponent> list, ITooltipFlag flag) {
        // Gem
        Gems gem = getGem(stack);
        if (gem != null) list.add(gem.getDisplayName());
        // How to use
        list.add(new TextComponentTranslation(this.getTranslationKey() + ".desc"));

        // Display coordinates if modifier key is held.
        DimPos pos = getBoundPosition(stack);
        if (pos != null) {
            if (KeyTracker.isControlDown()) {
                list.add(new TextComponentTranslation(this.getTranslationKey() + ".boundTo", pos));
            } else {
                list.add(new TextComponentTranslation("misc.silentgems.pressCtrl"));
            }
        } else {
            list.add(new TextComponentTranslation(this.getTranslationKey() + ".notBound"));
        }
    }

    @Override
    public void fillItemGroup(ItemGroup group, NonNullList<ItemStack> items) {
        if (!isInGroup(group)) return;

        for (Gems gem : Gems.values()) {
            ItemStack stack = new ItemStack(this);
            setGem(stack, gem);
            items.add(stack);
        }
    }

    @Override
    public EnumAction getUseAction(ItemStack stack) {
        return EnumAction.BOW;
    }

    @Override
    public int getUseDuration(ItemStack stack) {
        return 133700;
    }

    @Override
    public boolean hasEffect(ItemStack stack) {
        return stack.getOrCreateTag().getBoolean(NBT_READY);
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand) {
        ItemStack stack = player.getHeldItem(hand);
        DimPos pos = getBoundPosition(stack);
        if (pos != null) {
            player.setActiveHand(hand);
            return new ActionResult<>(EnumActionResult.SUCCESS, stack);
        } else {
            player.sendMessage(new TextComponentTranslation(this.getTranslationKey() + ".notBound"));
            return new ActionResult<>(EnumActionResult.PASS, stack);
        }
    }

    @Override
    public void onUsingTick(ItemStack stack, EntityLivingBase player, int count) {
        if (player.world.isRemote) {
            int timeUsed = getUseDuration(stack) - count;
            if (timeUsed >= GemsConfig.RETURN_HOME_USE_TIME) {
                stack.getOrCreateTag().setBoolean(NBT_READY, true);
            }
        }
    }

    @Override
    public void inventoryTick(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
        if (!isSelected && stack.getOrCreateTag().getBoolean(NBT_READY)) {
            stack.getOrCreateTag().setBoolean(NBT_READY, false);
        }
    }

    @Override
    public void onPlayerStoppedUsing(ItemStack stack, World world, EntityLivingBase entity, int timeLeft) {
        if (!(entity instanceof EntityPlayer)) {
            return;
        }
        EntityPlayer player = (EntityPlayer) entity;

        if (world.isRemote) {
            stack.getOrCreateTag().setBoolean(NBT_READY, false);
        } else {
            // Did player use item long enough?
            int timeUsed = getUseDuration(stack) - timeLeft;
            if (timeUsed >= GemsConfig.RETURN_HOME_USE_TIME) {
                tryTeleportPlayer(stack, player);
            }
        }
    }

    public void tryTeleportPlayer(ItemStack stack, EntityPlayer player) {
        DimPos pos = getBoundPosition(stack);
        if (pos == null || !(player instanceof EntityPlayerMP)) return;

        // Dimension valid?
        DimensionType dimensionType = DimensionType.getById(pos.getDimension());
        if (dimensionType == null) {
            player.sendMessage(new TextComponentTranslation("teleporter.silentgems.invalidDimension"));
            return;
        }

        // Is the destination sane? (ie, y > 0)
        if (pos.getY() <= 0) {
            player.sendMessage(new TextComponentTranslation("teleporter.silentgems.notSane"));
            return;
        }

        // Is the destination safe? (ie, no solid block at head level)
        if (!TeleportUtil.isDestinationSafe(player, pos)) {
            player.sendMessage(new TextComponentTranslation("teleporter.silentgems.notSafe"));
            return;
        }

        // It should be safe to teleport.
        // Reset fall distance then teleport.
        player.fallDistance = 0;
        float soundPitch = 0.8f + 0.3f * SilentGems.random.nextFloat();
        // Sound at source
        player.world.playSound(null, player.getPosition(), SoundEvents.ENTITY_ENDERMAN_TELEPORT, SoundCategory.PLAYERS, 1.0f, soundPitch);
        if (TeleportUtil.teleportPlayerTo((EntityPlayerMP) player, pos)) {
            Chaos.generate(player, getTeleportCost(stack, player));
        }
        // Sound at destination
        player.world.playSound(null, pos.getPos(), SoundEvents.ENTITY_ENDERMAN_TELEPORT, SoundCategory.PLAYERS, 1.0f, soundPitch);
    }

    // ===================
    // = Baubles support =
    // ===================

//    @Override
//    @Optional.Method(modid = BaublesCompat.MOD_ID)
//    public BaubleType getBaubleType(ItemStack stack) {
//        return BaubleType.AMULET;
//    }
//
//    @SideOnly(Side.CLIENT)
//    @Override
//    @Optional.Method(modid = BaublesCompat.MOD_ID)
//    public void onPlayerBaubleRender(ItemStack stack, EntityPlayer player, RenderType renderType, float partialTicks) {
//        if (renderType == RenderType.BODY) {
//            float scale = 0.5f;
//            GlStateManager.scale(scale, scale, scale);
//            IRenderBauble.Helper.rotateIfSneaking(player);
//            GlStateManager.rotate(180, 0, 1, 0);
//            IRenderBauble.Helper.translateToChest();
//            GlStateManager.translate(0.0, 3.0, 0.55);
//            Minecraft.getMinecraft().getRenderItem().renderItem(stack, TransformType.FIXED);
//        }
//    }
}
