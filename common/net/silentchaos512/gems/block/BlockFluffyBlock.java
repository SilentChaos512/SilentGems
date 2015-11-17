package net.silentchaos512.gems.block;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.world.World;
import net.silentchaos512.gems.lib.Names;

public class BlockFluffyBlock extends BlockSG {

  public BlockFluffyBlock() {

    super(Material.cloth);

    setHardness(0.8f);
    setResistance(3.0f);
    setStepSound(Block.soundTypeCloth);

    setUnlocalizedName(Names.FLUFFY_BLOCK);
  }

  @Override
  public void onFallenUpon(World world, int x, int y, int z, Entity entity, float f) {

    entity.fallDistance = 0f;
  }
}
