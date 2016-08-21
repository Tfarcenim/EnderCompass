package com.outlook.siribby.endercompass;

import com.outlook.siribby.endercompass.client.EnderCompassClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItemFrame;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.IItemPropertyGetter;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;

public class ItemEnderCompass extends Item {
    public ItemEnderCompass() {
        addPropertyOverride(new ResourceLocation("angle"), new IItemPropertyGetter() {
            @SideOnly(Side.CLIENT)
            double rotation;
            @SideOnly(Side.CLIENT)
            double rota;
            @SideOnly(Side.CLIENT)
            long lastUpdateTick;

            @SideOnly(Side.CLIENT)
            public float apply(ItemStack stack, @Nullable World worldIn, @Nullable EntityLivingBase entityIn) {
                if (entityIn == null && !stack.isOnItemFrame()) {
                    return 0.0F;
                } else {
                    boolean flag = entityIn != null;
                    Entity entity = flag ? entityIn : stack.getItemFrame();

                    if (worldIn == null) {
                        worldIn = entity.worldObj;
                    }

                    BlockPos strongholdPos = EnderCompassClient.getStrongholdPos();
                    double d0;

                    if (strongholdPos != null) {
                        double d1 = flag ? (double) entity.rotationYaw : getFrameRotation((EntityItemFrame) entity);
                        d1 = d1 % 360.0D;
                        double d2 = getPosToAngle(worldIn, entity, strongholdPos);
                        d0 = Math.PI - ((d1 - 90.0D) * 0.01745329238474369D - d2);
                    } else {
                        d0 = Math.random() * (Math.PI * 2D);
                    }

                    if (flag) {
                        d0 = wobble(worldIn, d0);
                    }

                    float f = (float) (d0 / (Math.PI * 2D));
                    return MathHelper.positiveModulo(f, 1.0F);
                }
            }

            @SideOnly(Side.CLIENT)
            private double wobble(World p_185093_1_, double p_185093_2_) {
                if (p_185093_1_.getTotalWorldTime() != lastUpdateTick) {
                    lastUpdateTick = p_185093_1_.getTotalWorldTime();
                    double d0 = p_185093_2_ - rotation;
                    d0 = d0 % (Math.PI * 2D);
                    d0 = MathHelper.clamp_double(d0, -1.0D, 1.0D);
                    rota += d0 * 0.1D;
                    rota *= 0.8D;
                    rotation += rota;
                }

                return rotation;
            }

            @SideOnly(Side.CLIENT)
            private double getFrameRotation(EntityItemFrame p_185094_1_) {
                return (double) MathHelper.clampAngle(180 + p_185094_1_.facingDirection.getHorizontalIndex() * 90);
            }

            @SideOnly(Side.CLIENT)
            private double getPosToAngle(World p_185092_1_, Entity p_185092_2_, BlockPos blockpos) {
                return Math.atan2((double) blockpos.getZ() - p_185092_2_.posZ, (double) blockpos.getX() - p_185092_2_.posX);
            }
        });
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(ItemStack stack, World world, EntityPlayer player, EnumHand hand) {
        if (world.isRemote) {
            EnderCompassClient.resetStrongholdPos();
        }
        return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, stack);
    }
}
