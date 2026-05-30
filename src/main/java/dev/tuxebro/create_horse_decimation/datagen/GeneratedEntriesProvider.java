package dev.tuxebro.create_horse_decimation.datagen;

import com.simibubi.create.api.registry.CreateRegistries;
import dev.tuxebro.create_horse_decimation.CreateHorseDecimation;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.DatapackBuiltinEntriesProvider;

import java.util.Set;
import java.util.concurrent.CompletableFuture;

public class GeneratedEntriesProvider extends DatapackBuiltinEntriesProvider {
    private static final RegistrySetBuilder BUILDER = new RegistrySetBuilder()
            .add(CreateRegistries.POTATO_PROJECTILE_TYPE, ModPotatoProjectileTypes::bootstrap);

    public GeneratedEntriesProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries, BUILDER, Set.of(CreateHorseDecimation.MOD_ID));
    }

    @Override
    public String getName() {
        return "Create: Horse DECIMATION's Generated Registry Entries";
    }
}
