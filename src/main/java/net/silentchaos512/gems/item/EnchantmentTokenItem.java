package net.silentchaos512.gems.item;

import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentData;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.EnchantmentType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.loading.moddiscovery.ModInfo;
import net.minecraftforge.registries.ForgeRegistries;
import net.silentchaos512.gems.SilentGems;
import net.silentchaos512.gems.client.key.KeyTracker;
import net.silentchaos512.gems.init.GemsItemGroups;
import net.silentchaos512.gems.init.GemsItems;
import net.silentchaos512.lib.event.ClientTicks;

import javax.annotation.Nullable;
import java.util.*;
import java.util.Map.Entry;

public final class EnchantmentTokenItem extends Item {
    public enum Icon {
        ANY, ARMOR, BOW, EMPTY, FISHING_ROD, SWORD, TOOL, TRIDENT, UNKNOWN;

        public String getName() {
            return name().toLowerCase(Locale.ROOT);
        }
    }

    public static final ResourceLocation MODEL_INDEX = SilentGems.getId("model_index");
    private static final String NBT_ENCHANTMENTS = "TokenEnchantments";

    private static final Map<Enchantment, Integer> OUTLINE_COLOR_MAP = new HashMap<>();
    private static final Map<String, Icon> MODELS_BY_TYPE = new HashMap<>();

    static {
        MODELS_BY_TYPE.put(EnchantmentType.VANISHABLE.toString(), Icon.ANY);
        MODELS_BY_TYPE.put(EnchantmentType.BREAKABLE.toString(), Icon.ANY);
        MODELS_BY_TYPE.put(EnchantmentType.ARMOR.toString(), Icon.ARMOR);
        MODELS_BY_TYPE.put(EnchantmentType.ARMOR_CHEST.toString(), Icon.ARMOR);
        MODELS_BY_TYPE.put(EnchantmentType.ARMOR_FEET.toString(), Icon.ARMOR);
        MODELS_BY_TYPE.put(EnchantmentType.ARMOR_HEAD.toString(), Icon.ARMOR);
        MODELS_BY_TYPE.put(EnchantmentType.ARMOR_LEGS.toString(), Icon.ARMOR);
        MODELS_BY_TYPE.put(EnchantmentType.WEARABLE.toString(), Icon.ARMOR);
        MODELS_BY_TYPE.put(EnchantmentType.BOW.toString(), Icon.BOW);
        MODELS_BY_TYPE.put(EnchantmentType.DIGGER.toString(), Icon.TOOL);
        MODELS_BY_TYPE.put(EnchantmentType.FISHING_ROD.toString(), Icon.FISHING_ROD);
        MODELS_BY_TYPE.put(EnchantmentType.TRIDENT.toString(), Icon.TRIDENT);
        MODELS_BY_TYPE.put(EnchantmentType.WEAPON.toString(), Icon.SWORD);
    }

    public EnchantmentTokenItem() {
        super(new Properties().group(GemsItemGroups.UTILITY));
    }

    // ==============================================
    // Methods for "constructing" enchantment tokens.
    // ==============================================

    public static ItemStack construct(EnchantmentData data) {
        ItemStack stack = new ItemStack(GemsItems.ENCHANTMENT_TOKEN);
        addEnchantment(stack, data);
        return stack;
    }

    public static void addEnchantment(ItemStack stack, Enchantment enchantment, int level) {
        addEnchantment(stack, new EnchantmentData(enchantment, level));
    }

    public static void addEnchantment(ItemStack stack, EnchantmentData data) {
        ListNBT tagList = getEnchantments(stack);
        boolean needToAddEnchantment = true;
        ResourceLocation id = ForgeRegistries.ENCHANTMENTS.getKey(data.enchantment);

        for (int i = 0; i < tagList.size(); ++i) {
            CompoundNBT tags = tagList.getCompound(i);
            ResourceLocation existingId = ResourceLocation.tryCreate(tags.getString("id"));
            if (existingId != null && existingId.equals(id)) {
                if (tags.getInt("lvl") < data.enchantmentLevel) {
                    tags.putShort("lvl", (short) data.enchantmentLevel);
                }

                needToAddEnchantment = false;
                break;
            }
        }

        if (needToAddEnchantment) {
            CompoundNBT tags = new CompoundNBT();
            tags.putString("id", String.valueOf(id));
            tags.putShort("lvl", (short) data.enchantmentLevel);
            tagList.add(tags);
        }

        stack.getOrCreateTag().put(NBT_ENCHANTMENTS, tagList);
    }

