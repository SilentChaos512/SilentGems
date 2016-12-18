package net.silentchaos512.gems.item;

import java.util.List;
import java.util.Set;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemModelMesher;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemTool;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.silentchaos512.gems.SilentGems;
import net.silentchaos512.gems.api.lib.EnumMaterialGrade;
import net.silentchaos512.gems.api.lib.EnumMaterialTier;
import net.silentchaos512.gems.api.lib.EnumPartPosition;
import net.silentchaos512.gems.api.tool.part.ToolPart;
import net.silentchaos512.gems.api.tool.part.ToolPartRegistry;
import net.silentchaos512.gems.api.tool.part.ToolPartTip;
import net.silentchaos512.gems.client.key.KeyTracker;
import net.silentchaos512.gems.client.render.ToolItemOverrideHandler;
import net.silentchaos512.gems.client.render.ToolModel;
import net.silentchaos512.gems.item.tool.ItemGemAxe;
import net.silentchaos512.gems.item.tool.ItemGemBow;
import net.silentchaos512.gems.item.tool.ItemGemHoe;
import net.silentchaos512.gems.item.tool.ItemGemShield;
import net.silentchaos512.gems.item.tool.ItemGemShovel;
import net.silentchaos512.gems.item.tool.ItemGemSword;
import net.silentchaos512.gems.item.tool.ItemGemTomahawk;
import net.silentchaos512.gems.lib.TooltipHelper;
import net.silentchaos512.gems.util.ToolHelper;
import net.silentchaos512.lib.util.LocalizationHelper;

public class ToolRenderHelper extends ToolRenderHelperBase {

  public static ToolRenderHelper getInstance() {

    return (ToolRenderHelper) instance;
  }

  public static final String NBT_MODEL_INDEX = "SGModel";
  public static final String SMART_MODEL_NAME = SilentGems.MOD_ID + ":Tool";
  public static final ModelResourceLocation SMART_MODEL = new ModelResourceLocation(
      SMART_MODEL_NAME, "inventory");

  public static final int DARK_GEM_SHADE = 0x999999;

  // public static final Pattern numberFormat = Pattern.compile("\\d+\\.\\d{3}");

  protected Set<ModelResourceLocation> modelSet = null;
  protected ModelResourceLocation[] models;
  protected ModelResourceLocation[] arrowModels;

  public ModelResourceLocation modelBlank;
  public ModelResourceLocation modelError;

