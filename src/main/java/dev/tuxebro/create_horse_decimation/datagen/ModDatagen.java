package dev.tuxebro.create_horse_decimation.datagen;

import com.simibubi.create.Create;
import com.simibubi.create.foundation.ponder.CreatePonderPlugin;
import com.simibubi.create.infrastructure.data.TagLangGenerator;
import com.tterrag.registrate.providers.ProviderType;
import dev.tuxebro.create_horse_decimation.CreateHorseDecimation;
import dev.tuxebro.create_horse_decimation.datagen.recipe.ModCompactingRecipeGen;
import dev.tuxebro.create_horse_decimation.datagen.recipe.ModMechanicalCraftingRecipeGen;
import dev.tuxebro.create_horse_decimation.datagen.recipe.ModMixingRecipeGen;
import dev.tuxebro.create_horse_decimation.datagen.recipe.ModRecipeProvider;
import dev.tuxebro.create_horse_decimation.ponders.ModPonderPlugin;
import net.createmod.ponder.foundation.PonderIndex;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.data.event.GatherDataEvent;

import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;

public class ModDatagen {
    public static void gatherDataHighPriority(GatherDataEvent event) {
        if (event.getMods().contains(CreateHorseDecimation.MOD_ID))
            addExtraRegistrateData();
    }

    public static void gatherData(GatherDataEvent event) {
        if (!event.getMods().contains(CreateHorseDecimation.MOD_ID))
            return;

        DataGenerator generator = event.getGenerator();
        PackOutput output = generator.getPackOutput();
        CompletableFuture<HolderLookup.Provider> lookupProvider = event.getLookupProvider();
        ExistingFileHelper existingFileHelper = event.getExistingFileHelper();

        GeneratedEntriesProvider entriesProvider = new GeneratedEntriesProvider(output, lookupProvider);
        lookupProvider = entriesProvider.getRegistryProvider();

        generator.addProvider(event.includeServer(), entriesProvider);

        generator.addProvider(event.includeServer(), new ModMechanicalCraftingRecipeGen(output, lookupProvider));

        if (!event.includeServer()) return;

        ModRecipeProvider.registerAllProcessing(generator, output, lookupProvider);

    }

    private static void addExtraRegistrateData() {
        CreateHorseDecimation.registrate().addDataGenerator(ProviderType.LANG, provider -> {
            BiConsumer<String, String> langConsumer = provider::add;

            providePonderLang(langConsumer);
        });
    }

    private static void providePonderLang(BiConsumer<String, String> consumer) {
        // Register this since FMLClientSetupEvent does not run during datagen
        PonderIndex.addPlugin(new ModPonderPlugin());

        PonderIndex.getLangAccess().provideLang(CreateHorseDecimation.MOD_ID, consumer);
    }
}
