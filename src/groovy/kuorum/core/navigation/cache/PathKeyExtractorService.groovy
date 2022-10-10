package kuorum.core.navigation.cache

import com.google.common.collect.Sets

class PathKeyExtractorService {

    private static final int TOKENS_TO_LOCATE = 2

    private static final Set TOKENS_TO_IGNORE = Sets.newHashSet("","account","cta","pb")

    String extractKey(String path) {
        StringBuilder stringBuilder = new StringBuilder()
        int appendCount = 0
        String[] splited = path.split("/")
        for (int i = 0; i < splited.length && appendCount < TOKENS_TO_LOCATE; i++) {
            String token = splited[i]
            if (!TOKENS_TO_IGNORE.contains(token)) {
                appendCount++
                stringBuilder.append(token)
            }
        }
        return stringBuilder.toString()
    }
}
