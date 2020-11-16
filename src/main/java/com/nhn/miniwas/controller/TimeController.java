package com.nhn.miniwas.controller;

import com.nhn.miniwas.request.HttpRequest;
import com.nhn.miniwas.response.HttpResponse;
import com.nhn.miniwas.util.DateUtils;
import com.nhn.miniwas.vo.ContentType;

import java.text.ParseException;
import java.util.Date;

public class TimeController extends AbstractController {
    @Override
    public void doGet(HttpRequest httpRequest, HttpResponse httpResponse) throws ParseException {
        httpResponse.addHeader(ContentType.CONTENT_TYPE, ContentType.HTML_CONTENT_TYPE);

        // spec 7. 현재 시각을 출력 하는 구현체
        httpResponse.processBody("<h3>Path = " + httpRequest.getPath() + "</h3>" +
                "<h3>Now Time = " + DateUtils.getNowTimeToyyyyMMddHHmm(new Date()) + "</h3>");
    }

    @Override
    public void doPost(HttpRequest httpRequest, HttpResponse httpResponse) {

    }
}
