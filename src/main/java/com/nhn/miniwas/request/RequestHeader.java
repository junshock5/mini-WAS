package com.nhn.miniwas.request;

import ch.qos.logback.classic.Logger;
import com.nhn.miniwas.Dispatcher;
import org.slf4j.LoggerFactory;

import java.util.Map;

public class RequestHeader {
    private static final Logger logger = (Logger) LoggerFactory.getLogger(RequestHeader.class);

    private Map<String, String> headers;

    public RequestHeader(Map<String, String> headers) {
        this.headers = headers;
    }

    public int getContentLength() {
        if (headers.get("Content-Length") == null) {
            return 0;
        }
        int length = 0;
        try {
            length = Integer.parseInt(headers.get("Content-Length"));
        } catch (NumberFormatException e) {
            logger.error("getContentLength parseInt {} ", e);
        }
        return length;
    }

    public String getHeaderValue(String key) {
        return headers.get(key);
    }
}
