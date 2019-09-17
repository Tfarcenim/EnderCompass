package io.github.mribby.endercompass;

import io.github.mribby.endercompass.network.PacketHandler;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.registries.ObjectHolder;
import org.apache.commons.lang3.tuple.Pair;

import java.util.stream.IntStream;

@Mod(EnderCompass.MODID)
public class EnderCompass {

  public static final String MODID = "endercompass";

  @ObjectHolder(MODID+":ender_compass")
  public static final Item ENDER_COMPASS = null;

  @Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
  public static class RegistryEvents {

    @SubscribeEvent
    public static void items(final RegistryEvent.Register<Item> event) {
      event.getRegistry().register(new ItemEnderCompass(new Item.Properties().group(ItemGroup.TOOLS))
              .setRegistryName("ender_compass"));
    }
    @SubscribeEvent
    public static void registerPlayPackets(FMLCommonSetupEvent event) {
      PacketHandler.registerMessages(MODID);
    }
  }

  @SuppressWarnings("ConstantConditions")
  public static boolean containsCompass(IInventory inventory) {
    return IntStream.range(0, inventory.getSizeInventory()).mapToObj(inventory::getStackInSlot).anyMatch(stack -> stack.getItem() == ENDER_COMPASS);
  }

}
