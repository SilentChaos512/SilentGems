package net.silentchaos512.gems.block.supercharger;

import lombok.Getter;
import net.minecraft.item.Item;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.tags.Tag;

import java.util.*;

public class SuperchargerPillarStructure {
    @Getter private final int tier;
    private final List<Ingredient> list = new ArrayList<>();

    public SuperchargerPillarStructure(int tier, Collection<Tag<Item>> blocks) {
        this.tier = tier;
        blocks.stream().map(Ingredient::fromTag).forEach(this.list::add);
    }

    public List<Ingredient> getBlocks() {
        return Collections.unmodifiableList(list);
    }
}
