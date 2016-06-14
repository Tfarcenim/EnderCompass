package com.outlook.siribby.endercompass.client;

import com.outlook.siribby.endercompass.EnderCompassMod;
import com.outlook.siribby.endercompass.network.EnderCompassProxy;
import com.outlook.siribby.endercompass.network.MessageGetStrongholdPos;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureCompass;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.entity.item.EntityItemFrame;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.client.event.RenderItemInFrameEvent;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.ReflectionHelper;

import java.util.Map;

public class EnderCompassClient extends EnderCompassProxy {
    public static final TextureEnderCompass ender_compass_icon = new TextureEnderCompass("endercompass:items/ender_compass");

    public static BlockPos strongholdPos;
    private static World strongholdWorld;

    public static Minecraft minecraft = FMLClientHandler.instance().getClient();

    @Override
    public void preInit() {
        ModelLoader.setCustomModelResourceLocation(EnderCompassMod.ender_compass, 0, new ModelResourceLocation("endercompass:ender_compass", "inventory"));
    }

    @SubscribeEvent
    public void onPreTextureStich(TextureStitchEvent.Pre event) {
        Map<String, TextureAtlasSprite> mapRegisteredSprites = ReflectionHelper.getPrivateValue(TextureMap.class, event.map, "mapRegisteredSprites", "field_110574_e");
        mapRegisteredSprites.put(ender_compass_icon.getIconName(), ender_compass_icon);
    }

    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event) {
        if (minecraft.theWorld != strongholdWorld && minecraft.thePlayer != null) {
            strongholdPos = null;
            strongholdWorld = minecraft.theWorld;
            EnderCompassMod.network.sendToServer(new MessageGetStrongholdPos());
        }
    }

    @SubscribeEvent
    public void onRender(RenderItemInFrameEvent event) {
        ItemStack stack = event.item;
        EntityItemFrame itemFrame = event.entityItemFrame;

        if (stack.getItem() == EnderCompassMod.ender_compass) {
            TextureAtlasSprite texture = minecraft.getTextureMapBlocks().getAtlasSprite(ender_compass_icon.getIconName());
            minecraft.getTextureManager().bindTexture(TextureMap.locationBlocksTexture);

            if (texture instanceof TextureCompass) {
                TextureCompass compassTexture = (TextureCompass) texture;
                double angle = compassTexture.currentAngle;
                double delta = compassTexture.angleDelta;
                compassTexture.currentAngle = 0.0D;
                compassTexture.angleDelta = 0.0D;
                compassTexture.updateCompass(itemFrame.worldObj, itemFrame.posX, itemFrame.posZ, (double) MathHelper.wrapAngleTo180_float((float) (180 + itemFrame.facingDirection.getHorizontalIndex() * 90)), false, true);
                compassTexture.currentAngle = angle;
                compassTexture.angleDelta = delta;
            } else {
                texture = null;
            }

            GlStateManager.scale(0.5F, 0.5F, 0.5F);

            if (!minecraft.getRenderItem().shouldRenderItemIn3D(stack)) {
                GlStateManager.rotate(180.0F, 0.0F, 1.0F, 0.0F);
            }

            GlStateManager.pushAttrib();
            RenderHelper.enableStandardItemLighting();
            minecraft.getRenderItem().renderItem(stack, ItemCameraTransforms.TransformType.FIXED);
            RenderHelper.disableStandardItemLighting();
            GlStateManager.popAttrib();

            if (texture != null && texture.getFrameCount() > 0) {
                texture.updateAnimation();
            }

            event.setCanceled(true);
        }
    }
}
