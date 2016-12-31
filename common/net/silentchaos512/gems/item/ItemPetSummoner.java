package net.silentchaos512.gems.item;

import net.minecraft.block.BlockFence;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.passive.EntityOcelot;
import net.minecraft.entity.passive.EntityTameable;
import net.minecraft.entity.passive.EntityWolf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.MobSpawnerBaseLogic;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityMobSpawner;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.ShapelessOreRecipe;
import net.silentchaos512.gems.SilentGems;
import net.silentchaos512.gems.lib.GemsCreativeTabs;
import net.silentchaos512.gems.lib.Names;
import net.silentchaos512.lib.item.ItemNamedSubtypes;

public class ItemPetSummoner extends ItemNamedSubtypes {

  public static final String[] NAMES = { Names.SUMMON_KITTY, Names.SUMMON_PUPPY };

  public ItemPetSummoner() {

    super(NAMES, SilentGems.MODID, Names.PET_SUMMONER);
  }

  @Override
  public void addRecipes() {

    ItemStack anyGem = new ItemStack(ModItems.gem, 1, OreDictionary.WILDCARD_VALUE);

    GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(this, 1, 0), anyGem,
        new ItemStack(Items.FISH, 1, OreDictionary.WILDCARD_VALUE),
        ModItems.craftingMaterial.yarnBall));

    GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(this, 1, 1), anyGem, Items.BEEF,
        ModItems.craftingMaterial.rawhideBone));
    GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(this, 1, 1), anyGem, Items.PORKCHOP,
        ModItems.craftingMaterial.rawhideBone));
    GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(this, 1, 1), anyGem, Items.CHICKEN,
        ModItems.craftingMaterial.rawhideBone));
  }

  @Override
  public EnumActionResult onItemUse(EntityPlayer player, World world, BlockPos pos,
      EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {

    ItemStack stack = player.getHeldItem(hand);

    if (world.isRemote) {
      return EnumActionResult.SUCCESS;
    } else if (!player.canPlayerEdit(pos.offset(facing), facing, stack)) {
      return EnumActionResult.PASS;
    } else {
      IBlockState state = world.getBlockState(pos);

      pos = pos.offset(facing);
      double d0 = 0.0D;

      if (facing == EnumFacing.UP && state instanceof BlockFence) {
        d0 = 0.5D;
      }

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

      if (pet != null) {
        if (pet instanceof EntityLivingBase && stack.hasDisplayName()) {
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
          pet.heal(12.0f);
        }
      }

      return EnumActionResult.SUCCESS;
    }
  }

  @Override
  public boolean hasEffect(ItemStack stack) {

    return true;
  }
}
