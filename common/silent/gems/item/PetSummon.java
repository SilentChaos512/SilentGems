package silent.gems.item;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.BlockFence;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IEntityLivingData;
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
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.ShapelessOreRecipe;
import silent.gems.core.util.LocalizationHelper;
import silent.gems.lib.Names;
import silent.gems.lib.Reference;
import silent.gems.lib.Strings;

public class PetSummon extends ItemSG {

  public final static String[] NAMES = { Names.SUMMON_KITTY, Names.SUMMON_PUPPY };

  public PetSummon() {

    super(2);
    isGlowing = true;
    setMaxStackSize(64);
    setHasSubtypes(true);
    setMaxDamage(0);
    setUnlocalizedName(Names.SUMMON_PET);
  }

  @Override
  public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean par4) {

    list.add(EnumChatFormatting.ITALIC
        + LocalizationHelper.getItemDescription(NAMES[stack.getItemDamage()], 0));
  }

  @Override
  public void addRecipes() {

    GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(this, 1, 0), new Object[] {
        Strings.ORE_DICT_GEM_BASIC, Items.fish, CraftingMaterial.getStack(Names.YARN_BALL) }));

    GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(this, 1, 1), new Object[] {
        Strings.ORE_DICT_GEM_BASIC, Items.beef, CraftingMaterial.getStack(Names.RAWHIDE_BONE) }));
    GameRegistry
        .addRecipe(new ShapelessOreRecipe(new ItemStack(this, 1, 1), new Object[] {
            Strings.ORE_DICT_GEM_BASIC, Items.porkchop,
            CraftingMaterial.getStack(Names.RAWHIDE_BONE) }));
    GameRegistry
        .addRecipe(new ShapelessOreRecipe(new ItemStack(this, 1, 1), new Object[] {
            Strings.ORE_DICT_GEM_BASIC, Items.chicken,
            CraftingMaterial.getStack(Names.RAWHIDE_BONE) }));
  }

  @Override
  public String[] getVariantNames() {

    String[] result = new String[NAMES.length];
    for (int i = 0; i < result.length; ++i) {
      result[i] = Reference.MOD_ID + ":" + NAMES[i];
    }
    return result;
  }

  @Override
  public String getUnlocalizedName(ItemStack stack) {

    return getUnlocalizedName(NAMES[stack.getItemDamage()]);
  }

  @Override
  public boolean onItemUse(ItemStack stack, EntityPlayer player, World world, BlockPos pos,
      EnumFacing side, float hitX, float hitY, float hitZ) {

    if (world.isRemote) {
      return true;
    } else if (!player.canPlayerEdit(pos.offset(side), side, stack)) {
      return false;
    } else {
      IBlockState iblockstate = world.getBlockState(pos);

      if (iblockstate.getBlock() == Blocks.mob_spawner) {
        TileEntity tileentity = world.getTileEntity(pos);

        if (tileentity instanceof TileEntityMobSpawner) {
          MobSpawnerBaseLogic mobspawnerbaselogic = ((TileEntityMobSpawner) tileentity)
              .getSpawnerBaseLogic();
          mobspawnerbaselogic.setEntityName(EntityList.getStringFromID(stack.getMetadata()));
          tileentity.markDirty();
          world.markBlockForUpdate(pos);

          if (!player.capabilities.isCreativeMode) {
            --stack.stackSize;
          }

          return true;
        }
      }

      pos = pos.offset(side);
      double d0 = 0.0D;

      if (side == EnumFacing.UP && iblockstate instanceof BlockFence) {
        d0 = 0.5D;
      }

      // Entity entity = spawnCreature(world, stack.getMetadata(), (double) pos.getX() + 0.5D,
      // (double) pos.getY() + d0, (double) pos.getZ() + 0.5D);

      EntityTameable pet;
      if (stack.getItemDamage() == 0) {
        pet = new EntityOcelot(world);
      } else if (stack.getItemDamage() == 1) {
        pet = new EntityWolf(world);
      } else {
        return true;
      }

      // Set position
      pet.setLocationAndAngles(pos.getX(), pos.getY(), pos.getZ(),
          MathHelper.wrapAngleTo180_float(world.rand.nextFloat() * 360.0F), 0.0F);
      pet.rotationYawHead = pet.rotationYaw;
      pet.renderYawOffset = pet.rotationYaw;
      // pet.func_180482_a(world.getDifficultyForLocation(new BlockPos(pet)), (IEntityLivingData)null);
      world.spawnEntityInWorld(pet);
      pet.playLivingSound();

      if (pet != null) {
        if (pet instanceof EntityLivingBase && stack.hasDisplayName()) {
          pet.setCustomNameTag(stack.getDisplayName());
        }

        if (!player.capabilities.isCreativeMode) {
          --stack.stackSize;
        }

        // Make it tame and set master.
        pet.setTamed(true);
        pet.setOwnerId(player.getUniqueID().toString());
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

      return true;
    }
  }

  // @Override
  // public void registerIcons(IIconRegister iconRegister) {
  //
  // for (int i = 0; i < NAMES.length; ++i) {
  // icons[i] = iconRegister.registerIcon(Strings.RESOURCE_PREFIX + NAMES[i]);
  // }
  // }
}
