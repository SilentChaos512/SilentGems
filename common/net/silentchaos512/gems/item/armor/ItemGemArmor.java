package net.silentchaos512.gems.item.armor;

import java.lang.reflect.Field;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.annotation.Nonnull;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;

import net.minecraft.client.model.ModelBiped;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.common.ISpecialArmor;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.silentchaos512.gems.SilentGems;
import net.silentchaos512.gems.api.IArmor;
import net.silentchaos512.gems.api.lib.EnumMaterialGrade;
import net.silentchaos512.gems.api.tool.part.ToolPart;
import net.silentchaos512.gems.api.tool.part.ToolPartRegistry;
import net.silentchaos512.gems.client.gui.ModelGemArmor;
import net.silentchaos512.gems.client.key.KeyTracker;
import net.silentchaos512.gems.init.ModItems;
import net.silentchaos512.gems.item.ToolRenderHelper;
import net.silentchaos512.gems.lib.EnumGem;
import net.silentchaos512.gems.network.NetworkHandler;
import net.silentchaos512.gems.network.message.MessageItemRename;
import net.silentchaos512.gems.util.ArmorHelper;
import net.silentchaos512.gems.util.ToolHelper;
import net.silentchaos512.lib.item.ItemArmorSL;
import net.silentchaos512.lib.registry.RecipeMaker;
import net.silentchaos512.lib.util.ItemHelper;
import net.silentchaos512.lib.util.LocalizationHelper;
import net.silentchaos512.lib.util.StackHelper;

public class ItemGemArmor extends ItemArmorSL implements ISpecialArmor, IArmor {

  public static final float[] ABSORPTION_RATIO_BY_SLOT = { 0.175f, 0.3f, 0.4f, 0.125f }; // sum = 1, starts with boots
  public static final int[] MAX_DAMAGE_ARRAY = new int[] { 13, 15, 16, 11 }; // Copied from ItemArmor
  protected static UUID[] ARMOR_MODIFIERS;
  public static final boolean HAS_EFFECT = false; // Set true for enchanted glow.

  static {

    try {
      Field field = ItemArmor.class.getDeclaredField("ARMOR_MODIFIERS");
      field.setAccessible(true);
      ARMOR_MODIFIERS = (UUID[]) field.get(null);
    } catch (Exception ex) {
      ARMOR_MODIFIERS = new UUID[] { UUID.fromString("845DB27C-C624-495F-8C9F-6020A9A58B6B"),
          UUID.fromString("D8499B04-0E66-4726-AB29-64469D734E0D"),
          UUID.fromString("9F3D476D-C118-4544-8365-64846904B48E"),
          UUID.fromString("2AD3F246-FEE1-4E67-B886-69FD380BB150") };
    }
  }

  private List<ItemStack> subItems = null;
  // private ModelBiped model;

  // protected Map<EntityEquipmentSlot, ModelBiped> models = null;
  // protected Map<EntityEquipmentSlot, Map<int[], ModelBiped>> models = null;
  protected Map<String, Map<EntityEquipmentSlot, ModelBiped>> models = null;
  public final EntityEquipmentSlot type;

  public ItemGemArmor(int renderIndexIn, EntityEquipmentSlot equipmentSlotIn, String name) {

    super(SilentGems.MODID, name, ArmorMaterial.DIAMOND, renderIndexIn, equipmentSlotIn);
    this.type = equipmentSlotIn;
  }

  @Override
  public ItemStack constructArmor(ItemStack... materials) {

    return ArmorHelper.constructArmor(this, materials);
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

    int x = ArmorHelper.getMaxDamage(stack);
    float y = (1.8f * x + 1515) / 131;
    return (int) (MAX_DAMAGE_ARRAY[armorType.getIndex()] * y);
  }

  public static int getPlayerTotalGemArmorValue(EntityLivingBase player) {

    float total = 0;
    for (ItemStack armor : player.getArmorInventoryList()) {
      if (StackHelper.isValid(armor)) {
        if (armor.getItem() instanceof ItemGemArmor) {
          total += ((ItemGemArmor) armor.getItem()).getProtection(armor);
        } else if (armor.getItem() instanceof ItemArmor) {
          total += ((ItemArmor) armor.getItem()).damageReduceAmount;
        }
      }
    }
    return (int) Math.round(total);
  }

