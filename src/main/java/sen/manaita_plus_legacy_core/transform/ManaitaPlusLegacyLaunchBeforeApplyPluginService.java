package sen.manaita_plus_legacy_core.transform;

import cpw.mods.modlauncher.serviceapi.ILaunchPluginService;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.*;
import org.objectweb.asm.tree.analysis.Frame;

import java.util.*;

public class ManaitaPlusLegacyLaunchBeforeApplyPluginService implements ILaunchPluginService {
    public static final ManaitaPlusLegacyLaunchBeforeApplyPluginService instance = new ManaitaPlusLegacyLaunchBeforeApplyPluginService();
    public static final Map<MethodNode, ManaitaPlusLegacyLaunchBeforePluginService.MethodNodeArg> methodI = new HashMap<>();

    ManaitaPlusLegacyLaunchBeforeApplyPluginService() {}
    @Override
    public String name() {
        return "ManaitaPlusLegacyLaunchBeforeApplyPluginService";
    }

    @Override
    public EnumSet<Phase> handlesClass(Type classType, boolean isEmpty) {
        return EnumSet.of(Phase.AFTER);
    }

    @Override
    public boolean processClass(Phase phase, ClassNode classNode, Type classType) {
        if (classNode.name.startsWith("sen/")) return false;

        boolean flag = false;
        for (int i1 = 0; i1 < classNode.methods.size(); i1++) {
            MethodNode method = classNode.methods.get(i1);

            ManaitaPlusLegacyLaunchBeforePluginService.MethodNodeArg arg = methodI.remove(method);
            if (arg == null) continue;

            InsnList savedInstructions = arg.instructions();
            Set<MethodInsnNode> methodInsnNodes = arg.methodInsnCallBack();
            Map<LabelNode, LabelNode> originalToNewMap = arg.labelNodeLabelNodeMap();
            Map<LabelNode, LabelNode> newToOriginalMap = new ManaitaPlusLegacyLaunchBeforePluginService.InsnMap();

            for (Map.Entry<LabelNode, LabelNode> e : originalToNewMap.entrySet()) {
                newToOriginalMap.put(e.getValue(), e.getKey());
            }

            // ---------- 修复后的锚点查找 ----------
            LabelNode beforeSenOriginal = null;
            boolean foundCallBack = false;
            List<LabelNode> beginEnds = new ArrayList<>();
            for (AbstractInsnNode ins : savedInstructions) {
                if (ins instanceof LabelNode label) {
                    if (!newToOriginalMap.containsKey(label)) {
                        System.err.println("What????" + classNode.name + " . " + method.name + method.desc);
                        break;
                    }
                    LabelNode orig = newToOriginalMap.get(label);
                    if (foundCallBack)  {
                        // 遇到 sen 调用后第一个映射标签，记录并停止
                        beginEnds.add(beforeSenOriginal);
                        beginEnds.add(orig);
                        foundCallBack = false;
                    }
                    beforeSenOriginal = orig;
                } else if (foundCallBack) {
                    continue;
                } else if (ins instanceof MethodInsnNode methodInsnNode && methodInsnNodes.contains(methodInsnNode)) {
                    foundCallBack = true;
                }
            }

            if (beginEnds.isEmpty()) {
                System.err.println(classNode.name + " . " + method.name + method.desc + " no anchors");
                continue;
            }

            Set<LabelNode> removedLabels = new HashSet<>();
            for (int i = 0; i < beginEnds.size(); i+=2) {
                LabelNode b = beginEnds.get(i);
                LabelNode a = beginEnds.get(i + 1);
                AbstractInsnNode currentBefore = null;
                AbstractInsnNode currentAfter = null;
                LabelNode beforeSenNew = originalToNewMap.get(b);
                LabelNode afterSenNew = originalToNewMap.get(a);
                for (AbstractInsnNode ins : method.instructions) {
                    if (!(ins instanceof LabelNode label))
                        continue;
                    if (ins == b) {
                        currentBefore = ins;
                    } else if (ins == a) {
                        currentAfter = ins;
                        break;
                    } else if (currentBefore != null) {
//                        InsnList insnNodes = method.instructions;
//                        method.instructions = arg.instructions();
//                        String str = method.name + method.desc;
//                        saveClassNode(classNode);
//                        method.instructions = insnNodes;
//                        saveClassNode(classNode,str);
                        if (originalToNewMap.containsKey(label)) System.err.println("LMLLLL");
                        System.err.println("Found that adding a method's LabelNode resulted in failure." + classNode.name + " . " + method.name + method.desc);
                        break;
                    }
                }
                if (currentBefore == null || currentAfter == null) {
                    System.err.println(classNode.name + " . " + method.name + method.desc + " labels missing in current method");
                    continue;
                }
                InsnList patch = new InsnList();
                boolean copying = false;
                for (AbstractInsnNode ins : savedInstructions) {
                    if (ins == beforeSenNew) {
                        copying = true;
                        continue;
                    } else if (ins == afterSenNew) {
                        break;
                    }
                    if (copying) {
                        patch.add(ins.clone(newToOriginalMap));
                    }
                }

                Iterator<AbstractInsnNode> it = method.instructions.iterator();
                boolean deleting = false;
                while (it.hasNext()) {
                    AbstractInsnNode ins = it.next();
                    if (ins == currentBefore) {
                        deleting = true;
                        continue; // 保留 currentBefore 作为插入点
                    } else if (ins == currentAfter) {
                        break; // 保留 currentAfter，不删除
                    }
                    if (deleting) {
                        if (ins instanceof LabelNode label) {
                            removedLabels.add(label);
                        }
                        it.remove();
                    }
                }
                // 在 currentBefore 之后插入补丁
                method.instructions.insert(currentBefore, patch);
            }

            ListIterator<AbstractInsnNode> iterator = method.instructions.iterator();
            while (iterator.hasNext()) {
                AbstractInsnNode ins = iterator.next();
                if (ins instanceof FrameNode frameNode) {
                    iterator.remove();
                }
            }
            // 清理局部变量表
            if (method.localVariables != null) {
                List<LocalVariableNode> newVars = new ArrayList<>();
                for (LocalVariableNode lv : method.localVariables) {
                    if (removedLabels.contains(lv.start) || removedLabels.contains(lv.end)) {
                        continue;
                    }
                    newVars.add(lv);
                }
                method.localVariables = newVars;
            }

            // 清理异常表
            if (method.tryCatchBlocks != null) {
                List<TryCatchBlockNode> newTryCatch = new ArrayList<>();
                for (TryCatchBlockNode tc : method.tryCatchBlocks) {
                    if (removedLabels.contains(tc.start) || removedLabels.contains(tc.end) || removedLabels.contains(tc.handler)) {
                        continue;
                    }
                    newTryCatch.add(tc);
                }
                method.tryCatchBlocks = newTryCatch;
            }


            System.err.println(classNode.name + " . " + method.name + method.desc + " merged successfully");
            flag = true;
        }

        return flag;
    }
}