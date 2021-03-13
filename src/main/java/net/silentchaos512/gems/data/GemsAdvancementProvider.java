package net.silentchaos512.gems.data;

import com.google.common.collect.Sets;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.FrameType;
import net.minecraft.advancements.ICriterionInstance;
import net.minecraft.advancements.IRequirementsStrategy;
import net.minecraft.advancements.criterion.*;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.DirectoryCache;
import net.minecraft.data.IDataProvider;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.tags.ITag;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.silentchaos512.gems.SilentGems;
import net.silentchaos512.gems.block.HardenedRock;
import net.silentchaos512.gems.block.MiscBlocks;
import net.silentchaos512.gems.init.GemsBlocks;
import net.silentchaos512.gems.init.GemsItems;
import net.silentchaos512.gems.init.GemsTags;
import net.silentchaos512.gems.item.CraftingItems;
import net.silentchaos512.gems.lib.Gems;
import net.silentchaos512.lib.advancements.GenericIntTrigger;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Set;
import java.util.function.Consumer;

public class GemsAdvancementProvider implements IDataProvider {
    private static final Logger LOGGER = LogManager.getLogger();
    private static final Gson GSON = (new GsonBuilder()).setPrettyPrinting().create();
    private final DataGenerator generator;

    public GemsAdvancementProvider(DataGenerator generator) {
        this.generator = generator;
    }

    @Override
    public void act(DirectoryCache cache) throws IOException {
        Path path = this.generator.getOutputFolder();
        Set<ResourceLocation> set = Sets.newHashSet();
        //noinspection OverlyLongLambda
        Consumer<Advancement> consumer = (p_204017_3_) -> {
            if (!set.add(p_204017_3_.getId())) {
                throw new IllegalStateException("Duplicate advancement " + p_204017_3_.getId());
            } else {
                Path path1 = getPath(path, p_204017_3_);

                try {
                    IDataProvider.save(GSON, cache, p_204017_3_.copy().serialize(), path1);
                } catch (IOException ioexception) {
                    LOGGER.error("Couldn't save advancement {}", path1, ioexception);
                }

            }
        };

        new Advancements().accept(consumer);
    }

    @Override
    public String getName() {
        return "Silent's Gems - Advancements";
    }

    private static Path getPath(Path pathIn, Advancement advancementIn) {
        return pathIn.resolve("data/" + advancementIn.getId().getNamespace() + "/advancements/" + advancementIn.getId().getPath() + ".json");
    }

