package com.nhn.miniwas.response;

import java.io.DataOutputStream;
import java.io.OutputStream;

public class HttpResponseGenerator {
    public static HttpResponse createHttpResponse(OutputStream out) {
        return new HttpResponse(new DataOutputStream(out));
    }
}
