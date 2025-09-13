package sen.manaita_plus_legacy.common.util;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;

import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.WeakHashMap;

public enum ManaitaPlusEntityList {
    manaita("manaita"),
    death("death"),
    remove("remove");


    ManaitaPlusEntityList(String name) {
        if (name.isEmpty()) name = "Null" + name();
        this.name = name;
    }

    private final Map<Entity, Object> entities = new WeakHashMap<>();
    private final Map<UUID, Object> uuids = new WeakHashMap<>();

    private final String name;

    public void add(Entity entity) {
        if (entity == null) return;
        entity.addTag(name);

        uuids.put(entity.getUUID(),Boolean.TRUE);
        if (entity instanceof Player) return;
        entities.put(entity,Boolean.TRUE);
    }

    public void remove(Entity entity) {
        if (entity == null) return;
        entity.removeTag(name);

        entities.remove(entity);
        uuids.remove(entity.getUUID());
    }


    public boolean accept(Entity entity) {
        if (entity == null) return false;
        return entities.containsKey(entity) || uuids.containsKey(entity.getUUID()) || entity.getTags().contains(name);
    }

    public Set<Entity> getEntities() {
        return entities.keySet();
    }
}
