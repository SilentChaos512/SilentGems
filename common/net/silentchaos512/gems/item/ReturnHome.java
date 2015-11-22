package net.silentchaos512.gems.item;

import java.util.List;

import org.lwjgl.input.Keyboard;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemMeshDefinition;
import net.minecraft.client.renderer.ItemModelMesher;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Items;
import net.minecraft.item.EnumAction;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.silentchaos512.gems.SilentGems;
import net.silentchaos512.gems.configuration.Config;
import net.silentchaos512.gems.core.registry.IRegisterModels;
import net.silentchaos512.gems.core.util.LocalizationHelper;
import net.silentchaos512.gems.core.util.LogHelper;
import net.silentchaos512.gems.core.util.NBTHelper;
import net.silentchaos512.gems.core.util.PlayerHelper;
import net.silentchaos512.gems.core.util.TeleportUtil;
import net.silentchaos512.gems.lib.EnumGem;
import net.silentchaos512.gems.lib.Names;
import net.silentchaos512.gems.lib.Strings;

public class ReturnHome extends ItemSG {

  public static final String BOUND_TO = "BoundTo";
  public static final String NOT_BOUND = "NotBound";
  public static final String NOT_SANE = "NotSane";
  public static final String NOT_SAFE = "NotSafe";

  public static final String NBT_GEM = "Gem";
  public static final String NBT_CHARGED = "IsCharged";

  private final ItemStack[] subItems;
  private ModelResourceLocation[] models;

  public ReturnHome() {

    super();

    setMaxStackSize(1);
    setMaxDamage(Config.RETURN_HOME_MAX_USES);
    setNoRepair();
    setUnlocalizedName(Names.RETURN_HOME);
    rarity = EnumRarity.UNCOMMON;

    // Create gem subtypes
    subItems = new ItemStack[EnumGem.values().length];
    ItemStack stack;
    for (int i = 0; i < EnumGem.values().length; ++i) {
      stack = new ItemStack(this);
      setGem(stack, i);
      subItems[i] = stack;
    }
  }
  
  private int getTagInteger(ItemStack stack, String key) {

    if (stack != null) {
      if (!stack.hasTagCompound()) {
        return 0;
      } else {
        return stack.getTagCompound().getInteger(key);
      }
    }
    return 0;
  }
  
  private void setTagInteger(ItemStack stack, String key, int value) {
    
    if (stack != null) {
      if (!stack.hasTagCompound()) {
        stack.setTagCompound(new NBTTagCompound());
      }
      stack.getTagCompound().setInteger(key, value);
    }
  }
  
  private boolean getTagBoolean(ItemStack stack, String key) {

    if (stack != null) {
      if (!stack.hasTagCompound()) {
        return false;
      } else {
        return stack.getTagCompound().getBoolean(key);
      }
    }
    return false;
  }
  
  private void setTagBoolean(ItemStack stack, String key, boolean value) {
    
    if (stack != null) {
      if (!stack.hasTagCompound()) {
        stack.setTagCompound(new NBTTagCompound());
      }
      stack.getTagCompound().setBoolean(key, value);
    }
  }

