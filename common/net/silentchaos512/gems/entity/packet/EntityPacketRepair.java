package net.silentchaos512.gems.entity.packet;

import java.util.List;
import java.util.Random;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.util.SoundCategory;
import net.minecraft.world.World;
import net.silentchaos512.gems.SilentGems;
import net.silentchaos512.gems.entity.packet.EntityChaosNodePacket.ColorPair;
import net.silentchaos512.lib.util.Color;
import net.silentchaos512.lib.util.PlayerHelper;

public class EntityPacketRepair extends EntityChaosNodePacket {

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
    List<ItemStack> items = PlayerHelper.getNonNullStacks(player);
    items.removeIf(stack -> !stack.isItemStackDamageable() || stack.getItemDamage() == 0);
    if (items.size() > 0) {
      stackToRepair = items.get(rand.nextInt(items.size()));
    }

    if (stackToRepair != null) {
      stackToRepair.attemptDamageItem((int) -amount, rand);
      worldObj.playSound(null, posX, posY, posZ, SoundEvents.block_anvil_use, SoundCategory.AMBIENT,
          0.5f, 2.0f + (float) (0.2 * rand.nextGaussian()));
    }

    super.onImpactWithEntity(entity);
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
}
