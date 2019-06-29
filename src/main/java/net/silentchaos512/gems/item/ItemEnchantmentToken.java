package net.silentchaos512.gems.item;

import com.google.gson.JsonObject;
import net.minecraft.block.Block;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentData;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.EnumEnchantmentType;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.init.Enchantments;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.silentchaos512.gems.SilentGems;
import net.silentchaos512.gems.client.handler.ClientTickHandler;
import net.silentchaos512.gems.client.key.KeyTracker;
import net.silentchaos512.gems.config.GemsConfig;
import net.silentchaos512.gems.init.ModBlocks;
import net.silentchaos512.gems.lib.EnumGem;
import net.silentchaos512.gems.lib.Names;
import net.silentchaos512.lib.recipe.RecipeJsonHell;
import net.silentchaos512.lib.registry.IAddRecipes;
import net.silentchaos512.lib.registry.ICustomModel;
import net.silentchaos512.lib.registry.RecipeMaker;

import javax.annotation.Nullable;
import java.util.*;
import java.util.Map.Entry;

public class ItemEnchantmentToken extends Item implements IAddRecipes, ICustomModel {
    public enum Icon {
        ANY, ARMOR, BOW, EMPTY, FISHING_ROD, SWORD, TOOL, UNKNOWN;

        public String getName() {
            return name().toLowerCase(Locale.ROOT);
        }
    }

    private static final String NBT_ENCHANTMENTS = "TokenEnchantments";

    private static final Map<Enchantment, String> RECIPE_MAP = new HashMap<>();
    private static final Map<Enchantment, Integer> OUTLINE_COLOR_MAP = new HashMap<>();
    private static final Map<EnumEnchantmentType, Icon> MODELS_BY_TYPE = new EnumMap<>(EnumEnchantmentType.class);

    static {
        MODELS_BY_TYPE.put(EnumEnchantmentType.ALL, Icon.ANY);
        MODELS_BY_TYPE.put(EnumEnchantmentType.BREAKABLE, Icon.ANY);
        MODELS_BY_TYPE.put(EnumEnchantmentType.ARMOR, Icon.ARMOR);
        MODELS_BY_TYPE.put(EnumEnchantmentType.ARMOR_CHEST, Icon.ARMOR);
        MODELS_BY_TYPE.put(EnumEnchantmentType.ARMOR_FEET, Icon.ARMOR);
        MODELS_BY_TYPE.put(EnumEnchantmentType.ARMOR_HEAD, Icon.ARMOR);
        MODELS_BY_TYPE.put(EnumEnchantmentType.ARMOR_LEGS, Icon.ARMOR);
        MODELS_BY_TYPE.put(EnumEnchantmentType.WEARABLE, Icon.ARMOR);
        MODELS_BY_TYPE.put(EnumEnchantmentType.BOW, Icon.BOW);
        MODELS_BY_TYPE.put(EnumEnchantmentType.DIGGER, Icon.TOOL);
        MODELS_BY_TYPE.put(EnumEnchantmentType.FISHING_ROD, Icon.FISHING_ROD);
        MODELS_BY_TYPE.put(EnumEnchantmentType.WEAPON, Icon.SWORD);
    }

    private boolean modRecipesInitialized = false;

    public static final int BLANK_META = 256;

    public ItemEnchantmentToken() {
        addPropertyOverride(new ResourceLocation("model_index"), ItemEnchantmentToken::getModel);
    }

    // ==============================================
    // Methods for "constructing" enchantment tokens.
    // ==============================================

    @Nullable
    private static ResourceLocation tryGetId(String id) {
        try {
            return new ResourceLocation(id);
        } catch (NullPointerException ex) {
            return null;
        }
    }

    public ItemStack constructToken(Enchantment enchantment) {
        return constructToken(enchantment, 1);
    }

    public ItemStack constructToken(Enchantment enchantment, int level) {
        ItemStack stack = new ItemStack(this);
        addEnchantment(stack, new EnchantmentData(enchantment, level));
        return stack;
    }

