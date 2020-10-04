package com.nhn.miniwas.controller;

import com.nhn.miniwas.request.HttpRequest;
import com.nhn.miniwas.response.HttpResponse;

import java.util.Date;

public class TimeController extends AbstractController {
    @Override
    public void doGet(HttpRequest httpRequest, HttpResponse httpResponse) {
        httpResponse.addHeader(CONTENT_TYPE, HTML_CONTENT_TYPE);

        // spec 7. 현재 시각을 출력 하는 구현체
        httpResponse.processBody("<h3>Path = " + httpRequest.getPath() + "</h3>" +
                "<h3>Now Time = " + new Date().toString() + "</h3>");
    }
}
