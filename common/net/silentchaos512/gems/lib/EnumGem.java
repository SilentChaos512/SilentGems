package net.silentchaos512.gems.lib;

import net.minecraft.item.Item.ToolMaterial;
import net.minecraft.item.ItemArmor.ArmorMaterial;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.MathHelper;
import net.silentchaos512.gems.block.ModBlocks;
import net.silentchaos512.gems.item.ModItems;
import net.silentchaos512.gems.material.ModMaterials;

public enum EnumGem implements IStringSerializable {

  RUBY(0, "Ruby"),
  GARNET(1, "Garnet"),
  TOPAZ(2, "Topaz"),
  HELIODOR(3, "Heliodor"),
  PERIDOT(4, "Peridot"),
  EMERALD(5, "Beryl"),
  AQUAMARINE(6, "Aquamarine"),
  SAPPHIRE(7, "Sapphire"),
  IOLITE(8, "Iolite"),
  AMETHYST(9, "Amethyst"),
  MORGANITE(10, "Morganite"),
  ONYX(11, "Onyx");

  public final byte id;
  public final String name;

  private EnumGem(int id, String name) {

    this.id = (byte) id;
    this.name = name;
  }

  /**
   * Gets the gem with the given meta (id). The meta is clamped to the bounds of the values array.
   * @param meta
   * @return
   */
  public static EnumGem get(int meta) {

    return values()[MathHelper.clamp_int(meta, 0, values().length - 1)];
  }
  
  @Override
  public String getName() {

    return name;
  }

  /**
   * Gets an ItemStack of one of the corresponding GemBlock.
   * 
   * @return
   */
  public ItemStack getBlock() {

    return new ItemStack(ModBlocks.gemBlock, 1, id);
  }

  public String getBlockOreName() {

    return "block" + this.name;
  }

  /**
   * Gets an ItemStack of one of the corresponding Gem.
   * 
   * @return
   */
  public ItemStack getItem() {

    return new ItemStack(ModItems.gem, 1, id);
  }

  public String getItemOreName() {

    return "gem" + this.name;
  }

  /**
   * Gets an ItemStack of one of the corresponding GemOre.
   * 
   * @return
   */
  public ItemStack getOre() {

    return new ItemStack(ModBlocks.gemOre, 1, id);
  }

  public String getOreBlockOreName() {

    return "ore" + this.name;
  }

  /**
   * Gets an ItemStack of one of the corresponding GemShard, if there is one.
   * 
   * @return
   */
  public ItemStack getShard() {

    return new ItemStack(ModItems.gemShard, 1, id);
  }

  public String getShardOreName() {

    return "nugget" + this.name;
  }

  /**
   * Gets the tool material for this gem, if there is one.
   * 
   * @return
   */
  public ToolMaterial getToolMaterial(boolean supercharged) {

    if (supercharged) {
      if (id == RUBY.id)
        return ModMaterials.toolSupRuby;
      else if (id == GARNET.id)
        return ModMaterials.toolSupGarnet;
      else if (id == TOPAZ.id)
        return ModMaterials.toolSupTopaz;
      else if (id == HELIODOR.id)
        return ModMaterials.toolSupHeliodor;
      else if (id == PERIDOT.id)
        return ModMaterials.toolSupPeridot;
      else if (id == EMERALD.id)
        return ModMaterials.toolSupEmerald;
      else if (id == AQUAMARINE.id)
        return ModMaterials.toolSupAquamarine;
      else if (id == SAPPHIRE.id)
        return ModMaterials.toolSupSapphire;
      else if (id == IOLITE.id)
        return ModMaterials.toolSupIolite;
      else if (id == AMETHYST.id)
        return ModMaterials.toolSupAmethyst;
      else if (id == MORGANITE.id)
        return ModMaterials.toolSupMorganite;
      else if (id == ONYX.id)
        return ModMaterials.toolSupOnyx;
      else
        return null;
    } else {
      if (id == RUBY.id)
        return ModMaterials.toolRegRuby;
      else if (id == GARNET.id)
        return ModMaterials.toolRegGarnet;
      else if (id == TOPAZ.id)
        return ModMaterials.toolRegTopaz;
      else if (id == HELIODOR.id)
        return ModMaterials.toolRegHeliodor;
      else if (id == PERIDOT.id)
        return ModMaterials.toolRegPeridot;
      else if (id == EMERALD.id)
        return ModMaterials.toolRegEmerald;
      else if (id == AQUAMARINE.id)
        return ModMaterials.toolRegAquamarine;
      else if (id == SAPPHIRE.id)
        return ModMaterials.toolRegSapphire;
      else if (id == IOLITE.id)
        return ModMaterials.toolRegIolite;
      else if (id == AMETHYST.id)
        return ModMaterials.toolRegAmethyst;
      else if (id == MORGANITE.id)
        return ModMaterials.toolRegMorganite;
      else if (id == ONYX.id)
        return ModMaterials.toolRegOnyx;
      else
        return null;
    }
  }

  public ArmorMaterial getArmorMaterial(boolean supercharged) {

    if (supercharged) {
      if (id == RUBY.id)
        return ModMaterials.armorSupRuby;
      else if (id == GARNET.id)
        return ModMaterials.armorSupGarnet;
      else if (id == TOPAZ.id)
        return ModMaterials.armorSupTopaz;
      else if (id == HELIODOR.id)
        return ModMaterials.armorSupHeliodor;
      else if (id == PERIDOT.id)
        return ModMaterials.armorSupPeridot;
      else if (id == EMERALD.id)
        return ModMaterials.armorSupEmerald;
      else if (id == AQUAMARINE.id)
        return ModMaterials.armorSupAquamarine;
      else if (id == SAPPHIRE.id)
        return ModMaterials.armorSupSapphire;
      else if (id == IOLITE.id)
        return ModMaterials.armorSupIolite;
      else if (id == AMETHYST.id)
        return ModMaterials.armorSupAmethyst;
      else if (id == MORGANITE.id)
        return ModMaterials.armorSupMorganite;
      else if (id == ONYX.id)
        return ModMaterials.armorSupOnyx;
      else
        return null;
    } else {
      if (id == RUBY.id)
        return ModMaterials.armorRegRuby;
      else if (id == GARNET.id)
        return ModMaterials.armorRegGarnet;
      else if (id == TOPAZ.id)
        return ModMaterials.armorRegTopaz;
      else if (id == HELIODOR.id)
        return ModMaterials.armorRegHeliodor;
      else if (id == PERIDOT.id)
        return ModMaterials.armorRegPeridot;
      else if (id == EMERALD.id)
        return ModMaterials.armorRegEmerald;
      else if (id == AQUAMARINE.id)
        return ModMaterials.armorRegAquamarine;
      else if (id == SAPPHIRE.id)
        return ModMaterials.armorRegSapphire;
      else if (id == IOLITE.id)
        return ModMaterials.armorRegIolite;
      else if (id == AMETHYST.id)
        return ModMaterials.armorRegAmethyst;
      else if (id == MORGANITE.id)
        return ModMaterials.armorRegMorganite;
      else if (id == ONYX.id)
        return ModMaterials.armorRegOnyx;
      else
        return null;
    }
  }
}
