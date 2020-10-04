package com.nhn.miniwas.response;

import java.io.DataOutputStream;
import java.io.IOException;

public class StatusLine {
    private String version;
    private HttpStatusCode statusCode;

    public StatusLine(String version, HttpStatusCode statusCode) {
        this.version = version;
        this.statusCode = statusCode;
    }

    public void addWriteStatusLine(DataOutputStream dos) throws IOException {
        dos.writeBytes(version + " " + statusCode.getHttpStatusNumber() + "\r\n");
    }
}
