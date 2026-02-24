package net.silentchaos512.gems.setup;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.fml.common.EventBusSubscriber;

@EventBusSubscriber(value = Dist.CLIENT)
public final class GemsClientProxy {
    private GemsClientProxy() {}
}
