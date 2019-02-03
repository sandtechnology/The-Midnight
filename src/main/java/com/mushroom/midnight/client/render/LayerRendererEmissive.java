package com.mushroom.midnight.client.render;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.ResourceLocation;

public class LayerRendererEmissive<T extends EntityLivingBase> implements LayerRenderer<T> {
    private static final Minecraft CLIENT = Minecraft.getMinecraft();

    private final ModelBase model;
    private final ResourceLocation texture;

    public LayerRendererEmissive(ModelBase model, ResourceLocation texture) {
        this.model = model;
        this.texture = texture;
    }

    @Override
    public void doRenderLayer(T entity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
        if (entity.isInvisible()) {
            return;
        }

        CLIENT.getTextureManager().bindTexture(this.texture);
        GlStateManager.depthMask(true);
        GlStateManager.enableBlend();

        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 240, 240);
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        GlStateManager.disableLighting();

        this.model.render(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);

        GlStateManager.enableLighting();
        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, OpenGlHelper.lastBrightnessX, OpenGlHelper.lastBrightnessY);
    }

    @Override
    public boolean shouldCombineTextures() {
        return false;
    }
}
