package io.github.mribby.endercompass.client;

import io.github.mribby.endercompass.EnderCompassMod;
import io.github.mribby.endercompass.network.EnderCompassProxy;
import io.github.mribby.endercompass.network.MessageGetStrongholdPos;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class EnderCompassClient extends EnderCompassProxy {
    private static BlockPos strongholdPos;
    private static World strongholdWorld;

    public static Minecraft getMinecraft() {
        return FMLClientHandler.instance().getClient();
    }

    public static WorldClient getWorld() {
        return getMinecraft().theWorld;
    }

    public static EntityPlayerSP getPlayer() {
        return getMinecraft().thePlayer;
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
        EnderCompassMod.network.sendToServer(new MessageGetStrongholdPos());
    }

    @Override
    public void preInit() {
        EnderCompassMod.ENDER_COMPASS.addPropertyOverride(new ResourceLocation("angle"), new EnderCompassAngleGetter());
        ModelLoader.setCustomModelResourceLocation(EnderCompassMod.ENDER_COMPASS, 0, new ModelResourceLocation("endercompass:ender_compass", "inventory"));
    }

    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event) {
        if (hasEnderCompass()) {
            if (getWorld() != strongholdWorld) {
                resetStrongholdPos();
            }
        }
    }
}
