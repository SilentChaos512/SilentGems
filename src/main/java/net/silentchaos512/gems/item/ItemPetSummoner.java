package net.silentchaos512.gems.item;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.passive.EntityOcelot;
import net.minecraft.entity.passive.EntityTameable;
import net.minecraft.entity.passive.EntityWolf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.oredict.OreDictionary;
import net.silentchaos512.gems.SilentGems;
import net.silentchaos512.gems.init.ModItems;
import net.silentchaos512.gems.lib.Names;
import net.silentchaos512.lib.registry.IAddRecipes;
import net.silentchaos512.lib.registry.ICustomModel;
import net.silentchaos512.lib.registry.RecipeMaker;

import javax.annotation.Nullable;
import java.util.List;

public class ItemPetSummoner extends Item implements IAddRecipes, ICustomModel {
    private static final String[] NAMES = { Names.SUMMON_KITTY, Names.SUMMON_PUPPY };

    public ItemPetSummoner() {
        setHasSubtypes(true);
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        tooltip.add(TextFormatting.ITALIC + SilentGems.i18n.translate(this.getTranslationKey(stack) + ".desc"));
    }

    @Override
    public void addRecipes(RecipeMaker recipes) {
        ItemStack anyGem = new ItemStack(ModItems.gem, 1, OreDictionary.WILDCARD_VALUE);

        recipes.addShapelessOre("summon_kitty", new ItemStack(this, 1, 0), anyGem,
                new ItemStack(Items.FISH, 1, OreDictionary.WILDCARD_VALUE),
                ModItems.craftingMaterial.yarnBall);

        recipes.addShapelessOre("summon_puppy_0", new ItemStack(this, 1, 1), anyGem, Items.BEEF,
                ModItems.craftingMaterial.rawhideBone);
        recipes.addShapelessOre("summon_puppy_1", new ItemStack(this, 1, 1), anyGem, Items.PORKCHOP,
                ModItems.craftingMaterial.rawhideBone);
        recipes.addShapelessOre("summon_puppy_2", new ItemStack(this, 1, 1), anyGem, Items.CHICKEN,
                ModItems.craftingMaterial.rawhideBone);
    }

    @Override
    public EnumActionResult onItemUse(EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        ItemStack stack = player.getHeldItem(hand);

        if (world.isRemote) {
            return EnumActionResult.SUCCESS;
        } else if (!player.canPlayerEdit(pos.offset(facing), facing, stack)) {
            return EnumActionResult.PASS;
        } else {
            IBlockState state = world.getBlockState(pos);

            pos = pos.offset(facing);

            EntityTameable pet;
            if (stack.getItemDamage() == 0) {
                pet = new EntityOcelot(world);
            } else if (stack.getItemDamage() == 1) {
                pet = new EntityWolf(world);
            } else {
                return EnumActionResult.FAIL;
            }

            // Set position
            pet.setLocationAndAngles(pos.getX(), pos.getY(), pos.getZ(),
                    MathHelper.wrapDegrees(world.rand.nextFloat() * 360.0F), 0.0F);
            pet.rotationYawHead = pet.rotationYaw;
            pet.renderYawOffset = pet.rotationYaw;
            world.spawnEntity(pet);
            pet.playLivingSound();

            if (stack.hasDisplayName()) {
                pet.setCustomNameTag(stack.getDisplayName());
            }

            if (!player.capabilities.isCreativeMode) {
                stack.shrink(1);
            }

            // Make it tame and set master.
            pet.setTamed(true);
            pet.setOwnerId(player.getUniqueID());
            world.setEntityState(pet, (byte) 7);

            // Set cat skin.
            if (pet instanceof EntityOcelot) {
                EntityOcelot kitty = (EntityOcelot) pet;
                kitty.setTameSkin(1 + world.rand.nextInt(3));
            }

            // Heal dogs.
            if (pet instanceof EntityWolf) {
                pet.heal(pet.getMaxHealth() - pet.getHealth());
            }

            return EnumActionResult.SUCCESS;
        }
    }

    @Override
    public boolean hasEffect(ItemStack stack) {
        return true;
    }

    @Override
    public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> items) {
        if (!isInCreativeTab(tab)) return;
        items.add(new ItemStack(this, 1, 0));
        items.add(new ItemStack(this, 1, 1));
    }

    @Override
    public String getTranslationKey(ItemStack stack) {
        return "item." + SilentGems.MODID + "." + (NAMES[stack.getItemDamage() % 2]);
    }

    @Override
    public void registerModels() {
        SilentGems.registry.setModel(this, 0, NAMES[0]);
        SilentGems.registry.setModel(this, 1, NAMES[1]);
    }
}
