package sen.manaita_plus_legacy_core.handler;

import cpw.mods.jarhandling.SecureJar;
import cpw.mods.modlauncher.*;
import cpw.mods.modlauncher.serviceapi.ILaunchPluginService;
import net.minecraft.client.renderer.GameRenderer;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;
import org.jetbrains.annotations.Nullable;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.ClassNode;
import sen.manaita_plus_legacy_core.transform.ManaitaPlusLegacyLaunchBeforePluginService;
import sen.manaita_plus_legacy_core.transform.ManaitaPlusLegacyLaunchPluginService;
import sen.manaita_plus_legacy_core.util.Helper;

import java.lang.reflect.Field;
import java.util.*;


public class ManaitaPlusLegacyPluginHandler extends LaunchPluginHandler {
    private static final Logger LOGGER = Helper.getFieldValue(LaunchPluginHandler.class,"LOGGER", Logger.class);
    private Map<String, ILaunchPluginService> plugins1;
    static final Marker MODLAUNCHER = MarkerManager.getMarker("MODLAUNCHER");
    static final Marker LAUNCHPLUGIN = MarkerManager.getMarker("LAUNCHPLUGIN").addParents(MODLAUNCHER);
    private final LaunchPluginHandler handler;
    public static final Field setFieldValue;

    static {
        try {
            setFieldValue = LaunchPluginHandler.class.getDeclaredField("plugins");
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        }
    }

    public ManaitaPlusLegacyPluginHandler(final ModuleLayerHandler layerHandler,LaunchPluginHandler handler) {
        super(layerHandler);
        this.handler = handler;
        plugins1 = (Map<String, ILaunchPluginService>) Helper.getFieldValue(handler, "plugins", Map.class);
        Helper.setFieldValue(setFieldValue,this,plugins1);
    }

    public Optional<ILaunchPluginService> get(final String name) {
        plugins1 = (Map<String, ILaunchPluginService>) Helper.getFieldValue(handler, "plugins", Map.class);
        Helper.setFieldValue(setFieldValue,this,plugins1);
        return Optional.ofNullable(plugins1.get(name));
    }

    public EnumMap<ILaunchPluginService.Phase, List<ILaunchPluginService>> computeLaunchPluginTransformerSet(final Type className, final boolean isEmpty, final String reason, final TransformerAuditTrail auditTrail) {
        final EnumMap<ILaunchPluginService.Phase, List<ILaunchPluginService>> phaseObjectEnumMap = new EnumMap<>(ILaunchPluginService.Phase.class);
        Set<ILaunchPluginService> uniqueValues = new HashSet<>();
        plugins1 = (Map<String, ILaunchPluginService>) Helper.getFieldValue(handler, "plugins", Map.class);
        Helper.setFieldValue(setFieldValue,this,plugins1);
        phaseObjectEnumMap.computeIfAbsent(ILaunchPluginService.Phase.BEFORE, e -> new ArrayList<>()).add(ManaitaPlusLegacyLaunchBeforePluginService.instance);
        for (ILaunchPluginService plugin : plugins1.values()) {
            if (plugin instanceof ManaitaPlusLegacyLaunchPluginService || plugin instanceof ManaitaPlusLegacyLaunchBeforePluginService) continue;
            if (className.getClassName().startsWith("sen.") && !plugin.getClass().getName().startsWith("net.minecraftforge")) continue;
            for (ILaunchPluginService.Phase ph : plugin.handlesClass(className, isEmpty, reason)) {
                phaseObjectEnumMap.computeIfAbsent(ph, e -> new ArrayList<>()).add(plugin);
                if (uniqueValues.add(plugin)) {
                    plugin.customAuditConsumer(className.getClassName(), strings -> auditTrail.addPluginCustomAuditTrail(className.getClassName(), plugin, strings));
                }
            }
        }
        phaseObjectEnumMap.computeIfAbsent(ILaunchPluginService.Phase.AFTER, e -> new ArrayList<>()).add(ManaitaPlusLegacyLaunchPluginService.instance);
        LOGGER.debug(LAUNCHPLUGIN, "LaunchPluginService {}", ()->phaseObjectEnumMap);
        return phaseObjectEnumMap;
    }

    void offerScanResultsToPlugins(List<SecureJar> scanResults) {
        plugins1.forEach((n,p)->p.addResources(scanResults));
    }

    int offerClassNodeToPlugins(final ILaunchPluginService.Phase phase, final List<ILaunchPluginService> plugins, @Nullable final ClassNode node, final Type className, TransformerAuditTrail auditTrail, final String reason) {
        int flags = 0;
        for (ILaunchPluginService iLaunchPluginService : plugins) {
            LOGGER.debug(LAUNCHPLUGIN, "LauncherPluginService {} offering transform {}", iLaunchPluginService.name(), className.getClassName());
            final int pluginFlags = iLaunchPluginService.processClassWithFlags(phase, node, className, reason);
            if (pluginFlags != ILaunchPluginService.ComputeFlags.NO_REWRITE) {
                auditTrail.addPluginAuditTrail(className.getClassName(), iLaunchPluginService, phase);
                LOGGER.debug(LAUNCHPLUGIN, "LauncherPluginService {} transformed {} with class compute flags {}", iLaunchPluginService.name(), className.getClassName(), pluginFlags);
                flags |= pluginFlags;
            }
        }
        LOGGER.debug(LAUNCHPLUGIN, "Final flags state for {} is {}", className.getClassName(), flags);
        return flags;
    }

}
