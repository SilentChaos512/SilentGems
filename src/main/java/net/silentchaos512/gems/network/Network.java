package net.silentchaos512.gems.network;

import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.HandshakeHandler;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;
import net.silentchaos512.gems.GemsBase;
import net.silentchaos512.gems.soul.Soul;

import java.util.Objects;

public final class Network {
    private static final ResourceLocation NAME = GemsBase.getId("network");

    public static SimpleChannel channel;

    public static final String NET_VERSION = "2";

    static {
        channel = NetworkRegistry.ChannelBuilder.named(NAME)
                .clientAcceptedVersions(s -> Objects.equals(s, NET_VERSION))
                .serverAcceptedVersions(s -> Objects.equals(s, NET_VERSION))
                .networkProtocolVersion(() -> NET_VERSION)
                .simpleChannel();

        channel.messageBuilder(SyncSoulsPacket.class, 1)
                .loginIndex(LoginPacket::getLoginIndex, LoginPacket::setLoginIndex)
                .decoder(SyncSoulsPacket::fromBytes)
                .encoder(SyncSoulsPacket::toBytes)
                .markAsLoginPacket()
                .consumerMainThread(HandshakeHandler.biConsumerFor((hh, msg, ctx) -> {
                    Soul.handleSyncPacket(msg, ctx);
                    channel.reply(new LoginPacket.Reply(), ctx.get());
                }))
                .add();
        channel.messageBuilder(LoginPacket.Reply.class, 2)
                .loginIndex(LoginPacket::getLoginIndex, LoginPacket::setLoginIndex)
                .decoder(buffer -> new LoginPacket.Reply())
                .encoder((msg, buffer) -> {})
                .consumerMainThread(HandshakeHandler.indexFirst((hh, msg, ctx) -> msg.handle(ctx)))
                .add();
    }

    private Network() {}

    public static void init() { }
}
