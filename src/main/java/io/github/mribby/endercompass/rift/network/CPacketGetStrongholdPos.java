package io.github.mribby.endercompass.rift.network;

import io.github.mribby.endercompass.rift.EnderCompassMod;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.*;
import net.minecraft.network.play.INetHandlerPlayServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldServer;

import java.io.IOException;

public class CPacketGetStrongholdPos implements Packet<INetHandlerPlayServer> {
    @Override
    public void readPacketData(PacketBuffer buf) throws IOException {}

    @Override
    public void writePacketData(PacketBuffer buf) throws IOException {}

    @Override
    public void processPacket(INetHandlerPlayServer connection) {
        EntityPlayerMP player = ((NetHandlerPlayServer) connection).player;
        WorldServer world = player.getServerWorld();
        PacketThreadUtil.checkThreadAndEnqueue(this, connection, world);
        if (EnderCompassMod.containsCompass(player.inventory)) {
            world.addScheduledTask(() -> {
                BlockPos pos = world.getChunkProvider().findNearestStructure(world, "Stronghold", new BlockPos(player), 100);
                if (pos != null) {
                    player.connection.sendPacket(new SPacketSetStrongholdPos(pos));
                }
            });
        }
    }
}
