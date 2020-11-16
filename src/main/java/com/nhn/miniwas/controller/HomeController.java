package com.nhn.miniwas.controller;

import com.nhn.miniwas.request.HttpRequest;
import com.nhn.miniwas.response.HttpResponse;
import com.nhn.miniwas.vo.ContentType;

public class HomeController extends AbstractController {
    @Override
    public void doGet(HttpRequest httpRequest, HttpResponse httpResponse) {
        httpResponse.addHeader(ContentType.CONTENT_TYPE, ContentType.HTML_CONTENT_TYPE);
        httpResponse.process(httpRequest.getPath());
    }

    @Override
    public void doPost(HttpRequest httpRequest, HttpResponse httpResponse) {

    }
}
