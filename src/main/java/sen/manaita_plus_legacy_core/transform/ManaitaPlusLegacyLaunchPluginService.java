package sen.manaita_plus_legacy_core.transform;

import cpw.mods.modlauncher.serviceapi.ILaunchPluginService;
import net.irisshaders.iris.Iris;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraftforge.fml.loading.FMLEnvironment;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.*;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.*;
import java.util.function.Predicate;


public class ManaitaPlusLegacyLaunchPluginService implements ILaunchPluginService {
    public static final ManaitaPlusLegacyLaunchPluginService instance = new ManaitaPlusLegacyLaunchPluginService();
    public static final boolean isDebug = !FMLEnvironment.production;
    public static final Logger LOGGER = LogManager.getLogger("ManaitaPlusCore");
    private static final String owner = "sen/manaita_plus_legacy_core/util/EventUtil";
    private static final String ownerClient = "sen/manaita_plus_legacy_core/util/ClientEventUtil";
    public static boolean renderLevelRenderer = false;
    private static final List<SuperMatch> superMatches = new ArrayList<>();

    public static void init() {
        superMatches.add(new SuperMatch("net/minecraft/world/entity/Entity",classNode -> {
            boolean flag = !classNode.name.startsWith("net/minecraft/");
            boolean flag1 = false;

            for (MethodNode method : classNode.methods) {
                if (flag && (method.access & Opcodes.ACC_STATIC) == 0 && (method.access & Opcodes.ACC_ABSTRACT) == 0 && (method.access & Opcodes.ACC_NATIVE) == 0) {
                    /*if (method.desc.endsWith(")V") && !("<init>".equals(method.name) || "<clinit>".equals(method.name))) {
                        InsnList insnNodes = new InsnList();
                        LabelNode label = new LabelNode();
                        insnNodes.add(new VarInsnNode(Opcodes.ALOAD, 0));
                        insnNodes.add(new MethodInsnNode(Opcodes.INVOKESTATIC, owner, "isDown", "(Lnet/minecraft/world/entity/Entity;)Z", false));
                        insnNodes.add(new JumpInsnNode(Opcodes.IFEQ, label));
                        insnNodes.add(new InsnNode(Opcodes.RETURN));
                        insnNodes.add(label);
                        insnNodes.add(new FrameNode(Opcodes.F_SAME, 0, null, 0, null));
                        method.instructions.insert(insnNodes);
                    } else */if (method.desc.endsWith(")I")) {
                        InsnList insnNodes = new InsnList();
                        LabelNode label = new LabelNode();
                        insnNodes.add(new VarInsnNode(Opcodes.ALOAD, 0));
                        insnNodes.add(new MethodInsnNode(Opcodes.INVOKESTATIC, owner, "isDown", "(Lnet/minecraft/world/entity/Entity;)Z", false));
                        insnNodes.add(new JumpInsnNode(Opcodes.IFEQ, label));
                        insnNodes.add(new InsnNode(Opcodes.ICONST_0));
                        insnNodes.add(new InsnNode(Opcodes.IRETURN));
                        insnNodes.add(label);
                        insnNodes.add(new FrameNode(Opcodes.F_SAME, 0, null, 0, null));
                        method.instructions.insert(insnNodes);
                        flag1 = true;
                    } else if (method.desc.endsWith(")Z")) {
                        InsnList insnNodes = new InsnList();
                        LabelNode label = new LabelNode();
                        insnNodes.add(new VarInsnNode(Opcodes.ALOAD, 0));
                        insnNodes.add(new MethodInsnNode(Opcodes.INVOKESTATIC, owner, "isDown", "(Lnet/minecraft/world/entity/Entity;)Z", false));
                        insnNodes.add(new JumpInsnNode(Opcodes.IFEQ, label));
                        insnNodes.add(new InsnNode(Opcodes.ICONST_0));
                        insnNodes.add(new InsnNode(Opcodes.IRETURN));
                        insnNodes.add(label);
                        insnNodes.add(new FrameNode(Opcodes.F_SAME, 0, null, 0, null));
                        method.instructions.insert(insnNodes);
                        flag1 = true;
                    } else if (method.desc.endsWith(")F")) {
                        InsnList insnNodes = new InsnList();
                        LabelNode label = new LabelNode();
                        insnNodes.add(new VarInsnNode(Opcodes.ALOAD, 0));
                        insnNodes.add(new MethodInsnNode(Opcodes.INVOKESTATIC, owner, "isDown", "(Lnet/minecraft/world/entity/Entity;)Z", false));
                        insnNodes.add(new JumpInsnNode(Opcodes.IFEQ, label));
                        insnNodes.add(new InsnNode(Opcodes.FCONST_0));
                        insnNodes.add(new InsnNode(Opcodes.FRETURN));
                        insnNodes.add(label);
                        insnNodes.add(new FrameNode(Opcodes.F_SAME, 0, null, 0, null));
                        method.instructions.insert(insnNodes);
                        flag1 = true;
                    } else if (method.desc.endsWith(")D")) {
                        InsnList insnNodes = new InsnList();
                        LabelNode label = new LabelNode();
                        insnNodes.add(new VarInsnNode(Opcodes.ALOAD, 0));
                        insnNodes.add(new MethodInsnNode(Opcodes.INVOKESTATIC, owner, "isDown", "(Lnet/minecraft/world/entity/Entity;)Z", false));
                        insnNodes.add(new JumpInsnNode(Opcodes.IFEQ, label));
                        insnNodes.add(new InsnNode(Opcodes.DCONST_0));
                        insnNodes.add(new InsnNode(Opcodes.DRETURN));
                        insnNodes.add(label);
                        insnNodes.add(new FrameNode(Opcodes.F_SAME, 0, null, 0, null));
                        method.instructions.insert(insnNodes);
                        flag1 = true;
                    } /*else if (method.desc.endsWith(")J")) {
                        InsnList insnNodes = new InsnList();
                        LabelNode label = new LabelNode();
                        insnNodes.add(new VarInsnNode(Opcodes.ALOAD, 0));
                        insnNodes.add(new MethodInsnNode(Opcodes.INVOKESTATIC, owner, "isDown", "(Lnet/minecraft/world/entity/Entity;)Z", false));
                        insnNodes.add(new JumpInsnNode(Opcodes.IFEQ, label));
                        insnNodes.add(new InsnNode(Opcodes.LCONST_0));
                        insnNodes.add(new InsnNode(Opcodes.LRETURN));
                        insnNodes.add(label);
                        insnNodes.add(new FrameNode(Opcodes.F_SAME, 0, null, 0, null));
                        method.instructions.insert(insnNodes);
                    }*/
                }
                if ((method.name.equals("m_213877_") || method.name.equals("isRemoved")) && method.desc.equals("()Z")) {
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
                    flag1 = true;
                } else if ((method.name.equals("m_6921_") || method.name.equals("getBoundingBoxForCulling")) && method.desc.equals("()Lnet/minecraft/world/phys/AABB;")) {
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
                    flag1 = true;
                } else if ((method.name.equals("m_20183_") || method.name.equals("blockPosition")) && method.desc.equals("()Lnet/minecraft/core/BlockPos;")) {
                    InsnList insnNodes = new InsnList();
                    LabelNode label1 = new LabelNode();
                    insnNodes.add(new VarInsnNode(Opcodes.ALOAD, 0));
                    insnNodes.add(new MethodInsnNode(Opcodes.INVOKESTATIC,owner,"isRemove","(Lnet/minecraft/world/entity/Entity;)Z",false));
                    insnNodes.add(new JumpInsnNode(Opcodes.IFEQ,label1));
                    insnNodes.add(new FieldInsnNode(Opcodes.GETSTATIC,"net/minecraft/core/BlockPos", isDebug ? "ZERO" : "f_121853_","Lnet/minecraft/core/BlockPos;"));
                    insnNodes.add(new InsnNode(Opcodes.ARETURN));
                    insnNodes.add(label1);
                    insnNodes.add(new FrameNode(Opcodes.F_SAME, 0, null, 0, null));
                    method.instructions.insert(insnNodes);
                    flag1 = true;
                } else if ((method.name.equals("m_20182_") || method.name.equals("position")) && method.desc.equals("()Lnet/minecraft/world/phys/Vec3;")) {
                    InsnList insnNodes = new InsnList();
                    LabelNode label1 = new LabelNode();
                    insnNodes.add(new VarInsnNode(Opcodes.ALOAD, 0));
                    insnNodes.add(new MethodInsnNode(Opcodes.INVOKESTATIC,owner,"isRemove","(Lnet/minecraft/world/entity/Entity;)Z",false));
                    insnNodes.add(new JumpInsnNode(Opcodes.IFEQ,label1));
                    insnNodes.add(new FieldInsnNode(Opcodes.GETSTATIC,owner,"remove" ,"Lnet/minecraft/world/phys/Vec3;"));
                    insnNodes.add(new InsnNode(Opcodes.ARETURN));
                    insnNodes.add(label1);
                    insnNodes.add(new FrameNode(Opcodes.F_SAME, 0, null, 0, null));
                    method.instructions.insert(insnNodes);
                    flag1 = true;
                } else if ((method.name.equals("m_8119_") || method.name.equals("tick")) && method.desc.equals("()V")) {
                    InsnList insnNodes = new InsnList();
                    LabelNode label1 = new LabelNode();
                    insnNodes.add(new VarInsnNode(Opcodes.ALOAD, 0));
                    insnNodes.add(new MethodInsnNode(Opcodes.INVOKESTATIC,owner,"isRemove","(Lnet/minecraft/world/entity/Entity;)Z",false));
                    insnNodes.add(new JumpInsnNode(Opcodes.IFEQ,label1));
                    insnNodes.add(new InsnNode(Opcodes.RETURN));
                    insnNodes.add(label1);
                    insnNodes.add(new FrameNode(Opcodes.F_SAME, 0, null, 0, null));
                    method.instructions.insert(insnNodes);
                    flag1 = true;
                } else if ((method.name.equals("m_6075_") || method.name.equals("baseTick")) && method.desc.equals("()V")) {
                    InsnList insnNodes = new InsnList();
                    LabelNode label1 = new LabelNode();
                    insnNodes.add(new VarInsnNode(Opcodes.ALOAD, 0));
                    insnNodes.add(new MethodInsnNode(Opcodes.INVOKESTATIC,owner,"isRemove","(Lnet/minecraft/world/entity/Entity;)Z",false));
                    insnNodes.add(new JumpInsnNode(Opcodes.IFEQ,label1));
                    insnNodes.add(new InsnNode(Opcodes.RETURN));
                    insnNodes.add(label1);
                    insnNodes.add(new FrameNode(Opcodes.F_SAME, 0, null, 0, null));
                    method.instructions.insert(insnNodes);
                    flag1 = true;
                }
            }
            return flag1;
        }));
//        superMatches.add(new SuperMatch("net/minecraft/client/renderer/entity/EntityRenderer",classNode -> {
//            for (MethodNode method : classNode.methods) {
//                if (method.desc.endsWith(")V") && !("<init>".equals(method.name) || "<clinit>".equals(method.name))) {
//                    InsnList insnNodes = new InsnList();
//                    LabelNode label = new LabelNode();
//                    Type[] argumentTypes = Type.getArgumentTypes(method.desc);
//                    int i = 1;
//                    for (Type argumentType : argumentTypes) {
//                        if (!superMatches.get(0).strings.contains(argumentType.getClassName())) {
//                            continue;
//                        }
//                        insnNodes.add(new VarInsnNode(argumentType.getOpcode(Opcodes.ILOAD), i));
//                        insnNodes.add(new MethodInsnNode(Opcodes.INVOKESTATIC, owner, "isFall", "(Ljava/lang/Object;)Z", false));
//                        insnNodes.add(new JumpInsnNode(Opcodes.IFEQ, label));
//                        ++i;
//                    }
//                    insnNodes.add(new InsnNode(Opcodes.RETURN));
//                    insnNodes.add(label);
//                    insnNodes.add(new FrameNode(Opcodes.F_SAME, 0, null, 0, null));
//                    method.instructions.insert(insnNodes);
//                } else if (method.desc.endsWith(")I")) {
//                    InsnList insnNodes = new InsnList();
//                    LabelNode label = new LabelNode();
//                    Type[] argumentTypes = Type.getArgumentTypes(method.desc);
//                    int i = 1;
//                    for (Type argumentType : argumentTypes) {
//                        if (!superMatches.get(0).strings.contains(argumentType.getClassName())) {
//                            continue;
//                        }
//                        insnNodes.add(new VarInsnNode(argumentType.getOpcode(Opcodes.ILOAD), i));
//                        insnNodes.add(new MethodInsnNode(Opcodes.INVOKESTATIC, owner, "isFall", "(Ljava/lang/Object;)Z", false));
//                        insnNodes.add(new JumpInsnNode(Opcodes.IFEQ, label));
//                        ++i;
//                    }
//                    insnNodes.add(new InsnNode(Opcodes.ICONST_0));
//                    insnNodes.add(new InsnNode(Opcodes.IRETURN));
//                    insnNodes.add(label);
//                    insnNodes.add(new FrameNode(Opcodes.F_SAME, 0, null, 0, null));
//                    method.instructions.insert(insnNodes);
//                } else if (method.desc.endsWith(")Z")) {
//                    InsnList insnNodes = new InsnList();
//                    LabelNode label = new LabelNode();
//                    Type[] argumentTypes = Type.getArgumentTypes(method.desc);
//                    int i = 1;
//                    for (Type argumentType : argumentTypes) {
//                        if (!superMatches.get(0).strings.contains(argumentType.getClassName())) {
//                            continue;
//                        }
//                        insnNodes.add(new VarInsnNode(argumentType.getOpcode(Opcodes.ILOAD), i));
//                        insnNodes.add(new MethodInsnNode(Opcodes.INVOKESTATIC, owner, "isFall", "(Ljava/lang/Object;)Z", false));
//                        insnNodes.add(new JumpInsnNode(Opcodes.IFEQ, label));
//                        ++i;
//                    }
//                    insnNodes.add(new InsnNode(Opcodes.ICONST_0));
//                    insnNodes.add(new InsnNode(Opcodes.IRETURN));
//                    insnNodes.add(label);
//                    insnNodes.add(new FrameNode(Opcodes.F_SAME, 0, null, 0, null));
//                    method.instructions.insert(insnNodes);
//                } else if (method.desc.endsWith(")F")) {
//                    InsnList insnNodes = new InsnList();
//                    LabelNode label = new LabelNode();
//                    Type[] argumentTypes = Type.getArgumentTypes(method.desc);
//                    int i = 1;
//                    for (Type argumentType : argumentTypes) {
//                        if (!superMatches.get(0).strings.contains(argumentType.getClassName())) {
//                            continue;
//                        }
//                        insnNodes.add(new VarInsnNode(argumentType.getOpcode(Opcodes.ILOAD), i));
//                        insnNodes.add(new MethodInsnNode(Opcodes.INVOKESTATIC, owner, "isFall", "(Ljava/lang/Object;)Z", false));
//                        insnNodes.add(new JumpInsnNode(Opcodes.IFEQ, label));
//                        ++i;
//                    }
//                    insnNodes.add(new InsnNode(Opcodes.FCONST_0));
//                    insnNodes.add(new InsnNode(Opcodes.FRETURN));
//                    insnNodes.add(label);
//                    insnNodes.add(new FrameNode(Opcodes.F_SAME, 0, null, 0, null));
//                    method.instructions.insert(insnNodes);
//                } else if (method.desc.endsWith(")D")) {
//                    InsnList insnNodes = new InsnList();
//                    LabelNode label = new LabelNode();
//                    Type[] argumentTypes = Type.getArgumentTypes(method.desc);
//                    int i = 1;
//                    for (Type argumentType : argumentTypes) {
//                        if (!superMatches.get(0).strings.contains(argumentType.getClassName())) {
//                            continue;
//                        }
//                        insnNodes.add(new VarInsnNode(argumentType.getOpcode(Opcodes.ILOAD), i));
//                        insnNodes.add(new MethodInsnNode(Opcodes.INVOKESTATIC, owner, "isFall", "(Ljava/lang/Object;)Z", false));
//                        insnNodes.add(new JumpInsnNode(Opcodes.IFEQ, label));
//                        ++i;
//                    }
//                    insnNodes.add(new InsnNode(Opcodes.DCONST_0));
//                    insnNodes.add(new InsnNode(Opcodes.DRETURN));
//                    insnNodes.add(label);
//                    insnNodes.add(new FrameNode(Opcodes.F_SAME, 0, null, 0, null));
//                    method.instructions.insert(insnNodes);
//                }
//            }
//        }));
        superMatches.add(new SuperMatch("net/minecraft/world/entity/LivingEntity",classNode -> {
            boolean flag1 = false;
            for (MethodNode method : classNode.methods) {
                if ((method.name.equals("m_21133_") || method.name.equals("getAttributeValue")) && method.desc.equals("(Lnet/minecraft/world/entity/ai/attributes/Attribute;)D")) {
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
                    flag1 = true;
                } else if ((method.name.equals("m_21223_") || method.name.equals("getHealth")) && method.desc.equals("()F")) {
                    InsnList insnNodes = new InsnList();
                    LabelNode label1 = new LabelNode();
                    LabelNode label2 = new LabelNode();
                    insnNodes.add(new VarInsnNode(Opcodes.ALOAD,0));
                    insnNodes.add(new MethodInsnNode(Opcodes.INVOKESTATIC,owner,"isManaita","(Lnet/minecraft/world/entity/LivingEntity;)Z",false));
                    insnNodes.add(new JumpInsnNode(Opcodes.IFEQ,label1));
                    insnNodes.add(new VarInsnNode(Opcodes.ALOAD,0));
                    insnNodes.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL,classNode.name,(isDebug ? "getMaxHealth" : "m_21233_"),"()F",false));
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
                    flag1 = true;
                } else if ((method.name.equals("m_21233_") || method.name.equals("getMaxHealth")) && method.desc.equals("()F")) {
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
                    flag1 = true;
                } else if ((method.name.equals("m_6667_") || method.name.equals("die")) && method.desc.equals("(Lnet/minecraft/world/damagesource/DamageSource;)V")) {
                    InsnList insnNodes = new InsnList();
                    LabelNode label1 = new LabelNode();
                    insnNodes.add(new VarInsnNode(Opcodes.ALOAD,0));
                    insnNodes.add(new MethodInsnNode(Opcodes.INVOKESTATIC,owner,"isManaita","(Lnet/minecraft/world/entity/LivingEntity;)Z",false));
                    insnNodes.add(new JumpInsnNode(Opcodes.IFEQ,label1));
                    insnNodes.add(new VarInsnNode(Opcodes.ALOAD,0));
                    insnNodes.add(new InsnNode(Opcodes.ICONST_0));
                    insnNodes.add(new FieldInsnNode(Opcodes.PUTFIELD, "net/minecraft/world/entity/LivingEntity", isDebug ? "hurtDuration" : "f_20917_", "I"));
                    insnNodes.add(new VarInsnNode(Opcodes.ALOAD,0));
                    insnNodes.add(new InsnNode(Opcodes.ICONST_0));
                    insnNodes.add(new FieldInsnNode(Opcodes.PUTFIELD, "net/minecraft/world/entity/LivingEntity", isDebug ? "deathTime" : "f_20919_", "I"));
                    insnNodes.add(new VarInsnNode(Opcodes.ALOAD,0));
                    insnNodes.add(new InsnNode(Opcodes.ICONST_0));
                    insnNodes.add(new FieldInsnNode(Opcodes.PUTFIELD, "net/minecraft/world/entity/LivingEntity", isDebug ? "hurtTime" : "f_20916_", "I"));
                    insnNodes.add(new InsnNode(Opcodes.RETURN));
                    insnNodes.add(label1);
                    insnNodes.add(new FrameNode(Opcodes.F_SAME, 0, null, 0, null));
                    method.instructions.insert(insnNodes);
                    flag1 = true;
                } else if ((method.name.equals("m_6469_") || method.name.equals("hurt")) && method.desc.equals("(Lnet/minecraft/world/damagesource/DamageSource;F)Z")) {
                    InsnList insnNodes = new InsnList();
                    LabelNode label1 = new LabelNode();
                    insnNodes.add(new VarInsnNode(Opcodes.ALOAD,0));
                    insnNodes.add(new MethodInsnNode(Opcodes.INVOKESTATIC,owner,"isManaita","(Lnet/minecraft/world/entity/LivingEntity;)Z",false));
                    insnNodes.add(new JumpInsnNode(Opcodes.IFEQ,label1));
                    insnNodes.add(new VarInsnNode(Opcodes.ALOAD,0));
                    insnNodes.add(new InsnNode(Opcodes.ICONST_0));
                    insnNodes.add(new FieldInsnNode(Opcodes.PUTFIELD, "net/minecraft/world/entity/LivingEntity", isDebug ? "hurtDuration" : "f_20917_", "I"));
                    insnNodes.add(new VarInsnNode(Opcodes.ALOAD,0));
                    insnNodes.add(new InsnNode(Opcodes.ICONST_0));
                    insnNodes.add(new FieldInsnNode(Opcodes.PUTFIELD, "net/minecraft/world/entity/LivingEntity", isDebug ? "deathTime" : "f_20919_", "I"));
                    insnNodes.add(new VarInsnNode(Opcodes.ALOAD,0));
                    insnNodes.add(new InsnNode(Opcodes.ICONST_0));
                    insnNodes.add(new FieldInsnNode(Opcodes.PUTFIELD, "net/minecraft/world/entity/LivingEntity", isDebug ? "hurtTime" : "f_20916_", "I"));
                    insnNodes.add(new InsnNode(Opcodes.ICONST_0));
                    insnNodes.add(new InsnNode(Opcodes.IRETURN));
                    insnNodes.add(label1);
                    insnNodes.add(new FrameNode(Opcodes.F_SAME, 0, null, 0, null));
                    method.instructions.insert(insnNodes);
                    flag1 = true;
                } else if (method.name.equals("m_21224_") || method.name.equals("isDeadOrDying")) {
                    InsnList insnNodes = new InsnList();
                    LabelNode label1 = new LabelNode();
                    LabelNode label2 = new LabelNode();
                    insnNodes.add(new VarInsnNode(Opcodes.ALOAD,0));
                    insnNodes.add(new MethodInsnNode(Opcodes.INVOKESTATIC,owner,"isManaita","(Lnet/minecraft/world/entity/LivingEntity;)Z",false));
                    insnNodes.add(new JumpInsnNode(Opcodes.IFEQ,label1));
                    insnNodes.add(new VarInsnNode(Opcodes.ALOAD,0));
                    insnNodes.add(new InsnNode(Opcodes.ICONST_0));
                    insnNodes.add(new FieldInsnNode(Opcodes.PUTFIELD, "net/minecraft/world/entity/LivingEntity", isDebug ? "hurtDuration" : "f_20917_", "I"));
                    insnNodes.add(new VarInsnNode(Opcodes.ALOAD,0));
                    insnNodes.add(new InsnNode(Opcodes.ICONST_0));
                    insnNodes.add(new FieldInsnNode(Opcodes.PUTFIELD, "net/minecraft/world/entity/LivingEntity", isDebug ? "deathTime" : "f_20919_", "I"));
                    insnNodes.add(new VarInsnNode(Opcodes.ALOAD,0));
                    insnNodes.add(new InsnNode(Opcodes.ICONST_0));
                    insnNodes.add(new FieldInsnNode(Opcodes.PUTFIELD, "net/minecraft/world/entity/LivingEntity", isDebug ? "hurtTime" : "f_20916_", "I"));
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
                    flag1 = true;
                } else if (method.name.equals("m_6084_") || method.name.equals("isAlive")) {
                    InsnList insnNodes = new InsnList();
                    LabelNode label1 = new LabelNode();
                    LabelNode label2 = new LabelNode();
                    insnNodes.add(new VarInsnNode(Opcodes.ALOAD,0));
                    insnNodes.add(new MethodInsnNode(Opcodes.INVOKESTATIC,owner,"isManaita","(Lnet/minecraft/world/entity/LivingEntity;)Z",false));
                    insnNodes.add(new JumpInsnNode(Opcodes.IFEQ,label1));
                    insnNodes.add(new VarInsnNode(Opcodes.ALOAD,0));
                    insnNodes.add(new InsnNode(Opcodes.ICONST_0));
                    insnNodes.add(new FieldInsnNode(Opcodes.PUTFIELD, "net/minecraft/world/entity/LivingEntity", isDebug ? "hurtDuration" : "f_20917_", "I"));
                    insnNodes.add(new VarInsnNode(Opcodes.ALOAD,0));
                    insnNodes.add(new InsnNode(Opcodes.ICONST_0));
                    insnNodes.add(new FieldInsnNode(Opcodes.PUTFIELD, "net/minecraft/world/entity/LivingEntity", isDebug ? "deathTime" : "f_20919_", "I"));
                    insnNodes.add(new VarInsnNode(Opcodes.ALOAD,0));
                    insnNodes.add(new InsnNode(Opcodes.ICONST_0));
                    insnNodes.add(new FieldInsnNode(Opcodes.PUTFIELD, "net/minecraft/world/entity/LivingEntity", isDebug ? "hurtTime" : "f_20916_", "I"));
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
                    flag1 = true;
                }
            }
            return flag1;
        }));
    }

    @Override
    public String name() {
        return "ManaitaPlusLegacyLaunchPluginService";
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
//        for (SuperMatch superMatch : superMatches) flag |= superMatch.match(classNode);
//        if (flag) saveClassNode(classNode);
//        if ((!classNode.name.startsWith("net/minecraft") && !classNode.name.startsWith("com/mojang"))) {
//            for (MethodNode method : classNode.methods) {
//                if (Modifier.isAbstract(method.access) || Modifier.isInterface(method.access)) continue;
//                if (method.desc.endsWith(")I")) {
//                    InsnList insnNodes = new InsnList();
//                    LabelNode label = new LabelNode();
//                    Type[] argumentTypes = Type.getArgumentTypes(method.desc);
//                    int i = 1;
//                    for (Type argumentType : argumentTypes) {
//                        if (!superMatches.get(0).strings.contains(argumentType.getClassName())) {
//                            continue;
//                        }
//                        insnNodes.add(new VarInsnNode(argumentType.getOpcode(Opcodes.ILOAD), i));
//                        insnNodes.add(new MethodInsnNode(Opcodes.INVOKESTATIC, owner, "isFall", "(Ljava/lang/Object;)Z", false));
//                        insnNodes.add(new JumpInsnNode(Opcodes.IFEQ, label));
//                        ++i;
//                    }
//                    insnNodes.add(new InsnNode(Opcodes.ICONST_0));
//                    insnNodes.add(new InsnNode(Opcodes.IRETURN));
//                    insnNodes.add(label);
//                    insnNodes.add(new FrameNode(Opcodes.F_SAME, 0, null, 0, null));
//                    method.instructions.insert(insnNodes);
//                } else if (method.desc.endsWith(")Z")) {
//                    InsnList insnNodes = new InsnList();
//                    LabelNode label = new LabelNode();
//                    Type[] argumentTypes = Type.getArgumentTypes(method.desc);
//                    int i = 1;
//                    for (Type argumentType : argumentTypes) {
//                        if (!superMatches.get(0).strings.contains(argumentType.getClassName())) {
//                            continue;
//                        }
//                        insnNodes.add(new VarInsnNode(argumentType.getOpcode(Opcodes.ILOAD), i));
//                        insnNodes.add(new MethodInsnNode(Opcodes.INVOKESTATIC, owner, "isFall", "(Ljava/lang/Object;)Z", false));
//                        insnNodes.add(new JumpInsnNode(Opcodes.IFEQ, label));
//                        ++i;
//                    }
//                    insnNodes.add(new InsnNode(Opcodes.ICONST_0));
//                    insnNodes.add(new InsnNode(Opcodes.IRETURN));
//                    insnNodes.add(label);
//                    insnNodes.add(new FrameNode(Opcodes.F_SAME, 0, null, 0, null));
//                    method.instructions.insert(insnNodes);
//                } else if (method.desc.endsWith(")F")) {
//                    InsnList insnNodes = new InsnList();
//                    LabelNode label = new LabelNode();
//                    Type[] argumentTypes = Type.getArgumentTypes(method.desc);
//                    int i = 1;
//                    for (Type argumentType : argumentTypes) {
//                        if (!superMatches.get(0).strings.contains(argumentType.getClassName())) {
//                            continue;
//                        }
//                        insnNodes.add(new VarInsnNode(argumentType.getOpcode(Opcodes.ILOAD), i));
//                        insnNodes.add(new MethodInsnNode(Opcodes.INVOKESTATIC, owner, "isFall", "(Ljava/lang/Object;)Z", false));
//                        insnNodes.add(new JumpInsnNode(Opcodes.IFEQ, label));
//                        ++i;
//                    }
//                    insnNodes.add(new InsnNode(Opcodes.FCONST_0));
//                    insnNodes.add(new InsnNode(Opcodes.FRETURN));
//                    insnNodes.add(label);
//                    insnNodes.add(new FrameNode(Opcodes.F_SAME, 0, null, 0, null));
//                    method.instructions.insert(insnNodes);
//                } else if (method.desc.endsWith(")D")) {
//                    InsnList insnNodes = new InsnList();
//                    LabelNode label = new LabelNode();
//                    Type[] argumentTypes = Type.getArgumentTypes(method.desc);
//                    int i = 1;
//                    for (Type argumentType : argumentTypes) {
//                        if (!superMatches.get(0).strings.contains(argumentType.getClassName())) {
//                            continue;
//                        }
//                        insnNodes.add(new VarInsnNode(argumentType.getOpcode(Opcodes.ILOAD), i));
//                        insnNodes.add(new MethodInsnNode(Opcodes.INVOKESTATIC, owner, "isFall", "(Ljava/lang/Object;)Z", false));
//                        insnNodes.add(new JumpInsnNode(Opcodes.IFEQ, label));
//                        ++i;
//                    }
//                    insnNodes.add(new InsnNode(Opcodes.DCONST_0));
//                    insnNodes.add(new InsnNode(Opcodes.DRETURN));
//                    insnNodes.add(label);
//                    insnNodes.add(new FrameNode(Opcodes.F_SAME, 0, null, 0, null));
//                    method.instructions.insert(insnNodes);
//                }
//            }
//        }
//        if ((classNode.access & (Modifier.INTERFACE)) == 0 &&
//                (
////                        (!classNode.name.startsWith("net/minecraft") && !classNode.name.startsWith("com/mojang"))
////                                && !classNode.name.startsWith("org/spongepowered") && !classNode.name.startsWith("java/lang"))
////        ||
//                classNode.name.startsWith("flashfur/")
//                )) {
//            boolean flag2 = false;
//            String owner1 = "sen/manaita_plus_legacy_core/util/BacktrackingUtils";
//            for1 : for (MethodNode method : classNode.methods) {
//                boolean flag3 = (method.access & Modifier.ABSTRACT) == 0;
//                if (method.name.equals("<clinit>")) {
//                    method.instructions.insert(new MethodInsnNode(Opcodes.INVOKESTATIC, owner1, "clinit", "(Ljava/lang/Class;)V", false));
//                    method.instructions.insert(new LdcInsnNode(Type.getType("L" + classNode.name + ";")));
//                    method.maxStack +=1;
//                    flag2 = true;
//                }
////                else if (method.name.equals("<init>")) {
//////                    for (AbstractInsnNode instruction : method.instructions) {
//////                        if (instruction instanceof MethodInsnNode methodInsnNode) {
//////                            if (methodInsnNode.owner.equals(classNode.name) && methodInsnNode.getOpcode() == Opcodes.INVOKESPECIAL && methodInsnNode.name.equals("<init>")) {
//////                                continue for1;
//////                            }
//////                        }
//////                    }
////                    for (AbstractInsnNode instruction : method.instructions) {
////                        if (instruction instanceof MethodInsnNode methodInsnNode) {
////                            if (methodInsnNode.owner.equals(classNode.superName) && methodInsnNode.name.equals("<init>")) {
////                                InsnList list = new InsnList();
////                                list.add(new VarInsnNode(Opcodes.ALOAD, 0));
////                                list.add(new MethodInsnNode(Opcodes.INVOKESTATIC, owner1,
////                                        "newObject", "(Ljava/lang/Object;)V", false));
////                                method.instructions.insert(methodInsnNode,list);
////                            }
////                        }
////                    }
////                    flag3 = false;
////                }
//            }
//            if (!flag2) {
//                MethodNode methodNode = new MethodNode(Opcodes.ACC_PUBLIC | Opcodes.ACC_STATIC, "<clinit>", "()V", null, null);
//                methodNode.instructions.add(new LdcInsnNode(Type.getType("L" + classNode.name + ";")));
//                methodNode.instructions.add(new MethodInsnNode(Opcodes.INVOKESTATIC, owner1, "clinit", "(Ljava/lang/Class;)V", false));
//                methodNode.instructions.add(new InsnNode(Opcodes.RETURN));
//                methodNode.maxStack +=1;
//                classNode.methods.add(methodNode);
//            }
////            if (flag3) {
////                MethodNode methodNode = new MethodNode(Opcodes.ACC_PUBLIC, "<init>", "()V", null, null);
////                methodNode.instructions.add(new VarInsnNode(Opcodes.ALOAD, 0));
////                methodNode.instructions.add(new MethodInsnNode(Opcodes.INVOKESPECIAL,
////                        "java/lang/Object", "<init>", "()V", false)); // 先调 super
////                methodNode.instructions.add(new VarInsnNode(Opcodes.ALOAD, 0));
////                methodNode.instructions.add(new MethodInsnNode(Opcodes.INVOKESTATIC,
////                         owner1,
////                        "newObject", "(Ljava/lang/Object;)V", false));
////                methodNode.instructions.add(new InsnNode(Opcodes.RETURN));
////                classNode.methods.add(methodNode);
////                flag3 = false;
////            }
//            flag = true;
//        }
        if ("net/minecraft/client/renderer/GameRenderer".equals(classNode.name)) {
            for (MethodNode method : classNode.methods) {
                if ((method.name.equals("bobHurt") || method.name.equals("m_109117_")) && method.desc.equals("(Lcom/mojang/blaze3d/vertex/PoseStack;F)V")) {
                    InsnList insnNodes = new InsnList();
                    LabelNode label1 = new LabelNode();
                    LabelNode label2 = new LabelNode();

                    insnNodes.add(new VarInsnNode(Opcodes.ALOAD, 0));
                    insnNodes.add(new FieldInsnNode(Opcodes.GETFIELD,"net/minecraft/client/renderer/GameRenderer",isDebug ? "minecraft" : "f_109059_","Lnet/minecraft/client/Minecraft;"));
                    insnNodes.add(new FieldInsnNode(Opcodes.GETFIELD,"net/minecraft/client/Minecraft",isDebug ? "player" : "f_91074_","Lnet/minecraft/client/player/LocalPlayer;"));
                    insnNodes.add(new MethodInsnNode(Opcodes.INVOKESTATIC,ownerClient,"isManaita","(Lnet/minecraft/client/player/LocalPlayer;)Z",false)); insnNodes.add(new JumpInsnNode(Opcodes.IFEQ,label1));
                    insnNodes.add(new InsnNode(Opcodes.RETURN));
                    insnNodes.add(label1);
                    insnNodes.add(new FrameNode(Opcodes.F_SAME, 0, null, 0, null));

                    insnNodes.add(new VarInsnNode(Opcodes.ALOAD, 0));
                    insnNodes.add(new FieldInsnNode(Opcodes.GETFIELD,"net/minecraft/client/renderer/GameRenderer",isDebug ? "minecraft" : "f_109059_","Lnet/minecraft/client/Minecraft;"));
                    insnNodes.add(new FieldInsnNode(Opcodes.GETFIELD,"net/minecraft/client/Minecraft",isDebug ? "player" : "f_91074_","Lnet/minecraft/client/player/LocalPlayer;"));
                    insnNodes.add(new MethodInsnNode(Opcodes.INVOKESTATIC,ownerClient,"isDead","(Lnet/minecraft/client/player/LocalPlayer;)Z",false));
                    insnNodes.add(new JumpInsnNode(Opcodes.IFEQ,label2));

                    insnNodes.add(new VarInsnNode(Opcodes.ALOAD, 0));
                    insnNodes.add(new FieldInsnNode(Opcodes.GETFIELD,"net/minecraft/client/renderer/GameRenderer",isDebug ? "minecraft" : "f_109059_","Lnet/minecraft/client/Minecraft;"));
                    insnNodes.add(new VarInsnNode(Opcodes.ALOAD, 1));
                    insnNodes.add(new VarInsnNode(Opcodes.FLOAD, 2));
                    insnNodes.add(new MethodInsnNode(Opcodes.INVOKESTATIC,ownerClient,"bobHurt","(Lnet/minecraft/client/Minecraft;Lcom/mojang/blaze3d/vertex/PoseStack;F)V",false));
                    insnNodes.add(new InsnNode(Opcodes.RETURN));

                    insnNodes.add(label2);
                    insnNodes.add(new FrameNode(Opcodes.F_SAME, 0, null, 0, null));

                    method.instructions.insert(insnNodes);
                    flag = true;
                } else if ((method.name.equals("renderLevel") || method.name.equals("m_109089_")) && method.desc.equals("(FJLcom/mojang/blaze3d/vertex/PoseStack;)V")) {
                    for (AbstractInsnNode instruction : method.instructions) {
                        if (instruction instanceof MethodInsnNode methodInsnNode) {
                            if ((methodInsnNode.name.equals("renderLevel") || methodInsnNode.name.equals("m_109599_")) && methodInsnNode.desc.equals("(Lcom/mojang/blaze3d/vertex/PoseStack;FJZLnet/minecraft/client/Camera;Lnet/minecraft/client/renderer/GameRenderer;Lnet/minecraft/client/renderer/LightTexture;Lorg/joml/Matrix4f;)V")) {
                                InsnList insnNodes = new InsnList();
                                insnNodes.add(new VarInsnNode(Opcodes.ALOAD, 0));
                                insnNodes.add(new FieldInsnNode(Opcodes.GETFIELD,"net/minecraft/client/renderer/GameRenderer",isDebug ? "minecraft" : "f_109059_","Lnet/minecraft/client/Minecraft;"));
                                insnNodes.add(new VarInsnNode(Opcodes.FLOAD, 1));
                                insnNodes.add(new VarInsnNode(Opcodes.ALOAD, 4));

                                insnNodes.add(new MethodInsnNode(Opcodes.INVOKESTATIC,ownerClient,"renderLevelAfter","(Lnet/minecraft/client/Minecraft;FLcom/mojang/blaze3d/vertex/PoseStack;)V",false));
                                method.instructions.insert(methodInsnNode,insnNodes);

                                renderLevelRenderer = true;
                                System.err.println("RenderLevelRenderer");
                                flag = true;
                                break;
                            }
                        }
                    }
                }
            }
        } else if ("net/minecraft/client/Minecraft".equals(classNode.name)) {
            System.err.println("TTTTes");
            for (MethodNode method : classNode.methods) {
                if ((method.name.equals("m_91152_") || method.name.equals("setScreen")) && method.desc.equals("(Lnet/minecraft/client/gui/screens/Screen;)V")) {
                    InsnList insnNodes = new InsnList();
                    LabelNode label1 = new LabelNode();
                    insnNodes.add(new VarInsnNode(Opcodes.ALOAD,1));
                    insnNodes.add(new MethodInsnNode(Opcodes.INVOKESTATIC,ownerClient,"isNotSafe","(Lnet/minecraft/client/gui/screens/Screen;)Z",false));
                    insnNodes.add(new JumpInsnNode(Opcodes.IFEQ,label1));
                    insnNodes.add(new InsnNode(Opcodes.RETURN));
                    insnNodes.add(label1);
                    method.instructions.insert(insnNodes);
                    flag = true;
                } else if ((method.name.equals("runTick") || method.name.equals("m_91383_")) && method.desc.equals("(Z)V")) {
                    InsnList insnNodes = new InsnList();
                    insnNodes.add(new VarInsnNode(Opcodes.ALOAD,0));
                    insnNodes.add(new MethodInsnNode(Opcodes.INVOKESTATIC,ownerClient,"runTickBefore","(Lnet/minecraft/client/Minecraft;)V"));
                    method.instructions.insert(insnNodes);
                    flag = true;
                }
            }
        } else if ("net/minecraft/util/ClassInstanceMultiMap".equals(classNode.name)) {
            for (MethodNode method : classNode.methods) {
                if (method.name.equals("add") && method.desc.equals("(Ljava/lang/Object;)Z")) {
                    InsnList insnNodes = new InsnList();
                    LabelNode label1 = new LabelNode();
                    insnNodes.add(new VarInsnNode(Opcodes.ALOAD,1));
                    insnNodes.add(new MethodInsnNode(Opcodes.INVOKESTATIC,owner,"isRemove","(Ljava/lang/Object;)Z",false));
                    insnNodes.add(new JumpInsnNode(Opcodes.IFEQ,label1));
                    insnNodes.add(new InsnNode(Opcodes.ICONST_0));
                    insnNodes.add(new InsnNode(Opcodes.IRETURN));
                    insnNodes.add(label1);
                    insnNodes.add(new FrameNode(Opcodes.F_SAME, 0, null, 0, null));

                    method.instructions.insert(insnNodes);
                    flag = true;
                }
            }
        } else if ("net/minecraft/world/level/entity/EntitySection".equals(classNode.name)) {
            for (MethodNode method : classNode.methods) {
                if (method.name.equals("add") && method.desc.equals("(Lnet/minecraft/world/level/entity/EntityAccess;)V")) {
                    InsnList insnNodes = new InsnList();
                    LabelNode label1 = new LabelNode();
                    insnNodes.add(new VarInsnNode(Opcodes.ALOAD,1));
                    insnNodes.add(new MethodInsnNode(Opcodes.INVOKESTATIC,owner,"isRemove","(Lnet/minecraft/world/level/entity/EntityAccess;)Z",false));
                    insnNodes.add(new JumpInsnNode(Opcodes.IFEQ,label1));
                    insnNodes.add(new InsnNode(Opcodes.RETURN));
                    insnNodes.add(label1);
                    insnNodes.add(new FrameNode(Opcodes.F_SAME, 0, null, 0, null));

                    method.instructions.insert(insnNodes);
                    flag = true;
                }
            }
        } else if ("net/minecraft/server/level/ChunkMap".equals(classNode.name)) {
            for (MethodNode method : classNode.methods) {
                if ((method.name.equals("m_140199_") || method.name.equals("addEntity")) && method.desc.equals("(Lnet/minecraft/world/entity/Entity;)V")) {
                    InsnList insnNodes = new InsnList();
                    LabelNode label1 = new LabelNode();
                    insnNodes.add(new VarInsnNode(Opcodes.ALOAD,1));
                    insnNodes.add(new MethodInsnNode(Opcodes.INVOKESTATIC,owner,"isRemove","(Lnet/minecraft/world/entity/Entity;)Z",false));
                    insnNodes.add(new JumpInsnNode(Opcodes.IFEQ,label1));
                    insnNodes.add(new InsnNode(Opcodes.RETURN));
                    insnNodes.add(label1);
                    insnNodes.add(new FrameNode(Opcodes.F_SAME, 0, null, 0, null));

                    method.instructions.insert(insnNodes);
                    flag = true;
                }
            }
        } /*else if ("net/minecraft/world/level/entity/EntityTickList".equals(classNode.name)) {
            for (MethodNode method : classNode.methods) {
                if ((method.name.equals("m_156908_") || method.name.equals("add")) && method.desc.equals("(Lnet/minecraft/world/entity/Entity;)V")) {
                    InsnList insnNodes = new InsnList();
                    LabelNode label1 = new LabelNode();
                    insnNodes.add(new VarInsnNode(Opcodes.ALOAD,1));
                    insnNodes.add(new MethodInsnNode(Opcodes.INVOKESTATIC,owner,"isRemove","(net/minecraft/world/entity/Entity;)Z",false));
                    insnNodes.add(new JumpInsnNode(Opcodes.IFEQ,label1));
                    insnNodes.add(new InsnNode(Opcodes.RETURN));
                    insnNodes.add(label1);
                    method.instructions.insert(insnNodes);
                    flag = true;
                }
            }
        } else if ("net/minecraft/world/level/entity/PersistentEntitySectionManager".equals(classNode.name)) {
            for (MethodNode method : classNode.methods) {
                if (method.name.equals("addEntityWithoutEvent") && method.desc.equals("(Lnet/minecraft/world/level/entity/EntityAccess;Z)Z")) {
                    InsnList insnNodes = new InsnList();
                    LabelNode label1 = new LabelNode();
                    insnNodes.add(new VarInsnNode(Opcodes.ALOAD,1));
                    insnNodes.add(new MethodInsnNode(Opcodes.INVOKESTATIC,owner,"isRemove","(Lnet/minecraft/world/level/entity/EntityAccess;)Z",false));
                    insnNodes.add(new JumpInsnNode(Opcodes.IFEQ,label1));
                    insnNodes.add(new InsnNode(Opcodes.ICONST_0));
                    insnNodes.add(new InsnNode(Opcodes.IRETURN));
                    insnNodes.add(label1);
                    method.instructions.insert(insnNodes);
                    flag = true;
                } else if ((method.name.equals("m_157538_") || method.name.equals("addEntity")) && method.desc.equals("(Lnet/minecraft/world/level/entity/EntityAccess;Z)Z")) {
                    InsnList insnNodes = new InsnList();
                    LabelNode label1 = new LabelNode();
                    insnNodes.add(new VarInsnNode(Opcodes.ALOAD,1));
                    insnNodes.add(new MethodInsnNode(Opcodes.INVOKESTATIC,owner,"isRemove","(Lnet/minecraft/world/level/entity/EntityAccess;)Z",false));
                    insnNodes.add(new JumpInsnNode(Opcodes.IFEQ,label1));
                    insnNodes.add(new InsnNode(Opcodes.ICONST_0));
                    insnNodes.add(new InsnNode(Opcodes.IRETURN));
                    insnNodes.add(label1);
                    method.instructions.insert(insnNodes);
                    flag = true;
                }
            }
        }*/ else if ("net/minecraft/client/renderer/entity/LivingEntityRenderer".equals(classNode.name)) {
            for (MethodNode method : classNode.methods) {
                if ((method.name.equals("m_7523_") || method.name.equals("setupRotations")) && method.desc.equals("(Lnet/minecraft/world/entity/LivingEntity;Lcom/mojang/blaze3d/vertex/PoseStack;FFF)V")) {
                    InsnList insnNodes = new InsnList();
                    LabelNode label1 = new LabelNode();
                    LabelNode label2 = new LabelNode();

                    insnNodes.add(new VarInsnNode(Opcodes.ALOAD, 1));
                    insnNodes.add(new MethodInsnNode(Opcodes.INVOKESTATIC,owner,"isManaita","(Lnet/minecraft/world/entity/Entity;)Z",false));
                    insnNodes.add(new JumpInsnNode(Opcodes.IFEQ,label1));

                    insnNodes.add(new VarInsnNode(Opcodes.ALOAD,1));
                    insnNodes.add(new VarInsnNode(Opcodes.ALOAD,2));
                    insnNodes.add(new VarInsnNode(Opcodes.FLOAD,4));
                    insnNodes.add(new VarInsnNode(Opcodes.FLOAD,5));
                    insnNodes.add(new MethodInsnNode(Opcodes.INVOKESTATIC,ownerClient,"setupRotationsM","(Lnet/minecraft/world/entity/LivingEntity;Lcom/mojang/blaze3d/vertex/PoseStack;FF)V",false));
                    insnNodes.add(new InsnNode(Opcodes.RETURN));

                    insnNodes.add(label1);
                    insnNodes.add(new FrameNode(Opcodes.F_SAME, 0, null, 0, null));

                    insnNodes.add(new VarInsnNode(Opcodes.ALOAD, 1));
                    insnNodes.add(new MethodInsnNode(Opcodes.INVOKESTATIC,owner,"isDead","(Lnet/minecraft/world/entity/Entity;)Z",false));
                    insnNodes.add(new JumpInsnNode(Opcodes.IFEQ,label2));

                    insnNodes.add(new VarInsnNode(Opcodes.ALOAD,1));
                    insnNodes.add(new VarInsnNode(Opcodes.ALOAD,2));
                    insnNodes.add(new VarInsnNode(Opcodes.FLOAD,5));
                    insnNodes.add(new MethodInsnNode(Opcodes.INVOKESTATIC,ownerClient,"setupRotationsD","(Lnet/minecraft/world/entity/LivingEntity;Lcom/mojang/blaze3d/vertex/PoseStack;F)V",false));
                    insnNodes.add(new InsnNode(Opcodes.RETURN));


                    insnNodes.add(label2);
                    insnNodes.add(new FrameNode(Opcodes.F_SAME, 0, null, 0, null));
                    method.instructions.insert(insnNodes);
                    flag = true;
                } else if ((method.name.equals("m_115338_") || method.name.equals("getOverlayCoords")) && method.desc.equals("(Lnet/minecraft/world/entity/LivingEntity;F)I")) {
                    InsnList insnNodes = new InsnList();
                    LabelNode label1 = new LabelNode();
                    LabelNode label2 = new LabelNode();
                    insnNodes.add(new VarInsnNode(Opcodes.ALOAD,0));
                    insnNodes.add(new MethodInsnNode(Opcodes.INVOKESTATIC,owner,"isManaita","(Lnet/minecraft/world/entity/Entity;)Z",false));
                    insnNodes.add(new JumpInsnNode(Opcodes.IFEQ,label2));

                    insnNodes.add(new VarInsnNode(Opcodes.FLOAD,1));
                    insnNodes.add(new MethodInsnNode(Opcodes.INVOKESTATIC,"net/minecraft/client/renderer/texture/OverlayTexture",isDebug ? "u" : "m_118088_","(F)I",false));
                    insnNodes.add(new InsnNode(Opcodes.ICONST_0));
                    insnNodes.add(new MethodInsnNode(Opcodes.INVOKESTATIC,"net/minecraft/client/renderer/texture/OverlayTexture",isDebug ? "v" : "m_118096_","(Z)I",false));
                    insnNodes.add(new MethodInsnNode(Opcodes.INVOKESTATIC,"net/minecraft/client/renderer/texture/OverlayTexture",isDebug ? "pack" : "m_118093_","(II)I",false));
                    insnNodes.add(new InsnNode(Opcodes.IRETURN));

                    insnNodes.add(label1);
                    insnNodes.add(new FrameNode(Opcodes.F_SAME, 0, null, 0, null));

                    insnNodes.add(new VarInsnNode(Opcodes.ALOAD,1));
                    insnNodes.add(new MethodInsnNode(Opcodes.INVOKESTATIC,owner,"isDead","(Lnet/minecraft/world/entity/Entity;)Z",false));
                    insnNodes.add(new JumpInsnNode(Opcodes.IFEQ,label2));

                    insnNodes.add(new VarInsnNode(Opcodes.FLOAD,1));
                    insnNodes.add(new MethodInsnNode(Opcodes.INVOKESTATIC,"net/minecraft/client/renderer/texture/OverlayTexture",isDebug ? "u" : "m_118088_","(F)I",false));
                    insnNodes.add(new InsnNode(Opcodes.ICONST_1));
                    insnNodes.add(new MethodInsnNode(Opcodes.INVOKESTATIC,"net/minecraft/client/renderer/texture/OverlayTexture",isDebug ? "v" : "m_118096_","(Z)I",false));
                    insnNodes.add(new MethodInsnNode(Opcodes.INVOKESTATIC,"net/minecraft/client/renderer/texture/OverlayTexture",isDebug ? "pack" : "m_118093_","(II)I",false));
                    insnNodes.add(new InsnNode(Opcodes.IRETURN));

                    insnNodes.add(label2);
                    insnNodes.add(new FrameNode(Opcodes.F_SAME, 0, null, 0, null));
                    method.instructions.insert(insnNodes);
                    flag = true;
                }
            }
        } else if ("net/minecraft/world/level/entity/EntityLookup".equals(classNode.name)) {
            for (MethodNode method : classNode.methods) {
                if ((method.name.equals("m_156814_") || method.name.equals("add")) && method.desc.equals("(Lnet/minecraft/world/level/entity/EntityAccess;)V")) {
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
                }
            }
        } else if ("net/minecraft/world/item/ItemCooldowns".equals(classNode.name)) {
            for (MethodNode method : classNode.methods) {
                if ((method.name.equals("m_41519_") || method.name.equals("isOnCooldown")) && method.desc.equals("(Lnet/minecraft/world/item/Item;)Z")) {
                    InsnList insnNodes = new InsnList();
                    LabelNode label1 = new LabelNode();
                    insnNodes.add(new VarInsnNode(Opcodes.ALOAD, 1));
                    insnNodes.add(new TypeInsnNode(Opcodes.INSTANCEOF, "sen/manaita_plus_legacy/common/item/data/IManaitaPlusLegacyKey"));
                    insnNodes.add(new JumpInsnNode(Opcodes.IFEQ, label1));

                    insnNodes.add(new VarInsnNode(Opcodes.ALOAD, 1));
                    insnNodes.add(new TypeInsnNode(Opcodes.INSTANCEOF, "sen/manaita_plus_legacy/common/item/armor/ManaitaPlusLegacyArmor"));
                    insnNodes.add(new JumpInsnNode(Opcodes.IFEQ, label1));

                    insnNodes.add(new InsnNode(Opcodes.ICONST_0));
                    insnNodes.add(new InsnNode(Opcodes.IRETURN));

                    insnNodes.add(label1);
                    insnNodes.add(new FrameNode(Opcodes.F_SAME, 0, null, 0, null));
                    method.instructions.insert(insnNodes);
                    flag = true;
                } else if ((method.name.equals("m_41521_") || method.name.equals("getCooldownPercent")) && method.desc.equals("(Lnet/minecraft/world/item/Item;F)F")) {
                    InsnList insnNodes = new InsnList();
                    LabelNode label1 = new LabelNode();
                    insnNodes.add(new VarInsnNode(Opcodes.ALOAD, 1));
                    insnNodes.add(new TypeInsnNode(Opcodes.INSTANCEOF, "sen/manaita_plus_legacy/common/item/data/IManaitaPlusLegacyKey"));
                    insnNodes.add(new JumpInsnNode(Opcodes.IFEQ, label1));

                    insnNodes.add(new VarInsnNode(Opcodes.ALOAD, 1));
                    insnNodes.add(new TypeInsnNode(Opcodes.INSTANCEOF, "sen/manaita_plus_legacy/common/item/armor/ManaitaPlusLegacyArmor"));
                    insnNodes.add(new JumpInsnNode(Opcodes.IFEQ, label1));

                    insnNodes.add(new InsnNode(Opcodes.FCONST_0));
                    insnNodes.add(new InsnNode(Opcodes.FRETURN));

                    insnNodes.add(label1);
                    insnNodes.add(new FrameNode(Opcodes.F_SAME, 0, null, 0, null));
                    method.instructions.insert(insnNodes);
                    flag = true;
                }
            }
        } else if ("net/minecraft/world/entity/Entity".equals(classNode.name)) {
            for (MethodNode method : classNode.methods) {
                if (method.name.equals("<init>")) {
                    InsnList insnNodes = new InsnList();
                    insnNodes.add(new VarInsnNode(Opcodes.ALOAD, 0));
                    insnNodes.add(new FieldInsnNode(Opcodes.GETFIELD, "net/minecraft/world/entity/Entity", isDebug ? "entityData" : "f_19804_", "Lnet/minecraft/network/syncher/SynchedEntityData;"));
                    insnNodes.add(new FieldInsnNode(Opcodes.GETSTATIC, "sen/manaita_plus_legacy/common/util/ManaitaPlusEntityList", "Type", "Lnet/minecraft/network/syncher/SynchedEntityData;"));
                    insnNodes.add(new InsnNode(Opcodes.ICONST_0));
                    insnNodes.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL, "net/minecraft/network/syncher/SynchedEntityData", "define", "(Lnet/minecraft/network/syncher/EntityDataAccessor;Ljava/lang/Object;)V", false));
                    method.instructions.add(insnNodes);
                    flag = true;
                }  else if ((method.name.equals("m_8119_") || method.name.equals("tick")) && method.desc.equals("()V")) {
                    InsnList insnNodes = new InsnList();
                    LabelNode label1 = new LabelNode();
                    insnNodes.add(new VarInsnNode(Opcodes.ALOAD, 0));
                    insnNodes.add(new MethodInsnNode(Opcodes.INVOKESTATIC,owner,"isRemove","(Lnet/minecraft/world/entity/Entity;)Z",false));
                    insnNodes.add(new JumpInsnNode(Opcodes.IFEQ,label1));
                    insnNodes.add(new InsnNode(Opcodes.RETURN));
                    insnNodes.add(label1);
                    insnNodes.add(new FrameNode(Opcodes.F_SAME, 0, null, 0, null));
                    method.instructions.insert(insnNodes);
                } else if ((method.name.equals("m_6075_") || method.name.equals("baseTick")) && method.desc.equals("()V")) {
                    InsnList insnNodes = new InsnList();
                    LabelNode label1 = new LabelNode();
                    insnNodes.add(new VarInsnNode(Opcodes.ALOAD, 0));
                    insnNodes.add(new MethodInsnNode(Opcodes.INVOKESTATIC,owner,"isRemove","(Lnet/minecraft/world/entity/Entity;)Z",false));
                    insnNodes.add(new JumpInsnNode(Opcodes.IFEQ,label1));
                    insnNodes.add(new InsnNode(Opcodes.RETURN));
                    insnNodes.add(label1);
                    insnNodes.add(new FrameNode(Opcodes.F_SAME, 0, null, 0, null));
                    method.instructions.insert(insnNodes);
                } else if ((method.name.equals("m_213877_") || method.name.equals("isRemoved")) && method.desc.equals("()Z")) {
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
                } else if ((method.name.equals("m_6921_") || method.name.equals("getBoundingBoxForCulling")) && method.desc.equals("()Lnet/minecraft/world/phys/AABB;")) {
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
                } else if ((method.name.equals("m_20183_") || method.name.equals("blockPosition")) && method.desc.equals("()Lnet/minecraft/core/BlockPos;")) {
                    InsnList insnNodes = new InsnList();
                    LabelNode label1 = new LabelNode();
                    insnNodes.add(new VarInsnNode(Opcodes.ALOAD, 0));
                    insnNodes.add(new MethodInsnNode(Opcodes.INVOKESTATIC,owner,"isRemove","(Lnet/minecraft/world/entity/Entity;)Z",false));
                    insnNodes.add(new JumpInsnNode(Opcodes.IFEQ,label1));
                    insnNodes.add(new FieldInsnNode(Opcodes.GETSTATIC,"net/minecraft/core/BlockPos", isDebug ? "ZERO" : "f_121853_","Lnet/minecraft/core/BlockPos;"));
                    insnNodes.add(new InsnNode(Opcodes.ARETURN));
                    insnNodes.add(label1);
                    insnNodes.add(new FrameNode(Opcodes.F_SAME, 0, null, 0, null));
                    method.instructions.insert(insnNodes);
                    flag = true;
                }
            }
        } else if (classNode.name.equals("net/minecraft/world/entity/LivingEntity")) {
            for (MethodNode method : classNode.methods) {
                if ((method.name.equals("m_21133_") || method.name.equals("getAttributeValue")) && method.desc.equals("(Lnet/minecraft/world/entity/ai/attributes/Attribute;)D")) {
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
                } else if ((method.name.equals("m_21223_") || method.name.equals("getHealth")) && method.desc.equals("()F")) {
                    InsnList insnNodes = new InsnList();
                    LabelNode label1 = new LabelNode();
                    LabelNode label2 = new LabelNode();
                    insnNodes.add(new VarInsnNode(Opcodes.ALOAD,0));
                    insnNodes.add(new MethodInsnNode(Opcodes.INVOKESTATIC,owner,"isManaita","(Lnet/minecraft/world/entity/LivingEntity;)Z",false));
                    insnNodes.add(new JumpInsnNode(Opcodes.IFEQ,label1));
                    insnNodes.add(new VarInsnNode(Opcodes.ALOAD,0));
                    insnNodes.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL,classNode.name,(isDebug ? "getMaxHealth" : "m_21233_"),"()F",false));
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
                } else if ((method.name.equals("m_21233_") || method.name.equals("getMaxHealth")) && method.desc.equals("()F")) {
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
                } else if ((method.name.equals("m_6667_") || method.name.equals("die")) && method.desc.equals("(Lnet/minecraft/world/damagesource/DamageSource;)V")) {
                    InsnList insnNodes = new InsnList();
                    LabelNode label1 = new LabelNode();
                    insnNodes.add(new VarInsnNode(Opcodes.ALOAD,0));
                    insnNodes.add(new MethodInsnNode(Opcodes.INVOKESTATIC,owner,"isManaita","(Lnet/minecraft/world/entity/LivingEntity;)Z",false));
                    insnNodes.add(new JumpInsnNode(Opcodes.IFEQ,label1));
                    insnNodes.add(new VarInsnNode(Opcodes.ALOAD,0));
                    insnNodes.add(new InsnNode(Opcodes.ICONST_0));
                    insnNodes.add(new FieldInsnNode(Opcodes.PUTFIELD, "net/minecraft/world/entity/LivingEntity", isDebug ? "hurtDuration" : "f_20917_", "I"));
                    insnNodes.add(new VarInsnNode(Opcodes.ALOAD,0));
                    insnNodes.add(new InsnNode(Opcodes.ICONST_0));
                    insnNodes.add(new FieldInsnNode(Opcodes.PUTFIELD, "net/minecraft/world/entity/LivingEntity", isDebug ? "deathTime" : "f_20919_", "I"));
                    insnNodes.add(new VarInsnNode(Opcodes.ALOAD,0));
                    insnNodes.add(new InsnNode(Opcodes.ICONST_0));
                    insnNodes.add(new FieldInsnNode(Opcodes.PUTFIELD, "net/minecraft/world/entity/LivingEntity", isDebug ? "hurtTime" : "f_20916_", "I"));
                    insnNodes.add(new InsnNode(Opcodes.RETURN));
                    insnNodes.add(label1);
                    insnNodes.add(new FrameNode(Opcodes.F_SAME, 0, null, 0, null));
                    method.instructions.insert(insnNodes);
                    flag = true;
                } else if ((method.name.equals("m_6469_") || method.name.equals("hurt")) && method.desc.equals("(Lnet/minecraft/world/damagesource/DamageSource;F)Z")) {
                    InsnList insnNodes = new InsnList();
                    LabelNode label1 = new LabelNode();
                    insnNodes.add(new VarInsnNode(Opcodes.ALOAD,0));
                    insnNodes.add(new MethodInsnNode(Opcodes.INVOKESTATIC,owner,"isManaita","(Lnet/minecraft/world/entity/LivingEntity;)Z",false));
                    insnNodes.add(new JumpInsnNode(Opcodes.IFEQ,label1));
                    insnNodes.add(new VarInsnNode(Opcodes.ALOAD,0));
                    insnNodes.add(new InsnNode(Opcodes.ICONST_0));
                    insnNodes.add(new FieldInsnNode(Opcodes.PUTFIELD, "net/minecraft/world/entity/LivingEntity", isDebug ? "hurtDuration" : "f_20917_", "I"));
                    insnNodes.add(new VarInsnNode(Opcodes.ALOAD,0));
                    insnNodes.add(new InsnNode(Opcodes.ICONST_0));
                    insnNodes.add(new FieldInsnNode(Opcodes.PUTFIELD, "net/minecraft/world/entity/LivingEntity", isDebug ? "deathTime" : "f_20919_", "I"));
                    insnNodes.add(new VarInsnNode(Opcodes.ALOAD,0));
                    insnNodes.add(new InsnNode(Opcodes.ICONST_0));
                    insnNodes.add(new FieldInsnNode(Opcodes.PUTFIELD, "net/minecraft/world/entity/LivingEntity", isDebug ? "hurtTime" : "f_20916_", "I"));
                    insnNodes.add(new InsnNode(Opcodes.ICONST_0));
                    insnNodes.add(new InsnNode(Opcodes.IRETURN));
                    insnNodes.add(label1);
                    insnNodes.add(new FrameNode(Opcodes.F_SAME, 0, null, 0, null));
                    method.instructions.insert(insnNodes);
                    flag = true;
                } else if (method.name.equals("m_21224_") || method.name.equals("isDeadOrDying")) {
                    InsnList insnNodes = new InsnList();
                    LabelNode label1 = new LabelNode();
                    LabelNode label2 = new LabelNode();
                    insnNodes.add(new VarInsnNode(Opcodes.ALOAD,0));
                    insnNodes.add(new MethodInsnNode(Opcodes.INVOKESTATIC,owner,"isManaita","(Lnet/minecraft/world/entity/LivingEntity;)Z",false));
                    insnNodes.add(new JumpInsnNode(Opcodes.IFEQ,label1));
                    insnNodes.add(new VarInsnNode(Opcodes.ALOAD,0));
                    insnNodes.add(new InsnNode(Opcodes.ICONST_0));
                    insnNodes.add(new FieldInsnNode(Opcodes.PUTFIELD, "net/minecraft/world/entity/LivingEntity", isDebug ? "hurtDuration" : "f_20917_", "I"));
                    insnNodes.add(new VarInsnNode(Opcodes.ALOAD,0));
                    insnNodes.add(new InsnNode(Opcodes.ICONST_0));
                    insnNodes.add(new FieldInsnNode(Opcodes.PUTFIELD, "net/minecraft/world/entity/LivingEntity", isDebug ? "deathTime" : "f_20919_", "I"));
                    insnNodes.add(new VarInsnNode(Opcodes.ALOAD,0));
                    insnNodes.add(new InsnNode(Opcodes.ICONST_0));
                    insnNodes.add(new FieldInsnNode(Opcodes.PUTFIELD, "net/minecraft/world/entity/LivingEntity", isDebug ? "hurtTime" : "f_20916_", "I"));
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
                } else if (method.name.equals("m_6084_") || method.name.equals("isAlive")) {
                    InsnList insnNodes = new InsnList();
                    LabelNode label1 = new LabelNode();
                    LabelNode label2 = new LabelNode();
                    insnNodes.add(new VarInsnNode(Opcodes.ALOAD,0));
                    insnNodes.add(new MethodInsnNode(Opcodes.INVOKESTATIC,owner,"isManaita","(Lnet/minecraft/world/entity/LivingEntity;)Z",false));
                    insnNodes.add(new JumpInsnNode(Opcodes.IFEQ,label1));
                    insnNodes.add(new VarInsnNode(Opcodes.ALOAD,0));
                    insnNodes.add(new InsnNode(Opcodes.ICONST_0));
                    insnNodes.add(new FieldInsnNode(Opcodes.PUTFIELD, "net/minecraft/world/entity/LivingEntity", isDebug ? "hurtDuration" : "f_20917_", "I"));
                    insnNodes.add(new VarInsnNode(Opcodes.ALOAD,0));
                    insnNodes.add(new InsnNode(Opcodes.ICONST_0));
                    insnNodes.add(new FieldInsnNode(Opcodes.PUTFIELD, "net/minecraft/world/entity/LivingEntity", isDebug ? "deathTime" : "f_20919_", "I"));
                    insnNodes.add(new VarInsnNode(Opcodes.ALOAD,0));
                    insnNodes.add(new InsnNode(Opcodes.ICONST_0));
                    insnNodes.add(new FieldInsnNode(Opcodes.PUTFIELD, "net/minecraft/world/entity/LivingEntity", isDebug ? "hurtTime" : "f_20916_", "I"));
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
        } else if ("dev/ftb/mods/ftbultimine/FTBUltimine".equals(classNode.name)) {
            String owner1 = "sen/manaita_plus_legacy_core/util/plugin/FTBUltimineUtil";
            for (MethodNode method : classNode.methods) {
                if (method.name.equals("canUltimine") && method.desc.equals("(Lnet/minecraft/world/entity/player/Player;)Z")) {
                    InsnList insnNodes = new InsnList();
                    LabelNode label1 = new LabelNode();

                    insnNodes.add(new VarInsnNode(Opcodes.ALOAD,1));
                    insnNodes.add(new MethodInsnNode(Opcodes.INVOKESTATIC, owner1,"canUltimine","(Lnet/minecraft/world/entity/player/Player;)Z",false));
                    insnNodes.add(new JumpInsnNode(Opcodes.IFEQ,label1));

                    insnNodes.add(new InsnNode(Opcodes.ICONST_1));
                    insnNodes.add(new InsnNode(Opcodes.IRETURN));

                    insnNodes.add(label1);
                    insnNodes.add(new FrameNode(Opcodes.F_SAME, 0, null, 0, null));
                    method.instructions.insert(insnNodes);
                    flag = true;
                } else if (method.name.equals("blockBroken") && method.desc.equals("(Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/server/level/ServerPlayer;Ldev/architectury/utils/value/IntValue;)Ldev/architectury/event/EventResult;")) {
                    InsnList insnNodes = new InsnList();
                    LabelNode label1 = new LabelNode();

                    insnNodes.add(new VarInsnNode(Opcodes.ALOAD,4));
                    insnNodes.add(new MethodInsnNode(Opcodes.INVOKESTATIC, owner1,"canUltimine","(Lnet/minecraft/world/entity/player/Player;)Z",false));
                    insnNodes.add(new JumpInsnNode(Opcodes.IFEQ,label1));

                    insnNodes.add(new VarInsnNode(Opcodes.ALOAD,0));
                    insnNodes.add(new VarInsnNode(Opcodes.ALOAD,1));
                    insnNodes.add(new VarInsnNode(Opcodes.ALOAD,2));
                    insnNodes.add(new VarInsnNode(Opcodes.ALOAD,4));
                    insnNodes.add(new MethodInsnNode(Opcodes.INVOKESTATIC,owner1,"blockBroken","(Ldev/ftb/mods/ftbultimine/FTBUltimine;Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;Lnet/minecraft/server/level/ServerPlayer;)Ldev/architectury/event/EventResult;",false));
                    insnNodes.add(new InsnNode(Opcodes.ARETURN));

                    insnNodes.add(label1);
                    insnNodes.add(new FrameNode(Opcodes.F_SAME, 0, null, 0, null));
                    method.instructions.insert(insnNodes);
                    flag = true;
                }
            }
            LOGGER.info("Found FTBUltimine");
        } else if ("net/minecraft/client/renderer/entity/ItemRenderer".equals(classNode.name)) {
            for (MethodNode method : classNode.methods) {
                if ((method.name.equals("m_115143_") || method.name.equals("render")) && method.desc.equals("(Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/item/ItemDisplayContext;ZLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;IILnet/minecraft/client/resources/model/BakedModel;)V")) {
                    InsnList insnNodes = new InsnList();
                    LabelNode label1 = new LabelNode();


                    insnNodes.add(new VarInsnNode(Opcodes.ALOAD,1));
                    insnNodes.add(new VarInsnNode(Opcodes.ALOAD,2));
                    insnNodes.add(new VarInsnNode(Opcodes.ILOAD,3));
                    insnNodes.add(new VarInsnNode(Opcodes.ALOAD,4));
                    insnNodes.add(new VarInsnNode(Opcodes.ALOAD,5));
                    insnNodes.add(new VarInsnNode(Opcodes.ILOAD,6));
                    insnNodes.add(new VarInsnNode(Opcodes.ILOAD,7));
                    insnNodes.add(new VarInsnNode(Opcodes.ALOAD,8));
                    insnNodes.add(new MethodInsnNode(Opcodes.INVOKESTATIC, ownerClient,"onRenderItem","(Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/item/ItemDisplayContext;ZLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;IILnet/minecraft/client/resources/model/BakedModel;)Z",false));
                    insnNodes.add(new JumpInsnNode(Opcodes.IFEQ,label1));

                    insnNodes.add(new InsnNode(Opcodes.RETURN));

                    insnNodes.add(label1);
                    insnNodes.add(new FrameNode(Opcodes.F_SAME, 0, null, 0, null));
                    method.instructions.insert(insnNodes);
                    flag = true;
                }
            }
        } else if ("net/minecraft/world/item/ItemStack".equals(classNode.name)) {
            for (MethodNode method : classNode.methods) {
                if ((method.name.equals("m_220157_") || method.name.equals("hurt")) && method.desc.equals("(ILnet/minecraft/util/RandomSource;Lnet/minecraft/server/level/ServerPlayer;)Z")) {
                    InsnList insnNodes = new InsnList();
                    LabelNode label1 = new LabelNode();


                    insnNodes.add(new VarInsnNode(Opcodes.ALOAD,3));
                    insnNodes.add(new MethodInsnNode(Opcodes.INVOKESTATIC, owner,"canHurt","(Lnet/minecraft/server/level/ServerPlayer;)Z",false));
                    insnNodes.add(new JumpInsnNode(Opcodes.IFNE,label1));

                    insnNodes.add(new InsnNode(Opcodes.ICONST_0));
                    insnNodes.add(new InsnNode(Opcodes.IRETURN));

                    insnNodes.add(label1);
                    insnNodes.add(new FrameNode(Opcodes.F_SAME, 0, null, 0, null));
                    method.instructions.insert(insnNodes);
                    flag = true;
                }
            }
        }
