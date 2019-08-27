package io.github.mribby.endercompass.network;

import io.github.mribby.endercompass.client.EnderCompassClient;
import net.minecraft.client.Minecraft;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;

public class SMessageSetStrongholdPos implements IMessage, IMessageHandler<SMessageSetStrongholdPos, IMessage> {
    private int x;
    private int y;
    private int z;

    public SMessageSetStrongholdPos() {}

    public SMessageSetStrongholdPos(BlockPos position) {
        x = position.getX();
        y = position.getY();
        z = position.getZ();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(x);
        buf.writeInt(y);
        buf.writeInt(z);
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        x = buf.readInt();
        y = buf.readInt();
        z = buf.readInt();
    }

    @Override
    public IMessage onMessage(SMessageSetStrongholdPos message, MessageContext ctx) {
        final BlockPos position = new BlockPos(message.x, message.y, message.z);
        Minecraft.getMinecraft().addScheduledTask(() -> EnderCompassClient.setStrongholdPos(position));
        return null;
    }
}
