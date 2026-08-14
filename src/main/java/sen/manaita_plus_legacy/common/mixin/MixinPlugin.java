package sen.manaita_plus_legacy.common.mixin;

import cpw.mods.modlauncher.*;
import cpw.mods.modlauncher.api.ITransformer;
import cpw.mods.modlauncher.serviceapi.ILaunchPluginService;
import it.unimi.dsi.fastutil.objects.ObjectIterators;
import org.jetbrains.annotations.NotNull;
import org.objectweb.asm.tree.ClassNode;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;
import sen.manaita_plus_legacy_core.ManaitaPlusTransformationService;
import sen.manaita_plus_legacy_core.handler.ManaitaPlusLegacyPluginHandler;
import sen.manaita_plus_legacy_core.transform.ManaitaPlusLegacyLaunchBeforeApplyPluginService;
import sen.manaita_plus_legacy_core.transform.ManaitaPlusLegacyLaunchBeforePluginService;
import sen.manaita_plus_legacy_core.transform.ManaitaPlusLegacyLaunchPluginService;
import sen.manaita_plus_legacy_core.transform.ManaitaPlusLegacyLaunchTransformer;
import sen.manaita_plus_legacy_core.util.CallBackHelper;
import sen.manaita_plus_legacy_core.util.ClientEventUtil;
import sen.manaita_plus_legacy_core.util.EventUtil;
import sen.manaita_plus_legacy_core.util.Helper;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

public class MixinPlugin implements IMixinConfigPlugin {
    static {
        init();
        {
            ManaitaPlusLegacyLaunchPluginService.class.getName();
            ManaitaPlusLegacyLaunchBeforePluginService.class.getName();
            ManaitaPlusLegacyLaunchBeforeApplyPluginService.class.getName();
            ClientEventUtil.class.getName();
            EventUtil.class.getName();
            CallBackHelper.class.getName();
            ManaitaPlusLegacyLaunchPluginService.init();
//            ManaitaPlusLegacyLaunchTransformer.class.getName();
            ManaitaPlusTransformationService.class.getName();
            TransformingClassLoader classLoader = Helper.getFieldValue(Launcher.INSTANCE, "classLoader", TransformingClassLoader.class);
            ModuleLayerHandler moduleLayerHandler = Helper.getFieldValue(Launcher.INSTANCE, "moduleLayerHandler", ModuleLayerHandler.class);
            ClassTransformer transformingClassLoader = Helper.getFieldValue(classLoader,"classTransformer",ClassTransformer.class);
            LaunchPluginHandler pluginHandler = Helper.getFieldValue(transformingClassLoader,"pluginHandler",LaunchPluginHandler.class);
            Helper.setFieldValue(transformingClassLoader,"pluginHandler",new ManaitaPlusLegacyPluginHandler(moduleLayerHandler,pluginHandler));

            Map<String, ILaunchPluginService> plugins = (Map<String, ILaunchPluginService>) Helper.getFieldValue(pluginHandler, "plugins", Map.class);
            Map<String, ILaunchPluginService> newMap = new HashMap<>();
            if (plugins != null) {
                for (String name : plugins.keySet()) {
                    ILaunchPluginService value = plugins.get(name);
                    if (value instanceof ManaitaPlusLegacyLaunchPluginService
//                            || value instanceof ManaitaPlusLegacyLaunchBeforeApplyPluginService
//                            || value instanceof ManaitaPlusLegacyLaunchBeforePluginService
                    ) continue;
                    newMap.put(name, value);
                }
            }
//            newMap.put("ManaitaPlusLegacyBefore", new ManaitaPlusLegacyLaunchBeforePluginService());
//            newMap.put("ManaitaPlusLegacyBeforeApply", new ManaitaPlusLegacyLaunchBeforeApplyPluginService());
            newMap.put("ManaitaPlusLegacy", new ManaitaPlusLegacyLaunchPluginService());
            Helper.setFieldValue(pluginHandler, "plugins", newMap);
        }


        LaunchPluginHandler handler = Helper.getFieldValue(Launcher.INSTANCE, "launchPlugins", LaunchPluginHandler.class);
        Map<String, ILaunchPluginService> plugins = (Map<String, ILaunchPluginService>) Helper.getFieldValue(handler, "plugins", Map.class);
        Map<String, ILaunchPluginService> newMap = new HashMap<>();
        if (plugins != null) {
            for (String name : plugins.keySet()) {
                ILaunchPluginService value = plugins.get(name);
                if (value instanceof ManaitaPlusLegacyLaunchPluginService
//                        || value instanceof ManaitaPlusLegacyLaunchBeforeApplyPluginService
//                        || value instanceof ManaitaPlusLegacyLaunchBeforePluginService
                ) continue;
                newMap.put(name, value);
            }
        }
//        newMap.put("ManaitaPlusLegacyBefore", new ManaitaPlusLegacyLaunchBeforePluginService());
//        newMap.put("ManaitaPlusLegacyBeforeApply", new ManaitaPlusLegacyLaunchBeforeApplyPluginService());
        newMap.put("ManaitaPlusLegacy", new ManaitaPlusLegacyLaunchPluginService());
        Helper.setFieldValue(handler, "plugins", newMap);

//        try {
//            Method declaredMethod = Class.class.getDeclaredMethod("getDeclaredFields0", boolean.class);
//            declaredMethod.setAccessible(true);
//
//            ManaitaPlusLegacyReflection.declaredMethod = declaredMethod;
//            Class<?> aClass = Class.forName("jdk.internal.reflect.Reflection");
//            Object x = declaredMethod.invoke(aClass,false);
//            if (x instanceof Field[] fields) {
//                Unsafe unsafe = Helper.UNSAFE;
//                Set<String> ALL_MEMBERS = Set.of("*");
//                for (Field f : fields) {
//                    Object objectVolatile;
//                    if (Modifier.isStatic(f.getModifiers())) {
//                        if (f.getType() != int.class &&
//                                f.getType() != boolean.class &&
//                                f.getType() != float.class &&
//                                f.getType() != double.class &&
//                                f.getType() != long.class &&
//                                f.getType() != byte.class &&
//                                f.getType() != short.class &&
//                                f.getType() != char.class
//                        )  {
//                            objectVolatile = unsafe.getObjectVolatile(aClass, unsafe.staticFieldOffset(f));
//                            if (objectVolatile instanceof Map filterMap) {
//                                filterMap.put(Helper.class,ALL_MEMBERS);
//                                filterMap.put(ManaitaPlusLegacyTagData.class,ALL_MEMBERS);
//                                filterMap.put(ManaitaPlusLegacyEntityData.class,ALL_MEMBERS);
//                                filterMap.put(ManaitaPlusLegacyReflection.class,ALL_MEMBERS);
//                            }
//                        }
//                    }
//                }
//            }
//        } catch (NoSuchMethodException | ClassNotFoundException | InvocationTargetException | IllegalAccessException e) {
//            throw new RuntimeException(e);
//        }
    }

    public static void init() {

    }

    @Override
    public void onLoad(String mixinPackage) {

    }

    @Override
    public String getRefMapperConfig() {
        return null;
    }

    @Override
    public boolean shouldApplyMixin(String targetClassName, String mixinClassName) {
        return true;
    }

    @Override
    public void acceptTargets(Set<String> myTargets, Set<String> otherTargets) {

    }

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
