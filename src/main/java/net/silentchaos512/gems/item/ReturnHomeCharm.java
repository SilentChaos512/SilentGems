package net.silentchaos512.gems.item;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;
import net.silentchaos512.gems.lib.Gems;
import net.silentchaos512.gems.init.ModItemGroups;

import javax.annotation.Nullable;
import java.util.List;

//@Optional.InterfaceList({
//        @Optional.Interface(iface = "baubles.api.IBauble", modid = BaublesCompat.MOD_ID),
//        @Optional.Interface(iface = "baubles.api.render.IRenderBauble", modid = BaublesCompat.MOD_ID)})
public class ReturnHomeCharm extends Item /*ItemChaosStorage implements IAddRecipes, ICustomModel, IBauble, IRenderBauble*/ {
    private static final String TEXT_BOUND_TO = "boundTo";
    private static final String TEXT_NOT_BOUND = "notBound";
    private static final String TEXT_NOT_ENOUGH_CHARGE = "notEnoughCharge";
    private static final String TEXT_NOT_SANE = "notSane";
    private static final String TEXT_NOT_SAFE = "notSafe";

    private static final String NBT_READY = "IsReady";

    public ReturnHomeCharm() {
        super(new Properties()
                .group(ModItemGroups.UTILITY));
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World world, List<ITextComponent> list, ITooltipFlag flag) {
//        // Is ctrl key down?
//        boolean modifier = Keyboard.isKeyDown(Keyboard.KEY_LCONTROL) || Keyboard.isKeyDown(Keyboard.KEY_RCONTROL);
//
//        // How to use
//        list.add(TextFormatting.ITALIC + SilentGems.i18n.subText(this, "desc"));
//
//        // Display coordinates if modifier key is held.
//        DimensionalPosition pos = getBoundPosition(stack);
//        if (pos != null) {
//            if (modifier) {
//                list.add(SilentGems.i18n.subText(this, TEXT_BOUND_TO, pos));
//            } else {
//                list.add(SilentGems.i18n.miscText("pressCtrl"));
//            }
//        } else {
//            list.add(SilentGems.i18n.subText(this, TEXT_NOT_BOUND));
//        }
    }

    @Override
    public void fillItemGroup(ItemGroup group, NonNullList<ItemStack> items) {
        if (!isInGroup(group)) return;

        for (Gems gem : Gems.values()) {
            ItemStack stack = new ItemStack(this);
            // TODO: how to set gem?
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
//        return NBTHelper.getTagBoolean(stack, NBT_READY);
        return false;
    }

//    @Nullable
//    public DimensionalPosition getBoundPosition(ItemStack stack) {
//        if (!stack.hasTagCompound()) {
//            return null;
//        }
//
//        DimensionalPosition pos = DimensionalPosition.readFromNBT(stack.getTagCompound());
//        if (pos.equals(DimensionalPosition.ZERO)) {
//            return null;
//        }
//        return pos;
//    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand) {
        ItemStack stack = player.getHeldItem(hand);
//        DimensionalPosition pos = getBoundPosition(stack);
//        if (pos != null) {
//            player.setActiveHand(hand);
//            return new ActionResult<>(EnumActionResult.SUCCESS, stack);
//        } else {
//            // PlayerHelper.addChatMessage(player,
//            // SilentGems.instance.localizationHelper.getItemSubText(itemName, TEXT_NOT_BOUND));
            return new ActionResult<>(EnumActionResult.PASS, stack);
//        }
    }

    @Override
    public void onUsingTick(ItemStack stack, EntityLivingBase player, int count) {
//        if (player.world.isRemote) {
//            int timeUsed = getMaxItemUseDuration(stack) - count;
//            if (timeUsed >= GemsConfig.RETURN_HOME_USE_TIME) {
//                NBTHelper.setTagBoolean(stack, NBT_READY, true);
//            }
//        }
    }

    @Override
    public void inventoryTick(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
//        if (!isSelected && NBTHelper.getTagBoolean(stack, NBT_READY)) {
//            NBTHelper.setTagBoolean(stack, NBT_READY, false);
//        }
    }

    @Override
    public void onPlayerStoppedUsing(ItemStack stack, World world, EntityLivingBase entity, int timeLeft) {
        if (!(entity instanceof EntityPlayer)) {
            return;
        }
        EntityPlayer player = (EntityPlayer) entity;

//        if (world.isRemote) {
//            NBTHelper.setTagBoolean(stack, NBT_READY, false);
//        } else {
//            int timeUsed = getMaxItemUseDuration(stack) - timeLeft;
//
//            // Did player use item long enough?
//            if (timeUsed < GemsConfig.RETURN_HOME_USE_TIME) {
//                return;
//            }
//
//            tryTeleportPlayer(stack, player);
//        }
    }

    public int getTeleportCost(ItemStack stack, EntityPlayer player) {
        // Currently a flat cost, but could be changed to consider distance.
//        return player.capabilities.isCreativeMode ? 0 : GemsConfig.RETURN_HOME_USE_COST;
        return 0;
    }

    public void tryTeleportPlayer(ItemStack stack, EntityPlayer player) {
//        DimensionalPosition pos = getBoundPosition(stack);
//
//        // Not bound?
//        if (pos == null) {
//            return;
//        }
//
//        // Enough charge?
//        if (getCharge(stack) < getTeleportCost(stack, player)) {
//            ChatHelper.sendMessage(player, SilentGems.i18n.subText(this, TEXT_NOT_ENOUGH_CHARGE));
//            return;
//        }
//
//        // Is the destination sane? (ie, y > 0)
//        if (pos.y <= 0) {
//            ChatHelper.sendMessage(player, SilentGems.i18n.subText(this, TEXT_NOT_SANE));
//            return;
//        }
//
//        // Is the destination safe? (ie, no solid block at head level)
//        WorldServer worldServer = player.getServer().getWorld(pos.dim);
//        int height = (int) Math.ceil(player.eyeHeight);
//        BlockPos target = pos.toBlockPos().up(height);
//
//        // It should be safe to teleport.
//        // Reset fall distance then teleport.
//        player.fallDistance = 0.0f;
//        teleportPlayer(stack, player, pos);
//        // Play sounds
//        float soundPitch = 0.8f + 0.3f * SilentGems.random.nextFloat();
//        for (BlockPos p : new BlockPos[]{player.getPosition(), pos.toBlockPos()}) {
//            player.world.playSound(null, p, SoundEvents.ENTITY_ENDERMEN_TELEPORT, SoundCategory.PLAYERS,
//                    1.0f, soundPitch);
//        }
    }

//    private void teleportPlayer(ItemStack stack, EntityPlayer player, DimensionalPosition pos) {
//        if (player instanceof EntityPlayerMP) {
//            TeleportUtil.teleportPlayerTo((EntityPlayerMP) player, pos);
//        }
//        extractCharge(stack, getTeleportCost(stack, player), false);
//    }

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
