package com.nhn.miniwas.response;

public enum HttpStatusCode {
    // spec 3. Html 에러 파일 만들때 첫 3글자가 Http Code 이어야 한다.
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
