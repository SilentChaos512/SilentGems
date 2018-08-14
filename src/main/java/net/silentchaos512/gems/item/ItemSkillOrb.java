package net.silentchaos512.gems.item;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;
import net.silentchaos512.gems.SilentGems;
import net.silentchaos512.gems.api.IArmor;
import net.silentchaos512.gems.api.ITool;
import net.silentchaos512.gems.lib.soul.SoulSkill;
import net.silentchaos512.gems.lib.soul.ToolSoul;
import net.silentchaos512.gems.util.SoulManager;
import net.silentchaos512.gems.util.ToolHelper;
import net.silentchaos512.lib.util.ChatHelper;
import net.silentchaos512.lib.util.StackHelper;

import javax.annotation.Nullable;

public class ItemSkillOrb extends Item {
    private static final String NBT_ID = "SG_Skill_ID";

    public ItemSkillOrb() {
    }

    public ItemStack getStack(SoulSkill skill) {
        ItemStack stack = new ItemStack(this);
        NBTTagCompound tags = new NBTTagCompound();
        tags.setString(NBT_ID, skill.id);
        stack.setTagCompound(tags);
        return stack;
    }

    @Nullable
    private SoulSkill getSkill(ItemStack stack) {
        if (StackHelper.isEmpty(stack) || !stack.hasTagCompound()) {
            return null;
        }
        return SoulSkill.getById(stack.getTagCompound().getString(NBT_ID));
    }

    @Override
    public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> list) {
        if (!isInCreativeTab(tab)) return;
        SoulSkill.getSkillList().forEach(skill -> list.add(getStack(skill)));
    }

    @Override
    public String getItemStackDisplayName(ItemStack stack) {
        SoulSkill skill = getSkill(stack);
        if (skill == null) {
            return getTranslationKey(stack);
        }
        return SilentGems.i18n.subText(this, "name_proper", skill.getLocalizedName(ItemStack.EMPTY, 0));
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand) {
        ItemStack offhand = player.getHeldItemOffhand();

        // Orb in main hand, tool/armor in offhand.
        if (hand != EnumHand.MAIN_HAND || StackHelper.isEmpty(offhand)
                || !(offhand.getItem() instanceof ITool || offhand.getItem() instanceof IArmor)) {
            return new ActionResult<>(EnumActionResult.PASS, player.getHeldItem(hand));
        }

        ItemStack orb = player.getHeldItemMainhand();

        // Check for tool soul.
        ToolSoul soul = SoulManager.getSoul(offhand);
        if (soul == null) {
            if (!world.isRemote) {
                ChatHelper.translate(player, SilentGems.i18n.getKey(this, "no_soul"));
            }
            return new ActionResult<>(EnumActionResult.PASS, orb);
        }

        // Check skill on orb.
        SoulSkill skill = getSkill(orb);
        if (skill == null) {
            if (!world.isRemote) {
                ChatHelper.translate(player, SilentGems.i18n.subText(this, "no_skill"));
            }
            return new ActionResult<>(EnumActionResult.PASS, orb);
        }

        if (world.isRemote) {
            return new ActionResult<>(EnumActionResult.SUCCESS, player.getHeldItem(hand));
        }

        // Try to add or level up the skill.
        if (soul.addOrLevelSkill(skill, offhand, player)) {
            ToolHelper.recalculateStats(offhand);
            orb.shrink(1);
            return new ActionResult<>(EnumActionResult.SUCCESS, orb);
        } else {
            ChatHelper.translate(player, SilentGems.i18n.getKey(this, "max_level"));
            return new ActionResult<>(EnumActionResult.PASS, player.getHeldItem(hand));
        }
    }
}