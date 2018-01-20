package net.silentchaos512.gems.client.gui;

import java.util.Map.Entry;
import java.util.Set;
import java.util.UUID;

import com.google.common.collect.Multimap;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemAxe;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemFishingRod;
import net.minecraft.item.ItemHoe;
import net.minecraft.item.ItemShears;
import net.minecraft.item.ItemShield;
import net.minecraft.item.ItemSpade;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.item.ItemTool;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.RenderTooltipEvent;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.silentchaos512.gems.SilentGems;
import net.silentchaos512.gems.api.ITool;
import net.silentchaos512.gems.api.lib.EnumMaterialTier;
import net.silentchaos512.gems.item.tool.ItemGemBow;
import net.silentchaos512.gems.item.tool.ItemGemShield;
import net.silentchaos512.gems.item.tool.ItemGemSickle;
import net.silentchaos512.gems.util.ToolHelper;
import net.silentchaos512.lib.util.StackHelper;

public class EquipmentTooltips extends Gui {

  public static final ResourceLocation TEXTURE = new ResourceLocation(SilentGems.MODID,
      "textures/gui/hud.png");

  private int lastWidth = 0;
  private boolean isTinkersLoaded;

  public EquipmentTooltips() {

    this.isTinkersLoaded = Loader.isModLoaded("tconstruct");
  }

  @SubscribeEvent
  public void onRenderTooltip(RenderTooltipEvent.PostText event) {

    ItemStack stack = event.getStack();
    Item item = stack.getItem();
    boolean isTinkersHarvestTool = isTinkersLoaded
        && item instanceof slimeknights.tconstruct.library.tools.AoeToolCore;
    boolean isTinkersWeapon = isTinkersLoaded
        && item instanceof slimeknights.tconstruct.library.tools.SwordCore;
    boolean isTinkersBow = isTinkersLoaded
        && item instanceof slimeknights.tconstruct.library.tools.ranged.BowCore;

    // Tools (pickaxes, shovels, axes, and more)
    if (item instanceof ItemTool || item instanceof ItemHoe || item instanceof ItemShears
        || item instanceof ItemFishingRod || isTinkersHarvestTool) {
      renderBackground(event);
      renderForTool(event, stack);
    }
    // Swords
    else if (item instanceof ItemSword || isTinkersWeapon) {
      boolean isCaster = item instanceof ITool
          && ToolHelper.getToolTier(stack).ordinal() >= EnumMaterialTier.SUPER.ordinal();
      renderBackground(event);
      renderForWeapon(event, stack);
    }
    // Bows
    else if (item instanceof ItemBow || isTinkersBow) {
      renderBackground(event);
      renderForBow(event, stack);
    }
    // Shields
    else if (item instanceof ItemShield) {
      renderBackground(event);
      renderForShield(event, stack);
    }
    // Armor
    else if (item instanceof ItemArmor) {
      renderBackground(event);
      renderForArmor(event, stack);
    }
    // Unknown
  }

  private void renderBackground(RenderTooltipEvent.PostText event) {

    final int backgroundColor = 0xC0100010;
    int left = event.getX() - 1;
    int top = event.getY() - 17;
    int right = Math.max(event.getX() + lastWidth, event.getX() + event.getWidth() + 1);
    int bottom = event.getY() - 4;
    drawRect(left, top, right, bottom, backgroundColor);
  }

