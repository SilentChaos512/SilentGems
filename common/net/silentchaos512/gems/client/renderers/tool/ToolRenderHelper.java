package net.silentchaos512.gems.client.renderers.tool;

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
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.silentchaos512.gems.SilentGems;
import net.silentchaos512.gems.core.registry.IHasVariants;
import net.silentchaos512.gems.core.registry.IRegisterModels;
import net.silentchaos512.gems.core.registry.SRegistry;
import net.silentchaos512.gems.core.util.LogHelper;
import net.silentchaos512.gems.core.util.ToolHelper;
import net.silentchaos512.gems.item.ModItems;
import net.silentchaos512.gems.item.tool.GemAxe;
import net.silentchaos512.gems.item.tool.GemBow;
import net.silentchaos512.gems.item.tool.GemHoe;
import net.silentchaos512.gems.item.tool.GemPickaxe;
import net.silentchaos512.gems.item.tool.GemShovel;
import net.silentchaos512.gems.item.tool.GemSickle;
import net.silentchaos512.gems.item.tool.GemSword;
import net.silentchaos512.gems.lib.EnumGem;
import net.silentchaos512.gems.lib.Names;
import net.silentchaos512.gems.material.ModMaterials;

/**
 * This no longer needs to be an item.
 */
@SuppressWarnings("deprecation")
@SideOnly(Side.CLIENT)
public class ToolRenderHelper extends Item implements IHasVariants, IRegisterModels {

  public static ToolRenderHelper instance;

  public static void init() {

    if (instance == null) {
      instance = ModItems.toolRenderHelper;
    }
  }

  /*
   * Constants
   */

  // The number of head types
  public static final int HEAD_TYPE_COUNT = 15;
  // The number of rod types
  public static final int ROD_TYPE_COUNT = 2;
  // The number of bow stages
  public static final int BOW_STAGE_COUNT = 4;
  // The number of rod gem decorations
  public static final int ROD_DECO_TYPE_COUNT = 15;
  // The number of wool grip types (shouldn't change)
  public static final int ROD_WOOL_TYPE_COUNT = 16;
  // The number of mining tool tips.
  public static final int TIP_TYPE_COUNT = 3;

  // Render pass IDs and count
  public static final int PASS_ROD = 0;
  public static final int PASS_HEAD_M = 1;
  public static final int PASS_HEAD_L = 2;
  public static final int PASS_HEAD_R = 3;
  public static final int PASS_ROD_DECO = 4;
  public static final int PASS_ROD_WOOL = 5;
  public static final int PASS_TIP = 6;
  public static final int RENDER_PASS_COUNT = 7;

  /*
   * Models
   */

  HashMap<Integer, String> modelKeys = new HashMap<Integer, String>();

  // Shared
  public ModelResourceLocation modelBlank; // A completely transparent texture.
  public ModelResourceLocation modelError; // Shows up if I screw up.
  public ModelResourceLocation[] modelMainRodDeco; // Rod decoration used by most tools.
  public ModelResourceLocation[] modelMainRodWool; // Rod wool grip used by most tools.

  // Specific tool icon collections.
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

  @SubscribeEvent
  public void onModelBake(ModelBakeEvent event) {

    LogHelper.info("Swapping tool models for smart models...");

    for (ModelResourceLocation modelLocation : SRegistry.toolBaseModels) {
      Object object = event.modelRegistry.getObject(modelLocation);
      if (object instanceof IBakedModel) {
        IBakedModel existingModel = (IBakedModel) object;
        ToolSmartModel customModel = new ToolSmartModel(existingModel);
        event.modelRegistry.putObject(modelLocation, customModel);
      }
    }

    LogHelper.info("Done with tool models!");
  }

  @Override
  public void registerModels() {

    registerModels(false);
  }
  
  public void registerModels(boolean dryRun) {

    LogHelper.info("Registering tool part models...");
    registerIndex = 0;

    // Shared models
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

    LogHelper.info("Done with tool part models!");
  }

  private void registerModelsForCollection(String toolClass, int index, boolean dryRun) {

    ToolModelCollection models = getCollectionByName(toolClass, 0);
    String strIndex = index == 3 ? "_3" : ""; // Index 3 is fully drawn bows
    // Head parts
    for (int head = 0; head < HEAD_TYPE_COUNT; ++head) {
      models.headM[head] = registerModel(toolClass + head + "M" + strIndex, dryRun);
      models.headL[head] = registerModel(toolClass + head + "L" + strIndex, dryRun);
      models.headR[head] = registerModel(toolClass + head + "R" + strIndex, dryRun);
    }

    // Rods
    if (toolClass.equals("Bow")) {
      models.rod[0] = registerModel(toolClass + "_MainNormal" + index, dryRun);
      models.rod[1] = registerModel(toolClass + "_MainOrnate" + index, dryRun);
    } else {
      models.rod[0] = registerModel(toolClass + "_RodNormal", dryRun);
      models.rod[1] = registerModel(toolClass + "_RodOrnate", dryRun);
    }

    // Tips
    for (int tip = 0; tip < TIP_TYPE_COUNT; ++tip) {
      models.tip[tip] = registerModel(toolClass + "Tip" + tip, dryRun);
    }

    // Deco bits (swords and bows, others use main)
    if (toolClass.equals("Sword") || toolClass.equals("Bow")) {
      for (int deco = 0; deco < ROD_DECO_TYPE_COUNT; ++deco) {
        models.rodDeco[deco] = registerModel(toolClass + "Deco" + deco, dryRun);
      }
    } else {
      models.rodDeco = modelMainRodDeco;
    }

    // Wool (swords and sickles, others use main, bows register arrows)
    if (toolClass.equals("Sword") || toolClass.equals("Sickle")) {
      for (int wool = 0; wool < ROD_WOOL_TYPE_COUNT; ++wool) {
        models.rodWool[wool] = registerModel(toolClass + "Wool" + wool, dryRun);
      }
    } else if (toolClass.equals("Bow")) {
      // Bows use first index of the wool array for arrows (just reusing the render pass).
      // The other 15 positions are set to the blank texture. Index 0 has no arrow.
      for (int i = 0; i < ROD_WOOL_TYPE_COUNT; ++i) {
        if (i > 0 || index == 0) {
          models.rodWool[i] = modelBlank;
        } else {
          models.rodWool[i] = registerModel("Bow_Arrow" + index, dryRun);
        }
      }
    } else {
      models.rodWool = modelMainRodWool;
    }
  }

