package dev.tuxebro.create_horse_decimation.datagen.recipe;

import com.simibubi.create.api.data.recipe.MixingRecipeGen;
import dev.tuxebro.create_horse_decimation.CreateHorseDecimation;
import dev.tuxebro.create_horse_decimation.ModFluids;
import dev.tuxebro.create_horse_decimation.ModItems;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.Tags;

import java.util.concurrent.CompletableFuture;

public class ModMixingRecipeGen extends MixingRecipeGen {
    GeneratedRecipe HORSE_GLUE_FLUID = create("horse_glue_fluid", b -> b
            .require(Tags.Fluids.WATER, 1000)
            .require(ModItems.HORSE_DUST)
            .output(ModFluids.HORSE_GLUE.get(), 1000)
            .duration(100));

    public ModMixingRecipeGen(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries, CreateHorseDecimation.MOD_ID);
    }
}
