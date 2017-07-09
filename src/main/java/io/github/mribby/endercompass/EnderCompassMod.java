package io.github.mribby.endercompass;

import io.github.mribby.endercompass.network.EnderCompassProxy;
import io.github.mribby.endercompass.network.MessageGetStrongholdPos;
import io.github.mribby.endercompass.network.MessageSetStrongholdPos;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

@Mod(modid = EnderCompassMod.ID, name = EnderCompassMod.NAME, version = EnderCompassMod.VERSION, updateJSON = EnderCompassMod.UPDATE_JSON_URL, acceptedMinecraftVersions = EnderCompassMod.MINECRAFT_VERSIONS)
public class EnderCompassMod {
    public static final String ID = "endercompass";
    public static final String NAME = "Ender Compass";
    public static final String VERSION = "@VERSION@";
    public static final String UPDATE_JSON_URL = "https://gist.github.com/MrIbby/174385130d65a4da3d9d6c472ac47114/raw";
    public static final String MINECRAFT_VERSIONS = "*";

    public static final Item ENDER_COMPASS = new ItemEnderCompass().setUnlocalizedName("compassEnd").setCreativeTab(CreativeTabs.TOOLS).setRegistryName("ender_compass");

    @SidedProxy(clientSide = "io.github.mribby.endercompass.client.EnderCompassClient", serverSide = "io.github.mribby.endercompass.network.EnderCompassProxy")
    public static EnderCompassProxy proxy;
    public static SimpleNetworkWrapper network;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {

        //todo: ChestGenHooks.addItem(ChestGenHooks.STRONGHOLD_LIBRARY, new WeightedRandomChestContent(ender_compass, 0, 1, 1, 1));

        network = NetworkRegistry.INSTANCE.newSimpleChannel("endercompass");
        network.registerMessage(new MessageGetStrongholdPos(), MessageGetStrongholdPos.class, 0, Side.SERVER);
        network.registerMessage(new MessageSetStrongholdPos(), MessageSetStrongholdPos.class, 1, Side.CLIENT);

        MinecraftForge.EVENT_BUS.register(this);

        proxy.preInit();
    }

    @SubscribeEvent
    public void onRegister(RegistryEvent.Register<Item> event) {
        event.getRegistry().register(ENDER_COMPASS);
    }

    @Mod.EventBusSubscriber
    @Config(modid = EnderCompassMod.ID)
    public static class EnderCompassConfig {

        @Config.LangKey("endercompass.configgui.checkInventory")
        @Config.Comment("Set this to true if the player should have an ender compass in their inventory in order for it to work")
        public static boolean checkInventory = false;

        @SubscribeEvent
        public static void onConfigChanged(ConfigChangedEvent.OnConfigChangedEvent event) {
            if (event.getModID().equals(EnderCompassMod.ID))
                ConfigManager.sync(EnderCompassMod.ID, Config.Type.INSTANCE);
        }

    }

    public static boolean containsCompass(IInventory inventory) {
        if (EnderCompassConfig.checkInventory) {
            for (int slot = 0; slot < inventory.getSizeInventory(); slot++) {
                ItemStack stack = inventory.getStackInSlot(slot);
                if (!stack.isEmpty() && stack.getItem() == ENDER_COMPASS) {
                    return true;
                }
            }
            return false;
        } else {
            return true;
        }
    }

}
