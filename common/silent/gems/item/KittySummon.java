package silent.gems.item;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.entity.passive.EntityOcelot;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Facing;
import net.minecraft.world.World;
import net.minecraftforge.oredict.ShapelessOreRecipe;
import silent.gems.lib.Names;
import silent.gems.lib.Strings;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class KittySummon extends ItemSG {

    public KittySummon(int id) {

        super(id);
        isGlowing = true;
        setUnlocalizedName(Names.KITTY_SUMMON);
    }

    @Override
    public boolean onItemUse(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY,
            float hitZ) {

        if (!world.isRemote) {

            if (!player.capabilities.isCreativeMode) {
                --stack.stackSize;
            }

            int id = world.getBlockId(x, y, z);
            x += Facing.offsetsXForSide[side];
            y += Facing.offsetsYForSide[side];
            z += Facing.offsetsZForSide[side];
            double d = 0.0;

            if (side == 1 && Block.blocksList[id] != null && Block.blocksList[id].getRenderType() == 11) {
                d = 0.5;
            }

            EntityOcelot kitty = new EntityOcelot(world);
            kitty.setPosition(x, y, z);
            world.spawnEntityInWorld(kitty);

            // Make the ocelot tame
            kitty.setTamed(true);
            kitty.setTameSkin(1 + world.rand.nextInt(3));
            kitty.setOwner(player.getCommandSenderName());
            world.setEntityState(kitty, (byte) 7);

            // Easter egg names
            if (player.getEntityName().equals(Strings.PLAYER_SILENT)) {
                kitty.setCustomNameTag(Strings.CAT_SILENT[world.rand.nextInt(Strings.CAT_SILENT.length)]);
            }
            else if (player.getEntityName().equals(Strings.PLAYER_CHAOTIC)) {
                kitty.setCustomNameTag(Strings.CAT_CHAOTIC[world.rand.nextInt(Strings.CAT_CHAOTIC.length)]);
            }
        }

        return true;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IconRegister iconRegister) {

        itemIcon = iconRegister.registerIcon(Strings.RESOURCE_PREFIX + Names.YARN_BALL);
    }

    @Override
    public void addRecipes() {

        GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(this), new Object[] { Strings.ORE_DICT_GEM_BASIC, Item.fishRaw,
                CraftingMaterial.getStack(Names.YARN_BALL) }));
    }
}
