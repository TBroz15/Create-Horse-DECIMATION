package dev.tuxebro.create_horse_decimation.config;

import net.createmod.catnip.config.ConfigBase;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.config.ModConfigEvent;
import net.neoforged.neoforge.common.ModConfigSpec;
import net.neoforged.fml.config.ModConfig;
import org.apache.commons.lang3.tuple.Pair;

import java.util.EnumMap;
import java.util.Map;
import java.util.function.Supplier;

// Derived from https://github.com/StickyPiston-development/CreateCobblestone/
@EventBusSubscriber
public class Config {
    public static final Map<ModConfig.Type, ConfigBase> CONFIGS = new EnumMap<>(ModConfig.Type.class);

    public static boolean isLoaded = false;

    public static ClientConfig client = register(ClientConfig::new, ModConfig.Type.CLIENT);
    public static ServerConfig server = register(ServerConfig::new, ModConfig.Type.SERVER);

    private static <T extends ConfigBase> T register(Supplier<T> factory, ModConfig.Type side) {
        Pair<T, ModConfigSpec> specPair = new ModConfigSpec.Builder().configure(builder -> {
            T config = factory.get();
            config.registerAll(builder);
            return config;
        });

        T config = specPair.getLeft();
        config.specification = specPair.getRight();
        CONFIGS.put(side, config);
        return config;
    }

    public static ConfigBase byType(ModConfig.Type type) {
        return CONFIGS.get(type);
    }

    public static void loadConfig(ModConfigEvent event) {
        for (ConfigBase config : CONFIGS.values()) {
            if (config.specification != event.getConfig().getSpec()) continue;
            config.onLoad();
        }

        isLoaded = true;
    }

    @SubscribeEvent
    public static void onLoad(ModConfigEvent.Loading event) {
        loadConfig(event);
    }

    @SubscribeEvent
    public static void onReload(ModConfigEvent.Reloading event) {
        loadConfig(event);
    }

    public static void register(ModContainer container) {
        for (Map.Entry<ModConfig.Type, ConfigBase> pair : CONFIGS.entrySet())
            container.registerConfig(pair.getKey(), pair.getValue().specification);
    }
}
