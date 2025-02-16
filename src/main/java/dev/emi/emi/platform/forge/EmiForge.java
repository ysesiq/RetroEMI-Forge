package dev.emi.emi.platform.forge;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import dev.emi.emi.EMIPostInit;
import dev.emi.emi.platform.EmiMain;

@Mod(
    modid = EmiForge.MODID,
    name = EmiForge.NAME,
    version = EmiForge.VERSION
)
public class EmiForge {
    public static final String MODID = "emi";
    public static final String NAME = "EMI";
    public static final String VERSION = "alpha";

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        EmiMain.init();
    }

    @Mod.EventHandler
    public void postInit(FMLInitializationEvent event) {
        EMIPostInit.initEMI();
    }
}
