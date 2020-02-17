package kuorum.util.log

import org.apache.log4j.Layout
import org.apache.log4j.Level
import org.apache.log4j.spi.LocationInfo
import org.apache.log4j.spi.LoggingEvent
import org.apache.log4j.spi.ThrowableInformation
import org.graylog2.GelfMessageFactory
import org.graylog2.GelfMessageProvider
import org.graylog2.log.Log4jVersionChecker

class GelfOVHMessageFactory extends GelfMessageFactory{
    private static final int MAX_SHORT_MESSAGE_LENGTH = 250;
    private static final String ORIGIN_HOST_KEY = "originHost";
    private static final String LOGGER_NAME = "logger";
    private static final String LOGGER_NDC = "loggerNdc";
    private static final String THREAD_NAME = "thread";
    private static final String JAVA_TIMESTAMP = "timestampMs";

    public GelfOVHMessageFactory() {
    }

    public static GelfOVHMessage makeMessage(Layout layout, LoggingEvent event, GelfMessageProvider provider) {
        long timeStamp = Log4jVersionChecker.getTimeStamp(event);
        Level level = event.getLevel();
        String file = null;
        String lineNumber = null;
        if (provider.isIncludeLocation()) {
            LocationInfo locationInformation = event.getLocationInformation();
            file = locationInformation.getFileName();
            lineNumber = locationInformation.getLineNumber();
        }

        String renderedMessage = layout != null ? layout.format(event) : event.getRenderedMessage();
        if (renderedMessage == null) {
            renderedMessage = "";
        }

        if (provider.isExtractStacktrace()) {
            ThrowableInformation throwableInformation = event.getThrowableInformation();
            if (throwableInformation != null) {
                String stackTrace = extractStacktrace(throwableInformation);
                if (stackTrace != null) {
                    renderedMessage = renderedMessage + "\n\r" + extractStacktrace(throwableInformation);
                }
            }
        }

        String shortMessage;
        if (renderedMessage.length() > 250) {
            shortMessage = renderedMessage.substring(0, 249);
        } else {
            shortMessage = renderedMessage;
        }

        GelfOVHMessage gelfMessage = new GelfOVHMessage(shortMessage, renderedMessage, timeStamp, String.valueOf(level.getSyslogEquivalent()), lineNumber, file);
        if (provider.getOriginHost() != null) {
            gelfMessage.setHost(provider.getOriginHost());
        }

        if (provider.getFacility() != null) {
            gelfMessage.setFacility(provider.getFacility());
        }

        Map<String, String> fields = provider.getFields();
        Iterator i = fields.entrySet().iterator();

        while(true) {
            while(i.hasNext()) {
                Map.Entry<String, String> entry = (Map.Entry)i.next();
                if (((String)entry.getKey()).equals("originHost") && gelfMessage.getHost() == null) {
                    gelfMessage.setHost((String)fields.get("originHost"));
                } else {
                    gelfMessage.addField((String)entry.getKey(), (String)entry.getValue());
                }
            }

            if (provider.isAddExtendedInformation()) {
                gelfMessage.addField("thread", event.getThreadName());
                gelfMessage.addField("logger", event.getLoggerName());
                gelfMessage.addField("timestampMs", Long.toString(gelfMessage.getJavaTimestamp()));
                Map<String, Object> mdc = event.getProperties();
                if (mdc != null) {
                    Iterator j = mdc.entrySet().iterator();

                    while(j.hasNext()) {
                        Map.Entry<String, Object> entry = (Map.Entry)j.next();
                        Object value = provider.transformExtendedField((String)entry.getKey(), entry.getValue());
                        gelfMessage.addField((String)entry.getKey(), value);
                    }
                }

                String ndc = event.getNDC();
                if (ndc != null) {
                    gelfMessage.addField("loggerNdc", ndc);
                }
            }

            return gelfMessage;
        }
    }

    private static String extractStacktrace(ThrowableInformation throwableInformation) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        Throwable t = throwableInformation.getThrowable();
        if (t != null) {
            t.printStackTrace(pw);
            return sw.toString();
        } else {
            return null;
        }
    }
}
