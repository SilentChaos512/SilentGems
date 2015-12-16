package net.silentchaos512.gems.item.tool;

import java.util.List;

import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
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
import net.silentchaos512.gems.core.util.ToolHelper;
import net.silentchaos512.gems.entity.projectile.EntityProjectileChaosOrb;
import net.silentchaos512.gems.item.CraftingMaterial;
import net.silentchaos512.gems.lib.Names;
import net.silentchaos512.gems.material.ModMaterials;

public class GemSword extends ItemSword {

  public static final String NBT_SHOT_CHARGED = "ShotCharged";

  public static final int CHARGE_DELAY = 20;

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

    if (getShotCharged(stack)) {
      setShotCharged(stack, false);
      int sharpness = EnchantmentHelper.getEnchantmentLevel(Enchantment.sharpness.effectId, stack);
      float damage = toolMaterial.getDamageVsEntity() * 0.75f + sharpness;
      int color = ToolHelper.getToolHeadRight(stack);
      EntityProjectileChaosOrb shot = new EntityProjectileChaosOrb(world, entityLiving, damage,
          color, true);
      world.spawnEntityInWorld(shot);
    }

    return super.onEntitySwing(entityLiving, stack);
  }

  @Override
  public void onPlayerStoppedUsing(ItemStack stack, World world, EntityPlayer player,
      int itemInUseCount) {

    if (getMaxItemUseDuration(stack) - itemInUseCount > CHARGE_DELAY) {
      setShotCharged(stack, true);
    }
  }

  @Override
  public void onUsingTick(ItemStack stack, EntityPlayer player, int count) {

    if (gemId != ModMaterials.CHAOS_GEM_ID || !player.worldObj.isRemote) {
      return;
    }

    int time = getMaxItemUseDuration(stack) - count;
    Vec3 vec = player.getLookVec();
    // vec.rotateAroundY(-0.5f);
    // vec.rotateAroundZ(0.5f);

    if (time < CHARGE_DELAY / 2 && !getShotCharged(stack)) {
      // Charging particles
      if (SilentGems.proxy.getParticleSettings() == 0) {
        for (int i = 0; i < 4; ++i) {
          double posX = player.posX + vec.xCoord;
          double posY = player.posY + vec.yCoord - 0.1;
          double posZ = player.posZ + vec.zCoord;
          double motionX = player.worldObj.rand.nextGaussian() * 0.05 + player.motionX;
          double motionY = player.worldObj.rand.nextGaussian() * 0.05 + player.motionY;
          double motionZ = player.worldObj.rand.nextGaussian() * 0.05 + player.motionZ;

          int colorIndex = ToolHelper.getToolHeadRight(stack);
          colorIndex = colorIndex < 0 ? ModMaterials.CHAOS_GEM_ID : colorIndex;
          int color = EntityProjectileChaosOrb.COLORS[MathHelper.clamp_int(colorIndex, 0,
              EntityProjectileChaosOrb.COLORS.length)];

          // player.worldObj.spawnParticle("fireworksSpark", posX - 10 * motionX, posY - 10 * motionY,
          // posZ - 10 * motionZ, motionX, motionY, motionZ);
          SilentGems.proxy.spawnParticles(ClientProxy.FX_CHAOS_CHARGE, color, player.worldObj,
              posX - 10 * motionX, posY - 10 * motionY, posZ - 10 * motionZ, motionX, motionY,
              motionZ);
        }
      }
    } else if (time == CHARGE_DELAY) {
      // Play a sound
      player.playSound("random.fizz", 0.25f, 1.5f);

      // Full charge particles.
      if (SilentGems.proxy.getParticleSettings() < 2) {
        double posX = player.posX + vec.xCoord;
        double posY = player.posY + vec.yCoord - 0.1;
        double posZ = player.posZ + vec.zCoord;

        int colorIndex = ToolHelper.getToolHeadRight(stack);
        colorIndex = colorIndex < 0 ? ModMaterials.CHAOS_GEM_ID : colorIndex;
        int color = EntityProjectileChaosOrb.COLORS[MathHelper.clamp_int(colorIndex, 0,
            EntityProjectileChaosOrb.COLORS.length)];

        for (int i = 0; i < 16; ++i) {
          double motionX = player.worldObj.rand.nextGaussian() * 0.01;
          double motionY = player.worldObj.rand.nextGaussian() * 0.05;
          double motionZ = player.worldObj.rand.nextGaussian() * 0.01;
          SilentGems.proxy.spawnParticles(ClientProxy.FX_CHAOS_CHARGE, color, player.worldObj, posX,
              posY, posZ, motionX, motionY, motionZ);
        }
      }
    }
  }

  public boolean getShotCharged(ItemStack stack) {

    return stack.hasTagCompound() && stack.getTagCompound().getBoolean(NBT_SHOT_CHARGED);
  }

  private void setShotCharged(ItemStack stack, boolean value) {

    stack.getTagCompound().setBoolean(NBT_SHOT_CHARGED, value);
  }
}
