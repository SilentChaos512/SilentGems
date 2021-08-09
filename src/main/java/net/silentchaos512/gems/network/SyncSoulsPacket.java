package net.silentchaos512.gems.network;

import net.minecraft.network.FriendlyByteBuf;
import net.silentchaos512.gems.soul.Soul;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Used to send soul data (soul gems) to the client. Since the information is based on the world
 * seed, it is generated when the server is started. So for dedicated servers, we need to send this
 * information to the client.
 */
public class SyncSoulsPacket extends LoginPacket {
    private List<Soul> souls;

    public SyncSoulsPacket() {
        this(Soul.getValues());
    }

    public SyncSoulsPacket(Collection<Soul> soulsIn) {
        this.souls = new ArrayList<>(soulsIn);
    }

    public static SyncSoulsPacket fromBytes(FriendlyByteBuf buf) {
        SyncSoulsPacket packet = new SyncSoulsPacket();
        packet.souls = new ArrayList<>();
        int count = buf.readVarInt();

        for (int i = 0; i < count; ++i) {
            packet.souls.add(Soul.read(buf));
        }

        return packet;
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeVarInt(this.souls.size());
        this.souls.forEach(soul -> soul.write(buf));
    }

    @SuppressWarnings("AssignmentOrReturnOfFieldWithMutableType")
    public List<Soul> getSouls() {
        return this.souls;
    }
}
