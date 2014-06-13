package silent.gems.item;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.passive.EntityOcelot;
import net.minecraft.entity.passive.EntityTameable;
import net.minecraft.entity.passive.EntityWolf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.Facing;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import net.minecraftforge.oredict.ShapelessOreRecipe;
import silent.gems.core.util.LocalizationHelper;
import silent.gems.lib.Names;
import silent.gems.lib.Strings;
import cpw.mods.fml.common.registry.GameRegistry;

public class PetSummon extends ItemSG {

    public final static String[] NAMES = { Names.SUMMON_KITTY, Names.SUMMON_PUPPY };

    public PetSummon() {

        icons = new IIcon[NAMES.length];
        isGlowing = true;
        setMaxStackSize(64);
        setHasSubtypes(true);
        setMaxDamage(0);
        setCreativeTab(CreativeTabs.tabMisc);
        setUnlocalizedName(Names.SUMMON_PET);
    }
    
    @Override
    public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean par4) {
        
        list.add(EnumChatFormatting.ITALIC + LocalizationHelper.getItemDescription(NAMES[stack.getItemDamage()], 0));
    }

    @Override
    public void addRecipes() {

        GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(this, 1, 0), new Object[] { Strings.ORE_DICT_GEM_BASIC, Items.fish,
                CraftingMaterial.getStack(Names.YARN_BALL) }));
        
        GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(this, 1, 1), new Object[] { Strings.ORE_DICT_GEM_BASIC, Items.beef,
                CraftingMaterial.getStack(Names.RAWHIDE_BONE) }));
        GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(this, 1, 1), new Object[] { Strings.ORE_DICT_GEM_BASIC, Items.porkchop,
            CraftingMaterial.getStack(Names.RAWHIDE_BONE) }));
        GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(this, 1, 1), new Object[] { Strings.ORE_DICT_GEM_BASIC, Items.chicken,
            CraftingMaterial.getStack(Names.RAWHIDE_BONE) }));
    }

    @Override
    public String getUnlocalizedName(ItemStack stack) {

        return getUnlocalizedName(NAMES[stack.getItemDamage()]);
    }

    @Override
    public boolean onItemUse(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY,
            float hitZ) {

        if (!world.isRemote) {

            if (!player.capabilities.isCreativeMode) {
                --stack.stackSize;
            }

            Block block = world.getBlock(x, y, z);
            x += Facing.offsetsXForSide[side];
            y += Facing.offsetsYForSide[side];
            z += Facing.offsetsZForSide[side];
            double d = 0.0;

            if (side == 1 && block.getRenderType() == 11) {
                d = 0.5;
            }

            // Create pet entity.
            EntityTameable pet;
            if (stack.getItemDamage() == 0) {
                pet = new EntityOcelot(world);
            }
            else if (stack.getItemDamage() == 1) {
                pet = new EntityWolf(world);
            }
            else {
                return true;
            }

            // Set position, spawn in world.
            pet.setPosition(x + 0.5, y + 0.5, z + 0.5);
            world.spawnEntityInWorld(pet);

            // Make it tame and set master.
            pet.setTamed(true);
            pet.setOwner(player.getCommandSenderName());
            world.setEntityState(pet, (byte) 7);

            // Set cat skin.
            if (pet instanceof EntityOcelot) {
                EntityOcelot kitty = (EntityOcelot) pet;
                kitty.setTameSkin(1 + world.rand.nextInt(3));
            }
            
            // Heal dogs.
            if (pet instanceof EntityWolf) {
                pet.heal(12.0f);
            }

            // Custom name?
            if (stack.hasDisplayName()) {
                pet.setCustomNameTag(stack.getDisplayName());
            }
        }

        return true;
    }

    @Override
    public void registerIcons(IIconRegister iconRegister) {

        for (int i = 0; i < NAMES.length; ++i) {
            icons[i] = iconRegister.registerIcon(Strings.RESOURCE_PREFIX + NAMES[i]);
        }
    }
}
