package net.silentchaos512.gems.api.tool.part;

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
  protected ModelResourceLocation model;
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
   * Gets the amount of durability to repair when decorating with this part.
   * 
   * @param tool
   *          The tool being decorating/repaired.
   * @return The amount of durability to restore, usually a fraction of the tool's max.
   */
  public int getRepairAmount(ItemStack tool) {

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
  public ModelResourceLocation getModel(ItemStack tool, EnumPartPosition pos) {

    return model;
  }

  public String getUnlocalizedName() {

    return key;
  }

  public abstract int getDurability();

  public abstract float getHarvestSpeed();

  public abstract int getHarvestLevel();

  public abstract float getMeleeDamage();

  public abstract float getMagicDamage();

  public abstract int getEnchantability();

  public abstract float getMeleeSpeed();

  public abstract float getChargeSpeed();

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
