package dev.tuxebro.create_horse_decimation.utils;

import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;

public class DebugginStuff {
    public static void showParticlesAABB(Level level, AABB box, SimpleParticleType type) {
        if (level.isClientSide) return;
        ServerLevel serverLevel = (ServerLevel) level;

        double[][] corners = {
                {box.minX, box.minY, box.minZ},
                {box.maxX, box.minY, box.minZ},
                {box.minX, box.maxY, box.minZ},
                {box.maxX, box.maxY, box.minZ},
                {box.minX, box.minY, box.maxZ},
                {box.maxX, box.minY, box.maxZ},
                {box.minX, box.maxY, box.maxZ},
                {box.maxX, box.maxY, box.maxZ},
                {box.getCenter().x, box.getCenter().y, box.getCenter().z}
        };

        for (double[] corner : corners) {
            serverLevel.sendParticles(
                    type,
                    corner[0], corner[1], corner[2],
                    1,
                    0, 0, 0,
                    0
            );
        }
    }
}