    public ItemStack constructToken(Map<Enchantment, Integer> enchantmentMap) {
        ItemStack result = new ItemStack(this);
        setEnchantments(result, enchantmentMap);
        return result;
    }

    public static void addEnchantment(ItemStack stack, EnchantmentData data) {
        NBTTagList tagList = getEnchantments(stack);
        boolean needToAddEnchantment = true;
        ResourceLocation id = ForgeRegistries.ENCHANTMENTS.getKey(data.enchantment);

        for (int i = 0; i < tagList.tagCount(); ++i) {
            NBTTagCompound tags = tagList.getCompoundTagAt(i);
            ResourceLocation existingId = tryGetId(tags.getString("id"));
            if (existingId != null && existingId.equals(id)) {
                if (tags.getInteger("lvl") < data.enchantmentLevel) {
                    tags.setShort("lvl", (short) data.enchantmentLevel);
                }

                needToAddEnchantment = false;
                break;
            }
        }

        if (needToAddEnchantment) {
            NBTTagCompound tags = new NBTTagCompound();
            tags.setString("id", String.valueOf(id));
            tags.setShort("lvl", (short) data.enchantmentLevel);
            tagList.appendTag(tags);
        }

        if (stack.getTagCompound() == null)
            stack.setTagCompound(new NBTTagCompound());
        stack.getTagCompound().setTag(NBT_ENCHANTMENTS, tagList);
    }

    public static void setEnchantments(ItemStack token, Map<Enchantment, Integer> map) {
        if (token.isEmpty())
            return;
        if (token.getTagCompound() == null)
            token.setTagCompound(new NBTTagCompound());
        if (token.getTagCompound().hasKey(NBT_ENCHANTMENTS))
            token.getTagCompound().removeTag(NBT_ENCHANTMENTS);

        NBTTagList tagList = new NBTTagList();

        for (Entry<Enchantment, Integer> entry : map.entrySet()) {
            ResourceLocation registryName = entry.getKey().getRegistryName();
            String name = Objects.requireNonNull(registryName).toString();
            int level = entry.getValue();
            NBTTagCompound compound = new NBTTagCompound();
            compound.setString("name", name);
            compound.setShort("lvl", (short) level);
            tagList.appendTag(compound);
        }

        token.getTagCompound().setTag(NBT_ENCHANTMENTS, tagList);
    }

    public static NBTTagList getEnchantments(ItemStack stack) {
        NBTTagCompound tags = stack.getTagCompound();
        return tags != null ? tags.getTagList(NBT_ENCHANTMENTS, 10) : new NBTTagList();
    }

    private static Map<Enchantment, Integer> getEnchantmentMap(ItemStack stack) {
        Map<Enchantment, Integer> map = new HashMap<>();
        NBTTagList tagList = getEnchantments(stack);

        for (int i = 0; i < tagList.tagCount(); ++i) {
            NBTTagCompound tag = tagList.getCompoundTagAt(i);
            ResourceLocation id = tryGetId(tag.getString("id"));
            if (id != null) {
                Enchantment enchantment = ForgeRegistries.ENCHANTMENTS.getValue(id);
                if (enchantment != null) {
                    map.put(enchantment, tag.getInteger("lvl"));
                }
            }
        }

        return map;
    }

    // ========
    // Crafting
    // ========

    public static boolean applyTokenToTool(ItemStack token, ItemStack tool) {
        if (token.isEmpty() || tool.isEmpty()) {
            return false;
        }

        // Get enchantments on token.
        Map<Enchantment, Integer> enchantmentsOnToken = getEnchantmentMap(token);
        if (enchantmentsOnToken.isEmpty())
            return false;

        // Get enchantments on tool.
        Map<Enchantment, Integer> enchantmentsOnTool = EnchantmentHelper.getEnchantments(tool);

        // Make sure all enchantments can apply to the tool.
        for (Entry<Enchantment, Integer> entry : enchantmentsOnToken.entrySet()) {
            Enchantment ench = entry.getKey();
            // Valid for tool?
            if (!ench.canApply(tool))
                return false;

            // Does new enchantment conflict with any existing ones?
            for (Enchantment enchTool : enchantmentsOnTool.keySet()) {
                if (!ench.equals(enchTool) && !ench.isCompatibleWith(enchTool))
                    return false;
            }
        }

        // Appy enchantments to new copy of tool.
        if (!mergeEnchantmentLists(enchantmentsOnToken, enchantmentsOnTool)) {
            return false;
        }
        EnchantmentHelper.setEnchantments(enchantmentsOnToken, tool);
        return true;
    }

