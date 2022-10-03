package kuorum.core.navigation.cache

import kuorum.core.customDomain.filter.SplitServletOutputStream

import javax.servlet.ServletOutputStream
import javax.servlet.http.HttpServletResponse
import javax.servlet.http.HttpServletResponseWrapper

/**
 * HttpServletResponseWrapper en caché, que registrará algunos datos de {@link HttpServletResponse} en {@link ResponseContent}.
 */
class CacheHttpServletResponseWrapper extends HttpServletResponseWrapper {

    private PrintWriter cachedWriter = null;
    private ResponseContent result = null;
    private SplitServletOutputStream cacheOut = null;


    CacheHttpServletResponseWrapper(HttpServletResponse response) {
        super(response);
        this.result = new ResponseContent();
    }

    /**
     * Get a response content
     *
     * @return The content
     */
    ResponseContent getContent() {
        // Flush the buffer
        try {
            flush();
        } catch (IOException ignore) {
        }
        // Create the byte array
        result.commit();
        // Return the result from this response
        return result;
    }

    /**
     * Get an output stream
     *
     * @throws IOException
     */
    ServletOutputStream getOutputStream() throws IOException {
        // Pass this faked servlet output stream that captures what is sent
        if (cacheOut == null) {
            cacheOut = new SplitServletOutputStream(result.getOutputStream(), super.getOutputStream());
        }
        return cacheOut;
    }

    /**
     * Get a print writer
     *
     * @throws IOException
     */
    PrintWriter getWriter() throws IOException {
        if (cachedWriter == null) {
            String encoding = getCharacterEncoding();
            if (encoding != null) {
                cachedWriter = new PrintWriter(new OutputStreamWriter(getOutputStream(), encoding));
            } else { // using the default character encoding
                cachedWriter = new PrintWriter(new OutputStreamWriter(getOutputStream()));
            }
        }

        return cachedWriter;
    }

    /**
     * Flushes all streams.
     *
     * @throws IOException
     */
    private void flush() throws IOException {
        if (cacheOut != null) {
            cacheOut.flush();
        }

        if (cachedWriter != null) {
            cachedWriter.flush();
        }
    }

    void flushBuffer() throws IOException {
        super.flushBuffer();
        flush();
    }
}
