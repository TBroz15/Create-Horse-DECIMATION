package dev.tuxebro.create_horse_decimation.ponders;

import com.simibubi.create.AllBlocks;
import com.tterrag.registrate.util.entry.RegistryEntry;
import dev.tuxebro.create_horse_decimation.CreateHorseDecimation;
import dev.tuxebro.create_horse_decimation.ModBlocks;
import dev.tuxebro.create_horse_decimation.ModItems;
import net.createmod.catnip.registry.RegisteredObjectsHelper;
import net.createmod.ponder.api.registration.PonderTagRegistrationHelper;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.ItemLike;

public class ModPonderTags {

    public static final ResourceLocation HORSE_DECIMATION = loc("horse_decimation");

    public static void register(PonderTagRegistrationHelper<ResourceLocation> helper) {
        PonderTagRegistrationHelper<RegistryEntry<?, ?>> HELPER = helper.withKeyFunction(RegistryEntry::getId);

        PonderTagRegistrationHelper<ItemLike> itemHelper = helper.withKeyFunction(
                RegisteredObjectsHelper::getKeyOrThrow);

        helper.registerTag(HORSE_DECIMATION)
                .addToIndex()
                .item(ModItems.HORSE_JPG, true, false)
                .title("Horse Decimation")
                .description("Components that helps you decimate those foul beasts")
                .register();

        HELPER.addToTag(HORSE_DECIMATION)
                .add(AllBlocks.CRUSHING_WHEEL)
                .add(ModBlocks.HORSE_ABDUCTOR_BLOCK);
    }

    private static ResourceLocation loc(String id) {
        return CreateHorseDecimation.asResource(id);
    }
}
