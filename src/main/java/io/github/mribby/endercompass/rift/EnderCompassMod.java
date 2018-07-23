package io.github.mribby.endercompass.rift;

import io.github.mribby.endercompass.rift.network.CPacketGetStrongholdPos;
import io.github.mribby.endercompass.rift.network.SPacketSetStrongholdPos;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.network.EnumPacketDirection;
import net.minecraft.util.ResourceLocation;
import org.dimdev.rift.listener.ItemAdder;
import org.dimdev.rift.listener.PacketAdder;

public class EnderCompassMod implements ItemAdder, PacketAdder {
    public static final Item ENDER_COMPASS = new ItemEnderCompass(new Item.Builder().group(ItemGroup.TOOLS));

    @Override
    public void registerItems() {
        Item.registerItem(new ResourceLocation("endercompass", "ender_compass"), ENDER_COMPASS);
    }

    @Override
    public void registerHandshakingPackets(PacketRegistrationReceiver receiver) {}

    @Override
    public void registerPlayPackets(PacketRegistrationReceiver receiver) {
        receiver.registerPacket(EnumPacketDirection.SERVERBOUND, CPacketGetStrongholdPos.class);
        receiver.registerPacket(EnumPacketDirection.CLIENTBOUND, SPacketSetStrongholdPos.class);
    }

    @Override
    public void registerStatusPackets(PacketRegistrationReceiver receiver) {}

    @Override
    public void registerLoginPackets(PacketRegistrationReceiver receiver) {}

    // TODO: config
    public static class EnderCompassConfig {
        public static boolean checkInventory = false;
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
