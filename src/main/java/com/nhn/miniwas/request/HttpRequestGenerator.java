package com.nhn.miniwas.request;

import com.nhn.miniwas.util.HttpRequestUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

public class HttpRequestGenerator {
    public static HttpRequest createHttpRequest(InputStream in) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8));
        RequestLine requestLine = HttpRequestUtils.createRequestLine(bufferedReader);
        RequestHeader requestHeader = HttpRequestUtils.createRequestHeader(bufferedReader);
        RequestBody requestBody = HttpRequestUtils.createRequestBody(bufferedReader, requestHeader.getContentLength());
        return new HttpRequest(requestLine, requestHeader, requestBody);
    }
}
