package net.silentchaos512.gems.api.tool.part;

import java.util.List;

import com.google.common.collect.Lists;

import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.silentchaos512.gems.api.lib.EnumMaterialTier;
import net.silentchaos512.gems.api.lib.EnumPartPosition;

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
   * Generally not used, since parts have different models for different tools, or even for different positions.
   */
  @SideOnly(Side.CLIENT)
  @Deprecated
  protected ModelResourceLocation model; // Use getter instead.
  /**
   * The unique ID for the part. I recommend prefixing it with your mod ID. Example: SilentGems:RodGold.
   */
  protected String key;
  /**
   * An item that represents the tool part. It's also the item that is used in crafting. For example, the items that
   * apply tip upgrades are the crafting stacks for those parts.
   * 
   * This variable may not be null. If you want multiple items to be used for this part, you may specify an ore
   * dictionary key to use.
   */
  protected ItemStack craftingStack;
  /**
   * The ore dictionary key to match for this part. This may be an empty string if you do not want to use the ore
   * dictionary.
   */
  protected String craftingOreDictName;
  /**
   * The tier of the part. Part tiers determine what parts can be crafted together to make a tool, as well as the tier
   * of the resulting tool.
   */
  protected EnumMaterialTier tier;
  /**
   * The color apply to the layer when rendering.
   */
  @Deprecated // Use getter instead.
  protected int color = 0xFFFFFF;

  public ToolPart(String key, ItemStack craftingStack) {

    this(key, craftingStack, "");
  }

  protected ToolPart(String key, ItemStack craftingStack, String craftingOreDictName) {

    this.key = key;
    this.craftingStack = craftingStack;
    this.craftingOreDictName = craftingOreDictName;
    this.tier = EnumMaterialTier.REGULAR;
  }

  public String getKey() {

    return key;
  }

  public ItemStack getCraftingStack() {

    return craftingStack;
  }

  public String getCraftingOreDictName() {

    return craftingOreDictName;
  }

  public EnumMaterialTier getTier() {

    return tier;
  }

  /**
   * Gets the color applied when rendering the part.
   * 
   * @param toolOrArmor
   *          The tool or armor stack being rendered.
   * @return
   */
  public int getColor(ItemStack toolOrArmor) {

    return color;
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

  /**
   * Gets the model for the part on the specified tool and position. Some parts can exist in multiple positions
   * (ToolPartMain), plus the models are usually different for different tool types.
   * 
   * @param tool
   *          The tool being rendered.
   * @param pos
   *          The position of the part.
   * @return
   */
  @SideOnly(Side.CLIENT)
  public ModelResourceLocation getModel(ItemStack toolOrArmor, EnumPartPosition pos) {

    return model;
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

  public abstract int getDurability();

  public abstract float getHarvestSpeed();

  public abstract int getHarvestLevel();

  public abstract float getMeleeDamage();

  public abstract float getMagicDamage();

  public abstract int getEnchantability();

  public abstract float getMeleeSpeed();

  public abstract float getChargeSpeed();

  // Only needed for armor.
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
  public abstract boolean validForPosition(EnumPartPosition pos);
}
