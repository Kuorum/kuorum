package kuorum.core.navigation.cache

import javax.servlet.ServletResponse

/**
 * Holds the servlet response in a byte array so that it can be held in the
 * cache (and, since this class is serializable, optionally persisted to disk).
 *
 * @version $Revision: 362 $
 * @author <a href="mailto:sergek@lokitech.com">Serge Knystautas</a>
 */
class ResponseContent implements Serializable {

    private static final long serialVersionUID = 3815371067383546908L

    private transient ByteArrayOutputStream bout = new ByteArrayOutputStream(1000)
    private byte[] content = null

    /**
     * Get an output stream. This is used by the
     * {@link kuorum.core.customDomain.filter.SplitServletOutputStream} to capture the original (uncached)
     * response into a byte array.
     *
     * @return the original (uncached) response, returns null if response is
     *         already committed.
     */
    OutputStream getOutputStream() {
        return bout
    }

    /**
     * Gets the size of this cached content.
     *
     * @return The size of the content, in bytes. If no content exists, this
     *         method returns <code>-1</code>.
     */
    int getSize() {
        return (content != null) ? content.length : (-1)
    }

    /**
     * Called once the response has been written in its entirety. This method
     * commits the response output stream by converting the output stream into a
     * byte array.
     */
    void commit() {
        if (bout != null) {
            content = bout.toByteArray()
            bout = null
        }
    }

    /**
     * Writes this cached data out to the supplied <code>ServletResponse</code>.
     *
     * @param response The servlet response to output the cached content to.
     * @throws IOException
     */
    void writeTo(ServletResponse response) throws IOException {
        OutputStream out = new BufferedOutputStream(response.getOutputStream())

        response.setContentLength(content.length)
        out.write(content)
        out.flush()
    }

    byte[] getContent(){
        return content
    }
}