  private void renderForTool(RenderTooltipEvent.PostText event, ItemStack stack) {

    Minecraft mc = Minecraft.getMinecraft();
    FontRenderer fontRenderer = event.getFontRenderer();
    ItemStack currentEquip = mc.player.getHeldItemMainhand();
    boolean isAxe = stack.getItem() instanceof ItemAxe;
    boolean isDurabilityOnly = !(stack.getItem() instanceof ItemTool);
    // boolean isHoe = stack.getItem() instanceof ItemHoe;
    // boolean isSickle = stack.getItem() instanceof ItemGemSickle;
    // boolean isShears = stack.getItem() instanceof ItemShears;
    // boolean isFishingRod = stack.getItem() instanceof ItemFishingRod;

    double scale = 0.75;
    int x = (int) (event.getX() / scale);
    int y = (int) ((event.getY() - 16) / scale);

    int durability = getDurability(stack, 0);
    int equippedDurability = getDurability(currentEquip, durability);
    int harvestLevel = getHarvestLevel(stack, 0);
    int equippedHarvestLevel = getHarvestLevel(currentEquip, harvestLevel);
    float harvestSpeed = getHarvestSpeed(stack, 0);
    float equippedHarvestSpeed = getHarvestSpeed(currentEquip, harvestSpeed);
    float meleeDamage = getMeleeDamage(stack, 0);
    float equippedMeleeDamage = getMeleeDamage(currentEquip, meleeDamage);
    float meleeSpeed = getMeleeSpeed(stack, 0);
    float equippedMeleeSpeed = getMeleeSpeed(currentEquip, meleeSpeed);

    GlStateManager.pushMatrix();
    GlStateManager.color(1f, 1f, 1f, 1f);
    GlStateManager.scale(scale, scale, scale);

    mc.renderEngine.bindTexture(TEXTURE);

    boolean currentIsDurabilityOnly = currentEquip.getItem() instanceof ItemHoe
        || currentEquip.getItem() instanceof ItemGemSickle
        || currentEquip.getItem() instanceof ItemFishingRod;
    boolean bothWeapons = (isWeapon(stack) || stack.getItem() instanceof ItemTool)
        && (isWeapon(currentEquip) || currentEquip.getItem() instanceof ItemTool);

    // Durability
    x = renderStat(mc, fontRenderer, 0, x, y, durability, equippedDurability,
        StackHelper.isValid(currentEquip));
    // Harvest Level
    if (!isAxe && !isDurabilityOnly && harvestLevel > -1) {
      x = renderStat(mc, fontRenderer, 1, x, y, harvestLevel, equippedHarvestLevel,
          !currentIsDurabilityOnly && currentEquip.getItem() instanceof ItemTool);
    }
    // Harvest Speed
    if (!isDurabilityOnly && harvestSpeed > 0)
      x = renderStat(mc, fontRenderer, 2, x, y, harvestSpeed, equippedHarvestSpeed,
          !currentIsDurabilityOnly && currentEquip.getItem() instanceof ItemTool);
    // Melee Damage and Speed
    if (isAxe) {
      x = renderStat(mc, fontRenderer, 3, x, y, meleeDamage, equippedMeleeDamage, bothWeapons);
      x = renderStat(mc, fontRenderer, 5, x, y, meleeSpeed, equippedMeleeSpeed, bothWeapons);
    }
    lastWidth = (int) (x * scale - event.getX());

    GlStateManager.popMatrix();
  }

  private void renderForWeapon(RenderTooltipEvent.PostText event, ItemStack stack) {

    Minecraft mc = Minecraft.getMinecraft();
    FontRenderer fontRenderer = event.getFontRenderer();
    ItemStack currentEquip = mc.player.getHeldItemMainhand();

    double scale = 0.75;
    int x = (int) (event.getX() / scale);
    int y = (int) ((event.getY() - 16) / scale);

    int durability = getDurability(stack, 0);
    int equippedDurability = getDurability(currentEquip, durability);
    float meleeDamage = getMeleeDamage(stack, 0);
    float equippedMeleeDamage = getMeleeDamage(currentEquip, meleeDamage);
    float magicDamage = getMagicDamage(stack, 0);
    float equippedMagicDamage = getMagicDamage(currentEquip, magicDamage);
    float meleeSpeed = getMeleeSpeed(stack, 0);
    float equippedMeleeSpeed = getMeleeSpeed(currentEquip, meleeSpeed);

    GlStateManager.pushMatrix();
    GlStateManager.color(1f, 1f, 1f, 1f);
    GlStateManager.scale(scale, scale, scale);

    mc.renderEngine.bindTexture(TEXTURE);

    boolean bothWeapons = (isWeapon(stack) || stack.getItem() instanceof ItemTool)
        && (isWeapon(currentEquip) || currentEquip.getItem() instanceof ItemTool);
    boolean eitherIsCaster = bothWeapons && (isCaster(stack) || isCaster(currentEquip));

    // Durability
    x = renderStat(mc, fontRenderer, 0, x, y, durability, equippedDurability,
        StackHelper.isValid(currentEquip));
    // Melee Damage
    x = renderStat(mc, fontRenderer, 3, x, y, meleeDamage, equippedMeleeDamage, bothWeapons);
    // Magic Damage
    if (magicDamage > 0 || equippedMagicDamage > 0)
      x = renderStat(mc, fontRenderer, 4, x, y, magicDamage, equippedMagicDamage, eitherIsCaster);
    // Melee Speed
    x = renderStat(mc, fontRenderer, 5, x, y, meleeSpeed, equippedMeleeSpeed, bothWeapons);
    lastWidth = (int) (x * scale - event.getX());

    GlStateManager.popMatrix();
  }