  @Override
  public void addInformation(ItemStack tool, EntityPlayer player, List list, boolean advanced) {

    LocalizationHelper loc = SilentGems.instance.localizationHelper;
    boolean controlDown = KeyTracker.isControlDown();
    boolean altDown = KeyTracker.isAltDown();
    boolean shiftDown = KeyTracker.isShiftDown();
    String line;

    // Show original owner?
    if (controlDown) {
      String owner = ToolHelper.getOriginalOwner(tool);
      if (owner.equals(SilentGems.localizationHelper.getMiscText("Tooltip.OriginalOwner.Creative")))
        owner = TextFormatting.LIGHT_PURPLE + owner;

      if (!owner.isEmpty())
        list.add(loc.getMiscText("Tooltip.OriginalOwner", owner));
      else
        list.add(loc.getMiscText("Tooltip.OriginalOwner.Unknown"));
    }

    // Broken?
    if (ToolHelper.isBroken(tool)) {
      line = loc.getMiscText("Tooltip.Broken");
      list.add(line);
    }

    // Tipped upgrade
    ToolPartTip partTip = (ToolPartTip) ToolHelper.getConstructionTip(tool);
    if (partTip != null) {
      String tipName = partTip.getUnlocalizedName().replaceFirst("[^:]+:", "");
      tipName = loc.getMiscText("Tooltip." + tipName);
      line = loc.getMiscText("Tooltip.Tipped", tipName);
      list.add(line);
    }

    final Item item = tool.getItem();
    final boolean isSword = item instanceof ItemGemSword;
    final boolean isAxe = item instanceof ItemGemAxe;
    final boolean isWeapon = isSword || isAxe;
    final boolean isCaster = isSword
        && ToolHelper.getToolTier(tool).ordinal() >= EnumMaterialTier.SUPER.ordinal();
    final boolean isBow = item instanceof ItemGemBow;
    final boolean isDigger = item instanceof ItemTool;
    final boolean isShield = item instanceof ItemGemShield;

    final String sep = loc.getMiscText("Tooltip.Separator");

    if (controlDown) {
      // Properties Header
      line = loc.getMiscText("Tooltip.Properties");
      list.add(line);

      TextFormatting color = TextFormatting.YELLOW;

      int durabilityMax = ToolHelper.getMaxDamage(tool);
      int durability = durabilityMax - tool.getItemDamage();
      String s1 = String.format(durability > 9999 ? "%,d" : "%d", durability);
      String s2 = String.format(durabilityMax > 9999 ? "%,d" : "%d", durabilityMax);
      line = loc.getMiscText("Tooltip.Durability", s1 + " / " + s2);
      list.add(color + "  " + line);

      if (isShield)
        list.add(color + getTooltipLine("BlockingPower", ToolHelper.getBlockingPower(tool)));

      if (isDigger) { // @formatter:off
        list.add(color + getTooltipLine("HarvestLevel", ToolHelper.getHarvestLevel(tool)));
        list.add(color + getTooltipLine("HarvestSpeed", ToolHelper.getDigSpeedOnProperMaterial(tool)));
      } // @formatter:on

      if (isWeapon) {
        list.add(color + getTooltipLine("MeleeSpeed", ToolHelper.getMeleeSpeedModifier(tool) + 4)
            .replaceFirst("%", ""));
        list.add(color + getTooltipLine("MeleeDamage", ToolHelper.getMeleeDamageModifier(tool)));
        if (isCaster)
          list.add(color + getTooltipLine("MagicDamage", ToolHelper.getMagicDamageModifier(tool)));
      }

      if (isBow) {
        list.add(color + getTooltipLine("DrawDelay", ModItems.bow.getDrawDelay(tool)));
        list.add(color + getTooltipLine("ArrowDamage", 2f + ModItems.bow.getArrowDamage(tool)));
      }

      list.add(color + getTooltipLine("ChargeSpeed", ToolHelper.getChargeSpeed(tool)));

      // Statistics Header
      list.add(sep);
      line = loc.getMiscText("Tooltip.Statistics");
      list.add(line);

      list.add(getTooltipLine("BlocksMined", ToolHelper.getStatBlocksMined(tool)));

      if (isDigger) {
        list.add(getTooltipLine("BlocksPlaced", ToolHelper.getStatBlocksPlaced(tool)));
      }

      if (item instanceof ItemGemShovel) {
        list.add(getTooltipLine("PathsMade", ToolHelper.getStatPathsMade(tool)));
      }

      if (item instanceof ItemGemHoe) {
        list.add(getTooltipLine("BlocksTilled", ToolHelper.getStatBlocksTilled(tool)));
      }

      list.add(getTooltipLine("HitsLanded", ToolHelper.getStatHitsLanded(tool)));

      if (isCaster || isBow)
        list.add(getTooltipLine("ShotsFired", ToolHelper.getStatShotsFired(tool)));

      if (item instanceof ItemGemTomahawk)
        list.add(getTooltipLine("ThrownCount", ToolHelper.getStatThrownCount(tool)));

      if (isWeapon)
        list.add(getTooltipLine("KillCount", ToolHelper.getStatKillCount(tool)));

      list.add(getTooltipLine("Redecorated", ToolHelper.getStatRedecorated(tool)));
      list.add(sep);
    } else {
      list.add(TextFormatting.GOLD + loc.getMiscText("PressCtrl"));
    }

    if (altDown) {
      line = loc.getMiscText("Tooltip.Construction");
      list.add(line);

      ToolPart[] parts = ToolHelper.getConstructionParts(tool);
      EnumMaterialGrade[] grades = ToolHelper.getConstructionGrades(tool);

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

    // Debug render layers
    if (controlDown && shiftDown && tool.hasTagCompound()) {
      if (!altDown)
        list.add(sep);
      for (EnumPartPosition pos : EnumPartPosition.values()) {
        NBTTagCompound tags = tool.getTagCompound().getCompoundTag(NBT_MODEL_INDEX);
        if (tags != null) {
          String key = "Layer" + pos.ordinal();
          String str = "%s: %d, %X";
          str = String.format(str, key, tags.getInteger(key), tags.getInteger(key + "Color"));
          list.add(str);
        }
      }
    }
  }

  public String getTooltipLine(String key, int value) {

    // String number;
    // if (value > 9999)
    // number = "%,d";
    // else
    // number = "%d";
    //
    // number = String.format(number, value);
    // String line = SilentGems.instance.localizationHelper.getMiscText("Tooltip." + key, number);
    // return " " + line;

    return TooltipHelper.get(key, value, true);
  }

  public String getTooltipLine(String key, float value) {

    // String number = "%.2f";
    //
    // number = String.format(number, value);
    // String line = SilentGems.instance.localizationHelper.getMiscText("Tooltip." + key, number);
    // return " " + line;

    return TooltipHelper.get(key, value, true);
  }

  @SubscribeEvent
  public void onModelBake(ModelBakeEvent event) {

    log.info("ToolRenderHelper.onModelBake");
    Object object = event.getModelRegistry().getObject(SMART_MODEL);
    if (object instanceof IBakedModel) {
      IBakedModel existingModel = (IBakedModel) object;
      ToolModel customModel = new ToolModel(existingModel);
      event.getModelRegistry().putObject(SMART_MODEL, customModel);
      ToolItemOverrideHandler.baseModel = customModel;
    }
  }

  /**
   * Creates the list of all possible models.
   */
  protected void buildModelSet() {

    if (modelSet != null) {
      return;
    }

    Set<ModelResourceLocation> set = Sets.newConcurrentHashSet();

    for (ToolPart part : ToolPartRegistry.getValues()) {
      for (EnumPartPosition pos : EnumPartPosition.values()) {
        for (Item itemTool : ModItems.tools) {
          for (int frame = 0; frame < (itemTool instanceof ItemGemBow ? 4 : 1); ++frame) {
            ModelResourceLocation model = part.getModel(new ItemStack(itemTool), pos, frame);
            if (model != null) {
              set.add(model);
            }
          }
        }
      }
    }

    // Bow "arrow" models
    arrowModels = new ModelResourceLocation[8];
    for (int i = 0; i < 8; ++i) {
      String tier = i < 4 ? "Regular" : "Super";
      ModelResourceLocation model = new ModelResourceLocation(
          SilentGems.MOD_ID + ":bow/BowArrow" + tier + (i & 3));
      if (model != null)
        set.add(model);
      arrowModels[i] = model;
    }

    modelSet = set;
    models = set.toArray(new ModelResourceLocation[set.size()]);
  }

  /**
   * Gets the animation frame for bows. Returns 0 for everything else.
   */
  public int getAnimationFrame(ItemStack tool) {

    if (tool != null && tool.getItem() instanceof ItemGemBow) {
      EntityPlayer player = Minecraft.getMinecraft().thePlayer;
      float pull = tool.getItem().getPropertyGetter(ItemGemBow.RESOURCE_PULL).apply(tool,
          player.worldObj, player);
      float pulling = tool.getItem().getPropertyGetter(ItemGemBow.RESOURCE_PULLING).apply(tool,
          player.worldObj, player);

      if (pull > 0.9f)
        return 3;
      if (pull > 0.65f)
        return 2;
      if (pulling > 0f)
        return 1;
    }

    return 0;
  }

  /**
   * Gets the model for the specified tool and position. Gets the animation frame on its own. Stores model index in tool
   * NBT for fast acess.
   */
  public ModelResourceLocation getModel(ItemStack tool, EnumPartPosition pos) {

    if (tool == null || !tool.hasTagCompound()) {
      return modelError;
    }

    NBTTagCompound tags = tool.getTagCompound().getCompoundTag(NBT_MODEL_INDEX);
    int frame = getAnimationFrame(tool);
    String key = "Layer" + pos.ordinal() + (frame > 0 ? "_" + frame : "");
    boolean isBow = tool.getItem() instanceof ItemGemBow;

    if (!tags.hasKey(key)) {
      // Model is currently not indexed! We'll need to figure out what it should be.

      // Bow "arrow" models
      if (pos == EnumPartPosition.ROD_GRIP && isBow) {
        return getArrowModel(tool, frame);
      }

      // Get the render part for this position.
      ToolPart part = ToolHelper.getRenderPart(tool, pos);
      if (part == null) {
        tags.setInteger(key, -1);
        tool.getTagCompound().setTag(NBT_MODEL_INDEX, tags);
        return null;
      }

      // Get the desired model for the current position and animation frame.
      ModelResourceLocation target = part.getModel(tool, pos, frame);
      // Find the model in the list. Store the index in NBT for fast access.
      for (int i = 0; i < models.length; ++i) {
        if (models[i].equals(target)) {
          tags.setInteger(key, i);
          tool.getTagCompound().setTag(NBT_MODEL_INDEX, tags);
          return target;
        }
      }
      return modelError;
    }

    // Grab the indexed model.
    return getModel(tags.getInteger(key));
  }

  /**
   * Gets an indexed model (index stored in tool NBT) for performance reasons. Without this, the framerate takes a HUGE
   * drop!
   */
  public ModelResourceLocation getModel(int index) {

    if (index < 0) {
      return null;
    } else if (index < models.length) {
      return models[index];
    } else {
      return modelError;
    }
  }

  /**
   * Gets the arrow model for the animation frame for bows.
   */
  public ModelResourceLocation getArrowModel(ItemStack tool, int frame) {

    if (frame < 0 || frame > 3)
      return null;
    if (ToolHelper.isBroken(tool))
      return modelBlank;
    boolean superTier = ToolHelper.getToolTier(tool).ordinal() >= EnumMaterialTier.SUPER.ordinal();
    return arrowModels[superTier ? frame + 4 : frame];
  }

  @Override
  public List<ModelResourceLocation> getVariants() {

    buildModelSet();
    List ret = Lists.newArrayList(modelSet);

    // Error models
    modelError = new ModelResourceLocation(SilentGems.MOD_ID + ":Error", "inventory");
    ret.add(modelError);
    ret.add(new ModelResourceLocation(SilentGems.MOD_ID + ":sword/_error", "inventory"));
    ret.add(new ModelResourceLocation(SilentGems.MOD_ID + ":katana/_error", "inventory"));
    ret.add(new ModelResourceLocation(SilentGems.MOD_ID + ":scepter/_error", "inventory"));
    ret.add(new ModelResourceLocation(SilentGems.MOD_ID + ":tomahawk/_error", "inventory"));
    ret.add(new ModelResourceLocation(SilentGems.MOD_ID + ":pickaxe/_error", "inventory"));
    ret.add(new ModelResourceLocation(SilentGems.MOD_ID + ":shovel/_error", "inventory"));
    ret.add(new ModelResourceLocation(SilentGems.MOD_ID + ":axe/_error", "inventory"));
    ret.add(new ModelResourceLocation(SilentGems.MOD_ID + ":hoe/_error", "inventory"));
    ret.add(new ModelResourceLocation(SilentGems.MOD_ID + ":sickle/_error", "inventory"));

    return ret;
  }

  @Override
  public boolean registerModels() {

    ItemModelMesher mesher = Minecraft.getMinecraft().getRenderItem().getItemModelMesher();
    int index = 0;

    for (ModelResourceLocation model : modelSet) {
      mesher.register(this, index++, model);
    }

    // Error models
    mesher.register(this, index++,
        new ModelResourceLocation(SilentGems.MOD_ID + ":Error", "inventory"));
    mesher.register(this, index++,
        new ModelResourceLocation(SilentGems.MOD_ID + ":sword/_error", "inventory"));
    mesher.register(this, index++,
        new ModelResourceLocation(SilentGems.MOD_ID + ":katana/_error", "inventory"));
    mesher.register(this, index++,
        new ModelResourceLocation(SilentGems.MOD_ID + ":scepter/_error", "inventory"));
    mesher.register(this, index++,
        new ModelResourceLocation(SilentGems.MOD_ID + ":tomahawk/_error", "inventory"));
    mesher.register(this, index++,
        new ModelResourceLocation(SilentGems.MOD_ID + ":pickaxe/_error", "inventory"));
    mesher.register(this, index++,
        new ModelResourceLocation(SilentGems.MOD_ID + ":shovel/_error", "inventory"));
    mesher.register(this, index++,
        new ModelResourceLocation(SilentGems.MOD_ID + ":axe/_error", "inventory"));
    mesher.register(this, index++,
        new ModelResourceLocation(SilentGems.MOD_ID + ":hoe/_error", "inventory"));
    mesher.register(this, index++,
        new ModelResourceLocation(SilentGems.MOD_ID + ":sickle/_error", "inventory"));

    return true;
  }
}
