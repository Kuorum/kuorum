package kuorum

/**
 * Converts an object to a query  params on a URL.
 *
 * Returns a Map<String,String>
 */
class AmazonS3KeyCodec {
    static encode = { def target ->
        def s3Key = target.trim()
        if (s3Key.startsWith("http")) {
            try {
                s3Key = (java.net.URI.create(s3Key)).getPath()
            } catch (Exception e) {
                return s3Key; // If error it returns as is
            }
        }
        s3Key.replace(/(\/)+/, "/")

        if (s3Key.startsWith("/")) {
            return s3Key.subSequence(1, s3Key.length())
        } else {
            return s3Key;
        }
    }
    static decode = { target ->
        //TODO
        return null;
    }

}
