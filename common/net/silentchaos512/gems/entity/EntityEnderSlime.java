package net.silentchaos512.gems.entity;

import javax.annotation.Nullable;

import net.minecraft.entity.monster.EntityMagmaCube;
import net.minecraft.entity.monster.EntitySlime;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraft.world.storage.loot.LootTableList;
import net.silentchaos512.gems.SilentGems;
import net.silentchaos512.gems.init.ModItems;
import net.silentchaos512.lib.util.StackHelper;

public class EntityEnderSlime extends EntityMagmaCube {

  public EntityEnderSlime(World worldIn) {

    super(worldIn);
  }

  @Override
  protected EnumParticleTypes getParticleType() {

    return EnumParticleTypes.DRAGON_BREATH;
  }

  protected EntitySlime createInstance() {

    return new EntityEnderSlime(this.world);
  }

  @Override
  protected @Nullable ResourceLocation getLootTable() {

    // Use dropFewItems, not sure how the loot tables work :/
    return null;
  }

  @Override
  protected void dropFewItems(boolean hitByPlayer, int looting) {

    if (isSmallSlime())
      return;

    int dropCount = rand.nextInt(4) - 2;
    if (looting > 0) {
      dropCount += rand.nextInt(looting / 2 + 1);
    }
    for (int i = 0; i < dropCount; ++i) {
      entityDropItem(StackHelper.safeCopy(ModItems.craftingMaterial.enderSlimeBall), height / 2);
    }
  }
}
