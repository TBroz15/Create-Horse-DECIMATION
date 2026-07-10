package dev.tuxebro.create_horse_decimation.event;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.PlayerWakeUpEvent;

//@EventBusSubscriber
public class BedEventHandler {
//    @SubscribeEvent
    // TODO: add random roll to spawn a jorse
    public static void onPlayerSleep(PlayerWakeUpEvent event) {
        var player = event.getEntity();
        var level = player.level();

        if (!(level instanceof ServerLevel serverLevel)) return;
        boolean isSleepingSuccessful = !event.wakeImmediately() && !event.updateLevel();
        if (!isSleepingSuccessful) return;

        BlockPos jorsePos = findSafeJorsePos(player, serverLevel);
        var jorse = EntityType.HORSE.spawn(serverLevel, jorsePos, MobSpawnType.SPAWN_EGG);
        if (jorse == null) return;

        serverLevel.addFreshEntity(jorse);
    }

    // TODO: Properly spawn jorse in front of the player since sleeping direction is weird
    public static BlockPos findSafeJorsePos(Player player, ServerLevel serverLevel) {
        final int MAX_HORSE_DISTANCE = 5;

        Direction direction = player.getDirection();
        direction.getClockWise();
        BlockPos jorsePos = player
                .blockPosition()
                .relative(direction, MAX_HORSE_DISTANCE);

        for (int i = 0; i < MAX_HORSE_DISTANCE; i++) {
            var block = serverLevel.getBlockState(jorsePos.above());
            if (block.is(BlockTags.AIR)) break;

            jorsePos = jorsePos.relative(direction, -1);
        }

        return jorsePos;
    }
}
