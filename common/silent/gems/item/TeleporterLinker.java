package silent.gems.item;

import java.util.List;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumChatFormatting;
import silent.gems.core.util.LocalizationHelper;
import silent.gems.core.util.LogHelper;
import silent.gems.lib.Names;
import silent.gems.lib.Strings;
import cpw.mods.fml.common.registry.GameRegistry;

public class TeleporterLinker extends ItemSG {

    public TeleporterLinker() {

        setMaxStackSize(1);
        setUnlocalizedName(Names.TELEPORTER_LINKER);
    }

    @Override
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
            list.add(EnumChatFormatting.GREEN + LocalizationHelper.getOtherItemKey(itemName, "Active"));
            list.add(LogHelper.coordFromNBT(stack.stackTagCompound));
        }
        else {
            list.add(EnumChatFormatting.RED + LocalizationHelper.getOtherItemKey(itemName, "Inactive"));
        }

        list.add(EnumChatFormatting.DARK_GRAY + LocalizationHelper.getItemDescription(itemName, 0));
    }

    @Override
    public void addRecipes() {

        GameRegistry.addShapedRecipe(new ItemStack(this), "c", "s", 'c', CraftingMaterial.getStack(Names.CHAOS_ESSENCE_PLUS), 's',
                CraftingMaterial.getStack(Names.ORNATE_STICK));
    }

    @Override
    public boolean hasEffect(ItemStack stack, int pass) {

        return stack.stackTagCompound != null && stack.stackTagCompound.hasKey(Strings.TELEPORTER_LINKER_STATE)
                && stack.stackTagCompound.getBoolean(Strings.TELEPORTER_LINKER_STATE);
    }
}
