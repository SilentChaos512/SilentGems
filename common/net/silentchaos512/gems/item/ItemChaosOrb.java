package net.silentchaos512.gems.item;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.NotImplementedException;
import org.lwjgl.input.Keyboard;

import com.google.common.collect.Lists;

import baubles.api.BaubleType;
import baubles.api.BaublesApi;
import baubles.api.IBauble;
import baubles.api.cap.IBaublesItemHandler;
import baubles.api.render.IRenderBauble;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.ItemModelMesher;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms.TransformType;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.Optional;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.silentchaos512.gems.SilentGems;
import net.silentchaos512.gems.api.energy.IChaosStorage;
import net.silentchaos512.gems.compat.BaublesCompat;
import net.silentchaos512.gems.handler.PlayerDataHandler;
import net.silentchaos512.gems.handler.PlayerDataHandler.PlayerData;
import net.silentchaos512.gems.init.ModItems;
import net.silentchaos512.gems.lib.Names;
import net.silentchaos512.gems.util.ChaosUtil;
import net.silentchaos512.gems.util.NBTHelper;
import net.silentchaos512.lib.registry.RecipeMaker;
import net.silentchaos512.lib.util.ChatHelper;
import net.silentchaos512.lib.util.LocalizationHelper;
import net.silentchaos512.lib.util.PlayerHelper;
import net.silentchaos512.lib.util.StackHelper;

@Optional.InterfaceList({
    @Optional.Interface(iface = "baubles.api.IBauble", modid = BaublesCompat.MOD_ID),
    @Optional.Interface(iface = "baubles.api.render.IRenderBauble", modid = BaublesCompat.MOD_ID) })
public class ItemChaosOrb extends ItemChaosStorage implements IBauble, IRenderBauble {

  public static enum Type {

    //@formatter:off
    POTATO(     5000, 0.01f, 1),
    FRAGILE(  100000, 0.20f, 3),
    REFINED( 1000000, 0.05f, 5),
    SUPREME(10000000, 0.00f, 1);
    //@formatter:on

    public final int maxCharge;
    public final float breakChance;
    public final int crackStages;

    Type(int maxCharge, float breakChance, int crackStages) {

      this.maxCharge = maxCharge;
      this.breakChance = breakChance;
      this.crackStages = crackStages;
    }
  }

  public static final String NBT_CHARGE = "ChaosCharge";
  public static final String NBT_ITEM_SEND = "ItemSend";

  public static final int MAX_ITEM_SEND = 2000;

  public ItemChaosOrb() {

    super(Type.values().length, Names.CHAOS_ORB, 0);
  }

  @Override
  public void clAddInformation(ItemStack stack, World world, List list, boolean advanced) {

    final boolean shifted = Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)
        || Keyboard.isKeyDown(Keyboard.KEY_LSHIFT);

    LocalizationHelper loc = SilentGems.instance.localizationHelper;

    list.add(loc.getMiscText("ChaosCharge", getCharge(stack), getMaxCharge(stack)));
    list.add(loc.getItemSubText(itemName, "breakChance", (int) (getType(stack).breakChance * 100)));

    boolean mode = isItemSendEnabled(stack);
    String onOrOff = loc.getMiscText("state." + (mode ? "on" : "off"));
    onOrOff = (mode ? TextFormatting.GREEN : TextFormatting.RED) + onOrOff;
    list.add(loc.getItemSubText(Names.CHAOS_ORB, "itemSend", onOrOff));

    if (shifted) {
      for (String str : loc.getItemDescriptionLines(itemName)) {
        list.add(TextFormatting.ITALIC + str);
      }
    }
  }

  @Override
  public void addRecipes(RecipeMaker recipes) {

    String chaosEssence = "gemChaos";
    ItemStack chaosEssenceEnriched = ModItems.craftingMaterial.chaosEssenceEnriched;
    ItemStack chaosEssenceCrystallized = ModItems.craftingMaterial.chaosEssenceCrystallized;
    String chaosEssenceShard = "nuggetChaos";

    for (Type type : Type.values()) {
      ItemStack result = new ItemStack(this, 1, type.ordinal());

      switch (type) {
        case FRAGILE:
          recipes.addShapedOre("chaos_orb_fragile", result, "ccc", "cdc", "ccc", 'c', chaosEssence,
              'd', "gemDiamond");
          break;
        case POTATO:
          recipes.addShapedOre("chaos_orb_potato", result, "ccc", "cpc", "ccc", 'c',
              chaosEssenceShard, 'p', Items.POTATO);
          break;
        case REFINED:
          recipes.addShaped("chaos_orb_refined", result, " c ", "coc", " c ", 'c',
              chaosEssenceEnriched, 'o', new ItemStack(this, 1, Type.FRAGILE.ordinal()));
          break;
        case SUPREME:
          recipes.addShaped("chaos_orb_supreme", result, " c ", "coc", " c ", 'c',
              chaosEssenceCrystallized, 'o', new ItemStack(this, 1, Type.REFINED.ordinal()));
          break;
        default:
          throw new NotImplementedException("No recipe for chaos orb of type " + type);
      }
    }
  }

  @Override
  public void getModels(Map<Integer, ModelResourceLocation> models) {

    for (Type orbType : Type.values()) {
      String name = (getFullName() + orbType.ordinal()).toLowerCase();
      models.put(orbType.ordinal(), new ModelResourceLocation(name, "inventory"));
      for (int i = 1; i < orbType.crackStages; ++i) {
        int meta = orbType.ordinal() + (i << 4);
        models.put(meta, new ModelResourceLocation(name + "_" + i, "inventory"));
      }
    }
  }

