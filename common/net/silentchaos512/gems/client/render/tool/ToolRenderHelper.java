package net.silentchaos512.gems.client.render.tool;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.model.IBakedModel;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MathHelper;
import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.silentchaos512.gems.SilentGems;
import net.silentchaos512.gems.core.registry.IHasVariants;
import net.silentchaos512.gems.core.registry.IRegisterModels;
import net.silentchaos512.gems.core.util.LogHelper;
import net.silentchaos512.gems.core.util.ToolHelper;
import net.silentchaos512.gems.item.ToolRenderHelperBase;
import net.silentchaos512.gems.item.tool.GemAxe;
import net.silentchaos512.gems.item.tool.GemBow;
import net.silentchaos512.gems.item.tool.GemHoe;
import net.silentchaos512.gems.item.tool.GemPickaxe;
import net.silentchaos512.gems.item.tool.GemShovel;
import net.silentchaos512.gems.item.tool.GemSickle;
import net.silentchaos512.gems.item.tool.GemSword;
import net.silentchaos512.gems.lib.EnumTipUpgrade;
import net.silentchaos512.gems.lib.Names;

/**
 * A "fake" item that registers models for tool parts and handles retrieving the correct model resource locations for
 * tools. Note that you may see the word "icon" in this file. If you do, assume it should be "model", as parts of this
 * file are directly from 1.7.
 */
