package kuorum.core.customDomain.filter

import javax.servlet.ServletResponse
import javax.servlet.http.HttpServletResponse
import java.util.zip.GZIPInputStream
/**
 * Holds the servlet response in a byte array so that it can be held in the
 * cache (and, since this class is serializable, optionally persisted to disk).
 *
 * @version $Revision: 362 $
 * @author <a href="mailto:sergek@lokitech.com">Serge Knystautas</a>
 */
public class ResponseContent implements Serializable {
    private static final long serialVersionUID = 1L;
    private transient ByteArrayOutputStream bout = new ByteArrayOutputStream(1000);
    private Locale locale = null;
    private String contentEncoding = null;
    private String contentType = null;
    private byte[] content = null;
    private long expires = Long.MAX_VALUE;
    private long lastModified = -1;
    private long maxAge = -60;

    public String getContentType() {
        return contentType;
    }

    /**
     * Set the content type. We capture this so that when we serve this data
     * from cache, we can set the correct content type on the response.
     */
    public void setContentType(String value) {
        contentType = value;
    }

    public long getLastModified() {
        return lastModified;
    }

    public void setLastModified(long value) {
        lastModified = value;
    }

    public String getContentEncoding() {
        return contentEncoding;
    }

    public void setContentEncoding(String contentEncoding) {
        this.contentEncoding = contentEncoding;
    }

    /**
     * Set the Locale. We capture this so that when we serve this data from
     * cache, we can set the correct locale on the response.
     */
    public void setLocale(Locale value) {
        locale = value;
    }

    /**
     * @return the expires date and time in miliseconds when the content will be
     *         stale
     */
    public long getExpires() {
        return expires;
    }

    /**
     * Sets the expires date and time in miliseconds.
     *
     * @param value time in miliseconds when the content will expire
     */
    public void setExpires(long value) {
        expires = value;
    }

    /**
     * Returns the max age of the content in miliseconds. If expires header and
     * cache control are enabled both, both will be equal.
     *
     * @return the max age of the content in miliseconds, if -1 max-age is
     *         disabled
     */
    public long getMaxAge() {
        return maxAge;
    }

    /**
     * Sets the max age date and time in miliseconds. If the parameter is -1,
     * the max-age parameter won't be set by default in the Cache-Control
     * header.
     *
     * @param value sets the intial
     */
    public void setMaxAge(long value) {
        maxAge = value;
    }

    /**
     * Get an output stream. This is used by the
     * {@link SplitServletOutputStream} to capture the original (uncached)
     * response into a byte array.
     *
     * @return the original (uncached) response, returns null if response is
     *         already committed.
     */
    public OutputStream getOutputStream() {
        return bout;
    }

    /**
     * Gets the size of this cached content.
     *
     * @return The size of the content, in bytes. If no content exists, this
     *         method returns <code>-1</code>.
     */
    public int getSize() {
        return (content != null) ? content.length : (-1);
    }

    /**
     * Called once the response has been written in its entirety. This method
     * commits the response output stream by converting the output stream into a
     * byte array.
     */
    public void commit() {
        if (bout != null) {
            content = bout.toByteArray();
            bout = null;
        }
    }

    /**
     * Writes this cached data out to the supplied <code>ServletResponse</code>.
     *
     * @param response The servlet response to output the cached content to.
     * @throws IOException
     */
    public void writeTo(ServletResponse response) throws IOException {
        writeTo(response, false, false);
    }

    /**
     * Writes this cached data out to the supplied <code>ServletResponse</code>.
     *
     * @param response The servlet response to output the cached content to.
     * @param fragment is true if this content a fragment or part of a page
     * @param acceptsGZip is true if client browser supports gzip compression
     * @throws IOException
     */
    public void writeTo(ServletResponse response, boolean fragment, boolean acceptsGZip) throws IOException {
        // Send the content type and data to this response
        if (contentType != null) {
            response.setContentType(contentType);
        }

        if (fragment) {
            // Don't support gzip compression if the content is a fragment of a
            // page
            acceptsGZip = false;
        } else {
            // add special headers for a complete page
            if (response instanceof HttpServletResponse) {
                HttpServletResponse httpResponse = (HttpServletResponse) response;

                // add the last modified header
                if (lastModified != -1) {
                    httpResponse.setDateHeader(CacheResponseSpringFilter.HEADER_LAST_MODIFIED, lastModified);
                }

                // add the expires header
                if (expires != Long.MAX_VALUE) {
                    httpResponse.setDateHeader(CacheResponseSpringFilter.HEADER_EXPIRES, expires);
                }

                // add the cache-control header for max-age
                if (maxAge == CacheResponseSpringFilter.MAX_AGE_TIME) {
                    // do nothing
                } else if (maxAge > 0) { // set max-age based on life time
                    long currentMaxAge = maxAge / 1000 - System.currentTimeMillis() / 1000;
                    if (currentMaxAge < 0) {
                        currentMaxAge = 0;
                    }
                    httpResponse.addHeader(CacheResponseSpringFilter.HEADER_CACHE_CONTROL, "max-age=" + currentMaxAge);
                } else {
                    httpResponse.addHeader(CacheResponseSpringFilter.HEADER_CACHE_CONTROL, "max-age=" + (-maxAge));
                }

            }
        }

        if (locale != null) {
            response.setLocale(locale);
        }

        OutputStream out = new BufferedOutputStream(response.getOutputStream());

        if (isContentGZiped()) {
            if (acceptsGZip) {
                ((HttpServletResponse) response).addHeader(CacheResponseSpringFilter.HEADER_CONTENT_ENCODING, "gzip");
                response.setContentLength(content.length);
                out.write(content);
            } else {
                // client doesn't support, so we have to uncompress it
                ByteArrayInputStream bais = new ByteArrayInputStream(content);
                GZIPInputStream zis = new GZIPInputStream(bais);

                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                int numBytesRead = 0;
                byte[] tempBytes = new byte[4196];

                while ((numBytesRead = zis.read(tempBytes, 0, tempBytes.length)) != -1) {
                    baos.write(tempBytes, 0, numBytesRead);
                }

                byte[] result = baos.toByteArray();

                response.setContentLength(result.length);
                out.write(result);
            }
        } else {
            // the content isn't compressed
            // regardless if the client browser supports gzip we will just
            // return the content
            response.setContentLength(content.length);
            out.write(content);
        }
        out.flush();
    }

    /**
     * @return true if the content is GZIP compressed
     */
    public boolean isContentGZiped() {
        return "gzip".equals(contentEncoding);
    }
}