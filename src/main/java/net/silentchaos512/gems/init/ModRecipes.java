package net.silentchaos512.gems.init;

import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.RecipeSerializers;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import net.silentchaos512.gems.SilentGems;
import net.silentchaos512.gems.crafting.ApplyEnchantmentTokenRecipe;
import net.silentchaos512.gems.crafting.ModifySoulUrnRecipe;
import net.silentchaos512.gems.crafting.SoulUrnRecipe;

public class ModRecipes {
    public static void init() {
        RecipeSerializers.register(ApplyEnchantmentTokenRecipe.Serializer.INSTANCE);
        RecipeSerializers.register(ModifySoulUrnRecipe.Serializer.INSTANCE);
        RecipeSerializers.register(SoulUrnRecipe.Serializer.INSTANCE);

        MinecraftForge.EVENT_BUS.addListener(ModRecipes::onPlayerJoinServer);
    }

    private static void onPlayerJoinServer(PlayerEvent.PlayerLoggedInEvent event) {
        if (!event.player.world.isRemote) {
            if (event.player.world.getServer() == null) return;

            event.player.unlockRecipes(event.player.world.getServer().getRecipeManager().getRecipes()
                    .stream()
                    .map(IRecipe::getId)
                    .filter(name -> name.getNamespace().equals(SilentGems.MOD_ID))
                    .toArray(ResourceLocation[]::new));
        }
    }
}
