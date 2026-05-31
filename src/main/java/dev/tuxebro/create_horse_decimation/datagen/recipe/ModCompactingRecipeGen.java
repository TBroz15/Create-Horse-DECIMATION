package dev.tuxebro.create_horse_decimation.datagen.recipe;

import com.simibubi.create.api.data.recipe.CompactingRecipeGen;
import com.simibubi.create.content.processing.recipe.HeatCondition;
import dev.tuxebro.create_horse_decimation.CreateHorseDecimation;
import dev.tuxebro.create_horse_decimation.ModBlocks;
import dev.tuxebro.create_horse_decimation.ModFluids;
import dev.tuxebro.create_horse_decimation.ModItems;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;

import java.util.concurrent.CompletableFuture;

public class ModCompactingRecipeGen extends CompactingRecipeGen {
    GeneratedRecipe HORSE_GLUE_BALL = create("horse_glue_ball", b -> b
            .require(ModFluids.HORSE_GLUE.get(), 100)
            .output(ModItems.HORSE_GLUE_BALL)
            .requiresHeat(HeatCondition.HEATED));

    GeneratedRecipe HORSE_BLOCK = create("horse_block", b -> b
            .require(ModItems.HORSE_JPG)
            .require(ModItems.HORSE_JPG)
            .require(ModItems.HORSE_JPG)
            .require(ModItems.HORSE_JPG)
            .require(ModItems.HORSE_JPG)
            .require(ModItems.HORSE_JPG)
            .require(ModItems.HORSE_JPG)
            .require(ModItems.HORSE_JPG)
            .require(ModItems.HORSE_JPG)
            .output(ModBlocks.HORSE_BLOCK));

    public ModCompactingRecipeGen(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries, CreateHorseDecimation.MOD_ID);
    }
}
