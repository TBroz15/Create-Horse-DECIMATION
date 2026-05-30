package dev.tuxebro.create_horse_decimation;

import com.simibubi.create.AllSoundEvents;
import com.simibubi.create.Create;
import com.simibubi.create.foundation.data.CreateRegistrate;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.HashMap;
import java.util.Map;

public class ModSoundEvents {
    public static final DeferredRegister<SoundEvent> SOUND_EVENTS =
            DeferredRegister.create(BuiltInRegistries.SOUND_EVENT, CreateHorseDecimation.MOD_ID);

    public static final DeferredHolder<SoundEvent, SoundEvent> SNIFFY_SNIFFA =
            registerEvent("sniffy_sniffa");

    public static final DeferredHolder<SoundEvent, SoundEvent> HORSE_NOTE =
            registerEvent("horse_note");

    private static AllSoundEvents.SoundEntryBuilder create(String name) {
        return create(CreateHorseDecimation.asResource(name));
    }

    public static AllSoundEvents.SoundEntryBuilder create(ResourceLocation id) {
        return new AllSoundEvents.SoundEntryBuilder(id);
    }

    private static DeferredHolder<SoundEvent, SoundEvent> registerEvent(String name) {
        return SOUND_EVENTS.register(name, () -> SoundEvent.createVariableRangeEvent(
                ResourceLocation.fromNamespaceAndPath(CreateHorseDecimation.MOD_ID, name)
        ));
    }

    public static void register(IEventBus modEventBus) {
        SOUND_EVENTS.register(modEventBus);
    }
}
