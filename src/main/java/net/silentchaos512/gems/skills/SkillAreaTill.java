package net.silentchaos512.gems.skills;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.silentchaos512.gems.SilentGems;


public class SkillAreaTill extends ToolSkill {

  public static final SkillAreaTill INSTANCE = new SkillAreaTill();

  @Override
  public boolean activate(ItemStack tool, EntityPlayer player, BlockPos pos) {

    // Handle this in ItemGemHoe#onItemUse
    return false;
  }

  @Override
  public int getCost(ItemStack tool, EntityPlayer player, BlockPos pos) {

    return 50;
  }

  @Override
  public String getTranslatedName() {

    return SilentGems.localizationHelper.getLocalizedString("skill", "AreaTill.name");
  }
}
