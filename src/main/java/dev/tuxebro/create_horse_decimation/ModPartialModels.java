package dev.tuxebro.create_horse_decimation;

import dev.engine_room.flywheel.lib.model.baked.PartialModel;
import net.createmod.catnip.data.Couple;
import net.minecraft.resources.ResourceLocation;

import java.util.HashMap;
import java.util.Map;

public class ModPartialModels {
    public static final Map<ResourceLocation, Couple<PartialModel>> FOLDING_DOORS = new HashMap<>();

    static {
        putFoldingDoor(ModBlocks.HORSE_DOOR.getId().getPath());
    }

    private static void putFoldingDoor(String path) {
        FOLDING_DOORS.put(CreateHorseDecimation.asResource(path),
                Couple.create(block(path + "/fold_left"), block(path + "/fold_right")));
    }

    private static PartialModel block(String path) {
        return PartialModel.of(CreateHorseDecimation.asResource("block/" + path));
    }

    public static void register() {}
}
