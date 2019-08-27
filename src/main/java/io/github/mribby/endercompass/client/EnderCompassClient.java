package io.github.mribby.endercompass.client;

import io.github.mribby.endercompass.EnderCompass;
import io.github.mribby.endercompass.network.CMessageGetStrongholdPos;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;

@Mod.EventBusSubscriber(value = Side.CLIENT,modid = EnderCompass.ID)
public class EnderCompassClient {
  private static BlockPos strongholdPos;
  private static World strongholdWorld;
  private static final Minecraft mc = Minecraft.getMinecraft();

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
    EnderCompass.network.sendToServer(new CMessageGetStrongholdPos());
  }

  @SubscribeEvent
  public static void onModelRegistry(ModelRegistryEvent event) {
    EnderCompass.ENDER_COMPASS.addPropertyOverride(new ResourceLocation("angle"), new EnderCompassAngleGetter());
    ModelLoader.setCustomModelResourceLocation(EnderCompass.ENDER_COMPASS, 0, new ModelResourceLocation("endercompass:ender_compass", "inventory"));
  }

  @SubscribeEvent
  public static void onClientTick(TickEvent.ClientTickEvent event) {
    if (hasEnderCompass()) {
      if (mc.world != strongholdWorld) {
        resetStrongholdPos();
      }
    }
  }
}
