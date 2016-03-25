package net.silentchaos512.gems.item.tool;

import java.util.List;

import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;

import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.silentchaos512.gems.SilentGems;
import net.silentchaos512.gems.api.ITool;
import net.silentchaos512.gems.api.lib.EnumMaterialTier;
import net.silentchaos512.gems.client.render.entity.EntityChaosProjectileHoming;
import net.silentchaos512.gems.entity.EntityChaosProjectile;
import net.silentchaos512.gems.entity.EntityChaosProjectileSweep;
import net.silentchaos512.gems.handler.PlayerDataHandler;
import net.silentchaos512.gems.handler.PlayerDataHandler.PlayerData;
import net.silentchaos512.gems.item.ModItems;
import net.silentchaos512.gems.item.ToolRenderHelper;
import net.silentchaos512.gems.lib.EnumGem;
import net.silentchaos512.gems.lib.Names;
import net.silentchaos512.gems.util.ToolHelper;
import net.silentchaos512.lib.registry.IRegistryObject;

public class ItemGemSword extends ItemSword implements IRegistryObject, ITool {

  protected List<ItemStack> subItems = null;

  public ItemGemSword() {

    super(ToolMaterial.DIAMOND);
  }

  public ItemStack constructTool(boolean supercharged, ItemStack material) {

    return constructTool(supercharged, material, material, material);
  }

  public ItemStack constructTool(boolean supercharged, ItemStack... materials) {

    ItemStack rod = supercharged ? ModItems.craftingMaterial.getStack(Names.ORNATE_STICK_GOLD)
        : new ItemStack(Items.stick);
    return ToolHelper.constructTool(this, rod, materials);
  }

  // ===============
  // ITool overrides
  // ===============

  @Override
  public ItemStack constructTool(ItemStack rod, ItemStack... materials) {

    if (materials.length == 2) {
      ItemStack temp = materials[0];
      materials[0] = materials[1];
      materials[1] = temp;
    }
    return ToolHelper.constructTool(this, rod, materials);
  }

  @Override
  public float getMeleeDamage(ItemStack tool) {

    return getBaseMeleeDamageModifier() + ToolHelper.getMeleeDamage(tool);
  }

  @Override
  public float getMagicDamage(ItemStack tool) {

    return 2.0f + ToolHelper.getMagicDamage(tool);
  }

  @Override
  public float getBaseMeleeDamageModifier() {

    return 4.0f;
  }

  @Override
  public float getBaseMeleeSpeedModifier() {

    return -2.4f;
  }

  // ==============
  // Item overrides
  // ==============

