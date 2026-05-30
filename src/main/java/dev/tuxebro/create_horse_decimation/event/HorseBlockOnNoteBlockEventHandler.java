package dev.tuxebro.create_horse_decimation.event;

import dev.tuxebro.create_horse_decimation.ModBlocks;
import dev.tuxebro.create_horse_decimation.ModSoundEvents;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.level.NoteBlockEvent;

@EventBusSubscriber
public class HorseBlockOnNoteBlockEventHandler {

    @SubscribeEvent
    public static void onNoteBlockEvent(NoteBlockEvent.Play event) {
        var pos = event.getPos();
        var level = event.getLevel();

        var blockStateBelow = level.getBlockState(pos.below());

        if (!blockStateBelow.is(ModBlocks.HORSE_BLOCK)) return;
        event.setInstrument(NoteBlockInstrument.CUSTOM_HEAD);

        var noteID = event.getVanillaNoteId();

        var pitch = (float) Math.pow(2.0, (noteID - 12) / 12.0);

        level.playSound(
                null,
                pos,
                ModSoundEvents.HORSE_NOTE.get(),
                net.minecraft.sounds.SoundSource.RECORDS,
                1.0F,
                pitch
        );

        if (!(level instanceof net.minecraft.server.level.ServerLevel serverLevel)) return;

        var particleColorModifier = (double) noteID / 24.0;

        serverLevel.sendParticles(
                net.minecraft.core.particles.ParticleTypes.NOTE,
                pos.getX() + 0.5,
                pos.getY() + 1.2,
                pos.getZ() + 0.5,
                0,
                particleColorModifier,
                0.0,
                0.0,
                1.0
        );
    }
}
