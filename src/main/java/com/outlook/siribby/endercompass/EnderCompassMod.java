package com.outlook.siribby.endercompass;

import com.outlook.siribby.endercompass.network.EnderCompassProxy;
import com.outlook.siribby.endercompass.network.MessageGetStrongholdPos;
import com.outlook.siribby.endercompass.network.MessageSetStrongholdPos;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.WeightedRandomChestContent;
import net.minecraftforge.common.ChestGenHooks;
import net.minecraftforge.common.MinecraftForge;

@Mod(modid = "endercompass", name = "Ender Compass", version = "@VERSION@", acceptedMinecraftVersions = "*")
public class EnderCompassMod {
    public static final Item ender_compass = new Item().setUnlocalizedName("compassEnd").setCreativeTab(CreativeTabs.tabTools);

    @SidedProxy(clientSide = "com.outlook.siribby.endercompass.client.EnderCompassClient", serverSide = "com.outlook.siribby.endercompass.network.EnderCompassProxy")
    public static EnderCompassProxy proxy;
    public static SimpleNetworkWrapper network;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        GameRegistry.registerItem(ender_compass, "ender_compass");
        GameRegistry.addRecipe(new ItemStack(ender_compass), " E ", "ECE", " E ", 'E', Items.ender_eye, 'C', Items.compass);
        ChestGenHooks.addItem(ChestGenHooks.STRONGHOLD_LIBRARY, new WeightedRandomChestContent(ender_compass, 0, 1, 1, 1));

        network = NetworkRegistry.INSTANCE.newSimpleChannel("endercompass");
        network.registerMessage(new MessageGetStrongholdPos(), MessageGetStrongholdPos.class, 0, Side.SERVER);
        network.registerMessage(new MessageSetStrongholdPos(), MessageSetStrongholdPos.class, 1, Side.CLIENT);

        MinecraftForge.EVENT_BUS.register(proxy);

        proxy.preInit();
    }
}