  private void renderForBow(RenderTooltipEvent.PostText event, ItemStack stack) {

    Minecraft mc = Minecraft.getMinecraft();
    FontRenderer fontRenderer = event.getFontRenderer();
    ItemStack currentEquip = mc.player.getHeldItemMainhand();
    boolean itemsComparable = stack.getItem() instanceof ItemBow
        && currentEquip.getItem() instanceof ItemBow;

    double scale = 0.75;
    int x = (int) (event.getX() / scale);
    int y = (int) ((event.getY() - 16) / scale);

    int durability = getDurability(stack, 0);
    int equippedDurability = getDurability(currentEquip, durability);
    // We don't have anything to get stats for other mods' bows, do we? Guess we have to assume vanilla stats.
    // Arrow damage: display actual damage
    float arrowDamage = stack.getItem() instanceof ItemGemBow
        ? 2f + ((ItemGemBow) stack.getItem()).getArrowDamage(stack)
        : 2f;
    float equippedArrowDamage = currentEquip.getItem() instanceof ItemGemBow
        ? 2f + ((ItemGemBow) currentEquip.getItem()).getArrowDamage(currentEquip)
        : (currentEquip.getItem() instanceof ItemBow ? 2f : arrowDamage);
    // Draw speed: display the draw speed value used in tooltips.
    float drawSpeed = stack.getItem() instanceof ItemGemBow
        ? ((ItemGemBow) stack.getItem()).getDrawSpeedForDisplay(stack)
        : 1f;
    float equippedDrawSpeed = currentEquip.getItem() instanceof ItemGemBow
        ? ((ItemGemBow) currentEquip.getItem()).getDrawSpeedForDisplay(currentEquip)
        : (currentEquip.getItem() instanceof ItemBow ? 1f : drawSpeed);

    GlStateManager.pushMatrix();
    GlStateManager.color(1f, 1f, 1f, 1f);
    GlStateManager.scale(scale, scale, scale);

    mc.renderEngine.bindTexture(TEXTURE);

    // Durability
    x = renderStat(mc, fontRenderer, 0, x, y, durability, equippedDurability,
        StackHelper.isValid(currentEquip));
    // Arrow Damage
    x = renderStat(mc, fontRenderer, 8, x, y, arrowDamage, equippedArrowDamage, itemsComparable);
    // Draw Speed
    x = renderStat(mc, fontRenderer, 5, x, y, drawSpeed, equippedDrawSpeed, itemsComparable);
    lastWidth = (int) (x * scale - event.getX());

    GlStateManager.popMatrix();
  }

  private void renderForShield(RenderTooltipEvent.PostText event, ItemStack stack) {

    Minecraft mc = Minecraft.getMinecraft();
    FontRenderer fontRenderer = event.getFontRenderer();
    ItemStack currentEquip = mc.player.getHeldItemMainhand();

    double scale = 0.75;
    int x = (int) (event.getX() / scale);
    int y = (int) ((event.getY() - 16) / scale);

    int durability = getDurability(stack, 0);
    int equippedDurability = getDurability(currentEquip, durability);
    float magicProtection = stack.getItem() instanceof ItemGemShield
        ? 100f * ToolHelper.getMagicProtection(stack)
        : 0f;
    float equippedMagicProtection = currentEquip.getItem() instanceof ItemGemShield
        ? 100f * ToolHelper.getMagicProtection(currentEquip)
        : 0f;

    boolean bothAreShield = stack.getItem() instanceof ItemShield
        && currentEquip.getItem() instanceof ItemShield;

    GlStateManager.pushMatrix();
    GlStateManager.color(1f, 1f, 1f, 1f);
    GlStateManager.scale(scale, scale, scale);

    mc.renderEngine.bindTexture(TEXTURE);

    // Durability
    x = renderStat(mc, fontRenderer, 0, x, y, durability, equippedDurability,
        StackHelper.isValid(currentEquip));
    x = renderStat(mc, fontRenderer, 9, x, y, magicProtection, equippedMagicProtection,
        bothAreShield);
    lastWidth = (int) (x * scale - event.getX());

    GlStateManager.popMatrix();
  }

