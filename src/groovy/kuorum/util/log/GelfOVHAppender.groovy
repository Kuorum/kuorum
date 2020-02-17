package kuorum.util.log

import org.apache.log4j.spi.LoggingEvent
import org.graylog2.GelfSenderResult
import org.graylog2.log.GelfAppender

class GelfOVHAppender extends GelfAppender{

    protected void append(LoggingEvent event) {
        GelfOVHMessage gelfMessage = GelfOVHMessageFactory.makeMessage(this.layout, event, this);
        if (this.getGelfSender() == null) {
            this.errorHandler.error("Could not send GELF message. Gelf Sender is not initialised and equals null");
        } else {
            GelfSenderResult gelfSenderResult = this.getGelfSender().sendMessage(gelfMessage);
            if (!GelfSenderResult.OK.equals(gelfSenderResult)) {
                this.errorHandler.error("Error during sending GELF message. Error code: " + gelfSenderResult.getCode() + ".", gelfSenderResult.getException(), 1);
            }
        }

    }
}
