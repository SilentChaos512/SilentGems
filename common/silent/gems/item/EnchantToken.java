package silent.gems.item;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.client.resources.I18n;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.Icon;
import silent.gems.core.util.LocalizationHelper;
import silent.gems.enchantment.ModEnchantments;
import silent.gems.item.tool.GemAxe;
import silent.gems.item.tool.GemHoe;
import silent.gems.item.tool.GemPickaxe;
import silent.gems.item.tool.GemShovel;
import silent.gems.item.tool.GemSword;
import silent.gems.item.tool.Sigil;
import silent.gems.lib.EnumGem;
import silent.gems.lib.Names;
import silent.gems.lib.Strings;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;


public class EnchantToken extends ItemSG {

    /**
     * Small class for storing enchantment data in hash map
     */
    public static class EnchData {

        public byte validTools;
        public Enchantment enchantment;
        public char rune;
        public byte xpCost;

        public String getName() {

            return enchantment.getName();
        }

        public int getMaxLevel() {

            return enchantment.getMaxLevel();
        }
    }
    
    //public final static Icon[] iconRunes = new Icon[26];

    // These constants are used to store which tools an enchantment is valid
    // for. See init().
    public final static byte T_SIGIL = 32;
    public final static byte T_SWORD = 16;
    public final static byte T_PICKAXE = 8;
    public final static byte T_SHOVEL = 4;
    public final static byte T_AXE = 2;
    public final static byte T_HOE = 1;
    
//    @SideOnly(Side.CLIENT)
//    Icon iconS, iconT;

    /**
     * Stores the enchantments that there are tokens for.
     */
    public static HashMap<Integer, EnchData> enchants = new HashMap<Integer, EnchData>();

    public EnchantToken(int id) {

        super(id);
        setMaxStackSize(64);
        setHasSubtypes(true);
        setMaxDamage(0);
        setCreativeTab(CreativeTabs.tabMaterials);
    }

    /**
     * Populates the enchants hash map.
     */
    public static void init() {

        if (!enchants.isEmpty()) {
            return;
        }

        addEnchantment(Enchantment.baneOfArthropods, T_SWORD | T_PICKAXE | T_SHOVEL, 'S', 5);
        addEnchantment(Enchantment.efficiency, T_SWORD | T_PICKAXE | T_SHOVEL | T_AXE, 'T', 5);
        addEnchantment(Enchantment.fireAspect, T_SWORD, 'S', 5);
        addEnchantment(Enchantment.fortune, T_PICKAXE | T_SHOVEL | T_AXE | T_HOE, 'T', 15);
        addEnchantment(Enchantment.knockback, T_SWORD | T_AXE | T_HOE, 'S', 5);
        addEnchantment(Enchantment.looting, T_SWORD, 'S', 15);
        addEnchantment(Enchantment.sharpness, T_SWORD | T_AXE, 'S', 10);
        addEnchantment(Enchantment.silkTouch, T_SWORD | T_PICKAXE | T_SHOVEL | T_AXE, 'T', 10);
        addEnchantment(Enchantment.smite, T_SWORD | T_AXE | T_HOE, 'S', 5);
        addEnchantment(Enchantment.unbreaking, T_SWORD | T_PICKAXE | T_SHOVEL | T_AXE | T_HOE | T_SIGIL, 'T', 5);

        // TODO: Uncomment this.
//        addEnchantment(ModEnchantments.iceAspect, T_SWORD, 'S');
        addEnchantment(ModEnchantments.mending, T_SWORD | T_PICKAXE | T_SHOVEL | T_AXE | T_HOE, 'T', 10);
//        addEnchantment(ModEnchantments.nihil, T_SWORD, 'S');
    }

    /**
     * Adds an enchantment to the hash map. validTools is appended to the enchantment name after VALID_TOOL_SEP.
     * 
     * @param e
     * @param validTools
     */
    private static void addEnchantment(Enchantment e, int validTools, char rune, int xpCost) {

        EnchData data = new EnchantToken.EnchData();
        data.enchantment = e;
        data.validTools = (byte) validTools;
        data.rune = rune;
        data.xpCost = (byte) xpCost;
        enchants.put(e.effectId, data);
    }

    /**
     * Gets the localized name of the enchantment by first stripping off the valid tool data.
     * 
     * @param key
     * @return
     */
    public static String getEnchantmentName(int key) {

        String s = "";

        if (enchants.containsKey(key)) {
            s = enchants.get(key).getName();
            s = I18n.getString(s);
        }

        return s;
    }

