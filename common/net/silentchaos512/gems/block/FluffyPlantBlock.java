package net.silentchaos512.gems.block;

import java.util.List;

import com.google.common.collect.Lists;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.BlockCrops;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import net.silentchaos512.gems.core.registry.SRegistry;
import net.silentchaos512.gems.item.tool.GemSickle;
import net.silentchaos512.gems.lib.Names;
import net.silentchaos512.gems.lib.Strings;

public class FluffyPlantBlock extends BlockCrops {

  private IIcon[] icons;

  public FluffyPlantBlock() {

    this.setBlockName(Names.FLUFFY_PLANT);
    this.setBlockTextureName(Strings.RESOURCE_PREFIX + Names.FLUFFY_PLANT);
  }

  @Override
  public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player,
      int side, float hitX, float hitY, float hitZ) {

    ItemStack heldItem = player.getHeldItem();
    if (heldItem != null && heldItem.getItem() instanceof GemSickle) {
      return false;
    }

    // Right-click to harvest
    List<ItemStack> drops = Lists.newArrayList();
    int age = world.getBlockMetadata(x, y, z);

    // Get drops if mature
    if (age >= 7) {
      for (int i = 0; i < 3; ++i) {
        if (i == 0 || world.rand.nextInt(15) <= age) {
          drops.add(new ItemStack(func_149866_i(), 1, damageDropped(age)));
        }
      }

      // Reset to newly planted state
      world.setBlockMetadataWithNotify(x, y, z, 0, 2);
    }

    // Spawn items in world
    for (ItemStack stack : drops) {
      dropBlockAsItem(world, x, y, z, stack);
    }

    return !drops.isEmpty();
  }

  @Override
  protected Item func_149866_i() {

    return SRegistry.getItem(Names.FLUFFY_SEED);
  }

  @Override
  protected Item func_149865_P() {

    return SRegistry.getItem(Names.FLUFFY_SEED);
  }

  @SideOnly(Side.CLIENT)
  @Override
  public IIcon getIcon(int side, int meta) {

    if (meta < 7) {
      if (meta == 6) {
        meta = 5;
      }

      return this.icons[meta >> 1];
    } else {
      return this.icons[3];
    }
  }

  @Override
  public int getRenderType() {

    return 1;
  }

  @Override
  public String getUnlocalizedName() {

    return "tile." + Names.FLUFFY_PLANT;
  }

  @SideOnly(Side.CLIENT)
  @Override
  public void registerBlockIcons(IIconRegister reg) {

    this.icons = new IIcon[4];

    for (int i = 0; i < this.icons.length; ++i) {
      this.icons[i] = reg.registerIcon(this.getTextureName() + i);
    }
  }
}
