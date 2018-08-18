package net.silentchaos512.gems.item.tool;

import com.google.common.collect.Multimap;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemHoe;
import net.minecraft.item.ItemStack;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.silentchaos512.gems.SilentGems;
import net.silentchaos512.gems.api.ITool;
import net.silentchaos512.gems.config.ConfigOptionToolClass;
import net.silentchaos512.gems.config.GemsConfig;
import net.silentchaos512.gems.handler.PlayerDataHandler;
import net.silentchaos512.gems.handler.PlayerDataHandler.PlayerData;
import net.silentchaos512.gems.init.ModItems;
import net.silentchaos512.gems.item.ToolRenderHelper;
import net.silentchaos512.gems.lib.Names;
import net.silentchaos512.gems.lib.soul.ToolSoul;
import net.silentchaos512.gems.skills.SkillAreaTill;
import net.silentchaos512.gems.skills.ToolSkill;
import net.silentchaos512.gems.util.SoulManager;
import net.silentchaos512.gems.util.ToolHelper;
import net.silentchaos512.lib.registry.IAddRecipes;
import net.silentchaos512.lib.registry.ICustomModel;
import net.silentchaos512.lib.registry.RecipeMaker;

import java.util.List;

public class ItemGemHoe extends ItemHoe implements ITool, IAddRecipes, ICustomModel {
    public ItemGemHoe() {
        super(ToolHelper.FAKE_MATERIAL);
        setTranslationKey(SilentGems.RESOURCE_PREFIX + Names.HOE);
        setNoRepair();
    }

    public ItemStack constructTool(boolean supercharged, ItemStack material) {
        return constructTool(supercharged, material, material, material);
    }

    public ItemStack constructTool(boolean supercharged, ItemStack... materials) {
        ItemStack rod = supercharged ? ModItems.craftingMaterial.toolRodGold : new ItemStack(Items.STICK);
        return ToolHelper.constructTool(this, rod, materials);
    }

    @Override
    public EnumActionResult onItemUse(EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
        ItemStack stack = player.getHeldItem(hand);

        if (ToolHelper.isBroken(stack)) {
            return EnumActionResult.PASS;
        }

        int tilledCount = 0;
        EnumActionResult result;

        // Till the target block first.
        result = super.onItemUse(player, world, pos, hand, side, hitX, hitY, hitZ);
        if (result == EnumActionResult.SUCCESS) {
            ++tilledCount;
        } else {
            return EnumActionResult.FAIL;
        }

        // Do we have super till and can it be used?
        ToolSkill skill = ToolHelper.getSuperSkill(stack);
        boolean skillEnabled = skill instanceof SkillAreaTill && ToolHelper.isSpecialAbilityEnabled(stack);
        int skillCost = skill != null ? skill.getCost(stack, player, pos) : 0;
        PlayerData data = PlayerDataHandler.get(player);

        // Must have tilled first block, has skill and is enabled, player has enough chaos.
        if (tilledCount > 0 && skillEnabled && data.getCurrentChaos() >= skillCost) {
            EnumFacing playerFacing = player.getHorizontalFacing();

            // Tilling up to 8 extra blocks in the direction the player is facing.
            for (int i = 0, yOffset = 0; i < 8; ++i) {
                BlockPos blockpos = pos.offset(playerFacing, i + 1).up(yOffset);
                if (super.onItemUse(player, world, blockpos, hand, side, hitX, hitY,
                        hitZ) == EnumActionResult.SUCCESS) {
                    // Same height.
                    ++tilledCount;
                } else if (super.onItemUse(player, world, blockpos.up(1), hand, side, hitX, hitY,
                        hitZ) == EnumActionResult.SUCCESS) {
                    // Go up one block.
                    ++tilledCount;
                    ++yOffset;
                } else if (super.onItemUse(player, world, blockpos.down(1), hand, side, hitX, hitY,
                        hitZ) == EnumActionResult.SUCCESS) {
                    // Go down one block.
                    ++tilledCount;
                    --yOffset;
                } else {
                    // Hit a cliff or wall.
                    break;
                }
            }

            if (tilledCount > 1) {
                if (data != null) {
                    data.drainChaos(skillCost);
                }
            }
        }

        // Sound, XP, damage, stats
        if (tilledCount > 0) {
            world.playSound(player, pos, SoundEvents.ITEM_HOE_TILL, SoundCategory.BLOCKS, 1f, 1f);
            if (!world.isRemote) {
                ToolSoul soul = SoulManager.getSoul(stack);
                if (soul != null) {
                    soul.addXp((int) (ToolSoul.XP_FACTOR_TILLING * tilledCount), stack, player);
                }
                ToolHelper.incrementStatBlocksTilled(stack, tilledCount);
                ToolHelper.attemptDamageTool(stack, tilledCount, player);
            }
        }

        return EnumActionResult.SUCCESS;
    }