  @Override
  public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean advanced) {

    ToolRenderHelper.getInstance().addInformation(stack, player, list, advanced);
  }

  @Override
  public void getSubItems(Item item, CreativeTabs tab, List list) {

    if (subItems == null) {
      subItems = Lists.newArrayList();
      subItems.add(constructTool(false, new ItemStack(Items.flint)));
      for (EnumGem gem : EnumGem.values()) {
        subItems.add(constructTool(false, gem.getItem()));
      }
      for (EnumGem gem : EnumGem.values()) {
        subItems.add(constructTool(true, gem.getItemSuper()));
      }
    }

    list.addAll(subItems);
  }

  @Override
  public void onUpdate(ItemStack stack, World worldIn, Entity entityIn, int itemSlot,
      boolean isSelected) {

    // if (!worldIn.isRemote) {
    // ToolHelper.incrementCharge(stack, 1.0f);
    // }

    // GemsWorldSavedData data = GemsWorldSavedData.get(entityIn.worldObj);
    // SilentGems.instance.logHelper.debug(data.getChaosForPlayer((EntityPlayer) entityIn));
  }

  @Override
  public boolean onEntitySwing(EntityLivingBase entityLiving, ItemStack stack) {

    World world = entityLiving.worldObj;
    if (world.isRemote || !(entityLiving instanceof EntityPlayer)) {
      return false;
    }

    EntityPlayer player = (EntityPlayer) entityLiving;
    if (!player.isSneaking()) {
      return false;
    }

    int costToCast = getShotCost(entityLiving, stack);
    PlayerData data = PlayerDataHandler.get(player);
    if (data.chaos >= costToCast) {
      if (!player.capabilities.isCreativeMode) {
        data.drainChaos(costToCast);
        stack.damageItem(1, entityLiving);
      }

      ToolHelper.incrementStatShotsFired(stack, 1);

      if (!world.isRemote) {
        for (EntityChaosProjectile shot : getShots(entityLiving, stack)) {
          world.spawnEntityInWorld(shot);
        }
      }
    }

    // EntityPlayer player = (EntityPlayer) entityLiving;
    // GemsWorldSavedData data = GemsWorldSavedData.get(entityLiving.worldObj);
    // int chaos = data.getChaosForPlayer(player);
    //
    // if (chaos >= 1000) {
    // for (EntityChaosProjectile shot : getShots(entityLiving, stack)) {
    // world.spawnEntityInWorld(shot);
    // }
    // int extracted = data.extractChaosFromPlayer(player, 1000);
    // SilentGems.instance.logHelper.debug(chaos - extracted, extracted);
    // }

    return super.onEntitySwing(entityLiving, stack);
  }

  private List<EntityChaosProjectile> getShots(EntityLivingBase entityLiving, ItemStack stack) {

    List<EntityChaosProjectile> list = Lists.newArrayList();

    if (ToolHelper.getToolTier(stack) != EnumMaterialTier.SUPER) {
      return list;
    }

    float damage = getMagicDamage(stack);
    if (stack.getItem() == ModItems.scepter) {
      for (int i = 0; i < 5; ++i) {
        list.add(new EntityChaosProjectileHoming(entityLiving, stack, damage));
      }
    }
    if (stack.getItem() == ModItems.katana) {
      list.add(new EntityChaosProjectileSweep(entityLiving, stack, damage, 0.0f));
      list.add(new EntityChaosProjectileSweep(entityLiving, stack, damage, -0.075f));
      list.add(new EntityChaosProjectileSweep(entityLiving, stack, damage, 0.075f));
    }
    if (stack.getItem() == ModItems.sword) {
      list.add(new EntityChaosProjectile(entityLiving, stack, damage));
    }

    return list;
  }

  private int getShotCost(EntityLivingBase entityLiving, ItemStack stack) {

    // TODO

    if (stack != null) {
      if (stack.getItem() == ModItems.scepter)
        return 5000;
      if (stack.getItem() == ModItems.katana)
        return 2000;
      if (stack.getItem() == ModItems.sword)
        return 1000;
    }
    return 1500;
  }

  // @Override
  // public void onPlayerStoppedUsing(ItemStack stack, World world, EntityLivingBase entityLiving,
  // int timeLeft) {
  //
  // // if (getMaxItemUseDuration(stack) - itemInUseCount > CHARGE_DELAY) {
  // // setShotCharged(stack, true);
  // // }
  // }

  // @Override
  // public void onUsingTick(ItemStack stack, EntityLivingBase player, int count) {
  //
  // // if (gemId != ModMaterials.CHAOS_GEM_ID || !player.worldObj.isRemote || getShotCharged(stack)) {
  // // return;
  // // }
  // //
  // // int time = getMaxItemUseDuration(stack) - count;
  // // Vec3 vec = player.getLookVec();
  // // // vec.rotateAroundY(-0.5f);
  // // // vec.rotateAroundZ(0.5f);
  // //
  // // if (time < CHARGE_DELAY / 2) {
  // // // Charging particles
  // // if (SilentGems.proxy.getParticleSettings() == 0) {
  // // for (int i = 0; i < 4; ++i) {
  // // double posX = player.posX + vec.xCoord + player.motionX * 10;
  // // double posY = player.posY + vec.yCoord + 1.55;
  // // double posZ = player.posZ + vec.zCoord + player.motionZ * 10;
  // // double motionX = itemRand.nextGaussian() * 0.05;
  // // double motionY = itemRand.nextGaussian() * 0.05;
  // // double motionZ = itemRand.nextGaussian() * 0.05;
  // //
  // // int colorIndex = ToolHelper.getToolHeadRight(stack);
  // // colorIndex = colorIndex < 0 ? ModMaterials.CHAOS_GEM_ID : colorIndex;
  // // int color = EntityProjectileChaosOrb.COLORS[MathHelper.clamp_int(colorIndex, 0,
  // // EntityProjectileChaosOrb.COLORS.length)];
  // //
  // // SilentGems.proxy.spawnParticles(ClientProxy.FX_CHAOS_CHARGE, color, player.worldObj,
  // // posX - 10 * motionX, posY - 10 * motionY, posZ - 10 * motionZ, motionX, motionY,
  // // motionZ);
  // // }
  // // }
  // // } else if (time == CHARGE_DELAY) {
  // // // Play a sound
  // // player.playSound("random.fizz", 0.2f, (float) (1.25f + itemRand.nextGaussian() * 0.1f));
  // //
  // // // Full charge particles.
  // // if (SilentGems.proxy.getParticleSettings() < 2) {
  // // double posX = player.posX + vec.xCoord;
  // // double posY = player.posY + vec.yCoord + 1.55;
  // // double posZ = player.posZ + vec.zCoord;
  // //
  // // int colorIndex = ToolHelper.getToolHeadRight(stack);
  // // colorIndex = colorIndex < 0 ? ModMaterials.CHAOS_GEM_ID : colorIndex;
  // // int color = EntityProjectileChaosOrb.COLORS[MathHelper.clamp_int(colorIndex, 0,
  // // EntityProjectileChaosOrb.COLORS.length)];
  // //
  // // for (int i = 0; i < 16; ++i) {
  // // double motionX = itemRand.nextGaussian() * 0.01;
  // // double motionY = itemRand.nextGaussian() * 0.05;
  // // double motionZ = itemRand.nextGaussian() * 0.01;
  // // SilentGems.proxy.spawnParticles(ClientProxy.FX_CHAOS_CHARGE, color, player.worldObj, posX,
  // // posY, posZ, motionX, motionY, motionZ);
  // // }
  // // }
  // // }
  // }

  @Override
  public EnumActionResult onItemUse(ItemStack stack, EntityPlayer player, World world, BlockPos pos,
      EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {

    return super.onItemUse(stack, player, world, pos, hand, side, hitX, hitY, hitZ);
    // return ToolHelper.onItemUse(stack, player, world, pos, side, hitX, hitY, hitZ);
  }

  @Override
  public boolean onBlockStartBreak(ItemStack stack, BlockPos pos, EntityPlayer player) {

    boolean canceled = super.onBlockStartBreak(stack, pos, player);
    if (!canceled) {
      ToolHelper.onBlockStartBreak(stack, pos, player);
    }
    return canceled;
  }

  @Override
  public int getMaxDamage(ItemStack stack) {

    return ToolHelper.getMaxDamage(stack);
  }

  // @Override
  // public int getColorFromItemStack(ItemStack stack, int pass) {
  //
  // return ToolRenderHelper.getInstance().getColorFromItemStack(stack, pass);
  // }

  @Override
  public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack,
      boolean slotChanged) {

    return ToolRenderHelper.instance.shouldCauseReequipAnimation(oldStack, newStack, slotChanged);
  }

  @Override
  public int getItemEnchantability(ItemStack stack) {

    return ToolHelper.getItemEnchantability(stack);
  }

  // ==================
  // ItemSword overrides
  // ==================

  // @Override
  // public float getDigSpeed(ItemStack stack, IBlockState state) {
  //
  // return ToolHelper.getDigSpeed(stack, efficiencyOnProperMaterial, state,
  // extraEffectiveMaterials);
  // }
  //
  // @Override
  // public int getHarvestLevel(ItemStack stack, String toolClass) {
  //
  // if (super.getHarvestLevel(stack, toolClass) < 0) {
  // GemTest.instance.logHelper.derp();
  // return 0;
  // }
  // return ToolHelper.getHarvestLevel(stack);
  // }

  @Override
  public Multimap<String, AttributeModifier> getAttributeModifiers(EntityEquipmentSlot slot,
      ItemStack stack) {

    return ToolHelper.getAttributeModifiers(slot, stack);
  }

  @Override
  public boolean hitEntity(ItemStack stack, EntityLivingBase entity1, EntityLivingBase entity2) {

    ToolHelper.hitEntity(stack);
    return super.hitEntity(stack, entity1, entity2);
  }

  @Override
  public boolean getIsRepairable(ItemStack stack1, ItemStack stack2) {

    return ToolHelper.getIsRepairable(stack1, stack2);
  }

  // ===============
  // IRegistryObject
  // ===============

  @Override
  public void addRecipes() {

    String line1 = "g";
    String line2 = "g";
    String line3 = "s";
    ItemStack flint = new ItemStack(Items.flint);
    GameRegistry.addRecipe(new ShapedOreRecipe(constructTool(false, flint), line1, line2, line3,
        'g', flint, 's', "stickWood"));
    for (EnumGem gem : EnumGem.values()) {
      GameRegistry.addRecipe(new ShapedOreRecipe(constructTool(false, gem.getItem()), line1, line2,
          line3, 'g', gem.getItem(), 's', "stickWood"));
      GameRegistry.addRecipe(new ShapedOreRecipe(constructTool(true, gem.getItemSuper()), line1,
          line2, line3, 'g', gem.getItemSuper(), 's',
          ModItems.craftingMaterial.getStack(Names.ORNATE_STICK_GOLD)));
    }
  }

  @Override
  public void addOreDict() {

  }

  @Override
  public String getName() {

    return Names.SWORD;
  }

  @Override
  public String getFullName() {

    return getModId() + ":" + getName();
  }

  @Override
  public String getModId() {

    return SilentGems.MOD_ID;
  }

  @Override
  public List<ModelResourceLocation> getVariants() {

    return Lists.newArrayList(ToolRenderHelper.SMART_MODEL);
  }

  @Override
  public boolean registerModels() {

    return false;
  }
}
