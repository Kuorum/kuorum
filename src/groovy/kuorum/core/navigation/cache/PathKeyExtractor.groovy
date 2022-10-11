package kuorum.core.navigation.cache

import com.google.common.collect.Sets

class PathKeyExtractor {

    private static final int TOKENS_TO_LOCATE = 2 //TODO parameter?

    private static final Set TOKENS_TO_IGNORE = Sets.newHashSet("","account","cta","pb","ct","ajax")

    String extractKey(String path) {
        StringBuilder stringBuilder = new StringBuilder()
        int appendCount = 0
        String[] splitted = path.split("/")
        for (int i = 0; i < splitted.length && appendCount < TOKENS_TO_LOCATE; i++) {
            String token = splitted[i]
            if (!TOKENS_TO_IGNORE.contains(token)) {
                appendCount++
                stringBuilder.append(token)
            }
        }
        return stringBuilder.toString()
    }
}
