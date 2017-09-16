package net.silentchaos512.gems.entity.packet;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.SoundCategory;
import net.minecraft.world.World;
import net.silentchaos512.gems.SilentGems;
import net.silentchaos512.lib.collection.ItemStackList;
import net.silentchaos512.lib.util.Color;
import net.silentchaos512.lib.util.ItemHelper;
import net.silentchaos512.lib.util.PlayerHelper;

public class EntityPacketRepair extends EntityChaosNodePacket {

  public static Set<Item> REPAIR_WHITELIST = new HashSet<>();
  public static Set<Item> REPAIR_BLACKLIST = new HashSet<>();

  public static final int VALUE_MEAN = 10;
  public static final int VALUE_DEVIATION = 2;

  public static final Color COLOR_HEAD = new Color(0x0033CC);
  public static final Color COLOR_TAIL = new Color(0x66CCFF);
  public static final ColorPair COLOR_PAIR = new ColorPair(COLOR_HEAD, COLOR_TAIL);
  public static final int COLOR_INDEX = 2;

  public EntityPacketRepair(World worldIn, EntityLivingBase target) {

    this(worldIn, target, selectRepairValue(SilentGems.instance.random));
  }

  public EntityPacketRepair(World worldIn, EntityLivingBase target, int amount) {

    super(worldIn, target);
    this.amount = amount;
  }

  public EntityPacketRepair(World worldIn) {

    super(worldIn);
  }

  public static int selectRepairValue(Random rand) {

    int value = (int) (VALUE_MEAN + VALUE_DEVIATION * rand.nextGaussian());
    return value <= 0 ? 1 : value;
  }

  @Override
  public void onImpactWithEntity(EntityLivingBase entity) {

    if (!(entity instanceof EntityPlayer)) {
      super.onImpactWithEntity(entity);
      return;
    }

    Random rand = SilentGems.instance.random;

    EntityPlayer player = (EntityPlayer) entity;
    ItemStack stackToRepair = null;
    int sizeMain = player.inventory.getSizeInventory();
    int index = rand.nextInt(sizeMain + 5);

    // Select a random item.
    ItemStackList items = PlayerHelper.getNonEmptyStacks(player);
    items.removeIf(stack -> !canRepair(stack));
    if (items.size() > 0) {
      stackToRepair = items.get(rand.nextInt(items.size()));
    }

    if (stackToRepair != null) {
      ItemHelper.attemptDamageItem(stackToRepair, (int) -amount, rand, player);
      world.playSound(null, posX, posY, posZ, SoundEvents.BLOCK_ANVIL_USE, SoundCategory.AMBIENT,
          0.5f, 2.0f + (float) (0.2 * rand.nextGaussian()));
    }

    super.onImpactWithEntity(entity);
  }

  protected boolean canRepair(ItemStack stack) {

    if (REPAIR_BLACKLIST.contains(stack.getItem())) {
      return false;
    }

    return stack.getItemDamage() > 0 && stack.isItemStackDamageable()
        && (stack.getItem().isRepairable() || REPAIR_WHITELIST.contains(stack.getItem()));
  }

  @Override
  public Color getColorHead() {

    return COLOR_HEAD;
  }

  @Override
  public Color getColorTail() {

    return COLOR_TAIL;
  }

  @Override
  public ColorPair getColorPair() {

    return COLOR_PAIR;
  }

  @Override
  public int getColorIndex() {

    return COLOR_INDEX;
  }

  public static void loadItemList(boolean whitelist, String[] list) {

    for (int i = 0; i < list.length; ++i) {
      Item item = Item.getByNameOrId(list[i]);
      if (item != null) {
        if (whitelist) {
          REPAIR_WHITELIST.add(item);
        } else {
          REPAIR_BLACKLIST.add(item);
        }
      }
    }
  }
}
