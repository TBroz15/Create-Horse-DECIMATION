package dev.tuxebro.create_horse_decimation.event;

import dev.tuxebro.create_horse_decimation.CreateHorseDecimation;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.tick.ServerTickEvent;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@EventBusSubscriber(modid = CreateHorseDecimation.MOD_ID)
public class TickHandler {
    private static final ConcurrentHashMap<Runnable, AtomicInteger> DELAYED_TASKS = new ConcurrentHashMap<>();
    private static final ConcurrentHashMap<LimitedInterval, AtomicInteger> LIMITED_INTERVAL_TASKS = new ConcurrentHashMap<>();

    public static void addDelayedTask(int ticks, Runnable action) {
        DELAYED_TASKS.put(action, new AtomicInteger(ticks));
    }

    public static void addLimitedIntervalTasks(int ticksPerRun, int maxRuns, Runnable action) {
        LIMITED_INTERVAL_TASKS.put(new LimitedInterval(ticksPerRun, action), new AtomicInteger(maxRuns));
    }

    @SubscribeEvent
    public static void onTick(ServerTickEvent.Post event) {
        LIMITED_INTERVAL_TASKS.forEach((interval, runs) -> {
            boolean isTriggered = interval.onTick();
            if (!isTriggered) return;

            var runsInt = runs.getAndDecrement();

            if (runsInt > 0)
                return;

            LIMITED_INTERVAL_TASKS.remove(interval);
        });

        DELAYED_TASKS.forEach((runnable, atomicTicks) -> {
            int ticks = atomicTicks.decrementAndGet();

            if (!(ticks <= 0)) return;

            runnable.run();
            DELAYED_TASKS.remove(runnable);
        });

    }

    private static class LimitedInterval {
        public final AtomicInteger ticks = new AtomicInteger(0);

        public final Integer ticksPerRun;
        public final Runnable action;

        public LimitedInterval(int ticksPerRun, Runnable action) {
            this.ticksPerRun = ticksPerRun;
            this.action = action;
        }

        public boolean onTick() {
            ticks.incrementAndGet();

            if (!(ticks.get() >= ticksPerRun))
                return false;

            ticks.set(0);

            action.run();

            return true;
        }
    }
}