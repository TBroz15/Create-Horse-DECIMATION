package dev.tuxebro.create_horse_decimation.ponders;

import dev.tuxebro.create_horse_decimation.CreateHorseDecimation;
import net.createmod.ponder.api.level.PonderLevel;
import net.createmod.ponder.api.registration.*;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

public class ModPonderPlugin implements PonderPlugin {
    @Override
    public @NotNull String getModId() {
        return CreateHorseDecimation.MOD_ID;
    }

    @Override
    public void registerScenes(PonderSceneRegistrationHelper<ResourceLocation> helper) {
        ModPonders.registerScenes(helper);
    }

    @Override
    public void registerTags(PonderTagRegistrationHelper<ResourceLocation> helper) {
        ModPonderTags.register(helper);
    }

    @Override
    public void registerSharedText(SharedTextRegistrationHelper helper) {
//        PonderPlugin.super.registerSharedText(helper);
    }

    @Override
    public void indexExclusions(IndexExclusionHelper helper) {
//        PonderPlugin.super.indexExclusions(helper);
    }
}
