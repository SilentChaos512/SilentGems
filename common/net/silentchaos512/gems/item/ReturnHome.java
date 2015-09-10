package net.silentchaos512.gems.item;

import java.util.List;

import org.lwjgl.input.Keyboard;

import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.client.renderer.texture.IIconRegister;
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
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.silentchaos512.gems.SilentGems;
import net.silentchaos512.gems.configuration.Config;
import net.silentchaos512.gems.core.util.LocalizationHelper;
import net.silentchaos512.gems.core.util.LogHelper;
import net.silentchaos512.gems.core.util.NBTHelper;
import net.silentchaos512.gems.core.util.PlayerHelper;
import net.silentchaos512.gems.core.util.TeleportUtil;
import net.silentchaos512.gems.lib.EnumGem;
import net.silentchaos512.gems.lib.Names;
import net.silentchaos512.gems.lib.Strings;
import net.silentchaos512.gems.world.TeleporterGems;

public class ReturnHome extends ItemSG {

  public static final String BOUND_TO = "BoundTo";
  public static final String NOT_BOUND = "NotBound";
  public static final String NOT_SANE = "NotSane";
  public static final String NOT_SAFE = "NotSafe";

  public static final String NBT_GEM = "Gem";

  private final ItemStack[] subItems;

  public ReturnHome() {

    super();

    setMaxStackSize(1);
    setMaxDamage(Config.returnHomeMaxUses);
    setNoRepair();
    setUnlocalizedName(Names.RETURN_HOME);
    rarity = EnumRarity.uncommon;

    // Create gem subtypes
    subItems = new ItemStack[EnumGem.values().length];
    ItemStack stack;
    for (int i = 0; i < EnumGem.values().length; ++i) {
      stack = new ItemStack(this);
      setGem(stack, i);
      subItems[i] = stack;
    }
  }

  @Override
  public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean par4) {

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
    if (stack.stackTagCompound != null && NBTHelper.hasValidXYZD(stack.stackTagCompound)) {
      if (modifier) {
        String s = LocalizationHelper.getOtherItemKey(itemName, BOUND_TO);
        s = String.format(s, LogHelper.coordFromNBT(stack.stackTagCompound));
        list.add(EnumChatFormatting.GREEN + s);
      } else {
        list.add(LocalizationHelper.getMiscText(Strings.PRESS_CTRL));
      }
    } else {
      list.add(EnumChatFormatting.RED + LocalizationHelper.getOtherItemKey(itemName, NOT_BOUND));
    }
  }

  public int getGem(ItemStack stack) {

    if (stack == null) {
      return 0;
    } else if (stack.stackTagCompound == null) {
      stack.setTagCompound(new NBTTagCompound());
    }

    return stack.stackTagCompound.getInteger(NBT_GEM);
  }

  public void setGem(ItemStack stack, int gem) {

    if (stack == null || gem < 0 || gem >= EnumGem.values().length) {
      return;
    } else if (stack.stackTagCompound == null) {
      stack.setTagCompound(new NBTTagCompound());
    }

    stack.stackTagCompound.setInteger(NBT_GEM, gem);
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

    return EnumAction.bow;
  }

  @Override
  public int getMaxItemUseDuration(ItemStack stack) {

    return 133700;
  }

  @Override
  public void getSubItems(Item item, CreativeTabs tab, List list) {

    for (ItemStack stack : subItems) {
      list.add(stack);
    }
  }

  @Override
  public void registerIcons(IIconRegister reg) {

    icons = new IIcon[EnumGem.values().length];
    for (int i = 0; i < EnumGem.values().length; ++i) {
      icons[i] = reg.registerIcon(Strings.RESOURCE_PREFIX + itemName + i);
    }
  }

  @Override
  public IIcon getIcon(ItemStack stack, int pass) {

    int gem = MathHelper.clamp_int(getGem(stack), 0, EnumGem.values().length - 1);
    return icons[gem];
  }

  @Override
  public boolean requiresMultipleRenderPasses() {

    return true; // The ItemStack-sensitive getIcon isn't called unless this is true.
  }

  @Override
  public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player) {

    if (stack != null && stack.stackTagCompound != null
        && NBTHelper.hasValidXYZD(stack.stackTagCompound)) {
      player.setItemInUse(stack, this.getMaxItemUseDuration(stack));
    } else if (!world.isRemote) {
      PlayerHelper.addChatMessage(player, LocalizationHelper.getOtherItemKey(itemName, NOT_BOUND));
    }

    return stack;
  }

  @Override
  public void onPlayerStoppedUsing(ItemStack stack, World world, EntityPlayer player,
      int itemInUseCount) {

    if (!world.isRemote) {
      int timeUsed = this.getMaxItemUseDuration(stack) - itemInUseCount;

      // Did player use item long enough?
      if (timeUsed < Config.returnHomeUseTime) {
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

    return stack.stackTagCompound.getInteger("Y") > 0;
  }

  private boolean isDestinationObstructed(ItemStack stack, World world) {

    NBTTagCompound tags = stack.stackTagCompound;
    int dx = tags.getInteger("X");
    int dy = tags.getInteger("Y");
    int dz = tags.getInteger("Z");
    int dd = tags.getInteger("D");

    WorldServer worldServer = MinecraftServer.getServer().worldServerForDimension(dd);
    return !worldServer.isBlockNormalCubeDefault(dx, dy + 2, dz, true);
  }

  private void teleportPlayer(ItemStack stack, EntityPlayer player) {

    if (stack.stackTagCompound == null) {
      return;
    }

    NBTTagCompound tags = stack.stackTagCompound;
    if (!NBTHelper.hasValidXYZD(tags) || tags.getInteger("Y") <= 0) {
      LogHelper.warning("Invalid location for teleport effect");
      return;
    }

    int dx = tags.getInteger("X");
    int dy = tags.getInteger("Y");
    int dz = tags.getInteger("Z");
    int dd = tags.getInteger("D");

    // Dismount and teleport mount
    if (player.ridingEntity != null) {
      Entity mount = player.ridingEntity;
      player.mountEntity((Entity) null);
      if (dd != mount.dimension) {
        mount.travelToDimension(dd); // FIXME: Will this spawn Nether portals?
      }
      mount.setLocationAndAngles(dx + 0.5, dy + 1.0, dz + 0.5, mount.rotationYaw,
          mount.rotationPitch);
    }

    // Teleport player
    if (player instanceof EntityPlayerMP) {
      TeleportUtil.teleportPlayerTo((EntityPlayerMP) player, dx, dy, dz, dd);
    }
//    int oldDimension = player.worldObj.provider.dimensionId;
//    if (dd != oldDimension) {
//      if (player instanceof EntityPlayerMP) {
//        WorldServer worldServer = MinecraftServer.getServer().worldServerForDimension(dd);
//        EntityPlayerMP playerMP = (EntityPlayerMP) player;
//        playerMP.mcServer.getConfigurationManager().transferPlayerToDimension(playerMP, dd,
//            new TeleporterGems(worldServer));
//        if (oldDimension == 1) {
//          worldServer.spawnEntityInWorld(playerMP);
//        }
//      }
//    }
//    player.setPositionAndUpdate(dx + 0.5, dy + 1.0, dz + 0.5);

    // Damage item
    if (!player.capabilities.isCreativeMode) {
      if (stack.attemptDamageItem(1, player.worldObj.rand)) {
        player.destroyCurrentEquippedItem();
      }
    }
  }
}
