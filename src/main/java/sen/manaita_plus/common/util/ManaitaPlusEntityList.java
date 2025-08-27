package sen.manaita_plus.common.util;

import com.google.common.collect.Sets;
import net.minecraft.world.entity.Entity;

import java.util.Set;
import java.util.UUID;

public enum ManaitaPlusEntityList {
    player("god114514"),
    death("death114514"),
    attack("attacke"),
    remove("remove");


    ManaitaPlusEntityList(String name) {
        if (name.isEmpty()) name = "Null" + name();
        this.name = name;
    }

    private final String name;
    private final Set<Entity> entities = Sets.newHashSet();
    private final Set<UUID> uuids = Sets.newHashSet();


    public void add(Entity entity) {
        if (entity == null) return;
        entity.addTag(name);
        entities.add(entity);
        uuids.add(entity.getUUID());
    }

    public void remove(Entity entity) {
        if (entity == null) return;
        entity.addTag(name);
        entities.remove(entity);
        uuids.remove(entity.getUUID());
    }

    @Override
    public String toString() {
        return "ManaitaEntityList{" +
                "name='" + name + '\'' +
                ", entities=" + entities +
                ", uuids=" + uuids +
                '}';
    }

    public boolean accept(Entity entity) {
        if (entity == null) return false;
        return entities.contains(entity) || uuids.contains(entity.getUUID()) /*|| entity.getTags().contains(name)*/ /*|| (name.equals("god114514") && player.)*/;
    }

//    public boolean accept(Entity entity) {
//
//        if (entity != null)
//            return entities.contains(entity) ||
//                    names.contains(entity.getName().getString()) ||
//                    uuids.contains(entity.getUUID()) || entity.getTags().contains(name) /*|| (name.equals("god114514") && player.)*/;
//        return false;
//    }

}
