package io.github.mribby.endercompass;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

import javax.annotation.Nullable;
import java.util.List;

public class ItemEnderCompass extends Item {
    public ItemEnderCompass(Properties builder) {
        super(builder);
        addPropertyOverride(new ResourceLocation("angle"), new EnderCompassAngleGetter()); // TODO: client-side only?
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World world, PlayerEntity player, Hand hand) {
        if (!world.isRemote) {
            BlockPos strongholdPos = ((ServerWorld)world).findNearestStructure("Stronghold", new BlockPos(player),100000,true);
            if (strongholdPos != null) {
                ItemStack stack = player.getHeldItem(hand);
                stack.getOrCreateTag().putIntArray("pos",new int[]{strongholdPos.getX(),strongholdPos.getY(),strongholdPos.getZ()});
                player.sendStatusMessage(new TranslationTextComponent("endercompass.found.text"),false);
            } else {
                player.sendStatusMessage(new TranslationTextComponent("endercompass.not_found.text"),false);
            }
        }
        return new ActionResult<>(ActionResultType.SUCCESS, player.getHeldItem(hand));
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World p_77624_2_, List<ITextComponent> list, ITooltipFlag p_77624_4_) {
        if (stack.hasTag() && stack.getTag().contains("pos")) {
            int[] xyz = stack.getTag().getIntArray("pos");
            list.add(new StringTextComponent("X: "+xyz[0]));
            list.add(new StringTextComponent("Y: "+xyz[1]));
            list.add(new StringTextComponent("Z: "+xyz[2]));
        }
    }
}
