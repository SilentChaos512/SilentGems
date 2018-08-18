package net.silentchaos512.gems.item;

import com.google.common.collect.Sets;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemTool;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.Phase;
import net.silentchaos512.gems.SilentGems;
import net.silentchaos512.gems.api.ITool;
import net.silentchaos512.gems.api.lib.EnumMaterialGrade;
import net.silentchaos512.gems.api.lib.EnumMaterialTier;
import net.silentchaos512.gems.api.lib.IPartPosition;
import net.silentchaos512.gems.api.lib.ToolPartPosition;
import net.silentchaos512.gems.api.tool.ToolStats;
import net.silentchaos512.gems.api.tool.part.ToolPart;
import net.silentchaos512.gems.api.tool.part.ToolPartRegistry;
import net.silentchaos512.gems.api.tool.part.ToolPartTip;
import net.silentchaos512.gems.client.handler.ClientTickHandler;
import net.silentchaos512.gems.client.key.KeyTracker;
import net.silentchaos512.gems.client.render.ToolItemOverrideHandler;
import net.silentchaos512.gems.client.render.ToolModel;
import net.silentchaos512.gems.config.GemsConfig;
import net.silentchaos512.gems.init.ModItems;
import net.silentchaos512.gems.item.tool.*;
import net.silentchaos512.gems.lib.TooltipHelper;
import net.silentchaos512.gems.lib.client.ArmorModelData;
import net.silentchaos512.gems.lib.client.IModelData;
import net.silentchaos512.gems.lib.client.ToolModelData;
import net.silentchaos512.gems.lib.soul.ToolSoul;
import net.silentchaos512.gems.util.EnumMagicType;
import net.silentchaos512.gems.util.SoulManager;
import net.silentchaos512.gems.util.ToolHelper;
import net.silentchaos512.lib.registry.ICustomModel;

