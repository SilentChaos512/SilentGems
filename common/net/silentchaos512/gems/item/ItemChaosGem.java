package net.silentchaos512.gems.item;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.lwjgl.opengl.GL11;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import baubles.api.BaubleType;
import baubles.api.IBauble;
import baubles.api.render.IRenderBauble;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms.TransformType;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
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
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.Optional;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.silentchaos512.gems.SilentGems;
import net.silentchaos512.gems.compat.BaublesCompat;
import net.silentchaos512.gems.init.ModItems;
import net.silentchaos512.gems.lib.ChaosBuff;
import net.silentchaos512.gems.lib.EnumGem;
import net.silentchaos512.gems.lib.Names;
import net.silentchaos512.lib.client.render.BufferBuilderSL;
import net.silentchaos512.lib.registry.RecipeMaker;
import net.silentchaos512.lib.util.ItemHelper;
import net.silentchaos512.lib.util.LocalizationHelper;
import net.silentchaos512.lib.util.PlayerHelper;
import net.silentchaos512.lib.util.StackHelper;

@Optional.InterfaceList({
    @Optional.Interface(iface = "baubles.api.IBauble", modid = BaublesCompat.MOD_ID),
    @Optional.Interface(iface = "baubles.api.render.IRenderBauble", modid = BaublesCompat.MOD_ID) })
public class ItemChaosGem extends ItemChaosStorage implements IBauble, IRenderBauble {

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
  public void addRecipes(RecipeMaker recipes) {

    for (EnumGem gem : EnumGem.values())
      recipes.addSurroundOre("chaos_gem_" + gem.name(), new ItemStack(this, 1, gem.ordinal()),
          gem.getBlockOreName(), ModItems.craftingMaterial.chaosEssenceEnriched);
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

    return StackHelper.isValid(stack) && stack.hasTagCompound()
        && stack.getTagCompound().getBoolean(NBT_ENABLED);
  }