    public static ListNBT getEnchantments(ItemStack stack) {
        CompoundNBT tags = stack.getTag();
        return tags != null ? tags.getList(NBT_ENCHANTMENTS, 10) : new ListNBT();
    }

    private static Map<Enchantment, Integer> getEnchantmentMap(ItemStack stack) {
        Map<Enchantment, Integer> map = new HashMap<>();
        ListNBT tagList = getEnchantments(stack);

        for (int i = 0; i < tagList.size(); ++i) {
            CompoundNBT tag = tagList.getCompound(i);
            ResourceLocation id = ResourceLocation.tryCreate(tag.getString("id"));
            Enchantment enchantment = ForgeRegistries.ENCHANTMENTS.getValue(id);
            if (enchantment != null) {
                map.put(enchantment, tag.getInt("lvl"));
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

        // Enchantments on token
        Map<Enchantment, Integer> enchantmentsOnToken = getEnchantmentMap(token);
        if (enchantmentsOnToken.isEmpty()) {
            return false;
        }

        // Enchantments on tool
        Map<Enchantment, Integer> enchantmentsOnTool = EnchantmentHelper.getEnchantments(tool);

        // Make sure all enchantments can apply to the tool
        for (Entry<Enchantment, Integer> entry : enchantmentsOnToken.entrySet()) {
            Enchantment ench = entry.getKey();
            // Valid for tool?
            if (!ench.canApply(tool)) {
                return false;
            }

            // Does new enchantment conflict with any existing ones?
            for (Enchantment enchTool : enchantmentsOnTool.keySet()) {
                if (!ench.equals(enchTool) && !ench.isCompatibleWith(enchTool)) {
                    return false;
                }
            }
        }

        // Appy enchantments to new copy of tool
        if (!mergeEnchantmentLists(enchantmentsOnToken, enchantmentsOnTool)) {
            return false;
        }
        EnchantmentHelper.setEnchantments(enchantmentsOnToken, tool);

        return true;
    }

    private static boolean mergeEnchantmentLists(Map<Enchantment, Integer> ench1, Map<Enchantment, Integer> ench2) {
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

    // =========================
    // Item and ItemSL overrides
    // =========================

    @Override
    public void addInformation(ItemStack stack, @Nullable World world, List<ITextComponent> list, ITooltipFlag flag) {
        Map<Enchantment, Integer> enchants = getEnchantmentMap(stack);

        if (enchants.size() == 1) {
            Enchantment ench = enchants.keySet().iterator().next();
            list.add(subText("maxLevel", ench.getMaxLevel()));

            // Recipe info
//            if (KeyTracker.isControlDown()) {
//                list.add(subText("materials"));
//                String recipeString = recipeMap.get(ench);
//                if (recipeString != null && !recipeString.isEmpty()) {
//                    for (String str : recipeString.split(";")) {
//                        list.add("  " + str);
//                    }
//                }
//            } else {
//                list.add(SilentGems.i18n.subText(this, "pressCtrl"));
//            }

            // Debug info
            if (KeyTracker.isAltDown()) {
                ResourceLocation registryName = Objects.requireNonNull(ench.getRegistryName());
                list.add(new StringTextComponent(registryName.toString())
                        .func_240699_a_(TextFormatting.DARK_GRAY));
            }
        }

        // Enchantment list
        for (Entry<Enchantment, Integer> entry : enchants.entrySet()) {
            Enchantment enchantment = entry.getKey();
            ITextComponent enchName = enchantment.getDisplayName(entry.getValue());
            ResourceLocation registryName = Objects.requireNonNull(enchantment.getRegistryName());
            String modName = "Unknown";
            for (ModInfo info : ModList.get().getMods()) {
                if (info.getModId().equals(registryName.getNamespace())) {
                    modName = info.getDisplayName();
                    break;
                }
            }
            list.add(subText("enchNameWithMod", enchName, modName));
            String descKey = enchantment.getName() + ".desc";
            if (I18n.hasKey(descKey)) {
                list.add(new TranslationTextComponent(descKey).func_240699_a_(TextFormatting.ITALIC));
            }
        }
    }

    private ITextComponent subText(String key, Object... formatArgs) {
        ResourceLocation id = Objects.requireNonNull(getRegistryName());
        String fullKey = String.format("item.%s.%s.%s", id.getNamespace(), id.getPath(), key);
        return new TranslationTextComponent(fullKey, formatArgs);
    }

    @Override
    public void fillItemGroup(ItemGroup group, NonNullList<ItemStack> items) {
        if (!isInGroup(group)) return;

        List<ItemStack> tokens = NonNullList.create();
        for (Enchantment enchantment : ForgeRegistries.ENCHANTMENTS) {
            tokens.add(construct(new EnchantmentData(enchantment, 1)));
        }

        // Sort by type, then enchantment name
        tokens.sort(EnchantmentTokenItem::compareEnchantmentNames);
        items.addAll(tokens);
    }

    private static int compareEnchantmentNames(ItemStack o1, ItemStack o2) {
        // First compare icon names (group together enchantments of one type)
        int k = -getModelIcon(o1).getName().compareTo(getModelIcon(o2).getName());
        if (k == 0) {
            Enchantment ench1 = getSingleEnchantment(o1);
            Enchantment ench2 = getSingleEnchantment(o2);
            if (ench1 != null && ench2 != null) {
                // If this crashes the enchantment is at fault, nothing should be done about it.
                ITextComponent name1 = ench1.getDisplayName(1);
                ITextComponent name2 = ench2.getDisplayName(1);
                return name1.getString().compareTo(name2.getString());
            }
        }
        return k;
    }

    @Nullable
    public static Enchantment getSingleEnchantment(ItemStack token) {
        Map<Enchantment, Integer> map = getEnchantmentMap(token);
        if (map.size() != 1) return null;
        return map.keySet().iterator().next();
    }

    // =====================
    // = Rendering methods =
    // =====================

    @Override
    public boolean hasEffect(ItemStack stack) {
        return false;
    }

    private static float OUTLINE_PULSATE_SPEED = 1f / (3f * (float) Math.PI);

    public static int getItemColor(ItemStack stack, int tintIndex) {
        if (tintIndex != 1) return 0xFFFFFF;

        Enchantment ench = getSingleEnchantment(stack);
        if (ench != null && OUTLINE_COLOR_MAP.containsKey(ench)) {
            int k = OUTLINE_COLOR_MAP.get(ench);
            int r = (k >> 16) & 255;
            int g = (k >> 8) & 255;
            int b = k & 255;

            int j = (int) (160 * MathHelper.sin(ClientTicks.ticksInGame() * OUTLINE_PULSATE_SPEED));
            j = MathHelper.clamp(j, 0, 255);
            r = MathHelper.clamp(r + j, 0, 255);
            g = MathHelper.clamp(g + j, 0, 255);
            b = MathHelper.clamp(b + j, 0, 255);
            return (r << 16) | (g << 8) | b;
        }
        return 0x8040CC;
    }

    public static void setOutlineColor(Enchantment enchantment, int color) {
        OUTLINE_COLOR_MAP.put(enchantment, color);
    }

    public static float getModel(ItemStack stack, World world, LivingEntity entity) {
        return getModelIcon(stack).ordinal();
    }

    private static Icon getModelIcon(ItemStack stack) {
        Map<Enchantment, Integer> map = getEnchantmentMap(stack);
        if (map.isEmpty()) return Icon.EMPTY;

        EnchantmentType type = map.keySet().iterator().next().type;
        if (type == null) return Icon.UNKNOWN;

        return MODELS_BY_TYPE.getOrDefault(type.toString(), Icon.UNKNOWN);
    }

    private String getEnchantmentDebugInfo(Enchantment ench) {
        String str = ench.toString();
        str += "\n    Name: " + ench.getName();
        str += "\n    Registry Name: " + ench.getRegistryName();
        str += "\n    Name: " + ench.getName();
        str += "\n    Max Level: " + ench.getMaxLevel();
        str += "\n    Type: " + ench.type;
        str += "\n    Allowed On Books: " + ench.isAllowedOnBooks();
        str += "\n    Curse: " + ench.isCurse();
        str += "\n    Treasure: " + ench.isTreasureEnchantment();
        return str;
    }
}
