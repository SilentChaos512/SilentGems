package net.silentchaos512.gems.item;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.lwjgl.opengl.GL11;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.silentchaos512.gems.SilentGems;
import net.silentchaos512.gems.lib.ChaosBuff;
import net.silentchaos512.gems.lib.EnumGem;
import net.silentchaos512.gems.lib.Names;
import net.silentchaos512.lib.registry.SRegistry;
import net.silentchaos512.lib.util.RecipeHelper;

public class ItemChaosGem extends ItemChaosStorage {

  public static final String NBT_ENABLED = "enabled";
  public static final String NBT_BUFF_LIST = "buff_list";
  public static final String NBT_BUFF_KEY = "key";
  public static final String NBT_BUFF_LEVEL = "level";

  public static final int ID_CHEATY_GEM = EnumGem.values().length;
  public static final int BASE_CAPACITY = 2000000;
  public static final int UPGRADE_CAPACITY = 1000000;
  public static final int SELF_RECHARGE_BASE = 10;
  public static final int MAX_SLOTS = 20;

  public ItemChaosGem() {

    super(EnumGem.values().length + 1, Names.CHAOS_GEM, BASE_CAPACITY);
    setMaxStackSize(1);
  }

  // ===================
  // = IRegistryObject =
  // ===================

  @Override
  public void addRecipes() {

    for (EnumGem gem : EnumGem.values())
      RecipeHelper.addSurroundOre(new ItemStack(this, 1, gem.ordinal()), gem.getBlockOreName(),
          ModItems.craftingMaterial.chaosEssenceEnriched);
  }

  // =================
  // = IChaosHandler =
  // =================

  @Override
  public int getMaxCharge(ItemStack stack) {

    if (stack.getItemDamage() == ID_CHEATY_GEM)
      return 0;
    int capacityLevel = getBuffLevel(stack, ChaosBuff.CAPACITY);
    return BASE_CAPACITY + UPGRADE_CAPACITY * capacityLevel;
  }

  // =====================
  // = Chaos Gem methods =
  // =====================

  public int getTotalChargeDrain(ItemStack stack, EntityPlayer player) {

    int total = 0;
    for (Entry<ChaosBuff, Integer> entry : getBuffs(stack).entrySet())
      total += entry.getKey().getChaosCost(entry.getValue(), player);
    return total;
  }

  public int getSelfRechargeAmount(ItemStack stack) {

    int rechargeLevel = getBuffLevel(stack, ChaosBuff.RECHARGE);
    return SELF_RECHARGE_BASE * rechargeLevel;
  }

  public int getSlotsUsed(ItemStack stack) {

    return getSlotsUsed(getBuffs(stack));
  }

  public int getSlotsUsed(Map<ChaosBuff, Integer> buffMap) {

    int slotsUsed = 0;
    for (Entry<ChaosBuff, Integer> entry : buffMap.entrySet())
      slotsUsed += entry.getKey().getSlotsUsed(entry.getValue());
    return slotsUsed;
  }

  public boolean isEnabled(ItemStack stack) {

    return stack != null && stack.hasTagCompound()
        && stack.getTagCompound().getBoolean(NBT_ENABLED);
  }

  public void setEnabled(ItemStack stack, boolean val) {

    if (stack == null)
      return;
    if (!stack.hasTagCompound())
      stack.setTagCompound(new NBTTagCompound());

    if (getCharge(stack) <= 0)
      val = false;
    stack.getTagCompound().setBoolean(NBT_ENABLED, val);
  }

  public int getBuffLevel(ItemStack stack, ChaosBuff buff) {

    Map<ChaosBuff, Integer> buffMap = getBuffs(stack);
    if (buffMap.containsKey(buff))
      return buffMap.get(buff);
    return 0;
  }

  public Map<ChaosBuff, Integer> getBuffs(ItemStack stack) {

    if (stack == null || !stack.hasTagCompound() || !stack.getTagCompound().hasKey(NBT_BUFF_LIST))
      return Maps.newHashMap();

    NBTTagList tagList = stack.getTagCompound().getTagList(NBT_BUFF_LIST, 10);
    Map<ChaosBuff, Integer> map = Maps.newLinkedHashMap();

    if (tagList != null) {
      for (int i = 0; i < tagList.tagCount(); ++i) {
        String key = tagList.getCompoundTagAt(i).getString(NBT_BUFF_KEY);
        int level = tagList.getCompoundTagAt(i).getShort(NBT_BUFF_LEVEL);
        map.put(ChaosBuff.byKey(key), level);
      }
    }

    return map;
  }

  public boolean addBuff(ItemStack stack, ChaosBuff buff) {

    if (canAddBuff(stack, buff)) {
      Map<ChaosBuff, Integer> buffMap = getBuffs(stack);
      int currentLevel = 0;
      if (buffMap.containsKey(buff))
        currentLevel = buffMap.get(buff);

      buffMap.put(buff, currentLevel + 1);
      setBuffs(stack, buffMap);

      return true;
    }

    return false;
  }

