package net.silentchaos512.gems.proxy;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraft.world.storage.loot.LootTableList;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.silentchaos512.gems.SilentGems;
import net.silentchaos512.gems.api.Skulls;
import net.silentchaos512.gems.client.gui.GuiHandlerSilentGems;
import net.silentchaos512.gems.compat.BaublesCompat;
import net.silentchaos512.gems.compat.crafttweaker.CTSilentGems;
import net.silentchaos512.gems.compat.evilcraft.EvilCraftCompat;
import net.silentchaos512.gems.compat.gear.SGearCompat;
import net.silentchaos512.gems.config.GemsConfig;
import net.silentchaos512.gems.event.GemsCommonEvents;
import net.silentchaos512.gems.event.ShieldEventHandler;
import net.silentchaos512.gems.handler.PlayerDataHandler;
import net.silentchaos512.gems.init.ModItems;
import net.silentchaos512.gems.init.SGearMaterials;
import net.silentchaos512.gems.item.quiver.QuiverHelper;
import net.silentchaos512.gems.lib.EnumModParticles;
import net.silentchaos512.gems.lib.module.ModuleHalloweenHijinks;
import net.silentchaos512.gems.lib.module.ModuleHolidayCheer;
import net.silentchaos512.gems.network.NetworkHandler;
import net.silentchaos512.gems.util.SoulManager;
import net.silentchaos512.gems.util.ToolHelper;
import net.silentchaos512.lib.proxy.IProxy;
import net.silentchaos512.lib.registry.SRegistry;
import net.silentchaos512.lib.util.Color;

public class GemsCommonProxy implements IProxy {
    @Override
    public void preInit(SRegistry registry, FMLPreInitializationEvent event) {
        ToolHelper.FAKE_MATERIAL.setRepairItem(ModItems.craftingMaterial.chaosEssenceEnriched);

        ModItems.guideBook.book.preInit();

        NetworkHandler.init();

        NetworkRegistry.INSTANCE.registerGuiHandler(SilentGems.instance, new GuiHandlerSilentGems());

        MinecraftForge.EVENT_BUS.register(new PlayerDataHandler.EventHandler());
        MinecraftForge.EVENT_BUS.register(new GemsCommonEvents());
        MinecraftForge.EVENT_BUS.register(new ShieldEventHandler());
        MinecraftForge.EVENT_BUS.register(QuiverHelper.instance);
        MinecraftForge.EVENT_BUS.register(new SoulManager());
        MinecraftForge.EVENT_BUS.register(ModuleHolidayCheer.instance);
        MinecraftForge.EVENT_BUS.register(ModuleHalloweenHijinks.instance);

        LootTableList.register(new ResourceLocation(SilentGems.MODID, "ender_slime"));

        // Silent Gear support?
        if (Loader.isModLoaded("silentgear") && GemsConfig.ENABLE_SGEAR_MATERIALS)
            SGearMaterials.init();

        registry.preInit(event);
    }

    @Override
    public void init(SRegistry registry, FMLInitializationEvent event) {
        registry.init(event);

        if (Loader.isModLoaded("evilcraft")) {
            EvilCraftCompat.init();
        }
    }

    @Override
    public void postInit(SRegistry registry, FMLPostInitializationEvent event) {
        Skulls.init();
        ModItems.enchantmentToken.addModRecipes();
        ModItems.guideBook.book.postInit();

        if (Loader.isModLoaded(BaublesCompat.MOD_ID))
            BaublesCompat.MOD_LOADED = true;

        if (Loader.isModLoaded("crafttweaker"))
            CTSilentGems.postInit();

        registry.postInit(event);
    }

    public void spawnParticles(EnumModParticles type, Color color, World world, double x, double y, double z, double motionX, double motionY, double motionZ) {
    }

    @Override
    public int getParticleSettings() {
        return 0;
    }

    @Override
    public EntityPlayer getClientPlayer() {
        return null;
    }

    public boolean isClientPlayerHoldingDebugItem() {
        EntityPlayer player = getClientPlayer();
        if (player == null) {
            return false;
        }

        ItemStack mainhand = player.getHeldItemMainhand();
        ItemStack offhand = player.getHeldItemOffhand();
        return mainhand.getItem() == ModItems.debugItem || offhand.getItem() == ModItems.debugItem;
    }

    public boolean isSGearMainPart(ItemStack stack) {
        if (Loader.isModLoaded("silentgear")) {
            return SGearCompat.isMainPart(stack);
        }
        return false;
    }

    public int getSGearPartTier(ItemStack stack) {
        if (Loader.isModLoaded("silentgear")) {
            return SGearCompat.getPartTier(stack);
        }
        return -1;
    }
}
