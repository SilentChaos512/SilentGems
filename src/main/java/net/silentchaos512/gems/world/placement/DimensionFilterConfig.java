package net.silentchaos512.gems.world.placement;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;
import net.minecraft.world.gen.placement.IPlacementConfig;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;

public class DimensionFilterConfig implements IPlacementConfig {
    public static final Codec<DimensionFilterConfig> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    Codec.BOOL.fieldOf("is_whitelist").forGetter(config -> config.isWhitelist),
                    Codec.STRING.listOf().fieldOf("list").forGetter(config ->
                            config.dimensions.stream()
                                    .map(rk -> rk.getRegistryName().toString())
                                    .collect(Collectors.toList()))
            ).apply(instance, (isWhitelist, strList) -> {
                Collection<RegistryKey<World>> dims = strList.stream()
                        .map(str -> RegistryKey.getOrCreateKey(Registry.WORLD_KEY, new ResourceLocation(str)))
                        .collect(Collectors.toList());
                return new DimensionFilterConfig(isWhitelist, dims);
            }));

    private final boolean isWhitelist;
    private final Collection<RegistryKey<World>> dimensions = new ArrayList<>();

    public DimensionFilterConfig(boolean isWhitelist, Collection<RegistryKey<World>> dimensions) {
        this.isWhitelist = isWhitelist;
        this.dimensions.addAll(dimensions);
    }

    @SafeVarargs
    public static DimensionFilterConfig whitelist(RegistryKey<World>... dimensions) {
        return new DimensionFilterConfig(true, Arrays.asList(dimensions));
    }

    @SafeVarargs
    public static DimensionFilterConfig blacklist(RegistryKey<World>... dimensions) {
        return new DimensionFilterConfig(false, Arrays.asList(dimensions));
    }

    public boolean matches(RegistryKey<World> dimension) {
        return this.dimensions.contains(dimension) == this.isWhitelist;
    }
}
