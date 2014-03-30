package silent.gems.lib;

import net.minecraft.util.ResourceLocation;
import silent.gems.core.util.ResourceLocationHelper;

public class Textures {

    // Base file paths
    public final static String BLOCK_SHEET_LOCATION = "textures/blocks/";
    public final static String ENTITY_SHEET_LOCATION = "textures/entity/";
    public final static String ITEM_SHEET_LOCATION = "textures/items/";
    public final static String MODEL_SHEET_LOCATION = "textures/models/";
    public final static String GUI_SHEET_LOCATION = "textures/gui/";
    public final static String EFFECTS_LOCATION = "textures/effects/";

    // Item/Block sprite sheets
    //None yet.

    // Armor sprite sheets

    // GUI textures
    public final static ResourceLocation GUI_SHINY_CRAFTER = ResourceLocationHelper.getResourceLocation(GUI_SHEET_LOCATION + "ShinyCrafter.png");

    // Model textures
    public final static ResourceLocation ITEM_SIGIL = ResourceLocationHelper.getResourceLocation(MODEL_SHEET_LOCATION + "Sigil.png");

    // Effect textures
    public final static ResourceLocation EFFECT_MAGIC_PROJECTILE = ResourceLocationHelper.getResourceLocation(EFFECTS_LOCATION
            + "magic.png");
}
