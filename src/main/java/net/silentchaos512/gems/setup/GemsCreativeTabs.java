package net.silentchaos512.gems.setup;

import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;
import net.silentchaos512.gems.GemsBase;
import net.silentchaos512.gems.util.Gems;

public class GemsCreativeTabs {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, GemsBase.MOD_ID);

    public static final RegistryObject<CreativeModeTab> TAB = CREATIVE_TABS.register("tab", () ->
            CreativeModeTab.builder()
                    .icon(() -> Gems.RUBY.getItem().getDefaultInstance())
                    .title(Component.translatable("itemGroup.silentgems"))
                    .displayItems((itemDisplayParameters, output) -> {
                        // TODO: What about sub items?
                        Registration.ITEMS.getEntries().forEach(ro -> output.accept(ro.get()));
                    })
                    .build());
}