  private void renderForArmor(RenderTooltipEvent.PostText event, ItemStack stack) {

    Minecraft mc = Minecraft.getMinecraft();
    FontRenderer fontRenderer = event.getFontRenderer();

    ItemArmor itemArmor = (ItemArmor) stack.getItem();
    ItemStack currentEquip = StackHelper.empty();
    for (ItemStack itemstack : mc.player.getEquipmentAndArmor()) {
      Item item = itemstack.getItem();
      if (item instanceof ItemArmor && ((ItemArmor) item).armorType == itemArmor.armorType) {
        currentEquip = itemstack;
      }
    }

    double scale = 0.75;
    int x = (int) (event.getX() / scale);
    int y = (int) ((event.getY() - 16) / scale);

    int durability = getDurability(stack, 0);
    int equippedDurability = getDurability(currentEquip, durability);
    float protection = getProtection(stack, 0);
    float equippedProtection = getProtection(currentEquip, protection);
    float toughness = getToughness(stack, 0);
    float equippedToughness = getToughness(currentEquip, toughness);

    GlStateManager.pushMatrix();
    GlStateManager.color(1f, 1f, 1f, 1f);
    GlStateManager.scale(scale, scale, scale);

    mc.renderEngine.bindTexture(TEXTURE);

    // Durability
    x = renderStat(mc, fontRenderer, 0, x, y, durability, equippedDurability,
        StackHelper.isValid(currentEquip));
    // Protection
    x = renderStat(mc, fontRenderer, 6, x, y, protection, equippedProtection,
        StackHelper.isValid(currentEquip));
    // Toughness
    if (toughness > 0 || equippedToughness > 0)
      x = renderStat(mc, fontRenderer, 7, x, y, toughness, equippedToughness,
          StackHelper.isValid(currentEquip));
    lastWidth = (int) (x * scale - event.getX());

    GlStateManager.popMatrix();
  }

  private int renderStat(Minecraft mc, FontRenderer fontRenderer, int statIconIndex, int x, int y,
      float currentStat, float equippedStat, boolean isComparable) {

    mc.renderEngine.bindTexture(TEXTURE);

    // Draw stat icon
    drawTexturedModalRect(x, y, 16 * statIconIndex, 240, 16, 16);
    x += 18;

    // Draw stat value
    String text = formatStat(currentStat);
    fontRenderer.drawStringWithShadow(text, x, y + 5, 0xFFFFFF);
    x += fontRenderer.getStringWidth(text);
    mc.renderEngine.bindTexture(TEXTURE);

    // Draw comparison arrow (if appropriate)
    if (!isComparable) {
      x += 5;
    } else if (currentStat > equippedStat) {
      // Up arrow
      drawTexturedModalRect(x, y, 224, 240, 16, 16);
      x += 18;
    } else if (currentStat < equippedStat) {
      // Down arrow
      drawTexturedModalRect(x, y, 240, 240, 16, 16);
      x += 18;
    } else {
      // Dash
      drawTexturedModalRect(x, y, 208, 240, 16, 16);
      x += 18;
    }

    return x;
  }

  public static final String FORMAT_INT = "%d";
  public static final String FORMAT_FLOAT = "%.2f";

  private String formatStat(float value) {

    if (value == (int) value)
      return String.format(FORMAT_INT, (int) value);
    return String.format(FORMAT_FLOAT, value);
  }

  /***************
   * Stat Getters
   ***************/

  private int getDurability(ItemStack stack, int defaultValue) {

    if (StackHelper.isEmpty(stack) || !stack.isItemStackDamageable())
      return defaultValue;
    return stack.getMaxDamage();
  }

  private int getHarvestLevel(ItemStack stack, int defaultValue) {

    Item item = stack.getItem();
    if (StackHelper.isEmpty(stack))
      return defaultValue;

    // Tinkers tools?
    if (isTinkersLoaded && item instanceof slimeknights.tconstruct.library.tools.ToolCore)
      return slimeknights.tconstruct.library.utils.ToolHelper.getHarvestLevelStat(stack);

    if (!(item instanceof ItemTool))
      return defaultValue;

    ItemTool itemTool = (ItemTool) item;
    IBlockState state = getBlockForTool(stack);
    int maxLevel = -1;
    // This doesn't work with all modded tools, but most.
    for (String toolClass : itemTool.getToolClasses(stack)) {
      int harvestLevel = itemTool.getHarvestLevel(stack, toolClass, null, state);
      maxLevel = Math.max(maxLevel, harvestLevel);
    }
    return maxLevel;
  }

  private float getHarvestSpeed(ItemStack stack, float defaultValue) {

    Item item = stack.getItem();
    if (StackHelper.isEmpty(stack))
      return defaultValue;

    if (isTinkersLoaded && item instanceof slimeknights.tconstruct.library.tools.ToolCore)
      return slimeknights.tconstruct.library.utils.ToolHelper.getMiningSpeedStat(stack);

    if (!(item instanceof ItemTool))
      return 0;

    // Get an appropriate blockstate for the tool (assume stone if class is unknown).
    IBlockState state = getBlockForTool(stack);

    try {
      return item.getStrVsBlock(stack, state);
    } catch (NullPointerException ex) {
      return 0;
    }
  }

