package silent.gems.item;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import silent.gems.lib.Names;

public class DebugItem extends ItemSG {

    public DebugItem() {

        setMaxStackSize(1);
        setUnlocalizedName(Names.DEBUG_ITEM);
    }

    @Override
    public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player) {

        // Make player hungry.
        int k = player.getFoodStats().getFoodLevel();
        player.getFoodStats().setFoodLevel(k > 2 ? k - 2 : k);
        // Remove potion effect.
        player.curePotionEffects(new ItemStack(Items.milk_bucket));

        return stack;
    }
}
