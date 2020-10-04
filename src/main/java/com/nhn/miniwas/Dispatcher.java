package com.nhn.miniwas;

import com.nhn.miniwas.request.HttpRequest;
import com.nhn.miniwas.response.HttpResponse;
import ch.qos.logback.classic.Logger;
import org.slf4j.LoggerFactory;

public class Dispatcher {
    private static final Logger logger = (Logger) LoggerFactory.getLogger(Dispatcher.class);

    private HttpRequest httpRequest;
    private HttpResponse httpResponse;

    public Dispatcher(HttpRequest httpRequest, HttpResponse httpResponse) {
        this.httpRequest = httpRequest;
        this.httpResponse = httpResponse;
    }

    public void dispatch() {
        logger.debug("requestLine : {}", httpRequest.getPath());
        HandlerMapping.findController(httpRequest.getPath()).service(httpRequest, httpResponse);
    }
}
