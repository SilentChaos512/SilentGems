package net.silentchaos512.gems.item;

import net.minecraftforge.oredict.OreDictionary;
import net.silentchaos512.gems.SilentGems;
import net.silentchaos512.gems.lib.EnumGem;
import net.silentchaos512.gems.lib.Names;
import net.silentchaos512.lib.item.ItemSL;


public class ItemGemShard extends ItemSL {

  public ItemGemShard() {

    super(EnumGem.values().length, SilentGems.MOD_ID, Names.GEM_SHARD);
  }

  @Override
  public void addOreDict() {

    for (EnumGem gem : EnumGem.values()) {
      OreDictionary.registerOre(gem.getShardOreName(), gem.getShard());
    }
  }
}
