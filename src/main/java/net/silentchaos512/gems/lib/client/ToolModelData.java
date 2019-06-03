package net.silentchaos512.gems.lib.client;

import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.silentchaos512.gems.api.lib.IPartPosition;
import net.silentchaos512.gems.api.lib.ToolPartPosition;
import net.silentchaos512.gems.api.tool.part.ToolPart;
import net.silentchaos512.gems.item.ToolRenderHelper;
import net.silentchaos512.gems.util.ToolHelper;

@SideOnly(Side.CLIENT)
public class ToolModelData implements IModelData {
    /**
     * The models. First index is animation frame, second index is render pass (part pos)
     */
    private ModelResourceLocation[][] models;
    /**
     * The colors, standard 24-bit.
     */
    private int[] colors;

    public ToolModelData(ItemStack tool) {
        int totalFrames = ToolRenderHelper.getInstance().getTotalAnimationFrames(tool);
        models = new ModelResourceLocation[totalFrames][ToolPartPosition.values().length];
        colors = new int[ToolPartPosition.values().length];

        for (ToolPartPosition pass : ToolPartPosition.values()) {
            // Get part for each render pass.
            ToolPart part = ToolHelper.getRenderPart(tool, pass);
            if (part != null) {
                // If part exists, get model for each frame and color.
                for (int frame = 0; frame < totalFrames; ++frame) {
                    models[frame][pass.ordinal()] = part.getModel(tool, pass, frame);
                }
                colors[pass.ordinal()] = part.getColor(tool, pass, 0);
            } else {
                // No part.
                colors[pass.ordinal()] = 0xFFFFFF;
            }
        }
    }

    @Override
    public ModelResourceLocation getModel(IPartPosition pos, int frame) {
        int pass = pos.getRenderPass();
        if (frame < 0 || frame >= models.length || pass < 0 || pass >= models[0].length) {
            return null;
        }
        return models[frame][pass];
    }

    @Override
    public int getColor(IPartPosition pos, int frame) {
        int pass = pos.getRenderPass();
        if (pass < 0 || pass >= colors.length) {
            return 0xFFFFFF;
        }
        return colors[pos.getRenderPass()];
    }
}