  @Override
  public ArmorProperties getProperties(EntityLivingBase player, ItemStack armor,
      DamageSource source, double damage, int slot) {

    // TODO: Special protection, like fall damage?
    if (source.isUnblockable())
      return new ArmorProperties(0, 1, 0);

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

    return new ArmorProperties(0, ratio, Integer.MAX_VALUE);
  }

  @Override
  public int getArmorDisplay(EntityPlayer player, ItemStack armor, int slot) {

    return (int) Math.round(getProtection(armor));
  }

  @Override
  public void damageArmor(EntityLivingBase entity, ItemStack stack, DamageSource source, int damage,
      int slot) {

    int amount = damage - (int) (getToughness(stack) * SilentGems.random.nextFloat());
    int durabilityLeft = getMaxDamage(stack) - stack.getItemDamage();
    amount = amount < 0 ? 0 : (amount > durabilityLeft ? durabilityLeft : amount);
    EntityPlayer player = entity instanceof EntityPlayer ? (EntityPlayer) entity : null;
    ItemHelper.attemptDamageItem(stack, amount, SilentGems.instance.random, player);
  }

  @Override
  public Multimap<String, AttributeModifier> getAttributeModifiers(EntityEquipmentSlot slot,
      ItemStack stack) {

    Multimap<String, AttributeModifier> multimap = HashMultimap.create();

    if (slot == this.armorType) {
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

    return SilentGems.RESOURCE_PREFIX + "textures/armor/temparmor.png";
    // return super.getArmorTexture(stack, entity, slot, type);
  }

  @Nonnull
  @Override
  @SideOnly(Side.CLIENT)
  public ModelBiped getArmorModel(EntityLivingBase entity, ItemStack itemStack,
      EntityEquipmentSlot slot, ModelBiped original) {

    ModelBiped model = getArmorModelForSlot(entity, itemStack, slot);
    if (model == null) {
      model = provideArmorModelForSlot(itemStack, slot, ArmorHelper.getRenderColorString(itemStack),
          ArmorHelper.getRenderColorList(itemStack));
    }

    if (model != null) {
      model.setModelAttributes(original);
      return model;
    }

    return super.getArmorModel(entity, itemStack, slot, original);
  }

  @Override
  public boolean hasEffect(ItemStack stack) {

    return HAS_EFFECT;
  }

  @SideOnly(Side.CLIENT)
  public ModelBiped getArmorModelForSlot(EntityLivingBase entity, ItemStack stack,
      EntityEquipmentSlot slot) {

    if (models == null) {
      models = new HashMap<>(32 * 32 * 32 * 32);
      // models = new EnumMap<>(EntityEquipmentSlot.class);
    }
    String colorString = ArmorHelper.getRenderColorString(stack);
    if (models.get(colorString) == null) {
      models.put(colorString, new EnumMap<>(EntityEquipmentSlot.class));
    }
    return models.get(colorString).get(slot);
    // if (models.get(slot) == null)
    // {
    // models.put(slot, new HashMap<>());
    // }
    // return models.get(slot).get(ArmorHelper.getRenderColorList(stack));
  }

  @SideOnly(Side.CLIENT)
  public ModelBiped provideArmorModelForSlot(ItemStack stack, EntityEquipmentSlot slot,
      String colorString, int[] colors) {

    models.get(colorString).put(slot, new ModelGemArmor(slot, colors));
    return models.get(colorString).get(slot);
    // models.get(slot).put(colors, new ModelGemArmor(slot, colors));
    // return models.get(slot).get(colors);
    // models.put(slot, new ModelGemArmor(slot, ArmorHelper.getRenderColorList(stack)));
    // return models.get(slot);
  }

  @Override
  public boolean getIsRepairable(ItemStack toRepair, ItemStack repair) {

    // TODO Tier detection
    return false;
  }

  @Override
  public int getItemEnchantability(ItemStack stack) {

    return ArmorHelper.getItemEnchantability(stack);
  }

  @Override
  public void onUpdate(ItemStack armor, World world, Entity entity, int itemSlot,
      boolean isSelected) {

    if (world.getTotalWorldTime() % ToolHelper.CHECK_NAME_FREQUENCY == 0
        && entity instanceof EntityPlayer) {
      EntityPlayer player = (EntityPlayer) entity;
      if (world.isRemote && armor.hasTagCompound()
          && armor.getTagCompound().hasKey(ToolHelper.NBT_TEMP_PARTLIST)) {
        NBTTagCompound compound = armor.getTagCompound()
            .getCompoundTag(ToolHelper.NBT_TEMP_PARTLIST);

        int i = 0;
        String key = "part" + i;
        List<ItemStack> parts = Lists.newArrayList();

        // Load part stacks.
        do {
          NBTTagCompound tag = compound.getCompoundTag(key);
          parts.add(StackHelper.loadFromNBT(tag));
          key = "part" + ++i;
        } while (compound.hasKey(key));

        // Create name on the client.
        String displayName = ToolHelper.createToolName(armor.getItem(), parts);
        // tool.setStackDisplayName(displayName);

        // Send to the server.
        MessageItemRename message = new MessageItemRename(player.getName(), itemSlot, displayName,
            armor);
        SilentGems.logHelper.info("Sending armor name \"" + displayName + "\" to server.");
        NetworkHandler.INSTANCE.sendToServer(message);
      }
    }
  }

  @Override
  public void clAddInformation(ItemStack stack, World world, List list, boolean advanced) {

    LocalizationHelper loc = SilentGems.instance.localizationHelper;
    ToolRenderHelper helper = ToolRenderHelper.getInstance();
    boolean controlDown = KeyTracker.isControlDown();
    boolean altDown = KeyTracker.isAltDown();
    String line;

    // Show original owner?
    if (controlDown) {
      String owner = ArmorHelper.getOriginalOwner(stack);
      if (!owner.isEmpty())
        list.add(loc.getMiscText("Tooltip.OriginalOwner", owner));
      else
        list.add(loc.getMiscText("Tooltip.OriginalOwner.Unknown"));
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
      list.add(color + helper.getTooltipLine("Durability", getMaxDamage(stack)));
      list.add(color + helper.getTooltipLine("Protection", getProtection(stack)));

      // Statistics Header
      list.add(sep);
      list.add(loc.getMiscText("Tooltip.Statistics"));

      list.add(helper.getTooltipLine("DamageTaken", ArmorHelper.getStatDamageTaken(stack)));
      list.add(helper.getTooltipLine("Redecorated", ArmorHelper.getStatRedecorated(stack)));

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
      list.add(sep);
    } else {
      list.add(TextFormatting.GOLD + loc.getMiscText("PressAlt"));
    }
  }

  @Override
  protected void clGetSubItems(Item item, CreativeTabs tab, List<ItemStack> list) {

    if (!isInCreativeTab(tab))
      return;

    if (subItems == null) {
      subItems = Lists.newArrayList();

      // Test broken items.
      // ItemStack testBroken = constructArmor(new ItemStack(Items.FLINT));
      // testBroken.setItemDamage(getMaxDamage(testBroken) - 1);
      // subItems.add(testBroken);

      // Flint
      subItems.add(constructArmor(new ItemStack(Items.FLINT)));

      // Regular Gems
      for (EnumGem gem : EnumGem.values())
        subItems.add(constructArmor(gem.getItem()));

      // Super Gems
      for (EnumGem gem : EnumGem.values())
        subItems.add(constructArmor(gem.getItemSuper()));

      // Set maker name.
      for (ItemStack stack : subItems)
        ArmorHelper.setOriginalOwner(stack, TextFormatting.LIGHT_PURPLE + "Creative");
    }

    list.addAll(subItems);
  }

  @Override
  public void addRecipes(RecipeMaker recipes) {

    addRecipe(recipes, itemName + "_flint", new ItemStack(Items.FLINT));
    for (EnumGem gem : EnumGem.values()) {
      addRecipe(recipes, itemName + "_" + gem.name(), gem.getItem());
      addRecipe(recipes, itemName + "_" + gem.name() + "_super", gem.getItemSuper());
    }
  }

  protected void addRecipe(RecipeMaker recipes, String name, ItemStack material) {

    ToolPart part = ToolPartRegistry.fromStack(material);
    if (part != null && !part.isBlacklisted(material))
      recipes.addShaped(name, constructArmor(material), " g ", "gfg", " g ", 'g', material, 'f',
          ModItems.armorFrame.getFrameForArmorPiece(this, part.getTier()));
  }

  @Override
  public String getUnlocalizedName(ItemStack stack) {

    return "item.silentgems:" + itemName;
  }
}
