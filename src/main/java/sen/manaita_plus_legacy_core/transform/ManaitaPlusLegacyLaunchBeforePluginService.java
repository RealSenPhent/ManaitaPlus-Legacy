package sen.manaita_plus_legacy_core.transform;

import cpw.mods.modlauncher.serviceapi.ILaunchPluginService;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.*;

import java.util.*;

import static sen.manaita_plus_legacy_core.transform.ManaitaPlusLegacyLaunchPluginService.isDebug;

public class ManaitaPlusLegacyLaunchBeforePluginService implements ILaunchPluginService {
    public static final ManaitaPlusLegacyLaunchBeforePluginService instance = new ManaitaPlusLegacyLaunchBeforePluginService();
    private static final String owner = "sen/manaita_plus_legacy_core/util/CallBackHelper";

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
        List<MethodNode> methods = new ArrayList<>();
        for (MethodNode method : classNode.methods) {
            for (AbstractInsnNode instruction : method.instructions) {
                if (instruction instanceof MethodInsnNode methodInsnNode) {
                    if (methodInsnNode.owner.equals("net/minecraft/world/entity/LivingEntity")
                            && (methodInsnNode.name.equals("getHealth") || methodInsnNode.name.equals("m_21223_"))
                            && methodInsnNode.desc.equals("()F") && !methodInsnNode.itf) {
                        flag = true;
                        break;
                    } else if (methodInsnNode.owner.equals("net/minecraft/world/entity/LivingEntity")
                            && (methodInsnNode.name.equals("m_21224_") || methodInsnNode.name.equals("isDeadOrDying"))
                            && methodInsnNode.desc.equals("()Z") && !methodInsnNode.itf) {
                        flag = true;
                        break;
                    } else if (methodInsnNode.owner.equals("net/minecraft/world/entity/LivingEntity")
                            && (methodInsnNode.name.equals("m_6084_") || methodInsnNode.name.equals("isAlive"))
                            && methodInsnNode.desc.equals("()Z") && !methodInsnNode.itf) {
                        flag = true;
                        break;
                    }
                }
            }
            if (flag) {
                methods.add(method);
                flag = false;
            }
        }


        if (!methods.isEmpty()) {
            List<MethodInsnNode> call = new ArrayList<>();
            for (MethodNode method : methods) {
                // 克隆标签映射（原标签 -> 新标签）
                Map<LabelNode, LabelNode> clonedLabels = new InsnMap();
                // 克隆 try-catch 块
                List<TryCatchBlockNode> newTryCatch = new ArrayList<>();
                if (method.tryCatchBlocks != null) {
                    for (TryCatchBlockNode tc : method.tryCatchBlocks) {
                        newTryCatch.add(new TryCatchBlockNode(
                                clonedLabels.get(tc.start),
                                clonedLabels.get(tc.end),
                                clonedLabels.get(tc.handler),
                                tc.type
                        ));
                    }
                }

                // 克隆局部变量表
                List<LocalVariableNode> newVars = new ArrayList<>();
                if (method.localVariables != null) {
                    for (LocalVariableNode lv : method.localVariables) {
                        newVars.add(new LocalVariableNode(
                                lv.name, lv.desc, lv.signature,
                                clonedLabels.get(lv.start), clonedLabels.get(lv.end),
                                lv.index
                        ));
                    }
                }

                // 克隆整个指令列表（此时 method.instructions 已包含修改后的调用）
                InsnList clonedInsn = new InsnList();
                for (AbstractInsnNode ins : method.instructions) {
                    AbstractInsnNode clone = ins.clone(clonedLabels);
                    clonedInsn.add(clone);
                    if (clone instanceof MethodInsnNode methodInsnNode) {
                        if (methodInsnNode.owner.equals("net/minecraft/world/entity/LivingEntity")
                                && (methodInsnNode.name.equals("getHealth") || methodInsnNode.name.equals("m_21223_"))
                                && methodInsnNode.desc.equals("()F") && !methodInsnNode.itf) {
                            methodInsnNode.setOpcode(Opcodes.INVOKESTATIC);
                            methodInsnNode.owner = owner;
                            methodInsnNode.name = "getHealth";
                            methodInsnNode.desc = "(Lnet/minecraft/world/entity/LivingEntity;)F";
                            call.add(methodInsnNode);
                        } else if (methodInsnNode.owner.equals("net/minecraft/world/entity/LivingEntity")
                                && (methodInsnNode.name.equals("m_21224_") || methodInsnNode.name.equals("isDeadOrDying"))
                                && methodInsnNode.desc.equals("()Z") && !methodInsnNode.itf) {
                            methodInsnNode.setOpcode(Opcodes.INVOKESTATIC);
                            methodInsnNode.owner = owner;
                            methodInsnNode.name = "isDeadOrDying";
                            methodInsnNode.desc = "(Lnet/minecraft/world/entity/LivingEntity;)Z";
                            call.add(methodInsnNode);
                        } else if (methodInsnNode.owner.equals("net/minecraft/world/entity/LivingEntity")
                                && (methodInsnNode.name.equals("m_6084_") || methodInsnNode.name.equals("isAlive"))
                                && methodInsnNode.desc.equals("()Z") && !methodInsnNode.itf) {
                            methodInsnNode.setOpcode(Opcodes.INVOKESTATIC);
                            methodInsnNode.owner = owner;
                            methodInsnNode.name = "isAlive";
                            methodInsnNode.desc = "(Lnet/minecraft/world/entity/LivingEntity;)Z";
                            call.add(methodInsnNode);
                        }
                    }
                }

                ManaitaPlusLegacyLaunchBeforeApplyPluginService.methodI.put(
                        method,
                        new MethodNodeArg(clonedInsn, new HashMap<>(clonedLabels), newTryCatch, newVars, new HashSet<>(call))
                );
            }
        }

        return false;
    }

    public record MethodNodeArg(InsnList instructions,
                                Map<LabelNode, LabelNode> labelNodeLabelNodeMap,
                                List<TryCatchBlockNode> newTryCatch,
                                List<LocalVariableNode> newVars,
                                Set<MethodInsnNode> methodInsnCallBack) {}

    public static class InsnMap extends HashMap<LabelNode, LabelNode> {
        @Override
        public LabelNode get(Object key) {
            LabelNode newLabel = super.get(key);
            if (newLabel == null) {
                newLabel = new LabelNode();
                put((LabelNode) key, newLabel);
            }
            return newLabel;
        }
    }
}