    @Override
    protected void setBlock(ItemStack stack, EntityPlayer player, World worldIn, BlockPos pos, IBlockState state) {
        // Unlike ItemHoe#setBlock, this does not play a sound or damage the tool.
        // That will be handled in onItemUse
        if (!worldIn.isRemote) {
            worldIn.setBlockState(pos, state, 11);
        }
    }

    // ===============
    // ITool overrides
    // ===============

    public ConfigOptionToolClass getConfig() {
        return GemsConfig.hoe;
    }

    @Override
    public ItemStack constructTool(ItemStack rod, ItemStack... materials) {
        if (getConfig().isDisabled)
            return ItemStack.EMPTY;
        return ToolHelper.constructTool(this, rod, materials);
    }

    @Override
    public float getMeleeDamageModifier() {
        return -4.0f;
    }

    @Override
    public float getMagicDamageModifier() {
        return 0.0f;
    }

    @Override
    public float getMeleeSpeedModifier() {
        return 1.0f;
    }

    // ==============
    // Item overrides
    // ==============

    @Override
    public boolean onBlockStartBreak(ItemStack stack, BlockPos pos, EntityPlayer player) {
        boolean canceled = super.onBlockStartBreak(stack, pos, player);
        if (!canceled) {
            ToolHelper.onBlockStartBreak(stack, pos, player);
        }
        return canceled;
    }

    @Override
    public Multimap<String, AttributeModifier> getAttributeModifiers(EntityEquipmentSlot slot, ItemStack stack) {
        return ToolHelper.getAttributeModifiers(slot, stack);
    }

    @Override
    public int getMaxDamage(ItemStack stack) {
        return ToolHelper.getMaxDamage(stack);
    }

    @Override
    public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged) {
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
    public boolean hitEntity(ItemStack stack, EntityLivingBase entity1, EntityLivingBase entity2) {
        return ToolHelper.hitEntity(stack, entity1, entity2);
    }

    @Override
    public boolean getIsRepairable(ItemStack stack1, ItemStack stack2) {
        return ToolHelper.getIsRepairable(stack1, stack2);
    }

    @Override
    public void onUpdate(ItemStack tool, World world, Entity entity, int itemSlot, boolean isSelected) {
        ToolHelper.onUpdate(tool, world, entity, itemSlot, isSelected);
    }

    @Override
    public boolean onEntityItemUpdate(EntityItem entityItem) {
        return ToolHelper.onEntityItemUpdate(entityItem);
    }

    @Override
    public void addRecipes(RecipeMaker recipes) {
        if (!getConfig().isDisabled)
            ToolHelper.addExampleRecipe(this, "hh", " r", " r");
    }

    @Override
    public void addInformation(ItemStack stack, World world, List list, ITooltipFlag flag) {
        ToolRenderHelper.getInstance().addInformation(stack, world, list, flag);
    }

    @Override
    public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> list) {
        if (!isInCreativeTab(tab)) return;
        list.addAll(ToolHelper.getSubItems(this, 2));
    }

    @Override
    public void registerModels() {
        SilentGems.registry.setModel(this, 0, "tool");
    }
}
