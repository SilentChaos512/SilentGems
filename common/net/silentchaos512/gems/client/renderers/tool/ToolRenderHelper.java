package net.silentchaos512.gems.client.renderers.tool;

import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.IBakedModel;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.silentchaos512.gems.SilentGems;
import net.silentchaos512.gems.core.registry.SRegistry;
import net.silentchaos512.gems.core.util.LogHelper;
import net.silentchaos512.gems.core.util.ToolHelper;
import net.silentchaos512.gems.item.tool.GemAxe;
import net.silentchaos512.gems.item.tool.GemBow;
import net.silentchaos512.gems.item.tool.GemHoe;
import net.silentchaos512.gems.item.tool.GemPickaxe;
import net.silentchaos512.gems.item.tool.GemShovel;
import net.silentchaos512.gems.item.tool.GemSickle;
import net.silentchaos512.gems.item.tool.GemSword;
import net.silentchaos512.gems.lib.EnumGem;
import net.silentchaos512.gems.material.ModMaterials;

/**
 * This no longer needs to be an item.
 */
@SuppressWarnings("deprecation")
@SideOnly(Side.CLIENT)
public class ToolRenderHelper {

  public static ToolRenderHelper instance;

  public static void init() {

    if (instance == null) {
      instance = new ToolRenderHelper();
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
   * Icons
   */

  // Shared
  public TextureAtlasSprite iconBlank; // A completely transparent texture.
  public TextureAtlasSprite iconError; // Shows up if I screw up.
  public TextureAtlasSprite[] iconMainRodDeco; // Rod decoration used by most tools.
  public TextureAtlasSprite[] iconMainRodWool; // Rod wool grip used by most tools.

  // Specific tool icon collections.
  public final ToolSpriteCollection swordIcons = new ToolSpriteCollection();
  public final ToolSpriteCollection pickaxeIcons = new ToolSpriteCollection();
  public final ToolSpriteCollection shovelIcons = new ToolSpriteCollection();
  public final ToolSpriteCollection axeIcons = new ToolSpriteCollection();
  public final ToolSpriteCollection hoeIcons = new ToolSpriteCollection();
  public final ToolSpriteCollection sickleIcons = new ToolSpriteCollection();
  public final ToolSpriteCollection bow0Icons = new ToolSpriteCollection();
  public final ToolSpriteCollection bow1Icons = new ToolSpriteCollection();
  public final ToolSpriteCollection bow2Icons = new ToolSpriteCollection();
  public final ToolSpriteCollection bow3Icons = new ToolSpriteCollection();

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

  @SubscribeEvent
  public void onStitchTexture(TextureStitchEvent.Pre event) {

    LogHelper.info("Stitching tool textures...");

    /*
     * Shared textures
     */

    iconBlank = registerSprite(event, "Blank");
    iconError = registerSprite(event, "Error");

    // Deco bits used by most tools.
    iconMainRodDeco = new TextureAtlasSprite[ROD_DECO_TYPE_COUNT];
    for (int i = 0; i < ROD_DECO_TYPE_COUNT; ++i) {
      iconMainRodDeco[i] = registerSprite(event, "ToolDeco" + i);
    }

    // Rod wool used by most tools.
    iconMainRodWool = new TextureAtlasSprite[ROD_WOOL_TYPE_COUNT];
    for (int i = 0; i < ROD_WOOL_TYPE_COUNT; ++i) {
      iconMainRodWool[i] = registerSprite(event, "RodWool" + i);
    }

    for (String toolClass : ToolHelper.TOOL_CLASSES) {
      registerSpritesForCollection(event, toolClass, 0);
      if (toolClass.equals("Bow")) {
        registerSpritesForCollection(event, toolClass, 1);
        registerSpritesForCollection(event, toolClass, 2);
        registerSpritesForCollection(event, toolClass, 3);
      }
    }

    LogHelper.info("Done with tool textures!");
  }

  private void registerSpritesForCollection(TextureStitchEvent.Pre event, String toolClass,
      int index) {

    ToolSpriteCollection sprites = getCollectionByName(toolClass, 0);
    String strIndex = index == 3 ? "_3" : ""; // Index 3 is fully drawn bows
    // Head parts
    for (int head = 0; head < HEAD_TYPE_COUNT; ++head) {
      sprites.headM[head] = registerSprite(event, toolClass + head + strIndex);
      sprites.headL[head] = registerSprite(event, toolClass + head + "L" + strIndex);
      sprites.headR[head] = registerSprite(event, toolClass + head + "R" + strIndex);
    }

    // Rods
    if (toolClass.equals("Bow")) {
      sprites.rod[0] = registerSprite(event, toolClass + "_MainNormal");
      sprites.rod[1] = registerSprite(event, toolClass + "_MainOrnate");
    } else {
      sprites.rod[0] = registerSprite(event, toolClass + "_RodNormal");
      sprites.rod[1] = registerSprite(event, toolClass + "_RodOrnate");
    }

    // Tips
    for (int tip = 0; tip < TIP_TYPE_COUNT; ++tip) {
      sprites.tip[tip] = registerSprite(event, toolClass + "Tip" + tip);
    }

    // Deco bits (swords and bows, others use main)
    if (toolClass.equals("Sword") || toolClass.equals("Bow")) {
      for (int deco = 0; deco < ROD_DECO_TYPE_COUNT; ++deco) {
        sprites.rodDeco[deco] = registerSprite(event, toolClass + "Deco" + deco);
      }
    } else {
      sprites.rodDeco = iconMainRodDeco;
    }

    // Wool (swords and sickles, others use main, bows register arrows)
    if (toolClass.equals("Sword") || toolClass.equals("Sickle")) {
      for (int wool = 0; wool < ROD_WOOL_TYPE_COUNT; ++wool) {
        sprites.rodWool[wool] = registerSprite(event, toolClass + "Wool" + wool);
      }
    } else if (toolClass.equals("Bow")) {
      // Bows use first index of the wool array for arrows (just reusing the render pass).
      // The other 15 positions are set to the blank texture. Index 0 has no arrow.
      for (int i = 0; i < ROD_WOOL_TYPE_COUNT; ++i) {
        if (i > 0 || index == 0) {
          sprites.rodWool[i] = iconBlank;
        } else {
          sprites.rodWool[i] = registerSprite(event, "Bow_Arrow" + index);
        }
      }
    } else {
      sprites.rodWool = iconMainRodWool;
    }
  }

  private TextureAtlasSprite registerSprite(TextureStitchEvent.Pre event, String name) {

    return event.map.registerSprite(new ResourceLocation(SilentGems.MOD_ID, name));
  }

  private ToolSpriteCollection getCollectionByName(String toolClass, int index) {

    switch (toolClass) {
      case "Sword":
        return swordIcons;
      case "Pickaxe":
        return pickaxeIcons;
      case "Shovel":
        return shovelIcons;
      case "Axe":
        return axeIcons;
      case "Hoe":
        return hoeIcons;
      case "Sickle":
        return sickleIcons;
      case "Bow": {
        switch (index) {
          case 0:
            return bow0Icons;
          case 1:
            return bow1Icons;
          case 2:
            return bow2Icons;
          case 3:
            return bow3Icons;
          default:
            LogHelper.severe("ToolRenderHelper.getCollectionByName: Unknown bow index: " + index);
            return null;
        }
      }
      default:
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

  public TextureAtlasSprite getIcon(ItemStack stack, int pass, int gemId, boolean supercharged) {

    return getIcon(stack, pass, gemId, supercharged, stack, stack.getMaxItemUseDuration());
  }

  public TextureAtlasSprite getIcon(ItemStack stack, int pass, int gemId, boolean supercharged,
      ItemStack usingItem, int useRemaining) {

    ToolSpriteCollection icons;
    Item item = stack.getItem();

    if (item instanceof GemSword) {
      icons = swordIcons;
    } else if (item instanceof GemPickaxe) {
      icons = pickaxeIcons;
    } else if (item instanceof GemShovel) {
      icons = shovelIcons;
    } else if (item instanceof GemAxe) {
      icons = axeIcons;
    } else if (item instanceof GemHoe) {
      icons = hoeIcons;
    } else if (item instanceof GemSickle) {
      icons = sickleIcons;
    } else if (item instanceof GemBow) {
      int index = ((GemBow) item).getUsingIndex(stack, useRemaining);
      switch (index) {
        case 0:
          icons = bow0Icons;
          break;
        case 1:
          icons = bow1Icons;
          break;
        case 2:
          icons = bow2Icons;
          break;
        case 3:
          icons = bow3Icons;
          break;
        default:
          icons = bow0Icons;
      }
    } else {
      return iconError;
    }

    switch (pass) {
      case PASS_ROD:
        return getRodIcon(icons, stack, supercharged);
      case PASS_ROD_DECO:
        return getRodDecoIcon(icons, stack, supercharged);
      case PASS_ROD_WOOL:
        return getRodWoolIcon(icons, stack, supercharged);
      case PASS_HEAD_M:
        return getHeadMiddleIcon(icons, stack, gemId);
      case PASS_HEAD_L:
        return getHeadLeftIcon(icons, stack, gemId);
      case PASS_HEAD_R:
        return getHeadRightIcon(icons, stack, gemId);
      case PASS_TIP:
        return getTipIcon(icons, stack, gemId);
      default:
        return iconBlank;
    }
  }

  public boolean hasKey(ItemStack stack, String key) {

    return stack.hasTagCompound() && stack.getTagCompound().hasKey(key);
  }

  public TextureAtlasSprite getRodIcon(ToolSpriteCollection icons, ItemStack stack,
      boolean supercharged) {

    return supercharged ? icons.rod[1] : icons.rod[0];
  }

  public TextureAtlasSprite getRodDecoIcon(ToolSpriteCollection icons, ItemStack stack,
      boolean supercharged) {

    // Regular tools have no rod decoration!
    if (!supercharged) {
      return iconBlank;
    }

    int k = ToolHelper.getToolRodDeco(stack);
    if (k > -1) {
      k = MathHelper.clamp_int(k, 0, ROD_DECO_TYPE_COUNT - 1);
      return icons.rodDeco[k];
    }
    return icons.rodDeco[12];
  }

  public TextureAtlasSprite getRodWoolIcon(ToolSpriteCollection icons, ItemStack stack,
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

    return iconBlank;
  }

  public TextureAtlasSprite getHeadMiddleIcon(ToolSpriteCollection icons, ItemStack stack,
      int gemId) {

    int k = ToolHelper.getToolHeadMiddle(stack);
    if (k > -1) {
      k = MathHelper.clamp_int(k, 0, HEAD_TYPE_COUNT - 1);
      return icons.headM[k];
    }

    if (gemId >= 0 && gemId < HEAD_TYPE_COUNT) {
      return icons.headM[gemId];
    } else {
      return iconError;
    }
  }

  public TextureAtlasSprite getHeadLeftIcon(ToolSpriteCollection icons, ItemStack stack,
      int gemId) {

    int k = ToolHelper.getToolHeadLeft(stack);
    if (k > -1) {
      k = MathHelper.clamp_int(k, 0, HEAD_TYPE_COUNT - 1);
      return icons.headL[k];
    }

    if (gemId >= 0 && gemId < HEAD_TYPE_COUNT) {
      return icons.headL[gemId];
    } else {
      return iconError;
    }
  }

  public TextureAtlasSprite getHeadRightIcon(ToolSpriteCollection icons, ItemStack stack,
      int gemId) {

    int k = ToolHelper.getToolHeadRight(stack);
    if (k > -1) {
      k = MathHelper.clamp_int(k, 0, HEAD_TYPE_COUNT - 1);
      return icons.headR[k];
    }

    if (gemId >= 0 && gemId < HEAD_TYPE_COUNT) {
      return icons.headR[gemId];
    } else {
      return iconError;
    }
  }

  public TextureAtlasSprite getTipIcon(ToolSpriteCollection icons, ItemStack stack, int gemId) {

    int k = ToolHelper.getToolHeadTip(stack);
    if (k > -1) {
      k -= 1;
      if (k < 0) {
        return iconBlank;
      } else if (k > TIP_TYPE_COUNT - 1) {
        return iconError;
      }
      return icons.tip[k];
    } else {
      return iconBlank;
    }
  }
}
