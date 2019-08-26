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

  public EnderCompass() {
    ModLoadingContext.get().registerConfig(ModConfig.Type.SERVER, SERVER_SPEC);
  }


  public static final EnderCompassConfig SERVER;
  public static final ForgeConfigSpec SERVER_SPEC;

  static {
    final Pair<EnderCompassConfig, ForgeConfigSpec> specPair = new ForgeConfigSpec.Builder().configure(EnderCompassConfig::new);
    SERVER_SPEC = specPair.getRight();
    SERVER = specPair.getLeft();
  }

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


  // TODO: config
  public static class EnderCompassConfig {

    public static ForgeConfigSpec.BooleanValue checkInventory;


    public EnderCompassConfig(ForgeConfigSpec.Builder builder) {
      builder.push("general");
      checkInventory = builder
              .comment("Check the inventory for compass before changing direction")
              .define("check inventory", false);
    }

  }

  @SuppressWarnings("ConstantConditions")
  public static boolean containsCompass(IInventory inventory) {
    return EnderCompassConfig.checkInventory.get() && IntStream.range(0, inventory.getSizeInventory()).mapToObj(inventory::getStackInSlot).anyMatch(stack -> !stack.isEmpty() && stack.getItem() == ENDER_COMPASS);
  }

}
