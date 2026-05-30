package dev.tuxebro.create_horse_decimation.datagen.recipe;

import com.simibubi.create.AllBlocks;
import com.simibubi.create.api.data.recipe.CrushingRecipeGen;
import dev.tuxebro.create_horse_decimation.CreateHorseDecimation;
import dev.tuxebro.create_horse_decimation.ModBlocks;
import dev.tuxebro.create_horse_decimation.ModItems;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;

import java.util.concurrent.CompletableFuture;

public class ModCrushingRecipeGen extends CrushingRecipeGen {

    GeneratedRecipe HORSE_JPG = create(() -> ModItems.HORSE_JPG, builder -> builder
            .duration(200)
            .output(ModItems.HORSE_DUST, 1)
            .output(.50f, ModItems.HORSE_DUST, 1)
            .output(.25f, AllBlocks.EXPERIENCE_BLOCK, 1));

    GeneratedRecipe HORSE_BLOCK = create(() -> ModBlocks.HORSE_BLOCK, builder -> builder
            .duration(260)
            .output(ModItems.HORSE_DUST, 10)
            .output(AllBlocks.EXPERIENCE_BLOCK, 1)
            .output(.50f, ModItems.HORSE_DUST, 3 ));

    public ModCrushingRecipeGen(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries, CreateHorseDecimation.MOD_ID);
    }
}
