package net.silentchaos512.gems.lib.chaosbuff;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import net.silentchaos512.gems.SilentGems;
import net.silentchaos512.utils.Color;

import javax.annotation.Nullable;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Supplier;

public class SimpleChaosBuff implements IChaosBuff {
    static final Serializer<SimpleChaosBuff> SERIALIZER = new Serializer<>(Serializer.NAME, SimpleChaosBuff::new);

    private final ResourceLocation id;
    Supplier<ITextComponent> displayName;
    int maxLevel;
    int[] slotsByLevel;
    int inactiveCost;
    int[] activeCostByLevel;
    CostConditions[] costConditions;

    public SimpleChaosBuff(ResourceLocation id) {
        this.id = id;
    }

    @Override
    public ResourceLocation getId() {
        return this.id;
    }

    @Override
    public void applyTo(EntityPlayer player, int level) { }

    @Override
    public void removeFrom(EntityPlayer player) { }

    @Override
    public int getChaosGenerated(@Nullable EntityPlayer player, int level) {
        if (player != null && !this.isActive(player) || this.activeCostByLevel.length == 0) {
            return this.inactiveCost;
        }
        int clamp = MathHelper.clamp(level, 0, this.activeCostByLevel.length - 1);
        return this.activeCostByLevel[clamp];
    }

    @Override
    public int getMaxLevel() {
        return this.maxLevel;
    }

    @Override
    public int getSlotsForLevel(int level) {
        if (this.slotsByLevel.length == 0) {
            return 0;
        }
        int clamp = MathHelper.clamp(level, 0, this.slotsByLevel.length - 1);
        return this.slotsByLevel[clamp];
    }

    @Override
    public boolean isActive(EntityPlayer player) {
        for (CostConditions c : costConditions) {
            if (c != null && !c.appliesTo(player)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public ITextComponent getDisplayName(int level) {
        if (level < 1) {
            return displayName.get();
        }
        return displayName.get()
                .appendText(" ")
                .appendSibling(new TextComponentTranslation("enchantment.level." + level));
    }

    @Override
    public int getRuneColor() {
        return Color.VALUE_WHITE;
    }

    @Override
    public IChaosBuffSerializer<?> getSerializer() {
        return SERIALIZER;
    }

    public static final class Serializer<T extends SimpleChaosBuff> implements IChaosBuffSerializer<T> {
        private static final ResourceLocation NAME = SilentGems.getId("simple");

        private final ResourceLocation serializerId;
        private final Function<ResourceLocation, T> factory;
        private final BiConsumer<T, JsonObject> readJson;

        public Serializer(ResourceLocation serializerId, Function<ResourceLocation, T> factory) {
            this(serializerId, factory, null);
        }

        public Serializer(ResourceLocation serializerId,
                          Function<ResourceLocation, T> factory,
                          @Nullable BiConsumer<T, JsonObject> readJson) {
            this.serializerId = serializerId;
            this.factory = factory;
            this.readJson = readJson;
        }

        @Override
        public T read(ResourceLocation id, JsonObject json) {
            T buff = factory.apply(id);
            buff.maxLevel = JsonUtils.getInt(json, "maxLevel", 1);
            buff.displayName = readTextComponent(json, "displayName");

            readSlots(buff, json);
            readCost(buff, json.get("cost"));

            if (readJson != null) {
                readJson.accept(buff, json);
            }

            return buff;
        }

        private void readSlots(T buff, JsonObject json) {
            JsonElement elem = json.get("slots");
            if (elem.isJsonArray()) {
                JsonArray array = elem.getAsJsonArray();
                buff.slotsByLevel = new int[array.size()];
                for (int i = 0; i < array.size(); ++i) {
                    buff.slotsByLevel[i] = array.get(i).getAsInt();
                }
            } else {
                buff.slotsByLevel = new int[]{elem.getAsInt()};
            }
        }

        private void readCost(T buff, JsonElement jsonElement) {
            if (!jsonElement.isJsonObject()) {
                throw new JsonParseException("Expected 'cost' to be an object");
            }
            JsonObject json = jsonElement.getAsJsonObject();

            buff.inactiveCost = JsonUtils.getInt(json, "inactive", 0);

            JsonElement elem = json.get("active");
            if (elem == null) {
                throw new JsonParseException("Missing required element, 'cost.active' (should be array or int)");
            } else if (elem.isJsonArray()) {
                JsonArray array = elem.getAsJsonArray();
                buff.activeCostByLevel = new int[array.size()];
                for (int i = 0; i < array.size(); ++i) {
                    buff.activeCostByLevel[i] = array.get(i).getAsInt();
                }
            } else {
                buff.activeCostByLevel = new int[]{elem.getAsInt()};
            }

            JsonElement elem1 = json.get("conditions");
            if (elem1 == null) {
                buff.costConditions = new CostConditions[0];
            } else if (elem1.isJsonArray()) {
                JsonArray array = elem1.getAsJsonArray();
                buff.costConditions = new CostConditions[array.size()];
                for (int i = 0; i < array.size(); ++i) {
                    String str = array.get(i).getAsString();
                    buff.costConditions[i] = CostConditions.from(str);
                }
            } else {
                buff.costConditions = new CostConditions[]{CostConditions.from(elem1.getAsString())};
            }
        }

        @Override
        public T read(ResourceLocation id, PacketBuffer buffer) {
            T buff = factory.apply(id);
            // TODO
            return buff;
        }

        @Override
        public void write(PacketBuffer buffer, T trait) {
            // TODO
        }

        @Override
        public ResourceLocation getName() {
            return this.serializerId;
        }

        private static Supplier<ITextComponent> readTextComponent(JsonObject json, String name) {
            JsonElement element = json.get(name);
            if (element != null && element.isJsonObject()) {
                JsonObject obj = element.getAsJsonObject();
                final boolean translate = JsonUtils.getBoolean(obj, "translate", false);
                final String value = JsonUtils.getString(obj, "name");
                return translate
                        ? () -> new TextComponentTranslation(value)
                        : () -> new TextComponentString(value);
            } else if (element != null) {
                throw new JsonParseException("Expected '" + name + "' to be an object");
            } else {
                throw new JsonParseException("Missing required object '" + name + "'");
            }
        }
    }
}
