package io.github.mribby.endercompass.client;

import io.github.mribby.endercompass.EnderCompass;
import io.github.mribby.endercompass.network.CPacketGetStrongholdPos;
import io.github.mribby.endercompass.network.PacketHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.network.NetworkDirection;

@Mod.EventBusSubscriber(value = Dist.CLIENT)
public class EnderCompassClient {

  private static BlockPos strongholdPos;
  private static World strongholdWorld;

  private static final Minecraft mc = Minecraft.getInstance();

  public static boolean hasEnderCompass() {
    return mc.player != null && EnderCompass.containsCompass(mc.player.inventory);
  }

  public static BlockPos getStrongholdPos() {
    return hasEnderCompass() ? strongholdPos : null;
  }

  public static void setStrongholdPos(BlockPos pos) {
    strongholdPos = pos;
  }

  public static void resetStrongholdPos() {
    strongholdPos = null;
    strongholdWorld = mc.world;
    PacketHandler.INSTANCE.sendTo(new CPacketGetStrongholdPos(), mc.player.connection.getNetworkManager(), NetworkDirection.PLAY_TO_SERVER);
  }

  @SubscribeEvent
  public static void clientTick(TickEvent.ClientTickEvent e) {
    if (hasEnderCompass()) {
      if (mc.world != strongholdWorld) {
        resetStrongholdPos();
      }
    }
  }
}