    /**
     * Creates a String listing the tools this enchantment can be applied to.
     * 
     * @param key
     * @return
     */
    public static String validToolsFor(int key) {

        List l = new ArrayList<String>();
        int k = enchants.get(key).validTools;

        try {
            if ((k & T_SWORD) != 0)
                l.add(LocalizationHelper.getMessageText(Strings.TOOL_SWORD, Strings.EMPTY));
            if ((k & T_PICKAXE) != 0)
                l.add(LocalizationHelper.getMessageText(Strings.TOOL_PICKAXE, Strings.EMPTY));
            if ((k & T_SHOVEL) != 0)
                l.add(LocalizationHelper.getMessageText(Strings.TOOL_SHOVEL, Strings.EMPTY));
            if ((k & T_AXE) != 0)
                l.add(LocalizationHelper.getMessageText(Strings.TOOL_AXE, Strings.EMPTY));
            if ((k & T_HOE) != 0)
                l.add(LocalizationHelper.getMessageText(Strings.TOOL_HOE, Strings.EMPTY));
            if ((k & T_SIGIL) != 0)
                l.add(LocalizationHelper.getMessageText(Strings.TOOL_SIGIL, Strings.EMPTY));

            StringBuilder sb = new StringBuilder();

            // Separate each item with commas
            for (Object o : l) {
                if (sb.length() > 0) {
                    sb.append(", ");
                }
                sb.append((String) o);
            }

            return sb.toString();
        }
        catch (Exception ex) {
        }

        return "";
    }

    /**
     * Determine if token can be applied to the tool. Checks that the tool is the right type, the enchantments don't
     * conflict, and the enchantment can be "leveled up".
     * 
     * @param token
     * @param tool
     * @return
     */
    public static boolean capApplyTokenToTool(ItemStack token, ItemStack tool) {

        int k = token.getItemDamage();
        if (!enchants.containsKey(k)) {
            return false;
        }

        EnchData e = enchants.get(k);
        k = enchants.get(k).validTools;

        if ((tool.getItem() instanceof GemSword && (k & T_SWORD) != 0)
                || (tool.getItem() instanceof GemPickaxe && (k & T_PICKAXE) != 0)
                || (tool.getItem() instanceof GemShovel && (k & T_SHOVEL) != 0)
                || (tool.getItem() instanceof GemAxe && (k & T_AXE) != 0)
                || (tool.getItem() instanceof Sigil && (k & T_SIGIL) != 0)
                || (tool.getItem() instanceof GemHoe && (k & T_HOE) != 0)) {
            // Token and tool type match.
            // Does tool have any enchantments?
            if (tool.hasTagCompound()) {
                if (!tool.stackTagCompound.hasKey("ench")) {
                    return true;
                }
            }
            else if (!tool.hasTagCompound()) {
                return true;
            }

            // Does tool already have this enchantment? If so, can it be
            // upgraded?
            k = EnchantmentHelper.getEnchantmentLevel(e.enchantment.effectId, tool);
            if (k == 0) {
                // Tool does not have this enchantment. Does it conflict with
                // existing enchants?
                for (int i = 0; i < tool.getEnchantmentTagList().tagCount(); ++i) {
                    k = ((NBTTagCompound) tool.getEnchantmentTagList().tagAt(i)).getShort("id");
                    if (!e.enchantment.canApplyTogether(Enchantment.enchantmentsList[k])
                            || !Enchantment.enchantmentsList[k].canApplyTogether(e.enchantment)) {
                        return false;
                    }
                }
                return true;
            }
            else if (k < e.getMaxLevel()) {
                // Tool has enchantment, but it can be leveled up.
                return true;
            }
        }

        return false;
    }

    /**
     * Applies the token's enchantment to the tool. Need to check canApplyTokenToTool before calling.
     * 
     * @param token
     * @param tool
     */
    public static void enchantTool(ItemStack token, ItemStack tool) {

        int k = token.getItemDamage();
        EnchData e = enchants.get(k);
        k = EnchantmentHelper.getEnchantmentLevel(e.enchantment.effectId, tool);

        // Adding enchantment is easy, leveling it up is a bit harder.
        if (k == 0) {
            tool.addEnchantment(e.enchantment, 0);
        }

        if (tool.stackTagCompound == null) {
            tool.setTagCompound(new NBTTagCompound());
        }
        if (!tool.stackTagCompound.hasKey("ench")) {
            tool.stackTagCompound.setTag("ench", new NBTTagList("ench"));
        }

        NBTTagCompound t;
        for (int i = 0; i < tool.getEnchantmentTagList().tagCount(); ++i) {
            t = (NBTTagCompound) tool.getEnchantmentTagList().tagAt(i);
            k = t.getShort("id");
            if (k == e.enchantment.effectId) {
                k = t.getShort("lvl");
                t.setShort("lvl", (short) (k + 1));
            }
        }
    }

