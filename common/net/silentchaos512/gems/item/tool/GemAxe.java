package net.silentchaos512.gems.item.tool;

import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemAxe;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.silentchaos512.gems.SilentGems;
import net.silentchaos512.gems.api.IPlaceable;
import net.silentchaos512.gems.client.renderers.tool.ToolRenderHelper;
import net.silentchaos512.gems.configuration.Config;
import net.silentchaos512.gems.core.registry.SRegistry;
import net.silentchaos512.gems.core.util.LocalizationHelper;
import net.silentchaos512.gems.enchantment.EnchantmentAOE;
import net.silentchaos512.gems.enchantment.ModEnchantments;
import net.silentchaos512.gems.item.CraftingMaterial;
import net.silentchaos512.gems.item.ModItems;
import net.silentchaos512.gems.lib.Names;
import net.silentchaos512.gems.lib.Strings;
import net.silentchaos512.gems.material.ModMaterials;

public class GemAxe extends ItemAxe {

  public final int gemId;
  public final boolean supercharged;

  public static final Material[] extraEffectiveMaterials = { Material.leaves, Material.plants,
      Material.vine };

  public GemAxe(ToolMaterial toolMaterial, int gemId, boolean supercharged) {

    super(toolMaterial);
    this.gemId = gemId;
    this.supercharged = supercharged;
    this.setMaxDamage(toolMaterial.getMaxUses());
    addRecipe(new ItemStack(this), gemId, supercharged);
    this.setCreativeTab(SilentGems.tabSilentGems);
  }

  public static void addRecipe(ItemStack tool, int gemId, boolean supercharged) {

    ItemStack material = new ItemStack(ModItems.gem, 1, gemId + (supercharged ? 16 : 0));

    // Fish/flint tools
    if (gemId == ModMaterials.FISH_GEM_ID) {
      material = new ItemStack(Items.fish);
    } else if (gemId == ModMaterials.FLINT_GEM_ID) {
      material = new ItemStack(Items.flint);
    }

    if (supercharged) {
      GameRegistry.addRecipe(new ShapedOreRecipe(tool, true, "gg", "gs", " s", 'g', material, 's',
          CraftingMaterial.getStack(Names.ORNATE_STICK)));
    } else {
      GameRegistry.addRecipe(
          new ShapedOreRecipe(tool, true, "gg", "gs", " s", 'g', material, 's', "stickWood"));
    }
  }

  @Override
  public float getDigSpeed(ItemStack stack, Block block, int meta) {

    float speed = efficiencyOnProperMaterial;

    if (ForgeHooks.isToolEffective(stack, block, meta)) {
      return speed;
    }

    for (Material m : extraEffectiveMaterials) {
      if (block.getMaterial() == m) {
        return speed;
      }
    }

    return super.getDigSpeed(stack, block, meta);
  }
  
  @Override
  public int getHarvestLevel(ItemStack stack, String toolClass) {
    
    int level = super.getHarvestLevel(stack, toolClass);

    if (stack.stackTagCompound != null) {
      int tip = stack.stackTagCompound.getByte(Strings.TOOL_ICON_TIP);
      if (tip == 1 && level < Config.MINING_LEVEL_IRON_TIP) {
        // Iron tip
        level = Config.MINING_LEVEL_IRON_TIP;
      } else if (tip == 2 && level < Config.MINING_LEVEL_DIAMOND_TIP) {
        // Diamond tip
        level = Config.MINING_LEVEL_DIAMOND_TIP;
      }
    }
    
    return level;
  }
  
  @Override
  public int getMaxDamage(ItemStack stack) {
    
    int uses = super.getMaxDamage(stack);
    
    if (stack.stackTagCompound != null) {
      int tip = stack.stackTagCompound.getByte(Strings.TOOL_ICON_TIP);
      if (tip == 1) {
        // Iron tip
        uses += Config.DURABILITY_BOOST_IRON_TIP;
      } else if (tip == 2) {
        // Diamond tip
        uses += Config.DURABILITY_BOOST_DIAMOND_TIP;
      }
    }
    
    return uses;
  }

