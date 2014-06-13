package silent.gems.block;

import net.minecraft.block.material.Material;
import net.minecraft.client.entity.EntityClientPlayerMP;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Icon;
import net.minecraft.world.World;
import silent.gems.core.registry.SRegistry;
import silent.gems.item.CraftingMaterial;
import silent.gems.lib.Names;
import silent.gems.lib.Strings;
import silent.gems.network.packet.PacketTest;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class MiscBlock extends BlockSG {

    public static final String[] names = { Names.CHAOS_ESSENCE_BLOCK };

    public MiscBlock(int id) {

        super(id, Material.iron);
        icons = new Icon[names.length];
        setHasSubtypes(true);
        setCreativeTab(CreativeTabs.tabMaterials);
        setUnlocalizedName(Names.MISC_BLOCKS);
    }

    public static ItemStack getStack(String name) {

        for (int i = 0; i < names.length; ++i) {
            if (names[i].equals(name)) {
                return new ItemStack(SRegistry.getBlock(Names.MISC_BLOCKS), 1, i);
            }
        }

        return null;
    }

    public static ItemStack getStack(String name, int count) {

        for (int i = 0; i < names.length; ++i) {
            if (names[i].equals(name)) {
                return new ItemStack(SRegistry.getBlock(Names.MISC_BLOCKS), count, i);
            }
        }

        return null;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void registerIcons(IconRegister iconRegister) {

        for (int i = 0; i < names.length; ++i) {
            icons[i] = iconRegister.registerIcon(Strings.RESOURCE_PREFIX + names[i]);
        }
    }

    @Override
    public void addRecipes() {

        GameRegistry.addShapedRecipe(getStack(Names.CHAOS_ESSENCE_BLOCK), "ccc", "ccc", "ccc", 'c',
                CraftingMaterial.getStack(Names.CHAOS_ESSENCE));
        GameRegistry.addShapelessRecipe(CraftingMaterial.getStack(Names.CHAOS_ESSENCE, 9), getStack(Names.CHAOS_ESSENCE_BLOCK));
    }

//    @Override
//    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int par6, float hitX, float hitY, float hitZ) {
//
//        PacketTest packet = new PacketTest("You clicked my block. Good job.");
//
//        Side side = FMLCommonHandler.instance().getEffectiveSide();
//        if (side == Side.SERVER) {
//            EntityPlayerMP playerMP = (EntityPlayerMP) player;
//        }
//        else if (side == Side.CLIENT) {
//            EntityClientPlayerMP playerMP = (EntityClientPlayerMP) player;
//            playerMP.sendQueue.addToSendQueue(packet.packetType.populatePacket(packet));
//        }
//
//        return false;
//    }
}
