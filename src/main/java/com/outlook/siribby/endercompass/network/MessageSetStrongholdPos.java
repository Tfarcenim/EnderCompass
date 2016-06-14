package com.outlook.siribby.endercompass.network;

import com.outlook.siribby.endercompass.client.EnderCompassClient;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;

public class MessageSetStrongholdPos implements IMessage, IMessageHandler<MessageSetStrongholdPos, IMessage> {
    private int x;
    private int y;
    private int z;

    public MessageSetStrongholdPos() {}

    public MessageSetStrongholdPos(BlockPos position) {
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
    public IMessage onMessage(final MessageSetStrongholdPos message, MessageContext ctx) {
        EnderCompassClient.minecraft.addScheduledTask(new Runnable() {
            @Override
            public void run() {
                EnderCompassClient.strongholdPos = new BlockPos(message.x, message.y, message.z);
            }
        });
        return null;
    }
}
