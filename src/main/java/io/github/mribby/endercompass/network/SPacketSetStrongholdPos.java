package io.github.mribby.endercompass.network;

import io.github.mribby.endercompass.client.EnderCompassClient;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class SPacketSetStrongholdPos {
  private int x;
  private int y;
  private int z;

  public SPacketSetStrongholdPos() {}

  public SPacketSetStrongholdPos(BlockPos position) {
    x = position.getX();
    y = position.getY();
    z = position.getZ();
  }

  public SPacketSetStrongholdPos(PacketBuffer buf) {
    x = buf.readInt();
    y = buf.readInt();
    z = buf.readInt();
  }

  public void encode(PacketBuffer buf) {
    buf.writeInt(x);
    buf.writeInt(y);
    buf.writeInt(z);
  }

  public void handle(Supplier<NetworkEvent.Context> ctx) {
    BlockPos position = new BlockPos(x, y, z);
    ctx.get().enqueueWork(() -> EnderCompassClient.setStrongholdPos(position));
    ctx.get().setPacketHandled(true);
  }
}
