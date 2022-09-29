package kuorum.core.customDomain.filter

import org.apache.commons.logging.Log
import org.apache.commons.logging.LogFactory

import javax.servlet.ServletOutputStream
import javax.servlet.http.HttpServletResponse
import javax.servlet.http.HttpServletResponseWrapper
/**
 * HttpServletResponseWrapper en caché, que registrará algunos datos de {@link HttpServletResponse} en {@link ResponseContent}.
 */
public class CacheHttpServletResponseWrapper extends HttpServletResponseWrapper {
    private final Log log = LogFactory.getLog(this.getClass());

    private PrintWriter cachedWriter = null;
    private ResponseContent result = null;
    private SplitServletOutputStream cacheOut = null;
    private int status = SC_OK;
    private long cacheControl = -60;

    public CacheHttpServletResponseWrapper(HttpServletResponse response) {
        this(response, Long.MAX_VALUE, CacheResponseSpringFilter.EXPIRES_ON, CacheResponseSpringFilter.LAST_MODIFIED_INITIAL, -60);
    }


    public CacheHttpServletResponseWrapper(HttpServletResponse response, long time, long lastModified, long expires,
                                           long cacheControl) {
        super(response);
        this.result = new ResponseContent();
        this.cacheControl = cacheControl;

        // setting a default last modified value based on object creation and
        // remove the millis
        if (lastModified == CacheResponseSpringFilter.LAST_MODIFIED_INITIAL) {
            long current = System.currentTimeMillis();
            current = current - (current % 1000);
            result.setLastModified(current);
            super.setDateHeader(CacheResponseSpringFilter.HEADER_LAST_MODIFIED, result.getLastModified());
        }
        // setting the expires value
        if (expires == CacheResponseSpringFilter.EXPIRES_ON) {
            result.setExpires(result.getLastModified() + time);
            super.setDateHeader(CacheResponseSpringFilter.HEADER_EXPIRES, result.getExpires());
        }
        // setting the cache control with max-age
        if (this.cacheControl == CacheResponseSpringFilter.MAX_AGE_TIME) {
            // set the count down
            long maxAge = System.currentTimeMillis();
            maxAge = maxAge - (maxAge % 1000) + time;
            result.setMaxAge(maxAge);
            super.addHeader(CacheResponseSpringFilter.HEADER_CACHE_CONTROL, "max-age=" + time / 1000);
        } else if (this.cacheControl != CacheResponseSpringFilter.MAX_AGE_TIME) {
            result.setMaxAge(this.cacheControl);
            super.addHeader(CacheResponseSpringFilter.HEADER_CACHE_CONTROL, "max-age=" + (-this.cacheControl));
        } else if (this.cacheControl == CacheResponseSpringFilter.MAX_AGE_TIME) {
            result.setMaxAge(this.cacheControl);
        }
    }
    /**
     * Get a response content
     *
     * @return The content
     */
    public ResponseContent getContent() {
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
     * Set the content type
     *
     * @param value The content type
     */
    public void setContentType(String value) {
        if (log.isDebugEnabled()) {
            log.debug("ContentType: " + value);
        }
        super.setContentType(value);
        result.setContentType(value);
    }

    /**
     * Set a header field
     *
     * @param name The header name
     * @param value The header value
     */
    public void setHeader(String name, String value) {
        if (log.isDebugEnabled()) {
            log.debug("header: " + name + ": " + value);
        }
        if (CacheResponseSpringFilter.HEADER_CONTENT_TYPE.equalsIgnoreCase(name)) {
            result.setContentType(value);
        }

        if (CacheResponseSpringFilter.HEADER_CONTENT_ENCODING.equalsIgnoreCase(name)) {
            result.setContentEncoding(value);
        }
        super.setHeader(name, value);
    }

    /**
     * Add a header field
     *
     * @param name The header name
     * @param value The header value
     */
    public void addHeader(String name, String value) {
        if (log.isDebugEnabled()) {
            log.debug("header: " + name + ": " + value);
        }

        if (CacheResponseSpringFilter.HEADER_CONTENT_TYPE.equalsIgnoreCase(name)) {
            result.setContentType(value);
        }

        if (CacheResponseSpringFilter.HEADER_CONTENT_ENCODING.equalsIgnoreCase(name)) {
            result.setContentEncoding(value);
        }

        super.addHeader(name, value);
    }

    /**
     * We override this so we can catch the response status. Only responses with
     * a status of 200 (<code>SC_OK</code>) will be cached.
     */
    public void setStatus(int status) {
        super.setStatus(status);
        this.status = status;
    }

    /**
     * We override this so we can catch the response status. Only responses with
     * a status of 200 (<code>SC_OK</code>) will be cached.
     */
    public void sendError(int status, String string) throws IOException {
        super.sendError(status, string);
        this.status = status;
    }

    /**
     * We override this so we can catch the response status. Only responses with
     * a status of 200 (<code>SC_OK</code>) will be cached.
     */
    public void sendError(int status) throws IOException {
        super.sendError(status);
        this.status = status;
    }

    /**
     * We override this so we can catch the response status. Only responses with
     * a status of 200 (<code>SC_OK</code>) will be cached.
     */
    public void setStatus(int status, String string) {
        super.setStatus(status, string);
        this.status = status;
    }

    /**
     * We override this so we can catch the response status. Only responses with
     * a status of 200 (<code>SC_OK</code>) will be cached.
     */
    public void sendRedirect(String location) throws IOException {
        this.status = SC_MOVED_TEMPORARILY;
        super.sendRedirect(location);
    }

    /**
     * Retrieves the captured HttpResponse status.
     */
    public int getStatus() {
        return status;
    }

    /**
     * Set the locale
     *
     * @param value The locale
     */
    public void setLocale(Locale value) {
        super.setLocale(value);
        result.setLocale(value);
    }

    /**
     * Get an output stream
     *
     * @throws IOException
     */
    public ServletOutputStream getOutputStream() throws IOException {
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
    public PrintWriter getWriter() throws IOException {
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

    public void flushBuffer() throws IOException {
        super.flushBuffer();
        flush();
    }
}