    @Override
    @SuppressWarnings({ "rawtypes", "unchecked" })
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean par4) {

        final int k = stack.getItemDamage();

        if (k == 0) {
            list.add(LocalizationHelper.getMessageText(Strings.EFFECT_EMPTY, EnumChatFormatting.GOLD));
            list.add(LocalizationHelper.getMessageText(Strings.ENCHANT_TOKEN_1, Strings.EMPTY));
        }
        else if (enchants.containsKey(k)) {
            // Enchantment name.
            list.add(EnumChatFormatting.GOLD + getEnchantmentName(k));
            // Description text for my enchantments.
            if (k == ModEnchantments.mending.effectId) {
                list.add(LocalizationHelper.getMessageText(Strings.ENCHANT_MENDING, EnumChatFormatting.DARK_GRAY));
            }
            // TODO: Uncomment this.
//            if (k == ModEnchantments.iceAspect.effectId) {
//                list.add(LocalizationHelper.getMessageText(Strings.ENCHANT_ICE_ASPECT, EnumChatFormatting.DARK_GRAY));
//            }
//            else if (k == ModEnchantments.mending.effectId) {
//                list.add(LocalizationHelper.getMessageText(Strings.ENCHANT_MENDING, EnumChatFormatting.DARK_GRAY));
//            }
//            else if (k == ModEnchantments.nihil.effectId) {
//                list.add(LocalizationHelper.getMessageText(Strings.ENCHANT_NIHIL, EnumChatFormatting.DARK_GRAY));
//            }
            // List of valid tools.
            list.add(LocalizationHelper.getMessageText(Strings.ENCHANT_TOKEN_2, Strings.EMPTY));
            list.add(validToolsFor(k));
            // XP level cost
            list.add(EnumChatFormatting.DARK_GREEN + "Costs " + enchants.get(k).xpCost + " levels to use.");
            list.add(LocalizationHelper.getMessageText(Strings.ENCHANT_TOKEN_3, EnumChatFormatting.DARK_GRAY));
        }
    }

    @Override
    public boolean hasEffect(ItemStack stack, int pass) {

        return stack.getItemDamage() != 0;
    }

    @Override
    public EnumRarity getRarity(ItemStack stack) {

        return stack.getItemDamage() == 0 ? EnumRarity.common : EnumRarity.rare;
    }

    @Override
    @SuppressWarnings({ "rawtypes", "unchecked" })
    @SideOnly(Side.CLIENT)
    public void getSubItems(int itemID, CreativeTabs tabs, List list) {

        list.add(new ItemStack(this, 1, 0));

        for (int k : enchants.keySet()) {
            list.add(new ItemStack(this, 1, k));
        }
    }

    @Override
    public String getUnlocalizedName(ItemStack stack) {

        return getUnlocalizedName(Names.ENCHANT_TOKEN);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void registerIcons(IconRegister reg) {

        String prefix = Strings.RESOURCE_PREFIX + "EnchantmentToken";
//        iconS = reg.registerIcon(prefix + "_S");
//        iconT = reg.registerIcon(prefix + "_T");
        itemIcon = reg.registerIcon(prefix);
    }
    
//    @SideOnly(Side.CLIENT)
//    @Override
//    public boolean requiresMultipleRenderPasses() {
//        
//        return true;
//    }
//    
//    @SideOnly(Side.CLIENT)
//    @Override
//    public Icon getIconFromDamageForRenderPass(int meta, int pass) {
//        
//        if (pass == 0) {
//            return itemIcon;
//        }
//        else {
//            char c = getEnchantmentName(meta).toUpperCase().charAt(0);
//            return iconRunes[c - 'A'];
//        }
//    }
    
    @SideOnly(Side.CLIENT)
    @Override
    public Icon getIconFromDamage(int meta) {
        
        if (!enchants.containsKey(meta)) {
            return itemIcon;
        }
        
        switch (enchants.get(meta).rune) {
//            case 'S':
//                return iconS;
//            case 'T':
//                return iconT;
            default:
                return itemIcon;
        }
    }

    @Override
    public void addRecipes() {

        ItemStack baseToken = new ItemStack(this, 1, 0);
        ItemStack chaosEssence = CraftingMaterial.getStack(Names.CHAOS_ESSENCE);

        GameRegistry.addShapedRecipe(new ItemStack(this, 8, 0), "ggg", "rer", "ggg", 'g', Item.ingotGold, 'r', Item.redstone, 'e',
                chaosEssence);
        
        int gemCount = 2;
        addTokenRecipe(Enchantment.baneOfArthropods.effectId, EnumGem.AMETHYST.getItem(), gemCount, Item.spiderEye, 4, baseToken);
        addTokenRecipe(Enchantment.efficiency.effectId, EnumGem.EMERALD.getItem(), gemCount, Item.ingotGold, 2, baseToken);
        addTokenRecipe(Enchantment.fireAspect.effectId, EnumGem.GARNET.getItem(), gemCount, Item.blazePowder, 4, baseToken);
        addTokenRecipe(Enchantment.fortune.effectId, EnumGem.HELIODOR.getItem(), gemCount, Item.diamond, 3, baseToken);
        addTokenRecipe(Enchantment.knockback.effectId, EnumGem.AQUAMARINE.getItem(), gemCount, Item.feather, 5, baseToken);
        addTokenRecipe(Enchantment.looting.effectId, EnumGem.TOPAZ.getItem(), gemCount, Item.emerald, 2, baseToken);
        addTokenRecipe(Enchantment.sharpness.effectId, EnumGem.RUBY.getItem(), gemCount, Item.flint, 5, baseToken);
        addTokenRecipe(Enchantment.silkTouch.effectId, EnumGem.IOLITE.getItem(), gemCount, Item.emerald, 3, baseToken);
        addTokenRecipe(Enchantment.smite.effectId, EnumGem.PERIDOT.getItem(), gemCount, Item.rottenFlesh, 5, baseToken);
        addTokenRecipe(Enchantment.unbreaking.effectId, EnumGem.SAPPHIRE.getItem(), gemCount, Item.ingotIron, 5, baseToken);
        
        addTokenRecipe(ModEnchantments.mending.effectId, EnumGem.MORGANITE.getItem(), gemCount, CraftingMaterial.getStack(Names.MYSTERY_GOO), 2, baseToken);
        
//        addTokenRecipe(Enchantment.baneOfArthropods.effectId, ruby, 1, Item.spiderEye, 4, baseToken);
//        addTokenRecipe(Enchantment.efficiency.effectId, emerald, 1, Item.ingotGold, 3, baseToken);
//        addTokenRecipe(Enchantment.fireAspect.effectId, ruby, 1, Item.blazePowder, 4, baseToken);
//        addTokenRecipe(Enchantment.fortune.effectId, topaz, 1, Block.blockDiamond, 1, baseToken);
//        addTokenRecipe(Enchantment.knockback.effectId, emerald, 1, Item.feather, 5, baseToken);
//        addTokenRecipe(Enchantment.looting.effectId, topaz, 1, Item.emerald, 2, baseToken);
//        addTokenRecipe(Enchantment.sharpness.effectId, ruby, 1, Item.flint, 5, baseToken);
//        addTokenRecipe(Enchantment.silkTouch.effectId, abyssite, 1, Item.emerald, 2, baseToken);
//        addTokenRecipe(Enchantment.smite.effectId, ruby, 1, Item.rottenFlesh, 5, baseToken);
//        addTokenRecipe(Enchantment.unbreaking.effectId, sapphire, 1, Item.ingotIron, 5, baseToken);

//        addTokenRecipe(ModEnchantments.iceAspect.effectId, sapphire, 1, Block.blockSnow, 3, baseToken);
//        addTokenRecipe(ModEnchantments.mending.effectId, purite, 1, CraftingMaterial.getStack(Names.MYSTERY_GOO), 3, baseToken);
//        addTokenRecipe(ModEnchantments.nihil.effectId, purite, 1, Item.potato, 2, baseToken);
    }

    private void addTokenRecipe(int key, ItemStack gem, int gemCount, ItemStack otherMaterial, int otherCount, ItemStack baseToken) {

        String row1 = gemCount == 1 ? " g " : (gemCount == 2 ? "g g" : "ggg");
        String row2 = otherCount < 4 ? " t " : "mtm";
        String row3 = otherCount == 1 ? " m " : (otherCount == 2 || otherCount == 4 ? "m m" : "mmm");

        GameRegistry.addShapedRecipe(new ItemStack(this, 1, key), row1, row2, row3, 'g', gem, 'm', otherMaterial, 't', baseToken);
    }

    private void addTokenRecipe(int key, ItemStack gem, int gemCount, Block otherMaterial, int otherCount, ItemStack baseToken) {

        addTokenRecipe(key, gem, gemCount, new ItemStack(otherMaterial), otherCount, baseToken);
    }

    private void addTokenRecipe(int key, ItemStack gem, int gemCount, Item otherMaterial, int otherCount, ItemStack baseToken) {

        addTokenRecipe(key, gem, gemCount, new ItemStack(otherMaterial), otherCount, baseToken);
    }
}
