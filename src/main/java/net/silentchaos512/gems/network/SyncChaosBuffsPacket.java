package net.silentchaos512.gems.network;

import net.minecraft.network.PacketBuffer;
import net.silentchaos512.gems.lib.chaosbuff.ChaosBuffManager;
import net.silentchaos512.gems.lib.chaosbuff.ChaosBuffSerializers;
import net.silentchaos512.gems.lib.chaosbuff.IChaosBuff;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class SyncChaosBuffsPacket extends LoginPacket {
    private List<IChaosBuff> buffs;

    public SyncChaosBuffsPacket() {
        this(ChaosBuffManager.getValues());
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
