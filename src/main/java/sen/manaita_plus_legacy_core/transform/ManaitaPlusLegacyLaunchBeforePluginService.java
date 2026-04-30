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
    private static final String owner = "sen/manaita_plus_legacy_core/util/EventUtil";
    public static final Map<MethodNode,InsnList> methodI = new HashMap<>();
    public static boolean yuanS;
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
                            methodInsnNode.setOpcode(Opcodes.INVOKESTATIC);
                            methodInsnNode.owner = owner;
                            methodInsnNode.name = "getHealth";
                            methodInsnNode.desc = "(Lnet/minecraft/world/entity/LivingEntity;)F";

//                            InsnList insnNodes = new InsnList();
//                            LabelNode label1 = new LabelNode();
//                            LabelNode label2 = new LabelNode();
//                            LabelNode label3 = new LabelNode();
////                            InsnList entity = new InsnList();
//                            if (methodInsnNode.getPrevious() instanceof VarInsnNode varInsnNode) {
//                                int var = varInsnNode.var;
//                                insnNodes.add(new VarInsnNode(Opcodes.ALOAD, var));
//                                insnNodes.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL, "sen/manaita_plus_legacy/common/util/entity/ManaitaPlusLegacyEntityData", "accept", "(Lnet/minecraft/world/entity/Entity;)Z", false));
//                                insnNodes.add(new JumpInsnNode(Opcodes.IFEQ, label1));
//                                insnNodes.add(new VarInsnNode(Opcodes.ALOAD, var));
//                                insnNodes.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL, "net/minecraft/world/entity/LivingEntity", (isDebug ? "getMaxHealth" : "m_21233_"), "()F", false));
//                                insnNodes.add(new LdcInsnNode(20.0F));
//                                insnNodes.add(new MethodInsnNode(Opcodes.INVOKESTATIC, "java/lang/Math", "max", "(FF)F", false));
//                                insnNodes.add(new JumpInsnNode(Opcodes.GOTO, label2));
//                                insnNodes.add(label1);
//                                insnNodes.add(new FrameNode(Opcodes.F_APPEND, 2, new Object[]{Opcodes.INTEGER, "net/minecraft/world/entity/LivingEntity"}, 0, null));
//
//                                insnNodes.add(new FieldInsnNode(Opcodes.GETSTATIC, "sen/manaita_plus_legacy/common/util/entity/ManaitaPlusLegacyEntityData", "death", "Lsen/manaita_plus_legacy/common/util/entity/ManaitaPlusLegacyEntityData;"));
//                                insnNodes.add(new VarInsnNode(Opcodes.ALOAD, var));
//                                insnNodes.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL, "sen/manaita_plus_legacy/common/util/entity/ManaitaPlusLegacyEntityData", "accept", "(Lnet/minecraft/world/entity/Entity;)Z", false));
//                                insnNodes.add(new JumpInsnNode(Opcodes.IFEQ, label3));
//                                insnNodes.add(new InsnNode(Opcodes.FCONST_0));
//                                insnNodes.add(new JumpInsnNode(Opcodes.GOTO, label2));
//                                insnNodes.add(label3);
//                                insnNodes.add(new FrameNode(Opcodes.F_SAME, 0, null, 0, null));
//                                insnNodes.add(new VarInsnNode(Opcodes.ALOAD, var));
//                                insnNodes.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL, "net/minecraft/world/entity/LivingEntity", methodInsnNode.name, "()F", false));
//                                insnNodes.add(label2);
//                                insnNodes.add(new FrameNode(Opcodes.F_SAME1, 0, null, 1, new Object[]{Opcodes.FLOAT}));
//

//                            method.instructions.set(methodInsnNode.getPrevious(),new LabelNode());
////                        method.instructions.insert(methodInsnNode, insnNodes);
////                        method.instructions.set(methodInsnNode, new FieldInsnNode(Opcodes.GETSTATIC, "sen/manaita_plus_legacy/common/util/entity/ManaitaPlusLegacyEntityData", "manaita", "Lsen/manaita_plus_legacy/common/util/entity/ManaitaPlusLegacyEntityData;"));
//
//                            }
                        }
                    } /*else if (instruction instanceof FieldInsnNode fieldInsnNode) {
                        if (fieldInsnNode.getOpcode() == Opcodes.GETFIELD && fieldInsnNode.owner.equals("net/minecraft/client/Minecraft") && (fieldInsnNode.name.equals("screen") || fieldInsnNode.name.equals("f_91080_")) && fieldInsnNode.desc.equals("Lnet/minecraft/client/gui/screens/Screen;")) {
                            method.instructions.set(fieldInsnNode, new MethodInsnNode(Opcodes.INVOKESTATIC, owner, "setScreen", "(Lnet/minecraft/client/Minecraft;Lnet/minecraft/client/gui/screens/Screen;)V", false));
                        }
                    }*/
                }
            }
        }
        InsnList insnNodes;
        Map<LabelNode, LabelNode> labelNodes = new HashMap<>();
        for (MethodNode method : classNode.methods) {
            insnNodes = new InsnList();
            LabelNode label = new LabelNode();
            insnNodes.add(new InsnNode(Opcodes.ICONST_1));
            insnNodes.add(new JumpInsnNode(Opcodes.IFEQ, label));
            for (AbstractInsnNode instruction : method.instructions) {
                if (instruction instanceof LabelNode labelNode) {
                    labelNodes.put(labelNode, new LabelNode());
                }
                insnNodes.add(instruction.clone(labelNodes));
            }
            insnNodes.add(label);
            insnNodes.add(new FrameNode(Opcodes.F_SAME, 0, null, 0, null));
            methodI.put(method, insnNodes);
        }
        return flag;
    }


}
