package net.silentchaos512.gems.lib;

import net.minecraft.item.Item.ToolMaterial;
import net.minecraft.item.ItemStack;
import net.silentchaos512.gems.core.registry.SRegistry;
import net.silentchaos512.gems.material.ModMaterials;

public enum EnumGem {

    RUBY(0, "Ruby"),
    GARNET(1, "Garnet"),
    TOPAZ(2, "Topaz"),
    HELIODOR(3, "Heliodor"),
    PERIDOT(4, "Peridot"),
    EMERALD(5, "Emerald"),
    AQUAMARINE(6, "Aquamarine"),
    SAPPHIRE(7, "Sapphire"),
    IOLITE(8, "Iolite"),
    AMETHYST(9, "Amethyst"),
    MORGANITE(10, "Morganite"),
    ONYX(11, "Onyx");

    public final byte id;
    public final byte rank;
    public final String name;
    
    private EnumGem(int id, String name) {
        
        this.id = (byte) id;
        this.rank = 1;
        this.name = name;
    }
    
    public static EnumGem[] all() {
        
        return values();
    }
    
    /**
     * Gets an ItemStack of one of the corresponding GemBlock.
     * @return
     */
    public ItemStack getBlock() {
        
        return new ItemStack(SRegistry.getBlock(Names.GEM_BLOCK), 1, id);
    }
    
    /**
     * Gets an ItemStack of one of the corresponding Gem.
     * @return
     */
    public ItemStack getItem() {
        
        return new ItemStack(SRegistry.getItem(Names.GEM_ITEM), 1, id);
    }
    
    /**
     * Gets an ItemStack of one of the corresponding GemOre.
     * @return
     */
    public ItemStack getOre() {
        
        return new ItemStack(SRegistry.getBlock(Names.GEM_ORE), 1, id);
    }
    
    /**
     * Gets an ItemStack of one of the corresponding GemShard, if there is one.
     * @return
     */
    public ItemStack getShard() {
        
        return new ItemStack(SRegistry.getItem(Names.GEM_SHARD), 1, id);
    }
    
    /**
     * Gets the tool material for this gem, if there is one.
     * @return
     */
    public ToolMaterial getToolMaterial(boolean supercharged) {
        
        if (supercharged) {
            if (id == RUBY.id)
                return ModMaterials.toolSupRuby;
            else if (id == GARNET.id)
                return ModMaterials.toolSupGarnet;
            else if (id == TOPAZ.id)
                return ModMaterials.toolSupTopaz;
            else if (id == HELIODOR.id)
                return ModMaterials.toolSupHeliodor;
            else if (id == PERIDOT.id)
                return ModMaterials.toolSupPeridot;
            else if (id == EMERALD.id)
                return ModMaterials.toolSupEmerald;
            else if (id == AQUAMARINE.id)
                return ModMaterials.toolSupAquamarine;
            else if (id == SAPPHIRE.id)
                return ModMaterials.toolSupSapphire;
            else if (id == IOLITE.id)
                return ModMaterials.toolSupIolite;
            else if (id == AMETHYST.id)
                return ModMaterials.toolSupAmethyst;
            else if (id == MORGANITE.id)
                return ModMaterials.toolSupMorganite;
            else if (id == ONYX.id)
                return ModMaterials.toolSupOnyx;
            else
                return null;
        }
        else {
            if (id == RUBY.id)
                return ModMaterials.toolRegRuby;
            else if (id == GARNET.id)
                return ModMaterials.toolRegGarnet;
            else if (id == TOPAZ.id)
                return ModMaterials.toolRegTopaz;
            else if (id == HELIODOR.id)
                return ModMaterials.toolRegHeliodor;
            else if (id == PERIDOT.id)
                return ModMaterials.toolRegPeridot;
            else if (id == EMERALD.id)
                return ModMaterials.toolRegEmerald;
            else if (id == AQUAMARINE.id)
                return ModMaterials.toolRegAquamarine;
            else if (id == SAPPHIRE.id)
                return ModMaterials.toolRegSapphire;
            else if (id == IOLITE.id)
                return ModMaterials.toolRegIolite;
            else if (id == AMETHYST.id)
                return ModMaterials.toolRegAmethyst;
            else if (id == MORGANITE.id)
                return ModMaterials.toolRegMorganite;
            else if (id == ONYX.id)
                return ModMaterials.toolRegOnyx;
            else
                return null;
        }
    }
}
