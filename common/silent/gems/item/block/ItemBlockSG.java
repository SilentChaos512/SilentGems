package silent.gems.item.block;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlockWithMetadata;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IIcon;
import silent.gems.block.BlockSG;
import silent.gems.core.util.LocalizationHelper;
import silent.gems.lib.Strings;

public class ItemBlockSG extends ItemBlockWithMetadata {

    protected boolean gemSubtypes = false;
    protected Block block;
    protected String itemName = "null";
    
    public ItemBlockSG(Block block) {

        super(block, block);
        
        // Block and block name
        this.block = block;
        this.itemName = block.getUnlocalizedName().substring(5);
        
        // Subtypes?
        if (block instanceof BlockSG) {
            BlockSG b = (BlockSG) block;
            gemSubtypes = b.getHasGemSubtypes();
            hasSubtypes = b.getHasSubtypes();
        }
    }

    @Override
    public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean par4) {
        
        int i = 1;
        String s = LocalizationHelper.getBlockDescription(itemName, i);
        while (!s.equals(LocalizationHelper.getBlockDescriptionKey(itemName, i)) && i < 8) {
            list.add(EnumChatFormatting.ITALIC + s);
            s = LocalizationHelper.getBlockDescription(itemName, ++i);
        }
        
        if (i == 1) {
            s = LocalizationHelper.getBlockDescription(itemName, 0);
            if (!s.equals(LocalizationHelper.getBlockDescriptionKey(itemName, 0))) {
                list.add(EnumChatFormatting.ITALIC + LocalizationHelper.getBlockDescription(itemName, 0));
            }
        }
    }
    
    @Override
    public IIcon getIconFromDamage(int meta) {
        
        if (hasSubtypes && block instanceof BlockSG) {
            BlockSG b = (BlockSG) block;
            if (b.icons != null && meta < b.icons.length) {
                return b.icons[meta];
            }
        }
        return super.getIconFromDamage(meta);
    }
    
    @Override
    public String getUnlocalizedName(ItemStack stack) {
        
        StringBuilder sb = new StringBuilder("tile.");
        sb.append(Strings.RESOURCE_PREFIX);
        sb.append(itemName);
        
        if (hasSubtypes) {
            sb.append(stack.getItemDamage());
        }

        return sb.toString();
    }
}