  public int getGemId() {

    return gemId;
  }

  @Override
  public IIcon getIcon(ItemStack stack, int pass) {

    return ToolRenderHelper.instance.getIcon(stack, pass, gemId, supercharged);
  }

  @Override
  public boolean getIsRepairable(ItemStack stack1, ItemStack stack2) {

    ItemStack material = new ItemStack(SRegistry.getItem(Names.GEM_ITEM), 1,
        gemId + (supercharged ? 16 : 0));
    if (material.getItem() == stack2.getItem()
        && material.getItemDamage() == stack2.getItemDamage()) {
      return true;
    } else {
      return super.getIsRepairable(stack1, stack2);
    }
  }

  @Override
  public int getRenderPasses(int meta) {

    return ToolRenderHelper.RENDER_PASS_COUNT;
  }

  @Override
  public String getUnlocalizedName(ItemStack stack) {

    return LocalizationHelper.TOOL_PREFIX + "Axe" + gemId + (supercharged ? "Plus" : "");
  }

  @Override
  public boolean hasEffect(ItemStack stack, int pass) {

    return stack.isItemEnchanted() && pass == 5;
  }

  @Override
  public boolean onBlockDestroyed(ItemStack stack, World world, Block block, int x, int y, int z,
      EntityLivingBase entity) {

    if (block.getMaterial() != Material.leaves) {
      return super.onBlockDestroyed(stack, world, block, x, y, z, entity);
    } else {
      return true;
    }
  }

  @Override
  public boolean onItemUse(ItemStack stack, EntityPlayer player, World world, int x, int y, int z,
      int side, float hitX, float hitY, float hitZ) {

    boolean used = false;
    int toolSlot = player.inventory.currentItem;
    int itemSlot = toolSlot + 1;
    ItemStack nextStack = null;

    if (toolSlot < 8) {
      nextStack = player.inventory.getStackInSlot(itemSlot);
      if (nextStack != null) {
        Item item = nextStack.getItem();
        if (item instanceof ItemBlock || item instanceof IPlaceable) {
          ForgeDirection d = ForgeDirection.VALID_DIRECTIONS[side];

          int px = x + d.offsetX;
          int py = y + d.offsetY;
          int pz = z + d.offsetZ;
          int playerX = (int) Math.floor(player.posX);
          int playerY = (int) Math.floor(player.posY);
          int playerZ = (int) Math.floor(player.posZ);

          // Check for overlap with player, except for torches and torch bandolier
          if (Item.getIdFromItem(item) != Block.getIdFromBlock(Blocks.torch)
              && item != SRegistry.getItem(Names.TORCH_BANDOLIER) && px == playerX
              && (py == playerY || py == playerY + 1 || py == playerY - 1) && pz == playerZ) {
            return false;
          }

          used = item.onItemUse(nextStack, player, world, x, y, z, side, hitX, hitY, hitZ);
          if (nextStack.stackSize < 1) {
            nextStack = null;
            player.inventory.setInventorySlotContents(itemSlot, null);
          }
        }
      }
    }

    return used;
  }

  @Override
  public boolean requiresMultipleRenderPasses() {

    return true;
  }
  
  @Override
  public void registerIcons(IIconRegister reg) {

    if (gemId >= 0 && gemId < ToolRenderHelper.HEAD_TYPE_COUNT) {
      itemIcon = ToolRenderHelper.instance.axeIcons.headM[gemId];
    }
  }

  @Override
  public boolean onBlockStartBreak(ItemStack stack, int x, int y, int z, EntityPlayer player) {

    if (EnchantmentHelper.getEnchantmentLevel(ModEnchantments.aoe.effectId, stack) > 0) {
      EnchantmentAOE.tryActivate(stack, x, y, z, player);
    }

    return super.onBlockStartBreak(stack, x, y, z, player);
  }
}
