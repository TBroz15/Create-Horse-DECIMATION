package dev.tuxebro.create_horse_decimation.config;

import dev.tuxebro.create_horse_decimation.CreateHorseDecimation;
import net.createmod.catnip.config.ui.BaseConfigScreen;
import net.minecraft.client.gui.screens.Screen;
import net.neoforged.fml.ModContainer;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;

public class ScreenManager {
    private static boolean done = false;

    private static BaseConfigScreen createConfigScreen(Screen parent) {
        if (!done) {
            BaseConfigScreen.setDefaultActionFor(CreateHorseDecimation.MOD_ID, base ->
                    base.withSpecs(Config.client.specification, null, Config.server.specification)
                            .withButtonLabels("", "", "")
            );
            done = true;
        }
        return new BaseConfigScreen(parent, CreateHorseDecimation.MOD_ID);
    }

    public static void registerConfigScreen(ModContainer modContainer) {
        modContainer.registerExtensionPoint(
                IConfigScreenFactory.class,
                (container, parent) -> ScreenManager.createConfigScreen(parent));
    }
}
