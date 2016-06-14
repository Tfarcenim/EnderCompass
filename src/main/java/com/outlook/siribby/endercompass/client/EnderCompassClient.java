package com.outlook.siribby.endercompass.client;

import com.outlook.siribby.endercompass.EnderCompassMod;
import com.outlook.siribby.endercompass.network.EnderCompassProxy;
import com.outlook.siribby.endercompass.network.MessageGetStrongholdPos;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class EnderCompassClient extends EnderCompassProxy {
    public static BlockPos strongholdPos;
    private static World strongholdWorld;

    public static Minecraft minecraft = FMLClientHandler.instance().getClient();

    @Override
    public void preInit() {
        ModelLoader.setCustomModelResourceLocation(EnderCompassMod.ender_compass, 0, new ModelResourceLocation("endercompass:ender_compass", "inventory"));
    }

    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event) {
        if (minecraft.theWorld != strongholdWorld && minecraft.thePlayer != null) {
            strongholdPos = null;
            strongholdWorld = minecraft.theWorld;
            EnderCompassMod.network.sendToServer(new MessageGetStrongholdPos());
        }
    }
}
