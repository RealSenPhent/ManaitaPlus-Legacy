package sen.manaita_plus_legacy.common.savedata;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.saveddata.SavedData;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

public class GodSwordData extends SavedData {
    private int size;
    private HashSet<String> entity;

    // 创建新世界数据时调用的工厂方法
    public static GodSwordData create() {
        return new GodSwordData();
    }

    // 从NBT中加载世界数据时调用的工厂方法
    public static GodSwordData load(CompoundTag nbt) {
        GodSwordData data = new GodSwordData();
        int anInt = nbt.getInt("size");
        for (int i = 0; i < anInt; i++) {
            String string = nbt.getString("entityClass" + i);
            data.entity.add(string);
        }
        return data;
    }

    public HashSet<String> getEntity() {
        return entity;
    }

    // 修改数据的方法
    public void setEntityType(Collection<Entity> entityType) {
        entityType.clear();
        for (Entity entity1 : entityType) {
            String name = entity1.getClass().getName();
            entity.add(name);
        }
        this.setDirty(true);
    }

    public void addEntityType(Collection<Entity> entityType) {
        for (Entity entity1 : entityType) {
            String name = entity1.getClass().getName();
            entity.add(name);
        }
        this.setDirty(true);
    }

    @Override
    public CompoundTag save(CompoundTag p_77763_) {
        p_77763_.putInt("size", size);
        int i = 0;
        for (String string : entity) {
            p_77763_.putString("entityClass" + i, string);
            ++i;
        }
        return p_77763_;
    }
}