    public static boolean mergeEnchantmentLists(Map<Enchantment, Integer> ench1, Map<Enchantment, Integer> ench2) {
        int level, newLevel;
        // Add enchantments from second list to first...
        for (Enchantment enchantment : ench2.keySet()) {
            level = newLevel = ench2.get(enchantment);
            // If first list contains the enchantment, try increasing the level.
            if (ench1.containsKey(enchantment)) {
                newLevel = ench1.get(enchantment) + level;
                // Level too high?
                if (newLevel > enchantment.getMaxLevel()) {
                    return false;
                }
            }
            ench1.put(enchantment, newLevel);
        }

        return true;
    }

    // =========
    // Rendering
    // =========

    private static float getModel(ItemStack stack, World world, EntityLivingBase entity) {
        return getModelIcon(stack).ordinal();
    }

    private static Icon getModelIcon(ItemStack stack) {
        Map<Enchantment, Integer> map = getEnchantmentMap(stack);
        if (map.isEmpty()) return Icon.EMPTY;
        return MODELS_BY_TYPE.getOrDefault(map.keySet().iterator().next().type, Icon.UNKNOWN);
    }

    // =========================
    // Item and ItemSL overrides
    // =========================

    @Override
    public void addInformation(ItemStack stack, @Nullable World world, List<String> list, ITooltipFlag flag) {
        Map<Enchantment, Integer> enchants = getEnchantmentMap(stack);

        if (enchants.size() == 1) {
            Enchantment ench = enchants.keySet().iterator().next();
            list.add(SilentGems.i18n.subText(this, "maxLevel", ench.getMaxLevel()));

            // Recipe info
            if (KeyTracker.isControlDown()) {
                list.add(SilentGems.i18n.subText(this, "materials"));
                String recipeString = RECIPE_MAP.get(ench);
                if (recipeString != null && !recipeString.isEmpty()) {
                    for (String str : recipeString.split(";")) {
                        list.add("  " + str);
                    }
                }
            } else {
                list.add(SilentGems.i18n.subText(this, "pressCtrl"));
            }

            // Debug info
            if (KeyTracker.isAltDown()) {
                ResourceLocation registryName = ench.getRegistryName();
                list.add(TextFormatting.DARK_GRAY + Objects.requireNonNull(registryName).toString());
                // list.add(TextFormatting.DARK_GRAY + "EnchID: " + ench.getEnchantmentID(ench));
            }
        }

        // Enchantment list
        for (Entry<Enchantment, Integer> entry : enchants.entrySet()) {
            Enchantment e = entry.getKey();
            String enchName = e.getTranslatedName(entry.getValue());
            ResourceLocation registryName = Objects.requireNonNull(e.getRegistryName());
            String modName = Loader.instance().getIndexedModList().get(registryName.getNamespace()).getName();
            list.add(SilentGems.i18n.subText(this, "enchNameWithMod", enchName, modName));
            //noinspection DynamicRegexReplaceableByCompiledPattern
            String descKey = e.getName().replaceAll(":", ".").toLowerCase(Locale.ROOT) + ".desc";
            String desc = SilentGems.i18n.translate(descKey);
            if (!desc.equals(descKey))
                list.add(TextFormatting.ITALIC + "  " + desc);
        }
    }

