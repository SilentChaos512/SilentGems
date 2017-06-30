package net.silentchaos512.gems.item;

import java.util.List;

import org.lwjgl.input.Keyboard;

import baubles.api.BaubleType;
import baubles.api.IBauble;
import baubles.api.render.IRenderBauble;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms.TransformType;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.EnumAction;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.common.Optional;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.silentchaos512.gems.SilentGems;
import net.silentchaos512.gems.compat.BaublesCompat;
import net.silentchaos512.gems.config.GemsConfig;
import net.silentchaos512.gems.init.ModItems;
import net.silentchaos512.gems.lib.EnumGem;
import net.silentchaos512.gems.lib.Names;
import net.silentchaos512.gems.util.NBTHelper;
import net.silentchaos512.gems.util.TeleportUtil;
import net.silentchaos512.lib.registry.RecipeMaker;
import net.silentchaos512.lib.util.ChatHelper;
import net.silentchaos512.lib.util.DimensionalPosition;
import net.silentchaos512.lib.util.ItemHelper;
import net.silentchaos512.lib.util.LocalizationHelper;

@Optional.InterfaceList({
    @Optional.Interface(iface = "baubles.api.IBauble", modid = BaublesCompat.MOD_ID),
    @Optional.Interface(iface = "baubles.api.render.IRenderBauble", modid = BaublesCompat.MOD_ID) })
public class ItemReturnHome extends ItemChaosStorage implements IBauble, IRenderBauble {

  public static final String TEXT_BOUND_TO = "BoundTo";
  public static final String TEXT_NOT_BOUND = "NotBound";
  public static final String TEXT_NOT_ENOUGH_CHARGE = "NotEnoughCharge";
  public static final String TEXT_NOT_SANE = "NotSane";
  public static final String TEXT_NOT_SAFE = "NotSafe";

  public static final String NBT_READY = "IsReady";

  public ItemReturnHome() {

    super(EnumGem.values().length, Names.RETURN_HOME_CHARM, GemsConfig.RETURN_HOME_MAX_CHARGE);
  }

  @Override
  public void clAddInformation(ItemStack stack, World world, List list, boolean par4) {

    // Is ctrl key down?
    boolean modifier = Keyboard.isKeyDown(Keyboard.KEY_LCONTROL)
        || Keyboard.isKeyDown(Keyboard.KEY_RCONTROL);

    LocalizationHelper loc = SilentGems.instance.localizationHelper;

    // How to use
    list.addAll(loc.getItemDescriptionLines(itemName));

    // Display coordinates if modifier key is held.
    DimensionalPosition pos = getBoundPosition(stack);
    if (pos != null) {
      if (modifier) {
        list.add(loc.getItemSubText(itemName, TEXT_BOUND_TO, pos));
      } else {
        list.add(loc.getMiscText("PressCtrl"));
      }
    } else {
      list.add(loc.getItemSubText(itemName, TEXT_NOT_BOUND));
    }
  }

  @Override
  public void addRecipes(RecipeMaker recipes) {

    for (EnumGem gem : EnumGem.values()) {
      ItemStack result = new ItemStack(this, 1, gem.ordinal());
      recipes.addShapedOre("return_home_" + gem.name(), result, " s ", "sgs", "ici", 's',
          ModItems.craftingMaterial.gildedString, 'g', gem.getItemOreName(), 'i', "ingotGold", 'c',
          new ItemStack(ModItems.chaosOrb, 1, ItemChaosOrb.Type.FRAGILE.ordinal()));
    }
  }

  @Override
  protected void clGetSubItems(Item item, CreativeTabs tab, List<ItemStack> list) {

    if (!ItemHelper.isInCreativeTab(item, tab))
      return;

    for (EnumGem gem : EnumGem.values()) {
      ItemStack stack = new ItemStack(item, 1, gem.ordinal());
      setCharge(stack, getMaxCharge(stack));
      list.add(stack);
    }
  }

  @Override
  public EnumAction getItemUseAction(ItemStack stack) {

    return EnumAction.BOW;
  }

  @Override
  public int getMaxItemUseDuration(ItemStack stack) {

    return 133700;
  }

  @Override
  public boolean hasEffect(ItemStack stack) {

    return NBTHelper.getTagBoolean(stack, NBT_READY);
  }

  @Override
  public String getNameForStack(ItemStack stack) {

    return itemName;
  }

  public DimensionalPosition getBoundPosition(ItemStack stack) {

    if (!stack.hasTagCompound()) {
      return null;
    }

    DimensionalPosition pos = DimensionalPosition.readFromNBT(stack.getTagCompound());
    if (pos.equals(DimensionalPosition.ZERO)) {
      return null;
    }
    return pos;
  }

  @Override
  protected ActionResult<ItemStack> clOnItemRightClick(World world, EntityPlayer player,
      EnumHand hand) {

    ItemStack stack = player.getHeldItem(hand);
    DimensionalPosition pos = getBoundPosition(stack);
    if (pos != null) {
      player.setActiveHand(hand);
      return new ActionResult(EnumActionResult.SUCCESS, stack);
    } else {
      // PlayerHelper.addChatMessage(player,
      // SilentGems.instance.localizationHelper.getItemSubText(itemName, TEXT_NOT_BOUND));
      return new ActionResult(EnumActionResult.PASS, stack);
    }
  }