  public void setEnabled(ItemStack stack, boolean val) {

    if (StackHelper.isEmpty(stack))
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

    if (StackHelper.isEmpty(stack) || !stack.hasTagCompound()
        || !stack.getTagCompound().hasKey(NBT_BUFF_LIST))
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

    if (StackHelper.isEmpty(stack))
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

    if (StackHelper.isEmpty(stack))
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

    if (!enabled || totalDrain <= 0) {
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
  protected ActionResult<ItemStack> clOnItemRightClick(World world, EntityPlayer player,
      EnumHand hand) {

    ItemStack stack = player.getHeldItem(hand);
    // Enable/disable
    if (StackHelper.isValid(stack) && getCharge(stack) > 0) {
      setEnabled(stack, !isEnabled(stack));
      if (isEnabled(stack))
        applyEffects(stack, player);
      else
        removeEffects(stack, player);
    }

    return new ActionResult(EnumActionResult.SUCCESS, stack);
  }

  @Override
  public void clAddInformation(ItemStack stack, World world, List list, boolean advanced) {

    LocalizationHelper loc = SilentGems.localizationHelper;

    for (Entry<ChaosBuff, Integer> entry : getBuffs(stack).entrySet())
      list.add(TextFormatting.GOLD + entry.getKey().getLocalizedName(entry.getValue()));

    int slotsUsed = getSlotsUsed(stack);
    int totalDrain = getTotalChargeDrain(stack, SilentGems.proxy.getClientPlayer());
    list.add(loc.getItemSubText(itemName, "charge", getCharge(stack), getMaxCharge(stack)));
    list.add(loc.getItemSubText(itemName, "slots", slotsUsed, MAX_SLOTS));
    list.add(loc.getItemSubText(itemName, "drain", totalDrain));
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
  protected void clGetSubItems(Item item, CreativeTabs tab, List<ItemStack> list) {

    if (!ItemHelper.isInCreativeTab(item, tab))
      return;

    for (int i = 0; i < subItemCount; ++i) {
      // Empty
      ItemStack stack1 = new ItemStack(item, 1, i);
      list.add(stack1);
      // Full
      if (getMaxCharge(stack1) > 0) {
        ItemStack stack2 = StackHelper.safeCopy(stack1);
        receiveCharge(stack2, getMaxCharge(stack2), false);
        list.add(stack2);
      }
    }
  }

  // ===================
  // = Baubles support =
  // ===================

  @Override
  @Optional.Method(modid = BaublesCompat.MOD_ID)
  public BaubleType getBaubleType(ItemStack stack) {

    return BaubleType.BELT;
  }

  @Override
  @Optional.Method(modid = BaublesCompat.MOD_ID)
  public void onWornTick(ItemStack stack, EntityLivingBase player) {

    onUpdate(stack, player.world, player, 0, false);
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
      float scale = 0.5f;
      GlStateManager.scale(scale, scale, scale);
      IRenderBauble.Helper.rotateIfSneaking(player);
      GlStateManager.rotate(90, 0, 1, 0);
      IRenderBauble.Helper.translateToChest();
      GlStateManager.translate(0.0, 1.7, 0.3);
      Minecraft.getMinecraft().getRenderItem().renderItem(stack, TransformType.FIXED);
    }
  }

  // ==============
  // = HUD render =
  // ==============

  public static final class Gui {

    public static final ResourceLocation TEXTURE_FRAME = new ResourceLocation(SilentGems.MODID,
        "textures/gui/chaosbarframe.png");
    public static final ResourceLocation TEXTURE_BAR = new ResourceLocation(SilentGems.MODID,
        "textures/gui/chaosbar.png");

    public static final int BAR_WIDTH = 40;
    public static final int BAR_HEIGHT = 8;

    @SideOnly(Side.CLIENT)
    public static void renderGameOverlay(Minecraft mc) {

      EntityPlayer player = mc.player;
      List<ItemStack> gems = Lists.newArrayList();
      ItemChaosGem chaosGem = ModItems.chaosGem;

      List<ItemStack> playerInventory = PlayerHelper.getNonEmptyStacks(player);
      playerInventory
          .addAll(BaublesCompat.getBaubles(player, s -> s.getItem() instanceof ItemChaosGem));
      for (ItemStack stack : playerInventory)
        if (StackHelper.isValid(stack) && stack.getItem() instanceof ItemChaosGem)
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

        posX = (int) (res.getScaledWidth() / scale * 0.025f);
        posY = (int) (res.getScaledHeight() / scale * 0.05f + index * (BAR_HEIGHT + 2));

        // Bar
        GL11.glColor4f(red, green, blue, 0.5f);
        mc.renderEngine.bindTexture(TEXTURE_BAR);
        int barPosWidth = (int) (BAR_WIDTH * storedFraction);
        int barPosX = posX;
        int barPosHeight = BAR_HEIGHT;
        int barPosY = posY;
        drawRect(barPosX, barPosY, 0, 0, barPosWidth, barPosHeight);

        // Bar frame
        GL11.glColor3f(1f, 1f, 1f);
        mc.renderEngine.bindTexture(TEXTURE_FRAME);
        drawRect(posX, posY, 0, 0, BAR_WIDTH, BAR_HEIGHT);

        GL11.glEnable(GL11.GL_BLEND);
        GL11.glPopMatrix();

        // Render charge level/percentage?
        GL11.glPushMatrix();

        GL11.glColor3f(1f, 1f, 1f);
        scale = 0.7f;
        GL11.glScalef(scale, scale, 1f);

        FontRenderer fontRender = mc.fontRenderer;
        String format = "%.2f%%";
        String str = String.format(format, storedFraction * 100f);
        posX = (int) (res.getScaledWidth() / scale * 0.025f + 3);
        posY = (int) (res.getScaledHeight() / scale * 0.05f + index * (BAR_HEIGHT + 2) / scale + 1);
        fontRender.drawStringWithShadow(str, posX, posY, 0xFFFFFF);

        GL11.glPopMatrix();

        ++index;
      }
    }

    private static void drawRect(float x, float y, float u, float v, float width, float height) {

      Tessellator tess = Tessellator.getInstance();
      BufferBuilderSL buff = BufferBuilderSL.INSTANCE.acquireBuffer(tess);
      buff.begin(7, DefaultVertexFormats.POSITION_TEX);
      buff.pos(x, y + height, 0).tex(0, 1).endVertex();
      buff.pos(x + width, y + height, 0).tex(1, 1).endVertex();
      buff.pos(x + width, y, 0).tex(1, 0).endVertex();
      buff.pos(x, y, 0).tex(0, 0).endVertex();
      tess.draw();
    }
  }
}
