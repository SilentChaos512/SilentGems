package net.silentchaos512.gems.lib.soul;

import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.silentchaos512.gear.api.parts.*;
import net.silentchaos512.gear.api.stats.CommonItemStats;
import net.silentchaos512.gear.api.stats.ItemStat;
import net.silentchaos512.gear.api.stats.StatInstance;
import net.silentchaos512.gems.util.SoulManager;
import net.silentchaos512.utils.MathUtils;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ToolSoulPart extends ItemPart {
    public static final PartType PART_TYPE = PartType.create("gear_soul", "S", ToolSoulPart::new);
    public static final IPartPosition PART_POSITION = new IPartPosition() {
        @Override
        public String getTexturePrefix() {
            return "";
        }

        @Override
        public String getModelIndex() {
            return "";
        }
    };

    public ToolSoulPart(ResourceLocation name, PartOrigins origin) {
        super(name, origin);
    }

    @Override
    public PartType getType() {
        return PART_TYPE;
    }

    @Override
    public IPartPosition getPartPosition() {
        return PART_POSITION;
    }

    @Override
    public void addInformation(ItemPartData itemPartData, ItemStack itemStack, World world, List<String> list, boolean b) {
    }

    @Override
    public String getTypeName() {
        return "gear_soul";
    }

    @Nullable
    @Override
    public ResourceLocation getTexture(ItemPartData part, ItemStack gear, String gearClass, IPartPosition position, int animationFrame) {
        return null;
    }

    @Nullable
    @Override
    public ResourceLocation getBrokenTexture(ItemPartData part, ItemStack gear, String gearClass, IPartPosition position) {
        return null;
    }

    @Override
    public Collection<StatInstance> getStatModifiers(ItemStack gear, ItemStat stat, ItemPartData part) {
        Collection<StatInstance> mods = new ArrayList<>();
        ToolSoul soul = SoulManager.getSoul(!gear.isEmpty() ? gear : part.getCraftingItem());
        if (soul != null) {
            float amount = getSoulStatModifier(soul, stat);
            if (!MathUtils.doublesEqual(amount, 0)) {
                mods.add(getSoulBoostedModifier(stat, amount - 1));
            }
        }
        return mods;
    }

    private static float getSoulStatModifier(ToolSoul soul, ItemStat stat) {
        if (stat == CommonItemStats.ARMOR) return soul.getProtectionModifier();
        if (stat == CommonItemStats.DURABILITY) return soul.getDurabilityModifier();
        if (stat == CommonItemStats.HARVEST_SPEED) return soul.getHarvestSpeedModifier();
        if (stat == CommonItemStats.MAGIC_DAMAGE) return soul.getMagicDamageModifier();
        if (stat == CommonItemStats.MELEE_DAMAGE) return soul.getMeleeDamageModifier();
        return 0;
    }

    private static StatInstance getSoulBoostedModifier(ItemStat stat, float value) {
        return new StatInstance("silentgems:soul/" + stat.getName().getPath(), value, StatInstance.Operation.MUL1);
    }

/*    @Override
    public Map<Trait, Integer> getTraits(ItemStack gear, ItemPartData part) {
        Map<Trait, Integer> traits = new HashMap<>(super.getTraits(gear, part));

        ToolSoul soul = SoulManager.getSoul(gear);
        if (soul != null) {
            soul.getSkills().forEach((skill, level) -> traits.put(skill.getTrait(), level));
        }

        return traits;
    }*/
}