    @Override
    public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> list) {
        if (!isInCreativeTab(tab)) return;

        List<ItemStack> tokens = new ArrayList<>();
        for (ResourceLocation key : Enchantment.REGISTRY.getKeys()) {
            Enchantment enchantment = Enchantment.REGISTRY.getObject(key);
            if (enchantment != null)
                tokens.add(constructToken(enchantment));
        }

        // Sort by type, then enchantment name.
        tokens.sort(this::compareEnchantmentNames);

        // Empty token first.
        tokens.add(0, new ItemStack(this, 1, BLANK_META));

        list.addAll(tokens);
    }

    private int compareEnchantmentNames(ItemStack o1, ItemStack o2) {
        int k = -getModelIcon(o1).compareTo(getModelIcon(o2));
        if (k == 0) {
            Enchantment ench1 = getSingleEnchantment(o1);
            Enchantment ench2 = getSingleEnchantment(o2);
            if (ench1 != null && ench2 != null) {
                // If this crashes the enchantment is at fault, nothing should be done about it.
                k = ench1.getTranslatedName(1).compareTo(ench2.getTranslatedName(1));
            }
        }
        return k;
    }

    @Nullable
    public Enchantment getSingleEnchantment(ItemStack token) {
        Map<Enchantment, Integer> map = getEnchantmentMap(token);
        if (map.size() != 1) return null;
        return map.keySet().iterator().next();
    }

    @Override
    public String getTranslationKey(ItemStack stack) {
        boolean hasEnchants = stack.getTagCompound() != null && stack.getTagCompound().hasKey(NBT_ENCHANTMENTS);
        return super.getTranslationKey(stack) + (hasEnchants ? "" : "_blank");
    }

    // =====================
    // = Rendering methods =
    // =====================

    @Override
    public boolean hasEffect(ItemStack stack) {
        return false;
    }

    private static final float OUTLINE_PULSATE_SPEED = 1f / (3f * (float) Math.PI);

    public int getOutlineColor(ItemStack stack) {
        Enchantment ench = getSingleEnchantment(stack);
        if (ench != null && OUTLINE_COLOR_MAP.containsKey(ench)) {
            int k = OUTLINE_COLOR_MAP.get(ench);
            int r = (k >> 16) & 255;
            int g = (k >> 8) & 255;
            int b = k & 255;

            int j = (int) (160 * MathHelper.sin(ClientTickHandler.ticksInGame * OUTLINE_PULSATE_SPEED));
            j = MathHelper.clamp(j, 0, 255);
            r = MathHelper.clamp(r + j, 0, 255);
            g = MathHelper.clamp(g + j, 0, 255);
            b = MathHelper.clamp(b + j, 0, 255);
            return (r << 16) | (g << 8) | b;
        }
        return 0xFFFFFF;
    }

    /**
     * Token outline colors can be customized here.
     */
    public void setColorsForDefaultTokens() {
        // OUTLINE_COLOR_MAP.put(Enchantments.UNBREAKING, 0x0000FF); // example
    }

    // =========================
    // IRegistryObject overrides
    // =========================

    @Override
    public void addRecipes(RecipeMaker recipes) {
        // Add custom serializer to allow enchantment token recipe JSONs
        //noinspection OverlyLongLambda
        SilentGems.registry.getRecipeMaker().setRecipeSerializer(this, (result, components) -> {
            JsonObject json = RecipeJsonHell.ShapedSerializer.INSTANCE.serialize(result, components);
            json.remove("type");
            json.addProperty("type", "silentgems:enchantment_token");
            Enchantment enchantment = this.getSingleEnchantment(result);
            if (enchantment != null) {
                ResourceLocation name = Objects.requireNonNull(enchantment.getRegistryName());
                json.getAsJsonObject("result").addProperty("enchantment", name.toString());
            }
            return json;
        });
    }

    @Deprecated
    public void addModRecipes() {
        // FIXME?
        if (modRecipesInitialized)
            return;
        modRecipesInitialized = true;

        // Ender Core
        if (Loader.isModLoaded("endercore")) {
            SilentGems.logHelper.info("Adding enchantment token recipes for Ender Core:");
            addModTokenRecipe("endercore:xpboost", EnumGem.MOLDAVITE, Blocks.GOLD_BLOCK, 1);
            addModTokenRecipe("endercore:autosmelt", EnumGem.GARNET, new ItemStack(ModBlocks.miscBlock, 1, 3), 4);
        }
        // Ender IO
        if (Loader.isModLoaded("enderio")) {
            SilentGems.logHelper.info("Adding enchantment token recipes for Ender IO:");
            addModTokenRecipe("enderio:soulBound", EnumGem.OPAL, "itemEnderCrystal", 1);
        }
        // Ender Zoo
        if (Loader.isModLoaded("enderzoo")) {
            SilentGems.logHelper.info("Adding enchantment token recipes for Ender Zoo:");
            Item witherDust = Item.getByNameOrId("enderzoo:witheringDust");
            if (witherDust != null) {
                addModTokenRecipe("enderzoo:witherWeapon", EnumGem.ONYX, witherDust, 5);
                addModTokenRecipe("enderzoo:witherArrow", EnumGem.BLACK_DIAMOND, witherDust, 5);
            }
        }
    }

    @Deprecated
    public void addTokenRecipe(Enchantment ench, EnumGem gem, Object other, int otherCount) {
        if ((ench == Enchantments.FROST_WALKER && GemsConfig.RECIPE_TOKEN_FROST_WALKER_DISABLE)
                || (ench == Enchantments.MENDING && GemsConfig.RECIPE_TOKEN_MENDING_DISABLE)) {
            return;
        }

        // Add a default outline color based on gem color.
        if (!OUTLINE_COLOR_MAP.containsKey(ench))
            OUTLINE_COLOR_MAP.put(ench, gem.getColor());

        String line1 = "g g";
        String line2 = otherCount > 3 ? "oto" : " t ";
        String line3 = otherCount == 3 || otherCount > 4 ? "ooo" : (otherCount == 2 || otherCount == 4 ? "o o" : " o ");

        ItemStack token = constructToken(ench);
        //noinspection DynamicRegexReplaceableByCompiledPattern
        String recipeName = "enchantment_token_" + ench.getName().replaceAll(":", "_");
        SilentGems.registry.getRecipeMaker().addShapedOre(recipeName, token, line1, line2, line3, 'g',
                gem.getItemOreName(), 'o', other, 't', new ItemStack(this, 1, BLANK_META));

        // Add to recipe map (tooltip recipe info)
        String recipeString = "2 " + gem.getItemOreName() + ";" + otherCount + " ";
        //noinspection ChainOfInstanceofChecks
        if (other instanceof String)
            recipeString += (String) other;
        else if (other instanceof ItemStack)
            recipeString += ((ItemStack) other).getDisplayName();
        else if (other instanceof Block)
            recipeString += (new ItemStack((Block) other)).getDisplayName();
        else if (other instanceof Item)
            recipeString += (new ItemStack((Item) other)).getDisplayName();
        RECIPE_MAP.put(ench, recipeString);
    }

    @Deprecated
    public void addModTokenRecipe(String enchantmentName, EnumGem gem, Object other, int otherCount) {
        SilentGems.logHelper.info("    Attempting to add token recipe for {}...", enchantmentName);
        Enchantment enchantment = Enchantment.REGISTRY.getObject(new ResourceLocation(enchantmentName));
        if (enchantment == null) {
            SilentGems.logHelper.warn("    Failed to add! Enchantment is null?");
            return;
        }
        addTokenRecipe(enchantment, gem, other, otherCount);
    }

    @Override
    public void registerModels() {
        SilentGems.registry.setModel(this, 0, Names.ENCHANTMENT_TOKEN);
        int i = 1;
        for (Icon type : Icon.values()) {
            SilentGems.registry.setModel(this, i++, Names.ENCHANTMENT_TOKEN + "_" + type.getName());
        }
        SilentGems.registry.setModel(this, BLANK_META, Names.ENCHANTMENT_TOKEN);
    }
}
