package net.silentchaos512.gems.compat.hwyla;

import mcp.mobius.waila.Waila;
import mcp.mobius.waila.api.ITaggableList;
import mcp.mobius.waila.api.event.WailaTooltipEvent;
import net.minecraft.block.Block;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.silentchaos512.gems.SilentGems;

import java.util.Objects;

public class GemsHwylaEvents {
    private static final ResourceLocation OBJECT_NAME_TAG = new ResourceLocation(Waila.MODID, "object_name");

    @SubscribeEvent
    public void onTooltip(WailaTooltipEvent event) {
        Block block = event.getAccessor().getBlock();
        if (Objects.requireNonNull(block.getRegistryName()).getNamespace().equals(SilentGems.MOD_ID)) {
            // For blocks from this mod, we need to correct the object name
            ITaggableList<ResourceLocation, ITextComponent> tooltip = (ITaggableList<ResourceLocation, ITextComponent>) event.getCurrentTip();
            tooltip.setTag(OBJECT_NAME_TAG, block.getNameTextComponent().applyTextStyle(TextFormatting.WHITE));
        }
    }
}
