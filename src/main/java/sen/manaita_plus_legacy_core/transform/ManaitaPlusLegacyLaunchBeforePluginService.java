package sen.manaita_plus_legacy_core.transform;

import cpw.mods.modlauncher.serviceapi.ILaunchPluginService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.*;

import java.lang.reflect.Method;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

import static sen.manaita_plus_legacy_core.transform.ManaitaPlusLegacyLaunchPluginService.isDebug;


public class ManaitaPlusLegacyLaunchBeforePluginService implements ILaunchPluginService {
    public static final ManaitaPlusLegacyLaunchBeforePluginService instance = new ManaitaPlusLegacyLaunchBeforePluginService();
    private static final String owner = "sen/manaita_plus_legacy_core/util/CallBackHelper";
    public static final Map<MethodNode,InsnList> methodI = new HashMap<>();
    @Override
    public String name() {
        return "ManaitaPlusLegacyLaunchBeforePluginService";
    }

    @Override
    public EnumSet<Phase> handlesClass(Type classType, boolean isEmpty) {
        return EnumSet.of(Phase.BEFORE);
    }

    @Override
    public boolean processClass(Phase phase, ClassNode classNode, Type classType) {
        if (classNode.name.startsWith("sen/")) return false;
        boolean flag = false;
        if ("net/minecraft/client/Minecraft".equals(classNode.name)) {
            System.err.println("TTTTesBBBB");
        }
        {
            for (MethodNode method : classNode.methods) {
                for (int i = 0; i < method.instructions.size(); i++) {
                    AbstractInsnNode instruction = method.instructions.get(i);
                    if (instruction instanceof MethodInsnNode methodInsnNode) {
                        if (methodInsnNode.owner.equals("net/minecraft/world/entity/LivingEntity") && (methodInsnNode.name.equals("getHealth") || methodInsnNode.name.equals("m_21223_")) && methodInsnNode.desc.equals("()F") && !methodInsnNode.itf) {
//                            System.err.println(classNode.name + " method " + method.name + " is invalid " + methodInsnNode.name);
                            methodInsnNode.setOpcode(Opcodes.INVOKESTATIC);
                            methodInsnNode.owner = owner;
                            methodInsnNode.name = "getHealth";
                            methodInsnNode.desc = "(Lnet/minecraft/world/entity/LivingEntity;)F";
                            flag = true;
                        } else if (methodInsnNode.owner.equals("net/minecraft/world/entity/LivingEntity") && (methodInsnNode.name.equals("m_21224_") || methodInsnNode.name.equals("isDeadOrDying")) && methodInsnNode.desc.equals("()Z")) {
//                            System.err.println(classNode.name + " method " + method.name + " is invalid " + methodInsnNode.name);
                            methodInsnNode.setOpcode(Opcodes.INVOKESTATIC);
                            methodInsnNode.owner = owner;
                            methodInsnNode.name = "isDeadOrDying";
                            methodInsnNode.desc = "(Lnet/minecraft/world/entity/LivingEntity;)Z";
                            flag = true;
                        } else if (methodInsnNode.owner.equals("net/minecraft/world/entity/LivingEntity") && (methodInsnNode.name.equals("m_6084_") || methodInsnNode.name.equals("isAlive")) && methodInsnNode.desc.equals("()Z")) {
//                            System.err.println(classNode.name + " method " + method.name + " is invalid " + methodInsnNode.name);
                            methodInsnNode.setOpcode(Opcodes.INVOKESTATIC);
                            methodInsnNode.owner = owner;
                            methodInsnNode.name = "isAlive";
                            methodInsnNode.desc = "(Lnet/minecraft/world/entity/LivingEntity;)Z";
                            flag = true;
                        }


                        /*else if (instruction instanceof FieldInsnNode fieldInsnNode) {
                        if (fieldInsnNode.getOpcode() == Opcodes.GETFIELD && fieldInsnNode.owner.equals("net/minecraft/client/Minecraft") && (fieldInsnNode.name.equals("screen") || fieldInsnNode.name.equals("f_91080_")) && fieldInsnNode.desc.equals("Lnet/minecraft/client/gui/screens/Screen;")) {
                            method.instructions.set(fieldInsnNode, new MethodInsnNode(Opcodes.INVOKESTATIC, owner, "setScreen", "(Lnet/minecraft/client/Minecraft;Lnet/minecraft/client/gui/screens/Screen;)V", false));
                        }
                    }*/
                    }
                }
            }
        }
        return flag;
    }


}
