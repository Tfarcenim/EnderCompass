package io.github.mribby.endercompass.rift;

import io.github.mribby.endercompass.rift.client.EnderCompassAngleGetter;
import io.github.mribby.endercompass.rift.client.EnderCompassClient;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

public class ItemEnderCompass extends Item {
    public ItemEnderCompass(Builder builder) {
        super(builder);
        addPropertyOverride(new ResourceLocation("angle"), new EnderCompassAngleGetter()); // TODO: client-side only?
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand) {
        if (world.isRemote) {
            EnderCompassClient.resetStrongholdPos();
        }
        return new ActionResult<>(EnumActionResult.SUCCESS, player.getHeldItem(hand));
    }
}
