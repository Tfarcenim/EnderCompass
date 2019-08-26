package io.github.mribby.endercompass.network;

import io.github.mribby.endercompass.EnderCompass;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;

public class PacketHandler {

  public static SimpleChannel INSTANCE;

  public static void registerMessages(String channelName) {
    INSTANCE = NetworkRegistry.newSimpleChannel(new ResourceLocation(EnderCompass.MODID, channelName), () -> "1.0", s -> true, s -> true);
    INSTANCE.registerMessage(0, SPacketSetStrongholdPos.class,
            SPacketSetStrongholdPos::encode,
            SPacketSetStrongholdPos::new,
            SPacketSetStrongholdPos::handle);
    INSTANCE.registerMessage(1, CPacketGetStrongholdPos.class,
            CPacketGetStrongholdPos::encode,
            CPacketGetStrongholdPos::new,
            CPacketGetStrongholdPos::handle);
  }

}
