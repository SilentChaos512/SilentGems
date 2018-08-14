package net.silentchaos512.gems.item.tool;

import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.silentchaos512.gems.SilentGems;
import net.silentchaos512.gems.api.ITool;
import net.silentchaos512.gems.api.lib.EnumMaterialTier;
import net.silentchaos512.gems.client.gui.GuiChaosBar;
import net.silentchaos512.gems.config.ConfigOptionToolClass;
import net.silentchaos512.gems.config.GemsConfig;
import net.silentchaos512.gems.entity.EntityChaosProjectile;
import net.silentchaos512.gems.entity.EntityChaosProjectileHoming;
import net.silentchaos512.gems.entity.EntityChaosProjectileScatter;
import net.silentchaos512.gems.entity.EntityChaosProjectileSweep;
import net.silentchaos512.gems.handler.PlayerDataHandler;
import net.silentchaos512.gems.handler.PlayerDataHandler.PlayerData;
import net.silentchaos512.gems.init.ModEnchantments;
import net.silentchaos512.gems.init.ModItems;
import net.silentchaos512.gems.item.ToolRenderHelper;
import net.silentchaos512.gems.lib.Names;
import net.silentchaos512.gems.lib.soul.ToolSoul;
import net.silentchaos512.gems.util.EnumMagicType;
import net.silentchaos512.gems.util.SoulManager;
import net.silentchaos512.gems.util.ToolHelper;
import net.silentchaos512.lib.item.IItemSL;
import net.silentchaos512.lib.registry.IRegistryObject;
import net.silentchaos512.lib.registry.RecipeMaker;
import net.silentchaos512.lib.util.EntityHelper;
import net.silentchaos512.lib.util.ItemHelper;
import net.silentchaos512.lib.util.StackHelper;

import java.util.List;
import java.util.Map;

public class ItemGemSword extends ItemSword implements IRegistryObject, ITool, IItemSL {

    public ItemGemSword() {

        super(ToolHelper.FAKE_MATERIAL);
        setTranslationKey(SilentGems.RESOURCE_PREFIX + Names.SWORD);
        setNoRepair();
    }

    public ItemStack constructTool(boolean supercharged, ItemStack material) {

        return constructTool(supercharged, material, material, material);
    }

    public ItemStack constructTool(boolean supercharged, ItemStack... materials) {

        if (getConfig().isDisabled)
            return StackHelper.empty();
        ItemStack rod = supercharged ? ModItems.craftingMaterial.toolRodGold
                : new ItemStack(Items.STICK);
        return ToolHelper.constructTool(this, rod, materials);
    }

    // ===============
    // ITool overrides
    // ===============

    public ConfigOptionToolClass getConfig() {

        return GemsConfig.sword;
    }

    @Override
    public ItemStack constructTool(ItemStack rod, ItemStack... materials) {

        if (getConfig().isDisabled)
            return StackHelper.empty();
        if (materials.length == 2) {
            ItemStack temp = materials[0];
            materials[0] = materials[1];
            materials[1] = temp;
        }
        return ToolHelper.constructTool(this, rod, materials);
    }

    @Override
    public float getMeleeDamageModifier() {

        return 3.0f;
    }

    @Override
    public float getMagicDamageModifier() {

        return 2.0f;
    }

    @Override
    public float getMeleeSpeedModifier() {

        return -2.4f;
    }

    // ==============
    // Item overrides
    // ==============

    @Override
    public ActionResult<ItemStack> onItemLeftClickSL(World world, EntityPlayer player,
                                                     EnumHand hand) {

        ItemStack stack = player.getHeldItem(hand);

        if (!player.isSneaking()
                || ToolHelper.getToolTier(stack).ordinal() < EnumMaterialTier.SUPER.ordinal()) {
            return new ActionResult<ItemStack>(EnumActionResult.PASS, stack);
        }

        if (world.isRemote) {
            GuiChaosBar.INSTANCE.show();
            return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, stack);
        }

        int costToCast = getShotCost(player, stack);
        int cooldown = getShotCooldown(player, stack);
        PlayerData data = PlayerDataHandler.get(player);

        if (data.chaos >= costToCast && data.magicCooldown <= 0) {
            if (!player.capabilities.isCreativeMode) {
                data.drainChaos(costToCast);
                data.magicCooldown = cooldown;
                stack.damageItem(1, player);
            }

            ToolHelper.incrementStatShotsFired(stack, 1);

            if (!world.isRemote) {
                for (EntityChaosProjectile shot : getShots(player, stack)) {
                    EntityHelper.safeSpawn(shot);
                }
            }
        }