  public void setBuffs(ItemStack stack, Map<ChaosBuff, Integer> buffs) {

    if (stack == null)
      return;
    if (!stack.hasTagCompound())
      stack.setTagCompound(new NBTTagCompound());
    if (stack.getTagCompound().hasKey(NBT_BUFF_LIST))
      stack.getTagCompound().removeTag(NBT_BUFF_LIST);

    NBTTagList tagList = new NBTTagList();

    for (Entry<ChaosBuff, Integer> entry : buffs.entrySet()) {
      ChaosBuff buff = entry.getKey();
      int level = entry.getValue();
      NBTTagCompound compound = new NBTTagCompound();
      compound.setString(NBT_BUFF_KEY, buff.getKey());
      compound.setShort(NBT_BUFF_LEVEL, (short) level);
      tagList.appendTag(compound);
    }

    stack.getTagCompound().setTag(NBT_BUFF_LIST, tagList);
  }

  public boolean canAddBuff(ItemStack stack, ChaosBuff buff) {

    if (stack == null)
      return false;
    if (!stack.hasTagCompound())
      return true;

    Map<ChaosBuff, Integer> buffMap = getBuffs(stack);

    // We already have this buff, but might be able to increment
    if (buffMap.containsKey(buff)) {
      int currentLevel = buffMap.get(buff);
      if (currentLevel >= buff.getMaxLevel())
        return false;
      buffMap.put(buff, currentLevel + 1);
    }
    // We don't have the buff
    else {
      buffMap.put(buff, 1);
    }

    // Not exceeding max slots?
    return getSlotsUsed(buffMap) <= MAX_SLOTS;
  }

  public boolean isCheatyGem(ItemStack stack) {

    return stack.getItemDamage() == 32;
  }

  public void applyEffects(ItemStack stack, EntityPlayer player) {

    for (Entry<ChaosBuff, Integer> entry : getBuffs(stack).entrySet())
      entry.getKey().applyToPlayer(player, entry.getValue(), stack);
  }

  public void removeEffects(ItemStack stack, EntityPlayer player) {

    for (Entry<ChaosBuff, Integer> entry : getBuffs(stack).entrySet())
      entry.getKey().removeFromPlayer(player, entry.getValue(), stack);
  }

  // ==================
  // = Item overrides =
  // ==================

  @Override
  public void onUpdate(ItemStack stack, World world, Entity entity, int itemSlot,
      boolean isSelected) {

    if (world.isRemote || !(entity instanceof EntityPlayer))
      return;

    EntityPlayer player = (EntityPlayer) entity;
    boolean enabled = isEnabled(stack);
    int totalDrain = getTotalChargeDrain(stack, player);

    // Apply effects?
    if (enabled) {
      applyEffects(stack, player);

      // Drain charge?
      if (!isCheatyGem(stack)) {
        extractCharge(stack, totalDrain, false);
        // Disable if out of charge.
        if (getCharge(stack) <= 0) {
          setEnabled(stack, false);
          removeEffects(stack, player);
        }
      }
    }

    if (!enabled || totalDrain <= 0){
      // Self-recharge when disabled?
      receiveCharge(stack, getSelfRechargeAmount(stack), false);
    }
  }

  @Override
  public boolean onDroppedByPlayer(ItemStack stack, EntityPlayer player) {

    // Set disabled
    setEnabled(stack, false);
    removeEffects(stack, player);
    return true;
  }

  @Override
  public ActionResult<ItemStack> onItemRightClick(ItemStack stack, World world, EntityPlayer player,
      EnumHand hand) {

    // Enable/disable
    if (getCharge(stack) > 0) {
      setEnabled(stack, !isEnabled(stack));
      if (isEnabled(stack))
        applyEffects(stack, player);
      else
        removeEffects(stack, player);
    }

    return new ActionResult(EnumActionResult.SUCCESS, stack);
  }

