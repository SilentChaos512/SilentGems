package silent.gems.core.tick;

import java.util.EnumSet;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import silent.gems.core.registry.SRegistry;
import silent.gems.enchantment.ModEnchantments;
import silent.gems.item.ChaosGem;
import silent.gems.item.TorchBandolier;
import silent.gems.lib.Names;
import silent.gems.lib.Strings;
import cpw.mods.fml.common.IScheduledTickHandler;
import cpw.mods.fml.common.TickType;

public class ItemTickHandler implements IScheduledTickHandler {

    @Override
    public void tickStart(EnumSet<TickType> type, Object... tickData) {

        EntityPlayer player = (EntityPlayer) tickData[0];
        
        int torchBandolierId = SRegistry.getItem(Names.TORCH_BANDOLIER).itemID;
        int chaosGemId = SRegistry.getItem(Names.CHAOS_GEM).itemID;

        for (ItemStack stack : player.inventory.mainInventory) {
            if (stack != null) {
                if (stack.itemID == torchBandolierId && stack.stackTagCompound != null
                        && stack.stackTagCompound.getBoolean(Strings.TORCH_BANDOLIER_AUTO_FILL)) {
                    ((TorchBandolier) stack.getItem()).absorbTorches(stack, player);
                }
                else if (stack.itemID == chaosGemId) {
                   ((ChaosGem) stack.getItem()).doTick(stack, player); 
                }
            }
        }
        
        ItemStack stack;
        for (int i = 0; i < InventoryPlayer.getHotbarSize(); ++i) {
            stack = player.inventory.getStackInSlot(i);
            if (stack != null) {
                ModEnchantments.mending.tryActivate(player, stack);
            }
        }
    }

    @Override
    public void tickEnd(EnumSet<TickType> type, Object... tickData) {

    }

    @Override
    public EnumSet<TickType> ticks() {

        return EnumSet.of(TickType.PLAYER);
    }

    @Override
    public String getLabel() {

        return "ItemScheduled";
    }

    @Override
    public int nextTickSpacing() {

        return 20;
    }

}
