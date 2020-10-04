package com.nhn.miniwas.controller;


import com.nhn.miniwas.request.HttpRequest;
import com.nhn.miniwas.response.HttpResponse;

import java.text.ParseException;

public interface Controller {
    void service(HttpRequest httpRequest, HttpResponse httpResponse) throws ParseException;
}
