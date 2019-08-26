package io.github.mribby.endercompass;

import io.github.mribby.endercompass.client.EnderCompassAngleGetter;
import io.github.mribby.endercompass.client.EnderCompassClient;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

public class ItemEnderCompass extends Item {
    public ItemEnderCompass(Properties builder) {
        super(builder);
        addPropertyOverride(new ResourceLocation("angle"), new EnderCompassAngleGetter()); // TODO: client-side only?
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World world, PlayerEntity player, Hand hand) {
        if (world.isRemote) {
            EnderCompassClient.resetStrongholdPos();
        }
        return new ActionResult<>(ActionResultType.SUCCESS, player.getHeldItem(hand));
    }
}
