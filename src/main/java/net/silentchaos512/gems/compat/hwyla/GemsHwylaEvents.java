package net.silentchaos512.gems.compat.hwyla;

import mcp.mobius.waila.Waila;
import mcp.mobius.waila.api.ITaggableList;
import mcp.mobius.waila.api.event.WailaTooltipEvent;
import net.minecraft.block.Block;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.silentchaos512.gems.block.IGemBlock;

public class GemsHwylaEvents {
    private static final ResourceLocation OBJECT_NAME_TAG = new ResourceLocation(Waila.MODID, "object_name");

    @SubscribeEvent
    public void onTooltip(WailaTooltipEvent event) {
        Block block = event.getAccessor().getBlock();
        if (block instanceof IGemBlock) {
            // For blocks from this mod, we need to correct the object name
            ITaggableList<ResourceLocation, ITextComponent> tooltip = (ITaggableList<ResourceLocation, ITextComponent>) event.getCurrentTip();
            tooltip.setTag(OBJECT_NAME_TAG, ((IGemBlock) block).getGemBlockName().applyTextStyle(TextFormatting.WHITE));
        }
    }
}
