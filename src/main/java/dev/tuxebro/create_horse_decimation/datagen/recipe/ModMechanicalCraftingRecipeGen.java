package dev.tuxebro.create_horse_decimation.datagen.recipe;

import com.simibubi.create.AllBlocks;
import com.simibubi.create.AllItems;
import com.simibubi.create.api.data.recipe.MechanicalCraftingRecipeGen;
import dev.tuxebro.create_horse_decimation.CreateHorseDecimation;
import dev.tuxebro.create_horse_decimation.ModBlocks;
import dev.tuxebro.create_horse_decimation.ModItems;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;

import java.util.concurrent.CompletableFuture;

public class ModMechanicalCraftingRecipeGen extends MechanicalCraftingRecipeGen {
    GeneratedRecipe HORSE_ABDUCTOR = create(ModBlocks.HORSE_ABDUCTOR_BLOCK::get).returns(1)
            .recipe(b -> b
                    .key('C', AllBlocks.CHUTE)
                    .key('B', AllBlocks.BRASS_CASING)
                    .key('H', ModItems.HORSE_DUST)
                    .key('N', AllBlocks.NOZZLE)
                    .key('F', AllItems.FILTER)

                    .patternLine(" C ")
                    .patternLine("CBC")
                    .patternLine("HNF")
                    .patternLine(" C ")
            );

    public ModMechanicalCraftingRecipeGen(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries, CreateHorseDecimation.MOD_ID);
    }
}
