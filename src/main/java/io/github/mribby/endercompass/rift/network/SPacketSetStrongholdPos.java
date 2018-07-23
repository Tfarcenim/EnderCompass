package io.github.mribby.endercompass.rift.network;

import io.github.mribby.endercompass.rift.client.EnderCompassClient;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;
import net.minecraft.util.math.BlockPos;

import java.io.IOException;

public class SPacketSetStrongholdPos implements Packet<INetHandlerPlayClient> {
    private int x;
    private int y;
    private int z;

    public SPacketSetStrongholdPos() {}

    public SPacketSetStrongholdPos(BlockPos position) {
        x = position.getX();
        y = position.getY();
        z = position.getZ();
    }

    @Override
    public void readPacketData(PacketBuffer buf) throws IOException {
        x = buf.readInt();
        y = buf.readInt();
        z = buf.readInt();
    }

    @Override
    public void writePacketData(PacketBuffer buf) throws IOException {
        buf.writeInt(x);
        buf.writeInt(y);
        buf.writeInt(z);
    }

    @Override
    public void processPacket(INetHandlerPlayClient connection) {
        BlockPos position = new BlockPos(x, y, z);
        EnderCompassClient.getMinecraft().addScheduledTask(() -> EnderCompassClient.setStrongholdPos(position));
    }
}