    private static class Advancements implements Consumer<Consumer<Advancement>> {
        @Override
        public void accept(Consumer<Advancement> consumer) {
            Advancement root = Advancement.Builder.builder()
                    .withDisplay(Gems.ALEXANDRITE.getItem(), title("root"), description("root"), new ResourceLocation("minecraft:textures/gui/advancements/backgrounds/adventure.png"), FrameType.TASK, false, false, false)
                    .withCriterion("get_item", getItem(Items.CRAFTING_TABLE))
                    .register(consumer, id("root"));

            Advancement getGems = simpleGetItem(consumer, GemsTags.Items.GEMS, Gems.RUBY.getItem(), root, "gem");

            for (Gems.Set set : Gems.Set.values()) {
                String name = "all_gems_" + set.getName();
                Advancement.Builder builder = Advancement.Builder.builder()
                        .withParent(getGems)
                        .withDisplay(set.getSetIcon().getItem(), title(name), description(name), null, FrameType.CHALLENGE, true, true, false);
                for (Gems gem : set) {
                    builder.withCriterion(gem.getName(), getItem(gem.getItem()));
                }
                builder.withRequirementsStrategy(IRequirementsStrategy.AND).register(consumer, id(name));
            }

            Advancement chaosCrystal = simpleGetItem(consumer, CraftingItems.CHAOS_CRYSTAL, root, "chaos_crystal");
            Advancement chaosIron = Advancement.Builder.builder()
                    .withParent(chaosCrystal)
                    .withDisplay(CraftingItems.CHAOS_IRON_INGOT, title("chaos_iron"), description("chaos_iron"), null, FrameType.TASK, true, true, false)
                    .withCriterion("get_dust", getItem(CraftingItems.CHAOS_IRON_DUST))
                    .withCriterion("get_ingot", getItem(CraftingItems.CHAOS_IRON_INGOT))
                    .register(consumer, id("chaos_iron"));
            Advancement teleporterAnchor = simpleGetItem(consumer, GemsBlocks.TELEPORTER_ANCHOR, chaosCrystal, "teleporter_anchor");
            Advancement returnHomeCharm = simpleGetItem(consumer, GemsTags.Items.RETURN_HOME_CHARMS, Gems.AMETHYST.getReturnHomeCharm(), teleporterAnchor, "return_home_charm");

            Advancement tokenEnchanter = simpleGetItem(consumer, GemsBlocks.TOKEN_ENCHANTER, chaosIron, "token_enchanter");
            Advancement enchantmentToken = simpleGetItem(consumer, GemsItems.ENCHANTMENT_TOKEN, tokenEnchanter, "enchantment_token");
            Advancement enrichedChaos = simpleGetItem(consumer, CraftingItems.ENRICHED_CHAOS_CRYSTAL, tokenEnchanter, "enriched_chaos");
            Advancement chaosMeter = simpleGetItem(consumer, GemsItems.CHAOS_METER, enrichedChaos, "chaos_meter");

            Advancement transmutationAltar = simpleGetItem(consumer, GemsBlocks.TRANSMUTATION_ALTAR, enrichedChaos, "transmutation_altar");
            Advancement transmutationCrystals = Advancement.Builder.builder()
                    .withParent(transmutationAltar)
                    .withDisplay(CraftingItems.MAGMA_CREAM_CRYSTAL, title("transmutation_crystals"), description("transmutation_crystals"), null, FrameType.GOAL, true, true, false)
                    .withCriterion("slime", getItem(CraftingItems.SLIME_CRYSTAL))
                    .withCriterion("magma", getItem(CraftingItems.MAGMA_CREAM_CRYSTAL))
                    .withCriterion("ender", getItem(CraftingItems.ENDER_SLIME_CRYSTAL))
                    .withRequirementsStrategy(IRequirementsStrategy.AND)
                    .register(consumer, id("transmutation_crystals"));

            Advancement supercharger = simpleGetItem(consumer, GemsBlocks.SUPERCHARGER, chaosIron, "supercharger");
            Advancement superchargerPillar1 = Advancement.Builder.builder()
                    .withParent(supercharger)
                    .withDisplay(CraftingItems.CHARGING_AGENT, title("supercharger_pillar1"), description("supercharger_pillar1"), null, FrameType.TASK, true, true, false)
                    .withCriterion("get_charging_agent", getItem(CraftingItems.CHARGING_AGENT))
                    .withCriterion("get_caps", getItem(GemsTags.Items.SUPERCHARGER_PILLAR_CAP, 4))
                    .withCriterion("get_bases", getItem(GemsTags.Items.SUPERCHARGER_PILLAR_LEVEL1, 4))
                    .withRequirementsStrategy(IRequirementsStrategy.AND)
                    .register(consumer, id("supercharger_pillar1"));
            Advancement superchargerPillar2 = Advancement.Builder.builder()
                    .withParent(superchargerPillar1)
                    .withDisplay(CraftingItems.SUPER_CHARGING_AGENT, title("supercharger_pillar2"), description("supercharger_pillar2"), null, FrameType.TASK, true, true, false)
                    .withCriterion("get_charging_agent", getItem(CraftingItems.SUPER_CHARGING_AGENT))
                    .withCriterion("get_bases", getItem(GemsTags.Items.SUPERCHARGER_PILLAR_LEVEL2, 4))
                    .withRequirementsStrategy(IRequirementsStrategy.AND)
                    .register(consumer, id("supercharger_pillar2"));
            Advancement superchargerPillar3 = Advancement.Builder.builder()
                    .withParent(superchargerPillar2)
                    .withDisplay(CraftingItems.ULTRA_CHARGING_AGENT, title("supercharger_pillar3"), description("supercharger_pillar3"), null, FrameType.CHALLENGE, true, true, false)
                    .withCriterion("get_charging_agent", getItem(CraftingItems.SUPER_CHARGING_AGENT))
                    .withCriterion("get_bases", getItem(GemsTags.Items.SUPERCHARGER_PILLAR_LEVEL3, 4))
                    .withRequirementsStrategy(IRequirementsStrategy.AND)
                    .register(consumer, id("supercharger_pillar3"));

            Advancement pedestal = simpleGetItem(consumer, GemsTags.Items.PEDESTALS, GemsBlocks.STONE_PEDESTAL, enrichedChaos, "pedestal");
            Advancement chaosOrbOnPedestal = Advancement.Builder.builder()
                    .withParent(pedestal)
                    .withDisplay(GemsItems.PERFECT_CHAOS_ORB, title("chaos_orb_on_pedestal"), description("chaos_orb_on_pedestal"), null, FrameType.GOAL, true, true, false)
                    .withCriterion("place", placedBlockWithItem(ItemPredicate.Builder.create().tag(GemsTags.Items.CHAOS_ORBS).build()))
                    .register(consumer, id("chaos_orb_on_pedestal"));

            Advancement chaosGem = Advancement.Builder.builder()
                    .withParent(enrichedChaos)
                    .withDisplay(Gems.GREEN_SAPPHIRE.getChaosGem(), title("chaos_gem"), description("chaos_gem"), null, FrameType.TASK, true, true, false)
                    .withCriterion("get_gem", getItem(GemsTags.Items.CHAOS_GEMS))
                    .withCriterion("get_rune", getItem(GemsItems.CHAOS_RUNE))
                    .register(consumer, id("chaos_gem"));
            Advancement chaosGemOnPedestal = Advancement.Builder.builder()
                    .withParent(chaosGem)
                    .withDisplay(Gems.TANZANITE.getChaosGem(), title("chaos_gem_on_pedestal"), description("chaos_gem_on_pedestal"), null, FrameType.GOAL, true, true, false)
                    .withCriterion("place", placedBlockWithItem(ItemPredicate.Builder.create().tag(GemsTags.Items.CHAOS_GEMS).build()))
                    .register(consumer, id("chaos_gem_on_pedestal"));


            Advancement enderCrystal = simpleGetItem(consumer, CraftingItems.ENDER_CRYSTAL, root, "ender_crystal");
            Advancement enderCrystalBlock = Advancement.Builder.builder()
                    .withParent(enderCrystal)
                    .withDisplay(MiscBlocks.ENDER_CRYSTAL, title("ender_crystal_block"), description("ender_crystal_block"), null, FrameType.TASK, true, true, false)
                    .withCriterion("walk_on", GenericIntTrigger.Instance.instance(MiscBlocks.WALK_ON_ENDER_CRYSTAL, 1))
                    .register(consumer, id("ender_crystal_block"));

            Advancement teleporter = simpleGetItem(consumer, GemsTags.Items.TELEPORTERS, Gems.SAPPHIRE.getTeleporter(), enderCrystal, "teleporter");
            Advancement linkTeleporter = Advancement.Builder.builder()
                    .withParent(teleporter)
                    .withDisplay(GemsItems.TELEPORTER_LINKER, title("link_teleporter"), description("link_teleporter"), null, FrameType.TASK, true, true, false)
                    .withCriterion("get_link", getItem(GemsItems.TELEPORTER_LINKER))
                    .withCriterion("link_teleporters", placedBlockWithItem(ItemPredicate.Builder.create().item(GemsItems.TELEPORTER_LINKER).build()))
                    .register(consumer, id("link_teleporter"));

            Advancement soulGem = simpleGetItem(consumer, GemsItems.SOUL_GEM, root, "soul_gem");
            Advancement soulUrn = simpleGetItem(consumer, GemsBlocks.SOUL_URN, soulGem, "soul_urn");

            Advancement glowrose = simpleGetItem(consumer, GemsTags.Items.GLOWROSES, Gems.RUBY.getGlowrose(), root, "glowrose");
            Advancement luminousFlowerPot = Advancement.Builder.builder()
                    .withParent(glowrose)
                    .withDisplay(GemsBlocks.LUMINOUS_FLOWER_POT, title("luminous_flower_pot"), description("luminous_flower_pot"), null, FrameType.GOAL, true, true, false)
                    .withCriterion("get_pot", getItem(GemsBlocks.LUMINOUS_FLOWER_POT))
                    .withCriterion("plant_flower", placedBlockWithItem(ItemPredicate.Builder.create().tag(GemsTags.Items.GLOWROSES).build()))
                    .withRequirementsStrategy(IRequirementsStrategy.AND)
                    .register(consumer, id("luminous_flower_pot"));

            Advancement growFluffyPuffs = Advancement.Builder.builder()
                    .withParent(root)
                    .withDisplay(CraftingItems.FLUFFY_PUFF, title("grow_fluffy_puffs"), description("grow_fluffy_puffs"), null, FrameType.TASK, true, true, false)
                    .withCriterion("plant_seeds", PlacedBlockTrigger.Instance.placedBlock(GemsBlocks.FLUFFY_PUFF_PLANT.get()))
                    .withCriterion("harvest", getItem(CraftingItems.FLUFFY_PUFF))
                    .withRequirementsStrategy(IRequirementsStrategy.AND)
                    .register(consumer, id("grow_fluffy_puffs"));
            Advancement fluffyBlocks = simpleGetItem(consumer, GemsTags.Items.FLUFFY_BLOCKS, GemsBlocks.WHITE_FLUFFY_BLOCK, growFluffyPuffs, "fluffy_block");

            Advancement hardenedRock = simpleGetItem(consumer, GemsTags.Items.HARDENED_ROCKS, HardenedRock.STONE.asBlock(), root, "hardened_rock");
        }

