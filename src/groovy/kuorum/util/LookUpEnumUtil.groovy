package kuorum.util

/**
 * Created by iduetxe on 19/05/14.
 */
class LookUpEnumUtil {

    public static <E extends Enum<E>> E lookup(Class<E> e, String id, def defaultVal) {
        E result = defaultVal
        try {
            if (id)
                result = Enum.valueOf(e, id.trim().toUpperCase());
        } catch (IllegalArgumentException ex) {
        }

        return result;
    }
}
