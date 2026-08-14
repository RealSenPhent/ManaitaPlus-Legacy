package sen.manaita_plus_legacy.client.util;

import sen.manaita_plus_legacy.client.event.EventHandler;

public class TimeUtil {
    public static float getRenderTime() {
        return EventHandler.renderTime + EventHandler.renderFrame;
    }
}
