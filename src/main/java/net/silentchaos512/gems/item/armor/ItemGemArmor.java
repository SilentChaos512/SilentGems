package net.silentchaos512.gems.item.armor;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.common.ISpecialArmor;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.silentchaos512.gems.SilentGems;
import net.silentchaos512.gems.api.IArmor;
import net.silentchaos512.gems.api.lib.ArmorPartPosition;
import net.silentchaos512.gems.api.lib.EnumMaterialGrade;
import net.silentchaos512.gems.api.lib.EnumMaterialTier;
import net.silentchaos512.gems.api.tool.part.ToolPart;
import net.silentchaos512.gems.api.tool.part.ToolPartRegistry;
import net.silentchaos512.gems.client.gui.ModelGemArmor;
import net.silentchaos512.gems.client.key.KeyTracker;
import net.silentchaos512.gems.config.GemsConfig;
import net.silentchaos512.gems.init.ModItems;
import net.silentchaos512.gems.item.ToolRenderHelper;
import net.silentchaos512.gems.lib.soul.ToolSoul;
import net.silentchaos512.gems.util.ArmorHelper;
import net.silentchaos512.gems.util.SoulManager;
import net.silentchaos512.gems.util.ToolHelper;
import net.silentchaos512.lib.item.ItemArmorSL;
import net.silentchaos512.lib.registry.RecipeMaker;
import net.silentchaos512.lib.util.ItemHelper;
import net.silentchaos512.lib.util.LocalizationHelper;
import net.silentchaos512.lib.util.StackHelper;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ItemGemArmor extends ItemArmorSL implements ISpecialArmor, IArmor {

  // sum = 1, starts with boots
  private static final float[] ABSORPTION_RATIO_BY_SLOT = { 0.175f, 0.3f, 0.4f, 0.125f };
  // Screw access transformers, they don't work half the time!
  private static final int[] MAX_DAMAGE_ARRAY = new int[]{13, 15, 16, 11};
  private static final UUID[] ARMOR_MODIFIERS = new UUID[]{UUID.fromString("845DB27C-C624-495F-8C9F-6020A9A58B6B"), UUID.fromString("D8499B04-0E66-4726-AB29-64469D734E0D"), UUID.fromString("9F3D476D-C118-4544-8365-64846904B48E"), UUID.fromString("2AD3F246-FEE1-4E67-B886-69FD380BB150")};

  private List<ItemStack> subItems = null;
  public final EntityEquipmentSlot type;

  public ItemGemArmor(int renderIndexIn, EntityEquipmentSlot equipmentSlotIn, String name) {

    super(SilentGems.MODID, name, ArmorMaterial.DIAMOND, renderIndexIn, equipmentSlotIn);
    this.type = equipmentSlotIn;
    setNoRepair();
  }

  public ItemStack constructArmor(EnumMaterialTier tier, ItemStack material) {

    ItemStack frame = ModItems.armorFrame.getFrameForArmorPiece(armorType, tier);
    return constructArmor(frame, material, material, material, material);
  }

  @Override
  public ItemStack constructArmor(ItemStack frame, ItemStack... materials) {

    return ArmorHelper.constructArmor(this, frame, materials);
  }

  public float getProtection(ItemStack armor) {

    return ABSORPTION_RATIO_BY_SLOT[armorType.getIndex()] * ArmorHelper.getProtection(armor);
  }

  public float getToughness(ItemStack stack) {

    float durability = ArmorHelper.getMaxDamage(stack) / 1536f;
    float protection = ArmorHelper.getProtection(stack) / 20f;
    float value = durability + protection - 0.8f;
    return MathHelper.clamp(value, 0f, 4f);
  }

  @Override
  public int getMaxDamage(ItemStack stack) {

    EnumMaterialTier tier = ToolHelper.getToolTier(stack);
    int x = ArmorHelper.getMaxDamage(stack);
    float y = (1.8f * x + 1515) / 131;
    float z = tier != null ? y * (tier.ordinal() + 1f) / 2f : y;
    return (int) (MAX_DAMAGE_ARRAY[armorType.getIndex()] * z);
  }

  public static int getPlayerTotalGemArmorValue(EntityLivingBase player) {

    float total = 0;
    for (ItemStack armor : player.getArmorInventoryList()) {
      if (StackHelper.isValid(armor)) {
        if (armor.getItem() instanceof ItemGemArmor) {
          if (!ToolHelper.isBroken(armor)) {
            total += ((ItemGemArmor) armor.getItem()).getProtection(armor);
          }
        } else if (armor.getItem() instanceof ItemArmor) {
          total += ((ItemArmor) armor.getItem()).damageReduceAmount;
        }
      }
    }
    return Math.round(total);
  }

  @Override
  public ArmorProperties getProperties(EntityLivingBase player, ItemStack armor,
      DamageSource source, double damage, int slot) {

    if (ToolHelper.isBroken(armor)) {
      return new ArmorProperties(0, 0.0, 0);
    }

    // TODO: Special protection, like fall damage?
    if (source.isUnblockable()) {
      return new ArmorProperties(0, 1, 0);
    }

    // Basing ratios on total armor value.
    float protection = MathHelper.clamp(getPlayerTotalGemArmorValue(player), 0,
        ArmorHelper.PROTECTION_CAP); // Capping total to 39.

    float ratio = ABSORPTION_RATIO_BY_SLOT[armorType.getIndex()];
    if (protection <= 20) {
      // 80% max (20 armor points) = diamond armor
      ratio *= protection / 25f;
    } else {
      // 40 armor points = invincible
      ratio *= MathHelper.clamp(protection / 100f, 0f, 0.98f) + 0.6f;
    }
    int durabilityLeft = armor.getMaxDamage() - armor.getItemDamage();

    ArmorProperties prop = new ArmorProperties(0, ratio, durabilityLeft - 1);
    // prop.Toughness = getToughness(armor);
    return prop;
  }

  @Override
  public int getArmorDisplay(EntityPlayer player, ItemStack armor, int slot) {

    // Handled by attribute modifiers.
    return 0;
    // return Math.round(getProtection(armor));
  }

  @Override
  public void damageArmor(EntityLivingBase entity, ItemStack stack, DamageSource source, int damage,
      int slot) {

    if (ToolHelper.isBroken(stack)) {
      return;
    }

    if (entity instanceof EntityPlayer) {
      ToolSoul soul = SoulManager.getSoul(stack);
      if (soul != null) {
        soul.addXp((int) (ToolSoul.XP_FACTOR_ARMOR_DAMAGED * damage), stack, (EntityPlayer) entity);
      }
    }

    // int amount = damage - (int) (getToughness(stack) * SilentGems.random.nextFloat());
    // int durabilityLeft = stack.getMaxDamage() - stack.getItemDamage();
    // amount = amount < 0 ? 0 : (amount > durabilityLeft ? durabilityLeft : amount);
    // EntityPlayer player = entity instanceof EntityPlayer ? (EntityPlayer) entity : null;
    // ToolHelper.attemptDamageTool(stack, amount, player);
  }

  @Override
  public Multimap<String, AttributeModifier> getAttributeModifiers(EntityEquipmentSlot slot,
      ItemStack stack) {

    Multimap<String, AttributeModifier> multimap = HashMultimap.create();

    if (slot == this.armorType && !ToolHelper.isBroken(stack)) {
      multimap.put(SharedMonsterAttributes.ARMOR.getName(), new AttributeModifier(
          ARMOR_MODIFIERS[slot.getIndex()], "Armor modifier", getProtection(stack), 0));
      multimap.put(SharedMonsterAttributes.ARMOR_TOUGHNESS.getName(), new AttributeModifier(
          ARMOR_MODIFIERS[slot.getIndex()], "Armor toughness", getToughness(stack), 0));
    }

    return multimap;
  }

  @Nonnull
  @Override
  public String getArmorTexture(ItemStack stack, Entity entity, EntityEquipmentSlot slot,
      String type) {

    // FIXME
    return SilentGems.RESOURCE_PREFIX + "textures/armor/temparmor.png";
    // return SilentGems.RESOURCE_PREFIX + "textures/armor/gemarmor_" + (slot == EntityEquipmentSlot.LEGS ? "2" : "1") +
    // ".png";
  }

  // FIXME
  @Override
  @SideOnly(Side.CLIENT)
  public ModelBiped getArmorModel(EntityLivingBase entity, ItemStack itemStack,
      EntityEquipmentSlot slot, ModelBiped original) {

    ModelGemArmor model = ModelGemArmor.getModel(ArmorHelper.getRenderColorList(itemStack));
    if (model != null) {
      model.setModelAttributes(original);
      return model;
    }
    return super.getArmorModel(entity, itemStack, slot, original);
  }

  @Override
  public boolean hasEffect(ItemStack stack) {

    return false;
  }

  @Override
  public boolean getIsRepairable(ItemStack toRepair, ItemStack repair) {

    return ToolHelper.getIsRepairable(toRepair, repair);
  }

  @Override
  public int getItemEnchantability(ItemStack stack) {

    return ArmorHelper.getItemEnchantability(stack);
  }

  @Override
  public void onUpdate(ItemStack armor, World world, Entity entity, int itemSlot,
      boolean isSelected) {

    ToolHelper.onUpdate(armor, world, entity, itemSlot, isSelected);
  }

  @Override
  public void clAddInformation(ItemStack stack, World world, List<String> list, boolean advanced) {

    LocalizationHelper loc = SilentGems.localizationHelper;
    ToolRenderHelper helper = ToolRenderHelper.getInstance();
    boolean controlDown = KeyTracker.isControlDown();
    boolean shiftDown = KeyTracker.isShiftDown();
    boolean altDown = KeyTracker.isAltDown();
    String line;

    // UUID
    if (GemsConfig.DEBUG_MODE && controlDown && shiftDown) {
      UUID uuid = ToolHelper.hasUUID(stack) ? ToolHelper.getUUID(stack) : null;
      list.add(uuid == null ? "No UUID" : uuid.toString());
      uuid = ToolHelper.getSoulUUID(stack);
      list.add(uuid == null ? "No Soul UUID" : uuid.toString());
    }

    // Tool Soul
    ToolSoul soul = SoulManager.getSoul(stack);
    if (soul != null) {
      soul.addInformation(stack, world, list, advanced);
    }

    // Show original owner?
    if (controlDown) {
      String owner = ArmorHelper.getOriginalOwner(stack);
      if (!owner.isEmpty()) {
        list.add(loc.getMiscText("Tooltip.OriginalOwner", owner));
      } else {
        list.add(loc.getMiscText("Tooltip.OriginalOwner.Unknown"));
      }
    }

    // TODO: Remove me
    if (altDown) {
      list.add(TextFormatting.RED + "Armor models WIP.");
    }

    // Broken?
    if (ArmorHelper.isBroken(stack)) {
      list.add(loc.getMiscText("Tooltip.Broken"));
    }

    final String sep = loc.getMiscText("Tooltip.Separator");

    if (controlDown) {
      // Properties header
      list.add(loc.getMiscText("Tooltip.Properties"));

      TextFormatting color = TextFormatting.YELLOW;
      float durabilityBoost = ToolSoul.getDurabilityModifierForDisplay(soul);
      list.add(color + helper.getTooltipLine("Durability", getMaxDamage(stack), durabilityBoost));
      float protectionBoost = ToolSoul.getProtectionModifierForDisplay(soul);
      list.add(color + helper.getTooltipLine("Protection", getProtection(stack), protectionBoost));

      // Statistics Header
      list.add(sep);
      list.add(loc.getMiscText("Tooltip.Statistics"));

      list.add(helper.getTooltipLine("DamageTaken", ArmorHelper.getStatDamageTaken(stack), 0f));
      list.add(helper.getTooltipLine("Redecorated", ArmorHelper.getStatRedecorated(stack), 0f));

      list.add(sep);
    } else {
      list.add(TextFormatting.GOLD + loc.getMiscText("PressCtrl"));
    }

    if (altDown) {
      list.add(loc.getMiscText("Tooltip.Construction"));

      ToolPart[] parts = ArmorHelper.getConstructionParts(stack);
      EnumMaterialGrade[] grades = ArmorHelper.getConstructionGrades(stack);

      for (int i = 0; i < parts.length; ++i) {
        ToolPart part = parts[i];
        EnumMaterialGrade grade = grades[i];

        line = "  " + TextFormatting.YELLOW + part.getKey() + TextFormatting.GOLD + " (" + grade
            + ")";
        list.add(line);
      }
      ToolPart partFrame = ArmorHelper.getPart(stack, ArmorPartPosition.FRAME);
      if (partFrame != null)
        list.add("  " + TextFormatting.YELLOW + partFrame.getKey());
      list.add(sep);
    } else {
      list.add(TextFormatting.GOLD + loc.getMiscText("PressAlt"));
    }
  }

  @Override
  protected void clGetSubItems(Item item, CreativeTabs tab, List<ItemStack> list) {

    if (!ItemHelper.isInCreativeTab(item, tab)) {
      return;
    }

    if (subItems == null) {
      subItems = new ArrayList<>();

      ToolPartRegistry.getMains().forEach(part -> {
        if (!part.isBlacklisted(part.getCraftingStack())) {
          ItemStack armor = constructArmor(part.getTier(), part.getCraftingStack());
          armor.getTagCompound().setBoolean(ToolHelper.NBT_EXAMPLE_TOOL, true);
          subItems.add(armor);
        }
      });

      // Set maker name.
      for (ItemStack stack : subItems) {
        ArmorHelper.setOriginalOwner(stack, TextFormatting.LIGHT_PURPLE + "Creative");
        stack.getTagCompound().setBoolean(ToolHelper.NBT_EXAMPLE_TOOL, true);
      }
    }

    list.addAll(subItems);
  }

  @Override
  public void addRecipes(RecipeMaker recipes) {

    ToolHelper.addExampleRecipe(this, new String[] { " h ", "hah", " h " });
  }

  @Override
  public String getUnlocalizedName(ItemStack stack) {

    return "item.silentgems:" + itemName;
  }
}
