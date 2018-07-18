package net.silentchaos512.gems.item;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;
import net.silentchaos512.gems.SilentGems;
import net.silentchaos512.gems.api.IArmor;
import net.silentchaos512.gems.api.ITool;
import net.silentchaos512.gems.lib.Names;
import net.silentchaos512.gems.lib.soul.SoulSkill;
import net.silentchaos512.gems.lib.soul.ToolSoul;
import net.silentchaos512.gems.util.SoulManager;
import net.silentchaos512.gems.util.ToolHelper;
import net.silentchaos512.lib.item.ItemSL;
import net.silentchaos512.lib.util.ChatHelper;
import net.silentchaos512.lib.util.ItemHelper;
import net.silentchaos512.lib.util.LocalizationHelper;
import net.silentchaos512.lib.util.StackHelper;

import java.util.List;

public class ItemSkillOrb extends ItemSL {

  public static final String NBT_ID = "SG_Skill_ID";

  public ItemSkillOrb() {

    super(1, SilentGems.MODID, Names.SKILL_ORB);
  }

  public ItemStack getStack(SoulSkill skill) {

    ItemStack stack = new ItemStack(this);
    NBTTagCompound tags = new NBTTagCompound();
    tags.setString(NBT_ID, skill.id);
    stack.setTagCompound(tags);
    return stack;
  }

  public SoulSkill getSkill(ItemStack stack) {

    if (StackHelper.isEmpty(stack) || !stack.hasTagCompound()) {
      return null;
    }
    return SoulSkill.getById(stack.getTagCompound().getString(NBT_ID));
  }

  @Override
  protected void clGetSubItems(Item item, CreativeTabs tab, List<ItemStack> list) {

    if (!ItemHelper.isInCreativeTab(item, tab))
      return;

    for (SoulSkill skill : SoulSkill.getSkillList()) {
      list.add(getStack(skill));
    }
  }

  @Override
  public String getItemStackDisplayName(ItemStack stack) {

    LocalizationHelper loc = SilentGems.localizationHelper;
    SoulSkill skill = getSkill(stack);
    if (skill == null) {
      return getTranslationKey(stack);
    }

    return loc.getItemSubText(Names.SKILL_ORB, "name_proper",
        skill.getLocalizedName(StackHelper.empty(), 0));
  }

  @Override
  protected ActionResult<ItemStack> clOnItemRightClick(World world, EntityPlayer player,
      EnumHand hand) {

    ItemStack offhand = player.getHeldItemOffhand();

    // Orb in main hand, tool/armor in offhand.
    if (hand != EnumHand.MAIN_HAND || StackHelper.isEmpty(offhand)
        || !(offhand.getItem() instanceof ITool || offhand.getItem() instanceof IArmor)) {
      return new ActionResult<ItemStack>(EnumActionResult.PASS, player.getHeldItem(hand));
    }

    ItemStack orb = player.getHeldItemMainhand();

    // Check for tool soul.
    ToolSoul soul = SoulManager.getSoul(offhand);
    if (soul == null) {
      if (!world.isRemote) {
        ChatHelper.sendMessage(player,
            SilentGems.localizationHelper.getItemSubText(Names.SKILL_ORB, "no_soul"));
      }
      return new ActionResult<ItemStack>(EnumActionResult.PASS, orb);
    }

    // Check skill on orb.
    SoulSkill skill = getSkill(orb);
    if (skill == null) {
      if (!world.isRemote) {
        ChatHelper.sendMessage(player,
            SilentGems.localizationHelper.getItemSubText(Names.SKILL_ORB, "no_skill"));
      }
      return new ActionResult<ItemStack>(EnumActionResult.PASS, orb);
    }

    if (world.isRemote) {
      return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, player.getHeldItem(hand));
    }

    // Try to add or level up the skill.
    if (soul.addOrLevelSkill(skill, offhand, player)) {
      ToolHelper.recalculateStats(offhand);
      StackHelper.shrink(orb, 1);
      return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, orb);
    } else {
      ChatHelper.sendMessage(player,
          SilentGems.localizationHelper.getItemSubText(Names.SKILL_ORB, "max_level"));
      return new ActionResult<ItemStack>(EnumActionResult.PASS, player.getHeldItem(hand));
    }
  }
}