package net.silentchaos512.gems.item;

import java.util.List;
import java.util.Set;

import org.lwjgl.input.Keyboard;

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
import net.silentchaos512.gems.api.lib.EnumMaterialTier;
import net.silentchaos512.gems.api.lib.EnumPartPosition;
import net.silentchaos512.gems.api.tool.part.ToolPart;
import net.silentchaos512.gems.api.tool.part.ToolPartRegistry;
import net.silentchaos512.gems.client.render.ToolItemOverrideHandler;
import net.silentchaos512.gems.client.render.ToolModel;
import net.silentchaos512.gems.item.tool.ItemGemHoe;
import net.silentchaos512.gems.item.tool.ItemGemSword;
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

  // public static final Pattern numberFormat = Pattern.compile("\\d+\\.\\d{3}");

  protected Set<ModelResourceLocation> modelSet = null;
  protected ModelResourceLocation[] models;

  public ModelResourceLocation modelBlank;
  public ModelResourceLocation modelError;

  private static int registerIndex = 0;
  private static int modelCount = 0;

  @Override
  public void addInformation(ItemStack tool, EntityPlayer player, List list, boolean advanced) {

    LocalizationHelper loc = SilentGems.instance.localizationHelper;
    boolean shifted = Keyboard.isKeyDown(Keyboard.KEY_LCONTROL)
        || Keyboard.isKeyDown(Keyboard.KEY_RCONTROL);
    String line;

    if (!shifted) {
      list.add(TextFormatting.GOLD + loc.getMiscText("PressCtrl"));
      return;
    }

    final boolean isWeapon = tool.getItem() instanceof ItemGemSword;
    final boolean isCaster = isWeapon && ToolHelper.getToolTier(tool) == EnumMaterialTier.SUPER;
    final boolean isBow = false; // TODO
    final boolean isDigger = tool.getItem() instanceof ItemTool;

    final String sep = loc.getMiscText("Tooltip.Separator");

    // Properties Header
    line = loc.getMiscText("Tooltip.Properties");
    list.add(line);

    list.add(getTooltipLine("Durability", ToolHelper.getMaxDamage(tool)));

    if (isDigger) {
      list.add(getTooltipLine("HarvestLevel", ToolHelper.getHarvestLevel(tool)));
      list.add(getTooltipLine("HarvestSpeed", ToolHelper.getDigSpeedOnProperMaterial(tool)));
    }

    if (isWeapon) {
      list.add(getTooltipLine("MeleeDamage", ToolHelper.getMeleeDamageModifier(tool)));
      list.add(getTooltipLine("MagicDamage", ToolHelper.getMagicDamageModifier(tool)));
    }

    list.add(getTooltipLine("ChargeSpeed", ToolHelper.getChargeSpeed(tool)));

    // Statistics Header
    list.add(sep);
    line = loc.getMiscText("Tooltip.Statistics");
    list.add(line);

    list.add(getTooltipLine("BlocksMined", ToolHelper.getStatBlocksMined(tool)));

    if (isDigger) {
      list.add(getTooltipLine("BlocksPlaced", ToolHelper.getStatBlocksPlaced(tool)));
    }

    if (tool.getItem() instanceof ItemGemHoe) {
      list.add(getTooltipLine("BlocksTilled", ToolHelper.getStatBlocksTilled(tool)));
    }

    list.add(getTooltipLine("HitsLanded", ToolHelper.getStatHitsLanded(tool)));

    if (isCaster || isBow) {
      list.add(getTooltipLine("ShotsFired", ToolHelper.getStatShotsFired(tool)));
      // list.add(getTooltipLine("ShotsLanded", ToolHelper.getStatShotsLanded(tool)));
    }

    list.add(getTooltipLine("Redecorated", ToolHelper.getStatRedecorated(tool)));
  }

  private String getTooltipLine(String key, int value) {

    String line = SilentGems.instance.localizationHelper.getMiscText("Tooltip." + key);
    String number;
    if (value > 9999)
      number = "%,d";
    else
      number = "%d";

    number = String.format(number, value);
    line = String.format(line, number);
    return "  " + line;
  }

  private String getTooltipLine(String key, float value) {

    String line = SilentGems.instance.localizationHelper.getMiscText("Tooltip." + key);
    String number = "%.2f";

    number = String.format(number, value);
    line = String.format(line, number);
    return "  " + line;
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

  protected void buildModelSet() {

    if (modelSet != null) {
      return;
    }

    Set<ModelResourceLocation> set = Sets.newConcurrentHashSet();

    for (ToolPart part : ToolPartRegistry.getValues()) {
      for (EnumPartPosition pos : EnumPartPosition.values()) {
        for (Item itemTool : ModItems.tools) {
          ModelResourceLocation model = part.getModel(new ItemStack(itemTool), pos);
          if (model != null) {
            set.add(model);
          }
        }
      }
    }

    modelSet = set;
    models = set.toArray(new ModelResourceLocation[set.size()]);
  }

  public ModelResourceLocation getModel(ItemStack tool, EnumPartPosition pos) {

    if (tool == null || !tool.hasTagCompound()) {
      return modelError;
    }

    NBTTagCompound tags = tool.getTagCompound().getCompoundTag(NBT_MODEL_INDEX);
    String key = "Layer" + pos.ordinal();

    if (!tags.hasKey(key)) {
      ToolPart part = ToolHelper.getRenderPart(tool, pos);
      if (part == null) {
        tags.setInteger(key, -1);
        tool.getTagCompound().setTag(NBT_MODEL_INDEX, tags);
        return null;
      }
      ModelResourceLocation target = part.getModel(tool, pos);
      // SilentGems.instance.logHelper.debug(target);
      for (int i = 0; i < models.length; ++i) {
        if (models[i].equals(target)) {
          tags.setInteger(key, i);
          tool.getTagCompound().setTag(NBT_MODEL_INDEX, tags);
          return target;
        }
      }
      // SilentGems.instance.logHelper.derp();
      return modelError;
    }

    // SilentGems.instance.logHelper.debug(models.length, modelSet.size());
    // SilentGems.instance.logHelper.debug(pos.ordinal() + ": " + tool.getTagCompound().getInteger(key) + ": " +
    // getModel(tool.getTagCompound().getInteger(key)));
    return getModel(tags.getInteger(key));
  }

  public ModelResourceLocation getModel(int index) {

    // SilentGems.instance.logHelper.debug(index);
    if (index < 0) {
      return null;
    } else if (index < models.length) {
      // SilentGems.instance.logHelper.debug(models[index]);
      return models[index];
    } else {
      return modelError;
    }
  }

  @Override
  public List<ModelResourceLocation> getVariants() {

    buildModelSet();
    return Lists.newArrayList(modelSet);
  }

  @Override
  public boolean registerModels() {

    ItemModelMesher mesher = Minecraft.getMinecraft().getRenderItem().getItemModelMesher();
    int index = 0;

    for (ModelResourceLocation model : modelSet) {
      mesher.register(this, index++, model);
    }

    return true;
  }

  // FIXME
  // @Override
  // public int getColorFromItemStack(ItemStack tool, int pass) {
  //
  // return ToolHelper.getColorForPass(tool, pass);
  // }
}
