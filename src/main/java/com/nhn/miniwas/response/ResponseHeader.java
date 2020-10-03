package com.nhn.miniwas.response;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ResponseHeader {
    private Map<String, String> headers = new HashMap<>();

    public void addWriteHeader(DataOutputStream dos) throws IOException {
        for (String key : headers.keySet()) {
            dos.writeBytes(key + ": " + headers.get(key) + "\r\n");
        }
        dos.writeBytes("\r\n");
    }

    public void addHeader(String header, String value) {
        headers.put(header, value);
    }
}
