package com.nhn.miniwas.controller;

import com.nhn.miniwas.request.HttpRequest;
import com.nhn.miniwas.response.HttpResponse;

public class HomeController extends AbstractController {
    @Override
    public void doGet(HttpRequest httpRequest, HttpResponse httpResponse) {
        httpResponse.addHeader(CONTENT_TYPE, HTML_CONTENT_TYPE);
        httpResponse.process(httpRequest.getPath());
    }
}