import javax.annotation.Nullable;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class ToolRenderHelper extends ToolRenderHelperBase implements ICustomModel {
    public static ToolRenderHelper getInstance() {
        return (ToolRenderHelper) instance;
    }

    public static final String NBT_MODEL_INDEX = "SGModel";
    public static final String SMART_MODEL_NAME = SilentGems.RESOURCE_PREFIX.toLowerCase() + "tool";
    public static final ModelResourceLocation SMART_MODEL = new ModelResourceLocation(SMART_MODEL_NAME, "inventory");

    private Set<ModelResourceLocation> modelSet = null;
    private ModelResourceLocation[] arrowModels;

    /**
     * Rendering cache for non-example tools (in player's inventory, in chests, etc.) Cleared
     * occasionally so it doesn't end up with a large number of unneeded objects.
     */
    private Map<UUID, IModelData> modelCache = new HashMap<>();
    /**
     * Rendering cache for example tools (JEI, creative, etc.) Cleared more often, as it can fill
     * with huge numbers of objects that are used only for a short time (i.e., player browsing
     * JEI).
     */
    private Map<UUID, IModelData> modelCacheExamples = new HashMap<>();

    public ModelResourceLocation modelBlank;
    public ModelResourceLocation modelError;
    public ModelResourceLocation modelGooglyEyes;

    @Override
    public void addInformation(ItemStack tool, World world, List<String> list, ITooltipFlag flag) {
        boolean controlDown = KeyTracker.isControlDown();
        boolean altDown = KeyTracker.isAltDown();
        boolean shiftDown = KeyTracker.isShiftDown();
        String line;

        // Tipped upgrade
        ToolPartTip partTip = (ToolPartTip) ToolHelper.getConstructionTip(tool);
        if (partTip != null) {
            String tipName = partTip.getUnlocalizedName().replaceFirst("[^:]+:", "");
            tipName = SilentGems.i18n.miscText("tooltip." + tipName);
            line = SilentGems.i18n.miscText("tooltip.tipped", tipName);
            list.add(line);
        }

        // UUID
        if (GemsConfig.DEBUG_MODE && controlDown && shiftDown) {
            UUID uuid = ToolHelper.hasUUID(tool) ? ToolHelper.getUUID(tool) : null;
            list.add(uuid == null ? "No UUID" : uuid.toString());
            uuid = ToolHelper.getSoulUUID(tool);
            list.add(uuid == null ? "No Soul UUID" : uuid.toString());
        }

        // Tool Soul
        ToolSoul soul = SoulManager.getSoul(tool);
        if (soul != null) {
            soul.addInformation(tool, world, list, flag.isAdvanced());
        }

        // Show original owner?
        if (controlDown) {
            String owner = ToolHelper.getOriginalOwner(tool);
            if (owner.equals(SilentGems.i18n.miscText("tooltip.OriginalOwner.Creative")))
                owner = TextFormatting.LIGHT_PURPLE + owner;

            if (!owner.isEmpty())
                list.add(SilentGems.i18n.miscText("tooltip.OriginalOwner", owner));
            else
                list.add(SilentGems.i18n.miscText("tooltip.OriginalOwner.Unknown"));
        }

        if (controlDown && tool.getTagCompound().getBoolean(ToolHelper.NBT_LOCK_STATS)) {
            list.add(SilentGems.i18n.miscText("tooltip.LockedStats"));
        }

        // Example tool?
        if (tool.hasTagCompound() && tool.getTagCompound().hasKey(ToolHelper.NBT_EXAMPLE_TOOL_TIER)) {
            EnumMaterialTier tier = EnumMaterialTier.values()[tool.getTagCompound()
                    .getInteger(ToolHelper.NBT_EXAMPLE_TOOL_TIER)];
            list.add(SilentGems.i18n.miscText("tooltip.ExampleTool", tier));
        }
        // Missing data?
        else if (ToolHelper.hasNoConstruction(tool)) {
            list.add(SilentGems.i18n.miscText("tooltip.NoData1"));
            list.add(SilentGems.i18n.miscText("tooltip.NoData2"));
        }
        // Broken?
        else if (ToolHelper.isBroken(tool)) {
            line = SilentGems.i18n.miscText("tooltip.Broken");
            list.add(line);
        }

        final Item item = tool.getItem();
        final boolean isSword = item instanceof ItemGemSword;
        final boolean isAxe = item instanceof ItemGemAxe;
        final boolean isWeapon = isSword || isAxe;
        final boolean isCaster = isSword && ToolHelper.getToolTier(tool).ordinal() >= EnumMaterialTier.SUPER.ordinal();
        final boolean isBow = item instanceof ItemGemBow;
        final boolean isDigger = item instanceof ItemTool;
        final boolean isShield = item instanceof ItemGemShield;

        final String sep = SilentGems.i18n.miscText("tooltip.Separator");

        if (controlDown) {
            // Properties Header
            line = SilentGems.i18n.miscText("tooltip.Properties");
            list.add(line);

            TextFormatting color = TextFormatting.YELLOW;

            // Tier
            EnumMaterialTier tier = ToolHelper.getToolTier(tool);
            line = TextFormatting.RESET + SilentGems.i18n.miscText("tooltier." + tier);
            list.add("  " + color + SilentGems.i18n.miscText("tooltier", line));

            int durabilityMax = tool.getMaxDamage();
            int durability = durabilityMax - tool.getItemDamage();
            String s1 = String.format(durability > 9999 ? "%,d" : "%d", durability);
            String s2 = String.format(durabilityMax > 9999 ? "%,d" : "%d", durabilityMax);
            float durabilityBoost = ToolSoul.getDurabilityModifierForDisplay(soul);
            String durBoostLine = durabilityBoost == 0f ? ""
                    : " (" + TooltipHelper.numberToPercent(durabilityBoost, 0, true) + TextFormatting.RESET
                    + ")";
            line = SilentGems.i18n.miscText("tooltip.Durability", s1 + " / " + s2 + durBoostLine);
            list.add(color + "  " + line);

            if (isShield) {
                float magicProtection = (int) (ToolHelper.getMagicProtection(tool) * 100);
                list.add(color + getTooltipLine("MagicProtection", magicProtection, 0f));
            }

            if (isDigger) { // @formatter:off
        int harvestLevel = ToolHelper.getHarvestLevel(tool);
        String str = color + getTooltipLine("HarvestLevel", harvestLevel, 0f);
        String key = "tooltip.level" + harvestLevel;
        String val = SilentGems.i18n.miscText(key);
        if (!val.equals("misc.silentgems:" + key)) str += " (" +  val + ")";
        list.add(str);
        float harvestSpeedModifier = ToolSoul.getHarvestSpeedModifierForDisplay(soul);
        list.add(color + getTooltipLine("HarvestSpeed", ToolHelper.getDigSpeedOnProperMaterial(tool),
            harvestSpeedModifier));
      } // @formatter:on

            if (isWeapon) {
                float meleeSpeed = 4 + ToolHelper.getMeleeSpeedModifier(tool);
                list.add(color + getTooltipLine("MeleeSpeed", meleeSpeed, 0f).replaceFirst("%", ""));
                float meleeDamage = 1 + ToolHelper.getMeleeDamageModifier(tool);
                float meleeModifier = ToolSoul.getMeleeDamageModifierForDisplay(soul);
                list.add(color + getTooltipLine("MeleeDamage", meleeDamage, meleeModifier));

                if (isCaster) {
                    EnumMagicType magicType = EnumMagicType.getMagicType(tool);
                    float damagePerShot = magicType.getDamagePerShot(tool);
                    String damageString = damagePerShot == (int) damagePerShot
                            ? Integer.toString((int) damagePerShot)
                            : String.format(TooltipHelper.FORMAT_FLOAT, damagePerShot);
                    String str = damageString + "" + TextFormatting.DARK_GRAY + "x"
                            + magicType.getShotCount(tool);
                    float magicModifier = ToolSoul.getMagicDamageModifierForDisplay(soul);
                    list.add(color + getTooltipLine("MagicDamage", str, magicModifier));
                }
            }

            if (isBow) {
                ToolStats statsNoSoul = ToolHelper.getStats(tool, false);
                float drawSpeed = ModItems.bow.getDrawSpeedForDisplay(tool);
                float drawSpeedPreSoul = ItemGemBow.getDrawSpeedForDisplay(statsNoSoul.meleeSpeed,
                        statsNoSoul.harvestSpeed);
                float drawSpeedBoost = (drawSpeed - drawSpeedPreSoul) / drawSpeedPreSoul;
                list.add(color + getTooltipLine("DrawSpeed", drawSpeed, drawSpeedBoost));

                float arrowDamage = ModItems.bow.getArrowDamageForDisplay(tool);
                float arrowDamagePreSoul = ItemGemBow.getArrowDamageForDisplay(statsNoSoul.meleeDamage);
                float arrowDamageBoost = (arrowDamage - arrowDamagePreSoul) / arrowDamagePreSoul;
                list.add(color + getTooltipLine("ArrowDamage", arrowDamage, arrowDamageBoost));
            }

            list.add(color + getTooltipLine("ChargeSpeed", ToolHelper.getChargeSpeed(tool), 0f));
        } else {
            list.add(TextFormatting.GOLD + SilentGems.i18n.miscText("tooltip.CtrlForProp"));
        }

        if (altDown) {
            // Statistics Header
            list.add(sep);
            line = SilentGems.i18n.miscText("tooltip.Statistics");
            list.add(line);

            list.add(getTooltipLine("BlocksMined", ToolHelper.getStatBlocksMined(tool), 0f));

            if (isDigger) {
                list.add(getTooltipLine("BlocksPlaced", ToolHelper.getStatBlocksPlaced(tool), 0f));
            }

            if (item instanceof ItemGemShovel) {
                list.add(getTooltipLine("PathsMade", ToolHelper.getStatPathsMade(tool), 0f));
            }

            if (item instanceof ItemGemHoe) {
                list.add(getTooltipLine("BlocksTilled", ToolHelper.getStatBlocksTilled(tool), 0f));
            }

            list.add(getTooltipLine("HitsLanded", ToolHelper.getStatHitsLanded(tool), 0f));

            if (isCaster || isBow)
                list.add(getTooltipLine("ShotsFired", ToolHelper.getStatShotsFired(tool), 0f));

            if (item instanceof ItemGemTomahawk)
                list.add(getTooltipLine("ThrownCount", ToolHelper.getStatThrownCount(tool), 0f));

            if (isWeapon)
                list.add(getTooltipLine("KillCount", ToolHelper.getStatKillCount(tool), 0f));

            list.add(getTooltipLine("Redecorated", ToolHelper.getStatRedecorated(tool), 0f));
            list.add(sep);

            line = SilentGems.i18n.miscText("tooltip.Construction");
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
            ToolPart partRod = ToolHelper.getConstructionRod(tool);
            if (partRod != null) {
                list.add("  " + TextFormatting.YELLOW + partRod.getKey());
            }
            list.add(sep);
        } else {
            list.add(TextFormatting.GOLD + SilentGems.i18n.miscText("tooltip.AltForStat"));
        }

        // Debug render layers
        if (controlDown && shiftDown && tool.hasTagCompound()) {
            if (!altDown)
                list.add(sep);
            list.add("Render Layers");
            IModelData modelData = getModelCache(tool);
            if (modelData != null) {
                for (ToolPartPosition pos : ToolPartPosition.values()) {
                    String key = "Layer" + pos.ordinal();
                    String str = "  %s: %s, %X";
                    ToolPart renderPart = ToolHelper.getRenderPart(tool, pos);
                    ModelResourceLocation model = renderPart == null ? null
                            : renderPart.getModel(tool, pos, 0);
                    str = String.format(str, pos.name(), model == null ? "null" : model.toString(),
                            modelData.getColor(pos, 0));
                    list.add(str);
                }
            }
        }

        list.add(SilentGems.i18n.miscText("legacyItem"));
    }

    public String getTooltipLine(String key, int value, float soulBoost) {
        String line = TooltipHelper.get(key, value, true);
        if (soulBoost != 0f) {
            line += TextFormatting.RESET + " (" + TooltipHelper.numberToPercent(soulBoost, 0, true)
                    + TextFormatting.RESET + ")";
        }
        return line;
    }

    public String getTooltipLine(String key, float value, float soulBoost) {
        String line = TooltipHelper.get(key, value, true);
        if (soulBoost != 0f) {
            line += TextFormatting.RESET + " (" + TooltipHelper.numberToPercent(soulBoost, 0, true)
                    + TextFormatting.RESET + ")";
        }
        return line;
    }

    public String getTooltipLine(String key, String value, float soulBoost) {
        String line = TooltipHelper.get(key, value, true);
        if (soulBoost != 0f) {
            line += TextFormatting.RESET + " (" + TooltipHelper.numberToPercent(soulBoost, 0, true)
                    + TextFormatting.RESET + ")";
        }
        return line;
    }

    @Nullable
    private IModelData getModelCache(ItemStack tool) {
        if (!ToolHelper.hasUUID(tool)) {
            return null;
        }

        UUID uuid = ToolHelper.getUUID(tool);
        if (ToolHelper.isExampleItem(tool)) {
            return modelCacheExamples.get(uuid);
        }
        return modelCache.get(uuid);
    }

    @Override
    public void updateModelCache(ItemStack toolOrArmor) {
        if (ToolHelper.hasUUID(toolOrArmor)) {
            UUID uuid = ToolHelper.getUUID(toolOrArmor);
            IModelData modelData = toolOrArmor.getItem() instanceof ITool ? new ToolModelData(toolOrArmor)
                    : new ArmorModelData(toolOrArmor);

            if (ToolHelper.isExampleItem(toolOrArmor)) {
                modelCacheExamples.put(uuid, modelData);
            } else {
                modelCache.put(uuid, modelData);
            }
        }
    }

    @SubscribeEvent
    public void onClientTick(ClientTickEvent event) {
        if (event.phase == Phase.END) {
            // Clear tool model caches (TODO: configs?)
            if (ClientTickHandler.ticksInGame % (10 * 60 * 20) == 0) {
                modelCache.clear();
                // SilentGems.logHelper.debug("Cleared normal model cache.");
            }
            if (ClientTickHandler.ticksInGame % (60 * 20) == 0) {
                modelCacheExamples.clear();
                // SilentGems.logHelper.debug("Cleared examples model cache.");
            }
        }
    }

    @SubscribeEvent
    public void onModelBake(ModelBakeEvent event) {
        // log.info("ToolRenderHelper.onModelBake");
        Object object = event.getModelRegistry().getObject(SMART_MODEL);
        if (object != null) {
            IBakedModel existingModel = (IBakedModel) object;
            ToolModel customModel = new ToolModel(existingModel);
            event.getModelRegistry().putObject(SMART_MODEL, customModel);
            ToolItemOverrideHandler.baseModel = customModel;
        }
    }

    /**
     * Creates the list of all possible models.
     */
    private void buildModelSet() {
        if (modelSet != null) {
            return;
        }

        Set<ModelResourceLocation> set = Sets.newConcurrentHashSet();

        for (ToolPart part : ToolPartRegistry.getValues()) {
            for (ToolPartPosition pos : ToolPartPosition.values()) {
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
            ModelResourceLocation model = new ModelResourceLocation(SilentGems.RESOURCE_PREFIX + "bow/bow_arrow_" + tier + (i & 3));
            set.add(model);
            arrowModels[i] = model;
        }

        modelSet = set;
        ModelResourceLocation[] models = set.toArray(new ModelResourceLocation[0]);
    }

    /**
     * Gets the animation frame for bows. Returns 0 for everything else.
     */
    private int getAnimationFrame(ItemStack tool) {

        if (tool.getItem() instanceof ItemGemBow) {
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

    public int getTotalAnimationFrames(ItemStack tool) {

        return tool.getItem() instanceof ItemGemBow ? 4 : 1;
    }

    public int getColor(ItemStack toolOrArmor, @Nullable IPartPosition pos) {
        if (toolOrArmor.isEmpty() || pos == null) {
            return 0xFFFFFF;
        }

        IModelData modelData = getModelCache(toolOrArmor);
        if (modelData == null) {
            updateModelCache(toolOrArmor);
            modelData = getModelCache(toolOrArmor);
        }

        if (modelData == null) {
            return 0xFFFFFF;
        }

        return modelData.getColor(pos, getAnimationFrame(toolOrArmor));
    }

    /**
     * Gets the model for the specified tool and position. Gets the animation frame on its own.
     * Models are cached for performance.
     */
    @Nullable
    public ModelResourceLocation getModel(ItemStack tool, ToolPartPosition pos) {
        if (tool.isEmpty()) {
            return modelError;
        }

        IModelData modelData = getModelCache(tool);
        int frame = getAnimationFrame(tool);
        boolean isBow = tool.getItem() instanceof ItemGemBow;

        if (modelData == null) {
            updateModelCache(tool);
            modelData = getModelCache(tool);
        }

        if (ToolHelper.isBroken(tool) || modelData == null || modelData.getModel(pos, frame) == null) {
            // Model is currently not cached? Or a special case like broken tools?
            // I assume broken tools won't be kept that way too often, so do we even need to cache that model?

            // Bow "arrow" models
            if (pos == ToolPartPosition.ROD_GRIP && isBow) {
                return getArrowModel(tool, frame);
            }

            // Get the render part for this position.
            ToolPart part = ToolHelper.getRenderPart(tool, pos);
            if (part == null) {
                return null;
            }

            // Get the desired model for the current position and animation frame.
            return !ToolHelper.isBroken(tool) ? part.getModel(tool, pos, frame) : part.getBrokenModel(tool, pos, frame);
        }

        // Grab the cached model.
        return modelData.getModel(pos, frame);
    }

    /**
     * Gets the arrow model for the animation frame for bows.
     */
    @Nullable
    private ModelResourceLocation getArrowModel(ItemStack tool, int frame) {
        if (frame < 0 || frame > 3)
            return null;
        if (ToolHelper.isBroken(tool))
            return modelBlank;
        boolean superTier = ToolHelper.getToolTier(tool).ordinal() >= EnumMaterialTier.SUPER.ordinal();
        return arrowModels[superTier ? frame + 4 : frame];
    }

    @Override
    public void registerModels() {
        buildModelSet();
        AtomicInteger index = new AtomicInteger();
        this.modelSet.forEach(model -> ModelLoader.setCustomModelResourceLocation(this, index.getAndIncrement(), model));

        // Extra models
        this.modelGooglyEyes = new ModelResourceLocation(SilentGems.RESOURCE_PREFIX + "googlyeyes", "inventory");
        this.modelError = new ModelResourceLocation(SilentGems.RESOURCE_PREFIX + "error", "inventory");
        ModelLoader.setCustomModelResourceLocation(this, index.getAndIncrement(), this.modelGooglyEyes);
        ModelLoader.setCustomModelResourceLocation(this, index.getAndIncrement(), this.modelError);

        // Error and broken models
        for (String str : new String[]{"sword", "dagger", "katana", "machete", "scepter", "tomahawk",
                "pickaxe", "shovel", "axe", "paxel", "hoe", "sickle", "bow"}) {
            ModelLoader.setCustomModelResourceLocation(this, index.getAndIncrement(),
                    new ModelResourceLocation(SilentGems.RESOURCE_PREFIX + str + "/_error", "inventory"));
            ModelLoader.setCustomModelResourceLocation(this, index.getAndIncrement(),
                    new ModelResourceLocation(SilentGems.RESOURCE_PREFIX + str + "/" + str + "_broken", "inventory"));
        }
    }
}
