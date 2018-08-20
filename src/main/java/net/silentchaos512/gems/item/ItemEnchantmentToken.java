package net.silentchaos512.gems.item;

import com.google.common.collect.ImmutableList;
import com.google.gson.JsonObject;
import net.minecraft.block.Block;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.init.Blocks;
import net.minecraft.init.Enchantments;
import net.minecraft.init.Items;
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
import net.minecraftforge.oredict.OreDictionary;
import net.silentchaos512.gems.SilentGems;
import net.silentchaos512.gems.client.handler.ClientTickHandler;
import net.silentchaos512.gems.client.key.KeyTracker;
import net.silentchaos512.gems.config.GemsConfig;
import net.silentchaos512.gems.init.ModBlocks;
import net.silentchaos512.gems.init.ModEnchantments;
import net.silentchaos512.gems.init.ModItems;
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
    private static final String NBT_ENCHANTMENTS = "TokenEnchantments";
    /*
     * Model keys
     */
    private static final String KEY_ANY = "Any";
    private static final String KEY_ARMOR = "Armor";
    private static final String KEY_BOW = "Bow";
    private static final String KEY_EMPTY = "Empty";
    private static final String KEY_FISHING_ROD = "FishingRod";
    private static final String KEY_WEAPON = "Sword";
    private static final String KEY_DIGGER = "Tool";
    private static final String KEY_UNKNOWN = "Unknown";
    private static final String[] MODEL_TYPES = {KEY_ANY, KEY_ARMOR, KEY_BOW, KEY_EMPTY, KEY_FISHING_ROD, KEY_WEAPON, KEY_DIGGER, KEY_UNKNOWN};

    private Map<String, Integer> modelMap = new HashMap<>();
    private Map<Enchantment, String> recipeMap = new HashMap<>();
    private Map<Enchantment, Integer> colorMap = new HashMap<>();
    private boolean modRecipesInitialized = false;

    public static final int BLANK_META = 256;

    public ItemEnchantmentToken() {
        for (int i = 0; i < MODEL_TYPES.length; ++i) {
            modelMap.put(MODEL_TYPES[i], i);
        }
        addPropertyOverride(new ResourceLocation("model_index"), (stack, world, entity) ->
                modelMap.get(getModelKey(stack)));
    }

    // ==============================================
    // Methods for "constructing" enchantment tokens.
    // ==============================================

    public ItemStack constructToken(Enchantment enchantment) {
        return constructToken(enchantment, 1);
    }

    public ItemStack constructToken(Enchantment enchantment, int level) {
        return addEnchantment(new ItemStack(this), enchantment, level);
    }

    public ItemStack constructToken(Map<Enchantment, Integer> enchantmentMap) {
        ItemStack result = new ItemStack(this);
        setEnchantments(result, enchantmentMap);
        return result;
    }

    public ItemStack addEnchantment(ItemStack stack, Enchantment enchantment, int level) {
        ItemStack result = stack.copy();
        try {
            Map<Enchantment, Integer> map = getEnchantments(stack);
            map.put(enchantment, level);
            setEnchantments(result, map);
        } catch (NullPointerException ex) {
            String str = "Failed to construct an enchantment token! This will likely result in a broken recipe.\n";
            str += "The following is a list of details on the enchantment at fault. Please direct your anger at:\n";
            str += getEnchantmentDebugInfo(enchantment);
            SilentGems.logHelper.warn(str);
        }
        return result;
    }

    public Map<Enchantment, Integer> getEnchantments(ItemStack token) {
        if (token.isEmpty() || !token.hasTagCompound())
            return new HashMap<>();

        NBTTagList tagList = token.getTagCompound().getTagList(NBT_ENCHANTMENTS, 10);
        Map<Enchantment, Integer> map = new HashMap<>();

        for (int i = 0; i < tagList.tagCount(); ++i) {
            String name = tagList.getCompoundTagAt(i).getString("name");
            int level = tagList.getCompoundTagAt(i).getShort("lvl");

            Enchantment ench = Enchantment.REGISTRY.getObject(new ResourceLocation(name));
            if (ench != null)
                map.put(ench, level);
        }

        return map;
    }

    public void setEnchantments(ItemStack token, Map<Enchantment, Integer> map) {
        if (token.isEmpty())
            return;
        if (!token.hasTagCompound())
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

    // ========
    // Crafting
    // ========

    public boolean applyTokenToTool(ItemStack token, ItemStack tool) {
        if (token.isEmpty() || tool.isEmpty()) {
            return false;
        }

        // Get enchantments on token.
        Map<Enchantment, Integer> enchantmentsOnToken = getEnchantments(token);
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

    public boolean mergeEnchantmentLists(Map<Enchantment, Integer> ench1, Map<Enchantment, Integer> ench2) {
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

    private boolean loggedIssue139Catch = false;

    public String getModelKey(ItemStack stack) {

        Map<Enchantment, Integer> enchMap = getEnchantments(stack);
        String key = KEY_EMPTY;

        if (!enchMap.isEmpty()) {
            Enchantment ench = enchMap.keySet().iterator().next();
            if (ench == null || ench.type == null)
                return KEY_UNKNOWN;

            try {
                switch (ench.type) {
                    case ALL:
                    case BREAKABLE:
                        return KEY_ANY;
                    case ARMOR:
                    case ARMOR_CHEST:
                    case ARMOR_FEET:
                    case ARMOR_HEAD:
                    case ARMOR_LEGS:
                    case WEARABLE:
                        return KEY_ARMOR;
                    case BOW:
                        return KEY_BOW;
                    case DIGGER:
                        return KEY_DIGGER;
                    case FISHING_ROD:
                        return KEY_FISHING_ROD;
                    case WEAPON:
                        return KEY_WEAPON;
                    default:
                        return KEY_UNKNOWN;
                }
            } catch (ArrayIndexOutOfBoundsException ex) {
                // Some mod is causing a crash I can't reproduce. Try to catch it and log any information possible.
                if (!loggedIssue139Catch) {
                    loggedIssue139Catch = true;
                    SilentGems.logHelper.noticableWarning(true, ImmutableList.of(
                            "Caught an odd exception in ItemEnchantmentToken#getModelKey. This will only be logged once.",
                            "Offending token: " + stack,
                            "Offending enchantment: " + ench + " (" + ench.getName() + ")"));
                    return KEY_EMPTY;
                }
            }
        }

        return key;
    }

    // =========================
    // Item and ItemSL overrides
    // =========================

    @Override
    public void addInformation(ItemStack stack, World world, List<String> list, ITooltipFlag flag) {
        Map<Enchantment, Integer> enchants = getEnchantments(stack);

        if (enchants.size() == 1) {
            Enchantment ench = enchants.keySet().iterator().next();
            list.add(SilentGems.i18n.subText(this, "maxLevel", ench.getMaxLevel()));

            // Recipe info
            if (KeyTracker.isControlDown()) {
                list.add(SilentGems.i18n.subText(this, "materials"));
                String recipeString = recipeMap.get(ench);
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
            String descKey = e.getName().replaceAll(":", ".").toLowerCase() + ".desc";
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
        tokens.sort((o1, o2) -> {
            int k = -getModelKey(o1).compareTo(getModelKey(o2));
            if (k == 0) {
                Enchantment ench1 = getSingleEnchantment(o1);
                Enchantment ench2 = getSingleEnchantment(o2);
                if (ench1 == null || ench2 == null)
                    return 0;
                return ench1.getTranslatedName(1).compareTo(ench2.getTranslatedName(1));
            }
            return k;
        });

        // Empty token first.
        tokens.add(0, new ItemStack(this, 1, BLANK_META));

        list.addAll(tokens);
    }

    @Nullable
    public Enchantment getSingleEnchantment(ItemStack token) {
        Map<Enchantment, Integer> map = getEnchantments(token);
        if (map.size() != 1) return null;
        return map.keySet().iterator().next();
    }

    @Override
    public String getTranslationKey(ItemStack stack) {
        boolean hasEnchants = stack.hasTagCompound() && stack.getTagCompound().hasKey(NBT_ENCHANTMENTS);
        return super.getTranslationKey(stack) + (hasEnchants ? "" : "_blank");
    }

    // =====================
    // = Rendering methods =
    // =====================

    @Override
    public boolean hasEffect(ItemStack stack) {
        return false;
    }

    private static float OUTLINE_PULSATE_SPEED = 1f / (3f * (float) Math.PI);

    public int getOutlineColor(ItemStack stack) {
        Enchantment ench = getSingleEnchantment(stack);
        if (ench != null && colorMap.containsKey(ench)) {
            int k = colorMap.get(ench);
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
        // colorMap.put(Enchantments.UNBREAKING, 0x0000FF); // example
    }

    // =========================
    // IRegistryObject overrides
    // =========================

    @Override
    public void addRecipes(RecipeMaker recipes) {
        // Add custom serializer to allow enchantment token recipe JSONs
        SilentGems.registry.getRecipeMaker().setRecipeSerializer(this, (result, components) -> {
            JsonObject json = RecipeJsonHell.ShapedSerializer.INSTANCE.serialize(result, components);
            json.remove("type");
            json.addProperty("type", "silentgems:enchantment_token");
            Enchantment enchantment = this.getSingleEnchantment(result);
            if (enchantment != null) {
                json.getAsJsonObject("result").addProperty("enchantment", enchantment.getRegistryName().toString());
            }
            return json;
        });

        // Blank
        recipes.addShapedOre("enchantment_token_blank", new ItemStack(this, 12, BLANK_META), "ggg",
                "lcl", "ggg", 'g', "ingotGold", 'l', "gemLapis", 'c', "gemChaos");
        // Uncrafting
        recipes.addShapeless("enchantment_token_clear", new ItemStack(this, 1, BLANK_META),
                new ItemStack(this, 1, OreDictionary.WILDCARD_VALUE));

        // All
        addTokenRecipe(Enchantments.UNBREAKING, EnumGem.SAPPHIRE, "ingotIron", 5);
        addTokenRecipe(Enchantments.MENDING, EnumGem.BLACK_DIAMOND, Items.NETHER_STAR, 1);

        // Digging tools
        addTokenRecipe(Enchantments.EFFICIENCY, EnumGem.BERYL, "dustGlowstone", 4);
        addTokenRecipe(Enchantments.FORTUNE, EnumGem.GOLDEN_BERYL, "gemDiamond", 3);
        addTokenRecipe(Enchantments.SILK_TOUCH, EnumGem.VIOLET_SAPPHIRE, "gemEmerald", 3);
        ItemStack netherShard = ModItems.craftingMaterial.netherShard;
        addTokenRecipe(ModEnchantments.gravity, EnumGem.AMBER, netherShard, 2);

        // Melee weapons
        addTokenRecipe(Enchantments.BANE_OF_ARTHROPODS, EnumGem.AMETHYST, Items.SPIDER_EYE, 4);
        addTokenRecipe(Enchantments.FIRE_ASPECT, EnumGem.GARNET, Items.BLAZE_POWDER, 4);
        addTokenRecipe(Enchantments.KNOCKBACK, EnumGem.AQUAMARINE, Items.FEATHER, 5);
        addTokenRecipe(Enchantments.LOOTING, EnumGem.TURQUOISE, "gemEmerald", 2);
        addTokenRecipe(Enchantments.SHARPNESS, EnumGem.RUBY, Items.FLINT, 5);
        addTokenRecipe(Enchantments.SMITE, EnumGem.PERIDOT, Items.ROTTEN_FLESH, 5);
        addTokenRecipe(Enchantments.SWEEPING, EnumGem.MOLDAVITE, Items.REEDS, 5);
        addTokenRecipe(ModEnchantments.lifeSteal, EnumGem.MORGANITE, Items.GOLDEN_APPLE, 3);
        addTokenRecipe(ModEnchantments.magicDamage, EnumGem.LEPIDOLITE, Items.BLAZE_ROD, 4);
        addTokenRecipe(ModEnchantments.iceAspect, EnumGem.OPAL, Blocks.ICE, 5);
        addTokenRecipe(ModEnchantments.lightningAspect, EnumGem.HELIODOR, Items.GLOWSTONE_DUST, 4);

        // Ranged weapons
        addTokenRecipe(Enchantments.FLAME, EnumGem.SPINEL, Items.BLAZE_ROD, 2);
        addTokenRecipe(Enchantments.INFINITY, EnumGem.AMETRINE, Items.ENDER_EYE, 4);
        addTokenRecipe(Enchantments.POWER, EnumGem.CARNELIAN, Items.ARROW, 5);
        addTokenRecipe(Enchantments.PUNCH, EnumGem.JASPER, Blocks.PISTON, 2);

        // Fishing Rod
        addTokenRecipe(Enchantments.LUCK_OF_THE_SEA, EnumGem.AQUAMARINE,
                new ItemStack(Items.FISH, 1, OreDictionary.WILDCARD_VALUE), 5);
        addTokenRecipe(Enchantments.LURE, EnumGem.INDICOLITE, Blocks.TRIPWIRE_HOOK, 4);

        // Armor
        addTokenRecipe(Enchantments.AQUA_AFFINITY, EnumGem.BLUE_TOPAZ, "blockLapis", 2);
        addTokenRecipe(Enchantments.BLAST_PROTECTION, EnumGem.ONYX, Items.GUNPOWDER, 5);
        ItemStack plume = ModItems.craftingMaterial.plume;
        addTokenRecipe(Enchantments.FEATHER_FALLING, EnumGem.MALACHITE, plume, 2);
        addTokenRecipe(Enchantments.FIRE_PROTECTION, EnumGem.CITRINE, Items.MAGMA_CREAM, 2);
        addTokenRecipe(Enchantments.PROJECTILE_PROTECTION, EnumGem.AGATE, Items.ARROW, 4);
        addTokenRecipe(Enchantments.PROTECTION, EnumGem.IOLITE, Blocks.IRON_BARS, 4);
        addTokenRecipe(Enchantments.RESPIRATION, EnumGem.TANZANITE, new ItemStack(Items.FISH, 1, 3), 1);
        addTokenRecipe(Enchantments.THORNS, EnumGem.TOPAZ, new ItemStack(Blocks.DOUBLE_PLANT, 1, 4), 2);
        addTokenRecipe(Enchantments.DEPTH_STRIDER, EnumGem.MOONSTONE, Blocks.CLAY, 3);
        ItemStack enderFrost = ModItems.craftingMaterial.enderFrost;
        addTokenRecipe(Enchantments.FROST_WALKER, EnumGem.ALEXANDRITE, enderFrost, 4);
    }

    public void addModRecipes() {

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

    public void addTokenRecipe(Enchantment ench, EnumGem gem, Object other, int otherCount) {
        if ((ench == Enchantments.FROST_WALKER && GemsConfig.RECIPE_TOKEN_FROST_WALKER_DISABLE)
                || (ench == Enchantments.MENDING && GemsConfig.RECIPE_TOKEN_MENDING_DISABLE)) {
            return;
        }

        // Add a default outline color based on gem color.
        if (!colorMap.containsKey(ench))
            colorMap.put(ench, gem.getColor());

        String line1 = "g g";
        String line2 = otherCount > 3 ? "oto" : " t ";
        String line3 = otherCount == 3 || otherCount > 4 ? "ooo" : (otherCount == 2 || otherCount == 4 ? "o o" : " o ");

        ItemStack token = constructToken(ench);
        String recipeName = "enchantment_token_" + ench.getName().replaceAll(":", "_");
        SilentGems.registry.getRecipeMaker().addShapedOre(recipeName, token, line1, line2, line3, 'g',
                gem.getItemOreName(), 'o', other, 't', new ItemStack(this, 1, BLANK_META));

        // Add to recipe map (tooltip recipe info)
        String recipeString = "2 " + gem.getItemOreName() + ";" + otherCount + " ";
        if (other instanceof String)
            recipeString += (String) other;
        else if (other instanceof ItemStack)
            recipeString += ((ItemStack) other).getDisplayName();
        else if (other instanceof Block)
            recipeString += (new ItemStack((Block) other)).getDisplayName();
        else if (other instanceof Item)
            recipeString += (new ItemStack((Item) other)).getDisplayName();
        recipeMap.put(ench, recipeString);
    }

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
        for (String type : MODEL_TYPES) {
            SilentGems.registry.setModel(this, i++, Names.ENCHANTMENT_TOKEN + "_" + type.toLowerCase(Locale.ROOT));
        }
        SilentGems.registry.setModel(this, BLANK_META, Names.ENCHANTMENT_TOKEN);
    }

    private String getEnchantmentDebugInfo(Enchantment ench) {
        String str = ench.toString();
        str += "\n    Name: " + ench.getName();
        str += "\n    Registry Name: " + ench.getRegistryName();
        str += "\n    Translated Name: " + ench.getTranslatedName(1);
        str += "\n    Max Level: " + ench.getMaxLevel();
        str += "\n    Type: " + ench.type;
        str += "\n    Allowed On Books: " + ench.isAllowedOnBooks();
        str += "\n    Curse: " + ench.isCurse();
        str += "\n    Treasure: " + ench.isTreasureEnchantment();
        return str;
    }
}