  @Override
  public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean advanced) {

    // TODO
    for (Entry<ChaosBuff, Integer> entry : getBuffs(stack).entrySet()) {
      list.add(entry.getKey().getLocalizedName(entry.getValue()));
    }

    int slotsUsed = getSlotsUsed(stack);
    int totalDrain = getTotalChargeDrain(stack, player);
    list.add(String.format("Charge: %,d / %,d", getCharge(stack), getMaxCharge(stack)));
    list.add(String.format("Slots: %d / %d", slotsUsed, MAX_SLOTS));
    list.add(String.format("Drain: %d", totalDrain));
  }

  @Override
  public EnumRarity getRarity(ItemStack stack) {

    return EnumRarity.RARE;
  }

  @Override
  public double getDurabilityForDisplay(ItemStack stack) {

    double charge = getCharge(stack);
    double max = getMaxCharge(stack);
    return max > 0 ? (max - charge) / max : 1.0;
  }

  @Override
  public boolean showDurabilityBar(ItemStack stack) {

    return getCharge(stack) < getMaxCharge(stack);
  }

  @Override
  public boolean hasEffect(ItemStack stack) {

    return isEnabled(stack);
  }

  @Override
  public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack,
      boolean slotChanged) {

    boolean oldEnabled = isEnabled(oldStack);
    boolean newEnabled = isEnabled(newStack);
    return slotChanged || oldEnabled != newEnabled || !oldStack.isItemEqual(newStack);
  }

  @Override
  public void getSubItems(Item item, CreativeTabs tab, List list) {

    for (int i = 0; i < subItemCount; ++i) {
      // Empty
      ItemStack stack1 = new ItemStack(item, 1, i);
      list.add(stack1);
      // Full
      ItemStack stack2 = stack1.copy();
      receiveCharge(stack2, getMaxCharge(stack2), false);
      list.add(stack2);
    }
  }

  // ==============
  // = HUD render =
  // ==============

  public static final ResourceLocation TEXTURE_FRAME = new ResourceLocation(
      SilentGems.MOD_ID.toLowerCase(), "textures/gui/ChaosBarFrame.png");
  public static final ResourceLocation TEXTURE_BAR = new ResourceLocation(
      SilentGems.MOD_ID.toLowerCase(), "textures/gui/ChaosBar.png");

  public static final int BAR_WIDTH = 64;
  public static final int BAR_HEIGHT = 8;

  @SideOnly(Side.CLIENT)
  public static void renderGameOverlay(Minecraft mc) {

    EntityPlayer player = mc.thePlayer;
    List<ItemStack> gems = Lists.newArrayList();
    ItemChaosGem chaosGem = ModItems.chaosGem;

    for (ItemStack stack : player.inventory.mainInventory)
      if (stack != null && stack.getItem() instanceof ItemChaosGem)
        if (chaosGem.isEnabled(stack) && chaosGem.getTotalChargeDrain(stack, player) > 0)
          gems.add(stack);

    if (gems.isEmpty())
      return;

    ScaledResolution res = new ScaledResolution(mc);

    int current;
    int max;
    float storedFraction;

    int posX;
    int posY;
    int index = 1;
    float scale;

    for (ItemStack gem : gems) {
      current = chaosGem.getCharge(gem);
      max = chaosGem.getMaxCharge(gem);
      storedFraction = (float) current / (float) max;

      // Get bar color
      float red = 1f;
      float green = 1f;
      float blue = 1f;
      int gemId = gem.getItemDamage();
      if (gemId >= 0 && gemId < EnumGem.values().length) {
        int color = EnumGem.values()[gemId].getColor();
        red = (color >> 16 & 255) / 255f;
        green = (color >> 8 & 255) / 255f;
        blue = (color & 255) / 255f;
      }

      GL11.glDisable(GL11.GL_LIGHTING);
      GL11.glEnable(GL11.GL_BLEND);

      scale = 1f;
      GL11.glPushMatrix();
      GL11.glScalef(scale, scale, 1f);

      posX = (int) (res.getScaledWidth() / scale / 2 - BAR_WIDTH / 2);
      posY = (int) (res.getScaledHeight() / scale * 0.05f + index * (BAR_HEIGHT + 2));

      // Bar
      GL11.glColor4f(red, green, blue, 0.5f);
      mc.renderEngine.bindTexture(TEXTURE_BAR);
      float barPosWidth = BAR_WIDTH * storedFraction;
      float barPosX = posX;
      float barPosHeight = BAR_HEIGHT;
      float barPosY = posY;
      // RenderHelper.drawRect(barPosX, barPosY, 0, 0, barPosWidth, barPosHeight);

      // Bar frame
      GL11.glColor3f(1f, 1f, 1f);
      mc.renderEngine.bindTexture(TEXTURE_FRAME);
      // RenderHelper.drawRect(posX, posY, 0, 0, BAR_WIDTH, BAR_HEIGHT);

      GL11.glEnable(GL11.GL_BLEND);
      GL11.glPopMatrix();

      // Render charge level/percentage?
      GL11.glPushMatrix();

      GL11.glColor3f(1f, 1f, 1f);
      scale = 0.7f;
      GL11.glScalef(scale, scale, 1f);

      FontRenderer fontRender = mc.fontRendererObj;
      String format = "%.2f%%";
      String str = String.format(format, storedFraction * 100f);
      posX = (int) (res.getScaledWidth() / scale / 2 - fontRender.getStringWidth(str) / 2);
      posY = (int) (res.getScaledHeight() / scale * 0.05f + index * (BAR_HEIGHT + 2) / scale + 1);
      fontRender.drawStringWithShadow(str, posX, posY, 0xFFFFFF);

      GL11.glPopMatrix();

      ++index;
    }
  }
}