@SuppressWarnings("deprecation")
public class ToolRenderHelper extends ToolRenderHelperBase
    implements IHasVariants, IRegisterModels {

  public static ToolRenderHelper getInstance() {

    return (ToolRenderHelper) instance;
  }

  /*
   * Models
   */

  private HashMap<Integer, String> modelKeys = new HashMap<Integer, String>();

  // @SideOnly(Side.CLIENT)
  // private ModelResourceLocation[] smartModels = new ModelResourceLocation[BOW_STAGE_COUNT];

  // Shared
  public ModelResourceLocation modelBlank; // A completely transparent texture.
  public ModelResourceLocation modelError; // Shows up if I screw up.
  public ModelResourceLocation[] modelMainRodDeco; // Rod decoration used by most tools.
  public ModelResourceLocation[] modelMainRodWool; // Rod wool grip used by most tools.

  // Specific tool model collections.
  public final ToolModelCollection swordModels = new ToolModelCollection();
  public final ToolModelCollection pickaxeModels = new ToolModelCollection();
  public final ToolModelCollection shovelModels = new ToolModelCollection();
  public final ToolModelCollection axeModels = new ToolModelCollection();
  public final ToolModelCollection hoeModels = new ToolModelCollection();
  public final ToolModelCollection sickleModels = new ToolModelCollection();
  public final ToolModelCollection bow0Models = new ToolModelCollection();
  public final ToolModelCollection bow1Models = new ToolModelCollection();
  public final ToolModelCollection bow2Models = new ToolModelCollection();
  public final ToolModelCollection bow3Models = new ToolModelCollection();

  /**
   * Incremented throughtout model registration, both simulated and not. Reset when registerModels is called.
   */
  private static int registerIndex = 0;
  /**
   * Stores the total number of part models after registerModels (either simulated or not) is called.
   */
  private static int modelCount = 0;

  /**
   * Swaps the base tool models for ToolSmartModel.
   * 
   * @param event
   */
  @SideOnly(Side.CLIENT)
  public void onModelBake(ModelBakeEvent event) {

    LogHelper.info("Swapping tool models for smart models...");

    // SRegistry keeps a list of all tool models registered.
    // for (ModelResourceLocation modelLocation : SRegistry.toolBaseModels) {
    // Object object = event.modelRegistry.getObject(modelLocation);
    // if (object instanceof IBakedModel) {
    // IBakedModel existingModel = (IBakedModel) object;
    // ToolSmartModel customModel = new ToolSmartModel(existingModel);
    // // Replace existing model with the smart model.
    // event.modelRegistry.putObject(modelLocation, customModel);
    // }
    // }

    // "Frames" no longer used, right?
    for (int i = 0; i < BOW_STAGE_COUNT; ++i) {
      ModelResourceLocation modelLocation = new ModelResourceLocation(SMART_MODEL_NAME + i,
          "inventory");
      // smartModels[i] = modelLocation;
      Object object = event.modelRegistry.getObject(modelLocation);
      if (object instanceof IBakedModel) {
        IBakedModel existingModel = (IBakedModel) object;
        ToolSmartModel customModel = new ToolSmartModel(existingModel);
        event.modelRegistry.putObject(modelLocation, customModel);
      }
    }

    // Broken tool model
    ModelResourceLocation modelLocation = new ModelResourceLocation(BROKEN_SMART_MODEL_NAME,
        "inventory");
    Object object = event.modelRegistry.getObject(modelLocation);
    if (object instanceof IBakedModel) {
      IBakedModel existingModel = (IBakedModel) object;
      BrokenToolSmartModel customModel = new BrokenToolSmartModel(existingModel);
      event.modelRegistry.putObject(modelLocation, customModel);
    }
  }

  /**
   * Called in SRegistry for all blocks/items that implement IRegisterModels. This is the chance to register whatever
   * models we need!
   */
  @SideOnly(Side.CLIENT)
  @Override
  public void registerModels() {

    registerModels(false);
  }

  /**
   * Registers models, or simulates it to get a model count.
   * 
   * @param dryRun
   *          If true, registration is not done, but the number of models will be calculated and stored in modelCount.
   */
  @SideOnly(Side.CLIENT)
  public void registerModels(boolean dryRun) {

    if (!dryRun) {
      LogHelper.info("Registering tool part models...");
    }

    // Reset registerIndex, but not modelCount!
    registerIndex = 0;

    // Shared models (includes shared rod deco/wool models)
    modelBlank = registerModel("Blank", dryRun);
    modelError = registerModel("Error", dryRun);
    modelMainRodDeco = new ModelResourceLocation[ROD_DECO_TYPE_COUNT];
    for (int i = 0; i < modelMainRodDeco.length; ++i) {
      modelMainRodDeco[i] = registerModel("ToolDeco" + i, dryRun);
    }
    modelMainRodWool = new ModelResourceLocation[ROD_WOOL_TYPE_COUNT];
    for (int i = 0; i < modelMainRodWool.length; ++i) {
      modelMainRodWool[i] = registerModel("RodWool" + i, dryRun);
    }

    // Tool-specific models
    for (String toolClass : ToolHelper.TOOL_CLASSES) {
      registerModelsForCollection(toolClass, 0, dryRun);
      if (toolClass.equals("Bow")) {
        registerModelsForCollection(toolClass, 1, dryRun);
        registerModelsForCollection(toolClass, 2, dryRun);
        registerModelsForCollection(toolClass, 3, dryRun);
      }
    }

    if (!dryRun) {
      LogHelper.info("Done with tool part models!");
    }
  }

  /**
   * Registers all models for a specific collection.
   * 
   * @param toolClass
   * @param index
   * @param dryRun
   */
  @SideOnly(Side.CLIENT)
  private void registerModelsForCollection(String toolClass, int index, boolean dryRun) {

    ToolModelCollection models = getCollectionByName(toolClass, index);
    String strIndex = index == 3 ? "_3" : ""; // Index 3 is fully drawn bows
    String prefix = toolClass.toLowerCase() + "/" + toolClass;

    // Head parts
    for (int head = 0; head < HEAD_TYPE_COUNT; ++head) {
      models.headM[head] = registerModel(prefix + head + "M" + strIndex, dryRun);
      models.headL[head] = registerModel(prefix + head + "L" + strIndex, dryRun);
      models.headR[head] = registerModel(prefix + head + "R" + strIndex, dryRun);
    }

    // Rods
    if (toolClass.equals("Bow")) {
      models.rod[0] = registerModel(prefix + "_MainNormal" + index, dryRun);
      models.rod[1] = registerModel(prefix + "_MainOrnate" + index, dryRun);
    } else {
      models.rod[0] = registerModel(prefix + "_RodNormal", dryRun);
      models.rod[1] = registerModel(prefix + "_RodOrnate", dryRun);
    }

    // Tips
    for (int tip = 0; tip < TIP_TYPE_COUNT; ++tip) {
      models.tip[tip] = registerModel(
          prefix + "Tip" + (tip == TIP_TYPE_COUNT - 1 ? 0 : tip) + strIndex, dryRun);
    }

    // Deco bits (swords and bows, others use main)
    if (toolClass.equals("Sword") || toolClass.equals("Bow")) {
      for (int deco = 0; deco < ROD_DECO_TYPE_COUNT; ++deco) {
        models.rodDeco[deco] = registerModel(prefix + "Deco" + deco, dryRun);
      }
    } else {
      models.rodDeco = modelMainRodDeco;
    }

    // Wool (swords and sickles, others use main, bows register arrows)
    if (toolClass.equals("Sword") || toolClass.equals("Sickle")) {
      for (int wool = 0; wool < ROD_WOOL_TYPE_COUNT; ++wool) {
        models.rodWool[wool] = registerModel(prefix + "Wool" + wool, dryRun);
      }
    } else if (toolClass.equals("Bow")) {
      // Bows use first index of the wool array for arrows (just reusing the render pass).
      // The other 15 positions are set to the blank texture. Index 0 has no arrow.
      for (int i = 0; i < ROD_WOOL_TYPE_COUNT; ++i) {
        if (i > 0 || index == 0) {
          models.rodWool[i] = modelBlank;
        } else {
          models.rodWool[i] = registerModel("bow/Bow_Arrow" + index, dryRun);
        }
      }
    } else {
      models.rodWool = modelMainRodWool;
    }
  }

  /**
   * Registers a specific model.
   * 
   * @param name
   * @param dryRun
   * @return
   */
  @SideOnly(Side.CLIENT)
  private ModelResourceLocation registerModel(String name, boolean dryRun) {

    ModelResourceLocation location = new ModelResourceLocation(SilentGems.MOD_ID + ":" + name,
        "inventory");

    if (!dryRun) {
      // If not simulating, we should actually register the model.
      Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(this, registerIndex++,
          location);
    } else {
      // Otherwise, we're just calculating model count and pairing up integer keys with the names.
      modelKeys.put(registerIndex++, name);
    }

    modelCount = Math.max(registerIndex, modelCount);
    return location;
  }

  @Override
  public int getColorFromItemStack(ItemStack stack, int pass) {

    // FIXME: Doesn't work
    if (pass == PASS_TIP) {
      EnumTipUpgrade tip = EnumTipUpgrade.getById(ToolHelper.getToolHeadTip(stack));
      return tip.getColor();
    }
    return 0xFFFFFF;
  }

  /**
   * Gets the names of all models.
   */
  @Override
  public String[] getVariantNames() {

    // Need the model count. Simulate registering models...
    registerModels(true);

    List<String> list = new ArrayList<String>();
    for (int i = 0; i < modelCount; ++i) {
      list.add(SilentGems.MOD_ID + ":" + modelKeys.get(i));
    }
    return list.toArray(new String[list.size()]);
  }

  // Required by IHasVariants, but not used.
  @Override
  public String getName() {

    return Names.convert("ToolRenderItem"); // Note: it's bad practice to hard-code the name!
  }

  // Required by IHasVariants, but not used.
  @Override
  public String getFullName() {

    return Names.convert(SilentGems.MOD_ID + ":" + getName());
  }

  /**
   * Gets the model collection for the specified tool class and animation index.
   * 
   * @param toolClass
   * @param index
   * @return
   */
  @SideOnly(Side.CLIENT)
  private ToolModelCollection getCollectionByName(String toolClass, int index) {

    if (toolClass.equals("Sword")) {
      return swordModels;
    } else if (toolClass.equals("Pickaxe")) {
      return pickaxeModels;
    } else if (toolClass.equals("Shovel")) {
      return shovelModels;
    } else if (toolClass.equals("Axe")) {
      return axeModels;
    } else if (toolClass.equals("Hoe")) {
      return hoeModels;
    } else if (toolClass.equals("Sickle")) {
      return sickleModels;
    } else if (toolClass.equals("Bow")) {
      switch (index) {
        case 0:
          return bow0Models;
        case 1:
          return bow1Models;
        case 2:
          return bow2Models;
        case 3:
          return bow3Models;
        default:
          LogHelper.severe("ToolRenderHelper.getCollectionByName: Unknown bow index: " + index);
          return null;
      }
    } else {
      LogHelper.severe("ToolRenderHelper.getCollectionByName: Unknown tool class: " + toolClass);
      return null;
    }
  }

  public ModelResourceLocation getModel(ItemStack stack, int pass) {

    int gemId = ToolHelper.getToolGemId(stack);
    boolean supercharged = ToolHelper.getToolIsSupercharged(stack);
    return getModel(stack, pass, gemId, supercharged);
  }

  /**
   * Gets the part model for the given tool and render pass.
   * 
   * @param stack
   *          The tool
   * @param pass
   *          The render pass
   * @param gemId
   *          The gem ID of the base tool
   * @param supercharged
   *          The supercharged value of the base tool
   * @return The appropriate tool part model
   */
  @SideOnly(Side.CLIENT)
  public ModelResourceLocation getModel(ItemStack stack, int pass, int gemId,
      boolean supercharged) {

    return getModel(stack, pass, gemId, supercharged, stack.getMaxItemUseDuration());
  }

  /**
   * Gets the part model for the given tool, render pass, and use time.
   * 
   * @param stack
   *          The tool
   * @param pass
   *          The render pass
   * @param gemId
   *          The gem ID of the base tool
   * @param supercharged
   *          The supercharged value of the base tool
   * @param usingItem
   *          Also the tool?
   * @param useRemaining
   *          Use time remaining
   * @return
   */
  @SideOnly(Side.CLIENT)
  public ModelResourceLocation getModel(ItemStack stack, int pass, int gemId, boolean supercharged,
      int animationFrame) {

    ToolModelCollection models;
    Item item = stack.getItem();

    if (item instanceof GemSword) {
      models = swordModels;
    } else if (item instanceof GemPickaxe) {
      models = pickaxeModels;
    } else if (item instanceof GemShovel) {
      models = shovelModels;
    } else if (item instanceof GemAxe) {
      models = axeModels;
    } else if (item instanceof GemHoe) {
      models = hoeModels;
    } else if (item instanceof GemSickle) {
      models = sickleModels;
    } else if (item instanceof GemBow) {
      switch (animationFrame) {
        case 0:
          models = bow0Models;
          break;
        case 1:
          models = bow1Models;
          break;
        case 2:
          models = bow2Models;
          break;
        case 3:
          models = bow3Models;
          break;
        default:
          models = bow0Models;
      }
    } else {
      return modelError;
    }

    switch (pass) {
      case PASS_ROD:
        return getRodModel(models, stack, supercharged);
      case PASS_ROD_DECO:
        return getRodDecoModel(models, stack, supercharged);
      case PASS_ROD_WOOL:
        return getRodWoolModel(models, stack, supercharged);
      case PASS_HEAD_M:
        return getHeadMiddleModel(models, stack, gemId);
      case PASS_HEAD_L:
        return getHeadLeftModel(models, stack, gemId);
      case PASS_HEAD_R:
        return getHeadRightModel(models, stack, gemId);
      case PASS_TIP:
        return getTipModel(models, stack, gemId);
      default:
        return modelBlank;
    }
  }

  /*
   * The following methods get part models for various "render passes".
   */

  @SideOnly(Side.CLIENT)
  public ModelResourceLocation getRodModel(ToolModelCollection icons, ItemStack stack,
      boolean supercharged) {

    return supercharged ? icons.rod[1] : icons.rod[0];
  }

  @SideOnly(Side.CLIENT)
  public ModelResourceLocation getRodDecoModel(ToolModelCollection icons, ItemStack stack,
      boolean supercharged) {

    // Regular tools have no rod decoration!
    if (!supercharged) {
      return modelBlank;
    }

    int k = ToolHelper.getToolRodDeco(stack);
    if (k > -1) {
      k = MathHelper.clamp_int(k, 0, ROD_DECO_TYPE_COUNT - 1);
      return icons.rodDeco[k];
    }
    return icons.rodDeco[12];
  }

  @SideOnly(Side.CLIENT)
  public ModelResourceLocation getRodWoolModel(ToolModelCollection icons, ItemStack stack,
      boolean supercharged) {

    if (stack.getItem() instanceof GemBow) {
      // Bow arrow texture.
      return icons.rodWool[0];
    }

    int k = ToolHelper.getToolRodWool(stack);
    if (k > -1) {
      k = MathHelper.clamp_int(k, 0, ROD_WOOL_TYPE_COUNT - 1);
      return icons.rodWool[k];
    }

    return modelBlank;
  }

  @SideOnly(Side.CLIENT)
  public ModelResourceLocation getHeadMiddleModel(ToolModelCollection icons, ItemStack stack,
      int gemId) {

    int k = ToolHelper.getToolHeadMiddle(stack);
    if (k > -1) {
      k = MathHelper.clamp_int(k, 0, HEAD_TYPE_COUNT - 1);
      return icons.headM[k];
    }

    if (gemId >= 0 && gemId < HEAD_TYPE_COUNT) {
      return icons.headM[gemId];
    } else {
      return modelError;
    }
  }

  @SideOnly(Side.CLIENT)
  public ModelResourceLocation getHeadLeftModel(ToolModelCollection icons, ItemStack stack,
      int gemId) {

    int k = ToolHelper.getToolHeadLeft(stack);
    if (k > -1) {
      k = MathHelper.clamp_int(k, 0, HEAD_TYPE_COUNT - 1);
      return icons.headL[k];
    }

    if (gemId >= 0 && gemId < HEAD_TYPE_COUNT) {
      return icons.headL[gemId];
    } else {
      return modelError;
    }
  }

  @SideOnly(Side.CLIENT)
  public ModelResourceLocation getHeadRightModel(ToolModelCollection icons, ItemStack stack,
      int gemId) {

    int k = ToolHelper.getToolHeadRight(stack);
    if (k > -1) {
      k = MathHelper.clamp_int(k, 0, HEAD_TYPE_COUNT - 1);
      return icons.headR[k];
    }

    if (gemId >= 0 && gemId < HEAD_TYPE_COUNT) {
      return icons.headR[gemId];
    } else {
      return modelError;
    }
  }

  @SideOnly(Side.CLIENT)
  public ModelResourceLocation getTipModel(ToolModelCollection icons, ItemStack stack, int gemId) {

    int k = ToolHelper.getToolHeadTip(stack);
    if (k > -1) {
      k -= 1;
      if (k < 0) {
        return modelBlank;
      } else if (k > TIP_TYPE_COUNT - 1) {
        return icons.tip[TIP_TYPE_COUNT - 1];
      }
      return icons.tip[k];
    } else {
      return modelBlank;
    }
  }
}
