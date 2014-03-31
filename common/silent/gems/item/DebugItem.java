package silent.gems.item;

import silent.gems.lib.Names;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class DebugItem extends ItemSG {

    public DebugItem(int id) {

        super(id);
        setUnlocalizedName(Names.DEBUG_ITEM);
    }

    @Override
    public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player) {

        // Make player hungry.
        int k = player.getFoodStats().getFoodLevel();
        player.getFoodStats().setFoodLevel(k > 2 ? k - 2 : k);
        // Remove potion effect.
        player.curePotionEffects(new ItemStack(Item.bucketMilk));
        
        return stack;
    }
}
