package net.silentchaos512.gems.item.tool;

import java.util.List;

import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.silentchaos512.gems.SilentGems;
import net.silentchaos512.gems.api.ITool;
import net.silentchaos512.gems.api.lib.EnumMaterialTier;
import net.silentchaos512.gems.config.GemsConfig;
import net.silentchaos512.gems.enchantment.ModEnchantments;
import net.silentchaos512.gems.entity.EntityChaosProjectile;
import net.silentchaos512.gems.entity.EntityChaosProjectileHoming;
import net.silentchaos512.gems.entity.EntityChaosProjectileSweep;
import net.silentchaos512.gems.handler.PlayerDataHandler;
import net.silentchaos512.gems.handler.PlayerDataHandler.PlayerData;
import net.silentchaos512.gems.item.ModItems;
import net.silentchaos512.gems.item.ToolRenderHelper;
import net.silentchaos512.gems.lib.EnumGem;
import net.silentchaos512.gems.lib.Names;
import net.silentchaos512.gems.util.ToolHelper;
import net.silentchaos512.lib.registry.IRegistryObject;

public class ItemGemSword extends ItemSword implements IRegistryObject, ITool {

  protected List<ItemStack> subItems = null;

  public ItemGemSword() {

    super(ToolHelper.FAKE_MATERIAL);
    setUnlocalizedName(SilentGems.RESOURCE_PREFIX + Names.SWORD);
  }

  public ItemStack constructTool(boolean supercharged, ItemStack material) {

    return constructTool(supercharged, material, material, material);
  }

  public ItemStack constructTool(boolean supercharged, ItemStack... materials) {

    if (GemsConfig.TOOL_DISABLE_SWORD)
      return ItemStack.EMPTY;
    ItemStack rod = supercharged ? ModItems.craftingMaterial.toolRodGold
        : new ItemStack(Items.STICK);
    return ToolHelper.constructTool(this, rod, materials);
  }

  // ===============
  // ITool overrides
  // ===============

  @Override
  public ItemStack constructTool(ItemStack rod, ItemStack... materials) {

    if (GemsConfig.TOOL_DISABLE_SWORD)
      return ItemStack.EMPTY;
    if (materials.length == 2) {
      ItemStack temp = materials[0];
      materials[0] = materials[1];
      materials[1] = temp;
    }
    return ToolHelper.constructTool(this, rod, materials);
  }

  @Override
  public float getMeleeDamage(ItemStack tool) {

    return getBaseMeleeDamageModifier() + ToolHelper.getMeleeDamage(tool);
  }

  @Override
  public float getMagicDamage(ItemStack tool) {

    int magicEnchLevel = EnchantmentHelper.getEnchantmentLevel(ModEnchantments.magicDamage, tool);
    return 2.0f + ToolHelper.getMagicDamage(tool);
  }

  @Override
  public float getBaseMeleeDamageModifier() {

    return 3.0f;
  }

  @Override
  public float getBaseMeleeSpeedModifier() {

    return -2.4f;
  }

  // ==============
  // Item overrides
  // ==============

