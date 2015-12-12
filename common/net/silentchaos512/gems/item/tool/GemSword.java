package net.silentchaos512.gems.item.tool;

import java.util.List;

import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.silentchaos512.gems.SilentGems;
import net.silentchaos512.gems.client.renderers.tool.ToolRenderHelper;
import net.silentchaos512.gems.core.proxy.ClientProxy;
import net.silentchaos512.gems.core.util.LocalizationHelper;
import net.silentchaos512.gems.core.util.LogHelper;
import net.silentchaos512.gems.core.util.ToolHelper;
import net.silentchaos512.gems.entity.projectile.EntityProjectileChaosOrb;
import net.silentchaos512.gems.item.CraftingMaterial;
import net.silentchaos512.gems.lib.Names;
import net.silentchaos512.gems.material.ModMaterials;

public class GemSword extends ItemSword {

  public final int gemId;
  public final boolean supercharged;
  private final ToolMaterial toolMaterial;

  public GemSword(ToolMaterial toolMaterial, int gemId, boolean supercharged) {

    super(toolMaterial);
    this.gemId = gemId;
    this.supercharged = supercharged;
    this.toolMaterial = toolMaterial;
    this.setMaxDamage(toolMaterial.getMaxUses());
    addRecipe(new ItemStack(this), gemId, supercharged);
    this.setCreativeTab(SilentGems.tabSilentGems);
  }

  @Override
  public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean advanced) {

    ToolHelper.addInformation(stack, player, list, advanced);
  }

  public static void addRecipe(ItemStack tool, int gemId, boolean supercharged) {

    ItemStack material = ToolHelper.getCraftingMaterial(gemId, supercharged);

    if (supercharged) {
      GameRegistry.addRecipe(new ShapedOreRecipe(tool, true, "g", "g", "s", 'g', material, 's',
          CraftingMaterial.getStack(Names.ORNATE_STICK)));
    } else {
      GameRegistry.addRecipe(
          new ShapedOreRecipe(tool, true, "g", "g", "s", 'g', material, 's', "stickWood"));
    }
  }

  public int getGemId() {

    return gemId;
  }

  @Override
  public IIcon getIcon(ItemStack stack, int pass) {

    return ToolRenderHelper.instance.getIcon(stack, pass, gemId, supercharged);
  }

  @Override
  public int getMaxDamage(ItemStack stack) {

    return super.getMaxDamage(stack) + ToolHelper.getDurabilityBoost(stack);
  }

  @Override
  public boolean getIsRepairable(ItemStack stack1, ItemStack stack2) {

    return ToolHelper.getIsRepairable(stack1, stack2);
  }

  @Override
  public int getRenderPasses(int meta) {

    return ToolRenderHelper.RENDER_PASS_COUNT;
  }

  @Override
  public String getUnlocalizedName(ItemStack stack) {

    return LocalizationHelper.TOOL_PREFIX + "Sword" + gemId + (supercharged ? "Plus" : "");
  }

  @Override
  public boolean hasEffect(ItemStack stack, int pass) {

    return ToolRenderHelper.instance.hasEffect(stack, pass);
  }

  @Override
  public boolean requiresMultipleRenderPasses() {

    return true;
  }

  @Override
  public void registerIcons(IIconRegister reg) {

    if (gemId >= 0 && gemId < ToolRenderHelper.HEAD_TYPE_COUNT) {
      itemIcon = ToolRenderHelper.instance.swordIcons.headM[gemId];
    }
  }

  @Override
  public boolean onBlockStartBreak(ItemStack stack, int x, int y, int z, EntityPlayer player) {

    boolean canceled = super.onBlockStartBreak(stack, x, y, z, player);
    if (!canceled) {
      ToolHelper.onBlockStartBreak(stack, x, y, z, player);
    }
    return canceled;
  }

  @Override
  public boolean hitEntity(ItemStack stack, EntityLivingBase entity1, EntityLivingBase entity2) {

    ToolHelper.hitEntity(stack);
    return super.hitEntity(stack, entity1, entity2);
  }

  @Override
  public boolean onEntitySwing(EntityLivingBase entityLiving, ItemStack stack) {

    World world = entityLiving.worldObj;
    if (world.isRemote || gemId != ModMaterials.CHAOS_GEM_ID) {
      return false;
    }

    if (stack.getTagCompound().getBoolean("ShotCharged")) {
      stack.getTagCompound().setBoolean("ShotCharged", false);
      float damage = toolMaterial.getDamageVsEntity();
      int color = ToolHelper.getToolHeadRight(stack);
      EntityProjectileChaosOrb shot = new EntityProjectileChaosOrb(world, entityLiving, damage,
          color, false);
      world.spawnEntityInWorld(shot);
    }

    return super.onEntitySwing(entityLiving, stack);
  }

  @Override
  public void onUsingTick(ItemStack stack, EntityPlayer player, int count) {

    if (gemId != ModMaterials.CHAOS_GEM_ID) {
      return;
    }

    // TODO Charge projectile
    int time = getMaxItemUseDuration(stack) - count;
    Vec3 vec = player.getLookVec();
    vec.rotateAroundY(-0.5f);

    if (time < 10 && !stack.getTagCompound().getBoolean("ShotCharged")) {
      double posX = player.posX + vec.xCoord;
      double posY = player.posY + vec.yCoord + 0.4;
      double posZ = player.posZ + vec.zCoord;
      double motionX = player.worldObj.rand.nextGaussian() * 0.25;
      double motionY = player.worldObj.rand.nextGaussian() * 0.25;
      double motionZ = player.worldObj.rand.nextGaussian() * 0.25;

      int gemId = ToolHelper.getToolHeadRight(stack);
      int color = EntityProjectileChaosOrb.COLORS[MathHelper.clamp_int(gemId, 0,
          EntityProjectileChaosOrb.COLORS.length)];

      player.worldObj.spawnParticle("fireworksSpark", posX - 10 * motionX, posY - 10 * motionY,
          posZ - 10 * motionZ, motionX, motionY, motionZ);
      // SilentGems.proxy.spawnParticles(ClientProxy.FX_CHAOS_CHARGE, color, player.worldObj,
      // player.posX, player.posY, player.posZ, motionX, motionY, motionZ);
    } else if (time > 20 && !stack.getTagCompound().getBoolean("ShotCharged")) {
      stack.getTagCompound().setBoolean("ShotCharged", true);
      double posX = player.posX + vec.xCoord;
      double posY = player.posY + vec.yCoord + 0.4;
      double posZ = player.posZ + vec.zCoord;

      int gemId = ToolHelper.getToolHeadRight(stack);
      int color = EntityProjectileChaosOrb.COLORS[MathHelper.clamp_int(gemId, 0,
          EntityProjectileChaosOrb.COLORS.length)];

      for (int i = 0; i < 16; ++i) {
        double motionX = player.worldObj.rand.nextGaussian() * 0.15;
        double motionY = player.worldObj.rand.nextGaussian() * 0.15;
        double motionZ = player.worldObj.rand.nextGaussian() * 0.15;
        player.worldObj.spawnParticle("fireworksSpark", posX, posY, posZ, motionX, motionY,
            motionZ);
      }
    }
  }
}
