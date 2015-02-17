package silent.gems.item.block;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import silent.gems.block.BlockSG;
import silent.gems.core.util.LocalizationHelper;
import silent.gems.lib.Strings;

public class ItemBlockSG extends ItemBlock {

    protected boolean gemSubtypes = false;
    protected Block block;
    protected String itemName = "null";
    
    public ItemBlockSG(Block block) {

        super(block);
        this.setMaxDamage(0);
        
        // Block and block name
        this.block = block;
        this.itemName = block.getUnlocalizedName().substring(5);
        
        // Subtypes?
        if (block instanceof BlockSG) {
            BlockSG blockSG = (BlockSG) block;
            gemSubtypes = blockSG.getHasGemSubtypes();
            this.setHasSubtypes(blockSG.getHasSubtypes());
        }
    }
    
    @Override
    public int getMetadata(int meta) {
      
      return meta;
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
    public String getUnlocalizedName(ItemStack stack) {
        
        String s = "tile.";
        s += Strings.RESOURCE_PREFIX;
        s += itemName;
        
        if (hasSubtypes) {
            s += stack.getItemDamage();
        }

        return s;
    }
}
