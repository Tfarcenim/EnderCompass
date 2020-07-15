package io.github.mribby.endercompass;

import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod(EnderCompass.MODID)
@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)

public class EnderCompass {

	public static final String MODID = "endercompass";

	public static Item ENDER_COMPASS;

	@SubscribeEvent
	public static void items(final RegistryEvent.Register<Item> event) {
		event.getRegistry().register(ENDER_COMPASS = new ItemEnderCompass(new Item.Properties().group(ItemGroup.TOOLS))
						.setRegistryName("ender_compass"));
	}
}
