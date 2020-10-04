package com.nhn.miniwas.response;

import java.io.DataOutputStream;
import java.io.OutputStream;

public class HttpResponseFactory {
    public static HttpResponse createHttpResponse(OutputStream out) {
        return new HttpResponse(new DataOutputStream(out));
    }
}