//  @Override
//  public boolean registerModels() {
//
//    for (Type orbType : Type.values()) {
//      String name = getFullName().toLowerCase() + orbType.ordinal();
//      ModelResourceLocation model = new ModelResourceLocation(name, "inventory");
//      ModelLoader.setCustomModelResourceLocation(this, orbType.ordinal(), model);
//      for (int i = 1; i < orbType.crackStages; ++i) {
//        int meta = orbType.ordinal() + (i << 4);
//        model = new ModelResourceLocation(name + "_" + i, "inventory");
//        ModelLoader.setCustomModelResourceLocation(this, meta, model);
//      }
//    }
//
//    return true;
//  }

  @Override
  protected void clGetSubItems(Item item, CreativeTabs tab, List<ItemStack> list) {

    if (!isInCreativeTab(tab))
      return;

    for (Type type : Type.values()) {
      ItemStack empty = new ItemStack(item, 1, type.ordinal());
      ItemStack half = empty.copy();
      ItemStack full = empty.copy();

      NBTHelper.setTagInt(half, NBT_CHARGE, getMaxCharge(half) / 2);
      NBTHelper.setTagInt(full, NBT_CHARGE, this.getMaxCharge(full));

      list.add(empty);
      list.add(half);
      list.add(full);
    }
  }

  @Override
  public String getNameForStack(ItemStack stack) {

    return itemName + (stack.getItemDamage() & 0x0F);
  }

  @Override
  protected ActionResult<ItemStack> clOnItemRightClick(World world, EntityPlayer player,
      EnumHand hand) {

    ItemStack stack = player.getHeldItem(hand);
    if (player.isSneaking()) {
      toggleItemSendEnabled(stack);
      boolean mode = isItemSendEnabled(stack);

      LocalizationHelper loc = SilentGems.localizationHelper;
      String onOrOff = loc.getMiscText("state." + (mode ? "on" : "off"));
      onOrOff = (mode ? TextFormatting.GREEN : TextFormatting.RED) + onOrOff;
      String line = loc.getItemSubText(Names.CHAOS_ORB, "itemSend", onOrOff);
      ChatHelper.sendStatusMessage(player, new TextComponentString(line), true);
    }
    return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, stack);
  }

  @Override
  public void onUpdate(ItemStack stack, World worldIn, Entity entityIn, int itemSlot,
      boolean isSelected) {

    if (worldIn.isRemote || worldIn.getTotalWorldTime() % 20 != 0) {
      return;
    }

    if (entityIn instanceof EntityPlayer) {
      EntityPlayer player = (EntityPlayer) entityIn;
      PlayerData data = PlayerDataHandler.get(player);

      // Send Chaos to player
      int amount = extractCharge(stack, data.getChaosChargeSpeed(), true);
      amount = data.sendChaos(amount);
      extractCharge(stack, amount, false);

      // Damage from player send?
      int breakTries = amount / PlayerData.CHAOS_MAX_TRANSFER;
      for (int i = 0; i < breakTries; ++i) {
        if (amount > 0 && SilentGems.random.nextFloat() < getBreakChance(stack)) {
          damageOrb(stack, player);
          break;
        }
      }

      // Try recharge player's items?
      if (isItemSendEnabled(stack)) {
        int totalSentToItems = 0;
        for (ItemStack itemstack : ChaosUtil.getChaosStorageItems(player)) {
          if (itemstack.getItem() != this && itemstack.getItem() instanceof IChaosStorage) {
            int toSend = Math.min(getCharge(stack), MAX_ITEM_SEND);
            totalSentToItems += ((IChaosStorage) itemstack.getItem()).receiveCharge(itemstack,
                toSend, false);
          }
        }
        extractCharge(stack, totalSentToItems, false);

        // Damage from item send? (lower damage chance)
        breakTries = totalSentToItems / MAX_ITEM_SEND;
        for (int i = 0; i < breakTries; ++i) {
          if (SilentGems.random.nextFloat() < getBreakChance(stack) / 3) {
            damageOrb(stack, player);
            break;
          }
        }
      }
    }
  }

  public Type getType(ItemStack stack) {

    int meta = stack.getItemDamage() & 0x0F;
    if (meta >= 0 && meta < Type.values().length) {
      return Type.values()[meta];
    }
    return Type.POTATO;
  }

  public float getBreakChance(ItemStack stack) {

    float baseChance = getType(stack).breakChance;
    int charge = getCharge(stack);
    int maxCharge = getMaxCharge(stack);
    float chargePercent = (float) charge / maxCharge;

    // No chance of breaking above half charge.
    if (chargePercent > 0.5f) {
      return 0f;
    }

    return baseChance * 2 * (0.5f - chargePercent);
  }

  public void damageOrb(ItemStack stack, EntityPlayer player) {

    // Increase damage level.
    int currentDamageLevel = (stack.getItemDamage() & 0xF0) >> 4;
    int newMeta = (stack.getItemDamage() & 0x0F) + (++currentDamageLevel << 4);
    stack.setItemDamage(newMeta);

    // Did it break?
    if (currentDamageLevel >= getType(stack).crackStages) {
      breakOrb(stack, player);
      return;
    }

    // Chat notification.
    String line = SilentGems.localizationHelper.getItemSubText(itemName, "crack",
        stack.getDisplayName());
    ChatHelper.sendMessage(player, line);

    // Glass breaking sound.
    player.world.playSound(null, player.posX, player.posY, player.posZ,
        SoundEvents.BLOCK_GLASS_BREAK, SoundCategory.AMBIENT, 0.6f, 1.5f);
  }

  public void breakOrb(ItemStack stack, EntityPlayer player) {

    // Chat notification.
    int pieceCount = SilentGems.random.nextInt(99000) + 1000;
    String line = SilentGems.localizationHelper.getItemSubText(itemName, "break",
        stack.getDisplayName(), pieceCount);
    ChatHelper.sendMessage(player, line);

    // Glass breaking sound.
    player.world.playSound(null, player.posX, player.posY, player.posZ,
        SoundEvents.BLOCK_GLASS_BREAK, SoundCategory.AMBIENT, 0.7f, -2.5f);

    // Delete the broken orb.
    PlayerHelper.removeItem(player, stack);
  }

  public boolean isItemSendEnabled(ItemStack stack) {

    if (StackHelper.isEmpty(stack) || !stack.hasTagCompound())
      return false;
    if (!stack.getTagCompound().hasKey(NBT_ITEM_SEND))
      return true;
    return stack.getTagCompound().getBoolean(NBT_ITEM_SEND);
  }

  public void toggleItemSendEnabled(ItemStack stack) {

    if (StackHelper.isEmpty(stack))
      return;
    if (!stack.hasTagCompound())
      stack.setTagCompound(new NBTTagCompound());
    boolean value = isItemSendEnabled(stack);
    stack.getTagCompound().setBoolean(NBT_ITEM_SEND, !value);
  }

  @Override
  public int getMaxCharge(ItemStack stack) {

    return getType(stack).maxCharge;
  }

  // ===================
  // = Baubles support =
  // ===================

  @Override
  @Optional.Method(modid = BaublesCompat.MOD_ID)
  public BaubleType getBaubleType(ItemStack stack) {

    return BaubleType.TRINKET;
  }

  @Override
  @Optional.Method(modid = BaublesCompat.MOD_ID)
  public void onWornTick(ItemStack stack, EntityLivingBase player) {

    onUpdate(stack, player.world, player, 0, false);

    // If broken, remove from bauble slot as the normal onUpdate doesn't check that.
    int currentDamageLevel = (stack.getItemDamage() & 0xF0) >> 4;
    if (currentDamageLevel >= getType(stack).crackStages) {
      int[] slots = getBaubleType(stack).getValidSlots();
      IBaublesItemHandler inventory = BaublesApi.getBaublesHandler((EntityPlayer) player);

      for (int i = 0; i < slots.length; ++i) {
        ItemStack inSlot = inventory.getStackInSlot(slots[i]);
        if (inSlot == stack) {
          inventory.extractItem(slots[i], 1, false);
          break;
        }
      }
    }
  }

  @Override
  @Optional.Method(modid = BaublesCompat.MOD_ID)
  public boolean willAutoSync(ItemStack stack, EntityLivingBase player) {

    return true;
  }

  @SideOnly(Side.CLIENT)
  @Override
  @Optional.Method(modid = BaublesCompat.MOD_ID)
  public void onPlayerBaubleRender(ItemStack stack, EntityPlayer player, RenderType renderType,
      float partialTicks) {

    if (renderType == RenderType.BODY) {
      float scale = 0.4f;
      GlStateManager.scale(scale, scale, scale);
      IRenderBauble.Helper.rotateIfSneaking(player);
      GlStateManager.rotate(90, 0, 1, 0);
      IRenderBauble.Helper.translateToChest();
      GlStateManager.translate(0.0, 1.5, 1.55);
      Minecraft.getMinecraft().getRenderItem().renderItem(stack, TransformType.FIXED);
    }
  }
}
