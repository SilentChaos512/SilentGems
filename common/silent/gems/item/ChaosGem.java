package silent.gems.item;

import java.util.List;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Icon;
import net.minecraft.world.World;
import silent.gems.core.registry.SRegistry;
import silent.gems.core.util.RecipeHelper;
import silent.gems.lib.EnumGem;
import silent.gems.lib.Names;

public class ChaosGem extends ItemSG {

    public ChaosGem(int id) {

        super(id);
        icons = new Icon[EnumGem.all().length];
        setMaxStackSize(1);
        setHasSubtypes(true);
        setHasGemSubtypes(true);
        setUnlocalizedName(Names.CHAOS_GEM);
        setMaxDamage(0);
        setCreativeTab(CreativeTabs.tabTools);
    }
    
    @Override
    @SuppressWarnings({ "rawtypes", "unchecked" })
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean par4) {
        
        // TODO
    }
    
    @Override
    public boolean onDroppedByPlayer(ItemStack item, EntityPlayer player) {
        
        // TODO Set disabled NBT (will that work here?)
        return true;
    }
    
    @Override
    public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player) {
        
        // TODO
        return stack;
    }

    @Override
    public void addRecipes() {

        for (int i = 0; i < EnumGem.all().length; ++i) {
            RecipeHelper.addSurround(new ItemStack(this, 1, i), new ItemStack(SRegistry.getItem(Names.GEM_ITEM), 1, i),
                    CraftingMaterial.getStack(Names.CHAOS_ESSENCE_PLUS));
        }
    }
}
