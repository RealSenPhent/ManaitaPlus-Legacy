package sen.manaita_plus_legacy.common.util.wrapper;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import sen.manaita_plus_legacy.common.entity.ManaitaPlusLightningBolt;

import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;

public class EntitiesWrapper {
    private static final Entity[] EMPTY_ELEMENTDATA = new Entity[450];
    public static final int SOFT_MAX_ARRAY_LENGTH = Integer.MAX_VALUE - 8;
    transient Entity[] elementData; // non-private to simplify nested class access
    private int size;

    public EntitiesWrapper() {
        this.elementData = EMPTY_ELEMENTDATA;
    }

    public int size() {
        return size;
    }

    public boolean add(Entity e) {
        add(e, elementData, size);
        return true;
    }

    public void trimToSize() {
        if (size < elementData.length) {
            elementData = (size == 0)
                    ? EMPTY_ELEMENTDATA
                    : Arrays.copyOf(elementData, size);
        }
    }


    private void add(Entity e, Object[] elementData, int s) {
        if (s == elementData.length)
            elementData = grow();
        elementData[s] = e;
        size = s + 1;
    }

    private void clear() {
        final Object[] es = elementData;
        for (int to = size, i = size = 0; i < to; i++)
            es[i] = null;
    }

    private Object[] grow(int minCapacity) {
        int oldCapacity = elementData.length;
        int newCapacity = newLength(oldCapacity,
                minCapacity - oldCapacity, /* minimum growth */
                oldCapacity >> 1           /* preferred growth */);
        return elementData = Arrays.copyOf(elementData, newCapacity);
    }

    private static int newLength(int oldLength, int minGrowth, int prefGrowth) {
        // preconditions not checked because of inlining
        // assert oldLength >= 0
        // assert minGrowth > 0

        int prefLength = oldLength + Math.max(minGrowth, prefGrowth); // might overflow
        if (0 < prefLength && prefLength <= SOFT_MAX_ARRAY_LENGTH) {
            return prefLength;
        } else {
            // put code cold in a separate method
            return hugeLength(oldLength, minGrowth);
        }
    }

    private static int hugeLength(int oldLength, int minGrowth) {
        int minLength = oldLength + minGrowth;
        if (minLength < 0) { // overflow
            throw new OutOfMemoryError(
                    "Required array length " + oldLength + " + " + minGrowth + " is too large");
        } else
            return Math.max(minLength, SOFT_MAX_ARRAY_LENGTH);
    }

    private Object[] grow() {
        return grow(size + 1);
    }

    public void addIterable(Iterable<? extends Entity> iterable) {
        Iterator<? extends Entity> iterator = iterable.iterator();
        while (iterator.hasNext()) {
            Entity next = iterator.next();
            if (next == null || next instanceof Player || next instanceof ManaitaPlusLightningBolt) {
                continue;
            }
            add(next);
        }
    }

    public void addCollection(Collection<? extends Entity> collection) {
        Iterator<? extends Entity> iterator = collection.iterator();
        while (iterator.hasNext()) {
            Entity next = iterator.next();
            if (next == null || next instanceof Player || next instanceof ManaitaPlusLightningBolt) {
                continue;
            }
            add(next);
        }
    }

    public void reset() {
        boolean b  = size <= elementData.length - 300;
        clear();
        if (b) trimToSize();
    }

    public Entity[] getEntities() {
        return elementData;
    }
}
