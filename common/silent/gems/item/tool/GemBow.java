package silent.gems.item.tool;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;
import silent.gems.SilentGems;
import silent.gems.core.registry.IAddRecipe;
import silent.gems.core.registry.IHasVariants;
import silent.gems.core.registry.SRegistry;
import silent.gems.core.util.LocalizationHelper;
import silent.gems.item.CraftingMaterial;
import silent.gems.item.ModItems;
import silent.gems.lib.Names;

public class GemBow extends ItemBow implements IAddRecipe, IHasVariants {

  private final int gemId;
  private final double arrowDamage;

  public GemBow(ToolMaterial toolMaterial, int gemId) {

    super();
    this.gemId = gemId;
    this.arrowDamage = (double) (toolMaterial.getDamageVsEntity()) / 2.0 + 0.5;
    this.setMaxDamage(toolMaterial.getMaxUses() * 2);
    this.setCreativeTab(SilentGems.tabSilentGems);
  }

  @Override
  public String[] getVariantNames() {

    return new String[] { getFullName() };
  }

  @Override
  public String getName() {

    return "Bow" + gemId;
  }

  @Override
  public String getFullName() {

    return SilentGems.MOD_ID + ":" + getName() + "_Standby";
  }

  @Override
  public void addRecipes() {

    ItemStack ornateStick = CraftingMaterial.getStack(Names.ORNATE_STICK);
    ItemStack gildedString = CraftingMaterial.getStack(Names.GILDED_STRING);
    ItemStack gem = new ItemStack(ModItems.gem, 1, gemId);

    GameRegistry.addShapedRecipe(new ItemStack(this), "ogs", "g s", "ogs", 'o', ornateStick, 'g',
        gem, 's', gildedString);
  }

  @Override
  public void addOreDict() {

  }

  @Override
  public boolean getIsRepairable(ItemStack stack1, ItemStack stack2) {

    ItemStack material = new ItemStack(SRegistry.getItem(Names.GEM_ITEM), 1, gemId);
    if (material.getItem() == stack2.getItem()
        && material.getItemDamage() == stack2.getItemDamage()) {
      return true;
    } else {
      return super.getIsRepairable(stack1, stack2);
    }
  }

  @Override
  public String getUnlocalizedName(ItemStack stack) {

    return LocalizationHelper.TOOL_PREFIX + "Bow" + gemId;
  }

  public int getGemId() {

    return gemId;
  }
  
  @Override
  public int getItemEnchantability() {
    
    return 10;
  }
  
  @Override
  public void onPlayerStoppedUsing(ItemStack stack, World worldIn, EntityPlayer playerIn, int timeLeft)
  {
      int j = this.getMaxItemUseDuration(stack) - timeLeft;
      net.minecraftforge.event.entity.player.ArrowLooseEvent event = new net.minecraftforge.event.entity.player.ArrowLooseEvent(playerIn, stack, j);
      if (net.minecraftforge.common.MinecraftForge.EVENT_BUS.post(event)) return;
      j = event.charge;

      boolean flag = playerIn.capabilities.isCreativeMode || EnchantmentHelper.getEnchantmentLevel(Enchantment.infinity.effectId, stack) > 0;

      if (flag || playerIn.inventory.hasItem(Items.arrow))
      {
          float f = (float)j / 20.0F;
          f = (f * f + f * 2.0F) / 3.0F;

          if ((double)f < 0.1D)
          {
              return;
          }

          if (f > 1.0F)
          {
              f = 1.0F;
          }

          EntityArrow entityarrow = new EntityArrow(worldIn, playerIn, f * 2.0F);
          entityarrow.setDamage(this.arrowDamage);

          if (f == 1.0F)
          {
              entityarrow.setIsCritical(true);
          }

          int k = EnchantmentHelper.getEnchantmentLevel(Enchantment.power.effectId, stack);

          if (k > 0)
          {
              entityarrow.setDamage(entityarrow.getDamage() + (double)k * 0.5D + 0.5D);
          }

          int l = EnchantmentHelper.getEnchantmentLevel(Enchantment.punch.effectId, stack);

          if (l > 0)
          {
              entityarrow.setKnockbackStrength(l);
          }

          if (EnchantmentHelper.getEnchantmentLevel(Enchantment.flame.effectId, stack) > 0)
          {
              entityarrow.setFire(100);
          }

          stack.damageItem(1, playerIn);
          worldIn.playSoundAtEntity(playerIn, "random.bow", 1.0F, 1.0F / (itemRand.nextFloat() * 0.4F + 1.2F) + f * 0.5F);

          if (flag)
          {
              entityarrow.canBePickedUp = 2;
          }
          else
          {
              playerIn.inventory.consumeInventoryItem(Items.arrow);
          }

          playerIn.triggerAchievement(StatList.objectUseStats[Item.getIdFromItem(this)]);

          if (!worldIn.isRemote)
          {
              worldIn.spawnEntityInWorld(entityarrow);
          }
      }
  }
}