        @Nonnull
        private static PlacedBlockTrigger.Instance placedBlockWithItem(ItemPredicate item) {
            return new PlacedBlockTrigger.Instance(EntityPredicate.AndPredicate.ANY_AND,
                    null,
                    StatePropertiesPredicate.EMPTY,
                    LocationPredicate.ANY,
                    item);
        }

        private static Advancement simpleGetItem(Consumer<Advancement> consumer, IItemProvider item, Advancement parent, String key) {
            return simpleGetItem(consumer, getItem(item), new ItemStack(item), parent, key);
        }

        private static Advancement simpleGetItem(Consumer<Advancement> consumer, ITag<Item> item, IItemProvider icon, Advancement parent, String key) {
            return simpleGetItem(consumer, getItem(item), new ItemStack(icon), parent, key);
        }

        private static Advancement simpleGetItem(Consumer<Advancement> consumer, ICriterionInstance item, ItemStack icon, Advancement parent, String key) {
            return Advancement.Builder.builder()
                    .withParent(parent)
                    .withDisplay(icon, title(key), description(key), null, FrameType.TASK, true, true, false)
                    .withCriterion("get_item", item)
                    .register(consumer, id(key));
        }

        private static String id(String path) {
            return SilentGems.getId(path).toString();
        }

        private static ICriterionInstance getItem(IItemProvider... items) {
            return InventoryChangeTrigger.Instance.forItems(items);
        }

        private static ICriterionInstance getItem(ITag<Item> tag) {
            return InventoryChangeTrigger.Instance.forItems(ItemPredicate.Builder.create().tag(tag).build());
        }

        private static ICriterionInstance getItem(ITag<Item> tag, int count) {
            return InventoryChangeTrigger.Instance.forItems(new ItemPredicate(tag,
                    null,
                    MinMaxBounds.IntBound.atLeast(count),
                    MinMaxBounds.IntBound.UNBOUNDED,
                    EnchantmentPredicate.enchantments,
                    EnchantmentPredicate.enchantments,
                    null,
                    NBTPredicate.ANY));
        }

        private static ICriterionInstance genericInt(ResourceLocation id, int value) {
            return GenericIntTrigger.Instance.instance(id, value);
        }

        private static ITextComponent title(String key) {
            return new TranslationTextComponent("advancements.silentgems." + key + ".title");
        }

        private static ITextComponent description(String key) {
            return new TranslationTextComponent("advancements.silentgems." + key + ".description");
        }
    }
}
