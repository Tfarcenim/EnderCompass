package io.github.mribby.endercompass;

import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.item.ItemFrameEntity;
import net.minecraft.item.IItemPropertyGetter;
import net.minecraft.item.ItemModelsProperties;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

import javax.annotation.Nullable;

@Mod.EventBusSubscriber(value = Dist.CLIENT,bus = Mod.EventBusSubscriber.Bus.MOD)
public class EnderCompassClient implements IItemPropertyGetter {

	@SubscribeEvent
	public static void models(FMLClientSetupEvent e) {
		ItemModelsProperties.func_239418_a_(EnderCompass.ENDER_COMPASS,new ResourceLocation("angle"),new EnderCompassClient());
	}

	private double prevAngle = 0.0D;
	private double prevWobble = 0.0D;
	private long prevWorldTime = 0L;

	/**
	 * Calculates the compass angle from an item stack and an entity/item frame
	 *
	 * @param stack The item stack
	 * @param world The world
	 * @param livingEntity The entity
	 * @return The angle
	 */
	@Override
	public float call(ItemStack stack, @Nullable ClientWorld world, @Nullable LivingEntity livingEntity) {
		boolean isLiving = livingEntity != null;

		if (!isLiving && !stack.isOnItemFrame() || !stack.hasTag() || !stack.getTag().contains("pos")) {
			return 0;
		}

		Entity entity = isLiving ? livingEntity : stack.getItemFrame();

		if (world == null) {
			world = (ClientWorld)entity.world;
		}

		int[] strongholdPos = stack.getTag().getIntArray("pos");
		double angle;

		double entityAngle = isLiving ? entity.rotationYaw : getFrameAngle((ItemFrameEntity) entity);
		entityAngle /= 360.0D;
		entityAngle = MathHelper.positiveModulo(entityAngle, 1.0D);
		double posAngle = getPosToAngle(strongholdPos, entity);
		posAngle /= Math.PI * 2D;
		angle = 0.5D - (entityAngle - 0.25D - posAngle);

		if (isLiving) {
			angle = wobble(world, angle);
		}

		return MathHelper.positiveModulo((float) angle, 1.0F);
	}

	/**
	 * Adds wobbliness based on the previous angle and the specified angle
	 *
	 * @param world The world
	 * @param angle The current angle
	 * @return The new, wobbly angle
	 */
	private double wobble(World world, double angle) {
		long worldTime = world.getGameTime();
		if (worldTime != prevWorldTime) {
			prevWorldTime = worldTime;
			double angleDifference = angle - prevAngle;
			angleDifference = MathHelper.positiveModulo(angleDifference + 0.5D, 1.0D) - 0.5D;
			prevWobble += angleDifference * 0.1D;
			prevWobble *= 0.8D;
			prevAngle = MathHelper.positiveModulo(prevAngle + prevWobble, 1.0D);
		}
		return prevAngle;
	}

	/**
	 * Gets the facing direction of an item frame in degrees
	 *
	 * @param entity The entity instance of the item frame
	 * @return The angle
	 */
	private double getFrameAngle(ItemFrameEntity entity) {
		return MathHelper.wrapDegrees(180 + entity.getHorizontalFacing().getHorizontalIndex() * 90);
	}

	/**
	 * Gets the angle from an entity to the specified position in radians
	 *
	 * @param pos The position
	 * @param entity The entity
	 * @return The angle
	 */
	private double getPosToAngle(int[] pos, Entity entity) {
		return Math.atan2(pos[2] - entity.getPosZ(), pos[0] - entity.getPosX());
	}
}
