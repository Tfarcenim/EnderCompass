package io.github.mribby.endercompass.network;

import io.github.mribby.endercompass.EnderCompass;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class CPacketGetStrongholdPos {

  public CPacketGetStrongholdPos(){}

  public CPacketGetStrongholdPos(PacketBuffer buf) {}

  public void encode(PacketBuffer buf) {}

  public void handle(Supplier<NetworkEvent.Context> ctx) {
    ServerPlayerEntity player = ctx.get().getSender();
    ServerWorld world = player.getServerWorld();
    if (EnderCompass.containsCompass(player.inventory)) {
      ctx.get().enqueueWork(() -> {
        BlockPos pos = (world.findNearestStructure("Stronghold", new BlockPos(player),1000,true));
        if (pos != null) {
          PacketHandler.INSTANCE.sendToServer(new SPacketSetStrongholdPos(pos));
        }
      });
    }
    ctx.get().setPacketHandled(true);
  }
}
