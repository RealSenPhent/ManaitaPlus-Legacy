package sen.manaita_plus_legacy_core.transform;

import cpw.mods.modlauncher.TransformerHolder;
import cpw.mods.modlauncher.api.ITransformer;
import cpw.mods.modlauncher.api.ITransformerVotingContext;
import cpw.mods.modlauncher.api.TransformerVoteResult;
import net.minecraft.client.renderer.LevelRenderer;
import org.jetbrains.annotations.NotNull;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.*;
import sen.manaita_plus_legacy_core.ManaitaPlusTransformationService;

import java.util.Set;

import static sen.manaita_plus_legacy_core.transform.ManaitaPlusLegacyLaunchPluginService.isDebug;


public class ManaitaPlusLegacyLaunchTransformer implements ITransformer<ClassNode> {
    public static final TransformerHolder instance = new TransformerHolder<>(new ManaitaPlusLegacyLaunchTransformer(), new ManaitaPlusTransformationService());
    private static final String owner = "sen/manaita_plus_legacy_core/util/EventUtil";

    @NotNull
    @Override
    public ClassNode transform(ClassNode classNode, ITransformerVotingContext context) {
        if (classNode.name.startsWith("sen/")) return classNode;
        if ("net/minecraft/client/Minecraft".equals(classNode.name)) {
            System.err.println("TTTTes");
            for (MethodNode method : classNode.methods) {
                if ((method.name.equals("m_91152_") || method.name.equals("setScreen")) && method.desc.equals("(Lnet/minecraft/client/gui/screens/Screen;)V")) {
                    InsnList insnNodes = new InsnList();
                    LabelNode label1 = new LabelNode();
                    insnNodes.add(new VarInsnNode(Opcodes.ALOAD,1));
                    insnNodes.add(new MethodInsnNode(Opcodes.INVOKESTATIC,owner,"isNotSafe","(Lnet/minecraft/client/gui/screens/Screen;)Z",false));
                    insnNodes.add(new JumpInsnNode(Opcodes.IFEQ,label1));
                    insnNodes.add(new InsnNode(Opcodes.RETURN));
                    insnNodes.add(label1);
                    method.instructions.insert(insnNodes);
                } else if ((method.name.equals("runTick") || method.name.equals("m_91383_")) && method.desc.equals("(Z)V")) {
                    InsnList insnNodes = new InsnList();
                    insnNodes.add(new VarInsnNode(Opcodes.ALOAD,0));
                    insnNodes.add(new MethodInsnNode(Opcodes.INVOKESTATIC,owner,"runTickBefore","(Lnet/minecraft/client/Minecraft;)V"));
                    method.instructions.insert(insnNodes);
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
                } else if ((method.name.equals("m_6000_") || method.name.equals("shouldRender")) && method.desc.equals("(DDD)Z")) {
                    InsnList insnNodes = new InsnList();
                    LabelNode label1 = new LabelNode();
                    LabelNode label2 = new LabelNode();
                    insnNodes.add(new VarInsnNode(Opcodes.ALOAD, 0));
                    insnNodes.add(new MethodInsnNode(Opcodes.INVOKESTATIC,owner,"isManaita","(Lnet/minecraft/world/entity/Entity;)Z",false));
                    insnNodes.add(new JumpInsnNode(Opcodes.IFEQ,label1));
                    insnNodes.add(new InsnNode(Opcodes.ICONST_1));
                    insnNodes.add(new InsnNode(Opcodes.IRETURN));
                    insnNodes.add(label1);
                    insnNodes.add(new FrameNode(Opcodes.F_SAME, 0, null, 0, null));
                    insnNodes.add(new VarInsnNode(Opcodes.ALOAD, 0));
                    insnNodes.add(new MethodInsnNode(Opcodes.INVOKESTATIC,owner,"isRemove","(Lnet/minecraft/world/entity/Entity;)Z",false));
                    insnNodes.add(new JumpInsnNode(Opcodes.IFEQ,label2));
                    insnNodes.add(new InsnNode(Opcodes.ICONST_0));
                    insnNodes.add(new InsnNode(Opcodes.IRETURN));
                    insnNodes.add(label2);
                    insnNodes.add(new FrameNode(Opcodes.F_SAME, 0, null, 0, null));
                    method.instructions.insert(insnNodes);
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
                } else if (method.name.equals("m_6469_") || method.name.equals("hurt")) {
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
                }
            }
        }
        return classNode;
    }


    @NotNull
    @Override
    public TransformerVoteResult castVote(ITransformerVotingContext context) {
        return TransformerVoteResult.YES;
    }


    @Override
    public @NotNull Set<Target> targets() {
        return Set.of();
    }
}
