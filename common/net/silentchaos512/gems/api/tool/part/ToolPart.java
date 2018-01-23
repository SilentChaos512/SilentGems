package net.silentchaos512.gems.api.tool.part;

import java.util.List;

import javax.annotation.Nonnull;

import com.google.common.collect.Lists;

import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.silentchaos512.gems.SilentGems;
import net.silentchaos512.gems.api.lib.EnumMaterialGrade;
import net.silentchaos512.gems.api.lib.EnumMaterialTier;
import net.silentchaos512.gems.api.lib.IPartPosition;
import net.silentchaos512.gems.api.lib.ToolPartPosition;
import net.silentchaos512.gems.api.stats.ItemStat;
import net.silentchaos512.gems.api.stats.ItemStatModifier;
import net.silentchaos512.gems.api.stats.PartStats;
import net.silentchaos512.gems.api.tool.ToolStats;
import net.silentchaos512.gems.config.GemsConfig;
import net.silentchaos512.gems.item.ToolRenderHelper;
import net.silentchaos512.lib.registry.IRegistryObject;
import net.silentchaos512.lib.util.StackHelper;

/**
 * Represents a part that tools can be made from. Add-ons should not extend this class! Extend one of the subclasses
 * instead.
 * 
 * ToolPartMain is for heads and rod decoration. ToolPartGrip is rod attachments like wool. ToolPartRod is for rods.
 * ToolPartTip is for tips.
 * 
 * ToolPartRegistry is where tool parts are registered. See net.silentchaos512.gems.lib.part.ModParts for examples.
 * 
 * @author SilentChaos512
 *
 */
public abstract class ToolPart {

  /**
   * The unique ID for the part. I recommend prefixing it with your mod ID. Example: SilentGems:RodGold.
   */
  protected @Nonnull String key;
  /**
   * An item that represents the tool part. It's also the item that is used in crafting. For example, the items that
   * apply tip upgrades are the crafting stacks for those parts.
   * 
   * This variable may not be null. If you want multiple items to be used for this part, you may specify an ore
   * dictionary key to use.
   */
  protected @Nonnull ItemStack craftingStack;
  /**
   * The ore dictionary key to match for this part. This may be an empty string if you do not want to use the ore
   * dictionary.
   */
  protected @Nonnull String craftingOreDictName;
  /**
   * The tier of the part. Part tiers determine what parts can be crafted together to make a tool, as well as the tier
   * of the resulting tool.
   */
  protected EnumMaterialTier tier;

  protected int rarity;
  protected @Nonnull PartStats stats;

  public ToolPart(String key, ItemStack craftingStack) {

    this(key, craftingStack, "");
  }

  protected ToolPart(String key, ItemStack craftingStack, String craftingOreDictName) {

    this.key = key.toLowerCase();
    this.craftingStack = craftingStack;
    this.craftingOreDictName = craftingOreDictName;
    this.tier = EnumMaterialTier.REGULAR;
  }

  void setStatsFromOldMethods() {

    if (stats == null) {
      stats = new PartStats();
      stats.armor = getProtection();
      stats.attackSpeed = getMeleeSpeed();
      stats.chargeSpeed = getChargeSpeed();
      stats.durability = getDurability();
      stats.enchantablity = getEnchantability();
      stats.harvestLevel = getHarvestLevel();
      stats.harvestSpeed = getHarvestSpeed();
      stats.magicArmor = 0f;
      stats.magicDamage = getMagicDamage();
      stats.meleeDamage = getMeleeDamage();
      stats.rangedDamage = 0f;
      stats.rangedSpeed = 0f;
    }
  }

  /**
   * @return The unique indentifier for the part.
   */
  public @Nonnull String getKey() {

    return key;
  }

  /**
   * Gets the item used for constructing tools with this part.
   * 
   * @return The crafting stack.
   */
  public @Nonnull ItemStack getCraftingStack() {

    return craftingStack;
  }

  /**
   * Gets the ore dictionary key for items used to construct the tool/armor.
   * 
   * @return The ore dictionary key to match when constructing.
   */
  public @Nonnull String getCraftingOreDictName() {

    return craftingOreDictName;
  }

  public EnumMaterialTier getTier() {

    return tier;
  }

  /**
   * Gets the color applied when rendering the part.
   * 
   * @deprecated Use more sensitive version below.
   * @param toolOrArmor
   *          The tool or armor stack being rendered.
   * @return
   */
  @Deprecated
  public int getColor(ItemStack toolOrArmor) {

    return 0xFFFFFF;
  }

  /**
   * Gets the color applied when rendering the part.
   * 
   * @param toolOrArmor
   *          The tool or armor stack being rendered.
   * @return
   */
  public int getColor(ItemStack toolOrArmor, IPartPosition position, int animationFrame) {

    // Pass control to deprecated method by default, for backwards compatibility.
    return getColor(toolOrArmor);
  }

  /**
   * Returns a display name for the part and given ItemStack. For ToolPartMain, this is used in constructing the tool's
   * name. Defaults to the stack's display name, which the player can set in an anvil.
   * 
   * @param partRep
   *          The ItemStack, typically the representative that is being used in crafting.
   * @return
   */
  public String getDisplayName(ItemStack partRep) {

    return partRep.getDisplayName();
  }

  /**
   * Returns a prefix used in tool name generation (ie, "Supercharged")
   * 
   * @param partRep
   *          The ItemStack, typically the representative that is being used in crafting.
   * @return
   */
  public String getDisplayNamePrefix(ItemStack partRep) {

    return "";
  }

