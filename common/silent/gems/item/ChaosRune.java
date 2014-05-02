package silent.gems.item;

import java.util.List;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import silent.gems.core.util.LocalizationHelper;
import silent.gems.lib.Names;
import silent.gems.lib.Strings;
import silent.gems.lib.buff.ChaosBuff;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ChaosRune extends ItemSG {

    public ChaosRune(int id) {

        super(id);
        setMaxStackSize(16);
        setHasSubtypes(true);
        setMaxDamage(0);
        setCreativeTab(CreativeTabs.tabMaterials);
        setUnlocalizedName(Names.CHAOS_RUNE);
        rarity = EnumRarity.rare;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean par4) {

        int d = stack.getItemDamage();

        if (d >= 0 && d < ChaosBuff.all.size()) {
            list.add(EnumChatFormatting.GOLD
                    + LocalizationHelper.getLocalizedString(Strings.BUFF_RESOURCE_PREFIX + ChaosBuff.all.get(d).name));
            list.add(LocalizationHelper.getMessageText(itemName + "." + ChaosBuff.all.get(d).name));
        }
        else {
            list.add(EnumChatFormatting.RED + "Invalid meta value!");
        }
        
        // Temporary
//        if (d == ChaosBuff.getBuffByName("flight").id) {
//            list.add(EnumChatFormatting.RED + "Buggy on servers (kicks for flying).");
//            list.add(EnumChatFormatting.RED + "Trying to fix :(");
//        }
        
        // Information on how to use.
        list.add(LocalizationHelper.getMessageText(Strings.CHAOS_RUNE_1, EnumChatFormatting.DARK_GRAY));
    }

    @Override
    @SuppressWarnings({ "rawtypes", "unchecked" })
    @SideOnly(Side.CLIENT)
    public void getSubItems(int itemID, CreativeTabs tabs, List list) {

        for (int i = 0; i < ChaosBuff.all.size(); ++i) {
            list.add(new ItemStack(this, 1, i));
        }
    }
    
    @Override
    public String getUnlocalizedName(ItemStack stack) {

        return getUnlocalizedName(Names.CHAOS_RUNE);
    }

    @Override
    public void addRecipes() {

        // TODO
    }
}
