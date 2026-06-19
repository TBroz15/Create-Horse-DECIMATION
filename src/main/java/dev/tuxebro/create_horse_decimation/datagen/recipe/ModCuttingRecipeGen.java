package dev.tuxebro.create_horse_decimation.datagen.recipe;

import com.simibubi.create.api.data.recipe.CuttingRecipeGen;
import dev.tuxebro.create_horse_decimation.CreateHorseDecimation;
import dev.tuxebro.create_horse_decimation.ModBlocks;
import dev.tuxebro.create_horse_decimation.ModItems;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;

import java.util.concurrent.CompletableFuture;

public class ModCuttingRecipeGen extends CuttingRecipeGen {
    GeneratedRecipe
        HORSE_BLOCK = create(ModBlocks.HORSE_BLOCK::asItem, builder -> builder
            .duration(100)
            .output(ModBlocks.HORSE_DOOR, 1));

    public ModCuttingRecipeGen(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries, CreateHorseDecimation.MOD_ID);
    }
}
