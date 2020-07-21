package net.silentchaos512.gems.block.supercharger;

import net.minecraft.item.Item;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.tags.ITag;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Currently just used by JEI? Could possibly use this to simplify tile entity code somewhat.
 */
public class SuperchargerPillarStructure {
    private final int tier;
    private final List<Ingredient> list = new ArrayList<>();

    public SuperchargerPillarStructure(int tier, Collection<ITag<Item>> blocks) {
        this.tier = tier;
        blocks.stream().map(Ingredient::fromTag).forEach(this.list::add);
    }

    public int getTier() {
        return tier;
    }

    public List<Ingredient> getBlocks() {
        return Collections.unmodifiableList(list);
    }
}
