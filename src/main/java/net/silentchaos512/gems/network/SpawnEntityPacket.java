package net.silentchaos512.gems.network;

import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;
import net.minecraftforge.fml.LogicalSidedProvider;
import net.minecraftforge.fml.common.registry.IEntityAdditionalSpawnData;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.Optional;
import java.util.UUID;
import java.util.function.Supplier;

/**
 * More or less a straight copy from {@link net.minecraftforge.fml.network.FMLPlayMessages.SpawnEntity}.
 */
public class SpawnEntityPacket {
    private final Entity entity;
    private final int typeId;
    private final int entityId;
    private final UUID uuid;
    private final double posX, posY, posZ;
    private final byte pitch, yaw, headYaw;
    private final int velX, velY, velZ;
    private final PacketBuffer buf;

    public SpawnEntityPacket(Entity e) {
        this.entity = e;
        this.typeId = Registry.ENTITY_TYPE.getId(e.getType());
        this.entityId = e.getId();
        this.uuid = e.getUUID();
        this.posX = e.getX();
        this.posY = e.getY();
        this.posZ = e.getZ();
        this.pitch = (byte) MathHelper.floor(e.xRot * 256.0F / 360.0F);
        this.yaw = (byte) MathHelper.floor(e.yRot * 256.0F / 360.0F);
        this.headYaw = (byte) (e.getYHeadRot() * 256.0F / 360.0F);
        Vector3d vec3d = e.getDeltaMovement();
        double d1 = MathHelper.clamp(vec3d.x, -3.9D, 3.9D);
        double d2 = MathHelper.clamp(vec3d.y, -3.9D, 3.9D);
        double d3 = MathHelper.clamp(vec3d.z, -3.9D, 3.9D);
        this.velX = (int) (d1 * 8000.0D);
        this.velY = (int) (d2 * 8000.0D);
        this.velZ = (int) (d3 * 8000.0D);
        this.buf = null;
    }

    private SpawnEntityPacket(int typeId, int entityId, UUID uuid, double posX, double posY, double posZ, byte pitch, byte yaw, byte headYaw, int velX, int velY, int velZ, PacketBuffer buf) {
        this.entity = null;
        this.typeId = typeId;
        this.entityId = entityId;
        this.uuid = uuid;
        this.posX = posX;
        this.posY = posY;
        this.posZ = posZ;
        this.pitch = pitch;
        this.yaw = yaw;
        this.headYaw = headYaw;
        this.velX = velX;
        this.velY = velY;
        this.velZ = velZ;
        this.buf = buf;
    }

    public static void encode(SpawnEntityPacket msg, PacketBuffer buf) {
        buf.writeVarInt(msg.typeId);
        buf.writeInt(msg.entityId);
        buf.writeLong(msg.uuid.getMostSignificantBits());
        buf.writeLong(msg.uuid.getLeastSignificantBits());
        buf.writeDouble(msg.posX);
        buf.writeDouble(msg.posY);
        buf.writeDouble(msg.posZ);
        buf.writeByte(msg.pitch);
        buf.writeByte(msg.yaw);
        buf.writeByte(msg.headYaw);
        buf.writeShort(msg.velX);
        buf.writeShort(msg.velY);
        buf.writeShort(msg.velZ);
        if (msg.entity instanceof IEntityAdditionalSpawnData) {
            ((IEntityAdditionalSpawnData) msg.entity).writeSpawnData(buf);
        }
    }

    public static SpawnEntityPacket decode(PacketBuffer buf) {
        return new SpawnEntityPacket(
                buf.readVarInt(),
                buf.readInt(),
                new UUID(buf.readLong(), buf.readLong()),
                buf.readDouble(), buf.readDouble(), buf.readDouble(),
                buf.readByte(), buf.readByte(), buf.readByte(),
                buf.readShort(), buf.readShort(), buf.readShort(),
                buf
        );
    }

    public static void handle(SpawnEntityPacket msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            EntityType<?> type = Registry.ENTITY_TYPE.byId(msg.typeId);
            if (type == null) {
                throw new RuntimeException(String.format("Could not spawn entity (id %d) with unknown type at (%f, %f, %f)", msg.entityId, msg.posX, msg.posY, msg.posZ));
            }

            Optional<World> world = LogicalSidedProvider.CLIENTWORLD.get(ctx.get().getDirection().getReceptionSide());
            Entity e = world.map(type::create).orElse(null);
            if (e == null) {
                return;
            }

            e.setPacketCoordinates(msg.posX, msg.posY, msg.posZ);
            e.absMoveTo(msg.posX, msg.posY, msg.posZ, (msg.yaw * 360) / 256.0F, (msg.pitch * 360) / 256.0F);
            e.setYHeadRot((msg.headYaw * 360) / 256.0F);
            e.setYBodyRot((msg.headYaw * 360) / 256.0F);

            e.setId(msg.entityId);
            e.setUUID(msg.uuid);
            world.filter(ClientWorld.class::isInstance).ifPresent(w -> ((ClientWorld) w).putNonPlayerEntity(msg.entityId, e));
            e.lerpMotion(msg.velX / 8000.0, msg.velY / 8000.0, msg.velZ / 8000.0);
            if (e instanceof IEntityAdditionalSpawnData) {
                ((IEntityAdditionalSpawnData) e).readSpawnData(msg.buf);
            }
        });
        ctx.get().setPacketHandled(true);
    }
}
