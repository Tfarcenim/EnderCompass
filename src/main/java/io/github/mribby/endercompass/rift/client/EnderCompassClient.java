package io.github.mribby.endercompass.rift.client;

import io.github.mribby.endercompass.rift.EnderCompassMod;
import io.github.mribby.endercompass.rift.network.CPacketGetStrongholdPos;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.dimdev.rift.listener.ClientTickable;

public class EnderCompassClient implements ClientTickable {
    private static BlockPos strongholdPos;
    private static World strongholdWorld;

    public static Minecraft getMinecraft() {
        return Minecraft.getMinecraft();
    }

    public static WorldClient getWorld() {
        return getMinecraft().world;
    }

    public static EntityPlayerSP getPlayer() {
        return getMinecraft().player;
    }

    public static boolean hasEnderCompass() {
        return getPlayer() != null && EnderCompassMod.containsCompass(getPlayer().inventory);
    }

    public static BlockPos getStrongholdPos() {
        return hasEnderCompass() ? strongholdPos : null;
    }

    public static void setStrongholdPos(BlockPos pos) {
        strongholdPos = pos;
    }

    public static void resetStrongholdPos() {
        strongholdPos = null;
        strongholdWorld = getWorld();
        getMinecraft().getConnection().sendPacket(new CPacketGetStrongholdPos());
    }

    @Override
    public void clientTick() {
        if (hasEnderCompass()) {
            if (getWorld() != strongholdWorld) {
                resetStrongholdPos();
            }
        }
    }
}
