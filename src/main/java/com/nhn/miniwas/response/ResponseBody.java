package com.nhn.miniwas.response;

import java.io.DataOutputStream;
import java.io.IOException;

public class ResponseBody {
    private byte[] body;

    public ResponseBody(byte[] body) {
        this.body = body;
    }

    public void responseBody(DataOutputStream dos) {
        try {
            dos.write(body, 0, body.length);
            dos.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getBodyLength() {
        return String.valueOf(body.length);
    }
}
