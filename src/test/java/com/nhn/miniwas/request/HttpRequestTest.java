package com.nhn.miniwas.request;

import junit.framework.TestCase;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;


public class HttpRequestTest extends TestCase {
    private final String httpHeader = "GET /index.html HTTP/1.1\n" +
            "Host: localhost:8080\n" +
            "Connection: keep-alive\n" +
            "Accept: */*\n" +
            "\n";

    private HttpRequest httpRequest;

    @Before
    public void setUp() throws Exception {
        httpRequest = HttpRequestFactory.createHttpRequest(new ByteArrayInputStream(httpHeader.getBytes()));
    }

    @Test
    public void testGetHeader() throws IOException {
        assertThat(httpRequest.getHeader("Connection")).isEqualTo("keep-alive");
    }

    @Test
    public void testGetMethod() throws IOException {
        assertThat(httpRequest.getMethod()).isEqualTo("GET");
    }

    @Test
    public void testGetPath() throws IOException {
        assertThat(httpRequest.getPath()).isEqualTo("/index.html");
    }

    @Test
    public void testGetVersion() throws IOException {
        assertThat(httpRequest.getVersion()).isEqualTo("HTTP/1.1");
    }
}