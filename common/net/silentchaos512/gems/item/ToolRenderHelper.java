package net.silentchaos512.gems.item;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.common.collect.MapMaker;
import com.google.common.collect.Sets;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemTool;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.client.model.ModelLoader;
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
import net.silentchaos512.gems.init.ModItems;
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
  public static final String SMART_MODEL_NAME = SilentGems.RESOURCE_PREFIX.toLowerCase() + "tool";
  public static final ModelResourceLocation SMART_MODEL = new ModelResourceLocation(
      SMART_MODEL_NAME, "inventory");

  protected Set<ModelResourceLocation> modelSet = null;
  protected ModelResourceLocation[] models;
  protected ModelResourceLocation[] arrowModels;

  public ModelResourceLocation modelBlank;
  public ModelResourceLocation modelError;
  public ModelResourceLocation modelGooglyEyes;

  @Override
  public void clAddInformation(ItemStack tool, World world, List list, boolean advanced) {

    LocalizationHelper loc = SilentGems.instance.localizationHelper;
    boolean controlDown = KeyTracker.isControlDown();
    boolean altDown = KeyTracker.isAltDown();
    boolean shiftDown = KeyTracker.isShiftDown();
    String line;

    // Tipped upgrade
    ToolPartTip partTip = (ToolPartTip) ToolHelper.getConstructionTip(tool);
    if (partTip != null) {
      String tipName = partTip.getUnlocalizedName().replaceFirst("[^:]+:", "");
      tipName = loc.getMiscText("Tooltip." + tipName);
      line = loc.getMiscText("Tooltip.Tipped", tipName);
      list.add(line);
    }

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

    // Example tool?
    if (tool.hasTagCompound() && tool.getTagCompound().hasKey(ToolHelper.NBT_EXAMPLE_TOOL_TIER)) {
      EnumMaterialTier tier = EnumMaterialTier.values()[tool.getTagCompound()
          .getInteger(ToolHelper.NBT_EXAMPLE_TOOL_TIER)];
      list.add(loc.getMiscText("Tooltip.ExampleTool", tier));
    }
    // Missing data?
    else if (ToolHelper.hasNoConstruction(tool)) {
      list.add(loc.getMiscText("Tooltip.NoData1"));
      list.add(loc.getMiscText("Tooltip.NoData2"));
    }
    // Broken?
    else if (ToolHelper.isBroken(tool)) {
      line = loc.getMiscText("Tooltip.Broken");
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

      // Tier
      EnumMaterialTier tier = ToolHelper.getToolTier(tool);
      line = TextFormatting.RESET + loc.getMiscText("ToolTier." + tier);
      list.add("  " + color + loc.getMiscText("ToolTier", line));

      int durabilityMax = ToolHelper.getMaxDamage(tool);
      int durability = durabilityMax - tool.getItemDamage();
      String s1 = String.format(durability > 9999 ? "%,d" : "%d", durability);
      String s2 = String.format(durabilityMax > 9999 ? "%,d" : "%d", durabilityMax);
      line = loc.getMiscText("Tooltip.Durability", s1 + " / " + s2);
      list.add(color + "  " + line);

      if (isShield) {
        float magicProtection = (int) (ToolHelper.getMagicProtection(tool) * 100);
        list.add(color + getTooltipLine("MagicProtection", magicProtection));
      }

      if (isDigger) { // @formatter:off
        int harvestLevel = ToolHelper.getHarvestLevel(tool);
        String str = color + getTooltipLine("HarvestLevel", harvestLevel);
        String key = "Tooltip.level" + harvestLevel;
        String val = SilentGems.localizationHelper.getMiscText(key);
        if (!val.equals("misc.silentgems:" + key)) str += " (" +  val + ")";
        list.add(str);
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
        list.add(color + getTooltipLine("DrawSpeed", ModItems.bow.getDrawSpeedForDisplay(tool)));
        list.add(color + getTooltipLine("ArrowDamage", ModItems.bow.getArrowDamageForDisplay(tool)));
      }

      list.add(color + getTooltipLine("ChargeSpeed", ToolHelper.getChargeSpeed(tool)));
    } else {
      list.add(TextFormatting.GOLD + loc.getMiscText("Tooltip.CtrlForProp"));
    }

    if (altDown) {
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
      list.add(TextFormatting.GOLD + loc.getMiscText("Tooltip.AltForStat"));
    }

    // Debug render layers
    if (controlDown && shiftDown && tool.hasTagCompound()) {
      if (!altDown)
        list.add(sep);
      list.add("Render Layers");
      for (EnumPartPosition pos : EnumPartPosition.values()) {
        NBTTagCompound tags = tool.getTagCompound().getCompoundTag(NBT_MODEL_INDEX);
        if (tags != null) {
          String key = "Layer" + pos.ordinal();
          String str = "  %s: %s, %d, %X";
          ToolPart renderPart = ToolHelper.getRenderPart(tool, pos);
          ModelResourceLocation model = renderPart == null ? null
              : renderPart.getModel(tool, pos, 0);
          str = String.format(str, pos.name(), model == null ? "null" : model.toString(),
              tags.getInteger(key), tags.getInteger(key + "Color"));
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

    // log.info("ToolRenderHelper.onModelBake");
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
      String tier = i < 4 ? "regular" : "super";
      ModelResourceLocation model = new ModelResourceLocation(
          SilentGems.RESOURCE_PREFIX + "bow/bow_arrow_" + tier + (i & 3));
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
      EntityPlayer player = Minecraft.getMinecraft().player;
      float pull = tool.getItem().getPropertyGetter(ItemGemBow.RESOURCE_PULL).apply(tool,
          player.world, player);
      float pulling = tool.getItem().getPropertyGetter(ItemGemBow.RESOURCE_PULLING).apply(tool,
          player.world, player);

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

    if (ToolHelper.isBroken(tool) || !tags.hasKey(key)) {
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
      ModelResourceLocation target = !ToolHelper.isBroken(tool) ? part.getModel(tool, pos, frame)
          : part.getBrokenModel(tool, pos, frame);

      // Find the model in the list. Store the index in NBT for fast access.
      for (int i = 0; i < models.length; ++i) {
        if (models[i].equals(target)) {
          tags.setInteger(key, i);
          tool.getTagCompound().setTag(NBT_MODEL_INDEX, tags);
          return target;
        }
      }
      return target;
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
  public void getModels(Map<Integer, ModelResourceLocation> models) {

    buildModelSet();
    modelSet.forEach(model -> models.put(models.size(), model));
    String prefix = SilentGems.RESOURCE_PREFIX.toLowerCase();

    // Extra models
    int i = models.size();
    modelGooglyEyes = new ModelResourceLocation(prefix + "googlyeyes", "inventory");
    models.put(i++, modelGooglyEyes);
    modelError = new ModelResourceLocation(prefix + "error", "inventory");
    models.put(i++, modelError);

    // Error and broken models.
    for (String str : new String[] { "sword", "dagger", "katana", "scepter", "tomahawk", "pickaxe",
        "shovel", "axe", "paxel", "hoe", "sickle", "bow" }) {
      models.put(i++, new ModelResourceLocation(prefix + str + "/_error", "inventory"));
      models.put(i++, new ModelResourceLocation(prefix + str + "/" + str + "_broken", "inventory"));
    }
  }
}
