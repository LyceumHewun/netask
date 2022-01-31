package cc.lyceum.netask.util;

import java.util.UUID;

/**
 * @author Lyceum
 */
public class KeyUtils {

    public static UUID uuidV4() {
        return UUID.randomUUID();
    }

    public static String uuidV4Str() {
        return uuidV4().toString();
    }

    public static String uuidV4NoDashStr() {
        return uuidV4Str().replaceAll("-", "");
    }
}
