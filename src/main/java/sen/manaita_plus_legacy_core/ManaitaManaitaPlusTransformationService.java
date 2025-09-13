package sen.manaita_plus_legacy_core;

import cpw.mods.modlauncher.LaunchPluginHandler;
import cpw.mods.modlauncher.Launcher;
import cpw.mods.modlauncher.api.*;
import cpw.mods.modlauncher.serviceapi.ILaunchPluginService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import sen.manaita_plus_legacy_core.transform.ManaitaPlusLaunchPluginService;
import sen.manaita_plus_legacy_core.util.Helper;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class ManaitaManaitaPlusTransformationService implements ITransformationService {
    public static final Logger LOGGER = LogManager.getLogger("ManaitaPlusCore");
    static {
        LaunchPluginHandler handler = Helper.getFieldValue(Launcher.INSTANCE, "launchPlugins", LaunchPluginHandler.class);
        Map<String, ILaunchPluginService> plugins = (Map<String, ILaunchPluginService>) Helper.getFieldValue(handler, "plugins", Map.class);
        Map<String, ILaunchPluginService> newMap = new ConcurrentHashMap<>();
        newMap.put("!!!ManaitaPlus", new ManaitaPlusLaunchPluginService());
        if (plugins != null)
            for (String name : plugins.keySet())
                newMap.put(name, plugins.get(name));
        Helper.setFieldValue(handler, "plugins", newMap);
        Helper.coexistenceCoreAndMod();
    }

    @Override
    public @NotNull String name() {
        return "ManaitaPlusTransformationService";
    }

    @Override
    public void initialize(IEnvironment environment) {
    }

    @Override
    public void onLoad(IEnvironment env, Set<String> otherServices) {
    }

    @Override
    public @NotNull List<ITransformer> transformers() {
        return List.of();
    }
}
