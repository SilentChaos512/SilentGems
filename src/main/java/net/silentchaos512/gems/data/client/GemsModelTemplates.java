package net.silentchaos512.gems.data.client;

import net.minecraft.client.data.models.model.ModelLocationUtils;
import net.minecraft.client.data.models.model.ModelTemplate;

import java.util.Optional;

public class GemsModelTemplates {
    public static final ModelTemplate TELEPORTER = new ModelTemplate(
            Optional.of(ModelLocationUtils.decorateItemModelLocation("silentgems:teleporter")),
            Optional.empty(),
            GemsTextureSlots.FRAME,
            GemsTextureSlots.GEM
    );
}
