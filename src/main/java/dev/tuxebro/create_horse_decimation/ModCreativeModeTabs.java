package dev.tuxebro.create_horse_decimation;

import com.simibubi.create.AllCreativeModeTabs;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import static dev.tuxebro.create_horse_decimation.CreateHorseDecimation.REGISTRATE;

public class ModCreativeModeTabs {
    public static final DeferredRegister<CreativeModeTab> REGISTER =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, CreateHorseDecimation.MOD_ID);

    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> MAIN_TAB =
            REGISTER.register("main", () -> CreativeModeTab.builder()
                .title(Component.literal("Create: Horse DECIMATION"))
                .icon(() -> ModItems.HORSE_JPG.get().asItem().getDefaultInstance())
                .withTabsBefore(AllCreativeModeTabs.PALETTES_CREATIVE_TAB.getId())
                .displayItems((itemDisplayParameters, output) -> REGISTRATE.getAll(Registries.ITEM).forEach((itemEntry -> {
                    output.accept(itemEntry.get());
                })))
                .build());

    public static void register(IEventBus modEventBus) {
        REGISTER.register(modEventBus);
    }
}
