package com.nhn.miniwas.controller;


import com.nhn.miniwas.request.HttpRequest;
import com.nhn.miniwas.response.HttpResponse;

public abstract class AbstractController implements Controller {
    @Override
    public void service(HttpRequest httpRequest, HttpResponse httpResponse) {
        if ("GET" .equals(httpRequest.getMethod())) {
            doGet(httpRequest, httpResponse);
        }

        if ("POST" .equals(httpRequest.getMethod())) {
            doPost(httpRequest, httpResponse);
        }
    }

    abstract void doGet(HttpRequest httpRequest, HttpResponse httpResponse);

    abstract void doPost(HttpRequest httpRequest, HttpResponse httpResponse);
}