  @Override
  public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean advanced) {

    ToolRenderHelper.getInstance().addInformation(stack, player, list, advanced);
  }

  @Override
  public void getSubItems(Item item, CreativeTabs tab, NonNullList list) {

    if (subItems == null) {
      subItems = ToolHelper.getSubItems(item, 2);
    }
    list.addAll(subItems);
  }

  @Override
  public boolean onEntitySwing(EntityLivingBase entityLiving, ItemStack stack) {

    World world = entityLiving.world;
    if (world.isRemote || !(entityLiving instanceof EntityPlayer)
        || ToolHelper.getToolTier(stack).ordinal() < EnumMaterialTier.SUPER.ordinal()) {
      return false;
    }

    EntityPlayer player = (EntityPlayer) entityLiving;
    if (!player.isSneaking()) {
      return false;
    }

    int costToCast = getShotCost(player, stack);
    int cooldown = getShotCooldown(player, stack);
    PlayerData data = PlayerDataHandler.get(player);

    if (data.chaos >= costToCast && data.magicCooldown <= 0) {
      if (!player.capabilities.isCreativeMode) {
        data.drainChaos(costToCast);
        data.magicCooldown = cooldown;
        stack.damageItem(1, entityLiving);
      }

      ToolHelper.incrementStatShotsFired(stack, 1);

      if (!world.isRemote) {
        for (EntityChaosProjectile shot : getShots(player, stack)) {
          world.spawnEntity(shot);
        }
      }
    }

    return super.onEntitySwing(entityLiving, stack);
  }

  private List<EntityChaosProjectile> getShots(EntityPlayer player, ItemStack stack) {

    List<EntityChaosProjectile> list = Lists.newArrayList();

    if (ToolHelper.getToolTier(stack).ordinal() < EnumMaterialTier.SUPER.ordinal()) {
      return list;
    }

    // Calculate magic damage.
    // Includes player "magic strength" (currently just a constant 1, might do something with it later).
    float damage = 1f + getMagicDamage(stack);
    // Magic damage enchantment
    int magicEnchLevel = EnchantmentHelper.getEnchantmentLevel(ModEnchantments.magicDamage, stack);
    if (magicEnchLevel > 0)
      damage += ModEnchantments.magicDamage.calcDamage(magicEnchLevel);

    if (stack.getItem() == ModItems.scepter) {
      for (int i = 0; i < 5; ++i) {
        list.add(new EntityChaosProjectileHoming(player, stack, damage));
      }
    }
    if (stack.getItem() == ModItems.katana) {
      list.add(new EntityChaosProjectileSweep(player, stack, damage, 0.0f));
      list.add(new EntityChaosProjectileSweep(player, stack, damage, -0.075f));
      list.add(new EntityChaosProjectileSweep(player, stack, damage, 0.075f));
    }
    if (stack.getItem() == ModItems.sword) {
      list.add(new EntityChaosProjectile(player, stack, damage));
    }

    return list;
  }

  private int getShotCost(EntityPlayer player, ItemStack stack) {

    if (player.capabilities.isCreativeMode)
      return 0;

    if (!stack.isEmpty()) {
      Item item = stack.getItem();
      // @formatter:off
      if (item == ModItems.scepter) return 5000;
      if (item == ModItems.katana) return 2000;
      if (item == ModItems.sword) return 1000;
      // @formatter:on
    }

    return 1500;
  }

  private int getShotCooldown(EntityPlayer player, ItemStack stack) {

    if (player.capabilities.isCreativeMode)
      return 0;

    if (!stack.isEmpty()) {
      Item item = stack.getItem();
      // @formatter:off
      if (item == ModItems.scepter) return 20;
      if (item == ModItems.katana) return 10;
      if (item == ModItems.sword) return 5;
      // @formatter:on
    }

    return 10;
  }

  @Override
  public boolean onBlockStartBreak(ItemStack stack, BlockPos pos, EntityPlayer player) {

    boolean canceled = super.onBlockStartBreak(stack, pos, player);
    if (!canceled) {
      ToolHelper.onBlockStartBreak(stack, pos, player);
    }
    return canceled;
  }

  @Override
  public int getMaxDamage(ItemStack stack) {

    return ToolHelper.getMaxDamage(stack);
  }

  @Override
  public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack,
      boolean slotChanged) {

    return ToolRenderHelper.instance.shouldCauseReequipAnimation(oldStack, newStack, slotChanged);
  }

  @Override
  public boolean hasEffect(ItemStack stack) {

    return ToolRenderHelper.instance.hasEffect(stack);
  }

  @Override
  public int getItemEnchantability(ItemStack stack) {

    return ToolHelper.getItemEnchantability(stack);
  }

  @Override
  public void onUpdate(ItemStack tool, World world, Entity entity, int itemSlot,
      boolean isSelected) {

    ToolHelper.onUpdate(tool, world, entity, itemSlot, isSelected);
  }

  // ==================
  // ItemSword overrides
  // ==================

  @Override
  public Multimap<String, AttributeModifier> getAttributeModifiers(EntityEquipmentSlot slot,
      ItemStack stack) {

    return ToolHelper.getAttributeModifiers(slot, stack);
  }

  @Override
  public boolean onBlockDestroyed(ItemStack stack, World world, IBlockState state, BlockPos pos,
      EntityLivingBase entityLiving) {

    return ToolHelper.onBlockDestroyed(stack, world, state, pos, entityLiving);
  }

  @Override
  public boolean hitEntity(ItemStack stack, EntityLivingBase entity1, EntityLivingBase entity2) {

    return ToolHelper.hitEntity(stack, entity1, entity2);
  }

  @Override
  public boolean getIsRepairable(ItemStack stack1, ItemStack stack2) {

    return ToolHelper.getIsRepairable(stack1, stack2);
  }

  // ===============
  // IRegistryObject
  // ===============

  @Override
  public void addRecipes() {

    if (GemsConfig.TOOL_DISABLE_SWORD)
      return;

    String l1 = "g";
    String l2 = "g";
    String l3 = "s";
    ItemStack flint = new ItemStack(Items.FLINT);
    ItemStack rodGold = ModItems.craftingMaterial.toolRodGold;

    // Flint
    ToolHelper.addRecipe(constructTool(false, flint), l1, l2, l3, flint, "stickWood");
    for (EnumGem gem : EnumGem.values()) {
      // Regular
      ToolHelper.addRecipe(constructTool(false, gem.getItem()), l1, l2, l3, gem.getItem(), "stickWood");
      // Super
      ToolHelper.addRecipe(constructTool(true, gem.getItemSuper()), l1, l2, l3, gem.getItemSuper(), rodGold);
    }
  }

  @Override
  public void addOreDict() {

  }

  @Override
  public String getName() {

    return Names.SWORD;
  }

  @Override
  public String getFullName() {

    return getModId() + ":" + getName();
  }

  @Override
  public String getModId() {

    return SilentGems.MODID;
  }

  @Override
  public List<ModelResourceLocation> getVariants() {

    return Lists.newArrayList(ToolRenderHelper.SMART_MODEL);
  }

  @Override
  public boolean registerModels() {

    return false;
  }
}