//        else if ("net/minecraft/client/renderer/entity/layers/ItemInHandLayer".equals(classNode.name)) {
//            for (MethodNode method : classNode.methods) {
//                if ((method.name.equals("m_117184_") || method.name.equals("renderArmWithItem")) && method.desc.equals("(Lnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/item/ItemDisplayContext;Lnet/minecraft/world/entity/HumanoidArm;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;I)V")) {
//                    for (AbstractInsnNode instruction : method.instructions) {
//                        if (instruction instanceof MethodInsnNode methodInsnNode) {
//                            if (methodInsnNode.getOpcode() == 185 && methodInsnNode.owner.equals("net/minecraft/client/model/ArmedModel") && (methodInsnNode.name.equals("m_6002_") || methodInsnNode.name.equals("translateToHand")) && methodInsnNode.desc.equals("(Lnet/minecraft/world/entity/HumanoidArm;Lcom/mojang/blaze3d/vertex/PoseStack;)V")) {
//                                InsnList insnNodes8 = new InsnList();
//                                LabelNode labelNode = new LabelNode();
//                                insnNodes8.add(new VarInsnNode(25, 1));
//                                insnNodes8.add(new VarInsnNode(25, 2));
//                                insnNodes8.add(new VarInsnNode(25, 4));
//                                insnNodes8.add(new MethodInsnNode(184, owner, "shouldRenderHeldItemBlocking", "(Lnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/entity/HumanoidArm;)Z"));
//                                insnNodes8.add(new JumpInsnNode(153, labelNode));
//                                insnNodes8.add(new VarInsnNode(25, 1));
//                                insnNodes8.add(new VarInsnNode(25, 2));
//                                insnNodes8.add(new VarInsnNode(25, 3));
//                                insnNodes8.add(new VarInsnNode(25, 4));
//                                insnNodes8.add(new VarInsnNode(25, 5));
//                                insnNodes8.add(new VarInsnNode(25, 6));
//                                insnNodes8.add(new VarInsnNode(21, 7));
//                                insnNodes8.add(new VarInsnNode(25, 0));
//                                insnNodes8.add(new FieldInsnNode(180, classNode.name, isDebug ? "itemInHandRenderer" : "f_234844_", "Lnet/minecraft/client/renderer/ItemInHandRenderer;"));
//                                insnNodes8.add(new MethodInsnNode(184, owner, "renderArmWithItem", "(Lnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/item/ItemDisplayContext;Lnet/minecraft/world/entity/HumanoidArm;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;ILnet/minecraft/client/renderer/ItemInHandRenderer;)V"));
//                                insnNodes8.add(new InsnNode(177));
//                                insnNodes8.add(labelNode);
//                                insnNodes8.add(new FrameNode(3, 0,null, 0,null));
//                                method.instructions.insert(methodInsnNode, insnNodes8);
//                                flag = true;
//                                System.err.println("add block poseStack to layer");
//                                break;
//                            }
//                        }
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

    public static class SuperMatch {
        public Set<String> strings = new HashSet<>();
        private final Predicate<ClassNode> predicate;

        public SuperMatch(String owner, Predicate<ClassNode> predicate) {
            this.predicate = predicate;
            strings.add(owner);
        }

        public boolean match(ClassNode classNode) {
           if (strings.contains(classNode.superName)) {
               strings.add(classNode.name);
               return predicate.test(classNode);
           }
           return false;
        }
    }

    /**
     * 将 ClassNode 保存到 D:\test\<类简单名>.class
     * @param classNode 已构建或修改好的 ClassNode
     */
    public static void saveClassNode(ClassNode classNode) {
        // 1. 将 ClassNode 转换为字节数组
        ClassWriter cw = new ClassWriter(0);
        classNode.accept(cw);
        byte[] bytecode = cw.toByteArray();

        // 2. 从内部名称中提取简单类名 (例如 "com/example/MyClass" -> "MyClass")
        String internalName = classNode.name;
        String simpleClassName = internalName.substring(internalName.lastIndexOf('/') + 1);

        // 3. 确保目录存在
        File dir = new File("D:\\test");
        if (!dir.exists()) {
            dir.mkdirs();
        }

        // 4. 写入文件
        File classFile = new File(dir, simpleClassName + ".class");
        try (FileOutputStream fos = new FileOutputStream(classFile)) {
            fos.write(bytecode);
        } catch (IOException ignore) {}

        System.out.println("类已保存到: " + classFile.getAbsolutePath());
    }

}
