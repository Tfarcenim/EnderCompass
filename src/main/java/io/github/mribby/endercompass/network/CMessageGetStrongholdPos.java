package io.github.mribby.endercompass.network;

import io.github.mribby.endercompass.EnderCompass;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayerMP;

public class CMessageGetStrongholdPos implements IMessage, IMessageHandler<CMessageGetStrongholdPos, IMessage> {
    public CMessageGetStrongholdPos() {}

    @Override
    public void toBytes(ByteBuf buf) {}

    @Override
    public void fromBytes(ByteBuf buf) {}

    @Override
    public IMessage onMessage(CMessageGetStrongholdPos message, MessageContext ctx) {
        final EntityPlayerMP player = ctx.getServerHandler().player;
        if (EnderCompass.containsCompass(player.inventory)) {
            final WorldServer world = (WorldServer) player.world;
            world.addScheduledTask(() -> {
                BlockPos pos = world.getChunkProvider().getNearestStructurePos(world, "Stronghold", new BlockPos(player), false);
                if (pos != null) {
                    EnderCompass.network.sendTo(new SMessageSetStrongholdPos(pos), player);
                }
            });
        }
        return null;
    }
}