  @Override
  public void onUsingTick(ItemStack stack, EntityLivingBase player, int count) {

    if (player.world.isRemote) {
      int timeUsed = getMaxItemUseDuration(stack) - count;
      if (timeUsed >= GemsConfig.RETURN_HOME_USE_TIME) {
        NBTHelper.setTagBoolean(stack, NBT_READY, true);
      }
    }
  }

  @Override
  public void onUpdate(ItemStack stack, World world, Entity entity, int itemSlot,
      boolean isSelected) {

    if (!isSelected && NBTHelper.getTagBoolean(stack, NBT_READY)) {
      NBTHelper.setTagBoolean(stack, NBT_READY, false);
    }
  }

  @Override
  public void onPlayerStoppedUsing(ItemStack stack, World world, EntityLivingBase entity,
      int timeLeft) {

    if (!(entity instanceof EntityPlayer)) {
      return;
    }
    EntityPlayer player = (EntityPlayer) entity;

    if (world.isRemote) {
      NBTHelper.setTagBoolean(stack, NBT_READY, false);
    } else {
      int timeUsed = getMaxItemUseDuration(stack) - timeLeft;

      // Did player use item long enough?
      if (timeUsed < GemsConfig.RETURN_HOME_USE_TIME) {
        return;
      }

      tryTeleportPlayer(stack, player);
    }
  }

  public int getTeleportCost(ItemStack stack, EntityPlayer player) {

    // Currently a flat cost, but could be changed to consider distance.
    return player.capabilities.isCreativeMode ? 0 : GemsConfig.RETURN_HOME_USE_COST;
  }

  public void tryTeleportPlayer(ItemStack stack, EntityPlayer player) {

    LocalizationHelper loc = SilentGems.localizationHelper;
    DimensionalPosition pos = getBoundPosition(stack);

    // Not bound?
    if (pos == null) {
      return;
    }

    // Enough charge?
    if (getCharge(stack) < getTeleportCost(stack, player)) {
      ChatHelper.sendMessage(player, loc.getItemSubText(itemName, TEXT_NOT_ENOUGH_CHARGE));
      return;
    }

    // Is the destination sane? (ie, y > 0)
    if (pos.y <= 0) {
      ChatHelper.sendMessage(player, loc.getItemSubText(itemName, TEXT_NOT_SANE));
      return;
    }

    // Is the destination safe? (ie, no solid block at head level)
    WorldServer worldServer = player.getServer().getWorld(pos.dim);
    int height = (int) Math.ceil(player.eyeHeight);
    BlockPos target = pos.toBlockPos().up(height);

    // FIXME: Obstruction checks?
    // if (worldServer.isBlockNormalCube(target, true)) {
    // PlayerHelper.addChatMessage(player, loc.getItemSubText(itemName, TEXT_NOT_SAFE));
    // PlayerHelper.addChatMessage(player, "" + target);
    // PlayerHelper.addChatMessage(player, "" + worldServer.getBlockState(target));
    // SilentGems.logHelper.warning("Return Home Charm believes destination is obstructed:\n"
    // + "Target: " + target + "\nBlockstate: " + worldServer.getBlockState(target));
    // return;
    // }

    // It should be safe to teleport.
    // Reset fall distance then teleport.
    player.fallDistance = 0.0f;
    teleportPlayer(stack, player, pos);
    // Play sounds
    float soundPitch = 0.8f + 0.3f * SilentGems.random.nextFloat();
    for (BlockPos p : new BlockPos[] { player.getPosition(), pos.toBlockPos() }) {
      player.world.playSound(null, p, SoundEvents.ENTITY_ENDERMEN_TELEPORT, SoundCategory.PLAYERS,
          1.0f, soundPitch);
    }
  }

  protected void teleportPlayer(ItemStack stack, EntityPlayer player, DimensionalPosition pos) {

    if (player instanceof EntityPlayerMP) {
      TeleportUtil.teleportPlayerTo((EntityPlayerMP) player, pos);
    }

    extractCharge(stack, getTeleportCost(stack, player), false);
  }

  // ===================
  // = Baubles support =
  // ===================

  @Override
  @Optional.Method(modid = BaublesCompat.MOD_ID)
  public BaubleType getBaubleType(ItemStack stack) {

    return BaubleType.AMULET;
  }

  @SideOnly(Side.CLIENT)
  @Override
  @Optional.Method(modid = BaublesCompat.MOD_ID)
  public void onPlayerBaubleRender(ItemStack stack, EntityPlayer player, RenderType renderType,
      float partialTicks) {

    if (renderType == RenderType.BODY) {
      float scale = 0.5f;
      GlStateManager.scale(scale, scale, scale);
      IRenderBauble.Helper.rotateIfSneaking(player);
      GlStateManager.rotate(180, 0, 1, 0);
      IRenderBauble.Helper.translateToChest();
      GlStateManager.translate(0.0, 3.0, 0.55);
      Minecraft.getMinecraft().getRenderItem().renderItem(stack, TransformType.FIXED);
    }
  }
}
