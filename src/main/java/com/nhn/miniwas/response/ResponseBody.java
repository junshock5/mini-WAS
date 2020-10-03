package com.nhn.miniwas.response;

import java.io.DataOutputStream;
import java.io.IOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ResponseBody {
    private static final Logger logger = (Logger) LoggerFactory.getLogger(ResponseBody.class);
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