  @Override
  public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean par4) {
    
    list.add(EnumChatFormatting.ITALIC + "Yes, I know they all have the same texture!");

    // Is ctrl key down?
    boolean modifier = Keyboard.isKeyDown(Keyboard.KEY_LCONTROL)
        || Keyboard.isKeyDown(Keyboard.KEY_RCONTROL);

    // How to use
    list.add(EnumChatFormatting.AQUA + LocalizationHelper.getItemDescription(itemName, 1));
    // Item description
    if (this.showFlavorText()) {
      list.add(EnumChatFormatting.ITALIC + LocalizationHelper.getItemDescription(itemName, 2));
    }

    // Display coordinates if modifier key is held.
    if (NBTHelper.hasValidXYZD(stack.getTagCompound())) {
      if (modifier) {
        String s = LocalizationHelper.getOtherItemKey(itemName, BOUND_TO);
        s = String.format(s, LogHelper.coordFromNBT(stack.getTagCompound()));
        list.add(EnumChatFormatting.GREEN + s);
      } else {
        list.add(LocalizationHelper.getMiscText(Strings.PRESS_CTRL));
      }
    } else {
      list.add(EnumChatFormatting.RED + LocalizationHelper.getOtherItemKey(itemName, NOT_BOUND));
    }
  }

  public int getGem(ItemStack stack) {

    return getTagInteger(stack, NBT_GEM);
  }

  public void setGem(ItemStack stack, int gem) {

    setTagInteger(stack, NBT_GEM, gem);
  }

  @Override
  public void addRecipes() {

    ItemStack essence = CraftingMaterial.getStack(Names.CHAOS_ESSENCE_PLUS);
    for (ItemStack stack : subItems) {
      String gem = EnumGem.values()[getGem(stack)].getItemOreName();
      GameRegistry.addRecipe(new ShapedOreRecipe(stack, " s ", "sgs", "ici", 's', Items.string, 'g',
          gem, 'i', "ingotGold", 'c', essence));
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

    return getTagBoolean(stack, NBT_CHARGED);
  }
  
  @Override
  public String[] getVariantNames() {
    
//    String[] result = new String[EnumGem.values().length];
//    for (int i = 0; i < result.length; ++i) {
//      result[i] = getFullName() + i;
//    }
//    return result;
    return new String[] { getFullName() + "0" };
  }
  
//  @Override
//  public void registerModels() {
//    
//    models = new ModelResourceLocation[EnumGem.values().length];
//    String[] variants = getVariantNames();
//    for (int i = 0; i < variants.length; ++i) {
//      models[i] = new ModelResourceLocation(variants[i]);
//    }
//    
//    ItemModelMesher mesher = Minecraft.getMinecraft().getRenderItem().getItemModelMesher();
//    // FIXME: Return home models.
////    for (ModelResourceLocation model : models) {
////      mesher.register(this, 0, model);
////    }
//    mesher.register(this, 0, models[0]);
//  }
  
//  @Override
//  public ModelResourceLocation getModel(ItemStack stack, EntityPlayer player, int useRemaining) {
//    
//    int gem = getGem(stack);
//    return models[MathHelper.clamp_int(gem, 0, EnumGem.values().length - 1)];
//  }

  @Override
  public void getSubItems(Item item, CreativeTabs tab, List list) {

    for (ItemStack stack : subItems) {
      list.add(stack);
    }
  }

  @Override
  public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player) {

    if (NBTHelper.hasValidXYZD(stack.getTagCompound())) {
      player.setItemInUse(stack, this.getMaxItemUseDuration(stack));
    } else if (!world.isRemote) {
      PlayerHelper.addChatMessage(player, LocalizationHelper.getOtherItemKey(itemName, NOT_BOUND));
    }

    return stack;
  }

  @Override
  public void onUsingTick(ItemStack stack, EntityPlayer player, int count) {

    if (player.worldObj.isRemote) {
      int timeUsed = getMaxItemUseDuration(stack) - count;
      if (timeUsed >= Config.RETURN_HOME_USE_TIME) {
        setTagBoolean(stack, NBT_CHARGED, true);
      }
    }
  }
  
  @Override
  public void onUpdate(ItemStack stack, World world, Entity entity, int k, boolean b) {

    if (entity instanceof EntityPlayer) {
      EntityPlayer player = (EntityPlayer) entity;
      if (player.getCurrentEquippedItem() != stack) {
        setTagBoolean(stack, NBT_CHARGED, false);
      }
    }
  }

  @Override
  public void onPlayerStoppedUsing(ItemStack stack, World world, EntityPlayer player,
      int itemInUseCount) {
    
    setTagBoolean(stack, NBT_CHARGED, false);

    if (!world.isRemote) {
      int timeUsed = this.getMaxItemUseDuration(stack) - itemInUseCount;

      // Did player use item long enough?
      if (timeUsed < Config.RETURN_HOME_USE_TIME) {
        return;
      }

      // Is the destination sane? (ie, y > 0)
      if (!this.isDestinationAboveZero(stack)) {
        String str = LocalizationHelper.getOtherItemKey(this.itemName, NOT_SANE);
        PlayerHelper.addChatMessage(player, str);
        return;
      }

      // Is the destination safe? (ie, no solid block at head level)
      if (!this.isDestinationObstructed(stack, world)) {
        String str = LocalizationHelper.getOtherItemKey(this.itemName, NOT_SAFE);
        PlayerHelper.addChatMessage(player, str);
        return;
      }

      // It should be safe to teleport.
      float soundPitch = SilentGems.instance.random.nextFloat();
      soundPitch = soundPitch * 0.3f + 0.8f;
      world.playSoundAtEntity(player, "mob.endermen.portal", 1.0f, soundPitch);
      // Reset fall distance and teleport.
      player.fallDistance = 0.0f;
      teleportPlayer(stack, player);
      world.playSoundAtEntity(player, "mob.endermen.portal", 1.0f, soundPitch);
    }
  }

  private boolean isDestinationAboveZero(ItemStack stack) {

    return getTagInteger(stack, "Y") > 0;
  }

  private boolean isDestinationObstructed(ItemStack stack, World world) {

    NBTTagCompound tags = stack.getTagCompound();
    int dx = tags.getInteger("X");
    int dy = tags.getInteger("Y");
    int dz = tags.getInteger("Z");
    int dd = tags.getInteger("D");

    WorldServer worldServer = MinecraftServer.getServer().worldServerForDimension(dd);
    return !worldServer.isBlockNormalCube(new BlockPos(dx, dy + 2, dz), true);
  }

  private void teleportPlayer(ItemStack stack, EntityPlayer player) {

    if (!stack.hasTagCompound()) {
      return;
    }

    NBTTagCompound tags = stack.getTagCompound();
    if (!NBTHelper.hasValidXYZD(tags) || tags.getInteger("Y") <= 0) {
      LogHelper.warning("Invalid location for teleport effect");
      return;
    }

    int dx = tags.getInteger("X");
    int dy = tags.getInteger("Y");
    int dz = tags.getInteger("Z");
    int dd = tags.getInteger("D");

    // Teleport player
    if (player instanceof EntityPlayerMP) {
      TeleportUtil.teleportPlayerTo((EntityPlayerMP) player, dx, dy, dz, dd);
    }

    // Damage item
    if (!player.capabilities.isCreativeMode) {
      if (stack.attemptDamageItem(1, player.worldObj.rand)) {
        player.destroyCurrentEquippedItem();
      }
    }
  }
}