  private static int registerIndex = 0;
  private static int modelCount = 0;

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
  public String[] getVariantNames() {

    // Need the model count. Simulate registering models...
    registerModels(true);

    List<String> list = new ArrayList<String>();
    for (int i = 0; i < modelCount; ++i) {
      list.add(SilentGems.MOD_ID + ":" + modelKeys.get(i));
    }
    return list.toArray(new String[list.size()]);
  }

  @Override
  public String getName() {

    return "ToolRenderItem";
  }

  @Override
  public String getFullName() {

    return Names.convert(SilentGems.MOD_ID + ":" + getName());
  }

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

  public String getName(ItemStack tool) {

    Item item = tool.getItem();
    String toolClass;
    String toolType;

    // Get tool class
    if (item instanceof GemSword) {
      toolClass = "Sword";
    } else if (item instanceof GemPickaxe) {
      toolClass = "Pickaxe";
    } else if (item instanceof GemShovel) {
      toolClass = "Shovel";
    } else if (item instanceof GemAxe) {
      toolClass = "Axe";
    } else if (item instanceof GemHoe) {
      toolClass = "Hoe";
    } else if (item instanceof GemSickle) {
      toolClass = "Sickle";
    } else if (item instanceof GemBow) {
      toolClass = "Bow";
    } else {
      LogHelper.debug("ToolRenderHelper.getName: Unknown tool class! " + tool.toString());
      toolClass = "Unknown";
    }

    // Get "material"
    int gemId = ToolHelper.getToolGemId(tool);
    switch (gemId) {
      case ModMaterials.CHAOS_GEM_ID:
        toolType = "Chaos";
        break;
      case ModMaterials.FLINT_GEM_ID:
        toolType = "Flint";
        break;
      case ModMaterials.FISH_GEM_ID:
        toolType = "Fish";
        break;
      default:
        toolType = Integer.toString(gemId);
        break;
    }

    boolean supercharged = ToolHelper.getToolIsSupercharged(tool);
    return toolClass + toolType + (supercharged && gemId < EnumGem.values().length ? "Plus" : "");
  }

  public String getFullName(ItemStack tool) {

    return SilentGems.MOD_ID + ":" + getName(tool);
  }

  public String[] getVariantNames(ItemStack tool) {

    return new String[] { getFullName(tool) };
  }

  /**
   * Determines whether or not to use the enchanted glow.
   */
  public boolean hasEffect(ItemStack tool) {

    return tool.isItemEnchanted() && !ToolHelper.getToolNoGlint(tool);
  }

  /**
   * Prevents the bobbing caused by tools updating their damage or NBT.
   */
  public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack,
      boolean slotChanged) {

    return slotChanged;
  }

  public int getPossibleToolCombinations() {

    // Attempts to get the number of possible permutations for a single tool type.
    // I believe this is correct, but not sure.
    return HEAD_TYPE_COUNT * HEAD_TYPE_COUNT * HEAD_TYPE_COUNT * ROD_TYPE_COUNT
        * ROD_DECO_TYPE_COUNT * (ROD_WOOL_TYPE_COUNT + 1) * (TIP_TYPE_COUNT + 1);

  }

  public ModelResourceLocation getModel(ItemStack stack, int pass, int gemId,
      boolean supercharged) {

    return getModel(stack, pass, gemId, supercharged, stack, stack.getMaxItemUseDuration());
  }

  public ModelResourceLocation getModel(ItemStack stack, int pass, int gemId, boolean supercharged,
      ItemStack usingItem, int useRemaining) {

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
      int index = ((GemBow) item).getUsingIndex(stack, useRemaining);
      switch (index) {
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

  public boolean hasKey(ItemStack stack, String key) {

    return stack.hasTagCompound() && stack.getTagCompound().hasKey(key);
  }

  public ModelResourceLocation getRodModel(ToolModelCollection icons, ItemStack stack,
      boolean supercharged) {

    return supercharged ? icons.rod[1] : icons.rod[0];
  }

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

  public ModelResourceLocation getTipModel(ToolModelCollection icons, ItemStack stack, int gemId) {

    int k = ToolHelper.getToolHeadTip(stack);
    if (k > -1) {
      k -= 1;
      if (k < 0) {
        return modelBlank;
      } else if (k > TIP_TYPE_COUNT - 1) {
        return modelError;
      }
      return icons.tip[k];
    } else {
      return modelBlank;
    }
  }
}
