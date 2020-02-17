package kuorum.util.log

import org.graylog2.GelfMessage
import org.json.simple.JSONValue
import org.slf4j.MDC

class GelfOVHMessage extends GelfMessage{

    public GelfOVHMessage(String shortMessage, String fullMessage, long timestamp, String level, String line, String file) {
       super(shortMessage,fullMessage,timestamp,level,line,file)
    }

    public String toJson() {
        Map<String, Object> map = new HashMap();
        map.put("version", this.getVersion());
        map.put("host", this.getHost());
        map.put("short_message", this.getShortMessage());
        map.put("full_message", this.getFullMessage());
//        map.put("timestamp", this.getTimestamp());
        map.put("facility", this.getFacility());
//        map.put("domain", MDC.get("domain")?:"");
//        map.put("userAlias", MDC.get("userAlias")?:"");
        if (MDC.copyOfContextMap){
            map.putAll(MDC.copyOfContextMap)
        }

        try {
            map.put("level", Long.parseLong(this.getLevel()));
        } catch (NumberFormatException var5) {
            map.put("level", 6L);
        }

        if (null != this.getFile()) {
            map.put("file", this.getFile());
        }

        if (null != this.getLine()) {
            try {
                map.put("line", Long.parseLong(this.getLine()));
            } catch (NumberFormatException var4) {
                map.put("line", -1L);
            }
        }

        Iterator i$ = this.additonalFields.entrySet().iterator();

        while(i$.hasNext()) {
            Map.Entry<String, Object> additionalField = (Map.Entry)i$.next();
            if (!"id".equals(additionalField.getKey())) {
//                map.put("_" + (String)additionalField.getKey(), additionalField.getValue());
                map.put((String)additionalField.getKey(), additionalField.getValue());
            }
        }

        return JSONValue.toJSONString(map);
    }
}
