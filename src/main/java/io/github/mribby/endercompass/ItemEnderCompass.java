package io.github.mribby.endercompass;

import io.github.mribby.endercompass.client.EnderCompassAngleGetter;
import io.github.mribby.endercompass.client.EnderCompassClient;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

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

    @Override
    public void addInformation(ItemStack stack, @Nullable World p_77624_2_, List<ITextComponent> list, ITooltipFlag p_77624_4_) {
        if (EnderCompassClient.getStrongholdPos() != null)
        list.add(new StringTextComponent( EnderCompassClient.getStrongholdPos().toString()));
    }
}
