package net.silentchaos512.gems.network;

import net.minecraft.network.PacketBuffer;
import net.silentchaos512.gems.lib.chaosbuff.ChaosBuffSerializers;
import net.silentchaos512.gems.lib.chaosbuff.IChaosBuff;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Used to send soul data (soul gems) to the client. Since the information is based on the world
 * seed, it is generated when the server is started. So for dedicated servers, we need to send this
 * information to the client.
 */
public class SyncChaosBuffsPacket {
    private List<IChaosBuff> buffs;

    public SyncChaosBuffsPacket() {
    }

    public SyncChaosBuffsPacket(Collection<IChaosBuff> buffsIn) {
        this.buffs = new ArrayList<>(buffsIn);
    }

    public static SyncChaosBuffsPacket fromBytes(PacketBuffer buffer) {
        SyncChaosBuffsPacket packet = new SyncChaosBuffsPacket();
        packet.buffs = new ArrayList<>();
        int count = buffer.readVarInt();

        for (int i = 0; i < count; ++i) {
            packet.buffs.add(ChaosBuffSerializers.read(buffer));
        }

        return packet;
    }

    public void toBytes(PacketBuffer buffer) {
        buffer.writeVarInt(this.buffs.size());
        this.buffs.forEach(buff -> ChaosBuffSerializers.write(buff, buffer));
    }

    @SuppressWarnings("AssignmentOrReturnOfFieldWithMutableType")
    public List<IChaosBuff> getBuffs() {
        return this.buffs;
    }
}
