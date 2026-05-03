package sen.manaita_plus_legacy_core.transform;

import cpw.mods.modlauncher.serviceapi.ILaunchPluginService;
import net.minecraft.client.renderer.entity.layers.ItemInHandLayer;
import net.minecraftforge.fml.loading.FMLEnvironment;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.*;

import java.lang.reflect.Method;
import java.util.EnumSet;



public class ManaitaPlusLegacyLaunchPluginService implements ILaunchPluginService {
    public static final ManaitaPlusLegacyLaunchPluginService instance = new ManaitaPlusLegacyLaunchPluginService();
    public static final boolean isDebug = !FMLEnvironment.production;
    public static final Logger LOGGER = LogManager.getLogger("ManaitaPlusCore");
    private static final String owner = "sen/manaita_plus_legacy_core/util/EventUtil";
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
        /*if ("net/minecraft/client/gui/screens/inventory/tooltip/TooltipRenderUtil".equals(classNode.name)) {
            for (MethodNode method : classNode.methods) {
                if ((method.name.equals("m_280153_") || method.name.equals("renderTooltipBackground")) && method.desc.equals("(Lnet/minecraft/client/gui/GuiGraphics;IIIIIIIII)V")) {
                    InsnList insnNodes = new InsnList();
                    insnNodes.add(new VarInsnNode(Opcodes.ALOAD,0));
                    insnNodes.add(new VarInsnNode(Opcodes.ILOAD,1));
                    insnNodes.add(new VarInsnNode(Opcodes.ILOAD,2));
                    insnNodes.add(new VarInsnNode(Opcodes.ILOAD,3));
                    insnNodes.add(new VarInsnNode(Opcodes.ILOAD,4));
                    insnNodes.add(new VarInsnNode(Opcodes.ILOAD,5));
                    insnNodes.add(new VarInsnNode(Opcodes.ILOAD,6));
                    insnNodes.add(new VarInsnNode(Opcodes.ILOAD,7));
                    insnNodes.add(new VarInsnNode(Opcodes.ILOAD,8));
                    insnNodes.add(new VarInsnNode(Opcodes.ILOAD,9));
                    insnNodes.add(new MethodInsnNode(Opcodes.INVOKESTATIC,"sen/manaita_plus_legacy_core/util/TooltipUtil","renderTooltipBackground","(Lnet/minecraft/client/gui/GuiGraphics;IIIIIIIII)V",false));
                    insnNodes.add(new InsnNode(Opcodes.RETURN));
                    method.instructions.clear();
                    method.instructions.add(insnNodes);
                    flag = true;
                }
            }
        } else*/ if ("net/minecraft/client/renderer/GameRenderer".equals(classNode.name)) {
            for (MethodNode method : classNode.methods) {
                if ((method.name.equals("bobHurt") || method.name.equals("m_109117_")) && method.desc.equals("(Lcom/mojang/blaze3d/vertex/PoseStack;F)V")) {
                    InsnList insnNodes = new InsnList();
                    LabelNode label1 = new LabelNode();
                    LabelNode label2 = new LabelNode();

                    insnNodes.add(new VarInsnNode(Opcodes.ALOAD, 0));
                    insnNodes.add(new FieldInsnNode(Opcodes.GETFIELD,"net/minecraft/client/renderer/GameRenderer",isDebug ? "minecraft" : "f_109059_","Lnet/minecraft/client/Minecraft;"));
                    insnNodes.add(new FieldInsnNode(Opcodes.GETFIELD,"net/minecraft/client/Minecraft",isDebug ? "player" : "f_91074_","Lnet/minecraft/client/player/LocalPlayer;"));
                    insnNodes.add(new MethodInsnNode(Opcodes.INVOKESTATIC,owner,"isManaita","(Lnet/minecraft/world/entity/Entity;)Z",false));
                    insnNodes.add(new JumpInsnNode(Opcodes.IFEQ,label1));
                    insnNodes.add(new InsnNode(Opcodes.RETURN));
                    insnNodes.add(label1);
                    insnNodes.add(new FrameNode(Opcodes.F_SAME, 0, null, 0, null));

                    insnNodes.add(new VarInsnNode(Opcodes.ALOAD, 0));
                    insnNodes.add(new FieldInsnNode(Opcodes.GETFIELD,"net/minecraft/client/renderer/GameRenderer",isDebug ? "minecraft" : "f_109059_","Lnet/minecraft/client/Minecraft;"));
                    insnNodes.add(new FieldInsnNode(Opcodes.GETFIELD,"net/minecraft/client/Minecraft",isDebug ? "player" : "f_91074_","Lnet/minecraft/client/player/LocalPlayer;"));
                    insnNodes.add(new MethodInsnNode(Opcodes.INVOKESTATIC,owner,"isDead","(Lnet/minecraft/world/entity/Entity;)Z",false));
                    insnNodes.add(new JumpInsnNode(Opcodes.IFEQ,label2));

                    insnNodes.add(new VarInsnNode(Opcodes.ALOAD, 0));
                    insnNodes.add(new FieldInsnNode(Opcodes.GETFIELD,"net/minecraft/client/renderer/GameRenderer",isDebug ? "minecraft" : "f_109059_","Lnet/minecraft/client/Minecraft;"));
                    insnNodes.add(new VarInsnNode(Opcodes.ALOAD, 1));
                    insnNodes.add(new VarInsnNode(Opcodes.FLOAD, 2));
                    insnNodes.add(new MethodInsnNode(Opcodes.INVOKESTATIC,owner,"bobHurt","(Lnet/minecraft/client/Minecraft;Lcom/mojang/blaze3d/vertex/PoseStack;F)V",false));
                    insnNodes.add(new InsnNode(Opcodes.RETURN));

                    insnNodes.add(label2);
                    insnNodes.add(new FrameNode(Opcodes.F_SAME, 0, null, 0, null));

                    method.instructions.insert(insnNodes);
                }
            }
        } else if ("net/minecraft/client/Minecraft".equals(classNode.name)) {
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
                    flag = true;
                } else if ((method.name.equals("runTick") || method.name.equals("m_91383_")) && method.desc.equals("(Z)V")) {
                    InsnList insnNodes = new InsnList();
                    insnNodes.add(new VarInsnNode(Opcodes.ALOAD,0));
                    insnNodes.add(new MethodInsnNode(Opcodes.INVOKESTATIC,owner,"runTickBefore","(Lnet/minecraft/client/Minecraft;)V"));
                    method.instructions.insert(insnNodes);
                    flag = true;
                }
            }
        } else if ("net/minecraft/client/renderer/entity/LivingEntityRenderer".equals(classNode.name)) {
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
                    insnNodes.add(new MethodInsnNode(Opcodes.INVOKESTATIC,owner,"setupRotationsM","(Lnet/minecraft/world/entity/LivingEntity;Lcom/mojang/blaze3d/vertex/PoseStack;FF)V",false));
                    insnNodes.add(new InsnNode(Opcodes.RETURN));

                    insnNodes.add(label1);
                    insnNodes.add(new FrameNode(Opcodes.F_SAME, 0, null, 0, null));

                    insnNodes.add(new VarInsnNode(Opcodes.ALOAD, 1));
                    insnNodes.add(new MethodInsnNode(Opcodes.INVOKESTATIC,owner,"isDead","(Lnet/minecraft/world/entity/Entity;)Z",false));
                    insnNodes.add(new JumpInsnNode(Opcodes.IFEQ,label2));

                    insnNodes.add(new VarInsnNode(Opcodes.ALOAD,1));
                    insnNodes.add(new VarInsnNode(Opcodes.ALOAD,2));
                    insnNodes.add(new VarInsnNode(Opcodes.FLOAD,5));
                    insnNodes.add(new MethodInsnNode(Opcodes.INVOKESTATIC,owner,"setupRotationsD","(Lnet/minecraft/world/entity/LivingEntity;Lcom/mojang/blaze3d/vertex/PoseStack;F)V",false));
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
            for (MethodNode method : classNode.methods) {
                if (method.name.equals("blockBroken") && method.desc.equals("(Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/server/level/ServerPlayer;Ldev/architectury/utils/value/IntValue;)Ldev/architectury/event/EventResult;")) {
                    String owner1 = "sen/manaita_plus_legacy_core/util/plugin/FTBUltimineUtil";
                    InsnList insnNodes = new InsnList();
                    LabelNode label1 = new LabelNode();

                    insnNodes.add(new VarInsnNode(Opcodes.ALOAD,4));
                    insnNodes.add(new MethodInsnNode(Opcodes.INVOKESTATIC, owner1,"canUltimine","(Lnet/minecraft/server/level/ServerPlayer;)Z",false));
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
                    break;
                }
            }
            LOGGER.info("Found FTBUltimine");
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


}
