package com.nhn.miniwas.util;

import com.google.common.base.Strings;
import com.google.common.collect.Maps;
import com.nhn.miniwas.request.RequestBody;
import com.nhn.miniwas.request.RequestHeader;
import com.nhn.miniwas.request.RequestLine;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class HttpRequestUtils {
    /**
     * @param queryString은 URL 이후에 전달되는 field1=value1&field2=value2 형식
     * @return
     */
    public static Map<String, String> parseQueryString(String queryString) {
        return parseValues(queryString, "&");
    }

    /**
     * @param 쿠키 값은 name1=value1; name2=value2 형식
     * @return
     */
    public static Map<String, String> parseCookies(String cookies) {
        return parseValues(cookies, "; ");
    }

    /**
     * @param 파싱  문자열
     * @param 구분자 ex) &,;
     * @return 구분자로 나뉜 문자 maps
     */
    private static Map<String, String> parseValues(String values, String separator) {
        if (Strings.isNullOrEmpty(values)) {
            return Maps.newHashMap();
        }

        String[] tokens = values.split(separator);
        return Arrays.stream(tokens).map(t -> getKeyValue(t, "=")).filter(p -> p != null)
                .collect(Collectors.toMap(p -> p.getKey(), p -> p.getValue()));
    }

    /**
     * @param 파싱  문자열
     * @param 구분자 ex) &,;
     * @return 구분자로 나뉜 문자 map
     */
    static Pair getKeyValue(String keyValue, String regex) {
        if (Strings.isNullOrEmpty(keyValue)) {
            return null;
        }

        String[] tokens = keyValue.split(regex);
        if (tokens.length != 2) {
            return null;
        }

        return new Pair(tokens[0], tokens[1]);
    }

    public static Pair parseHeader(String header) {
        return getKeyValue(header, ": ");
    }

    public static RequestHeader createRequestHeader(BufferedReader br) throws IOException {
        String line;
        Map<String, String> headers = new HashMap<>();
        while (!(line = br.readLine()).equals("")) {
            String[] header = line.split(": ");
            headers.put(header[0], header[1]);
        }
        return new RequestHeader(headers);
    }

    public static RequestBody createRequestBody(BufferedReader br, int contentLength) throws IOException {
        if (contentLength == 0) {
            return null;
        }
        String body = IOUtils.readData(br, contentLength);
        return new RequestBody(body);
    }

    public static RequestLine createRequestLine(BufferedReader br) throws IOException {
        String[] requestLine = br.readLine().split(" ");
        return new RequestLine(requestLine[0], requestLine[1], requestLine[2]);
    }
}
