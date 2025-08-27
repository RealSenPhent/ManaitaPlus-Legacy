package sen.manaita_plus_core.transform;

import cpw.mods.modlauncher.serviceapi.ILaunchPluginService;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.*;

import java.lang.reflect.Method;
import java.util.*;



public class ManaitaPlusLaunchPluginService implements ILaunchPluginService {

    private static final String owner = "sen/manaita_plus_core/util/EventUtil";
    @Override
    public String name() {
        return "ManaitaLaunchPluginService";
    }

    @Override
    public EnumSet<Phase> handlesClass(Type classType, boolean isEmpty) {
        return EnumSet.of(Phase.AFTER);
    }

    @Override
    public int processClassWithFlags(Phase phase, ClassNode classNode, Type classType, String reason) {
        return ILaunchPluginService.super.processClassWithFlags(phase, classNode, classType, reason);
    }

    @Override
    public boolean processClass(Phase phase, ClassNode classNode, Type classType) {
        if (classNode.name.startsWith("sen/")) return false;
        boolean flag = false;
        if ("net/minecraftclient/player/LocalPlayer".equals(classNode.name)) {
            for (MethodNode method : classNode.methods) {
                if (method.name.equals("m_108632_") && method.desc.equals("()Z")) {
                    InsnList insnNodes = new InsnList();
                    LabelNode label1 = new LabelNode();
                    LabelNode label2 = new LabelNode();
                    insnNodes.add(new VarInsnNode(Opcodes.ALOAD,0));
                    insnNodes.add(new MethodInsnNode(Opcodes.INVOKESTATIC,owner,"isManaita","(Lnet/minecraftclient/player/LocalPlayer;)Z",false));
                    insnNodes.add(new JumpInsnNode(Opcodes.IFEQ,label1));
                    insnNodes.add(new InsnNode(Opcodes.ICONST_0));
                    insnNodes.add(new InsnNode(Opcodes.IRETURN));
                    insnNodes.add(label1);
                    insnNodes.add(new VarInsnNode(Opcodes.ALOAD,0));
                    insnNodes.add(new MethodInsnNode(Opcodes.INVOKESTATIC,owner,"isDead","(Lnet/minecraftclient/player/LocalPlayer;)Z",false));
                    insnNodes.add(new JumpInsnNode(Opcodes.IFEQ,label2));
                    insnNodes.add(new InsnNode(Opcodes.ICONST_1));
                    insnNodes.add(new InsnNode(Opcodes.IRETURN));
                    insnNodes.add(label2);
                    method.instructions.insert(insnNodes);
                    flag = true;
                }
            }
        } else if ("net/minecraft/world/level/entity/EntityLookup".equals(classNode.name)) {
            for (MethodNode method : classNode.methods) {
                if (method.name.equals("m_156814_") && method.desc.equals("(Lnet/minecraft/world/level/entity/EntityAccess;)V")) {
                    InsnList insnNodes = new InsnList();
                    LabelNode label1 = new LabelNode();
                    insnNodes.add(new VarInsnNode(Opcodes.ALOAD, 1));
                    insnNodes.add(new TypeInsnNode(Opcodes.INSTANCEOF, "net/minecraft/world/entity/Entity"));
                    insnNodes.add(new JumpInsnNode(Opcodes.IFEQ, label1));
                    insnNodes.add(new VarInsnNode(Opcodes.ALOAD, 1));
                    insnNodes.add(new TypeInsnNode(Opcodes.CHECKCAST, "net/minecraft/world/entity/Entity"));
                    insnNodes.add(new MethodInsnNode(Opcodes.INVOKESTATIC, owner, "isRemove", "(Lnet/minecraft/world/entity/Entity;)Z", false));
                    insnNodes.add(new JumpInsnNode(Opcodes.IFEQ, label1));
                    insnNodes.add(new InsnNode(Opcodes.RETURN));
                    insnNodes.add(label1);
                    insnNodes.add(new FrameNode(Opcodes.F_SAME, 0, null, 0, null));
                    method.instructions.insert(insnNodes);
                    flag = true;
                } else if ((method.name.equals("m_260822_") && method.desc.equals("(Lnet/minecraft/world/level/entity/EntityTypeTest;Lnet/minecraft/util/AbortableIterationConsumer;)V")) || (method.name.equals("m_156811_") && method.desc.equals("()V"))) {
                    InsnList insnNodes = new InsnList();

                    // 计算局部变量索引 - 确保不会与现有局部变量冲突
                    int baseLocals =  1; // 静态方法没有this参数
                    // 加上方法参数占用的局部变量槽位
                    Type[] argTypes = Type.getArgumentTypes(method.desc);
                    for (Type argType : argTypes) {
                        baseLocals += argType.getSize();
                    }

                    int iterator = baseLocals;
                    int next = baseLocals + 1;
                    method.maxLocals = Math.max(method.maxLocals, next + 1); // 确保maxLocals足够大

                    LabelNode label1 = new LabelNode();
                    LabelNode label2 = new LabelNode();
                    LabelNode label3 = new LabelNode();

                    insnNodes.add(new VarInsnNode(Opcodes.ALOAD, 0));
                    insnNodes.add(new FieldInsnNode(Opcodes.GETFIELD, classNode.name, "f_156807_", "Lit/unimi/dsi/fastutil/ints/Int2ObjectMap;"));
                    insnNodes.add(new MethodInsnNode(Opcodes.INVOKEINTERFACE, "it/unimi/dsi/fastutil/ints/Int2ObjectMap", "values", "()Lit/unimi/dsi/fastutil/objects/ObjectCollection;", true));
                    insnNodes.add(new MethodInsnNode(Opcodes.INVOKEINTERFACE, "it/unimi/dsi/fastutil/objects/ObjectCollection", "iterator", "()Lit/unimi/dsi/fastutil/objects/ObjectIterator;", true));
                    insnNodes.add(new VarInsnNode(Opcodes.ASTORE, iterator));
                    insnNodes.add(label1);
                    insnNodes.add(new FrameNode(Opcodes.F_APPEND, 1, new Object[]{"it/unimi/dsi/fastutil/objects/ObjectIterator"}, 0, null));
                    insnNodes.add(new VarInsnNode(Opcodes.ALOAD, iterator));
                    insnNodes.add(new MethodInsnNode(Opcodes.INVOKEINTERFACE, "it/unimi/dsi/fastutil/objects/ObjectIterator", "hasNext", "()Z", true));
                    insnNodes.add(new JumpInsnNode(Opcodes.IFEQ, label2));
                    insnNodes.add(new VarInsnNode(Opcodes.ALOAD, iterator));
                    insnNodes.add(new MethodInsnNode(Opcodes.INVOKEINTERFACE, "it/unimi/dsi/fastutil/objects/ObjectIterator", "next", "()Ljava/lang/Object;", true));
                    insnNodes.add(new VarInsnNode(Opcodes.ASTORE, next));
                    insnNodes.add(new VarInsnNode(Opcodes.ALOAD, next));
                    insnNodes.add(new TypeInsnNode(Opcodes.INSTANCEOF, "net/minecraft/world/entity/Entity"));
                    insnNodes.add(new JumpInsnNode(Opcodes.IFEQ, label3));
                    insnNodes.add(new VarInsnNode(Opcodes.ALOAD, next));
                    insnNodes.add(new TypeInsnNode(Opcodes.CHECKCAST, "net/minecraft/world/entity/Entity"));
                    insnNodes.add(new MethodInsnNode(Opcodes.INVOKESTATIC, owner, "isRemove", "(Lnet/minecraft/world/entity/Entity;)Z", false));
                    insnNodes.add(new JumpInsnNode(Opcodes.IFEQ, label3));
                    insnNodes.add(new VarInsnNode(Opcodes.ALOAD, iterator));
                    insnNodes.add(new MethodInsnNode(Opcodes.INVOKEINTERFACE, "it/unimi/dsi/fastutil/objects/ObjectIterator", "remove", "()V", true));
                    insnNodes.add(label3);
                    insnNodes.add(new FrameNode(Opcodes.F_SAME, 0, null, 0, null));
                    insnNodes.add(new JumpInsnNode(Opcodes.GOTO, label1));
                    insnNodes.add(label2);
                    insnNodes.add(new FrameNode(Opcodes.F_CHOP, 1, null, 0, null));
                    method.instructions.insert(insnNodes);
                    flag = true;
                }
            }
        } else if ("net/minecraft/util/ClassInstanceMultiMap".equals(classNode.name)) {
            for (MethodNode method : classNode.methods) {
                if (method.name.equals("add")) {
                    InsnList insnNodes = new InsnList();

                    insnNodes.add(new VarInsnNode(Opcodes.ALOAD, 1));

                    method.instructions.insert(insnNodes);
                    flag = true;
                }
                if ((method.name.equals("iterator") && method.desc.equals("()Ljava/util/Iterator;")) || (method.name.equals("m_13532_") && method.desc.equals("()Ljava/util/List;"))) {
                    InsnList insnNodes = new InsnList();

                    insnNodes.add(new VarInsnNode(Opcodes.ALOAD, 0));
                    insnNodes.add(new FieldInsnNode(Opcodes.GETFIELD, classNode.name, "f_13529_", "Ljava/util/List;"));
                    insnNodes.add(new MethodInsnNode(Opcodes.INVOKESTATIC, owner, "onIterator", "(Ljava/util/List;)V", false));

                    method.instructions.insert(insnNodes);
                    flag = true;
                } else if (method.name.equals("m_13533_") && method.desc.equals("(Ljava/lang/Class;)Ljava/util/Collection;")) {
                    InsnList insnNodes = new InsnList();

                    insnNodes.add(new VarInsnNode(Opcodes.ALOAD, 0));
                    insnNodes.add(new FieldInsnNode(Opcodes.GETFIELD, classNode.name, "f_13527_", "Ljava/util/Map;"));
                    insnNodes.add(new MethodInsnNode(Opcodes.INVOKESTATIC, owner, "onFind", "(Ljava/util/Map;)V", false));

                    method.instructions.insert(insnNodes);
                    flag = true;
                }
            }
        } else if ("Lnet/minecraft/world/level/entity/EntityTickList".equals(classNode.name)) {
            for (MethodNode method : classNode.methods) {
                if (method.name.equals("m_156910_") && method.desc.equals("(Ljava/util/function/Consumer;)V")) {
                    InsnList insnNodes = new InsnList();

                    // 计算局部变量索引 - 确保不会与现有局部变量冲突
                    int baseLocals =  1; // 静态方法没有this参数
                    // 加上方法参数占用的局部变量槽位
                    Type[] argTypes = Type.getArgumentTypes(method.desc);
                    for (Type argType : argTypes) {
                        baseLocals += argType.getSize();
                    }

                    int iterator = baseLocals;
                    int next = baseLocals + 1;
                    method.maxLocals = Math.max(method.maxLocals, next + 1); // 确保maxLocals足够大

                    LabelNode label1 = new LabelNode();
                    LabelNode label2 = new LabelNode();
                    LabelNode label3 = new LabelNode();

                    insnNodes.add(new VarInsnNode(Opcodes.ALOAD, 0));
                    insnNodes.add(new FieldInsnNode(Opcodes.GETFIELD, classNode.name, "f_156903_", "Lit/unimi/dsi/fastutil/ints/Int2ObjectMap;"));
                    insnNodes.add(new MethodInsnNode(Opcodes.INVOKEINTERFACE, "it/unimi/dsi/fastutil/ints/Int2ObjectMap", "values", "()Lit/unimi/dsi/fastutil/objects/ObjectCollection;", true));
                    insnNodes.add(new MethodInsnNode(Opcodes.INVOKEINTERFACE, "it/unimi/dsi/fastutil/objects/ObjectCollection", "iterator", "()Lit/unimi/dsi/fastutil/objects/ObjectIterator;", true));
                    insnNodes.add(new VarInsnNode(Opcodes.ASTORE, iterator));
                    insnNodes.add(label1);
                    insnNodes.add(new FrameNode(Opcodes.F_APPEND, 1, new Object[]{"it/unimi/dsi/fastutil/objects/ObjectIterator"}, 0, null));
                    insnNodes.add(new VarInsnNode(Opcodes.ALOAD, iterator));
                    insnNodes.add(new MethodInsnNode(Opcodes.INVOKEINTERFACE, "it/unimi/dsi/fastutil/objects/ObjectIterator", "hasNext", "()Z", true));
                    insnNodes.add(new JumpInsnNode(Opcodes.IFEQ, label2));
                    insnNodes.add(new VarInsnNode(Opcodes.ALOAD, iterator));
                    insnNodes.add(new MethodInsnNode(Opcodes.INVOKEINTERFACE, "it/unimi/dsi/fastutil/objects/ObjectIterator", "next", "()Ljava/lang/Object;", true));
                    insnNodes.add(new VarInsnNode(Opcodes.ASTORE, next));
                    insnNodes.add(new VarInsnNode(Opcodes.ALOAD, next));
                    insnNodes.add(new TypeInsnNode(Opcodes.INSTANCEOF, "net/minecraft/world/entity/Entity"));
                    insnNodes.add(new JumpInsnNode(Opcodes.IFEQ, label3));
                    insnNodes.add(new VarInsnNode(Opcodes.ALOAD, next));
                    insnNodes.add(new TypeInsnNode(Opcodes.CHECKCAST, "net/minecraft/world/entity/Entity"));
                    insnNodes.add(new MethodInsnNode(Opcodes.INVOKESTATIC, owner, "isRemove", "(Lnet/minecraft/world/entity/Entity;)Z", false));
                    insnNodes.add(new JumpInsnNode(Opcodes.IFEQ, label3));
                    insnNodes.add(new VarInsnNode(Opcodes.ALOAD, iterator));
                    insnNodes.add(new MethodInsnNode(Opcodes.INVOKEINTERFACE, "it/unimi/dsi/fastutil/objects/ObjectIterator", "remove", "()V", true));
                    insnNodes.add(label3);
                    insnNodes.add(new FrameNode(Opcodes.F_SAME, 0, null, 0, null));
                    insnNodes.add(new JumpInsnNode(Opcodes.GOTO, label1));
                    insnNodes.add(label2);
                    insnNodes.add(new FrameNode(Opcodes.F_CHOP, 1, null, 0, null));
                    method.instructions.insert(insnNodes);
                    flag = true;
                }
            }
        } else if ("net/minecraft/world/entity/Entity".equals(classNode.name)) {
            for (MethodNode method : classNode.methods) {
                if (method.name.equals("m_213877_") && method.desc.equals("()Z")) {
                    InsnList insnNodes = new InsnList();
                    LabelNode label1 = new LabelNode();
                    LabelNode label2 = new LabelNode();
                    insnNodes.add(new VarInsnNode(Opcodes.ALOAD, 0));
                    insnNodes.add(new MethodInsnNode(Opcodes.INVOKESTATIC,owner,"isManaita","(Lnet/minecraft/world/entity/Entity;)Z",false));
                    insnNodes.add(new JumpInsnNode(Opcodes.IFEQ,label1));
                    insnNodes.add(new InsnNode(Opcodes.ICONST_0));
                    insnNodes.add(new InsnNode(Opcodes.IRETURN));
                    insnNodes.add(label1);
                    insnNodes.add(new FrameNode(Opcodes.F_SAME, 0, null, 0, null));
                    insnNodes.add(new VarInsnNode(Opcodes.ALOAD, 0));
                    insnNodes.add(new MethodInsnNode(Opcodes.INVOKESTATIC,owner,"isRemove","(Lnet/minecraft/world/entity/Entity;)Z",false));
                    insnNodes.add(new JumpInsnNode(Opcodes.IFEQ,label2));
                    insnNodes.add(new InsnNode(Opcodes.ICONST_1));
                    insnNodes.add(new InsnNode(Opcodes.IRETURN));
                    insnNodes.add(label2);
                    insnNodes.add(new FrameNode(Opcodes.F_SAME, 0, null, 0, null));
                    method.instructions.insert(insnNodes);
                    flag = true;
                } else if (method.name.equals("m_6921_")) {
                    InsnList insnNodes = new InsnList();
                    LabelNode label1 = new LabelNode();
                    insnNodes.add(new VarInsnNode(Opcodes.ALOAD, 0));
                    insnNodes.add(new MethodInsnNode(Opcodes.INVOKESTATIC,owner,"isRemove","(Lnet/minecraft/world/entity/Entity;)Z",false));
                    insnNodes.add(new JumpInsnNode(Opcodes.IFEQ,label1));
                    insnNodes.add(new FieldInsnNode(Opcodes.GETSTATIC,classNode.name, "f_19845_","Lnet/minecraft/world/phys/AABB;"));
                    insnNodes.add(new InsnNode(Opcodes.ARETURN));
                    insnNodes.add(label1);
                    insnNodes.add(new FrameNode(Opcodes.F_SAME, 0, null, 0, null));
                    method.instructions.insert(insnNodes);
                    flag = true;
                } else if (method.name.equals("m_20183_")) {
                    InsnList insnNodes = new InsnList();
                    LabelNode label1 = new LabelNode();
                    insnNodes.add(new VarInsnNode(Opcodes.ALOAD, 0));
                    insnNodes.add(new MethodInsnNode(Opcodes.INVOKESTATIC,owner,"isRemove","(Lnet/minecraft/world/entity/Entity;)Z",false));
                    insnNodes.add(new JumpInsnNode(Opcodes.IFEQ,label1));
                    insnNodes.add(new FieldInsnNode(Opcodes.GETSTATIC,"net/minecraft/core/BlockPos", "f_121853_","Lnet/minecraft/core/BlockPos;"));
                    insnNodes.add(new InsnNode(Opcodes.ARETURN));
                    insnNodes.add(label1);
                    insnNodes.add(new FrameNode(Opcodes.F_SAME, 0, null, 0, null));
                    method.instructions.insert(insnNodes);
                    flag = true;
                }
            }
        } else if (classNode.name.equals("net/minecraft/world/entity/LivingEntity")) {
            for (MethodNode method : classNode.methods) {
                if (method.name.equals("m_21133_")) {
                    InsnList insnNodes = new InsnList();
                    LabelNode label1 = new LabelNode();
                    insnNodes.add(new VarInsnNode(Opcodes.ALOAD,0));
                    insnNodes.add(new MethodInsnNode(Opcodes.INVOKESTATIC,owner,"isManaita","(Lnet/minecraft/world/entity/LivingEntity;)Z",false));
                    insnNodes.add(new JumpInsnNode(Opcodes.IFEQ,label1));
                    insnNodes.add(new VarInsnNode(Opcodes.ALOAD,0));
                    insnNodes.add(new VarInsnNode(Opcodes.ALOAD,1));
                    insnNodes.add(new MethodInsnNode(Opcodes.INVOKESTATIC,owner,"getAttributeValue","(Lnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/world/entity/ai/attributes/Attribute;)D",false));
                    insnNodes.add(new InsnNode(Opcodes.DRETURN));
                    insnNodes.add(label1);
                    insnNodes.add(new FrameNode(Opcodes.F_SAME, 0, null, 0, null));
                    method.instructions.insert(insnNodes);
                    flag = true;
                } else if (method.name.equals("m_21223_")) {
                    InsnList insnNodes = new InsnList();
                    LabelNode label1 = new LabelNode();
                    LabelNode label2 = new LabelNode();
                    insnNodes.add(new VarInsnNode(Opcodes.ALOAD,0));
                    insnNodes.add(new MethodInsnNode(Opcodes.INVOKESTATIC,owner,"isManaita","(Lnet/minecraft/world/entity/LivingEntity;)Z",false));
                    insnNodes.add(new JumpInsnNode(Opcodes.IFEQ,label1));
                    insnNodes.add(new VarInsnNode(Opcodes.ALOAD,0));
                    insnNodes.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL,classNode.name,"m_21233_","()F",false));
                    insnNodes.add(new InsnNode(Opcodes.FRETURN));
                    insnNodes.add(label1);
                    insnNodes.add(new FrameNode(Opcodes.F_SAME, 0, null, 0, null));
                    insnNodes.add(new VarInsnNode(Opcodes.ALOAD,0));
                    insnNodes.add(new MethodInsnNode(Opcodes.INVOKESTATIC,owner,"isDead","(Lnet/minecraft/world/entity/LivingEntity;)Z",false));
                    insnNodes.add(new JumpInsnNode(Opcodes.IFEQ,label2));
                    insnNodes.add(new InsnNode(Opcodes.FCONST_0));
                    insnNodes.add(new InsnNode(Opcodes.FRETURN));
                    insnNodes.add(label2);
                    insnNodes.add(new FrameNode(Opcodes.F_SAME, 0, null, 0, null));
                    method.instructions.insert(insnNodes);
                    flag = true;
                } else if (method.name.equals("m_21233_")) {
                    InsnList insnNodes = new InsnList();
                    LabelNode label1 = new LabelNode();
                    insnNodes.add(new VarInsnNode(Opcodes.ALOAD,0));
                    insnNodes.add(new MethodInsnNode(Opcodes.INVOKESTATIC,owner,"isManaita","(Lnet/minecraft/world/entity/LivingEntity;)Z",false));
                    insnNodes.add(new JumpInsnNode(Opcodes.IFEQ,label1));
                    insnNodes.add(new VarInsnNode(Opcodes.ALOAD,0));
                    insnNodes.add(new MethodInsnNode(Opcodes.INVOKESTATIC,owner,"getMaxHealth","(Lnet/minecraft/world/entity/LivingEntity;)F",false));
                    insnNodes.add(new InsnNode(Opcodes.FRETURN));
                    insnNodes.add(label1);
                    insnNodes.add(new FrameNode(Opcodes.F_SAME, 0, null, 0, null));
                    method.instructions.insert(insnNodes);
                    flag = true;
                } else if (method.name.equals("m_6667_")) {
                    InsnList insnNodes = new InsnList();
                    LabelNode label1 = new LabelNode();
                    insnNodes.add(new VarInsnNode(Opcodes.ALOAD,0));
                    insnNodes.add(new MethodInsnNode(Opcodes.INVOKESTATIC,owner,"isManaita","(Lnet/minecraft/world/entity/LivingEntity;)Z",false));
                    insnNodes.add(new JumpInsnNode(Opcodes.IFEQ,label1));
                    insnNodes.add(new VarInsnNode(Opcodes.ALOAD,0));
                    insnNodes.add(new InsnNode(Opcodes.ICONST_0));
                    insnNodes.add(new FieldInsnNode(Opcodes.PUTFIELD, "net/minecraft/world/entity/LivingEntity", "f_20917_", "I"));
                    insnNodes.add(new VarInsnNode(Opcodes.ALOAD,0));
                    insnNodes.add(new InsnNode(Opcodes.ICONST_0));
                    insnNodes.add(new FieldInsnNode(Opcodes.PUTFIELD, "net/minecraft/world/entity/LivingEntity", "f_20919_", "I"));
                    insnNodes.add(new VarInsnNode(Opcodes.ALOAD,0));
                    insnNodes.add(new InsnNode(Opcodes.ICONST_0));
                    insnNodes.add(new FieldInsnNode(Opcodes.PUTFIELD, "net/minecraft/world/entity/LivingEntity", "f_20916_", "I"));
                    insnNodes.add(new InsnNode(Opcodes.RETURN));
                    insnNodes.add(label1);
                    insnNodes.add(new FrameNode(Opcodes.F_SAME, 0, null, 0, null));
                    method.instructions.insert(insnNodes);
                    flag = true;
                } else if (method.name.equals("m_6469_")) {
                    InsnList insnNodes = new InsnList();
                    LabelNode label1 = new LabelNode();
                    insnNodes.add(new VarInsnNode(Opcodes.ALOAD,0));
                    insnNodes.add(new MethodInsnNode(Opcodes.INVOKESTATIC,owner,"isManaita","(Lnet/minecraft/world/entity/LivingEntity;)Z",false));
                    insnNodes.add(new JumpInsnNode(Opcodes.IFEQ,label1));
                    insnNodes.add(new VarInsnNode(Opcodes.ALOAD,0));
                    insnNodes.add(new InsnNode(Opcodes.ICONST_0));
                    insnNodes.add(new FieldInsnNode(Opcodes.PUTFIELD, "net/minecraft/world/entity/LivingEntity", "f_20917_", "I"));
                    insnNodes.add(new VarInsnNode(Opcodes.ALOAD,0));
                    insnNodes.add(new InsnNode(Opcodes.ICONST_0));
                    insnNodes.add(new FieldInsnNode(Opcodes.PUTFIELD, "net/minecraft/world/entity/LivingEntity", "f_20919_", "I"));
                    insnNodes.add(new VarInsnNode(Opcodes.ALOAD,0));
                    insnNodes.add(new InsnNode(Opcodes.ICONST_0));
                    insnNodes.add(new FieldInsnNode(Opcodes.PUTFIELD, "net/minecraft/world/entity/LivingEntity", "f_20916_", "I"));
                    insnNodes.add(new InsnNode(Opcodes.ICONST_0));
                    insnNodes.add(new InsnNode(Opcodes.IRETURN));
                    insnNodes.add(label1);
                    insnNodes.add(new FrameNode(Opcodes.F_SAME, 0, null, 0, null));
                    method.instructions.insert(insnNodes);
                    flag = true;
                } else if (method.name.equals("m_21224_")) {
                    InsnList insnNodes = new InsnList();
                    LabelNode label1 = new LabelNode();
                    LabelNode label2 = new LabelNode();
                    insnNodes.add(new VarInsnNode(Opcodes.ALOAD,0));
                    insnNodes.add(new MethodInsnNode(Opcodes.INVOKESTATIC,owner,"isManaita","(Lnet/minecraft/world/entity/LivingEntity;)Z",false));
                    insnNodes.add(new JumpInsnNode(Opcodes.IFEQ,label1));
                    insnNodes.add(new VarInsnNode(Opcodes.ALOAD,0));
                    insnNodes.add(new InsnNode(Opcodes.ICONST_0));
                    insnNodes.add(new FieldInsnNode(Opcodes.PUTFIELD, "net/minecraft/world/entity/LivingEntity", "f_20917_", "I"));
                    insnNodes.add(new VarInsnNode(Opcodes.ALOAD,0));
                    insnNodes.add(new InsnNode(Opcodes.ICONST_0));
                    insnNodes.add(new FieldInsnNode(Opcodes.PUTFIELD, "net/minecraft/world/entity/LivingEntity", "f_20919_", "I"));
                    insnNodes.add(new VarInsnNode(Opcodes.ALOAD,0));
                    insnNodes.add(new InsnNode(Opcodes.ICONST_0));
                    insnNodes.add(new FieldInsnNode(Opcodes.PUTFIELD, "net/minecraft/world/entity/LivingEntity", "f_20916_", "I"));
                    insnNodes.add(new InsnNode(Opcodes.ICONST_0));
                    insnNodes.add(new InsnNode(Opcodes.IRETURN));
                    insnNodes.add(label1);
                    insnNodes.add(new FrameNode(Opcodes.F_SAME, 0, null, 0, null));
                    insnNodes.add(new VarInsnNode(Opcodes.ALOAD,0));
                    insnNodes.add(new MethodInsnNode(Opcodes.INVOKESTATIC,owner,"isDead","(Lnet/minecraft/world/entity/LivingEntity;)Z",false));
                    insnNodes.add(new JumpInsnNode(Opcodes.IFEQ,label2));
                    insnNodes.add(new InsnNode(Opcodes.ICONST_1));
                    insnNodes.add(new InsnNode(Opcodes.IRETURN));
                    insnNodes.add(label2);
                    insnNodes.add(new FrameNode(Opcodes.F_SAME, 0, null, 0, null));
                    method.instructions.insert(insnNodes);
                    flag = true;
                } else if (method.name.equals("m_6084_")) {
                    InsnList insnNodes = new InsnList();
                    LabelNode label1 = new LabelNode();
                    LabelNode label2 = new LabelNode();
                    insnNodes.add(new VarInsnNode(Opcodes.ALOAD,0));
                    insnNodes.add(new MethodInsnNode(Opcodes.INVOKESTATIC,owner,"isManaita","(Lnet/minecraft/world/entity/LivingEntity;)Z",false));
                    insnNodes.add(new JumpInsnNode(Opcodes.IFEQ,label1));
                    insnNodes.add(new VarInsnNode(Opcodes.ALOAD,0));
                    insnNodes.add(new InsnNode(Opcodes.ICONST_0));
                    insnNodes.add(new FieldInsnNode(Opcodes.PUTFIELD, "net/minecraft/world/entity/LivingEntity", "f_20917_", "I"));
                    insnNodes.add(new VarInsnNode(Opcodes.ALOAD,0));
                    insnNodes.add(new InsnNode(Opcodes.ICONST_0));
                    insnNodes.add(new FieldInsnNode(Opcodes.PUTFIELD, "net/minecraft/world/entity/LivingEntity", "f_20919_", "I"));
                    insnNodes.add(new VarInsnNode(Opcodes.ALOAD,0));
                    insnNodes.add(new InsnNode(Opcodes.ICONST_0));
                    insnNodes.add(new FieldInsnNode(Opcodes.PUTFIELD, "net/minecraft/world/entity/LivingEntity", "f_20916_", "I"));
                    insnNodes.add(new InsnNode(Opcodes.ICONST_1));
                    insnNodes.add(new InsnNode(Opcodes.IRETURN));
                    insnNodes.add(label1);
                    insnNodes.add(new FrameNode(Opcodes.F_SAME, 0, null, 0, null));
                    insnNodes.add(new VarInsnNode(Opcodes.ALOAD,0));
                    insnNodes.add(new MethodInsnNode(Opcodes.INVOKESTATIC,owner,"isDead","(Lnet/minecraft/world/entity/LivingEntity;)Z",false));
                    insnNodes.add(new JumpInsnNode(Opcodes.IFEQ,label2));
                    insnNodes.add(new InsnNode(Opcodes.ICONST_0));
                    insnNodes.add(new InsnNode(Opcodes.IRETURN));
                    insnNodes.add(label2);
                    insnNodes.add(new FrameNode(Opcodes.F_SAME, 0, null, 0, null));
                    method.instructions.insert(insnNodes);
                    flag = true;
                }
            }
        }
