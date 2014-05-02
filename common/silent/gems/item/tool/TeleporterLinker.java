package silent.gems.item.tool;

import java.util.List;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumChatFormatting;
import silent.gems.core.util.LocalizationHelper;
import silent.gems.core.util.LogHelper;
import silent.gems.item.CraftingMaterial;
import silent.gems.item.ItemSG;
import silent.gems.lib.Names;
import silent.gems.lib.Strings;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class TeleporterLinker extends ItemSG {

    public TeleporterLinker(int id) {

        super(id);

        setMaxStackSize(1);
        setCreativeTab(CreativeTabs.tabTools);
        setUnlocalizedName(Names.TELEPORTER_LINKER);
    }

    @Override
    @SuppressWarnings({ "rawtypes", "unchecked" })
    public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean par4) {
        
        // Verify NBT
        if (stack.stackTagCompound == null) {
            stack.stackTagCompound = new NBTTagCompound();
        }
        if (!stack.stackTagCompound.hasKey(Strings.TELEPORTER_LINKER_STATE)) {
            stack.stackTagCompound.setBoolean(Strings.TELEPORTER_LINKER_STATE, false);
        }

        // Display state and coords (if active)
        if (stack.stackTagCompound.getBoolean(Strings.TELEPORTER_LINKER_STATE)) {
            list.add(LocalizationHelper.getMessageText(Strings.TELEPORTER_LINKER_ACTIVE, EnumChatFormatting.GREEN));
            list.add(LogHelper.coordFromNBT(stack.stackTagCompound));
        }
        else {
            list.add(LocalizationHelper.getMessageText(Strings.TELEPORTER_LINKER_INACTIVE, EnumChatFormatting.RED));
        }
        
        list.add(LocalizationHelper.getMessageText(itemName, EnumChatFormatting.DARK_GRAY));
    }

    @Override
    @SideOnly(Side.CLIENT)
    public boolean hasEffect(ItemStack stack, int pass) {

        return stack.stackTagCompound != null && stack.stackTagCompound.hasKey(Strings.TELEPORTER_LINKER_STATE)
                && stack.stackTagCompound.getBoolean(Strings.TELEPORTER_LINKER_STATE);
    }

    @Override
    public void addRecipes() {

        GameRegistry.addShapedRecipe(new ItemStack(this), "c", "s", 'c', CraftingMaterial.getStack(Names.CHAOS_ESSENCE_PLUS), 's',
                CraftingMaterial.getStack(Names.ORNATE_STICK));
    }
}
