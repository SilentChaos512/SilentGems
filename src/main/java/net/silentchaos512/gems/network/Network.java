package net.silentchaos512.gems.network;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;
import net.silentchaos512.gems.SilentGems;

import java.util.Objects;

public final class Network {
    private static final ResourceLocation NAME = SilentGems.getId("network");

    static SimpleChannel channel;
    static {
        channel = NetworkRegistry.ChannelBuilder.named(NAME)
                .clientAcceptedVersions(s -> Objects.equals(s, "1"))
                .serverAcceptedVersions(s -> Objects.equals(s, "1"))
                .networkProtocolVersion(() -> "1")
                .simpleChannel();

        channel.messageBuilder(GeneralSyncPacket.class, 1)
                .decoder(GeneralSyncPacket::fromBytes)
                .encoder(GeneralSyncPacket::toBytes)
                .consumer(GeneralSyncPacket::handle)
                .add();
    }

    private Network() {}

    public static void init() { }
}
