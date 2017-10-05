package net.silentchaos512.gems.entity;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

// Something like a disk that can cut vegetation?
public class EntityChaosProjectileCutter extends EntityChaosProjectile {

  public EntityChaosProjectileCutter(World worldIn) {

    super(worldIn);
    this.gravity = false;
    setSize(4.0f, 1.0f);
  }

  public EntityChaosProjectileCutter(EntityLivingBase shooter, ItemStack castingStack,
      float damage) {

    super(shooter, castingStack, damage);
    this.gravity = false;
    // TODO Auto-generated constructor stub
  }

}
