package dev.tuxebro.create_horse_decimation;

import com.electronwill.nightconfig.core.file.CommentedFileConfig;
import com.mojang.logging.LogUtils;
import net.neoforged.fml.loading.FMLPaths;
import org.objectweb.asm.tree.ClassNode;
import org.slf4j.Logger;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;

import java.util.List;
import java.util.Set;

public class ModMixinPlugin implements IMixinConfigPlugin {
    public static final Logger LOGGER = LogUtils.getLogger();

    private static boolean isSurvivalFriendlyEnabled() {
        var configBaseDir = FMLPaths.CONFIGDIR.get();
        var serverConfigFileName = CreateHorseDecimation.MOD_ID+"-"+"server.toml";

        var basePath = configBaseDir.resolve(serverConfigFileName);
        var configFile = basePath.toFile();

        if (!configFile.exists()) {
            return false;
        }

        try {
            CommentedFileConfig config = CommentedFileConfig.builder(configFile).build();
            config.load();

            var isSurvivalFriendlyEnabled = config.getOrElse("server.survivalFriendlyEnabled", false);

            config.close();
            return isSurvivalFriendlyEnabled;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public String getRefMapperConfig() {
        return "";
    }

    @Override
    public boolean shouldApplyMixin(String targetClassName, String mixinClassName) {
        if (mixinClassName.equals("dev.tuxebro.create_horse_decimation.mixin.DetectHorseBlockDestroyMixin")) {
            return !isSurvivalFriendlyEnabled();
        }

        return true;
    }

    @Override
    public void acceptTargets(Set<String> myTargets, Set<String> otherTargets) {

    }

    @Override
    public void onLoad(String mixinPackage) {}

    @Override
    public List<String> getMixins() {
        return List.of();
    }

    @Override
    public void preApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {

    }

    @Override
    public void postApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {

    }
}
