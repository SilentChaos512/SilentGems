package net.silentchaos512.gems.client.render.handlers;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.silentchaos512.gems.core.util.LogHelper;

public class TextureHandler {

    @SubscribeEvent
    public void stitchTexture(TextureStitchEvent.Pre pre)
    {
        LogHelper.info("Stitching misc textures into the map - M4thG33k");
        pre.map.registerSprite(new ResourceLocation("silentgems","blocks/ChaosPylonPassive"));
        pre.map.registerSprite(new ResourceLocation("silentgems","blocks/ChaosPylonBurner"));
        pre.map.registerSprite(new ResourceLocation("silentgems","blocks/ChaosAltar"));
    }
}
