package dev.tuxebro.create_horse_decimation.compat;

import dev.tuxebro.create_horse_decimation.compat.sable.PossibleSableCompat;
import dev.tuxebro.create_horse_decimation.compat.sable.SableCompat;
import net.neoforged.fml.ModList;

public class ModCompat {
    private static final String SABLE_MOD_ID = "sable";
    private static final boolean IS_SABLE_LOADED = ModList.get().isLoaded(SABLE_MOD_ID);

   private static PossibleSableCompat createSableCompat() {
        if (IS_SABLE_LOADED) return new SableCompat();
        else return new PossibleSableCompat();
   }

   public static PossibleSableCompat Sable =  createSableCompat();
}
