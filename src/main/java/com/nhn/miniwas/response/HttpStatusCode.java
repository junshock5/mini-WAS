package com.nhn.miniwas.response;

public enum HttpStatusCode {
    OK("200 OK"),
    FOUND("302 Found"),
    UNAUTHORIZED("401 Unauthorized"),
    FORBIDDEN("403 Forbidden"),
    NOT_FOUND("404 Not Found"),
    ERROR("500 Internal Server Error");

    private String httpStatusNumber;

    HttpStatusCode(String httpStatusNumber) {
        this.httpStatusNumber = httpStatusNumber;
    }

    public String getHttpStatusNumber() {
        return httpStatusNumber;
    }
}