        return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, stack);
    }

    private List<EntityChaosProjectile> getShots(EntityPlayer player, ItemStack stack) {

        List<EntityChaosProjectile> list = Lists.newArrayList();

        if (ToolHelper.getToolTier(stack).ordinal() < EnumMaterialTier.SUPER.ordinal()) {
            return list;
        }

        EnumMagicType magicType = EnumMagicType.getMagicType(stack);
        int shotCount = magicType.getShotCount(stack);

        // Calculate magic damage.
        // Includes player "magic strength" (currently just a constant 1, might do something with it later).
        float damage = 1f + getMagicDamage(stack);
        // Magic damage enchantment
        int magicEnchLevel = EnchantmentHelper.getEnchantmentLevel(ModEnchantments.magicDamage, stack);
        if (magicEnchLevel > 0)
            damage += ModEnchantments.magicDamage.calcDamage(magicEnchLevel);
        damage *= magicType.getDamagePerShotMultiplier();

        Item item = stack.getItem();
        // Dagger
        if (item instanceof ItemGemDagger) {
            for (int i = 0; i < shotCount; ++i) {
                list.add(new EntityChaosProjectileHoming(player, stack, damage, false, 0.0325, 0.25));
            }
        }
        // Scepter
        else if (item instanceof ItemGemScepter) {
            for (int i = 0; i < shotCount; ++i) {
                list.add(new EntityChaosProjectileHoming(player, stack, damage, true, 0.075, 0.35));
            }
        }
        // Katana
        else if (item instanceof ItemGemKatana) {
            final float maxAngle = 0.05f;
            for (float angle = -maxAngle; angle <= maxAngle; angle += maxAngle / (shotCount / 2)) {
                list.add(new EntityChaosProjectileSweep(player, stack, damage, angle));
            }
        }
        // Machete
        else if (item instanceof ItemGemMachete) {
            for (int i = 0; i < shotCount; ++i) {
                list.add(new EntityChaosProjectileScatter(player, stack, damage));
            }
        }
        // Classic sword (default)
        else {
            for (int i = 0; i < shotCount; ++i) {
                EntityChaosProjectile e = new EntityChaosProjectile(player, stack, damage);
                e.motionY += 0.2 * i;
                list.add(e);
            }
        }

        return list;
    }

    private int getShotCost(EntityPlayer player, ItemStack stack) {

        if (player.capabilities.isCreativeMode)
            return 0;

        return EnumMagicType.getMagicType(stack).getCost(stack);
    }

    private int getShotCooldown(EntityPlayer player, ItemStack stack) {

        if (player.capabilities.isCreativeMode)
            return 0;

        if (StackHelper.isValid(stack)) {
            Item item = stack.getItem();
            // @formatter:off
      if (item == ModItems.dagger) return 5;
      if (item == ModItems.scepter) return 20;
      if (item == ModItems.katana) return 10;
      if (item == ModItems.sword) return 5;
      // @formatter:on
        }

        return 10;
    }

    @Override
    public EnumActionResult onItemUse(EntityPlayer player, World world, BlockPos pos, EnumHand hand,
                                      EnumFacing facing, float hitX, float hitY, float hitZ) {

        ToolSoul soul = SoulManager.getSoul(player.getHeldItem(hand));
        if (soul != null && soul.activateSkillsOnBlock(player.getHeldItem(hand), player, world, pos, facing, hitX, hitY, hitZ)) {
            return EnumActionResult.SUCCESS;
        }
        return EnumActionResult.PASS;
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
    public EnumRarity getRarity(ItemStack stack) {

        return ToolRenderHelper.instance.getRarity(stack);
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

    @Override
    public boolean onEntityItemUpdate(EntityItem entityItem) {

        return ToolHelper.onEntityItemUpdate(entityItem);
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
    public void addRecipes(RecipeMaker recipes) {

        if (!getConfig().isDisabled)
            ToolHelper.addExampleRecipe(this, "h", "h", "r");
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
    public void getModels(Map<Integer, ModelResourceLocation> models) {

        models.put(0, ToolRenderHelper.SMART_MODEL);
    }

    @Override
    public boolean registerModels() {

        return false;
    }

    // =================================
    // Cross Compatibility (MC 10/11/12)
    // =================================

    @Override
    public void addInformation(ItemStack stack, World world, List list, ITooltipFlag flag) {

        ToolRenderHelper.getInstance().addInformation(stack, world, list, flag);
    }

    // getSubItems 1.10.2
    public void func_150895_a(Item item, CreativeTabs tab, List<ItemStack> list) {

        clGetSubItems(item, tab, list);
    }

    // getSubItems 1.11.2
    public void func_150895_a(Item item, CreativeTabs tab, NonNullList<ItemStack> list) {

        clGetSubItems(item, tab, list);
    }

    @Override
    public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> list) {

        clGetSubItems(this, tab, list);
    }

    protected void clGetSubItems(Item item, CreativeTabs tab, List<ItemStack> list) {

        if (!ItemHelper.isInCreativeTab(item, tab))
            return;

        list.addAll(ToolHelper.getSubItems(item, 3));
    }

    // onItemUse
    // public EnumActionResult func_180614_a(ItemStack stack, EntityPlayer player, World world, BlockPos pos,
    // EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
    //
    // return onItemUse(player, world, pos, hand, side, hitX, hitY, hitZ);
    // }
}