  private IBlockState getBlockForTool(ItemStack stack) {

    IBlockState state;
    Item item = stack.getItem();
    Set<String> toolClasses = item.getToolClasses(stack);

    if (item instanceof ItemSpade || toolClasses.contains("shovel"))
      state = Blocks.DIRT.getDefaultState();
    else if (item instanceof ItemAxe || toolClasses.contains("axe"))
      state = Blocks.LOG.getDefaultState();
    else
      state = Blocks.STONE.getDefaultState();
    return state;
  }

  protected static final UUID ATTACK_DAMAGE_MODIFIER = UUID
      .fromString("CB3F55D3-645C-4F38-A497-9C13A33DB5CF");
  protected static final UUID ATTACK_SPEED_MODIFIER = UUID
      .fromString("FA233E1C-4180-4865-B01B-BCCE9785ACA3");

  private float getMeleeDamage(ItemStack stack, float defaultValue) {

    if (StackHelper.isEmpty(stack))
      return defaultValue;

    Multimap<String, AttributeModifier> multimap = stack
        .getAttributeModifiers(EntityEquipmentSlot.MAINHAND);

    for (Entry<String, AttributeModifier> entry : multimap.entries()) {
      AttributeModifier mod = entry.getValue();
      if (mod.getID().equals(ATTACK_DAMAGE_MODIFIER)) {
        return (float) mod.getAmount() + 1f;
      }
    }

    return 0f;
  }

  private float getMagicDamage(ItemStack stack, float defaultValue) {

    Item item = stack.getItem();
    if (item instanceof ITool && ((ITool) item).isCaster(stack)) {
      return ((ITool) item).getMagicDamage(stack) + 1f;
    }
    return 0f;
  }

  private float getMeleeSpeed(ItemStack stack, float defaultValue) {

    if (StackHelper.isEmpty(stack))
      return defaultValue;

    Multimap<String, AttributeModifier> multimap = stack
        .getAttributeModifiers(EntityEquipmentSlot.MAINHAND);

    for (Entry<String, AttributeModifier> entry : multimap.entries()) {
      AttributeModifier mod = entry.getValue();
      if (mod.getID().equals(ATTACK_SPEED_MODIFIER)) {
        return (float) mod.getAmount() + 4f;
      }
    }

    return 0f;
  }

  private float getProtection(ItemStack stack, float defaultValue) {

    if (StackHelper.isEmpty(stack))
      return defaultValue;

    ItemArmor itemArmor = (ItemArmor) stack.getItem();
    EntityEquipmentSlot slot = itemArmor.armorType;
    UUID uuid = ItemArmor.ARMOR_MODIFIERS[slot.getIndex()];

    Multimap<String, AttributeModifier> multimap = stack.getAttributeModifiers(slot);

    for (Entry<String, AttributeModifier> entry : multimap.entries()) {
      String key = entry.getKey();
      AttributeModifier mod = entry.getValue();
      if (key.equals(SharedMonsterAttributes.ARMOR.getName()) && mod.getID().equals(uuid)) {
        return (float) mod.getAmount();
      }
    }

    return 0f;
  }

  private float getToughness(ItemStack stack, float defaultValue) {

    if (StackHelper.isEmpty(stack))
      return defaultValue;

    ItemArmor itemArmor = (ItemArmor) stack.getItem();
    EntityEquipmentSlot slot = itemArmor.armorType;
    UUID uuid = ItemArmor.ARMOR_MODIFIERS[slot.getIndex()];

    Multimap<String, AttributeModifier> multimap = stack.getAttributeModifiers(slot);

    for (Entry<String, AttributeModifier> entry : multimap.entries()) {
      String key = entry.getKey();
      AttributeModifier mod = entry.getValue();
      if (key.equals(SharedMonsterAttributes.ARMOR_TOUGHNESS.getName())
          && mod.getID().equals(uuid)) {
        return (float) mod.getAmount();
      }
    }

    return 0f;
  }

  private boolean isWeapon(ItemStack stack) {

    return stack.getItem() instanceof ItemSword || stack.getItem() instanceof ItemAxe
        || (isTinkersLoaded
            && stack.getItem() instanceof slimeknights.tconstruct.library.tools.SwordCore);
  }

  private boolean isCaster(ItemStack stack) {

    return stack.getItem() instanceof ITool && ((ITool) stack.getItem()).isCaster(stack);
  }
}
