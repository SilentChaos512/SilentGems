package net.silentchaos512.gems.data.client;

import net.minecraft.client.data.models.model.ModelTemplate;
import net.silentchaos512.gems.SilentGems;

import java.util.Optional;

public class GemsModelTemplates {
    public static final ModelTemplate TELEPORTER = new ModelTemplate(
            Optional.of(SilentGems.getId("block/teleporter")),
            Optional.empty(),
            GemsTextureSlots.FRAME,
            GemsTextureSlots.GEM
    );
}
