package com.outlook.siribby.endercompass.network;

import com.outlook.siribby.endercompass.EnderCompassMod;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayerMP;

public class MessageGetStrongholdPos implements IMessage, IMessageHandler<MessageGetStrongholdPos, IMessage> {
    public MessageGetStrongholdPos() {}

    @Override
    public void toBytes(ByteBuf buf) {}

    @Override
    public void fromBytes(ByteBuf buf) {}

    @Override
    public IMessage onMessage(MessageGetStrongholdPos message, MessageContext ctx) {
        final EntityPlayerMP player = ctx.getServerHandler().playerEntity;
        if (EnderCompassMod.containsCompass(player.inventory)) {
            final WorldServer world = (WorldServer) player.worldObj;
            world.addScheduledTask(new Runnable() {
                @Override
                public void run() {
                    BlockPos pos = world.getChunkProvider().getStrongholdGen(world, "Stronghold", new BlockPos(player));
                    if (pos != null) {
                        EnderCompassMod.network.sendTo(new MessageSetStrongholdPos(pos), player);
                    }
                }
            });
        }
        return null;
    }
}
