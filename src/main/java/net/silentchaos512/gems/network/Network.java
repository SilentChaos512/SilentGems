package net.silentchaos512.gems.network;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.network.FMLHandshakeHandler;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;
import net.silentchaos512.gems.SilentGems;
import net.silentchaos512.gems.lib.chaosbuff.ChaosBuffManager;
import net.silentchaos512.gems.lib.soul.Soul;

import java.util.Objects;

public final class Network {
    private static final ResourceLocation NAME = SilentGems.getId("network");

    public static SimpleChannel channel;
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
        channel.messageBuilder(SyncSoulsPacket.class, 2)
                .loginIndex(LoginPacket::getLoginIndex, LoginPacket::setLoginIndex)
                .decoder(SyncSoulsPacket::fromBytes)
                .encoder(SyncSoulsPacket::toBytes)
                .markAsLoginPacket()
                .consumer(FMLHandshakeHandler.biConsumerFor((hh, msg, ctx) -> {
                    Soul.handleSyncPacket(msg, ctx);
                    channel.reply(new LoginPacket.Reply(), ctx.get());
                }))
                .add();
        channel.messageBuilder(SyncChaosBuffsPacket.class, 3)
                .loginIndex(LoginPacket::getLoginIndex, LoginPacket::setLoginIndex)
                .decoder(SyncChaosBuffsPacket::fromBytes)
                .encoder(SyncChaosBuffsPacket::toBytes)
                .markAsLoginPacket()
                .consumer(FMLHandshakeHandler.biConsumerFor((hh, msg, ctx) -> {
                    ChaosBuffManager.handlePacket(msg, ctx);
                    channel.reply(new LoginPacket.Reply(), ctx.get());
                }))
                .add();
        channel.messageBuilder(SpawnEntityPacket.class, 4)
                .decoder(SpawnEntityPacket::decode)
                .encoder(SpawnEntityPacket::encode)
                .consumer(SpawnEntityPacket::handle)
                .add();
        channel.messageBuilder(LoginPacket.Reply.class, 5)
                .loginIndex(LoginPacket::getLoginIndex, LoginPacket::setLoginIndex)
                .decoder(buffer -> new LoginPacket.Reply())
                .encoder((msg, buffer) -> {})
                .consumer(FMLHandshakeHandler.indexFirst((hh, msg, ctx) -> msg.handle(ctx)))
                .add();
    }

    private Network() {}

    public static void init() { }
}
