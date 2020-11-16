package com.nhn.miniwas.util;

import org.junit.Test;

import java.util.Map;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.*;

public class HttpRequestUtilsTest {

    @Test
    public void parseQueryString() {
        String queryString = "userId=topojs8&password";
        Map<String, String> parameters = HttpRequestUtils.parseQueryString(queryString);
        assertThat(parameters.get("userId"), is("topojs8"));
        assertThat(parameters.get("password"), is(nullValue()));
    }

    @Test
    public void parseQueryString_null() {
        Map<String, String> parameters = HttpRequestUtils.parseQueryString(null);
        assertThat(parameters.isEmpty(), is(true));

        parameters = HttpRequestUtils.parseQueryString("");
        assertThat(parameters.isEmpty(), is(true));

        parameters = HttpRequestUtils.parseQueryString(" ");
        assertThat(parameters.isEmpty(), is(true));
    }

    @Test
    public void getRootSeparatorCount() {
        int separatorCount = HttpRequestUtils.getRootSeparatorCount("C:\\Users\\topojs8\\Downloads\\mini-was\\src\\test\\java\\com\\nhn\\miniwas\\util\\HttpRequestUtilsTest.java");
        assertThat(separatorCount, is(12));
    }

    @Test
    public void getKeyValue() {
        Pair pair = HttpRequestUtils.getKeyValue("userId=topojs8", "=");
        assertThat(pair, is(new Pair("userId", "topojs8")));
    }

    @Test
    public void getKeyValue_invalid() throws Exception {
        Pair pair = HttpRequestUtils.getKeyValue("userId", "=");
        assertThat(pair, is(nullValue()));
    }

    @Test
    public void parseHeader() {
        String header = "Content-Length: 59";
        Pair pair = HttpRequestUtils.parseHeader(header);
        assertThat(pair, is(new Pair("Content-Length", "59")));
    }
}