  /**
   * Gets the amount of durability to repair when decorating with this part.
   * 
   * @param toolOrArmor
   *          The tool or armor being decorating/repaired.
   * @param partRep
   *          The stack that represents the tool part.
   * @return The amount of durability to restore, usually a fraction of the tool's max.
   */
  public int getRepairAmount(ItemStack toolOrArmor, ItemStack partRep) {

    return 0;
  }

  public abstract ItemStatModifier getStatModifier(ItemStat stat, EnumMaterialGrade grade);

  /**
   * Determines if the stack matches the tool part for crafting purposes. Override if more complex matching is needed.
   * 
   * @param partRep
   *          The item we are checking for a match.
   * @param matchOreDict
   *          If true, the ore dictionary key will also be checked for a match.
   * @return True if partRep matches the part, false otherwise.
   */
  public boolean matchesForCrafting(ItemStack partRep, boolean matchOreDict) {

    if (StackHelper.isEmpty(partRep))
      return false;
    if (partRep.isItemEqual(getCraftingStack()))
      return true;
    if (matchOreDict)
      return StackHelper.matchesOreDict(partRep, getCraftingOreDictName());
    return false;
  }

  /**
   * Determines if the stack matches the tool part for decorating purposes. Override if more complex matching is needed.
   * By default, this just redirects to matchesForCrafting.
   * 
   * @param partRep
   *          The item we are checking for a match.
   * @param matchOreDict
   *          If true, the ore dictionary key will also be checked for a match.
   * @return True if partRep matches the part, false otherwise.
   */
  public boolean matchesForDecorating(ItemStack partRep, boolean matchOreDict) {

    return matchesForCrafting(partRep, matchOreDict);
  }

  /**
   * Gets the model for the part on the specified tool and position. Some parts can exist in multiple positions
   * (ToolPartMain), plus the models are usually different for different tool types.
   * 
   * @deprecated Use frame-sensitive version instead!
   * 
   * @param tool
   *          The tool being rendered.
   * @param pos
   *          The position of the part.
   * @return
   */
  @Deprecated
  @SideOnly(Side.CLIENT)
  public ModelResourceLocation getModel(ItemStack toolOrArmor, ToolPartPosition pos) {

    return getModel(toolOrArmor, pos, 0);
  }

  @SideOnly(Side.CLIENT)
  public abstract ModelResourceLocation getModel(ItemStack toolOrArmor, ToolPartPosition pos,
      int frame);

  @SideOnly(Side.CLIENT)
  public ModelResourceLocation getBrokenModel(ItemStack toolOrArmor, ToolPartPosition pos,
      int frame) {

    if (pos == ToolPartPosition.HEAD) {
      // Heads have a special broken model. It's a plain grayscale image, so an appropriate color should be returned in
      // getColor if the tool is broken (ToolHelper#isBroken).
      String name = ((IRegistryObject) toolOrArmor.getItem()).getName();
      name = SilentGems.RESOURCE_PREFIX + name + "/" + name + "_broken";
      return new ModelResourceLocation(name.toLowerCase(), "inventory");
    } else if (pos == ToolPartPosition.TIP) {
      // Tips are not rendered on broken tools.
      return ToolRenderHelper.getInstance().modelBlank;
    }

    // All other passes are left alone.
    return getModel(toolOrArmor, pos, frame);
  }

  public String getUnlocalizedName() {

    return key;
  }

  public List<EnumMaterialTier> getCompatibleTiers() {

    List<EnumMaterialTier> list = Lists.newArrayList();
    for (EnumMaterialTier tier : EnumMaterialTier.values())
      if (validForToolOfTier(tier))
        list.add(tier);
    return list;
  }

  public boolean isBlacklisted(ItemStack partStack) {

    if (tier == EnumMaterialTier.MUNDANE && GemsConfig.PART_DISABLE_ALL_MUNDANE)
      return true;
    else if (tier == EnumMaterialTier.REGULAR && GemsConfig.PART_DISABLE_ALL_REGULAR)
      return true;
    else if (tier == EnumMaterialTier.SUPER && GemsConfig.PART_DISABLE_ALL_SUPER)
      return true;
    else
      return GemsConfig.PART_BLACKLIST.contains(key);
  }

  @Deprecated
  public void applyStats(ToolStats stats) {

  }

  @Deprecated
  public abstract int getDurability();

  @Deprecated
  public abstract float getHarvestSpeed();

  @Deprecated
  public abstract int getHarvestLevel();

  @Deprecated
  public abstract float getMeleeDamage();

  @Deprecated
  public abstract float getMagicDamage();

  @Deprecated
  public abstract int getEnchantability();

  @Deprecated
  public abstract float getMeleeSpeed();

  @Deprecated
  public abstract float getChargeSpeed();

  @Deprecated
  public abstract float getProtection();

  /**
   * Determines if the part is valid for tools of the given tier. Generally, this is true if the part and tool tier is
   * the same, but that's not always the case.
   * 
   * @param toolTier
   * @return
   */
  public abstract boolean validForToolOfTier(EnumMaterialTier toolTier);

  /**
   * Determines if the part is valid for the given position. I believe this is unused at the moment.
   * 
   * @param pos
   * @return
   */
  public abstract boolean validForPosition(IPartPosition pos);

  @Override
  public String toString() {

    String str = "ToolPart{";
    str += "Key: " + getKey() + ", ";
    str += "CraftingStack: " + getCraftingStack() + ", ";
    str += "CraftingOreDictName: '" + getCraftingOreDictName() + "', ";
    str += "Tier: " + getTier();
    str += "}";
    return str;
  }
}