//        if (classNode.name.equals("net/minecraft/world/entity/player/Player")) {
//            MethodNode method = new MethodNode(Opcodes.ACC_PUBLIC,"m_21223_","()F",null,null);
//            method.visitCode();
//            Label start = new Label();
//            method.visitLabel(start);
//            method.visitVarInsn(Opcodes.ALOAD, 0);
//            method.visitMethodInsn(Opcodes.INVOKESTATIC, "sen/manaita_plus_core/util/EventUtil", "getHealth", "(Lnet/minecraft/world/entity/player/Player;)F", false);
//            method.visitInsn(Opcodes.FRETURN);
//            Label end = new Label();
//            method.visitLabel(end);
//            method.visitLocalVariable("this", "Lnet/minecraft/world/entity/player/Player;", null, start, end, 0);
//            method.visitEnd();
//            classNode.methods.add(method);
//            ManaitaTransformationService.LOGGER.error("add getHealth to Player");
//        }
//        Iterator<MethodNode> iter0 = classNode.methods.iterator();
//        MethodNode method;
//        while (iter0.hasNext()) {
//            method = iter0.next();
//            for (AbstractInsnNode node : method.instructions) {
//                if (node instanceof FieldInsnNode fieldInsnNode && fieldInsnNode.getOpcode() == Opcodes.GETFIELD) {
//                    if (fieldInsnNode.owner.equals("net/minecraft/client/multiplayer/ClientLevel") && fieldInsnNode.name.equals("f_171630_")) {
//                        method.instructions.set(fieldInsnNode, new MethodInsnNode(Opcodes.INVOKESTATIC, owner, "getTickingEntities", "(Lnet/minecraft/client/multiplayer/ClientLevel;)Lnet/minecraft/world/level/entity/EntityTickList;", false));
//                    }
//                    if (fieldInsnNode.owner.equals("net/minecraft/server/level/ServerLevel") && fieldInsnNode.name.equals("f_143243_")) {
//                        method.instructions.set(fieldInsnNode, new MethodInsnNode(Opcodes.INVOKESTATIC, owner, "getEntityTickList", "(Lnet/minecraft/server/level/ServerLevel;)Lnet/minecraft/world/level/entity/EntityTickList;", false));
//                    }
//                    if (fieldInsnNode.owner.equals("net/minecraft/world/level/entity/EntityLookup") && fieldInsnNode.name.equals("f_156807_")) {
//                        method.instructions.set(fieldInsnNode, new MethodInsnNode(Opcodes.INVOKESTATIC, owner, "getById", "(Lnet/minecraft/world/level/entity/EntityLookup;)Lit/unimi/dsi/fastutil/ints/Int2ObjectMap;", false));
//                    }
//                } else if (node instanceof MethodInsnNode methodInsnNode) {
//                    if (methodInsnNode.owner.equals("net/minecraft/world/entity/LivingEntity") && methodInsnNode.name.equals("m_21223_") && methodInsnNode.desc.equals("()F") /*&& methodInsnNode.getOpcode() == Opcodes.INVOKEVIRTUAL*/) {
//                        methodInsnNode.setOpcode(Opcodes.INVOKESTATIC);
//                        methodInsnNode.owner = owner;
//                        methodInsnNode.name = "getHealth";
//                        methodInsnNode.desc = "(Lnet/minecraft/world/entity/LivingEntity;)F";
//                    }
//                }
//            }
//        }
        return flag;
    }



    public static String getMethodDescriptor(final Method method) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append('(');
        Class<?>[] parameters = method.getParameterTypes();
        for (Class<?> parameter : parameters) {
            appendDescriptor(parameter, stringBuilder);
        }
        appendDescriptor(String.class,stringBuilder);
        stringBuilder.append(')');
        appendDescriptor(method.getReturnType(), stringBuilder);
        return stringBuilder.toString();
    }

    private static void appendDescriptor(final Class<?> clazz, final StringBuilder stringBuilder) {
        Class<?> currentClass = clazz;
        while (currentClass.isArray()) {
            stringBuilder.append('[');
            currentClass = currentClass.getComponentType();
        }
        if (currentClass.isPrimitive()) {
            char descriptor;
            if (currentClass == Integer.TYPE) {
                descriptor = 'I';
            } else if (currentClass == Void.TYPE) {
                descriptor = 'V';
            } else if (currentClass == Boolean.TYPE) {
                descriptor = 'Z';
            } else if (currentClass == Byte.TYPE) {
                descriptor = 'B';
            } else if (currentClass == Character.TYPE) {
                descriptor = 'C';
            } else if (currentClass == Short.TYPE) {
                descriptor = 'S';
            } else if (currentClass == Double.TYPE) {
                descriptor = 'D';
            } else if (currentClass == Float.TYPE) {
                descriptor = 'F';
            } else if (currentClass == Long.TYPE) {
                descriptor = 'J';
            } else {
                throw new AssertionError();
            }
            stringBuilder.append(descriptor);
        } else {
            stringBuilder.append('L').append(Type.getInternalName(currentClass)).append(';');
        }
    }


}
